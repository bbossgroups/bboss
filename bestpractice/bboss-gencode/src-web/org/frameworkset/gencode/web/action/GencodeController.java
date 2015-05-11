package org.frameworkset.gencode.web.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.gencode.core.GencodeServiceImpl;
import org.frameworkset.gencode.core.Util;
import org.frameworkset.gencode.entity.AnnoParam;
import org.frameworkset.gencode.entity.Annotation;
import org.frameworkset.gencode.entity.ConditionField;
import org.frameworkset.gencode.entity.ControlInfo;
import org.frameworkset.gencode.entity.Field;
import org.frameworkset.gencode.entity.FieldInfo;
import org.frameworkset.gencode.entity.ModuleMetaInfo;
import org.frameworkset.gencode.entity.SortField;
import org.frameworkset.gencode.web.entity.Gencode;
import org.frameworkset.gencode.web.entity.GencodeCondition;
import org.frameworkset.gencode.web.service.GencodeException;
import org.frameworkset.gencode.web.service.GencodeService;
import org.frameworkset.soa.ObjectSerializable;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.sql.TableMetaData;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.StringUtil;

public class GencodeController {
	private static Logger log = Logger.getLogger(GencodeController.class);
	private GencodeService gencodeService;

	public @ResponseBody
	String deleteGencode(String id) {
		try {
			gencodeService.deleteGencode(id);
			return "success";
		} catch (GencodeException e) {
			log.error("delete Gencode failed:", e);
			return StringUtil.formatBRException(e);
		} catch (Throwable e) {
			log.error("delete Gencode failed:", e);
			return StringUtil.formatBRException(e);
		}

	}

	public @ResponseBody
	String deleteBatchGencode(String... ids) {
		try {
			gencodeService.deleteBatchGencode(ids);
			return "success";
		} catch (Throwable e) {
			log.error("delete Batch ids failed:", e);
			return StringUtil.formatBRException(e);
		}

	}

	public @ResponseBody
	String updateGencode(Gencode gencode) {
		try {
			gencodeService.updateGencode(gencode);
			return "success";
		} catch (Throwable e) {
			log.error("update Gencode failed:", e);
			return StringUtil.formatBRException(e);
		}

	}

	public String getGencode(String id, ModelMap model) throws GencodeException {
		try {
			Gencode gencode = gencodeService.getGencode(id);
			model.addAttribute("gencode", gencode);
			return "path:getGencode";
		} catch (GencodeException e) {
			throw e;
		} catch (Throwable e) {
			throw new GencodeException("get Gencode failed::id=" + id, e);
		}

	}

	public String queryListInfoGencodes(
			GencodeCondition conditions,
			@PagerParam(name = PagerParam.SORT, defaultvalue = "CREATETIME") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "true") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			ModelMap model) throws GencodeException {
		try {
			if (sortKey != null && !sortKey.equals("")) {
				conditions.setSortKey(sortKey);
				conditions.setSortDesc(desc);
			}
			String tablename = conditions.getTablename();
			if (tablename != null && !tablename.equals("")) {
				conditions.setTablename("%" + tablename + "%");
			}
			String author = conditions.getAuthor();
			if (author != null && !author.equals("")) {
				conditions.setAuthor("%" + author + "%");
			}
			ListInfo gencodes = gencodeService.queryListInfoGencodes(
					conditions, offset, pagesize);
			model.addAttribute("gencodes", gencodes);
			return "path:queryListInfoGencodes";
		} catch (GencodeException e) {
			throw e;
		} catch (Exception e) {
			throw new GencodeException("pagine query Gencode failed:", e);
		}

	}

	public String queryListGencodes(GencodeCondition conditions, ModelMap model)
			throws GencodeException {
		try {

			String tablename = conditions.getTablename();
			if (tablename != null && !tablename.equals("")) {
				conditions.setTablename("%" + tablename + "%");
			}
			String author = conditions.getAuthor();
			if (author != null && !author.equals("")) {
				conditions.setAuthor("%" + author + "%");
			}
			List<Gencode> gencodes = gencodeService
					.queryListGencodes(conditions);
			model.addAttribute("gencodes", gencodes);
			return "path:queryListGencodes";
		} catch (GencodeException e) {
			throw e;
		} catch (Exception e) {
			throw new GencodeException("query Gencode failed:", e);
		}

	}

	public @ResponseBody
	List<String> loadtables(String dbname) {
		Set<TableMetaData> tableMetas = DBUtil.getTableMetaDatas(dbname);
		List<String> tables = new ArrayList<String>();
		if (tableMetas != null) {
			for (TableMetaData meta : tableMetas) {
				tables.add(meta.getTableName());
			}
		}
		return tables;
	}

	public String selecttable(ModelMap model,GencodeCondition conditions) {
		List<String> dbs = DBUtil.getAllPoolNames();
		model.addAttribute("dbs", dbs);
		Set<TableMetaData> tableMetas = DBUtil.getTableMetaDatas("bspf");
		List<String> tables = new ArrayList<String>();
		if (tableMetas != null) {
			for (TableMetaData meta : tableMetas) {
				tables.add(meta.getTableName());
			}
		}
		String tablename = conditions.getTablename();
		if (tablename != null && !tablename.equals("")) {
			conditions.setTablename("%" + tablename + "%");
		}
		String author = conditions.getAuthor();
		if (author != null && !author.equals("")) {
			conditions.setAuthor("%" + author + "%");
		}
		List<Gencode> gencodes = gencodeService
				.queryListGencodes(conditions);
		model.addAttribute("gencodes", gencodes);
		model.addAttribute("tables", tables);
		return "path:selecttable";
	}
	
	public String tableconfig(String dbname, String tableName,
			ModelMap model) {
		if (tableName == null)
			return "path:tableconfig";
		TableMetaData tableMeta = DBUtil.getTableMetaData(dbname, tableName);
		model.addAttribute("table", tableMeta);
		model.addAttribute("tableName", tableName);
		model.addAttribute("dbname", dbname);
		List<FieldInfo> fields = Util.getSimpleFields(tableMeta);

		model.addAttribute("fields", fields);
		return "path:tableconfig";
	}
	
	public String tablereconfig(String gencodeid,ModelMap model) {
		if (gencodeid == null)
			return "path:tableconfig";
		Gencode gencode = gencodeService.getGencode(gencodeid);
		if(gencode == null)
			return "path:tableconfig";
		model.addAttribute("gencode", gencode);
		model.addAttribute("tableName", gencode.getTablename());
		model.addAttribute("dbname", gencode.getDbname());
		ControlInfo controlInfo = ObjectSerializable.toBean(gencode.getControlparams(), ControlInfo.class); 
		@SuppressWarnings("unchecked")
		List<FieldInfo> fields = ObjectSerializable.toBean(gencode.getFieldinfos(), List.class); 
//		List<Field> fields = GencodeServiceImpl.getSimpleFields(tableMeta);
		
		model.addAttribute("fields", fields);
		model.addAttribute("gencodeid", gencodeid);
		
		model.addAttribute("controlparams", controlInfo);
		return "path:tableconfig";
	}

	private void handleConditionFields(GencodeServiceImpl gencodeService,List<FieldInfo> fields)
	{
		for(int i = 0; fields != null && i < fields.size(); i ++)
		{
			FieldInfo fieldInfo = fields.get(i);
			if(fieldInfo.getQcondition() == 1)
			{
				ConditionField bm = new ConditionField();
				this.convertField(gencodeService, fieldInfo, bm,Util.other);
				bm.setColumnname(fieldInfo.getColumnname());
				bm.setLike(fieldInfo.getQtype() == 1);
//				bm.setOr(true);
				gencodeService.addCondition(bm);
			}
		}
	}
	
	private void handleSortFields(GencodeServiceImpl gencodeService,List<FieldInfo> fields)
	{
		for(int i = 0; fields != null && i < fields.size(); i ++)
		{
			FieldInfo fieldInfo = fields.get(i);
			if(fieldInfo.getSfield() == 1)
			{
				SortField id = new SortField();
//				id.setColumnname(fieldInfo.getColumnname());
				id.setDesc(fieldInfo.getStype() == 1);
				this.convertField(gencodeService, fieldInfo, id, Util.other);
				if(i == 0)
				{
					id.setDefaultSortField(true);
					gencodeService.setDefaultSortField(id);
				}
				
				gencodeService.addSortField(id);	
			}
		}
	}

	private String extendType(String type)
	{
		if(type.equals("url")||
			type.equals("creditcard")||
			type.equals("email")||
			type.equals("file")||
			type.equals("idcard")||
			type.equals("textarea")||
			type.equals("htmleditor")||
			type.equals("word")||
			type.equals("excel")||
			type.equals("ppt")||
			type.equals("fuction"))
		{
			return "String";
		}
		else
			return type;
	}
	
	private Field convertField(GencodeServiceImpl gencodeService,FieldInfo fieldInfo,Field f,int pagetype)
	{
		f.setType(extendType(fieldInfo.getType()));
		f.setExtendType(fieldInfo.getType());
		f.setFieldCNName(fieldInfo.getFieldCNName());
		if(gencodeService.isGenI18n())
		{
			f.setFieldAsciiCNName(SimpleStringUtil.native2ascii(fieldInfo.getFieldCNName()));
		}
		
		f.setPk(fieldInfo.isPk());
		boolean isp = false;
		if(gencodeService.getModuleMetaInfo().isAutogenprimarykey() && f.isPk())
		{
			Annotation anno = new Annotation();
			anno.setName("PrimaryKey");
			if(SimpleStringUtil.isNotEmpty(gencodeService.getModuleMetaInfo().getPkname()))
			{
				anno.addAnnotationParam("pkname", gencodeService.getModuleMetaInfo().getPkname(),AnnoParam.V_STRING);
			}
			f.addAnnotation(anno);
			isp = true;
			gencodeService.addEntityImport("com.frameworkset.orm.annotation.PrimaryKey");
		}
		if(fieldInfo.getColumntype().equals("CLOB"))
		{
			Annotation anno = new Annotation();
			anno.setName("Column");
			anno.addAnnotationParam("type", "clob",AnnoParam.V_STRING);
			f.addAnnotation(anno);
			gencodeService.addEntityImport("com.frameworkset.orm.annotation.Column");
		}
		else if(fieldInfo.getColumntype().equals("BLOB"))
		{
			Annotation anno = new Annotation();
			anno.setName("Column");
			anno.addAnnotationParam("type", "blob",AnnoParam.V_STRING);
			gencodeService.addEntityImport("com.frameworkset.orm.annotation.Column");
			f.addAnnotation(anno);
		}
		
		else if(fieldInfo.getColumntype().startsWith("TIMESTAMP") || fieldInfo.getColumntype().equals("DATE"))
		{
			if(fieldInfo.getDateformat() != null && !fieldInfo.getDateformat().equals(""))
			{
				Annotation anno = new Annotation();
				anno.setName("RequestParam");
				anno.addAnnotationParam("dateformat", fieldInfo.getDateformat(),AnnoParam.V_STRING);
				
				gencodeService.addEntityImport("org.frameworkset.util.annotations.RequestParam");
				
				f.addAnnotation(anno);
			}
			if(f instanceof ConditionField)
			{
				if(fieldInfo.getColumntype().startsWith("TIMESTAMP"))
					gencodeService.addConditionImport("java.sql.Timestamp");
				else
					gencodeService.addConditionImport("java.sql.Date");
			}
			else
			{
				
				if(fieldInfo.getColumntype().startsWith("TIMESTAMP"))
					gencodeService.addEntityImport("java.sql.Timestamp");
				else
					gencodeService.addEntityImport("java.sql.Date");
			}
		}
		
		
         	 
    	 f.setMfieldName(fieldInfo.getMfieldName());
    	 f.setFieldName(fieldInfo.getFieldName());
    	 f.setColumnname(fieldInfo.getColumnname());
    	 
    	 f.setTypecheck(fieldInfo.getTypecheck() == 1);
    	 f.setDaterange(fieldInfo.getDaterange() == 1);
    	 f.setDateformat(fieldInfo.getDateformat());
    	 f.setNumformat(fieldInfo.getNumformat());
    	 
    	 f.setDefaultValue(fieldInfo.getDefaultValue());
    	 f.setReplace(fieldInfo.getReplace());
    	 f.setMaxlength(fieldInfo.getMaxlength());
    	 f.setMinlength(fieldInfo.getMinlength());
    	 if(Util.addpage == pagetype)
    	 {
	    	 f.setEditable(true);
	    	 f.setRequired(fieldInfo.getAddcontrolParams().contains("必填"));
    	 }
    	 else if(Util.editpage == pagetype)
    	 {
	    	 f.setEditable(fieldInfo.getEditcontrolParams().contains("编辑"));
	    	 f.setRequired(fieldInfo.getEditcontrolParams().contains("必填"));
    	 }
    	 
    	 if(isp)
    	 {
    		 gencodeService.setPrimaryField(f);
    		 gencodeService.setPrimaryKeyName(f.getFieldName());
//    		 fields.add(0, f);
    	 }
    	 else
    	 {
//    		 fields.add(f);
    	 }
         return f;
        
	}
	
	private void handleListFields(GencodeServiceImpl gencodeService,List<FieldInfo> fields)
	{
		List<Field> listShowFields = new ArrayList<Field>();
		List<Field> listHiddenFields = new ArrayList<Field>();
		for(int i = 0; fields != null && i < fields.size(); i ++)
		{
			FieldInfo fieldInfo = fields.get(i);
			String inlist = fieldInfo.getInlist();
		
			if(inlist != null && inlist.contains("包含"))
			{
				Field f = new Field();
				if(inlist.contains("隐藏"))
				{
					 f.setSortField(true);
					 f.setDesc(fieldInfo.getStype() == 1);
					convertField(gencodeService,fieldInfo,f,Util.listpage);
					listHiddenFields.add(f);
				}
				else
				{
					 f.setSortField(true);
					 f.setDesc(fieldInfo.getStype() == 1);
					convertField(gencodeService,fieldInfo,f,Util.listpage);
					if(f.isPk())
					{
						listShowFields.add(0, f);
					}
					else
					{
						listShowFields.add(f);
					}
				}
			}
			
			
			
			
		}
		gencodeService.setListHiddenFields(listHiddenFields);
		gencodeService.setListShowFields(listShowFields);
	}
	
	private void handleAddFields(GencodeServiceImpl gencodeService,List<FieldInfo> fields)
	{
		List<Field> addShowFields = new ArrayList<Field>();
		for(int i = 0; fields != null && i < fields.size(); i ++)
		{
			FieldInfo fieldInfo = fields.get(i);
			String inlist = fieldInfo.getInlist();
		
			if(inlist != null && inlist.contains("显示"))
			{
				Field f = new Field();
				convertField(gencodeService,fieldInfo,f ,Util.addpage);
				if(f.isPk())
				{
					addShowFields.add(0,f);
				}
				else
				{
					addShowFields.add(f);
				}
			}
			
		}
		gencodeService.setAddShowFields(addShowFields);
	}
	
	private void handleEntityFields(GencodeServiceImpl gencodeService,List<FieldInfo> fields)
	{
		List<Field> entityFields = new ArrayList<Field>();
		for(int i = 0; fields != null && i < fields.size(); i ++)
		{
			FieldInfo fieldInfo = fields.get(i);
			Field f = new Field();
			convertField(gencodeService,fieldInfo,f,Util.other);
			if(f.isPk())
				entityFields.add(0, f);
			else
				entityFields.add(f);
			
			
		}
		gencodeService.setEntityFields(entityFields);
	}
	
	private void handleEditorFields(GencodeServiceImpl gencodeService,List<FieldInfo> fields)
	{
		List<Field> editShowFields = new ArrayList<Field>();
		List<Field> editHiddenFields = new ArrayList<Field>();
		for(int i = 0; fields != null && i < fields.size(); i ++)
		{
			FieldInfo fieldInfo = fields.get(i);
			String inlist = fieldInfo.getInlist();
			Field f = new Field();
			if(inlist != null && inlist.contains("显示"))
			{
				convertField(gencodeService,fieldInfo,f,Util.editpage);
				if(f.isPk())
					editShowFields.add(0, f);
				else
					editShowFields.add(f);
				
			}
			else
			{
				convertField(gencodeService,fieldInfo,f,Util.editpage);
				editHiddenFields.add(f);
			}
			
		}
		gencodeService.setEditShowFields(editShowFields);
		gencodeService.setEditHiddenFields(editHiddenFields);
	}
	
	private void handleViewFields(GencodeServiceImpl gencodeService,List<FieldInfo> fields)
	{
		List<Field> viewShowFields = new ArrayList<Field>();
		List<Field> viewHiddenFields = new ArrayList<Field>();
		for(int i = 0; fields != null && i < fields.size(); i ++)
		{
			FieldInfo fieldInfo = fields.get(i);
			String inlist = fieldInfo.getInlist();
		
			if(inlist != null && inlist.contains("隐藏"))
			{
				Field f = new Field();
				convertField(gencodeService,fieldInfo,f,Util.viewpage);		
				viewHiddenFields.add(f);
			}
			else{
				Field f = new Field();
				convertField(gencodeService,fieldInfo,f,Util.viewpage);			
				if(f.isPk())
					viewShowFields.add(0,f);
				else
					viewShowFields.add(f);
			}			
		}
		gencodeService.setViewShowFields(viewShowFields);
		gencodeService.setViewHiddenFields(viewHiddenFields);
	}
	public @ResponseBody
	Map<String, String> gencode(ControlInfo controlInfo, List<FieldInfo> fields,String gencodeid) {
		Map<String, String> ret = new HashMap<String, String>();
		//先保存配置信息，成功后再生成代码
		_tempsave(  controlInfo,   fields, gencodeid, ret);
		
		GencodeServiceImpl gencodeService = new GencodeServiceImpl(true);
		ModuleMetaInfo moduleMetaInfo = new ModuleMetaInfo();
		moduleMetaInfo.setTableName(controlInfo.getTableName());//指定表名，根据表结构来生成所有的文件
		moduleMetaInfo.setPkname(controlInfo.getPkname());//设置oracle sequence名称，用来生成表的主键,对应TABLEINFO表中TABLE_NAME字段的值
		moduleMetaInfo.setSystem(controlInfo.getSystem());//lcjf,系统代码，如果指定了system，那么对应的jsp页面会存放到lcjf/area目录下面，对应的mvc组件装配文件存在在/WEB-INF/conf/lcjf下面，否则jsp在/area下，mvc组件装配文件存在在/WEB-INF/conf/area下
		moduleMetaInfo.setModuleName(controlInfo.getModuleName());//指定模块名称，源码和配置文件都会存放在相应模块名称的目录下面
		moduleMetaInfo.setModuleCNName(controlInfo.getModuleCNName());//指定模块中文名称
		moduleMetaInfo.setDatasourceName(controlInfo.getDbname());//指定数据源名称，在poolman.xml文件中配置
		moduleMetaInfo.setPackagePath(controlInfo.getPackagePath());//java程序对应的包路径
		
		moduleMetaInfo.setAutogenprimarykey(controlInfo.getControlParams().contains("autopk"));
		//moduleMetaInfo.setServiceName("AreaManagerService");
		moduleMetaInfo.setSourcedir(controlInfo.getSourcedir());//生成文件存放的物理目录，如果不存在，会自动创建
		moduleMetaInfo.setIgnoreEntityFirstToken(true); //忽略表的第一个下滑线签名的token，例如表名td_app_bom中，只会保留app_bom部分，然后根据这部分来生成实体、配置文件名称
		moduleMetaInfo.setAuthor(controlInfo.getAuthor());//程序作者
		moduleMetaInfo.setCompany(controlInfo.getCompany());//公司信息
		moduleMetaInfo.setVersion(controlInfo.getVersion());//版本信息
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date());
		moduleMetaInfo.setDate(date);//指定日期
		gencodeService.setGenI18n(controlInfo.getControlParams().contains("geni18n"));//生成国际化属性配置文件
		moduleMetaInfo.setClearSourcedir(controlInfo.getControlParams().contains("clearSourcedir"));//是否清空源码目录
		gencodeService.setExcelVersion(controlInfo.getExcelVersion());
		gencodeService.setExportExcel(gencodeService.getExcelVersion() != -1);
		gencodeService.setTheme(controlInfo.getTheme());//设置默认主题风格		
		gencodeService.setModuleMetaInfo(moduleMetaInfo);
		/************以下代码片段指定界面查询字段，以及查询条件组合方式、是否是模糊查询等*******/
		handleConditionFields(  gencodeService, fields);
//		ConditionField bm = new ConditionField();
//		bm.setColumnname("TABLENAME");
//		bm.setLike(true);
//		bm.setOr(true);
//		gencodeService.addCondition(bm);		
//		
//		ConditionField bm1 = new ConditionField();
//		bm1.setColumnname("AUTHOR");
//		bm1.setLike(true);
//		bm1.setOr(true);
//		gencodeService.addCondition(bm1);		

		/************以上代码片段指定界面查询字段，以及查询条件组合方式、是否是模糊查询等********/		
		/************以下代码片段指定界面排序字段**********************************/
		handleSortFields(  gencodeService,  fields);
//		SortField id = new SortField();
//		id.setColumnname("CREATETIME");
//		id.setDesc(true);
//		id.setDefaultSortField(true);
//		gencodeService.addSortField(id);	
		/************以上代码片段指定界面排序字段**********************************/		
		
		handleListFields(gencodeService,fields);
		handleAddFields(gencodeService, fields);
		handleEditorFields(gencodeService,fields);
		handleViewFields(gencodeService,fields);
		handleEntityFields(gencodeService,fields);
		gencodeService.genCode();//执行代码生成逻辑
		ret.put("result", "success");
		return ret;
		
	}

	public @ResponseBody
	Map<String, String> tempsave(ControlInfo controlInfo, List<FieldInfo> fields,String gencodeid)
	{
		Map<String, String> ret = new HashMap<String, String>();
		_tempsave(  controlInfo,   fields,  gencodeid, ret);
		return ret;
	}
	private void _tempsave(ControlInfo controlInfo, List<FieldInfo> fields,String gencodeid,Map<String, String> ret) {
		// 控制器
		
		try {
			
			Gencode gencode = new Gencode();
			gencode.setAuthor(controlInfo.getAuthor());
			gencode.setCompany(controlInfo.getCompany());
			gencode.setCreatetime(new Timestamp(System.currentTimeMillis()));
			gencode.setDbname(controlInfo.getDbname());
			gencode.setTablename(controlInfo.getTableName());
			gencode.setUpdatetime(gencode.getCreatetime());
			gencode.setControlparams(ObjectSerializable.toXML(controlInfo));
			gencode.setFieldinfos(ObjectSerializable.toXML(fields));
			if(gencodeid == null || gencodeid.equals(""))
			{
				gencodeService.addGencode(gencode);
				ret.put("gencodeid", gencode.getId());
			}
			else
			{
				gencode.setId(gencodeid);
				gencodeService.updateGencode(gencode);
				ret.put("gencodeid", gencode.getId());
			}
			ret.put("result", "success");
			

		} catch (GencodeException e) {
			log.error("add Gencode failed:", e);
			ret.put("result", StringUtil.formatBRException(e));
		} catch (Throwable e) {
			log.error("add Gencode failed:", e);
			ret.put("result", StringUtil.formatBRException(e));
		}

		
	}

	public String index(ModelMap model) {
		// Set<TableMetaData> tableMetas = DBUtil.getTableMetaDatas("bspf");
		// model.addAttribute("tables", tableMetas);

		return "path:index";
	}

	/**
	 * 表单列表查询，配置历史
	 * 
	 * @param model
	 * @return
	 */
	public String formlist(ModelMap model) {
		return "path:formlist";
	}

}
