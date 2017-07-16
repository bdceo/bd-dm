package com.bdsoft.datamin.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bdsoft.datamin.fetch.douban.book.feed.BuyLinkFeed;

@TableName(value = "dou_buylink")
public class DouBuylink implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;

	private String bookIsbn;

	private String bookstore;

	private String savePrice;

	private String storePrice;

	private String storeUrl;

	public DouBuylink() {
		super();
	}

	public DouBuylink(BuyLinkFeed feed, String isbn) {
		this.bookstore = feed.getBookstore();
		this.storeUrl = feed.getStoreUrl();
		this.storePrice = feed.getStorePrice();
		this.savePrice = feed.getSavePrice();
		this.bookIsbn = isbn;
	}


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBookIsbn() {
		return this.bookIsbn;
	}

	public void setBookIsbn(String bookIsbn) {
		this.bookIsbn = bookIsbn;
	}

	public String getBookstore() {
		return this.bookstore;
	}

	public void setBookstore(String bookstore) {
		this.bookstore = bookstore;
	}

	public String getSavePrice() {
		return this.savePrice;
	}

	public void setSavePrice(String savePrice) {
		this.savePrice = savePrice;
	}

	public String getStorePrice() {
		return this.storePrice;
	}

	public void setStorePrice(String storePrice) {
		this.storePrice = storePrice;
	}

	public String getStoreUrl() {
		return this.storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}

}