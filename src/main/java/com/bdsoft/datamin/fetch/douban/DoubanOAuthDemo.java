package com.bdsoft.datamin.fetch.douban;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gdata.client.douban.DoubanService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.util.ServiceException;

/**
 * 网络Google-code例子实现
 * @author bdceo
 *
 */
public class DoubanOAuthDemo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
//		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		String title = "豆瓣API - OAuth认证  @ Java";
		String requestToken = request.getParameter("oauth_token");

		out.println("<h1>" + title + "</h1>");
		String apiKey = "01b11c39ad702886051354ad08507992";
		String secret = "dc5c5df1518f6b66";

		out.println("<html>");
		out.println("<head>");
		out.println("<title>" + title + "</title>");
		out.println("</head>");
		out.println("<body bgcolor=\"white\">");

		// 1,获取未授权的Request_Token
		DoubanService myService = new DoubanService("subApplication", apiKey,
				secret);

		if (requestToken != null) {
			// 3，使用授权后的Request_Token换取Access_Token
			out.println(requestToken);
			try {
				Cookie[] cookies = request.getCookies();
				if (cookies == null) {
					out.println("request token secret not found in cookie");
					return;
				}
				Cookie c = cookies[0];
				// set request token and token secret
				myService.setRequestTokenSecret(c.getValue());
				myService.setRequestToken(requestToken);

				// 4，使用Access_Token访问或修改受保护资源
				myService.getAccessToken();

				myService.createSaying(new PlainTextConstruct(title));

				out.println("<br/> OK，恭喜你已经验证通过</br>你已经发了一条说说到豆瓣,赶快看看去吧。");
			} catch (IOException e) {
				out.println("Oops! networking error!");
				e.printStackTrace();
			} catch (ServiceException e) {
				out.println("Oops! wrong request token!");
				e.printStackTrace();
			}

		} else {
			// 2，请求用户授权Request_Token
			out.println(request.getRequestURL());
			out.println("<br/>");
			// step1 : generate authorization url and set the callback url to
			// the current url
			out.println("<a href="
					+ myService.getAuthorizationUrl(request.getRequestURL()
							.toString()) + "> 点击前往豆瓣进行应用授权.</a>");
			// put request secret in cookie
			Cookie c = new Cookie("secret", myService.getRequestTokenSecret());
			response.addCookie(c);
		}

		out.println("</body>");
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
