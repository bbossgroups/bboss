package org.frameworkset.gencode.entity;

public class ModuleMetaInfo {
	/**
	 * 存放生成的工程文件的根目录，规范化的存放代码工程的目录结构为：
	 * src-modulename
	 * WebRoot/modulename
	 * WebRoot/WEB-INF/conf/modulename
	 * 
	 */
	private String sourcedir;
	/**
	 * jsp相对于WebRoot的存放根路径（可选）
	 */
	private String system;
	/**
	 * 业务模块的名称，根据模块名称生成存放java源码、前端界面和资源文件、配置文件的存放目录
	 */
	private String moduleName;
	/**
	 * 模块中文名称
	 */
	private String moduleCNName;
	/**
	 * 生成的模块java源码包根路径，同时会在下面建以下子目录：
	 * service 存放业务组件、持久层sql配置文件
	 * entity 存放po类
	 * action 存放控制器类
	 * webservice 存放webservice和hessian服务类
	 * 
	 */
	private String packagePath;
	private String tableName;
	/**
	 * 表主键名称，对应tableinfo表中的表名称
	 */
	private String pkname;
	private boolean genWebservice;
	private boolean genHessian;
	private String hessianServicePort;
	private String webservicePort;
	private boolean genFront;
	/**
	 * 生成代码时是否清除源码目录
	 */
	private boolean clearSourcedir = true;
	
	private boolean exportExcel;
	private boolean importExcel;
	
	private String entityName;
	private String serviceName;
	private String mainJspName;
	private String addJspName;
	private String updateJspName;
	private String detailJspName;
	private String datasourceName;
	private boolean autogenprimarykey = true;
	private String encodecharset = "UTF-8";
	private String date = "";			
	private String company;
	private String author;
	private String version;
	/**
	 * 0:2003
	 * 1:2007
	 * 2:2010
	 * 2:2013
	 */
	private int excelVersion;
	
	private boolean ignoreEntityFirstToken = false;
	
	private boolean intergretWorkflow;
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public String getPackagePath() {
		return packagePath;
	}
	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public boolean isGenWebservice() {
		return genWebservice;
	}
	public void setGenWebservice(boolean genWebservice) {
		this.genWebservice = genWebservice;
	}
	public boolean isGenHessian() {
		return genHessian;
	}
	public void setGenHessian(boolean genHessian) {
		this.genHessian = genHessian;
	}
	public boolean isGenFront() {
		return genFront;
	}
	public void setGenFront(boolean genFront) {
		this.genFront = genFront;
	}
	public String getHessianServicePort() {
		return hessianServicePort;
	}
	public void setHessianServicePort(String hessianServicePort) {
		this.hessianServicePort = hessianServicePort;
	}
	public String getWebservicePort() {
		return webservicePort;
	}
	public void setWebservicePort(String webservicePort) {
		this.webservicePort = webservicePort;
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
	public String getSourcedir() {
		return sourcedir;
	}
	public void setSourcedir(String sourcedir) {
		this.sourcedir = sourcedir;
	}
	public boolean isClearSourcedir() {
		return clearSourcedir;
	}
	public void setClearSourcedir(boolean clearSourcedir) {
		this.clearSourcedir = clearSourcedir;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getMainJspName() {
		return mainJspName;
	}
	public void setMainJspName(String mainJspName) {
		this.mainJspName = mainJspName;
	}
	public String getAddJspName() {
		return addJspName;
	}
	public void setAddJspName(String addJspName) {
		this.addJspName = addJspName;
	}
	public String getUpdateJspName() {
		return updateJspName;
	}
	public void setUpdateJspName(String updateJspName) {
		this.updateJspName = updateJspName;
	}
	public String getDetailJspName() {
		return detailJspName;
	}
	public void setDetailJspName(String detailJspName) {
		this.detailJspName = detailJspName;
	}
	public String getDatasourceName() {
		return datasourceName;
	}
	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}
	public boolean isIgnoreEntityFirstToken() {
		return ignoreEntityFirstToken;
	}
	public void setIgnoreEntityFirstToken(boolean ignoreEntityFirstToken) {
		this.ignoreEntityFirstToken = ignoreEntityFirstToken;
	}
	public boolean isAutogenprimarykey() {
		return autogenprimarykey;
	}
	public void setAutogenprimarykey(boolean autogenprimarykey) {
		this.autogenprimarykey = autogenprimarykey;
	}
	public String getEncodecharset() {
		return encodecharset;
	}
	public void setEncodecharset(String encodecharset) {
		this.encodecharset = encodecharset;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getModuleCNName() {
		return moduleCNName;
	}
	public void setModuleCNName(String moduleCNName) {
		this.moduleCNName = moduleCNName;
	}
	public String getPkname() {
		return pkname;
	}
	public void setPkname(String pkname) {
		this.pkname = pkname;
	}
	
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}

}
