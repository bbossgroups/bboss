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

import com.frameworkset.commons.dbcp.BasicDataSourceFactory;
import com.frameworkset.orm.adapter.DB;
import org.frameworkset.balance.RoundRobinList;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;
import java.util.logging.Logger;

/**
 * <p>Description: </p>
 * <p></p>
 *
 * @author biaoping.yin
 * @Date 2023/12/26
 */
public class RoundbinBalanceDatasource extends BalanceDatasource {
     
    private RoundRobinList<DBNode> roundRobinList;
     
    public RoundbinBalanceDatasource(BalanceDatasourceConfig balanceDatasourceConfig,String url, DB db, Properties properties)  {
        super(    balanceDatasourceConfig,url,   db,   properties);
    }
    @Override
    protected void buildAddress() throws Exception{
        List<DBNode> dbNodes = new ArrayList<>(nodes.size());
        DataSource currentDatasource = null;
        for(String node: nodes){
            dbNodes.add(new DBNode(node));
            currentDatasource = BasicDataSourceFactory.createDBCP2DataSource(node,properties);
            balanceDatasources.put(node,currentDatasource);
            if(this.currentDatasource == null){
                this.currentDatasource = currentDatasource;
            }
        }
        roundRobinList = new RoundRobinList<>(dbNodes);
    }
    @Override
    protected String getAnyUrl() throws SQLException {
        DBNode dbNode = roundRobinList.get(true);
        return dbNode.getNode();
    }
   
}
