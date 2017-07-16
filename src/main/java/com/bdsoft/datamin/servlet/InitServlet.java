package com.bdsoft.datamin.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdsoft.datamin.fetch.douban.DoubanController;
import com.bdsoft.datamin.util.http.ProxyFactory;

public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(InitServlet.class);

	public InitServlet() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		DoubanController.init4web();
		ProxyFactory.getInstance();

		log.info("初始化完毕");
	}

	public void destroy() {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}