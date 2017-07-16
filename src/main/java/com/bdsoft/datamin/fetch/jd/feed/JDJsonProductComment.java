package com.bdsoft.datamin.fetch.jd.feed;

public class JDJsonProductComment {

	private String skuid;// 商品id
	private int score1Count;// 1分评论总数
	private int score2Count;
	private int score3Count;
	private int score4Count;
	private int score5Count;
	private int commentCount;// 总给评论数

	public String getSkuid() {
		return skuid;
	}

	public void setSkuid(String skuid) {
		this.skuid = skuid;
	}

	public int getScore1Count() {
		return score1Count;
	}

	public void setScore1Count(int score1Count) {
		this.score1Count = score1Count;
	}

	public int getScore2Count() {
		return score2Count;
	}

	public void setScore2Count(int score2Count) {
		this.score2Count = score2Count;
	}

	public int getScore3Count() {
		return score3Count;
	}

	public void setScore3Count(int score3Count) {
		this.score3Count = score3Count;
	}

	public int getScore4Count() {
		return score4Count;
	}

	public void setScore4Count(int score4Count) {
		this.score4Count = score4Count;
	}

	public int getScore5Count() {
		return score5Count;
	}

	public void setScore5Count(int score5Count) {
		this.score5Count = score5Count;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

}
