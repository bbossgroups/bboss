package com.frameworkset.util;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * "{\\"message\\":\\"{\\\"date\\\":\\\"2016 Jul  2 23:00:05\\\",\\\"router\\\":\\\"175.10.31.66\\\",\\\"program\\\":\\\"kt-macscan\\\",\\\"unixtime\\\":\\\"1467471612\\\",\\\"msg_kunteng\\\":\\\"hiwifi_ac#011{\\\\\"d\\\\\":[{\\\\\"rssi\\\\\":\\\\\"-83\\\\\",\\\\\"macaddr\\\\\":\\\\\"BC44864826C9\\\\\"},{\\\\\"rssi\\\\\":\\\\\"-77\\\\\",\\\\\"macaddr\\\\\":\\\\\"8C34FD3A02EE\\\\\"},{\\\\\"rssi\\\\\":\\\\\"-92\\\\\",\\\\\"macaddr\\\\\":\\\\\"64B47340ED5D\\\\\"},{\\\\\"rssi\\\\\":\\\\\"-88\\\\\",\\\\\"macaddr\\\\\":\\\\\"206E9CD7FFE5\\\\\"}],\\\\\"s\\\\\":\\\\\"scan\\\\\",\\\\\"v\\\\\":\\\\\"1\\\\\"} \\\\\"127.0.0.1\\\\\" \\\\\"60ACC8010598\\\\\"\\\"}\\",\\"@version\\":\\"1\\",\\"@timestamp\\":\\"2016-07-02T15:00:05.921Z\\",\\"path\\":\\"/data2/log/wifimsgnew/msg_20160702.log\\",\\"host\\":\\"dell\\"}"
 * @author biaoping.yin
 *
 */
public class LogstashMacMsg {
	private String message;
	@JsonProperty("@version")
	private String version;
	@JsonProperty("@timestamp")
	private String timestamp;
	private String path;
	private String host;
	public LogstashMacMsg() {
		// TODO Auto-generated constructor stub
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}

}
