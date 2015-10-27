/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    *
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *  Author of Learning Java 						     					 *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.util;

/**
 * @author biaoping.yin
 * 文件处理实用类
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.frameworkset.cache.FileContentCache;

public class FileUtil 
{
	private static Logger log = Logger.getLogger(FileContentCache.class);
	private static final ListResourceBundle mimeTypes = new FileMIMETypes();
	public static final String apppath;
	static{
		 URL location = (FileUtil.class).getProtectionDomain().getCodeSource().getLocation();
	        File appDir = computeApplicationDir(location, new File("."));
		apppath = appDir.getParentFile().getPath();
	}
	
	/**
	 * 获取页面类型
	 * @param fileName
	 * @return
	 */
	public static String getMimeType(String fileExt) {
		try {
			return mimeTypes.getString(fileExt);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 获取页面类型
	 * @param fileName
	 * @return
	 */
	public static String getMimeTypeByleName(String fileName) {
		try {
			String fileExt = FileUtil.getFileExtByFileName(fileName);			
			return mimeTypes.getString(fileExt);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * determine the OS name
	 * 
	 * @return The name of the OS
	 */
	public static final String getOS() {
		return System.getProperty("os.name");
	}

	/**
	 * @return True if the OS is a Windows derivate.
	 */
	public static final boolean isWindows() {
		return getOS().startsWith("Windows");
	}

	/**
	 * @return True if the OS is a Linux derivate.
	 */
	public static final boolean isLinux() {
		return getOS().startsWith("Linux");
	}
	private static File computeApplicationDir(URL location, File defaultDir)
    {
        if (location == null)
        {
            System.out.println("Warning: Cannot locate the program directory. Assuming default.");
            return defaultDir;
        }
        if (!"file".equalsIgnoreCase(location.getProtocol()))
        {
            System.out.println("Warning: Unrecognized location type. Assuming default.");
            return new File(".");
        }
        String file = location.getFile();
        if (!file.endsWith(".jar") && !file.endsWith(".zip"))
        {
            try
            {
                return (new File(URLDecoder.decode(location.getFile(), "UTF-8"))).getParentFile().getParentFile();
            }
            catch (UnsupportedEncodingException e)
            {

            }

            System.out.println("Warning: Unrecognized location type. Assuming default.");
            return new File(location.getFile());
        }
        else
        {
            
            try
            {
                File path = null;//new File(URLDecoder.decode(location.toExternalForm().substring(6), "UTF-8")).getParentFile();
                if(!isLinux())
                {
                	path = new File(URLDecoder.decode(location.toExternalForm().substring(6), "UTF-8")).getParentFile().getParentFile();
                }
                else
                {
                	path = new File(URLDecoder.decode(location.toExternalForm().substring(5), "UTF-8")).getParentFile().getParentFile();
                }
//                System.out.println("path: " + path.getAbsolutePath());
//                System.out.println("location: " + location.getPath());
//                System.out.println("external from location: " + URLDecoder.decode(location.toExternalForm().substring(6), "UTF-8"));
//                System.out.println("external from location + 6: " + URLDecoder.decode(location.toExternalForm(), "UTF-8"));
                
                return path;
            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        System.out.println("Warning: Unrecognized location type. Assuming default.");
        return new File(location.getFile());
    }
    public FileUtil()
    {
    }

    /**
     * Description:读取文件的内容，将其保存在StringBuffer对象中返回，
     * 
     * @param file
     * @return StringBuffer
     * @throws Exception
     *             StringBuffer
     */
    public static StringBuffer read(String file) throws Exception
    {
        BufferedReader in = null;
        StringBuffer sb = new StringBuffer();
        String s = null;
        StringBuffer stringbuffer;
        try
        {
            in = new BufferedReader(new FileReader(file));
            while ((s = in.readLine()) != null)
                sb.append(s).append('\n');
            stringbuffer = sb;
        }
        finally
        {
            if (in != null)
                in.close();
        }
        return stringbuffer;
    }

    /**
     * Description:获取属性文件的类容
     * 
     * @param propsFile
     * @return Properties
     * @throws Exception
     *             Properties
     */
    public static Properties getProperties(String propsFile) throws Exception
    {
        return getProperties(propsFile, false);
    }

    /**
     * Description:获取属性文件的内容，并且根据addToSystemProps的值是否装载系统属性
     * 
     * @param propsFile
     * @param addToSystemProps
     *            true:装载系统属性，false不装载系统属性
     * @return Properties
     * @throws Exception
     *             Properties
     */
    public static Properties getProperties(String propsFile, boolean addToSystemProps) throws Exception
    {
        FileInputStream fis = null;
        Properties props = null;
        try
        {
            fis = new FileInputStream(propsFile);
            props = addToSystemProps ? new Properties(System.getProperties()) : new Properties();
            props.load(fis);
            fis.close();
        }
        finally
        {
            if (fis != null)
                fis.close();
        }
        return props;
    }

    public static File createNewFile(String filePath)
    {
        File file = new File(filePath);
        if (file.exists())
            return file;
        File dir = file.getParentFile();
        if (!dir.exists())
            dir.mkdirs();
        try
        {
            file.createNewFile();
            return file;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return file;

    }

    public static File createNewFileOnExist(String filePath)
    {
        File file = new File(filePath);
        if (file.exists())
            file.delete();
        File dir = file.getParentFile();
        if (!dir.exists())
            dir.mkdirs();
        try
        {
            file.createNewFile();
            return file;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return file;

    }

    public static File createNewDirectory(String directorPath)
    {
        File dir = new File(directorPath);
        if (dir.exists())
            return dir;
        dir.mkdirs();
        return dir;
    }

    public static void copy(File sourceFile, String destinction) throws IOException
    {
        // File sourceFile = new File(source);
        if (!sourceFile.exists())
            return;
        File dest_f = new File(destinction);
        if (!dest_f.exists())
            dest_f.mkdirs();

        if (sourceFile.isDirectory())
        {
            java.io.File[] files = sourceFile.listFiles();
            for (int i = 0; files != null && i < files.length; i++)
            {
                File temp = files[i];
                if (temp.isDirectory())
                {
                    String fileName = temp.getName();
                    copy(temp, destinction + "/" + fileName);
                }
                else
                {
                    fileCopy(temp.getAbsolutePath(), destinction + "/" + temp.getName());
                }

            }
        }
        else
        {
            File destinctionFile = new File(destinction);
            if (!destinctionFile.exists())
            {
                destinctionFile.mkdirs();
            }
            String dest = destinction + "/" + sourceFile.getName();
            fileCopy(sourceFile, dest);
        }

        // File destinctionFile = new File(destinction);
        // if (destinctionFile.exists())
        // ;

    }

    /**
     * 目录拷贝,用于对目录的所有文件和子目录进行递归拷贝
     * 
     * @param source
     * @param destinction
     *            必须为目录
     * @throws IOException
     */
    public static void copy(String source, String destinction) throws IOException
    {
        File sourceFile = new File(source);
        copy(sourceFile, destinction);

    }

    public static void makeFile(String destinctionFile)
    {
        File f = new File(destinctionFile);
        File pf = f.getParentFile();
        if (f.exists())
            return;
        if (!pf.exists())
        {
            pf.mkdirs();
        }

        try
        {
            f.createNewFile();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void fileCopy(String sourcefile, String destinctionFile) throws IOException
    {
        fileCopy(new File(sourcefile), destinctionFile);
    }

    public static void fileCopy(File sourcefile, String destinctionFile) throws IOException
    {

        FileInputStream stFileInputStream = null;

        FileOutputStream stFileOutputStream = null;

        try
        {
            makeFile(destinctionFile);

            stFileInputStream = new FileInputStream(sourcefile);

            stFileOutputStream = new FileOutputStream(destinctionFile);

            int arraySize = 1024;
            byte buffer[] = new byte[arraySize];
            int bytesRead;
            while ((bytesRead = stFileInputStream.read(buffer)) != -1)
            {
                stFileOutputStream.write(buffer, 0, bytesRead);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (stFileInputStream != null)
                try
                {
                    stFileInputStream.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (stFileOutputStream != null)
                try
                {
                    stFileOutputStream.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

    }
    
    
    public static void fileCopy(File sourcefile, File destinctionFile) throws IOException
    {

        FileInputStream stFileInputStream = null;

        FileOutputStream stFileOutputStream = null;

        try
        {
//            makeFile(destinctionFile);

            stFileInputStream = new FileInputStream(sourcefile);

            stFileOutputStream = new FileOutputStream(destinctionFile);

            int arraySize = 1024;
            byte buffer[] = new byte[arraySize];
            int bytesRead;
            while ((bytesRead = stFileInputStream.read(buffer)) != -1)
            {
                stFileOutputStream.write(buffer, 0, bytesRead);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (stFileInputStream != null)
                try
                {
                    stFileInputStream.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (stFileOutputStream != null)
                try
                {
                    stFileOutputStream.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

    }
    
    /**
     * 获取文件得内容
     * 
     * @param filePath
     *            文件得物理路径
     * @return
     * @throws IOException
     */
    public static String getFileContent(String filePath,String charSet) throws IOException
    {
    	return getFileContent(new File( filePath),charSet);
    }
    
    
    /**
     * 获取文件得内容
     * 
     * @param filePath
     *            文件得物理路径
     * @return
     * @throws IOException
     */
    public static String getFileContent(File file,String charSet) throws IOException
    {
    	ByteArrayOutputStream swriter = null;
        OutputStream temp = null;
        InputStream reader = null;
        try
        {
        	reader = new FileInputStream(file);
        	swriter = new ByteArrayOutputStream();
        	temp = new BufferedOutputStream(swriter);

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = reader.read(buffer)) > 0)
            {
            	temp.write(buffer, 0, len);
            }
            temp.flush();
            if(charSet != null && !charSet.equals(""))
            	return swriter.toString(charSet);
            else
            	return swriter.toString();
        }
        catch (FileNotFoundException e)
        {
           log.error("Get File Content Error:", e);
            return "";
        }
        catch (IOException e)
        {
        	log.error("Get File Content Error:", e);
            throw e;
        }
        finally
        {
            if (reader != null)
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                }
            if (swriter != null)
                try
                {
                    swriter.close();
                }
                catch (IOException e)
                {
                }
            if (temp != null)
                try
                {
                	temp.close();
                }
                catch (IOException e)
                {
                }
        }
    }
    
    public static void main(String[] args)
    {
    	try {
			System.out.println(FileUtil.getFileContent("D:\\workspace\\bbossgroup-2.0-RC2\\bboss-mvc/WebRoot/jsp/databind/table.jsp","UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private static final int EOF = -1;

    // 获取指定路径和文件后缀名的文件名列表
    public Vector getFileNames(String pathName, String suffix) throws Exception
    {
        Vector v = new Vector();
        String[] fileNames = null;

        File file = new File(pathName);

        fileNames = file.list();
        if (fileNames == null)
            throw new Exception();

        for (int i = 0; i < fileNames.length; i++)
        {
            if (suffix.equals("*") || fileNames[i].toLowerCase().endsWith(suffix.toLowerCase()))
                v.addElement(fileNames[i]);
        }

        return v;
    }

    /**
     * 删除文件目录下的所有子文件和子目录，操作一定要小心
     * 
     * @param publishTemppath
     */
    public static void deleteFile(String path)
    {
        File file = new File(path);
        if (!file.exists() || file.isFile())
            return;
        if (file.isDirectory())
            deleteSubfiles(file.getAbsolutePath());

        file.delete();

    }
    
    /**
     * 删除文件目录下的所有子文件和子目录，操作一定要小心
     * 
     * @param publishTemppath
     */
    public static void removeFileOrDirectory(String path)
    {
        File file = new File(path);
        if (!file.exists())
            return;
        if (file.isDirectory())
            deleteSubfiles(file.getAbsolutePath());

        file.delete();

    }

    /**
     * 只删除目标文件
     * 
     * @param path
     *            文件绝对路径
     * @author da.wei200710171007
     */
    public static void deleteFileOnly(String path)
    {
        File file = new File(path);
        if (file.exists() && file.isFile())
        {
            file.delete();
        }
    }
    
    

    /**
     * 移动文件
     */
    public static void moveFile(String sourceFileName, String destPath) throws Exception
    {
        File src = new File(sourceFileName);
        // File dest = new File(destPath );
        if (!src.exists())
        {
            throw new Exception("save file[" + sourceFileName + "] to file[" + destPath + "] failed:" + sourceFileName
                    + " not exist.");
        }
        // if (dest.exists()) {
        // if (!dest.delete())
        // {
        // System.out.println("delete dest file failed:" +
        // dest.getAbsolutePath()
        // + " the file is read= " + dest.canRead()
        // + " the file is write= " + dest.canWrite());
        // }
        // // throw new FileMoveException(
        // // "Dest file already exists,delete fail!");
        // }
        // src = null;
        // dest = null;
        try
        {
            FileUtil.fileCopy(sourceFileName, destPath);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            System.out.println("save file[" + sourceFileName + "] to file[" + destPath + "]" + e.getMessage());
            e.printStackTrace();
        }

        // if (!src.renameTo(dest))
        // {
        // try {
        // System.setOut(new java.io.PrintStream(new
        // java.io.FileOutputStream(new File("d:/test.log"))));
        // } catch (FileNotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        //                  
        // System.out.println("src.getAbsolutePath():" + src.getAbsolutePath());
        // System.out.println("src.exist():" + src.exists());
        // System.out.println("dest.getAbsolutePath():" +
        // dest.getAbsolutePath());
        // System.out.println("dest.exist():" + dest.exists());
        // // Runtime.getRuntime().halt(1);
        //                  
        // throw new FileMoveException("Move file fail!");
        // }
    }

    
    /**
     * 重命名文件，原来的文件会被删除
     * @param source
     * @param dest
     */
    public static void renameFile(String source,String dest)
    {
        File file = new File(source);
        file.renameTo(new File(dest));
    }
    
    /**
     * 重命名文件，原来的文件会被删除
     * @param source
     * @param dest
     */
    public static void renameFile(File source,File dest)
    {
     
        source.renameTo(dest);
    }
    
    /**
     * 备份文件，删除原来的文件
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void bakFile(String source, String dest) throws IOException{
    	File file = new File(source);
        boolean state = file.renameTo(new File(dest));
        if(!state){
        	//将文件source拷贝到dest
        	fileCopy(source,dest);
        	//删除source文件
        	deleteFileOnly(source);
        }
    }
    
    

    public static void moveSubFiles(String sourceFileName, String destPath)
    {
        File src = new File(sourceFileName);
        File dest = new File(destPath);
        if (!dest.exists())
            dest.mkdirs();
        if (src.isFile())
            return;
        else
        {
            File[] files = src.listFiles();
            String destFile = null;
            for (int i = 0; files != null && i < files.length; i++)
            {
                if (files[i].isDirectory())
                {
                    String temp_name = files[i].getName();
                    try
                    {
                        moveSubFiles(files[i].getAbsolutePath(), destPath + "/" + temp_name);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        continue;
                    }
                }
                else
                {

                    destFile = destPath + "/" + files[i].getName();
                    try
                    {
                        moveFile(files[i].getAbsolutePath(), destFile);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static List upzip(ZipInputStream zip, String destPath) throws ZipException, IOException
    {
        List fileNames = new ArrayList();
        ZipEntry azipfile = null;
        while ((azipfile = zip.getNextEntry()) != null)
        {
            // String name = new
            // String(azipfile.getName().getBytes("UTF-8"),"GBK");
            String name = azipfile.getName();
            fileNames.add(name);
            if (!azipfile.isDirectory())
            {
                File targetFile = new File(destPath, name);
                targetFile.getParentFile().mkdirs();
                if (targetFile.exists())
                {
                    targetFile.delete();
                }
                targetFile.createNewFile();
                BufferedOutputStream diskfile = null;
                FileOutputStream out = null;
                try
                {
                	out = new FileOutputStream(targetFile);
	                diskfile = new BufferedOutputStream(out);
	                byte[] buffer = new byte[1024];
	                int read;
	                while ((read = zip.read(buffer)) != -1)
	                {
	                    diskfile.write(buffer, 0, read);
	                }
	                diskfile.flush();
	                
                }
                catch(Exception e)
                {
                	e.printStackTrace();
                }
                finally
                {
                	try {
						if(out != null)
							out.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	try {
						if(diskfile != null)
							diskfile.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        }
        return fileNames;
    }

    /**
     * 将zip文件解压到destPath路径下面
     * 
     * @param sourceFileName
     * @param destPath
     * @return
     * @throws ZipException
     * @throws IOException
     *             FileUtil.java
     * @author: ge.tao
     */

    public static void unzip(String sourceFileName, String destPath) throws ZipException, IOException
    {
    	ZipFile zf = null;
//        if (sourceFileName.endsWith(".zip") || sourceFileName.endsWith(".war"))
        try
        {
            zf = new ZipFile(sourceFileName);
            Enumeration en = zf.entries();
          

            while (en.hasMoreElements())
            {
                ZipEntry zipEnt = (ZipEntry) en.nextElement();
                saveEntry(destPath, zipEnt, zf);
               
            }
            
           
        }
        finally
        {
        	if(zf != null)
        		zf.close();
        }
       
        
    }

    public static void saveEntry(String destPath, ZipEntry target, ZipFile zf) throws ZipException, IOException
    {
    	 InputStream is = null;
    	 BufferedInputStream bis =  null;
    	 FileOutputStream fos = null;
    	 BufferedOutputStream bos = null;
        try
        {
            File file = new File(destPath + "/" + target.getName());
            if (target.isDirectory())
            {
                file.mkdirs();
            }
            else
            {
                is = zf.getInputStream(target);
                bis = new BufferedInputStream(is);
                File dir = new File(file.getParent());
                dir.mkdirs();
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);

                int c;
                while ((c = bis.read()) != EOF)
                {
                    bos.write((byte) c);
                }
                bos.flush();
            }
        }
        catch (ZipException e)
        {
            throw e;
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
        	try {
				if(bis != null)
					bis.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	try {
				if(is != null)
					is.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	try {
				if(bos != null)
					 bos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	try {
				if(fos != null)
					 fos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    // 创建目录
    public static boolean createDir(String dirName)
    {
        File file = new File(dirName);
        if (!file.exists())
            return file.mkdir();
        return true;
    }

    //
    public static void createFile(String fileName) throws IOException
    {
        File file = new File(fileName);

        if (!file.exists())
        {
            if (!file.createNewFile())
                throw new IOException("Create file fail!");
        }

    }

    public static void writeFile(String fileName, String text) throws IOException
    {
        FileWriter fw = new FileWriter(fileName, true);
        try
        {
            fw.write(text, 0, text.length());
        }
        catch (IOException ioe)
        {
            throw new IOException("Write text to " + fileName + " fail!");
        }
        finally
        {
            fw.close();
        }

    }

    public static void writeFile(String fileName, String text, boolean isAppend) throws IOException
    {
        FileWriter fw = new FileWriter(fileName, isAppend);
        try
        {
            fw.write(text, 0, text.length());
        }
        catch (IOException ioe)
        {
            throw new IOException("Write text to " + fileName + " fail!");
        }
        finally
        {
            fw.close();
        }

    }

    /**
     * 删除文件目录下的所有子文件和子目录，操作一定要小心
     * 
     * @param publishTemppath
     */
    public static void deleteSubfiles(String publishTemppath)
    {
        File file = new File(publishTemppath);
        if (!file.exists() || file.isFile())
            return;
        File[] files = file.listFiles();
        for (int i = 0; files != null && i < files.length; i++)
        {
            File temp = files[i];
            if (temp.isDirectory())
            {
                deleteSubfiles(temp.getAbsolutePath());
            }
            temp.delete();
        }

    }

    public static String getFileExtByFileName(String fileName)
    {
        if (fileName == null)
            return "";
        else
        {
            int idx = fileName.lastIndexOf(".");
            if (idx != -1)
                return fileName.substring(idx + 1);
            else return "";
        }
    }

    /**
     * 获取文件得内容
     * 
     * @param filePath
     *            文件得物理路径
     * @return
     * @throws IOException
     */
    public static String getFileContent(String filePath) throws IOException
    {
        Writer swriter = null;
        Reader reader = null;
        try
        {
            reader = new FileReader(filePath);
            
            swriter = new StringWriter();

            int len = 0;
            char[] buffer = new char[1024];
            while ((len = reader.read(buffer)) != -1)
            {
                swriter.write(buffer, 0, len);
            }
            return swriter.toString();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return "";
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            if (reader != null)
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                }
            if (swriter != null)
                try
                {
                    swriter.close();
                }
                catch (IOException e)
                {
                }
        }
    }

    /**
     * 
     * @param path
     * @return
     */
    public static boolean hasSubDirectory(String path, String uri)
    {
        File file = null;
        if (uri == null || uri.trim().length() == 0)
        {
            file = new File(path);
        }
        else
        {
            file = new File(path, uri);
        }

        if (!file.exists() || file.isFile())
            return false;
        File[] subFiles = file.listFiles(new FileFilter()
        {

            public boolean accept(File pathname)
            {
                if (pathname.isDirectory())
                    return true;
                else return false;
            }

        });

        return subFiles.length > 0;

    }

    /**
     * 
     * @param path
     * @return
     */
    public static boolean hasSubDirectory(String path)
    {
        return hasSubDirectory(path, null);
    }

    /**
     * 
     * @param path
     * @return
     */
    public static boolean hasSubFiles(String path, String uri)
    {
        File file = new File(path, uri);
        if (!file.exists() || file.isFile())
            return false;
        File[] subFiles = file.listFiles(new FileFilter()
        {

            public boolean accept(File pathname)
            {
                if (!pathname.isDirectory())
                    return true;
                else return false;
            }

        });

        return subFiles.length > 0;

    }

    /**
     * 
     * @param path
     * @return
     */
    public static boolean hasSubFiles(String path)
    {
        File file = new File(path);
        if (!file.exists() || file.isFile())
            return false;
        File[] subFiles = file.listFiles(new FileFilter()
        {

            public boolean accept(File pathname)
            {
                if (!pathname.isDirectory())
                    return true;
                else return false;
            }

        });

        return subFiles.length > 0;

    }

    public static File[] getSubDirectories(String parentpath, String uri)
    {
        File file = null;
        if (uri == null || uri.trim().length() == 0)
        {
            file = new File(parentpath);
        }
        else
        {
            file = new File(parentpath, uri);
        }
        if (!file.exists() || file.isFile())
            return null;
        File[] subFiles = file.listFiles(new FileFilter()
        {

            public boolean accept(File pathname)
            {
                if (pathname.isDirectory())
                    return true;
                else return false;
            }

        });
        return subFiles;
    }

    public static File[] getSubDirectories(String parentpath)
    {
        return getSubDirectories(parentpath, null);
    }

    /**
     * 获取某个路径下的所有文件(不包括文件夹)
     */
    public static File[] getSubFiles(String parentpath)
    {
        return getSubFiles(parentpath, (String) null);
    }

    /**
     * 获取某个路径下的所有文件(不包括文件夹)
     */
    public static File[] getSubFiles(String parentpath, String uri)
    {
        File file = null;
        if (uri == null || uri.trim().length() == 0)
        {
            file = new File(parentpath);
        }
        else
        {
            file = new File(parentpath, uri);
        }
        if (!file.exists() || file.isFile())
            return null;
        File[] subFiles = file.listFiles(new FileFilter()
        {
            public boolean accept(File pathname)
            {
                if (pathname.isFile())
                    return true;
                else return false;
            }

        });
        return subFiles;
    }

    public static File[] getSubFiles(String parentpath, FileFilter fileFilter)
    {
        return getSubFiles(parentpath, null, fileFilter);
    }

    public static File[] getSubFiles(String parentpath, String uri, FileFilter fileFilter)
    {
        File file = null;
        if (uri == null || uri.trim().length() == 0)
        {
            file = new File(parentpath);
        }
        else
        {
            file = new File(parentpath, uri);
        }
        if (!file.exists() || file.isFile())
            return null;

        File[] files = null;
        if (fileFilter != null)
        {
            files = file.listFiles(fileFilter);
        }
        else
        {
            files = file.listFiles();
        }

        // 预防传递进来的FileFilter没有把文件过滤掉
        int rLen = 0;
        for (int i = 0; files != null && i < files.length; i++)
        {
            if (files[i].isFile())
            {
                files[rLen] = files[i];
                rLen++;
            }
        }
        File[] r = new File[rLen];
        System.arraycopy(files, 0, r, 0, rLen);
        return r;
    }

    /**
     * 参考getSubDirectorieAndFiles(String parentpath,String uri,FileFilter
     * fileFilter)方法
     */
    public static File[] getSubDirectorieAndFiles(String parentpath)
    {
        return getSubDirectorieAndFiles(parentpath, null, null);
    }

    /**
     * 参考getSubDirectorieAndFiles(String parentpath,String uri,FileFilter
     * fileFilter)方法
     */
    public static File[] getSubDirectorieAndFiles(String parentpath, String uri)
    {
        return getSubDirectorieAndFiles(parentpath, uri, null);
    }

    /**
     * 参考getSubDirectorieAndFiles(String parentpath,String uri,FileFilter
     * fileFilter)方法
     */
    public static File[] getSubDirectorieAndFiles(String parentpath, FileFilter fileFilter)
    {
        return getSubDirectorieAndFiles(parentpath, null, fileFilter);
    }

    /**
     * 获取某个路径下的文件
     * 
     * @param parentpath
     *            绝对路径
     * @param uri
     *            相对与 parentpath的相对路径
     * @param fileFilter
     *            过滤某些文件,这个权力交给了使用该方法的用户
     * @return
     */
    public static File[] getSubDirectorieAndFiles(String parentpath, String uri, FileFilter fileFilter)
    {
        File file = null;
        if (uri == null || uri.trim().length() == 0)
        {
            file = new File(parentpath);
        }
        else
        {
            file = new File(parentpath, uri);
        }
        if (!file.exists() || file.isFile())
            return null;
        if (fileFilter != null)
        {
            return file.listFiles(fileFilter);
        }
        else
        {
            return file.listFiles();
        }
    }

    public static String getFileContent(File file)
    {
        Writer swriter = null;
        Reader reader = null;
        try
        {
            reader = new FileReader(file);
            swriter = new StringWriter();

            int len = 0;
            char[] buffer = new char[1024];
            while ((len = reader.read(buffer)) > 0)
            {
                swriter.write(buffer, 0, len);
            }
            swriter.flush();
            return swriter.toString();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return "";
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "";
        }
        finally
        {
            if (reader != null)
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                }
            if (swriter != null)
                try
                {
                    swriter.close();
                }
                catch (IOException e)
                {
                }
        }

    }

    /**
     * 从输入流中读取字节数组
     * 
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] readFully(InputStream in) throws IOException
    {

        if (in instanceof ByteArrayInputStream)
        {
            // content can be read in one pass
            int size = in.available();
            byte[] bytes = new byte[size];

            // read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < size)
            {
                numRead = in.read(bytes, offset, size - offset);
                if (numRead >= 0)
                {
                    offset += numRead;
                }
                else
                {
                    break;
                }
            }
            return bytes;
        }

        // copy buffer
        byte[] xfer = new byte[2048];
        // output buffer
        ByteArrayOutputStream out = new ByteArrayOutputStream(xfer.length);

        // transfer data from input to output in xfer-sized chunks.
        for (int bytesRead = in.read(xfer, 0, xfer.length); bytesRead >= 0; bytesRead = in.read(xfer, 0, xfer.length))
        {
            if (bytesRead > 0)
            {
                out.write(xfer, 0, bytesRead);
            }
        }
        in.close();
        out.close();
        return out.toByteArray();
    }

    // private static final Category cat;
    //
    // static
    // {
    // cat = Category.getInstance(com.pow2.util.FileUtil.class);
    // }
    private static void zipFile(File source, String basePath, ZipOutputStream zos) throws IOException {
		File[] files = null;
		if (source.isDirectory()) {
			files = source.listFiles();
		} else {
			files = new File[1];
			files[0] = source;
		}

		
		String pathName;
		byte[] buf = new byte[1024];
		int length = 0;
		try {
			for (File file : files) {
				if (file.isDirectory()) {
					pathName = file.getPath().substring(basePath.length() + 1) + "/";
					zos.putNextEntry(new ZipEntry(pathName));
					zipFile(file, basePath, zos);
				} else {
					pathName = file.getPath().substring(basePath.length() + 1);
					
					BufferedInputStream bis = null;
					InputStream is = null;
					try
					{
						is = new FileInputStream(file);
						bis = new BufferedInputStream(is);
						zos.putNextEntry(new ZipEntry(pathName));
						while ((length = bis.read(buf)) > 0) {
							zos.write(buf, 0, length);
						}
					}
					finally
					{
						if (is != null) {
							try {
								is.close();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (bis != null) {
							try {
								bis.close();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		} finally {
			
		}

	}
    public static File zip(File f,File destfile)
    {
    	java.io.BufferedOutputStream out = null;
		ZipOutputStream zipOut = null;
		try {
			 
			out = new BufferedOutputStream(new FileOutputStream(
					destfile));

			zipOut = new ZipOutputStream(out);

			String basePath;

			if(f.isDirectory())
				basePath = f.getPath();
			else
				basePath = f.getParent();

			zipFile(f, basePath, zipOut);
			
			return destfile;

		} catch (IOException e) {
			 
			return null;
		}
		finally
		{
			if(zipOut != null)
				try {
					zipOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
    }
    public static File zip(File f,String destfile)
    {
    	java.io.BufferedOutputStream out = null;
		ZipOutputStream zipOut = null;
		try {
			File ret = new File(destfile);
			out = new BufferedOutputStream(new FileOutputStream(
					ret));

			zipOut = new ZipOutputStream(out);

			String basePath;

			if(f.isDirectory())
				basePath = f.getPath();
			else
				basePath = f.getParent();

			zipFile(f, basePath, zipOut);
			
			return ret;

		} catch (IOException e) {
			 
			return null;
		}
		finally
		{
			if(zipOut != null)
				try {
					zipOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}
    
}
