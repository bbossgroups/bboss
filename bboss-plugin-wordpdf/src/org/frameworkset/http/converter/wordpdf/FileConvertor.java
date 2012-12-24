package org.frameworkset.http.converter.wordpdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

public class FileConvertor {
	static OfficeManager officeManager ;
	public static void init(String officeHome)
	{
		if(officeManager == null)
		{
			synchronized(FileConvertor.class)
			{
				try {
					if(officeManager == null)
					{
						DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
//					config.setOfficeHome("E:\\Program Files\\OpenOffice.org 3");
						if(officeHome != null)
							config.setOfficeHome(officeHome);
						officeManager = config.buildOfficeManager();
						officeManager.start();
					}
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OfficeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public static File getRealWord(String wordtemplate, String wordfile,
			Map<String, Object> bookdatas) throws Exception {

		/*
		 * 1.将现有的word模板复制一份，保存为合同编号.doc 2.在新的文档里面插入动态值 3.转为pdf
		 */
		// String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
		// "TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
		// "CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
		// "StageDate", "FirstAmt", "DepositPercent", "Deposit",
		// "ServiceChargePercent", "ServiceCharge", "NotarizationFee",
		// "InsuranceTerm", "Insurance", "ReinsuranceDeposit",
		// "FinanceAmt", "FinanceFC", "LackAmtPayDate",
		// "LackAmtFinalPayDate", "ReceiverName", "ReceiverID",
		// "ReceiverTel", "Insurer" };
		// String[] mapValue = new String[] { "湖南三一工程机械有限公司", "湖南三一工程机械有限公司",
		// "六桥泵车", "SY5419THB 52E(6)", "2", "风冷", "V09660", "300.00",
		// "600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
		// "10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
		// "2012年8月31日", "卿琳", "430111199910102121", "13800138200", "卿琳" };
		// 1.将现有的word模板复制一份，保存为合同编号.doc

		if (wordtemplate != null) {
			FileInputStream template = null;

			FileOutputStream contract = null;
			try {
				template = new FileInputStream(wordtemplate);

				contract = new FileOutputStream(wordfile);
				byte[] buf = new byte[32];
				int hasRead = 0;
				while ((hasRead = template.read(buf)) > 0) {
					contract.write(buf, 0, hasRead);
				}
			} catch (Exception e) {
				throw e;
			} finally {
				if (null != template) {
					template.close();
				}
				if (null != contract) {
					contract.close();
				}
			}
			// 2.在新的文档里面插入动态值
			ActiveXComponent word = null;
			Dispatch doc = null;
			try {
				word = new ActiveXComponent("Word.Application");
				word.setProperty("Visible", new Variant(false));
				Dispatch documents = word.getProperty("Documents").toDispatch();
				doc = Dispatch.call(documents, "Open", wordfile).toDispatch();
				if (bookdatas != null && bookdatas.size() > 0) {
					Dispatch activeDocument = word
							.getProperty("ActiveDocument").toDispatch();
					Dispatch Marks = ActiveXComponent.call(activeDocument,
							"Bookmarks").toDispatch();
					Set<String> keys = bookdatas.keySet();
					for (String key : keys) {
						boolean bookMarkExist = ActiveXComponent.call(Marks,
								"Exists", key).toBoolean(); // 查找标签
						if (bookMarkExist) {

							Dispatch rangeItem = Dispatch.call(Marks, "Item",
									key).toDispatch();
							Dispatch range = Dispatch.call(rangeItem, "Range")
									.toDispatch();
							Dispatch.put(range, "Text",
									new Variant(bookdatas.get(key)));// 插入书签的值
						}
					}
					Dispatch.call(doc, "Save");
				}

			} finally {
				if (doc != null) {
					try {
						Dispatch.call(doc, "Close", new Variant(true));
						doc = null;
					} catch (Exception e) {

					}
				}

				if (word != null) {
					try {
						Dispatch.call(word, "Quit");
						word = null;
					} catch (Exception e) {

					}
				}
			}
		}

		return new File(wordfile);

	}

	public static File getRealWord(String wordtemplate, String wordfile,
			String[] bookMarks, String[] bookdatas) throws Exception {

		/*
		 * 1.将现有的word模板复制一份，保存为合同编号.doc 2.在新的文档里面插入动态值 3.转为pdf
		 */
		// String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
		// "TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
		// "CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
		// "StageDate", "FirstAmt", "DepositPercent", "Deposit",
		// "ServiceChargePercent", "ServiceCharge", "NotarizationFee",
		// "InsuranceTerm", "Insurance", "ReinsuranceDeposit",
		// "FinanceAmt", "FinanceFC", "LackAmtPayDate",
		// "LackAmtFinalPayDate", "ReceiverName", "ReceiverID",
		// "ReceiverTel", "Insurer" };
		// String[] mapValue = new String[] { "湖南三一工程机械有限公司", "湖南三一工程机械有限公司",
		// "六桥泵车", "SY5419THB 52E(6)", "2", "风冷", "V09660", "300.00",
		// "600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
		// "10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
		// "2012年8月31日", "卿琳", "430111199910102121", "13800138200", "卿琳" };
		// 1.将现有的word模板复制一份，保存为合同编号.doc

		if (wordtemplate != null) {
			FileInputStream template = null;

			FileOutputStream contract = null;
			try {
				template = new FileInputStream(wordtemplate);

				contract = new FileOutputStream(wordfile);
				byte[] buf = new byte[32];
				int hasRead = 0;
				while ((hasRead = template.read(buf)) > 0) {
					contract.write(buf, 0, hasRead);
				}
			} catch (Exception e) {
//				e.printStackTrace();
				throw e;
			} finally {
				if (null != template) {
					template.close();
				}
				if (null != contract) {
					contract.close();
				}
			}
			// 2.在新的文档里面插入动态值
			ActiveXComponent word = null;
			Dispatch doc = null;
			try {
				word = new ActiveXComponent("Word.Application");
				word.setProperty("Visible", new Variant(false));
				Dispatch documents = word.getProperty("Documents").toDispatch();
				doc = Dispatch.call(documents, "Open", wordfile).toDispatch();
				if (bookMarks != null && bookMarks.length > 0) {
					Dispatch activeDocument = word
							.getProperty("ActiveDocument").toDispatch();
					Dispatch Marks = ActiveXComponent.call(activeDocument,
							"Bookmarks").toDispatch();
					int i = 0;
					for (String key : bookMarks) {
						boolean bookMarkExist = ActiveXComponent.call(Marks,
								"Exists", key).toBoolean(); // 查找标签
						if (bookMarkExist) {

							Dispatch rangeItem = Dispatch.call(Marks, "Item",
									key).toDispatch();
							Dispatch range = Dispatch.call(rangeItem, "Range")
									.toDispatch();
							Dispatch.put(range, "Text", new Variant(
									bookdatas[i]));// 插入书签的值
						}
						i++;
					}
					Dispatch.call(doc, "Save");
				}

			} finally {
				if (doc != null) {
					try {
						Dispatch.call(doc, "Close", new Variant(true));
						doc = null;
					} catch (Exception e) {

					}
				}

				if (word != null) {
					try {
						Dispatch.call(word, "Quit");
						word = null;
					} catch (Exception e) {

					}
				}
			}
		}

		return new File(wordfile);

	}

	public static  File realWordConvertorByFlashPager(String flashpaperWorkDir, String wordtemplate,
			String wordfile, String[] bookMarks, String[] bookdatas,
			String topath,long waittimes) throws Exception {

		FileConvertor.getRealWord(wordtemplate, wordfile, bookMarks,bookdatas);
		flashPaperConvert(flashpaperWorkDir, wordfile, topath,waittimes);

		return new File(topath);

	}
	
	public static  File realWordConvertorBySWFTool(String swftoolWorkDir, String wordtemplate,
			String wordfile, String[] bookMarks, String[] bookdatas,String pdfpath,
			String swfpath) throws Exception {
		FileConvertor.getRealWord(wordtemplate, wordfile, bookMarks,bookdatas);
		FileConvertor.wordToPDFByOpenOffice(wordfile, pdfpath);
		FileConvertor.swftoolsConvert(swftoolWorkDir, pdfpath, swfpath);

		return new File(swfpath);

	}
	public static final int MERGE_BEFORE = 1;
	public static final int MERGE_AFTER = 0;
	public static boolean mergePdfFiles(String[] files, String firstfile,String tofile,int mergeposition) throws Exception {
		boolean retValue = false;
		Document document = null;
		try {

			if (mergeposition == MERGE_AFTER) {
				PdfReader reader_ = new PdfReader(firstfile);
				document = new Document(new PdfReader(reader_).getPageSize(1));
				PdfCopy copy = new PdfCopy(document, new FileOutputStream(
						tofile));
				document.open();
				reader_ = new PdfReader(firstfile);
				int nf = reader_.getNumberOfPages();
				for (int j = 1; j <= nf; j++) {

					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader_, j);
					copy.addPage(page);
				}
				for (int i = 0; i < files.length; i++) {
					reader_ = new PdfReader(files[i]);
					int n = reader_.getNumberOfPages();
					for (int j = 1; j <= n; j++) {
						document.newPage();
						PdfImportedPage page = copy.getImportedPage(reader_, j);
						copy.addPage(page);
					}
				}
			} else {
				PdfReader reader_ = new PdfReader(files[0]);
				document = new Document(new PdfReader(reader_).getPageSize(1));
				PdfCopy copy = new PdfCopy(document, new FileOutputStream(
						tofile));
				document.open();
				for (int i = 0; i < files.length; i++) {
					PdfReader reader = new PdfReader(files[i]);
					int n = reader.getNumberOfPages();
					for (int j = 1; j <= n; j++) {
						document.newPage();
						PdfImportedPage page = copy.getImportedPage(reader, j);
						copy.addPage(page);
					}
				}
				reader_ = new PdfReader(firstfile);
				int nf = reader_.getNumberOfPages();
				for (int j = 1; j <= nf; j++) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader_, j);
					copy.addPage(page);
				}
			}
			retValue = true;
		} catch (Exception e) {
			throw e;
		} finally {
			document.close();
		}
		return retValue;
	}
	public static  File realWordConvertorByFlashPaper(String flashpaperWorkDir, String wordtemplate,
			String wordfile, Map<String,Object> bookdatas,
			String topath,long waittimes) throws Exception {

		FileConvertor.getRealWord(wordtemplate, wordfile, bookdatas);
		flashPaperConvert(flashpaperWorkDir, wordfile, topath, waittimes);

		return new File(topath);

	}
	public static void assertInitOfficeManager()
	{
		if(officeManager == null)
		{
			throw new AssertInitOfficeManagerFailed("Openoffice not right inited.please call method FileConvertor.init(String officeHome).");
		}
	}
	public static File wordToPDFByOpenOffice(String wordfile,String pdffile)
	{
		File pdfFile = new File(pdffile);
		File wordContract = new File(wordfile);
		init(null);
		assertInitOfficeManager();
		OfficeDocumentConverter converter = new OfficeDocumentConverter(
				officeManager);
		converter.convert(wordContract, pdfFile);
		return pdfFile;
	}
	public static void swftoolsConvert(String swftoolsWorkDir,
			String sourcePath, String destPath) throws Exception
	{
		//pdf2swf.exe -t D:\anjie_test.pdf  -s flashversion=9 -o d:\anjie_test.swf
		String command = swftoolsWorkDir + "pdf2swf.exe " + sourcePath
				+ " -s flashversion=9 -o " + destPath;

		Process pro = Runtime.getRuntime().exec(command);
		try {			
			pro.waitFor();
		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	public static void flashPaperConvert(String flashpaperWorkDir,
			String sourcePath, String destPath,long waittimes) throws Exception {
		String command = flashpaperWorkDir + "flashprinter.exe " + sourcePath
				+ " -o " + destPath;

		Process pro = Runtime.getRuntime().exec(command);
		try {
			pro.waitFor();
		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
		File file = new File(destPath);
		int i = 0;
		while (true) {
			try {
				Thread.currentThread().sleep( waittimes);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
			if(file.exists())
			{
				break;
			}
			if(i == 10)
				break;
			i ++;

		}
//		long lastmodify = file.length();
//		long oldlastmodify = lastmodify;
////		try {
////			Thread.currentThread().sleep(100);
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
//////			e.printStackTrace();
////		}
//		int i = 0;
//		while(i <= 10)
//		{
//			lastmodify = file.length();
//			System.out.println(lastmodify);
//			if(lastmodify>oldlastmodify)
//			{
//				System.out.println(oldlastmodify);
//				oldlastmodify = lastmodify;
//				try {
//					Thread.currentThread().sleep(100);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
////					e.printStackTrace();
//				}
//				
//			}
//			else
//			{
//				i ++;
//			}
//		}
			
		
	}

}
