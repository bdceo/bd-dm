/**
 * CutImage.java
 * bd-dm
 * Copyright (c) 2016, bdsoft版权所有.
*/
package com.bdsoft.img;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.bdsoft.datamin.util.BDFileUtil;

/**
 * CutImage
 * 
 * @author bdceo
 * @date 2016-9-19 上午7:03:25
 * @version V1.0
 */
public class CutImage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File img = new File("/home/dcy/tmp/6p.jpg");
		String type = BDFileUtil.getFileType(img);
		String name = BDFileUtil.getFileName(img) + "-6." + type;
		String out = img.getParent() + File.separator + name;
		try {
			BufferedImage src = ImageIO.read(img);
			int w = src.getWidth();
			int h = src.getHeight();

			int nw = h, nh = h, x = w/6*5, y = 0;
			System.out.println(String.format("nw=%d, nh=%d, x=%d, y=%d", nw,nh,x,y));
			
			BufferedImage dest = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = dest.createGraphics();
			g.fillRect(0, 0, nw, nh);
			// 前四个坐标--输出：起始坐标-->终点坐标
			// 后四个坐标--输入：起始坐标-->终点坐标
			g.drawImage(src, 0, 0, nw, nh, x, y, w, nh, null);
			ImageIO.write(dest, type, new File(out));
			System.out.println("图片提取到 >> " + out);
		} catch (Exception e) {
				e.printStackTrace();  
		}
	}

}
