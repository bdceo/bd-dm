package com.bdsoft.datamin.util.http;

public class ProxyEnt {

	private String key; // 唯一标示

	private String host; // 代理域名
	private int port = 80; // 代理端口，默认80

	private boolean auth = false; // 验证，默认不需要验证
	private String authUser; // 用户名
	private String authPwd; // 密码

	private int proxyOK = 1;// 1-ok，0-bad，默认ok

	public ProxyEnt() {
	}

	public ProxyEnt(String host) {
		this.host = host;
	}

	public ProxyEnt(String host, String port) {
		this(host, Integer.parseInt(port));
	}

	public ProxyEnt(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public String toString() {
		return "ProxyEnt [host=" + host + ", port=" + port + ", proxyOK="
				+ proxyOK + "]";
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	public String getAuthUser() {
		return authUser;
	}

	public void setAuthUser(String authUser) {
		this.authUser = authUser;
	}

	public String getAuthPwd() {
		return authPwd;
	}

	public void setAuthPwd(String authPwd) {
		this.authPwd = authPwd;
	}

	public int getProxyOK() {
		return proxyOK;
	}

	public void setProxyOK(int proxyOK) {
		this.proxyOK = proxyOK;
	}

}
