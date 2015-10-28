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

public class TestChar {
    public static void testChar()
    {
        DBUtil db = new DBUtil();
        try {
            
//            db.executeSelect("hb"," select G103.X0313_1 G103_X0313_1, qygm.name qygm_name  from v101 " +
//                            "left join G103 on V101.CODE = G103.CODE " +
//                            "left join qygm on V101.X0110 = qygm.CODE " +
//                            "where 1 = 1 " +
//                            "and v101.x0110 in ('1')");
            
//            
            db.executeSelect("hb","select X0313_1,x0313_2,x0313_3 from g103",100,10);

            System.out.println(db.size());
            System.out.println(db.getString(0, 0));
            System.out.println(db.getString(0, 1));
            System.out.println(db.getString(0, 2));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        testChar();
    }

}
