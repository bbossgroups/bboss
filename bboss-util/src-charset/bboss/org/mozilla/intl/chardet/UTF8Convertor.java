package bboss.org.mozilla.intl.chardet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.frameworkset.util.FileCopyUtils;

import com.frameworkset.util.FileUtil;

public class UTF8Convertor {
	private StringBuffer buffer = new StringBuffer();
	
	public String getUnknown()
	{
		return buffer.toString();
	}
	private boolean contain(String excludedirs[],String name)
	{
		for(int i = 0; excludedirs != null && i < excludedirs.length; i ++)
		{
			if(name.equals(excludedirs[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean endwith(String excludedirs[],String name)
	{
		for(int i = 0; excludedirs != null && i < excludedirs.length; i ++)
		{
			if(name.endsWith(excludedirs[i]))
			{
				return true;
			}
		}
		return false;
	}
	public void convertCharsetToUtf_8(String dir,final String[] excludedirs,final  String includefiles[])
	{
		File root = new File(dir);
		File[] files = root.listFiles(new FileFilter(){

			public boolean accept(File arg0) {
				if(arg0.isDirectory() )
				{
					if(!contain(excludedirs,arg0.getName()))
						return true;
					return false;
				}
				else if(endwith(includefiles,arg0.getName()))
				{
					return true;
				}
				else
				{
					return false;
				}
					
			}
			
		});
		
		for(int i = 0; i < files.length; i ++)
		{
			try {
				if(files[i].isFile())
					doconvert(files[i]);
				else
				{
					convertCharsetToUtf_8(files[i].getCanonicalPath(),excludedirs,includefiles);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void convertCharsetToUtf_8(String dir)
	{
		convertCharsetToUtf_8(dir,
							new String[]{".svn","classes",".settings","文档","dbinit-system","distrib","doc","lib","lib-client","lib-compile"},
							new String[]{".jsp",".java",".js",".css",".txt",".properties",".html",".htm",".tld",".vm",".xml"});
				
	}
	
	
	
	public static class Found
	{
		private boolean found = false;
		private String charset = null;

		public String getCharset() {
			return charset;
		}

		public void setCharset(String charset) {
			this.charset = charset;
		}

		public boolean isFound() {
			return found;
		}

		public void setFound(boolean found) {
			this.found = found;
		}
	}
	
	public static String takecharset(String test) throws UnsupportedEncodingException
	{
		return takecharset(test.getBytes("ISO-8859-1"));
	}
	
	
	public static String takecharset(byte[] test) throws UnsupportedEncodingException
	{
		final Found found = new Found();
		int lang = nsPSMDetector.ALL;
		nsDetector det = new nsDetector(lang);

		// Set an observer...
		// The Notify() will be called when a matching charset is found.

		det.Init(new nsICharsetDetectionObserver() {
			public void Notify(String charset) {
				found.setFound(true);
				found.setCharset(charset);				
			}
		});
	

//		byte[] buf = test.getBytes("ISO-8859-1");
//		int len;
		boolean done = false;
		boolean isAscii = true;

		

			// Check if the stream is only ascii.
			if (isAscii)
				isAscii = det.isAscii(test, test.length);

			// DoIt if non-ascii and not done yet.
			if (!isAscii && !done)
				done = det.DoIt(test, test.length, false);
		
		det.DataEnd();

		if (isAscii) {

			found.setFound(true);
		}
		
		
		if(found.isFound())
		{
			String prob[] = det.getProbableCharsets();
			String charset = found.getCharset();
			if(charset != null )
			{
//				return charset;
				return prob != null && prob.length > 0?prob[0]:charset;
			}
			else
			{
//				boolean isutf8 = false;
//				boolean containgbk = false;
//				for (int i = 0; prob != null && i < prob.length; i++) {
////					System.out.println("Probable Charset = " + prob[i]);
//					if(prob[i].startsWith("UTF-"))
//					{
//						isutf8 = true;
//					}
//					else if(prob[i].startsWith("GB"))
//					{
//						containgbk = true;
//					}
////					if(prob[i].toLowerCase().startsWith("gb"))
////					{
////						String test = FileUtil.getFileContent(f,"GBK");
////						FileCopyUtils.copy(test, new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
////						break;
////					}
//				
//				}
//				if(!isutf8 && containgbk)
//				{
//					return "GBK";
//				}
//				else if(isutf8)
//					return "UTF-8";
//				else	
					return prob != null && prob.length > 0?prob[0]:charset;
			}
			
		}
		else
		{
			String prob[] = det.getProbableCharsets();
			
			boolean isutf8 = false;
			boolean containgbk = false;
			for (int i = 0; prob != null && i < prob.length; i++) {
//				System.out.println("Probable Charset = " + prob[i]);
				if(prob[i].equals("UTF-8"))
				{
					isutf8 = true;
//					if(containgbk)
						break;
				}
				else if(prob[i].startsWith("GB"))
				{
					containgbk = true;
					if(isutf8)
						break;
				}
//				if(prob[i].toLowerCase().startsWith("gb"))
//				{
//					String test = FileUtil.getFileContent(f,"GBK");
//					FileCopyUtils.copy(test, new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
//					break;
//				}
			
			}
			if(!isutf8 && containgbk)
			{
				return "GBK";
			}
			else if(isutf8)
				return "UTF-8";
			else	
				return prob != null && prob.length > 0?prob[0]:null;
			
		}
//		return null;
	}
	
	/**
	 * 获取文件内容字符集
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static String takefilecharset(File f) throws Exception {
		return takeurlcharset(f.toURL());
	}
	
	/**
	 * 获取文件内容字符集
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static String takeurlcharset(URL url) throws Exception {
		final Found found = new Found();
		

		int lang = nsPSMDetector.ALL;
		nsDetector det = new nsDetector(lang);

		// Set an observer...
		// The Notify() will be called when a matching charset is found.

		det.Init(new nsICharsetDetectionObserver() {
			public void Notify(String charset) {
				found.setFound(true);
				found.setCharset(charset);
				
			}
		});

//		URL url = f.toURL();
//		URL url = new URL(argv[0]);
		BufferedInputStream imp = new BufferedInputStream(url.openStream());

		byte[] buf = new byte[1024];
		int len;
		boolean done = false;
		boolean isAscii = true;

		while ((len = imp.read(buf, 0, buf.length)) != -1) {

			// Check if the stream is only ascii.
			if (isAscii)
				isAscii = det.isAscii(buf, len);

			// DoIt if non-ascii and not done yet.
			if (!isAscii && !done)
				done = det.DoIt(buf, len, false);
		}
		det.DataEnd();

		if (isAscii) {
			found.setFound(true);
		}
		
		
		if(found.isFound())
		{
			String prob[] = det.getProbableCharsets();
//			System.out.println(prob[0]);
			String chars = found.getCharset(); 
			if(chars != null )
			{
				return  chars;
			}
			else if(prob != null && prob.length > 0 )
			{
				return  prob[0];
			}
			else 
				return null;
		}
		else
		{
			String prob[] = det.getProbableCharsets();
			
			boolean isutf8 = false;
			boolean containgbk = false;
			for (int i = 0; prob != null &&  i < prob.length; i++) {
//				System.out.println("Probable Charset = " + prob[i]);
				if(prob[i].equals("UTF-8"))
				{
					isutf8 = true; 
				}
				else if(prob[i].startsWith("GB"))
				{
					containgbk = true;
				}

			
			}
			if(!isutf8 && containgbk)
			{
				return "GBK";
			}
			else if(isutf8)
			{
				return "UTF-8";
			}
			else
			{
				return prob != null && prob.length > 0?prob[0]:null;
			}
			
			
		}
	}
	/**
	 * 将文件内容从GBK转换为UTF-8
	 * @param f
	 * @throws Exception
	 */
	public void doconvert(File f) throws Exception {
		final Found found = new Found();
		

		int lang = nsPSMDetector.ALL;
		nsDetector det = new nsDetector(lang);

		// Set an observer...
		// The Notify() will be called when a matching charset is found.

		det.Init(new nsICharsetDetectionObserver() {
			public void Notify(String charset) {
				found.setFound(true);
				found.setCharset(charset);
				
			}
		});

		URL url = f.toURL();
//		URL url = new URL(argv[0]);
		BufferedInputStream imp = new BufferedInputStream(url.openStream());

		byte[] buf = new byte[1024];
		int len;
		boolean done = false;
		boolean isAscii = true;

		while ((len = imp.read(buf, 0, buf.length)) != -1) {

			// Check if the stream is only ascii.
			if (isAscii)
				isAscii = det.isAscii(buf, len);

			// DoIt if non-ascii and not done yet.
			if (!isAscii && !done)
				done = det.DoIt(buf, len, false);
		}
		det.DataEnd();

		if (isAscii) {
//			System.out.println("CHARSET = ASCII");
			found.setFound(true);
		}
		
		
		if(found.isFound())
		{
			String prob[] = det.getProbableCharsets();
//			System.out.println(prob[0]);
			if(found.getCharset() != null && found.getCharset().startsWith("GB"))
			{
//				String test = FileUtil.getFileContent(f,"GBK");
//				FileCopyUtils.copy(test, new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
				convertfilecharset(f,"GBK","UTF-8");
			}
			else if(prob != null && prob.length > 0 && prob[0].startsWith("GB"))
			{
//				String test = FileUtil.getFileContent(f,"GBK");
//				FileCopyUtils.copy(test, new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
				convertfilecharset(f,"GBK","UTF-8");
			}
		}
		else
		{
			String prob[] = det.getProbableCharsets();
			buffer.append("file=").append(f.getCanonicalPath()).append(",charset[\r\n");
			boolean isutf8 = false;
			boolean containgbk = false;
			for (int i = 0; prob != null &&  i < prob.length; i++) {
//				System.out.println("Probable Charset = " + prob[i]);
				if(prob[i].equals("UTF-8"))
				{
					isutf8 = true;
				}
				else if(prob[i].startsWith("GB"))
				{
					containgbk = true;
				}
//				if(prob[i].toLowerCase().startsWith("gb"))
//				{
//					String test = FileUtil.getFileContent(f,"GBK");
//					FileCopyUtils.copy(test, new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
//					break;
//				}
				buffer.append(prob[i]).append(",");
			}
			if(!isutf8 && containgbk)
			{
//				String test = FileUtil.getFileContent(f,"GBK");
//				FileCopyUtils.copy(test, new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
				convertfilecharset(f,"GBK","UTF-8");
			}
			buffer.append("]\r\n");
		}
	}
	
	public static void convertfilecharset(File f,String srccharset,String destcharset) throws IOException
	{
		String test = FileUtil.getFileContent(f,srccharset);
		FileCopyUtils.copy(test, new OutputStreamWriter(new FileOutputStream(f), destcharset));
	}
	
	public static void main(String[] args) throws Exception
	{
		UTF8Convertor convertor = new UTF8Convertor();
    	//转换文件字符编码GBK,GB2312,gb18030为UTF-8
//		convertor.convertCharsetToUtf_8("D:\\workspace\\SanyPDP\\src-htmlparser");
		
		//转换文件字符编码GBK,GB2312,gb18030为UTF-8,指定忽略的目录名称，指定要转换的文件类型
//		convertor.convertCharsetToUtf_8("D:\\workspace\\SanyPDP\\src-htmlparser",
//				new String[]{".svn","classes",".settings","文档","dbinit-system","distrib","doc","lib","lib-client","lib-compile"},
//				new String[]{".jsp",".java",".js",".css",".txt",".properties",".html",".htm",".tld",".vm",".xml"});
		convertor.convertCharsetToUtf_8("F:\\workspace\\bboss-cms\\WebRoot\\test",
				new String[]{".svn"},
				new String[]{".jsp"});
		
		//获取文件内容编码集
//		 String charset = convertor.takefilecharset(new File("D:\\workspace\\smc-desktop/src-sys/com/frameworkset/platform/sysmgrcore/purviewmanager/PurviewManagerOrgTree.java"));
		//打印没有精确识别出字符集的文件信息
		 System.out.println(convertor.getUnknown());
		
	}

}
