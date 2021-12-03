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
public class AttackException extends RuntimeException{
	private AttackContext attackContext;
	public AttackException() {
	}

	public AttackException(String message,AttackContext attackContext) {
		super(message);
		this.attackContext = attackContext;
	}

	public AttackException(String message, Throwable cause,AttackContext attackContext) {
		super(message, cause);
		this.attackContext = attackContext;
	}

	public AttackException(Throwable cause,AttackContext attackContext) {
		super(cause);
		this.attackContext = attackContext;
	}

	public AttackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,AttackContext attackContext) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.attackContext = attackContext;
	}

	public AttackContext getAttackContext() {
		return attackContext;
	}
}
