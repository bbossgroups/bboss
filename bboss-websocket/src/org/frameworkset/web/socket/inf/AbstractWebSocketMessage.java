package org.frameworkset.web.socket.inf;

import org.frameworkset.util.Assert;
import org.frameworkset.util.ObjectUtils;

public abstract class AbstractWebSocketMessage<T> implements WebSocketMessage<T> {

	private final T payload;

	private final boolean last;


	/**
	 * Create a new WebSocket message with the given payload.
	 * @param payload the non-null payload
	 */
	AbstractWebSocketMessage(T payload) {
		this(payload, true);
	}

	/**
	 * Create a new WebSocket message given payload representing the full or partial
	 * message content. When the {@code isLast} boolean flag is set to {@code false}
	 * the message is sent as partial content and more partial messages will be
	 * expected until the boolean flag is set to {@code true}.
	 * @param payload the non-null payload
	 * @param isLast if the message is the last of a series of partial messages
	 */
	AbstractWebSocketMessage(T payload, boolean isLast) {
		Assert.notNull(payload, "payload must not be null");
		this.payload = payload;
		this.last = isLast;
	}


	/**
	 * Return the message payload, never be {@code null}.
	 */
	public T getPayload() {
		return this.payload;
	}

	/**
	 * Whether this is the last part of a message sent as a series of partial messages.
	 */
	public boolean isLast() {
		return this.last;
	}

	@Override
	public int hashCode() {
		return AbstractWebSocketMessage.class.hashCode() * 13 + ObjectUtils.nullSafeHashCode(this.payload);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AbstractWebSocketMessage)) {
			return false;
		}
		AbstractWebSocketMessage<?> otherMessage = (AbstractWebSocketMessage<?>) other;
		return ObjectUtils.nullSafeEquals(this.payload, otherMessage.payload);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " payload= " + toStringPayload()
				+ ", byteCount=" + getPayloadLength() + ", last=" + isLast() + "]";
	}

	protected abstract String toStringPayload();

}
