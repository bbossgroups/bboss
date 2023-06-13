/*
 *  Copyright 2008-2010 biaoping.yin
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
package org.frameworkset.util;

import junit.framework.TestCase;

/**
 * @author Alef Arendsen
 * @author Seth Ladd
 * @author Juergen Hoeller
 */
public class PathMatcherTests extends TestCase {

	public void testMath(){
		PathMatcher pathMatcher = new AntPathMatcher();
		assertTrue(pathMatcher.match("https://wap.hn.10086.cn/h5web/static/newPersonal/personal_index_home.html?moduleId=wechat_hnydwt&positionId=wechat_XBGRZX_WTCDL&chanId=E008&WT.place=HN_WT&WT.mc_id=2006_Wx_1_WTCDL_XBGRZX**", "https://wap.hn.10086.cn/h5web/static/newPersonal/personal_index_home.html?moduleId=wechat_hnydwt&positionId=wechat_XBGRZX_WTCDL&chanId=E008&WT.place=HN_WT&WT.mc_id=2006_Wx_1_WTCDL_XBGRZX"));
		assertTrue(pathMatcher.match("https://wap.hn.10086.cn/h5web/static/newPersonal/personal_index_home.html?moduleId=wechat_hnydwt&positionId=wechat_XBGRZX_WTCDL&chanId=*&WT.place=HN_WT&WT.mc_id=2006_Wx_1_WTCDL_XBGRZX", "https://wap.hn.10086.cn/h5web/static/newPersonal/personal_index_home.html?moduleId=wechat_hnydwt&positionId=wechat_XBGRZX_WTCDL&chanId=E008&WT.place=HN_WT&WT.mc_id=2006_Wx_1_WTCDL_XBGRZX"));
		assertTrue(pathMatcher.match("https://wap.hn.10086.cn/h5web/static/newPersonal/personal_index_home.html?moduleId=wechat_hnydwt*", "https://wap.hn.10086.cn/h5web/static/newPersonal/personal_index_home.html?moduleId=wechat_hnydwt&positionId=wechat_XBGRZX_WTCDL&chanId=E008&WT.place=HN_WT&WT.mc_id=2006_Wx_1_WTCDL_XBGRZX"));
	}
	public void testAntPathMatcher() {
		PathMatcher pathMatcher = new AntPathMatcher();

		// test exact matching
		assertTrue(pathMatcher.match("test", "test"));
		assertTrue(pathMatcher.match("/test", "/test"));
		assertFalse(pathMatcher.match("/test.jpg", "test.jpg"));
		assertFalse(pathMatcher.match("test", "/test"));
		assertFalse(pathMatcher.match("/test", "test"));

		// test matching with ?'s
		assertTrue(pathMatcher.match("t?st", "test"));
		assertTrue(pathMatcher.match("??st", "test"));
		assertTrue(pathMatcher.match("tes?", "test"));
		assertTrue(pathMatcher.match("te??", "test"));
		assertTrue(pathMatcher.match("?es?", "test"));
		assertFalse(pathMatcher.match("tes?", "tes"));
		assertFalse(pathMatcher.match("tes?", "testt"));
		assertFalse(pathMatcher.match("tes?", "tsst"));

		// test matchin with *'s
		assertTrue(pathMatcher.match("*", "test"));
		assertTrue(pathMatcher.match("test*", "test"));
		assertTrue(pathMatcher.match("test*", "testTest"));
		assertTrue(pathMatcher.match("test/*", "test/Test"));
		assertTrue(pathMatcher.match("test/*", "test/t"));
		assertTrue(pathMatcher.match("test/*", "test/"));
		assertTrue(pathMatcher.match("*test*", "AnothertestTest"));
		assertTrue(pathMatcher.match("*test", "Anothertest"));
		assertTrue(pathMatcher.match("*.*", "test."));
		assertTrue(pathMatcher.match("*.*", "test.test"));
		assertTrue(pathMatcher.match("*.*", "test.test.test"));
		assertTrue(pathMatcher.match("test*aaa", "testblaaaa"));
		assertFalse(pathMatcher.match("test*", "tst"));
		assertFalse(pathMatcher.match("test*", "tsttest"));
		assertFalse(pathMatcher.match("test*", "test/"));
		assertFalse(pathMatcher.match("test*", "test/t"));
		assertFalse(pathMatcher.match("test/*", "test"));
		assertFalse(pathMatcher.match("*test*", "tsttst"));
		assertFalse(pathMatcher.match("*test", "tsttst"));
		assertFalse(pathMatcher.match("*.*", "tsttst"));
		assertFalse(pathMatcher.match("test*aaa", "test"));
		assertFalse(pathMatcher.match("test*aaa", "testblaaab"));

		// test matching with ?'s and /'s
		assertTrue(pathMatcher.match("/?", "/a"));
		assertTrue(pathMatcher.match("/?/a", "/a/a"));
		assertTrue(pathMatcher.match("/a/?", "/a/b"));
		assertTrue(pathMatcher.match("/??/a", "/aa/a"));
		assertTrue(pathMatcher.match("/a/??", "/a/bb"));
		assertTrue(pathMatcher.match("/?", "/a"));

		// test matching with **'s
		assertTrue(pathMatcher.match("/**", "/testing/testing"));
		assertTrue(pathMatcher.match("/*/**", "/testing/testing"));
		assertTrue(pathMatcher.match("/**/*", "/testing/testing"));
		assertTrue(pathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla"));
		assertTrue(pathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla/bla"));
		assertTrue(pathMatcher.match("/**/test", "/bla/bla/test"));
		assertTrue(pathMatcher.match("/bla/**/**/bla", "/bla/bla/bla/bla/bla/bla"));
		assertTrue(pathMatcher.match("/bla*bla/test", "/blaXXXbla/test"));
		assertTrue(pathMatcher.match("/*bla/test", "/XXXbla/test"));
		assertFalse(pathMatcher.match("/bla*bla/test", "/blaXXXbl/test"));
		assertFalse(pathMatcher.match("/*bla/test", "XXXblab/test"));
		assertFalse(pathMatcher.match("/*bla/test", "XXXbl/test"));

		assertFalse(pathMatcher.match("/????", "/bala/bla"));
		assertFalse(pathMatcher.match("/**/*bla", "/bla/bla/bla/bbb"));

		assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing/"));
		assertTrue(pathMatcher.match("/*bla*/**/bla/*", "/XXXblaXXXX/testing/testing/bla/testing"));
		assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing"));
		assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing.jpg"));

		assertTrue(pathMatcher.match("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing/"));
		assertTrue(pathMatcher.match("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing"));
		assertTrue(pathMatcher.match("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing"));
		assertFalse(pathMatcher.match("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing/testing"));

		assertFalse(pathMatcher.match("/x/x/**/bla", "/x/x/x/"));

		assertTrue(pathMatcher.match("", ""));
	}

	public void testAntPathMatcherWithMatchStart() {
		PathMatcher pathMatcher = new AntPathMatcher();

		// test exact matching
		assertTrue(pathMatcher.matchStart("test", "test"));
		assertTrue(pathMatcher.matchStart("/test", "/test"));
		assertFalse(pathMatcher.matchStart("/test.jpg", "test.jpg"));
		assertFalse(pathMatcher.matchStart("test", "/test"));
		assertFalse(pathMatcher.matchStart("/test", "test"));

		// test matching with ?'s
		assertTrue(pathMatcher.matchStart("t?st", "test"));
		assertTrue(pathMatcher.matchStart("??st", "test"));
		assertTrue(pathMatcher.matchStart("tes?", "test"));
		assertTrue(pathMatcher.matchStart("te??", "test"));
		assertTrue(pathMatcher.matchStart("?es?", "test"));
		assertFalse(pathMatcher.matchStart("tes?", "tes"));
		assertFalse(pathMatcher.matchStart("tes?", "testt"));
		assertFalse(pathMatcher.matchStart("tes?", "tsst"));

		// test matchin with *'s
		assertTrue(pathMatcher.matchStart("*", "test"));
		assertTrue(pathMatcher.matchStart("test*", "test"));
		assertTrue(pathMatcher.matchStart("test*", "testTest"));
		assertTrue(pathMatcher.matchStart("test/*", "test/Test"));
		assertTrue(pathMatcher.matchStart("test/*", "test/t"));
		assertTrue(pathMatcher.matchStart("test/*", "test/"));
		assertTrue(pathMatcher.matchStart("*test*", "AnothertestTest"));
		assertTrue(pathMatcher.matchStart("*test", "Anothertest"));
		assertTrue(pathMatcher.matchStart("*.*", "test."));
		assertTrue(pathMatcher.matchStart("*.*", "test.test"));
		assertTrue(pathMatcher.matchStart("*.*", "test.test.test"));
		assertTrue(pathMatcher.matchStart("test*aaa", "testblaaaa"));
		assertFalse(pathMatcher.matchStart("test*", "tst"));
		assertFalse(pathMatcher.matchStart("test*", "test/"));
		assertFalse(pathMatcher.matchStart("test*", "tsttest"));
		assertFalse(pathMatcher.matchStart("test*", "test/"));
		assertFalse(pathMatcher.matchStart("test*", "test/t"));
		assertTrue(pathMatcher.matchStart("test/*", "test"));
		assertTrue(pathMatcher.matchStart("test/t*.txt", "test"));
		assertFalse(pathMatcher.matchStart("*test*", "tsttst"));
		assertFalse(pathMatcher.matchStart("*test", "tsttst"));
		assertFalse(pathMatcher.matchStart("*.*", "tsttst"));
		assertFalse(pathMatcher.matchStart("test*aaa", "test"));
		assertFalse(pathMatcher.matchStart("test*aaa", "testblaaab"));

		// test matching with ?'s and /'s
		assertTrue(pathMatcher.matchStart("/?", "/a"));
		assertTrue(pathMatcher.matchStart("/?/a", "/a/a"));
		assertTrue(pathMatcher.matchStart("/a/?", "/a/b"));
		assertTrue(pathMatcher.matchStart("/??/a", "/aa/a"));
		assertTrue(pathMatcher.matchStart("/a/??", "/a/bb"));
		assertTrue(pathMatcher.matchStart("/?", "/a"));

		// test matching with **'s
		assertTrue(pathMatcher.matchStart("/**", "/testing/testing"));
		assertTrue(pathMatcher.matchStart("/*/**", "/testing/testing"));
		assertTrue(pathMatcher.matchStart("/**/*", "/testing/testing"));
		assertTrue(pathMatcher.matchStart("test*/**", "test/"));
		assertTrue(pathMatcher.matchStart("test*/**", "test/t"));
		assertTrue(pathMatcher.matchStart("/bla/**/bla", "/bla/testing/testing/bla"));
		assertTrue(pathMatcher.matchStart("/bla/**/bla", "/bla/testing/testing/bla/bla"));
		assertTrue(pathMatcher.matchStart("/**/test", "/bla/bla/test"));
		assertTrue(pathMatcher.matchStart("/bla/**/**/bla", "/bla/bla/bla/bla/bla/bla"));
		assertTrue(pathMatcher.matchStart("/bla*bla/test", "/blaXXXbla/test"));
		assertTrue(pathMatcher.matchStart("/*bla/test", "/XXXbla/test"));
		assertFalse(pathMatcher.matchStart("/bla*bla/test", "/blaXXXbl/test"));
		assertFalse(pathMatcher.matchStart("/*bla/test", "XXXblab/test"));
		assertFalse(pathMatcher.matchStart("/*bla/test", "XXXbl/test"));

		assertFalse(pathMatcher.matchStart("/????", "/bala/bla"));
		assertTrue(pathMatcher.matchStart("/**/*bla", "/bla/bla/bla/bbb"));

		assertTrue(pathMatcher.matchStart("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing/"));
		assertTrue(pathMatcher.matchStart("/*bla*/**/bla/*", "/XXXblaXXXX/testing/testing/bla/testing"));
		assertTrue(pathMatcher.matchStart("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing"));
		assertTrue(pathMatcher.matchStart("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing.jpg"));

		assertTrue(pathMatcher.matchStart("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing/"));
		assertTrue(pathMatcher.matchStart("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing"));
		assertTrue(pathMatcher.matchStart("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing"));
		assertTrue(pathMatcher.matchStart("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing/testing"));

		assertTrue(pathMatcher.matchStart("/x/x/**/bla", "/x/x/x/"));

		assertTrue(pathMatcher.matchStart("", ""));
	}

	public void testAntPathMatcherWithUniqueDeliminator() {
		AntPathMatcher pathMatcher = new AntPathMatcher();
		pathMatcher.setPathSeparator(".");

		// test exact matching
		assertTrue(pathMatcher.match("test", "test"));
		assertTrue(pathMatcher.match(".test", ".test"));
		assertFalse(pathMatcher.match(".test/jpg", "test/jpg"));
		assertFalse(pathMatcher.match("test", ".test"));
		assertFalse(pathMatcher.match(".test", "test"));

		// test matching with ?'s
		assertTrue(pathMatcher.match("t?st", "test"));
		assertTrue(pathMatcher.match("??st", "test"));
		assertTrue(pathMatcher.match("tes?", "test"));
		assertTrue(pathMatcher.match("te??", "test"));
		assertTrue(pathMatcher.match("?es?", "test"));
		assertFalse(pathMatcher.match("tes?", "tes"));
		assertFalse(pathMatcher.match("tes?", "testt"));
		assertFalse(pathMatcher.match("tes?", "tsst"));

		// test matchin with *'s
		assertTrue(pathMatcher.match("*", "test"));
		assertTrue(pathMatcher.match("test*", "test"));
		assertTrue(pathMatcher.match("test*", "testTest"));
		assertTrue(pathMatcher.match("*test*", "AnothertestTest"));
		assertTrue(pathMatcher.match("*test", "Anothertest"));
		assertTrue(pathMatcher.match("*/*", "test/"));
		assertTrue(pathMatcher.match("*/*", "test/test"));
		assertTrue(pathMatcher.match("*/*", "test/test/test"));
		assertTrue(pathMatcher.match("test*aaa", "testblaaaa"));
		assertFalse(pathMatcher.match("test*", "tst"));
		assertFalse(pathMatcher.match("test*", "tsttest"));
		assertFalse(pathMatcher.match("*test*", "tsttst"));
		assertFalse(pathMatcher.match("*test", "tsttst"));
		assertFalse(pathMatcher.match("*/*", "tsttst"));
		assertFalse(pathMatcher.match("test*aaa", "test"));
		assertFalse(pathMatcher.match("test*aaa", "testblaaab"));

		// test matching with ?'s and .'s
		assertTrue(pathMatcher.match(".?", ".a"));
		assertTrue(pathMatcher.match(".?.a", ".a.a"));
		assertTrue(pathMatcher.match(".a.?", ".a.b"));
		assertTrue(pathMatcher.match(".??.a", ".aa.a"));
		assertTrue(pathMatcher.match(".a.??", ".a.bb"));
		assertTrue(pathMatcher.match(".?", ".a"));

		// test matching with **'s
		assertTrue(pathMatcher.match(".**", ".testing.testing"));
		assertTrue(pathMatcher.match(".*.**", ".testing.testing"));
		assertTrue(pathMatcher.match(".**.*", ".testing.testing"));
		assertTrue(pathMatcher.match(".bla.**.bla", ".bla.testing.testing.bla"));
		assertTrue(pathMatcher.match(".bla.**.bla", ".bla.testing.testing.bla.bla"));
		assertTrue(pathMatcher.match(".**.test", ".bla.bla.test"));
		assertTrue(pathMatcher.match(".bla.**.**.bla", ".bla.bla.bla.bla.bla.bla"));
		assertTrue(pathMatcher.match(".bla*bla.test", ".blaXXXbla.test"));
		assertTrue(pathMatcher.match(".*bla.test", ".XXXbla.test"));
		assertFalse(pathMatcher.match(".bla*bla.test", ".blaXXXbl.test"));
		assertFalse(pathMatcher.match(".*bla.test", "XXXblab.test"));
		assertFalse(pathMatcher.match(".*bla.test", "XXXbl.test"));
	}

	public void testAntPathMatcherExtractPathWithinPattern() throws Exception {
		PathMatcher pathMatcher = new AntPathMatcher();

		assertEquals("", pathMatcher.extractPathWithinPattern("/docs/commit.html", "/docs/commit.html"));

		assertEquals("cvs/commit", pathMatcher.extractPathWithinPattern("/docs/*", "/docs/cvs/commit"));
		assertEquals("commit.html", pathMatcher.extractPathWithinPattern("/docs/cvs/*.html", "/docs/cvs/commit.html"));
		assertEquals("cvs/commit", pathMatcher.extractPathWithinPattern("/docs/**", "/docs/cvs/commit"));
		assertEquals("cvs/commit.html", pathMatcher.extractPathWithinPattern("/docs/**/*.html", "/docs/cvs/commit.html"));
		assertEquals("commit.html", pathMatcher.extractPathWithinPattern("/docs/**/*.html", "/docs/commit.html"));
		assertEquals("commit.html", pathMatcher.extractPathWithinPattern("/*.html", "/commit.html"));
		assertEquals("docs/commit.html", pathMatcher.extractPathWithinPattern("/*.html", "/docs/commit.html"));
		assertEquals("/commit.html", pathMatcher.extractPathWithinPattern("*.html", "/commit.html"));
		assertEquals("/docs/commit.html", pathMatcher.extractPathWithinPattern("*.html", "/docs/commit.html"));
		assertEquals("/docs/commit.html", pathMatcher.extractPathWithinPattern("**/*.*", "/docs/commit.html"));
		assertEquals("/docs/commit.html", pathMatcher.extractPathWithinPattern("*", "/docs/commit.html"));

		assertEquals("docs/cvs/commit", pathMatcher.extractPathWithinPattern("/d?cs/*", "/docs/cvs/commit"));
		assertEquals("cvs/commit.html", pathMatcher.extractPathWithinPattern("/docs/c?s/*.html", "/docs/cvs/commit.html"));
		assertEquals("docs/cvs/commit", pathMatcher.extractPathWithinPattern("/d?cs/**", "/docs/cvs/commit"));
		assertEquals("docs/cvs/commit.html", pathMatcher.extractPathWithinPattern("/d?cs/**/*.html", "/docs/cvs/commit.html"));
	}
	public void testrefer()
	{
		PathMatcher pathMatcher = new AntPathMatcher();
		System.out.println(pathMatcher.urlContain("*.test.com.cn", "http://test.test.com.cn/test"));
		System.out.println(pathMatcher.urlContain("http://*.test.com.cn", "http://test.test.com.cn/test"));
		System.out.println("###############################################");
		System.out.println(pathMatcher.urlContain("*.test.com.cn", "http://test.test.com.cn/test"));
		System.out.println(pathMatcher.urlContain("http://*.test.com.cn", "https://test.test.com.cn/test"));
		System.out.println("###############################################");
		System.out.println(pathMatcher.urlMatch("*.s.com.cn/test", "http://test.test.com.cn/test"));
		System.out.println(pathMatcher.urlMatch("http://*.s.com.cn/test", "http://test.s.com.cn/test"));
		
		System.out.println("###############################################");
		System.out.println(pathMatcher.urlMatch("*.s.com.cn/test", "http://test.s.com.cn/test/f"));
		System.out.println(pathMatcher.urlMatch("http://*.s.com.cn/test", "http://test.s.com.cn/test/4"));
	}
	public void testyidiantong()
	{
		PathMatcher pathMatcher = new AntPathMatcher();
		//		https://wap.hn.10086.cn/o2o_new/extension/shareForm?type=011&departId=EC3Vc5YDbBm1bA55cDRKcoLTinVqLxQFOCZGnQM0uasyOX3eAFTzqXEbqcRZxZkgY4shVeaNdEhfedsnRrGcDAzOy12xjF3e_RVZHhazqqUC2SKJ-DOj7x0JBSNhTIfWgnUJ9uR6oqxSjDHji9qLIWIgAFrQUD4Py8uDF0Ys4Lk&suNo=r9gqxYSmCpPqXkzKEm_2Qiuo_nqtuTUsorMQ7b-0ZknOFjhmSmDrr_L4lYTTf-Y9bBAs_OzyzYmbRBWeXBoio5HvVQ5wdM2m_sQqPh38LIfRCf-jRDINU-L6OP_mjsRPd0oksCH7CPlG4Q1n8wdysx7lvc-xJ22zs6dkezXZy7I&suPhone=MRBHbSiMHSujL-R8m15f-YZhSuG1JukG6nQZKmIJFQHQoEfmCV8_n-HJ46FsNTjbMtLEMWy8WSavghln9Oem0FRriNRzDkD0fWt9OX5SaBBh8QCRbC0wV9tnNEqQoQ6SZ9oJsYO1FxwJiPEQp2oN3rlNekfNWqX8bffV6bmoSnM&extChnl=1001&extType=1&chanId=ZZZZ&code=013LXA0w3LwtNV2YHk1w3NAMe54LXA0i&state=1
		boolean matched = pathMatcher.match("**/**extChnl=1001**","https://wap.hn.s.cn/wap/static/activity/UpEnjoyPacDown/index.html?forceLogin=1&forceAttention=0&sourceShopId=202004103013&sourceStaffId=100006841088&shopPhone=15575974417&serialNumberO2OQD=15575974417&isShare=yes&shopId=202004103013&bizHandType=3&extChnl=1001&departId=Rn34qvpo3LA%3D&suPhone=DZQ0W5%2Fg%2F%2F%2BP6qr7Laak8w%3D%3D&CHANID=E050&sourceChannelId=E050&chanId=E050&suNo=&openId=oTxKsuFDgrIIBl33gjoy2kP_y_YQ&secretOpenId=fc878a36f64aaae68ff3cb8f92513d0d");
		System.out.println(matched);
	}


}
