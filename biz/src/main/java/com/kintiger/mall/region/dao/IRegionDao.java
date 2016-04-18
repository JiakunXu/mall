package com.kintiger.mall.region.dao;

import java.util.List;

import com.kintiger.mall.api.region.bo.Region;

/**
 * 区域 dao 接口.
 * 
 * @author xujiakun
 * 
 */
public interface IRegionDao {

	/**
	 * 
	 * @param region
	 * @return
	 */
	List<Region> getRegionList(Region region);

}
