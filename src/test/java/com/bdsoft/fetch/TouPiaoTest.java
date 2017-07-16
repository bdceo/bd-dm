package com.bdsoft.fetch;

import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;

public class TouPiaoTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String url = "http://pa10oo1143.mychewang.cn/plugin.php?id=hejin_toupiao&model=ticket&zid=2684&formhash=5575de1f&_=1471100911241";

		url = "http://pa10oo1143.mychewang.cn/plugin.php";

		BDHttpParam hp = BDHttpParam.init();

		hp.addCookie("hjbox_openid", "ofMpPwUkSy_pWO133B1FRrYXJ2cE");
		hp.addCookie("lQEv_2132_saltkey", "V4xlv9Z9");
		hp.addCookie("lQEv_2132_sid", "bMiiLe");

		hp.addHeader(
				"User-Agent",
				"Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");

		hp.addCommon("_", System.currentTimeMillis() + "");
		hp.addCommon("zid", "2684");
		hp.addCommon("formhash", "5575de1f");
		hp.addCommon("model", "ticket");
		hp.addCommon("id", "hejin_toupiao");

		String res = BDHttpUtil.sendGet(url, hp);
		
		BDFileUtil.writeFile("d:/home/toupiao.txt", res);
		System.out.println(res);
	}

}
