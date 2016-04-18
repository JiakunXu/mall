package com.kintiger.mall.api.file;

import java.util.List;

import com.kintiger.mall.api.file.bo.FileInfo;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 文件.
 * 
 * @author xujiakun
 * 
 */
public interface IFileService {

	String ERROR_MESSAGE = "操作失败！";

	String ERROR_INPUT_MESSAGE = "操作失败，输入有误！";

	String FILE_TYPE_DEFAULT = "dfs";

	String FILE_TYPE_HDFS = "hdfs";

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	/**
	 * 获取文件信息.
	 * 
	 * @param shopId
	 * @param fileId
	 * @return
	 */
	FileInfo getFileInfo(String shopId, String fileId);

	/**
	 * 保存文件信息.
	 * 
	 * @param shopId
	 * @param fileInfo
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createFileInfo(String shopId, FileInfo fileInfo, String modifyUser);

	/**
	 * 查询文件列表.
	 * 
	 * @param shopId
	 * @param fileInfo
	 * @return
	 */
	int getFileCount(String shopId, FileInfo fileInfo);

	/**
	 * 查询文件列表.
	 * 
	 * @param shopId
	 * @param fileInfo
	 * @return
	 */
	List<FileInfo> getFileList(String shopId, FileInfo fileInfo);

	/**
	 * 删除文件.
	 * 
	 * @param shopId
	 * @param fileId
	 * @param modifyUser
	 * @return
	 */
	BooleanResult deleteFile(String shopId, String[] fileId, String modifyUser);

}
