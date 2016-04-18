package com.kintiger.mall.api.pageview;

import java.util.List;

import com.kintiger.mall.api.pageview.bo.Pageview;

/**
 * Pageview.
 * 
 * @author xujiakun
 * 
 */
public interface IPageviewService {

	String ACTION_NAME_HOMEPAGE = "/shop/homepage.htm";

	String ACTION_NAME_ITEM = "/item/item.htm";

	/**
	 * 创建记录 Pageview.
	 * 
	 * @param pageviews
	 * @return
	 */
	boolean createPageview(List<Pageview> pageviews);

	/**
	 * PV/UV.
	 * 
	 * @param shopId
	 * @param actionName
	 * @param gmtStart
	 * @param gmtEnd
	 * @return
	 */
	List<Pageview> getPageviewStats(String shopId, String actionName, String gmtStart, String gmtEnd);

}
