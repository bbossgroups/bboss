package com.frameworkset.sqlexecutor;

/**
 * <br>
 * 临控指令申请流程表 对应表 T_ZT_ZDRY_LKZLSQLC
 * 
 * @author: <a href="mailto:zhangye.lin@chinacreator.com">林彰叶</a>
 */
public class TlkzlsqlcBean implements java.io.Serializable {
	private String bksqlcbh;// bksqlcbh 临控指令流程编号 varchar2(27) true false true
	private String lkzlbh;// lkzlbh 临控指令编号 varchar2(27) false false false
	private String lkzlspr;// lkzlspr 临控指令审批人 varchar2(10) false false false
	private String lkzlspdwid;// lkzlspdwid 临控指令审批单位编号 varchar2(12) false false false
	private String lkzlspdwmc;// lkzlspdwmc 临控指令审批单位名称 varchar2(70) false false false
	private String lkzlspzt;// lkzlspzt 临控指令审批状态（1、基层领导审批，2、市局领导审批，3、情报中心领导审批） varchar2(1) false false false
	private String lkzlspsj;// lkzlspsj 临控指令审批时间 date false false false
	private String lkzlspyj;// lkzlspyj 临控指令审批意见（0、不同意，1、同意） varchar2(1) false false false
	private String lkzlspyjsm;// lkzlspyjsm 临控指令审批意见说明 varchar2(400) false false false
	public String getBksqlcbh() {
		return bksqlcbh;
	}
	public void setBksqlcbh(String bksqlcbh) {
		this.bksqlcbh = bksqlcbh;
	}
	public String getLkzlbh() {
		return lkzlbh;
	}
	public void setLkzlbh(String lkzlbh) {
		this.lkzlbh = lkzlbh;
	}
	public String getLkzlspr() {
		return lkzlspr;
	}
	public void setLkzlspr(String lkzlspr) {
		this.lkzlspr = lkzlspr;
	}
	public String getLkzlspdwid() {
		return lkzlspdwid;
	}
	public void setLkzlspdwid(String lkzlspdwid) {
		this.lkzlspdwid = lkzlspdwid;
	}
	public String getLkzlspdwmc() {
		return lkzlspdwmc;
	}
	public void setLkzlspdwmc(String lkzlspdwmc) {
		this.lkzlspdwmc = lkzlspdwmc;
	}
	public String getLkzlspzt() {
		return lkzlspzt;
	}
	public void setLkzlspzt(String lkzlspzt) {
		this.lkzlspzt = lkzlspzt;
	}
	public String getLkzlspsj() {
		return lkzlspsj;
	}
	public void setLkzlspsj(String lkzlspsj) {
		this.lkzlspsj = lkzlspsj;
	}
	public String getLkzlspyj() {
		return lkzlspyj;
	}
	public void setLkzlspyj(String lkzlspyj) {
		this.lkzlspyj = lkzlspyj;
	}
	public String getLkzlspyjsm() {
		return lkzlspyjsm;
	}
	public void setLkzlspyjsm(String lkzlspyjsm) {
		this.lkzlspyjsm = lkzlspyjsm;
	}
}
