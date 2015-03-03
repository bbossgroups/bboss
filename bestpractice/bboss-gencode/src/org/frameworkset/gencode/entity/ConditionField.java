package org.frameworkset.gencode.entity;

public class ConditionField extends Field{
	 
	private boolean or = false;
	private boolean like = false;
	 
	
	 
	public boolean isOr() {
		return or;
	}
	public void setOr(boolean or) {
		this.or = or;
	}
	public boolean isLike() {
		return like;
	}
	public void setLike(boolean like) {
		this.like = like;
	}
}
