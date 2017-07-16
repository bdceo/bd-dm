
初始化种子url：

	1，抓取图书排行榜，入 jd_queue 库
		执行：com.bdsoft.datamin.fetch.jd.booktop.FetchBookTop.java
		
	2，抓取所有分类地址第一页商品地址，输出到：seeds.txt 文件
		执行：com.bdsoft.datamin.fetch.jd.cattegory.FetchCattegory.java   抓取分类导航页，入 jd_queue_cats 库
		执行：com.bdsoft.datamin.fetch.jd.cattegory.FetchProductUrl.java	抓取各分类首页商品，输出：seeds.txt 文件
		
	3，提取 seeds 文件，入库 jd_queue 库
		执行：com.bdsoft.datamin.fetch.jd.data.ImportUrl.java
		