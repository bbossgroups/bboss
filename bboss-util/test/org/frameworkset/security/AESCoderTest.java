package org.frameworkset.security;

public class AESCoderTest {
	public static void main(String[] args) throws Exception{
		AESCoder dd = new AESCoder("1234567812345678dddddddddd");
    	String r = dd.encrypt("bbbbbbbbbbbbbbbbbbbbb");
    	System.out.println(r);
    	System.out.println(dd.decrypt(r));
    
	}

}
