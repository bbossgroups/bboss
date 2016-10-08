package org.frameworkset.web.servlet.context;

import java.util.List;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.LinkConfigFile;
import org.frameworkset.spi.assemble.ManagerImport;
import org.frameworkset.spi.assemble.ManagerImportWrapper;
import org.frameworkset.spi.assemble.ServiceProviderManager;

public class WebServiceManagerProvider extends ServiceProviderManager{

	public WebServiceManagerProvider(BaseApplicationContext applicationContext, String charset) {
		super(applicationContext, charset);
		// TODO Auto-generated constructor stub
	}

	public WebServiceManagerProvider(BaseApplicationContext applicationContext) {
		super(applicationContext);
		// TODO Auto-generated constructor stub
	}
	
	 protected ManagerImportWrapper sortManagerImports(List<ManagerImport> mis)
    {
		 ManagerImportWrapper mi = new ManagerImportWrapper();
		 mi.setImports(mis);
		 if(mis == null || mis.size() == 0)
		 {
			 
			 return mi;
		 }
		 ManagerImport imp = null;
		 int position = -1;
		 for(int i = 0; i < mis.size(); i ++)
		 {
			 imp = mis.get(i);
			 if(imp.getFile().endsWith("bboss-mvc.xml"))
			 {
				 position = i;
				 break;
			 }
		 }
		 if(position >= 0)
		 {
			 if(position > 0)
			 {
				 mis.remove(position);
				 mis.add(0, imp);
			 }
			 LinkConfigFile linkconfigFile = new WebLinkConfigFile(null, imp.getFile(), null);
			 mi.setParent(linkconfigFile);
		 }
		 return mi;
    }

}
