package org.frameworkset.web.tag;

public class LocaleTag extends RequestContextAwareTag{

	@Override
	protected int doStartTagInternal() throws Exception {
		writeMessage(this.getRequestContext().getLocaleName());
		return super.SKIP_BODY;
	}

}
