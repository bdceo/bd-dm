package com.bdsoft.img;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.poi.util.IOUtils;

import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;

/**
 * 测试下载文件接口：http响应头：Content-disposition<br/>
 * 参考：http://www.cnblogs.com/brucejia/archive/2012/12/24/2831060.html<br/>
 * 
 * @author dcy
 */
public class DownloadImg {

	public static void main(String[] args) throws Exception {
		String url = "http://beta-storage.acgcn.autodesk.com/api/v2/files/download?file_ids=18215162";
		BDHttpParam pm = BDHttpParam.init().addHeader("X-AFC", "HW1ONB").addHeader("X-Session",
				"0DC23BA0-D366-485E-B137-155A46DBD7E4");

		HttpResponse res = BDHttpUtil.sendGetWithBack(url, pm);
		System.out.println(res.getStatusLine().getStatusCode());

		// 从response-header提取文件名
		String name = "";
		Header[] hds = res.getAllHeaders();
		for (Header hd : hds) {
			System.out.println(hd.getName() + "=" + hd.getValue());
			// Content-Disposition=attachment; filename="docName.doc"
			if (hd.getName().equals("Content-Disposition")) {
				name = hd.getValue();
				break;
			}
		}
		name = name.substring(name.indexOf("\"") + 1, name.lastIndexOf("\""));

		// 输出到文件
		InputStream in = res.getEntity().getContent();
		String path = "/home/dcy/".concat(name);
		FileOutputStream out = new FileOutputStream(path);
		IOUtils.copy(in, out);
		out.flush();

		// 判断文件类型，重命名文件
//		String type = BDFileUtil.getFileType(new File(path));
//		System.out.println(type);
//		String newPath = path.concat(".").concat(type);
//		System.out.println(newPath);
//		new File(path).renameTo(new File(newPath));
	}

}
