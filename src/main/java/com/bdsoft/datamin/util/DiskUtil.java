package com.bdsoft.datamin.util;

import java.io.File;

/**
 * 磁盘检测
 * <p>
 *
 * @author   丁辰叶
 * @date	 2016-5-5
 * @version  1.0.0
 */
public class DiskUtil {

	private static File[] space = File.listRoots();

	public static void main(String[] args) {
		for (File f : space) {
			System.out.println(f.getPath());
			long total = f.getTotalSpace() / 1024 / 1024 / 1024;
			long uesed = f.getUsableSpace() / 1024 / 1024 / 1024;
			long free = f.getFreeSpace() / 1024 / 1024 / 1024;
			System.out.print("总大小：" + total);
			System.out.print("G，已使用：" + uesed);
			System.out.println("G，可用：" + free + "G");
		}
	}

	public static long getFree(String name) {
		long free = 0L;
		for (File f : space) {
			String path = f.getPath().toLowerCase();
			if (path.contains(name.toLowerCase())) {
				free = f.getFreeSpace() / 1024 / 1024 / 1024;
				System.out.println(name + " 盘，可用空间：" + free);
				break;
			}
		}
		return free;
	}

}
