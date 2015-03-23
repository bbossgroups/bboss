package org.frameworkset.gencode.web.action;

import java.util.Set;

import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.sql.TableMetaData;

public class TableSet {

	public TableSet() {
		// TODO Auto-generated constructor stub
	}
	
	public String tableset(String dbname,String tableName,ModelMap model)
	{
		Set<TableMetaData> tableMetas =   DBUtil.getTableMetaDatas("bspf");
		model.addAttribute("tables", tableMetas);
		if(tableName != null)
		{
			model.addAttribute("dbname", dbname);
			model.addAttribute("tableName", tableName);
			TableMetaData tableMeta =   DBUtil.getTableMetaData(dbname,tableName);
			model.addAttribute("tablemeta", tableMeta);
		}
		return "path:tableset";
	}
	
	public String tablemeta(String dbname,String tableName,ModelMap model)
	{
		if(tableName == null)
			return "path:tableconfig";
		TableMetaData tableMeta =   DBUtil.getTableMetaData(dbname,tableName);
		model.addAttribute("table", tableMeta);
		model.addAttribute("dbname", dbname);
		return "path:tableconfig";
	}

}
