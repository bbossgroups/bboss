package org.frameworkset.web.socket.sockjs.frame;

import org.frameworkset.util.Assert;

public class DefaultSockJsFrameFormat  implements SockJsFrameFormat {

	private final String format;


	public DefaultSockJsFrameFormat(String format) {
		Assert.notNull(format, "format must not be null");
		this.format = format;
	}


	@Override
	public String format(SockJsFrame frame) {
		return String.format(this.format, preProcessContent(frame.getContent()));
	}

	protected String preProcessContent(String content) {
		return content;
	}
}
