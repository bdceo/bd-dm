package com.bdsoft.datamin.util.exception;

/**
 * 自定义异常：抓取时疑似IP被封
 * 
 * @author	丁辰叶
 * @date	2012-9-25
 */
public class IpLimitedException extends RuntimeException {

	private static final long serialVersionUID = -3261418501622903631L;

	public IpLimitedException() {
		super();
	}

	public IpLimitedException(String msg) {
		super(msg);
	}

}
