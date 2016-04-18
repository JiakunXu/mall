package com.kintiger.mall.spec.dao;

import java.util.List;

import com.kintiger.mall.api.spec.bo.SpecCat;
import com.kintiger.mall.api.spec.bo.SpecItem;

/**
 * dao 接口.
 * 
 * @author xujiakun
 * 
 */
public interface ISpecDao {

	/**
	 * 获取店铺规格类目信息.
	 * 
	 * @param specCat
	 * @return
	 */
	List<SpecCat> getSpecCatList(SpecCat specCat);

	/**
	 * 获取店铺规格类目明细信息.
	 * 
	 * @param specItem
	 * @return
	 */
	List<SpecItem> getSpecItemList(SpecItem specItem);

	/**
	 * 创建店铺规格明细.
	 * 
	 * @param specItem
	 * @return
	 */
	String createSpecItem(SpecItem specItem);

}
