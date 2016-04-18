package com.kintiger.mall.api.trade.bo;

import java.math.BigDecimal;
import java.util.List;

import com.kintiger.mall.api.item.bo.ItemFile;
import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 订单.
 * 
 * @author xujiakun
 * 
 */
public class Order extends SearchInfo {

	private static final long serialVersionUID = -1206444713292456901L;

	private String orderId;

	/**
	 * 交易ID.
	 */
	private String tradeId;

	/**
	 * 商品 ID.
	 */
	private String itemId;

	private String skuId;

	/**
	 * 商品名称.
	 */
	private String itemName;

	/**
	 * sku所对应的销售属性的中文名字串，格式如：Pid1:vid1:pid_name1:vid_name1;Pid2:vid2:pid_name2:vid_name2.
	 */
	private String propertiesName;

	/**
	 * 购买该sku商品的数量.
	 */
	private int quantity;

	/**
	 * 购买价格.
	 */
	private BigDecimal price;

	/**
	 * 积分兑换.
	 */
	private BigDecimal points;

	/**
	 * 涨价或减价.
	 */
	private BigDecimal change;

	/**
	 * 商品文件.
	 */
	private List<ItemFile> itemFileList;

	/**
	 * 库存.
	 */
	private int stock;

	/**
	 * 状态 D:删除 U:正常.
	 */
	private String state;

	/**
	 * 操作人ID.
	 */
	private String modifyUser;

	/**
	 * 店铺id 创建时权限控制.
	 */
	private String shopId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 某商品总价格.
	 * 
	 * @return
	 */
	public BigDecimal getTotal() {
		if (this.price != null) {
			return this.price.multiply(new BigDecimal(this.quantity));
		}

		return BigDecimal.ZERO;
	}

	public BigDecimal getPoints() {
		return points;
	}

	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	public List<ItemFile> getItemFileList() {
		return itemFileList;
	}

	public void setItemFileList(List<ItemFile> itemFileList) {
		this.itemFileList = itemFileList;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
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

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

}
