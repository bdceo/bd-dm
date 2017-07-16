package com.bdsoft.datamin.fetch.jd;

/**
 * 多线程分发抓取
 *
 * @author   丁辰叶
 * @date	 2016-5-26
 * @version  1.0.0
 */
public class SEtest {

	// 2013-10-17：抓取京东评论，子线程任务分发处理
	// 具体应用参见JDFetchQueue.JdReviewFetcher线程类
	public static void subThreadCalc() {
		int count = 1010;
		int mod = count % 10;
		int shang = count / 10;
		int pages = (mod == 0) ? shang : shang + 1;// 总页数
		System.out.println("pages=" + pages);

		int maxt = 10;

		mod = pages % maxt;// 是否10个线程 可以正好平均处理
		int per = pages / maxt;// 每个线程处理多少页

		System.out.println("mod=" + mod);

		System.out.println("per=" + per);

		int s = 0, e = 0;
		if (pages <= maxt) {
			for (int i = 0; i < pages; i++) {
				e++;
				System.out.println("s=" + s + ",e=" + e);
				s = e;
			}
		} else {
			for (int i = 0; i < maxt; i++) {
				e = (i + 1) * per;
				if (i == (maxt - 1) && mod > 0) {
					e += mod;
				}
				System.out.println("s=" + s + ",e=" + e);
				s = e;
			}
		}
	}
}
