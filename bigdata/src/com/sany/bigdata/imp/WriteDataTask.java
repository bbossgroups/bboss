package com.sany.bigdata.imp;

import java.sql.ResultSet;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.ResultSetNullRowHandler;
import com.frameworkset.common.poolman.sql.PoolManResultSetMetaData;
import com.frameworkset.util.SimpleStringUtil;

public class WriteDataTask {
	 private static Logger log = Logger.getLogger(WriteDataTask.class);
	 BlockingQueue<FileSegment> upfileQueues;
	 GenFileHelper genFileHelper;
	 FileSegment fileSegment;
	 public WriteDataTask(GenFileHelper genFileHelper, BlockingQueue<FileSegment> upfileQueues,FileSegment fileSegment)
	{
			this.fileSegment = fileSegment;
		 this.upfileQueues = upfileQueues;
		 this.genFileHelper = genFileHelper;
	 }

	 PoolManResultSetMetaData metaData;
	 StringBuilder buidler = null;
	private Object getValue(int colType,FileSegment fileSegment ,ResultSet row,int i,String colName) throws Exception
	{
		Object value = null;
		
		try {
			if(colType == java.sql.Types.TIMESTAMP )
			{
				value = row.getTimestamp(i+1);
				if(value != null)
					value = ((java.sql.Timestamp)value).getTime();
				else
					value  = 0;
			}
			else if(colType == java.sql.Types.DATE)
			{
				value = row.getDate(i+1);
				if(value != null)
					value = ((java.sql.Date)value).getTime();
				else
					value  = 0;
			}
			else
				value = row.getString(i+1);
		} catch (Exception e) {
			String pkvalue = row.getString(this.fileSegment.job.config.pkname);
			log.error("Get column["+colName+"] value for row that pkvalue["+this.fileSegment.job.config.pkname+"="+pkvalue+"] failed:",e);
			 
		}
		return value;
	}
	private void write(FileSegment fileSegment ,ResultSet row) throws Exception
    {
		try
		{
			if(metaData == null)
				metaData = PoolManResultSetMetaData.getCopy(row.getMetaData());
			
	
			
	    	int counts = metaData.getColumnCount();
	    	if(fileSegment.job.config.datatype == null || fileSegment.job.config.datatype.equals("json"))
	    	{
		    	buidler.append("{");
				for(int i =0; i < counts; i++)
				{
					String colName = metaData.getColumnLabelUpperByIndex(i);
					int colType = metaData.getColumnTypeByIndex(i);
					if("ROWNUM__".equals(colName))//去掉oracle的行伪列
						continue;
					
					Object value = getValue(  colType,  fileSegment ,  row,  i,  colName);
					 
					if(i == 0)
					{
						buidler.append("\""+colName+"\":\""+value  +"\"");
					}
					else
					{
						buidler.append(",\""+colName+"\":\""+value  +"\"");
					}
				}
				buidler.append("}");
				
	    	}
	    	else
	    	{
	    		for(int i =0; i < counts; i++)
	    		{
		    		String colName = metaData.getColumnLabelUpperByIndex(i);
					int colType = metaData.getColumnTypeByIndex(i);
					
					if("ROWNUM__".equals(colName))//去掉oracle的行伪列
						continue;
					Object value = getValue(  colType,  fileSegment ,  row,  i,  colName);
						
					if(i == 0)
					{
						buidler.append(colName).append("#").append(value );
					}
					else
					{
						buidler.append("#").append(colName).append("#").append(value );
					}
	    		}
	    	}
	    	fileSegment.writeLine(buidler.toString());
	    	buidler.setLength(0);
		}
		catch(Exception e)
		{
			fileSegment.errorrow();
			throw e;
		}
		
 
    	
    	
    }
 
 
	 private void genpage(final FileSegment fileSegment  ) throws Exception
	    {
		 	
		 	
		 	if(!fileSegment.usepagine())//采用主键分区模式
		 	{
		    	SQLExecutor.queryWithDBNameByNullRowHandler(new ResultSetNullRowHandler(){
	
					@Override
					public void handleRow(ResultSet row) throws Exception {
						write(  fileSegment,row);
						
					}
		    		
		    	}, fileSegment.getDBName(), fileSegment.getQuerystatement(),fileSegment.getEndoffset(),fileSegment.getStartoffset());
		 	}
		 	else//采用分页分区模式，mysql，oracle
		 	{
		 		DBUtil.getDBAdapter(fileSegment.getDBName()).queryByNullRowHandler(new ResultSetNullRowHandler(){
		 			
					@Override
					public void handleRow(ResultSet row) throws Exception {
						write(  fileSegment,row);
						
					}
		    		
		    	}, fileSegment.getDBName(), fileSegment.getPageinestatement(),fileSegment.getStartoffset(),(int)fileSegment.getPagesize());
		 		
		 	}
	    }
	
	 
	 
	public void run() {
		try {
			
			 buidler = new StringBuilder();
			fileSegment.init();
			log.info("开始生成文件："+fileSegment.toString());
			genpage( fileSegment  ) ;
			fileSegment.flush();
			fileSegment.genendtimestamp =  System.currentTimeMillis();
			
			log.info("生成文件结束："+fileSegment.toString());
		} catch (Exception e) {
			fileSegment.taskStatus.setStatus(2);
			 
			fileSegment.taskStatus.setErrorInfo(SimpleStringUtil.exceptionToString(e));
			log.error("生成文件异常结束："+fileSegment.toString(),e);
			if(genFileHelper.genlocalfile())
				genFileHelper.countdownupfilecount();
		}
		finally
		{
			this.buidler = null;
			fileSegment.close();
		}
		if(genFileHelper.genlocalfile())
		{
			if(fileSegment.getRows() == 0)//忽略空文件上传
			{
				genFileHelper.countdownupfilecount();
			}
			else
			{
				try {
					upfileQueues.put(fileSegment);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					genFileHelper.countdownupfilecount();
				} 
			}
		}
		
	}
}
