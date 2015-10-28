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
package com.frameworkset.common.poolman.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import com.frameworkset.common.poolman.StatementInfo;
import com.frameworkset.common.poolman.sql.ParserException;
import com.frameworkset.common.poolman.sql.PrimaryKey;
import com.frameworkset.common.poolman.sql.PrimaryKeyCacheManager;
import com.frameworkset.common.poolman.sql.Sequence;
import com.frameworkset.common.poolman.sql.UpdateSQL;
import com.frameworkset.util.RegexUtil;
import com.frameworkset.util.SimpleStringUtil;

/**
 * 解析sql语句程序
 * 
 * @author biaoping.yin created on 2005-3-31 version 1.0
 */
public class StatementParser implements Serializable
{
	 private static Logger log = Logger.getLogger(StatementParser.class);

	/**
	 * insert正则表达式解析insert语句 如果insert语句与正则表达式匹配，本方法根据表达式对语句的分组
	 * 将insert语句拆分为6部分,并且将这几部分存放到字符串数组返回 分别为： 1.insert关键字 2.into关键字 3.表名称 4.表字段组
	 * 5.values关键字 6.插入字段值组
	 * 
	 * 比如insert语句： Insert into oa_meetingpromptsound ( soundCode , soundName ,
	 * soundFileName ) values ( '。.尹标平','bb','d()d' ) 将被分解为以下部分： 1.Insert 2.into
	 * 3.oa_meetingpromptsound 4.( soundCode , soundName , soundFileName )
	 * 5.values 6.( '。.尹标平','bb','d()d' )
	 */
	public static String[] parserInsert(String insert)
	{
		/**
		 * 定义insert语句的正则表达式 该表达式将insert语句拆分为6部分,分别为： 1.insert关键字 2.into关键字 3.表名称
		 * 4.表字段组 5.values关键字 6.插入字段值组 比如insert语句： Insert into
		 * oa_meetingpromptsound ( soundCode , soundName , soundFileName )
		 * values ( '。.尹标平','bb','d()d' ) 将被分解为以下部分： 1.Insert 2.into
		 * 3.oa_meetingpromptsound 4.( soundCode , soundName , soundFileName )
		 * 5.values 6.( '。.尹标平','bb','d()d' )
		 */
		String patternStr = "\\s*(insert)\\s+" + // 解析insert关键词
				"(into)\\s+" + // 解析into关键词
				"([^\\(^\\s]+)\\s*" + // 解析表名称
				"(\\([^\\)]+\\))\\s*" + // 解析表字段
				"(values)\\s*" + // 解析value关键词
				"(\\(.*(.*\n*)*.*)"; // 解析字段值

		/**
		 * 编译正则表达式patternStr，并用该表达式与传入的sql语句进行模式匹配,
		 * 如果匹配正确，则从匹配对象中提取出以上定义好的6部分，存放到数组中并返回 该数组
		 */

		PatternCompiler compiler = new Perl5Compiler();
		Pattern pattern = null;
		try
		{
			pattern = compiler.compile(patternStr,
					Perl5Compiler.CASE_INSENSITIVE_MASK);
		}
		catch (MalformedPatternException e)
		{
			e.printStackTrace();

			return null;
		}
		PatternMatcher matcher = new Perl5Matcher();
		MatchResult result = null;
		String[] tokens = null;
		boolean match = matcher.matches(insert, pattern);

		if (match)
		{
			result = matcher.getMatch();
			tokens = new String[6];
			for (int i = 0; i < 6; i++)
			{
				tokens[i] = result.group(i + 1).trim();
			}
		}

		return tokens;
	}

	public static String[] parseField(String fieldStr)
	{
        
        
        // ( soundCode , soundName , soundFileName )
		//fieldStr = fieldStr.trim();
		// String regx = "\\([\\s*([^\\,]+)\\,?]+\\)";
		String regx = "([^\\,^\\(^\\)]+)\\s*\\,?\\s*";
        return RegexUtil.containWithPatternMatcherInput(fieldStr,regx);
//		PatternCompiler compiler = new Perl5Compiler();
//		Pattern pattern = null;
//		try
//		{
//			pattern = compiler.compile(regx,
//					Perl5Compiler.CASE_INSENSITIVE_MASK);
//		}
//		catch (MalformedPatternException e)
//		{
//			e.printStackTrace();
//			return null;
//		}
//		PatternMatcher matcher = new Perl5Matcher();
//		MatchResult result = null;
//		String[] tokens = null;
//		if (matcher.matches(fieldStr, pattern))
//		{
//			result = matcher.getMatch();
//
//			tokens = new String[result.groups()];
//			for (int i = 0; i < result.groups(); i++)
//			{
//				tokens[i] = result.group(i + 1).trim();
//			}
//		}
//		return tokens;
	}

	public static String[] parserValues(String values)
	{
		
		String patternStr = "([^\\,]*)[\\,]?"; // 解析字段值
		
		String patternStr1 = "('?[^\\,]*'?)[\\,]?"; // 解析字段值

		/**
		 * 编译正则表达式patternStr，并用该表达式与传入的sql语句进行模式匹配,
		 * 如果匹配正确，则从匹配对象中提取出以上定义好的6部分，存放到数组中并返回 该数组
		 */
		String[] ret = RegexUtil.containWithPatternMatcherInput(values,patternStr1);

		PatternCompiler compiler = new Perl5Compiler();
		Pattern pattern = null;
		try
		{
			pattern = compiler.compile(patternStr,
					Perl5Compiler.CASE_INSENSITIVE_MASK);
		}
		catch (MalformedPatternException e)
		{
			e.printStackTrace();

			return null;
		}
		PatternMatcher matcher = new Perl5Matcher();
		MatchResult result = null;
		String[] tokens = null;
		boolean match = matcher.matches(values.trim(), pattern);
		System.out.println(match);
		if (match)
		{
			result = matcher.getMatch();
			tokens = new String[6];
			for (int i = 0; i < 6; i++)
			{
				tokens[i] = result.group(i + 1).trim();
				System.out.println(tokens[i]);
			}
		}

		return tokens;
	}
	
	class ParserValues
	{
		Stack operations = new Stack();
	}
	public static void main(String[] args)
	{
		
		String insert = "insert into td_cms_channel(NAME, DISPLAY_NAME, PARENT_ID,CHNL_PATH, CREATEUSER, CREATETIME, ORDER_NO,SITE_ID, STATUS, OUTLINE_TPL_ID, DETAIL_TPL_ID,CHNL_OUTLINE_DYNAMIC, DOC_DYNAMIC,CHNL_OUTLINE_PROTECT, DOC_PROTECT,WORKFLOW,PARENT_WORKFLOW) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		String insert = "insert into TD_CMS_DOCUMENT(TITLE, SUBTITLE, AUTHOR,DOCKIND,DOCOUTPUTID,CONTENT,STATUS,  KEYWORDS,DOCABSTRACT,DOCTYPE,DOCLEVEL,DOCWTIME,TITLECOLOR, PUBLISHTIME,DOCFLAG,DOCPUBURL,DOCUMENTPRIOR,CREATEUSER,   CREATETIME,LASTVISITTIME,PARENTDOCUMENT_ID, DOCSOURCE_ID,DETAILTEMPLATE_ID,CHANNEL_ID,USER_ID,ISLOCK,LINKTARGET,LINKFILE)   values('eee','eee','eee', 'null','null','', 0,'eee','eee',0, 0,TO_DATE('29-12-2006 10:54:49', 'DD-MM-YYYY HH24:MI:SS'),'#000000',TO_DATE('29-12-2006 10:54:49', 'DD-MM-YYYY HH24:MI:SS'),0, 'null',100,0,TO_DATE('29-12-2006 10:54:49', 'DD-MM-YYYY HH24:MI:SS'), TO_DATE('29-12-2006 10:54:49', 'DD-MM-YYYY HH24:MI:SS'),0,2,1, 2,1,0,'null','null')";
		parserInsert(insert);
//		String update = "update table1 set name='aaa' where id='1'";
//		parserUpdateSQL(update);
//		String patternStr = "(insert)\\s+" + // 解析insert关键词
//				"(into)\\s+" + // 解析into关键词
//				"([^\\(]+)\\s*" + // 解析表名称
//				"(\\([^\\)]+\\))\\s+" + // 解析表字段
//				"(values)\\s*" + // 解析value关键词
//				"(\\(.+)"; // 解析字段值
//
//		// parseField("( soundCode , soundName , soundFileName )");\
//		/**
//		 * 'XXXXX',
//		 * ',XXXXXXX,
//		 * XXXXXXXX,'
//		 * ','
//		 * ,
//		 * ',XXXXX'
//		 */
//		String sql = "'姜儒振 同志在 2005-05-12 批示：\r\n"
//				+ "据悉，长沙市政府拟改扩建阜埠路，这是对两所高,校8万师生所做的一件大好事。该报告所提建议，请长沙市政府认真研究，妥善处理。\r\n"
//				+ "请云昭省长批示。',to_date('2005-04-23 11:04:44','yyyy-mm-dd hh24:mi:ss'),2,1558";
////		for (int i = 0; i < 1; i++)
////			parserValues(sql);
//        
//        String fields = "( name , id , desc )";
//        String ret[] = parseField(fields);
//        String sqls[] = parserInsert("insert into TD_CMS_DOCUMENT(TITLE, SUBTITLE, AUTHOR,DOCKIND,DOCOUTPUTID,CONTENT,STATUS,  KEYWORDS,DOCABSTRACT,DOCTYPE,DOCLEVEL,DOCWTIME,TITLECOLOR, PUBLISHTIME,DOCFLAG,DOCPUBURL,DOCUMENTPRIOR,CREATEUSER,   CREATETIME,LASTVISITTIME,PARENTDOCUMENT_ID, DOCSOURCE_ID,DETAILTEMPLATE_ID,CHANNEL_ID,USER_ID,ISLOCK,LINKTARGET,LINKFILE)   values('eee','eee','eee', 'null','null','', 0,'eee','eee',0, 0,TO_DATE('29-12-2006 10:54:49', 'DD-MM-YYYY HH24:MI:SS'),'#000000',TO_DATE('29-12-2006 10:54:49', 'DD-MM-YYYY HH24:MI:SS'),0, 'null',100,0,TO_DATE('29-12-2006 10:54:49', 'DD-MM-YYYY HH24:MI:SS'), TO_DATE('29-12-2006 10:54:49', 'DD-MM-YYYY HH24:MI:SS'),0,2,1, 2,1,0,'null','null')");
//        
//        System.out.println(ret.length);
//        System.out.println(sqls.length);
//		// System.out.println("dbName:" +
//		// SQLManager.getInstance().getDefaultDBName());
//		// Object[] temp =
//		// PoolManStatement.refactorInsertStatement(sql,SQLManager.getInstance().getDefaultDBName());
//		// System.out.println(temp[0]);
//		// System.out.println(temp[1]);
//		// System.out.println(temp[2]);
//		// System.out.println(temp[3]);
//		//        
//		// parserInsert(sql);
	}
	
//	/**
//	 * 分析updatesql语句中包含的表名，关键字，和更新部分：table name
//	 * 例如：update table1 set name='aaa' where id='1';
//	 * @param updateSQL
//	 * @return String[4] {update,table1,set,values and where clause}
//	 */
//	public static String[] parserUpdateSQL(String updateSQL)
//	{
//		String regex = "\\s*(update)\\s+([^\\s]+)\\s+(set)\\s+(.+)";
//		PatternCompiler compiler = new Perl5Compiler();
//		Pattern pattern = null;
//		try
//		{
//			pattern = compiler.compile(regex,
//					Perl5Compiler.CASE_INSENSITIVE_MASK);
//		}
//		catch (MalformedPatternException e)
//		{
//			e.printStackTrace();
//
//			return null;
//		}
//		PatternMatcher matcher = new Perl5Matcher();
//		MatchResult result = null;
//		String[] tokens = null;
//		boolean match = matcher.matches(updateSQL, pattern);
//
//		if (match)
//		{
//			result = matcher.getMatch();
//			tokens = new String[4];
//			for (int i = 0; i < 4; i++)
//			{
//				tokens[i] = result.group(i + 1).trim();
//			}
//		}
//
//		return tokens;
//	}
//	public static Object[] refactorUpdateStatement(StatementInfo stateinfo) throws ParserException
//	{
//		return refactorUpdateStatement(stateinfo.getCon(),stateinfo.getSql(),stateinfo.getDbname());
//	}
//	 /**
//     * 拆分update语句，获取update的表和对应的主键字段信息
//     * @param updateStmt
//     * @param dbname
//     * @return
//     * @throws ParserException 
//     */
//    private static Object[] refactorUpdateStatement(Connection con,String updateStmt,String dbname) throws ParserException
//    {
//    	//{update,table1,set,values and where clause}
//    	String[] updateInfos = StatementParser.parserUpdateSQL(updateStmt);
//    	if(updateInfos == null)
//    		throw new ParserException("非法的更新语句：" + updateStmt);
//    		
//    	String tableName = updateInfos[1];
//    	PrimaryKey primaryKey = null;
//        try
//		{
//
//	        primaryKey = PrimaryKeyCacheManager.getInstance()
//														  .getPrimaryKeyCache(dbname)
//														  .getIDTable(con,tableName.toLowerCase());
//	        if(primaryKey == null)
//			{
//				//System.out.println("表'" + tableName + "'没有定义主键或定义了多主键！");
//	        	primaryKey = new PrimaryKey(dbname,updateInfos[1],null,null,con);
//	        	primaryKey.setHasTableinfo(false);
//				log.debug("未给表["+ tableName + "]定义主键信息,请检查tableinfo表中是否存在该表的记录：" + updateStmt);
//			}
//	        return new Object[] {primaryKey,updateInfos};
//
//		}
//        catch(Exception e)
//		{
//        	log.info("[db:"
//        	        + dbname
//        	        + "] tableinfo not initialed or initial failed"
//        	        + " or check the table "
//        	        + "[tableinfo has been created] or table[" + tableName
//        	        + "'s information has been inserted into tableinfo"
//        	        + "]\r\n please check log to view detail.");
//        	//e.printStackTrace();
//        	throw new ParserException("未给表["+ tableName + "]定义主键信息,请检查tableinfo表中是否存在该表的记录：" + updateStmt);
//        	
//
//			
//		}
//		
//    	
//    }
    
    /**
     * added by biaoping.yin on 2005.03.29
     * 重构insert语句,添加主键信息，首先获取主键值
     * @param insertStmt 数据库插入语句
     * @param dbname 数据库名称
     * @return ret
     * ret[0]:存放insert语句
     * ret[1]:存放新的主键值,如果不是插入语句则为空
     * ret[2]:更新表tableinfo中插入表对应主键值语句,如果不是插入语句则为空
     * ret[3]:PrimaryKey对象,如果不是插入语句则为空
     * ret[4]:标识主键是自动产生还是用户指定,0-标识自动
     * 		  1-标识用户指定
     *        如果不是插入语句则为空
     * ret[6]:field字段数组；
     * @throws SQLException 
     * 
     */
    public static Object[] refactorInsertStatement(Connection con,String insertStmt,String dbname) throws SQLException
    {
    	return refactorInsertStatement(con,insertStmt,dbname,false);
        
    }
    
    public static Object[] refactorInsertStatement(StatementInfo stmtInfo) throws SQLException
    {
    	return refactorInsertStatement(stmtInfo.getCon(),stmtInfo.getSql(),stmtInfo.getDbname(), stmtInfo.isPrepared());
    }

    /**
     * added by biaoping.yin on 2005.03.29
     * 重构insert语句,添加主键信息，首先获取主键值
     * @param insertStmt 数据库插入语句
     * @param dbname 数据库名称
     * @return ret
     * ret[0]:存放insert语句
     * ret[1]:存放新的主键值,如果不是插入语句则为空
     * ret[2]:更新表tableinfo中插入表对应主键值语句,如果不是插入语句则为空
     * ret[3]:PrimaryKey对象,如果不是插入语句则为空
     * ret[4]:标识主键是自动产生还是用户指定,0-标识自动
     * 		  1-标识用户指定
     *        如果不是插入语句则为空
     * ret[6]:field字段数组；
     * @throws SQLException 
     * 
     */
    public static Object[] refactorInsertStatement(Connection con ,String insertStmt,String dbname,boolean prepared) throws SQLException
    {
        Object[] ret = new Object[6];

        //String tableName = this.getInsertTableName(insertStmt);
        //this.getInsertTableName(insertStmt);
        /**
         *  1.Insert
		 * 	2.into
		 * 	3.oa_meetingpromptsound
		 * 	4.( soundCode , soundName , soundFileName )
		 * 	5.values
		 * 	6.( '。.尹标平','bb','d()d' )
         */
        String tableInfos[] = StatementParser.parserInsert(insertStmt);
        if(tableInfos == null)
        {
            ret[0] = insertStmt;
            return ret;
        }
        String insert = tableInfos[0];
        String into   = tableInfos[1];
        String tableName = tableInfos[2];
        String fields = tableInfos[3];

        String values_key = tableInfos[4];
        String values = tableInfos[5];
        PrimaryKey primaryKey = null;
        try
		{

	        primaryKey = PrimaryKeyCacheManager.getInstance()
														  .getPrimaryKeyCache(dbname)
														  .getIDTable(con,tableName.toLowerCase());

		}
        catch(Exception e)//如果抛出sql异常，忽略，不影响整个事务的处理。
		{
        	log.info("[db:"
        	        + dbname
        	        + "] tableinfo not initialed or initial failed"
        	        + " or check the table "
        	        + "[tableinfo has been created] or table[" + tableName
        	        + "'s information has been inserted into tableinfo"
        	        + "]\r\n please check log to view detail.");
        	//e.printStackTrace();

        	ret[0] = insertStmt;
        	
			return ret;
		}
		if(primaryKey == null)
		{
			//System.out.println("表'" + tableName + "'没有定义主键或定义了多主键！");
			ret[0] = insertStmt;
			PrimaryKey t_p = new PrimaryKey(dbname,tableName,null,null,con);
	        ret[3] = t_p;
			return ret;
		}


        //定义表的主键名称变量
        String idName = primaryKey.getPrimaryKeyName();
        boolean contain = containKey(fields,idName);
        ret[4] = new Integer(0);
        ret[5] = StatementParser.parseField(fields);

        if(contain)
        {
        	String s_temp = values.trim();
        	s_temp = s_temp.substring(1,s_temp.length() -1);
        	String keyValue = getKeyValue(s_temp);
            ret[0] = insertStmt;
            ret[1] = keyValue;
            PrimaryKey t_p = new PrimaryKey(primaryKey.getDbname(),
            								primaryKey.getTableName(),
            								primaryKey.getPrimaryKeyName(),
            								keyValue,con);
            ret[3] = t_p;
            ret[4] = new Integer(1);            
            return ret;
        }
        //将表的主键字段属性插入到insert语句中
        StringBuffer temp = new StringBuffer(fields);
        temp.insert(1,idName + ",");
        fields = temp.toString();

        
        temp = new StringBuffer(values);
//      //定义主键值变量
        Sequence idValue = primaryKey.generateObjectKey(con);
        temp.insert(1,PrimaryKey.changeID(idValue.getPrimaryKey(),dbname,primaryKey.getType()) + ",");
        values = temp.toString();
        temp = new StringBuffer(insert);
        temp.append(" ")
			.append(into)
			.append(" ")
			.append(tableName)
			.append(fields)
			.append(" ")
			.append(values_key)
			.append(values);
        //started here
        //设置insert语句
        ret[0] = temp.toString();
        //设置新的主键值
        ret[1] = idValue.getPrimaryKey();
        //设置更新tableinfo的语句
        List datas = new ArrayList();
        datas.add(new Long(idValue.getSequence()));
        datas.add(tableName.toLowerCase());
        datas.add(new Long(idValue.getSequence()));
        
        UpdateSQL preparedUpdate = new UpdateSQL(dbname,tableName,UpdateSQL.TABLE_INFO_UPDATE,  datas);
//        ret[2] = "update tableinfo set table_id_value=" + idValue.getSequence() +" where table_name='"+ tableName.toLowerCase() + "' and table_id_value <" + idValue.getSequence()  ;
        ret[2] = preparedUpdate  ;

        //设置表主键信息封装类
        ret[3] = primaryKey;
//        ret[4] = new Integer(0);

        return ret;
    }
    
    private static String getKeyValue(String values)
	{
		String regx = "";
		return "";
	}
    
    /**
     * 判断是否已有主键
     * @param fields
     * @param idName
     * @return boolean
     */
    private static boolean containKey(String fields,String idName)
    {
        String temp = fields.trim();
        temp = temp.substring(1,temp.length() - 1);
        //System.out.println(temp);
        String field[] = SimpleStringUtil.split(temp,",");
        for(int i = 0; field != null && i < field.length; i ++)
        {
            if(field[i].trim().equalsIgnoreCase(idName.trim()))
                return true;
        }
        return false;
    }
    
     
}
