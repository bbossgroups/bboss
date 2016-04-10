package org.frameworkset.web.socket.inf;

import java.nio.ByteBuffer;

public class PingMessage extends AbstractWebSocketMessage<ByteBuffer> {


	/**
	 * Create a new ping message with an empty payload.
	 */
	public PingMessage() {
		super(ByteBuffer.allocate(0));
	}

	/**
	 * Create a new ping message with the given ByteBuffer payload.
	 * @param payload the non-null payload
	 */
	public PingMessage(ByteBuffer payload) {
		super(payload);
	}


	@Override
	public int getPayloadLength() {
		return (getPayload() != null) ? getPayload().remaining() : 0;
	}

	@Override
	protected String toStringPayload() {
		return (getPayload() != null) ? getPayload().toString() : null;
	}


}
