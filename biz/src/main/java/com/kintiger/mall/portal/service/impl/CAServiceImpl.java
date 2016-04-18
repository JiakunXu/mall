package com.kintiger.mall.portal.service.impl;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.portal.ICAService;
import com.kintiger.mall.api.portal.bo.ValidateResult;
import com.kintiger.mall.api.user.IUserService;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.EncryptUtil;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.framework.util.UUIDUtil;

/**
 * 
 * @author xujiakun
 * 
 */
public class CAServiceImpl implements ICAService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(CAServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private IUserService userService;

	public ValidateResult validateUser(String passport, String password) {
		// 初始化返回值 状态 = 失败
		ValidateResult result = new ValidateResult();
		result.setResultCode(ICAService.RESULT_FAILED);
		result.setMessage(ICAService.INCORRECT_LOGIN);

		// 账号或密码为空
		if (StringUtils.isBlank(passport) || StringUtils.isEmpty(password)) {
			result.setMessage(ICAService.INCORRECT_NULL);
			return result;
		}

		// 根据passport查找用户信息
		User user = userService.getUser4Validate(passport.trim());

		// 判断登录用户是否在系统中
		if (user == null) {
			result.setMessage(ICAService.INCORRECT_LOGINID);
			return result;
		}

		try {
			if (EncryptUtil.encryptSHA(password).equals(user.getPassword())) {
				return setSuccessResult(result, user);
			}
		} catch (IOException e) {
			logger.error(e);
		}

		return result;
	}

	public ValidateResult validateToken(String token) {
		ValidateResult result = new ValidateResult();
		result.setResultCode(ICAService.RESULT_FAILED);
		result.setMessage(ICAService.INCORRECT_TOKEN);

		// if token is null then return
		if (StringUtils.isEmpty(token) || StringUtils.isEmpty(token.trim())) {
			return result;
		}

		try {
			User user = (User) memcachedCacheService.get(token);
			if (user != null) {
				// 令牌验证一次后 失效
				memcachedCacheService.remove(token);
				return setSuccessResult(result, user);
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return result;
	}

	public String generateToken(Object object) {
		try {
			String token = UUIDUtil.generate();
			memcachedCacheService.add(token, object, IMemcachedCacheService.CACHE_KEY_SSO_TOKEN_DEFAULT_EXP);

			return token;
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(object), e);
		}

		return null;
	}

	public boolean validateRequest(String userId, String url) {
		Boolean result = null;
		String key = userId + url;
		boolean state = (key.length() < 250) ? true : false;

		if (state) {
			try {
				result = (Boolean) memcachedCacheService.get(key);
			} catch (ServiceException e) {
				logger.error(userId + url, e);
			}
		}

		if (result != null) {
			return result;
		}

		return true;
	}

	private ValidateResult setSuccessResult(ValidateResult result, User user) {
		result.setResultCode(ICAService.RESULT_SUCCESS);
		user.setPassword(null);
		result.setUser(user);
		result.setMessage(null);
		return result;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

}
