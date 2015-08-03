package org.frameworkset.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.frameworkset.util.io.AbstractResource;
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
		final UrlResource url = new UrlResource("http://nj02.poms.baidupcs.com/file/cecf4f52d2ce2b35f36923758a6b2010?bkt=p2-nj-384&fid=4245631570-250528-422706410393673&time=1438604378&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203-kVmi90LWn7i%2Fr8gAznNM1ctC2%2BI%3D&to=n2b&fm=Nan,B,G,nc&sta_dx=200&sta_cs=0&sta_ft=war&sta_ct=0&fm2=Nanjing02,B,G,nc&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=000070aa5f610585259d46966ce42359d63d&sl=83361871&expires=8h&rt=sh&r=592517045&mlogid=2789062078&vuk=4245631570&vbdid=1671589608&fin=bboss.war&fn=bboss.war&slt=pm&uta=0&rtype=1&iv=0&isw=0");
		url.open();
		
		final InerRunnable run = new InerRunnable(url);
	 
		 
		url.savetofile(new File("d:/",url.getFilename()),new ResourceHandleListener<AbstractResource>() {
			
			@Override
			public void startEvent(AbstractResource resource,File dest) {
				
				run.start(); 
				
			}
			
			@Override
			public void handleDataEvent(AbstractResource resource,File dest) {
				
				run.refreshprocess();
			}
			
			@Override
			public void endEvent(AbstractResource resource,File dest) {
				run.end();
				
			}
		});
	}

}
