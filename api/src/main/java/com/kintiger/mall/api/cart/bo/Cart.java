package com.kintiger.mall.api.cart.bo;

import java.math.BigDecimal;
import java.util.List;

import com.kintiger.mall.api.item.bo.ItemFile;
import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 购物车.
 * 
 * @author
 * 
 */
public class Cart extends SearchInfo {

	private static final long serialVersionUID = -1870399095028606562L;

	/**
	 * 购物车ID.
	 */
	private String cartId;

	/**
	 * 用户ID.
	 */
	private String userId;

	/**
	 * 关联查询参数.
	 */
	private String shopId;

	/**
	 * 商品ID.
	 */
	private String itemId;

	/**
	 * 查询结果.
	 */
	private String itemName;

	/**
	 * sku所对应的销售属性的中文名字串，格式如：Pid1:vid1:pid_name1:vid_name1;Pid2:vid2:pid_name2:vid_name2.
	 */
	private String propertiesName;

	/**
	 * SKU ID.
	 */
	private String skuId;

	/**
	 * 价格(10.00).
	 */
	private BigDecimal price;

	/**
	 * 邮费.
	 */
	private BigDecimal postage;

	/**
	 * 0 非积分兑换 else 积分兑换.
	 */
	private String pointsId;

	/**
	 * 兑换所需积分.
	 */
	private BigDecimal points;

	/**
	 * 购物车该sku商品的数量.
	 */
	private int quantity;

	/**
	 * 商品文件.
	 */
	private List<ItemFile> itemFileList;

	/**
	 * 状态 D:删除 U:正常 E:已购买.
	 */
	private String state;

	/**
	 * 操作人ID.
	 */
	private String modifyUser;

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPostage() {
		return postage;
	}

	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}

	public String getPointsId() {
		return pointsId;
	}

	public void setPointsId(String pointsId) {
		this.pointsId = pointsId;
	}

	public BigDecimal getPoints() {
		return points;
	}

	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
