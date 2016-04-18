package com.kintiger.mall.shop.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.item.IItemService;
import com.kintiger.mall.api.item.bo.Item;
import com.kintiger.mall.api.pageview.IPageviewService;
import com.kintiger.mall.api.shop.IShopService;
import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.api.tag.ITagService;
import com.kintiger.mall.api.tag.bo.Tag;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.Pageview;
import com.kintiger.mall.framework.util.DateUtil;

/**
 * 店铺管理.
 * 
 * @author xujiakun
 * 
 */
public class ShopAction extends BaseAction {

	private static final long serialVersionUID = 4514913330469582753L;

	private IShopService shopService;

	private IItemService itemService;

	private ITagService tagService;

	private IPageviewService pageviewService;

	/**
	 * 店铺首页查询.
	 */
	private Shop shop;

	/**
	 * 买家查询店铺商品.
	 */
	private String alias;

	/**
	 * 商品标签.
	 */
	private List<Tag> tagList;

	private List<Item> itemList;

	/**
	 * 在线商城 商品查询条件 参数.
	 */
	private String tagId;

	/**
	 * 浏览.
	 */
	private int pv1;

	/**
	 * 浏览.
	 */
	private int uv1;

	/**
	 * 到店.
	 */
	private int pv2;

	/**
	 * 到店.
	 */
	private int uv2;

	/**
	 * 店铺管理首页(经过拦截器验证权限).
	 * 
	 * @return
	 */
	public String index() {
		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return LOGIN_TIMEOUT;
		}

		shop = shopService.getShop(shopId);

		// 店铺不存在
		if (shop == null) {
			return NONE;
		}

		String gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -1), DateUtil.DEFAULT_DATE_FORMAT);

		// 昨日浏览 pv1 uv1
		com.kintiger.mall.api.pageview.bo.Pageview pageview = null;

		List<com.kintiger.mall.api.pageview.bo.Pageview> list =
			pageviewService.getPageviewStats(shopId, null, gmtStart, gmtStart);
		if (list != null && list.size() > 0) {
			pageview = list.get(0);
			if (pageview != null) {
				pv1 = pageview.getPv();
				uv1 = pageview.getUv();
			}
		}

		// 昨日到店 pv2 uv2
		list = pageviewService.getPageviewStats(shopId, IPageviewService.ACTION_NAME_HOMEPAGE, gmtStart, gmtStart);
		if (list != null && list.size() > 0) {
			pageview = list.get(0);
			if (pageview != null) {
				pv2 = pageview.getPv();
				uv2 = pageview.getUv();
			}
		}

		return SUCCESS;
	}

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 店铺首页(不经过拦截器验证权限).
	 * 
	 * @return
	 */
	@Pageview()
	public String homepage() {
		shop = super.getShop();

		// 根据 shopId 获取商品 tag
		tagList = tagService.getTagList(shop.getShopId());

		// 根据 shopId 获取在售商品
		Item item = new Item();

		if (StringUtils.isNotBlank(tagId)) {
			item.setTagId(tagId.trim());
		}

		int c = itemService.getItemCount(shop.getShopId(), item);
		if (c == 0) {
			return SUCCESS;
		}

		item.setStart(0);
		item.setLimit(c);
		item.setSort("MODIFY_DATE");
		item.setDir("DESC");

		itemList = itemService.getItemList(shop.getShopId(), item);

		return SUCCESS;
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

	public ITagService getTagService() {
		return tagService;
	}

	public void setTagService(ITagService tagService) {
		this.tagService = tagService;
	}

	public IPageviewService getPageviewService() {
		return pageviewService;
	}

	public void setPageviewService(IPageviewService pageviewService) {
		this.pageviewService = pageviewService;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public List<Tag> getTagList() {
		return tagList;
	}

	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public int getPv1() {
		return pv1;
	}

	public void setPv1(int pv1) {
		this.pv1 = pv1;
	}

	public int getUv1() {
		return uv1;
	}

	public void setUv1(int uv1) {
		this.uv1 = uv1;
	}

	public int getPv2() {
		return pv2;
	}

	public void setPv2(int pv2) {
		this.pv2 = pv2;
	}

	public int getUv2() {
		return uv2;
	}

	public void setUv2(int uv2) {
		this.uv2 = uv2;
	}

}
