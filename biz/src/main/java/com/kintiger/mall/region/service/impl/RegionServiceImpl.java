package com.kintiger.mall.region.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.region.IRegionService;
import com.kintiger.mall.api.region.bo.Region;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.region.dao.IRegionDao;

/**
 * region 接口实现.
 * 
 * @author xujiakun
 * 
 */
public class RegionServiceImpl implements IRegionService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(RegionServiceImpl.class);

	private IRegionDao regionDao;

	@Override
	public List<Region> getRegionList(String parentRegionId) {
		if (StringUtils.isBlank(parentRegionId)) {
			return null;
		}

		Region region = new Region();
		region.setParentRegionId(parentRegionId.trim());

		try {
			return regionDao.getRegionList(region);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(region), e);
		}

		return null;
	}

	public IRegionDao getRegionDao() {
		return regionDao;
	}

	public void setRegionDao(IRegionDao regionDao) {
		this.regionDao = regionDao;
	}

}
