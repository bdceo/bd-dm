/**
 * BDExcelParser.java
 * com.bdsoft.hadoop.util
 * Copyright (c) 2015, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * 
 * <p>
 *
 * @author 丁辰叶
 * @date 2015-12-12
 * @version 1.0.0
 */
public class BDExcelParser {

	public static void main(String[] args) throws Exception {
		String path = "d:/download/xxx.xls";
		String data = parseExcelData(new FileInputStream(path));
		System.out.println(data);
	}

	/**
	 * 解析excel 每行已回车分隔 每列已制表符分隔
	 *
	 * @param in
	 *            excel文件输入流
	 * @return
	 */
	public static String parseExcelData(InputStream in) {
		long bytesRead = 0;
		StringBuilder content = new StringBuilder();
		try {
			HSSFWorkbook book = new HSSFWorkbook(in);
			HSSFSheet sheet = book.getSheetAt(0);

			Iterator<Row> rows = sheet.iterator();
			while (rows.hasNext()) {
				Row row = rows.next();

				Iterator<Cell> cells = row.iterator();
				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						bytesRead++;
						content.append(cell.getBooleanCellValue()).append("\t");
						break;
					case Cell.CELL_TYPE_NUMERIC:
						bytesRead++;
						content.append(new Double( cell.getNumericCellValue()).longValue()).append("\t");
//						content.append(cell.getNumericCellValue()).append("\t");
						break;
					case Cell.CELL_TYPE_STRING:
						bytesRead++;
						content.append(cell.getStringCellValue()).append("\t");
						break;
					}
				}
				content.append("\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content.toString();
	}

}
