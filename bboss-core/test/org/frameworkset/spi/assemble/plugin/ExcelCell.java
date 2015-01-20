package org.frameworkset.spi.assemble.plugin;

public class ExcelCell {
	private int cellpostion;

	private String javaFiledName;

	private String celltype;

    public int getCellpostion() {
        return cellpostion;
    }

    public void setCellpostion(int cellpostion) {
        this.cellpostion = cellpostion;
    }

    

    public String getCelltype() {
        return celltype;
    }

    public void setCelltype(String celltype) {
        this.celltype = celltype;
    }

	public String getJavaFiledName() {
		return javaFiledName;
	}

	public void setJavaFiledName(String javaFiledName) {
		this.javaFiledName = javaFiledName;
	}
}
