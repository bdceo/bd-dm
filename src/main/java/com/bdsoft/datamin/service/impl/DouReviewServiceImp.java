package com.bdsoft.datamin.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdsoft.datamin.entity.DouFetchQueue;
import com.bdsoft.datamin.entity.DouReview;
import com.bdsoft.datamin.entity.DouUser;
import com.bdsoft.datamin.fetch.douban.book.feed.ReviewFeed;
import com.bdsoft.datamin.mapper.DouFetchQueueMapper;
import com.bdsoft.datamin.mapper.DouReviewMapper;
import com.bdsoft.datamin.mapper.DouUserMapper;
import com.bdsoft.datamin.service.IDouReviewService;
import com.bdsoft.datamin.util.StringUtil;

/**
 * 图书评论详情
 *
 * @author   丁辰叶
 * @date	 2016-5-17
 * @version  1.0.0
 */
@Service
public class DouReviewServiceImp implements IDouReviewService {

	private static Logger log = LoggerFactory.getLogger(DouReviewServiceImp.class);

	@Autowired
	private DouReviewMapper idrd;
	@Autowired
	private DouUserMapper idud;
	@Autowired
	private DouFetchQueueMapper idftd;

	public boolean saveReview(DouFetchQueue fetchTmp, ReviewFeed feed) {
		boolean flag = false;

		fetchTmp.setFetchDate(new Date());
		if (feed == null) {// 网络错误
			fetchTmp.setFetchStat("3");
			flag = idftd.updateSelectiveById(fetchTmp) > 0;
		} else if (StringUtil.isEmpty(feed.getTitle())) {// 被和谐或删除
			fetchTmp.setFetchStat("2");
			flag = idftd.updateSelectiveById(fetchTmp) > 0;
		} else {// 正常
			// 1,保存用户

			DouUser user = new DouUser();
			user.setDouHome(feed.getUserHome());
			user = idud.selectOne(user);
			if (user == null) {
				user = new DouUser(feed);
				flag = idud.insertSelective(user) > 0;
			}

			// 2,保存评论
			DouReview review = new DouReview(feed, user, fetchTmp);
			try {
				fetchTmp.setFetchDate(new Date());
				flag = idrd.insertSelective(review) > 0;

				// 3,更新待抓取
				fetchTmp.setFetchStat((flag ? "2" : "3"));
			} catch (Exception e) {
				e.printStackTrace();
				fetchTmp.setFetchStat("3");
			} finally {
				flag = idftd.updateSelectiveById(fetchTmp) > 0;
			}
		}
		return flag;
	}
}