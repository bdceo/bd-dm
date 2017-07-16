<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>豆瓣图书-添加索引</title>
</head>
<%--
	/*String op = request.getParameter("op");
	if (StringUtil.isNotEmpty(op)) {
		System.out.println("请求OP=" + op);
		IDouBookDao bookDao = LogicFactory.getDouBookDao();

		String bookIndexPath = LuceneUtil.BOOK_INDEX;
		Directory dir = new SimpleFSDirectory(new File(bookIndexPath));
		Analyzer analy = new StandardAnalyzer(Version.LUCENE_36);
		IndexWriterConfig iwc = new IndexWriterConfig(
				Version.LUCENE_36, analy);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		iwc.setMaxBufferedDocs(1000);
		IndexWriter iw = new IndexWriter(dir, iwc);
		iw.deleteAll();

		// 建立图书索引
		int start = 1;
		int size = 100;
		//int total = bookDao.findAllCount();
		int total = 209633;
		int count = (total / size) + 1;
		//count = 1;
		for (int i = start; i <= count; i++) {
			List<DouBook> books = bookDao.findAll("id", "asc", i, size);
			System.out.println("\t第" + i + "页，开始建索引...");
			for (DouBook book : books) {
				System.out.println("第" + i + "页，书《" + book.getId()
						+ "#" + book.getBookName() + " "
						+ book.getBookNameEn() + " 》建立索引...");
				// 建立索引
				Document doc = new Document();
				doc.add(new Field("id", "" + book.getId(),
						Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("bookName", book.getBookName() + " "
						+ book.getBookNameEn(), Field.Store.YES,
						Field.Index.ANALYZED));
				doc.add(new Field("authorName", book.getBookAuthor()
						+ " " + book.getBookTranslator(),
						Field.Store.YES, Field.Index.ANALYZED));
				doc.add(new Field("tag", book.getBookTag() + " ",
						Field.Store.YES, Field.Index.ANALYZED));
				iw.addDocument(doc);
			}
			iw.commit();
			iw.optimize();
			System.out.println("\t第" + i + "页，索引建立完毕。");
		}
		iw.close();
		System.out.println("索引建立完毕，查看：" + bookIndexPath);
	}*/
--%>
<body>

</body>
</html>