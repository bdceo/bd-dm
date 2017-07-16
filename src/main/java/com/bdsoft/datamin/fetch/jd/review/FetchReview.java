package com.bdsoft.datamin.fetch.jd.review;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.bdsoft.datamin.entity.JDProduct;
import com.bdsoft.datamin.entity.JDReviews;
import com.bdsoft.datamin.entity.JDUser;
import com.bdsoft.datamin.entity.ReviewsExt;
import com.bdsoft.datamin.entity.UserExt;
import com.bdsoft.datamin.fetch.jd.JdUtil;
import com.bdsoft.datamin.fetch.jd.feed.JDJson;
import com.bdsoft.datamin.fetch.jd.feed.JDJsonComments;
import com.bdsoft.datamin.fetch.jd.feed.JDReviewFeed;
import com.bdsoft.datamin.fetch.jd.feed.JDUserFeed;
import com.bdsoft.datamin.fetch.jd.product.FetchProduct;
import com.bdsoft.datamin.mapper.JDProductMapper;
import com.bdsoft.datamin.mapper.JDReviewsMapper;
import com.bdsoft.datamin.mapper.JDUserMapper;
import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.DateUtil;
import com.bdsoft.datamin.util.StringUtil;
import com.bdsoft.datamin.util.http.NetUtil;
import com.google.gson.Gson;

public class FetchReview {

	// 请求时间间隔
	private static long THREAD_REVS_SLEEP = 250 * 1L;
	public static int RLIST_PAGESIZE = 10; // 评论列表页，每页多少条

	// 评论URL
	// 参数 s-评论类型：[s-0:全部；s-1:差评；s-2:中评；s-3:好评；s-4:晒单]
	private static final String REPLACE_ID = "#ID#";
	private static final String REPLACE_PAGE = "#PAGE#";
	private static final String REPLACE_B = "#B#";
	private static final String REPLACE_NOW = "#NOW#";
	private static final String REPLACE_VER = "#VER#";
	// http://s.club.jd.com/productpage/p-1685716-s-0-t-3-p-6.html?callback=fetchJSON_comment98vv111
	private static final String URL_4_REVIEW = "http://club.jd.com/productpage/p-#ID#-s-0-t-3-p-#PAGE#.html?callback=jsonp#B#&_=#NOW#";
	// private static final String URL_4_REVIEW =
	// "http://s.club.jd.com/productpage/p-#ID#-s-0-t-3-p-#PAGE#.html?callback=fetchJSON_comment98vv#VER#";

	// 抓取评论数据，特定http头部分
	public static Map<String, String> header = new HashMap<String, String>();

	private static JDProductMapper jdProductDao;
	private static JDReviewsMapper jdReviewDao;
	private static JDUserMapper jdUserDao;

	static {
		header.put("Accept", "*/*");
		header.put("Host", "s.club.jd.com");
		header.put("Connection", "keep-alive");

		BDSpringUtil.init();
		jdProductDao = BDSpringUtil.getBean(JDProductMapper.class);
		jdUserDao = BDSpringUtil.getBean(JDUserMapper.class);
		jdReviewDao = BDSpringUtil.getBean(JDReviewsMapper.class);
	}

	/**
	 * 抓取评论及用户信息
	 */
	public static void main(String[] args) throws Exception {
		Set<String> urls = new HashSet<String>();
		urls.add("http://item.jd.com/268661.html");// 下架
		urls.add("http://item.jd.com/1685716.html");// 上架
		urls.add("http://item.jd.com/11143153.html");// 书
		urls.add("http://item.jd.com/1583901383.html");// 商户

		for (String url : urls) {
			Date s = new Date();
			new FetchReview().getReviews(url);
			DateUtil.cost("抓取评论", s);
		}
	}

	/**
	 * 分页获取商品所有评论
	 * 
	 * @param url
	 *            商品地址
	 */
	public void getReviews(String url) {
		System.out.println("抓取评论：" + url);

		// 加载商品信息，提取评论总数
		JDProduct product = new FetchProduct().getProduct(url);
		int count = getReviewCount(url);

		int mod = count % RLIST_PAGESIZE;
		int shang = count / RLIST_PAGESIZE;
		int pages = (mod == 0) ? shang : shang + 1;// 总页数

		this.fetch(product, 0, pages);
	}

	/**
	 * 供抓取队列调用入口，执行分页抓取
	 * 
	 * @param jdp
	 *            抓取商品
	 * @param start
	 *            起始页
	 * @param end
	 *            结束页
	 */
	public void fetch(JDProduct jdp, int start, int end) {
		String pid = jdp.getPid();
		String url = jdp.getPurl();

		// 每页保存的评论信息
		List<JDReviewFeed> reviews = null;
		// 遍历抓取评论时去重用
		Set<Long> rids = new HashSet<Long>();
		// 抓取失败的分页码
		Set<Integer> errPgs = new HashSet<Integer>();

		int repc = 0;// 检测可能重复抓取数
		int pages = end;
		System.out.println("评论总页：" + pages);

		for (int i = start; i < end; i++) {
			System.out.println(String.format("抓取页 ：%d/%d", (i + 1), pages));
			reviews = new ArrayList<JDReviewFeed>();
			JDJson json = getReviews(url, i);
			if (json == null) {
				errPgs.add(i);
				continue;
			}
			// 评论空，继续下一页
			if (json.getComments().size() == 0) {
				System.out.println("\t暂无评论，抓取下一页");
				continue;
			}
			// 解析提取
			takeReviews(json, pid, reviews, rids);
			int res = reviews.size();
			if (res > 0) {
				// 入库
				int suc = saveReviews(reviews);
				if (suc < (res / 2)) {// 保存成功数，小于评论总数的一般，可能有重复
					repc++;
				}
				System.out.println(String.format("\t评论入库：%dof%d", suc, res));
			}
			if (repc > 3) {
				System.out.println("超过3次重复检测，不再抓取");
				break; // 超过5次重复检测，不再抓取
			}
			try {
				Thread.sleep(THREAD_REVS_SLEEP);
			} catch (Exception e) {
			}
		}

		// 错误的评论页重新尝试抓取
		int es = errPgs.size();
		for (Integer pg : errPgs) {
			System.out.println(String.format("抓取失败页：%d/%d，还剩：%d", pg, end, --es));
			reviews = new ArrayList<JDReviewFeed>();
			JDJson json = getReviews(url, pg.intValue());
			if (json != null) {
				takeReviews(json, pid, reviews, rids);
				int suc = saveReviews(reviews);
				System.out.println(String.format("\t评论入库：%d of %d", suc, reviews.size()));
			} else {
				System.out.println("\t抓取失败");
			}
		}
	}

	/**
	 * 获取商品总评论数
	 * 
	 * @param url
	 *            商品地址
	 * @return
	 */
	public int getReviewCount(String url) {
		JDJson json = getReviews(url, 0);
		if (json != null) {
			return json.getProductCommentSummary().getCommentCount();
		}
		return -1;
	}

	/**
	 * 获取单页评论详情
	 * 
	 * @param url
	 *            商品地址
	 * @param page
	 *            分页码
	 * @return
	 */
	private JDJson getReviews(String url, int page) {
		// 准备HTTP头信息
		Map<String, String> tmp = new HashMap<String, String>();
		addMutstHeader(tmp, url);
		// 获取商品SKUID
		String pid = JdUtil.takeSkuid(url);
		// 准备请求参数
		long now = System.currentTimeMillis();
		long b = now - (new Random().nextInt(1000));
		String rurl = URL_4_REVIEW.replaceAll(REPLACE_ID, pid);
		rurl = rurl.replaceAll(REPLACE_PAGE, String.valueOf(page));
		rurl = rurl.replaceAll(REPLACE_NOW, String.valueOf(now));
		rurl = rurl.replaceAll(REPLACE_B, String.valueOf(b));
		rurl = rurl.replaceAll(REPLACE_VER, String.valueOf(new Random().nextInt(Integer.parseInt(pid))));
		System.out.println("\t评论地址：" + rurl);
		try {
			String data = NetUtil.getHtmlSrc(rurl, NetUtil.CHARSET_GBK, tmp);
			// System.out.println("\t评论数据：" + data);
			data = JDJson.subSafeJson(data, JDJson.BRACKET_1L);
			// System.out.println("\t评论格式化：" + data);
			return new Gson().fromJson(data, JDJson.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 提取评论详情
	 * 
	 * @param json
	 * @param pid
	 * @param reviews
	 * @param rids
	 */
	private void takeReviews(JDJson json, String pid, List<JDReviewFeed> reviews, Set<Long> rids) {
		// System.out.println("\t准备提取：" + json);
		for (JDJsonComments com : json.getComments()) {
			JDUserFeed jdu = new JDUserFeed(com);
			JDReviewFeed jdr = new JDReviewFeed(pid, jdu, com);
			System.out.println("\t" + jdr);
			if (jdr.getId() == null) {
				continue;
			}
			if (rids.add(jdr.getId())) {
				reviews.add(jdr);
			}
		}
	}

	/**
	 * 评论入库，返回保存成功数
	 * 
	 * @param reviews
	 * @return
	 */
	private int saveReviews(List<JDReviewFeed> reviews) {
		int suc = 0;
		for (JDReviewFeed jdr : reviews) {
			// 保存评论
			JDReviews dbr = new JDReviews();
			dbr.setRid(jdr.getId());
			dbr = jdReviewDao.selectOne(dbr);
			if (dbr == null) {
				JDReviews review = new ReviewsExt(jdr);
				try {
					if (jdReviewDao.insertSelective(review) > 0) {
						suc++;
					}
				} catch (Exception e) {
					String msg = String.format("评论入库失败，err=%s\tuid=%s", e.getMessage(), jdr.getId());
					System.err.println(msg);
				}
			}

			// 保存评论用户
			JDUserFeed jdu = jdr.getJdu();
			JDUser dbu = new JDUser();
			dbu.setUid(jdu.getUid());
			dbu = jdUserDao.selectOne(dbu);
			try {
				if (dbu == null) {
					jdUserDao.insertSelective(new UserExt(jdu));
				}
			} catch (Exception e) {
				String msg = String.format("用户入库失败，err=%s\tuid=%s", e.getMessage(), jdu.getUid());
				System.err.println(msg);
			}
		}
		return suc;
	}

	public void addMutstHeader(Map<String, String> tmp, String url) {
		tmp.putAll(header);
		tmp.put("Referer", url);
		tmp.put("Cookie",
				"user-key=9598b78c-cfeb-4efa-b6bf-66ec722aaf63; __utmz=122270672.1446531357.1.1.utmcsr=jd.com|utmccn=(referral)|utmcmd=referral|utmcct=/allSort.aspx; jwotest_product=98; cn=0; _tp=KVl5NKRxAukOSDXosbc9XA%3D%3D; _pst=bdceo; TrackID=11aI38u3ARq3CL09PvpFfK1lj_JZQKrUQ2ssHezQApagVin_nkXH5xQkdIK7J6kyWgb6MKPHVUyM3Bh6sX8iIWQ; pinId=S6gSNir0Ryo; unick=bdceo; pin=bdceo; thor=E6613EC3BB5D55B24D85A4F9C181AC6AAE6E13E700A9A946AF83F3EB893054AC72904F42B5B6DFE4EA3D37CE5089607C8AE7AFAA579459C20BE09DD801456DD3FAA9CB62B78104E7C443BB5A9485339395492203200E0EA7463FED2FD8A610D5EDD508F739F126FA00A3BE6C091DB1FB1A550DA2D91EEAB7D80DF2A839FF8C6F; __jda=122270672.352832047.1446186710.1446797129.1446800633.12; __jdb=122270672.36.352832047|12.1446800633; __jdc=122270672; __jdv=122270672|direct|-|none|-; ipLocation=%u5317%u4EAC; areaId=1; ipLoc-djd=1-72-4137-0; __jdu=352832047");
	}

}
