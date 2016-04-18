package com.kintiger.mall.api.pageview.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 
 * @author xujiakun
 * 
 */
public class Pageview extends SearchInfo {

	private static final long serialVersionUID = -8887961280742376191L;

	private String id;

	/**
	 * 店铺id.
	 */
	private String shopId;

	private String actionName;

	/**
	 * url 参数.
	 */
	private String parameter;

	private String ip;

	/**
	 * pageview 时间(年-月-日 十:分:秒).
	 */
	private String createDate;

	private String modifyUser;

	// >>>>>>>>>>以下是辅助属性<<<<<<<<<<

	private int pv;

	private int uv;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

}
