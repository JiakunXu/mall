package com.kintiger.mall.pageview.dao;

import java.util.List;

import com.kintiger.mall.api.pageview.bo.Pageview;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IPageviewDao {

	/**
	 * 
	 * @param pageviews
	 * @return
	 */
	int createPageview(List<Pageview> pageviews);

	/**
	 * 
	 * @param pageview
	 * @return
	 */
	List<Pageview> getPageviewStats(Pageview pageview);

}
