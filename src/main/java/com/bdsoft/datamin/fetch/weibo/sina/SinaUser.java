package com.bdsoft.datamin.fetch.weibo.sina;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bdsoft.datamin.entity.WeiboUser;
import com.bdsoft.datamin.fetch.weibo.feed.UserFeed;
import com.bdsoft.datamin.mapper.WeiboUserMapper;
import com.bdsoft.datamin.util.BDSpringUtil;

/**
 * 新浪微博-用户抓取
 * 
 */
public class SinaUser {

	public static Integer SUM = 0;

	public static Integer START = 1;
	public static String KEYWORDS = "HRD";
	public static Integer PAGE = 50;

	public static Integer SLEEP = 15;

	/**
	 * 手工下载后解析html抓取
	 * 
	 * @param page
	 *            总页数
	 */
	public static void fetchByHand(int page) {
		PAGE = page;
		List<UserFeed> feeds = new ArrayList<UserFeed>();
		try {
			for (int i = 1; i <= PAGE; i++) {
				System.out.println("第" + i + "页");
				feeds = FetchUser.pickByHand("hrd/" + i + ".htm");
				saveDB(feeds);
				feeds.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveDB(feeds);
			System.out.println("抓取" + SUM + "个新用户");
		}
	}

	/**
	 * 未登录抓取
	 * 
	 * @param key
	 * @param start
	 * @param page
	 */
	public static void fetch(String key, int start, int page) {
		KEYWORDS = key;
		// START = start;
		// PAGE = page;
		SUM = 0;
		List<UserFeed> feeds = new ArrayList<UserFeed>();
		try {
			for (int i = START; i <= PAGE; i++) {
				feeds = FetchUser.pick(KEYWORDS, i);
				if (feeds.size() == 0) {
					break;
				}
				saveDB(feeds);
				feeds.clear();
				if (i == PAGE) {
					break;
				}
				System.out.print("休眠倒计时-->");
				for (int j = SLEEP; j > 0; j--) {
					System.out.print(" " + j);
					Thread.sleep(1000);
				}
				System.out.println("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveDB(feeds);
			System.out.println("OVER！关键词{ " + KEYWORDS + " }，抓取 " + SUM + " 个新用户。");
		}
	}

	/**
	 * 登陆后抓取，利用httpclient
	 * 
	 * @param key
	 * @param client
	 */
	public static void fetchWithLogin(String key, DefaultHttpClient client) {
		KEYWORDS = key;
		SUM = 0;
		List<UserFeed> feeds = new ArrayList<UserFeed>();
		try {
			for (int i = START; i <= PAGE; i++) {
				// feeds = FetchUser.pick(KEYWORDS, i);
				feeds = FetchUser.pickWithLogin(KEYWORDS, i, client);
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
					System.out.print(" " + j);
					Thread.sleep(1000);
				}
				System.out.println("");
			}
		} catch (Exception e) {
			System.out.println("出错了：" + e.getMessage());
			e.printStackTrace();
		} finally {
			saveDB(feeds);
			System.out.println("OVER！关键词{ " + KEYWORDS + " }，抓取 " + SUM + " 个新用户。");
		}
	}

	/**
	 * 保存微博用户入库
	 * 
	 * @param feeds
	 */
	private static void saveDB(List<UserFeed> feeds) {
		if (feeds.size() == 0) {
			return;
		}
		WeiboUserMapper iwu = BDSpringUtil.getBean(WeiboUserMapper.class);
		int j = 0;
		for (UserFeed f : feeds) {
			WeiboUser tmp = new WeiboUser(f);
			System.out.println("用户：" + f.getUser());

			WeiboUser wu = new WeiboUser();
			wu.setUserName(f.getUser());
			List<WeiboUser> wus = iwu.selectList(new EntityWrapper<WeiboUser>(wu));
			if (wus != null && wus.size() > 0) {
				tmp.setId(wus.get(0).getId());
				tmp.setAddTime(new Date());
				iwu.updateSelectiveById(tmp);
				System.out.println("老用户已更新");
				continue;
			}
			iwu.insertSelective(tmp);
			System.out.println("新用户入库");
			j++;
			SUM++;
		}
		System.out.println("新抓取[ " + j + " ]个微博用户");
	}
}
