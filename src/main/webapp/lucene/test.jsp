
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<%/*
	String indexDir = FileUtil.getWebRoot() + "index" + File.separator
			+ "www";
	IWwwFetchDao wfd = LogicFactory.getWwwFetchDao();
	Directory dir = new SimpleFSDirectory(new File(indexDir));
	Analyzer ana = new StandardAnalyzer(Version.LUCENE_36);

	String key = request.getParameter("key");
	if (StringUtil.isEmpty(key)) {
		// 建立索引
		List<WwwFetch> wfs = wfd.findAll();

		IndexWriterConfig iwc = new IndexWriterConfig(
				Version.LUCENE_36, ana);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		iwc.setMaxBufferedDocs(1000);
		IndexWriter indexWriter = new IndexWriter(dir, iwc);
		indexWriter.deleteAll();

		int size = wfs.size();
		for (int i = 0; i < size; i++) {
			if (i == 100) {
				break;
			}
			WwwFetch wf = wfs.get(i);
			Document doc = new Document();
			doc.add(new Field("fetch_id", String.valueOf(wf.getId()),
					Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("fetch_src", wf.getFetchSrc(),
					Field.Store.YES, Field.Index.NOT_ANALYZED,
					Field.TermVector.YES));
			doc.add(new Field("fetch_key", wf.getFetchKey(),
					Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("fetch_url", wf.getFetchUrl(),
					Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("fetch_email", wf.getFetchMail(),
					Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("fetch_content", wf.getFetchContent(),
					Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
			doc.add(new Field("fetch_date", DateUtil
					.formatRegularTime(wf.getFetchDate()),
					Field.Store.YES, Field.Index.NOT_ANALYZED));
			indexWriter.addDocument(doc);
		}
		indexWriter.commit();
		indexWriter.optimize();
		indexWriter.close();
		System.out.println("索引生成完毕");
	} else {
		// 搜索
		IndexSearcher iser = new IndexSearcher(dir);
		QueryParser q_key = new QueryParser(Version.LUCENE_36,
				"fetch_key", ana);
		QueryParser q_con = new QueryParser(Version.LUCENE_36,
				"fetch_content", ana);

		// 多字段搜索
		BooleanQuery all = new BooleanQuery();
		Query q1 = q_key.parse(key);
		all.add(q1, BooleanClause.Occur.MUST);
		Query q2 = q_con.parse(key);
		all.add(q2, BooleanClause.Occur.MUST);

		Term tm = new Term("fetch_src", "广告门");
		Query q3 = new TermQuery(tm);
		//all.add(q3, BooleanClause.Occur.MUST);

		// 设定排序
		SortField stf = new SortField("fetch_date", SortField.STRING,
				true);// true-倒叙，false-升序
		Sort sort = new Sort();
		sort.setSort(stf);

		// 执行搜索
		//TopDocs td = iser.search(q1, 10);
		TopDocs td = iser.search(all, 100, sort);

		// 设置高亮及关键词最多显示多少字描述
		SimpleHTMLFormatter shf = new SimpleHTMLFormatter(
				"<font color=red>", "</font>");
		Highlighter hl = new Highlighter(shf, new QueryScorer(all));
		hl.setTextFragmenter(new SimpleFragmenter(200));

		int sum = td.totalHits;
		ScoreDoc[] sds = td.scoreDocs;// 获取搜索结果
		int size = sds.length;
		System.out.println("结果总数=" + sum);
		for (int i = 0; i < size; i++) {
			Document doc = iser.doc(sds[i].doc);// 获取具体doc对象
			out.println("id=" + doc.get("fetch_id"));
			out.println("<a href=\"" + doc.get("fetch_url")
					+ "\" target=\"_blank\">");
			String tmp = doc.get("fetch_key");
			tmp = hl.getBestFragment(ana, "fetch_key", tmp);
			out.println(tmp + "</a>");
			tmp = doc.get("fetch_content");
			tmp = hl.getBestFragment(ana, "fetch_content", tmp);
			out.println("</br>" + tmp + "<hr/>");
		}
		iser.close();
	}*/
%>
<body>
	<form action="test.jsp" method="post">
		<input type="text" name="key" /> <input type="submit" />
	</form>
</body>
</html>