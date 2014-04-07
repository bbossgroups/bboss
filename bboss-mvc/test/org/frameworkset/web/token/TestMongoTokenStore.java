package org.frameworkset.web.token;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMongoTokenStore {
	private static MongodbTokenStore mongodbTokenStore;
	@Before
	public void init() throws Exception
	{
		mongodbTokenStore = new MongodbTokenStore();
		mongodbTokenStore.setTempTokendualtime(18000000);
		MemToken token = mongodbTokenStore.genDualToken("sim","yinbp","xxxxxxxxxxxxxxxxxxxxxx",30l*24l*60l*60l*1000l);
		Assert.assertTrue(TokenStore.temptoken_request_validateresult_ok == mongodbTokenStore.checkToken("sim","xxxxxxxxxxxxxxxxxxxxxx",token.getSigntoken()).getResult());
	}
	@Test
	public void genTemptokenAndValidate() throws Exception
	{		
		
		MemToken token = mongodbTokenStore.genTempToken();
		Assert.assertTrue(TokenStore.temptoken_request_validateresult_ok == mongodbTokenStore.checkToken(null,null,token.getToken()).getResult());
	}
	
	@Test
	public void gendualtokenAndValidate() throws Exception
	{
		//long start = System.currentTimeMillis();
		MemToken token = mongodbTokenStore.genDualToken("sim","yinbp","xxxxxxxxxxxxxxxxxxxxxx",30l*24l*60l*60l*1000l);
		//long end = System.currentTimeMillis();
		//System.out.println(end - start);
		//start = System.currentTimeMillis();
		Assert.assertTrue(TokenStore.temptoken_request_validateresult_ok == mongodbTokenStore.checkToken("sim","xxxxxxxxxxxxxxxxxxxxxx",token.getSigntoken()).getResult());
		//end = System.currentTimeMillis();
		//System.out.println(end - start);
	}
	
	
	@Test
	public void gentempauthortokenAndValidate() throws Exception
	{
		MemToken token = mongodbTokenStore.genAuthTempToken("sim","yinbp","xxxxxxxxxxxxxxxxxxxxxx");
		Assert.assertTrue(TokenStore.temptoken_request_validateresult_ok == mongodbTokenStore.checkToken("sim","xxxxxxxxxxxxxxxxxxxxxx",token.getSigntoken()).getResult());
	}
	
	
	public static void main(String[] args) throws Exception
	{
		final TestMongoTokenStore s = new TestMongoTokenStore();
		s.init();
		for(int i = 0; i < 10; i ++)
		{
			Thread t = new Thread(){
				public void run()
				{
					while(true)
					{
						try {
							long start = System.currentTimeMillis();
//							mongodbTokenStore.requestStart();
							s.gendualtokenAndValidate();
							 s.gentempauthortokenAndValidate();
							s.genTemptokenAndValidate();
							
							long end = System.currentTimeMillis();
							System.out.println("耗时:"+(end -start));
							sleep(1000);
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finally
						{
//							mongodbTokenStore.requestDone();
						}
						
					}
				}
			};
			t.start();
			
					
		}
	}
	
	

}
