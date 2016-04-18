package com.kintiger.mall.member.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.member.IMemberPointsService;
import com.kintiger.mall.api.member.bo.MemberPoints;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 会员积分.
 * 
 * @author xujiakun
 * 
 */
public class MemberPointsAction extends BaseAction {

	private static final long serialVersionUID = 5022713980376229517L;

	private IMemberPointsService memberPointsService;

	private List<MemberPoints> memberPointsList;

	/**
	 * 买家用户id.
	 */
	private String userId;

	/**
	 * 交易id.
	 */
	private String tradeId;

	/**
	 * ajax 返回.
	 */
	private String message;

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	@JsonResult(field = "message")
	public String validateReturnPoints() {
		User user = this.getUser();

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			message = "抱歉您没有创建店铺！";
			return JSON_RESULT;
		}

		BooleanResult res = memberPointsService.validateReturnPoints(userId, shopId, tradeId);

		if (!res.getResult()) {
			this.getServletResponse().setStatus(500);
		}

		message = res.getCode();

		return JSON_RESULT;
	}

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 查询积分信息
	 * 
	 * @return
	 */
	public String searchMemberPoints() {

		MemberPoints memberPoints = new MemberPoints();
		memberPoints.setGmtStart(DateUtil.getDateTime(DateUtil.addDays(new Date(), -30), DateUtil.DEFAULT_DATE_FORMAT));
		memberPoints.setGmtEnd(DateUtil.getNowDateStr());

		memberPointsList =
			memberPointsService.getMemberPointsList(getUser().getUserId(), getShop().getShopId(), memberPoints);

		return SUCCESS;
	}

	public IMemberPointsService getMemberPointsService() {
		return memberPointsService;
	}

	public void setMemberPointsService(IMemberPointsService memberPointsService) {
		this.memberPointsService = memberPointsService;
	}

	public List<MemberPoints> getMemberPointsList() {
		return memberPointsList;
	}

	public void setMemberPointsList(List<MemberPoints> memberPointsList) {
		this.memberPointsList = memberPointsList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
