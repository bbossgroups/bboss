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
package com.frameworkset.sqlparams;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.RollbackException;

import org.junit.Test;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.SQLParams;
import com.frameworkset.common.poolman.SetSQLParamException;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.VariableHandler;

public class TestSQLParams {
	
	
		private static final StringBuilder listRepositorySql = new StringBuilder();
		private static final StringBuilder insertRepositorySql = new StringBuilder();
		private static final StringBuilder updateRepositorySql = new StringBuilder();
		private static final StringBuilder deleteRepositorySql = new StringBuilder();
		private static final StringBuilder getRepositoryByNameSql = new StringBuilder();

		private static final StringBuilder listRepositoryDirctorySql = new StringBuilder();
		private static final StringBuilder listRepositoryDirctoryByParentIdSql = new StringBuilder();
		private static final StringBuilder deleteRepositoryTreeItemSql = new StringBuilder();
		private static final StringBuilder deleteRepositoryTreeSql = new StringBuilder();
		private static final StringBuilder insertRepositoryDirctorySql = new StringBuilder();
		private static final StringBuilder insertJobMetaSql = new StringBuilder();
		private static final StringBuilder insertTransMetaSql = new StringBuilder();
		private static final StringBuilder updateJobMetaSql = new StringBuilder();
		private static final StringBuilder updateTransMetaSql = new StringBuilder();

		private static final StringBuilder deleteDatasoureMetaSql = new StringBuilder();
		private static final StringBuilder insertDatasoureMetaSql = new StringBuilder();
		private static final StringBuilder updateDatasoureMetaSql = new StringBuilder();
		private static final StringBuilder getDatasoureMetaSql = new StringBuilder();

		private static final String[] REPOSITORY_ORDER_COLUMNS = new String[] {
				"NAME", "OBJECT_TYPE", "MODIFIED_USER", "MODIFIED_DATE",
				"DESCRIPTION" };

		static {
			listRepositorySql.append("select * ").append(
					" from CIM_ETL_REPOSITORY ").append(" where 1=1 ").append(
					" and ").append("HOST_ID = #[HOST_ID]").append(" and ").append(
					"PLUGIN_ID = #[PLUGIN_ID]").append(" and ").append(
					" CATEGORY_ID = #[CATEGORY_ID]").append(" and APP = #[APP]");

			insertRepositorySql
					.append("insert into CIM_ETL_REPOSITORY")
					.append(
							"(ID,HOST_ID, APP, PLUGIN_ID,CATEGORY_ID,NAME,DESCRIPTION)")
					.append(
							" values (#[ID],#[HOST_ID], #[APP], #[PLUGIN_ID],#[CATEGORY_ID],#[NAME],#[DESCRIPTION])");

			updateRepositorySql.append("update CIM_ETL_REPOSITORY").append(
					" set NAME=#[NAME],DESCRIPTION=#[DESCRIPTION]").append(
					" where ID=#[ID]");

			deleteRepositorySql.append("delete from CIM_ETL_REPOSITORY ").append(
					" where ID=#[ID]");

			getRepositoryByNameSql.append(" select * from CIM_ETL_REPOSITORY")
					.append(" where 1=1").append(" and ").append(
							"HOST_ID = #[HOST_ID]").append(" and ").append(
							"PLUGIN_ID = #[PLUGIN_ID]").append(" and ").append(
							" CATEGORY_ID = #[CATEGORY_ID]").append(
							" and NAME = #[NAME]");

			listRepositoryDirctorySql
					.append(
							" select REPOSITORY_ID, ID, PARENT_ID, DIR_NAME, NAME, OBJECT_TYPE, DESCRIPTION, MODIFIED_USER, MODIFIED_DATE")
					.append(" from CIM_ETL_RESOURCE_ITEM ").append(" where 1=1")
					.append(" and OBJECT_TYPE=#[OBJECT_TYPE] ").append(//修改目录下同名job和转换同步冲突问题，增加OBJECT_TYPE为主键
							" and REPOSITORY_ID = #[REPOSITORY_ID]");

			listRepositoryDirctoryByParentIdSql
					.append(
							" select REPOSITORY_ID, ID, PARENT_ID, DIR_NAME, NAME, OBJECT_TYPE, DESCRIPTION, MODIFIED_USER, MODIFIED_DATE")
					.append(" from CIM_ETL_RESOURCE_ITEM ").append(" where 1=1")
					.append(" and OBJECT_TYPE=#[OBJECT_TYPE] ").append(//修改目录下同名job和转换同步冲突问题，增加OBJECT_TYPE为主键
							" and REPOSITORY_ID = #[REPOSITORY_ID]").append(
							" and PARENT_ID = #[PARENT_ID]");

			deleteRepositoryTreeItemSql
					.append("delete from  CIM_ETL_RESOURCE_ITEM").append(
							" where 1=1")
							.append(" and OBJECT_TYPE=#[OBJECT_TYPE] ")//修改目录下同名job和转换同步冲突问题，增加OBJECT_TYPE为主键
							.append(
							" and REPOSITORY_ID = #[REPOSITORY_ID]").append(
							" and PARENT_ID = #[PARENT_ID]").append(
							" and NAME = #[NAME]");

			deleteRepositoryTreeSql.append("delete from  CIM_ETL_RESOURCE_ITEM")
					.append(" where 1=1").append(
							" and REPOSITORY_ID = #[REPOSITORY_ID]");

			insertTransMetaSql
					.append(" insert into CIM_ETL_RESOURCE_ITEM")
					.append(
							"(REPOSITORY_ID,ID,PARENT_ID,DIR_NAME,NAME,OBJECT_TYPE,DESCRIPTION,STATUS,MODIFIED_USER,MODIFIED_DATE,DATASOURCE_NAME,LOG_TABLE,FIELD_NAMES,STEP_PERFORMANCE_LOG_TABLE,STEP_PERFORMANCE_FIELD_NAMES,FLOW_IMGE)")
					.append(
							" values(#[REPOSITORY_ID],#[ID],#[PARENT_ID],#[DIR_NAME],#[NAME],#[OBJECT_TYPE],#[DESCRIPTION],#[STATUS],#[MODIFIED_USER],#[MODIFIED_DATE],#[DATASOURCE_NAME],#[LOG_TABLE],#[FIELD_NAMES],#[STEP_PERFORMANCE_LOG_TABLE],#[STEP_PERFORMANCE_FIELD_NAMES],#[FLOW_IMGE])");

			insertJobMetaSql
					.append(" insert into CIM_ETL_RESOURCE_ITEM")
					.append(
							"(REPOSITORY_ID,ID,PARENT_ID,DIR_NAME,NAME,OBJECT_TYPE,DESCRIPTION,STATUS,MODIFIED_USER,MODIFIED_DATE,DATASOURCE_NAME,LOG_TABLE,FIELD_NAMES,FLOW_IMGE)")
					.append(
							" values(#[REPOSITORY_ID],#[ID],#[PARENT_ID],#[DIR_NAME],#[NAME],#[OBJECT_TYPE],#[DESCRIPTION],#[STATUS],#[MODIFIED_USER],#[MODIFIED_DATE],#[DATASOURCE_NAME],#[LOG_TABLE],#[FIELD_NAMES],#[FLOW_IMGE])");

			insertRepositoryDirctorySql
					.append(" insert into CIM_ETL_RESOURCE_ITEM")
					.append(
							"(REPOSITORY_ID,ID,PARENT_ID,DIR_NAME,NAME,OBJECT_TYPE,DESCRIPTION,STATUS,MODIFIED_USER,MODIFIED_DATE)")
					.append(
							" values(#[REPOSITORY_ID],#[ID],#[PARENT_ID],#[DIR_NAME],#[NAME],#[OBJECT_TYPE],#[DESCRIPTION],#[STATUS],#[MODIFIED_USER],#[MODIFIED_DATE])");

			updateTransMetaSql
					.append(" update CIM_ETL_RESOURCE_ITEM ")
					.append(" set ")
					// .append("REPOSITORY_ID=#[REPOSITORY_ID],ID=#[ID],PARENT_ID=#[PARENT_ID],DIR_NAME=#[DIR_NAME],NAME=#[NAME],")
					.append(
							"OBJECT_TYPE=#[OBJECT_TYPE],DESCRIPTION=#[DESCRIPTION],STATUS=#[STATUS],MODIFIED_USER=#[MODIFIED_USER],MODIFIED_DATE=#[MODIFIED_DATE],DATASOURCE_NAME=#[DATASOURCE_NAME],LOG_TABLE=#[LOG_TABLE],FIELD_NAMES=#[FIELD_NAMES],STEP_PERFORMANCE_LOG_TABLE=#[STEP_PERFORMANCE_LOG_TABLE],STEP_PERFORMANCE_FIELD_NAMES=#[STEP_PERFORMANCE_FIELD_NAMES],FLOW_IMGE=#[FLOW_IMGE]")
					.append(" where 1=1").append(
							" and REPOSITORY_ID = #[REPOSITORY_ID]").append(
							" and PARENT_ID = #[PARENT_ID]").append(
							" and OBJECT_TYPE = #[OBJECT_TYPE]").append(//修改目录下同名job和转换同步冲突问题，增加OBJECT_TYPE为主键
							" and NAME = #[NAME]");

			updateJobMetaSql
					.append(" update CIM_ETL_RESOURCE_ITEM ")
					.append(" set ")
					// .append("REPOSITORY_ID=#[REPOSITORY_ID],ID=#[ID],PARENT_ID=#[PARENT_ID],DIR_NAME=#[DIR_NAME],NAME=#[NAME],")
					.append(
							"OBJECT_TYPE=#[OBJECT_TYPE],DESCRIPTION=#[DESCRIPTION],STATUS=#[STATUS],MODIFIED_USER=#[MODIFIED_USER],MODIFIED_DATE=#[MODIFIED_DATE],DATASOURCE_NAME=#[DATASOURCE_NAME],LOG_TABLE=#[LOG_TABLE],FIELD_NAMES=#[FIELD_NAMES],FLOW_IMGE=#[FLOW_IMGE]")
					.append(" where 1=1").append(
							" and REPOSITORY_ID = #[REPOSITORY_ID]").append(
							" and PARENT_ID = #[PARENT_ID]").append(
							" and OBJECT_TYPE = #[OBJECT_TYPE]").append(//修改目录下同名job和转换同步冲突问题，在表CIM_ETL_RESOURCE_ITEM中增加OBJECT_TYPE为主键
							" and NAME = #[NAME]");

			deleteDatasoureMetaSql.append(" delete from CIM_ETL_DATASOURCE_META")
					.append(" where 1=1").append(
							" and REPOSITORY_ID = #[REPOSITORY_ID]");

			insertDatasoureMetaSql
					.append(" insert into CIM_ETL_DATASOURCE_META")
					.append(
							"(REPOSITORY_ID,NAME,DRIVER,JDBC_URL,USERNAME,PASSWORD,VALIDATION_QUERY)")
					.append(
							" values (#[REPOSITORY_ID],#[NAME],#[DRIVER],#[JDBC_URL],#[USERNAME],#[PASSWORD],#[VALIDATION_QUERY])");

			updateDatasoureMetaSql
					.append(" update CIM_ETL_DATASOURCE_META")
					.append(
							" set DRIVER=#[DRIVER],JDBC_URL=#[JDBC_URL],USERNAME=#[USERNAME],PASSWORD=#[PASSWORD],VALIDATION_QUERY=#[VALIDATION_QUERY]")
					.append(" where 1=1").append(
							" and REPOSITORY_ID=#[REPOSITORY_ID]").append(
							" and NAME=#[NAME]");

			getDatasoureMetaSql.append(" select * from CIM_ETL_DATASOURCE_META")
					.append(" where 1=1").append(
							" and REPOSITORY_ID=#[REPOSITORY_ID]").append(
							" and NAME=#[NAME]");

		}

		/** ******************************** 资源库 ************************** */
		/**
		 * 查询资源库列表
		 */
		public List<RepositoryInfo> listRepository(AppParams appParams) {
			// TODO Auto-generated method stub
			List<RepositoryInfo> repositorys = new ArrayList<RepositoryInfo>();

			PreparedDBUtil dbutil = new PreparedDBUtil();
			String sql = listRepositorySql.toString();

			try {
				// 查询参数
				SQLParams params = new SQLParams();
				params.addSQLParam("HOST_ID", appParams.getBusinessId(),
						SQLParams.STRING);
				params.addSQLParam("PLUGIN_ID", appParams.getPluginid(),
						SQLParams.STRING);
				params.addSQLParam("CATEGORY_ID", appParams.getCategory(),
						SQLParams.STRING);
				params.addSQLParam("APP", appParams.getApp(), SQLParams.STRING);

				dbutil
						.preparedSelect(params, "bspf",
								sql);
				// 执行查询
				dbutil.executePrepared();

				for (int i = 0; i < dbutil.size(); i++) {
					RepositoryInfo repositoryInfo = new RepositoryInfo();

					repositoryInfo.setId(dbutil.getString(i, "ID"));
					repositoryInfo.setName(dbutil.getString(i, "NAME"));
					repositoryInfo.setDescription(dbutil
							.getString(i, "DESCRIPTION"));

					repositorys.add(repositoryInfo);
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("后台数据库操作错误！", e);
			}

			return repositorys;
		}
		
		@Test 
		public void testParser()
		{
			 String pretoken = "#\\[";
		    String endtoken = "\\]";
		    String sql = "insert into tb_user(userId,userName,userPassword) " +
			"values(#[userId],#[userName],#[userPassword])";
			String[][] args = VariableHandler.parser2ndSubstitution(sql, pretoken,endtoken, "?");
			System.out.println(args);
		}
		@Test
		public void testDynamicSql() throws SetSQLParamException
		{
			String listRepositorySql = "select *  from CIM_ETL_REPOSITORY  where 1=1 " +
			"#if($HOST_ID && !$HOST_ID.equals(\"\")) " +
			"	and HOST_ID = #[HOST_ID]" +
			"#end  " +
			" and PLUGIN_ID = #[PLUGIN_ID] " +
			" and CATEGORY_ID = #[CATEGORY_ID] and APP = #[APP]";
			SQLParams params = new SQLParams();
			params.addSQLParam("HOST_ID", null,
					SQLParams.STRING);
			params.addSQLParam("PLUGIN_ID", "pluginid",
					SQLParams.STRING);
			params.addSQLParam("CATEGORY_ID", "catogoryid",
					SQLParams.STRING);
			params.addSQLParam("APP", "app", SQLParams.STRING);
			params.buildParams(listRepositorySql,null);
			System.out.println(params.getNewsql());
//			Template template = VelocityUtil.getTemplate(RSConstant.PUBLISHER_DATA_VM);
//			VelocityContext context = new VelocityContext();
//			context.put("userAccuont", publisher_.getUserAccuont());
//			context.put("publisherName", publisher_.getPublisherName());
		}

		/** ******************************** 资源库 ************************** */
		/**
		 * 查询资源库列表
		 */
		public List<RepositoryInfo> dynamicQuery() {
			// TODO Auto-generated method stub
			List<RepositoryInfo> repositorys = new ArrayList<RepositoryInfo>();

			PreparedDBUtil dbutil = new PreparedDBUtil();
			String listRepositorySql = "select *  from CIM_ETL_REPOSITORY  where 1=1 " +
					"#if($HOST_ID && !$HOST_ID.equals(\"\")) " +
					"	and HOST_ID = #[HOST_ID]" +
					"#end  " +
					" and PLUGIN_ID = #[PLUGIN_ID] " +
					" and CATEGORY_ID = #[CATEGORY_ID] and APP = #[APP]";
			String sql = listRepositorySql.toString();

			try {
				// 查询参数
				SQLParams params = new SQLParams();
				params.addSQLParam("HOST_ID", null,
						SQLParams.STRING);
				params.addSQLParam("PLUGIN_ID", "pluginid",
						SQLParams.STRING);
				params.addSQLParam("CATEGORY_ID", "catogoryid",
						SQLParams.STRING);
				params.addSQLParam("APP", "app", SQLParams.STRING);

				dbutil.preparedSelect(params, "bspf",
								sql);
				// 执行查询
				dbutil.executePrepared();

				for (int i = 0; i < dbutil.size(); i++) {
					RepositoryInfo repositoryInfo = new RepositoryInfo();

					repositoryInfo.setId(dbutil.getString(i, "ID"));
					repositoryInfo.setName(dbutil.getString(i, "NAME"));
					repositoryInfo.setDescription(dbutil
							.getString(i, "DESCRIPTION"));

					repositorys.add(repositoryInfo);
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("后台数据库操作错误！", e);
			}

			return repositorys;
		}

		/**
		 * 通过名称取得资源库在数据库的ID
		 * 
		 * @param businessId
		 * @param pluginid
		 * @param category
		 * @param optionRepname
		 * @return
		 */
		public RepositoryInfo getRepositoryByName(AppParams appParams,
				String optionRepname) {
			// TODO Auto-generated method stub
			RepositoryInfo repositoryInfo = null;

			PreparedDBUtil dbutil = new PreparedDBUtil();
			String sql = getRepositoryByNameSql.toString();
			try {
				// 查询参数
				SQLParams params = new SQLParams();
				params.addSQLParam("HOST_ID", appParams.getBusinessId(),
						SQLParams.STRING);
				params.addSQLParam("PLUGIN_ID", appParams.getPluginid(),
						SQLParams.STRING);
				params.addSQLParam("CATEGORY_ID", appParams.getCategory(),
						SQLParams.STRING);
				params.addSQLParam("APP", appParams.getApp(), SQLParams.STRING);
				params.addSQLParam("NAME", optionRepname, SQLParams.STRING);

				dbutil
						.preparedSelect(params, "bspf",
								sql);
				// 执行查询
				dbutil.executePrepared();

				if (dbutil.size() > 0) {
					repositoryInfo = new RepositoryInfo();
					repositoryInfo.setId(dbutil.getString(0, "ID"));
					repositoryInfo.setName(dbutil.getString(0, "NAME"));
					repositoryInfo.setDescription(dbutil
							.getString(0, "DESCRIPTION"));
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("后台数据库操作错误！", e);
			}

			return repositoryInfo;
		}

		/**
		 * 将数据同步到数据库
		 * 
		 * @param deleteList
		 * @param insertList
		 * @param updateList
		 */
		public void synRepositoryToDb(AppParams appParams,
				List<RepositoryInfo> deleteList, List<RepositoryInfo> insertList,
				List<RepositoryInfo> updateList) {
			if (deleteList.size() > 0 || insertList.size() > 0
					|| updateList.size() > 0) {
				TransactionManager transactionManager = new TransactionManager();
				PreparedDBUtil preparedDBUtil = new PreparedDBUtil();

				try {
					transactionManager.begin();
					for (Iterator iterator = deleteList.iterator(); iterator
							.hasNext();) {
						RepositoryInfo repositoryInfo = (RepositoryInfo) iterator
								.next();
						deleteRepository(preparedDBUtil, repositoryInfo);
					}

					for (Iterator iterator = insertList.iterator(); iterator
							.hasNext();) {
						RepositoryInfo repositoryInfo = (RepositoryInfo) iterator
								.next();
						insertRepository(preparedDBUtil, repositoryInfo, appParams);
					}

					for (Iterator iterator = updateList.iterator(); iterator
							.hasNext();) {
						RepositoryInfo repositoryInfo = (RepositoryInfo) iterator
								.next();
						updateRepository(preparedDBUtil, repositoryInfo);
					}

					preparedDBUtil.executePreparedBatch();
					transactionManager.commit();
				} catch (SQLException e) {
					e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (RollbackException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					throw new RuntimeException("后台数据库操作错误！", e);
				} catch (RollbackException e) {
					e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (RollbackException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					throw new RuntimeException("后台数据库操作错误！", e);
				} catch (TransactionException e) {
					try {
						transactionManager.rollback();
					} catch (RollbackException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
					throw new RuntimeException("后台数据库操作错误！", e);
				}
			}
		}

		private void deleteRepository(PreparedDBUtil preparedDBUtil,
				RepositoryInfo repositoryInfo) throws SQLException {
			
			
			SQLParams params = new SQLParams();
			params.addSQLParam("ID", repositoryInfo.getId(), SQLParams.STRING);

			String sql = deleteRepositorySql.toString();
			preparedDBUtil.preparedDelete(params, "bspf",
					sql);
			preparedDBUtil.addPreparedBatch();

			
		}

		private void insertRepository(PreparedDBUtil preparedDBUtil,
				RepositoryInfo repositoryInfo, AppParams appParams)
				throws SQLException {
			SQLParams params = new SQLParams();
			params.addSQLParam("ID", getSEQ_CIM_ETL_REPOSITORY(), SQLParams.STRING);
			// ID,HOST_ID,PLUGIN_ID,CATEGORY_ID,NAME,DESCRIPTION,DATASOURCE_NAME,DRIVER,JDBC_URL,USERNAME,PASSWORD,VALIDATION_QUERY
			params.addSQLParam("HOST_ID", appParams.getBusinessId(),
					SQLParams.STRING);
			params.addSQLParam("PLUGIN_ID", appParams.getPluginid(),
					SQLParams.STRING);
			params.addSQLParam("CATEGORY_ID", appParams.getCategory(),
					SQLParams.STRING);
			params.addSQLParam("APP", appParams.getApp(), SQLParams.STRING);

			params.addSQLParam("NAME", repositoryInfo.getName(), SQLParams.STRING);
			params.addSQLParam("DESCRIPTION", repositoryInfo.getDescription(),
					SQLParams.STRING);

			String sql = insertRepositorySql.toString();
			preparedDBUtil.preparedInsert(params, "bspf",
					sql);
			preparedDBUtil.addPreparedBatch();
		}

		private synchronized String getSEQ_CIM_ETL_REPOSITORY() {
			String primaryKey = null;
			try {
				primaryKey = DBUtil.getNextStringPrimaryKey(
						"bspf", "CIM_ETL_REPOSITORY");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("后台数据库操作错误！", e);
			}
			return primaryKey;
		}

		private void updateRepository(PreparedDBUtil preparedDBUtil,
				RepositoryInfo repositoryInfo) throws SQLException {
			SQLParams params = new SQLParams();
			params.addSQLParam("ID", repositoryInfo.getId(), SQLParams.STRING);
			// ID,HOST_ID,PLUGIN_ID,CATEGORY_ID,NAME,DESCRIPTION,DATASOURCE_NAME,DRIVER,JDBC_URL,USERNAME,PASSWORD,VALIDATION_QUERY
			params.addSQLParam("NAME", repositoryInfo.getName(), SQLParams.STRING);
			params.addSQLParam("DESCRIPTION", repositoryInfo.getDescription(),
					SQLParams.STRING);

			String sql = updateRepositorySql.toString();
			preparedDBUtil.preparedUpdate(params, "bspf",
					sql);
			preparedDBUtil.addPreparedBatch();
		}
		@Test
		public void test()
		{
			ConfigSQLExecutor executor = new ConfigSQLExecutor("com/frameworkset/sqlparams/testsql.xml");
			try
			{
				RedYjChuZhiTongJiDto dto = new RedYjChuZhiTongJiDto();
				dto.setEndDate("2011-08-11");
				dto.setStartDate("2011-08-11");
				executor.queryListBean(RedYjChuZhiTongJiDto.class, "queryFieldWithSQLParams", dto);
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}



