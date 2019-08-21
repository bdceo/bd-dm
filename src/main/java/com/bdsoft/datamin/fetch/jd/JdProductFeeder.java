package com.bdsoft.datamin.fetch.jd;

import com.bdsoft.datamin.entity.JDQueue;
import com.bdsoft.datamin.util.DBUtils;
import com.bdsoft.datamin.util.DateUtil;
import com.bdsoft.datamin.util.mail.BDMailer;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品队列-生产者线程类
 */
public class JdProductFeeder extends Thread {

    private JdDao db; // db访问支持

    private JdFetchQueues queues; // 访问队列支持

    private DBUtils du = null;

    private long products = 0l;

    private long users = 0l;

    private long reviews = 0l;

    private long nowProducts = 0l;

    private long nowUsers = 0l;

    private long nowReviews = 0l;

    private int loop = 0;

    private Date lastDump = null;

    private long queueCheck = 1000L * 5;// 检测商品队列时间间隔

    public JdProductFeeder(JdDao db, JdFetchQueues queues, DBUtils du) {
        this.db = db;
        this.queues = queues;
        this.du = du;
    }

    /**
     * 生产者线程执行逻辑
     */
    @Override
    public void run() {
        init();
        while (true) {
            try {
                // 检测DB状态
                if (!JdConfig.MYSQL_OK.get()) {
                    System.out.println(getName() + ",MySQL服务维护中，等待...");
                    Thread.sleep(JdConfig.MYSQL_START_WAIT);
                    continue;
                }

                // 获取当前商品队列大小，小于检测阀值，触发加载新的URL入队列
                int size = queues.getPDTQsize();
                if (size <= JdConfig.minItem) {
                    reset(); // 刷新抓取统计起始值
                    dump();// 输出控制台，或发邮件

                    load();// 读DB，加载URL，入商品队列

                    // 新加载的数据，10分钟后才开始检测
                    Thread.sleep(1000 * 60 * 10);
                    continue;
                }
                Thread.sleep(queueCheck);
            } catch (CannotCreateTransactionException pe) {
                System.out.println(getName() + "，检测到数据库异常：" + pe.getMessage());
                // 改变相关服务状态，上报错误
                JdConfig.MYSQL_OK.compareAndSet(!JdConfig.MYSQL_HANDING.get(), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        // 读取数据库抓取统计
        Map<String, Integer> data = du.callProc(DBUtils.DBProc.JD_MAX);
        if (data.size() > 0) {
            this.products = this.nowProducts = data.get("product");
            this.users = this.nowUsers = data.get("user");
            this.reviews = this.nowReviews = data.get("review");
            System.out.println("最新统计：P-" + nowProducts + ",R-" + nowReviews + ",U-" + nowUsers);
        }
        // 第一次启动，初始化队列
        load();
    }

    private void load() {
        // 抓取周期统计
        loop++;
        lastDump = new Date();

        List<JDQueue> jdqs = db.readJdQueue(JdConfig.MAX_LOAD);
        DateUtil.cost("从DB提取URL队列", lastDump);

        System.out.println();
        if (jdqs != null && jdqs.size() > 0) {
            queues.enPDTqueue(jdqs);
            System.out.println(getName() + "，入商品队列=" + jdqs.size());
        }
    }

    public void reset() {
        try {
            Map<String, Integer> data = du.callProc(DBUtils.DBProc.JD_MAX);
            this.nowProducts = data.get("product");
            this.nowUsers = data.get("user");
            this.nowReviews = data.get("review");
            System.out.println("最新统计：P-" + nowProducts + ",R-" + nowReviews + ",U-" + nowUsers);
        } catch (CannotCreateTransactionException pe) {
            System.out.println(getName() + "，检测到数据库异常：" + pe.getMessage());
            // 改变相关服务状态，上报错误
            JdConfig.MYSQL_OK.compareAndSet(!JdConfig.MYSQL_HANDING.get(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 输出到控制台并发送统计邮件
    public void dump() {
        // 统计每轮抓取量
        long newPs = this.nowProducts - this.products;
        long newRs = this.nowReviews - this.reviews;
        long newUs = this.nowUsers - this.users;

        // 控制台输出和邮件内容
        StringBuffer console = new StringBuffer();
        console.append("----------------京东-抓取统计----------------------");
        console.append("\n统计第 <").append(loop).append("> 轮抓取");
        console.append("\n开始时间：").append(lastDump.toLocaleString());
        console.append("\n结束时间：").append(new Date().toLocaleString());
        console.append("\n\t商品总数：").append(this.nowProducts);
        console.append("\t新抓取：").append(newPs);
        console.append("\n\t评论总数：").append(this.nowReviews);
        console.append("\t新抓取：").append(newRs);
        console.append("\n\t用户总数：").append(this.nowUsers);
        console.append("\t新抓取：").append(newUs);
        console.append("\n----------------京东-抓取统计----------------------");

        // 邮件标题
        StringBuffer cheaf = new StringBuffer();
        cheaf.append("<").append(loop).append(">");
        cheaf.append("[P-").append(newPs).append(",R-").append(newRs).append(",U-").append(newUs).append("]");
        cheaf.append(",[Pt-").append(JdConfig.JD_PRO_FETCHER).append(",Rt-").append(JdConfig.JD_REV_FETCHER).append("/");
        cheaf.append(JdConfig.JD_REV_SUB_FETCHER).append("]");

        // 刷新统计
        this.products = this.nowProducts;
        this.reviews = this.nowReviews;
        this.users = this.nowUsers;

        String content = console.toString();
        System.out.println(content);

        String title = cheaf.toString();
        BDMailer.jdFetchCount(title, content);

        // TODO 写db，统计抓取效率{线程数，抓取一轮时间，抓取商品，评论，用户等数统计……}
        // 可以写成MapReduce任务统计
    }
}