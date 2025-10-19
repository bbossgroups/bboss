package org.frameworkset.web.servlet.handler;
/**
 * Copyright 2025 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.nio.charset.StandardCharsets;

/**
 * @author biaoping.yin
 * @Date 2025/10/19
 */
public class ReactorConstant {
   public static final byte[] SERVER_TERMINAL_BYTES = "服务端异常：终止会话!".getBytes(StandardCharsets.UTF_8);

}
