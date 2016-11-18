package org.frameworkset.util.encoder;

public class EncoderException
    extends IllegalStateException
{
    public EncoderException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EncoderException(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	public EncoderException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	private Throwable cause;

    EncoderException(String msg, Throwable cause)
    {
        super(msg);

        this.cause = cause;
    }

    public Throwable getCause()
    {
        return cause;
    }
}
