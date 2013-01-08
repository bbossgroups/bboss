package org.frameworkset.http.converter.wordpdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import ooo.connector.BootstrapSocketConnector;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;

import com.frameworkset.util.Util;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XEventListener;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.text.XBookmarksSupplier;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.xml.dom.XDocument;

public class FileConvertor {
	 private static OfficeManager officeManager ;
	 private static XComponentContext context;
	 public static void initXComponentContext(String officeHome) throws Exception
	 {
		 System.out.println(
	                "WriterDoc: Start up or connect to the remote service manager.");

	        // Get remote service manager.  We only need one instance regardless
	        // of the number of documents we create.
	        String oooExeFolder = officeHome!= null ?officeHome + "/program/":null;
	        if (context == null) {
	        	synchronized(FileConvertor.class)
	        	{
	        		 if (context == null) {
			            try {
			            	if(oooExeFolder == null)
			            		context = Bootstrap.bootstrap();
			            	else
			            		context = BootstrapSocketConnector.bootstrap(oooExeFolder);			            	
			            	 System.out.println(
			     	                "WriterDoc: Finshed Start up or connect to the remote service manager.");

			            } catch (BootstrapException e) {
			                throw new java.lang.Exception(e);
			            }
	        		 }
	        	}	        	
	        }
	 }
	public static void init(String officeHome)
	{
		
		if(officeManager == null)
		{
			synchronized(FileConvertor.class)
			{
				try {
					if(officeManager == null)
					{
						DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
//					config.setOfficeHome("E:\\Program Files\\OpenOffice.org 3");
						if(officeHome != null)
							config.setOfficeHome(officeHome);
						officeManager = config.buildOfficeManager();
						officeManager.start();
					}
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OfficeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public static File getRealWord(String wordtemplate, String wordfile,
			Map<String, Object> bookdatas) throws Exception {

		/*
		 * 1.将现有的word模板复制一份，保存为合同编号.doc 2.在新的文档里面插入动态值 3.转为pdf
		 */


		if (wordtemplate != null) {
			FileInputStream template = null;

			FileOutputStream contract = null;
			try {
				template = new FileInputStream(wordtemplate);

				contract = new FileOutputStream(wordfile);
				byte[] buf = new byte[32];
				int hasRead = 0;
				while ((hasRead = template.read(buf)) > 0) {
					contract.write(buf, 0, hasRead);
				}
			} catch (Exception e) {
				throw e;
			} finally {
				if (null != template) {
					template.close();
				}
				if (null != contract) {
					contract.close();
				}
			}
			// 2.在新的文档里面插入动态值
			ActiveXComponent word = null;
			Dispatch doc = null;
			try {
				
				if (bookdatas != null && bookdatas.size() > 0) {
					word = new ActiveXComponent("Word.Application");
					word.setProperty("Visible", new Variant(false));
					Dispatch documents = word.getProperty("Documents").toDispatch();
					doc = Dispatch.call(documents, "Open", wordfile).toDispatch();
					Dispatch activeDocument = word
							.getProperty("ActiveDocument").toDispatch();
					Dispatch Marks = ActiveXComponent.call(activeDocument,
							"Bookmarks").toDispatch();
					Set<String> keys = bookdatas.keySet();
					for (String key : keys) {
						boolean bookMarkExist = ActiveXComponent.call(Marks,
								"Exists", key).toBoolean(); // 查找标签
						if (bookMarkExist) {

							Dispatch rangeItem = Dispatch.call(Marks, "Item",
									key).toDispatch();
							Dispatch range = Dispatch.call(rangeItem, "Range")
									.toDispatch();
							Dispatch.put(range, "Text",
									new Variant(bookdatas.get(key)));// 插入书签的值
						}
						else
						{
							System.out.println(
				                    "Cannot find bookmark '" + key + "'");
						}
					}
					Dispatch.call(doc, "Save");
				}

			} finally {
				if (doc != null) {
					try {
						Dispatch.call(doc, "Close", new Variant(true));
						doc = null;
					} catch (Exception e) {

					}
				}

				if (word != null) {
					try {
						Dispatch.call(word, "Quit");
						word = null;
					} catch (Exception e) {

					}
				}
			}
		}

		return new File(wordfile);

	}
	
	public static File getRealWordByOpenoffice(String wordtemplate, String wordfile,
			Map<String, Object> bookdatas) throws Exception {

		/*
		 * 1.将现有的word模板复制一份，保存为合同编号.doc 2.在新的文档里面插入动态值 3.转为pdf
		 */


		/*
		 * 1.将现有的word模板复制一份，保存为合同编号.doc 2.在新的文档里面插入动态值 3.转为pdf
		 */
	
		if (wordtemplate != null) {
			FileInputStream template = null;

			FileOutputStream contract = null;
			try {
				template = new FileInputStream(wordtemplate);

				contract = new FileOutputStream(wordfile);
				byte[] buf = new byte[32];
				int hasRead = 0;
				while ((hasRead = template.read(buf)) > 0) {
					contract.write(buf, 0, hasRead);
				}
			} catch (Exception e) {
//				e.printStackTrace();
				throw e;
			} finally {
				if (null != template) {
					template.close();
				}
				if (null != contract) {
					contract.close();
				}
			}
			if(bookdatas != null && bookdatas.size() > 0)
			{
				
				initXComponentContext(null);
				// 2.在新的文档里面插入动态值
				XComponent document = null;
//				 XModel model = null;
				
	//	        FileConvertor.init(null);
	
		        ////////////////////////////////////////////////////////////////////
		        // Create the document.
		        ////////////////////////////////////////////////////////////////////
				try
				{
			        System.out.println(
			                "WriterDoc: Start up or connect to the remote service manager.");
		
			       
			        XMultiComponentFactory serviceManager = context.getServiceManager();
			        
			        // Retrieve the Desktop object and get its XComponentLoader.
			        Object desktop = serviceManager.createInstanceWithContext(
			                "com.sun.star.frame.Desktop", context);
			        XComponentLoader loader = (XComponentLoader) UnoRuntime.queryInterface(
			                XComponentLoader.class, desktop);
		
			        // Define general document properties (see
			        // com.sun.star.document.MediaDescriptor for the possibilities).
			        ArrayList<PropertyValue> props = new ArrayList<PropertyValue>();
			        PropertyValue p = null;
			      
		            // Enable the use of a template document.
		            p = new PropertyValue();
		            p.Name = "AsTemplate";
		            p.Value = new Boolean (true);
		            props.add(p);
			       
			       
			        // Make the document initially invisible so the user does not
			        // have to watch it being built.
			        p = new PropertyValue();
			        p.Name = "Hidden";
			        p.Value = new Boolean(true);
			        props.add(p);
		
			        PropertyValue[] properties = new PropertyValue[props.size()];
			        props.toArray(properties);
		
			        // Create the document
			        // (see com.sun.star.frame.XComponentLoader for argument details).
		
		
			      
		            // Create a new document that is a duplicate of the template.
		
		            System.out.println(
		                    "WriterDoc: Create the document '" + wordfile +
		                    "' based on template " + wordtemplate + ".");
		
		            String templateFileURL = filePathToURL(wordfile);
		            document = loader.loadComponentFromURL(
		                    templateFileURL,    // URL of templateFile.
		                    "_blank",           // Target frame name (_blank creates new frame).
		                    0,                  // Search flags.
		                    properties);        // Document attributes.
			       
		
			        // Get the document window and frame.
		
//			        model = (XModel) UnoRuntime.queryInterface(
//			                XModel.class, document);
			       
		//	        XController c = model.getCurrentController();
		//	        XFrame frame = c.getFrame();
		//	        XWindow window = frame.getContainerWindow();
		
			        ////////////////////////////////////////////////////////////////////
			        // Add document window listeners.
			        ////////////////////////////////////////////////////////////////////
		
			        System.out.println("WriterDoc: Add window listeners.");
		
			        // Example of adding a document displose listener so the application
			        // can know if the user manually exits the Writer window.
		
			        document.addEventListener(new XEventListener() {
			            public void disposing(EventObject e) {
			                System.out.println(
			                        "WriterDoc (Event Listener): The document window is closing.");
			            }
			        });
		
			        // Example of adding a window listener so the application can know
			        // when the document becomes initially visible (in the case of this
			        // implementation, we will manually set it visible below after we
			        // finish building it).
		
		//	        window.addWindowListener(new XWindowListener() {
		//	            public void windowShown(com.sun.star.lang.EventObject e) {
		//	                System.out.println(
		//	                        "WriterDoc (Window listener): The document window has become visible.");
		//	            }
		//	            public void windowHidden(com.sun.star.lang.EventObject e) { }
		//	            public void disposing(com.sun.star.lang.EventObject e) { }
		//	            public void windowResized(com.sun.star.awt.WindowEvent e) { }
		//	            public void windowMoved(com.sun.star.awt.WindowEvent e) { }
		//	        });
		
			        ////////////////////////////////////////////////////////////////////
			        // Control whether user can edit the document.
			        ////////////////////////////////////////////////////////////////////
		
		//	        if (! isUserEditable) {
		//	            window.setEnable(isUserEditable);
		//	        }
		
			        ////////////////////////////////////////////////////////////////////
			        // Get the document's fields and bookmarks if any.
			        ////////////////////////////////////////////////////////////////////
		
			        System.out.println("WriterDoc: Get document fields/bookmarks if any.");
		
			        // Fetch field and style information.
			        // NOTE: I have not found a use for these yet.
		
			       
		
			        // Get the document's bookmark name supplier.
		
			        XBookmarksSupplier bookmarksSupplier = (XBookmarksSupplier)
			        UnoRuntime.queryInterface(XBookmarksSupplier.class, document);
		
			        ////////////////////////////////////////////////////////////////////
			        // Example of inserting text into a field bookmarked in the template
			        ////////////////////////////////////////////////////////////////////
			        
			        XTextRange textRange = null;
		
			        // XText is the main interface to a distinct text unit, for
			        // example, the main text of a document, the text for headers
			        // and footers, or a single cell of a table.  It's
			        // insertTextContent method allows insertion of a text table,
			        // text frame, or text field.
			        XText textContent = null;
		
			        // XTextCursor provides methods to move the insertion/extraction
			        // cursor within an XText text unit.  In this case, we're
			        // defaulting the cursor to the beginning of the text unit.
			        XTextCursor textCursor = null;
			        int i = 0;
			        Set<String> keys = bookdatas.keySet();
			        for(String bookmarkName:keys)
			        {
				        // The name of the bookmark to insert the text into.
				       
			
				      
			            System.out.println(
			                    "WriterDoc: Insert text into the bookmarked text field named '" +
			                    bookmarkName + "'.");
			            // Get the XTextRange associated with the specified bookmark.
			            textRange = getBookmarkTextRange(bookmarksSupplier, bookmarkName);
			            if(textRange == null)
			            	continue;
			            textContent = textRange.getText();
			            // Create a text cursor then navigate to the text range.
			            textCursor = textContent.createTextCursor();
			            textCursor.gotoRange(textRange, false);
			      
				       
			
	//			        // How we will insert the text.
	//			        TextInsertionModeEnum textInsertMode = TextInsertionModeEnum.REPLACE_ALL;
			
				        // The text we will insert.
				        String textToInsert = String.valueOf(bookdatas.get(bookmarkName));
	//			        if (textInsertMode == TextInsertionModeEnum.ADD_SENTENCE) {
	//			            textToInsert = textToInsert;
	//			        }
	//			        else if (textInsertMode == TextInsertionModeEnum.ADD_PARAGRAPH) {
	//			            textToInsert = "\r\n" + textToInsert;
	//			        }
			
				        // Insert the given text at the bookmark position according to
				        // the desired insertion mode.
	//			        if (textInsertMode == TextInsertionModeEnum.REPLACE_ALL) {
				            textCursor.setString(textToInsert);
	//			        }
	//			        else {
	//			            String existingText = textCursor.getString();
	//			            if (existingText.length() > 0) {
	//			                textCursor.goRight((short) 0, false);
	//			            }
	//			            textCursor.setString(textToInsert);
	//			        }
			
				        // Move to the end of the text we just inserted.
				        textCursor.goRight((short) textToInsert.length(), false);
			
				        // NOTE: If the inserted text needs to have a particular font
				        // and/or color, then do the following in the above code:
				        //
				        //    1. Before calling setString, save the XTextCursor's property set.
				        //    2. Modify the XTextCursor's property set as desired.  Following
				        //       are the names of properties typically modified for color and
				        //       font: CharColor, CharFontName, CharFontFamily, CharFontPitch,
				        //       CharFontPosture, CharFontWeight, CharHeight.
				        //    3. After calling setString and moving the cursor to the end of
				        //       the inserted text, restore the XTextCursor's original properties.
			
			        }
		
			      
		
			        ////////////////////////////////////////////////////////////////////
			        // Make the document visible.
			        ////////////////////////////////////////////////////////////////////
		
			        System.out.println(
			                "WriterDoc: Document construction finished - make it visible.");
		
		//	        window.setVisible(true);
			       
			        saveDoc(document, wordfile, DocumentFormatEnum.MSWORD, true);
				}
				finally
				{
//					try {
//						if(model != null)
//							model.dispose();
//					} catch (Exception e) {
//						
//					}
					try {
						if(document != null)
							document.dispose();
					} catch (Exception e) {
						
					} 
				}
		        
			}
		}

		return new File(wordfile);

	}

	public static File getRealWord(String wordtemplate, String wordfile,
			String[] bookMarks, String[] bookdatas) throws Exception {

		/*
		 * 1.将现有的word模板复制一份，保存为合同编号.doc 2.在新的文档里面插入动态值 3.转为pdf
		 */


		if (wordtemplate != null) {
			FileInputStream template = null;

			FileOutputStream contract = null;
			try {
				template = new FileInputStream(wordtemplate);

				contract = new FileOutputStream(wordfile);
				byte[] buf = new byte[32];
				int hasRead = 0;
				while ((hasRead = template.read(buf)) > 0) {
					contract.write(buf, 0, hasRead);
				}
			} catch (Exception e) {
//				e.printStackTrace();
				throw e;
			} finally {
				if (null != template) {
					template.close();
				}
				if (null != contract) {
					contract.close();
				}
			}
			// 2.在新的文档里面插入动态值
			ActiveXComponent word = null;
			Dispatch doc = null;
			try {
				
				if (bookMarks != null && bookMarks.length > 0) {
					word = new ActiveXComponent("Word.Application");
					word.setProperty("Visible", new Variant(false));
					Dispatch documents = word.getProperty("Documents").toDispatch();
					doc = Dispatch.call(documents, "Open", wordfile).toDispatch();
					Dispatch activeDocument = word
							.getProperty("ActiveDocument").toDispatch();
					Dispatch Marks = ActiveXComponent.call(activeDocument,
							"Bookmarks").toDispatch();
					int i = 0;
					for (String key : bookMarks) {
						boolean bookMarkExist = ActiveXComponent.call(Marks,
								"Exists", key).toBoolean(); // 查找标签
						if (bookMarkExist) {

							Dispatch rangeItem = Dispatch.call(Marks, "Item",
									key).toDispatch();
							Dispatch range = Dispatch.call(rangeItem, "Range")
									.toDispatch();
							Dispatch.put(range, "Text", new Variant(
									bookdatas[i]));// 插入书签的值
						}
						else
						{
							System.out.println(
				                    "Cannot find bookmark '" + key + "'");
						}
						i++;
					}
					Dispatch.call(doc, "Save");
				}

			} finally {
				if (doc != null) {
					try {
						Dispatch.call(doc, "Close", new Variant(true));
						doc = null;
					} catch (Exception e) {

					}
				}

				if (word != null) {
					try {
						Dispatch.call(word, "Quit");
						word = null;
					} catch (Exception e) {

					}
				}
			}
		}

		return new File(wordfile);

	}
	
	public static File getRealWordByOpenoffice(String wordtemplate, String wordfile,
			String[] bookMarks, String[] bookdatas) throws Exception {

		/*
		 * 1.将现有的word模板复制一份，保存为合同编号.doc 2.在新的文档里面插入动态值 3.转为pdf
		 */
	
		if (wordtemplate != null) {
			FileInputStream template = null;

			FileOutputStream contract = null;
			try {
				template = new FileInputStream(wordtemplate);

				contract = new FileOutputStream(wordfile);
				byte[] buf = new byte[32];
				int hasRead = 0;
				while ((hasRead = template.read(buf)) > 0) {
					contract.write(buf, 0, hasRead);
				}
			} catch (Exception e) {
//				e.printStackTrace();
				throw e;
			} finally {
				if (null != template) {
					template.close();
				}
				if (null != contract) {
					contract.close();
				}
			}
			if(bookMarks != null && bookMarks.length > 0)
			{
				
				initXComponentContext(null);
				// 2.在新的文档里面插入动态值
				XComponent document = null;
//				XModel model = null;
	//	        FileConvertor.init(null);
	
		        ////////////////////////////////////////////////////////////////////
		        // Create the document.
		        ////////////////////////////////////////////////////////////////////
				try
				{
			        System.out.println(
			                "WriterDoc: Start up or connect to the remote service manager.");
		
			       
			        XMultiComponentFactory serviceManager = context.getServiceManager();
			        
			        // Retrieve the Desktop object and get its XComponentLoader.
			        Object desktop = serviceManager.createInstanceWithContext(
			                "com.sun.star.frame.Desktop", context);
			        XComponentLoader loader = (XComponentLoader) UnoRuntime.queryInterface(
			                XComponentLoader.class, desktop);
		
			        // Define general document properties (see
			        // com.sun.star.document.MediaDescriptor for the possibilities).
			        ArrayList<PropertyValue> props = new ArrayList<PropertyValue>();
			        PropertyValue p = null;
			      
		            // Enable the use of a template document.
		            p = new PropertyValue();
		            p.Name = "AsTemplate";
		            p.Value = new Boolean (true);
		            props.add(p);
			       
			       
			        // Make the document initially invisible so the user does not
			        // have to watch it being built.
			        p = new PropertyValue();
			        p.Name = "Hidden";
			        p.Value = new Boolean(true);
			        props.add(p);
		
			        PropertyValue[] properties = new PropertyValue[props.size()];
			        props.toArray(properties);
		
			        // Create the document
			        // (see com.sun.star.frame.XComponentLoader for argument details).
		
		
			      
		            // Create a new document that is a duplicate of the template.
		
		            System.out.println(
		                    "WriterDoc: Create the document '" + wordfile +
		                    "' based on template " + wordtemplate + ".");
		
		            String templateFileURL = filePathToURL(wordfile);
		            document = loader.loadComponentFromURL(
		            		templateFileURL,    // URL of templateFile.
		                    "_blank",           // Target frame name (_blank creates new frame).
		                    0,                  // Search flags.
		                    properties);        // Document attributes.
			       
		
			        // Get the document window and frame.
		
//			        model = (XModel) UnoRuntime.queryInterface(
//			                XModel.class, document);
		//	        XController c = model.getCurrentController();
		//	        XFrame frame = c.getFrame();
		//	        XWindow window = frame.getContainerWindow();
		
			        ////////////////////////////////////////////////////////////////////
			        // Add document window listeners.
			        ////////////////////////////////////////////////////////////////////
		
			        System.out.println("WriterDoc: Add window listeners.");
		
			        // Example of adding a document displose listener so the application
			        // can know if the user manually exits the Writer window.
		
			        document.addEventListener(new XEventListener() {
			            public void disposing(EventObject e) {
			                System.out.println(
			                        "WriterDoc (Event Listener): The document window is closing.");
			            }
			        });
		
			        // Example of adding a window listener so the application can know
			        // when the document becomes initially visible (in the case of this
			        // implementation, we will manually set it visible below after we
			        // finish building it).
		
		//	        window.addWindowListener(new XWindowListener() {
		//	            public void windowShown(com.sun.star.lang.EventObject e) {
		//	                System.out.println(
		//	                        "WriterDoc (Window listener): The document window has become visible.");
		//	            }
		//	            public void windowHidden(com.sun.star.lang.EventObject e) { }
		//	            public void disposing(com.sun.star.lang.EventObject e) { }
		//	            public void windowResized(com.sun.star.awt.WindowEvent e) { }
		//	            public void windowMoved(com.sun.star.awt.WindowEvent e) { }
		//	        });
		
			        ////////////////////////////////////////////////////////////////////
			        // Control whether user can edit the document.
			        ////////////////////////////////////////////////////////////////////
		
		//	        if (! isUserEditable) {
		//	            window.setEnable(isUserEditable);
		//	        }
		
			        ////////////////////////////////////////////////////////////////////
			        // Get the document's fields and bookmarks if any.
			        ////////////////////////////////////////////////////////////////////
		
			        System.out.println("WriterDoc: Get document fields/bookmarks if any.");
		
			        // Fetch field and style information.
			        // NOTE: I have not found a use for these yet.
		
			       
		
			        // Get the document's bookmark name supplier.
		
			        XBookmarksSupplier bookmarksSupplier = (XBookmarksSupplier)
			        UnoRuntime.queryInterface(XBookmarksSupplier.class, document);
		
			        ////////////////////////////////////////////////////////////////////
			        // Example of inserting text into a field bookmarked in the template
			        ////////////////////////////////////////////////////////////////////
			        
			        XTextRange textRange = null;
		
			        // XText is the main interface to a distinct text unit, for
			        // example, the main text of a document, the text for headers
			        // and footers, or a single cell of a table.  It's
			        // insertTextContent method allows insertion of a text table,
			        // text frame, or text field.
			        XText textContent = null;
		
			        // XTextCursor provides methods to move the insertion/extraction
			        // cursor within an XText text unit.  In this case, we're
			        // defaulting the cursor to the beginning of the text unit.
			        XTextCursor textCursor = null;
			        for(int i = 0; i < bookMarks.length; i ++)
			        {
				        // The name of the bookmark to insert the text into.
				        String bookmarkName = bookMarks[i];
			
				      
			            System.out.println(
			                    "WriterDoc: Insert text into the bookmarked text field named '" +
			                    bookmarkName + "'.");
			            // Get the XTextRange associated with the specified bookmark.
			            textRange = getBookmarkTextRange(bookmarksSupplier, bookmarkName);
			            if(textRange == null)
			            	continue;
			            textContent = textRange.getText();
			            // Create a text cursor then navigate to the text range.
			            textCursor = textContent.createTextCursor();
			            textCursor.gotoRange(textRange, false);
			      
				       
			
	//			        // How we will insert the text.
	//			        TextInsertionModeEnum textInsertMode = TextInsertionModeEnum.REPLACE_ALL;
			
				        // The text we will insert.
				        String textToInsert = bookdatas[i];
	//			        if (textInsertMode == TextInsertionModeEnum.ADD_SENTENCE) {
	//			            textToInsert = textToInsert;
	//			        }
	//			        else if (textInsertMode == TextInsertionModeEnum.ADD_PARAGRAPH) {
	//			            textToInsert = "\r\n" + textToInsert;
	//			        }
			
				        // Insert the given text at the bookmark position according to
				        // the desired insertion mode.
	//			        if (textInsertMode == TextInsertionModeEnum.REPLACE_ALL) {
				            textCursor.setString(textToInsert);
	//			        }
	//			        else {
	//			            String existingText = textCursor.getString();
	//			            if (existingText.length() > 0) {
	//			                textCursor.goRight((short) 0, false);
	//			            }
	//			            textCursor.setString(textToInsert);
	//			        }
			
				        // Move to the end of the text we just inserted.
				        textCursor.goRight((short) textToInsert.length(), false);
			
				        // NOTE: If the inserted text needs to have a particular font
				        // and/or color, then do the following in the above code:
				        //
				        //    1. Before calling setString, save the XTextCursor's property set.
				        //    2. Modify the XTextCursor's property set as desired.  Following
				        //       are the names of properties typically modified for color and
				        //       font: CharColor, CharFontName, CharFontFamily, CharFontPitch,
				        //       CharFontPosture, CharFontWeight, CharHeight.
				        //    3. After calling setString and moving the cursor to the end of
				        //       the inserted text, restore the XTextCursor's original properties.
			
			        }
		
			      
		
			        ////////////////////////////////////////////////////////////////////
			        // Make the document visible.
			        ////////////////////////////////////////////////////////////////////
		
			        System.out.println(
			                "WriterDoc: Document construction finished - make it visible.");
		
		//	        window.setVisible(true);
			       
			        saveDoc(document, wordfile, DocumentFormatEnum.MSWORD, true);
				}
				finally
				{
//					try {
//						if(model != null)
//							model.dispose();
//					} catch (Exception e) {
//						
//					}
					try {
						if(document != null)
							document.dispose();
					} catch (Exception e) {
						
					} 
				}
		        
			}
		}

		return new File(wordfile);

	}
	
	private static XTextRange getBookmarkTextRange(
            XBookmarksSupplier bookmarksSupplier, String bookmarkName)
        throws java.lang.Exception {
    	
        XTextRange textRange = null;

        // Get the collection of bookmarks in the document.
        XNameAccess bookmarkNames = bookmarksSupplier.getBookmarks();

        // Find the bookmark having the given name.
        Object bmk = null;
        try {
            bmk = bookmarkNames.getByName(bookmarkName);
        } catch (NoSuchElementException e) {
        }
        if (bmk == null) {
           System.out.println(
                    "Cannot find bookmark '" + bookmarkName + "'");
           return null;
        }

        // Get the bookmark's XTextContent.  It allows objects to be
        // inserted into a text and provides their location within a
        // text after insertion.

        XTextContent bookmarkContent = (XTextContent)
            UnoRuntime.queryInterface(XTextContent.class, bmk);

        // Get the bookmark's XTextRange.  It describes the object's
        // position within the text and provides access to the text.

        textRange = bookmarkContent.getAnchor();

        return textRange;
    }

	
	/**
     * Save a given document to a specified file.
     * <p>
     * @param document
     *      The 'handle' to the document.
     * @param saveFile
     *      The full path of the file in which to save the document.
     *      If null, this method will do nothing.
     * @param format
     *      The format in which to save the document.
     * @param overwrite
     *      If true, and the file already exists, it will be
     *      overwritten.  Otherwise, if the file already exists an
     *      exception will be thrown.
     */
    private static  void saveDoc(
            XDocument document,
            String saveFile,
            DocumentFormatEnum format,
            boolean overwrite) throws java.lang.Exception {

        if ((saveFile == null) || (saveFile.trim().length() == 0)) { return; }
        if (! overwrite) {
            File f = new File(saveFile);
            if (f.exists()) {
                throw new java.lang.Exception(
                        "File " + saveFile + " already exists; overwriting disabled.");
            }
        }

        String saveFileURL = filePathToURL(saveFile);
        XStorable storable = (XStorable) UnoRuntime.queryInterface(
                XStorable.class, document);

        PropertyValue[] properties = new PropertyValue[1];
        PropertyValue p = new PropertyValue();
        p.Name = "FilterName";
        p.Value = format.getFormatCode();
        properties[0] = p;

        storable.storeAsURL(saveFileURL, properties);
    }
    
    /** Convert a file path to URL format. */
    private static String filePathToURL(String file) {
        File f = new File(file);
        StringBuffer sb = new StringBuffer("file:///");
        try {
            sb.append(f.getCanonicalPath().replace('\\', '/'));
        } catch (IOException e) {
        }
        return sb.toString();
    }
    
    private static void saveDoc(
    		XComponent document,
            String saveFile,
            DocumentFormatEnum format,
            boolean overwrite) throws java.lang.Exception {

        if ((saveFile == null) || (saveFile.trim().length() == 0)) { return; }
        if (! overwrite) {
            File f = new File(saveFile);
            if (f.exists()) {
                throw new java.lang.Exception(
                        "File " + saveFile + " already exists; overwriting disabled.");
            }
        }

        String saveFileURL = filePathToURL(saveFile);
        XStorable storable = (XStorable) UnoRuntime.queryInterface(
                XStorable.class, document);

        PropertyValue[] properties = new PropertyValue[1];
        PropertyValue p = new PropertyValue();
        p.Name = "FilterName";
        p.Value = format.getFormatCode();
        properties[0] = p;

        storable.storeAsURL(saveFileURL, properties);
    }

	public static  File realWordConvertorByFlashPager(String flashpaperWorkDir, String wordtemplate,
			String wordfile, String[] bookMarks, String[] bookdatas,
			String topath,long waittimes) throws Exception {

		FileConvertor.getRealWord(wordtemplate, wordfile, bookMarks,bookdatas);
		flashPaperConvert(flashpaperWorkDir, wordfile, topath,waittimes);

		return new File(topath);

	}
	
	public static  File realWordConvertorBySWFTool(String swftoolWorkDir, String wordtemplate,
			String wordfile, String[] bookMarks, String[] bookdatas,String pdfpath,
			String swfpath) throws Exception {
		FileConvertor.getRealWord(wordtemplate, wordfile, bookMarks,bookdatas);
		FileConvertor.wordToPDFByOpenOffice(wordfile, pdfpath);
		FileConvertor.swftoolsConvert(swftoolWorkDir, pdfpath, swfpath);

		return new File(swfpath);

	}
	public static final int MERGE_BEFORE = 1;
	public static final int MERGE_AFTER = 0;
	public static boolean mergePdfFiles(String[] files, String firstfile,String tofile,int mergeposition) throws Exception {
		boolean retValue = false;
		Document document = null;
		try {

			if (mergeposition == MERGE_AFTER) {
				PdfReader reader_ = new PdfReader(firstfile);
				document = new Document(new PdfReader(reader_).getPageSize(1));
				PdfCopy copy = new PdfCopy(document, new FileOutputStream(
						tofile));
				document.open();
				reader_ = new PdfReader(firstfile);
				int nf = reader_.getNumberOfPages();
				for (int j = 1; j <= nf; j++) {

					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader_, j);
					copy.addPage(page);
				}
				for (int i = 0; i < files.length; i++) {
					reader_ = new PdfReader(files[i]);
					int n = reader_.getNumberOfPages();
					for (int j = 1; j <= n; j++) {
						document.newPage();
						PdfImportedPage page = copy.getImportedPage(reader_, j);
						copy.addPage(page);
					}
				}
			} else {
				PdfReader reader_ = new PdfReader(files[0]);
				document = new Document(new PdfReader(reader_).getPageSize(1));
				PdfCopy copy = new PdfCopy(document, new FileOutputStream(
						tofile));
				document.open();
				for (int i = 0; i < files.length; i++) {
					PdfReader reader = new PdfReader(files[i]);
					int n = reader.getNumberOfPages();
					for (int j = 1; j <= n; j++) {
						document.newPage();
						PdfImportedPage page = copy.getImportedPage(reader, j);
						copy.addPage(page);
					}
				}
				reader_ = new PdfReader(firstfile);
				int nf = reader_.getNumberOfPages();
				for (int j = 1; j <= nf; j++) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader_, j);
					copy.addPage(page);
				}
			}
			retValue = true;
		} catch (Exception e) {
			throw e;
		} finally {
			document.close();
		}
		return retValue;
	}
	public static  File realWordConvertorByFlashPaper(String flashpaperWorkDir, String wordtemplate,
			String wordfile, Map<String,Object> bookdatas,
			String topath,long waittimes) throws Exception {

		FileConvertor.getRealWord(wordtemplate, wordfile, bookdatas);
		flashPaperConvert(flashpaperWorkDir, wordfile, topath, waittimes);

		return new File(topath);

	}
	public static void assertInitOfficeManager()
	{
		if(officeManager == null)
		{
			throw new AssertInitOfficeManagerFailed("Openoffice not right inited.please call method FileConvertor.init(String officeHome).");
		}
	}
	public static File wordToPDFByOpenOffice(String wordfile,String pdffile)
	{
		File pdfFile = new File(pdffile);
		File wordContract = new File(wordfile);
		init(null);
		assertInitOfficeManager();
		OfficeDocumentConverter converter = new OfficeDocumentConverter(
				officeManager);
				
		converter.convert(wordContract, pdfFile);
		return pdfFile;
	}
	public static void swftoolsConvert(String swftoolsWorkDir,
			String sourcePath, String destPath) throws Exception
	{
		//pdf2swf.exe -t D:\anjie_test.pdf  -s flashversion=9 -o d:\anjie_test.swf
		String osname = Util.getOS();
		String command = null;
		if(Util.isWindows(osname))
		{
			command = swftoolsWorkDir + "pdf2swf.exe " + sourcePath
				+ " -s flashversion=9 -o " + destPath;
		}
		else
		{
			command = swftoolsWorkDir + "pdf2swf " + sourcePath
					+ " -s flashversion=9 -o " + destPath;
		}

		Process pro = Runtime.getRuntime().exec(command);
		try {			
			pro.waitFor();
		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	public static void flashPaperConvert(String flashpaperWorkDir,
			String sourcePath, String destPath,long waittimes) throws Exception {
		String command = flashpaperWorkDir + "flashprinter.exe " + sourcePath
				+ " -o " + destPath;

		Process pro = Runtime.getRuntime().exec(command);
		try {
			pro.waitFor();
		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
		File file = new File(destPath);
		int i = 0;
		while (true) {
			try {
				Thread.currentThread().sleep( waittimes);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
			if(file.exists())
			{
				break;
			}
			if(i == 10)
				break;
			i ++;

		}
//		long lastmodify = file.length();
//		long oldlastmodify = lastmodify;
////		try {
////			Thread.currentThread().sleep(100);
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
//////			e.printStackTrace();
////		}
//		int i = 0;
//		while(i <= 10)
//		{
//			lastmodify = file.length();
//			System.out.println(lastmodify);
//			if(lastmodify>oldlastmodify)
//			{
//				System.out.println(oldlastmodify);
//				oldlastmodify = lastmodify;
//				try {
//					Thread.currentThread().sleep(100);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
////					e.printStackTrace();
//				}
//				
//			}
//			else
//			{
//				i ++;
//			}
//		}
			
		
	}

    /** Enumeration for document formats. */
    public enum DocumentFormatEnum {
        PDF ("PDF", "writer_pdf_Export"),
        MSWORD ("MS-Word", "MS Word 97");
        private String name;
        private String code;
        private static DocumentFormatEnum[] values = { PDF, MSWORD };

        private DocumentFormatEnum(String name, String code) {
            this.name = name;
            this.code = code;
        }
        public String getName() { return name; }
        public String getFormatCode() { return code; }
        public static DocumentFormatEnum[] getValues() { return values; }
    }

    /** Enumeration for techniques of inserting text into the document. */
    public enum TextInsertionModeEnum {
        REPLACE_ALL   ("ReplaceAll", "Insert text into a specific text field, replacing contents"),
        ADD_TEXT      ("AddText", "Append text to the existing contents of a text field"),
        ADD_SENTENCE  ("AddSentence", "Append text into a specific text field as a new sentence"),
        ADD_PARAGRAPH ("AddParagraph", "Append text into a specific text field as a new paragraph");
        private String name;
        private String desc;
        private static TextInsertionModeEnum[] values =
            { REPLACE_ALL, ADD_TEXT, ADD_SENTENCE, ADD_PARAGRAPH };

        private TextInsertionModeEnum(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }
        public String getName() { return name; }
        public String getDesc() { return desc; }
        public static TextInsertionModeEnum[] getValues() { return values; }
    }

    /** Enumeration for techniques of inserting the image into a document. */
    public enum ImageInsertionModeEnum {
        INSERT_IN_TEXT_FIELD ("InsertInTextField", "Insert image into a specific text field"),
        INSERT_IN_TEXT_BODY  ("InsertInTextBody", "Insert image into the document body"),
        REPLACE_IN_TEXT_FIELD ("ReplaceInTextField", "Insert image into a specific text field, replacing existing content"),
        REPLACE_IN_TEXT_BODY ("ReplaceInTextBody", "Insert chart into the document body, replacing existing content");
        private String name;
        private String desc;
        private static ImageInsertionModeEnum[] values = {
            INSERT_IN_TEXT_FIELD,
            INSERT_IN_TEXT_BODY,
            REPLACE_IN_TEXT_FIELD,
            REPLACE_IN_TEXT_BODY
        };

        private ImageInsertionModeEnum(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }
        public String getName() { return name; }
        public String getDesc() { return desc; }
        public static ImageInsertionModeEnum[] getValues() { return values; }
    }

}
