package org.frameworkset.gencode.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.frameworkset.gencode.core.GencodeServiceImpl;
import org.frameworkset.gencode.entity.Field;
import org.frameworkset.gencode.web.entity.ControlInfo;
import org.frameworkset.gencode.web.entity.FieldInfo;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.sql.TableMetaData;

public class GencodeController {

	
	public String selecttable(ModelMap model)
	{
		List<String> dbs = DBUtil.getAllPoolNames();
		model.addAttribute("dbs", dbs);
		Set<TableMetaData> tableMetas =   DBUtil.getTableMetaDatas("bspf");
		List<String> tables = new ArrayList<String>();
		if(tableMetas != null)
		{
			for(TableMetaData meta :tableMetas)
			{
				tables.add(meta.getTableName());
			}
		}
		model.addAttribute("tables", tables);
		return "path:selecttable";
	}
	
	
	
	public String tableconfig(String dbname,String tableName,String formid,ModelMap model)
	{
		if(tableName == null)
			return "path:tableconfig";
		TableMetaData tableMeta =   DBUtil.getTableMetaData(dbname,tableName);
		model.addAttribute("table", tableMeta);
		model.addAttribute("tableName", tableName);
		model.addAttribute("dbname", dbname);
		List<Field> fields = GencodeServiceImpl.getSimpleFields(tableMeta);
		if(formid != null)
		{
			
		}
		model.addAttribute("fields", fields);
		return "path:tableconfig";
	}
	
	public @ResponseBody Map gencode(ControlInfo controlInfo,List<FieldInfo> fields)
	{
		Map ret = new HashMap();
		ret.put("result", "success");
		return ret;
	}
	
	public String index(ModelMap model)
	{
//		Set<TableMetaData> tableMetas =   DBUtil.getTableMetaDatas("bspf");
//		model.addAttribute("tables", tableMetas);
		
		return "path:index";
	}
	
	/**
	 * 表单列表查询，配置历史
	 * @param model
	 * @return
	 */
	public String formlist(ModelMap model)
	{
		return "path:formlist";
	}

}
