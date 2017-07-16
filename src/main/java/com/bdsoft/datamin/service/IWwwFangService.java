/**
 * IWwwFangService.java
 * com.bdsoft.datamin.service
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.service;

import java.util.Map;
import java.util.Set;

import com.bdsoft.datamin.entity.WwwFang;
 
public interface IWwwFangService {

	public Map<String, WwwFang> saveUrls(Set<String> urlSet);

	public void updateFang(WwwFang wf);
}
