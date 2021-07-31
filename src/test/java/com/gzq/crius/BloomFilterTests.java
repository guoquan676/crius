package com.gzq.crius;

import com.gzq.crius.bloomfilter.BloomFilterService;
import com.gzq.crius.bloomfilter.BloomInitData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BloomFilterTests {

	@Autowired
	BloomInitData bloomInitData;

	@Resource
	BloomFilterService bloomOrder;

	@Test
	public void test(){
		bloomInitData.initData(true);
		boolean contain1 = bloomOrder.contain("江苏省徐州市鼓楼区");
		System.out.println("contain ---->" + contain1);
		boolean contain2 = bloomOrder.contain( "新乡");
		System.out.println("contain ---->" + contain2);
		boolean add = bloomOrder.add( "新乡");
		System.out.println("add ----->" + add);
		boolean contain3 = bloomOrder.contain( "新乡");
		System.out.println("contain ---->" + contain3);
	}
}
