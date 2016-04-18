package com.kintiger.mall.api.region;

import java.util.List;

import com.kintiger.mall.api.region.bo.Region;

/**
 * 区域接口.
 * 
 * @author jiakunxu
 * 
 */
public interface IRegionService {

	/**
	 * 根据 parentRegionId 查询下级区域 父地区ID=0时，代表的是一级的类目.
	 * 
	 * @param parentRegionId
	 * @return
	 */
	List<Region> getRegionList(String parentRegionId);

}
