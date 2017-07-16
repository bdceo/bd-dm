package com.bdsoft.datamin.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdsoft.datamin.entity.DouBook;
import com.bdsoft.datamin.entity.DouBuylink;
import com.bdsoft.datamin.entity.DouFetchQueue;
import com.bdsoft.datamin.fetch.douban.book.feed.BuyLinkFeed;
import com.bdsoft.datamin.mapper.DouBookMapper;
import com.bdsoft.datamin.mapper.DouBuylinkMapper;
import com.bdsoft.datamin.mapper.DouFetchQueueMapper;
import com.bdsoft.datamin.service.IDouBuylinkService;

/**
 * 图书导购
 *
 * @author   丁辰叶
 * @date	 2016-5-17
 * @version  1.0.0
 */
@Service
public class DouBuylinkServiceImp implements IDouBuylinkService {

	private Logger log = LoggerFactory.getLogger(DouBuylinkServiceImp.class);

	@Autowired
	private DouFetchQueueMapper idftd;
	@Autowired
	private DouBookMapper idbd;
	@Autowired
	private DouBuylinkMapper idbld;

	public boolean saveBuyLinks(DouFetchQueue fetchTmp, List<BuyLinkFeed> feeds) {
		boolean flag = false;

		DouBook book = new DouBook();
		book.setBookIsbn(fetchTmp.getBookIsbn());
		book = idbd.selectOne(book);
		if (book == null) {
			log.info("isbn码未查到");
			return flag;
		}
		if (feeds.size() == 0) {
			flag = true;
		}

		// 保存图书购买链接
		for (BuyLinkFeed feed : feeds) {
			DouBuylink link = new DouBuylink();
			link.setStoreUrl(feed.getStoreUrl());
			link = idbld.selectOne(link);
			if (link == null) {
				link = new DouBuylink(feed, fetchTmp.getBookIsbn());
				flag = idbld.insertSelective(link) > 0;
				if (!flag) {
					log.info("保存出错：" + feed);
					break;
				}
			} else {
				flag = true;
			}
		}

		// 更新待抓取状态
		fetchTmp.setFetchDate(new Date());
		fetchTmp.setFetchStat((flag ? "2" : "3"));
		flag = idftd.updateSelectiveById(fetchTmp) > 0;

		return flag;
	}
}