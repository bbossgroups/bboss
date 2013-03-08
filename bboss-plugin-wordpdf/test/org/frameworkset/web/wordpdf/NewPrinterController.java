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
//			FileConvertor.initXComponentContext(officeHome);
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
	public @ResponseBody
	FileBlob contractPrint()
			throws Exception {
		
		String hetongbianhao = "123";
		String newfilepath = "d:/test/";
		String wordtemplate ="D:/workspace/bbossgroups-3.6.0/bboss-plugin-wordpdf/contract_mb.doc";
		File file=new File(newfilepath +hetongbianhao);
		if(!file.exists()){
			file.mkdirs();
		}
		String pdfpath = newfilepath +hetongbianhao+"/"+hetongbianhao
				+ ".pdf";
		String wordfile = newfilepath+ hetongbianhao+"/"+hetongbianhao
				+ ".doc";
		String toswfpath = newfilepath+ hetongbianhao+"/"+hetongbianhao
				+ ".swf";
		File f = new File(toswfpath);
		String[] bookMarks = new String[] { "s1", "s2", "s3", "s4", "s5", "s6",
				"s7", "s8", "s9", "s10", "s11", "s12", "s13", "s14", "s15",
				"s16", "s17", "s18", "s19", "s20", "s21", "s22" };
		String s1 = "123";// 提取订单受理号信息
		String s2 = "Custom_name";// 提取订单受理基础信息借款人信息
		String s3 = "Identification_card()";// 提取订单受理基础信息下个人基本信息的身份证信息
		String s4 = "Mobile_phone()";// 提取订单受理基础信息下个人基本信息的手机信息
		String s5 = "Live_address()";// 提取订单受理基础信息下个人基本信息的现居住地址
		String s6 = "" + "Loan_amount()";// 提取订单审批流程的总经理最终审批金额（大写）
		String s7 = "" + "Loan_amount()";// 提取订单审批流程的总经理最终审批金额（小写）
		String s8 = "" + "Time_limit()";// 提取订单审批流程的总经理最终审批贷款期限
		String s9 = "Loan_purpose()";// 提取订单受理基础信息中的借款用途
		String s10 = "" + "Interest_rate()";// 提取订单审批流程的总经理最终审批月利率
		String s11 = "" + "Bank_name()";// 提取订单受理基础信息的开户行信息
		String s12 = "Custom_name()";// 提取订单受理基础信息的借款人
		String s13 = "Bank_account()";// 提取订单受理基础信息的银行账号
		String s14 = "" + "Time_limit()";// 提取订单审批流程的总经理最终审批贷款期限
		String s15 = "" + "Repayment_date()";// 提取订单受理基础信息的每月还款日
		String s16 = "" + "Bank_name()";// 提取订单受理基础信息的开户行信息
		String s17 = "Custom_name()";// 提取订单受理基础信息的借款人
		String s18 = "Bank_account()";// 提取订单受理基础信息的银行账号
		String s19 = "1%";// 提取字典表提取每日罚息利率为1‰
		String s20 = "4%";// 提取字典表提取提前结清时，6个月之内的违约金4%
		String s21 = "3%";// 提取字典表提取提前结清时，超过6个月的违约金为3%
		String s22 = "1%";// 提取字典表刷卡手续费利率1%
		String[] bookdatas = new String[] { s1, s2, s3, s4, s5, s6, s7, s8, s9,
				s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22 };
		if (!f.exists()) {
			//FileConvertor.initXComponentContext(openofficeHomeDir);
			FileConvertor.init(officeHome);
			FileConvertor.getRealWordByOpenoffice(wordtemplate, wordfile,
					bookMarks, bookdatas);
			FileConvertor.wordToPDFByOpenOffice(wordfile, pdfpath);
			FileConvertor.swftoolsConvert(swftoolWorkDir, pdfpath, toswfpath);
		}
		FileBlob fileblob = new FileBlob(toswfpath, FileBlob.BROWSER);
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
		String hetongbianhao = "20121222";
	    String wordtemplate = "/opt/tomcat/wordpdf/anjie.doc";
	    String pdfpath = "/opt/tomcat/test/anjieswftools_" + hetongbianhao + ".pdf";
	    String wordfile = "/opt/tomcat/test/anjie_testswftools" + hetongbianhao + ".doc";
	    String toswfpath = "/opt/tomcat/test/contractswftools_" + hetongbianhao + ".swf";

//		String officeHome = "E:\\Program Files\\OpenOffice.org 3";
		File f = new File(toswfpath);
		if(!f.exists())
		{
			System.out.println("officeHome__________:" + officeHome);
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
	
	public @ResponseBody FileBlob getWordToPDFByOpenOffice() throws Exception  {
		System.out.println("--------------程序执行到此处------------------");
		String hetongbianhao="20131222";
		String pdfpath = "/opt/tomcat/test/anjieswftools_"+hetongbianhao+".pdf";
		String wordfile = "/opt/tomcat/wordpdf/anjie.doc";
//		String officeHome = "E:\\Program Files\\OpenOffice.org 3";
		{
			System.out.println("officeHome__________:" + officeHome);
			FileConvertor.init( officeHome);
			System.out.println("officeHome__________:" + officeHome);
			
			FileConvertor.wordToPDFByOpenOffice(wordfile, pdfpath);
//			FileConvertor.realWordConvertorBySWFTool( swftoolWorkDir,  wordtemplate,
//					 wordfile,  bookMarks, bookdatas, pdfpath,
//					 toswfpath);
			
		}
		FileBlob fileblob = new FileBlob(pdfpath,FileBlob.DOWNLOAD);
		return fileblob;
		

	}
	
	public @ResponseBody FileBlob getWordToSWFByOpenOffice() throws Exception  {
		System.out.println("--------------程序执行到此处------------------");
		String hetongbianhao="20131222";
		String pdfpath = "/opt/tomcat/test/anjieswftools_"+hetongbianhao+".swf";
		String wordfile = "/opt/tomcat/wordpdf/anjie.doc";
//		String officeHome = "E:\\Program Files\\OpenOffice.org 3";
		{
			System.out.println("officeHome__________:" + officeHome);
			FileConvertor.init( officeHome);
			System.out.println("officeHome__________:" + officeHome);
			
			FileConvertor.wordToPDFByOpenOffice(wordfile, pdfpath);
//			FileConvertor.realWordConvertorBySWFTool( swftoolWorkDir,  wordtemplate,
//					 wordfile,  bookMarks, bookdatas, pdfpath,
//					 toswfpath);
			
		}
		FileBlob fileblob = new FileBlob(pdfpath,FileBlob.DOWNLOAD);
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
	public static void main(String[] args)
	{
		FileConvertor.init("E:\\Program Files\\LibreOffice 3.6");
		FileConvertor.wordToPDFByOpenOffice("D:\\workspace\\SanyPDP\\文档\\二维条码\\GSP-MMS\\LmGzjMaterialBarcodeMaintAction.do.htm", "d:\\mms.pdf");
	}
}