package org.frameworkset.gencode.core;

import java.util.List;

import org.frameworkset.gencode.entity.Field;


public class SQLBuilder {
	
	private GencodeServiceImpl gencodeService;
	public SQLBuilder(GencodeServiceImpl gencodeService)
	{
		this.gencodeService = gencodeService;
	}
	public void buidlersql(SQL sql)
	{
		String tableName = this.gencodeService.getModuleMetaInfo().getTableName();
		if(sql.getOptype().equals(Constant.add))
		{
			StringBuilder builder = new StringBuilder();
			builder.append("insert into ").append(tableName).append(" (");
			List<Field> fs = this.gencodeService.getAllfields();
			StringBuilder values = new StringBuilder();
			values.append(" values(");
			for(int i = 0; i < fs.size(); i ++)
			{
				Field f = fs.get(i);
				if(i > 0)
				{
					builder.append(",");
					values.append(",");
				}
				builder.append(f.getColumnname());
				values.append("#[").append(f.getFieldName()).append("]");
				
			}
			builder.append(")");
			values.append(")");
			builder.append(values.toString());
			sql.setSql(builder.toString());
			
		}
		else if(sql.getOptype().equals( Constant.delete))
		{
			StringBuilder builder = new StringBuilder();
			builder.append("delete from ").append(tableName).append(" where ").append(gencodeService.getPrimaryKeyName()).append("=?");
			
			sql.setSql(builder.toString());
		}
		else if(sql.getOptype().equals( Constant.update))
		{
			StringBuilder builder = new StringBuilder();
			builder.append("update ").append(tableName).append(" set ");
			List<Field> fs = this.gencodeService.getAllfields();
			for(int i = 0; i < fs.size(); i ++)
			{
				Field f = fs.get(i);
				if(i > 0)
				{
					 
					builder.append(",");
				}
				builder.append(f.getColumnname()).append("=").append("#[").append(f.getFieldName()).append("]");
				
			}
			
			builder.append(" where ").append(gencodeService.getPrimaryKeyName()).append("=?");
			sql.setSql(builder.toString());
		}
		else if(sql.getOptype().equals( Constant.get))
		{
			StringBuilder builder = new StringBuilder();
			builder.append("select * from ").append(tableName).append(" where ").append(gencodeService.getPrimaryKeyName()).append("=?");
			
			sql.setSql(builder.toString());
		}
		else if(sql.getOptype().equals( Constant.paginequery))
		{
			StringBuilder builder = new StringBuilder();
			builder.append("select * from ").append(tableName).append(" where ").append(gencodeService.getPrimaryKeyName()).append("=?");
			
			sql.setSql(builder.toString());
		}
		
	}

}
