/*
 *  Copyright 2008-2010 biaoping.yin
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

package org.frameworkset.web.servlet.view;

/**
 * Abstract base class for template view resolvers,
 * in particular for Velocity and FreeMarker views.
 *
 * <p>Provides a convenient way to specify {@link AbstractTemplateView}'s
 * exposure flags for request attributes, session attributes,
 * and Bboss's macro helpers.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see AbstractTemplateView
 * @see org.frameworkset.web.servlet.view.velocity.VelocityViewResolver
 * @see org.frameworkset.web.servlet.view.freemarker.FreeMarkerViewResolver
 */
public class AbstractTemplateViewResolver extends UrlBasedViewResolver {

	private boolean exposeRequestAttributes = false;

	private boolean allowRequestOverride = false;

	private boolean exposeSessionAttributes = false;

	private boolean allowSessionOverride = false;

	private boolean exposeBbossMacroHelpers = true;


	@Override
	protected Class requiredViewClass() {
		return AbstractTemplateView.class;
	}

	/**
	 * Set whether all request attributes should be added to the
	 * model prior to merging with the template. Default is "false".
	 * @see AbstractTemplateView#setExposeRequestAttributes
	 */
	public void setExposeRequestAttributes(boolean exposeRequestAttributes) {
		this.exposeRequestAttributes = exposeRequestAttributes;
	}

	/**
	 * Set whether HttpServletRequest attributes are allowed to override (hide)
	 * controller generated model attributes of the same name. Default is "false",
	 * which causes an exception to be thrown if request attributes of the same
	 * name as model attributes are found.
	 * @see AbstractTemplateView#setAllowRequestOverride
	 */
	public void setAllowRequestOverride(boolean allowRequestOverride) {
		this.allowRequestOverride = allowRequestOverride;
	}

	/**
	 * Set whether all HttpSession attributes should be added to the
	 * model prior to merging with the template. Default is "false".
	 * @see AbstractTemplateView#setExposeSessionAttributes
	 */
	public void setExposeSessionAttributes(boolean exposeSessionAttributes) {
		this.exposeSessionAttributes = exposeSessionAttributes;
	}

	/**
	 * Set whether HttpSession attributes are allowed to override (hide)
	 * controller generated model attributes of the same name. Default is "false",
	 * which causes an exception to be thrown if session attributes of the same
	 * name as model attributes are found.
	 * @see AbstractTemplateView#setAllowSessionOverride
	 */
	public void setAllowSessionOverride(boolean allowSessionOverride) {
		this.allowSessionOverride = allowSessionOverride;
	}

	/**
	 * Set whether to expose a RequestContext for use by Bboss's macro library,
	 * under the name "BbossMacroRequestContext". Default is "true".
	 * @see AbstractTemplateView#setExposeBbossMacroHelpers
	 */
	public void setExposeBbossMacroHelpers(boolean exposeBbossMacroHelpers) {
		this.exposeBbossMacroHelpers = exposeBbossMacroHelpers;
	}


	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		AbstractTemplateView view = (AbstractTemplateView) super.buildView(viewName);
		view.setExposeRequestAttributes(this.exposeRequestAttributes);
		view.setAllowRequestOverride(this.allowRequestOverride);
		view.setExposeSessionAttributes(this.exposeSessionAttributes);
		view.setAllowSessionOverride(this.allowSessionOverride);
		view.setexposeBbossMacroHelpers(this.exposeBbossMacroHelpers);
		return view;
	}

}
