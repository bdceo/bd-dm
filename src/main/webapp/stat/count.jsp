<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>邮箱抓取统计</title>
<script type="text/javascript" src="../js/jscharts.js"></script>
</head>
<body>
<div id="graph">加载统计图...</div>
<%--
	IWeiboFetchDao imw = LogicFactory.getWeiboFetchDao();
	String sql = "select date(fetch_date),count(*) from weibo_fetch group by date(fetch_date)";
	List<Object> objs = imw.findBySQL(sql);
	Map<String,Integer> data = new LinkedHashMap<String,Integer>();
	data.put("10-15", 2251);
	data.put("10-16", 4513);
	//data.put("10-17", 0);
	for(Object o : objs){
		Object[] oo = (Object[])o;
		String t = StringUtil.nullToEmpty(oo[0]);
		Integer dc = ParseUtil.getIntValue(oo[1]);
		data.put(t.substring(t.indexOf("-")+1), dc); 
	}
	for(Map.Entry<String,Integer> en : data.entrySet()){
		System.out.println(en.getKey());
		System.out.println(en.getValue());
	}	
	sql = ""; 
--%>
<!-- 
<script type="text/javascript">
	
	var data = new Array(<%-- =data.size()%>);
	<%	int i=0;
		for(Map.Entry<String,Integer> en : data.entrySet()){%>
			data[<%=i%>] = ['<%=en.getKey()%>',<%=en.getValue()%>];
		<% i++;}
	%>
	
	var myChart = new JSChart('graph', 'line');
	myChart.setDataArray(data);
	myChart.setTitle('每日邮箱抓取统计');
	myChart.setTitleColor('#000000');
	myChart.setTitleFontSize(15);
	myChart.setAxisNameX('');
	myChart.setAxisNameY('');
	myChart.setAxisColor('#8420CA');
	myChart.setAxisValuesColor('#949494');
	myChart.setAxisPaddingLeft(100);
	myChart.setAxisPaddingRight(120);
	myChart.setAxisPaddingTop(50);
	myChart.setAxisPaddingBottom(40);
	myChart.setAxisValuesDecimals(3);
	myChart.setAxisValuesNumberX(10);
	myChart.setShowXValues(false);
	myChart.setGridColor('#C5A2DE');
	myChart.setLineColor('#BBBBBB');
	myChart.setLineWidth(3);
	myChart.setFlagColor('#9D12FD');
	myChart.setFlagRadius(3); 
		<%	
	for(Map.Entry<String,Integer> en : data.entrySet()){%>
		myChart.setTooltip(['<%=en.getKey()%>', 'Mail:<%=en.getValue()%>']);	
		myChart.setLabelX(['<%=en.getKey()%>', '<%=en.getKey()%>']);
	<%} --%> 
	myChart.setSize(1000, 500);
	//myChart.setBackgroundImage('../img/chart_bg.jpg');
	myChart.draw(); 
</script>   -->

</body>
</html>