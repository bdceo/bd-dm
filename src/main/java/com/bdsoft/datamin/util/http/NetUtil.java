package com.bdsoft.datamin.util.http;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.util.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.bdsoft.datamin.fetch.BDFetcher.NetMonitor;
import com.bdsoft.datamin.util.StringUtil;
import com.bdsoft.datamin.util.exception.IpLimitedException;
import com.bdsoft.datamin.util.exception.WebFetchException;

/**
 * 网络工具
 * 
 * @author 丁辰叶
 * @date 2016-5-6
 * @version 1.0.0
 */
public class NetUtil {

	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String CHARSET_GBK = "GBK";
	public static final String CHARSET_GB2312 = "GB2312";

	// 浏览器客户端代理
	public static final String BROWSER_UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36";
	public static final String BROWSER_ACCEPT = "*/*";

	public static void main(String[] args) throws Exception {
//		download(
//				"https://www.tsinova.com/system/product_pictures/pictureindices/53/medium/53.png?1464166157",
//				"d:/home/ddc/1.png");
//		
		String txt =  getHtmlSrc("https://s3.cn-north-1.amazonaws.com.cn/qa-juran-sheijijia-release-notes/versions.txt", "utf-8");
		System.out.println(txt);
	}

	public static void httpsDownload(String url, String store) {
		InputStream in = null;
		OutputStream out = null;
		HttpClient client = new DefaultHttpClient();
		try {
			SSLContext sslCtx = SSLContext.getInstance("SSL");
			sslCtx.init(null, new TrustManager[] { BDHttpUtil.X509_TM }, null);
			SSLSocketFactory sslSf = new SSLSocketFactory(sslCtx);
			ClientConnectionManager ccm = client.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, sslSf));

			HttpGet get = new HttpGet(url);
			HttpResponse res = client.execute(get);
			int code = res.getStatusLine().getStatusCode();
			if (code == 200) {
				in = res.getEntity().getContent();
				out = new FileOutputStream(new File(store));
				IOUtils.copy(in, out);
				out.flush();
				System.out.println("下载成功：" + url);
			} else {
				NetMonitor.log(code, url);
			}
		} catch (Exception e) {
			NetMonitor.log(e, url);
			System.out.println("下载出错：" + url);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			HttpClientUtils.closeQuietly(client);
		}
	}

	public static void download(String url, String store) {
		InputStream in = null;
		OutputStream out = null;
		try {
			System.out.println("开始下载：" + url);
			URL aurl = new URL(url);
			if (BDHttpUtil.isHttps(url)) {
				httpsDownload(url, store);
				return;
			}
			in = aurl.openStream();
			out = new FileOutputStream(new File(store));
			IOUtils.copy(in, out);
			out.flush();
			System.out.println("下载成功：" + url);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("下载出错：" + url);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception t) {
				t.printStackTrace();
			}
		}
	}

	/**
	 * 支持HTTP代理，抓取网页内容
	 * 
	 * @param url
	 *            地址
	 * @param charset
	 *            编码
	 * @param proxy
	 *            是否启动代理
	 * @return 网页源代码
	 */
	public static String getHtmlSrc(String url, String charset, boolean proxy) {
		// 启用代理，委托ProxyFactroy设置代理
		if (proxy) {
			ProxyFactory.getInstance().setProxy(url);
		}
		// 禁用代理
		else {
			System.setProperty("http.proxySet", "false");
		}
		return getHtmlSrc(url, charset);
	}

	/**
	 * 【GET】抓取网页内容 ，支持压缩
	 * 
	 * @param url
	 *            地址
	 * @param charset
	 *            编码，默认UTF-8
	 * @return 网页源代码
	 */
	public static String getHtmlSrc(String url, String charset) {
		if (StringUtil.isEmpty(charset)) {
			charset = CHARSET_UTF8;
		}
		InputStream in = null;
		try {
			HttpURLConnection http = (HttpURLConnection) new URL(url)
					.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(5 * 1000);
			http.setReadTimeout(10 * 1000);
			http.setRequestProperty("User-Agent", BROWSER_UA);
			http.setRequestProperty("Accept-Encoding", "gzip");// 启用http压缩

			int code = http.getResponseCode();
			if (code == 200) {
				in = http.getInputStream();
				String compress = http.getHeaderField("Content-Encoding");
				if (!StringUtil.isEmpty(compress)
						&& compress.indexOf("gzip") > -1) {
					in = new GZIPInputStream(http.getInputStream());
				}
				return new String(stream2byte(in), charset);
			} else if (code == 403) {
				throw new IpLimitedException(String.format("HTTP返回：%d，IP可能被封",
						code));
			} else {
				throw new WebFetchException(String.format("HTTP响应异常：%s,url=%s",
						code, url));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
			}
		}
		return null;
	}

	/**
	 * 【GET】抓取网页内容 ，支持压缩，自定义Http请求头
	 * 
	 * @param url
	 *            地址
	 * @param charset
	 *            编码，默认UTF-8
	 * @return 网页源代码
	 */
	public static String getHtmlSrc(String url, String charset,
			Map<String, String> header) {
		if (StringUtil.isEmpty(charset)) {
			charset = CHARSET_UTF8;
		}
		InputStream in = null;
		try {
			HttpURLConnection http = (HttpURLConnection) new URL(url)
					.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(5 * 1000);
			http.setReadTimeout(10 * 1000);
			http.setRequestProperty("User-Agent", BROWSER_UA);
			http.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");// 启用http压缩
			if (header != null && header.size() > 0) {
				for (Entry<String, String> en : header.entrySet()) {
					http.setRequestProperty(en.getKey(), en.getValue());
				}
			}

			int code = http.getResponseCode();
			if (code == 200) {
				in = http.getInputStream();
				String compress = http.getHeaderField("Content-Encoding");
				if (!StringUtil.isEmpty(compress)
						&& compress.indexOf("gzip") > -1) {
					in = new GZIPInputStream(http.getInputStream());
				}
				return new String(stream2byte(in), charset);
			} else if (code == 403) {
				throw new IpLimitedException(String.format("HTTP返回：%d，IP可能被封",
						code));
			} else {
				throw new WebFetchException(String.format("HTTP响应异常：%s,url=%s",
						code, url));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
			}
		}
		return null;
	}

	/**
	 * 【GET】抓取网页内容
	 * 
	 * @param url
	 *            地址
	 * @param http
	 *            ApacheHttpClient
	 * @return 网页源代码
	 * @throws Exception
	 */
	public static String getHtmlSrc(String url, HttpClient http)
			throws Exception {
		return http.execute(new HttpGet(url)).getEntity().toString();
	}

	/**
	 * 【POST】抓取网页内容
	 * 
	 * @param url
	 *            地址
	 * @param param
	 *            参数列表：a=xxx&b=xxx&……
	 * @param charset
	 *            编码
	 * @return 输入流
	 * @throws Exception
	 */
	public static InputStream getHtmlSrcByPost(String url, String param,
			String charset) {
		DataOutputStream outs = null;
		try {
			byte[] data = URLEncoder.encode(param, charset).getBytes();
			HttpURLConnection http = (HttpURLConnection) new URL(url)
					.openConnection();
			http.setConnectTimeout(10 * 1000);
			http.setReadTimeout(10 * 1000);
			http.setRequestMethod("POST");
			http.setDoOutput(true);// 发送POST请求必须设置允许输出
			http.setUseCaches(false);
			http.setRequestProperty("Connection", "Keep-Alive");
			http.setRequestProperty("Charset", charset);
			http.setRequestProperty("Content-Length",
					String.valueOf(data.length));
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setRequestProperty("User-Agent", BROWSER_UA);
			http.setRequestProperty("Accept-Encoding", "gzip");// 启用压缩

			// 发送请求并传递参数
			outs = new DataOutputStream(http.getOutputStream());
			outs.write(data);
			outs.flush();

			int code = http.getResponseCode();
			if (code == 200) {
				InputStream in = http.getInputStream();
				String compress = http.getHeaderField("Content-Encoding");
				if (!StringUtil.isEmpty(compress)
						&& compress.indexOf("gzip") > -1) {
					in = new GZIPInputStream(http.getInputStream());
				}
				return in;
			} else if (code == 403) {
				throw new IpLimitedException(String.format("HTTP返回：%d，IP可能被封",
						code));
			} else {
				throw new WebFetchException(String.format("HTTP响应异常：%s,url=%s",
						code, url));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outs != null) {
				try {
					outs.close();
				} catch (Exception e2) {
				}
			}
		}
		return null;
	}

	/**
	 * 【POST】抓取网页内容
	 * 
	 * @param url
	 *            地址
	 * @param param
	 *            参数列表：key#value
	 * @param charset
	 *            编码
	 * @return 输入流
	 */
	public static InputStream getHtmlSrcByPost(String url,
			Map<String, String> param, String charset) {
		DataOutputStream out = null;
		try {
			// 解析参数并编码
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : param.entrySet()) {
				sb.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), charset));
				sb.append("&");
			}
			byte[] data = sb.deleteCharAt(sb.length() - 1).toString()
					.getBytes();

			HttpURLConnection http = (HttpURLConnection) new URL(url)
					.openConnection();
			http.setConnectTimeout(10 * 1000);
			http.setReadTimeout(10 * 1000);
			http.setRequestMethod("POST");
			http.setDoOutput(true);// 发送POST请求必须设置允许输出
			http.setUseCaches(false);// 不适用cache
			http.setRequestProperty("Connection", "Keep-Alive");
			http.setRequestProperty("Charset", charset);
			http.setRequestProperty("Content-Length",
					String.valueOf(data.length));
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setRequestProperty("User-Agent", BROWSER_UA);
			http.setRequestProperty("Accept-Encoding", "gzip");// 启用压缩

			// 发送请求并传递参数
			out = new DataOutputStream(http.getOutputStream());
			out.write(data);
			out.flush();

			int code = http.getResponseCode();
			if (code == 200) {
				InputStream in = http.getInputStream();
				String compress = http.getHeaderField("Content-Encoding");
				if (!StringUtil.isEmpty(compress)
						&& compress.indexOf("gzip") > -1) {
					in = new GZIPInputStream(http.getInputStream());
				}
				return in;
			} else if (code == 403) {
				throw new IpLimitedException(String.format("HTTP返回：%d，IP可能被封",
						code));
			} else {
				throw new WebFetchException(String.format("HTTP响应异常：%s,url=%s",
						code, url));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e2) {
				}
			}
		}
		return null;
	}

	/**
	 * 【GET】抓取网页内容，带回Jsoup-doc对象
	 * 
	 * @param url
	 *            地址
	 * @param charset
	 *            字符集，默认UTF-8
	 * @return Jsoup-doc对象
	 */
	public static Document getJsoupDocByGet(String url, String charset) {
		if (StringUtil.isEmpty(charset)) {
			charset = CHARSET_UTF8;
		}
		InputStream in = null;
		try {
			HttpURLConnection http = (HttpURLConnection) new URL(url)
					.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(5 * 1000);
			http.setReadTimeout(10 * 1000);
			http.setRequestProperty("User-Agent", BROWSER_UA);
			http.setRequestProperty("Accept-Encoding", "gzip");// 启用http压缩

			int code = http.getResponseCode();
			if (code == 200) {
				in = http.getInputStream();
				String compress = http.getHeaderField("Content-Encoding");
				if (!StringUtil.isEmpty(compress)
						&& compress.indexOf("gzip") > -1) {
					in = new GZIPInputStream(http.getInputStream());
				}
				return Jsoup.parse(in, charset, url);
			} else if (code == 403) {
				throw new IpLimitedException(String.format("HTTP返回：%d，IP可能被封",
						code));
			} else {
				throw new WebFetchException(String.format("HTTP响应异常：%s,url=%s",
						code, url));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
			}
		}
		return null;
	}

	/**
	 * 【POST】抓取网页内容，带回Jsoup-doc对象
	 * 
	 * @param url
	 *            地址
	 * @param charset
	 *            字符集，默认UTF-8
	 * @param param
	 *            参数：key#value
	 * @param header
	 *            请求头：key#value
	 * @return Jsoup-doc对象
	 */
	public static Document getJsoupDocByPost(String url, String charset,
			Map<String, String> param, Map<String, String> header) {
		DataOutputStream out = null;
		InputStream in = null;
		try {
			// 解析参数列并编码
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : param.entrySet()) {
				sb.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), charset));
				sb.append("&");
			}
			byte[] params = sb.deleteCharAt(sb.length() - 1).toString()
					.getBytes();

			HttpURLConnection http = (HttpURLConnection) new URL(url)
					.openConnection();
			http.setConnectTimeout(5 * 1000);
			http.setReadTimeout(10 * 1000);
			http.setRequestMethod("POST");
			http.setDoOutput(true);// 发送POST请求必须设置允许输出
			http.setUseCaches(false);
			http.setRequestProperty("Connection", "Keep-Alive");
			http.setRequestProperty("Charset", charset);
			http.setRequestProperty("Content-Length",
					String.valueOf(params.length)); // 参数长度
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setRequestProperty("User-Agent", BROWSER_UA);
			http.setRequestProperty("Accept-Encoding", "gzip");// 启用压缩

			// 设置额外的http头信息
			if (header != null && header.size() > 0) {
				for (Entry<String, String> en : header.entrySet()) {
					http.setRequestProperty(en.getKey(), en.getValue());
				}
			}

			out = new DataOutputStream(http.getOutputStream());
			out.write(params);
			out.flush();

			int code = http.getResponseCode();
			if (code == 200) {
				in = http.getInputStream();
				String compress = http.getHeaderField("Content-Encoding");
				if (!StringUtil.isEmpty(compress)
						&& compress.indexOf("gzip") > -1) {
					in = new GZIPInputStream(http.getInputStream());
				}
				return Jsoup.parse(in, charset, url);
			} else if (code == 403) {
				throw new IpLimitedException(String.format("HTTP返回：%d，IP可能被封",
						code));
			} else {
				throw new WebFetchException(String.format("HTTP响应异常：%s,url=%s",
						code, url));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	/**
	 * 将输入流转成元数据(byte[])
	 * 
	 * @param in
	 *            输入流，请在方法外释放
	 * @return 元数据
	 */
	public static byte[] stream2byte(InputStream in) {
		int len = -1;
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream outs = null;
		try {
			outs = new ByteArrayOutputStream();
			while ((len = in.read(buffer)) != -1) {
				outs.write(buffer, 0, len);
			}
			outs.flush();
			return outs.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outs != null) {
				try {
					outs.close();
				} catch (Exception e2) {
				}
			}
		}
		return null;
	}

	/**
	 * 将输入流转换成字符串
	 * 
	 * @param in
	 *            输入流
	 * @param charset
	 *            编码
	 * @return 字符串
	 */
	public static String stream2string(InputStream in, String charset)
			throws Exception {
		return new String(stream2byte(in), charset);
	}

}
