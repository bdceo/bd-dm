package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Map;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bdsoft.datamin.fetch.douban.book.feed.BookFeed;
import com.bdsoft.datamin.util.StringUtil;

@TableName(value = "dou_book")
public class DouBook implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;

	private String authorInfo;

	private String bookAuthor;

	private String bookCatalog;

	private String bookInfo;

	private String bookIsbn;

	private String bookName;

	private String bookNameEn;

	private String bookPic;

	private String bookRank;

	private String bookSerial;

	private String bookTag;

	private String bookTranslator;

	private String douUrl;

	private String packed;

	private Integer pages;

	private String price;
	private String pubYear;

	private String publisher;

	@Override
	public String toString() {
		return "DouBook [authorInfo=" + authorInfo + ", bookAuthor=" + bookAuthor + ", bookCatalog=" + bookCatalog
				+ ", bookInfo=" + bookInfo + ", bookIsbn=" + bookIsbn + ", bookName=" + bookName + ", bookNameEn="
				+ bookNameEn + ", bookPic=" + bookPic + ", bookRank=" + bookRank + ", bookSerial=" + bookSerial
				+ ", bookTag=" + bookTag + ", bookTranslator=" + bookTranslator + ", douUrl=" + douUrl + ", packed="
				+ packed + ", pages=" + pages + ", price=" + price + ", pubYear=" + pubYear + ", publisher="
				+ publisher + "]";
	}

	public DouBook() {
		super();
	}

	public DouBook(BookFeed feed) {
		this.bookName = feed.getName();
		this.bookPic = feed.getPic();
		this.bookInfo = feed.getBookIntro();
		this.authorInfo = feed.getAuthorIntro();
		this.bookCatalog = feed.getBookDir();
		this.bookRank = feed.getRank();
		this.bookTag = feed.getTags();
		this.douUrl = feed.getUrl();
		// 图书基本信息
		Map<String, String> info = feed.getInfoMap();
		this.bookNameEn = info.get("原作名");
		this.bookAuthor = info.get("作者");
		this.bookTranslator = info.get("译者");
		this.publisher = info.get("出版社");
		this.pubYear = info.get("出版年");
		if (!StringUtil.isEmpty(info.get("页数"))) {
			try {
				this.pages = Integer.parseInt(info.get("页数"));
			} catch (Exception e) {
				this.pages = 0;
			}
		}
		if (!StringUtil.isEmpty(info.get("定价"))) {
			try {
				this.price = info.get("定价");
			} catch (Exception e) {
				this.price = "0.0";
			}
		}
		this.packed = info.get("装帧");
		this.bookSerial = info.get("丛书");
		this.bookIsbn = info.get("ISBN");
	}

	public DouBook(String authorInfo, String bookAuthor, String bookCatalog, String bookInfo, String bookIsbn,
			String bookName, String bookNameEn, String bookPic, String bookRank, String bookSerial, String bookTag,
			String bookTranslator, String douUrl, String packed, int pages, String price, String pubYear,
			String publisher) {
		this.authorInfo = authorInfo;
		this.bookAuthor = bookAuthor;
		this.bookCatalog = bookCatalog;
		this.bookInfo = bookInfo;
		this.bookIsbn = bookIsbn;
		this.bookName = bookName;
		this.bookNameEn = bookNameEn;
		this.bookPic = bookPic;
		this.bookRank = bookRank;
		this.bookSerial = bookSerial;
		this.bookTag = bookTag;
		this.bookTranslator = bookTranslator;
		this.douUrl = douUrl;
		this.packed = packed;
		this.pages = pages;
		this.price = price;
		this.pubYear = pubYear;
		this.publisher = publisher;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthorInfo() {
		return this.authorInfo;
	}

	public void setAuthorInfo(String authorInfo) {
		this.authorInfo = authorInfo;
	}

	public String getBookAuthor() {
		return this.bookAuthor;
	}

	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	public String getBookCatalog() {
		return this.bookCatalog;
	}

	public void setBookCatalog(String bookCatalog) {
		this.bookCatalog = bookCatalog;
	}

	public String getBookInfo() {
		return this.bookInfo;
	}

	public void setBookInfo(String bookInfo) {
		this.bookInfo = bookInfo;
	}

	public String getBookIsbn() {
		return this.bookIsbn;
	}

	public void setBookIsbn(String bookIsbn) {
		this.bookIsbn = bookIsbn;
	}

	public String getBookName() {
		return this.bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookNameEn() {
		return this.bookNameEn;
	}

	public void setBookNameEn(String bookNameEn) {
		this.bookNameEn = bookNameEn;
	}

	public String getBookPic() {
		return this.bookPic;
	}

	public void setBookPic(String bookPic) {
		this.bookPic = bookPic;
	}

	public String getBookRank() {
		return this.bookRank;
	}

	public void setBookRank(String bookRank) {
		this.bookRank = bookRank;
	}

	public String getBookSerial() {
		return this.bookSerial;
	}

	public void setBookSerial(String bookSerial) {
		this.bookSerial = bookSerial;
	}

	public String getBookTag() {
		return this.bookTag;
	}

	public void setBookTag(String bookTag) {
		this.bookTag = bookTag;
	}

	public String getBookTranslator() {
		return this.bookTranslator;
	}

	public void setBookTranslator(String bookTranslator) {
		this.bookTranslator = bookTranslator;
	}

	public String getDouUrl() {
		return this.douUrl;
	}

	public void setDouUrl(String douUrl) {
		this.douUrl = douUrl;
	}

	public String getPacked() {
		return this.packed;
	}

	public void setPacked(String packed) {
		this.packed = packed;
	}

	public Integer getPages() {
		return this.pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public String getPrice() {
		return this.price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPubYear() {
		return this.pubYear;
	}

	public void setPubYear(String pubYear) {
		this.pubYear = pubYear;
	}

	public String getPublisher() {
		return this.publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

}