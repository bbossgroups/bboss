package org.frameworkset.mvc;

import java.io.File;
import java.io.FileFilter;



/**
 * 
 * <p>Title: FileUtil</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class FileUtil  {
	/**

	 * 
	 * @param path
	 * @return
	 */
	public static boolean hasSubDirectory(String path)
	{
		return hasSubDirectory(path,null);
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static boolean hasSubDirectory(String path,String uri)
	{
		File file = null;
		if(uri==null || uri.trim().length()==0){
			file = new File(path);
		}else{
			file = new File(path,uri);
		}
		
		if(!file.exists() || file.isFile())
			return false;
		File[] subFiles = file.listFiles(new FileFilter()
				{

					public boolean accept(File pathname) {
						if(pathname.getName().endsWith(".svn"))
							return false;
						if(pathname.isDirectory())
							return true;
						else
							return false;
					}
			
				});
		
		return subFiles.length > 0;
		
	}
	
	public static File[] getSubDirectories(String parentpath,String uri)
	{
		File file = null;
		if(uri==null || uri.trim().length()==0){
			file = new File(parentpath);
		}else{
			file = new File(parentpath,uri);
		}
		if(!file.exists() || file.isFile())
			return null;
		File[] subFiles = file.listFiles(new FileFilter()
				{

					public boolean accept(File pathname) {
						if(pathname.getName().endsWith(".svn"))
							return false;
						if(pathname.isDirectory())
							return true;
						else
							return false;
					}
			
				});
		return subFiles;
	}

	
	public static File[] getSubDirectories(String parentpath)
	{
		return getSubDirectories(parentpath,null);
	}
	
	/**
	 * 获取某个路径下的所有文件(不包括文件夹)
	 */
	public static File[] getSubFiles(String parentpath){
		return getSubFiles(parentpath,(String)null);
	}		
	
	/**
	 * 获取某个路径下的所有文件(不包括文件夹)
	 */	
	public static File[] getSubFiles(String parentpath,String uri)
	{
		File file = null;
		if(uri==null || uri.trim().length()==0){
			file = new File(parentpath);
		}else{
			file = new File(parentpath,uri);
		}
		if(!file.exists() || file.isFile())
			return null;
		File[] subFiles = file.listFiles(new FileFilter()
				{
					public boolean accept(File pathname) {
						if(pathname.getName().endsWith(".svn") || pathname.getName().endsWith(".db"))
							return false;
						if(pathname.isFile())
							return true;
						else
							return false;
					}
			
				});
		return subFiles;
	}
	
	public static File[] getSubFiles(String parentpath,FileFilter fileFilter){
		return getSubFiles(parentpath,null,fileFilter);
	}	
	
	public static File[] getSubFiles(String parentpath,String uri,FileFilter fileFilter)
	{
		File file = null;
		if(uri==null || uri.trim().length()==0){
			file = new File(parentpath);
		}else{
			file = new File(parentpath,uri);
		}
		if(!file.exists() || file.isFile())
			return null;
		
		File[] files = null;
		if(fileFilter!=null){
			files = file.listFiles(fileFilter);
		}else{
			files = file.listFiles();
		}
		
		//预防传递进来的FileFilter没有把文件过滤掉
		int rLen = 0;
		for(int i=0;files!=null&&i<files.length;i++){
			if(files[i].isFile()){
				files[rLen] = files[i]; 
				rLen++;
			}
		}
		File[] r = new File[rLen];
		System.arraycopy(files,0,r,0,rLen);
		return r;
	}
	
	/**
	 * 参考getSubDirectorieAndFiles(String parentpath,String uri,FileFilter fileFilter)方法
	 */
	public static File[] getSubDirectorieAndFiles(String parentpath)
	{
		return getSubDirectorieAndFiles(parentpath,null,null);
	}

	/**
	 * 参考getSubDirectorieAndFiles(String parentpath,String uri,FileFilter fileFilter)方法
	 */
	public static File[] getSubDirectorieAndFiles(String parentpath,String uri)
	{
		return getSubDirectorieAndFiles(parentpath,uri,null);
	}
	
	/**
	 * 参考getSubDirectorieAndFiles(String parentpath,String uri,FileFilter fileFilter)方法
	 */
	public static File[] getSubDirectorieAndFiles(String parentpath,FileFilter fileFilter)
	{
		return getSubDirectorieAndFiles(parentpath,null,fileFilter);
	}
	
	/**
	 * 获取某个路径下的文件
	 * @param parentpath 绝对路径 
	 * @param uri 相对与 parentpath的相对路径
	 * @param fileFilter 过滤某些文件,这个权力交给了使用该方法的用户
	 * @return
	 */
	public static File[] getSubDirectorieAndFiles(String parentpath,String uri,FileFilter fileFilter)
	{
		File file = null;
		if(uri==null || uri.trim().length()==0){
			file = new File(parentpath);
		}else{
			file = new File(parentpath,uri);
		}
		if(!file.exists() || file.isFile())
			return null;
		if(fileFilter!=null){
			return file.listFiles(fileFilter);
		}else{
			return file.listFiles();
		}
	}
}