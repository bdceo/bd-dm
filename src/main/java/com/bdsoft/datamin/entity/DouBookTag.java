package com.bdsoft.datamin.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bdsoft.datamin.fetch.douban.book.FetchTag;

@TableName(value = "dou_book_tag")
public class DouBookTag implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = -1323945804269077739L;

	@TableId
	private Long id;

	private String tagName;

	private String tagUrl;

	private Integer tagBooks;

	private Integer fetchStat;// 1-未抓取；2-抓取中；3-抓取完

	private Integer fetchPage;

	public String toString() {
		return "\n标签：" + tagName + "，状态：" + fetchStat + "，书：" + tagBooks;
	}

	public DouBookTag() {
	}

	public DouBookTag(String name, int count) {
		this.tagName = name;
		this.tagUrl = FetchTag.TAG_URL + name;
		this.tagBooks = count;
		this.fetchStat = 1;
		this.fetchPage = 0;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagUrl() {
		return tagUrl;
	}

	public void setTagUrl(String tagUrl) {
		this.tagUrl = tagUrl;
	}

	public Integer getTagBooks() {
		return tagBooks;
	}

	public void setTagBooks(Integer tagBooks) {
		this.tagBooks = tagBooks;
	}

	public Integer getFetchStat() {
		return fetchStat;
	}

	public void setFetchStat(Integer fetchStat) {
		this.fetchStat = fetchStat;
	}

	public Integer getFetchPage() {
		return fetchPage;
	}

	public void setFetchPage(Integer fetchPage) {
		this.fetchPage = fetchPage;
	}

}