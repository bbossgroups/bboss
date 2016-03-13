package com.frameworkset.common.ecs;

public class Input extends BaseElement{
	private String type;
	private String name;
	private String ID;
	private Object value;
	private boolean checked;
	private boolean disabled;
	private Object size;
	private String onClick;
	private String onKeyDown;
	
	public static final String TEXT = "TEXT";
	  public static final String PASSWORD = "PASSWORD";
	  public static final String CHECKBOX = "CHECKBOX";
	  public static final String RADIO = "RADIO";
	  public static final String FILE = "FILE";
	  public static final String BUTTON = "BUTTON";
	  public static final String IMAGE = "IMAGE";
	  public static final String HIDDEN = "HIDDEN";
	  public static final String SUBMIT = "SUBMIT";
	  public static final String RESET = "RESET";
	  public static final String text = "text";
	  public static final String password = "password";
	  public static final String checkbox = "checkbox";
	  public static final String radio = "radio";
	  public static final String file = "file";
	  public static final String button = "button";
	  public static final String image = "image";
	  public static final String hidden = "hidden";
	  public static final String submit = "submit";
	  public static final String reset = "reset";
	 
	public Input() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void toString(StringBuilder a) {
		a.append("<input");
		if(type != null)
			a.append(" type=\"").append(type).append("\"");
		if(checked )
			a.append(" checked ");
		if(disabled )
			a.append(" disabled ");
		
		if(size != null)
			a.append(" size=\"").append(size).append("\"");
		if(name != null)
			a.append(" name=\"").append(name).append("\"");
		if(onKeyDown != null)
			a.append(" onKeyDown=\"").append(onKeyDown).append("\"");
		
		if(ID != null)
			a.append(" id=\"").append(ID).append("\"");
		if(value != null)
			a.append(" value=\"").append(value).append("\"");
		if(style != null)
			a.append(" style=\"").append(style).append("\"");
		if(clazz != null)
			a.append(" class=\"").append(clazz).append("\"");
		if(onClick != null)
			a.append(" onClick=\"").append(onClick).append("\"");
		
		if(this.extend != null)
			a.append(" ").append(extend);
		a.append(">");
		if(tagText != null)
			a.append(tagText);
		
		
	}
	public String getType() {
		return type;
	}
	public Input setType(String type) {
		this.type = type;
		return this;
	}
	public String getName() {
		return name;
	}
	public Input setName(String name) {
		this.name = name;
		return this;
	}
	public String getID() {
		return ID;
	}
	public Input setID(String iD) {
		ID = iD;
		return this;
	}
	public Object getValue() {
		return value;
	}
	public Input setValue(Object value) {
		this.value = value;
		return this;
	}
	public boolean isChecked() {
		return checked;
	}
	public Input setChecked(boolean checked) {
		this.checked = checked;
		return this;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public Input setDisabled(boolean disabled) {
		this.disabled = disabled;
		return this;
	}
	public Object getSize() {
		return size;
	}
	public Input setSize(Object size) {
		this.size = size;return this;
	}
	public String getOnClick() {
		return onClick;
	}
	public Input setOnClick(String onClick) {
		this.onClick = onClick;return this;
	}
	public String getOnKeyDown() {
		return onKeyDown;
	}
	public void setOnKeyDown(String onKeyDown) {
		this.onKeyDown = onKeyDown;
	}
}
