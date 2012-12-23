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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.converter.HttpMessageNotWritableException;

import com.frameworkset.util.StringUtil;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

/**
 * <p>
 * Word2PDFResponse.java
 * </p>
 * <p>
 * Description:word转pdf，然后下载和浏览功能组件
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2009
 * </p>
 * 
 * @Date 2012-9-17 上午9:54:32
 * @author biaoping.yin
 * @version 1.0
 */
public class Word2PDFResponse extends WordResponse {
	private boolean isPDFTemp = false;
	

	private String pdfFile;
	private String[] pdfMergeFiles;
	private int mergeposition = MERGE_AFTER;
	public static final int MERGE_BEFORE = 1;
	public static final int MERGE_AFTER = 0;
	static OfficeManager officeManager ;
	static void init()
	{
		if(officeManager == null)
		{
			synchronized(Word2PDFResponse.class)
			{
				if(officeManager == null)
				{
					officeManager = new DefaultOfficeManagerConfiguration().buildOfficeManager();
					officeManager.start();
				}
			}
		}
	}
	
	public Word2PDFResponse() {
		super();
	}

	protected void download(HttpOutputMessage outputMessage,
			HttpInputMessage inputMessage) {
//		try {
//			StringUtil.sendFile(inputMessage.getServletRequest(),
//					outputMessage.getResponse(), this.buildPDF());
//		} catch (Exception e) {
//			throw new HttpMessageNotWritableException(this.getPdfFile(), e);
//		}
		try {
			if(this.isCache() )
			{
				if(this.pdfFile != null)
				{
					CacheObject cacheObject = cacheObjects.get(this.pdfFile);
					if(cacheObject == null)
					{
						cacheObject = new CacheObject(this.pdfFile,this.getModifyTime());
						cacheObjects.put(this.pdfFile, cacheObject);
						StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), this.buildPDF());
					}
					else
					{
						if(cacheObject.isModified(this.getModifyTime()))
						{
							StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), this.buildPDF());
						}
						else
						{
							File temp = new File(this.pdfFile);
							if(temp.exists())
								StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(),  temp);
							else
								StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), this.buildPDF());
						}
					}
				}
				else
				{
					StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), this.buildPDF());
				}
			}
			else
			{				
				StringUtil.sendFile(inputMessage.getServletRequest(), outputMessage.getResponse(), this.buildPDF());
			}
		} 
		catch (Exception e)
		{
			throw new HttpMessageNotWritableException(this.getPdfFile(),e);
		}
	}
	
	protected void doFinally()
	{
		super.doFinally();
		if(isPDFTemp)
		{
			File f = new File(this.pdfFile);
			f.delete();
		}
		else 
		{
			if(!this.isCache())
			{
				File f = new File(this.pdfFile);
				f.delete();
			}
		}
			
	}

	protected void render(HttpOutputMessage outputMessage,
			HttpInputMessage inputMessage) throws Exception {
		try {
			if(this.isCache() )
			{
				if(this.pdfFile != null)
				{
					CacheObject cacheObject = cacheObjects.get(this.pdfFile);
					if(cacheObject == null)
					{
						cacheObject = new CacheObject(this.pdfFile,this.getModifyTime());
						cacheObjects.put(this.pdfFile, cacheObject);
						render_(outputMessage,
								inputMessage,this.buildPDF());
						
					}
					else
					{
						if(cacheObject.isModified(this.getModifyTime()))
						{
							render_(outputMessage,
									inputMessage,this.buildPDF());
						}
						else
						{
							File temp = new File(this.pdfFile);
							if(temp.exists())
								render_(outputMessage,
										inputMessage,temp);
							else
								render_(outputMessage,
										inputMessage,this.buildPDF());
						}
					}
				}
				else
				{
					render_(outputMessage,
							inputMessage,this.buildPDF());
				}
			}
			else
			{				
				render_(outputMessage,
						inputMessage,this.buildPDF());
			}
		} catch (IOException e) {
			throw new HttpMessageNotWritableException(this.getPdfFile(), e);
		}
	}
	
	protected void render_(HttpOutputMessage outputMessage,
			HttpInputMessage inputMessage,File file) {
		OutputStream out = null;
		PDDocument doc2 = null;
		try {
			File contract_pdf = file;
			doc2 = PDDocument.load(contract_pdf);
			HttpServletResponse response = outputMessage.getResponse();
			response.setContentType("application/pdf");
			response.setHeader( "Content-Disposition", "inline; filename="+handleCNName(contract_pdf.getName(),inputMessage.getServletRequest())); 
			out = response.getOutputStream();
			doc2.save(out);
			out.flush();
		} catch (Exception e) {
			throw new HttpMessageNotWritableException(this.getPdfFile(), e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(doc2 != null)
				try {
					doc2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	

	private File buildPDF() throws Exception {
		boolean needmerge = (this.pdfMergeFiles != null && this.pdfMergeFiles.length > 0);
		if (needmerge) {
			String tempfilename = gentempfile("pdf");
			File pdfFile = new File(tempfilename);			
			if(this.convter.isOpenoffice())
			{
				File wordContract = this.getRealWord();
				OfficeDocumentConverter converter = new OfficeDocumentConverter(
						officeManager);
				converter.convert(wordContract, pdfFile);
			}
			else
			{
				super.realWord2PDF(tempfilename,this.getWaittimes());
			}
			
			
			mergePdfFiles(this.pdfMergeFiles, tempfilename);
			pdfFile.delete();
//			mergePdfFiles_(new String[]{tempfilename,this.pdfMergeFiles[0]}, this.pdfFile);
			return new File(this.getPdfFile());
		} else {
			if(this.convter.isOpenoffice())
			{
				File pdfFile = new File(this.getPdfFile());
				File wordContract = this.getRealWord();
				OfficeManager officeManager = new DefaultOfficeManagerConfiguration()
						.buildOfficeManager();
				officeManager.start();
				OfficeDocumentConverter converter = new OfficeDocumentConverter(
						officeManager);
				converter.convert(wordContract, pdfFile);
				return pdfFile;
			}
			else
			{
				return super.realWord2PDF(this.getPdfFile(),this.getWaittimes());
				
			}
		}

	}



	// 合并pdf
	protected boolean mergePdfFiles(String[] files, String newfile) throws Exception {
		return FileConvertor.mergePdfFiles(files, newfile, getPdfFile(), mergeposition);
//		boolean retValue = false;
//		Document document = null;
//		try {
//
//			if (this.mergeposition == MERGE_AFTER) {
//				PdfReader reader_ = new PdfReader(newfile);
//				document = new Document(new PdfReader(reader_).getPageSize(1));
//				PdfCopy copy = new PdfCopy(document, new FileOutputStream(
//						this.getPdfFile()));
//				document.open();
//				reader_ = new PdfReader(newfile);
//				int nf = reader_.getNumberOfPages();
//				for (int j = 1; j <= nf; j++) {
//
//					document.newPage();
//					PdfImportedPage page = copy.getImportedPage(reader_, j);
//					copy.addPage(page);
//				}
//				for (int i = 0; i < files.length; i++) {
//					reader_ = new PdfReader(files[i]);
//					int n = reader_.getNumberOfPages();
//					for (int j = 1; j <= n; j++) {
//						document.newPage();
//						PdfImportedPage page = copy.getImportedPage(reader_, j);
//						copy.addPage(page);
//					}
//				}
//			} else {
//				PdfReader reader_ = new PdfReader(files[0]);
//				document = new Document(new PdfReader(reader_).getPageSize(1));
//				PdfCopy copy = new PdfCopy(document, new FileOutputStream(
//						this.getPdfFile()));
//				document.open();
//				for (int i = 0; i < files.length; i++) {
//					PdfReader reader = new PdfReader(files[i]);
//					int n = reader.getNumberOfPages();
//					for (int j = 1; j <= n; j++) {
//						document.newPage();
//						PdfImportedPage page = copy.getImportedPage(reader, j);
//						copy.addPage(page);
//					}
//				}
//				reader_ = new PdfReader(newfile);
//				int nf = reader_.getNumberOfPages();
//				for (int j = 1; j <= nf; j++) {
//					document.newPage();
//					PdfImportedPage page = copy.getImportedPage(reader_, j);
//					copy.addPage(page);
//				}
//			}
//			retValue = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			document.close();
//		}
//		return retValue;
	}

	public String getPdfFile() {
		if(!StringUtil.isEmpty(this.pdfFile) )
		{
			return pdfFile;
		}
		else
		{
			isPDFTemp = true;
			pdfFile = gentempfile("pdf");
			return pdfFile;
		}
	}

	public void setPdfFile(String pdfFile) {
		this.pdfFile = pdfFile;
	}

	public String[] getPdfMergeFiles() {
		return pdfMergeFiles;
	}

	public void setPdfMergeFiles(String[] pdfMergeFiles) {
		this.pdfMergeFiles = pdfMergeFiles;
	}



	public int getMergeposition() {
		return mergeposition;
	}

	public void setMergeposition(int mergeposition) {
		this.mergeposition = mergeposition;
	}

	public boolean isPDFTemp() {
		return isPDFTemp;
	}

	public void setPDFTemp(boolean isPDFTemp) {
		this.isPDFTemp = isPDFTemp;
	}
}
