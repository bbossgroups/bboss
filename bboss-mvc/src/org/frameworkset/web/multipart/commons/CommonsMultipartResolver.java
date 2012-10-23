package org.frameworkset.web.multipart.commons;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.frameworkset.util.Assert;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.web.multipart.DefaultMultipartHttpServletRequest;
import org.frameworkset.web.multipart.MaxUploadSizeExceededException;
import org.frameworkset.web.multipart.MultipartException;
import org.frameworkset.web.multipart.MultipartHttpServletRequest;
import org.frameworkset.web.multipart.MultipartResolver;
import org.frameworkset.web.servlet.context.ServletContextAware;
import org.frameworkset.web.servlet.context.WebApplicationContext;
import org.frameworkset.web.util.WebUtils;

public class CommonsMultipartResolver  extends CommonsFileUploadSupport
		implements MultipartResolver, ServletContextAware {


	private final boolean commonsFileUpload12Present =
			ClassUtils.hasMethod(ServletFileUpload.class, "isMultipartContent", new Class[] {HttpServletRequest.class});

	private boolean resolveLazily = false;


	/**
	 * Constructor for use as bean. Determines the servlet container's
	 * temporary directory via the ServletContext passed in as through the
	 * ServletContextAware interface (typically by a WebApplicationContext).
	 * @see #setServletContext
	 * @see ServletContextAware
	 * @see WebApplicationContext
	 */
	public CommonsMultipartResolver() {
		super();
	}

	/**
	 * Constructor for standalone usage. Determines the servlet container's
	 * temporary directory via the given ServletContext.
	 * @param servletContext the ServletContext to use
	 */
	public CommonsMultipartResolver(ServletContext servletContext) {
		this();
		setServletContext(servletContext);
	}


	/**
	 * Set whether to resolve the multipart request lazily at the time of
	 * file or parameter access.
	 * <p>Default is "false", resolving the multipart elements immediately, throwing
	 * corresponding exceptions at the time of the {@link #resolveMultipart} call.
	 * Switch this to "true" for lazy multipart parsing, throwing parse exceptions
	 * once the application attempts to obtain multipart files or parameters.
	 */
	public void setResolveLazily(boolean resolveLazily) {
		this.resolveLazily = resolveLazily;
	}

	/**
	 * Initialize the underlying <code>org.apache.commons.fileupload.servlet.ServletFileUpload</code>
	 * instance. Can be overridden to use a custom subclass, e.g. for testing purposes.
	 * @param fileItemFactory the Commons FileItemFactory to use
	 * @return the new ServletFileUpload instance
	 */
	protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
		return new ServletFileUpload(fileItemFactory);
	}

	public void setServletContext(ServletContext servletContext) {
		if (!isUploadTempDirSpecified()) {
			getFileItemFactory().setRepository(WebUtils.getTempDir(servletContext));
		}
	}


	public boolean isMultipart(HttpServletRequest request) {
		String contenttype = request.getContentType();
		if (request == null) {
			return false;
		}
		else if(contenttype != null && contenttype.equals(MultipartResolver.mimetype_application_octet_stream))
		{
			return true;
		}
		else if (commonsFileUpload12Present) {
			return ServletFileUpload.isMultipartContent(request);
		}
		else {
			return ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
		}
	}

	public MultipartHttpServletRequest resolveMultipart(final HttpServletRequest request) throws MultipartException {
		Assert.notNull(request, "Request must not be null");
		if (this.resolveLazily) {
			return new DefaultMultipartHttpServletRequest(request) {
				protected void initializeMultipart() {
					MultipartParsingResult parsingResult = parseRequest(request);
					setMultipartFiles(parsingResult.getMultipartFiles());
					setMultipartParameters(parsingResult.getMultipartParameters());
				}
			};
		}
		else {
			MultipartParsingResult parsingResult = parseRequest(request);
			return new DefaultMultipartHttpServletRequest(
					request, parsingResult.getMultipartFiles(), parsingResult.getMultipartParameters());
		}
	}

	/**
	 * Parse the given servlet request, resolving its multipart elements.
	 * @param request the request to parse
	 * @return the parsing result
	 * @throws MultipartException if multipart resolution failed.
	 */
	protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
		String[] encoding = determineEncoding(request);
		String contentType = request.getContentType();
		if(contentType != null && !contentType.equals(MultipartResolver.mimetype_application_octet_stream))
		{
			FileUpload fileUpload = prepareFileUpload(encoding[1] == null?encoding[0]:encoding[1]);
			try {
				List fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
				return parseFileItems(fileItems, encoding,request);
			}
			catch (FileUploadBase.SizeLimitExceededException ex) {
				throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
			}
			catch (FileUploadException ex) {
				throw new MultipartException("Could not parse multipart servlet request", ex);
			}
		}
		else
		{
			return parseOctetFileItems(request);
		}
	}

	/**
	 * Determine the encoding for the given request.
	 * Can be overridden in subclasses.
	 * <p>The default implementation checks the request encoding,
	 * falling back to the default encoding specified for this resolver.
	 * @param request current HTTP request
	 * @return the encoding for the request (never <code>null</code>)
	 * @see javax.servlet.ServletRequest#getCharacterEncoding
	 * @see #setDefaultEncoding
	 */
	protected String[] determineEncoding(HttpServletRequest request) {
//		String encoding = request.getCharacterEncoding();
//		if (encoding == null) {
//			encoding = getDefaultEncoding();
//		}
//		return encoding;
		String encoding = request.getCharacterEncoding();
		
		return new String[]{this.getDefaultEncoding(),encoding};
		
	}

	public void cleanupMultipart(MultipartHttpServletRequest request) {
		if (request != null) {
			try {
				cleanupFileItems(request.getFileMap().values());
			}
			catch (Throwable ex) {
				logger.warn("Failed to perform multipart cleanup for servlet request", ex);
			}
		}
	}


}
