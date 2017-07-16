package com.bdsoft.datamin.fetch.douban;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdsoft.datamin.entity.DouFetchQueue;
import com.bdsoft.datamin.fetch.douban.book.FetchBook;
import com.bdsoft.datamin.fetch.douban.book.FetchBuyLink;
import com.bdsoft.datamin.fetch.douban.book.FetchReview;
import com.bdsoft.datamin.fetch.douban.book.FetchTag;
import com.bdsoft.datamin.fetch.douban.book.feed.BookFeed;
import com.bdsoft.datamin.fetch.douban.book.feed.BuyLinkFeed;
import com.bdsoft.datamin.fetch.douban.book.feed.ReviewFeed;
import com.bdsoft.datamin.fetch.douban.book.feed.ReviewsFeed;
import com.bdsoft.datamin.fetch.douban.book.support.DouJobObservable;
import com.bdsoft.datamin.fetch.douban.fang.FetchFang;
import com.bdsoft.datamin.service.IDouBookService;
import com.bdsoft.datamin.service.IDouBuylinkService;
import com.bdsoft.datamin.service.IDouFetchQueueService;
import com.bdsoft.datamin.service.IDouReviewService;
import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.JobUtil;
import com.bdsoft.datamin.util.StringUtil;
import com.bdsoft.datamin.util.exception.IpLimitedException;

public class DoubanController extends DouJobObservable {

	private static Logger log = LoggerFactory.getLogger(DoubanController.class);

	/*---------------------BookAction 类的构造初始化相关----------------------------*/
	public static DoubanController instance;

	private DoubanController() {
		super();
	}

	public synchronized static DoubanController getInstance() {
		if (instance == null) {
			instance = new DoubanController();
		}
		return instance;
	}

	// job抓取的各分类URL起始id
	private static Map<String, Long> fromIdMap = new ConcurrentHashMap<String, Long>();

	public static void setIDmap(String key, Long value) {
		fromIdMap.put(key, value);
	}

	public static Long getStartIdForFetch(String key) {
		if (fromIdMap.get(key) == null) {
			return 0L;
		}
		return fromIdMap.get(key);
	}

	// web方式启动，初始化抓取队列
	public static void init4web() {
		BDSpringUtil.getBean(IDouBookService.class).initBookFetchFrom();
		log.info("豆瓣图书抓取初始化完毕");
	}

	// 抓取图书，是否使用代理
	public static boolean BOOK_HOST_PROXY = true;
	// public static boolean BOOK_HOST_PROXY = false;

	/*---------------------BookAction 类的 抓取配置信息----------------------------*/
	// 每次抓取各分类数量
	public static Integer FETCH_BOOK_PERS = 8;
	public static Integer FETCH_BUYLINK_PERS = 8;
	public static Integer FETCH_REVIEWS_PERS = 8;
	public static Integer FETCH_REVIEWD_PERS = 8;
	public static Integer FETCH_TAG_PERS = 8;

	public static Long FETCH_SLEEP = 1L * 200; // 抓取间隔
	public static Long JOB_PAUSE = 1L * 1000 * 60 * 30;// 30分钟

	// 11-书的总评；12-书的单评；21-书的导购；31-推荐的书
	public static final String FETCH_REVIEWS = "11";
	public static final String FETCH_REVIEWD = "12";
	public static final String FETCH_BUYLINK = "21";
	public static final String FETCH_BOOK = "31";

	/*---------------------BookAction 类的 抓取业务----------------------------*/

	/**
	 * 测试，豆瓣图书相关抓取
	 */
	public void fetchTest(String url) {
		if (StringUtil.isEmpty(url)) {
			url = "http://book.douban.com/subject/11613687/";
		}
		int 中文 = 1;
		FetchBook fb = new FetchBook();
		FetchBuyLink fbl = new FetchBuyLink();
		FetchReview fr = new FetchReview();
		FetchTag ft = new FetchTag();
		FetchFang ff = new FetchFang();
		IDouBookService bookService = BDSpringUtil.getBean(IDouBookService.class);
		IDouFetchQueueService fetchQueueService = BDSpringUtil.getBean(IDouFetchQueueService.class);
		try {
			// 图书详情
			//			BookFeed feed = fb.fetch(url);
			//			bookService.saveBook(feed);

			// 图书评论分页链接
			//			BookFeed feed2 = fb.fetchBug_ReviewList(url);
			//			bookService.saveBug_ReviewList(null, feed2);

			// 新书推荐
			//			List<DouFetchQueue> latestBooks = fb.fetchLatest();
			//			fetchQueueService.saveBooks(latestBooks);

			// 排行榜
			//			List<DouFetchQueue> chartBooks = fb.fetchChart();
			//			fetchQueueService.saveBooks(chartBooks);

			// 导购
			//			List<BuyLinkFeed> feed3 = fbl.fetch(url);
			//			DouFetchQueue dfq = new DouFetchQueue();
			//			dfq.setId(426613700632248320L);
			//			dfq.setBookIsbn("9787508646480");
			//			BDSpringUtil.getBean(IDouBuylinkService.class).saveBuyLinks(dfq, feed3);

			// 评论列表
			//			DouFetchQueue dfq = new DouFetchQueue();
			//			dfq.setId(426613700644831232L);
			//			dfq.setBookIsbn("9787508646480");
			//			dfq.setBookName("支付战争");
			//			List<ReviewsFeed> feeds = fr.fetchReviewList(url);
			//			fetchQueueService.saveReviews(dfq, feeds);

			// 最好评论
//			List<DouFetchQueue> list = fb.bestReview();
//			fetchQueueService.saveBooks(list);
			
			// 评论详情
			//			ReviewFeed feed = fr.fetchReview(url);
			//			DouFetchQueue dfq = new DouFetchQueue();
			//			dfq.setId(426667754829905920L);
			//			dfq.setBookIsbn("9787508646480");
			//			dfq.setBookName("支付战争");
			//			BDSpringUtil.getBean(IDouReviewService.class).saveReview(dfq, feed);

			// 图书标签
			//			List<DouBookTag> tags = ft.fetchTags();
			//			BDSpringUtil.getBean(IDouBookTagService.class).saveTags(tags);

			// 标签下的图书
			//			DouBookTag dbt = new DouBookTag();
			//			dbt.setId(426910371975004160L);
			//			dbt.setTagName("互联网");
			//			dbt.setTagUrl("http://book.douban.com/tag/互联网");
			//			dbt.setFetchPage(2);
			//			List<DouFetchQueue> tmps = ft.fetchTagBook(dbt);
			//			BDSpringUtil.getBean(IDouFetchQueueService.class).saveBooks(tmps);
			//			dbt.setFetchStat((tmps != null && tmps.size() == 0) ? 3 : 2);
			//			dbt.setFetchPage((tmps != null) ? (dbt.getFetchPage() + 1) : dbt.getFetchPage());
			//			BDSpringUtil.getBean(IDouBookTagService.class).updateTag(dbt);

			// 租房信息
			//			Set<String> urlSet = ff.fetchGroups();
			//			Map<String,WwwFang> fangMap = BDSpringUtil.getBean(IWwwFangService.class).saveUrls(urlSet);
			//			ff.fetchFang(fangMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 扫库抓取图书
	 */
	public void fetchBooks() {
		// 1,加载未抓取地址
		List<DouFetchQueue> tmps;
		synchronized (this) {
			Long from = getStartIdForFetch(FETCH_BOOK);
			tmps = BDSpringUtil.getBean(IDouFetchQueueService.class).getUnFetchBook(FETCH_BOOK_PERS, from);

			if (tmps.size() == 0) {
				log.info("没有新书可抓，job暂停，返回");
				super.jobNeedPause(JobUtil.JOB_BOOK_ID);
				return;
			}
			// 更新下次抓取开始id
			DouFetchQueue dft = tmps.get(tmps.size() - 1);
			setIDmap(FETCH_BOOK, dft.getId());
		}

		// 2,开始抓取
		FetchBook fetch = new FetchBook();
		for (DouFetchQueue tmp : tmps) {
			try {
				// 正常抓取
				BookFeed feed = fetch.fetch(tmp.getFetchUrl());
				BDSpringUtil.getBean(IDouBookService.class).saveBook(tmp, feed);

				// bug：评论列表未抓取
				feed = fetch.fetchBug_ReviewList(tmp.getFetchUrl());
				BDSpringUtil.getBean(IDouBookService.class).saveBug_ReviewList(tmp, feed);
				Thread.sleep(FETCH_SLEEP);
			} catch (IpLimitedException ie) {
				log.info(ie.getMessage());
				super.jobNeedPause(JobUtil.JOB_BOOK_ID);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	// 扫库抓取购买链接
	public void fetchBuyLinks() {
		List<DouFetchQueue> tmps;
		synchronized (this) {
			Long from = getStartIdForFetch(FETCH_BUYLINK);
			tmps = BDSpringUtil.getBean(IDouFetchQueueService.class).getUnFetchBuyLink(FETCH_BUYLINK_PERS, from);
			if (tmps.size() == 0) {
				log.info("没有导购可抓，job暂停，返回");
				super.jobNeedPause(JobUtil.JOB_BUYLINK_ID);
				return;
			}
			DouFetchQueue dft = tmps.get(tmps.size() - 1);
			setIDmap(FETCH_BUYLINK, dft.getId());
		}

		FetchBuyLink fetch = new FetchBuyLink();
		for (DouFetchQueue tmp : tmps) {
			try {
				List<BuyLinkFeed> feeds = fetch.fetch(tmp.getFetchUrl());
				BDSpringUtil.getBean(IDouBuylinkService.class).saveBuyLinks(tmp, feeds);
				Thread.sleep(FETCH_SLEEP);
			} catch (IpLimitedException ie) {
				log.info(ie.getMessage());
				super.jobNeedPause(JobUtil.JOB_BUYLINK_ID);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	// 扫库抓取评论列表
	public void fetchReviews() {
		List<DouFetchQueue> tmps;
		synchronized (this) {
			Long from = getStartIdForFetch(FETCH_REVIEWS);
			tmps = BDSpringUtil.getBean(IDouFetchQueueService.class).getUnFetchReviews(FETCH_REVIEWS_PERS, from);
			if (tmps.size() == 0) {
				log.info("没有评论列表可抓，job暂停，返回");
				super.jobNeedPause(JobUtil.JOB_REVIEWS_ID);
				return;
			}
			DouFetchQueue dft = tmps.get(tmps.size() - 1);
			setIDmap(FETCH_REVIEWS, dft.getId());
		}

		FetchReview fetch = new FetchReview();
		for (DouFetchQueue tmp : tmps) {
			try {
				List<ReviewsFeed> feeds = fetch.fetchReviewList(tmp.getFetchUrl());
				BDSpringUtil.getBean(IDouFetchQueueService.class).saveReviews(tmp, feeds);
				Thread.sleep(FETCH_SLEEP);
			} catch (IpLimitedException ie) {
				log.info(ie.getMessage());
				super.jobNeedPause(JobUtil.JOB_REVIEWS_ID);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	// 抓取评论详情
	public void fetchReview() {
		List<DouFetchQueue> tmps;
		synchronized (this) {
			Long from = getStartIdForFetch(FETCH_REVIEWD);
			tmps = BDSpringUtil.getBean(IDouFetchQueueService.class).getUnFetchReview(FETCH_REVIEWD_PERS, from);
			if (tmps.size() == 0) {
				log.info("没有评论可抓，job暂停，返回");
				super.jobNeedPause(JobUtil.JOB_REVIEWD_ID);
				return;
			}
			DouFetchQueue dft = tmps.get(tmps.size() - 1);
			setIDmap(FETCH_REVIEWD, dft.getId());
		}

		FetchReview fetch = new FetchReview();
		for (DouFetchQueue tmp : tmps) {
			try {
				ReviewFeed feed = fetch.fetchReview(tmp.getFetchUrl());
				BDSpringUtil.getBean(IDouReviewService.class).saveReview(tmp, feed);
				Thread.sleep(FETCH_SLEEP);
			} catch (IpLimitedException ie) {
				log.info(ie.getMessage());
				super.jobNeedPause(JobUtil.JOB_REVIEWD_ID);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	// 抓取新书速递
	public void fetchLatestBooks() {
		FetchBook fetch = new FetchBook();
		try {
			List<DouFetchQueue> tmps = fetch.fetchLatest();
			BDSpringUtil.getBean(IDouFetchQueueService.class).saveBooks(tmps);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 抓取排行榜
	public void fetchChartBooks() {
		FetchBook fetch = new FetchBook();
		List<DouFetchQueue> tmps = fetch.fetchChart();
		BDSpringUtil.getBean(IDouFetchQueueService.class).saveBooks(tmps);
	}

}
