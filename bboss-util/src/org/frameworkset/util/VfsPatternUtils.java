/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.frameworkset.util;

import org.frameworkset.util.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;


/**
 * Artificial class used for accessing the {@link VfsUtils} methods
 * without exposing them to the entire world.
 *
 * @author Costin Leau
 * @since 3.0.3
 */
public abstract class VfsPatternUtils extends VfsUtils {

	@Nullable
	public static Object getVisitorAttributes() {
		return doGetVisitorAttributes();
	}

	public static String getPath(Object resource) {
		String path = doGetPath(resource);
		return (path != null ? path : "");
	}

	public static Object findRoot(URL url) throws IOException {
		return getRoot(url);
	}

	public static void visit(Object resource, InvocationHandler visitor) throws IOException {
		Object visitorProxy = Proxy.newProxyInstance(
				VIRTUAL_FILE_VISITOR_INTERFACE.getClassLoader(),
				new Class<?>[] {VIRTUAL_FILE_VISITOR_INTERFACE}, visitor);
		invokeVfsMethod(VIRTUAL_FILE_METHOD_VISIT, resource, visitorProxy);
	}

}
