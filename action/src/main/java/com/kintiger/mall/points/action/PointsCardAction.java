package com.kintiger.mall.points.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.points.IPointsCardService;
import com.kintiger.mall.api.points.bo.PointsCard;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.Decode;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.util.ExcelUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 
 * @author xujiakun
 * 
 */
public class PointsCardAction extends BaseAction {

	private static final long serialVersionUID = -2359161958020689181L;

	private Logger4jExtend logger = Logger4jCollection.getLogger(PointsCardAction.class);

	private IPointsCardService pointsCardService;

	private int total;

	@Decode
	private String cardNo;

	@Decode
	private String userName;

	@Decode
	private String isUsed;

	private List<PointsCard> pointsCardList;

	/**
	 * 积分卡创建.
	 */
	private PointsCard pointsCard;

	/**
	 * 积分卡创建.
	 */
	private String quantity;

	/**
	 * 首页统计 未兑换积分卡.
	 */
	private int countOfNotUsed;

	/**
	 * 首页统计 已兑换积分卡.
	 */
	private int countOfUsed;

	/**
	 * 首页统计 已过期积分卡.
	 */
	private int countOfExpire;

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	/**
	 * 积分卡.
	 * 
	 * @return
	 */
	public String index() {
		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return LOGIN_TIMEOUT;
		}

		gmtStart = DateUtil.getNowDateStr();

		PointsCard pc = new PointsCard();

		pc.setIsUsed("N");
		countOfNotUsed = pointsCardService.getPointsCardCount(shopId, pc);

		pc.setIsUsed("Y");
		countOfUsed = pointsCardService.getPointsCardCount(shopId, pc);

		return SUCCESS;
	}

	@JsonResult(field = "pointsCardList", include = { "id", "cardNo", "password", "points", "expireDate", "userName",
		"isUsed", "expire" }, total = "total")
	public String getPointsCardJsonList() {
		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		PointsCard pc = new PointsCard();
		pc = getSearchInfo(pc);

		if (StringUtils.isNotBlank(cardNo)) {
			pc.setCardNo(cardNo.trim());
		}

		if (StringUtils.isNotBlank(userName)) {
			pc.setUserName(userName.trim());
		}

		if (StringUtils.isNotBlank(isUsed)) {
			pc.setIsUsed(isUsed.trim());
		}

		total = pointsCardService.getPointsCardCount(shopId, pc);

		if (total > 0) {
			pointsCardList = pointsCardService.getPointsCardList(shopId, pc);
		}

		return JSON_RESULT;
	}

	/**
	 * 创建积分卡.
	 * 
	 * @return
	 */
	public String createPointsCard() {
		User user = this.getUser();

		BooleanResult result =
			pointsCardService.createPointsCard(user.getShopId(), pointsCard, quantity, user.getUserId());

		if (result.getResult()) {
			this.setSuccessMessage("积分卡创建成功！");
		} else {
			this.setFailMessage(result.getCode());
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 积分卡导出.
	 * 
	 * @return
	 */
	public String exportPointsCardData() {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return RESULT_MESSAGE;
		}

		PointsCard pc = new PointsCard();

		if (StringUtils.isNotBlank(cardNo)) {
			pc.setCardNo(cardNo.trim());
		}

		if (StringUtils.isNotBlank(userName)) {
			pc.setUserName(userName.trim());
		}

		if (StringUtils.isNotBlank(isUsed)) {
			pc.setIsUsed(isUsed.trim());
		}

		total = pointsCardService.getPointsCardCount(shopId, pc);

		if (total > 0) {
			pc.setStart(0);
			pc.setLimit(total);
			pointsCardList = pointsCardService.getPointsCardList(shopId, pc);
		}

		List<String> props = new ArrayList<String>();
		props.add("id");
		props.add("cardNo");
		props.add("password");
		props.add("points4Excel");
		props.add("expireDate");
		props.add("userName");

		try {
			ExcelUtil eu = new ExcelUtil();

			inputStream =
				Thread.currentThread().getContextClassLoader().getResource("resource/exportPointsCardData.xls")
					.openStream();

			HttpServletResponse response = this.getServletResponse();
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment; filename=\""
				+ new String("积分卡导出".getBytes("GBK"), ("ISO8859-1")) + ".xls\"");
			outputStream = response.getOutputStream();

			eu.createExcelWithTemplate(inputStream, outputStream, props, pointsCardList);

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

	public IPointsCardService getPointsCardService() {
		return pointsCardService;
	}

	public void setPointsCardService(IPointsCardService pointsCardService) {
		this.pointsCardService = pointsCardService;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	public List<PointsCard> getPointsCardList() {
		return pointsCardList;
	}

	public void setPointsCardList(List<PointsCard> pointsCardList) {
		this.pointsCardList = pointsCardList;
	}

	public PointsCard getPointsCard() {
		return pointsCard;
	}

	public void setPointsCard(PointsCard pointsCard) {
		this.pointsCard = pointsCard;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public int getCountOfNotUsed() {
		return countOfNotUsed;
	}

	public void setCountOfNotUsed(int countOfNotUsed) {
		this.countOfNotUsed = countOfNotUsed;
	}

	public int getCountOfUsed() {
		return countOfUsed;
	}

	public void setCountOfUsed(int countOfUsed) {
		this.countOfUsed = countOfUsed;
	}

	public int getCountOfExpire() {
		return countOfExpire;
	}

	public void setCountOfExpire(int countOfExpire) {
		this.countOfExpire = countOfExpire;
	}

}
