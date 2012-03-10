/* -*- Mode: C; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 2 -*-
 *
 * The contents of this file are subject to the Netscape Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/NPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is mozilla.org code.
 *
 * The Initial Developer of the Original Code is Netscape
 * Communications Corporation.  Portions created by Netscape are
 * Copyright (C) 1998 Netscape Communications Corporation. All
 * Rights Reserved.
 *
 * Contributor(s):
 */

package bboss.org.mozilla.intl.chardet ;

import java.lang.* ;

public class nsDetector extends nsPSMDetector 
			implements nsICharsetDetector {

	nsICharsetDetectionObserver mObserver = null ;

	public nsDetector() {
		super() ;
	}

	public nsDetector(int langFlag) {
		super(langFlag) ;
	}

	public void Init(nsICharsetDetectionObserver aObserver) {

	  	mObserver = aObserver ;
		return ;

	}

	public boolean DoIt(byte[] aBuf, int aLen, boolean oDontFeedMe) {

		if (aBuf == null || oDontFeedMe )
		    return false ;

		this.HandleData(aBuf, aLen) ;	
		return mDone ;
	}

	public void Done() {
		this.DataEnd() ;
		return ;
	}

	public void Report(String charset) {
		if (mObserver != null)
		    mObserver.Notify(charset)  ;
	}

	public boolean isAscii(byte[] aBuf, int aLen) {

                for(int i=0; i<aLen; i++) {
                   if ((0x0080 & aBuf[i]) != 0) {
                      return false ;
                   }
                }
		return true ;
	}
}
