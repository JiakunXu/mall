package com.kintiger.mall.file.dao;

import java.util.List;

import com.kintiger.mall.api.file.bo.FileInfo;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IFileDao {

	/**
	 * 
	 * @param fileInfo
	 * @return
	 */
	FileInfo getFileInfo(FileInfo fileInfo);

	/**
	 * 
	 * @param fileInfo
	 * @return
	 */
	String createFileInfo(FileInfo fileInfo);

	/**
	 * 
	 * @param fileInfo
	 * @return
	 */
	int getFileCount(FileInfo fileInfo);

	/**
	 * 
	 * @param fileInfo
	 * @return
	 */
	List<FileInfo> getFileList(FileInfo fileInfo);

	/**
	 * 
	 * @param fileInfo
	 * @return
	 */
	int deleteFile(FileInfo fileInfo);

}
