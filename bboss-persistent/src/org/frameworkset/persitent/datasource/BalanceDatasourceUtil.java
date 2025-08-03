package org.frameworkset.persitent.datasource;
/**
 * Copyright 2025 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.frameworkset.common.poolman.util.DBConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

/**
 * @author biaoping.yin
 * @Date 2025/8/2
 */
public class BalanceDatasourceUtil {
    private static final Logger logger = LoggerFactory.getLogger(BalanceDatasourceUtil.class);

    public static String acceptsURL(String[] JDBC_CLICKHOUSE_PREFIXS,String url)  {
        for(String prefix:JDBC_CLICKHOUSE_PREFIXS) {
            if( url.startsWith(prefix))
                return prefix;
        }
        return null;
    }

    /**
     * jdbc:clickhouse://101.13.6.4:29000,101.13.6.7:29000,101.13.6.6:29000/visualops
     * jdbc:clickhouse:http://192.168.137.1:28123,192.168.137.1:28125,192.168.137.1:28126/visualops?ssl=false&connect_timeout=60000&socket_timeout=60000&query_timeout=60000&keep_alive_timeout=60000&clickhouse.jdbc.v1=false
     * @param url
     * @return
     * @throws SQLException
     */
    public static BalanceDatasourceConfig getBalanceDatasourceConfig(String[] JDBC_CLICKHOUSE_PREFIXS, String url, boolean supportFailLoad) throws InvalidValueException{
        
        //获取地址前缀，例如  jdbc:clickhouse:http://
        String prefix = acceptsURL(JDBC_CLICKHOUSE_PREFIXS,url);
        if (prefix == null) {
            return null;
        }
        try {
            BalanceDatasourceConfig balanceDatasourceConfig = new BalanceDatasourceConfig();
            int index = url.indexOf("/", prefix.length());
            //获取地址清单，例如  192.168.137.1:28123,192.168.137.1:28125,192.168.137.1:28126
            String hosts = null;
            String database2ndParams = null;
            if (index > 0) {
                hosts = url.substring(prefix.length(), index);
                //获取数据库和参数，例如  visualops?ssl=false&connect_timeout=60000&socket_timeout=60000&query_timeout=60000&keep_alive_timeout=60000&clickhouse.jdbc.v1=false
                database2ndParams = url.substring(index + 1);
            } else { //没有数据库和参数
                hosts = url.substring(prefix.length());
            }
            extractParameters(balanceDatasourceConfig, database2ndParams);

            String[] hostsA = hosts.split(",");


            List<String> hostsL = new ArrayList<>(hostsA.length);

            if (index < 0) {   ////没有数据库和参数
                for (int i = 0; i < hostsA.length; i++) {
                    StringBuilder tmp = new StringBuilder();
                    tmp.append(prefix).append(hostsA[i]);
                    if (supportFailLoad) { //如果支持故障容灾
                        for (int j = 0; j < hostsA.length; j++) {
                            if (j != i) {
                                tmp.append(",").append(hostsA[j]);
                            }
                        }
                    }
                    String nodeAdd = tmp.toString();
                    tmp.setLength(0);
                    if (logger.isInfoEnabled()) {
                        logger.info("add node:{}", nodeAdd);
                    }
                    hostsL.add(nodeAdd);

                }
            } else {
//            String database = url.substring(index);

                for (int i = 0; i < hostsA.length; i++) {
                    StringBuilder tmp = new StringBuilder();
                    tmp.append(prefix).append(hostsA[i]);
                    if (supportFailLoad) { //如果支持故障容灾
                        for (int j = 0; j < hostsA.length; j++) {
                            if (j != i) {
                                tmp.append(",").append(hostsA[j]);
                            }
                        }
                    }
                    tmp.append("/").append(balanceDatasourceConfig.getDatabase2ndParams());
                    String nodeAdd = tmp.toString();
                    tmp.setLength(0);
                    if (logger.isInfoEnabled()) {
                        logger.info("add node:{}", nodeAdd);
                    }
                    hostsL.add(nodeAdd);
                }

            }
            balanceDatasourceConfig.setNodes(hostsL);
            return balanceDatasourceConfig;
        }
        catch (Exception e){
            logger.warn("getBalanceDatasourceConfig of "+url + " failed:",e);
            return null;
        }
    }
    private static void ensure(boolean expr, String message) throws InvalidValueException {
        if (!expr) {
            throw new InvalidValueException(message);
        }
    }
    private static void extractParameters(BalanceDatasourceConfig balanceDatasourceConfig, String database2ndParams) throws InvalidValueException{
        int paramIndex = database2ndParams != null ?database2ndParams.indexOf("?"): -1;
        String database = null;
        String queryParameters = null;
        if(paramIndex >= 0){
            queryParameters = database2ndParams.substring(paramIndex+1);
            database = database2ndParams.substring(0,paramIndex);
            balanceDatasourceConfig.setDatabase(database);
            Map<String, String> parameters = new LinkedHashMap<>();
            StringTokenizer tokenizer = new StringTokenizer(queryParameters == null ? "" : queryParameters, "&");

            while (tokenizer.hasMoreTokens()) {
                String[] queryParameter = tokenizer.nextToken().split("=", 2);
                ensure(queryParameter.length == 2,
                        "JDBC URL Parameter '" + queryParameters + "' Error, Expected '='.");

                String name = queryParameter[0];
                String value = queryParameter[1];

                parameters.put(name,value);

            }
            String balance = parameters.remove("b.balance");
            balanceDatasourceConfig.setBalance(balance);
            String enableBalance = parameters.remove("b.enableBalance");
            if(enableBalance != null && enableBalance.equals("true")){
                balanceDatasourceConfig.setEnabaleBalance(true);
            }
            Iterator<Map.Entry<String, String>> iterator = parameters.entrySet().iterator();
            StringBuilder builder = new StringBuilder();
            while (iterator.hasNext()){
                Map.Entry<String, String> entry = iterator.next();
                if(builder.length() > 0) {
                    builder.append("&");
                }
                builder.append(entry.getKey()).append("=").append(entry.getValue());
            }
            if(builder.length() > 0) {
                balanceDatasourceConfig.setNormalParams(builder.toString());
            }
            if(database != null){
                if(balanceDatasourceConfig.getNormalParams() != null) {
                    balanceDatasourceConfig.setDatabase2ndParams(database + "?" + balanceDatasourceConfig.getNormalParams());
                }
                else{
                    balanceDatasourceConfig.setDatabase2ndParams(database );
                }
            }
            else{
                balanceDatasourceConfig.setDatabase2ndParams("?" + balanceDatasourceConfig.getNormalParams());
            }
            balanceDatasourceConfig.setParams(parameters);
        }
        else {
            balanceDatasourceConfig.setDatabase2ndParams(database2ndParams);
            balanceDatasourceConfig.setDatabase(database2ndParams);
        }
        
    }
    
    public static void main(String[] args) throws InvalidValueException {
        String[] JDBC_CLICKHOUSE_PREFIXS = {"jdbc:clickhouse://","jdbc:clickhouse:http://","jdbc:clickhouse:https://","jdbc:ch:http://","jdbc:ch:https://"};
        String addr = "jdbc:clickhouse://101.13.6.4:29000,101.13.6.7:29000,101.13.6.6:29000/visualops";
        BalanceDatasourceConfig balanceDatasourceConfig = getBalanceDatasourceConfig(JDBC_CLICKHOUSE_PREFIXS,addr,true);
        balanceDatasourceConfig = getBalanceDatasourceConfig(JDBC_CLICKHOUSE_PREFIXS,addr,false);
        addr = "jdbc:clickhouse:http://192.168.137.1:28123,192.168.137.1:28125,192.168.137.1:28126/visualops?ssl=false&connect_timeout=60000&socket_timeout=60000&query_timeout=60000&keep_alive_timeout=60000&clickhouse.jdbc.v1=false&b.balance="+ DBConf.BALANCE_RANDOM +"&b.enableBalance=true";
        balanceDatasourceConfig = getBalanceDatasourceConfig(JDBC_CLICKHOUSE_PREFIXS,addr,true);
        balanceDatasourceConfig = getBalanceDatasourceConfig(JDBC_CLICKHOUSE_PREFIXS,addr,false);
        addr = "jdbc:clickhouse://101.13.6.4:29000,101.13.6.7:29000,101.13.6.6:29000";
        balanceDatasourceConfig = getBalanceDatasourceConfig(JDBC_CLICKHOUSE_PREFIXS,addr,true);
        balanceDatasourceConfig = getBalanceDatasourceConfig(JDBC_CLICKHOUSE_PREFIXS,addr,false);
        addr = "jdbc:clickhouse:http://192.168.137.1:28123,192.168.137.1:28125,192.168.137.1:28126/?ssl=false&connect_timeout=60000&socket_timeout=60000&query_timeout=60000&keep_alive_timeout=60000&clickhouse.jdbc.v1=false";
        balanceDatasourceConfig = getBalanceDatasourceConfig(JDBC_CLICKHOUSE_PREFIXS,addr,true);
        balanceDatasourceConfig = getBalanceDatasourceConfig(JDBC_CLICKHOUSE_PREFIXS,addr,false);
    }
 
}
