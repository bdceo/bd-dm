package com.bdsoft.datamin.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdsoft.datamin.entity.WwwFetch;
import com.bdsoft.datamin.entity.WwwFetchTmp;
import com.bdsoft.datamin.mapper.WwwFetchMapper;
import com.bdsoft.datamin.mapper.WwwFetchTmpMapper;
import com.bdsoft.datamin.service.IWwwFetchTmpService;

@Service
public class WwwFetchTmpServiceImp implements IWwwFetchTmpService {

	@Autowired
	private WwwFetchMapper wf;
	@Autowired
	private WwwFetchTmpMapper wft;

	public void test() {
		Date date = new Date();
		WwwFetchTmp tmp = wft.selectById(3884L);
		tmp.setFetchDate(date);
		wft.updateSelectiveById(tmp);

		WwwFetch tmp2 = wf.selectById(4393L);
		tmp2.setFetchDate(date);
		wf.updateSelectiveById(tmp2);

		System.out.println("更新时间　＝　" + date.toLocaleString());
		tmp = wft.selectById(3884L);
		System.out.println("WwwFetchTmp = " + tmp.getFetchDate().toLocaleString());
		tmp2 = wf.selectById(4393L);
		System.out.println("WwwFetch = " + tmp2.getFetchDate().toLocaleString());
	}
}