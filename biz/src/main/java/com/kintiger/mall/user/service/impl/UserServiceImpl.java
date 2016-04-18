package com.kintiger.mall.user.service.impl;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.user.IUserService;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.EncryptUtil;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.user.dao.IUserDao;

/**
 * user service.
 * 
 * @author xujiakun
 * 
 */
public class UserServiceImpl implements IUserService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(UserServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private IUserDao userDao;

	@Override
	public BooleanResult createUser(final User user) {
		BooleanResult result = new BooleanResult();
		result.setCode(IUserService.ERROR_MESSAGE);
		result.setResult(false);

		// 创建用户
		String userId = null;
		try {
			userId = userDao.createUser(user);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(user), e);

			if (e.getMessage().indexOf("ORA-00001") != -1) {
				result.setCode("手机号码已注册！");
			}

			return result;
		}

		result.setCode(userId);
		result.setResult(true);
		return result;
	}

	@Override
	public User getUser4Validate(String passport) {
		if (StringUtils.isBlank(passport)) {
			return null;
		}

		User user = null;

		try {
			user =
				(User) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_PASSPORT
					+ passport.trim().toUpperCase());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_PASSPORT + passport, e);
		}

		if (user != null) {
			return user;
		}

		try {
			user = userDao.getUser4Validate(passport.trim());
		} catch (Exception e) {
			logger.error(passport, e);
		}

		// not null then set to cache
		if (user != null) {
			try {
				// 1h 超时
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_PASSPORT + passport.trim().toUpperCase(),
					user, IMemcachedCacheService.CACHE_KEY_PASSPORT_DEFAULT_EXP);
			} catch (ServiceException e) {
				logger.error(IMemcachedCacheService.CACHE_KEY_PASSPORT + passport, e);
			}
		}

		return user;
	}

	@Override
	public User getUser(String userId) {
		if (StringUtils.isBlank(userId)) {
			return null;
		}

		User user = null;

		try {
			user = (User) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_USER_ID + userId.trim());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_USER_ID + userId, e);
		}

		if (user != null) {
			return user;
		}

		user = new User();
		user.setUserId(userId.trim());
		try {
			user = userDao.getUser(user);
		} catch (Exception e) {
			logger.error(userId, e);

			user = null;
		}

		// not null then set to cache
		if (user != null) {
			try {
				// 1h 超时
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_USER_ID + userId.trim(), user,
					IMemcachedCacheService.CACHE_KEY_USER_ID_DEFAULT_EXP);
			} catch (ServiceException e) {
				logger.error(IMemcachedCacheService.CACHE_KEY_USER_ID + userId, e);
			}
		}

		return user;
	}

	@Override
	public BooleanResult updateUser(String userId, User user) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (user == null) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(userId)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		user.setUserId(userId.trim());
		user.setModifyUser(userId);

		try {
			int c = userDao.updateUser(user);

			if (c == 1) {
				result.setCode("修改信息成功！");
				result.setResult(true);
			} else {
				result.setCode("修改信息失败，请稍后再试！");
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(user), e);

			result.setCode("修改信息失败！");
		}

		return result;
	}

	@Override
	public BooleanResult resetPassword(String userId, String password, String oldPassword) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		User user = new User();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		user.setUserId(userId.trim());

		if (StringUtils.isBlank(password)) {
			result.setCode("新密码不能为空！");
			return result;
		}

		try {
			user.setPassword(EncryptUtil.encryptSHA(password));
		} catch (IOException e) {
			logger.error("password:" + password, e);
			result.setCode("新密码加密失败！");
			return result;
		}

		if (StringUtils.isBlank(oldPassword)) {
			result.setCode("原密码不能为空！");
			return result;
		}

		try {
			user.setOldPassword(EncryptUtil.encryptSHA(oldPassword));
		} catch (IOException e) {
			logger.error("oldPassword:" + oldPassword, e);
			result.setCode("原密码加密失败！");
			return result;
		}

		user.setModifyUser(userId);

		return updatePassword(user);
	}

	@Override
	public BooleanResult setPassword(String userId, String password, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		User user = new User();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		user.setUserId(userId.trim());

		if (StringUtils.isBlank(password)) {
			result.setCode("密码不能为空！");
			return result;
		}

		try {
			user.setPassword(EncryptUtil.encryptSHA(password));
		} catch (IOException e) {
			logger.error("password:" + password, e);

			result.setCode("密码加密失败！");
			return result;
		}

		user.setModifyUser(modifyUser);

		return updatePassword(user);
	}

	private BooleanResult updatePassword(User user) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		try {
			int c = userDao.updatePassword(user);

			if (c == 1) {
				result.setCode("修改密码成功！");
				result.setResult(true);

				// remove cache
				if (StringUtils.isNotEmpty(user.getPassport())) {
					remove(user.getUserId(), user.getPassport());
				} else {
					remove(user.getUserId());
				}
			} else {
				result.setCode("修改密码失败，请稍后再试！");
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(user), e);

			result.setCode("修改密码失败！");
		}

		return result;
	}

	/**
	 * remove cache.
	 * 
	 * @param userId
	 */
	private void remove(String userId) {
		try {
			memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_USER_ID + userId);
		} catch (ServiceException e) {
			logger.error(e);
		}

		// 根据 userId 获取 passport
		User u = getUser(userId);
		if (u != null) {
			try {
				memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_PASSPORT + u.getPassport());
			} catch (ServiceException e) {
				logger.error(e);
			}
		}
	}

	/**
	 * remove cache.
	 * 
	 * @param userId
	 * @param passport
	 */
	private void remove(String userId, String passport) {
		if (StringUtils.isNotEmpty(userId)) {
			try {
				memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_USER_ID + userId);
			} catch (ServiceException e) {
				logger.error(e);
			}
		}

		try {
			memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_PASSPORT + passport);
		} catch (ServiceException e) {
			logger.error(e);
		}
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

}
