package org.frameworkset.web.wordpdf;

import java.io.File;

import org.frameworkset.http.converter.wordpdf.Word2PDFResponse;
import org.frameworkset.http.converter.wordpdf.WordResponse;
import org.frameworkset.util.annotations.ResponseBody;

public class PrintController {
	public @ResponseBody Word2PDFResponse getPDF(boolean usecache)  {
		System.out.println("--------------程序执行到此处------------------");
		Word2PDFResponse response = new Word2PDFResponse();
		response.setCache(usecache);
		response.setWordFile("d:\\anjie_test.doc");
		response.setWordtemplate("D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\anjie.doc");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "湖南三一工程机械有限公司", "湖南三一工程机械有限公司",
				"六桥泵车", "SY5419THB 52E(6)", "2", "风冷", "V09660", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "卿琳", "430111199910102121", "13800138200", "卿琳","bboss" };
		for(int i = 0; i < bookMarks.length; i ++)
		{
			
			response.addBookValue(bookMarks[i], mapValue[i]);
		}
		response.setPdfFile("d:\\contract.pdf");
		response.setTempdir("d:\\");
		response.setPdfMergeFiles(new String[]{"D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\contract_part2.pdf" } );
		response.setMergeposition(Word2PDFResponse.MERGE_AFTER);

		response.setRendtype(Word2PDFResponse.BROWSER);
		return response;
		

	}
	public @ResponseBody Word2PDFResponse getPDFTemp()  {
		System.out.println("--------------程序执行到此处------------------");
		Word2PDFResponse response = new Word2PDFResponse();
//		response.setTempdir("d:\\");
//		response.setWordFile("d:\\anjie_test.doc");
		response.setWordtemplate("D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\anjie.doc");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "湖南三一工程机械有限公司", "湖南三一工程机械有限公司",
				"六桥泵车", "SY5419THB 52E(6)", "2", "风冷", "V09660", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "卿琳", "430111199910102121", "13800138200", "卿琳","bboss" };
		for(int i = 0; i < bookMarks.length; i ++)
		{
			
			response.addBookValue(bookMarks[i], mapValue[i]);
		}
//		response.setPdfFile("d:\\contract.pdf");
		
		response.setPdfMergeFiles(new String[]{"D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\contract_part2.pdf" } );
		response.setMergeposition(Word2PDFResponse.MERGE_AFTER);

		response.setRendtype(Word2PDFResponse.BROWSER);
		return response;
		

	}
	public @ResponseBody File returnFile()
	{
		return new File("D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\anjie.doc");
	}
	public @ResponseBody Word2PDFResponse downPDFTemp()  {
		System.out.println("--------------程序执行到此处------------------");
		Word2PDFResponse response = new Word2PDFResponse();
//		response.setTempdir("d:\\");
//		response.setWordFile("d:\\anjie_test.doc");
		response.setWordtemplate("D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\anjie.doc");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "湖南三一工程机械有限公司", "湖南三一工程机械有限公司",
				"六桥泵车", "SY5419THB 52E(6)", "2", "风冷", "V09660", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "卿琳", "430111199910102121", "13800138200", "卿琳","bboss" };
		for(int i = 0; i < bookMarks.length; i ++)
		{
			
			response.addBookValue(bookMarks[i], mapValue[i]);
		}
//		response.setPdfFile("d:\\contract.pdf");
		
		response.setPdfMergeFiles(new String[]{"D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\contract_part2.pdf" } );
		response.setMergeposition(Word2PDFResponse.MERGE_AFTER);

		response.setRendtype(Word2PDFResponse.DOWNLOAD);
		return response;
		

	}
	public @ResponseBody WordResponse getWord(boolean usecache)  {
		System.out.println("--------------程序执行到此处------------------");
		WordResponse response = new WordResponse();
		response.setCache(usecache);
		response.setWordFile("d:\\anjie_test1.doc");
		response.setWordtemplate("D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\anjie.doc");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "湖南三一工程机械有限公司", "湖南三一工程机械有限公司",
				"六桥泵车", "SY5419THB 52E(6)", "2", "风冷sdfsdf", "V09660ffff", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "卿琳", "430111199910102121", "13800138200", "卿琳","bboss" };
		for(int i = 0; i < bookMarks.length; i ++)
		{
			
			response.addBookValue(bookMarks[i], mapValue[i]);
		}
//		response.setPdfFile("d:\\contract.pdf");
		response.setTempdir("d:\\");
//		response.setPdfMergeFiles(new String[]{"D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\contract_part2.pdf" } );
//		response.setMergeposition(Word2PDFResponse.MERGE_AFTER);

		response.setRendtype(WordResponse.BROWSER);
		return response;
		

	}
	
	public @ResponseBody WordResponse getWordTemp()  {
		System.out.println("--------------程序执行到此处------------------");
		WordResponse response = new WordResponse();
		response.setTempdir("d:\\");
//		response.setWordFile("d:\\anjie_test1.doc");
		response.setWordtemplate("D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\anjie.doc");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "湖南三一工程机械有限公司", "湖南三一工程机械有限公司",
				"六桥泵车", "SY5419THB 52E(6)", "2", "风冷sdfsdf", "V09660ffff", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "卿琳", "430111199910102121", "13800138200", "卿琳","bboss" };
		for(int i = 0; i < bookMarks.length; i ++)
		{
			
			response.addBookValue(bookMarks[i], mapValue[i]);
		}
//		response.setPdfFile("d:\\contract.pdf");
		
//		response.setPdfMergeFiles(new String[]{"D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\contract_part2.pdf" } );
//		response.setMergeposition(Word2PDFResponse.MERGE_AFTER);

		response.setRendtype(WordResponse.BROWSER);
		return response;
		

	}
	
	public @ResponseBody WordResponse downWordTemp()  {
		System.out.println("--------------程序执行到此处------------------");
		WordResponse response = new WordResponse();
//		response.setTempdir("d:\\");
//		response.setWordFile("d:\\anjie_test1.doc");
		response.setWordtemplate("D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\anjie.doc");
		String[] bookMarks = new String[] { "DealerName", "Name", "CgName",
				"TypeName", "OrderQty", "CoolCode", "ChassisCode", "CustPrice",
				"CustAmt", "sumall", "EarnestPayDays", "EarnestAmt",
				"StageDate", "FirstAmt", "DepositPercent", "Deposit",
				"ServiceChargePercent", "ServiceCharge", "NotarizationFee",
				"InsuranceTerm", "Insurance", "ReinsuranceDeposit",
				"FinanceAmt", "FinanceFC", "LackAmtPayDate",
				"LackAmtFinalPayDate", "ReceiverName", "ReceiverID",   
				"ReceiverTel", "Insurer","authoriate" };
		String[] mapValue = new String[] { "湖南三一工程机械有限公司", "湖南三一工程机械有限公司",
				"六桥泵车", "SY5419THB 52E(6)", "2", "风冷sdfsdf", "V09660ffff", "300.00",
				"600.00", "陆佰万元整", "7", "100", "2012年8月31日", "60", "5", "3",
				"10", "6", "10", "5", "10", "21", "540", "10", "2012年8月31日",
				"2012年8月31日", "卿琳", "430111199910102121", "13800138200", "卿琳","bboss" };
		for(int i = 0; i < bookMarks.length; i ++)
		{
			
			response.addBookValue(bookMarks[i], mapValue[i]);
		}
//		response.setPdfFile("d:\\contract.pdf");
		
//		response.setPdfMergeFiles(new String[]{"D:\\bbossgroups-3.5.1\\bboss-mvc\\plugin\\wordpdf\\contract_part2.pdf" } );
//		response.setMergeposition(Word2PDFResponse.MERGE_AFTER);

		response.setRendtype(WordResponse.DOWNLOAD);
		return response;
		

	}
}