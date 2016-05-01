package org.frameworkset.web.socket.inf;

import java.nio.ByteBuffer;

public class BinaryMessage extends AbstractWebSocketMessage<ByteBuffer> {


	/**
	 * Create a new binary WebSocket message with the given ByteBuffer payload.
	 * @param payload the non-null payload
	 */
	public BinaryMessage(ByteBuffer payload) {
		super(payload, true);
	}

	/**
	 * Create a new binary WebSocket message with the given payload representing the
	 * full or partial message content. When the {@code isLast} boolean flag is set
	 * to {@code false} the message is sent as partial content and more partial
	 * messages will be expected until the boolean flag is set to {@code true}.
	 * @param payload the non-null payload
	 * @param isLast if the message is the last of a series of partial messages
	 */
	public BinaryMessage(ByteBuffer payload, boolean isLast) {
		super(payload, isLast);
	}

	/**
	 * Create a new binary WebSocket message with the given byte[] payload.
	 * @param payload a non-null payload; note that this value is not copied so care
	 * must be taken not to modify the array.
	 */
	public BinaryMessage(byte[] payload) {
		this(payload, true);
	}

	/**
	 * Create a new binary WebSocket message with the given byte[] payload representing
	 * the full or partial message content. When the {@code isLast} boolean flag is set
	 * to {@code false} the message is sent as partial content and more partial
	 * messages will be expected until the boolean flag is set to {@code true}.
	 * @param payload a non-null payload; note that this value is not copied so care
	 * must be taken not to modify the array.
	 * @param isLast if the message is the last of a series of partial messages
	 */
	public BinaryMessage(byte[] payload, boolean isLast) {
		this(payload, 0, ((payload == null) ? 0 : payload.length), isLast);
	}

	/**
	 * Create a new binary WebSocket message by wrapping an existing byte array.
	 * @param payload a non-null payload; note that this value is not copied so care
	 * must be taken not to modify the array.
	 * @param offset the offset into the array where the payload starts
	 * @param length the length of the array considered for the payload
	 * @param isLast if the message is the last of a series of partial messages
	 */
	public BinaryMessage(byte[] payload, int offset, int length, boolean isLast) {
		super((payload != null) ? ByteBuffer.wrap(payload, offset, length) : null, isLast);
	}


	@Override
	public int getPayloadLength() {
		return getPayload().remaining();
	}

	@Override
	protected String toStringPayload() {
		return getPayload().toString();
	}

}
