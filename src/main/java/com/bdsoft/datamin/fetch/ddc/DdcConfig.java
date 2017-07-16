package com.bdsoft.datamin.fetch.ddc;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.BDLogUtil;
import com.bdsoft.datamin.util.Utils;
import com.google.common.collect.Lists;

/**
 * 电动车抓取配置
 * 
 * @author 丁辰叶
 * @date 2016-7-11
 * @version 1.0.0
 */
public class DdcConfig {

	// 默认存储路径
	public static String BASE_STORE = "d:";

	static {
		System.out.println(111);
		BDLogUtil.init();

		// 判断操作系统
		if (Utils.isMacos()) {
			BASE_STORE = "/users/bdceo";
		}
		System.out.println("--" + BASE_STORE);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(222);
		System.out.println(Utils.getOsName());
		System.out.println(BASE_STORE);
	}

	public static String getOut(File file, String subfix) {
		String type = BDFileUtil.getFileType(file);
		String name = BDFileUtil.getFileName(file) + "-" + subfix + "." + type;
		return file.getParent() + File.separator + name;
	}

	public static List<File> listSub(String rootDir) {
		File root = new File(rootDir);
		File[] dirs = root.listFiles();
		List<File> fl = Lists.newArrayList();
		for (File dir : dirs) {
			File[] imgs = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith("png");
				}
			});
			fl.addAll(Arrays.asList(imgs));
		}
		return fl;
	}

	public static List<File> list(String rootDir, final String subfix) {
		File root = new File(rootDir);
		File[] imgs = root.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				name = name.toLowerCase();
				return name.endsWith(subfix) && (name.contains("00x") || name.contains("-zfx"));
			}
		});
		if (imgs.length > 0) {
			for (File img : imgs) {
				img.delete();
			}
		}
		imgs = root.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(subfix);
			}
		});
		return Arrays.asList(imgs);
	}
}
