package org.frameworkset.spi.persistent;
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

import com.frameworkset.orm.adapter.DBClickhouse;
import com.frameworkset.util.SimpleStringUtil;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 *
 * @author biaoping.yin
 * @Date 2023/12/27
 */
public class TestClickHouseDB {
    @Test
    public void test() throws SQLException {
        String url = "jdbc:clickhouse://10.13.6.4:29000,10.13.6.7:29000,10.13.6.6:29000/visualops";
        DBClickhouse dbClickhouse = new DBClickhouse();
        List<String> hostsL = dbClickhouse.getBalanceUrls(url);

        System.out.println(SimpleStringUtil.object2json(hostsL));
        url = "jdbc:clickhouse://10.13.6.4:29000,10.13.6.7:29000,10.13.6.6:29000/";

        hostsL = dbClickhouse.getBalanceUrls(url);

        System.out.println(SimpleStringUtil.object2json(hostsL));
        url = "jdbc:clickhouse://10.13.6.4:29000,10.13.6.7:29000,10.13.6.6:29000";
        hostsL = dbClickhouse.getBalanceUrls(url);

        System.out.println(SimpleStringUtil.object2json(hostsL));
        Map<String,Object> p = dbClickhouse.getUrlParams(url);

        System.out.println(SimpleStringUtil.object2json(p));

        url = "jdbc:clickhouse://10.13.6.4:29000,10.13.6.7:29000,10.13.6.6:29000/visualops?b.balance=roundbin&b.enableBalance=true";
        p = dbClickhouse.getUrlParams(url);

        System.out.println(SimpleStringUtil.object2json(p));


    }

}
