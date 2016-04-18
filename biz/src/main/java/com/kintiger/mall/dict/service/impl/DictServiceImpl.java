package com.kintiger.mall.dict.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.dict.dao.IDictDao;
import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.dict.IDictService;
import com.kintiger.mall.api.dict.bo.Dict;
import com.kintiger.mall.api.dict.bo.DictType;
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
public class DictServiceImpl implements IDictService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(DictServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IMemcachedCacheService memcachedCacheService;

	private IDictDao dictDao;

	public int getDictTypeCount(DictType dictType) {
		try {
			return dictDao.getDictTypeCount(dictType);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(dictType), e);
		}

		return 0;
	}

	public List<DictType> getDictTypeList(DictType dictType) {
		try {
			return dictDao.getDictTypeList(dictType);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(dictType), e);
		}

		return null;
	}

	public int getDictCount(Dict dict) {
		try {
			return dictDao.getDictCount(dict);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(dict), e);
		}

		return 0;
	}

	public List<Dict> getDictList(Dict dict) {
		try {
			return dictDao.getDictList(dict);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(dict), e);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Dict> getDicts(Dict dict) {
		List<Dict> list = null;
		StringBuilder s = new StringBuilder("dict.getDicts");

		if (dict != null) {
			if (StringUtils.isNotEmpty(dict.getDictTypeValue())) {
				s.append(dict.getDictTypeValue());
			}
			if (StringUtils.isNotEmpty(dict.getDictName())) {
				s.append(dict.getDictName());
			}
			if (StringUtils.isNotEmpty(dict.getDictValue())) {
				s.append(dict.getDictValue());
			}
		}

		try {
			list = (List<Dict>) memcachedCacheService.get(s.toString());
		} catch (ServiceException e) {
			logger.error(s.toString(), e);
		}

		if (list != null && list.size() > 0) {
			return list;
		}

		try {
			list = dictDao.getDicts(dict);
			// not null then set to cache
			if (list != null && list.size() > 0) {
				// 1m 超时
				memcachedCacheService.set(s.toString(), list, IMemcachedCacheService.CACHE_KEY_DICT_DEFAULT_EXP);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(dict), e);
		}

		return list;
	}

	public Map<String, String> getDictMap(String dictTypeValue) {
		Map<String, String> map = new HashMap<String, String>();

		if (StringUtils.isEmpty(dictTypeValue) || StringUtils.isEmpty(dictTypeValue.trim())) {
			return map;
		}

		Dict dict = new Dict();
		dict.setDictTypeValue(dictTypeValue.trim());
		List<Dict> dicts = getDicts(dict);

		if (dicts != null && dicts.size() > 0) {
			for (Dict d : dicts) {
				map.put(d.getDictName(), d.getDictValue());
			}
		}

		return map;
	}

	public BooleanResult createDict(Dict dict) {
		BooleanResult booleanResult = new BooleanResult();
		booleanResult.setResult(false);
		booleanResult.setCode(IDictService.ERROR_MESSAGE);

		try {
			String dictId = dictDao.createDict(dict);
			booleanResult.setResult(true);
			booleanResult.setCode(dictId);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(dict), e);
		}

		return booleanResult;
	}

	public BooleanResult createDictType(DictType dictType) {
		BooleanResult booleanResult = new BooleanResult();
		booleanResult.setResult(false);
		booleanResult.setCode(IDictService.ERROR_MESSAGE);

		try {
			String dictTypeId = dictDao.createDictType(dictType);
			booleanResult.setResult(true);
			booleanResult.setCode(dictTypeId);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(dictType), e);
		}

		return booleanResult;
	}

	public BooleanResult updateDict(Dict dict) {
		BooleanResult booleanResult = new BooleanResult();
		booleanResult.setResult(false);
		booleanResult.setCode(IDictService.ERROR_MESSAGE);

		try {
			int n = dictDao.updateDict(dict);
			if (n == 1) {
				booleanResult.setResult(true);
			} else {
				booleanResult.setCode(IDictService.ERROR_NULL_MESSAGE);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(dict), e);
		}

		return booleanResult;
	}

	public BooleanResult updateDictType(DictType dictType) {
		BooleanResult booleanResult = new BooleanResult();
		booleanResult.setResult(false);
		booleanResult.setCode(IDictService.ERROR_MESSAGE);

		try {
			int n = dictDao.updateDictType(dictType);
			if (n == 1) {
				booleanResult.setResult(true);
			} else {
				booleanResult.setCode(IDictService.ERROR_NULL_MESSAGE);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(dictType), e);
		}

		return booleanResult;
	}

	public BooleanResult deleteDictType(final DictType dictType) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);
		result.setCode(IDictService.ERROR_MESSAGE);

		boolean o = (Boolean) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				try {
					// 直接物理删除
					dictType.setState("D");
					int n = dictDao.deleteDictType(dictType);
					if (n != 1) {
						ts.setRollbackOnly();
						return false;
					}
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(dictType), e);
					ts.setRollbackOnly();
					return false;
				}

				Dict dict = new Dict();
				try {
					// 直接物理删除
					dict.setDictTypeId(dictType.getDictTypeId());
					dict.setModifyUser(dictType.getModifyUser());
					dict.setState("D");
					dictDao.deleteDict(dict);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(dict), e);
					ts.setRollbackOnly();
					return false;
				}

				return true;
			}
		});

		if (o) {
			result.setResult(true);
		}

		return result;
	}

	public BooleanResult deleteDict(Dict dict) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);
		result.setCode(IDictService.ERROR_MESSAGE);

		if (dict == null || StringUtils.isEmpty(dict.getDictId())) {
			return result;
		}

		try {
			int n = dictDao.deleteDict(dict);
			if (n == 1) {
				result.setResult(true);
			} else {
				result.setCode(IDictService.ERROR_NULL_MESSAGE);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(dict), e);
		}

		return result;
	}

	public DictType getDictType(DictType dictType) {
		try {
			return dictDao.getDictType(dictType);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(dictType), e);
		}

		return null;
	}

	public Dict getDict(Dict dict) {
		try {
			return dictDao.getDict(dict);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(dict), e);
		}

		return null;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IDictDao getDictDao() {
		return dictDao;
	}

	public void setDictDao(IDictDao dictDao) {
		this.dictDao = dictDao;
	}

}
