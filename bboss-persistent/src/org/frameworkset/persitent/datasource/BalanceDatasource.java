package org.frameworkset.persitent.datasource;
/**
 * Copyright 2023 bboss
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

import com.frameworkset.common.poolman.util.DatasourceUtil;
import com.frameworkset.commons.dbcp.BasicDataSourceFactory;
import com.frameworkset.orm.adapter.DB;
import org.frameworkset.balance.RoundRobinList;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * <p>Description: </p>
 * <p></p>
 *
 * @author biaoping.yin
 * @Date 2023/12/26
 */
public abstract class BalanceDatasource implements DataSource {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BalanceDatasource.class);
    protected Map<String,DataSource> balanceDatasources = new ConcurrentHashMap();
    protected String url;
    protected DB db;
    protected DataSource currentDatasource;
    protected Properties properties;
    protected boolean balance = false;
    protected List<String> nodes;
    private boolean closed;
    private Object closeLock = new Object();
    protected BalanceDatasourceConfig balanceDatasourceConfig;
     
    public BalanceDatasource(BalanceDatasourceConfig balanceDatasourceConfig,String url, DB db, Properties properties)  {
        this.url = url;
        this.db = db;
        this.properties = properties;
        this.balanceDatasourceConfig = balanceDatasourceConfig;
        this.nodes = balanceDatasourceConfig.getNodes();
    }
    protected abstract void buildAddress() throws Exception;
    public void init()throws Exception {
        if (nodes == null || nodes.size() == 1) {
            DataSource currentDatasource = BasicDataSourceFactory.createDBCP2DataSource(url, properties);
            this.currentDatasource = currentDatasource;
        } else {
            balance = true;
            buildAddress();

        }
    }
    
    public void close(){
        if(closed )
            return;
        synchronized (closeLock){
            if(closed )
                return;
            closed = true;
        }
        if(balanceDatasources.size() > 0){
            for(String node:nodes){
                DataSource dataSource = balanceDatasources.remove(node);
                if(dataSource != null){
                    DatasourceUtil.closeDS(dataSource);
                }
                
            }
//            nodes.clear();
        }
    }
       
    protected abstract String getAnyUrl() throws SQLException;

    private DataSource switchBalanceDatasource() throws SQLException{
        if(!balance)
            return currentDatasource;
        String node = getAnyUrl();
        if(logger.isDebugEnabled()){
            logger.debug("SwitchBalanceDatasource to {}",node);
        }
        return balanceDatasources.get(node);
        
    }
    @Override
    public Connection getConnection() throws SQLException {
        return switchBalanceDatasource().getConnection();
//        return currentDatasource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return switchBalanceDatasource().getConnection(username,password);
//        return currentDatasource.getConnection(username,password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return switchBalanceDatasource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return switchBalanceDatasource().isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return switchBalanceDatasource().getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        switchBalanceDatasource().setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        switchBalanceDatasource().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return switchBalanceDatasource().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        try {
            return switchBalanceDatasource().getParentLogger();
        } catch (SQLException e) {
            throw new SQLFeatureNotSupportedException(e);
        }
    }
}
