package com.bdsoft.datamin.fetch.douban.book.feed;

public class ReviewFeed {

	private String title;
	private String date;
	private String user;
	private String userHome;
	private String review;

	@Override
	public String toString() {
		int size = review.length();
		size = size > 20 ? 20 : size;
		String rev = review.substring(0, size) + "……";
		return "评论Feed[标题=" + title + ", 内容=" + rev + ", 日期=" + date + "]";
	}

	public ReviewFeed(String title, String date, String user, String userHome,
			String review) {
		super();
		this.title = title;
		this.date = date;
		this.user = user;
		this.userHome = userHome;
		this.review = review;
	}

	public ReviewFeed() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUserHome() {
		return userHome;
	}

	public void setUserHome(String userHome) {
		this.userHome = userHome;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

}
