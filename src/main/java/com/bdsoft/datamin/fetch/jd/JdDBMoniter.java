package com.bdsoft.datamin.fetch.jd;

import com.bdsoft.datamin.util.cmd.MySQLUtil;
import com.bdsoft.datamin.util.mail.BDMailer;

import java.util.Date;

/**
 * 数据库服务监控
 */
public class JdDBMoniter extends Thread {

    private long checkDBInteval = 1000 * 3;// 秒

    private String title = "京东抓取-数据库异常";

    private String msg = "数据库异常检测时间：";

    @Override
    public void run() {
        while (true) {
            try {
                if (JdConfig.MYSQL_OK.get()) {
                    System.out.println("数据库服务正常 @" + new Date().toLocaleString());
                    Thread.sleep(checkDBInteval);
                    continue;
                }
                System.out.println("数据库服务异常，开始处理");
                JdConfig.MYSQL_HANDING.set(true);
                // 发邮件通知
                BDMailer.alarm(this.title, this.msg);

                boolean op = MySQLUtil.start();
                System.out.println("处理结束，恢复状态");
                JdConfig.MYSQL_OK.set(op);
                JdConfig.MYSQL_HANDING.set(false);

                if (op) {
                    BDMailer.regain(this.title, "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}