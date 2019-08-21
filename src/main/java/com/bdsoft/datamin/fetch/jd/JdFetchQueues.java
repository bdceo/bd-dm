package com.bdsoft.datamin.fetch.jd;

import com.bdsoft.datamin.entity.JDProduct;
import com.bdsoft.datamin.entity.JDQueue;
import com.bdsoft.datamin.monitor.BDListener;
import com.bdsoft.datamin.monitor.BDMySQLListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 京东抓取队列
 */
public class JdFetchQueues {

    // 访问DB支持
    private JdDao db;

    // 商品队列
    List<JDQueue> pdtQueue = Collections.synchronizedList(new ArrayList<JDQueue>());

    // 评论队列
    List<JDProduct> revQueue = Collections.synchronizedList(new ArrayList<JDProduct>());

    // 服务监控
    List<BDListener> serviceLis = new ArrayList<>();

    // 失败抓取，最大尝试重新抓取次数
    private int maxRetry = 5;

    // 失败抓取队列，统计
    ConcurrentMap<String, AtomicInteger> retryJdqC = new ConcurrentHashMap<String, AtomicInteger>();

    AtomicLong pdtQcount = new AtomicLong(0);// 商品队列统计

    AtomicLong pdtQsuc = new AtomicLong(0);// 商品抓取成功统计

    AtomicLong revQcount = new AtomicLong(0);// 评论队列统计

    AtomicLong revQsuc = new AtomicLong(0);// 评论抓取成功统计

    public JdFetchQueues(JdDao db) {
        this.db = db;
        // 注册当前支持的监控事件
        serviceLis.add(new BDMySQLListener());
    }

    // 触发服务监听，遍历各服务状态，处理问题
    public synchronized void errReport() {
        System.out.println("接到错误事件上报...");
        for (BDListener lis : serviceLis) {
            System.out.println("处理错误事件");
            lis.handle();
        }
    }

    /**
     * 入商品队列，刷新统计
     *
     * @param jdqs
     */
    public synchronized void enPDTqueue(List<JDQueue> jdqs) {
        pdtQueue.addAll(jdqs);
        pdtQcount.addAndGet(jdqs.size());
        this.dump();
    }

    /**
     * 入商品队列，初始放入 或 出错放入以备重新抓
     *
     * @param jdq
     * @param reEnQ 是否出错重新放
     */
    public synchronized void enPDTqueue(JDQueue jdq, boolean reEnQ) {
        if (reEnQ) {
            String url = jdq.getUrl();
            AtomicInteger c = retryJdqC.get(url);
            if (c == null) {
                retryJdqC.put(url, new AtomicInteger(1));
            } else {
                System.out.println("商品抓取失败：" + jdq.getUrl());
                if (c.get() >= maxRetry) {
                    retryJdqC.remove(url);// 超过指定次数，移除
                    ctPDTsuc();
                    jdq.setQstatus(2);
                    jdq.setFerr("商品抓取失败，超过" + maxRetry + "次");
                    db.updateJdQueue(jdq);// 更新数据库
                    return;
                }
                // 刷新尝试次数
                c.incrementAndGet();
                retryJdqC.put(url, c);
            }
        }
        pdtQueue.add(jdq);
        pdtQcount.incrementAndGet();
    }

    public synchronized void reEnPDTqueue(JDQueue jdq) {
        pdtQueue.add(jdq);
        pdtQcount.incrementAndGet();
    }

    /**
     * 从商品队列提取一个URL
     *
     * @return
     */
    public synchronized JDQueue dePDTqueue() {
        if (pdtQueue.size() == 0) {
            return null;
        }
        JDQueue jdq = pdtQueue.remove(0);
        return jdq;
    }

    // 刷新商品队列成功数统计
    public void ctPDTsuc() {
        pdtQsuc.incrementAndGet();
    }

    // 抓取成功，从尝试抓取队列中移除统计
    public void rmvRetry(JDQueue jdq) {
        retryJdqC.remove(jdq.getUrl());
    }

    /**
     * 入评论队列
     *
     * @param jdp
     */
    public synchronized void enREVqueue(JDProduct jdp) {
        revQueue.add(jdp);
        revQcount.incrementAndGet();
    }

    public synchronized void reEnREVqueue(JDProduct jdp) {
        revQueue.add(0, jdp);
        revQcount.incrementAndGet();
    }

    /**
     * 从评论队列提取一个URL
     *
     * @return
     */
    public synchronized JDProduct deREVqueue() {
        if (revQueue.size() == 0) {
            return null;
        }
        return revQueue.remove(0);
    }

    // 刷新评论队列成功数统计
    public void ctREVsuc() {
        revQsuc.incrementAndGet();
    }

    public int getPDTQsize() {
        this.dump();
        return pdtQueue.size();
    }

    public int getREVQsize() {
        this.dump();
        return revQueue.size();
    }

    // 检查评论队列，以免待抓评论太多
    public synchronized boolean checkREVqueue() {
        if (getREVQsize() > JdConfig.maxREVqsize) {
            return false;
        }
        return true;
    }

    // 输出队列统计
    public String dump() {
        StringBuffer console = new StringBuffer();
        console.append("\n----------------------------------------");
        console.append("\n商品 >> 队列：");
        console.append("\n\t队列总数：" + pdtQcount.get());
        console.append("\n\t已处理：" + pdtQsuc.get());
        console.append("\n\t待处理：" + pdtQueue.size());
        console.append("\n");
        console.append("\n评论 >> 队列：");
        console.append("\n\t队列总数：" + revQcount.get());
        console.append("\n\t已处理：" + revQsuc.get());
        console.append("\n\t待处理：" + revQueue.size());
        console.append("\n");

        console.append(",[Pt-" + JdConfig.JD_PRO_FETCHER);
        console.append(",Rt-" + JdConfig.JD_REV_FETCHER);
        console.append("/" + JdConfig.JD_REV_SUB_FETCHER + "]");

        console.append("\n统计时间：" + new Date().toLocaleString());
        console.append("\n----------------------------------------");
        console.append("\n");
        String dump = console.toString();
        System.out.println(dump);
        return dump;
    }
}
