
#删除下架商品，不再抓取和维护
select count(*) from jd_queue where qstatus=3;

delete from jd_queue where qstatus=3;

delete from jd_queue where qstatus>0;

select id from jd_queue where qstatus>0 order by ftime 

delete from jd_queue where id in(
select id from (
select id from jd_queue where qstatus>0 order by ftime limit 1908) t );

select count(*) from jd_queue;

select count(*) from jd_queue where qstatus>0;

update jd_queue set qstatus=0 where qstatus>0;


select q.id,p.pname from jd_queue q,jd_product p where q.qurl=p.purl and q.qstatus=1 order by q.id desc limit 10;


select count(p.pid) from jd_product p where 
	not exists (select 1 from jd_reviews r where p.pid=r.pid);

 

mysqldump -hlocalhost -uroot -proot dm_jd | gzip > d:/mysql/bak/dm_jd_%date:~0,4%%date:~5,2%%date:~8,2%.sql.gz


商品 >> 队列：
	队列总数：13001
	已处理：10808
	待处理：1192

评论 >> 队列：
	队列总数：5239
	已处理：5218
	待处理：16

 