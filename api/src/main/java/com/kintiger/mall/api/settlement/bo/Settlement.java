package com.kintiger.mall.api.settlement.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 
 * @author xujiakun
 * 
 */
public class Settlement extends SearchInfo {

	private static final long serialVersionUID = 665530130837430122L;

	private String shopId;

	/**
	 * 交易创建时间.
	 */
	private String createDate;

	/**
	 * 交易金额.
	 */
	private String totalFee;

	/**
	 * 商户网站唯一订单号.
	 */
	private String outTradeNo;

	/**
	 * 支付交易号.
	 */
	private String tradeNo;

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

}
