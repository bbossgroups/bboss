/*
 *  Pager Tag Library
 *
 *  Copyright (C) 2002  James Klicman <james@jsptags.com>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.frameworkset.common.tag.pager.parser;

public final class PagerTagExport {

    public static final String
	PAGE_OFFSET = "pageOffset",
	PAGE_NUMBER = "pageNumber";

    private String pageOffset = null;
    private String pageNumber = null;

    final void setPageOffset(String id) {
	pageOffset = id;
    }

    final void setPageNumber(String id) {
	pageNumber = id;
    }

    public final String getPageOffset() {
	return pageOffset;
    }

    public final String getPageNumber() {
	return pageNumber;
    }
}
