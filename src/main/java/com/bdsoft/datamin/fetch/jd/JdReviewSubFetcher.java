package com.bdsoft.datamin.fetch.jd;

import com.bdsoft.datamin.entity.JDProduct;
import com.bdsoft.datamin.fetch.jd.review.FetchReview;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.concurrent.CyclicBarrier;

/**
 * 评论抓取子线程，具体负责抓取评论逻辑实现
 */
public class JdReviewSubFetcher implements Runnable {

    private String name;

    // 并发-障碍器
    private CyclicBarrier cb;

    // 商品信息，及分页范围
    private JDProduct jdp;

    private int start;

    private int end;

    // 评论抓取
    private FetchReview fetcher;

    public JdReviewSubFetcher(JDProduct jdp, int s, int e, CyclicBarrier cb, String name) {
        this.jdp = jdp;
        this.start = s;
        this.end = e;
        this.cb = cb;
        this.name = name;
        this.fetcher = new FetchReview();
    }

    /**
     * 抓取子线程实现逻辑
     */
    @Override
    public void run() {
        try {
            // 执行抓取，指派抓取范围，从start-end页
            System.out.println(name + "，启动...");
            fetcher.fetch(jdp, start, end);
            System.out.println(name + "，执行完毕.");
        } catch (CannotCreateTransactionException pe) {
            System.out.println(name + "，检测到数据库异常：" + pe.getMessage());
            // 改变相关服务状态，在障碍器最终线程中执行错误上报
            JdConfig.MYSQL_OK.compareAndSet(!JdConfig.MYSQL_HANDING.get(), false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                cb.await(); // 成功与否，都会通知障碍器，子线程抓取完毕
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
