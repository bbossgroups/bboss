/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package org.frameworkset.jodconvertor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.artofsolving.jodconverter.AbstractConversionTask;
import org.artofsolving.jodconverter.OfficeDocumentUtils;
import org.artofsolving.jodconverter.document.DocumentFamily;
import org.artofsolving.jodconverter.document.DocumentFormat;
import org.artofsolving.jodconverter.office.OfficeContext;
import org.artofsolving.jodconverter.office.OfficeException;

import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNameAccess;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XBookmarksSupplier;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.UnoRuntime;

/**
 * <p>
 * WorkBookmarkConvertorTask.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2009
 * </p>
 * 
 * @Date 2013-1-9 17:07:30
 * @author biaoping.yin
 * @version 1.0
 */
public class WorkBookmarkConvertorTask extends AbstractConversionTask {
	private final DocumentFormat outputFormat;
	private File wordtemplate;

	private Map<String, ?> defaultLoadProperties;
	private Map<String, ?> defaultStroreProperties;
	private DocumentFormat inputFormat;
	private Map<String, Object> mapbookdatas;
	private String[] bookmarks;
	private Object[] bookdatas;

	public WorkBookmarkConvertorTask(String wordtemplate, String wordfile,
			Map<String, Object> bookdatas, DocumentFormat outputFormat) {
		super(new File(wordfile), new File(wordfile));
		this.wordtemplate = new File(wordtemplate);
		this.mapbookdatas = bookdatas;
		this.outputFormat = outputFormat;
	}

	public WorkBookmarkConvertorTask(String wordtemplate, String wordfile,
			String[] bookmarks, Object[] bookdatas, DocumentFormat outputFormat) {
		super(new File(wordfile), new File(wordfile));
		this.wordtemplate = new File(wordtemplate);
		this.bookdatas = bookdatas;
		this.bookmarks = bookmarks;
		this.outputFormat = outputFormat;
	}

	@Override
	protected Map<String, ?> getLoadProperties(File inputFile) {
		Map<String, Object> loadProperties = new HashMap<String, Object>();
		if (defaultLoadProperties != null) {
			loadProperties.putAll(defaultLoadProperties);
		}
		if (inputFormat != null && inputFormat.getLoadProperties() != null) {
			loadProperties.putAll(inputFormat.getLoadProperties());
		}
		return loadProperties;
	}

	@Override
	protected Map<String, ?> getStoreProperties(File outputFile,
			XComponent document) {
		DocumentFamily family = OfficeDocumentUtils.getDocumentFamily(document);
		return outputFormat.getStoreProperties(family);
	}

	protected void initInputFile()
	{
		FileInputStream template = null;

		FileOutputStream contract = null;
		try {
			template = new FileInputStream(wordtemplate);
			contract = new FileOutputStream(this.inputFile);
			byte[] buf = new byte[32];
			int hasRead = 0;
			while ((hasRead = template.read(buf)) > 0) {
				contract.write(buf, 0, hasRead);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new OfficeException("模板模板复制出错：" + wordtemplate.getAbsolutePath()
					+ "->" + inputFile.getAbsolutePath(), e);
		} finally {
			if (null != template) {
				try {
					template.close();
				} catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
			if (null != contract) {
				try {
					contract.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void execute(OfficeContext context) throws OfficeException {
		initInputFile();
		super.execute(context);
	}

	@Override
	protected void modifyDocument(XComponent document) throws OfficeException {
		try {
			if (this.mapbookdatas != null) {
				this.modifyMapDataDocument(document);
			} else {
				this.modifyArrayDataDocument(document);
			}
		} catch (Exception e) {
			throw new OfficeException("",e);
		}
	}

	protected void modifyArrayDataDocument(XComponent document)
			throws Exception {
		XBookmarksSupplier bookmarksSupplier = (XBookmarksSupplier) UnoRuntime
				.queryInterface(XBookmarksSupplier.class, document);

		// //////////////////////////////////////////////////////////////////
		// Example of inserting text into a field bookmarked in the template
		// //////////////////////////////////////////////////////////////////

		XTextRange textRange = null;

		// XText is the main interface to a distinct text unit, for
		// example, the main text of a document, the text for headers
		// and footers, or a single cell of a table. It's
		// insertTextContent method allows insertion of a text table,
		// text frame, or text field.
		XText textContent = null;

		// XTextCursor provides methods to move the insertion/extraction
		// cursor within an XText text unit. In this case, we're
		// defaulting the cursor to the beginning of the text unit.
		XTextCursor textCursor = null;
		for (int i = 0; i < this.bookmarks.length; i++) {
			// The name of the bookmark to insert the text into.
			String bookmarkName = bookmarks[i];

			System.out
					.println("WriterDoc: Insert text into the bookmarked text field named '"
							+ bookmarkName + "'.");
			// Get the XTextRange associated with the specified bookmark.
			textRange = getBookmarkTextRange(bookmarksSupplier, bookmarkName);
			if (textRange == null)
				continue;
			textContent = textRange.getText();
			// Create a text cursor then navigate to the text range.
			textCursor = textContent.createTextCursor();
			textCursor.gotoRange(textRange, false);

			// // How we will insert the text.
			// TextInsertionModeEnum textInsertMode =
			// TextInsertionModeEnum.REPLACE_ALL;

			// The text we will insert.
			String textToInsert = String.valueOf(this.bookdatas[i]);
			// if (textInsertMode == TextInsertionModeEnum.ADD_SENTENCE) {
			// textToInsert = textToInsert;
			// }
			// else if (textInsertMode == TextInsertionModeEnum.ADD_PARAGRAPH) {
			// textToInsert = "\r\n" + textToInsert;
			// }

			// Insert the given text at the bookmark position according to
			// the desired insertion mode.
			// if (textInsertMode == TextInsertionModeEnum.REPLACE_ALL) {
			textCursor.setString(textToInsert);
			// }
			// else {
			// String existingText = textCursor.getString();
			// if (existingText.length() > 0) {
			// textCursor.goRight((short) 0, false);
			// }
			// textCursor.setString(textToInsert);
			// }

			// Move to the end of the text we just inserted.
			textCursor.goRight((short) textToInsert.length(), false);

			// NOTE: If the inserted text needs to have a particular font
			// and/or color, then do the following in the above code:
			//
			// 1. Before calling setString, save the XTextCursor's property set.
			// 2. Modify the XTextCursor's property set as desired. Following
			// are the names of properties typically modified for color and
			// font: CharColor, CharFontName, CharFontFamily, CharFontPitch,
			// CharFontPosture, CharFontWeight, CharHeight.
			// 3. After calling setString and moving the cursor to the end of
			// the inserted text, restore the XTextCursor's original properties.

		}
	}

	protected void modifyMapDataDocument(XComponent document) throws Exception {

		XBookmarksSupplier bookmarksSupplier = (XBookmarksSupplier) UnoRuntime
				.queryInterface(XBookmarksSupplier.class, document);

		// //////////////////////////////////////////////////////////////////
		// Example of inserting text into a field bookmarked in the template
		// //////////////////////////////////////////////////////////////////

		XTextRange textRange = null;

		// XText is the main interface to a distinct text unit, for
		// example, the main text of a document, the text for headers
		// and footers, or a single cell of a table. It's
		// insertTextContent method allows insertion of a text table,
		// text frame, or text field.
		XText textContent = null;

		// XTextCursor provides methods to move the insertion/extraction
		// cursor within an XText text unit. In this case, we're
		// defaulting the cursor to the beginning of the text unit.
		XTextCursor textCursor = null;
		int i = 0;
		Set<String> keys = mapbookdatas.keySet();
		for (String bookmarkName : keys) {
			// The name of the bookmark to insert the text into.

			System.out
					.println("WriterDoc: Insert text into the bookmarked text field named '"
							+ bookmarkName + "'.");
			// Get the XTextRange associated with the specified bookmark.
			textRange = getBookmarkTextRange(bookmarksSupplier, bookmarkName);
			if (textRange == null)
				continue;
			textContent = textRange.getText();
			// Create a text cursor then navigate to the text range.
			textCursor = textContent.createTextCursor();
			textCursor.gotoRange(textRange, false);

			// // How we will insert the text.
			// TextInsertionModeEnum textInsertMode =
			// TextInsertionModeEnum.REPLACE_ALL;

			// The text we will insert.
			String textToInsert = String
					.valueOf(mapbookdatas.get(bookmarkName));
			// if (textInsertMode == TextInsertionModeEnum.ADD_SENTENCE) {
			// textToInsert = textToInsert;
			// }
			// else if (textInsertMode == TextInsertionModeEnum.ADD_PARAGRAPH) {
			// textToInsert = "\r\n" + textToInsert;
			// }

			// Insert the given text at the bookmark position according to
			// the desired insertion mode.
			// if (textInsertMode == TextInsertionModeEnum.REPLACE_ALL) {
			textCursor.setString(textToInsert);
			// }
			// else {
			// String existingText = textCursor.getString();
			// if (existingText.length() > 0) {
			// textCursor.goRight((short) 0, false);
			// }
			// textCursor.setString(textToInsert);
			// }

			// Move to the end of the text we just inserted.
			textCursor.goRight((short) textToInsert.length(), false);

			// NOTE: If the inserted text needs to have a particular font
			// and/or color, then do the following in the above code:
			//
			// 1. Before calling setString, save the XTextCursor's property set.
			// 2. Modify the XTextCursor's property set as desired. Following
			// are the names of properties typically modified for color and
			// font: CharColor, CharFontName, CharFontFamily, CharFontPitch,
			// CharFontPosture, CharFontWeight, CharHeight.
			// 3. After calling setString and moving the cursor to the end of
			// the inserted text, restore the XTextCursor's original properties.

		}
	}

	protected XTextRange getBookmarkTextRange(
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
			System.out.println("Cannot find bookmark '" + bookmarkName + "'");
			return null;
		}

		// Get the bookmark's XTextContent. It allows objects to be
		// inserted into a text and provides their location within a
		// text after insertion.

		XTextContent bookmarkContent = (XTextContent) UnoRuntime
				.queryInterface(XTextContent.class, bmk);

		// Get the bookmark's XTextRange. It describes the object's
		// position within the text and provides access to the text.

		textRange = bookmarkContent.getAnchor();

		return textRange;
	}

	public Map<String, ?> getDefaultLoadProperties() {
		return defaultLoadProperties;
	}

	public void setDefaultLoadProperties(Map<String, ?> defaultLoadProperties) {
		this.defaultLoadProperties = defaultLoadProperties;
	}

	public Map<String, ?> getDefaultStroreProperties() {
		return defaultStroreProperties;
	}

	public void setDefaultStroreProperties(Map<String, ?> defaultStroreProperties) {
		this.defaultStroreProperties = defaultStroreProperties;
	}

	public void setInputFormat(DocumentFormat inputFormat2) {
		this.inputFormat = inputFormat2;
		
	}

}
