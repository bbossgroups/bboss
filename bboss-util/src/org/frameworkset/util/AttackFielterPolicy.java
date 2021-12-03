package org.frameworkset.util;
/**
 * Copyright 2020 bboss
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

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2021/12/1 9:02
 * @author biaoping.yin
 * @version 1.0
 */
public interface AttackFielterPolicy {
	public void init();
	public void load();
	public boolean isDisable();
	/**
	 * 单位：秒
	 * @return
	 */
	public Long getAttackRuleCacheRefreshInterval();
	public String[] getXSSWallwhilelist();
	public String[] getXSSWallfilterrules();
	public String[] getSensitiveWallwhilelist();
	public String[] getSensitiveWallfilterrules();

	/**
	 * attack白名单url，对应的url不会做xss、敏感词扫描
	 * @return
	 */
	public String[] getWhiteUrls();
	/**
	 * xss检测
	 * @return
	 */
	public boolean xssCheck(String paramValue,String xssWallRule);

	/**
	 * 敏感词检测
	 * @return
	 */
	public boolean sensitiveCheck(String paramValue,String sensitiveWallRule);


	/**
	 * 攻击处理
	 */
	public void attackHandle(AttackContext attackContext) throws AttackException;



}
