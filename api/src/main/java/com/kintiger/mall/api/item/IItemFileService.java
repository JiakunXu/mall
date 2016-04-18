package com.kintiger.mall.api.item;

import java.util.List;
import java.util.Map;

import com.kintiger.mall.api.item.bo.ItemFile;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 商品文件.
 * 
 * @author xujiakun
 * 
 */
public interface IItemFileService {

	/**
	 * 获取商品文件.
	 * 
	 * @param shopId
	 * @param itemId
	 * @return
	 */
	List<ItemFile> getItemFileList(String shopId, String itemId);

	/**
	 * 获取商品文件.
	 * 
	 * @param shopId
	 * @param itemId
	 * @return
	 */
	Map<String, List<ItemFile>> getItemFileList(String shopId, String[] itemId);

	/**
	 * 删除商品文件.
	 * 
	 * @param itemId
	 * @param modifyUser
	 * @return
	 */
	BooleanResult deleteItemFile(String itemId, String modifyUser);

	/**
	 * 创建商品文件.
	 * 
	 * @param itemId
	 * @param itemFileList
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createItemFile(String itemId, List<ItemFile> itemFileList, String modifyUser);

}
