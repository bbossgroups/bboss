package org.frameworkset.spi.remote.http;

import org.apache.http.HttpResponse;

public abstract class StatusResponseHandler {
	protected int reponseStatus;
	public int getReponseStatus() {
		return reponseStatus;
	}

	public void setReponseStatus(int reponseStatus) {
		this.reponseStatus = reponseStatus;
	}

	protected int initStatus(HttpResponse response){
		reponseStatus = response.getStatusLine().getStatusCode();
		return reponseStatus;
	}
}
