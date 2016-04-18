package com.kintiger.mall.api.file;

import java.io.File;
import java.io.OutputStream;

import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * DFS.
 * 
 * @author xujiakun
 * 
 */
public interface IDFSService {

	/**
	 * get a file's name.
	 * 
	 * @param shopId
	 * @param fileId
	 * @return fileName if save successully, or null if fail
	 */
	String getFileName(String shopId, String fileId);

	/**
	 * fetch a file to local disk.
	 * 
	 * @param shopId
	 * @param fileId
	 * @param localFilePath
	 * @return
	 */
	boolean fetchFile(String shopId, String fileId, String localFilePath);

	/**
	 * fetch a file to output stream.
	 * 
	 * @param shopId
	 * @param fileId
	 * @param output
	 * @return
	 */
	boolean fetchFile(String shopId, String fileId, OutputStream output);

	/**
	 * save a local file to hdfs.
	 * 
	 * @param shopId
	 * @param fileName
	 * @param suffix
	 * @param localFileName
	 * @param modifyUser
	 * @return fileId if save successully, or null if fail
	 */
	BooleanResult saveFile(String shopId, String fileName, String suffix, String localFileName, String modifyUser);

	/**
	 * save a local file(file) to hdfs.
	 * 
	 * @param shopId
	 * @param fileName
	 * @param suffix
	 * @param localFile
	 * @param modifyUser
	 * @return fileId if save successully, or null if fail
	 */
	BooleanResult saveFile(String shopId, String fileName, String suffix, File localFile, String modifyUser);

	/**
	 * save a local file(byte) to hdfs.
	 * 
	 * @param shopId
	 * @param fileName
	 * @param suffix
	 * @param bytes
	 * @param modifyUser
	 * @return fileId if save successully, or null if fail
	 */
	BooleanResult saveFile(String shopId, String fileName, String suffix, byte[] bytes, String modifyUser);

}
