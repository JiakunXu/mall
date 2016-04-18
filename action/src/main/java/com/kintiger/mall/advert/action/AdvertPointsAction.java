package com.kintiger.mall.advert.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.advert.IAdvertPointsService;
import com.kintiger.mall.api.advert.bo.AdvertPoints;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.util.ExcelUtil;

/**
 * 广告积分.
 * 
 * @author xujiakun
 * 
 */
public class AdvertPointsAction extends BaseAction {

	private static final long serialVersionUID = 5063654519578800165L;

	private Logger4jExtend logger = Logger4jCollection.getLogger(AdvertPointsAction.class);

	private IAdvertPointsService advertPointsService;

	private List<AdvertPoints> advertPointsList;

	public String exportAdvertPointsData() {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return RESULT_MESSAGE;
		}

		String gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -100), DateUtil.DEFAULT_DATE_FORMAT);
		String gmtEnd = DateUtil.getNowDateStr();

		advertPointsList = advertPointsService.getAdvertPointsList(shopId, gmtStart, gmtEnd);

		List<String> props = new ArrayList<String>();
		props.add("id");
		props.add("advertName");
		props.add("pointsDate");
		props.add("userName");
		props.add("mobile");
		props.add("points");

		try {
			ExcelUtil eu = new ExcelUtil();

			inputStream =
				Thread.currentThread().getContextClassLoader().getResource("resource/exportAdvertPointsData.xls")
					.openStream();

			HttpServletResponse response = this.getServletResponse();
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
				"attachment; filename=\"" + new String("点击广告赠送积分导出".getBytes("GBK"), ("ISO8859-1")) + "(" + gmtStart
					+ "~" + gmtEnd + ").xls\"");
			outputStream = response.getOutputStream();

			eu.createExcelWithTemplate(inputStream, outputStream, props, advertPointsList);

			outputStream.flush();
		} catch (IOException e) {
			logger.error(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}

		return RESULT_MESSAGE;
	}

	public IAdvertPointsService getAdvertPointsService() {
		return advertPointsService;
	}

	public void setAdvertPointsService(IAdvertPointsService advertPointsService) {
		this.advertPointsService = advertPointsService;
	}

	public List<AdvertPoints> getAdvertPointsList() {
		return advertPointsList;
	}

	public void setAdvertPointsList(List<AdvertPoints> advertPointsList) {
		this.advertPointsList = advertPointsList;
	}

}
