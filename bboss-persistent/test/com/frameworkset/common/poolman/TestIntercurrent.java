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
package com.frameworkset.common.poolman;


/**
 * 测试poolman数据库链接池并发插入操作
 * @author biaoping.yin
 * created on 2005-3-30
 * version 1.0 
 */
public class TestIntercurrent {
    
    public static void main(String[] args)
    {
        //try {
        TestThread testThread[] = new TestThread[] {new TestThread(),new TestThread(),new TestThread()};
        testThread[0].start();
        testThread[1].start();
        testThread[2].start();
        
            
//            DBUtil dbutil = new DBUtil();
//            try {
//                for(int i = 0 ; i < 5; i ++)
//                {
//                    dbutil.executeInsert("insert  into  oa_meetingpromptsound(soundCode,soundName,soundFileName) values('aaa','bb','dd')");
//                }
                
                //Class.forName("oracle.jdbc.driver.OracleDriver");
                //Connection con = DriverManager.getConnection("jdbc:oracle:thin:@202.197.40.150:1521:test","system","manager");
//            Connection con = DBUtil.getConection();//DriverManager.getConnection("jdbc:oracle:thin:@202.197.40.131:1521:ora8isol","oa","oa");
//            System.out.println("con1:"+con);
//            
//            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);            
//            //stmt.setFetchSize(maxsiz);
//            //stmt.setMaxRows(2);
//            ResultSet rs = stmt.executeQuery("select table_name,table_id_name,table_id_increment,table_id_value from tableinfo");
//            
//            //rs.beforeFirst();
//            int i = 0;
//           // rs.absolute(12);
//            while(rs.next())
//            {
//                i ++;
//                System.out.println("hasNext:" + true);
//            }
//            System.out.println("i:" + i);
//            rs.absolute(-4);
//            while(rs.next())
//            {
//                i ++;
//                System.out.println("hasNext:" + true);
//            }
//            System.out.println("i:" + i);
//            rs.close();
//            
//            stmt.close();
//            con.close();
////            System.out.println(- i);
//            //while(rs.)
//            //rs.absolute(2);
//            //System.out.println("id:" + rs.next());
//            //System.out.println("hasNext:" + rs.next());
//        }
////        catch (ClassNotFoundException e) {
////            
////           // e.printStackTrace();
////        }
//        catch (SQLException e) {
//            
//            e.printStackTrace();
//        }
//            } catch (SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        
    }

}
class TestThread extends Thread
{
    public void run()
    {
        DBUtil dbutil = new DBUtil();
        try {
            for(int i = 0 ; i < 5; i ++)
            {
                System.out.println(this + "||result:" + dbutil.executeInsert("insert  into  oa_meetingpromptsound(soundCode,soundName,soundFileName) values('aaa','bb','dd')"));
                
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}