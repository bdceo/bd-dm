package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "dou_fetch_queue")
public class DouFetchQueue implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;

	private String bookName;
	private String bookIsbn;

	private Date fetchDate;

	private String fetchFlag;// 11-书的总评；12-书的单评；21-书的导购；31-推荐的书

	private String fetchStat;// 1-未抓取；2-抓取完毕；3-抓取失败

	private String fetchUrl;

	public String toString() {
		return "待抓：《" + this.bookName + "》 " + this.fetchUrl + "\n";
	}

	public DouFetchQueue() {
		super();
	}

	/**
	 * 
	 * @param name
	 *            书名
	 * @param isbn
	 *            isbn
	 * @param url
	 *            地址
	 * @param flag
	 *            类型
	 */
	public DouFetchQueue(String name, String isbn, String url, String flag) {
		this.fetchStat = "1";
		this.bookName = name;
		this.bookIsbn = isbn;
		this.fetchUrl = url;
		this.fetchFlag = flag;
	}

	public DouFetchQueue(String flag) {
		this.fetchFlag = flag;
		this.fetchStat = "1";
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBookName() {
		return this.bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public Date getFetchDate() {
		return this.fetchDate;
	}

	public void setFetchDate(Date fetchDate) {
		this.fetchDate = fetchDate;
	}

	public String getBookIsbn() {
		return bookIsbn;
	}

	public void setBookIsbn(String bookIsbn) {
		this.bookIsbn = bookIsbn;
	}

	public String getFetchFlag() {
		return this.fetchFlag;
	}

	public void setFetchFlag(String fetchFlag) {
		this.fetchFlag = fetchFlag;
	}

	public String getFetchStat() {
		return this.fetchStat;
	}

	public void setFetchStat(String fetchStat) {
		this.fetchStat = fetchStat;
	}

	public String getFetchUrl() {
		return this.fetchUrl;
	}

	public void setFetchUrl(String fetchUrl) {
		this.fetchUrl = fetchUrl;
	}

}