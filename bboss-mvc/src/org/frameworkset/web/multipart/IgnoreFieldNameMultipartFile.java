/**
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
package org.frameworkset.web.multipart;

/**
 * <p> IgnoreFieldNameMultipartFile.java</p>
 * <p> Description: 如果参数类型指定为IgnoreFieldNameMultipartFile，则不管参数名称是否和附件
 * 对应的input元素是否相符，直接将上传附件对象或者对象数组直接赋给该参数
 * </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-5-15 下午3:21:04
 * @author biaoping.yin
 * @version 1.0
 */
public interface IgnoreFieldNameMultipartFile extends MultipartFile {

}
