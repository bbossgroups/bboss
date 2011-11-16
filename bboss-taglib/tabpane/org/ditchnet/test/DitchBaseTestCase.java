/*
 * The contents of this file are subject to the GNU Lesser General Public
 * License Version 2.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/lesser.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Developer:
 * Todd Ditchendorf, todd@ditchnet.org
 *
 */

package org.ditchnet.test;

import junit.framework.TestCase;


public abstract class DitchBaseTestCase extends TestCase {

	public DitchBaseTestCase(String name) {
		super(name);
	}
	
	protected void dump(String message) { 
		System.out.println(message);
	}
	
	protected void dump (String message0,String message1) {
		System.out.println(message0);
		System.out.println(message1);
	}
	
}
