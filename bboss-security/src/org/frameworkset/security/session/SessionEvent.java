package org.frameworkset.security.session;

public interface SessionEvent {
	public final int EventType_create = 0;
	public final int EventType_destroy = 1;
	public final int EventType_addAttibute = 2;
	public final int EventType_removeAttibute = 3;
	public Session getSource();
	public int getEventType();
	public String getAttributeName();
	public Object getAttributeValue();
}
