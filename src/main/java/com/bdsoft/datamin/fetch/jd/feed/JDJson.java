package com.bdsoft.datamin.fetch.jd.feed;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装用于发送Ajax请求，返回的JSON格式数据定义
 * 
 * @author bdceo
 * 
 */
public class JDJson {

	// 括号标示，用于从包含json的文本中截取安全的json
	public static String BRACKET_1L = "(";
	public static String BRACKET_1R = ")";
	public static String BRACKET_2L = "[";
	public static String BRACKET_2R = "]";
	public static String BRACKET_3L = "{";
	public static String BRACKET_3R = "}";

	private String id;// 商品ID，
	private String p;// 京东价
	private String m;// 参考价

	private String url;

	private String vid; // 店铺ID
	private String vender;// 店铺卖主

	private String companyName;
	private String firstAddr;
	private String secAddr;

	// 商品评论统计
	private JDJsonProductComment productCommentSummary;
	// 评论集合
	private List<JDJsonComments> comments = new ArrayList<JDJsonComments>();

	public JDJson() {
		super();
	}

	@Override
	public String toString() {
		return "JDJson [id=" + id + ", p=" + p + ", m=" + m + ", url=" + url
				+ ", vid=" + vid + ", vender=" + vender + ", companyName="
				+ companyName + ", firstAddr=" + firstAddr + ", secAddr="
				+ secAddr + ", comments=" + comments + "]";
	}

	// 格式化处理京东返回的json数据
	public static String subSafeJson(String str, String subFlag) {
		int fi = str.indexOf(subFlag);
		int li = str.lastIndexOf(subFlag);
		if (subFlag.equals(BRACKET_2L)) {
			li = str.lastIndexOf(BRACKET_2R);
		} else if (subFlag.equals(BRACKET_1L)) {
			li = str.lastIndexOf(BRACKET_1R);
		}
		if (str.contains(subFlag) && (fi != li)) {
			str = str.substring(fi + 1, li);
		}
		return str;
	}

	public String getId() {
		return id;
	}

	public List<JDJsonComments> getComments() {
		return comments;
	}

	public void setComments(List<JDJsonComments> jDJsonComments) {
		this.comments = jDJsonComments;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JDJsonProductComment getProductCommentSummary() {
		return productCommentSummary;
	}

	public void setProductCommentSummary(JDJsonProductComment productCommentSummary) {
		this.productCommentSummary = productCommentSummary;
	}

	public String getP() {
		return p;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getVender() {
		return vender;
	}

	public void setVender(String vender) {
		this.vender = vender;
	}

	public void setP(String p) {
		this.p = p;
	}

	public String getM() {
		return m;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFirstAddr() {
		return firstAddr;
	}

	public void setFirstAddr(String firstAddr) {
		this.firstAddr = firstAddr;
	}

	public String getSecAddr() {
		return secAddr;
	}

	public void setSecAddr(String secAddr) {
		this.secAddr = secAddr;
	}

	public void setM(String m) {
		this.m = m;
	}
}