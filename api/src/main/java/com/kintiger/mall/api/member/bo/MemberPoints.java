package com.kintiger.mall.api.member.bo;

import java.math.BigDecimal;
import java.util.Comparator;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 会员积分.
 * 
 * @author
 * 
 */
public class MemberPoints extends SearchInfo implements Comparator<MemberPoints> {

	private static final long serialVersionUID = -2434561623900795329L;

	/**
	 * ID.
	 */
	private String id;

	/**
	 * 会员ID.
	 */
	private String memId;

	/**
	 * 积分.
	 */
	private BigDecimal points;

	/**
	 * 积分来源.
	 */
	private String pointsSource;

	/**
	 * 交易ID.
	 */
	private String tradeId;

	/**
	 * 状态 D:删除 U:正常.
	 */
	private String state;

	/**
	 * 操作人ID.
	 */
	private String modifyUser;

	/**
	 * 创建时间.
	 */
	private String createDate;

	// >>>>>>>>>>以下是辅助属性<<<<<<<<<<

	/**
	 * 店铺id.
	 */
	private String shopId;

	/**
	 * 用戶id.
	 */
	private String userId;

	/**
	 * 交易订单类型(check: 临时订单; topay: 待付款; tosend: 待发货; send: 已发货; sign: 标记签收; cancel: 已关闭).
	 */
	private String type;

	// >>>>>>>>>>以下是根据 points 排序<<<<<<<<<<

	@Override
	public int compare(MemberPoints o1, MemberPoints o2) {
		return o1.getPoints().compareTo(o2.getPoints());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}

	public BigDecimal getPoints() {
		return points;
	}

	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	public String getPointsSource() {
		return pointsSource;
	}

	public void setPointsSource(String pointsSource) {
		this.pointsSource = pointsSource;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
