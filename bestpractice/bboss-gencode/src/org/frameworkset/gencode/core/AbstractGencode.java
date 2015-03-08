package org.frameworkset.gencode.core;

public abstract class AbstractGencode {
	protected GencodeServiceImpl gencodeService;
	public AbstractGencode(GencodeServiceImpl gencodeService)
	{
		this.gencodeService = gencodeService;
	}
	public AbstractGencode() {
		// TODO Auto-generated constructor stub
	}

}
