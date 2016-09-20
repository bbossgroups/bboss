package org.frameworkset.web.servlet.context;

import org.frameworkset.spi.assemble.LinkConfigFile;
import org.frameworkset.spi.assemble.PropertiesContainer;

public class WebLinkConfigFile extends LinkConfigFile {

	public WebLinkConfigFile(String fullPath, String configFile, LinkConfigFile parent) {
		super(fullPath, configFile, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addLinkConfigFile(LinkConfigFile linkConfigFile) {
		//return ;
	}
	@Override
	public void _loopback(PropertiesContainer propertiesContainer,LinkConfigFile son) {
		if( son.getConfigFile().equals(this.getConfigFile()))
		{
			if(configPropertiesFile == null)
				configPropertiesFile = new PropertiesContainer();
			configPropertiesFile.mergeParentConfigProperties(propertiesContainer);
		}
	}

}
