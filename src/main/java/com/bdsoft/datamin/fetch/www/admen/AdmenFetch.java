package com.bdsoft.datamin.fetch.www.admen;

import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bdsoft.datamin.entity.WwwFetch;
import com.bdsoft.datamin.entity.WwwFetchTmp;
import com.bdsoft.datamin.fetch.www.FetchFilter;
import com.bdsoft.datamin.fetch.www.FetchTmp;
import com.bdsoft.datamin.mapper.WwwFetchMapper;
import com.bdsoft.datamin.mapper.WwwFetchTmpMapper;
import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.http.NetUtil;

public class AdmenFetch {

	public static void fetchFromDb() {
		WwwFetchTmpMapper wftd = BDSpringUtil.getBean(WwwFetchTmpMapper.class);

		WwwFetchTmp wft = new WwwFetchTmp();
		wft.setFetchDomain(FetchTmp.WWW_ADMEN);
		wft.setFetchState(1);
		List<WwwFetchTmp> wfts = wftd.selectList(new EntityWrapper<WwwFetchTmp>(wft));
		if (wfts != null && wfts.size() > 0) {
			// 准备抓取
			int size = wfts.size();
			for (int i = 0; i < size; i++) {
				try {
					pick(wfts.get(i));
				} catch (Exception e) {
					System.out.println(e.getMessage());
					continue;
				}
			}
		}
	}

	public static void pick(WwwFetchTmp wft) throws Exception {
		WwwFetchTmpMapper wftd = BDSpringUtil.getBean(WwwFetchTmpMapper.class);
		WwwFetchMapper wfd = BDSpringUtil.getBean(WwwFetchMapper.class);

		System.out.println("抓取地址：" + wft.getFetchUrl());
		String src = NetUtil.getHtmlSrc(wft.getFetchUrl(), FetchTmp.CHARSET);

		Document html = Jsoup.parse(src);
		Element e = html.getElementById("news_contents");
		System.out.println("抓取内容：" + e.text());
		String mail = FetchFilter.pickEmail(e.text());
		System.out.println("抓取邮箱：" + mail);

		WwwFetch wf = new WwwFetch(wft);
		wf.setFetchContent(e.text());
		wf.setFetchMail(mail);
		wf.setFetchSrc("广告门 ");
		wfd.insertSelective(wf);
		System.out.println("已入库\n");

		wft.setFetchState(2);
		wft.setFetchDate(new Date());
		wftd.updateSelectiveById(wft);
	}

}
