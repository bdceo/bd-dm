package com.bdsoft.img;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.poi.util.IOUtils;

public class FetchAndCut {

	public static void main(String[] args) throws Exception {

		String webImg = "http://s3.cn-north-1.amazonaws.com.cn/ezhome-prod-render-assets/floorplan/render/images/2016-9-18/1d77fdfc-ad6c-4576-948c-533987be9953/da4ce69d_5a0c_4ee2_9a40_efeb96a040b1.jpg";

		// 下载图片
		URL aurl = new URL(webImg);
		InputStream in = aurl.openStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.copy(in, out);
		out.flush();

		// 截图
		byte[] bytes = out.toByteArray();
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);            
		BufferedImage src = ImageIO.read(bis);
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
		ByteArrayOutputStream out2= new ByteArrayOutputStream();
		ImageIO.write(dest, "jpg",out2 );
//		System.out.println("图片提取到 >> " + out);

		byte[] bytes2 = out2.toByteArray();
		System.out.println(bytes2.length/1024);
		ByteArrayInputStream bis2 = new ByteArrayInputStream(bytes2);     
		
		IOUtils.copy(bis2, new FileOutputStream("/home/dcy/tmp/xx.jpg"));
	}

}
