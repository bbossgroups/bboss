package org.frameworkset.gencode.entity;

public class ControlParam {
	private boolean print;
	private boolean genI18n;
	private String theme = "default";
	private boolean genRPCservice;
	private boolean intergretWorkflow;
	private boolean genFront;
	/**
	 * 0:2003
	 * 1:2007
	 * 2:2010
	 * 2:2013
	 */
	private int excelVersion;
	private boolean exportExcel;
	private boolean importExcel;
	public ControlParam() {
		// TODO Auto-generated constructor stub
	}
	public boolean isPrint() {
		return print;
	}
	public void setPrint(boolean print) {
		this.print = print;
	}
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
	public boolean isGenI18n() {
		return genI18n;
	}
	public void setGenI18n(boolean genI18n) {
		this.genI18n = genI18n;
	}
	
	public boolean isGenFront() {
		return genFront;
	}
	public void setGenFront(boolean genFront) {
		this.genFront = genFront;
	}

	public boolean isExportExcel() {
		return exportExcel;
	}
	public void setExportExcel(boolean exportExcel) {
		this.exportExcel = exportExcel;
	}
	public boolean isImportExcel() {
		return importExcel;
	}
	public void setImportExcel(boolean importExcel) {
		this.importExcel = importExcel;
	}
	public int getExcelVersion() {
		return excelVersion;
	}
	public void setExcelVersion(int excelVersion) {
		this.excelVersion = excelVersion;
	}
	public boolean isIntergretWorkflow() {
		return intergretWorkflow;
	}
	public void setIntergretWorkflow(boolean intergretWorkflow) {
		this.intergretWorkflow = intergretWorkflow;
	}
	 
	
	public boolean isGenRPCservice() {
		return genRPCservice;
	}
	public void setGenRPCservice(boolean genRPCservice) {
		this.genRPCservice = genRPCservice;
	}
}
