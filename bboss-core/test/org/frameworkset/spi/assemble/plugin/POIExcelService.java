package org.frameworkset.spi.assemble.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
/**
 * 本实例演示ioc注入插件的使用方法，通过ioc注入插件来扩展ioc依赖注入功能，实现自己的ioc依赖注入功能
 * @author yinbp
 *
 */
public class POIExcelService {
	/**
	 * 存储各种execl模板信息，模板的映射信息全部都设置在ioc配置文件中
	 */
	 private Map<String, ExcelTemplate> excelTemplates;
	 
    /**
     * 根据传入的excel流，将其中的数据根据对应的excel模板信息类将数据解析出来，形成beanType对象记录列表返回
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public <T> List<T> parseHSSFMapList(InputStream in,Class<T> beanType,String templateName) throws IOException 
    {
    	ExcelTemplate template = excelTemplates.get(templateName);
    	List<T> datas = null;
//    	datas = ....;
    	return datas;
    }
}
