package org.frameworkset.web.socket.inf;

import java.nio.charset.Charset;

public class TextMessage  extends AbstractWebSocketMessage<String> {

	private static final Charset UTF_8 = Charset.forName("UTF-8");

	private final byte[] bytes;


	/**
	 * Create a new text WebSocket message from the given CharSequence payload.
	 * @param payload the non-null payload
	 */
	public TextMessage(CharSequence payload) {
		super(payload.toString(), true);
		this.bytes = null;
	}

	/**
	 * Create a new text WebSocket message from the given byte[]. It is assumed the
	 * byte array can be encoded into an UTF-8 String.
	 *
	 * @param payload the non-null payload
	 */
	public TextMessage(byte[] payload) {
		super(new String(payload, UTF_8));
		this.bytes = payload;
	}

	/**
	 * Create a new text WebSocket message with the given payload representing the
	 * full or partial message content. When the {@code isLast} boolean flag is set
	 * to {@code false} the message is sent as partial content and more partial
	 * messages will be expected until the boolean flag is set to {@code true}.
	 * @param payload the non-null payload
	 * @param isLast whether this the last part of a series of partial messages
	 */
	public TextMessage(CharSequence payload, boolean isLast) {
		super(payload.toString(), isLast);
		this.bytes = null;
	}


	@Override
	public int getPayloadLength() {
		return asBytes().length;
	}

	public byte[] asBytes() {
		return (this.bytes != null ? this.bytes : getPayload().getBytes(UTF_8));
	}

	@Override
	protected String toStringPayload() {
		return (getPayloadLength() > 10) ? getPayload().substring(0, 10) + ".." : getPayload();
	}

}
