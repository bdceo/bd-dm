package com.bdsoft.datamin.service;

import java.util.List;

import com.bdsoft.datamin.entity.DouBookTag;

public interface IDouBookTagService {

	public void saveTags(List<DouBookTag> tags);

	public DouBookTag getUnfetchTag();

	public List<DouBookTag> getUnfetchTags(int size);

	public boolean updateTag(DouBookTag tag);
}
