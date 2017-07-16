package com.bdsoft.datamin.fetch.weibo.qq.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bdsoft.datamin.entity.WeiboFetch;
import com.bdsoft.datamin.entity.WeiboUser;
import com.bdsoft.datamin.fetch.weibo.feed.WeiboFeed;
import com.bdsoft.datamin.fetch.weibo.qq.FetchWeibo;
import com.bdsoft.datamin.fetch.www.FetchFilter;
import com.bdsoft.datamin.mapper.WeiboFetchMapper;
import com.bdsoft.datamin.mapper.WeiboUserMapper;
import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.StringUtil;

/**
 * 腾讯微博抓取
 * 
 */
public class QQWeibo {

	public static Integer SUM = 0;

	public static Integer START = 1;
	public static String KEYWORDS = "简历  邮箱";
	public static Integer PAGE = 0;

	public static Integer SLEEP = 15;
	public static String FILE_TYPE = ".txt";

	public static String FETCH_SRC = "t.qq.com";

	/**
	 * 页面抓取，入库
	 * 
	 * @param key
	 * @param start
	 * @param page
	 */
	public static void fetch(String key, DefaultHttpClient client) throws Exception {
		KEYWORDS = key;
		PAGE = 50;
		SUM = 0;
		List<WeiboFeed> feeds = new ArrayList<WeiboFeed>();
		try {
			for (int i = START; i <= PAGE; i++) {
				feeds = FetchWeibo.pick(KEYWORDS, i, client);
				if (feeds.size() == 0) {
					break;
				}
				saveDB(feeds);
				feeds.clear();
				if (i == PAGE) {
					break;
				}
				System.out.print("分页倒计时-->");
				for (int j = SLEEP; j > 0; j--) {
					System.out.print(j + " ");
					Thread.sleep(1000);
				}
				System.out.println("");
			}
		} catch (Exception e) {
			System.out.println("出错了：" + e.getMessage());
			e.printStackTrace();
			if (e.getMessage().equals("ip")) {
				throw new Exception(e.getMessage());
			}
		} finally {
			saveDB(feeds);
			System.out.println("关键词【" + KEYWORDS + "】共抓取" + SUM + "个新邮箱");
		}
	}

	/**
	 * 保存招聘微博入库
	 * 
	 * @param feeds
	 */
	public static void saveDB(List<WeiboFeed> feeds) {
		if (feeds.size() == 0) {
			return;
		}
		WeiboFetchMapper iwf = BDSpringUtil.getBean(WeiboFetchMapper.class);
		WeiboUserMapper iwu = BDSpringUtil.getBean(WeiboUserMapper.class);
		int j = 0;
		for (int i = 0; i < feeds.size(); i++) {
			WeiboFeed feed = feeds.get(i);
			// 解析获取邮箱
			String mail = FetchFilter.pickEmail(feed.toString());
			if (mail.trim().length() == 0) {
				continue;
			}
			feed.set_mail(mail);
			System.out.println("邮箱：" + mail);

			// 只保存非猎头发布的微博招聘信息
			WeiboFetch wf = new WeiboFetch(KEYWORDS, FETCH_SRC, feed);
			if (!FetchFilter.getInst().isHunter(feed.toString())) {
				// 判断邮箱重复
				WeiboFetch wfs = new WeiboFetch();
				wfs.setFetchMail(mail);
				List<WeiboFetch> tmp = iwf.selectList(new EntityWrapper<WeiboFetch>(wfs));
				if (tmp != null && tmp.size() > 0) {
					WeiboFetch fetch = tmp.get(0);
					// 导入较早的邮箱，没有微博内容，更新继续
					if (StringUtil.isEmpty(fetch.getWeibo())) {
						wf.setId(fetch.getId());
						iwf.updateSelectiveById(wf);
						System.out.println("\t老邮箱，更新微博内容");
					}
				} else {
					// 新邮箱新微博入库
					iwf.insertSelective(wf);
					j++;
					SUM++;
				}
			}

			// 微博用户入库
			// List<WeiboUser> wus = iwu.findByProperty("userName", feed
			// .getUserName());
			WeiboUser wu = new WeiboUser();
			wu.setUserHome(feed.getUserHome());
			List<WeiboUser> wus = iwu.selectList(new EntityWrapper<WeiboUser>(wu));
			if (wus != null && wus.size() > 0) {
				continue;
			}
			wu = new WeiboUser(wf);
			iwu.insertSelective(wu);
		}
		System.out.println("新抓取[" + j + "]个邮箱");
	}

	/**
	 * 导出新email到txt
	 */
	public static void exportDB() {
		WeiboFetchMapper imw = BDSpringUtil.getBean(WeiboFetchMapper.class);
		List<WeiboFetch> mws = imw.selectTodayFetch();
		StringBuffer sb = new StringBuffer();
		for (WeiboFetch m : mws) {
			sb.append(m.getFetchMail().replaceAll(";", "\n"));
			sb.append("\n");
		}

		String file = BDFileUtil.getClassPath() + new Date().getTime() + FILE_TYPE;
		System.out.println(file);
		BDFileUtil.writeFile(file, sb.toString(), true);
	}

}
