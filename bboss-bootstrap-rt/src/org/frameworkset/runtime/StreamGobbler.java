package org.frameworkset.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class StreamGobbler extends Thread {
	private static Logger logger = LoggerFactory.getLogger(StreamGobbler.class);
    InputStream is;
    String      type;
    OutputStream os;
    StreamGobbler(InputStream is, String type) {
        this(is, type, null);
    }
    public StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }
    public void run() {
    	 PrintWriter pw = null;
    	  InputStreamReader isr = null;
    	  BufferedReader br = null;
        try {
           
            if (os != null)
                pw = new PrintWriter(os);
             isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (pw != null)
                    pw.println(line);
                logger.info( line);
            }
            if (pw != null)
                pw.flush();
        } catch (IOException ioe) {
			logger.warn("",ioe);
        }
        finally{
        	try {
				if(pw != null)
					pw.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.warn("",e);
			}
        	try {
				if(os != null)
					os.close();
			} catch (Exception e) {
				logger.warn("",e);
			}
        	try {
				if(is != null)
					is.close();
			} catch (Exception e) {
				logger.warn("",e);
			}
        	try {
				if(isr != null)
					isr.close();
			} catch (Exception e) {
				logger.warn("",e);
			}
        	try {
				if(br != null)
					br.close();
			} catch (Exception e) {
				logger.warn("",e);
			}
        }
    }
}