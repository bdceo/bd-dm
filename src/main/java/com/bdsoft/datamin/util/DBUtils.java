package com.bdsoft.datamin.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bdsoft.datamin.util.cmd.MySQLUtil;
import com.beust.jcommander.internal.Lists;

public class DBUtils {

	// 存错过程定义
	public static enum DBProc {
		JD_MAX("pc_jd_max"), JD_RST("pc_jd_rst");

		private String call;

		private DBProc(String call) {
			this.call = call;
		}

		public String getCall() {
			return "call " + this.call + "();";
		}

		public String getCall(String[] ins) {
			StringBuffer sb = new StringBuffer("call ");
			sb.append(this.call);
			sb.append("('");
			sb.append(ins[0]);
			if (ins.length > 1) {
				for (int i = 1; i < ins.length; i++) {
					sb.append("','");
					sb.append(ins[i]);
				}
			}
			sb.append("');");
			return sb.toString();
		}
	}

	private static DBUtils dbu = null;

	private DBUtils() {
	}

	public static DBUtils getInstance() {
		if (dbu == null) {
			dbu = new DBUtils();
		}
		return dbu;
	}

	// 参考：http://depthjava.iteye.com/blog/410344
	public static void main(String[] args) {
		DBUtils db = DBUtils.getInstance();
		// db.callProc(DBProc.JD_MAX);
		db.callProc(DBProc.JD_RST, "16");
	}

	public Map<String, Integer> callProc(DBProc proc) {
		String sql = proc.getCall();
		System.out.println(sql);
		// TODO mybatis 调用存储过程
		List res = Lists.newArrayList();
		Map<String, Integer> data = new HashMap<String, Integer>();
		for (int i = 0; i < res.size(); i++) {
			Object[] row = (Object[]) res.get(i);
			// System.out.println(">>" + row[0] + "-" + row[1]);
			data.put((String) row[0], (Integer) row[1]);
		}
		System.out.println(data);
		return data;
	}

	// 可变长读参数
	public void callProc(DBProc proc, String... ins) {
		String sql = proc.getCall(ins);
		System.out.println(">>" + sql);

		Connection conn = null;
		try {
			conn = MySQLUtil.getCon();
			conn.setAutoCommit(false);
			CallableStatement call = conn.prepareCall(sql);
			int ok = call.executeUpdate();
			System.out.println(sql + " >> " + ok);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
			}
		}
	}

}
