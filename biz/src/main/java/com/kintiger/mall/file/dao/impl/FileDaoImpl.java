package com.kintiger.mall.file.dao.impl;

import java.util.List;

import com.kintiger.mall.api.file.bo.FileInfo;
import com.kintiger.mall.file.dao.IFileDao;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;

/**
 * file dao.
 * 
 * @author xujiakun
 * 
 */
public class FileDaoImpl extends BaseDaoImpl implements IFileDao {

	@Override
	public FileInfo getFileInfo(FileInfo fileInfo) {
		return (FileInfo) getSqlMapClientTemplate().queryForObject("file.getFileInfo", fileInfo);
	}

	@Override
	public String createFileInfo(FileInfo fileInfo) {
		return (String) getSqlMapClientTemplate().insert("file.createFileInfo", fileInfo);
	}

	@Override
	public int getFileCount(FileInfo fileInfo) {
		return (Integer) getSqlMapClientTemplate().queryForObject("file.getFileCount", fileInfo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileInfo> getFileList(FileInfo fileInfo) {
		return (List<FileInfo>) getSqlMapClientTemplate().queryForList("file.getFileList", fileInfo);
	}

	@Override
	public int deleteFile(FileInfo fileInfo) {
		return getSqlMapClientTemplate().update("file.deleteFile", fileInfo);
	}

}
