package org.frameworkset.web.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.frameworkset.http.HttpRequest;
import org.frameworkset.util.Assert;
import org.frameworkset.util.LinkedMultiValueMap;
import org.frameworkset.util.MultiValueMap;
import org.frameworkset.util.ObjectUtils;
import org.frameworkset.web.util.HierarchicalUriComponents.PathComponent;

import com.frameworkset.util.StringUtil;

public class UriComponentsBuilder  implements Cloneable {

	private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");

	private static final String SCHEME_PATTERN = "([^:/?#]+):";

	private static final String HTTP_PATTERN = "(?i)(http|https):";

	private static final String USERINFO_PATTERN = "([^@\\[/?#]*)";

	private static final String HOST_IPV4_PATTERN = "[^\\[/?#:]*";

	private static final String HOST_IPV6_PATTERN = "\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]";

	private static final String HOST_PATTERN = "(" + HOST_IPV6_PATTERN + "|" + HOST_IPV4_PATTERN + ")";

	private static final String PORT_PATTERN = "(\\d*(?:\\{[^/]+?\\})?)";

	private static final String PATH_PATTERN = "([^?#]*)";

	private static final String QUERY_PATTERN = "([^#]*)";

	private static final String LAST_PATTERN = "(.*)";

	// Regex patterns that matches URIs. See RFC 3986, appendix B
	private static final Pattern URI_PATTERN = Pattern.compile(
			"^(" + SCHEME_PATTERN + ")?" + "(//(" + USERINFO_PATTERN + "@)?" + HOST_PATTERN + "(:" + PORT_PATTERN +
					")?" + ")?" + PATH_PATTERN + "(\\?" + QUERY_PATTERN + ")?" + "(#" + LAST_PATTERN + ")?");

	private static final Pattern HTTP_URL_PATTERN = Pattern.compile(
			"^" + HTTP_PATTERN + "(//(" + USERINFO_PATTERN + "@)?" + HOST_PATTERN + "(:" + PORT_PATTERN + ")?" + ")?" +
					PATH_PATTERN + "(\\?" + LAST_PATTERN + ")?");

	private static final Pattern FORWARDED_HOST_PATTERN = Pattern.compile("host=\"?([^;,\"]+)\"?");

	private static final Pattern FORWARDED_PROTO_PATTERN = Pattern.compile("proto=\"?([^;,\"]+)\"?");


	private String scheme;

	private String ssp;

	private String userInfo;

	private String host;

	private String port;

	private CompositePathComponentBuilder pathBuilder;

	private final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();

	private String fragment;


	/**
	 * Default constructor. Protected to prevent direct instantiation.
	 * @see #newInstance()
	 * @see #fromPath(String)
	 * @see #fromUri(URI)
	 */
	protected UriComponentsBuilder() {
		this.pathBuilder = new CompositePathComponentBuilder();
	}

	/**
	 * Create a deep copy of the given UriComponentsBuilder.
	 * @param other the other builder to copy from
	 */
	protected UriComponentsBuilder(UriComponentsBuilder other) {
		this.scheme = other.scheme;
		this.ssp = other.ssp;
		this.userInfo = other.userInfo;
		this.host = other.host;
		this.port = other.port;
		this.pathBuilder = (CompositePathComponentBuilder) other.pathBuilder.clone();
		this.queryParams.putAll(other.queryParams);
		this.fragment = other.fragment;
	}


	// Factory methods

	/**
	 * Create a new, empty builder.
	 * @return the new {@code UriComponentsBuilder}
	 */
	public static UriComponentsBuilder newInstance() {
		return new UriComponentsBuilder();
	}

	/**
	 * Create a builder that is initialized with the given path.
	 * @param path the path to initialize with
	 * @return the new {@code UriComponentsBuilder}
	 */
	public static UriComponentsBuilder fromPath(String path) {
		UriComponentsBuilder builder = new UriComponentsBuilder();
		builder.path(path);
		return builder;
	}

	/**
	 * Create a builder that is initialized with the given {@code URI}.
	 * @param uri the URI to initialize with
	 * @return the new {@code UriComponentsBuilder}
	 */
	public static UriComponentsBuilder fromUri(URI uri) {
		UriComponentsBuilder builder = new UriComponentsBuilder();
		builder.uri(uri);
		return builder;
	}

	/**
	 * Create a builder that is initialized with the given URI string.
	 * <p><strong>Note:</strong> The presence of reserved characters can prevent
	 * correct parsing of the URI string. For example if a query parameter
	 * contains {@code '='} or {@code '&'} characters, the query string cannot
	 * be parsed unambiguously. Such values should be substituted for URI
	 * variables to enable correct parsing:
	 * <pre class="code">
	 * String uriString = &quot;/hotels/42?filter={value}&quot;;
	 * UriComponentsBuilder.fromUriString(uriString).buildAndExpand(&quot;hot&amp;cold&quot;);
	 * </pre>
	 * @param uri the URI string to initialize with
	 * @return the new {@code UriComponentsBuilder}
	 */
	public static UriComponentsBuilder fromUriString(String uri) {
		Assert.notNull(uri, "URI must not be null");
		Matcher matcher = URI_PATTERN.matcher(uri);
		if (matcher.matches()) {
			UriComponentsBuilder builder = new UriComponentsBuilder();
			String scheme = matcher.group(2);
			String userInfo = matcher.group(5);
			String host = matcher.group(6);
			String port = matcher.group(8);
			String path = matcher.group(9);
			String query = matcher.group(11);
			String fragment = matcher.group(13);
			boolean opaque = false;
			if (StringUtil.hasLength(scheme)) {
				String rest = uri.substring(scheme.length());
				if (!rest.startsWith(":/")) {
					opaque = true;
				}
			}
			builder.scheme(scheme);
			if (opaque) {
				String ssp = uri.substring(scheme.length()).substring(1);
				if (StringUtil.hasLength(fragment)) {
					ssp = ssp.substring(0, ssp.length() - (fragment.length() + 1));
				}
				builder.schemeSpecificPart(ssp);
			}
			else {
				builder.userInfo(userInfo);
				builder.host(host);
				if (StringUtil.hasLength(port)) {
					builder.port(port);
				}
				builder.path(path);
				builder.query(query);
			}
			if (StringUtil.hasText(fragment)) {
				builder.fragment(fragment);
			}
			return builder;
		}
		else {
			throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
		}
	}

	/**
	 * Create a URI components builder from the given HTTP URL String.
	 * <p><strong>Note:</strong> The presence of reserved characters can prevent
	 * correct parsing of the URI string. For example if a query parameter
	 * contains {@code '='} or {@code '&'} characters, the query string cannot
	 * be parsed unambiguously. Such values should be substituted for URI
	 * variables to enable correct parsing:
	 * <pre class="code">
	 * String uriString = &quot;/hotels/42?filter={value}&quot;;
	 * UriComponentsBuilder.fromUriString(uriString).buildAndExpand(&quot;hot&amp;cold&quot;);
	 * </pre>
	 * @param httpUrl the source URI
	 * @return the URI components of the URI
	 */
	public static UriComponentsBuilder fromHttpUrl(String httpUrl) {
		Assert.notNull(httpUrl, "HTTP URL must not be null");
		Matcher matcher = HTTP_URL_PATTERN.matcher(httpUrl);
		if (matcher.matches()) {
			UriComponentsBuilder builder = new UriComponentsBuilder();
			String scheme = matcher.group(1);
			builder.scheme(scheme != null ? scheme.toLowerCase() : null);
			builder.userInfo(matcher.group(4));
			String host = matcher.group(5);
			if (StringUtil.hasLength(scheme) && !StringUtil.hasLength(host)) {
				throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
			}
			builder.host(host);
			String port = matcher.group(7);
			if (StringUtil.hasLength(port)) {
				builder.port(port);
			}
			builder.path(matcher.group(8));
			builder.query(matcher.group(10));
			return builder;
		}
		else {
			throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
		}
	}

	/**
	 * Create a new {@code UriComponents} object from the URI associated with
	 * the given HttpRequest while also overlaying with values from the headers
	 * "Forwarded" (<a href="http://tools.ietf.org/html/rfc7239">RFC 7239</a>, or
	 * "X-Forwarded-Host", "X-Forwarded-Port", and "X-Forwarded-Proto" if "Forwarded" is
	 * not found.
	 * @param request the source request
	 * @return the URI components of the URI
	 * @since 4.1.5
	 */
	public static UriComponentsBuilder fromHttpRequest(HttpRequest request) {
		URI uri = request.getURI();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);

		String scheme = uri.getScheme();
		String host = uri.getHost();
		int port = uri.getPort();

		String forwardedHeader = request.getHeaders().getFirst("Forwarded");
		if (StringUtil.hasText(forwardedHeader)) {
			String forwardedToUse = StringUtil.commaDelimitedListToStringArray(forwardedHeader)[0];
			Matcher m = FORWARDED_HOST_PATTERN.matcher(forwardedToUse);
			if (m.find()) {
				host = m.group(1).trim();
			}
			m = FORWARDED_PROTO_PATTERN.matcher(forwardedToUse);
			if (m.find()) {
				scheme = m.group(1).trim();
			}
		}
		else {
			String hostHeader = request.getHeaders().getFirst("X-Forwarded-Host");
			if (StringUtil.hasText(hostHeader)) {
				String[] hosts = StringUtil.commaDelimitedListToStringArray(hostHeader);
				String hostToUse = hosts[0];
				if (hostToUse.contains(":")) {
					String[] hostAndPort = StringUtil.split(hostToUse, ":");
					host = hostAndPort[0];
					port = Integer.parseInt(hostAndPort[1]);
				}
				else {
					host = hostToUse;
					port = -1;
				}
			}

			String portHeader = request.getHeaders().getFirst("X-Forwarded-Port");
			if (StringUtil.hasText(portHeader)) {
				String[] ports = StringUtil.commaDelimitedListToStringArray(portHeader);
				port = Integer.parseInt(ports[0]);
			}

			String protocolHeader = request.getHeaders().getFirst("X-Forwarded-Proto");
			if (StringUtil.hasText(protocolHeader)) {
				String[] protocols = StringUtil.commaDelimitedListToStringArray(protocolHeader);
				scheme = protocols[0];
			}
		}

		builder.scheme(scheme);
		builder.host(host);
		builder.port(null);
		if (scheme.equals("http") && port != 80 || scheme.equals("https") && port != 443) {
			builder.port(port);
		}
		return builder;
	}


	/**
	 * Create an instance by parsing the "Origin" header of an HTTP request.
	 * @see <a href="https://tools.ietf.org/html/rfc6454">RFC 6454</a>
	 */
	public static UriComponentsBuilder fromOriginHeader(String origin) {
		Matcher matcher = URI_PATTERN.matcher(origin);
		if (matcher.matches()) {
			UriComponentsBuilder builder = new UriComponentsBuilder();
			String scheme = matcher.group(2);
			String host = matcher.group(6);
			String port = matcher.group(8);
			if (StringUtil.hasLength(scheme)) {
				builder.scheme(scheme);
			}
			builder.host(host);
			if (StringUtil.hasLength(port)) {
				builder.port(port);
			}
			return builder;
		}
		else {
			throw new IllegalArgumentException("[" + origin + "] is not a valid \"Origin\" header value");
		}
	}


	// build methods

	/**
	 * Build a {@code UriComponents} instance from the various components contained in this builder.
	 * @return the URI components
	 */
	public UriComponents build() {
		return build(false);
	}

	/**
	 * Build a {@code UriComponents} instance from the various components
	 * contained in this builder.
	 * @param encoded whether all the components set in this builder are
	 * encoded ({@code true}) or not ({@code false})
	 * @return the URI components
	 */
	public UriComponents build(boolean encoded) {
		if (this.ssp != null) {
			return new OpaqueUriComponents(this.scheme, this.ssp, this.fragment);
		}
		else {
			return new HierarchicalUriComponents(this.scheme, this.userInfo, this.host, this.port,
					this.pathBuilder.build(), this.queryParams, this.fragment, encoded, true);
		}
	}

	/**
	 * Build a {@code UriComponents} instance and replaces URI template variables
	 * with the values from a map. This is a shortcut method which combines
	 * calls to {@link #build()} and then {@link UriComponents#expand(Map)}.
	 * @param uriVariables the map of URI variables
	 * @return the URI components with expanded values
	 */
	public UriComponents buildAndExpand(Map<String, ?> uriVariables) {
		return build(false).expand(uriVariables);
	}

	/**
	 * Build a {@code UriComponents} instance and replaces URI template variables
	 * with the values from an array. This is a shortcut method which combines
	 * calls to {@link #build()} and then {@link UriComponents#expand(Object...)}.
	 * @param uriVariableValues URI variable values
	 * @return the URI components with expanded values
	 */
	public UriComponents buildAndExpand(Object... uriVariableValues) {
		return build(false).expand(uriVariableValues);
	}

	/**
	 * Build a URI String. This is a shortcut method which combines calls
	 * to {@link #build()}, then {@link UriComponents#encode()} and finally
	 * {@link UriComponents#toUriString()}.
	 * @since 4.1
	 * @see UriComponents#toUriString()
	 */
	public String toUriString() {
		return build(false).encode().toUriString();
	}


	// URI components methods

	/**
	 * Initialize all components of this URI builder with the components of the given URI.
	 * @param uri the URI
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder uri(URI uri) {
		Assert.notNull(uri, "URI must not be null");
		this.scheme = uri.getScheme();
		if (uri.isOpaque()) {
			this.ssp = uri.getRawSchemeSpecificPart();
			resetHierarchicalComponents();
		}
		else {
			if (uri.getRawUserInfo() != null) {
				this.userInfo = uri.getRawUserInfo();
			}
			if (uri.getHost() != null) {
				this.host = uri.getHost();
			}
			if (uri.getPort() != -1) {
				this.port = String.valueOf(uri.getPort());
			}
			if (StringUtil.hasLength(uri.getRawPath())) {
				this.pathBuilder = new CompositePathComponentBuilder(uri.getRawPath());
			}
			if (StringUtil.hasLength(uri.getRawQuery())) {
				this.queryParams.clear();
				query(uri.getRawQuery());
			}
			resetSchemeSpecificPart();
		}
		if (uri.getRawFragment() != null) {
			this.fragment = uri.getRawFragment();
		}
		return this;
	}

	private void resetHierarchicalComponents() {
		this.userInfo = null;
		this.host = null;
		this.port = null;
		this.pathBuilder = new CompositePathComponentBuilder();
		this.queryParams.clear();
	}

	private void resetSchemeSpecificPart() {
		this.ssp = null;
	}

	/**
	 * Set the URI scheme. The given scheme may contain URI template variables,
	 * and may also be {@code null} to clear the scheme of this builder.
	 * @param scheme the URI scheme
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder scheme(String scheme) {
		this.scheme = scheme;
		return this;
	}

	/**
	 * Set all components of this URI builder from the given {@link UriComponents}.
	 * @param uriComponents the UriComponents instance
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder uriComponents(UriComponents uriComponents) {
		Assert.notNull(uriComponents, "UriComponents must not be null");
		uriComponents.copyToUriComponentsBuilder(this);
		return this;
	}

	/**
	 * Set the URI scheme-specific-part. When invoked, this method overwrites
	 * {@linkplain #userInfo(String) user-info}, {@linkplain #host(String) host},
	 * {@linkplain #port(int) port}, {@linkplain #path(String) path}, and
	 * {@link #query(String) query}.
	 * @param ssp the URI scheme-specific-part, may contain URI template parameters
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder schemeSpecificPart(String ssp) {
		this.ssp = ssp;
		resetHierarchicalComponents();
		return this;
	}

	/**
	 * Set the URI user info. The given user info may contain URI template variables,
	 * and may also be {@code null} to clear the user info of this builder.
	 * @param userInfo the URI user info
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder userInfo(String userInfo) {
		this.userInfo = userInfo;
		resetSchemeSpecificPart();
		return this;
	}

	/**
	 * Set the URI host. The given host may contain URI template variables,
	 * and may also be {@code null} to clear the host of this builder.
	 * @param host the URI host
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder host(String host) {
		this.host = host;
		resetSchemeSpecificPart();
		return this;
	}

	/**
	 * Set the URI port. Passing {@code -1} will clear the port of this builder.
	 * @param port the URI port
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder port(int port) {
		Assert.isTrue(port >= -1, "Port must be >= -1");
		this.port = String.valueOf(port);
		resetSchemeSpecificPart();
		return this;
	}

	/**
	 * Set the URI port. Use this method only when the port needs to be
	 * parameterized with a URI variable. Otherwise use {@link #port(int)}.
	 * Passing {@code null} will clear the port of this builder.
	 * @param port the URI port
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder port(String port) {
		this.port = port;
		resetSchemeSpecificPart();
		return this;
	}

	/**
	 * Append the given path to the existing path of this builder.
	 * The given path may contain URI template variables.
	 * @param path the URI path
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder path(String path) {
		this.pathBuilder.addPath(path);
		resetSchemeSpecificPart();
		return this;
	}

	/**
	 * Set the path of this builder overriding all existing path and path segment values.
	 * @param path the URI path; a {@code null} value results in an empty path.
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder replacePath(String path) {
		this.pathBuilder = new CompositePathComponentBuilder(path);
		resetSchemeSpecificPart();
		return this;
	}

	/**
	 * Append path segments to the existing path. Each path segment may contain
	 * URI template variables and should not contain any slashes.
	 * Use {@code path("/")} subsequently to ensure a trailing slash.
	 * @param pathSegments the URI path segments
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder pathSegment(String... pathSegments) throws IllegalArgumentException {
		this.pathBuilder.addPathSegments(pathSegments);
		resetSchemeSpecificPart();
		return this;
	}

	/**
	 * Append the given query to the existing query of this builder.
	 * The given query may contain URI template variables.
	 * <p><strong>Note:</strong> The presence of reserved characters can prevent
	 * correct parsing of the URI string. For example if a query parameter
	 * contains {@code '='} or {@code '&'} characters, the query string cannot
	 * be parsed unambiguously. Such values should be substituted for URI
	 * variables to enable correct parsing:
	 * <pre class="code">
	 * UriComponentsBuilder.fromUriString(&quot;/hotels/42&quot;)
	 * 	.query(&quot;filter={value}&quot;)
	 * 	.buildAndExpand(&quot;hot&amp;cold&quot;);
	 * </pre>
	 * @param query the query string
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder query(String query) {
		if (query != null) {
			Matcher matcher = QUERY_PARAM_PATTERN.matcher(query);
			while (matcher.find()) {
				String name = matcher.group(1);
				String eq = matcher.group(2);
				String value = matcher.group(3);
				queryParam(name, (value != null ? value : (StringUtil.hasLength(eq) ? "" : null)));
			}
		}
		else {
			this.queryParams.clear();
		}
		resetSchemeSpecificPart();
		return this;
	}

	/**
	 * Set the query of this builder overriding all existing query parameters.
	 * @param query the query string; a {@code null} value removes all query parameters.
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder replaceQuery(String query) {
		this.queryParams.clear();
		query(query);
		resetSchemeSpecificPart();
		return this;
	}

	/**
	 * Append the given query parameter to the existing query parameters. The
	 * given name or any of the values may contain URI template variables. If no
	 * values are given, the resulting URI will contain the query parameter name
	 * only (i.e. {@code ?foo} instead of {@code ?foo=bar}.
	 * @param name the query parameter name
	 * @param values the query parameter values
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder queryParam(String name, Object... values) {
		Assert.notNull(name, "Name must not be null");
		if (!ObjectUtils.isEmpty(values)) {
			for (Object value : values) {
				String valueAsString = (value != null ? value.toString() : null);
				this.queryParams.add(name, valueAsString);
			}
		}
		else {
			this.queryParams.add(name, null);
		}
		resetSchemeSpecificPart();
		return this;
	}

	/**
	 * Add the given query parameters.
	 * @param params the params
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder queryParams(MultiValueMap<String, String> params) {
		if (params != null) {
			this.queryParams.putAll(params);
		}
		return this;
	}

	/**
	 * Set the query parameter values overriding all existing query values for
	 * the same parameter. If no values are given, the query parameter is removed.
	 * @param name the query parameter name
	 * @param values the query parameter values
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder replaceQueryParam(String name, Object... values) {
		Assert.notNull(name, "Name must not be null");
		this.queryParams.remove(name);
		if (!ObjectUtils.isEmpty(values)) {
			queryParam(name, values);
		}
		resetSchemeSpecificPart();
		return this;
	}

	/**
	 * Set the query parameter values overriding all existing query values.
	 * @param params the query parameter name
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder replaceQueryParams(MultiValueMap<String, String> params) {
		this.queryParams.clear();
		if (params != null) {
			this.queryParams.putAll(params);
		}
		return this;
	}

	/**
	 * Set the URI fragment. The given fragment may contain URI template variables,
	 * and may also be {@code null} to clear the fragment of this builder.
	 * @param fragment the URI fragment
	 * @return this UriComponentsBuilder
	 */
	public UriComponentsBuilder fragment(String fragment) {
		if (fragment != null) {
			Assert.hasLength(fragment, "Fragment must not be empty");
			this.fragment = fragment;
		}
		else {
			this.fragment = null;
		}
		return this;
	}

	@Override
	public Object clone() {
		return new UriComponentsBuilder(this);
	}


	private interface PathComponentBuilder extends Cloneable {

		PathComponent build();

		Object clone();
	}


	private static class CompositePathComponentBuilder implements PathComponentBuilder {

		private final LinkedList<PathComponentBuilder> builders = new LinkedList<PathComponentBuilder>();

		public CompositePathComponentBuilder() {
		}

		public CompositePathComponentBuilder(String path) {
			addPath(path);
		}

		public void addPathSegments(String... pathSegments) {
			if (!ObjectUtils.isEmpty(pathSegments)) {
				PathSegmentComponentBuilder psBuilder = getLastBuilder(PathSegmentComponentBuilder.class);
				FullPathComponentBuilder fpBuilder = getLastBuilder(FullPathComponentBuilder.class);
				if (psBuilder == null) {
					psBuilder = new PathSegmentComponentBuilder();
					this.builders.add(psBuilder);
					if (fpBuilder != null) {
						fpBuilder.removeTrailingSlash();
					}
				}
				psBuilder.append(pathSegments);
			}
		}

		public void addPath(String path) {
			if (StringUtil.hasText(path)) {
				PathSegmentComponentBuilder psBuilder = getLastBuilder(PathSegmentComponentBuilder.class);
				FullPathComponentBuilder fpBuilder = getLastBuilder(FullPathComponentBuilder.class);
				if (psBuilder != null) {
					path = path.startsWith("/") ? path : "/" + path;
				}
				if (fpBuilder == null) {
					fpBuilder = new FullPathComponentBuilder();
					this.builders.add(fpBuilder);
				}
				fpBuilder.append(path);
			}
		}

		@SuppressWarnings("unchecked")
		private <T> T getLastBuilder(Class<T> builderClass) {
			if (!this.builders.isEmpty()) {
				PathComponentBuilder last = this.builders.getLast();
				if (builderClass.isInstance(last)) {
					return (T) last;
				}
			}
			return null;
		}

		@Override
		public PathComponent build() {
			int size = this.builders.size();
			List<PathComponent> components = new ArrayList<PathComponent>(size);
			for (PathComponentBuilder componentBuilder : this.builders) {
				PathComponent pathComponent = componentBuilder.build();
				if (pathComponent != null) {
					components.add(pathComponent);
				}
			}
			if (components.isEmpty()) {
				return HierarchicalUriComponents.NULL_PATH_COMPONENT;
			}
			if (components.size() == 1) {
				return components.get(0);
			}
			return new HierarchicalUriComponents.PathComponentComposite(components);
		}

		@Override
		public Object clone() {
			CompositePathComponentBuilder compositeBuilder = new CompositePathComponentBuilder();
			for (PathComponentBuilder builder : this.builders) {
				compositeBuilder.builders.add((PathComponentBuilder) builder.clone());
			}
			return compositeBuilder;
		}
	}


	private static class FullPathComponentBuilder implements PathComponentBuilder {

		private final StringBuilder path = new StringBuilder();

		public void append(String path) {
			this.path.append(path);
		}

		@Override
		public PathComponent build() {
			if (this.path.length() == 0) {
				return null;
			}
			String path = this.path.toString();
			while (true) {
				int index = path.indexOf("//");
				if (index == -1) {
					break;
				}
				path = path.substring(0, index) + path.substring(index + 1);
			}
			return new HierarchicalUriComponents.FullPathComponent(path);
		}

		public void removeTrailingSlash() {
			int index = this.path.length() - 1;
			if (this.path.charAt(index) == '/') {
				this.path.deleteCharAt(index);
			}
		}

		@Override
		public Object clone() {
			FullPathComponentBuilder builder = new FullPathComponentBuilder();
			builder.append(this.path.toString());
			return builder;
		}
	}


	private static class PathSegmentComponentBuilder implements PathComponentBuilder {

		private final List<String> pathSegments = new LinkedList<String>();

		public void append(String... pathSegments) {
			for (String pathSegment : pathSegments) {
				if (StringUtil.hasText(pathSegment)) {
					this.pathSegments.add(pathSegment);
				}
			}
		}

		@Override
		public PathComponent build() {
			return (this.pathSegments.isEmpty() ? null :
					new HierarchicalUriComponents.PathSegmentComponent(this.pathSegments));
		}

		@Override
		public Object clone() {
			PathSegmentComponentBuilder builder = new PathSegmentComponentBuilder();
			builder.pathSegments.addAll(this.pathSegments);
			return builder;
		}
	}


}
