package org.frameworkset.util.io;

import java.io.File;

public interface ResourceHandleListener<T> {
	public void startEvent(T  resource,File dest);
	public void handleDataEvent(T resource,File dest);
	public void endEvent(T  resource,File dest);

}
