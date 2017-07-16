package com.bdsoft.datamin.fetch.douban.book.feed;

import java.util.HashMap;
import java.util.Map;

/**
 * 豆瓣图书详情页信息
 *
 * @author   丁辰叶
 * @date	 2016-5-17
 * @version  1.0.0
 */
public class BookFeed {

	private String url;
	private String name;
	private String pic;
	private Map<String, String> infoMap;
	private String rank;
	private String bookIntro;
	private String authorIntro;
	private String bookDir;
	private String tags;
	private Map<String, String> bookRec = new HashMap<String, String>();// 推荐图书
	private String buyLink;
	private String reviewLink;
	private int reviewCount;

	@Override
	public String toString() {
		return "图书Feed[书名=" + name + ", 评分=" + rank + ", 基本信息=" + infoMap + ", 简介=" + bookIntro + ", 标签=" + tags
				+ ", 推荐=" + bookRec.keySet() + "]";
	}

	public BookFeed() {
		super();
	}

	public BookFeed(String url) {
		this.url = url;
	}

	public BookFeed(String url, String name, String pic, Map<String, String> infoMap, String rank, String bookIntro,
			String authorIntro, String bookDir, String tags, Map<String, String> bookRec, String buyLink,
			String reviewLink) {
		super();
		this.url = url;
		this.name = name;
		this.pic = pic;
		this.infoMap = infoMap;
		this.rank = rank;
		this.bookIntro = bookIntro;
		this.authorIntro = authorIntro;
		this.bookDir = bookDir;
		this.tags = tags;
		this.bookRec = bookRec;
		this.buyLink = buyLink;
		this.reviewLink = reviewLink;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Map<String, String> getInfoMap() {
		return infoMap;
	}

	public void setInfoMap(Map<String, String> infoMap) {
		this.infoMap = infoMap;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getBookIntro() {
		return bookIntro;
	}

	public void setBookIntro(String bookIntro) {
		this.bookIntro = bookIntro;
	}

	public String getAuthorIntro() {
		return authorIntro;
	}

	public void setAuthorIntro(String authorIntro) {
		this.authorIntro = authorIntro;
	}

	public String getBookDir() {
		return bookDir;
	}

	public void setBookDir(String bookDir) {
		this.bookDir = bookDir;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Map<String, String> getBookRec() {
		return bookRec;
	}

	public void setBookRec(Map<String, String> bookRec) {
		this.bookRec = bookRec;
	}

	public String getBuyLink() {
		return buyLink;
	}

	public void setBuyLink(String buyLink) {
		this.buyLink = buyLink;
	}

	public String getReviewLink() {
		return reviewLink;
	}

	public void setReviewLink(String reviewLink) {
		this.reviewLink = reviewLink;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

}
