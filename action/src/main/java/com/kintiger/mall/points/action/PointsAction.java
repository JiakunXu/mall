package com.kintiger.mall.points.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.item.IItemService;
import com.kintiger.mall.api.item.bo.Item;
import com.kintiger.mall.api.points.IPointsService;
import com.kintiger.mall.api.points.bo.Points;
import com.kintiger.mall.api.shop.IShopService;
import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.Decode;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 积分兑换对应表.
 * 
 * @author xujiakun
 * 
 */
public class PointsAction extends BaseAction {

	private static final long serialVersionUID = 3730901200831751000L;

	private IPointsService pointsService;

	private IShopService shopService;

	private IItemService itemService;

	private int total;

	private List<Points> pointsList;

	@Decode
	private String itemName;

	/**
	 * 店铺别名.
	 */
	private String alias;

	/**
	 * 积分商城商品信息(买家查询).
	 */
	private String pointsId;

	/**
	 * 积分商城商品信息(买家查询).
	 */
	private String itemId;

	/**
	 * 积分商城商品信息(买家查询).
	 */
	private String skuId;

	/**
	 * 积分商城商品信息(买家查询).
	 */
	private String points;

	/**
	 * 积分商城商品信息 查询.
	 */
	private Item item;

	/**
	 * 兑换商品数量.
	 */
	private String quantity;

	/**
	 * 积分充值 积分卡号.
	 */
	private String cardNo;

	/**
	 * 积分充值 密码.
	 */
	private String password;

	/**
	 * ajax 返回.
	 */
	private String message;

	/**
	 * 积分商品兑换活动到期时间(2014-10-16).
	 */
	private String expireDate;

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	/**
	 * 积分商城.
	 * 
	 * @return
	 */
	public String index() {
		return SUCCESS;
	}

	@JsonResult(field = "pointsList", include = { "pointsId", "itemName", "propertiesName", "points", "expireDate" }, total = "total")
	public String getPointsJsonList() {
		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		Points p = new Points();
		p = getSearchInfo(p);

		if (StringUtils.isNotBlank(itemName)) {
			p.setItemName(itemName.trim());
		}

		total = pointsService.getPointsCount(shopId, p);

		if (total > 0) {
			pointsList = pointsService.getPointsList(shopId, p);
		}

		return JSON_RESULT;
	}

	/**
	 * 创建积分商城商品.
	 * 
	 * @return
	 */
	public String createPoints() {
		User user = this.getUser();

		BooleanResult result =
			pointsService.createPoints(user.getShopId(), itemId, points, expireDate, user.getUserId());

		if (result.getResult()) {
			this.setSuccessMessage("积分商城商品添加成功！");
		} else {
			this.setFailMessage(result.getCode());
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 积分商品下架.
	 * 
	 * @return
	 */
	public String deletePoints() {
		User user = getUser();

		BooleanResult result = pointsService.deletePoints(user.getShopId(), pointsId, user.getUserId());

		if (result.getResult()) {
			this.setSuccessMessage("积分商品下架成功！");
		} else {
			this.setFailMessage(result.getCode());
		}

		return RESULT_MESSAGE;
	}

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 积分商城.
	 * 
	 * @return
	 */
	public String mall() {
		Shop shop = this.getShop();

		Points p = new Points();

		int c = pointsService.getPointsCount(shop.getShopId(), p);
		if (c == 0) {
			return SUCCESS;
		}

		p.setStart(0);
		p.setLimit(c);
		p.setSort("points");
		p.setDir("asc");

		pointsList = pointsService.getPointsList(shop.getShopId(), p);

		return SUCCESS;
	}

	/**
	 * 客户端查询商品详细信息.
	 * 
	 * @return
	 */
	public String item() {

		// 根据 itemId 获取商品详细信息
		item = itemService.getItem(getShop().getShopId(), itemId, skuId);

		if (item == null) {
			// TODO
		}

		return SUCCESS;
	}

	/**
	 * 兑换积分.
	 * 
	 * @return
	 */
	@JsonResult(field = "message")
	public String exchange() {
		this.getServletResponse().setStatus(500);

		// 验证用户是否登陆
		User user = this.getUser();
		if (user == null) {
			message = "login";
			return JSON_RESULT;
		}

		Shop shop = this.getShop();
		if (shop == null) {
			message = "店铺信息不存在！";
			return JSON_RESULT;
		}

		BooleanResult result = pointsService.exchangePoints(getUser().getUserId(), itemId, skuId, pointsId, quantity);

		if (result.getResult()) {
			this.getServletResponse().setStatus(200);
			message = "购物车添加成功！";
		} else {
			message = result.getCode();
		}

		return JSON_RESULT;
	}

	/**
	 * 充值.
	 * 
	 * @return
	 */
	public String gotoRecharge() {
		return SUCCESS;
	}

	/**
	 * 积分卡充值.
	 * 
	 * @return
	 */
	@JsonResult(field = "message")
	public String recharge() {
		this.getServletResponse().setStatus(500);

		// 验证用户是否登陆
		User user = this.getUser();
		if (user == null) {
			message = "login";
			return JSON_RESULT;
		}

		Shop shop = this.getShop();
		if (shop == null) {
			message = "店铺信息不存在！";
			return JSON_RESULT;
		}

		BooleanResult result = pointsService.rechargePoints(user.getUserId(), shop.getShopId(), cardNo, password);

		if (result.getResult()) {
			this.getServletResponse().setStatus(200);
			message = "积分卡充值成功！";
		} else {
			message = result.getCode();
		}

		return JSON_RESULT;
	}

	public IPointsService getPointsService() {
		return pointsService;
	}

	public void setPointsService(IPointsService pointsService) {
		this.pointsService = pointsService;
	}

	public IShopService getShopService() {
		return shopService;
	}

	public void setShopService(IShopService shopService) {
		this.shopService = shopService;
	}

	public IItemService getItemService() {
		return itemService;
	}

	public void setItemService(IItemService itemService) {
		this.itemService = itemService;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<Points> getPointsList() {
		return pointsList;
	}

	public void setPointsList(List<Points> pointsList) {
		this.pointsList = pointsList;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getPointsId() {
		return pointsId;
	}

	public void setPointsId(String pointsId) {
		this.pointsId = pointsId;
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

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

}
