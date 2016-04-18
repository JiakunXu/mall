package com.kintiger.mall.api.spec;

import java.util.List;

import com.kintiger.mall.api.spec.bo.SpecCat;
import com.kintiger.mall.api.spec.bo.SpecItem;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 店铺规格类目接口.
 * 
 * @author xujiakun
 * 
 */
public interface ISpecService {

	/**
	 * 获取店铺规格类目信息.
	 * 
	 * @param shopId
	 * @return
	 */
	List<SpecCat> getSpecCatList(String shopId);

	/**
	 * 获取店铺规格类目信息.
	 * 
	 * @param shopId
	 * @param specCId
	 * @return
	 */
	List<SpecCat> getSpecCatList(String shopId, String[] specCId);

	/**
	 * 获取店铺规格类目明细信息.
	 * 
	 * @param shopId
	 * @param specCId
	 * @return
	 */
	List<SpecItem> getSpecItemList(String shopId, String specCId);

	/**
	 * 获取店铺规格类目明细信息.
	 * 
	 * @param shopId
	 * @param specItemId
	 * @return
	 */
	List<SpecItem> getSpecItemList(String shopId, String[] specItemId);

	/**
	 * 创建店铺规格类目明细.
	 * 
	 * @param shopId
	 * @param specCId
	 *            规格类目.
	 * @param specItemValue
	 *            规格明细值.
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createSpecItem(String shopId, String specCId, String specItemValue, String modifyUser);

}
