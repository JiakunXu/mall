package com.kintiger.mall.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * 
 * @author xujiakun
 * 
 */
public class SWFUtil {

	private static final Logger logger = Logger.getLogger(SWFUtil.class);

	/**
	 * 
	 * @param sourcePath
	 * @param path
	 * @param type
	 *            文件类型.
	 * @return
	 * @throws RuntimeException
	 */
	public static String convert(String sourcePath, String path, String type) throws RuntimeException {
		String p = path + "/" + DateUtil.getNowDatetimeStr("yyyyMM") + "/" + UUIDUtil.generate() + ".swf";

		Process process = null;
		InputStreamReader in = null;
		BufferedReader reader = null;

		String cmd = getToolPath(type) + sourcePath + " -o " + p;

		try {
			// Runtime执行后返回创建的进程对象
			process = Runtime.getRuntime().exec(cmd);

			in = new InputStreamReader(process.getInputStream());
			reader = new BufferedReader(in);

			while (reader.readLine() != null) {
				;
			}

			// 调用waitFor方法，是为了阻塞当前进程，直到cmd执行完
			process.waitFor();
		} catch (IOException e) {
			logger.error(e);
			throw new RuntimeException("PDF转SWF失败！");
		} catch (InterruptedException e) {
			logger.error(e);
			throw new RuntimeException("PDF转SWF失败！");
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

		return p;
	}

	private static String getToolPath(String type) {
		String osName = System.getProperty("os.name");

		if (Pattern.matches("Linux.*", osName)) {
			if ("FONT".equals(type)) {
				return "/usr/local/bin/font2swf ";
			} else if ("GIF".equals(type)) {
				return "/usr/local/bin/gif2swf ";
			} else if ("JPEG".equals(type) || "JPG".equals(type)) {
				return "/usr/local/bin/jpeg2swf -T 9 ";
			} else if ("PDF".equals(type)) {
				return "/usr/local/bin/pdf2swf -T 9 -t ";
			} else if ("PNG".equals(type)) {
				return "/usr/local/bin/png2swf -T 9 ";
			} else if ("WAV".equals(type)) {
				return "/usr/local/bin/wav2swf ";
			}
		} else if (Pattern.matches("Windows.*", osName)) {
			return "";
		} else if (Pattern.matches("Mac.*", osName)) {
			return "";
		}

		return null;
	}

}
