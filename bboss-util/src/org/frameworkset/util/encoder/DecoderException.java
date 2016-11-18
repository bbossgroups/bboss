package org.frameworkset.util.encoder;

public class DecoderException
    extends IllegalStateException
{
    public DecoderException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DecoderException(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	public DecoderException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	private Throwable cause;

    DecoderException(String msg, Throwable cause)
    {
        super(msg);

        this.cause = cause;
    }

    public Throwable getCause()
    {
        return cause;
    }
}
