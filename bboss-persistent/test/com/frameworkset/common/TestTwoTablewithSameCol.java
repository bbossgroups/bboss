/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.frameworkset.common;

import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;

public class TestTwoTablewithSameCol {
    
    public static void testTwoTablewithSameCol()
    {
        DBUtil db = new DBUtil();
        try {
            db.executeSelect("select table_a.id,table_a.id1,table_b.id,table_b.id1 from table_a,table_b");
            System.out.println("table_a.id:" + db.getInt(0, 0));
            System.out.println("table_a.id1:" + db.getInt(0, 1));
            System.out.println("table_b.id:" + db.getInt(0, 2));
            System.out.println("table_b.id1:" + db.getInt(0, 3));
            System.out.println();
            System.out.println("table_a.id:" + db.getInt(0, "ID"));
            System.out.println("table_a.id1:" + db.getInt(0, "ID1"));
            System.out.println("table_b.id:" + db.getInt(0, "id#$_2"));
            System.out.println("table_b.id1:" + db.getInt(0, "id1#$_3"));
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
    
    public static void testTwoTablewithSameColAlias()
    {
        DBUtil db = new DBUtil();
        try {
            db.executeSelect("select table_a.id as id,table_a.id1  as id,table_b.id  as id,table_b.id1  as id from table_a,table_b");
            System.out.println("table_a.id:" + db.getInt(0, 0));
            System.out.println("table_a.id1:" + db.getInt(0, 1));
            System.out.println("table_b.id:" + db.getInt(0, 2));
            System.out.println("table_b.id1:" + db.getInt(0, 3));
            System.out.println();
            System.out.println("table_a.id:" + db.getInt(0, "ID"));
            System.out.println("table_a.id1:" + db.getInt(0, "ID#$_1"));
            System.out.println("table_b.id:" + db.getInt(0, "id#$_2"));
            System.out.println("table_b.id1:" + db.getInt(0, "id#$_3"));
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void test()
    {
        DBUtil db = new DBUtil();
        try {
            db.executeSelect("select * from tableinfo ",10,10);
        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public static void testBug()
    {
        DBUtil db = new DBUtil();
        try {
            db.executeSelect("hb",
                             "select V101.CODE," +
                             "               V101.shi," +
                             "               V101.NAME," +
                             "               snst.name" +
                             " from v101" +
                             " left join G103 on V101.CODE = G103.CODE" +
                             " left join snst on g103.x0316_1=snst.code" +
                             " left join qygm on V101.X0110=qygm.CODE" +
                             " where 1=1" +
                             "   and v101.x0110 in ('1')");
            for(int i=0;i<4;i++){
                try{
                        System.out.println(db.getValue(0, i));
                }catch(NullPointerException e){
                        e.printStackTrace();
                        System.out.println("----------读值错误：列序号："+i);
                }
        }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

       
    }
    
    
    public static void main(String[] args)
    {
        testTwoTablewithSameColAlias();
        testTwoTablewithSameCol();
        test();
//        testTwoTablewithSameCol();
//        testTwoTablewithSameColAlias();
//        test();
        testBug();
    }

}
