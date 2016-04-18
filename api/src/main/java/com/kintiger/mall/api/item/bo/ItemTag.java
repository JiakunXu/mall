package com.kintiger.mall.api.item.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 商品 + 标签.
 * 
 * @author
 * 
 */
public class ItemTag extends SearchInfo {

	private static final long serialVersionUID = -3451635047027515872L;

	/**
	 * id.
	 */
	private String id;

	/**
	 * 商品id.
	 */
	private String itemId;

	/**
	 * 标签id.
	 */
	private String tagId;

	/**
	 * 修改人.
	 */
	private String modifyUser;

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

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

}
