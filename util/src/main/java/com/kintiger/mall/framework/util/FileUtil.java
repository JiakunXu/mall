package com.kintiger.mall.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * File Util.
 * 
 * @author xujiakun
 * 
 */
public final class FileUtil {

	private static final Logger logger = Logger.getLogger(FileUtil.class);

	private static final int BUFFER_SIZE = 16 * 1024;

	private FileUtil() {

	}

	public static boolean save(String path, String content) {
		boolean flag = false;
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(path));
			out.write(content.getBytes("GBK"));
			flag = true;
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}

		return flag;
	}

	/**
	 * 文件保存.
	 * 
	 * @param path
	 * @param source
	 * @return 文件路径
	 * @throws SystemException
	 */
	public static String save(String path, File source) throws RuntimeException {
		File target = new File(path);

		boolean flag = true;

		try {
			if (!target.getParentFile().exists()) {
				flag = target.getParentFile().mkdirs();
			}
			if (flag && !target.exists()) {
				flag = target.createNewFile();
			}
		} catch (IOException e) {
			logger.error("文件创建失败！", e);
		}

		if (!flag) {
			throw new RuntimeException("文件创建失败！");
		}

		// save as
		if (!saveAs(source, target)) {
			throw new RuntimeException("文件保存失败！");
		}

		return path;
	}

	/**
	 * 文件保存.
	 * 
	 * @param path
	 * @param bytes
	 * @return 文件路径
	 * @throws SystemException
	 */
	public static String save(String path, byte[] bytes) throws RuntimeException {
		File target = new File(path);

		boolean flag = true;

		try {
			if (!target.getParentFile().exists()) {
				flag = target.getParentFile().mkdirs();
			}
			if (flag && !target.exists()) {
				flag = target.createNewFile();
			}
		} catch (IOException e) {
			logger.error("文件创建失败！", e);
		}

		if (!flag) {
			throw new RuntimeException("文件创建失败！");
		}

		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(target), bytes.length);
			out.write(bytes, 0, bytes.length);
		} catch (Exception e) {
			logger.error("文件保存失败！", e);

			throw new RuntimeException("文件保存失败！");
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}

		return path;
	}

	/**
	 * 文件另存为.
	 * 
	 * @param source
	 *            源文件
	 * @param target
	 *            目标文件
	 * @return 保存是否成功
	 */
	public static boolean saveAs(File source, File target) {
		boolean flag = false;
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(source), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(target), BUFFER_SIZE);

			byte[] buffer = new byte[BUFFER_SIZE];
			int len;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}

			flag = true;
		} catch (Exception e) {
			logger.error("文件另存为失败！", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}

		return flag;
	}

	public static String read(String path) {
		StringBuilder fileContent = new StringBuilder();
		InputStreamReader in = null;
		BufferedReader reader = null;
		try {
			in = new InputStreamReader(new FileInputStream(path), "GBK");
			reader = new BufferedReader(in);

			String tempStr = reader.readLine();
			while (tempStr != null) {
				fileContent.append(tempStr);
				tempStr = reader.readLine();
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}

		return fileContent.toString();
	}

	/**
	 * 读取文件 -> output.
	 * 
	 * @param path
	 * @param output
	 * @throws SystemException
	 */
	public static void read(String path, OutputStream output) throws Exception {
		InputStream in = null;

		try {
			in = new FileInputStream(path);
			IOUtils.copyBytes(in, output, BUFFER_SIZE, true);
		} catch (IOException e) {
			throw new Exception("read file from " + path, e);
		} finally {
			IOUtils.closeStream(in);
		}
	}

	public static boolean delete(String path) {
		if (StringUtils.isBlank(path)) {
			return false;
		}

		File file = new File(path);

		return file.delete();
	}

	public static byte[] toByteArray(File source) throws RuntimeException {
		InputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(source), BUFFER_SIZE);
			out = new ByteArrayOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int len;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}

			return out.toByteArray();
		} catch (Exception e) {
			logger.error("文件转化失败！", e);

			throw new RuntimeException("文件转化失败！");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}

	/**
	 * 获取文件类型名 (.txt).
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtention(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}

		return fileName.substring(fileName.lastIndexOf('.'));
	}

	/**
	 * 获取文件名.
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getName(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}

		if (fileName.lastIndexOf('.') == -1) {
			return fileName;
		} else {
			return fileName.substring(0, fileName.lastIndexOf('.'));
		}
	}

	/**
	 * 获取文件类型名 (txt).
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getSuffix(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}

		if (fileName.lastIndexOf('.') == -1) {
			return "";
		} else {
			return fileName.substring(fileName.lastIndexOf('.') + 1);
		}
	}

}
