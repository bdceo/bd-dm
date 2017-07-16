<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>豆瓣图书-搜索</title>
</head>
<%--/*
	String key = request.getParameter("key");
	if (StringUtil.isNotEmpty(key)) {
		System.out.println("搜索图书-->" + key);
		String bookIndexPath = LuceneUtil.BOOK_INDEX;
		Directory dir = new SimpleFSDirectory(new File(bookIndexPath));
		Analyzer ana = new StandardAnalyzer(Version.LUCENE_36);
		IndexSearcher searcher = new IndexSearcher(dir);

		QueryParser qpName = new QueryParser(Version.LUCENE_36,
				"bookName", ana);
		QueryParser qpAuthor = new QueryParser(Version.LUCENE_36,
				"authorName", ana);
		QueryParser qpTag = new QueryParser(Version.LUCENE_36, "tag",
				ana);

		Query qName = qpName.parse(key);
		Query qAuthor = qpAuthor.parse(key);
		Query qTag = qpTag.parse(key);

		BooleanQuery query = new BooleanQuery();
		query.add(qName, BooleanClause.Occur.MUST);
		//query.add(qAuthor, BooleanClause.Occur.MUST);
		//query.add(qTag, BooleanClause.Occur.MUST);

		SortField sortField = new SortField("id",
				SortField.STRING, false);
		Sort sort = new Sort();
		sort.setSort(sortField);

		TopDocs docs = searcher.search(query, 100, sort);
		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter(
				"<font color=red>", "</font>");
		Highlighter hl = new Highlighter(formatter, new QueryScorer(
				query));
		hl.setTextFragmenter(new SimpleFragmenter(200));

		int sum = docs.totalHits;
		ScoreDoc[] docArr = docs.scoreDocs;
		int size = docArr.length;
		out.println("<hr/>");
		System.out.println("符合条件数：" + sum);
		for (int i = 0; i < size; i++) {
			Document doc = searcher.doc(docArr[i].doc);
			out.println(doc.get("id") + "#");

			String tmp = doc.get("bookName");
			//tmp = hl.getBestFragment(ana, "bookName", tmp);
			out.println("书名《" + tmp + "》");

			tmp = doc.get("authorName");
			//tmp = hl.getBestFragment(ana, "authorName", tmp);
			out.println("&nbsp;&nbsp;&nbsp;&nbsp;作者 - " + tmp);

			tmp = doc.get("tag");
			tmp = hl.getBestFragment(ana, "tag", tmp);
			out.println("<br/>标签：" + tmp);

			out.println("<br/><br/>");
		}
		searcher.close();
	}*/
--%>
<body>
	豆瓣图书搜索
	<form action="search.jsp" method="post">
		<input type="text" name="key" size=30 /> <input type="submit"
			value="BD 搜索" />
	</form>
</body>
</html>