package org.frameworkset.web.wordpdf;
import java.io.File;

import org.frameworkset.http.FileBlob;
import org.frameworkset.http.converter.wordpdf.FileConvertor;
import org.frameworkset.util.annotations.ResponseBody;

public class NewPrinterController {
	private String flashpaperWorkDir ;
	private String swftoolWorkDir;
	private String officeHome;
	public @ResponseBody FileBlob getPDF() throws Exception  {
		System.out.println("--------------程序执行到此处------------------");
		
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "公司", "公司",
				"车", "dd 52E(6)", "2", "风冷", "V09660", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "琳", "430111199910102121", "13800138200", "琳","bboss" };
		 
		String wordtemplate = "D:\\workspace\\bbossgroups-3.6.0\\bboss-plugin-wordpdf\\plugin\\wordpdf\\anjie.doc";
		String wordfile = "d:\\anjie_test.doc";
		String pdftmp = "d:\\contracttemp.pdf";
		String topdfpath = "d:\\contract.pdf";
		String[] mergePdfFiles = new String[]{"D:\\workspace\\bbossgroups-3.6.0\\bboss-plugin-wordpdf\\plugin\\wordpdf\\contract_part2.pdf" } ;
		long waittimes = 1000;
		FileConvertor.realWordConvertorByFlashPager(flashpaperWorkDir, wordtemplate, wordfile, bookMarks, mapValue,pdftmp, waittimes);
		FileConvertor.mergePdfFiles(mergePdfFiles, pdftmp, topdfpath, FileConvertor.MERGE_AFTER);
	
		FileBlob fileblob = new FileBlob(topdfpath,FileBlob.BROWSER);
		return fileblob;
		

	}
	public @ResponseBody FileBlob getPDFTemp() throws Exception  {
		System.out.println("--------------程序执行到此处------------------");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "公司", "公司",
				"车", "dd 52E(6)", "2", "风冷", "V09660", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "琳", "430111199910102121", "13800138200", "琳","bboss" };
		
		String wordtemplate = "D:\\workspace\\bbossgroups-3.6.0\\bboss-plugin-wordpdf\\plugin\\wordpdf\\anjie.doc";
		String wordfile = "d:\\anjie_test.doc";
		String topdfpath = "d:\\contract.pdf";
		long waittimes = 1000;
		FileConvertor.realWordConvertorByFlashPager(flashpaperWorkDir, wordtemplate, wordfile, bookMarks, mapValue,topdfpath, waittimes);
		FileBlob fileblob = new FileBlob(topdfpath,FileBlob.BROWSER);
		return fileblob;
		

	}
	public @ResponseBody FileBlob getSWFTemp() throws Exception  {
		System.out.println("--------------程序执行到此处------------------");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "公司", "公司",
				"车", "dd 52E(6)", "2", "风冷", "V09660", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "琳", "430111199910102121", "13800138200", "琳","bboss" };
		String hetongbianhao="20121222";
		String wordtemplate = "D:\\workspace\\bbossgroups-3.6.0\\bboss-plugin-wordpdf\\plugin\\wordpdf\\anjie.doc";
		String wordfile = "d:\\test\\anjie_test.doc";
		String toswfpath = "d:\\test\\contract_"+hetongbianhao+".swf";
		File f = new File(toswfpath);
		if(!f.exists())
		{
			long waittimes = 1000;
			FileConvertor.realWordConvertorByFlashPager(flashpaperWorkDir, wordtemplate, wordfile, bookMarks, mapValue,toswfpath, waittimes);
			
		}
		FileBlob fileblob = new FileBlob(toswfpath,FileBlob.BROWSER);
		return fileblob;
		

	}
	public @ResponseBody FileBlob getSWFTempUseOpenOffice() throws Exception  {
		System.out.println("--------------程序执行到此处------------------");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] bookdatas = new String[] { "公司", "公司",
				"车", "dd 52E(6)", "2", "风冷", "V09660", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "琳", "430111199910102121", "13800138200", "琳","bboss" };
		String hetongbianhao="20121222";
		String wordtemplate = "D:\\workspace\\bbossgroups-3.6.0\\bboss-plugin-wordpdf\\plugin\\wordpdf\\anjie.doc";
		String pdfpath = "d:\\test\\anjieswftools_"+hetongbianhao+".pdf";
		String wordfile = "d:\\test\\anjie_testswftools"+hetongbianhao+".doc";
		String toswfpath = "d:\\test\\contractswftools_"+hetongbianhao+".swf";
//		String officeHome = "E:\\Program Files\\OpenOffice.org 3";
		File f = new File(toswfpath);
		if(!f.exists())
		{
			FileConvertor.initXComponentContext(officeHome);
			FileConvertor.init( officeHome);
			FileConvertor.getRealWord(wordtemplate, wordfile,bookMarks, bookdatas);
			FileConvertor.wordToPDFByOpenOffice(wordfile, pdfpath);
			FileConvertor.swftoolsConvert(swftoolWorkDir, pdfpath, toswfpath);
//			FileConvertor.realWordConvertorBySWFTool( swftoolWorkDir,  wordtemplate,
//					 wordfile,  bookMarks, bookdatas, pdfpath,
//					 toswfpath);
			
		}
		FileBlob fileblob = new FileBlob(toswfpath,FileBlob.BROWSER);
		return fileblob;
		

	}
	
	public @ResponseBody FileBlob getSWFTempAllUseOpenOffice() throws Exception  {
		System.out.println("--------------程序执行到此处------------------");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] bookdatas = new String[] { "公司", "公司",
				"车", "dd 52E(6)", "2", "风冷", "V09660", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "琳", "430111199910102121", "13800138200", "琳","bboss" };
		String hetongbianhao="20121222";
		String wordtemplate = "/opt/tomcat/wordpdf/anjie.doc";
		String pdfpath = "/opt/tomcat/test/anjieswftools_"+hetongbianhao+".pdf";
		String wordfile = "/opt/tomcat/test/anjie_testswftools"+hetongbianhao+".doc";
		String toswfpath = "/opt/tomcat/test/contractswftools_"+hetongbianhao+".swf";
//		String officeHome = "E:\\Program Files\\OpenOffice.org 3";
		File f = new File(toswfpath);
		if(!f.exists())
		{
			FileConvertor.initXComponentContext(officeHome);
			FileConvertor.init( officeHome);
			FileConvertor.getRealWordByOpenoffice(wordtemplate, wordfile,bookMarks, bookdatas);
			FileConvertor.wordToPDFByOpenOffice(wordfile, pdfpath);
			FileConvertor.swftoolsConvert(swftoolWorkDir, pdfpath, toswfpath);
//			FileConvertor.realWordConvertorBySWFTool( swftoolWorkDir,  wordtemplate,
//					 wordfile,  bookMarks, bookdatas, pdfpath,
//					 toswfpath);
			
		}
		FileBlob fileblob = new FileBlob(toswfpath,FileBlob.BROWSER);
		return fileblob;
		

	}
	public @ResponseBody File returnFile()
	{
		return new File("D:\\workspace\\bbossgroups-3.6.0\\bboss-plugin-wordpdf\\plugin\\wordpdf\\anjie.doc");
	}
	public @ResponseBody FileBlob downPDFTemp() throws Exception  {
		System.out.println("--------------程序执行到此处------------------");
		
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "公司", "公司",
				"车", "dd 52E(6)", "2", "风冷", "V09660", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "琳", "430111199910102121", "13800138200", "琳","bboss" };
		String wordtemplate = "D:\\workspace\\bbossgroups-3.6.0\\bboss-plugin-wordpdf\\plugin\\wordpdf\\anjie.doc";
		String wordfile = "d:\\anjie_test.doc";
		String pdftmp = "d:\\contracttemp.pdf";
		String topdfpath = "d:\\contract.pdf";
		String[] mergePdfFiles = new String[]{"D:\\workspace\\bbossgroups-3.6.0\\bboss-plugin-wordpdf\\plugin\\wordpdf\\contract_part2.pdf" } ;
		long waittimes = 1000;
		FileConvertor.realWordConvertorByFlashPager(flashpaperWorkDir, wordtemplate, wordfile, bookMarks, mapValue,pdftmp, waittimes);
		FileConvertor.mergePdfFiles(mergePdfFiles, pdftmp, topdfpath, FileConvertor.MERGE_AFTER);
	
		FileBlob fileblob = new FileBlob(topdfpath,FileBlob.DOWNLOAD);
		return fileblob;
		

	}
	public @ResponseBody FileBlob getWord() throws Exception  {
		System.out.println("--------------程序执行到此处------------------");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "公司", "公司",
				"车", "dd 52E(6)", "2", "风冷sdfsdf", "V09660ffff", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "琳", "430111199910102121", "13800138200", "琳","bboss" };
		String wordtemplate = "D:\\workspace\\bbossgroups-3.6.0\\bboss-plugin-wordpdf\\plugin\\wordpdf\\anjie.doc";
		String wordfile = "d:\\anjie_test.doc";
		FileConvertor.getRealWord(wordtemplate, wordfile, bookMarks,mapValue);
	
		FileBlob fileblob = new FileBlob(wordfile,FileBlob.BROWSER);
		return fileblob;
		

	}
	
	public @ResponseBody FileBlob getWordTemp() throws Exception  {
		System.out.println("--------------程序执行到此处------------------");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "公司", "公司",
				"车", "dd 52E(6)", "2", "风冷sdfsdf", "V09660ffff", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "琳", "430111199910102121", "13800138200", "琳","bboss" };
		String wordtemplate = "D:\\workspace\\bbossgroups-3.6.0\\bboss-plugin-wordpdf\\plugin\\wordpdf\\anjie.doc";
		String wordfile = "d:\\anjie_test.doc";
		FileConvertor.getRealWord(wordtemplate, wordfile, bookMarks,mapValue);
	
		FileBlob fileblob = new FileBlob(wordfile,FileBlob.BROWSER);
		return fileblob;
		
		

	}
	
	public @ResponseBody FileBlob downWordTemp() throws Exception  {
		System.out.println("--------------程序执行到此处------------------");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "公司", "公司",
				"车", "dd 52E(6)", "2", "风冷sdfsdf", "V09660ffff", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "琳", "430111199910102121", "13800138200", "琳","bboss" };
		String wordtemplate = "D:\\workspace\\bbossgroups-3.6.0\\bboss-plugin-wordpdf\\plugin\\wordpdf\\anjie.doc";
		String wordfile = "d:\\anjie_test.doc";
		FileConvertor.getRealWord(wordtemplate, wordfile, bookMarks,mapValue);
	
		FileBlob fileblob = new FileBlob(wordfile,FileBlob.DOWNLOAD);
		return fileblob;
		
		

	}
}