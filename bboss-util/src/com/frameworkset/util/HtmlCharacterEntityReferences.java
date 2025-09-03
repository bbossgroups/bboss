/*
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
package com.frameworkset.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.frameworkset.util.Assert;

/**
 * <p>Title: HtmlCharacterEntityReferences.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-28
 * @author biaoping.yin
 * @version 1.0
 */
public class HtmlCharacterEntityReferences {
	static final char REFERENCE_START = '&';

	static final String DECIMAL_REFERENCE_START = "&#";

	static final String HEX_REFERENCE_START = "&#x";

	static final char REFERENCE_END = ';';

	static final char CHAR_NULL = (char) -1;


	private static final String PROPERTIES_FILE = "HtmlCharacterEntityReferences.properties";


	private final String[] characterToEntityReferenceMap = new String[3000];

	private final Map entityReferenceToCharacterMap = new HashMap(252);


	/**
	 * Returns a new set of character entity references reflecting the HTML 4.0 character set.
	 */
	public HtmlCharacterEntityReferences() {
		Properties entityReferences = new Properties();

		// Load refeence definition file.
		InputStream is = HtmlCharacterEntityReferences.class.getResourceAsStream(PROPERTIES_FILE);
		if (is == null) {
			throw new IllegalStateException(
					"Cannot find reference definition file [HtmlCharacterEntityReferences.properties] as class path resource");
		}
		try {
			try {
				entityReferences.load(is);
			}
			finally {
				is.close();
			}
		}
		catch (IOException ex) {
			throw new IllegalStateException(
					"Failed to parse reference definition file [HtmlCharacterEntityReferences.properties]: " +  ex.getMessage());
		}

		// Parse reference definition properites.
		Enumeration keys = entityReferences.propertyNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			int referredChar = Integer.parseInt(key);
			Assert.isTrue((referredChar < 1000 || (referredChar >= 8000 && referredChar < 10000)),
					"Invalid reference to special HTML entity: " + referredChar);
			int index = (referredChar < 1000 ? referredChar : referredChar - 7000);
			String reference = entityReferences.getProperty(key);
			this.characterToEntityReferenceMap[index] = REFERENCE_START + reference + REFERENCE_END;
			this.entityReferenceToCharacterMap.put(reference,   referredChar);
		}
	}

	/**
	 * Return the number of supported entity references.
	 */
	public int getSupportedReferenceCount() {
		return this.entityReferenceToCharacterMap.size();
	}

	/**
	 * Return true if the given character is mapped to a supported entity reference.
	 */
	public boolean isMappedToReference(char character) {
		return (convertToReference(character) != null);
	}

	/**
	 * Return the reference mapped to the given character or <code>null</code>.
	 */
	public String convertToReference(char character) {
		if (character < 1000 || (character >= 8000 && character < 10000)) {
			int index = (character < 1000 ? character : character - 7000);
			String entityReference = this.characterToEntityReferenceMap[index];
			if (entityReference != null) {
				return entityReference;
			}
		}
		return null;
	}

	/**
	 * Return the char mapped to the given entityReference or -1.
	 */
	public char convertToCharacter(String entityReference) {
		Character referredCharacter = (Character) this.entityReferenceToCharacterMap.get(entityReference);
		if (referredCharacter != null) {
			return referredCharacter.charValue();
		}
		return CHAR_NULL;
	}

}
