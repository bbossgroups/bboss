package org.frameworkset.spi.assemble.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.frameworkset.spi.CallContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProList;
import org.frameworkset.spi.assemble.ProMap;

public class ExcelIOCPlugin implements IocPlugin<ProMap, Map<String, ExcelTemplate>>{

	@Override
	public Map<String, ExcelTemplate> ioc(ProMap promap, CallContext callcontext) {
		Map<String, ExcelTemplate> excelTemplates = new HashMap<String, ExcelTemplate>();
        Set<Map.Entry<String, Pro>> entries = promap.entrySet();
        Iterator<Map.Entry<String, Pro>> it = entries.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Pro> e = it.next();
            String name = e.getKey();
            Pro pro = e.getValue();
            String templatepath = pro.getStringExtendAttribute("templatepath");

            ExcelTemplate template = new ExcelTemplate();
            template.setName(name);
            template.setStarrow(pro.getIntExtendAttribute("statrow", 1));
            template.setEndrow(pro.getIntExtendAttribute("endrow", -1));
            template.setTemplatepath(templatepath);
            excelTemplates.put(name, template);

            List<ExcelCell> cellList = new ArrayList<ExcelCell>();
            template.setCells(cellList);

            ProList templateCellList = pro.getList();

            for (int i = 0; i < templateCellList.size(); i++) {

                Pro cellPro = (Pro) templateCellList.get(i);

                ExcelCell cellBean = new ExcelCell();
                cellBean.setCellpostion(cellPro.getIntExtendAttribute("cellpostion"));
                cellBean.setCellname(cellPro.getStringExtendAttribute("javaFiledName"));
                cellBean.setCelltype(cellPro.getStringExtendAttribute("celltype"));
                cellList.add(cellBean);
            }
        }
        return excelTemplates;
	}

}
