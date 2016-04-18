package com.kintiger.mall.api.points.bo;

import java.math.BigDecimal;
import java.util.List;

import com.kintiger.mall.api.item.bo.ItemFile;
import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 商品积分兑换.
 * 
 * @author
 * 
 */
public class Points extends SearchInfo {

	private static final long serialVersionUID = -2434561623900795329L;

	/**
	 * ID.
	 */
	private String pointsId;

	/**
	 * 店铺 id.
	 */
	private String shopId;

	/**
	 * 商品 id.
	 */
	private String itemId;

	/**
	 * sku id(SKU ID 当不存在 sku 时 为0 ).
	 */
	private String skuId;

	/**
	 * 兑换所需积分.
	 */
	private BigDecimal points;

	/**
	 * 兑换活动过期日期.
	 */
	private String expireDate;

	/**
	 * 商品名称.
	 */
	private String itemName;

	/**
	 * 规格名称.
	 */
	private String propertiesName;

	/**
	 * 商品文件.
	 */
	private List<ItemFile> itemFileList;

	/**
	 * 状态 D:删除 U:正常.
	 */
	private String state;

	/**
	 * 操作人ID.
	 */
	private String modifyUser;

	public String getPointsId() {
		return pointsId;
	}

	public void setPointsId(String pointsId) {
		this.pointsId = pointsId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getPoints() {
		return points;
	}

	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPropertiesName() {
		return propertiesName;
	}

	public void setPropertiesName(String propertiesName) {
		this.propertiesName = propertiesName;
	}

	public List<ItemFile> getItemFileList() {
		return itemFileList;
	}

	public void setItemFileList(List<ItemFile> itemFileList) {
		this.itemFileList = itemFileList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

}
