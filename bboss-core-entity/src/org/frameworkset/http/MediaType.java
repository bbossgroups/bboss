
/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.frameworkset.http;

import com.frameworkset.util.BaseSimpleStringUtil;
import org.frameworkset.util.Assert;
import org.frameworkset.util.CompoundComparator;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;

/**
 * A sub-class of {@link MimeType} that adds support for quality parameters as defined
 * in the HTTP specification.
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @author Rossen Stoyanchev
 * @author Sebastien Deleuze
 * @since 3.0
 * @see <a href="http://tools.ietf.org/html/rfc7231#section-3.1.1.1">HTTP 1.1: Semantics
 * and Content, section 3.1.1.1</a>
 */
public class MediaType extends MimeType implements Serializable {

	private static final long serialVersionUID = 2069937152339670231L;

	/**
	 * Public constant media type that includes all media ranges (i.e. "&#42;/&#42;").
	 */
	public static final MediaType ALL;

	/**
	 * A String equivalent of {@link MediaType#ALL}.
	 */
	public static final String ALL_VALUE = "*/*";

	/**
	 *  Public constant media type for {@code application/atom+xml}.
	 */
	public final static MediaType APPLICATION_ATOM_XML;

	/**
	 * A String equivalent of {@link MediaType#APPLICATION_ATOM_XML}.
	 */
	public final static String APPLICATION_ATOM_XML_VALUE = "application/atom+xml";

	/**
	 * Public constant media type for {@code application/x-www-form-urlencoded}.
	 *  */
	public final static MediaType APPLICATION_FORM_URLENCODED;

	/**
	 * A String equivalent of {@link MediaType#APPLICATION_FORM_URLENCODED}.
	 */
	public final static String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";

	/**
	 * Public constant media type for {@code application/json}.
	 * @see #APPLICATION_JSON_UTF8
	 */
	public final static MediaType APPLICATION_JSON;

	/**
	 * A String equivalent of {@link MediaType#APPLICATION_JSON}.
	 * @see #APPLICATION_JSON_UTF8_VALUE
	 */
	public final static String APPLICATION_JSONP_VALUE = "application/jsonp";
	public final static String APPLICATION_JSON_VALUE = "application/json";
	public final static MediaType APPLICATION_JSONP;
	/**
	 * Public constant media type for {@code application/json;charset=UTF-8}.
	 */
	public final static MediaType APPLICATION_JSON_UTF8;

	/**
	 * A String equivalent of {@link MediaType#APPLICATION_JSON_UTF8}.
	 */
	public final static String APPLICATION_JSON_UTF8_VALUE = APPLICATION_JSON_VALUE + ";charset=UTF-8";

	/**
	 * Public constant media type for {@code application/octet-stream}.
	 *  */
	public final static MediaType APPLICATION_OCTET_STREAM;

	/**
	 * A String equivalent of {@link MediaType#APPLICATION_OCTET_STREAM}.
	 */
	public final static String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";

	/**
	 * Public constant media type for {@code application/xhtml+xml}.
	 *  */
	public final static MediaType APPLICATION_XHTML_XML;

	/**
	 * A String equivalent of {@link MediaType#APPLICATION_XHTML_XML}.
	 */
	public final static String APPLICATION_XHTML_XML_VALUE = "application/xhtml+xml";

	/**
	 * Public constant media type for {@code application/xml}.
	 */
	public final static MediaType APPLICATION_XML;

	/**
	 * A String equivalent of {@link MediaType#APPLICATION_XML}.
	 */
	public final static String APPLICATION_XML_VALUE = "application/xml";

	/**
	 * Public constant media type for {@code image/gif}.
	 */
	public final static MediaType IMAGE_GIF;

	/**
	 * A String equivalent of {@link MediaType#IMAGE_GIF}.
	 */
	public final static String IMAGE_GIF_VALUE = "image/gif";

	/**
	 * Public constant media type for {@code image/jpeg}.
	 */
	public final static MediaType IMAGE_JPEG;

	/**
	 * A String equivalent of {@link MediaType#IMAGE_JPEG}.
	 */
	public final static String IMAGE_JPEG_VALUE = "image/jpeg";

	/**
	 * Public constant media type for {@code image/png}.
	 */
	public final static MediaType IMAGE_PNG;

	/**
	 * A String equivalent of {@link MediaType#IMAGE_PNG}.
	 */
	public final static String IMAGE_PNG_VALUE = "image/png";

	/**
	 * Public constant media type for {@code multipart/form-data}.
	 *  */
	public final static MediaType MULTIPART_FORM_DATA;

	/**
	 * A String equivalent of {@link MediaType#MULTIPART_FORM_DATA}.
	 */
	public final static String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";

	/**
	 * Public constant media type for {@code text/html}.
	 *  */
	public final static MediaType TEXT_HTML;

	/**
	 * A String equivalent of {@link MediaType#TEXT_HTML}.
	 */
	public final static String TEXT_HTML_VALUE = "text/html";

	/**
	 * Public constant media type for {@code text/plain}.
	 *  */
	public final static MediaType TEXT_PLAIN;

	/**
	 * A String equivalent of {@link MediaType#TEXT_PLAIN}.
	 */
	public final static String TEXT_PLAIN_VALUE = "text/plain";

	/**
	 * Public constant media type for {@code text/xml}.
	 *  */
	public final static MediaType TEXT_XML;

	/**
	 * A String equivalent of {@link MediaType#TEXT_XML}.
	 */
	public final static String TEXT_XML_VALUE = "text/xml";


	private static final String PARAM_QUALITY_FACTOR = "q";
    /**
     * Public constant media type for {@code text/event-stream}.
     * @since 4.3.6
     * @see <a href="https://html.spec.whatwg.org/multipage/server-sent-events.html">Server-Sent Events</a>
     */
    public static final MediaType TEXT_EVENT_STREAM;

    /**
     * A String equivalent of {@link MediaType#TEXT_EVENT_STREAM}.
     * @since 4.3.6
     */
    public static final String TEXT_EVENT_STREAM_VALUE = "text/event-stream";

    /**
     * Public constant media type for {@code text/markdown}.
     * @since 4.3
     */
    public static final MediaType TEXT_MARKDOWN;

    /**
     * A String equivalent of {@link MediaType#TEXT_MARKDOWN}.
     * @since 4.3
     */
    public static final String TEXT_MARKDOWN_VALUE = "text/markdown";
	static {
		ALL = valueOf(ALL_VALUE);
		APPLICATION_ATOM_XML = valueOf(APPLICATION_ATOM_XML_VALUE);
		APPLICATION_FORM_URLENCODED = valueOf(APPLICATION_FORM_URLENCODED_VALUE);
		APPLICATION_JSON = valueOf(APPLICATION_JSON_VALUE);
		APPLICATION_JSONP = new MediaType("application","jsonp");
		APPLICATION_JSON_UTF8 = valueOf(APPLICATION_JSON_UTF8_VALUE);
		APPLICATION_OCTET_STREAM = valueOf(APPLICATION_OCTET_STREAM_VALUE);
		APPLICATION_XHTML_XML = valueOf(APPLICATION_XHTML_XML_VALUE);
		APPLICATION_XML = valueOf(APPLICATION_XML_VALUE);
		IMAGE_GIF = valueOf(IMAGE_GIF_VALUE);
		IMAGE_JPEG = valueOf(IMAGE_JPEG_VALUE);
		IMAGE_PNG = valueOf(IMAGE_PNG_VALUE);
		MULTIPART_FORM_DATA = valueOf(MULTIPART_FORM_DATA_VALUE);
		TEXT_HTML = valueOf(TEXT_HTML_VALUE);
		TEXT_PLAIN = valueOf(TEXT_PLAIN_VALUE);
		TEXT_XML = valueOf(TEXT_XML_VALUE);
        TEXT_EVENT_STREAM = new MediaType("text", "event-stream");

        TEXT_MARKDOWN = new MediaType("text", "markdown");
	}


	/**
	 * Create a new {@code MediaType} for the given primary type.
	 * <p>The {@linkplain #getSubtype() subtype} is set to "&#42;", parameters empty.
	 * @param type the primary type
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public MediaType(String type) {
		super(type);
	}

	/**
	 * Create a new {@code MediaType} for the given primary type and subtype.
	 * <p>The parameters are empty.
	 * @param type the primary type
	 * @param subtype the subtype
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public MediaType(String type, String subtype) {
		super(type, subtype, Collections.<String, String>emptyMap());
	}

	/**
	 * Create a new {@code MediaType} for the given type, subtype, and character set.
	 * @param type the primary type
	 * @param subtype the subtype
	 * @param charset the character set
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public MediaType(String type, String subtype, Charset charset) {
		super(type, subtype, charset);
	}

	/**
	 * Create a new {@code MediaType} for the given type, subtype, and quality value.
	 * @param type the primary type
	 * @param subtype the subtype
	 * @param qualityValue the quality value
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public MediaType(String type, String subtype, double qualityValue) {
		this(type, subtype, Collections.singletonMap(PARAM_QUALITY_FACTOR, Double.toString(qualityValue)));
	}

	/**
	 * Copy-constructor that copies the type and subtype of the given {@code MediaType},
	 * and allows for different parameter.
	 * @param other the other media type
	 * @param parameters the parameters, may be {@code null}
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public MediaType(MediaType other, Map<String, String> parameters) {
		super(other.getType(), other.getSubtype(), parameters);
	}

	/**
	 * Create a new {@code MediaType} for the given type, subtype, and parameters.
	 * @param type the primary type
	 * @param subtype the subtype
	 * @param parameters the parameters, may be {@code null}
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public MediaType(String type, String subtype, Map<String, String> parameters) {
		super(type, subtype, parameters);
	}


	@Override
	protected void checkParameters(String attribute, String value) {
		super.checkParameters(attribute, value);
		if (PARAM_QUALITY_FACTOR.equals(attribute)) {
			value = unquote(value);
			double d = Double.parseDouble(value);
			Assert.isTrue(d >= 0D && d <= 1D,
					"Invalid quality value \"" + value + "\": should be between 0.0 and 1.0");
		}
	}

	/**
	 * Return the quality value, as indicated by a {@code q} parameter, if any.
	 * Defaults to {@code 1.0}.
	 * @return the quality factory
	 */
	public double getQualityValue() {
		String qualityFactory = getParameter(PARAM_QUALITY_FACTOR);
		return (qualityFactory != null ? Double.parseDouble(unquote(qualityFactory)) : 1D);
	}

	/**
	 * Indicate whether this {@code MediaType} includes the given media type.
	 * <p>For instance, {@code text/*} includes {@code text/plain} and {@code text/html}, and {@code application/*+xml}
	 * includes {@code application/soap+xml}, etc. This method is <b>not</b> symmetric.
	 * @param other the reference media type with which to compare
	 * @return {@code true} if this media type includes the given media type; {@code false} otherwise
	 */
	public boolean includes(MediaType other) {
		return super.includes(other);
	}

	/**
	 * Indicate whether this {@code MediaType} is compatible with the given media type.
	 * <p>For instance, {@code text/*} is compatible with {@code text/plain}, {@code text/html}, and vice versa.
	 * In effect, this method is similar to {@link #includes(MediaType)}, except that it <b>is</b> symmetric.
	 * @param other the reference media type with which to compare
	 * @return {@code true} if this media type is compatible with the given media type; {@code false} otherwise
	 */
	public boolean isCompatibleWith(MediaType other) {
		return super.isCompatibleWith(other);
	}

	/**
	 * Return a replica of this instance with the quality value of the given MediaType.
	 * @return the same instance if the given MediaType doesn't have a quality value, or a new one otherwise
	 */
	public MediaType copyQualityValue(MediaType mediaType) {
		if (!mediaType.getParameters().containsKey(PARAM_QUALITY_FACTOR)) {
			return this;
		}
		Map<String, String> params = new LinkedHashMap<String, String>(getParameters());
		params.put(PARAM_QUALITY_FACTOR, mediaType.getParameters().get(PARAM_QUALITY_FACTOR));
		return new MediaType(this, params);
	}

	/**
	 * Return a replica of this instance with its quality value removed.
	 * @return the same instance if the media type doesn't contain a quality value, or a new one otherwise
	 */
	public MediaType removeQualityValue() {
		if (!getParameters().containsKey(PARAM_QUALITY_FACTOR)) {
			return this;
		}
		Map<String, String> params = new LinkedHashMap<String, String>(getParameters());
		params.remove(PARAM_QUALITY_FACTOR);
		return new MediaType(this, params);
	}


	/**
	 * Parse the given String value into a {@code MediaType} object,
	 * with this method name following the 'valueOf' naming convention
	 * @see #parseMediaType(String)
	 */
	public static MediaType valueOf(String value) {
		return parseMediaType(value);
	}

	/**
	 * Parse the given String into a single {@code MediaType}.
	 * @param mediaType the string to parse
	 * @return the media type
	 * @throws InvalidMediaTypeException if the string cannot be parsed
	 */
	public static MediaType parseMediaType(String mediaType) {
		MimeType type;
		try {
			type = MimeTypeUtils.parseMimeType(mediaType);
		}
		catch (InvalidMimeTypeException ex) {
			throw new InvalidMediaTypeException(ex);
		}
		try {
			return new MediaType(type.getType(), type.getSubtype(), type.getParameters());
		}
		catch (IllegalArgumentException ex) {
			throw new InvalidMediaTypeException(mediaType, ex.getMessage());
		}
	}


	/**
	 * Parse the given, comma-separated string into a list of {@code MediaType} objects.
	 * <p>This method can be used to parse an Accept or Content-Type header.
	 * @param mediaTypes the string to parse
	 * @return the list of media types
	 * @throws IllegalArgumentException if the string cannot be parsed
	 */
	public static List<MediaType> parseMediaTypes(String mediaTypes) {
		if (!BaseSimpleStringUtil.hasLength(mediaTypes)) {
			return Collections.emptyList();
		}
		String[] tokens = mediaTypes.split(",\\s*");
		List<MediaType> result = new ArrayList<MediaType>(tokens.length);
		for (String token : tokens) {
			result.add(parseMediaType(token));
		}
		return result;
	}

	/**
	 * Return a string representation of the given list of {@code MediaType} objects.
	 * <p>This method can be used to for an {@code Accept} or {@code Content-Type} header.
	 * @param mediaTypes the media types to create a string representation for
	 * @return the string representation
	 */
	public static String toString(Collection<MediaType> mediaTypes) {
		return MimeTypeUtils.toString(mediaTypes);
	}

	/**
	 * Sorts the given list of {@code MediaType} objects by specificity.
	 * <p>Given two media types:
	 * <ol>
	 * <li>if either media type has a {@linkplain #isWildcardType() wildcard type}, then the media type without the
	 * wildcard is ordered before the other.</li>
	 * <li>if the two media types have different {@linkplain #getType() types}, then they are considered equal and
	 * remain their current order.</li>
	 * <li>if either media type has a {@linkplain #isWildcardSubtype() wildcard subtype}, then the media type without
	 * the wildcard is sorted before the other.</li>
	 * <li>if the two media types have different {@linkplain #getSubtype() subtypes}, then they are considered equal
	 * and remain their current order.</li>
	 * <li>if the two media types have different {@linkplain #getQualityValue() quality value}, then the media type
	 * with the highest quality value is ordered before the other.</li>
	 * <li>if the two media types have a different amount of {@linkplain #getParameter(String) parameters}, then the
	 * media type with the most parameters is ordered before the other.</li>
	 * </ol>
	 * <p>For example:
	 * <blockquote>audio/basic &lt; audio/* &lt; *&#047;*</blockquote>
	 * <blockquote>audio/* &lt; audio/*;q=0.7; audio/*;q=0.3</blockquote>
	 * <blockquote>audio/basic;level=1 &lt; audio/basic</blockquote>
	 * <blockquote>audio/basic == text/html</blockquote>
	 * <blockquote>audio/basic == audio/wave</blockquote>
	 * @param mediaTypes the list of media types to be sorted
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.3.2">HTTP 1.1: Semantics
	 * and Content, section 5.3.2</a>
	 */
	public static void sortBySpecificity(List<MediaType> mediaTypes) {
		Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
		if (mediaTypes.size() > 1) {
			Collections.sort(mediaTypes, SPECIFICITY_COMPARATOR);
		}
	}

	/**
	 * Sorts the given list of {@code MediaType} objects by quality value.
	 * <p>Given two media types:
	 * <ol>
	 * <li>if the two media types have different {@linkplain #getQualityValue() quality value}, then the media type
	 * with the highest quality value is ordered before the other.</li>
	 * <li>if either media type has a {@linkplain #isWildcardType() wildcard type}, then the media type without the
	 * wildcard is ordered before the other.</li>
	 * <li>if the two media types have different {@linkplain #getType() types}, then they are considered equal and
	 * remain their current order.</li>
	 * <li>if either media type has a {@linkplain #isWildcardSubtype() wildcard subtype}, then the media type without
	 * the wildcard is sorted before the other.</li>
	 * <li>if the two media types have different {@linkplain #getSubtype() subtypes}, then they are considered equal
	 * and remain their current order.</li>
	 * <li>if the two media types have a different amount of {@linkplain #getParameter(String) parameters}, then the
	 * media type with the most parameters is ordered before the other.</li>
	 * </ol>
	 * @param mediaTypes the list of media types to be sorted
	 * @see #getQualityValue()
	 */
	public static void sortByQualityValue(List<MediaType> mediaTypes) {
		Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
		if (mediaTypes.size() > 1) {
			Collections.sort(mediaTypes, QUALITY_VALUE_COMPARATOR);
		}
	}

	/**
	 * Sorts the given list of {@code MediaType} objects by specificity as the
	 * primary criteria and quality value the secondary.
	 * @see MediaType#sortBySpecificity(List)
	 * @see MediaType#sortByQualityValue(List)
	 */
	public static void sortBySpecificityAndQuality(List<MediaType> mediaTypes) {
		Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
		if (mediaTypes.size() > 1) {
			Collections.sort(mediaTypes, new CompoundComparator<MediaType>(
					MediaType.SPECIFICITY_COMPARATOR, MediaType.QUALITY_VALUE_COMPARATOR));
		}
	}


	/**
	 * Comparator used by {@link #sortByQualityValue(List)}.
	 */
	public static final Comparator<MediaType> QUALITY_VALUE_COMPARATOR = new Comparator<MediaType>() {

		@Override
		public int compare(MediaType mediaType1, MediaType mediaType2) {
			double quality1 = mediaType1.getQualityValue();
			double quality2 = mediaType2.getQualityValue();
			int qualityComparison = Double.compare(quality2, quality1);
			if (qualityComparison != 0) {
				return qualityComparison;  // audio/*;q=0.7 < audio/*;q=0.3
			}
			else if (mediaType1.isWildcardType() && !mediaType2.isWildcardType()) { // */* < audio/*
				return 1;
			}
			else if (mediaType2.isWildcardType() && !mediaType1.isWildcardType()) { // audio/* > */*
				return -1;
			}
			else if (!mediaType1.getType().equals(mediaType2.getType())) { // audio/basic == text/html
				return 0;
			}
			else { // mediaType1.getType().equals(mediaType2.getType())
				if (mediaType1.isWildcardSubtype() && !mediaType2.isWildcardSubtype()) { // audio/* < audio/basic
					return 1;
				}
				else if (mediaType2.isWildcardSubtype() && !mediaType1.isWildcardSubtype()) { // audio/basic > audio/*
					return -1;
				}
				else if (!mediaType1.getSubtype().equals(mediaType2.getSubtype())) { // audio/basic == audio/wave
					return 0;
				}
				else {
					int paramsSize1 = mediaType1.getParameters().size();
					int paramsSize2 = mediaType2.getParameters().size();
					return (paramsSize2 < paramsSize1 ? -1 : (paramsSize2 == paramsSize1 ? 0 : 1)); // audio/basic;level=1 < audio/basic
				}
			}
		}
	};


	/**
	 * Comparator used by {@link #sortBySpecificity(List)}.
	 */
	public static final Comparator<MediaType> SPECIFICITY_COMPARATOR = new SpecificityComparator<MediaType>() {

		@Override
		protected int compareParameters(MediaType mediaType1, MediaType mediaType2) {
			double quality1 = mediaType1.getQualityValue();
			double quality2 = mediaType2.getQualityValue();
			int qualityComparison = Double.compare(quality2, quality1);
			if (qualityComparison != 0) {
				return qualityComparison;  // audio/*;q=0.7 < audio/*;q=0.3
			}
			return super.compareParameters(mediaType1, mediaType2);
		}
	};
	
	public static MediaType convertMediaType(String type,String charset)
	{
		MediaType temp = null;		 
		
//		String type =  datatype();
//		String charset =  charset();
		if(type == null )
		{
			if(charset != null)
			{
				temp = new MediaType("text","html",Charset.forName(charset));
			}
			else
			{
				temp = new MediaType("text","html",Charset.forName("UTF-8"));
			}
		}
		else if(type.equals("json"))
		{
			if(charset != null)
			{
				temp = new MediaType("application","json",Charset.forName(charset));
			}
			else
				temp = new MediaType("application","json",Charset.forName("UTF-8"));
		}
		else if(type.equals("jsonp"))
		{
			if(charset != null)
			{
				temp = new MediaType("application","jsonp",Charset.forName(charset));
			}
			else
				temp = new MediaType("application","jsonp",Charset.forName("UTF-8"));
		}
		
		else if(type.equals("xml"))
		{
			if(charset != null)
			{
				temp = new MediaType("application","xml",Charset.forName(charset));
			}
			else
				temp = new MediaType("application","xml",Charset.forName("UTF-8"));
		}
		else if(type.equals("javascript"))
		{
			if(charset != null)
			{
				temp = new MediaType("application","javascript",Charset.forName(charset));
			}
			else
				temp = new MediaType("application","javascript",Charset.forName("UTF-8"));
		}
		else if(type.equals("String") || type.equals("text")  ||type.equals("html")||type.equals("htm")  )
		{
			if(charset != null)
			{
				temp = new MediaType("text","html",Charset.forName(charset));
			}
			else
			{
				temp = new MediaType("text","html",Charset.forName("UTF-8"));
			}
		}
			//javascript
		return temp;
	}

}
