package org.frameworkset.web.socket.inf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.frameworkset.util.Assert;
import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.LinkedCaseInsensitiveMap;

import com.frameworkset.util.StringUtil;


public class WebSocketExtension {
	private final String name;

	private final Map<String, String> parameters;


	/**
	 * Create a WebSocketExtension with the given name.
	 * @param name the name of the extension
	 */
	public WebSocketExtension(String name) {
		this(name, null);
	}

	/**
	 * Create a WebSocketExtension with the given name and parameters.
	 * @param name the name of the extension
	 * @param parameters the parameters
	 */
	public WebSocketExtension(String name, Map<String, String> parameters) {
		Assert.hasLength(name, "extension name must not be empty");
		this.name = name;
		if (!CollectionUtils.isEmpty(parameters)) {
			Map<String, String> m = new LinkedCaseInsensitiveMap<String>(parameters.size(), Locale.ENGLISH);
			m.putAll(parameters);
			this.parameters = Collections.unmodifiableMap(m);
		}
		else {
			this.parameters = Collections.emptyMap();
		}
	}

	/**
	 * @return the name of the extension
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the parameters of the extension, never {@code null}
	 */
	public Map<String, String> getParameters() {
		return this.parameters;
	}

	/**
	 * Parse the given, comma-separated string into a list of {@code WebSocketExtension} objects.
	 * <p>This method can be used to parse a "Sec-WebSocket-Extension" extensions.
	 * @param extensions the string to parse
	 * @return the list of extensions
	 * @throws IllegalArgumentException if the string cannot be parsed
	 */
	public static List<WebSocketExtension> parseExtensions(String extensions) {
		if (extensions == null || !StringUtil.hasText(extensions)) {
			return Collections.emptyList();
		}
		else {
			List<WebSocketExtension> result = new ArrayList<WebSocketExtension>();
			for(String token : extensions.split(",")) {
				result.add(parseExtension(token));
			}
			return result;
		}
	}

	private static WebSocketExtension parseExtension(String extension) {
		Assert.doesNotContain(extension, ",", "Expected a single extension value: " + extension);
		String[] parts = StringUtil.tokenizeToStringArray(extension, ";");
		String name = parts[0].trim();

		Map<String, String> parameters = null;
		if (parts.length > 1) {
			parameters = new LinkedHashMap<String, String>(parts.length - 1);
			for (int i = 1; i < parts.length; i++) {
				String parameter = parts[i];
				int eqIndex = parameter.indexOf('=');
				if (eqIndex != -1) {
					String attribute = parameter.substring(0, eqIndex);
					String value = parameter.substring(eqIndex + 1, parameter.length());
					parameters.put(attribute, value);
				}
			}
		}

		return new WebSocketExtension(name, parameters);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (getClass() != o.getClass())) {
			return false;
		}
		WebSocketExtension that = (WebSocketExtension) o;
		if (!name.equals(that.name)) {
			return false;
		}
		if (!parameters.equals(that.parameters)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + parameters.hashCode();
		return result;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.name);
		for (String param : parameters.keySet()) {
			str.append(';');
			str.append(param);
			str.append('=');
			str.append(this.parameters.get(param));
		}
		return str.toString();
	}
}
