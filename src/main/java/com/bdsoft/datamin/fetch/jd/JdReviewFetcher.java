package com.bdsoft.datamin.fetch.jd;

import com.bdsoft.datamin.entity.JDProduct;
import com.bdsoft.datamin.fetch.jd.review.FetchReview;

import java.util.concurrent.CyclicBarrier;

/**
 * 评论队列-消费者线程
 */
public class JdReviewFetcher extends Thread {

    private JdFetchQueues queues; // 队列访问支持

    private long subSleep = 1000L * 5;// 秒，子线程抓取过程，检测时间间隔

    private long waitSleep = 1000L * 3;// 秒，队列空，等待时间

    private long subStartSleep = 1L * 100;// 毫秒，子线程启动延迟

    private boolean thisPause = false; // 子线程是否在执行抓取

    public JdReviewFetcher(JdFetchQueues queues) {
        this.queues = queues;
    }

    public void setThisPause(boolean thisPause) {
        this.thisPause = thisPause;
    }

    /**
     * 抓取评论线程逻辑实现
     */
    @Override
    public void run() {
        while (true) {
            try {
                // 检测DB状态
                if (!JdConfig.MYSQL_OK.get()) {
                    System.out.println(getName() + ",MySQL服务维护中，等待...");
                    Thread.sleep(JdConfig.MYSQL_START_WAIT);
                    continue;
                }

                // 如果子线程正在抓取，等待子线程执行完毕再开始下一个商品的评论抓取
                if (thisPause) {
                    System.out.println(getName() + ",子线程正在抓取，等待...");
                    Thread.sleep(subSleep);
                    continue;
                }

                // 如果评论队列空，需要等待
                JDProduct jdp = queues.deREVqueue();
                if (jdp == null) {
                    System.out.println(getName() + ",评论队列空，等待...");
                    Thread.sleep(waitSleep);
                    continue;
                }

                // 计算分页数，使得子线程可以平均分发处理抓取评论
                int count = jdp.getRvecount();// 商品总评论数
                if (count > 0) {
                    int mod = count % FetchReview.RLIST_PAGESIZE;
                    int shang = count / FetchReview.RLIST_PAGESIZE;
                    int pages = (mod == 0) ? shang : shang + 1;// 商品评论总页数

                    mod = pages % JdConfig.JD_REV_SUB_FETCHER;// 是否最大线程 可以正好平均处理
                    int per = pages / JdConfig.JD_REV_SUB_FETCHER; // 平均每个线程处理多少页数据

                    int thrds = (pages <= JdConfig.JD_REV_SUB_FETCHER) ? pages : JdConfig.JD_REV_SUB_FETCHER;// 计划分配执行抓取的线程数
                    System.out.println("\t商品评论总页数：" + pages + "，计划启动子线程数：" + thrds + "，URL=" + jdp.getPurl());

                    // 所有子线程任务结束处理逻辑
                    JdReviewEndFetcher endTask = new JdReviewEndFetcher(queues, this, jdp);
                    // 并发障碍器
                    CyclicBarrier cb = new CyclicBarrier(thrds, endTask);
                    // 启动子线程分发处理，分页抓取评论
                    int s = 0, e = 0;
                    if (pages <= JdConfig.JD_REV_SUB_FETCHER) {
                        for (int i = 0; i < thrds; i++) {
                            String n = getName() + "-#子线程#>" + (i + 1);
                            e++;
                            Runnable task = new JdReviewSubFetcher(jdp, s, e, cb, n);
                            new Thread(task).start();
                            s = e;
                            Thread.sleep(subStartSleep);
                        }
                    } else {
                        for (int i = 0; i < thrds; i++) {
                            String n = getName() + "-#子线程#>" + (i + 1);
                            e = (i + 1) * per;
                            if ((i == (thrds - 1)) && (mod > 0)) {
                                e += mod;
                            }
                            Runnable task = new JdReviewSubFetcher(jdp, s, e, cb, n);
                            new Thread(task).start();
                            s = e;
                            Thread.sleep(subStartSleep);
                        }
                    }
                } else {
                    queues.ctREVsuc();
                    System.out.println("\t商品无评论：" + jdp.getPurl());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
