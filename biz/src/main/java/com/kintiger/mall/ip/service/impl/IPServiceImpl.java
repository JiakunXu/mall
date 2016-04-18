package com.kintiger.mall.ip.service.impl;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.ip.IIPService;
import com.kintiger.mall.api.ip.bo.IP;
import com.kintiger.mall.api.ip.bo.IPInfo;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.HttpUtil;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.ip.dao.IIPDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class IPServiceImpl implements IIPService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(IPServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private IIPDao ipDao;

	/**
	 * taobao 接口.
	 */
	private String url;

	@Override
	public boolean validate(String ip) {
		if (StringUtils.isBlank(ip)) {
			return false;
		}

		// 内网 ip
		if ("127.0.0.1".equals(ip)) {
			return false;
		}

		// 1. 锁定验证 ip
		// 锁定当前订单
		try {
			memcachedCacheService.add(IMemcachedCacheService.CACHE_KEY_IP + ip.trim(), ip,
				IMemcachedCacheService.CACHE_KEY_IP_DEFAULT_EXP);
		} catch (ServiceException e) {
			return false;
		}

		// 2. 验证是否已存在
		int count = getIPCount(ip.trim());

		if (count > 0) {
			return true;
		}

		if (count == -1) {
			return false;
		}

		// 3. 创建 IP 信息
		String response = null;

		try {
			response = HttpUtil.get(url + ip);
		} catch (Exception e) {
			logger.error(url + ip, e);

			return false;
		}

		// taobao 无返回信息
		if (StringUtils.isBlank(response)) {
			return false;
		}

		IPInfo ipInfo = JSON.parseObject(response, IPInfo.class);

		// 其中code的值的含义为，0：成功，1：失败。
		if ("1".equals(ipInfo.getCode())) {
			return false;
		}

		IP pi = ipInfo.getData();

		// IANA是IP地址分配的最高级机构，这种情况表示这个IP未被分配，或者是IANA的保留IP，如D、E类IP地址（224.0.0.0~255.255.255.255），局域网地址等。
		if ("IANA".equals(pi.getCountryId())) {
			return false;
		}

		return createIP(pi);
	}

	private int getIPCount(String ip) {
		int count = 0;

		// get ip count from cache
		try {
			Object object = memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_IP_COUNT + ip);
			if (object != null) {
				count = (Integer) object;
			}
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_IP_COUNT + ip, e);
		}

		if (count > 0) {
			return count;
		}

		try {
			IP pi = new IP();
			pi.setIp(ip);
			count = ipDao.getIPCount(pi);

			// if count > 0 add cache
			if (count > 0) {
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_IP_COUNT + ip, count,
					IMemcachedCacheService.CACHE_KEY_IP_COUNT_DEFAULT_EXP);
			}

			return count;
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(ip), e);
		}

		return -1;
	}

	private boolean createIP(IP ip) {
		try {
			ip.setModifyUser("system");
			ipDao.createIP(ip);
			return true;
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(ip), e);
		}

		return false;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IIPDao getIpDao() {
		return ipDao;
	}

	public void setIpDao(IIPDao ipDao) {
		this.ipDao = ipDao;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
