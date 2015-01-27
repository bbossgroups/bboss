package org.frameworkset.gencode.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.gencode.entity.Field;
import org.frameworkset.gencode.entity.ModuleMetaInfo;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.common.poolman.sql.TableMetaData;
import com.frameworkset.orm.engine.EngineException;
import com.frameworkset.orm.engine.model.NameFactory;
import com.frameworkset.orm.engine.model.NameGenerator;
import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.VelocityUtil;

/**
 * 生成代码业务组件
 * @author yinbp
 *
 */
public class GencodeServiceImpl {
	private static final Logger log = Logger.getLogger(GencodeServiceImpl.class);
	/**
	 * 代码存放目录
	 */
	private File rootdir;
	private File resourcedir;
	private File javaSourceDir;
	private File javaEntiySourceDir;
	private File javaServiceSourceDir;
	private File javaActionSourceDir;
	private File jspSourceDir;
	private File mvcConfDir;
	private File webxmlFile;
	private ModuleMetaInfo moduleMetaInfo;
	private String javaTemplate = "gencode/java.vm";
	private String jspTemplate = "gencode/jsp.vm";
	public String genCode(ModuleMetaInfo moduleMetaInfo)
	{
		this.moduleMetaInfo = moduleMetaInfo; 
		this.init();
		genJavaSource();
		return "success";
	}
	
	private void init()
	{
		File f = new File(moduleMetaInfo.getSourcedir());
		if(!f.exists())
		{
			f.mkdirs();
		}
		else if(moduleMetaInfo.isClearSourcedir())
		{
			f.delete();
			f.mkdirs();
		}
		this.rootdir = f;
		String modulePackage = moduleMetaInfo.getPackagePath().replace(".", "/") ;
		modulePackage = modulePackage.endsWith("/") ? modulePackage + moduleMetaInfo.getModuleName():
			modulePackage + "/" + moduleMetaInfo.getModuleName();
		javaSourceDir = new File(this.rootdir,"src/"+modulePackage);
		if(!javaSourceDir.exists())
		{
			javaSourceDir.mkdirs();
		}
		
		javaEntiySourceDir = new File(this.javaSourceDir,  "entity");
		if(!javaEntiySourceDir.exists())
		{
			javaEntiySourceDir.mkdirs();
		}
		javaServiceSourceDir = new File(this.javaSourceDir,"service");
		if(!javaServiceSourceDir.exists())
		{
			javaServiceSourceDir.mkdirs();
		}
		javaActionSourceDir = new File(this.javaSourceDir,"action");
		if(!javaActionSourceDir.exists())
		{
			javaActionSourceDir.mkdirs();
		}
		resourcedir = new File(this.rootdir,"resources");
		if(!resourcedir.exists())
		{
			resourcedir.mkdirs();
		}
		
		jspSourceDir = new File(this.rootdir,"WebRoot/"+ moduleMetaInfo.getModuleName());
		if(!jspSourceDir.exists())
		{
			jspSourceDir.mkdirs();
		}
		mvcConfDir = new File(this.rootdir,"WebRoot/WEB-INF/conf/"+ moduleMetaInfo.getModuleName());
		if(!mvcConfDir.exists())
		{
			mvcConfDir.mkdirs();
		}
		webxmlFile = new File(this.rootdir,"WebRoot/WEB-INF/web.xml");
		if(!webxmlFile.exists())
		{
			try {
				webxmlFile.createNewFile();
			} catch (IOException e) {
				log.error("Create web.xml failed:"+webxmlFile.getAbsolutePath(),e);
			}
		}
		
	}
	private String convertType(String type)
	{
		if(type.equals("BigDecimal"))
		{
			return "long";
		}
		else
			return type;
	}
	private List<Field> getFields(TableMetaData tableMeta)
	{
		Set<ColumnMetaData> columns = tableMeta.getColumns();
		if(columns.size() > 0)
		{
			 
			List<Field> fs = new ArrayList<Field>();
			for(ColumnMetaData c:columns)
			{
				Field f = new Field();
				f.setType(convertType(c.getSchemaType().getJavaType()));
				 
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
	
	public void genJavaSource()
	{
		
		
		
		
		String entityName = this.moduleMetaInfo.getEntityName(); 
		if(SimpleStringUtil.isEmpty(entityName))
		{
			 
			 List inputs = new ArrayList(2);
	         inputs.add(moduleMetaInfo.getTableName());
	         inputs.add( NameGenerator.CONV_METHOD_JAVANAME);
	         
	         try
	         {
	        	 entityName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
	                                                 inputs,this.moduleMetaInfo.isIgnoreEntityFirstToken());
	             
	         }
	         catch (EngineException e)
	         {
	             log.error(e.getMessage(), e);
	         }
		}
		String entityJavaName = entityName + ".java";
		String serviceInfJavaName = entityName + "Service.java";
		String serviceImplJavaName = entityName + "ServiceImpl.java";
		String controllerJavaName = entityName + "Controller.java";
		
		File entity = new File(this.javaEntiySourceDir,entityJavaName);
		if(entity.exists())
		{
			entity.delete();
		}
		File serviceInf = new File(this.javaServiceSourceDir,serviceInfJavaName);
		if(serviceInf.exists())
		{
			serviceInf.delete();
		}
		File serviceImpl = new File(this.javaServiceSourceDir,serviceImplJavaName);
		if(serviceImpl.exists())
		{
			serviceImpl.delete();
		}
		File controller = new File(this.javaActionSourceDir,controllerJavaName);
		if(controller.exists())
		{
			controller.delete();
		}
		try {
			entity.createNewFile();
			serviceInf.createNewFile();
			serviceImpl.createNewFile();
			controller.createNewFile();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = format.format(new Date());			
			TableMetaData tableMeta = DBUtil.getTableMetaData(this.moduleMetaInfo.getDatasourceName(), this.moduleMetaInfo.getTableName());
			 List<Field> fields = getFields( tableMeta);
			 Template entitytempalte = VelocityUtil.getTemplate("gencode/entityjava.vm");
			 VelocityContext context = new VelocityContext();
			 context.put("fields", fields);
			 String entityPackageInfo = this.moduleMetaInfo.getPackagePath() + "." + this.moduleMetaInfo.getModuleName()+".entity";
			 context.put("package", entityPackageInfo);
			 context.put("import", "");
			 context.put("classname", entityName);
			 context.put("description", "");
			 context.put("company", "");
			 context.put("gendate", date);
			 context.put("author", "yinbp");
			 context.put("version", "v1.0");
			 OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(entity),"UTF-8");
			 entitytempalte.merge(context, writer);
			 writer.flush();
			 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void genEntity(String entityJavaName)
	{
		String entityPackageCode = "package "+ this.moduleMetaInfo.getPackagePath() +".entity;";
		StringBuffer buf = new StringBuffer();
		String importCode = "";
		String bodyCode = "";
		
//		StringBuffer buf = new StringBuffer();
	}
	
	public static void main(String[] args)
	{
		 List inputs = new ArrayList(2);
         inputs.add("td_tableinfo_name");
         inputs.add( NameGenerator.CONV_METHOD_UNDERSCORE);
         try
         {
             String javaName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
                                                 inputs,false);
             System.out.println(javaName);
         }
         catch (EngineException e)
         {
             log.error(e, e);
         }
		
           inputs = new ArrayList(2);
         inputs.add("td_tableinfo_name");
         inputs.add( NameGenerator.CONV_METHOD_UNDERSCORE_OMIT_SCHEMA);
         try
         {
             String javaName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
                                                 inputs,false);
             System.out.println(javaName);
         }
         catch (EngineException e)
         {
             log.error(e, e);
         }
         inputs = new ArrayList(2);
         inputs.add("td_tableinfo_name");
         inputs.add( NameGenerator.CONV_METHOD_JAVANAME);
         try
         {
             String javaName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
                                                 inputs,false);
             System.out.println(javaName);
         }
         catch (EngineException e)
         {
             log.error(e, e);
         }
         inputs = new ArrayList(2);
         inputs.add("td_tableinfo_name");
         inputs.add( NameGenerator.CONV_METHOD_NOCHANGE);
         try
         {
             String javaName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
                                                 inputs,false);
             System.out.println(javaName);
         }
         catch (EngineException e)
         {
             log.error(e, e);
         }
	}

}
