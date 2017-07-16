package com.bdsoft.datamin.fetch.douban.book.feed;

/**
 * 图书导购页
 *
 * @author   丁辰叶
 * @date	 2016-5-17
 * @version  1.0.0
 */
public class BuyLinkFeed {

	private String bookstore;// 商家
	private String storeUrl;// 购买链接
	private String storePrice;
	private String savePrice;

	public BuyLinkFeed() {
		super();
	}

	@Override
	public String toString() {
		return "导购Feed[商家=" + bookstore + ",价格=" + storePrice + "，节省=" + savePrice + ",地址=" + storeUrl + "]";
	}

	public BuyLinkFeed(String bookUrl, String bookstore, String storeUrl, String storePrice, String savePrice) {
		super();
		this.bookstore = bookstore;
		this.storeUrl = storeUrl;
		this.storePrice = storePrice;
		this.savePrice = savePrice;
	}

	public String getBookstore() {
		return bookstore;
	}

	public void setBookstore(String bookstore) {
		this.bookstore = bookstore;
	}

	public String getStoreUrl() {
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}

	public String getStorePrice() {
		return storePrice;
	}

	public void setStorePrice(String storePrice) {
		this.storePrice = storePrice;
	}

	public String getSavePrice() {
		return savePrice;
	}

	public void setSavePrice(String savePrice) {
		this.savePrice = savePrice;
	}

}
