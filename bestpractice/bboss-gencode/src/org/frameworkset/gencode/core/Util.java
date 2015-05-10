package org.frameworkset.gencode.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.gencode.entity.FieldInfo;

import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.common.poolman.sql.TableMetaData;
import com.frameworkset.orm.engine.EngineException;
import com.frameworkset.orm.engine.model.NameFactory;
import com.frameworkset.orm.engine.model.NameGenerator;
import com.frameworkset.util.SimpleStringUtil;

public class Util {
	private static Logger log = Logger.getLogger(Util.class);
	public final static int addpage = 0;
	public final static int editpage = 1;
	public final static int viewpage = 2;
	public final static int listpage = 3;
	public final static int other = 4;
	public Util() {
		// TODO Auto-generated constructor stub
	}
	public static String convertType(String type)
	{
		if(type.equals("BigDecimal"))
		{
			return "long";
		}
		else if(type.equals("Date"))
		{
			return "Timestamp";
		}
		else
			return type;
	}

	public static List<FieldInfo> getSimpleFields(TableMetaData tableMeta)
	{
		Set<ColumnMetaData> columns = tableMeta.getColumns();
		if(columns.size() > 0)
		{
			 
			List<FieldInfo> fs = new ArrayList<FieldInfo>();
			
			for(ColumnMetaData c:columns)
			{
				FieldInfo f = new FieldInfo();
				f.setType(convertType(c.getSchemaType().getJavaType()));
				
				if(c.getRemarks() != null)
				{
					f.setFieldCNName(c.getRemarks());
					f.setFieldAsciiCNName(SimpleStringUtil.native2ascii(c.getRemarks()));
				}
		         try
		         {
		        	 List<String> inputs = new ArrayList<String>(2);
			         inputs.add(c.getColumnName().toLowerCase());
			         inputs.add( NameGenerator.CONV_METHOD_JAVANAME);
		        	 String mfieldName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
		                                                 inputs,false);
		        	 inputs = new ArrayList<String>(2);
			         inputs.add(c.getColumnName().toLowerCase());
			         inputs.add( NameGenerator.CONV_METHOD_JAVAFIELDNAME);
		        	 String fieldName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
                             inputs,false);
		        	 f.setMfieldName(mfieldName);
		        	 f.setFieldName(fieldName);
		        	 f.setColumnname(c.getColumnName());
		        	 f.setColumntype(c.getTypeName());
		        	 fs.add(f);
		        	
		         }
		         catch (EngineException e)
		         {
		             log.error(e.getMessage(), e);
		         }
				
			}
			
			return fs;
			
		}
		return null;
	}
}
