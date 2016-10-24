package org.frameworkset.soa;

public class BeanWithException {
	private String v1;
	private String v2;
	public String getV1() throws IgnoreException1 {
		if(true)
			throw new IgnoreException1();
		return v1;
	}


	public void setV1(String v1) {
		this.v1 = v1;
	}


	public String getV2() throws IgnoreException2 {
		if(true)
			throw new IgnoreException2();
		return v2;
	}


	public void setV2(String v2) {
		this.v2 = v2;
	}


	public String getV3() {
			
		return v3;
	}


	public void setV3(String v3) {
		this.v3 = v3;
	}


	private String v3;
	

	public BeanWithException() {
		// TODO Auto-generated constructor stub
	}

}
