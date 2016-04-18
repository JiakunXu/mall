package com.kintiger.mall.pageview.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.pageview.IPageviewService;
import com.kintiger.mall.api.pageview.bo.Pageview;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.pageview.dao.IPageviewDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class PageviewServiceImpl implements IPageviewService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(PageviewServiceImpl.class);

	private IPageviewDao pageviewDao;

	@Override
	public boolean createPageview(List<Pageview> pageviews) {
		if (pageviews == null || pageviews.size() == 0) {
			return false;
		}

		try {
			pageviewDao.createPageview(pageviews);
			return true;
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(pageviews), e);
		}

		return false;
	}

	@Override
	public List<Pageview> getPageviewStats(String shopId, String actionName, String gmtStart, String gmtEnd) {
		if (StringUtils.isBlank(shopId)) {
			return null;
		}

		Pageview pageview = new Pageview();
		pageview.setShopId(shopId.trim());
		pageview.setActionName(actionName);
		pageview.setGmtStart(gmtStart);
		pageview.setGmtEnd(gmtEnd);

		try {
			return pageviewDao.getPageviewStats(pageview);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(pageview), e);
		}

		return null;
	}

	public IPageviewDao getPageviewDao() {
		return pageviewDao;
	}

	public void setPageviewDao(IPageviewDao pageviewDao) {
		this.pageviewDao = pageviewDao;
	}

}
