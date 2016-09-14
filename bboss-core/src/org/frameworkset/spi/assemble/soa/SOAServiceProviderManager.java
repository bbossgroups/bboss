package org.frameworkset.spi.assemble.soa;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.LinkConfigFile;
import org.frameworkset.spi.assemble.ProviderParser;
import org.frameworkset.spi.assemble.ServiceProviderManager;

/**
 * 
 * @author yinbp
 *
 */
public class SOAServiceProviderManager extends ServiceProviderManager{

	public SOAServiceProviderManager(BaseApplicationContext applicationContext, String charset) {
		super(applicationContext, charset);
		// TODO Auto-generated constructor stub
	}

	public SOAServiceProviderManager(BaseApplicationContext applicationContext) {
		super(applicationContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public  void destroy() {
		// TODO Auto-generated method stub
		super._destroy();
	}
	
	protected ProviderParser _buildProviderParser()
    {
    	return new SOAProviderParser(this.getApplicationContext());
    }
	
	protected ProviderParser _buildProviderParser(String url,LinkConfigFile linkconfigFile)
    {
    	return new SOAProviderParser(this.getApplicationContext(),url, linkconfigFile);
    }

}
