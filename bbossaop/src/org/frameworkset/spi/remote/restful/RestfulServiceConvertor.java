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

package org.frameworkset.spi.remote.restful;

/**
 * <p>Title: RestfulServiceConvertor.java</p> 
 * <p>Description: 地址信息翻译</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-5 下午04:33:57
 * @author biaoping.yin
 * @version 1.0
 */
public interface RestfulServiceConvertor
{
    /**
     * 将restful标识地址转换为实际目标注册地址
     * 转换规则，由restfuluddi转换出实际地址，然后用实际地址结合serviceid和相关的认证信息
     * 来获取下一步服务调用地址。
     * @param restfuluddi restful风格地址标识，需要转换为目标地址
     * @param serviceid 要调用的服务标识
     * @return
     */
    public String convert(String restfuluddi,String serviceid);
    
}
