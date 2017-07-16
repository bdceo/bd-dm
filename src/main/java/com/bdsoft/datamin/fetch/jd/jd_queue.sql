
#查询待抓
select count(1) from jd_queue
union
select count(1) from jd_queue where qstatus>0;
#删除所有已抓
delete from jd_queue where qstatus>0;
#删除指定
delete from jd_queue where id in (4569020,4577233);
#删除指定长度
delete from jd_queue where length(qurl)=34;

#查看最小长度和最大长度url
select min(length(qurl)) from jd_queue
union 
select max(length(qurl)) from jd_queue;

select length('http://item.jd.com/10296391.html') ; #32
select length('http://item.jd.com/1029546873.html') ; #34

# 统计指定长度url
select count(1) from jd_queue where length(qurl)=30;
# 查询指定长度url
select * from jd_queue where length(qurl)=30 limit 10;
# 查看
select * from jd_queue order by id desc limit 50;


select count(1) from jd_queue where length(qurl)=30
union
select count(1) from jd_queue where length(qurl)=31
union
select count(1) from jd_queue where length(qurl)=32
union
select count(1) from jd_queue where length(qurl)=33
union
select count(1) from jd_queue where length(qurl)=34;


select now();

