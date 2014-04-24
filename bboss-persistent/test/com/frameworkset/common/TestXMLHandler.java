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

import org.junit.Test;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.RowHandlerException;
import com.frameworkset.common.poolman.handle.XMLRowHandler;
import com.frameworkset.orm.engine.model.SchemaType;

/**
 * 
 * <p>Title: TestXMLHandler.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2009-5-14 上午10:45:37
 * @author biaoping.yin
 * @version 1.0
 */
public class TestXMLHandler {
	@Test
    public  void testCustomXMLHandler()
    {
        PreparedDBUtil db = new PreparedDBUtil();
        try {
            db.preparedSelect("select * from tableinfo");
//            String results_1 = db.executePreparedForXML();
            String results_ = db.executePreparedForXML(new XMLRowHandler(){
//
//               
                public void handleRow(StringBuffer record, Record origine)  {
                   
                    record.append("    <row>\r\n");
   
                    try {
                        SchemaType schemaType = super.getSchemaType("TABLE_NAME"); 
                        record.append(super.buildNode("column", 
                                                      "TABLE_NAME", 
                                                      schemaType.getName(),
                                                      schemaType.getJavaType(), 
                                                      origine.getString("TABLE_NAME"),
                                                      "\r\n"));
                        schemaType = super.getSchemaType("table_id_name");
                        record.append(super.buildNode("column", 
                                                      "table_id_name", 
                                                      schemaType.getName(),
                                                      schemaType.getJavaType(),  
                                                      origine.getString("table_id_name"),
                                                      "\r\n"));
                        schemaType = super.getSchemaType("TABLE_ID_INCREMENT");
                        record.append(super.buildNode("column", 
                                                      "TABLE_ID_INCREMENT", 
                                                      schemaType.getName(),
                                                      schemaType.getJavaType(),  
                                                      origine.getString("TABLE_ID_INCREMENT"),
                                                      "\r\n"));
                        
                        schemaType = super.getSchemaType("TABLE_ID_GENERATOR");
                        record.append(super.buildNode("column", 
                                                      "TABLE_ID_GENERATOR", 
                                                      schemaType.getName(),
                                                      schemaType.getJavaType(), 
                                                      origine.getString("TABLE_ID_GENERATOR"),
                                                      "\r\n"));
                        schemaType = super.getSchemaType("TABLE_ID_TYPE");
                        record.append(super.buildNode("column", 
                                                      "TABLE_ID_TYPE", 
                                                      schemaType.getName(),
                                                      schemaType.getJavaType(),  
                                                      origine.getString("TABLE_ID_TYPE"),
                                                      "\r\n"));
                        
                    } catch (SQLException e) {
                        
                        throw new RowHandlerException(e);
                    }
                    record.append("    </record>\r\n");
                }

                
                public String getEncoding() {
                    // TODO Auto-generated method stub
                    return "UTF-8";
                }

               
                public String getRootName() {
                    // TODO Auto-generated method stub
                    return "tableinfo";
                }

               
                public String getVersion() {
                    // TODO Auto-generated method stub
                    return "2.0";
                }
                
            });
            
            System.out.println(results_);
//            System.out.println(results_1);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    @Test
    public void testDefualtXML()
    {
        PreparedDBUtil db = new PreparedDBUtil();
        try {
            db.preparedSelect("select * from tableinfo");
            String results_1 = db.executePreparedForXML();

            System.out.println(results_1);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
//        testCustomXMLHandler();
    }
}
