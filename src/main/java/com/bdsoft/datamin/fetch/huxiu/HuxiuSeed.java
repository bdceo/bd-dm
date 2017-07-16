package com.bdsoft.datamin.fetch.huxiu;

import java.util.HashSet;
import java.util.Set;

public class HuxiuSeed {

	public static void main(String[] args) {

	}

	public static Set<String> initSeeds() {
		Set<String> seeds = new HashSet<String>();
		seeds.add("http://www.huxiu.com/books/0/" + HuxiuConfig.URL_P_PAGER
				+ ".html");
		return seeds;
	}

}
