package org.frameworkset.web.socket.sockjs;

import org.frameworkset.util.Assert;

public abstract class AbstractSockJsMessageCodec  implements SockJsMessageCodec {

	@Override
	public String encode(String... messages) {
		Assert.notNull(messages, "messages must not be null");
		StringBuilder sb = new StringBuilder();
		sb.append("a[");
		for (int i=0; i < messages.length; i++) {
			sb.append('"');
			char[] quotedChars = applyJsonQuoting(messages[i]);
			sb.append(escapeSockJsSpecialChars(quotedChars));
			sb.append('"');
            if (i < messages.length - 1) {
                sb.append(',');
            }
		}
		sb.append(']');
		return sb.toString();
	}

	/**
	 * Apply standard JSON string quoting (see http://www.json.org/).
	 */
	protected abstract char[] applyJsonQuoting(String content);

	/**
	 * See "JSON Unicode Encoding" section of SockJS protocol.
	 */
	private String escapeSockJsSpecialChars(char[] characters) {
		StringBuilder result = new StringBuilder();
		for (char c : characters) {
			if (isSockJsSpecialChar(c)) {
				result.append('\\').append('u');
				String hex = Integer.toHexString(c).toLowerCase();
				for (int i = 0; i < (4 - hex.length()); i++) {
					result.append('0');
				}
				result.append(hex);
			}
			else {
				result.append(c);
			}
		}
		return result.toString();
	}

	/**
	 * See `escapable_by_server` variable in the SockJS protocol test suite.
	 */
	private boolean isSockJsSpecialChar(char ch) {
		return (ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u200C' && ch <= '\u200F') ||
				(ch >= '\u2028' && ch <= '\u202F') || (ch >= '\u2060' && ch <= '\u206F') ||
				(ch >= '\uFFF0' && ch <= '\uFFFF') || (ch >= '\uD800' && ch <= '\uDFFF');
	}

}
