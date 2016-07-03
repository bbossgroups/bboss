/*
 * The contents of this file are subject to the GNU Lesser General Public
 * License Version 2.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/lesser.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Developer:
 * Todd Ditchendorf, todd@ditchnet.org
 *
 */

/**
 *	@author Todd Ditchendorf
 *	@version 0.8
 *	@since 0.8
 */
package org.ditchnet.jsp.taglib.tabs.listener;

import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Collections;
import java.io.File;
import java.io.Reader;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *	@author Todd Ditchendorf
 *	@since 0.8
 *	
 *	<p><code>ServletContextListener</code> that handles extracting CSS,
 *	JavaScript, and image resource files from this taglib's JAR file, and 
 *	places them in a directory called <code>org.ditchnet.taglib/</code> in this 
 *	web app's root directory. Why do this? The HTML files in which the tabs are 
 *	used must have access to the CSS, JavaScript and image files that render
 *	the tabs correctly. Therefore, they must be placed in a known location 
 *	outside of the <code>WEB-INF</code> directory so that client browsers can
 *	access them.</p>
 *	<p>By implementing this feature as as <code>ServletContextListener</code>,
 *	These resources are deployed immediately on reloading your web app, and 
 *	are therefore available before the first request!
 */
public class TabServletContextListener implements ServletContextListener {
	
	private static final String DEST_FOLDER_NAME = "/include/";
	public  static final String SCRIPT_URI = DEST_FOLDER_NAME + "tabs.js";
	public  static final String STYLE_URI  = DEST_FOLDER_NAME + "tabstyle.jsp";
	
	private static final List DEST_PATHS = Arrays.asList(
			new String [] { SCRIPT_URI, STYLE_URI });
	
	private static final List RESOURCE_PATHS = Arrays.asList(
		new String [] {"/lib/js/tabs.js", "/lib/style/tabs.css" });

	private static final String IMAGE_RESOURCE_FOLDER = "/lib/images/";
	
	private static final List IMAGE_RESOURCE_NAMES = Arrays.asList(
			new String [] {"default_tab_bg_left.gif","default_tab_bg_right.gif",
			"default_tab_bg_white_left.gif","default_tab_bg_white_right.gif"});
	
	/**
	 *	Handles finding all CSS, JavaScript, and image resources from this
	 *	taglib's JAR file and copies them to a directory in the web app's
	 *	root directory where the tab components can link to them.
	 */
	public void contextInitialized(final ServletContextEvent evt) {
		ServletContext servletContext = evt.getServletContext();
		createDitchnetDir(servletContext);
		String sourcePath,destPath,fileName;
		URL sourceURL;
		int i = 0;
		for (Iterator iter = RESOURCE_PATHS.iterator(); iter.hasNext();) {
			sourcePath = (String)iter.next();
			sourceURL  = getClass().getResource(sourcePath);
			destPath   = (String)DEST_PATHS.get(i);
			destPath   = servletContext.getRealPath(destPath);
			writeFile(sourceURL,destPath,servletContext);
			i++;
		}
		for (Iterator iter = IMAGE_RESOURCE_NAMES.iterator(); iter.hasNext();) {
			fileName   = (String)iter.next();
			sourcePath = IMAGE_RESOURCE_FOLDER + fileName;
			sourceURL  = getClass().getResource(sourcePath);
			destPath   = DEST_FOLDER_NAME + fileName;
			destPath   = servletContext.getRealPath(destPath);
			writeFile(sourceURL,destPath,servletContext);
		}
	}
	
	public void contextDestroyed(final ServletContextEvent evt) { }
	
	private void createDitchnetDir(final ServletContext servletContext) {
		String dirPath = servletContext.getRealPath(DEST_FOLDER_NAME);
		File dir = null;
		try {
			dir = new File(dirPath);
			dir.mkdir();
		} catch (Exception e) {
			//log.error("Error creating Ditchnet dir");
		}
	}
	
	private void writeFile(final URL fromURL,
						   final String toPath,
						   final ServletContext servletContext) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(fromURL.openStream());
			out = new BufferedOutputStream(new FileOutputStream(toPath));
			int len;
			byte [] buffer = new byte [4096];
			while ((len = in.read(buffer,0,buffer.length)) != -1) {
				out.write(buffer,0,len);
			}
			out.flush();
		} catch (Exception e) {
			//log.error("Error writing file dude: " + e.getMessage());
		} finally {
			try { in.close(); out.close(); } catch (Exception e) { }
		}
	}
	
}
