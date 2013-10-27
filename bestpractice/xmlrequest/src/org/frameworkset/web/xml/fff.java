package org.frameworkset.web.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

public class fff {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		InputStream in = null;//从request中获取
		Reader reader = null;
		StringWriter out = null;
		try {
			out = new StringWriter();
			Charset charset = Charset.forName("UTF-8");
			reader = new InputStreamReader(in, charset);
		
			char[] buffer = new char[4096];
			int bytesRead = -1;
			while ((bytesRead = reader.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				
			}
			out.flush();
			String xml = out.toString();
		}
		finally {
			try {
				reader.close();
			}
			catch (IOException ex) {
			}
			try {
				in.close();
			}
			catch (IOException ex) {
			}
			try {
				out.close();
			}
			catch (IOException ex) {
			}
		}

	}

}
