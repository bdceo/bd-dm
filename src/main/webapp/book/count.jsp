<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>豆瓣抓取</title>
</head>
<body>
	<%--
		/*String date = new Date().toLocaleString();

		// IDouFetchTmpDao ftd = LogicFactory.getDouFetchTmpDao();
		String sql = "";
		//sql = "call pc_fc()";
		sql = "call pc_ft()";
		// Query query = ftd.getEntityManager().createNativeQuery(sql);
		List rs = query.getResultList();
		out.println("执行：" + sql + ";<br/>");
		Map<String, Integer> data = new HashMap<String, Integer>();
		for (int i = 0, size = rs.size(); i < size; i++) {
			Object[] row = (Object[]) rs.get(i);
			out.println("&nbsp;&nbsp;" + row[0] + " = " + row[1]);
			out.println("<br/>");
			if (((String) row[0]).equals("exe")) {
				continue;
			}
			data.put((String) row[0], Integer.parseInt((String) row[1]));
		}

		int total = data.get("book");
		int trig = 35;
		int h = total / ((60 / trig) * BookAction.FETCH_BOOK_PERS) / 60;
		out.println("<br/>图书——预计抓取剩余时间：<b>" + h + "</b>&nbsp;小时<br/>");
		
		total = data.get("relist");
		trig = 14;
		h = total / ((60 / trig) * BookAction.FETCH_REVIEWS_PERS) / 60;
		out.println("<br/>评论列表——预计抓取剩余时间：<b>" + h + "</b>&nbsp;小时<br/>");
		
		total = data.get("redetail");
		trig = 15;
		h = total / ((60 / trig) * BookAction.FETCH_REVIEWD_PERS) / 60;
		out.println("<br/>评论详情——预计抓取剩余时间：<b>" + h + "</b>&nbsp;小时<br/>");
		*/
	--%>
	<hr />
</body>
</html>