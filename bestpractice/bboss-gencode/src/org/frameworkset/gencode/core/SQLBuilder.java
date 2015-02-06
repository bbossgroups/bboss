package org.frameworkset.gencode.core;

import java.util.List;

import org.frameworkset.gencode.entity.ConditionField;
import org.frameworkset.gencode.entity.Field;
import org.frameworkset.gencode.entity.SortField;


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
			if(gencodeService.getPrimaryField() != null)
				builder.append(" where ").append(gencodeService.getPrimaryField().getColumnname()).append("=#[").append(this.gencodeService.getPrimaryField().getFieldName()).append("]");
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
			builder.append("select * from ").append(tableName).append(" where 1=1");
			List<ConditionField> conditions = this.gencodeService.getConditions();
			if(conditions != null && conditions.size() > 0)
			{
				for(ConditionField f:conditions)
				{
					if(f.isOr())
					{
						builder.append(" or ");
					}
					else
					{
						builder.append(" and ");
					}
					if(f.isLike())
					{
						builder.append(f.getColumnName()).append(" like ").append("#[").append(f.getFieldName()).append("]");
					}
					else
					{
						builder.append(f.getColumnName()).append("=#[").append(f.getFieldName()).append("]");
					}
					//需要考虑分组的功能
				}
			}
			List<SortField> sorts = this.gencodeService.getSortFields();
			
			 
			builder.append("\r\n#if($sortKey && !$sortKey.equals(\"\"))\r\n")
			  	.append("order by $sortKey \r\n")
			  	.append("#if($sortDesc)\r\n")
			  	.append("  	desc \r\n")
			  	.append("#else\r\n")
			  	.append(" asc\r\n")
			  	.append(" #end\r\n	");
			if(sorts != null && sorts.size() > 0)
			{
				builder.append(" #else\r\n")
				  	.append(" order by  ");
				int i = 0;
				for(SortField f: sorts)
				{
					if(i > 0)
						builder.append(",");
					builder.append(f.getColumnName()).append(" ").append(f.isDesc()?"desc":"asc");
					i ++;
				}
			}
			builder.append("\r\n#end");			 
			
			sql.setSql(builder.toString());
		}
		
	}

}
