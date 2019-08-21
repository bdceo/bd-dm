package com.bdsoft.datamin.fetch.jd;

import com.bdsoft.datamin.entity.JDQueue;
import com.bdsoft.datamin.mapper.*;
import com.bdsoft.datamin.util.BDSpringUtil;

import java.util.Date;
import java.util.List;

/**
 * 持久层
 */
public class JdDao {

    private JDQueueMapper jdqDao;

    private JDUserMapper jduDao;

    private JDReviewsMapper jdrDao;

    private JDProductMapper jdpDao;

    private JDVenderMapper jdvDao;

    public JdDao() {
        this.jdqDao = BDSpringUtil.getBean(JDQueueMapper.class);
        this.jduDao = BDSpringUtil.getBean(JDUserMapper.class);
        this.jdrDao = BDSpringUtil.getBean(JDReviewsMapper.class);
        this.jdpDao = BDSpringUtil.getBean(JDProductMapper.class);
        this.jdvDao = BDSpringUtil.getBean(JDVenderMapper.class);
    }

    // 从jd_queue中读取指定数目待抓URL
    public List<JDQueue> readJdQueue(int sum) {
        // String sql = "select * from jd_queue where qstatus=0 order by ctime "
        // + JDFetcher.LOAD_ORDER + " limit " + sum;
        List<JDQueue> jdqs = jdqDao.selectUnFetchQueue(JdConfig.LOAD_ORDER, sum);

        // 20131215：每次读取新队列数据时删除已抓商品，较耗时，独线程处理
        new Thread() {
            public void run() {
                System.out.println("单独线程，从DB中删除已抓商品URL");
                // jdqDao.excuteSQL("delete from jd_queue where qstatus>0");
                jdqDao.deleteFetchedQueue();
            }
        }.start();
        return jdqs;
    }

    // 更新抓取URL的状态
    public boolean updateJdQueue(JDQueue jdq) {
        if (jdq.getRtime() != null) {
            jdq.setFtime(jdq.getRtime());
            jdq.setRtime(new Date());
        } else {
            jdq.setFtime(new Date());
            jdq.setRtime(jdq.getFtime());
        }
        return jdqDao.updateSelectiveById(jdq) > 0;
    }

    // 删除URL
    public boolean deleteJdQueue(JDQueue jdq) {
        long id = jdq.getId();
        if (id != 0) {
            return jdqDao.deleteById(id * 1L) > 0;
        }
        return false;
    }

    // 统计用，读取各表数据量
    public int getJdQueueCount() {
        return jdqDao.selectCount();
    }

    public int getJdPdtCount() {
        return jdpDao.selectCount();
    }

    public int getJdUserCount() {
        return jduDao.selectCount();
    }

    public int getJdRevCount() {
        return jdrDao.selectCount();
    }

    public int getJdVndCount() {
        return jdvDao.selectCount();
    }
}
