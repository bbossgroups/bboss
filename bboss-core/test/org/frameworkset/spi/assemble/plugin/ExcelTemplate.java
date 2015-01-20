package org.frameworkset.spi.assemble.plugin;

import java.util.List;

public class ExcelTemplate {
	String name;

    String templatepath;

    int starrow;

    int endrow = -1;

    List<ExcelCell> cells;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplatepath() {
        return templatepath;
    }

    public void setTemplatepath(String templatepath) {
        this.templatepath = templatepath;
    }

    public int getStarrow() {
        return starrow;
    }

    public void setStarrow(int starrow) {
        this.starrow = starrow;
    }

    public int getEndrow() {
        return endrow;
    }

    public void setEndrow(int endrow) {
        this.endrow = endrow;
    }

    public List<ExcelCell> getCells() {
        return cells;
    }

    public void setCells(List<ExcelCell> cells) {
        this.cells = cells;
    }
}
