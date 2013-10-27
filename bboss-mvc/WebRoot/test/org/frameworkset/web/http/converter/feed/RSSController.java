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
package org.frameworkset.web.http.converter.feed;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.handler.annotations.Controller;

import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Item;

/**
 * <p>Title: RSSController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-12-16
 * @author biaoping.yin
 * @version 1.0
 */
@Controller
public class RSSController {
	private String[] feedtypes=new String[]{"rss_0.91N", "rss_0.91U", "rss_0.93", "rss_0.9", "rss_0.92", "atom_0.3", "rss_1.0", "atom_1.0", "rss_2.0", "rss_0.94"};
//	http://localhost:8080/bboss-mvc/rss/rss.html
	public  @ResponseBody Channel rss()
	{
		 Channel channel = new Channel("rss_2.0");
		  channel.setTitle("The test of RSS generator(Rome)");
		  channel.setDescription("测试频道");
		  channel.setLink("http://www.stefli.com/");
		  channel.setTtl(5);
		  channel.setLanguage("en_US");
		  channel.setEncoding("utf-8");

		  List items = new ArrayList();
		  Item item = new Item();
		  item.setAuthor("<a title=\"\" href=\"http://www2.gliet.edu.cn/gdhq/kudesign/\" target=\"_blank\">stefli</a>");
		  item.setTitle("Here is a news!");
		  item.setLink("http://www.stefli.com/");
		  
		  item.setPubDate(new Date());

		  Description description = new Description();
		  description.setType("html");
		  description.setValue("The news is that you are <b>win</b> the game!!");
		  item.setDescription(description);

		  items.add(item);
		  channel.setItems(items);
		  return channel;
//		  WireFeedOutput out = new WireFeedOutput();
//		  try {
//		   System.out.println(out.outputString(channel));
//		  } catch (IllegalArgumentException e) {
//		   e.printStackTrace();
//		  } catch (FeedException e) {
//		   e.printStackTrace();
//		  }
	}
	private static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");

//	http://localhost:8080/bboss-mvc/rss/atom.html
	public  @ResponseBody Feed atom() throws ParseException
	{
		String feedType = "atom_1.0";
       
        Feed feed = new Feed();
        feed.setFeedType(feedType);
//        SyndFeed feed = new SyndFeedImpl();
//        feed.setFeedType(feedType);
        
        

        feed.setTitle("Sample Feed (created with ROME)");
        List links = new ArrayList();
        Link link = new Link();
        link.setHref("http://rome.dev.java.net");
        links.add(link);
        feed.setAlternateLinks(links);
//        feed.setDescription("This feed has been created using ROME (Java syndication utilities");

        List entries = new ArrayList();
        Entry entry;
        Content description;

        entry = new Entry();
        entry.setTitle("ROME v1.0");
        link = new Link();
        link.setHref("http://wiki.java.net/bin/view/Javawsxml/Rome01");
        links = new ArrayList();
        links.add(link);
        entry.setAlternateLinks(links);
        entry.setPublished(DATE_PARSER.parse("2004-06-08"));
        description = new Content();
        description.setType("text/plain");
        description.setValue("Initial release of ROME");
        entry.setSummary(description);
        entries.add(entry);
        
        

        entry = new Entry();
        entry.setTitle("ROME v2.0");
        link = new Link();
        link.setHref("http://wiki.java.net/bin/view/Javawsxml/Rome02");
        links = new ArrayList();
        links.add(link);
        entry.setAlternateLinks(links);
      
        entry.setPublished(DATE_PARSER.parse("2004-06-16"));
        description = new Content();
        description.setType("text/plain");
        description.setValue("Bug fixes, minor API changes and some new features");
        entry.setSummary(description);
        entries.add(entry);

        entry = new Entry();
        entry.setTitle("ROME v3.0");
        link = new Link();
        link.setHref("http://wiki.java.net/bin/view/Javawsxml/Rome03");
        links = new ArrayList();
        links.add(link);
        entry.setAlternateLinks(links);
     
        entry.setPublished(DATE_PARSER.parse("2004-07-27"));
        description = new Content();
        description.setType("text/html");
        description.setValue("<p>More Bug fixes, mor API changes, some new features and some Unit testing</p>"+
                             "<p>For details check the <a href=\"http://wiki.java.net/bin/view/Javawsxml/RomeChangesLog#RomeV03\">Changes Log</a></p>");
        entry.setSummary(description);
        entries.add(entry);

        feed.setEntries(entries);
        return feed;
//        Writer writer = new FileWriter(fileName);
//        SyndFeedOutput output = new SyndFeedOutput();
//        output.output(feed,writer);
//        writer.close();

	}

}
