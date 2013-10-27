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
package org.frameworkset.web.search;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.frameworkset.spi.DisposableBean;
import org.frameworkset.spi.InitializingBean;
import org.frameworkset.util.annotations.ExceptionHandle;
import org.frameworkset.util.annotations.HandlerMapping;
import org.frameworkset.util.annotations.HttpMethod;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.servlet.ModelAndView;

@HandlerMapping("/rest/search")
public class SearchController implements InitializingBean, DisposableBean {
	private String indexLocation = "E:\\lucene\\indexs\\book1\\index";
	private IndexSearcher searcher = null;
	
//http://localhost:8080/bboss-mvc/rest/search/dosearch
	@HandlerMapping(value = "/dosearch", method = { HttpMethod.GET,
			HttpMethod.POST })
	public ModelAndView search(
			@RequestParam(name = "query") String queryString,
			@RequestParam(name = "startat", defaultvalue = "0") int startindex,
			@RequestParam(name = "maxresults", defaultvalue = "50") int maxpage,
			HttpServletRequest request) throws IOException {

		Query query = null; // the Query created by the QueryParser
		TopDocs hits = null; // the search results
		
		int thispage = 0; // used for the for/next either maxpage or
		// hits.totalHits - startindex - whichever is
		// less
		if (queryString == null || queryString.equals(""))
		{
			return new ModelAndView("/search/results", "querystringerrorcode", "query String is null.")
			.addObject("queryString", "").addObject("maxpage",maxpage);

			
		}

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT); // construct
		// our
		// usual
		// analyzer
		try {
			QueryParser qp = new QueryParser(Version.LUCENE_CURRENT,
					"contents", analyzer);
			query = qp.parse(queryString); // parse the
		} catch (ParseException e) { // query and construct the Query
			// object
			// if it's just "operator error"
			// send them a nice error HTML
			String errorcode = "Error while parsing query: "
					+ escapeHTML(e.getMessage());
			return new ModelAndView("/search/results", "parsingerrorcode", errorcode)
						.addObject("maxpage",maxpage)
						.addObject("queryString", queryString).addObject("totalHits", 0);

		}
		String times = "0";
		if (searcher != null) { // if we've had no errors
			// searcher != null was to handle
			// a weird compilation bug
			thispage = maxpage; // default last element to maxpage
			long start = System.currentTimeMillis();
			hits = searcher.search(query, maxpage + startindex); // run the
			
			long end = System.currentTimeMillis();
			times = (end - start) + "毫秒";
			
			// query
			if (hits.totalHits == 0) { // if we got no results tell the user
				String emptycode = "I'm sorry I couldn't find what you were looking for. ";

				return new ModelAndView("/search/results", "emptycode",
						emptycode).addObject("maxpage",maxpage)
						.addObject("queryString", queryString)
						.addObject("totalHits", 0)
						.addObject("times", times);

			}
		}
		List documents = new ArrayList();

		if ((startindex + maxpage) > hits.totalHits) {
			thispage = hits.totalHits - startindex; // set the max index to
			// maxpage or last
		} // actual search result whichever is less

		for (int i = startindex; i < (thispage + startindex); i++) { // for each
			// element
			Document doc = searcher.doc(hits.scoreDocs[i].doc); // get the next
			DocumentInfo info = new DocumentInfo(); // document
			String doctitle = doc.get("title"); // get its title
			String url = doc.get("path"); // get its path field
			if (url != null && url.startsWith("../webapps/")) {
				url = url.substring(10);
			}
			if ((doctitle == null) || doctitle.equals(""))
				doctitle = url;
			String summary = doc.get("summary");
			info.setDoctitle(doctitle);
			info.setUrl(url);
			info.setSummary(summary);
			documents.add(info);
		}
		String moreurl = null;
		ModelAndView mv = new ModelAndView("/search/results")
		.addObject("docs",
				documents).addObject("maxpage",maxpage)
				.addObject("queryString", queryString)
				.addObject("times", times).addObject("totalHits", hits.totalHits);
		
		if ((startindex + maxpage) < hits.totalHits) { // if there are more
			// results...display
			// the more link
			moreurl = "/bboss-mvc/rest/search/dosearch?query="
					+ URLEncoder.encode(queryString) +

					"&maxresults=" + maxpage + "&startat="
					+ (startindex + maxpage);
			mv.addObject("moreurl", moreurl);
			// then include our footer.
		}

		return mv;

	}

	public void setIndexLocation(String indexLocation) {
		this.indexLocation = indexLocation;
	}

	public String escapeHTML(String s) {
		s = s.replaceAll("&", "&amp;");
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		s = s.replaceAll("\"", "&quot;");
		s = s.replaceAll("'", "&apos;");
		return s;
	}

	public void afterPropertiesSet() throws Exception {
		String indexName = indexLocation; // local copy of the configuration
		// variable
		try {
			
			File f = new File(
								indexName);
			if(f.exists())
			{
				IndexReader reader = IndexReader.open(FSDirectory.open(f), true); // only searching, so read-only=true
				searcher = new IndexSearcher(reader); // create an indexSearcher for
			}
			// our page
		} catch (Exception e) { // any error that happens is probably due
			e.printStackTrace();
		}

	}

	public void destroy() throws Exception {
		if (searcher != null)
			searcher.close();

	}
	
	/**
	 * 错误处理方法
	 * @param request
	 * @param response
	 * @param error
	 * @return
	 */
	@ExceptionHandle
	public ModelAndView exceptionHandler(HttpServletRequest request,
									HttpServletResponse response,
									Throwable error)
	{
		return null;
	}

}
