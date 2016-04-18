package com.kintiger.mall.framework.util;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;

/**
 * 
 * @author xujiakun
 * 
 */
public class PDFUtil {

	private static final Logger logger = Logger.getLogger(PDFUtil.class);

	public static synchronized String convert(File source, String path) throws RuntimeException {
		String p = path + "/" + DateUtil.getNowDatetimeStr("yyyyMM") + "/" + UUIDUtil.generate() + ".pdf";
		File target = new File(p);

		boolean flag = true;

		try {
			if (!target.getParentFile().exists()) {
				flag = target.getParentFile().mkdirs();
			}
			if (flag && !target.exists()) {
				flag = target.createNewFile();
			}
		} catch (IOException e) {
			logger.error("PDF文件创建失败！", e);
		}

		if (!flag) {
			throw new RuntimeException("PDF文件创建失败！");
		}

		OfficeManager officeManager = null;

		try {
			officeManager =
				new DefaultOfficeManagerConfiguration().setOfficeHome(getOfficeHome())
					.setPortNumbers(8880, 8882, 8884, 8886, 8888).buildOfficeManager();
			officeManager.start();

			OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
			converter.convert(source, target);
		} catch (Exception e) {
			throw new RuntimeException("文件转PDF失败！", e);
		} finally {
			if (officeManager != null) {
				try {
					officeManager.stop();
				} catch (OfficeException e) {
					logger.error(e);
				}
			}
		}

		return p;
	}

	private static String getOfficeHome() {
		String osName = System.getProperty("os.name");

		if (Pattern.matches("Linux.*", osName)) {
			return "/opt/openoffice4";
		} else if (Pattern.matches("Windows.*", osName)) {
			return "";
		} else if (Pattern.matches("Mac.*", osName)) {
			return "";
		}

		return null;
	}

}
