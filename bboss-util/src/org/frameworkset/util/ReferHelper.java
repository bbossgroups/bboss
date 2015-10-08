package org.frameworkset.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.frameworkset.util.AntPathMatcher;
import org.frameworkset.util.PathMatcher;

import com.frameworkset.util.StringUtil;

public class ReferHelper {
	private static Logger logger = Logger.getLogger(ReferHelper.class);
	private String[] refererwallwhilelist;
	private boolean refererDefender = false;
	private PathMatcher pathMatcher;
	private String[] wallfilterrules;
	private String[] wallwhilelist;
	public final static String[] wallfilterrules_default = new String[] {
			"<script", "%3Cscript", "script", "<img", "%3Cimg", "alert(",
			"alert%28", "eval(", "eval%28", "style=", "style%3D", "javascript",
			"update ", "drop ", "delete ", "insert ", "create ", "select ",
			"truncate " };

	public ReferHelper() {
		pathMatcher = new AntPathMatcher();
	}

	private boolean iswhilerefer(String referer) {
		if (this.refererwallwhilelist == null
				|| this.refererwallwhilelist.length == 0)
			return false;
		for (String whilereferername : this.refererwallwhilelist) {

			if (pathMatcher.urlContain(whilereferername, referer))
				return true;
		}
		return false;
	}

	public boolean dorefer(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (refererDefender) {
			/**
			 * 跨站点请求伪造。修复任务： 拒绝恶意请求。解决方案，过滤器中
			 * 
			 */
			String referer = request.getHeader("Referer"); // REFRESH
			// if(!iswhilerefer(referer))

			if (referer != null) {
				String basePath = null;
				String basePath80 = null;
				if (!request.getContextPath().equals("/")) {
					if (request.getServerPort() != 80) {
						basePath = request.getScheme() + "://"
								+ request.getServerName() + ":"
								+ request.getServerPort()
								+ request.getContextPath() + "/";
					} else {
						basePath = request.getScheme() + "://"
								+ request.getServerName() + ":"
								+ request.getServerPort()
								+ request.getContextPath() + "/";
						basePath80 = request.getScheme() + "://"
								+ request.getServerName()
								+ request.getContextPath() + "/";
					}
				} else {
					if (request.getServerPort() != 80) {
						basePath = request.getScheme() + "://"

						+ request.getServerName() + ":"
								+ request.getServerPort()

								+ request.getContextPath();
					} else {
						basePath = request.getScheme() + "://"

						+ request.getServerName() + ":"
								+ request.getServerPort()

								+ request.getContextPath();
						basePath80 = request.getScheme() + "://"
								+ request.getServerName()
								+ request.getContextPath();
					}
				}
				if (basePath80 == null) {
					if (referer.indexOf(basePath) < 0) {
						if (this.iswhilerefer(referer)) {
							// String context = request.getContextPath();
							// if(!context.equals("/"))
							// {
							// String uri = request.getRequestURI();
							// uri =
							// uri.substring(request.getContextPath().length());
							// request.getRequestDispatcher(uri).forward(request,
							// response);
							// }
							// else
							// {
							// request.getRequestDispatcher(context).forward(request,
							// response);
							// }
							// return;
							return false;
						} else {
							sendRedirect403(request, response);
							return true;
						}
					}
				} else {
					if (referer.indexOf(basePath) < 0
							&& referer.indexOf(basePath80) < 0) {
						// String context = request.getContextPath();
						// if(!context.equals("/"))
						// {
						// String uri = request.getRequestURI();
						// uri =
						// uri.substring(request.getContextPath().length());
						// request.getRequestDispatcher(uri).forward(request,
						// response);
						// }
						// else
						// {
						// request.getRequestDispatcher(context).forward(request,
						// response);
						// }
						// return;
						if (this.iswhilerefer(referer)) {
							return false;
						} else {
							sendRedirect403(request, response);
							return true;
						}
					}
				}

			}
		}
		return false;
	}

	public void sendRedirect403(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (!response.isCommitted()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}

	public String[] getRefererwallwhilelist() {
		return refererwallwhilelist;
	}

	public void setRefererwallwhilelist(String[] refererwallwhilelist) {
		this.refererwallwhilelist = refererwallwhilelist;
		if (StringUtil.isNotEmpty(this.refererwallwhilelist)) {
			for (int i = 0; i < this.refererwallwhilelist.length; i++) {
				this.refererwallwhilelist[i] = this.refererwallwhilelist[i]
						.trim();
			}
		}
	}

	public boolean isRefererDefender() {
		return refererDefender;
	}

	public void setRefererDefender(boolean refererDefender) {
		this.refererDefender = refererDefender;
	}

	public String[] getWallfilterrules() {
		return wallfilterrules;
	}

	public void setWallfilterrules(String[] wallfilterrules) {
		this.wallfilterrules = wallfilterrules;
	}

	public String[] getWallwhilelist() {
		return wallwhilelist;
	}

	public void setWallwhilelist(String[] wallwhilelist) {
		this.wallwhilelist = wallwhilelist;
	}

	public boolean iswhilename(String name) {
		if (this.wallwhilelist == null || this.wallwhilelist.length == 0)
			return true;
		for (String whilename : this.wallwhilelist) {
			if (whilename.equals(name))
				return true;
		}
		return false;
	}

	public void wallfilter(String name, String[] values) {
		if (this.wallfilterrules == null || this.wallfilterrules.length == 0
				|| values == null || values.length == 0 || iswhilename(name))
			return;

		int j = 0;
		for (String value : values) {
			if (value == null || value.equals("")) {
				j++;
				continue;
			}

			for (int i = 0; i < wallfilterrules.length; i++) {

				if (value.indexOf(wallfilterrules[i]) >= 0) {
					values[j] = null;
					logger.warn("参数" + name + "值" + value + "包含敏感词:"
							+ wallfilterrules[i] + ",存在安全隐患,系统自动过滤掉参数值!");
					break;
				}
			}
			j++;

		}
	}

}
