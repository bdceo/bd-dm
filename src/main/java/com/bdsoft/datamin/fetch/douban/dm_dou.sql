
select 'books',max(id) from dou_book
union
select 'buylinks',max(id) from dou_buylink  
union
select 'reviews',max(id) from dou_review  
union 
select 'users',max(id) from dou_user;

#11-书的总评；12-书的单评；21-书的导购；31-推荐的书

select 'reviews',count(*) from dou_fetch_tmp where fetch_flag='11' and fetch_stat=1
union
select 'reviewd',count(*) from dou_fetch_tmp where fetch_flag='12' and fetch_stat=1
union
select 'buylinks',count(*) from dou_fetch_tmp where fetch_flag='21' and fetch_stat=1
union
select 'books',count(*) from dou_fetch_tmp where fetch_flag='31' and fetch_stat=1;

select * from dou_fetch_tmp where fetch_flag='11' and fetch_stat=1 
order by id limit 5;
select * from dou_fetch_tmp where fetch_flag='12' and fetch_stat=1 
order by id limit 5;
select * from dou_fetch_tmp where fetch_flag='21' and fetch_stat=1 
order by id limit 5;
select * from dou_fetch_tmp where fetch_flag='31' and fetch_stat=1 
order by id limit 5;

select * from dou_fetch_tmp where fetch_url='http://book.douban.com/subject/2364312/reviews?score=&start=2750';

select * from dou_book where book_name like'%soa%';
#Java并发编程实战  9787111370048
#
select * from dou_book where book_isbn='9787111370048';
select * from dou_review where book_isbn='9787111370048';
select * from dou_buylink where book_isbn='9787111370048';

select count(*) from dou_fetch_tmp where fetch_flag='11' and fetch_stat=3;

update dou_fetch_tmp set fetch_stat=1 where fetch_stat=3 and fetch_flag='11';

select count(*) from weibo_mail where status=0
union
select count(*) from weibo_mail where status=1
union
select count(*) from weibo_mail where status=2;

#update weibo_mail set status=0 where status=2; 

select * from dou_book where dou_url='http://book.douban.com/subject/4242094/';

