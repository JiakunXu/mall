package com.kintiger.mall.market.action;

import java.util.Date;
import java.util.List;

import com.kintiger.mall.api.advert.IAdvertService;
import com.kintiger.mall.api.advert.bo.Advert;
import com.kintiger.mall.api.item.IItemService;
import com.kintiger.mall.api.item.bo.Item;
import com.kintiger.mall.api.points.IPointsCardService;
import com.kintiger.mall.api.points.bo.PointsCard;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.util.DateUtil;

/**
 * 市场营销(卖家).
 * 
 * @author xujiakun
 * 
 */
public class MarketAction extends BaseAction {

	private static final long serialVersionUID = 1726807941435762626L;

	private IPointsCardService pointsCardService;

	private IItemService itemService;

	private IAdvertService advertService;

	private List<PointsCard> pointsCardList;

	private List<Item> itemList;

	private List<Advert> advertList;

	/**
	 * 市场营销概览.
	 * 
	 * @return
	 */
	public String index() {
		String shopId = getUser().getShopId();

		// 积分兑换
		itemList = itemService.getItemStats(shopId, IItemService.STATS_TYPE_EXCHANGE);

		String gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -7), DateUtil.DEFAULT_DATE_FORMAT);
		String gmtEnd = DateUtil.getNowDateStr();

		// 积分卡
		pointsCardList = pointsCardService.getPointsCardStats(shopId, gmtStart, gmtEnd);

		// 广告投放
		advertList = advertService.getAdvertStats(shopId, gmtStart, gmtEnd);

		return SUCCESS;
	}

	public IPointsCardService getPointsCardService() {
		return pointsCardService;
	}

	public void setPointsCardService(IPointsCardService pointsCardService) {
		this.pointsCardService = pointsCardService;
	}

	public IItemService getItemService() {
		return itemService;
	}

	public void setItemService(IItemService itemService) {
		this.itemService = itemService;
	}

	public IAdvertService getAdvertService() {
		return advertService;
	}

	public void setAdvertService(IAdvertService advertService) {
		this.advertService = advertService;
	}

	public List<PointsCard> getPointsCardList() {
		return pointsCardList;
	}

	public void setPointsCardList(List<PointsCard> pointsCardList) {
		this.pointsCardList = pointsCardList;
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	public List<Advert> getAdvertList() {
		return advertList;
	}

	public void setAdvertList(List<Advert> advertList) {
		this.advertList = advertList;
	}

}
