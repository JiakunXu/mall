package com.kintiger.mall.register.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.register.IRegisterService;
import com.kintiger.mall.api.register.bo.RegisterResult;
import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.framework.action.BaseAction;

/**
 * 用户注册(移动端).
 * 
 * @author xujiakun
 * 
 */
public class MobileRegisterAction extends BaseAction {

	private static final long serialVersionUID = -8519084408115355013L;

	private static final String GOTO = "goto";

	private IRegisterService registerService;

	private String passport;

	private String password;

	/**
	 * 用户名.
	 */
	private String userName;

	/**
	 * 推荐人(userId).
	 */
	private String recommend;

	/**
	 * 验证码.
	 */
	private String checkCode;

	// >>>>>>>>>>以下是注册<<<<<<<<<<

	/**
	 * 注册首页.
	 * 
	 * @return
	 */
	public String index() {
		// if not null then 跳转 会员推荐
		if (getUser() != null) {
			return "recommend";
		}

		setAttribute();

		return SUCCESS;
	}

	/**
	 * 注册.
	 * 
	 * @return
	 */
	public String register() {
		Shop shop = this.getShop();
		if (shop == null) {
			this.setFailMessage("店铺信息不能为空！");
			return RESULT_MESSAGE;
		}

		// 手机号注册
		RegisterResult result =
			registerService.registerUser(passport, password, userName, shop.getShopId(), checkCode, recommend);

		// 注册成功
		if (IRegisterService.RESULT_SUCCESS.equals(result.getResultCode())) {
			this.setSuccessMessage(result.getMessage());
		} else {
			this.setFailMessage(result.getMessage());
		}

		return RESULT_MESSAGE;
	}

	private void setAttribute() {
		// if 存在 goto then
		HttpServletRequest request = getServletRequest();
		String url = request.getParameter(GOTO);
		if (StringUtils.isNotBlank(url)) {
			request.setAttribute(GOTO, url.trim());
		}
	}

	public IRegisterService getRegisterService() {
		return registerService;
	}

	public void setRegisterService(IRegisterService registerService) {
		this.registerService = registerService;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

}
