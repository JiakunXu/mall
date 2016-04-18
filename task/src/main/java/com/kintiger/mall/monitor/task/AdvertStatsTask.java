package com.kintiger.mall.monitor.task;

import com.kintiger.mall.api.advert.IAdvertStatsService;
import com.kintiger.mall.framework.annotation.Profiler;

/**
 * 积分历史记录统计.
 * 
 * @author xujiakun
 * 
 */
public class AdvertStatsTask {

	private IAdvertStatsService advertStatsService;

	@Profiler
	public void createAdvertStats() {
		advertStatsService.createAdvertStats();
	}

	public IAdvertStatsService getAdvertStatsService() {
		return advertStatsService;
	}

	public void setAdvertStatsService(IAdvertStatsService advertStatsService) {
		this.advertStatsService = advertStatsService;
	}

}
