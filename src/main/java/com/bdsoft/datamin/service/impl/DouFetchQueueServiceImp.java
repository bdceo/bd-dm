package com.bdsoft.datamin.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdsoft.datamin.entity.DouFetchQueue;
import com.bdsoft.datamin.fetch.douban.DoubanController;
import com.bdsoft.datamin.fetch.douban.book.feed.ReviewsFeed;
import com.bdsoft.datamin.mapper.DouFetchQueueMapper;
import com.bdsoft.datamin.service.IDouFetchQueueService;

/**
 * 豆瓣待抓取url队列
 *
 * @author   丁辰叶
 * @date	 2016-5-17
 * @version  1.0.0
 */
@Service
public class DouFetchQueueServiceImp implements IDouFetchQueueService {

	private static Logger log = LoggerFactory.getLogger(DouBookServiceImp.class);

	@Autowired
	private DouFetchQueueMapper idftd;

	public List<DouFetchQueue> getUnFetchBuyLink(int size, Long from) {
		return idftd.selectUnfetchObjlinks(from, DoubanController.FETCH_BUYLINK, size);
	}

	public List<DouFetchQueue> getUnFetchReviews(int size, Long from) {
		return idftd.selectUnfetchObjlinks(from, DoubanController.FETCH_REVIEWS, size);
	}

	public List<DouFetchQueue> getUnFetchReview(int size, Long from) {
		return idftd.selectUnfetchObjlinks(from, DoubanController.FETCH_REVIEWD, size);
	}

	public List<DouFetchQueue> getUnFetchBook(int size, Long from) {
		return idftd.selectUnfetchObjlinks(from, DoubanController.FETCH_BOOK, size);
	}

	public boolean saveBooks(List<DouFetchQueue> bookTmps) {
		for (DouFetchQueue tmp : bookTmps) {
			DouFetchQueue bak = new DouFetchQueue();
			bak.setFetchUrl(tmp.getFetchUrl());
			bak = idftd.selectOne(bak);
			if (bak != null) {
				log.info(bak.getBookName() + "\t库中已存在");
				continue;
			}
			idftd.insertSelective(tmp);
			log.info("入库 " + tmp.toString());
		}
		return true;
	}

	public boolean saveReviews(DouFetchQueue fetchTmp, List<ReviewsFeed> feeds) {
		boolean flag = false;
		if (feeds != null) {
			if (feeds.size() == 0) {
				flag = true;
			} else {
				for (ReviewsFeed feed : feeds) {
					// 电影评论不抓取
					if (feed.getUrl().contains("movie.douban")) {
						continue;
					}
					DouFetchQueue tmp = new DouFetchQueue();
					tmp.setFetchUrl(feed.getUrl());
					tmp = idftd.selectOne(tmp);
					if (tmp == null) {
						tmp = new DouFetchQueue(fetchTmp.getBookName(), fetchTmp.getBookIsbn(), feed.getUrl(),
								DoubanController.FETCH_REVIEWD);
						flag = idftd.insertSelective(tmp) > 0;
						if (!flag) {
							log.info("保存出错：" + feed);
							break;
						}
					} else {
						flag = true;
					}
				}
			}
		} else {// ip被封
			flag = false;
		}

		// 更新待抓取状态
		fetchTmp.setFetchDate(new Date());
		fetchTmp.setFetchStat((flag ? "2" : "3"));
		flag = idftd.updateSelectiveById(fetchTmp) > 0;

		return flag;
	}
}