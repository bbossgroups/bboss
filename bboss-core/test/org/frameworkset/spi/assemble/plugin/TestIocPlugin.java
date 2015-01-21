package org.frameworkset.spi.assemble.plugin;

import java.io.IOException;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Test;

public class TestIocPlugin {
	@Test
	public void testiocplugin() throws IOException
	{
		  BaseApplicationContext excelMapping = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/assemble/plugin/excelFieldInfo.xml");
		  POIExcelService serice = excelMapping.getTBeanObject("POIExcelService", POIExcelService.class);
		  serice.parseHSSFMapList(null, null,"exceltemplatefile1");
		  
	}

}
