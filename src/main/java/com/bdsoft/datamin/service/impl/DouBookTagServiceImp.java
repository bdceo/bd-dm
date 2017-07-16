package com.bdsoft.datamin.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdsoft.datamin.entity.DouBookTag;
import com.bdsoft.datamin.mapper.DouBookTagMapper;
import com.bdsoft.datamin.service.IDouBookTagService;
import com.bdsoft.datamin.util.Utils;

/**
 * 图书标签
 *
 * @author   丁辰叶
 * @date	 2016-5-18
 * @version  1.0.0
 */
@Service
public class DouBookTagServiceImp implements IDouBookTagService {

	private static Logger log = LoggerFactory.getLogger(DouBookTagServiceImp.class);
	
	@Autowired
	private DouBookTagMapper btd;

	// 保存图书标签-待抓
	public void saveTags(List<DouBookTag> tags) {
		for (DouBookTag tag : tags) {
			DouBookTag tmp = new DouBookTag();
			tmp.setTagName(tag.getTagName());
			tmp = btd.selectOne(tmp);
			if (tmp != null) {
				tmp = null;
				continue;
			}
			btd.insertSelective(tag);
			log.info("入库：" + tag);
		}
	}

	// 随机获取未抓取标签
	public DouBookTag getUnfetchTag() {
		List<DouBookTag> tags = btd.selectUnfetchTag(1);
		if (tags != null && tags.size() > 0) {
			return tags.get(0);
		} else {
			return null;
		}
	}

	// 随机获取未抓取标签
	public List<DouBookTag> getUnfetchTags(int size) {
		return btd.selectUnfetchTag(size);
	}

	public boolean updateTag(DouBookTag tag) {
		return btd.updateSelectiveById(tag) > 0;
	}

	public static void main(String[] saaa) {
		int size = 4;
		for (int i = 0; i < 50; i++) {
			int j = (int) (Math.random() * 4);
			j = Utils.intRandom(size);
			System.out.print(j + " ");
		}
	}

}
