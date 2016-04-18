package com.kintiger.mall.favorite.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.favorite.IFavoriteService;
import com.kintiger.mall.api.favorite.bo.Favorite;
import com.kintiger.mall.favorite.dao.IFavoriteDao;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;

/**
 * 
 * @author xujiakun
 * 
 */
public class FavoriteServiceImpl implements IFavoriteService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(FavoriteServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private TransactionTemplate transactionTemplate;

	private IFavoriteDao favoriteDao;

	public int getFavoriteCount(Favorite favorite) {
		try {
			return favoriteDao.getFavoriteCount(favorite);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(favorite), e);
		}

		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<Favorite> getFavoriteList(String userId, String node) {
		List<Favorite> list = null;

		try {
			list =
				(List<Favorite>) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_FAVORITE + userId + "&"
					+ node);
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_FAVORITE + userId + "&" + node, e);
		}

		if (list != null && list.size() > 0) {
			return list;
		}

		try {
			if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userId.trim()) || StringUtils.isEmpty(node)
				|| StringUtils.isEmpty(node.trim())) {
				return null;
			}

			Favorite f = new Favorite();
			f.setUserId(userId.trim());
			f.setPid(node.trim());

			list = getFavoriteList(f);

			// not null then set to cache
			if (list != null && list.size() > 0) {
				// 1h 超时
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_FAVORITE + userId + "&" + node, list,
					IMemcachedCacheService.CACHE_KEY_FAVORITE_DEFAULT_EXP);
			}
		} catch (Exception e) {
			logger.error("userId:" + userId + "node:" + node, e);
		}

		return list;
	}

	public List<Favorite> getFavoriteList(Favorite favorite) {
		try {
			return favoriteDao.getFavoriteList(favorite);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(favorite), e);
		}

		return null;
	}

	public BooleanResult createFavorite(Favorite favorite) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);
		result.setCode(IFavoriteService.ERROR_MESSAGE);

		try {
			favorite.setRank(getFavoriteCount(favorite));
			String id = favoriteDao.createFavorite(favorite);
			result.setCode(id);
			result.setResult(true);

			// 清空cache
			remove(favorite.getUserId(), favorite.getPid());
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(favorite), e);
		}

		return result;
	}

	public BooleanResult addFavorite(Favorite favorite) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);
		result.setCode(IFavoriteService.ERROR_MESSAGE);

		try {
			favorite.setRank(getFavoriteCount(favorite));
			String id = favoriteDao.addFavorite(favorite);
			result.setCode(id);
			result.setResult(true);

			// 清空cache
			remove(favorite.getUserId(), favorite.getPid());
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(favorite), e);
		}

		return result;
	}

	public BooleanResult updateFavorite(Favorite favorite) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);
		result.setCode(IFavoriteService.ERROR_MESSAGE);

		try {
			int c = favoriteDao.updateFavorite(favorite);
			if (c == 1) {
				result.setResult(true);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(favorite), e);
		}

		return result;
	}

	public BooleanResult deleteFavorite(Favorite favorite) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);
		result.setCode(IFavoriteService.ERROR_MESSAGE);

		try {
			int c = favoriteDao.deleteFavorite(favorite);
			result.setCode(String.valueOf(c));
			result.setResult(true);

			// 清空cache
			remove(favorite.getUserId(), favorite.getPid());
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(favorite), e);
		}

		return result;
	}

	public BooleanResult adjustFavoriteLevel(final List<Favorite> favoriteList, final String userId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);
		result.setCode(IFavoriteService.ERROR_MESSAGE);

		boolean o = (Boolean) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// 每个节点的子节点rank
				Map<String, Integer> map = new HashMap<String, Integer>();
				for (Favorite f : favoriteList) {
					try {
						Favorite g = favoriteDao.getFavorite(f);
						if (g == null) {
							ts.setRollbackOnly();
							return false;
						}

						remove(userId, g.getPid());

						// 修改人
						f.setModifyUser(userId);
						f.setUserId(userId);

						if (map.containsKey(f.getPid())) {
							int c = map.get(f.getPid());
							f.setRank(c);
							map.put(f.getPid(), c + 1);
						} else {
							f.setRank(0);
							map.put(f.getPid(), 1);
						}

						int c = favoriteDao.updateFavorite(f);
						if (c != 1) {
							ts.setRollbackOnly();
							return false;
						}

						remove(userId, f.getPid());
					} catch (Exception e) {
						logger.error(LogUtil.parserBean(favoriteList), e);
						ts.setRollbackOnly();
						return false;
					}
				}

				return true;
			}
		});

		if (o) {
			result.setResult(true);
		}

		return result;
	}

	/**
	 * remove cache.
	 * 
	 * @param id
	 */
	private void remove(String userId, String node) {
		try {
			memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_FAVORITE + userId + "&" + node);
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

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public IFavoriteDao getFavoriteDao() {
		return favoriteDao;
	}

	public void setFavoriteDao(IFavoriteDao favoriteDao) {
		this.favoriteDao = favoriteDao;
	}

}
