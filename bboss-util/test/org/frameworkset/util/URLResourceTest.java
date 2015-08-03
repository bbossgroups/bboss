package org.frameworkset.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.frameworkset.util.io.ResourceHandleListener;
import org.frameworkset.util.io.UrlResource;

public class URLResourceTest {

	public URLResourceTest() {
		// TODO Auto-generated constructor stub
	}
	
	static class InerRunnable extends Thread{
		boolean end;
		boolean first = true;
		UrlResource url;
		 
		long oldsavesize;
		String oldput ;
		private DecimalFormat formater = new DecimalFormat("#.##%");
		public InerRunnable(UrlResource url)
		{
			this.url= url; 
		}
		@Override
		public void run() {
			do
			{
				if(end)
					break;
				try {
					if(first)
					{
						System.out.print("文件下载开始："+url.getURL() + ",下载进度：");
						
						first = false;
					}
					else
					{
						long newsize = url.getSavesize();
						
						if(oldsavesize !=  newsize)
						{
							oldsavesize  = newsize;
							if(oldput == null)
							{
								float rate = (float) (newsize*1.0 / url.getTotalsize());
								oldput = newsize+"/"+url.getTotalsize()+","+formater.format(rate);
								System.out.print(oldput);
							}
							else
							{
								StringBuilder builder = new StringBuilder();
								for(int i = 0 ; i < oldput.length(); i ++)
									builder.append("\b");
								float rate = (float) (newsize*1.0 / url.getTotalsize());
								builder.append(newsize+"/"+url.getTotalsize()+",完成百分比："+formater.format(rate));
								System.out.print(builder);
								oldput = newsize+"/"+url.getTotalsize();
							}
							
						}
					}
					Thread.sleep(100);
				} catch (IOException e) {							
					end = true;
					break;
				} catch (InterruptedException e) {				
						break;
				}
			}while(true);
				
			
		}
		public void refreshprocess()
		{
//			synchronized(this)
//			{
//				this.notifyAll();
//			}
		}
		public void end()
		{
			this.end = true;
			synchronized(this)
			{
				this.notifyAll();
			}
		}
		
	}
	
	public static void main(String[] args) throws IOException
	{
		final UrlResource url = new UrlResource("http://www.bbossgroups.com/tool/download.htm?fileName=bboss.war");
		url.open();
		
		final InerRunnable run = new InerRunnable(url);
	 
		 
		url.savetofile(new File("d:/",url.getFilename()),new ResourceHandleListener<UrlResource>() {
			
			@Override
			public void startEvent(UrlResource resource,File dest) {
				
				run.start(); 
				
			}
			
			@Override
			public void handleDataEvent(UrlResource resource,File dest) {
				
				run.refreshprocess();
			}
			
			@Override
			public void endEvent(UrlResource resource,File dest) {
				run.end();
				
			}
		});
	}

}
