package com.frameworkset.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestStringUtil {
	@org.junit.Test
	public void testJsontoObject()
	{
		String ddd = "{'docId':true,'docname':'aaaa'}";
		Map data = SimpleStringUtil.json2Object(ddd, HashMap.class);
		System.out.println(data);
		
	}
	@org.junit.Test
	public void testArrayToenum()
	{
		String[] datas = new String[] {"1","2"};
		Enumeration enums = SimpleStringUtil.arryToenum(datas);
		while(enums.hasMoreElements())
		{
			System.out.println(enums.nextElement());
		}
		
	}
	 
	@org.junit.Test
	public void testObjecttoString()
	{
		String ddd = "{'docId':true,'docname':'aaaa'}";		
		System.out.println(SimpleStringUtil.tostring(ddd));
		
		System.out.println(SimpleStringUtil.tostring(new String[]{"aa","bb"}));
		List dd = new ArrayList();
		dd.add(1);
		dd.add(2);
		System.out.println(SimpleStringUtil.tostring(new Object[]{"aa",null,dd}));
		System.out.println(SimpleStringUtil.tostring(null));
	}
	
	@org.junit.Test
	public void testIntObjecttoJson()
	{
		 
		System.out.println(SimpleStringUtil.object2json(1));
	}
	
	@org.junit.Test
	public void testmObjecttoJson()
	{
		 
		try {
			String tet ="{\"date\":\"2016 Jul  2 00:06:56\",\"router\":\"175.10.31.66\",\"program\":\"kernel\",\"unixtime\":\"1467389223\",\"msg_kunteng\":\"[136152.820000] {\\\"log-type\\\":\\\"kt-wdpi\\\",\\\"p\\\":\\\"HTTP\\\",\\\"ptype\\\":\\\"GET\\\",\\\"wip\\\":\\\"192.168.1.105\\\",\\\"wmac\\\":\\\"60ACC8010598\\\",\\\"smac\\\":\\\"847303487152\\\",\\\"lmac\\\":\\\"60ACC8010598\\\",\\\"sip\\\":\\\"192.168.199.230\\\",\\\"dip\\\":\\\"123.151.10.172\\\",\\\"sport\\\":\\\"59440\\\",\\\"dport\\\":\\\"443\\\",\\\"url\\\":\\\"http://dns.weixin.qq.com/cgi-bin/micromsg-bin/newgetdns?uin=2597445619&clientversion=637736505&scene=0&net=1&md5=02d8691b08787fbbb9fd3ba88c887619&devicetype=android-21&lan=zh_CN&sigver=2\\\"}\"}";
			HashMap map = SimpleStringUtil.json2Object(tet, HashMap.class);
			System.out.println(map);
			tet = "{\"date\":\"2016 Jul  2 11:28:45\",\"router\":\"175.10.31.66\",\"program\":\"kt-macscan\",\"unixtime\":\"1467430132\",\"msg_kunteng\":\"hiwifi_ac#011{\\\"d\\\":[{\\\"rssi\\\":\\\"-87\\\",\\\"macaddr\\\":\\\"DC9B9CDDD165\\\"},{\\\"rssi\\\":\\\"-75\\\",\\\"macaddr\\\":\\\"08FD0E10531F\\\"},{\\\"rssi\\\":\\\"-86\\\",\\\"macaddr\\\":\\\"40E230331C0A\\\"}],\\\"s\\\":\\\"scan\\\",\\\"v\\\":\\\"1\\\"} \\\"127.0.0.1\\\" \\\"60ACC8010598\\\"\"}";
			MacMsg MacMsg = SimpleStringUtil.json2Object(tet, MacMsg.class);
			System.out.println(MacMsg.getMsg_kunteng());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
