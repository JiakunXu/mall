package com.kintiger.mall.hadoop.service.impl;

import java.io.File;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.api.file.IDFSService;
import com.kintiger.mall.api.file.IFileService;
import com.kintiger.mall.api.file.bo.FileInfo;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.HDFSUtil;
import com.kintiger.mall.framework.util.UUIDUtil;

/**
 * hdfs service().
 * 
 * @author xujiakun
 * 
 */
public class HDFSServiceImpl implements IDFSService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(HDFSServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IFileService fileService;

	private String ip;

	private int port;

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
		FileSystem fs = null;
		try {
			fs = HDFSUtil.getFileSystem(ip, port);

			FileInfo fileInfo = fileService.getFileInfo(shopId, fileId);

			HDFSUtil.download(fs, localFilePath + "/" + fileInfo.getFileName() + "." + fileInfo.getSuffix(),
				fileInfo.getFilePath());

			return true;
		} catch (Exception e) {
			logger.error(e);
		}

		return false;
	}

	@Override
	public boolean fetchFile(String shopId, String fileId, OutputStream output) {
		FileSystem fs = null;
		try {
			fs = HDFSUtil.getFileSystem(ip, port);

			FileInfo fileInfo = fileService.getFileInfo(shopId, fileId);

			HDFSUtil.read(fs, fileInfo.getFilePath(), output);

			return true;
		} catch (Exception e) {
			logger.error(e);
		}

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
		// 验证
		BooleanResult result = validate(shopId, fileName, suffix, modifyUser);
		if (!result.getResult()) {
			return result;
		}

		FileSystem fs = null;
		try {
			fs = HDFSUtil.getFileSystem(ip, port);
			// 0. rename fileName
			String fileId = UUIDUtil.generate();

			// 1. save file 2 hdfs
			HDFSUtil.upload(fs, localFileName, fileId);

			// 2. save info 2 oracle
			Path workDir = fs.getWorkingDirectory();

			return createFileInfo(shopId, fileId, fileName, suffix, workDir, modifyUser);
		} catch (Exception e) {
			logger.error(e);

			result.setResult(false);
			result.setCode("保存文件内容信息失败！");
		}

		return result;
	}

	@Override
	public BooleanResult saveFile(String shopId, String fileName, String suffix, File localFile, String modifyUser) {
		// 验证
		BooleanResult result = validate(shopId, fileName, suffix, modifyUser);
		if (!result.getResult()) {
			return result;
		}

		FileSystem fs = null;
		try {
			fs = HDFSUtil.getFileSystem(ip, port);
			// 0. rename fileName
			String fileId = UUIDUtil.generate();

			// 1. save file 2 hdfs
			HDFSUtil.write(fs, fileId, localFile);

			// 2. save info 2 oracle
			Path workDir = fs.getWorkingDirectory();

			return createFileInfo(shopId, fileId, fileName, suffix, workDir, modifyUser);
		} catch (Exception e) {
			logger.error(e);

			result.setResult(false);
			result.setCode("保存文件内容信息失败！");
		}

		return result;
	}

	@Override
	public BooleanResult saveFile(String shopId, String fileName, String suffix, byte[] bytes, String modifyUser) {
		// 验证
		BooleanResult result = validate(shopId, fileName, suffix, modifyUser);
		if (!result.getResult()) {
			return result;
		}

		FileSystem fs = null;
		try {
			fs = HDFSUtil.getFileSystem(ip, port);
			// 0. rename fileName
			String fileId = UUIDUtil.generate();

			// 1. save file 2 hdfs
			HDFSUtil.write(fs, fileId, bytes);

			// 2. save info 2 oracle
			Path workDir = fs.getWorkingDirectory();

			return createFileInfo(shopId, fileId, fileName, suffix, workDir, modifyUser);
		} catch (Exception e) {
			logger.error(e);

			result.setResult(false);
			result.setCode("保存文件内容信息失败！");
		}

		return result;
	}

	/**
	 * createFileInfo.
	 * 
	 * @param shopId
	 * @param fileId
	 * @param fileName
	 * @param suffix
	 * @param workDir
	 * @return
	 */
	private BooleanResult createFileInfo(String shopId, String fileId, String fileName, String suffix, Path workDir,
		String modifyUser) {
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFileName(fileName);
		fileInfo.setSuffix(suffix);
		fileInfo.setFilePath(workDir + "/" + fileId);
		fileInfo.setFileType(IFileService.FILE_TYPE_HDFS);

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
