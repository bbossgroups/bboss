package org.frameworkset.web.socket.inf;

import java.util.List;

public interface SubProtocolCapable {

	/**
	 * Return the list of supported sub-protocols.
	 */
	List<String> getSubProtocols();

}
