package org.frameworkset.gencode.web.action;

import java.util.Set;

import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.sql.TableMetaData;

public class TableSet {

	public TableSet() {
		// TODO Auto-generated constructor stub
	}
	
	public String tableset(ModelMap model)
	{
		Set<TableMetaData> tableMetas =   DBUtil.getTableMetaDatas("bspf");
		model.addAttribute("tables", tableMetas);
		model.addAttribute("dbname", "bspf");
		return "path:tableset";
	}
	
	public String tablemeta(String dbname,String tableName,ModelMap model)
	{
		TableMetaData tableMeta =   DBUtil.getTableMetaData(dbname,tableName);
		model.addAttribute("table", tableMeta);
		return "path:tableconfig";
	}

}
