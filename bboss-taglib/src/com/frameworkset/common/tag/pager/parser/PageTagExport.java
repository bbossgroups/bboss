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

public final class PageTagExport {

    public static final String
	PAGE_URL    = "pageUrl",
	PAGE_NUMBER = "pageNumber",
	FIRST_ITEM  = "firstItem",
	LAST_ITEM   = "lastItem";

    private String pageUrl = null;
    private String pageNumber = null;
    private String firstItem = null;
    private String lastItem = null;

    final void setPageUrl(String id) {
	pageUrl = id;
    }

    final void setPageNumber(String id) {
	pageNumber = id;
    }

    final void setFirstItem(String id) {
	firstItem = id;
    }

    final void setLastItem(String id) {
	lastItem = id;
    }

    public final String getPageUrl() {
	return pageUrl;
    }

    public final String getPageNumber() {
	return pageNumber;
    }

    public final String getFirstItem() {
	return firstItem;
    }

    public final String getLastItem() {
	return lastItem;
    }
}
