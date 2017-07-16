package com.bdsoft.datamin.fetch.douban;

import com.google.gdata.client.douban.DoubanService;
import com.google.gdata.data.douban.Attribute;
import com.google.gdata.data.douban.CollectionEntry;
import com.google.gdata.data.douban.CollectionFeed;
import com.google.gdata.data.douban.Subject;
import com.google.gdata.data.douban.SubjectEntry;
import com.google.gdata.data.douban.Tag;
import com.google.gdata.data.extensions.Rating;

/**
 * 测试api
 * @author bdceo
 *
 */
public class DoubanAPI {

	private static String douban_user_id = "3115199";
	private static String apiKey = "01b11c39ad702886051354ad08507992";
	private static String secret = "dc5c5df1518f6b66";

	public static void main(String[] args) {
		DoubanService service = new DoubanService("subApplication", apiKey,
				secret);

		// System.out.println("查看用户想看的:");
		// wishMovies(service, douban_user_id, 2, 1);

		System.out.println("查看电影详情 -->");
		String mid = "1823357";// 新街口
		movieDetail(service, mid);
	}

	/**
	 * 用户想看电影列表
	 * 
	 * @param service
	 *            豆瓣服务接口
	 * @param uid
	 *            授权用户ID
	 * @param startIndex
	 *            返回结果的起始元素索引
	 * @param maxResults
	 *            返回结果的数量
	 */
	private static void wishMovies(DoubanService service, String uid,
			int startIndex, int maxResults) {
		try {
			CollectionFeed movies = service.getUserCollections(uid, "movie",
					null, "wish", startIndex, maxResults);
			for (CollectionEntry m : movies.getEntries()) {
				printCollectionEntry(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 电影详情
	 * 
	 * @param service
	 *            豆瓣服务接口
	 * @param mid
	 *            电影资源ID
	 */
	private static void movieDetail(DoubanService service, String mid) {
		try {
			SubjectEntry movieEntry = service.getMovie(mid);
			printSubjectEntry(movieEntry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO 按标签搜，按主演搜电影

	// 打印电影详情
	private static void printSubjectEntry(SubjectEntry movieEntry) {
		if (movieEntry.getSummary() != null)
			System.out
					.println("电影简介：" + movieEntry.getSummary().getPlainText());
		System.out.println("导演：" + movieEntry.getAuthors().get(0).getName());
		System.out.println("电影名：" + movieEntry.getTitle().getPlainText());
		StringBuffer sbCast = new StringBuffer();
		for (Attribute attr : movieEntry.getAttributes()) {
			if (attr.getName().equals("cast")) {
				sbCast.append(" " + attr.getContent());
			} else if (attr.getName().equals("pubdate")) {
				System.out.println("上映日期：" + attr.getContent());
			} else if (attr.getName().equals("aka")) {
				System.out.println("又名：" + attr.getContent());
			} else if (attr.getName().equals("language")) {
				System.out.println("语言：" + attr.getContent());
			} else if (attr.getName().equals("country")) {
				System.out.println("制片国家/地区：" + attr.getContent());
			}
		}
		System.out.println("主演："
				+ sbCast.toString().trim().replaceAll(" ", "/"));
		for (Tag tag : movieEntry.getTags()) {
			System.out.print(tag.getName() + "（" + tag.getCount() + ")  ");
		}
		Rating rating = movieEntry.getRating();
		if (rating != null)
			System.out.println("\n最高评分：" + rating.getMax() + " 最低评分： "
					+ rating.getMin() + " 评价人数： " + rating.getNumRaters()
					+ " 平均 分： " + rating.getAverage());
		System.out
				.println("--------------------------------------------------");
	}

	// 打印收藏详情
	private static void printCollectionEntry(CollectionEntry movie) {
		System.out.println("api收藏主页 = " + movie.getId());
		System.out.println("标题  = " + movie.getTitle().getPlainText());
		if (!movie.getAuthors().isEmpty()) {
			System.out.println("author name = : "
					+ movie.getAuthors().get(0).getName());
			System.out.println("author URI = : "
					+ movie.getAuthors().get(0).getUri());
		}
		System.out.println("想看/看过 = " + movie.getStatus().getContent());
		printSubjectEntry(movie.getSubjectEntry());
		Rating rating = movie.getRating();
		if (rating != null)
			System.out.println("max = " + rating.getMax() + " min = "
					+ rating.getMin() + " value = " + rating.getValue()
					+ " numRaters = " + rating.getNumRaters() + " average = "
					+ rating.getAverage());
		System.out.println("Tags:");
		for (Tag tag : movie.getTags()) {
			System.out.println(tag.getName());
		}
	}

	private static void printSubjectEntry(Subject se) {
		if (se == null)
			return;
		if (se.getSummary() != null)
			System.out.println("简介 = " + se.getSummary().getPlainText());
		System.out.println("导演/作者 = " + se.getAuthors().get(0).getName());
		System.out.println("标题 = " + se.getTitle().getPlainText());

		for (Attribute attr : se.getAttributes()) {
			System.out.println(attr.getName() + " : " + attr.getContent());
		}
		System.out.println("api电影主页 = " + se.getId());
		for (Tag tag : se.getTags()) {
			System.out.println(tag.getName() + " : " + tag.getCount());
		}

		Rating rating = se.getRating();
		if (rating != null)
			System.out.println("max = " + rating.getMax() + " min = "
					+ rating.getMin() + " numRaters = " + rating.getNumRaters()
					+ " average = " + rating.getAverage());
		System.out.println("********************");
	}

}
