package org.frameworkset.http.client;

import org.frameworkset.spi.remote.http.HttpRequestUtil;
import org.junit.Test;

/**
 * Created by 1 on 2017/4/17.
 */
public class TestHttpRequestbody {
    @Test
    public void testHttpJsonRequestbody() throws Exception {
        System.out.println(HttpRequestUtil.sendJsonBody("{\"id\":\"15284b36-3404-4bf8-8f14-c2114f2d97fb\",\"data\":\"国产j2ee框架 bboss\"}","http://localhost:9096/xmlrequest/xml/echohttpjson.page"));
    }

    @Test
    public void testHttpStringRequestbody() throws Exception {
        System.out.println(HttpRequestUtil.sendStringBody("{\"id\":\"15284b36-3404-4bf8-8f14-c2114f2d97fb\",\"data\":\"国产j2ee框架 bboss\"}","http://localhost:8080/xmlrequest/xml/echohttpstring.page"));
    }
    
    
    @Test
    public void testHttpsJsonRequestbody() throws Exception {
        System.out.println(HttpRequestUtil.sendJsonBody("{\"id\":\"15284b36-3404-4bf8-8f14-c2114f2d97fb\",\"data\":\"国产j2ee框架 bboss\"}","https://bboss:8443/xmlrequest/xml/echohttpjson.page"));
    }

    @Test
    public void testHttpsStringRequestbody() throws Exception {
        System.out.println(HttpRequestUtil.sendStringBody("{\"id\":\"15284b36-3404-4bf8-8f14-c2114f2d97fb\",\"data\":\"国产j2ee框架 bboss\"}","https://localhost:8443/xmlrequest/xml/echohttpstring.page"));
    }
}
