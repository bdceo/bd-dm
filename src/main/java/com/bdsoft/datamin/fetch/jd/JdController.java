package com.bdsoft.datamin.fetch.jd;

import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.DBUtils;
import com.bdsoft.datamin.util.cmd.MySQLUtil;

/**
 * 抓取京东商品/评论
 *
 * @author 丁辰叶
 * @version 1.0.0
 * @date 2016-5-26
 */
public class JdController {

    static {
        // 检测mysql服务状态
        MySQLUtil.start();

        // 初始化spring容器
        BDSpringUtil.init();
    }

    /**
     * 入口
     */
    public static void main(String[] args) {
        // 从db读取商品url加入队列
        JdDao db = new JdDao();
        DBUtils du = DBUtils.getInstance();

        // --------------------------------------------------------------
        // 抓取队列核心控制类
        JdFetchQueues queues = new JdFetchQueues(db);

        // 商品队列-生产者线程
        JdProductFeeder jdpFeeder = new JdProductFeeder(db, queues, du);
        jdpFeeder.setName("#商品-队列线程#");
        jdpFeeder.start();

        // 商品队列-消费者线程 兼 评论队列-生产者线程
        for (int i = 0; i < JdConfig.JD_PRO_FETCHER; i++) {
            Thread jdpFetcher = new JdProductFetcher(db, queues);
            jdpFetcher.setName("#商品-抓取线程#>" + (i + 1));
            jdpFetcher.start();
        }

        // 评论队列-消费者
        for (int i = 0; i < JdConfig.JD_REV_FETCHER; i++) {
            Thread revFetcher = new JdReviewFetcher(queues);
            revFetcher.setName("#评论-抓取线程#>" + (i + 1));
            revFetcher.start();
        }
        // --------------------------------------------------------------

        // 数据库状态监控
        JdDBMoniter dbm = new JdDBMoniter();
        dbm.setDaemon(true);
        dbm.start();
    }

}
