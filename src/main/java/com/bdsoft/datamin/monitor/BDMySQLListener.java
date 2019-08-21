package com.bdsoft.datamin.monitor;


/**
 * Created by Administrator on 2019/8/21.
 */
public class BDMySQLListener implements BDListener {

    private boolean handling = false;

    private Object lock = new Object();

    public BDMySQLListener() {
    }

    public void handle() {
    }
}
