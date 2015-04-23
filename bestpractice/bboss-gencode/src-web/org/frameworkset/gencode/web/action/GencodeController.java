package org.frameworkset.gencode.web.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.gencode.core.GencodeServiceImpl;
import org.frameworkset.gencode.entity.Field;
import org.frameworkset.gencode.web.entity.ControlInfo;
import org.frameworkset.gencode.web.entity.FieldInfo;
import org.frameworkset.gencode.web.entity.Gencode;
import org.frameworkset.gencode.web.entity.GencodeCondition;
import org.frameworkset.gencode.web.service.GencodeException;
import org.frameworkset.gencode.web.service.GencodeService;
import org.frameworkset.soa.ObjectSerializable;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.common.poolman.sql.TableMetaData;
import com.frameworkset.orm.engine.EngineException;
import com.frameworkset.orm.engine.model.NameFactory;
import com.frameworkset.orm.engine.model.NameGenerator;
import com.frameworkset.util.ListInfo;
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
	public static List<FieldInfo> getSimpleFields(TableMetaData tableMeta)
	{
		Set<ColumnMetaData> columns = tableMeta.getColumns();
		if(columns.size() > 0)
		{
			 
			List<FieldInfo> fs = new ArrayList<FieldInfo>();
			
			for(ColumnMetaData c:columns)
			{
				FieldInfo f = new FieldInfo();
				f.setType(GencodeServiceImpl.convertType(c.getSchemaType().getJavaType()));
				
				
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
	public String tableconfig(String dbname, String tableName,
			ModelMap model) {
		if (tableName == null)
			return "path:tableconfig";
		TableMetaData tableMeta = DBUtil.getTableMetaData(dbname, tableName);
		model.addAttribute("table", tableMeta);
		model.addAttribute("tableName", tableName);
		model.addAttribute("dbname", dbname);
		List<FieldInfo> fields = getSimpleFields(tableMeta);

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

	public @ResponseBody
	Map<String, String> gencode(ControlInfo controlInfo, List<FieldInfo> fields,String gencodeid) {
		Map<String, String> ret = new HashMap<String, String>();
		_tempsave(  controlInfo,   fields, gencodeid, ret);
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
