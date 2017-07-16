package com.bdsoft.datamin.fetch.sdxjpc;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author 丁辰叶
 * @date 2014-7-18
 */
public class SdxjpcSeed {

	static String WEB_INDEX = "http://www.sdxjpc.com/scrp/bookcustomore.cfm";

	public static void main(String[] args) throws Exception {
		initSeeds();
	}

	public static Set<String> initSeeds() throws Exception {
		Set<String> seeds = new HashSet<String>();
		seeds.add(WEB_INDEX);
		return seeds;
	}

}
