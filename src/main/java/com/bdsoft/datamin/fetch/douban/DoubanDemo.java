//package com.bdsoft.datamin.fetch.douban;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.util.ArrayList;
//
//import com.google.gdata.client.douban.DoubanService;
//import com.google.gdata.data.Link;
//import com.google.gdata.data.PlainTextConstruct;
//import com.google.gdata.data.TextContent;
//import com.google.gdata.data.douban.Attribute;
//import com.google.gdata.data.douban.CollectionEntry;
//import com.google.gdata.data.douban.CollectionFeed;
//import com.google.gdata.data.douban.MiniblogEntry;
//import com.google.gdata.data.douban.MiniblogFeed;
//import com.google.gdata.data.douban.NoteEntry;
//import com.google.gdata.data.douban.NoteFeed;
//import com.google.gdata.data.douban.ReviewEntry;
//import com.google.gdata.data.douban.ReviewFeed;
//import com.google.gdata.data.douban.Status;
//import com.google.gdata.data.douban.Subject;
//import com.google.gdata.data.douban.SubjectEntry;
//import com.google.gdata.data.douban.SubjectFeed;
//import com.google.gdata.data.douban.Tag;
//import com.google.gdata.data.douban.TagEntry;
//import com.google.gdata.data.douban.TagFeed;
//import com.google.gdata.data.douban.UserEntry;
//import com.google.gdata.data.douban.UserFeed;
//import com.google.gdata.data.extensions.Rating;
//import com.google.gdata.util.ServiceException;
//
///**
// * 豆瓣 @ Google SVN 提供实例
// * 
// * @author bdceo
// */
//public class DoubanDemo {
//	private static String userId = "bdceo";
//	private static String douban_user_id = "3115199";
//	private static String api_key = "01b11c39ad702886051354ad08507992";
//	private static String api_secret = "dc5c5df1518f6b66";
//
//	public static void main(String[] args) throws Exception {
//		// 1，获取未授权的Request Token
//		DoubanService service = new DoubanService("subApplication", api_key,
//				api_secret);
//
//		// 2，请求用户授权Request Token
//		// System.out.println("复制如下链接到浏览器，完成授权认证再回来:");
//		// String authUrl = service.getAuthorizationUrl(null);
//		// System.out.println(authUrl);// 无回调
//		// byte buffer[] = new byte[1];
//		// System.in.read(buffer);
//
//		// 设置请求令牌
//		// String reqToken = authUrl.substring(authUrl.lastIndexOf("=") +
//		// 1,authUrl.length());
//		// System.out.println(reqToken);
//		// service.setRequestToken(reqToken);
//
//		// 设置请求令牌密匙
//		// System.out.println(service.getRequestTokenSecret());
//		// service.setRequestTokenSecret(service.getRequestTokenSecret());
//
//		// 3，使用授权后的Request Token换取Access Token
//		// service.getAccessToken();
//
//		// 4，使用Access Token访问或修改受保护资源
//		// System.out.println("用户");
//		// testUserEntry(service);
//		// testUserFriends(service);
//
//		// System.out.println("书籍，电影，音乐");
//		// testSubjectEntry(service);
//
//		// System.out.println("评论");
//		// testReviewEntry(service);
//		// testWriteReviewEntry(service);
//
//		// System.out.println("收藏");
//		// testCollectionEntry(service);
//		// testCollectionFeed(service);
//		// testWriteCollectionEntry(service);
//
//		// System.out.println("日记");
//		// testNoteEntry(service);
//		// testWriteNoteEntry(service);
//
//		// System.out.println("标签");
//		// testTagFeed(service);
//
//		System.out.println("用户广播");
//		testMiniblogEntry(service);
//	}
//
//	private static void testUserFriends(DoubanService service) {
//		try {
//			// 获取用户朋友
//			// http://api.douban.com/people/bdceo/friends?start-index=1&max-results=2
//			UserFeed uf = service.getUserFriends("bdceo", 1, 3);
//			for (UserEntry ue : uf.getEntries()) {
//				printUserEntry(ue);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void testWriteNoteEntry(DoubanService service) {
//		NoteEntry noteEntry;
//		try {
//			// 发表新日记
//			String content = "   bdceo的帐号第三方应用授权支付宝账户小豆基本设置第三方开发的应用（或网站）需要操作你的数据时，需要得到你的授权才能访问。你可以随时取消这些授权。若你曾授权用豆瓣账号登录合作网站，取消授权并不会导致该网站上的账号被删除。"
//					+ "应用 授权时间 操作 豆瓣电影 2011-08-01 01:36:29 取消授权Android豆瓣说 2011-06-26 00:20:15 取消授权豆瓣社区(手机版) 2010-08-30 21:24:45 取消授权";
//			// 1,日记标题 2,内容 3,是否公开 4,是否可回复
//			noteEntry = service.createNote(new PlainTextConstruct("豆瓣帐号"),
//					new PlainTextConstruct(content), "public", "yes");
//			printNoteEntry(noteEntry);
//
//			// 更新日记
//			String content2 = "立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋立秋";
//			service.updateNote(noteEntry, new PlainTextConstruct("豆瓣帐号"),
//					new PlainTextConstruct(content2), "private", "no");
//
//			// 删除日记
//			// service.deleteNote(noteEntry);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void testWriteCollectionEntry(DoubanService service) {
//		try {
//			CollectionEntry ce;
//
//			Rating rating = new Rating();
//			rating.setValue(4);
//
//			ArrayList<Tag> tags = new ArrayList<Tag>(2);
//			Tag t1 = new Tag();
//			t1.setName("科幻");
//			Tag t2 = new Tag();
//			t2.setName("美国");
//
//			tags.add(t1);
//			tags.add(t2);
//
//			String movieId = "3610047"; // 变形金刚
//			SubjectEntry se = service.getMovie(movieId);
//			ce = service.createCollection(new Status("wish"), se, tags, rating);
//
//			printCollectionEntry(ce);
//
//			// service.deleteCollection(ce);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void testMiniblogEntry(DoubanService service) {
//		MiniblogFeed mf;
//		try {
//			// 获取用户广播
//			mf = service.getUserMiniblogs(userId, 1, 2);
//			for (MiniblogEntry me : mf.getEntries()) {
//				printMiniblogEntry(me);
//			}
//			// 获取用户友邻广播
//			mf = service.getContactsMiniblogs(userId, 1, 2);
//			for (MiniblogEntry me : mf.getEntries()) {
//				printMiniblogEntry(me);
//			}
//			// 发布说说
//			service.createSaying(new PlainTextConstruct(
//					"我在用Java调取豆瓣的API发布一条说说……"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void testTagFeed(DoubanService service) {
//		try {
//			// 用户对书籍、电影、音乐标记的所有标签
//			// cat=movie&start-index=2&max-results=3
//			TagFeed tf = service.getUserTags(userId, "movie", 1, 4);
//			for (TagEntry te : tf.getEntries()) {
//				printTagEntry(te);
//			}
//			System.out.println("**************************************");
//			// 某个书籍，电影，音乐中标记最多的标签
//			String movieId = "3610047";// 变形金刚
//			// start-index=1&max-results=10
//			tf = service.getMovieTags(movieId, 1, 5);
//			for (TagEntry te : tf.getEntries()) {
//				printTagEntry(te);
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void testCollectionEntry(DoubanService service) {
//		try {
//			// 收藏
//			String cid = "2154164";
//			CollectionEntry ce = service.getCollection(cid);
//			printCollectionEntry(ce);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void testCollectionFeed(DoubanService service) {
//		try {
//			// 获取用户收藏信息
//			// cat=movie&start-index=1&max-results=2&tag=TimBurton
//			// status=wish
//			// 1,用户 2,电影 3,标签 4,想看
//			CollectionFeed cf = service.getUserCollections(douban_user_id,
//					"movie", null, "wish", 1, 4);
//			for (CollectionEntry ce : cf.getEntries()) {
//				printCollectionEntry(ce);
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void testReviewEntry(DoubanService service) {
//		ReviewEntry reviewEntry;
//		try {
//
//			// 获取评论信息
//			// http://api.douban.com/review/{reviewID}
//			String reviewId = "5001846";
//			reviewEntry = service.getReview(reviewId);
//			printReviewEntry(reviewEntry);
//
//			// 特定用户的所有评论
//			String userId = "bdceo";
//			ReviewFeed reviewFeed = service.getUserReviews(userId);
//			for (ReviewEntry sf : reviewFeed.getEntries()) {
//				printReviewEntry(sf);
//			}
//
//			// 特定书籍、电影、音乐的所有评论
//			String movieId = "3610047";
//			// start-index=2&max-results=2
//			reviewFeed = service.getMovieReviews(movieId, 2, 2, "score");
//			for (ReviewEntry sf : reviewFeed.getEntries()) {
//				printReviewEntry(sf);
//			}
//			reviewFeed = service.getMovieReviews(movieId, 2, 2, "time");
//			for (ReviewEntry sf : reviewFeed.getEntries()) {
//				printReviewEntry(sf);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void testWriteReviewEntry(DoubanService service) {
//		ReviewEntry reviewEntry;
//		try {
//			String movieId = "3610047"; // 变形金刚3 Transformers: Dark of the Moon
//			// (2011)
//			SubjectEntry se = service.getMovie(movieId);
//			Rating rate = new Rating();
//			rate.setValue(4);
//			String content = "还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！还没看，反映不错，期待！！！";
//			reviewEntry = service.createReview(se, new PlainTextConstruct(
//					"期待期待期待期待期待期待期待期待"), new PlainTextConstruct(content), rate);
//			printReviewEntry(reviewEntry);
//			// 删除评论
//			// service.deleteReview(reviewEntry);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void testSubjectEntry(DoubanService service) {
//		SubjectEntry subjectEntry;
//		try {
//			String bookId = "6404786";
//			// String isbnCode = "9787302251156";
//			// 获取书籍信息（电影，音乐类似）
//			// http://api.douban.com/book/subject/bookId
//			subjectEntry = service.getBook(bookId);
//
//			// http://api.douban.com/book/subject/isbn/isbnCode
//			// subjectEntry = service.getBook(isbnCode);
//			printSubjectEntry(subjectEntry);
//
//			// 搜索电影信息（书籍，音乐类似）
//			// http://api.douban.com/movie/subject/subjectID
//			// tag=cowboy&start-index=1&max-results=2
//			// q=null
//			SubjectFeed subjectFeed = service.findMovie("抗日", "爱情", 1, 1);
//			for (SubjectEntry sf : subjectFeed.getEntries()) {
//				printSubjectEntry(sf);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	private static void testNoteEntry(DoubanService service) {
//		NoteEntry noteEntry;
//		NoteFeed noteFeed;
//		try {
//			// 获取日记
//			String noteId = "88710109";
//			noteEntry = service.getNote(noteId);
//			printNoteEntry(noteEntry);
//
//			// 获取用户所有日记
//			noteFeed = service.getUserNotes("bdceo", 1, 4);
//			for (NoteEntry ne : noteFeed.getEntries()) {
//				printNoteEntry(ne);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void testUserEntry(DoubanService service) {
//		UserEntry userEntry;
//		try {
//			// 获取用户信息
//			// http://api.douban.com/people/userId
//			userEntry = service.getUser(userId);
//			printUserEntry(userEntry);
//
//			// 获取当前授权用户信息
//			// http://api.douban.com/people/@me
//			System.out.println("认证用户信息如下：");
//			printUserEntry(service.getAuthorizedUser());
//			System.out.println("——————————————————");
//
//			// 搜索用户
//			// http://api.douban.com/people?q=douban&start-index=10&max-results=1
//			UserFeed userFeed = service.findUser("ddong", 1, 1);
//			for (UserEntry ue : userFeed.getEntries()) {
//				printUserEntry(ue);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void printNoteEntry(NoteEntry noteEntry) {
//		if (noteEntry.getSummary() != null)
//			System.out.println("summary = "
//					+ noteEntry.getSummary().getPlainText());
//		if (noteEntry.getContent() != null)
//			System.out.println("content = "
//					+ ((TextContent) noteEntry.getContent()).getContent()
//							.getPlainText());
//		if (noteEntry.getAuthors() != null) {
//			if (!noteEntry.getAuthors().isEmpty())
//				System.out.println("author = "
//						+ noteEntry.getAuthors().get(0).getName());
//		}
//		System.out.println("title = " + noteEntry.getTitle().getPlainText());
//		/*
//		 * for (Attribute attr : noteEntry.getAttributes()) {
//		 * System.out.println(attr.getName() + " : " + attr.getContent()); }
//		 */
//		// System.out.println(noteEntry.getAttributes().isEmpty());
//		for (Attribute attr : noteEntry.getAttributes()) {
//			System.out.println(attr.getName() + " : " + attr.getContent());
//		}
//		System.out.println("id = " + noteEntry.getId());
//		System.out.println("-------------------");
//	}
//
//	private static void printReviewEntry(ReviewEntry reviewEntry) {
//		// System.out.println("content is " +
//		// reviewEntry.getPlainTextContent());
//		System.out.println("id is = " + reviewEntry.getId());
//		System.out.println("title is = "
//				+ reviewEntry.getTitle().getPlainText());
//		if (!reviewEntry.getAuthors().isEmpty()) {
//			System.out.println("author name is = "
//					+ reviewEntry.getAuthors().get(0).getName());
//			System.out.println("author URI is = "
//					+ reviewEntry.getAuthors().get(0).getUri());
//		}
//		System.out.println("updated date is = " + reviewEntry.getUpdated());
//		System.out.println("published date is = " + reviewEntry.getPublished());
//		if (reviewEntry.getSummary() != null)
//			System.out.println("summary is = "
//					+ reviewEntry.getSummary().getPlainText());
//		Rating rating = reviewEntry.getRating();
//		if (rating != null)
//			System.out.println("Rating : max = " + rating.getMax() + " min = "
//					+ rating.getMin() + " numRaters = " + rating.getNumRaters()
//					+ " average = " + rating.getAverage() + " value = "
//					+ rating.getValue());
//
//		// begin the subject info
//		System.out.println("begin the subject info:");
//		Subject se = reviewEntry.getSubjectEntry();
//		if (se != null)
//			printSubjectEntry(se);
//		else
//			System.out.println("no subject got");
//		System.out
//				.println("-----------------------------------------------------------");
//	}
//
//	private static void printSubjectEntry(SubjectEntry subjectEntry) {
//
//		if (subjectEntry.getSummary() != null)
//			System.out.println("summary = "
//					+ subjectEntry.getSummary().getPlainText());
//		System.out.println("author = "
//				+ subjectEntry.getAuthors().get(0).getName());
//		System.out.println("title = " + subjectEntry.getTitle().getPlainText());
//
//		for (Attribute attr : subjectEntry.getAttributes()) {
//			System.out.println(attr.getName() + " = " + attr.getContent());
//		}
//		System.out.println("id = " + subjectEntry.getId());
//		for (Tag tag : subjectEntry.getTags()) {
//			System.out.println(tag.getName() + " = " + tag.getCount());
//		}
//
//		Rating rating = subjectEntry.getRating();
//		if (rating != null)
//			System.out.println("max = " + rating.getMax() + " min = "
//					+ rating.getMin() + " numRaters = " + rating.getNumRaters()
//					+ " average = " + rating.getAverage());
//		System.out
//				.println("--------------------------------------------------");
//	}
//
//	private static void printSubjectEntry(Subject se) {
//		if (se == null)
//			return;
//		if (se.getSummary() != null)
//			System.out.println("summary = " + se.getSummary().getPlainText());
//		System.out.println("author = " + se.getAuthors().get(0).getName());
//		System.out.println("title = " + se.getTitle().getPlainText());
//
//		for (Attribute attr : se.getAttributes()) {
//			System.out.println(attr.getName() + " : " + attr.getContent());
//		}
//		System.out.println("id = " + se.getId());
//		for (Tag tag : se.getTags()) {
//			System.out.println(tag.getName() + " : " + tag.getCount());
//		}
//
//		Rating rating = se.getRating();
//		if (rating != null)
//			System.out.println("max = " + rating.getMax() + " min = "
//					+ rating.getMin() + " numRaters = " + rating.getNumRaters()
//					+ " average = " + rating.getAverage());
//		System.out.println("********************");
//	}
//
//	private static void printUserEntry(UserEntry ue) {
//
//		System.out.println("id = " + ue.getId());
//		System.out.println("uid = " + ue.getUid());
//		System.out.println("location = " + ue.getLocation());
//
//		System.out.println("content = "
//				+ ((TextContent) ue.getContent()).getContent().getPlainText());
//		System.out.println("title = " + ue.getTitle().getPlainText());
//
//		printUserEntryLinks(ue);
//		System.out.println("---------------------------------------------");
//
//	}
//
//	private static void printUserEntryLinks(UserEntry ue) {
//		System.out.println("--Links:");
//		for (Link link : ue.getLinks()) {
//			System.out.println("  " + link.getRel() + " = " + link.getHref());
//		}
//	}
//
//	private static void printMiniblogEntry(MiniblogEntry me) {
//		if (me.getContent() != null)
//			System.out.println("content = "
//					+ ((TextContent) me.getContent()).getContent()
//							.getPlainText());
//		System.out.println("title = " + me.getTitle().getPlainText());
//		System.out.println("id = " + me.getId());
//		System.out.println("published time = " + me.getPublished());
//		for (Attribute attr : me.getAttributes()) {
//			System.out.println(attr.getName() + " : " + attr.getContent());
//		}
//		System.out.println("---------------------------------------------");
//	}
//
//	private static void printTagEntry(TagEntry te) {
//		System.out.println("count = " + te.getCount().getContent());
//		System.out.println("id = " + te.getId());
//		System.out.println("title = " + te.getTitle().getPlainText());
//	}
//
//	private static void printCollectionEntry(CollectionEntry ce) {
//		System.out.println("id = " + ce.getId());
//		System.out.println("title = " + ce.getTitle().getPlainText());
//		if (!ce.getAuthors().isEmpty()) {
//			System.out.println("author name = : "
//					+ ce.getAuthors().get(0).getName());
//			System.out.println("author URI = : "
//					+ ce.getAuthors().get(0).getUri());
//		}
//		System.out.println("status = " + ce.getStatus().getContent());
//		printSubjectEntry(ce.getSubjectEntry());
//		Rating rating = ce.getRating();
//		if (rating != null)
//			System.out.println("max = " + rating.getMax() + " min = "
//					+ rating.getMin() + " value = " + rating.getValue()
//					+ " numRaters = " + rating.getNumRaters() + " average = "
//					+ rating.getAverage());
//		System.out.println("Tags:");
//		for (Tag tag : ce.getTags()) {
//			System.out.println(tag.getName());
//		}
//	}
//}
