package com.kintiger.mall.api.item.bo;

import com.kintiger.mall.api.file.bo.FileInfo;

/**
 * 商品文件.
 * 
 * @author xujiakun
 * 
 */
public class ItemFile extends FileInfo {

	private static final long serialVersionUID = -2252136499749108075L;

	private String id;

	/**
	 * 商品id.
	 */
	private String itemId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

}
