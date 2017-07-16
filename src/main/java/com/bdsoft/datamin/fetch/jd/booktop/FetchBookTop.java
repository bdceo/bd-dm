package com.bdsoft.datamin.fetch.jd.booktop;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.entity.JDQueue;
import com.bdsoft.datamin.fetch.jd.JdUtil;
import com.bdsoft.datamin.mapper.JDQueueMapper;
import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.TimeCoster;
import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;
import com.beust.jcommander.internal.Lists;

/**
 * 京东，(新书)图书销售排行榜
 * 
 * @author 丁辰叶
 * @date 2014-9-18
 */
public class FetchBookTop {

	private static JDQueueMapper jdqMapper;

	static {
		BDSpringUtil.init();
		jdqMapper = BDSpringUtil.getBean(JDQueueMapper.class);
	}

	/**
	 * 入口
	 */
	public static void main(String[] args) throws Exception {
		// 图书销量榜
		String bookSaleUrl = "http://book.jd.com/booktop/0-0-0.html?category=1713-0-0-0-10001-#PN";
		// 新书销量榜
		String newSaleUrl = "http://book.jd.com/booktop/0-1-0.html?category=1713-0-1-0-10001-#PN";
		// 图书热评榜
		String hotCommUrl = "http://book.jd.com/booktop/0-0-1.html?category=1713-0-0-1-10001-#PN";
		// 新书热评榜
		String newCommUrl = "http://book.jd.com/booktop/0-1-1.html?category=1713-0-1-1-10001-#PN";
		// 少儿畅销榜
		String childSaleUrl = "http://book.jd.com/booktop/1-0-0.html?category=20009-1-0-0-10001-10007-#PN";
		// 中小学教辅榜
		String stuSaleUrl = "http://book.jd.com/booktop/1-0-0.html?category=20010-1-0-0-10001-#PN";

		List<String> topList = Lists.newArrayList();
		topList.add(bookSaleUrl);
		topList.add(newSaleUrl);
		topList.add(hotCommUrl);
		topList.add(newCommUrl);
		topList.add(childSaleUrl);
		topList.add(stuSaleUrl);

		TimeCoster.getInstance().start("fetch-jd-booktop", "抓取各图书销售榜");
		// 每个榜单只抓取前5页
		for (String url : topList) {
			for (int start = 1; start <= 5; start++) {
				String topUrl = url.replaceAll("#PN", String.valueOf(start));
				parseTopBookUrl(topUrl);
			}
		}
		TimeCoster.getInstance().end("fetch-jd-booktop");
	}

	/**
	 * 解析排行榜页面，提取书籍url
	 * 
	 * @param topUrl
	 * @throws Exception
	 */
	public static void parseTopBookUrl(String topUrl) throws Exception {
		System.out.println("抓取排行》" + topUrl);
		String src = BDHttpUtil.sendGet(topUrl, BDHttpParam.init().setCharset(NetUtil.CHARSET_GBK));
		Document html = Jsoup.parse(src);

		List<JDQueue> jqs = Lists.newArrayList();

		Elements bookEles = html.select("div.m-list ul li");
		int index = 0;
		for (Element ele : bookEles) {

			index++;
			ele = ele.select("div.p-img a").first();
			String name = ele.attr("title");
			String href = JdUtil.repairUrl(ele.attr("href"));
			System.out.println(String.format("\n排名：%d 《%s》 \n地址：%s", index, name, href));

			JDQueue jdq = jdqMapper.selectOne(new JDQueue(href));
			if (jdq == null) {
				jqs.add(new JDQueue(href, 1));
			}
		}
		if (jqs.size() > 0) {
			int bs = jdqMapper.insertBatch(jqs);
			System.out.println("批量入库：" + bs);
		}
	}

}
