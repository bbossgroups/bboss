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

import java.lang.*;

public abstract class nsVerifier {

     static final byte eStart = (byte)0;
     static final byte eError = (byte)1;
     static final byte eItsMe = (byte)2;
     static final int eidxSft4bits = 3;
     static final int eSftMsk4bits = 7;
     static final int eBitSft4bits = 2;
     static final int eUnitMsk4bits = 0x0000000F;

     nsVerifier() {
     }

     public abstract String charset() ;
     public abstract int stFactor()   ;
     public abstract int[] cclass()   ;
     public abstract int[] states()   ;

     public abstract boolean isUCS2() ;

     public static byte getNextState(nsVerifier v, byte b, byte s) {

         return (byte) ( 0xFF & 
	     (((v.states()[((
		   (s*v.stFactor()+(((v.cclass()[((b&0xFF)>>v.eidxSft4bits)]) 
		   >> ((b & v.eSftMsk4bits) << v.eBitSft4bits)) 
		   & v.eUnitMsk4bits ))&0xFF)
		>> v.eidxSft4bits) ]) >> (((
		   (s*v.stFactor()+(((v.cclass()[((b&0xFF)>>v.eidxSft4bits)]) 
		   >> ((b & v.eSftMsk4bits) << v.eBitSft4bits)) 
		   & v.eUnitMsk4bits ))&0xFF) 
		& v.eSftMsk4bits) << v.eBitSft4bits)) & v.eUnitMsk4bits )
	 ) ;

     }


}
