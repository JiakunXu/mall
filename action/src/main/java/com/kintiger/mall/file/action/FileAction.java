package com.kintiger.mall.file.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.file.IDFSService;
import com.kintiger.mall.api.file.IFileService;
import com.kintiger.mall.api.file.bo.FileInfo;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.Decode;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.FileUtil;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 文件.
 * 
 * @author xujiakun
 * 
 */
public class FileAction extends BaseAction {

	private static final long serialVersionUID = 6285367046945881121L;

	private static final int BYTE = 4 * 1024;

	private Logger4jExtend logger = Logger4jCollection.getLogger(FileAction.class);

	private IFileService fileService;

	private IDFSService dfsService;

	private int total;

	private String fileId;

	private List<FileInfo> fileInfoList;

	@Decode
	private String fileName;

	private File upload;

	private String uploadFileName;

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	/**
	 * 查看文件.
	 * 
	 * @return
	 */
	public String searchFile() {
		return "searchFile";
	}

	@JsonResult(field = "fileInfoList", include = { "fileId", "fileName", "suffix", "filePath", "flag", "createDate",
		"modifyDate" }, total = "total")
	public String getFileJsonList() {
		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		FileInfo f = new FileInfo();
		f = getSearchInfo(f);

		if (StringUtils.isNotBlank(fileName)) {
			f.setFileName(fileName.trim());
		}

		total = fileService.getFileCount(shopId, f);

		if (total > 0) {
			fileInfoList = fileService.getFileList(shopId, f);
		}

		return JSON_RESULT;
	}

	public String fetchFile() {
		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return RESULT_MESSAGE;
		}

		OutputStream out = null;

		if (StringUtils.isNotBlank(fileId)) {
			try {
				HttpServletResponse response = getServletResponse();
				response.reset();
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment; filename=\""
					+ new String(dfsService.getFileName(shopId, fileId).getBytes("GBK"), "ISO8859-1") + "\"");

				out = response.getOutputStream();

				boolean result = dfsService.fetchFile(shopId, fileId, out);

				if (result) {
					out.write(BYTE);
					out.flush();
				}
			} catch (IOException e) {
				logger.error(fileId, e);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						logger.error(e);
					}
				}
			}
		}

		this.setFailMessage("文件下载失败！");
		return RESULT_MESSAGE;
	}

	/**
	 * 删除文件.
	 * 
	 * @return
	 */
	public String deleteFile() {
		User user = getUser();

		String[] l = new String[fileInfoList.size()];
		int i = 0;

		try {
			for (FileInfo c : fileInfoList) {
				if (StringUtils.isNotBlank(c.getFileId())) {
					l[i++] = c.getFileId();
				}
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(fileInfoList), e);
			this.setFailMessage(IFileService.ERROR_INPUT_MESSAGE);
			return RESULT_MESSAGE;
		}

		// 无有效的菜单id
		if (i == 0) {
			this.setFailMessage(IFileService.ERROR_INPUT_MESSAGE);
			return RESULT_MESSAGE;
		}

		BooleanResult result = fileService.deleteFile(user.getShopId(), l, user.getUserId());

		if (result.getResult()) {
			this.setSuccessMessage("已成功删除" + result.getCode() + "个文件！");
		} else {
			this.setFailMessage(result.getCode());
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 保存文件.
	 */
	public void saveFile() {
		User user = getUser();

		PrintWriter out = null;

		HttpServletResponse response = this.getServletResponse();
		try {
			response.setContentType("text/html; charset=GBK");
			out = response.getWriter();

			BooleanResult result =
				dfsService.saveFile(user.getShopId(), FileUtil.getName(uploadFileName),
					FileUtil.getSuffix(uploadFileName), upload, user.getUserId());

			if (!result.getResult()) {
				response.setStatus(500);
			}

			out.write(result.getCode());
			out.flush();
		} catch (Exception e) {
			logger.error(e);

			response.setStatus(500);
			out.write("上传失败！");
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}
	}

	public IFileService getFileService() {
		return fileService;
	}

	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	public IDFSService getDfsService() {
		return dfsService;
	}

	public void setDfsService(IDFSService dfsService) {
		this.dfsService = dfsService;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public List<FileInfo> getFileInfoList() {
		return fileInfoList;
	}

	public void setFileInfoList(List<FileInfo> fileInfoList) {
		this.fileInfoList = fileInfoList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

}
