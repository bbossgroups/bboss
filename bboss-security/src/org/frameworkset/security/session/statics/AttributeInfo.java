package org.frameworkset.security.session.statics;

public class AttributeInfo implements java.io.Serializable,Cloneable{
	private String name;
	private String cname;
	private String type;
	private Object value;
	private boolean like;
	private boolean enableEmptyValue;
	public AttributeInfo() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	@Override
	public AttributeInfo clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (AttributeInfo)super.clone();
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public boolean isLike() {
		return like;
	}
	public void setLike(boolean like) {
		this.like = like;
	}
	public boolean isEnableEmptyValue() {
		return enableEmptyValue;
	}
	public void setEnableEmptyValue(boolean enableEmptyValue) {
		this.enableEmptyValue = enableEmptyValue;
	}

}
