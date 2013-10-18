package com.frameworkset.common.tag.pager;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.common.poolman.SQLParams;

public class ListInfoDataInfoImpl implements DataInfo {
	com.frameworkset.util.ListInfo listInfo;
	public ListInfoDataInfoImpl(com.frameworkset.util.ListInfo listInfo)
	{
		this.listInfo = listInfo;
	}
	public int getDataSize() {
		// TODO Auto-generated method stub
		return listInfo.getSize();
	}

	public long getItemCount() {
		// TODO Auto-generated method stub
		return listInfo.getTotalSize();
	}

	public Object getListItems() {
		// TODO Auto-generated method stub
		return listInfo.getDatas();
	}

	public Object[] getListItemsFromDB() {
		throw new UnsupportedOperationException("ListInfoDataInfoImpl.getListItemsFromDB()");
	}

	public Object getObjectData() {
		throw new UnsupportedOperationException("ListInfoDataInfoImpl.getObjectData()");
	}

	public Object getPageItems() {
		// TODO Auto-generated method stub
		return listInfo.getDatas();
	}

	public Object[] getPageItemsFromDB() {
		throw new UnsupportedOperationException("ListInfoDataInfoImpl.getPageItemsFromDB()");
	}

	public Class getVOClass() {
//		throw new UnsupportedOperationException("ListInfoDataInfoImpl.getVOClass()");
		return null;
	}

	public void initial(String sortKey, boolean desc, long offSet,
			int pageItemsize, boolean isList, HttpServletRequest request) {
		throw new UnsupportedOperationException("ListInfoDataInfoImpl.initial(String sortKey, boolean desc, long offSet,int pageItemsize, boolean isList, HttpServletRequest request)");

	}

	public void initial(String sql, String dbName, long offSet,
			int pageItemsize, boolean isList, HttpServletRequest request) {
		throw new UnsupportedOperationException("ListInfoDataInfoImpl.initial(String sql, String dbName, long offSet,int pageItemsize, boolean isList, HttpServletRequest request)");

	}

	public void initial(String sql, String dbName, long offSet,
			int pageItemsize, boolean listMode, HttpServletRequest request,
			SQLParams params) {
		throw new UnsupportedOperationException("ListInfoDataInfoImpl.initial(String sql, String dbName, long offSet,int pageItemsize, boolean listMode, HttpServletRequest request,SQLParams params)");

	}
	@Override
	public boolean isMore() {
		return listInfo.isMore();
	}
	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.pager.DataInfo#getDataResultSize()
	 */
	@Override
	public int getDataResultSize() {
		// TODO Auto-generated method stub
		return this.listInfo.getResultSize();
	}

}
