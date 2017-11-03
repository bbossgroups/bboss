package org.frameworkset.soa;

/**
 * 序列化plugin工厂，应用自定义自己的序列化插件列表，自动加载
 */
public interface PluginFactory {
	public String[] getPlugins();
}
