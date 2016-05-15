package org.frameworkset.http;

import java.io.IOException;
import java.io.OutputStream;

public interface StreamingHttpOutputMessage extends HttpOutputMessage {

	/**
	 * Set the streaming body for this message.
	 * @param body the streaming body
	 */
	void setBody(Body body);


	/**
	 * Defines the contract for bodies that can be written directly to an {@link OutputStream}.
	 * It is useful with HTTP client libraries that provide indirect access to an
	 * {@link OutputStream} via a callback mechanism.
	 */
	interface Body {

		/**
		 * Write this body to the given {@link OutputStream}.
		 * @param outputStream the output stream to write to
		 * @throws IOException in case of errors
		 */
		void writeTo(OutputStream outputStream) throws IOException;
	}

}
