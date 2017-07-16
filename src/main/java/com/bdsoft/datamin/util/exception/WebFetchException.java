package com.bdsoft.datamin.util.exception;

/**
 * 自定义异常：抓取时出现网络问题
 * 
 * @author	丁辰叶
 * @date	2014-9-25
 */
public class WebFetchException extends Exception {

	private static final long serialVersionUID = 8398592528483483839L;

	public WebFetchException() {
		super();
	}

	public WebFetchException(String msg) {
		super(msg);
	}

}
