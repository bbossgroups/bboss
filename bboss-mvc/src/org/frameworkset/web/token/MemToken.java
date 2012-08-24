package org.frameworkset.web.token;
/**
 * @author biaoping.yin
 * 
 *
 */
public class MemToken {
	private String token;
	private long createTime;
	public String getToken() {
		return token;
	}

	public long getCreateTime() {
		return createTime;
	}
	
	
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return token.hashCode();
	}
	public MemToken(String token, long createTime) {
		super();
		this.token = token;
		this.createTime = createTime;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof MemToken)
		{
			return token.equals(((MemToken)obj).getToken());
		}
		return false;
		// TODO Auto-generated method stub
		
	}

}
