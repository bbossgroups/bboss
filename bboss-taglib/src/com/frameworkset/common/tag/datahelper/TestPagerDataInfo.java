/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    *
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *  Author of Learning Java 						     					 *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.tag.datahelper;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;


/**
 * @author biaoping.yin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class TestPagerDataInfo extends DataInfoImpl{

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.pager.DataInfo#getDataList()
	 */
	protected ListInfo getDataList(String sortKey,
							boolean desc) {
		// TODO Auto-generated method stub
		List list = new ArrayList();
		ListInfo info = new ListInfo();


		for(int i = 0; i < this.getItemCount(); i ++)
		{
			TestData data = new TestData();
			data.bgColor = webPalette[i][0];
			data.fontColor = webPalette[i][1];
			list.add(data);
		}
		info.setDatas( list);
		return info;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.pager.DataInfo#getDataList(int, int)
	 */
	protected ListInfo getDataList(String sortKey,
							boolean desc,
							long offSet,
							int pageItemsize)
	{


		ListInfo info = new ListInfo();
		List list = new ArrayList();

		//System.out.println("offSet > this.getItemCount() || pageItemsize == 0 :"+(offSet > this.getItemCount() || pageItemsize == 0));

		for(long i = offSet; i < offSet + pageItemsize && i < webPalette.length; i ++)
		{
			TestData data = new TestData();
			data.bgColor = webPalette[(int)i][0];
			data.fontColor = webPalette[(int)i][1];
			list.add(data);
		}
		info.setDatas(list);
		info.setTotalSize((long)webPalette.length);
		return info;
	}

	private static final String BLACK = "#000000", WHITE = "#ffffff";
	private static final String[][] webPalette = {
			{ WHITE,   BLACK},
			{"#cccccc",BLACK},
			{"#999999",BLACK},
		{ WHITE,   BLACK},
					{"#cccccc",BLACK},
					{"#999999",BLACK},
		{ WHITE,   BLACK},
					{"#cccccc",BLACK},
					{"#999999",BLACK},
		{ WHITE,   BLACK},
					{"#cccccc",BLACK},
					{"#999999",BLACK},{ WHITE,   BLACK},
		{"#cccccc",BLACK},
		{"#999999",BLACK},{ WHITE,   BLACK},
		{"#cccccc",BLACK},
		{"#999999",BLACK},{ WHITE,   BLACK},
		{"#cccccc",BLACK},
		{"#999999",BLACK},{ WHITE,   BLACK},
		{"#cccccc",BLACK},
		{"#999999",BLACK},{ WHITE,   BLACK},
		{"#cccccc",BLACK},
		{"#999999",BLACK},{ WHITE,   BLACK},
		{"#cccccc",BLACK},
		{"#999999",BLACK},{ WHITE,   BLACK},
		{"#cccccc",BLACK},
		{"#999999",BLACK},{ WHITE,   BLACK},
		{"#cccccc",BLACK},
		{"#999999",BLACK},{ WHITE,   BLACK},
		{"#cccccc",BLACK},
		{"#999999",BLACK},{ WHITE,   BLACK},
		{"#cccccc",BLACK},
		{"#999999",BLACK}
					};


}
