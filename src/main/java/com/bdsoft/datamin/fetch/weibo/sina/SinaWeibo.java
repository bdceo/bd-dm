package com.bdsoft.datamin.fetch.weibo.sina;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bdsoft.datamin.entity.WeiboFetch;
import com.bdsoft.datamin.entity.WeiboUser;
import com.bdsoft.datamin.fetch.weibo.feed.WeiboFeed;
import com.bdsoft.datamin.fetch.www.FetchFilter;
import com.bdsoft.datamin.mapper.WeiboFetchMapper;
import com.bdsoft.datamin.mapper.WeiboUserMapper;
import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.StringUtil;

/**
 * 新浪微博抓取
 * 
 */
public class SinaWeibo {

	public static Integer SUM = 0;

	public static Integer START = 1;
	public static String KEYWORDS = "简历  邮箱";
	public static Integer PAGE = 50;

	public static Integer SLEEP = 15;
	public static String FILE_TYPE = ".txt";

	public static String FETCH_SRC = "s.weibo.com";

	/**
	 * 页面抓取，入库
	 * 
	 * @param key
	 * @param start
	 * @param page
	 */
	public static void fetch(String key, int start, int page) throws Exception {
		KEYWORDS = key;
		// START = start;
		// PAGE = page;
		SUM = 0;
		List<WeiboFeed> feeds = new ArrayList<WeiboFeed>();
		try {
			for (int i = START; i <= PAGE; i++) {
				feeds = FetchWeibo.pick(KEYWORDS, i);
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

			WeiboUser wuc = new WeiboUser();
			wuc.setUserHome(feed.getUserHome());
			List<WeiboUser> wus = iwu.selectList(new EntityWrapper<WeiboUser>(wuc));
			if (wus != null && wus.size() > 0) {
				continue;
			}
			WeiboUser wu = new WeiboUser(wf);
			try {
				iwu.insertSelective(wu);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				continue;
			}
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

	/**
	 * 导入之前未入库的mail到DB
	 */
	public static void importAll() {
		WeiboFetchMapper imw = BDSpringUtil.getBean(WeiboFetchMapper.class);
		File f = new File(BDFileUtil.getClassPath() + "all_re.txt");
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				WeiboFetch wf = new WeiboFetch();
				wf.setFetchMail(line);
				List<WeiboFetch> tmp = imw.selectList(new EntityWrapper<WeiboFetch>(wf));
				System.out.println(line);
				if (tmp != null && tmp.size() > 0) {
					continue;
				} else {
					WeiboFetch mw = new WeiboFetch(line, FETCH_SRC);
					imw.insertSelective(mw);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析已有微博的用户类型
	 */
	public static void parseUser() {
		WeiboFetchMapper imw = BDSpringUtil.getBean(WeiboFetchMapper.class);
		WeiboUserMapper iwu = BDSpringUtil.getBean(WeiboUserMapper.class);
		try {
			List<WeiboFetch> wfs = imw.selectList(new EntityWrapper<WeiboFetch>(new WeiboFetch()));
			for (WeiboFetch wf : wfs) {
				System.out.println(wf.getId() + "_" + wf.getWeiboUser() + "_" + wf.getWeibo());
				if (StringUtil.isEmpty(wf.getWeibo())) {
					continue;
				}
				WeiboUser wuc = new WeiboUser();
				wuc.setUserMail(wf.getFetchMail());
				List<WeiboUser> wus = iwu.selectList(new EntityWrapper<WeiboUser>(wuc));
				if (wus != null && wus.size() > 0) {
					continue;
				}
				WeiboUser wu = new WeiboUser(wf);
				iwu.insertSelective(wu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新数据库表回车换行符
	 */
	public static void updateRN() {
		WeiboUserMapper imw = BDSpringUtil.getBean(WeiboUserMapper.class);
		try {
			List<WeiboUser> wfs = imw.selectList(new EntityWrapper<WeiboUser>(new WeiboUser()));
			for (WeiboUser wf : wfs) {
				System.out.println("前" + wf.getUserMail());
				wf.setUserMail(wf.getUserMail().trim());
				imw.updateSelectiveById(wf);
				System.out.println("后" + wf.getUserMail());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * IWeiboUserDao iwu = LogicFactory.getWeiboUserDaoInstance();
	 * List<WeiboUser> wus = iwu.findAll(); for(int i=0;i<wus.size();i++){
	 * WeiboUser u = wus.get(i); String mail = u.getUserMail();
	 * System.out.print(u.getId()); System.out.println("前："+mail);
	 * 
	 * String[] ms = mail.split("\n"); StringBuffer sb = new
	 * StringBuffer(ms[0]); for(int j=1; j<ms.length; j++ ){ sb.append(";");
	 * sb.append(ms[j]); } u.setUserMail(sb.toString()); iwu.update(u);
	 * 
	 * mail = u.getUserMail(); System.out.print(u.getId());
	 * System.out.println("后："+mail); }
	 */
}
