package com.bdsoft.datamin.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdsoft.datamin.entity.DouBook;
import com.bdsoft.datamin.entity.DouFetchQueue;
import com.bdsoft.datamin.fetch.douban.DoubanController;
import com.bdsoft.datamin.fetch.douban.book.feed.BookFeed;
import com.bdsoft.datamin.mapper.DouBookMapper;
import com.bdsoft.datamin.mapper.DouFetchQueueMapper;
import com.bdsoft.datamin.service.IDouBookService;

/**
 * 豆瓣图书
 *
 * @author   丁辰叶
 * @date	 2016-5-17
 * @version  1.0.0
 */
@Service
public class DouBookServiceImp implements IDouBookService {

	private static Logger log = LoggerFactory.getLogger(DouBookServiceImp.class);

	@Autowired
	private DouFetchQueueMapper idftd;
	@Autowired
	private DouBookMapper idbd;

	public boolean saveBook(BookFeed feed) {
		return saveBook(null, feed);
	}

	public boolean saveBug_ReviewList(DouFetchQueue fetchTmp, BookFeed feed) {
		boolean flag = false;

		if (feed != null) {
			flag = true;
			// 保存评论列表-待抓取
			if (feed.getReviewCount() > 25) {
				int pg = feed.getReviewCount() / 25 + 1;
				for (int i = 1; i < pg; i++) {
					String reviewUrl = feed.getReviewLink() + "?score=&start=" + (i * 25);
					DouFetchQueue tmp = new DouFetchQueue();
					tmp.setFetchUrl(reviewUrl);
					tmp = idftd.selectOne(tmp);
					if (tmp == null) {
						tmp = new DouFetchQueue(fetchTmp != null ? fetchTmp.getBookName() : "", "", reviewUrl,
								DoubanController.FETCH_REVIEWS);
						tmp.setFetchFlag(DoubanController.FETCH_REVIEWS);
						flag = idftd.insertSelective(tmp) > 0;
					}
				}
			}
		}

		// 更新待抓取状态
		if (fetchTmp != null) {
			fetchTmp.setFetchStat(flag ? "2" : "3");
			fetchTmp.setFetchDate(new Date());
			flag = idftd.updateSelectiveById(fetchTmp) > 0;
		}
		return flag;
	}

	public boolean saveBook(DouFetchQueue fetchTmp, BookFeed feed) {
		boolean flag = false;
		if (feed == null) {
			return flag;
		}

		String isbn = feed.getInfoMap().get("ISBN");
		if (StringUtils.isEmpty(isbn)) {
			log.info("BookFeed提取isbn空");
			return flag;
		}

		DouBook book = new DouBook();
		book.setBookIsbn(isbn);
		book = idbd.selectOne(book);
		if (book == null) {
			// 保存图书信息
			book = new DouBook(feed);
			flag = idbd.insertSelective(book) > 0;
		}

		// 保存购买链接-待抓取
		DouFetchQueue tmp = new DouFetchQueue();
		tmp.setFetchUrl(feed.getBuyLink());
		tmp = idftd.selectOne(tmp);
		if (tmp == null) {
			tmp = new DouFetchQueue(book.getBookName(), book.getBookIsbn(), feed.getBuyLink(), DoubanController.FETCH_BUYLINK);
			flag = idftd.insertSelective(tmp) > 0;
		}
		// 保存评论列表-待抓取
		tmp = new DouFetchQueue();
		tmp.setFetchUrl(feed.getReviewLink());
		tmp = idftd.selectOne(tmp);
		if (tmp == null) {
			tmp = new DouFetchQueue(book.getBookName(), book.getBookIsbn(), feed.getReviewLink(),
					DoubanController.FETCH_REVIEWS);
			flag = idftd.insertSelective(tmp) > 0;
		}
		if (feed.getReviewCount() > 25) {
			int pg = feed.getReviewCount() / 25 + 1;
			for (int i = 1; i < pg; i++) {
				String reviewUrl = feed.getReviewLink() + "?score=&start=" + (i * 25);
				tmp = new DouFetchQueue();
				tmp.setFetchUrl(reviewUrl);
				tmp = idftd.selectOne(tmp);
				if (tmp == null) {
					tmp = new DouFetchQueue(book.getBookName(), book.getBookIsbn(), reviewUrl, DoubanController.FETCH_REVIEWS);
					flag = idftd.insertSelective(tmp) > 0;
				}
			}
		}

		// 保存推荐图书-待抓取
		int size = 0;
		for (Entry<String, String> en : feed.getBookRec().entrySet()) {
			tmp = new DouFetchQueue();
			tmp.setFetchUrl(en.getValue());
			tmp = idftd.selectOne(tmp);
			if (tmp != null) {
				continue;
			}
			// 电影推荐，不抓取
			if (en.getValue().contains("movie.douban")) {
				continue;
			}
			tmp = new DouFetchQueue(en.getKey(), "", en.getValue(), DoubanController.FETCH_BOOK);
			flag = idftd.insertSelective(tmp) > 0;
			size++;
		}
		log.info("推荐新书：" + feed.getBookRec().size() + "/" + size);

		// 更新待抓取状态
		if (fetchTmp != null) {
			fetchTmp.setFetchStat(flag ? "2" : "3");
			fetchTmp.setFetchDate(new Date());
			idftd.updateSelectiveById(fetchTmp);
		}

		return flag;
	}

	public void initBookFetchFrom() {
		List<DouFetchQueue> dfts = idftd.selectBookUrl4init();
		for (DouFetchQueue row : dfts) {
			DoubanController.setIDmap(row.getFetchFlag(), row.getId());
		}
	}

}