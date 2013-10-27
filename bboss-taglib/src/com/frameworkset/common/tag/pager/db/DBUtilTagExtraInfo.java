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

package com.frameworkset.common.tag.pager.db;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * <p>Title: DBUtilTagExtraInfo.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-3-11 下午03:21:01
 * @author biaoping.yin
 * @version 1.0
 */
public class DBUtilTagExtraInfo extends TagExtraInfo
{
    public VariableInfo[] getVariableInfo(TagData tagData) {
        String result = tagData.getAttributeString("result");
        if(result == null || result.equals(""))
            result = "dbutil_result";
//      String id = (String)tagData.getAttribute("id");
        //System.out.println("id：" + id);
//      String export = tagData.getAttributeString("export");
//      if (export != null) {
//          try {
//              PagerTagExport pagerTagExport =
//                  TagExportParser.parsePagerTagExport(export);
//              int len = 0;
//              if (pagerTagExport.getPageOffset() != null)
//                  len++;
//              if (pagerTagExport.getPageNumber() != null)
//                  len++;
//
//              VariableInfo[] varinfo = new VariableInfo[len];
//              int i = 0;
//
//              String name;
//              if ((name = pagerTagExport.getPageOffset()) != null)
//                  varinfo[i++] = new VariableInfo(name,
//                          java.lang.Integer.class.getName(),
//                          true, VariableInfo.NESTED);
//              if ((name = pagerTagExport.getPageNumber()) != null)
//                  varinfo[i++] = new VariableInfo(name,
//                          java.lang.Integer.class.getName(),
//                          true, VariableInfo.NESTED);
//
//              return varinfo;
//          } catch (ParseException ex)  {
//              return new VariableInfo[0];
//          }
//      }

            
//      if(id == null || id.trim().equals(""))
            return new VariableInfo[] {new VariableInfo(result,
                    Object.class.getName(),
                    true, VariableInfo.AT_END)};
//      else
//          return new VariableInfo[] {new VariableInfo("pager_info_" + id,
//                  PagerInfo.class.getName(),
//                  true, VariableInfo.NESTED)};
    }

    public boolean isValid(TagData tagData) {
//      String export = tagData.getAttributeString("export");
//      if (export != null) {
//          try {
//              TagExportParser.parsePagerTagExport(export);
//          } catch (ParseException ex)  {
//              return false;
//          }
//      }
        return true;
    }
}
