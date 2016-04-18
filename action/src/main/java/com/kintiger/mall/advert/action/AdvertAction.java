package com.kintiger.mall.advert.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.advert.IAdvertService;
import com.kintiger.mall.api.advert.bo.Advert;
import com.kintiger.mall.api.shop.IShopService;
import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.util.ClientUtil;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 广告.
 * 
 * @author xujiakun
 * 
 */
public class AdvertAction extends BaseAction {

	private static final long serialVersionUID = 1579241749511515917L;

	private IAdvertService advertService;

	private IShopService shopService;

	private List<Advert> advertList;

	/**
	 * 店铺唯一 id.
	 */
	private String uuid;

	/**
	 * 广告id.
	 */
	private String advertId;

	/**
	 * 广告点击后重定向.
	 */
	private String url;

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	public String index() {
		String shopId = getUser().getShopId();

		// 根据 shopId 获取店铺 uuid
		Shop shop = shopService.getShop(shopId);
		// 店铺信息不存在
		if (shop == null) {
			return "440";
		}

		uuid = shop.getUuid();

		Advert advert = new Advert();
		advert = getSearchInfo(advert);

		int total = advertService.getAdvertCount(shopId, advert);
		advert.setStart(0);
		advert.setLimit(total);

		advertList = advertService.getAdvertList(shopId, advert);

		return SUCCESS;
	}

	@JsonResult(field = "advertList", include = { "dates", "pv", "uv", "points" })
	public String getAdvertStats() {
		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		String gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -30), DateUtil.DEFAULT_DATE_FORMAT);
		String gmtEnd = DateUtil.getNowDateStr();

		advertList = advertService.getAdvertStats(shopId, advertId, gmtStart, gmtEnd);

		return JSON_RESULT;
	}

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 广告点击(http://ip:port/kmall/advert/click.htm?alias=&advertId=1).
	 * 
	 * @return
	 */
	public String click() {
		BooleanResult result =
			advertService.clickAdvert(getUser().getUserId(), ClientUtil.getIpAddr(this.getServletRequest()), getShop()
				.getShopId(), advertId);

		String appUrl = env.getProperty("appUrl");

		if (result.getResult()) {
			url = appUrl + result.getCode();
		} else {
			url = appUrl + "/shop/homepage.htm?alias=" + getShop().getUuid();
		}

		return SUCCESS;
	}

	public IAdvertService getAdvertService() {
		return advertService;
	}

	public void setAdvertService(IAdvertService advertService) {
		this.advertService = advertService;
	}

	public IShopService getShopService() {
		return shopService;
	}

	public void setShopService(IShopService shopService) {
		this.shopService = shopService;
	}

	public List<Advert> getAdvertList() {
		return advertList;
	}

	public void setAdvertList(List<Advert> advertList) {
		this.advertList = advertList;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAdvertId() {
		return advertId;
	}

	public void setAdvertId(String advertId) {
		this.advertId = advertId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
