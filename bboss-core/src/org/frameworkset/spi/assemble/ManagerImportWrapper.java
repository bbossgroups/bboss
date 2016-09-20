package org.frameworkset.spi.assemble;

import java.util.List;

public class ManagerImportWrapper {
	private List<ManagerImport> imports;
	private LinkConfigFile parent;
	public List<ManagerImport> getImports() {
		return imports;
	}
	public void setImports(List<ManagerImport> imports) {
		this.imports = imports;
	}
	public LinkConfigFile getParent() {
		return parent;
	}
	public void setParent(LinkConfigFile parent) {
		this.parent = parent;
	}
}
