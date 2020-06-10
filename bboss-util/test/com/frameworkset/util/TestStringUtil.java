package com.frameworkset.util;

import org.frameworkset.json.JsonTypeReference;

import java.lang.reflect.Type;
import java.util.*;

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
//			tet = "{\"date\":\"2016 Jul  3 09:00:42\",\"router\":\"175.8.11.35\",\"program\":\"kt-macscan\",\"unixtime\":\"1467507650\",\"msg_kunteng\":\"hiwifi_ac#011{\\\"d\\\":[{\\\"rssi\\\":\\\"-85\\\",\\\"macaddr\\\":\\\"DAA119E5D635\\\"},{\\\"rssi\\\":\\\"-89\\\",\\\"macaddr\\\":\\\"BC44864826C9\\\"},{\\\"rssi\\\":\\\"-66\\\",\\\"macaddr\\\":\\\"18DC561A9BD3\\\"},{\\\"rssi\\\":\\\"-87\\\",\\\"macaddr\\\":\\\"DC9B9CDDD165\\\"},{\\\"rssi\\\":\\\"-89\\\",\\\"macaddr\\\":\\\"68A3C4C20538\\\"},{\\\"rssi\\\":\\\"-77\\\",\\\"macaddr\\\":\\\"A8BBCF603AFE\\\"},{\\\"rssi\\\":\\\"-81\\\",\\\"macaddr\\\":\\\"40E230331C0A\\\"}],\\\"s\\\":\\\"scan\\\",\\\"v\\\":\\\"1\\\"} \\\"127.0.0.1\\\" \\\"60ACC8010598\\\"\",\"@version\":\"1\",\"@timestamp\":\"2016-07-03T01:00:42.997Z\",\"path\":\"/data2/log/wifimsgnew/msg_20160703.log\",\"host\":\"dell\"}";
//			LogstashMacMsg LogstashMacMsg = SimpleStringUtil.json2Object(tet, LogstashMacMsg.class);
//			
//			System.out.println(LogstashMacMsg);
			tet = "{\"date\":\"2016 Jul  2 11:28:45\",\"router\":\"175.10.31.66\",\"program\":\"kt-macscan\",\"unixtime\":\"1467430132\",\"msg_kunteng\":\"hiwifi_ac#011{\\\"d\\\":[{\\\"rssi\\\":\\\"-87\\\",\\\"macaddr\\\":\\\"DC9B9CDDD165\\\"},{\\\"rssi\\\":\\\"-75\\\",\\\"macaddr\\\":\\\"08FD0E10531F\\\"},{\\\"rssi\\\":\\\"-86\\\",\\\"macaddr\\\":\\\"40E230331C0A\\\"}],\\\"s\\\":\\\"scan\\\",\\\"v\\\":\\\"1\\\"} \\\"127.0.0.1\\\" \\\"60ACC8010598\\\"\"}";
			MacMsg MacMsg = SimpleStringUtil.json2Object(tet, MacMsg.class);
			System.out.println(MacMsg.getMsg_kunteng());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	@org.junit.Test
	public void testListObjectSerial()
	{
		List<Integer> dd = new ArrayList<Integer>();
		dd.add(1);
		dd.add(2);
		String json = SimpleStringUtil.object2json(dd);
		dd =  (List<Integer>) StringUtil.json2ObjectWithType(json,new JsonTypeReference<List<Integer>>(){});
		System.out.println(dd);
		dd = SimpleStringUtil.json2ListObject(json,Integer.class);
		System.out.println(dd);
	}


	@org.junit.Test
	public void testListPObjectSerial()
	{
		List<Po> dd = new ArrayList<Po>();
		Po p = new Po();
		p.setId("id");
		p.setName("name");
		dd.add(p);
		p = new Po();
		p.setId("id1");
		p.setName("name1");
		dd.add(p);;
		String json = SimpleStringUtil.object2json(dd);
		dd =  (List<Po>) StringUtil.json2ObjectWithType(json,new JsonTypeReference<List<Po>>(){});
		System.out.println(dd);
		dd = SimpleStringUtil.json2ListObject(json,Po.class);
		System.out.println(dd);
		Set<Po> se = SimpleStringUtil.json2LSetObject(json,Po.class);
		Po[] a = SimpleStringUtil.json2LArrayObject(json,Po.class);
		json2ListObject("",Po.class);
	}

	public static <T> void json2ListObject(String jsonString,Class<T> beanType) {

		JsonTypeReference<List<T>> ref = new JsonTypeReference<List<T>>() {
		};

		Type type = ref.getType();
		System.out.println(type);

	}

}
