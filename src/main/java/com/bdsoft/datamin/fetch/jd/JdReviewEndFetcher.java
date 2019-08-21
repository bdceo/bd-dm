package com.bdsoft.datamin.fetch.jd;

import com.bdsoft.datamin.entity.JDProduct;

/**
 * 评论抓取子线程，结束抓取逻辑
 */
public class JdReviewEndFetcher implements Runnable {

    private JdFetchQueues queues;

    private JdReviewFetcher holdThread;

    private JDProduct jdp;

    public JdReviewEndFetcher(JdFetchQueues queues, JdReviewFetcher holdThrd, JDProduct jdp) {
        this.queues = queues;
        this.holdThread = holdThrd;
        this.holdThread.setThisPause(true);
        this.jdp = jdp;
    }

    /**
     * 结束抓取逻辑实现
     */
    @Override
    public void run() {
        try {
            // 刷新评论抓取统计
            queues.ctREVsuc();
            // 所有评论子线程抓取完毕，通知触发抓取下一个商品的评论
            this.holdThread.setThisPause(false);
            System.out.println(this.holdThread.getName() + ",所有子线程执行完毕.");

            // 数据库异常
            if (!JdConfig.MYSQL_OK.get()) {
                // 将正在抓取的商品重新放进评论队列
                queues.reEnREVqueue(this.jdp);
                System.out.println("数据库异常，导致抓取评论失败的商品，已重新放入队列");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}