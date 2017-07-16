package com.bdsoft.img;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class DownloadAndCutImage {

	public static void main(String[] args) {
		String url = "http://s3.cn-north-1.amazonaws.com.cn/ezhome-prod-render-assets/floorplan/render/images/2016-9-18/1d77fdfc-ad6c-4576-948c-533987be9953/da4ce69d_5a0c_4ee2_9a40_efeb96a040b1.jpg";
		InputStream in = dac(url);

		try {
			DownloadAndCutImage.copy(in, new FileOutputStream(new File("/home/dcy/tmp/cut.jpg")));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e2) {
					e2.getClass();
				}
			}
		}
	}

	public static InputStream dac(String webImgUrl) {
		// 下载图片
		InputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			URL iurl = new URL(webImgUrl);
			in = iurl.openStream();
			out = new ByteArrayOutputStream();
			DownloadAndCutImage.copy(in, out);
			out.flush();
		} catch (Exception e) {
			throw new RuntimeException("下载出错");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e2) {
				}
			}
		}

		// 截图
		byte[] srcImg = out.toByteArray();
		ByteArrayInputStream in2 = null;
		ByteArrayOutputStream out2 = null;
		try {
			in2 = new ByteArrayInputStream(srcImg);
			BufferedImage src = ImageIO.read(in2);
			int w = src.getWidth();
			int h = src.getHeight();
			int nw = h, nh = h, x = w / 6 * 5, y = 0;
			BufferedImage dest = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = dest.createGraphics();
			g.fillRect(0, 0, nw, nh);
			g.drawImage(src, 0, 0, nw, nh, x, y, w, nh, null);
			out2 = new ByteArrayOutputStream();
			ImageIO.write(dest, "jpg", out2);
		} catch (Exception e) {
			throw new RuntimeException("截图出错");
		} finally {
			if (in2 != null) {
				try {
					in2.close();
				} catch (Exception e2) {
				}
			}
		}

		// 内存输入流
		byte[] cutImg = out2.toByteArray();
		return new ByteArrayInputStream(cutImg);
	}

	public static void copy(InputStream inp, OutputStream out) throws IOException {
		byte[] buff = new byte[4096];
		int count;
		while ((count = inp.read(buff)) != -1) {
			if (count > 0) {
				out.write(buff, 0, count);
			}
		}
	}

}
