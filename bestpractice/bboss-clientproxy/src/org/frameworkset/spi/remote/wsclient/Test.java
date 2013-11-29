package org.frameworkset.spi.remote.wsclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.policy.Assertor;
import org.apache.cxf.ws.policy.EndpointPolicy;
import org.apache.cxf.ws.policy.EndpointPolicyImpl;
import org.apache.cxf.ws.policy.PolicyEngine;
import org.apache.cxf.ws.policy.PolicyEngineImpl;
import org.apache.cxf.ws.policy.PolicyUtils.WrappedAssertor;
import org.apache.cxf.ws.policy.selector.MaximalAlternativeSelector;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;

public class Test {
	private ZWSFIFYKZV1 port;
	private void init() throws Exception {
	    	if(port == null)
	    	{
	    		
	            //两种方获取WSDL文件（本地或者服务器端，因为服务器端需要权限验证所以只能用客户端的WSDL文件）
	            //ZWSFIFYKZV1_Service services = new ZWSFIFYKZV1_Service(wsdlURL,SERVICE_NAME);
	           ZWSFIFYKZV1_Service services = new ZWSFIFYKZV1_Service();
	           
	            port = services.getZWSFIFYKZV1();
	          
//	            ((JaxWsClientProxy)port).getClient().getEndpoint().getEndpointInfo().setProperty(HTTPConduitFactory.class.getName(), new HTTPConduitFactory(){
//
//					public HTTPConduit createConduit(HTTPTransportFactory f,
//							EndpointInfo localInfo, EndpointReferenceType target)
//							throws IOException {
//						// TODO Auto-generated method stub
//						return new URLConnectionHTTPConduit(f.getBus(),localInfo,target);
//					}
//	            	
//	            });
	            
	            EndpointInfo endinfo = ClientProxy.getClient(port).getEndpoint().getEndpointInfo();
	            PolicyEngineImpl pe = ClientProxy.getClient(port).getBus().getExtension(PolicyEngineImpl.class);
	            
	            pe.setAlternativeSelector(new MaximalAlternativeSelector(){

					@Override
					public Collection<Assertion> selectAlternative(
							Policy policy, PolicyEngine engine,
							Assertor assertor, List<List<Assertion>> request) {
						// TODO Auto-generated method stub
						try {
							Collection<Assertion> ca = super.selectAlternative(policy, engine, assertor, request);
							if(ca == null)
								ca = new ArrayList<Assertion>();
							return ca;
						} catch (Exception e) {
							return new ArrayList<Assertion>();
						}
					}
	            	
	            });
	           
	            EndpointPolicyImpl policy = new EndpointPolicyImpl(endinfo,pe,true,null){

					@Override
					public Collection<Assertion> getChosenAlternative() {
						// TODO Auto-generated method stub
						return new ArrayList<Assertion>();
					}

					@Override
					public void initialize() {
						// TODO Auto-generated method stub
						try {
							super.initialize();
						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
						}
					}
					
					
                	
                }; 
                
//               
                policy.getPolicy();
	            endinfo.setProperty("policy-engine-info-client-endpoint",  endinfo.getTraversedExtensor(
	            		policy , EndpointPolicy.class));
	            
	            HTTPConduit conduit = (HTTPConduit)(ClientProxy.getClient(port).getConduit());
	            ClassInfo info = ClassUtil.getClassInfo(EndpointPolicyImpl.class);
	            try {
					info.getPropertyDescriptor("assertor").setValue(policy, new WrappedAssertor(conduit));
					 policy.initialize();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}	            
//	            String ns = "http://cxf.apache.org/transports/http";
//	            ConduitInitiatorManager mgr = bus.getExtension(ConduitInitiatorManager.class);
//	            mgr.registerConduitInitiator("http://cxf.apache.org/transports/http", new org.apache.cxf.transport.http.HTTPTransportFactory(){
//	            	
//	            });
                Map map = ((BindingProvider) port).getRequestContext(); 
                map.put(BindingProvider.USERNAME_PROPERTY,"DSP");
                map.put(BindingProvider.PASSWORD_PROPERTY, "/852*963");
                map.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
	    	}     
	    }
	
	public void getData(String func_area,String pstng_date,String txtlg) throws Exception
	{
		 
		init();
		// create parameter
        ObjectFactory factory = new ObjectFactory();
        Holder<TableOfZstFykz> fi00O06 = new Holder<TableOfZstFykz>(factory.createTableOfZstFykz());
        //返回参数设置
        Holder<String>  o_func_area = new Holder<String>(""); 
        Holder<String> o_pstng_date = new Holder<String>("");
        Holder<String> o_txtlg = new Holder<String>("");
		port.zfmFiFykzV1(fi00O06, func_area, pstng_date, txtlg, o_func_area, o_pstng_date, o_txtlg);
	    
//		currMonthActFee.setHolder(fi00O06.value);
//		currMonthActFee.setO_FUNC_AREA(o_func_area.value);
//		currMonthActFee.setO_PSTNG_DATE(o_pstng_date.value);
//		currMonthActFee.setO_TXTLG(o_txtlg.value);
//		return currMonthActFee;
	}
	
	public static void main(String[] args) throws Exception {
//		Test client = new Test();
//		client.getData("0200", "2012-12-19", "泵送事业部");
		
		/*org.apache.cxf.jaxws.JaxWsProxyFactoryBean factory_ = new org.apache.cxf.jaxws.JaxWsProxyFactoryBean();
		//http://sanybwpci.sany.com.cn:8001/sap/bc/srt/wsdl/sdef_ZWS_FI_FYKZ_V1/wsdl11/ws_policy/document?sap-client=800
        //factory_.setAddress("http://DSP:%2F852*963@sanybwpci.sany.com.cn:8001/sap/bc/srt/rfc/sap/zws_fi_fykz_v1/800/zws_fi_fykz_v1/zws_fi_fykz_v1");
		factory_.setAddress("http://DSP:%2F852*963@sanybwpci.sany.com.cn:8001/sap/bc/srt/wsdl/sdef_ZWS_FI_FYKZ_V1/wsdl11/ws_policy/document?sap-client=800");
		factory_.setServiceClass(Test.class);
		Test wsservice =  (Test)factory_.create();
		ObjectFactory factory = new ObjectFactory();
        Holder<TableOfZstFykz> fi00O06 = new Holder<TableOfZstFykz>(factory.createTableOfZstFykz());
        //返回参数设置
        Holder<String>  o_func_area = new Holder<String>(""); 
        Holder<String> o_pstng_date = new Holder<String>("");
        Holder<String> o_txtlg = new Holder<String>("");
		wsservice.zfmFiFykzV1(fi00O06, "0200", "20121219", "泵送事业部", o_func_area, o_pstng_date, o_txtlg);
		*/
		
		test2();
	}
	
	public static void test2()
	{
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();


		//factory.getInInterceptors().add(new LoggingInInterceptor());
		//factory.getOutInterceptors().add(new LoggingOutInterceptor());


		factory.setServiceClass(ZWSFIFYKZV1.class);
		factory.setAddress("http://aabwpci.aa.com.cn:8001/sap/bc/srt/rfc/sap/zws_fi_fykz_v1/800/zws_fi_fykz_v1/zws_fi_fykz_v1"); 


		ZWSFIFYKZV1 info = (ZWSFIFYKZV1) factory.create();


		Client client = ClientProxy.getClient(info);
		HTTPConduit http = (HTTPConduit) client.getConduit();


		http.getAuthorization().setUserName("DSP");
		http.getAuthorization().setPassword("/852*963");
		Map map = ((BindingProvider) info).getRequestContext(); 
		map.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true); 
		ObjectFactory factory1 = new ObjectFactory();
		Holder<TableOfZstFykz> fi00O06 = new Holder<TableOfZstFykz>(factory1.createTableOfZstFykz()); 
		Holder<String> oFuncArea = new Holder<String>(""); 
		Holder<String> oPstngDate = new Holder<String>(""); 
		Holder<String> oTxtlg = new Holder<String>(""); 
		info.zfmFiFykzV1(fi00O06, "", "", "", oFuncArea, oPstngDate, oTxtlg);
		System.out.print(fi00O06.value);
		System.out.print(oFuncArea.value);
		System.out.print(oPstngDate.value);
		System.out.print(oTxtlg.value);
//		System.out.print(fi00O06.value);
	}
}
