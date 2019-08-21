package com.bdsoft.datamin.fetch.jd;

import com.bdsoft.datamin.entity.JDProduct;
import com.bdsoft.datamin.entity.JDQueue;
import com.bdsoft.datamin.fetch.jd.product.FetchProduct;
import com.bdsoft.datamin.util.StringUtil;
import org.springframework.transaction.CannotCreateTransactionException;

/**
 * 商品队列-消费者线程
 */
public class JdProductFetcher extends Thread {

    private JdDao db;

    private JdFetchQueues queues; // 队列访问支持

    private FetchProduct fetcher;// 抓取商品实现类

    private long fetcherSleep = 1L * 100;// 毫秒，抓取成功休眠间隔时间

    private long waitSleep = 1000L * 3;// 秒，队列空等待时间

    private long reviewSleep = 1000L * 15;// 秒，评论队列过大时等待时间

    public JdProductFetcher(JdDao db, JdFetchQueues queues) {
        this.db = db;
        this.queues = queues;
        this.fetcher = new FetchProduct();
    }

    /**
     * 抓取商品线程逻辑实现
     */
    @Override
    public void run() {
        while (true) {
            JDQueue jdq = null;
            try {
                // 检测DB状态
                if (!JdConfig.MYSQL_OK.get()) {
                    System.out.println(getName() + ",MySQL服务维护中，等待...");
                    Thread.sleep(JdConfig.MYSQL_START_WAIT);
                    continue;
                }

                // 检查评论队列，如果评论抓取慢，需要等待
                if (!queues.checkREVqueue()) {
                    System.out.println(getName() + ",待抓评论总数超过:" + JdConfig.maxREVqsize + "，等待...");
                    Thread.sleep(reviewSleep);
                    continue;
                }

                // 如果商品队列空，需要等待
                jdq = queues.dePDTqueue();
                if (jdq == null) {
                    System.out.println(getName() + ",商品队列空，等待...");
                    Thread.sleep(waitSleep);
                    continue;
                }

                // 执行抓取
                JDProduct jdp = fetcher.fetch(jdq);
                if (jdp == null) { // 抓取失败，重新放入队列，等待再次尝试抓取
                    queues.enPDTqueue(jdq, true);
                } else {
                    // 抓取成功，刷新统计数
                    queues.ctPDTsuc();
                    queues.rmvRetry(jdq);

                    // 2013-12-09，取消抓取衣服类商品
                    if (jdp.getRvecount() > 0) {
                        if (StringUtil.isEmpty(jdp.getCat1Code())) {
                            System.out.println("分类：一级分类未知，暂不抓取评论");
                            // } else if
                            // (!StringUtil.isEmpty(jdp.getCat1Code())
                            // && jdp.getCat1Code().equals("1315")) {
                            // System.out.println("分类：服饰鞋帽，暂不抓取评论");
                        } else {
                            // 触发评论抓取队列
                            queues.enREVqueue(jdp);
                            System.out.println(getName() + "，成功入评论队列：" + jdp.getPname());
                        }
                    }

                    // 更新jd_queue表状态：抓取成功
                    jdq.setQstatus(1);
                    if (jdp.getPstat() == 3) {// 下架商品不再维护
                        jdq.setQstatus(3);// 不再抓取
                    }
                    db.updateJdQueue(jdq);
                }
                Thread.sleep(fetcherSleep);
            } catch (CannotCreateTransactionException pe) {
                System.out.println(getName() + "，检测到数据库异常：" + pe.getMessage());
                // 将正在抓取的商品重新放进商品队列
                queues.reEnPDTqueue(jdq);
                // 改变相关服务状态，上报错误
                JdConfig.MYSQL_OK.compareAndSet(!JdConfig.MYSQL_HANDING.get(), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}