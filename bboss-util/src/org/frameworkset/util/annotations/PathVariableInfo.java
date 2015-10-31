package org.frameworkset.util.annotations;

/**
 * restful 路径变量对象
 * 
 * @author yinbp
 *
 */
public class PathVariableInfo implements java.io.Serializable{
	/**
	 *   
	 */
	private String variable;
	private boolean last ;
	private String constantstr;
	private int postion;

	public PathVariableInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getConstantstr() {
		return constantstr;
	}

	public void setConstantstr(String constantstr) {
		this.constantstr = constantstr;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public int getPostion() {
		return postion;
	}

	public void setPostion(int postion) {
		this.postion = postion;
	}

}
