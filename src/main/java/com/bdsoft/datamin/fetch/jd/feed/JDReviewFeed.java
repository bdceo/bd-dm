package com.bdsoft.datamin.fetch.jd.feed;

import com.bdsoft.datamin.util.StringUtil;

/**
 * 从抓取的评论json中提取有用的评论信息
 * 
 * @author 丁辰叶
 * @date 2015-11-4
 */
public class JDReviewFeed {

	private String pid;// 商品唯一码
	private Long id;// 评论ID
	private int score;// 评分
	private String content;// 心得
	private String conTime;// 评论时间
	private String buyTime;// 购买时间

	private String uid;// 用户ID
	private JDUserFeed jdu;// 评论用户信息

	@Override
	public String toString() {
		int len = Math.min(content.length(), 50);
		String tmpContent = content.substring(0, len);
		return "评论 [" + id + ",评分=" + score + "," + tmpContent + ", " + jdu + "]";
	}

	public JDReviewFeed() {
		super();
	}

	public JDReviewFeed(String pid) {
		this.pid = pid;
	}

	public JDReviewFeed(String pid, JDUserFeed jdu, JDJsonComments json) {
		this.pid = pid;
		this.jdu = jdu;
		this.uid = jdu.getUid();
		this.id = json.getId();
		if (StringUtil.isEmpty(json.getScore())) {
			this.score = -1;
		} else {
			this.score = Integer.parseInt(json.getScore());
		}
		this.content = json.getContent().replaceAll("\\s*", "");
		this.conTime = json.getCreationTime();
		this.buyTime = json.getReferenceTime();
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Long getId() {
		return id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getScore() {
		return score;
	}

	public JDUserFeed getJdu() {
		return jdu;
	}

	public void setJdu(JDUserFeed jdu) {
		this.jdu = jdu;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getConTime() {
		return conTime;
	}

	public void setConTime(String conTime) {
		this.conTime = conTime;
	}

	public String getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}

}
