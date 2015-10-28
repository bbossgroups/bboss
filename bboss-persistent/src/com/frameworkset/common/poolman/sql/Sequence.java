package com.frameworkset.common.poolman.sql;

import java.io.Serializable;

/**
 * 主键的序号和当前值
 * @author biaoping.yin
 *
 */
public class Sequence {
	private Serializable primaryKey;
	private long sequence;
	public Serializable getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(Serializable primaryKey) {
		this.primaryKey = primaryKey;
	}
	public long getSequence() {
		return sequence;
	}
	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

}
