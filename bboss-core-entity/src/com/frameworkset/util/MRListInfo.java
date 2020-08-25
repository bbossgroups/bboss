package com.frameworkset.util;

import org.frameworkset.util.MoreListInfo;

public class MRListInfo extends RListInfo{


	public MRListInfo() {
	}

	public MRListInfo(ListInfo listInfo) {
		super(listInfo);
	}

	public MRListInfo(ListInfo listInfo, long offset) {
		super(listInfo, offset);
	}

	public MoreListInfo getMoreListInfo()
	{
//		MoreListInfo moreList = new MoreListInfo(listInfo.getResultSize() < listInfo.getMaxPageItems(),listInfo.getResultSize(),listInfo.getDatas());
		return buildMoreListInfo(this);
	}

}
