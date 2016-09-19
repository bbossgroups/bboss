package org.frameworkset.spi.assemble.soa;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.CallContext;
import org.frameworkset.spi.assemble.BeanAccembleHelper;
import org.frameworkset.spi.assemble.CannotModifyException;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.PropertiesContainer;

public class SOAPro extends Pro {

	public SOAPro() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SOAPro(BaseApplicationContext applicationContext) {
		super(applicationContext);
		// TODO Auto-generated constructor stub
	}
	
	protected void _buildType() throws ClassNotFoundException
	{
		
		cls = BeanAccembleHelper.getClass(clazz);
		
	}
	protected void _buildRefValue(CallContext context, Object defaultValue)
	{
		beaninstance = accember.getRefValue(this, context,
						defaultValue);
		
	}
	protected void _buildBean(CallContext context,boolean convertcontainer)
	{		
		_initBean(  context,  convertcontainer)	;
	}
	
	protected void _buildFactoryClass() throws ClassNotFoundException
	 {
		 factoryClass = BeanAccembleHelper.getClass(this.getFactory_class());
			 
	 }
	
	protected void _buildIocpluginClass() throws ClassNotFoundException
	 {		 
			 				 
		_initIocpluginClass();
	 }
	
	protected void _initTBean(CallContext context, Class type)
	{
		
		if (this.isBean()) {
			beaninstance = accember.getBean(this, context);
		} else {
			beaninstance = this.getTrueValue(context);
		}
		
	}

	@Override
	public void setValue(String value, PropertiesContainer configPropertiesFile) {
		modify();
		this.value = value;
	}
	
	protected void modify() {
		
	}
	 
}
