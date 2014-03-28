package org.frameworkset.web.token;

import junit.framework.Assert;

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
		MemToken token = mongodbTokenStore.genToken("sim","xxxxxxxxxxxxxxxxxxxxxx",30*24*60*60*1000);
		Assert.assertEquals(MemTokenManager.temptoken_request_validateresult_ok, mongodbTokenStore.existToken(token.getAppid(),token.getStatictoken(),token.getToken()));
	}

}
