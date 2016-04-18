package com.kintiger.mall.file.service.impl;

import java.io.File;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.api.file.IDFSService;
import com.kintiger.mall.api.file.IFileService;
import com.kintiger.mall.api.file.bo.FileInfo;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.util.FileUtil;
import com.kintiger.mall.framework.util.UUIDUtil;

/**
 * 
 * @author xujiakun
 * 
 */
public class DFSServiceImpl implements IDFSService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(DFSServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IFileService fileService;

	private String fileUploadPath;

	private String fileDownloadPath;

	@Override
	public String getFileName(String shopId, String fileId) {
		FileInfo fileInfo = fileService.getFileInfo(shopId, fileId);

		if (fileInfo != null) {
			if (StringUtils.isNotEmpty(fileInfo.getSuffix())) {
				return fileInfo.getFileName() + "." + fileInfo.getSuffix();
			} else {
				return fileInfo.getFileName();
			}
		}

		return null;
	}

	@Override
	public boolean fetchFile(String shopId, String fileId, String localFilePath) {
		return false;
	}

	@Override
	public boolean fetchFile(String shopId, String fileId, OutputStream output) {
		return false;
	}

	private BooleanResult validate(String shopId, String fileName, String suffix, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(fileName)) {
			result.setCode("文件信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		result.setResult(true);
		return result;
	}

	@Override
	public BooleanResult saveFile(String shopId, String fileName, String suffix, String localFileName, String modifyUser) {
		return null;
	}

	@Override
	public BooleanResult saveFile(final String shopId, final String fileName, final String suffix,
		final File localFile, final String modifyUser) {
		// 验证
		BooleanResult result = validate(shopId, fileName, suffix, modifyUser);
		if (!result.getResult()) {
			return result;
		}

		if (localFile == null) {
			result.setCode("文件内容信息不能为空！");
			return result;
		}

		StringBuilder sb = new StringBuilder("/");
		sb.append(shopId).append("/").append(DateUtil.getNowDatetimeStr("yyyy")).append("/")
			.append(DateUtil.getNowDatetimeStr("MM")).append("/").append(DateUtil.getNowDatetimeStr("dd")).append("/")
			.append(UUIDUtil.generate()).append(".").append(suffix);

		final String path = sb.toString();
		final String uploadPath = fileUploadPath + path;
		final String downloadPath = fileDownloadPath + path;

		result = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// 1. createFileInfo
				BooleanResult result = createFileInfo(shopId, fileName, suffix, path, modifyUser);

				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				} else {
					result.setCode("{\"id\":\"" + result.getCode() + "\",\"downloadPath\":\"" + downloadPath + "\"}");
				}

				// 2. save file to df
				try {
					FileUtil.save(uploadPath, localFile);
				} catch (ServiceException e) {
					logger.error(e);
					ts.setRollbackOnly();

					result.setResult(false);
					result.setCode("保存文件内容信息失败！");
					return result;
				}

				return result;
			}
		});

		return result;
	}

	@Override
	public BooleanResult saveFile(String shopId, String fileName, String suffix, byte[] bytes, String modifyUser) {
		return null;
	}

	/**
	 * createFileInfo.
	 * 
	 * @param fileId
	 * @param fileName
	 * @param suffix
	 * @param workDir
	 * @return
	 * @throws ServiceException
	 */
	private BooleanResult createFileInfo(String shopId, String fileName, String suffix, String filePath,
		String modifyUser) {
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFileName(fileName);
		fileInfo.setSuffix(suffix);
		fileInfo.setFilePath(filePath);
		fileInfo.setFileType(IFileService.FILE_TYPE_DEFAULT);

		return fileService.createFileInfo(shopId, fileInfo, modifyUser);
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public IFileService getFileService() {
		return fileService;
	}

	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

	public String getFileDownloadPath() {
		return fileDownloadPath;
	}

	public void setFileDownloadPath(String fileDownloadPath) {
		this.fileDownloadPath = fileDownloadPath;
	}

}
