package org.frameworkset.spi.remote.http;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.frameworkset.util.SimpleStringUtil;

public class MapResponseHandler  implements ResponseHandler<Map> {

	public MapResponseHandler() {
		// TODO Auto-generated constructor stub
	}
	
	 @Override
     public Map handleResponse(final HttpResponse response)
             throws ClientProtocolException, IOException {
         int status = response.getStatusLine().getStatusCode();

         if (status >= 200 && status < 300) {
             HttpEntity entity = response.getEntity();
             return SimpleStringUtil.json2Object(entity.getContent(), Map.class);
             //return entity != null ? EntityUtils.toString(entity) : null;
         } else {
             HttpEntity entity = response.getEntity();
             if (entity != null )
            	 return SimpleStringUtil.json2Object(entity.getContent(), Map.class);
             else
                 throw new ClientProtocolException("Unexpected response status: " + status);
         }
     }
	
	

}
