package org.frameworkset.http.converter.wordpdf;

public class FileConvertorUtil {
	private String flashpaperWorkDir ;
	private String swftoolWorkDir;
	private String officeHome;
	public FileConvertorUtil() throws Exception {
		FileConvertor.init(officeHome);
		FileConvertor.initXComponentContext(officeHome);
	}
	
	

}
