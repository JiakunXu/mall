package com.kintiger.mall.settlement.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.settlement.ISettlementService;
import com.kintiger.mall.api.settlement.bo.Settlement;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 结算.
 * 
 * @author xujiakun
 * 
 */
public class SettlementAction extends BaseAction {

	private static final long serialVersionUID = 3972947160741202222L;

	private ISettlementService settlementService;

	private int total;

	private List<Settlement> settlementList;

	/**
	 * 支付方式.
	 */
	private String payType;

	// >>>>>>>>>>以下是结算结果<<<<<<<<<<

	/**
	 * 结算合计金额.
	 */
	private BigDecimal totalFee;

	/**
	 * 支付宝合计金额.
	 */
	private BigDecimal totalFeeAli;

	/**
	 * 微信合计金额.
	 */
	private BigDecimal totalFeeWx;

	/**
	 * 支付宝成交订单数.
	 */
	private int totalAli;

	/**
	 * 微信成交订单数.
	 */
	private int totalWx;

	/**
	 * 结算中心.
	 * 
	 * @return
	 */
	public String index() {
		gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -7), DateUtil.DEFAULT_DATE_FORMAT);
		gmtEnd = DateUtil.getNowDateStr();

		// 七天已付款
		String shopId = getUser().getShopId();

		Settlement s = new Settlement();
		s.setGmtStart(gmtStart);
		s.setGmtEnd(gmtEnd);

		totalFeeAli = settlementService.getSettlement(shopId, ISettlementService.TYPE_ALIPAY, s);

		totalFeeWx = settlementService.getSettlement(shopId, ISettlementService.TYPE_WX, s);

		totalFee = totalFeeAli.add(totalFeeWx);

		totalAli = settlementService.getSettlementCount(shopId, ISettlementService.TYPE_ALIPAY, s);

		totalWx = settlementService.getSettlementCount(shopId, ISettlementService.TYPE_WX, s);

		return SUCCESS;
	}

	@JsonResult(field = "settlementList", include = { "createDate", "totalFee", "outTradeNo", "tradeNo" })
	public String getSettlementJsonList() {
		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		Settlement s = new Settlement();
		s = getSearchInfo(s);

		total = settlementService.getSettlementCount(shopId, payType, s);

		if (total > 0) {
			settlementList = settlementService.getSettlementList(shopId, payType, s);
		}

		return JSON_RESULT;
	}

	public ISettlementService getSettlementService() {
		return settlementService;
	}

	public void setSettlementService(ISettlementService settlementService) {
		this.settlementService = settlementService;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<Settlement> getSettlementList() {
		return settlementList;
	}

	public void setSettlementList(List<Settlement> settlementList) {
		this.settlementList = settlementList;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public BigDecimal getTotalFeeAli() {
		return totalFeeAli;
	}

	public void setTotalFeeAli(BigDecimal totalFeeAli) {
		this.totalFeeAli = totalFeeAli;
	}

	public BigDecimal getTotalFeeWx() {
		return totalFeeWx;
	}

	public void setTotalFeeWx(BigDecimal totalFeeWx) {
		this.totalFeeWx = totalFeeWx;
	}

	public int getTotalAli() {
		return totalAli;
	}

	public void setTotalAli(int totalAli) {
		this.totalAli = totalAli;
	}

	public int getTotalWx() {
		return totalWx;
	}

	public void setTotalWx(int totalWx) {
		this.totalWx = totalWx;
	}

}
