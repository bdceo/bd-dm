/**
 * WwwFangServiceImpl.java
 * com.bdsoft.datamin.service.impl
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.service.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdsoft.datamin.entity.WwwFang;
import com.bdsoft.datamin.mapper.WwwFangMapper;
import com.bdsoft.datamin.service.IWwwFangService;
import com.google.common.collect.Maps;

/**
 * 互联网租房信息
 *
 * @author   丁辰叶
 * @date	 2016-5-18
 * @version  1.0.0
 */
@Service
public class WwwFangServiceImpl implements IWwwFangService {

	@Autowired
	private WwwFangMapper wwwFangMapper;

	public void updateFang(WwwFang wf) {
		wwwFangMapper.updateSelectiveById(wf);
	}

	public Map<String, WwwFang> saveUrls(Set<String> urlSet) {
		Map<String, WwwFang> map = Maps.newHashMap();
		Iterator<String> fangs = urlSet.iterator();
		while (fangs.hasNext()) {
			String furl = fangs.next();
			WwwFang wf = new WwwFang();
			wf.setInfoUrl(furl);
			wf = wwwFangMapper.selectOne(wf);
			if (wf == null) {
				wf = new WwwFang(furl);
				wf.setInfoFrom(1);
				wwwFangMapper.insertSelective(wf);
			}
			map.put(furl, wf);
		}
		return map;
	}
}
