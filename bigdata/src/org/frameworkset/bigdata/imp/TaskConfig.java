package org.frameworkset.bigdata.imp;

import java.util.List;
import java.util.Map;

public class TaskConfig implements java.io.Serializable{
	/**
	 * 数据库连接信息
	 */
	String driver;
	String dburl;
	String dbpassword;
	String dbuser;
	String validatesql;
	/**
	 * 创建的数据源是否采用连接池
	 */
	boolean usepool;
	private String readOnly;
	Map<String,List<Integer>> blocksplits; 
	/**
	 * 指定dbname主要是用于连接池，系统固化连接池，
	 * 数据库连接信息配置和dbname配置只能二选一
	 */
	String dbname;
	String tablename;
	String columns;
	String pkname;
	int datablocks;
	String hdfsserver;
	String hdfsdatadirpath;
	String localdirpath;
	long pagesize;
	String filebasename;
	int geneworkthreads=20;
    int uploadeworkthreads=20;
	int genqueques=5;
	int uploadqueues=5;
	int genquequetimewait=10;
	int uploadqueuetimewait=10;
	boolean genlocalfile;
	String datatype;
	String querystatement;
	String limitstatement;
	String countstatement;
 	String pageinestatement;
 	long tablerows;
 	boolean usepagine = false;
	boolean adminnodeasdatanode;
	TaskInfo tasks[];
	public String schema;
	String jobname;
	public boolean clearhdfsfiles;
	public long startid;
	public long endid;
	public String blocks;
	/**
	 * 当指定了具体的任务处理数据块blocks，则可以设定每块可以直接拆分的子数据块这样可以提升系统处理速度
	 */
	int subblocks;
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("jobname=").append(jobname).append("\r\n")
		.append("dbname=").append(dbname).append("\r\n")
				.append("schema=").append(this.schema).append("\r\n")
		.append("tablename=").append(tablename).append("\r\n")
		.append("columns=[").append(columns).append("]\r\n")
		.append("pkname=").append(pkname).append("\r\n");
		
		if(driver != null && driver.trim().length() > 0)
		{
			builder.append("driver=").append(driver).append("\r\n")
					.append("dburl=").append(this.dburl).append("\r\n")
			.append("dbpassword=").append(dbpassword).append("\r\n")
			.append("dbuser=[").append(dbuser).append("]\r\n")
			.append("usepool=[").append(usepool).append("]\r\n")
			.append("readOnly=[").append(readOnly).append("]\r\n")
			.append("validatesql=").append(validatesql).append("\r\n");
			
		}
		
		
		builder.append("datablocks=").append(datablocks).append("\r\n")
		.append("hdfsserver=").append(hdfsserver).append("\r\n")
		.append("hdfsdatadirpath=").append(hdfsdatadirpath).append("\r\n")
		.append("localdirpath=").append(localdirpath).append("\r\n")
		.append("pagesize=").append(pagesize).append("\r\n")
		.append("filebasename=").append(filebasename).append("\r\n")
		.append("geneworkthreads=").append(geneworkthreads).append("\r\n")
		.append("uploadeworkthreads=").append(uploadeworkthreads).append("\r\n")
		.append("genqueques=").append(genqueques).append("\r\n")
		.append("uploadqueues=").append(uploadqueues).append("\r\n")
		.append("genquequetimewait=").append(genquequetimewait).append("\r\n")
		.append("uploadqueuetimewait=").append(uploadqueuetimewait).append("\r\n")
		.append("genlocalfile=").append(genlocalfile).append("\r\n")
		.append("datatype=").append(datatype).append("\r\n")
		.append("usepagine=").append(usepagine).append("\r\n")
		.append("countstatement=").append(countstatement).append("\r\n")
		.append("pageinestatement=").append(pageinestatement).append("\r\n")
			.append("tablerows=").append(tablerows).append("\r\n")
		.append("querystatement=").append(querystatement).append("\r\n")
		.append("adminnodeasdatanode=").append(adminnodeasdatanode).append("\r\n")
		.append("limitstatement=").append(limitstatement).append("\r\n")
		.append("startid=").append(startid).append("\r\n")
		.append("endid=").append(endid).append("\r\n")
		.append("blocks=").append(blocks).append("\r\n")
		.append("clearhdfsfiles=").append(clearhdfsfiles);
		if(blocks != null && blocks.trim().length() > 0)
			builder.append("\r\n").append("subblocks=").append(subblocks);
		return builder.toString();
		
		
	}
	public String getDbname() {
		return dbname;
	}
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	public String getPkname() {
		return pkname;
	}
	public void setPkname(String pkname) {
		this.pkname = pkname;
	}
	public int getDatablocks() {
		return datablocks;
	}
	public void setDatablocks(int datablocks) {
		this.datablocks = datablocks;
	}
	public String getHdfsserver() {
		return hdfsserver;
	}
	public void setHdfsserver(String hdfsserver) {
		this.hdfsserver = hdfsserver;
	}
	public String getHdfsdatadirpath() {
		return hdfsdatadirpath;
	}
	public void setHdfsdatadirpath(String hdfsdatadirpath) {
		this.hdfsdatadirpath = hdfsdatadirpath;
	}
	public String getLocaldirpath() {
		return localdirpath;
	}
	public void setLocaldirpath(String localdirpath) {
		this.localdirpath = localdirpath;
	}
	public String getFilebasename() {
		return filebasename;
	}
	public void setFilebasename(String filebasename) {
		this.filebasename = filebasename;
	}
	public int getGeneworkthreads() {
		return geneworkthreads;
	}
	public void setGeneworkthreads(int geneworkthreads) {
		this.geneworkthreads = geneworkthreads;
	}
	public int getUploadeworkthreads() {
		return uploadeworkthreads;
	}
	public void setUploadeworkthreads(int uploadeworkthreads) {
		this.uploadeworkthreads = uploadeworkthreads;
	}
	public int getGenqueques() {
		return genqueques;
	}
	public void setGenqueques(int genqueques) {
		this.genqueques = genqueques;
	}
	public int getUploadqueues() {
		return uploadqueues;
	}
	public void setUploadqueues(int uploadqueues) {
		this.uploadqueues = uploadqueues;
	}
	public int getGenquequetimewait() {
		return genquequetimewait;
	}
	public void setGenquequetimewait(int genquequetimewait) {
		this.genquequetimewait = genquequetimewait;
	}
	public int getUploadqueuetimewait() {
		return uploadqueuetimewait;
	}
	public void setUploadqueuetimewait(int uploadqueuetimewait) {
		this.uploadqueuetimewait = uploadqueuetimewait;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public TaskInfo[] getTasks() {
		return tasks;
	}
	public void setTasks(TaskInfo[] tasks) {
		this.tasks = tasks;
	}
	public long getPagesize() {
		return pagesize;
	}
	public void setPagesize(long pagesize) {
		this.pagesize = pagesize;
	}
	public boolean isGenlocalfile() {
		return genlocalfile;
	}
	public void setGenlocalfile(boolean genlocalfile) {
		this.genlocalfile = genlocalfile;
	}
	public String getQuerystatement() {
		return querystatement;
	}
	public void setQuerystatement(String querystatement) {
		this.querystatement = querystatement;
	}
 
	public String getLimitstatement() {
		return limitstatement;
	}
	public void setLimitstatement(String limitstatement) {
		this.limitstatement = limitstatement;
	}
	public String getCountstatement() {
		return countstatement;
	}
	public void setCountstatement(String countstatement) {
		this.countstatement = countstatement;
	}
	public long getTablerows() {
		return tablerows;
	}
	public void setTablerows(long tablerows) {
		this.tablerows = tablerows;
	}
	public boolean isUsepagine() {
		return usepagine;
	}
	public void setUsepagine(boolean usepagine) {
		this.usepagine = usepagine;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getPageinestatement() {
		return pageinestatement;
	}
	public void setPageinestatement(String pageinestatement) {
		this.pageinestatement = pageinestatement;
	}
	public boolean isAdminnodeasdatanode() {
		return adminnodeasdatanode;
	}
	public void setAdminnodeasdatanode(boolean adminnodeasdatanode) {
		this.adminnodeasdatanode = adminnodeasdatanode;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public int getSubblocks() {
		return subblocks;
	}
	public void setSubblocks(int subblocks) {
		this.subblocks = subblocks;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getDburl() {
		return dburl;
	}
	public void setDburl(String dburl) {
		this.dburl = dburl;
	}
	public String getDbpassword() {
		return dbpassword;
	}
	public void setDbpassword(String dbpassword) {
		this.dbpassword = dbpassword;
	}
	public String getDbuser() {
		return dbuser;
	}
	public void setDbuser(String dbuser) {
		this.dbuser = dbuser;
	}
	public String getValidatesql() {
		return validatesql;
	}
	public void setValidatesql(String validatesql) {
		this.validatesql = validatesql;
	}
	public boolean isClearhdfsfiles() {
		return clearhdfsfiles;
	}
	public void setClearhdfsfiles(boolean clearhdfsfiles) {
		this.clearhdfsfiles = clearhdfsfiles;
	}
	public long getStartid() {
		return startid;
	}
	public void setStartid(long startid) {
		this.startid = startid;
	}
	public long getEndid() {
		return endid;
	}
	public void setEndid(long endid) {
		this.endid = endid;
	}
	public String getBlocks() {
		return blocks;
	}
	public void setBlocks(String blocks) {
		this.blocks = blocks;
	}
	public Map<String, List<Integer>> getBlocksplits() {
		return blocksplits;
	}
	public void setBlocksplits(Map<String, List<Integer>> blocksplits) {
		this.blocksplits = blocksplits;
	}
	public boolean isUsepool() {
		return usepool;
	}
	public void setUsepool(boolean usepool) {
		this.usepool = usepool;
	}
	public String getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
}
