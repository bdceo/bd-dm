package com.bdsoft.datamin.util;

import java.io.File;


public class LuceneUtil {

	// lucene 存放图书索引位置
	public static final String BOOK_INDEX;

	static {
		BOOK_INDEX = BDFileUtil.getWebRoot() + "index" + File.separator
				+ "dou-book";

	}

}
