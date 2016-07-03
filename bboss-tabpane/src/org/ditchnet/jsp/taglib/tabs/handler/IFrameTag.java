package org.ditchnet.jsp.taglib.tabs.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import com.frameworkset.common.ecs.IFrame;
import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.common.tag.contextmenu.ContextMenu;

public class IFrameTag extends BaseTag {
	
	private TabContainerTag tabContainer;
	private TabPaneTag pane = null;
	private String marginwidth;
	private String longdesc;
	private String name;
	private String src;
	private String frameborder;
	private String height;
	private String width;
	private String marginheight;
	private String scrolling;
	private String id;
	private String classname;
	private String style;
	private String align;
	private String tabIndex;
	private String extend;
	private String title;
	private int position = -1;
	private String text;
	private IFrame iframe ;
	public int doStartTag() throws JspException
	{
		//IFrame iframe = new IFrame();
		iframe = new IFrame();
		if(this.align != null)
			iframe.setAlign(this.align);
		if(this.classname != null)
			iframe.setClass(this.classname);
		if(this.extend != null)
			iframe.setExtend(this.getExtend());
		if(this.frameborder != null)
			iframe.setFrameBorder(!this.frameborder.equals("0"));
		if(this.height != null)
			iframe.setHeight(this.getHeight());
		if(this.id != null)
			iframe.setID(this.getId());
		if(this.longdesc != null)
			iframe.setLongDesc(this.longdesc);
		if(this.marginheight != null)
			iframe.setMarginHeight(this.marginheight);
		if(this.marginwidth != null)
			iframe.setMarginWidth(this.marginwidth);
		if(this.name != null)
			iframe.setName(this.name);
		if(this.scrolling != null)
			iframe.setScrolling(this.scrolling);
		if(this.style != null)
			iframe.setStyle(this.style);
		if(this.position != -1)
			iframe.setTagPosition(this.position );
		if(this.text != null)
			iframe.setTagText(this.text);
		if(this.title != null)
			iframe.setTitle(this.title);
		if(this.width != null)
			iframe.setWidth(this.width);
		//是否加载src页面
		loadSRC(iframe);
			
		int ret = super.doStartTag();
		return ret;
	}
	
	public TabContainerTag getTabContainer() {
		if (null == tabContainer) {
			tabContainer = (TabContainerTag)
					findAncestorWithClass(this,TabContainerTag.class);
		}
		return tabContainer;
	}
	
	public TabPaneTag getTabPaneTag() {
		if (null == pane) {
			pane = (TabPaneTag)
					findAncestorWithClass(this,TabPaneTag.class);
		}
		return pane;
	}
	
	public int doEndTag() throws JspException
	{
		
		
		List l = this.getTabPaneTag().getListIFrame();
		l.add(new String[]{this.id,this.src});
		
		try {
			this.getJspWriter().println(iframe);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		int ret = super.doEndTag();
		iframe = null;
		return ret;
	}
	
	/**
	 * 重置所有的属性
	 *
	 */
	void reset()
	{
		tabContainer = null;
		pane = null;
		marginwidth = null;
		longdesc = null;
		name = null;
		src = null;
		frameborder = null;
		height = null;
		width = null;
		marginheight = null;
		scrolling = null;
		id = null;
		classname = null;
		style = null;
		align = null;
		tabIndex = null;
		extend = null;
		title = null;
		position = -1;
		text = null;
		iframe = new IFrame();
	}
	
	
	    /**
	    Sets the LONGDESC="" attribute
	    @param   longdesc  the LONGDESC="" attribute
	*/
	public void setLongDesc(String longdesc)
	{
	    this.longdesc = longdesc;
	}
	
	/**
	    Sets the NAME="" attribute
	    @param   name  the NAME="" attribute
	 * @return 
	*/
	public void setName(String name)
	{
	    this.name = name;
	}
	
	/**
	    Sets the SRC="" attribute
	    @param   src  the SRC="" attribute
	*/
	public void setSrc(String src)
	{
	    this.src = src;
	}
	
	/**
	    Sets the FRAMEBORDER="" attribute
	    @param   frameborder  the FRAMEBORDER="" attribute
	*/
	public void setFrameBorder(String frameborder)
	{
		this.frameborder = frameborder;
//		
//	    if (frameborder)
//	        addAttribute("frameborder",Integer.toString(1));
//	    else
//	        addAttribute("frameborder",Integer.toString(0));            
//	    return this;
	}
	

	
	/**
	    Sets the MARGINWIDTH="" attribute
	    @param   marginwidth  the MARGINWIDTH="" attribute
	*/
	public void setMarginWidth(String marginwidth)
	{
	    this.marginwidth = marginwidth;
	}
	
	
	/**
	    Sets the HEIGHT="" attribute
	    @param   height  the HEIGHT="" attribute
	*/
	public void setHeight(String height)
	{
	    this.height = height;
	   
	}
	
	/**
	    Sets the WIDTH="" attribute
	    @param   width  the WIDTH="" attribute
	*/
	public void setWidth(String width)
	{
		this.width = width;
	}
	
	
	
	
	
	
	/**
	    Sets the SCROLLING="" attribute
	    @param   scrolling  the SCROLLING="" attribute
	*/
	public void setScrolling(String scrolling)
	{
		this.scrolling = scrolling;
	}
	
	/**
	    Sets the ALIGN="" attribute.
	
	    @param  align sets the ALIGN="" attribute. You can
	    use the AlignType.* variables for convience.
	*/
	public void setAlign(String align)
	{
	    this.align = align;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String className) {
		this.classname = className;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public String getFrameborder() {
		return frameborder;
	}

	public void setFrameborder(String frameborder) {
		this.frameborder = frameborder;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLongdesc() {
		return longdesc;
	}

	public void setLongdesc(String longdesc) {
		this.longdesc = longdesc;
	}

	public String getMarginheight() {
		return marginheight;
	}

	public void setMarginheight(String marginheight) {
		this.marginheight = marginheight;
	}

	public String getMarginwidth() {
		return marginwidth;
	}

	public void setMarginwidth(String marginwidth) {
		this.marginwidth = marginwidth;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getAlign() {
		return align;
	}

	public String getHeight() {
		return height;
	}

	public String getName() {
		return name;
	}

	public String getScrolling() {
		return scrolling;
	}

	public String getSrc() {
		return src;
	}

	public String getWidth() {
		return width;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String generateContent() {
		// TODO Auto-generated method stub
		return super.generateContent();
	}

	public ContextMenu getContextMenu() {
		// TODO Auto-generated method stub
		return super.getContextMenu();
	}

	public void initContextMenu() {
		// TODO Auto-generated method stub
		super.initContextMenu();
	}

	public boolean isEnablecontextmenu() {
		// TODO Auto-generated method stub
		return super.isEnablecontextmenu();
	}

	public void setEnablecontextmenu(boolean arg0) {
		// TODO Auto-generated method stub
		super.setEnablecontextmenu(arg0);
	}

	public void setPageContext(PageContext arg0) {
		// TODO Auto-generated method stub
		super.setPageContext(arg0);
	}

	public void write(OutputStream arg0) {
		// TODO Auto-generated method stub
		super.write(arg0);
	}

	public int doAfterBody() throws JspException {
		// TODO Auto-generated method stub
		return super.doAfterBody();
	}

	public void release() {
		// TODO Auto-generated method stub
		super.release();
	}

	public void setParent(Tag arg0) {
		// TODO Auto-generated method stub
		super.setParent(arg0);
	}

	public Tag getParent() {
		// TODO Auto-generated method stub
		return super.getParent();
	}

	public void setValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		super.setValue(arg0, arg1);
	}

	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return super.getValue(arg0);
	}

	public void removeValue(String arg0) {
		// TODO Auto-generated method stub
		super.removeValue(arg0);
	}

	public Enumeration getValues() {
		// TODO Auto-generated method stub
		return super.getValues();
	}

	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	public void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	private void loadSRC(IFrame iframe){
		if(this.src != null){
			if(this.getTabPaneTag().isLazeload()
					&& !this.getTabPaneTag().isSelectedTab()){
				iframe.setSrc("");
			}else{
				iframe.setSrc(this.src);
			}
		}
	}

	@Override
	public void doFinally() {
	
		iframe = null;
		reset();
		super.doFinally();
	}
	

}
