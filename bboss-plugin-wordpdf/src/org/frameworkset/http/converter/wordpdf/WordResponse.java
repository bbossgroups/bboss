/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package org.frameworkset.http.converter.wordpdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.converter.HttpMessageNotWritableException;

import com.frameworkset.util.StringUtil;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * <p> WordResponse.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-9-17 上午9:15:25
 * @author biaoping.yin
 * @version 1.0
 */
public class WordResponse {
	private boolean isWordTemp = false;
	protected String tempdir;
	private String wordtemplate;
	private String wordFile;
	private int rendtype = BROWSER;
	public static int BROWSER = 0;
	public static int DOWNLOAD = 1;
	private boolean cache = false;
	private long modifyTime =-1;
	protected Word2PDFMessageConverter convter;
	private String flashpaperWorkDir;
	protected static class CacheObject
	{
		public CacheObject(String path, long lastmodifyTime) {
			super();
			this.path = path;
			this.lastmodifyTime = lastmodifyTime;
		}
		
		private String path;
		private long lastmodifyTime;
		protected boolean isModified(long modifyTime)
		{
			if(modifyTime == -1)
				return false;
			boolean modified = modifyTime == this.lastmodifyTime;
			if(modified)
			{
				try {
					File f = new File(this.path);
					f.delete();
				} catch (Exception e) {
					
				}
				this.lastmodifyTime = modifyTime ;
			}
			return modified;
		}
	}
	protected static final Map<String,CacheObject> cacheObjects = new HashMap<String,CacheObject>();
	public WordResponse()
	{
		bookdatas = new HashMap<String,Object>();
	}
	private Map<String,Object> bookdatas;
	public void addBookValue(String book,Object value)
	{
		bookdatas.put(book, value);
	}
	public Map<String, Object> getBookdatas() {
		return bookdatas;
	}
	public String getWordtemplate() {
		return wordtemplate;
	}
	public void setWordtemplate(String wordtemplate) {
		this.wordtemplate = wordtemplate;
	}
	public String getTempdir() {
		return tempdir;
	}

	public void setTempdir(String tempdir) {
		this.tempdir = tempdir;
	}
	public String getWordFile() {
		if(!StringUtil.isEmpty(wordFile))
		{
			return wordFile;
		}
		else
		{
			isWordTemp = true;
			wordFile = gentempfile("doc");
			return wordFile;
		}
		
	}
	protected String gentempfile(String ext) {
		String name = this.tempdir + "\\"
				+ System.currentTimeMillis() + "." + ext;
		return name;
	}
	public void setWordFile(String wordFile) {
		this.wordFile = wordFile;
	}
	
	protected void download(HttpOutputMessage outputMessage, HttpInputMessage inputMessage)
	{
		try {
			if(this.isCache() )
			{
				if(this.wordFile != null)
				{
					CacheObject cacheObject = cacheObjects.get(this.wordFile);
					if(cacheObject == null)
					{
						cacheObject = new CacheObject(this.wordFile,this.modifyTime);
						cacheObjects.put(this.wordFile, cacheObject);
						StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), getRealWord());
					}
					else
					{
						if(cacheObject.isModified(modifyTime))
						{
							StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), getRealWord());
						}
						else
						{
							File temp = new File(this.wordFile);
							if(temp.exists())
								StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(),  temp);
							else
								StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), getRealWord());
						}
					}
				}
				else
				{
					StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), getRealWord());
				}
			}
			else
			{				
				StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), getRealWord());
			}
		} 
		catch (Exception e)
		{
			throw new HttpMessageNotWritableException(getWordFile(),e);
		}
	}
	public static String handleCNName(String name,HttpServletRequest request) throws UnsupportedEncodingException
	 {
		 
		 String agent = request.getHeader("User-Agent");
//		 log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>isie:" + agent);
	     boolean isie = agent.contains("MSIE ");
	     if(isie )
	     {
//	    	 if( (agent.indexOf("MSIE 6") > 0 || agent.indexOf("MSIE 5") > 0))
//	    	 {
//	    		 name = new String(name.getBytes(),"ISO-8859-1").replaceAll(" ", "-"); 
//	    	 }
//	    	 else
	    	 {
	    		 name = java.net.URLEncoder.encode(name.replaceAll(" ", "-"),"UTF-8");
	    	 }
	     }
	     else
	     {
	    	 name = new String(name.getBytes(),"ISO-8859-1").replaceAll(" ", "-"); 
	     }
	     return name;
	     
		 
	 }
	protected void doFinally()
	{
		if(isWordTemp)
		{
			
				File f = new File(this.wordFile);
				f.delete();
			
		}
		else
		{
			if(!this.isCache())
			{
				File f = new File(this.wordFile);
				f.delete();
			}
		}
	}
	protected void _resonse(HttpOutputMessage outputMessage, HttpInputMessage inputMessage) throws Exception
	{
		try
		{
			if(this.rendtype == BROWSER)
			{
				render(outputMessage, inputMessage);
			}
			else
			{
				download( outputMessage,  inputMessage);
			}
		}
		finally
		{
			
			try {
				doFinally();
			} catch (Exception e) {
				
			}
			
		}
	}
	
	protected void render(HttpOutputMessage outputMessage, HttpInputMessage inputMessage) throws Exception
	{
		try {
			if(this.isCache() )
			{
				if(this.wordFile != null)
				{
					CacheObject cacheObject = cacheObjects.get(this.wordFile);
					if(cacheObject == null)
					{
						cacheObject = new CacheObject(this.wordFile,this.modifyTime);
						cacheObjects.put(this.wordFile, cacheObject);
						_render( outputMessage,  inputMessage, this.getRealWord());
					}
					else
					{
						if(cacheObject.isModified(modifyTime))
						{
							_render( outputMessage,  inputMessage, this.getRealWord());
						}
						else
						{
							File temp = new File(this.wordFile);
							if(temp.exists())
								_render( outputMessage,  inputMessage, temp);
							else
								_render( outputMessage,  inputMessage, this.getRealWord());
						}
					}
				}
				else
				{
					_render( outputMessage,  inputMessage, this.getRealWord());
				}
			}
			else//this.getRealWord()
			{				
				_render( outputMessage,  inputMessage, this.getRealWord());
			}
		} 
		catch (Exception e)
		{
			throw new HttpMessageNotWritableException(getWordFile(),e);
		}
	}
	
	protected void _render(HttpOutputMessage outputMessage, HttpInputMessage inputMessage,File file)
	{
		 OutputStream out = null;
		 InputStream in = null;
		try {
			HttpServletResponse response = outputMessage.getResponse();
			response.setContentType("application/msword");
//			response.setContentType( "bin ");
	        response.setHeader( "Content-Disposition", "inline; filename="+handleCNName(file.getName(),inputMessage.getServletRequest())); 
			out =  response.getOutputStream();
			in = new FileInputStream(file);
			 byte buffer[] = new byte[1024];

	            long len;
	          
	           
	            while (true) {
	                len = in.read(buffer);
	             
	                if (len > 0) {
	                    out.write(buffer, 0, (int) len);
	                 
	                  
	                } else {
	                    break;
	                }
	            }
	            out.flush();
		} 
		catch (Exception e)
		{
			throw new HttpMessageNotWritableException(getWordFile(),e);
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
				if(in != null)
					in.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
		public int getRendtype() {
		return rendtype;
	}
	public void setRendtype(int rendtype) {
		this.rendtype = rendtype;
	}
	
	protected File getRealWord() throws Exception {
		return FileConvertor.getRealWord(getWordtemplate(), getWordFile(), bookdatas);
//		/*
//		 * 1.将现有的word模板复制一份，保存为合同编号.doc 2.在新的文档里面插入动态值 3.转为pdf
//		 */
////		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
////				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
////				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
////				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
////				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
////				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
////				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
////				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",
////				"ReceiverTel", "Insurer" };
////		String[] mapValue = new String[] { "湖南三一工程机械有限公司", "湖南三一工程机械有限公司",
////				"六桥泵车", "SY5419THB 52E(6)", "2", "风冷", "V09660", "300.00",
////				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
////				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
////				"2012年8月31日", "卿琳", "430111199910102121", "13800138200", "卿琳" };
//		// 1.将现有的word模板复制一份，保存为合同编号.doc
//		
//		if(getWordtemplate() != null && this.bookdatas != null && this.bookdatas.size() > 0)
//			{
//			FileInputStream template = null;
//	
//			FileOutputStream contract = null;
//			try {
//				template = new FileInputStream(getWordtemplate());
//	
//				contract = new FileOutputStream(getWordFile());
//				byte[] buf = new byte[32];
//				int hasRead = 0;
//				while ((hasRead = template.read(buf)) > 0) {
//					contract.write(buf, 0, hasRead);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				if (null != template) {
//					template.close();
//				}
//				if (null != contract) {
//					contract.close();
//				}
//			}
//			// 2.在新的文档里面插入动态值
//			ActiveXComponent word = null;
//			Dispatch doc = null;
//			try
//			{
//				word = new ActiveXComponent("Word.Application");
//				word.setProperty("Visible", new Variant(false));
//				Dispatch documents = word.getProperty("Documents").toDispatch();
//				doc = Dispatch.call(documents, "Open", getWordFile()).toDispatch();
//				Dispatch activeDocument = word.getProperty("ActiveDocument")
//						.toDispatch();
//				Dispatch Marks = ActiveXComponent.call(activeDocument, "Bookmarks").toDispatch();
//				Set<String> keys = this.bookdatas.keySet();
//				for (String key:keys) {
//					boolean bookMarkExist = ActiveXComponent.call(Marks, "Exists", key)
//							.toBoolean(); // 查找标签
//					if (bookMarkExist) {
//		
//						Dispatch rangeItem = Dispatch.call(Marks, "Item", key)
//								.toDispatch();
//						Dispatch range = Dispatch.call(rangeItem, "Range").toDispatch();
//						Dispatch.put(range, "Text", new Variant(this.bookdatas.get(key)));// 插入书签的值
//					}
//				}
//				Dispatch.call(doc, "Save");			   	
//			   return new File(getWordFile()) ;
//			}
//			finally
//			{
//				if(doc != null)
//				{
//					try {
//						Dispatch.call(doc, "Close", new Variant(true));
//						doc = null;
//					} catch (Exception e) {
//						
//					}
//				}
//				
//				if (word != null) {
//					try {
//						Dispatch.call(word, "Quit");
//						word = null;
//					} catch (Exception e) {
//						
//					}
//				}
//			}
//		}
//		else
//		{
//			return new File(getWordFile()) ;
//		}

		

	}
	static final int wdFormatPDF = 17;// PDF 格式 
	static final int ppSaveAsPDF = 32;// PDF 格式 
	private long waittimes = -1;
	public long getWaittimes() {
		return waittimes;
	}

	public void setWaittimes(long waittimes) {
		this.waittimes = waittimes;
	}
	public  void flashPaperConvert(String sourcePath, String destPath) throws IOException{
		String command = flashpaperWorkDir+"flashprinter.exe " + sourcePath     
                + " -o " + destPath;     
      
        Process pro = Runtime.getRuntime().exec(command);     
}

	protected File realWord2PDF(String pdfpath,long waittimes) throws Exception {
		return FileConvertor.realWordConvertor(flashpaperWorkDir, getWordtemplate(), getWordFile(), bookdatas, pdfpath, waittimes);
		/*
		 * 1.将现有的word模板复制一份，保存为合同编号.doc 2.在新的文档里面插入动态值 3.转为pdf
		 */
//		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
//				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
//				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
//				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
//				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
//				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
//				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
//				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",
//				"ReceiverTel", "Insurer" };
//		String[] mapValue = new String[] { "湖南三一工程机械有限公司", "湖南三一工程机械有限公司",
//				"六桥泵车", "SY5419THB 52E(6)", "2", "风冷", "V09660", "300.00",
//				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
//				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
//				"2012年8月31日", "卿琳", "430111199910102121", "13800138200", "卿琳" };
		// 1.将现有的word模板复制一份，保存为合同编号.doc
		
//		if(getWordtemplate() != null )
//		{
////			if(this.bookdatas != null && this.bookdatas.size() > 0)
//			{
//				FileInputStream template = null;
//		
//				FileOutputStream contract = null;
//				try {
//					template = new FileInputStream(getWordtemplate());
//		
//					contract = new FileOutputStream(getWordFile());
//					byte[] buf = new byte[32];
//					int hasRead = 0;
//					while ((hasRead = template.read(buf)) > 0) {
//						contract.write(buf, 0, hasRead);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				} finally {
//					if (null != template) {
//						template.close();
//					}
//					if (null != contract) {
//						contract.close();
//					}
//				}
//			}
//			
//		}
//		
////		else
////		{
////			return new File(getWordFile()) ;
////		}
//		
//		// 2.在新的文档里面插入动态值
//		ActiveXComponent word = null;
//		Dispatch doc = null;
//		Dispatch wordObject;
//		try
//		{
//			word = new ActiveXComponent("Word.Application");
//			wordObject = word.getObject();
//			Dispatch.put(wordObject, "Visible", new Variant(false));
//
////			word.setProperty("Visible", new Variant(false));
//			Dispatch documents = word.getProperty("Documents").toDispatch();
////			Dispatch documents = word.getProperty("Documents").toDispatch();
//			doc = Dispatch.call(documents, "Open", getWordFile()).toDispatch();
////			doc = Dispatch.call(documents, "Open", getWordFile()).toDispatch();
//			if(this.bookdatas != null && this.bookdatas.size() > 0)
//			{
//				Dispatch activeDocument = word.getProperty("ActiveDocument")
//						.toDispatch();
//				Dispatch Marks = ActiveXComponent.call(activeDocument, "Bookmarks").toDispatch();
//				Set<String> keys = this.bookdatas.keySet();
//				for (String key:keys) {
//					boolean bookMarkExist = ActiveXComponent.call(Marks, "Exists", key)
//							.toBoolean(); // 查找标签
//					if (bookMarkExist) {
//		
//						Dispatch rangeItem = Dispatch.call(Marks, "Item", key)
//								.toDispatch();
//						Dispatch range = Dispatch.call(rangeItem, "Range").toDispatch();
//						Dispatch.put(range, "Text", new Variant(this.bookdatas.get(key)));// 插入书签的值
//					}
//				}
//			}
//			Dispatch.call(doc, "Save");	
////			Dispatch.call(doc,//   
////					               "SaveAs", //   
////					               pdfpath, // FileName   
////					               wdFormatPDF);   
//			
//		}
//		finally
//		{
//			if(doc != null)
//			{
//				try {
//					Dispatch.call(doc, "Close", new Variant(true));
//					doc = null;
//				} catch (Exception e) {
//					
//				}
//			}
//			
//			if (word != null) {
//				try {
//					Dispatch.call(word, "Quit");
//					word = null;
//				} catch (Exception e) {
//					
//				}
//			}
//		}
//		flashPaperConvert(getWordFile(),pdfpath);
//	   	
//		   return new File(pdfpath) ;
		

	}
	public boolean isCache() {
		return cache;
	}
	public void setCache(boolean cache) {
		this.cache = cache;
	}
	
	public long getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Word2PDFMessageConverter getConvter() {
		return convter;
	}
	public void setConvter(Word2PDFMessageConverter convter) {
		this.convter = convter;
	}
	public String getFlashpaperWorkDir() {
		return flashpaperWorkDir;
	}
	public void setFlashpaperWorkDir(String flashpaperWorkDir) {
		this.flashpaperWorkDir = flashpaperWorkDir;
	}
	
	
}
