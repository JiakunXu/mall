package com.kintiger.mall.item.dao;

import java.util.List;

import com.kintiger.mall.api.item.bo.ItemFile;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IItemFileDao {

	/**
	 * 
	 * @param itemFile
	 * @return
	 */
	List<ItemFile> getItemFileList(ItemFile itemFile);

	/**
	 * 
	 * @param itemFile
	 * @return
	 */
	int deleteItemFile(ItemFile itemFile);

	/**
	 * 
	 * @param itemId
	 * @param itemFileList
	 * @param modifyUser
	 * @return
	 */
	int createItemFile(String itemId, List<ItemFile> itemFileList, String modifyUser);

}
