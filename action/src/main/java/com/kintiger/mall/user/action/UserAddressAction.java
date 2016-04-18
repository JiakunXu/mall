package com.kintiger.mall.user.action;

import javax.servlet.http.HttpServletResponse;

import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.api.user.IUserAddressService;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.api.user.bo.UserAddress;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 收货地址.
 * 
 * @author xujiakun
 * 
 */
public class UserAddressAction extends BaseAction {

	private static final long serialVersionUID = -1868748047421889403L;

	private IUserAddressService userAddressService;

	private UserAddress userAddress;

	private String addId;

	/**
	 * 创建收货地址并修改订单.
	 */
	private String tradeId;

	@JsonResult(field = "addId")
	public String createUserAddress() {
		HttpServletResponse response = this.getServletResponse();
		// 初始化 返回失败
		response.setStatus(500);
		addId = "收货地址创建失败，请稍后再试！";

		User user = this.getUser();
		if (user == null) {
			return JSON_RESULT;
		}

		Shop shop = this.getShop();
		if (shop == null) {
			// TODO
			return JSON_RESULT;
		}

		BooleanResult result =
			userAddressService.createUserAddress(user.getUserId(), userAddress, shop.getShopId(), tradeId);
		if (result.getResult()) {
			response.setStatus(200);
		}
		addId = result.getCode();

		return JSON_RESULT;
	}

	public IUserAddressService getUserAddressService() {
		return userAddressService;
	}

	public void setUserAddressService(IUserAddressService userAddressService) {
		this.userAddressService = userAddressService;
	}

	public UserAddress getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(UserAddress userAddress) {
		this.userAddress = userAddress;
	}

	public String getAddId() {
		return addId;
	}

	public void setAddId(String addId) {
		this.addId = addId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

}
