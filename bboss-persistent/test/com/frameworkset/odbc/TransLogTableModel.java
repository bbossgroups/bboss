package com.frameworkset.odbc;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TransLogTableModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1209003599502015062L;
	private int ID_BATCH;
	private String TRANSNAME;
	private String STATUS;
	private int LINES_READ;
	private int LINES_WRITTEN;
	private int LINES_UPDATED;
	private int LINES_INPUT;
	private int LINES_OUTPUT;
	private int ERRORS;
	private Date STARTDATE;
	private Date ENDDATE;
	private Date LOGDATE;
	private Date DEPDATE;
	private Date REPLAYDATE;
	private String LOG_FIELD;
	
	private List stepLogTotalList;
	
	public List getStepLogTotalList() {
		return stepLogTotalList;
	}
	public void setStepLogTotalList(List stepLogTotalList) {
		this.stepLogTotalList = stepLogTotalList;
	}
	public String getLOG_FIELD() {
		return LOG_FIELD;
	}
	public void setLOG_FIELD(String log_field) {
		LOG_FIELD = log_field;
	}
	public int getID_BATCH() {
		return ID_BATCH;
	}
	public void setID_BATCH(int id_batch) {
		ID_BATCH = id_batch;
	}
	public String getTRANSNAME() {
		return TRANSNAME;
	}
	public void setTRANSNAME(String transname) {
		TRANSNAME = transname;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String status) {
		STATUS = status;
	}
	public int getLINES_READ() {
		return LINES_READ;
	}
	public void setLINES_READ(int lines_read) {
		LINES_READ = lines_read;
	}
	public int getLINES_WRITTEN() {
		return LINES_WRITTEN;
	}
	public void setLINES_WRITTEN(int lines_written) {
		LINES_WRITTEN = lines_written;
	}
	public int getLINES_UPDATED() {
		return LINES_UPDATED;
	}
	public void setLINES_UPDATED(int lines_updated) {
		LINES_UPDATED = lines_updated;
	}
	public int getLINES_INPUT() {
		return LINES_INPUT;
	}
	public void setLINES_INPUT(int lines_input) {
		LINES_INPUT = lines_input;
	}
	public int getLINES_OUTPUT() {
		return LINES_OUTPUT;
	}
	public void setLINES_OUTPUT(int lines_output) {
		LINES_OUTPUT = lines_output;
	}
	public int getERRORS() {
		return ERRORS;
	}
	public void setERRORS(int errors) {
		ERRORS = errors;
	}
	public Date getSTARTDATE() {
		return STARTDATE;
	}
	public void setSTARTDATE(Date startdate) {
		STARTDATE = startdate;
	}
	public Date getENDDATE() {
		return ENDDATE;
	}
	public void setENDDATE(Date enddate) {
		ENDDATE = enddate;
	}
	public Date getLOGDATE() {
		return LOGDATE;
	}
	public void setLOGDATE(Date logdate) {
		LOGDATE = logdate;
	}
	public Date getDEPDATE() {
		return DEPDATE;
	}
	public void setDEPDATE(Date depdate) {
		DEPDATE = depdate;
	}
	public Date getREPLAYDATE() {
		return REPLAYDATE;
	}
	public void setREPLAYDATE(Date replaydate) {
		REPLAYDATE = replaydate;
	}
}
