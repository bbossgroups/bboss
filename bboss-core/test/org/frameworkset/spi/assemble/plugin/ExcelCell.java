package org.frameworkset.spi.assemble.plugin;

public class ExcelCell {
	int cellpostion;

    String cellname;

    String celltype;

    public int getCellpostion() {
        return cellpostion;
    }

    public void setCellpostion(int cellpostion) {
        this.cellpostion = cellpostion;
    }

    public String getCellname() {
        return cellname;
    }

    public void setCellname(String cellname) {
        this.cellname = cellname;
    }

    public String getCelltype() {
        return celltype;
    }

    public void setCelltype(String celltype) {
        this.celltype = celltype;
    }
}
