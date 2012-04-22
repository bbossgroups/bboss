package org.frameworkset.upload.dao;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.web.multipart.MultipartFile;

public interface UpLoadDao {
	
	/**
	 * ÉÏ´«¸½¼þ
	 * @param inputStream
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public boolean uploadFile(InputStream inputStream, long size,String filename) throws Exception;
	public void deletefiles() throws Exception;
	public List<HashMap> queryfiles() throws Exception;
	public File getDownloadFile(String fileid) throws Exception;
	public File getDownloadClobFile(String fileid) throws Exception;
	public void downloadFileFromBlob(String fileid,HttpServletRequest request, HttpServletResponse response)
			throws Exception;
	public void downloadFileFromClob(String fileid,HttpServletRequest request, HttpServletResponse response)
	throws Exception;

	public void uploadClobFile(MultipartFile file) throws Exception;
	public List<HashMap> queryclobfiles()throws Exception;
	
	
	
}
