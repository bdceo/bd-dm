package com.bdsoft.fetch;

import com.bdsoft.datamin.util.BDLogUtil;
import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;

public class HttpGzipTest {

	static {
		BDLogUtil.init();
	}

	public static void main(String[] args) {
		int flag = 0;
		String[] envs = { "localhost:8080", "bd-www.shejijia.com", "alpha-www.gdfcx.net:8080",
				"uat-www.gdfcx.net:8080" };
		String api = "http://" + envs[flag] + "/juranhome/caseBase/searchCase";
		
		api = "http://item.jd.com/2357091.html";

		BDHttpParam hp = BDHttpParam.init();
//		hp.addCookie("users",
//				"eyJtZW1iZXJfaWQiOiIxNDAxMjYwIiwieHNlc3Npb24iOiI4RDhENzAxNC1GQUQ1LTQ1MTctOTZCRS1CNUE2RkMwNDY2QzYiLCJhY3NfbWVtYmVyX2lkIjoiMjA3Mzg5NDQiLCJNZW1iZXJfVHlwZSI6Im1lbWJlciIsImhzX3VpZCI6IjYzMzhjMjk2LTQwMGEtNGQ1Yy04MmMxLWE5MmFjYjk3ZGQ3NyIsIlgtVG9rZW4iOiIxNTExMDgxNC1lYjNjLTQ2MjMtYjJjMi0yZWVkMzk1ZTNiNmQiLCJYX1Rva2VuIjoiMTUxMTA4MTQtZWIzYy00NjIzLWIyYzItMmVlZDM5NWUzYjZkIiwidXNlcl9uYW1lIjoiYmRjZW8iLCJtb2JpbGVfbm8iOiIiLCJhdmF0YXIiOiJodHRwOi8vdWF0NDE1aW1nLmdkZmN4Lm5ldDo4MDgyL2ltZy81N2NmN2I5MmVkNTAyYmNlMTJhYmZmNTQuaW1nIiwiZGl5X2p3dCI6ImV5SjBlWEFpT2lKS1YxUWlMQ0poYkdjaU9pSklVekkxTmlKOS5leUpCWTJObGMzTmZkRzlyWlc0aU9pSXhOVEV4TURneE5DMWxZak5qTFRRMk1qTXRZakpqTWkweVpXVmtNemsxWlROaU5tUWlMQ0pCWTJObGMzTmZkRzlyWlc1ZlpYaHdhWEpsWkY5aGRDSTZNVFEzTkRNMk5ETTBNall5T1gwLnpmcTAwY2FGd0h6aDZSRDRGMndjaWlMSWItNHJ3bjBLdjlkYkVNSy1XdTAiLCJYLVRva2VuX2V4cCI6MTQ3NDM2NDM0MjYyOSwiQUNTLVRva2VuIjoidWNJbElGcS1DTHZiLkFuLU84T3Vncks5UFV2LUlJUEZrc0VNU2JNZ2w2QTBPZkZsIiwidG9rZW4iOiIwQ0QyNzQxNTc4OUE5RTRBNDFFMzAwNzkzN0EzRjQ3RiIsImV6X2d1aWQiOiI2QzMzRDgxMzdGNUQ1NEUwMjFGMDQ5OUFBQjgzNjgwQiJ9");
//		hp.addCookie("JSESSIONID", "FE5D811D07497F3DB2E715485EACA31E");
//		hp.addCookie("CNZZDATA1259093497",
//				"148216172-1474277938-http%253A%252F%252Flocalhost%253A8080%252F%7C1474343684");
//		hp.addCookie("browsers", "browsersok");

		hp.addHeader("Accept-Encoding", "gzip");

//		hp.addCommon("page", "0");
//		hp.addCommon("pageSize", "12");
//		hp.addCommon("type", "3d");

		String res = BDHttpUtil.sendPost(api, hp);
//		System.out.println(res);

		System.out.println(String.format("响应数据>%dK", res.length() / 1024));

	}

}
