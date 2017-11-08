package org.frameworkset.spi.remote.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class StringResponseHandler implements ResponseHandler<String> {

	public StringResponseHandler() {
		// TODO Auto-generated constructor stub
	}
	
	 @Override
     public String handleResponse(final HttpResponse response)
             throws ClientProtocolException, IOException {
         int status = response.getStatusLine().getStatusCode();

         if (status >= 200 && status < 300) {
             HttpEntity entity = response.getEntity();

             return entity != null ? EntityUtils.toString(entity) : null;
         } else {
             HttpEntity entity = response.getEntity();
             if (entity != null )
                 throw new ClientProtocolException(EntityUtils.toString(entity));
             else
                 throw new ClientProtocolException("Unexpected response status: " + status);
         }
     }

}
