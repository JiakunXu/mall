package com.kintiger.mall.item.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.file.IFileService;
import com.kintiger.mall.api.item.IItemFileService;
import com.kintiger.mall.api.item.bo.ItemFile;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.item.dao.IItemFileDao;

/**
 * 商品文件.
 * 
 * @author xujiakun
 * 
 */
public class ItemFileServiceImpl implements IItemFileService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(ItemFileServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private IItemFileDao itemFileDao;

	private String fileDownloadPath;

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemFile> getItemFileList(String shopId, String itemId) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(itemId)) {
			return null;
		}

		List<ItemFile> itemFileList = null;

		try {
			itemFileList =
				(List<ItemFile>) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_ITEM_FILE_ITEM_ID
					+ itemId.trim());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_ITEM_FILE_ITEM_ID + itemId, e);
		}

		if (itemFileList != null && itemFileList.size() > 0) {
			return itemFileList;
		}

		ItemFile itemFile = new ItemFile();
		itemFile.setShopId(shopId.trim());
		itemFile.setItemId(itemId.trim());

		itemFileList = getItemFileList(itemFile);

		if (itemFileList == null || itemFileList.size() == 0) {
			return itemFileList;
		}

		for (ItemFile file : itemFileList) {
			// if file type == default then download the file by apache
			if (IFileService.FILE_TYPE_DEFAULT.equals(file.getFileType())) {
				file.setFilePath(fileDownloadPath + file.getFilePath());
			}
		}

		// set cache
		try {
			memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_ITEM_FILE_ITEM_ID + itemId.trim(), itemFileList,
				IMemcachedCacheService.CACHE_KEY_ITEM_FILE_ITEM_ID_DEFAULT_EXP);
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_ITEM_FILE_ITEM_ID + itemId, e);
		}

		return itemFileList;
	}

	@Override
	public Map<String, List<ItemFile>> getItemFileList(String shopId, String[] itemId) {
		if (StringUtils.isBlank(shopId) || itemId == null || itemId.length == 0) {
			return null;
		}

		Map<String, List<ItemFile>> map = new HashMap<String, List<ItemFile>>();

		// 调用接口 getItemFileList(String shopId, String itemId)
		for (String id : itemId) {
			if (map.containsKey(id)) {
				continue;
			}

			map.put(id, getItemFileList(shopId.trim(), id));
		}

		return map;
	}

	private List<ItemFile> getItemFileList(ItemFile itemFile) {
		try {
			return itemFileDao.getItemFileList(itemFile);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(itemFile), e);
		}

		return null;
	}

	@Override
	public BooleanResult deleteItemFile(String itemId, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		ItemFile itemFile = new ItemFile();

		if (StringUtils.isBlank(itemId)) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		itemFile.setItemId(itemId.trim());

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人不能为空！");
			return result;
		}

		itemFile.setModifyUser(modifyUser);

		try {
			itemFileDao.deleteItemFile(itemFile);
			result.setResult(true);

			// remove cache
			removeCache(itemId.trim());
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(itemFile), e);
		}

		return result;
	}

	@Override
	public BooleanResult createItemFile(String itemId, List<ItemFile> itemFileList, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(itemId)) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		if (itemFileList == null || itemFileList.size() == 0) {
			result.setCode("商品文件信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		try {
			itemFileDao.createItemFile(itemId, itemFileList, modifyUser);
			result.setResult(true);

			// remove cache
			removeCache(itemId.trim());
		} catch (Exception e) {
			logger.error("itemId:" + itemId + LogUtil.parserBean(itemFileList) + "modifyUser:" + modifyUser, e);

			result.setCode("创建商品文件信息失败！");
			return result;
		}

		return result;
	}

	private void removeCache(String itemId) {
		try {
			memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_ITEM_FILE_ITEM_ID + itemId);
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

	public IItemFileDao getItemFileDao() {
		return itemFileDao;
	}

	public void setItemFileDao(IItemFileDao itemFileDao) {
		this.itemFileDao = itemFileDao;
	}

	public String getFileDownloadPath() {
		return fileDownloadPath;
	}

	public void setFileDownloadPath(String fileDownloadPath) {
		this.fileDownloadPath = fileDownloadPath;
	}

}
