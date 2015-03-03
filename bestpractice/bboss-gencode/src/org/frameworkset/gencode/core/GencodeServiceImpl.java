package org.frameworkset.gencode.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.gencode.entity.AnnoParam;
import org.frameworkset.gencode.entity.Annotation;
import org.frameworkset.gencode.entity.ConditionField;
import org.frameworkset.gencode.entity.Field;
import org.frameworkset.gencode.entity.Method;
import org.frameworkset.gencode.entity.MethodParam;
import org.frameworkset.gencode.entity.ModuleMetaInfo;
import org.frameworkset.gencode.entity.SortField;

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
	private File mvcconf ;
	private File webxmlFile;
	private ModuleMetaInfo moduleMetaInfo;
	private String exceptionName;
	private String entityName;
	private String entityParamName;
	private String conditionEntityName;
	private String conditionEntityParamName;
	private String sqlfilepath;
	private SQLBuilder SQLBuilder ; 
	private List<SortField> sortFields;
	
	private Field primaryField ;
	private String primaryKeyName;
	private String modulePackage ;
	private String javamodulePackage ;
	private String serviceClass;
	private String controlClass;
	/**
	 * 需要作为查询条件的字段
	 */
	private List<Field> conditions;
	private List<SQL> sqls;
	/**
	 * 所有字段
	 */
	private List<Field> allfields;
	
	public String genCode(ModuleMetaInfo moduleMetaInfo)
	{
		this.moduleMetaInfo = moduleMetaInfo; 
		this.init();
		genJavaSource();
		genPersistentConfigfile();
		genUI();
		 genMVCConf();
		return "success";
	}
	
	/**
	 * 生成前端jsp ui界面
	 */
	private void genUI()
	{
		
	}
	
	/**
	 * 生成前端jsp ui界面
	 */
	private void genMVCConf()
	{
		GenMVCConf conf = new GenMVCConf(this);
		conf.gen();
	}
	
	
	private void init()
	{
			
		SQLBuilder = new SQLBuilder(this);
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
		modulePackage = moduleMetaInfo.getPackagePath().replace(".", "/") ;
		modulePackage = modulePackage.endsWith("/") ? modulePackage + moduleMetaInfo.getModuleName():
			modulePackage + "/" + moduleMetaInfo.getModuleName();
		javamodulePackage = !moduleMetaInfo.getPackagePath().endsWith(".")? 
						moduleMetaInfo.getPackagePath() + "." +moduleMetaInfo.getModuleName()
						:moduleMetaInfo.getPackagePath() +moduleMetaInfo.getModuleName();;
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
		mvcconf = new File(getMvcConfDir(),"bboss-"+getModuleMetaInfo().getModuleName()+".xml");
		webxmlFile = new File(this.rootdir,"WebRoot/WEB-INF/web.xml");
		if(!webxmlFile.exists())
		{
			try {
				webxmlFile.createNewFile();
			} catch (IOException e) {
				log.error("Create web.xml failed:"+webxmlFile.getAbsolutePath(),e);
			}
		}
		TableMetaData tableMeta = DBUtil.getTableMetaData(this.moduleMetaInfo.getDatasourceName(), this.moduleMetaInfo.getTableName());
		List<Field> fields = getFields( tableMeta);
		 this.allfields = fields;
		 if(this.conditions != null )
		 {
			 for(Field field:conditions)
			 {
				 
				 for(Field dbfield:this.allfields)
				 {
					 if(field.getFieldName().equals(dbfield.getFieldName()))
					 {
						 field.setType(dbfield.getType());
						 field.setColumnname(dbfield.getColumnname());
						 field.setMfieldName(dbfield.getMfieldName());
					 }
				 }
			 }
		 }
	}
	private String convertType(String type)
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
				boolean isp = false;
				if(this.moduleMetaInfo.isAutogenprimarykey() && tableMeta.getPrimaryKeyMetaData(c.getColumnName()) != null)
				{
					Annotation anno = new Annotation();
					anno.setName("PrimaryKey");
					f.addAnnotation(anno);
					isp = true;
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
		        	 if(isp)
		        	 {
		        		 this.primaryField = f;
		        		 this.primaryKeyName = f.getColumnname();
		        		 fs.add(0, f);
		        	 }
		        	 else
		        	 {
		        		 fs.add(f);
		        	 }
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
	
	
	/**
	 * 生成持久层sql配置文件
	 * @throws Exception 
	 */
	private void genPersistentConfigfile() 
	{
		File sqlconfig = new File(this.javaServiceSourceDir,this.entityParamName + ".xml");
		if(sqlconfig.exists())
		{
			sqlconfig.delete();
		}
		this.sqlfilepath = this.modulePackage + "/service/"+this.entityParamName + ".xml";
		try {
			sqlconfig.createNewFile();
			 Template persistentsqltempalte = VelocityUtil.getTemplate("gencode/conf/persistentsql.vm");
			 VelocityContext context = new VelocityContext();
			
			 context.put("sqls", this.sqls);
			 context.put("description",entityParamName+" sql配置文件");
			 context.put("company", this.moduleMetaInfo.getCompany());
			 context.put("gendate", this.moduleMetaInfo.getDate());
			 context.put("author", this.moduleMetaInfo.getAuthor());
			 context.put("version", this.moduleMetaInfo.getVersion());
			 context.put("filename", this.entityParamName + ".xml");
			 
			
			 writFile(context,persistentsqltempalte,sqlconfig,this.moduleMetaInfo.getEncodecharset());
		} catch (Exception e) {
			log.error("gen Persistent Config file failed:"+sqlconfig.getAbsolutePath(),e);
		}
	}
	/**
	 * 生成java源代码
	 */
	private void genJavaSource()
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
		String conditionEntityJavaName = entityName + "Condition.java";
		String serviceInfJavaName = entityName + "Service.java";
		String serviceImplJavaName = entityName + "ServiceImpl.java";
		String controllerJavaName = entityName + "Controller.java";
		String exceptionJavaName = entityName + "Exception.java";
		
		this.exceptionName = entityName + "Exception";
		this.entityName = entityName;
		this.conditionEntityName = entityName+"Condition";
		
		this.entityParamName = (entityName.charAt(0)+"").toLowerCase()  + entityName.substring(1);
		this.conditionEntityParamName = entityParamName+"Condition";
		this.serviceClass = this.javamodulePackage + ".service."+ entityName + "ServiceImpl";
		this.controlClass = this.javamodulePackage + ".action."+entityName + "Controller"; 
		File entity = new File(this.javaEntiySourceDir,entityJavaName);
		if(entity.exists())
		{
			entity.delete();
		}
		File conditionEntity = new File(this.javaEntiySourceDir,conditionEntityJavaName);
		if(conditionEntity.exists())
		{
			conditionEntity.delete();
		}
		File exception = new File(this.javaServiceSourceDir,exceptionJavaName);
		if(exception.exists())
		{
			exception.delete();
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
			exception.createNewFile();
			conditionEntity.createNewFile();
			
			
			
 
			 genEntity(  entityName,  this.moduleMetaInfo.getDate(),  this.moduleMetaInfo.getVersion(),this.moduleMetaInfo.getAuthor(),this.moduleMetaInfo.getCompany(),"服务实体类",entity);
			 genConditionEntity(  conditionEntityName,  this.moduleMetaInfo.getDate(),  this.moduleMetaInfo.getVersion(),this.moduleMetaInfo.getAuthor(),this.moduleMetaInfo.getCompany(),"查询条件实体类",conditionEntity);
			 genServiceInf(  entityName + "Service",this.moduleMetaInfo.getDate(),this.moduleMetaInfo.getVersion(),this.moduleMetaInfo.getAuthor(),this.moduleMetaInfo.getCompany(),"服务管理接口", serviceInf);
			 genException(entityName + "Exception",this.moduleMetaInfo.getDate(),this.moduleMetaInfo.getVersion(),this.moduleMetaInfo.getAuthor(),this.moduleMetaInfo.getCompany(),"异常处理类",exception);
			 String serviceInfName = entityName + "Service";
			 genServiceImpl(entityName + "ServiceImpl",serviceInfName,this.moduleMetaInfo.getDate(),this.moduleMetaInfo.getVersion(),this.moduleMetaInfo.getAuthor(),this.moduleMetaInfo.getCompany(),"业务处理类",serviceImpl);
			 genActionCode(entityName+"Controller",null,serviceInfName, this.moduleMetaInfo.getDate(),this.moduleMetaInfo.getVersion(),this.moduleMetaInfo.getAuthor(),this.moduleMetaInfo.getCompany(),"控制器处理类",controller);
			 
		} catch (Exception e) {
			log.error("gen java source file failed:",e);
		}
		
	}
	
	private void genEntity(String entityName,String date,String version,String author,String company,String description,File entity) throws Exception
	{
		 
		 List<String> imports = evalImport(this.allfields,false);
		 Template entitytempalte = VelocityUtil.getTemplate("gencode/java/entityjava.vm");
		 VelocityContext context = new VelocityContext();
		 context.put("fields", this.allfields);
		 String entityPackageInfo = this.moduleMetaInfo.getPackagePath() + "." + this.moduleMetaInfo.getModuleName()+".entity";
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", entityName);
		 context.put("description", description);
		 context.put("company", company);
		 context.put("gendate", date);
		 context.put("author", author);
		 context.put("version", version);
	 
		 writFile(context,entitytempalte,entity,this.moduleMetaInfo.getEncodecharset());
		
	}
	
	private void genConditionEntity(String entityName,String date,String version,String author,String company,String description,File entity) throws Exception
	{
		 
		 List<String> imports = evalImport(this.conditions, true);
		 Template entitytempalte = VelocityUtil.getTemplate("gencode/java/entityjava.vm");
		 VelocityContext context = new VelocityContext();
		 List<Field> _conditions = new ArrayList<Field>();
		 _conditions.addAll(conditions);
		 Field sort = new Field();
		 sort.setFieldName("sortKey");
		 sort.setMfieldName("SortKey");
		 sort.setType("String");
		 _conditions.add(sort);
		 Field desc = new Field();
		 desc.setFieldName("sortDesc");
		 desc.setMfieldName("SortDesc");
		 desc.setType("boolean");
		 _conditions.add(desc);
		 context.put("fields", _conditions);
		 String entityPackageInfo = this.moduleMetaInfo.getPackagePath() + "." + this.moduleMetaInfo.getModuleName()+".entity";
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", entityName);
		 context.put("description", description);
		 context.put("company", company);
		 context.put("gendate", date);
		 context.put("author", author);
		 context.put("version", version);
	 
		 writFile(context,entitytempalte,entity,this.moduleMetaInfo.getEncodecharset());
		
	}
	
	
	
	private void genServiceInf(String serviceinfName,String date,String version,String author,String company,String description,File serviceInf) throws Exception
	{
		 
		 List<String> imports = evalServiceInfImport();
		 Template serviceinftempalte = VelocityUtil.getTemplate("gencode/java/serviceinfjava.vm");
		 VelocityContext context = new VelocityContext();
		 context.put("fields", null);
		 String entityPackageInfo = this.moduleMetaInfo.getPackagePath() + "." + this.moduleMetaInfo.getModuleName()+".service";
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", serviceinfName);
		 context.put("description", description);
		 context.put("company", company);
		 context.put("gendate", date);
		 context.put("author", author);
		 context.put("version", version);
		
		 List<Method> methods = getMethods(0);
		 context.put("methods", methods);
	 
		 writFile(context,serviceinftempalte,serviceInf,this.moduleMetaInfo.getEncodecharset());
		
	}
	private List<Field> getServiceImplFields(String serviceName)
	{
		List<Field> fields = new ArrayList<Field>();
		Field log4j = new Field(); 
		log4j.setStaticed(true);
		log4j.setFieldName("log");
		log4j.setType("Logger");
		String entityPackageInfo = this.moduleMetaInfo.getPackagePath() + "." + this.moduleMetaInfo.getModuleName()+".service";
		log4j.setDefaultValue("Logger.getLogger("+entityPackageInfo + "."+serviceName+".class)");
		fields.add(log4j);
		
		Field dao = new Field(); 
		
		dao.setFieldName("executor");
		dao.setType("ConfigSQLExecutor");
		fields.add(dao);
		return fields; 
	}
	private void genServiceImpl(String serviceName,String serviceInfname,String date,String version,String author,String company,String description,File service) throws Exception
	{
		 
		 List<String> imports = evalServiceImplImport();
		 Template serviceinftempalte = VelocityUtil.getTemplate("gencode/java/serviceimpljava.vm");
		 VelocityContext context = new VelocityContext();
		 List<Field> fields = getServiceImplFields(serviceName);
		 context.put("fields", fields);
		 String entityPackageInfo = this.moduleMetaInfo.getPackagePath() + "." + this.moduleMetaInfo.getModuleName()+".service";
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", serviceName);
		 context.put("interfaceclassname", serviceInfname);
		 
		 context.put("description", description);
		 context.put("company", company);
		 context.put("gendate", date);
		 context.put("author", author);
		 context.put("version", version);
		
		 List<Method> methods = getMethods(1);
		 context.put("methods", methods);
		 
	 
		 writFile(context,serviceinftempalte,service,this.moduleMetaInfo.getEncodecharset());
		
	}
	
	private void genActionCode(String actionName,String actionInfName,String serviceInfName,String date,String version,String author,String company,String description,File action) throws Exception
	{
		 
		 List<String> imports = evalActionImplImport();
		 Template serviceinftempalte = VelocityUtil.getTemplate("gencode/java/serviceimpljava.vm");
		 VelocityContext context = new VelocityContext();
		 String serviceName = (serviceInfName.charAt(0)+"").toLowerCase() + serviceInfName.substring(1);
		 List<Field> fields = getActionImplFields(actionName,serviceInfName,serviceName);
		 context.put("fields", fields);
		 String entityPackageInfo = this.moduleMetaInfo.getPackagePath() + "." + this.moduleMetaInfo.getModuleName()+".action";
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", actionName);
		 context.put("interfaceclassname", actionInfName);
		 
		 context.put("description", description);
		 context.put("company", company);
		 context.put("gendate", date);
		 context.put("author", author);
		 context.put("version", version);
		
		 List<Method> methods = getMethods(2);
		 context.put("methods", methods);
		 context.put("conditionFields", this.conditions);
	 
		 writFile(context,serviceinftempalte,action,this.moduleMetaInfo.getEncodecharset());
		
	}
	private List<Field> getActionImplFields(String actionName,String serviceinfclassName,String serviceName) {
		List<Field> fields = new ArrayList<Field>();
		Field log4j = new Field(); 
		log4j.setStaticed(true);
		log4j.setFieldName("log");
		log4j.setType("Logger");
	
		log4j.setDefaultValue("Logger.getLogger("+actionName+".class)");
		fields.add(log4j);
		
		Field service = new Field(); 
		
		service.setFieldName(serviceName);
		service.setType(serviceinfclassName);
		fields.add(service);
		return fields; 
	}
	
	public static final Map<String,MethodBodyGenerate> methodBodyGenerates = new HashMap<String,MethodBodyGenerate>();
	static{
		methodBodyGenerates.put(Constant.add, new AddMethodBodyGenerate());
		methodBodyGenerates.put(Constant.update, new UpdateMethodBodyGenerate());
		methodBodyGenerates.put(Constant.delete, new DeleteMethodBodyGenerate());
		methodBodyGenerates.put(Constant.deletebatch, new DeleteBatchMethodBodyGenerate());
		methodBodyGenerates.put(Constant.get, new GetMethodBodyGenerate());
		methodBodyGenerates.put(Constant.query, new QueryMethodBodyGenerate());
		methodBodyGenerates.put(Constant.paginequery, new PagineQueryMethodBodyGenerate());
		
		methodBodyGenerates.put(Constant.toAdd, new ToAddMethodBodyGenerate());
		methodBodyGenerates.put(Constant.toUpdate, new ToUpdateMethodBodyGenerate());
		methodBodyGenerates.put(Constant.index, new IndexMethodBodyGenerate());
	}
	private void setMethodBody(Method method,String methodtype,String entityName,String paramName,String encodecharset,String exception,int componenttype) throws Exception
	{
		MethodBodyGenerate methodBodyGenerate = methodBodyGenerates.get(methodtype);
		methodBodyGenerate.gen(method,entityName,entityParamName,paramName,encodecharset,exception, componenttype,this);
	}
	private List<Method> getActionMethods(int classtype ) throws Exception {
		List<String> exceptions = new ArrayList<String>();
		exceptions.add(exceptionName);
		
		List<Method> methods = new ArrayList<Method>();
		Method add = new Method();//定义添加方法
		add.setMethodname("add"+entityName);
		add.setReturntype("String");
		add.addAnnotation(new Annotation("ResponseBody"));
	
		List<MethodParam> params = new ArrayList<MethodParam>();
		MethodParam param = new MethodParam();
		param.setType(this.entityName);
		param.setName(this.entityParamName);
		params.add(param);
		add.setParams(params);
		setMethodBody(add,Constant.add,entityName,entityParamName,this.moduleMetaInfo.getEncodecharset(),exceptionName, Constant.component_type_actionimpl);
		
		methods.add(add);
		
		
		
		
		Method delete = new Method();//定义删除方法
		delete.setMethodname("delete"+entityName);
		delete.addAnnotation(new Annotation("ResponseBody"));
		delete.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		if(primaryField != null)
		{
			param.setType(primaryField.getType());
			param.setName(primaryField.getFieldName());
		}
		else
		{
			param.setType("String");
			param.setName("id");
			 this.primaryKeyName = "id";
		}
		params.add(param);
		
		delete.setParams(params);
		setMethodBody(delete,Constant.delete,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(delete);
		
		Method deletebatch = new Method();//定义删除方法
		deletebatch.setMethodname("deleteBatch"+entityName);
		deletebatch.setReturntype("String");	
		deletebatch.addAnnotation(new Annotation("ResponseBody"));
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		if(primaryField != null)
		{
			param.setType(primaryField.getType() +"...");
			param.setName(primaryField.getFieldName()+"s");
		}
		else
		{
			param.setType("String...");
			param.setName("ids");
		}
		params.add(param);
		
		deletebatch.setParams(params);
		setMethodBody(deletebatch,Constant.deletebatch,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		
		methods.add(deletebatch);
		
		Method update = new Method();//定义修改方法
		update.setMethodname("update"+entityName);
		update.setReturntype("String");		
		update.addAnnotation(new Annotation("ResponseBody"));
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		param.setType(this.entityName);
		param.setName(this.entityParamName);
		
		params.add(param);
		update.setParams(params);
		
		setMethodBody(update,Constant.update,entityName,entityParamName,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(update);
		
		Method get = new Method();//定义获取方法
		get.setMethodname("get"+entityName);
		get.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		
		if(primaryField != null)
		{
			param.setType(primaryField.getType());
			param.setName(primaryField.getFieldName());
		}
		else
		{
			param.setType("String");
			param.setName("id");
		}
		String defaultSort = param.getName();
		params.add(param);
		param = new MethodParam();
		param.setName("model");
		param.setType("ModelMap");
		params.add(param);
		get.setExceptions(exceptions);		
		get.setParams(params);
		
		setMethodBody(get,Constant.get,entityName,defaultSort,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(get);
		
		
		
		Method paginequery = new Method();//定义获取方法
		paginequery.setMethodname("queryListInfo"+entityName+"s");
		paginequery.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		
//		param.addAnnotation(new Annotation("MapKey").addAnnotationParam("pattern","condition_*"));
		param.setType(this.conditionEntityName);
		param.setName("conditions");
		params.add(param);
		
		
		param = new MethodParam();		
		param.setType("String");
		param.setName("sortKey");
		param.addAnnotation(new Annotation("PagerParam").addAnnotationParam("name","PagerParam.SORT",AnnoParam.V_CONTAST).addAnnotationParam("defaultvalue",defaultSort));
		params.add(param);
		
		param = new MethodParam();
		param.setType("boolean");
		param.setName("desc");
		param.addAnnotation(new Annotation("PagerParam").addAnnotationParam("name","PagerParam.DESC",AnnoParam.V_CONTAST).addAnnotationParam("defaultvalue", "true", AnnoParam.V_STRING));
		
		params.add(param);
		param = new MethodParam();		
		param.setType("long");
		param.setName("offset");
		param.addAnnotation(new Annotation("PagerParam").addAnnotationParam("name","PagerParam.OFFSET",AnnoParam.V_CONTAST));
		
		params.add(param);
		param = new MethodParam();
		param.setType("int");
		param.setName("pagesize");
		param.addAnnotation(new Annotation("PagerParam").addAnnotationParam("name","PagerParam.PAGE_SIZE",AnnoParam.V_CONTAST).addAnnotationParam("defaultvalue", "10", AnnoParam.V_STRING));
		params.add(param);
		param = new MethodParam();
		param.setName("model");
		param.setType("ModelMap");
		params.add(param);
		paginequery.setExceptions(exceptions);		
		paginequery.setParams(params);
		 
		setMethodBody(paginequery,Constant.paginequery,entityName,"conditions",this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(paginequery);
		
		Method query = new Method();//定义获取方法
		query.setMethodname("queryList"+entityName+"s");
		query.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		
		
		param.setType(this.conditionEntityName);
		param.setName("conditions");
		params.add(param);
		param = new MethodParam();
		param.setName("model");
		param.setType("ModelMap");
		params.add(param);
		query.setExceptions(exceptions);		
		query.setParams(params);
		 
		setMethodBody(query,Constant.query,entityName,"conditions",this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(query);
		
		Method toUpdate = new Method();//定义获取方法
		toUpdate.setMethodname("toUpdate"+entityName);
		toUpdate.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		
		if(primaryField != null)
		{
			param.setType(primaryField.getType());
			param.setName(primaryField.getFieldName());
		}
		else
		{
			param.setType("String");
			param.setName("id");
		}
	
		params.add(param);
		param = new MethodParam();
		param.setName("model");
		param.setType("ModelMap");
		params.add(param);
		toUpdate.setExceptions(exceptions);		
		toUpdate.setParams(params);
		
		setMethodBody(toUpdate,Constant.toUpdate,entityName,defaultSort,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(toUpdate);
		
		Method toAdd = new Method();//定义获取方法
		toAdd.setMethodname("toAdd"+entityName);
		toAdd.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		
		toAdd.setParams(params);
		
		setMethodBody(toAdd,Constant.toAdd,entityName,defaultSort,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(toAdd);
		
		Method index = new Method();//定义获取方法
		index.setMethodname("index");
		index.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		
		index.setParams(params);
		
		setMethodBody(index,Constant.index,entityName,defaultSort,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(index);
//		Method count = new Method();//定义获取方法
//		query.setMethodname("queryList"+entityName+"s");
//		query.setReturntype("List<"+entityName+">");		
//		params = new ArrayList<MethodParam>();
//		param = new MethodParam();
//		
//		
//		param.setType("Map");
//		param.setName("conditions");
//		params.add(param);
//		query.setExceptions(exceptions);		
//		query.setParams(params);
//		methods.add(query);
		return methods;
	}
	private List<Method> getMethods(int classtype ) throws Exception {
		if(classtype == Constant.component_type_actionimpl || classtype == Constant.component_type_actioninf)
			return getActionMethods(classtype );
		List<String> exceptions = new ArrayList<String>();
		exceptions.add(exceptionName);
		
		List<Method> methods = new ArrayList<Method>();
		Method add = new Method();//定义添加方法
		add.setMethodname("add"+entityName);
		add.setReturntype("void");
		
		add.setExceptions(exceptions);
		List<MethodParam> params = new ArrayList<MethodParam>();
		MethodParam param = new MethodParam();
		param.setType(this.entityName);
		param.setName(this.entityParamName);
		params.add(param);
		add.setParams(params);
		if(classtype == Constant.component_type_serivceimpl)
		{
			setMethodBody(add,Constant.add,entityName,entityParamName,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
			sqls = new ArrayList<SQL>();
			SQL sql = new SQL();
			sql.setOptype(Constant.add);
			sql.setName("add" + this.entityName);
			evalsql(sql);
			this.sqls.add(sql);
		}
		
		methods.add(add);		
		
		Method delete = new Method();//定义删除方法
		delete.setMethodname("delete"+entityName);
		delete.setReturntype("void");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		if(primaryField != null)
		{
			param.setType(primaryField.getType());
			param.setName(primaryField.getFieldName());
		}
		else
		{
			param.setType("String");
			param.setName("id");
		}
		params.add(param);
		delete.setExceptions(exceptions);		
		delete.setParams(params);
		if(classtype == Constant.component_type_serivceimpl)
		{
			setMethodBody(delete,Constant.delete,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
			SQL sql = new SQL();
			sql.setOptype(Constant.delete);
			sql.setName("deleteByKey");
			evalsql(sql);
			this.sqls.add(sql);
		}
		
		methods.add(delete);
		
		Method deletebatch = new Method();//定义删除方法
		deletebatch.setMethodname("deleteBatch"+entityName);
		deletebatch.setReturntype("void");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		if(primaryField != null)
		{
			param.setType(primaryField.getType() +"...");
			param.setName(primaryField.getFieldName()+"s");
		}
		else
		{
			param.setType("String...");
			param.setName("ids");
		}
		params.add(param);
		deletebatch.setExceptions(exceptions);		
		deletebatch.setParams(params);
		if(classtype == Constant.component_type_serivceimpl)
			setMethodBody(deletebatch,Constant.deletebatch,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
		 
		
		methods.add(deletebatch);
		
		Method update = new Method();//定义修改方法
		update.setMethodname("update"+entityName);
		update.setReturntype("void");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		param.setType(this.entityName);
		param.setName(this.entityParamName);
		params.add(param);
		update.setExceptions(exceptions);		
		update.setParams(params);
		
		if(classtype == Constant.component_type_serivceimpl)
		{
			setMethodBody(update,Constant.update,entityName,entityParamName,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
			SQL sql = new SQL();
			sql.setOptype(Constant.update);
			sql.setName("update"+this.entityName);
			evalsql(sql);
			this.sqls.add(sql);
		}
		
		methods.add(update);
		
		Method get = new Method();//定义获取方法
		get.setMethodname("get"+entityName);
		get.setReturntype(entityName);		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		
		if(primaryField != null)
		{
			param.setType(primaryField.getType());
			param.setName(primaryField.getFieldName());
		}
		else
		{
			param.setType("String");
			param.setName("id");
		}
		params.add(param);
		get.setExceptions(exceptions);		
		get.setParams(params);
		
		if(classtype == Constant.component_type_serivceimpl)
		{
			setMethodBody(get,Constant.get,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
			SQL sql = new SQL();
			sql.setOptype(Constant.get);
			sql.setName("selectById");
			evalsql(sql);
			this.sqls.add(sql);
		}
		
		methods.add(get);
		
		Method paginequery = new Method();//定义获取方法
		paginequery.setMethodname("queryListInfo"+entityName+"s");
		paginequery.setReturntype("ListInfo");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		
		
		param.setType(this.conditionEntityName);
		param.setName("conditions");
		params.add(param);
		
		param = new MethodParam();
		param.setType("long");
		param.setName("offset");
		params.add(param);
		param = new MethodParam();
		param.setType("int");
		param.setName("pagesize");
		params.add(param);
		paginequery.setExceptions(exceptions);		
		paginequery.setParams(params);
		 
		if(classtype == Constant.component_type_serivceimpl)
		{
			setMethodBody(paginequery,Constant.paginequery,entityName,"conditions",this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
			SQL sql = new SQL();
			sql.setOptype(Constant.paginequery);
			sql.setName("queryList"+this.entityName);
			evalsql(sql);
			this.sqls.add(sql);
		}
		
		
		methods.add(paginequery);
		
		Method query = new Method();//定义获取方法
		query.setMethodname("queryList"+entityName+"s");
		query.setReturntype("List<"+entityName+">");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		
		
		param.setType(this.conditionEntityName);
		param.setName("conditions");
		params.add(param);
		query.setExceptions(exceptions);		
		query.setParams(params);
		 
		if(classtype == Constant.component_type_serivceimpl)
		{
			setMethodBody(query,Constant.query,entityName,"conditions",this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
		}
		
		methods.add(query);
		
//		Method count = new Method();//定义获取方法
//		query.setMethodname("queryList"+entityName+"s");
//		query.setReturntype("List<"+entityName+">");		
//		params = new ArrayList<MethodParam>();
//		param = new MethodParam();
//		
//		
//		param.setType("Map");
//		param.setName("conditions");
//		params.add(param);
//		query.setExceptions(exceptions);		
//		query.setParams(params);
//		methods.add(query);
		return methods;
	}
	
	private void evalsql(SQL sql) {
		SQLBuilder.buidlersql(sql);
		
	}

	public static void writFile(  VelocityContext context,Template tempalte,File file,String charset)throws Exception
	{
		FileOutputStream out = null;
		 OutputStreamWriter writer = null;
		 try
		 {
			 out = new FileOutputStream(file);
			 writer = new OutputStreamWriter(out,charset);
			 tempalte.merge(context, writer);
			 writer.flush();
		 }
		 catch(Exception e)
		 {
			 throw e;
		 }
		 finally
		 {
			 if(out != null)
				try {
					out.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 if(writer != null)
				try {
					writer.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 }
		 
	}
	
	public static String writetostring(  VelocityContext context,Template tempalte,String charset)throws Exception
	{
		
		StringWriter writer = null;
		 try
		 {
			  writer = new StringWriter();
			 
			 tempalte.merge(context, writer);
			 writer.flush();
			 return writer.toString();
		 }
		 catch(Exception e)
		 {
			 throw e;
		 }
		 finally
		 {
			 			 if(writer != null)
				try {
					writer.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 }
		 
	}

	private void genException(String exceptionJavaName,String date,String version,String author,String company,String description,File exception) throws Exception
	{
		 
		 List<String> imports = null;
		 Template serviceinftempalte = VelocityUtil.getTemplate("gencode/java/exception.vm");
		 VelocityContext context = new VelocityContext();
		 context.put("fields", null);
		 String entityPackageInfo = this.moduleMetaInfo.getPackagePath() + "." + this.moduleMetaInfo.getModuleName()+".service";
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", exceptionJavaName);
		 context.put("description", description);
		 context.put("company", company);
		 context.put("gendate", date);
		 context.put("author", author);
		 context.put("version", version);
		 writFile(context,serviceinftempalte,exception,this.moduleMetaInfo.getEncodecharset());
		 
		
	}
	
	private List<String> evalImport(List<Field> fields,boolean condition) {
		List<String> imports = new ArrayList<String>();
		for(Field f:fields)
		{
	         if(f.getType().equals("Timestamp"))
	         {
	        	 if(!imports.contains("java.sql.Timestamp"))
	        		 imports.add("java.sql.Timestamp");
	         }
	         
		}
		if(!condition && this.primaryField != null)
		{
			imports.add("com.frameworkset.orm.annotation.PrimaryKey");
		}
		return imports;
	}
	private List<String> evalServiceInfImport( ) {
		List<String> imports = new ArrayList<String>();
		imports.add(this.moduleMetaInfo.getPackagePath() + "." + this.moduleMetaInfo.getModuleName()+".entity.*");
		imports.add("com.frameworkset.util.ListInfo");
		imports.add("java.util.List");		
		imports.add("java.util.Map");
		return imports;
	}
	
	private List<String> evalServiceImplImport( ) {
		List<String> imports = new ArrayList<String>();
		imports.add(this.moduleMetaInfo.getPackagePath() + "." + this.moduleMetaInfo.getModuleName()+".entity.*");
		imports.add("com.frameworkset.util.ListInfo");
		imports.add("com.frameworkset.common.poolman.ConfigSQLExecutor");
		imports.add("org.apache.log4j.Logger");		
		imports.add("java.util.List");
		imports.add("java.util.ArrayList");
		imports.add("java.util.Map");
		imports.add("java.util.HashMap");
		imports.add("com.frameworkset.orm.transaction.TransactionManager");
		
		return imports;
	}
	
	private List<String> evalActionImplImport( ) {
		List<String> imports = new ArrayList<String>();
		imports.add(this.moduleMetaInfo.getPackagePath() + "." + this.moduleMetaInfo.getModuleName()+".entity.*");
		imports.add("com.frameworkset.util.ListInfo");
		imports.add("org.apache.log4j.Logger");		
		imports.add("java.util.List");
		imports.add("java.util.Map");
		imports.add("com.frameworkset.util.StringUtil");
		imports.add("org.frameworkset.demo." + this.moduleMetaInfo.getModuleName()+".service.*");
		imports.add("org.frameworkset.util.annotations.ResponseBody");
		imports.add("org.frameworkset.web.servlet.ModelMap");
		imports.add("org.frameworkset.util.annotations.PagerParam");
		imports.add("org.frameworkset.util.annotations.MapKey");
		/**
		 * import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.demo.appbom.entity.AppBom;
import org.frameworkset.demo.appbom.service.AppBomException;
import org.frameworkset.demo.appbom.service.AppBomService;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
		 */
		return imports;
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

	public List<Field> getConditions() {
		return conditions;
	}

	public List<Field> getAllfields() {
		return allfields;
	}

	public ModuleMetaInfo getModuleMetaInfo() {
		return moduleMetaInfo;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getEntityParamName() {
		return entityParamName;
	}

	public Field getPrimaryField() {
		return primaryField;
	}

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public List<SortField> getSortFields() {
		return sortFields;
	}

	public void setSortFields(List<SortField> sortFields) {
		this.sortFields = sortFields;
	}
	
	public void addCondition(ConditionField cf)
	{
		if(this.conditions == null)
		{
			conditions = new ArrayList<Field>();
		}
		conditions.add(cf);
	}
	
	public void addSortField(SortField sf)
	{
		if(this.sortFields == null)
		{
			sortFields = new ArrayList<SortField>();
		}
		sortFields.add(sf);
	}

	public File getMvcConfDir() {
		return mvcConfDir;
	}


	
	public String getServiceClass() {
		// TODO Auto-generated method stub
		return serviceClass;
	}
	
	public String getControlClass() {
		// TODO Auto-generated method stub
		return controlClass;
	}

	public String getSqlfilepath() {
		return sqlfilepath;
	}

	public File getMvcconf() {
		return mvcconf;
	}

	public void setMvcconf(File mvcconf) {
		this.mvcconf = mvcconf;
	}

	public String getConditionEntityName() {
		return conditionEntityName;
	}

	public void setConditionEntityName(String conditionEntityName) {
		this.conditionEntityName = conditionEntityName;
	}

	public String getConditionEntityParamName() {
		return conditionEntityParamName;
	}

	public void setConditionEntityParamName(String conditionEntityParamName) {
		this.conditionEntityParamName = conditionEntityParamName;
	}

}
