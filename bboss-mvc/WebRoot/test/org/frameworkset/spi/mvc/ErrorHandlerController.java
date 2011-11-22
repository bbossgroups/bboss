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
package org.frameworkset.spi.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.mvc.AbstractController;

/**
 * <p>Title: ErrorHandlerController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-10
 * @author biaoping.yin
 * @version 1.0
 */
public class ErrorHandlerController extends AbstractController
{
	private String viewName;
    private String ajaxViewName;
    private String ajaxExtension = ".ajax";

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getAjaxViewName() {
        return ajaxViewName;
    }

    public void setAjaxViewName(String ajaxViewName) {
        this.ajaxViewName = ajaxViewName;
    }

    public String getAjaxExtension() {
        return ajaxExtension;
    }

    public void setAjaxExtension(String ajaxExtension) {
        this.ajaxExtension = ajaxExtension;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response,PageContext pageContext) throws Exception {
        String originalURI = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (originalURI != null && originalURI.endsWith(ajaxExtension)) {
            return new ModelAndView(ajaxViewName);
        } else {
            return new ModelAndView(viewName);
        }
//        if (originalURI != null && originalURI.endsWith(ajaxExtension)) {
//            view.setViewName(ajaxViewName);
//        } else {
//            view.setViewName(viewName);
//        }
    }

}
