package com.bdsoft.datamin.fetch.douban.book.feed;

public class ReviewsFeed {

	private String title;
	private String url;

	@Override
	public String toString() {
		return "待抓评论Feed[主题=" + title + "]";
	}

	public ReviewsFeed(String title, String url) {
		super();
		this.title = title;
		this.url = url;
	}

	public ReviewsFeed() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
