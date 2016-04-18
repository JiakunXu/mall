package com.kintiger.mall.file.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.file.IFileService;
import com.kintiger.mall.api.file.bo.FileInfo;
import com.kintiger.mall.file.dao.IFileDao;
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
public class FileServiceImpl implements IFileService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(FileServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private IFileDao fileDao;

	private String fileDownloadPath;

	@Override
	public FileInfo getFileInfo(String shopId, String fileId) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(fileId)) {
			return null;
		}

		FileInfo info = null;

		try {
			info = (FileInfo) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_FILE_ID + fileId.trim());

			if (info != null) {
				return info;
			}
		} catch (Exception e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_FILE_ID + fileId, e);
		}

		try {
			info = new FileInfo();
			info.setShopId(shopId.trim());
			info.setFileId(fileId.trim());
			info = fileDao.getFileInfo(info);

			if (info != null) {
				// if file type == default then download the file by apache
				if (IFileService.FILE_TYPE_DEFAULT.equals(info.getFileType())) {
					info.setFilePath(fileDownloadPath + info.getFilePath());
				}
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_FILE_ID + fileId.trim(), info);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(info), e);
		}

		return info;
	}

	@Override
	public BooleanResult createFileInfo(String shopId, FileInfo fileInfo, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		if (fileInfo == null) {
			result.setCode("文件内容信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		fileInfo.setShopId(shopId.trim());
		fileInfo.setModifyUser(modifyUser);

		try {
			String fileId = fileDao.createFileInfo(fileInfo);
			result.setCode(fileId);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(fileInfo), e);
		}

		return result;
	}

	@Override
	public int getFileCount(String shopId, FileInfo fileInfo) {
		if (StringUtils.isBlank(shopId) || fileInfo == null) {
			return 0;
		}

		fileInfo.setShopId(shopId.trim());

		try {
			return fileDao.getFileCount(fileInfo);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(fileInfo), e);
		}

		return 0;
	}

	@Override
	public List<FileInfo> getFileList(String shopId, FileInfo fileInfo) {
		if (StringUtils.isBlank(shopId) || fileInfo == null) {
			return null;
		}

		fileInfo.setShopId(shopId.trim());

		try {
			List<FileInfo> fileList = fileDao.getFileList(fileInfo);
			if (fileList != null && fileList.size() > 0) {
				for (FileInfo file : fileList) {
					if (IFileService.FILE_TYPE_DEFAULT.equals(file.getFileType())) {
						file.setFilePath(fileDownloadPath + file.getFilePath());
					}
				}
			}

			return fileList;
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(fileInfo), e);
		}

		return null;
	}

	@Override
	public BooleanResult deleteFile(String shopId, String[] fileId, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		FileInfo fileInfo = new FileInfo();

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		fileInfo.setShopId(shopId.trim());

		if (fileId == null || fileId.length == 0) {
			result.setCode("文件内容信息不能为空！");
			return result;
		}

		fileInfo.setCodes(fileId);

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		fileInfo.setModifyUser(modifyUser);

		try {
			int count = fileDao.deleteFile(fileInfo);
			result.setCode(String.valueOf(count));
			result.setResult(true);

			// clear cache
			removeCache(fileInfo.getCodes());
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(fileInfo), e);

			result.setCode("删除文件内容信息失败！");
			return result;
		}

		return result;
	}

	private void removeCache(String[] ids) {
		try {
			for (String id : ids) {
				memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_FILE_ID + id);
			}
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

	public IFileDao getFileDao() {
		return fileDao;
	}

	public void setFileDao(IFileDao fileDao) {
		this.fileDao = fileDao;
	}

	public String getFileDownloadPath() {
		return fileDownloadPath;
	}

	public void setFileDownloadPath(String fileDownloadPath) {
		this.fileDownloadPath = fileDownloadPath;
	}

}
