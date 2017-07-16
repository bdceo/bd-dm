package com.bdsoft.datamin.fetch.jd.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bdsoft.datamin.entity.JDQueue;
import com.bdsoft.datamin.fetch.jd.JDFetcher;
import com.bdsoft.datamin.fetch.jd.cattegory.FetchProductUrl;

/**
 * 导入种子url
 * 
 * @author 丁辰叶
 * @dete 2016-09-29
 */
public class ImportUrl extends JDFetcher {

	public static void main(String[] args) {
		ImportUrl iu = new ImportUrl();
		System.out.println("-----------------------------------------");

		// 1-批量导入
		int fileTotal = 7;
		int fileStart = 3;
		String fileFlag = "#N#";
		String fileName = "jd_item_url_" + fileFlag + ".txt";

		for (int i = fileStart; i <= fileTotal; i++) {
			String file = fileName.replaceAll(fileFlag, i + "");
			iu.readDataFile(file);
		}

		// 2-导入一个
		iu.readDataFile(FetchProductUrl.SEED_FILE);

		System.out.println("URL导入完毕");
	}

	public void readDataFile(String path) {
		BufferedReader br = null;
		File f = new File(path);
		if (f.exists()) {
			try {
				br = new BufferedReader(new FileReader(f));
				String line = br.readLine();
				while (line != null) {
					saveUrl(line);
					line = br.readLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
	}

	public void saveUrl(String url) {
		System.out.println(url);
		Pattern pat = Pattern.compile(".*(http://item.jd.com/[\\d]+.html).*");
		Matcher ma = pat.matcher(url);
		if (ma.find()) {
			url = ma.group(1).trim();
			System.out.println(">>" + url);
			JDQueue jdq = new JDQueue(url);
			jdq = jdqMapper.selectOne(jdq);
			if (jdq != null) {
				System.out.println("URL已存在 >> " + jdq.getId());
			} else {
				jdq = new JDQueue(url, 1);
				int is = jdqMapper.insertSelective(jdq);
				System.out.println((is > 0) ? "URL入库成功 >> " + url : "URL入库失败>>" + url);
			}
		}
	}

}
