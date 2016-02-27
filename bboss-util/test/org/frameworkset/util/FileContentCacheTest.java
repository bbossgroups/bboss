package org.frameworkset.util;

import org.frameworkset.cache.FileContentCache;
import org.junit.Test;

public class FileContentCacheTest {

	public FileContentCacheTest() {
		// TODO Auto-generated constructor stub
	}
	@Test
	public void testMehtod()
	{
		
		try {
			//创建一个文件内容缓存组件并初始化,一般采用单列模式创建，根据需要可以创建多个单列实例。
			FileContentCache fileContentCache = new FileContentCache();
			fileContentCache.setRefreshInterval(5000);//设置定时探测扫描文件时间间隔，单位：毫秒，每个组件只会以daemon模式启动一个线程。
			fileContentCache.start("测试缓存组件");
			
			//使用组件方法获取文件内容：第一次从缓存读取
			String content = fileContentCache.getFileContent( "/opt/source/Test.java", "UTF-8",FileContentCache.PLAINEncode);//对内容不做任何处理
			content = fileContentCache.getFileContent( "/opt/source/Test.java", "UTF-8",FileContentCache.HTMLEncodej);//对html转义存储的文件内容进行还原处理
			content = fileContentCache.getFileContent( "/opt/source/Test.java", "UTF-8",FileContentCache.HTMLNoBREncode);//对内容进行html转义处理，忽略回车换行处理
			content = fileContentCache.getFileContent( "/opt/source/Test.java", "UTF-8",FileContentCache.HTMLEncode);//对内容进行html转义处理
			
			//后续从缓存读取
			content = fileContentCache.getFileContent( "/opt/source/Test.java", "UTF-8",FileContentCache.PLAINEncode);//对内容不做任何处理
			content = fileContentCache.getFileContent( "/opt/source/Test.java", "UTF-8",FileContentCache.HTMLEncodej);//对html转义存储的文件内容进行还原处理
			content = fileContentCache.getFileContent( "/opt/source/Test.java", "UTF-8",FileContentCache.HTMLNoBREncode);//对内容进行html转义处理，忽略回车换行处理
			content = fileContentCache.getFileContent( "/opt/source/Test.java", "UTF-8",FileContentCache.HTMLEncode);//对内容进行html转义处理
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
