package org.frameworkset.web.token;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

public class TestMongoTokenStore {
	private MongodbTokenStore mongodbTokenStore;
	@Before
	public void init()
	{
		mongodbTokenStore = new MongodbTokenStore();
		mongodbTokenStore.setTempTokendualtime(18000000);
	}
	@Test
	public void genTemptokenAndValidate()
	{
		MemToken token = mongodbTokenStore.genToken();
		Assert.assertEquals(MemTokenManager.temptoken_request_validateresult_ok, mongodbTokenStore.existToken(token.getToken()));
	}
	
	@Test
	public void gendualtokenAndValidate()
	{
		MemToken token = mongodbTokenStore.genToken("sim","xxxxxxxxxxxxxxxxxxxxxx",30l*24l*60l*60l*1000l);
		Assert.assertTrue(MemTokenManager.temptoken_request_validateresult_ok == mongodbTokenStore.existToken(token.getAppid(),token.getSecret(),token.getToken()));
	}

}
