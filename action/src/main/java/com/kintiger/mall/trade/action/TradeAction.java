package com.kintiger.mall.trade.action;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.express.IExpressService;
import com.kintiger.mall.api.express.bo.Express;
import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.api.trade.ITradeService;
import com.kintiger.mall.api.trade.bo.Trade;
import com.kintiger.mall.api.trade.bo.TradeExpress;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.annotation.Decode;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 交易管理.
 * 
 * @author xujiakun
 * 
 */
public class TradeAction extends BaseAction {

	private static final long serialVersionUID = 1733760096954128978L;

	private ITradeService tradeService;

	private IExpressService expressService;

	private int total;

	private List<Trade> tradeList;

	/**
	 * 区别 所有订单 和 加星订单.
	 */
	private String type;

	/**
	 * 查询条件 交易订单号.
	 */
	@Decode
	private String tradeNo;

	/**
	 * 查询条件 收货人.
	 */
	@Decode
	private String receiverName;

	/**
	 * 查询条件 收货人联系方式.
	 */
	@Decode
	private String receiverTel;

	/**
	 * 加星.
	 */
	private String tradeId;

	/**
	 * 加星的星.
	 */
	private String score;

	/**
	 * ajax 返回.
	 */
	private String message;

	/**
	 * 下订单.
	 */
	private Trade trade;

	/**
	 * 下订单 已选中的购物车.
	 */
	private String[] cartId;

	/**
	 * 统计值(未付款).
	 */
	private int countOfPay;

	/**
	 * 统计值(已付款).
	 */
	private int countOfPaid;

	/**
	 * 统计值(已签收).
	 */
	private int countOfSign;

	/**
	 * 统计值(已取消).
	 */
	private int countOfCancel;

	/**
	 * 店铺唯一 id.
	 */
	private String uuid;

	/**
	 * 物流公司.
	 */
	private List<Express> expressList;

	/**
	 * 是否需要物流 Y or N.
	 */
	private String shipment;

	/**
	 * 物流信息.
	 */
	private TradeExpress tradeExpress;

	/**
	 * 支付方式(alipay wxap).
	 */
	private String payType;

	// >>>>>>>>>>以下是首页统计值<<<<<<<<<<

	private int s1;

	private int s2;

	private int s3;

	private int s4;

	private int s5;

	private int s6;

	private int s7;

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	/**
	 * 交易管理首页.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "订单交易管理")
	public String index() {
		String shopId = getUser().getShopId();

		Trade t = new Trade();

		// 待付款
		t.setType(ITradeService.TO_PAY);
		s2 = tradeService.getTradeCount(shopId, t);

		// 待发货
		t.setType(ITradeService.TO_SEND);
		s3 = tradeService.getTradeCount(shopId, t);

		String gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -7), DateUtil.DEFAULT_DATE_FORMAT);
		String gmtEnd = DateUtil.getNowDateStr();

		// 7天总订单
		t.setType(null);
		t.setGmtStart(gmtStart);
		t.setGmtEnd(gmtEnd);
		s1 = tradeService.getTradeCount(shopId, t);

		// 7天收入
		s4 = tradeService.getTradeRevenue(shopId, gmtStart, gmtEnd);

		gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -1), DateUtil.DEFAULT_DATE_FORMAT);

		// 昨日下单笔数
		t.setGmtStart(gmtStart);
		t.setGmtEnd(gmtStart);
		s5 = tradeService.getTradeCount(shopId, t);

		// 昨日付款订单 pay_date = gmtStart
		t.setType(null);
		t.setGmtStart(null);
		t.setGmtEnd(null);
		t.setPayDate(gmtStart);
		s6 = tradeService.getTradeCount(shopId, t);

		// 昨日发货订单 send_date = gmtStart
		t.setPayDate(null);
		t.setSendDate(gmtStart);
		s7 = tradeService.getTradeCount(shopId, t);

		return SUCCESS;
	}

	@JsonResult(field = "tradeList", include = { "dates", "countOfCreate", "countOfPay", "countOfSend", "countOfSign",
		"countOfCancel" })
	public String getTradeStats() {
		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		String gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -30), DateUtil.DEFAULT_DATE_FORMAT);
		String gmtEnd = DateUtil.getNowDateStr();

		tradeList = tradeService.getTradeStats(shopId, gmtStart, gmtEnd);

		return JSON_RESULT;
	}

	/**
	 * 交易查询.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "订单交易查询")
	public String searchTrade() {
		// 1. 物流公司
		expressList = expressService.getExpressList();

		return "searchTrade";
	}

	@JsonResult(field = "tradeList", include = { "tradeId", "tradeNo", "price", "postage", "receiverName",
		"receiverTel", "createDate", "modifyDate", "payDate", "sendDate", "signDate", "feedbackType", "feedbackDate",
		"feedbackedDate", "type", "score", "userId" }, total = "total")
	public String getTradeJsonList() {
		String shopId = getUser().getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		Trade t = new Trade();
		t = getSearchInfo(t);

		if (StringUtils.isNotBlank(type)) {
			t.setType(type.trim());
		}

		if (StringUtils.isNotBlank(shipment)) {
			t.setShipment(shipment.trim());
		}

		if (StringUtils.isNotBlank(tradeNo)) {
			t.setTradeNo(tradeNo.trim());
		}

		if (StringUtils.isNotBlank(receiverName)) {
			t.setReceiverName(receiverName.trim());
		}

		if (StringUtils.isNotBlank(receiverTel)) {
			t.setReceiverTel(receiverTel.trim());
		}

		total = tradeService.getTradeCount(shopId, t);

		if (total > 0) {
			tradeList = tradeService.getTradeList(shopId, t);
		}

		return JSON_RESULT;
	}

	@JsonResult(field = "message")
	public String star() {
		User user = this.getUser();

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			message = "抱歉您没有创建店铺！";
			return JSON_RESULT;
		}

		BooleanResult res = tradeService.star(shopId, tradeId, score, user.getUserId());

		if (!res.getResult()) {
			this.getServletResponse().setStatus(500);
			message = res.getCode();
		}

		return JSON_RESULT;
	}

	/**
	 * 查看订单详情.
	 * 
	 * @return
	 */
	public String searchTradeDetail() {
		User user = this.getUser();

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			return LOGIN_TIMEOUT;
		}

		trade = tradeService.getTrade(shopId, tradeNo);

		return "searchTradeDetail";
	}

	/**
	 * 发货.
	 * 
	 * @return
	 */
	@JsonResult(field = "message")
	public String sendTrade() {
		User user = this.getUser();

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			message = "抱歉您没有创建店铺！";
			return JSON_RESULT;
		}

		BooleanResult res = tradeService.sendTrade(shopId, tradeId, shipment, tradeExpress, user.getUserId());

		if (!res.getResult()) {
			this.getServletResponse().setStatus(500);
		}

		message = res.getCode();

		return JSON_RESULT;
	}

	/**
	 * 取消订单.
	 * 
	 * @return
	 */
	@JsonResult(field = "message")
	public String cancelTrade() {
		User user = this.getUser();

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		BooleanResult res = tradeService.cancelTrade(shopId, tradeId, user.getUserId());

		if (!res.getResult()) {
			this.getServletResponse().setStatus(500);
			message = res.getCode();
		}

		return JSON_RESULT;
	}

	/**
	 * 卖家处理维权订单.
	 * 
	 * @return
	 */
	@JsonResult(field = "message")
	public String feedbackedTrade() {
		User user = this.getUser();

		String shopId = user.getShopId();

		if (StringUtils.isBlank(shopId)) {
			return JSON_RESULT;
		}

		BooleanResult res = tradeService.feedbackedTrade(shopId, tradeId, user.getUserId());

		if (!res.getResult()) {
			this.getServletResponse().setStatus(500);
			message = res.getCode();
		}

		return JSON_RESULT;
	}

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 买家 我的订单.
	 * 
	 * @return
	 */
	public String trade() {
		String shopId = this.getShop().getShopId();
		String userId = this.getUser().getUserId();

		// 待付款
		countOfPay = tradeService.getTradeCount(userId, shopId, new String[] { "check", "topay" });

		// 已付款
		countOfPaid = tradeService.getTradeCount(userId, shopId, new String[] { "tosend", "send" });

		// 已签收
		countOfSign = tradeService.getTradeCount(userId, shopId, new String[] { "sign" });

		// 已取消
		countOfCancel = tradeService.getTradeCount(userId, shopId, new String[] { "cancel" });

		return SUCCESS;
	}

	/**
	 * 买家查询订单.
	 * 
	 * @return
	 */
	public String searchITrade() {
		String shopId = this.getShop().getShopId();
		String userId = this.getUser().getUserId();

		if ("pay".equals(type)) {
			tradeList = tradeService.getTradeList(userId, shopId, new String[] { "check", "topay" });
		} else if ("paid".equals(type)) {
			tradeList = tradeService.getTradeList(userId, shopId, new String[] { "tosend", "send" });
		} else if ("sign".equals(type)) {
			tradeList = tradeService.getTradeList(userId, shopId, new String[] { "sign" });
		} else if ("feedback".equals(type)) {
			tradeList = tradeService.getTradeList(userId, shopId, new String[] { "feedback" });
		} else if ("cancel".equals(type)) {
			tradeList = tradeService.getTradeList(userId, shopId, new String[] { "cancel" });
		}

		return SUCCESS;
	}

	/**
	 * 下订单.
	 * 
	 * @return
	 */
	public String createTrade() {
		Shop shop = this.getShop();
		if (shop == null) {
			return NONE;
		}

		uuid = shop.getUuid();

		BooleanResult result = tradeService.createTrade(getUser().getUserId(), shop.getShopId(), cartId);
		if (result.getResult()) {
			tradeNo = result.getCode();
			// 跳转 confirm 页面 维护收货信息
			return SUCCESS;
		} else {
			// 跳转 cart 页面
			return ERROR;
		}
	}

	/**
	 * 跳转买家订单确认页面.
	 * 
	 * @return
	 */
	public String confirm() {

		trade = tradeService.getTrade(getUser().getUserId(), getShop().getShopId(), tradeNo);

		return SUCCESS;
	}

	/**
	 * 跳转买家订单明细页面.
	 * 
	 * @return
	 */
	public String detail() {

		trade = tradeService.getTrade(getUser().getUserId(), getShop().getShopId(), tradeNo);

		return SUCCESS;
	}

	@JsonResult(field = "message")
	public String cancelMyTrade() {
		HttpServletResponse response = this.getServletResponse();
		// 初始化 返回失败
		response.setStatus(500);

		Shop shop = this.getShop();
		if (shop == null) {
			message = "抱歉您没有选择店铺！";
			return JSON_RESULT;
		}

		BooleanResult res = tradeService.cancelMyTrade(getUser().getUserId(), shop.getShopId(), tradeId);
		if (res.getResult()) {
			response.setStatus(200);
		}

		message = res.getCode();

		return JSON_RESULT;
	}

	@JsonResult(field = "message")
	public String payTrade() {
		HttpServletResponse response = this.getServletResponse();
		// 初始化 返回失败
		response.setStatus(500);

		Shop shop = this.getShop();
		if (shop == null) {
			message = "抱歉您没有选择店铺！";
			return JSON_RESULT;
		}

		BooleanResult res =
			tradeService.payTrade(getUser().getUserId(), shop.getShopId(), tradeId, payType, this.getServletRequest(),
				this.getServletResponse());
		if (res.getResult()) {
			response.setStatus(200);
		}

		message = res.getCode();

		return JSON_RESULT;
	}

	/**
	 * 买家签收.
	 * 
	 * @return
	 */
	@JsonResult(field = "message")
	public String signTrade() {
		HttpServletResponse response = this.getServletResponse();
		// 初始化 返回失败
		response.setStatus(500);

		Shop shop = this.getShop();
		if (shop == null) {
			message = "抱歉您没有选择店铺！";
			return JSON_RESULT;
		}

		BooleanResult res = tradeService.signTrade(getUser().getUserId(), shop.getShopId(), tradeId);
		if (res.getResult()) {
			response.setStatus(200);
		}

		message = res.getCode();

		return JSON_RESULT;
	}

	/**
	 * 买家维权.
	 * 
	 * @return
	 */
	@JsonResult(field = "message")
	public String feedbackTrade() {
		HttpServletResponse response = this.getServletResponse();
		// 初始化 返回失败
		response.setStatus(500);

		Shop shop = this.getShop();
		if (shop == null) {
			message = "抱歉您没有选择店铺！";
			return JSON_RESULT;
		}

		BooleanResult res = tradeService.feedbackTrade(getUser().getUserId(), shop.getShopId(), tradeId);
		if (res.getResult()) {
			response.setStatus(200);
		}

		message = res.getCode();

		return JSON_RESULT;
	}

	/**
	 * 买家查看物流信息.
	 * 
	 * @return
	 */
	@JsonResult(field = "message")
	public String searchTradeExpress() {
		HttpServletResponse response = this.getServletResponse();
		// 初始化 返回失败
		response.setStatus(500);

		Shop shop = this.getShop();
		if (shop == null) {
			message = "抱歉您没有选择店铺！";
			return JSON_RESULT;
		}

		BooleanResult res = tradeService.getTradeExpress(getUser().getUserId(), shop.getShopId(), tradeId);
		if (res.getResult()) {
			response.setStatus(200);
		}

		message = res.getCode();

		return JSON_RESULT;
	}

	// >>>>>>>>>>以下是第三方交易平台<<<<<<<<<<

	/**
	 * 支付成功后回调处理交易.
	 * 
	 * @return
	 */
	public String callbackTrade() {
		BooleanResult res = tradeService.callbackTrade(this.getServletRequest());

		if (res.getResult()) {
			message = "支付成功！";
		} else {
			message = res.getCode();
		}

		return SUCCESS;
	}

	/**
	 * 微信维权通知.
	 * 
	 * @return
	 */
	@JsonResult(field = "message")
	public String rightsTrade() {
		message = "success";

		return JSON_RESULT;
	}

	/**
	 * 微信告警通知.
	 * 
	 * @return
	 */
	@JsonResult(field = "message")
	public String notifyTrade() {
		message = "success";

		return JSON_RESULT;
	}

	public ITradeService getTradeService() {
		return tradeService;
	}

	public void setTradeService(ITradeService tradeService) {
		this.tradeService = tradeService;
	}

	public IExpressService getExpressService() {
		return expressService;
	}

	public void setExpressService(IExpressService expressService) {
		this.expressService = expressService;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<Trade> getTradeList() {
		return tradeList;
	}

	public void setTradeList(List<Trade> tradeList) {
		this.tradeList = tradeList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverTel() {
		return receiverTel;
	}

	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Trade getTrade() {
		return trade;
	}

	public void setTrade(Trade trade) {
		this.trade = trade;
	}

	public String[] getCartId() {
		return cartId != null ? Arrays.copyOf(cartId, cartId.length) : null;
	}

	public void setCartId(String[] cartId) {
		if (cartId != null) {
			this.cartId = Arrays.copyOf(cartId, cartId.length);
		}
	}

	public int getCountOfPay() {
		return countOfPay;
	}

	public void setCountOfPay(int countOfPay) {
		this.countOfPay = countOfPay;
	}

	public int getCountOfPaid() {
		return countOfPaid;
	}

	public void setCountOfPaid(int countOfPaid) {
		this.countOfPaid = countOfPaid;
	}

	public int getCountOfSign() {
		return countOfSign;
	}

	public void setCountOfSign(int countOfSign) {
		this.countOfSign = countOfSign;
	}

	public int getCountOfCancel() {
		return countOfCancel;
	}

	public void setCountOfCancel(int countOfCancel) {
		this.countOfCancel = countOfCancel;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<Express> getExpressList() {
		return expressList;
	}

	public void setExpressList(List<Express> expressList) {
		this.expressList = expressList;
	}

	public String getShipment() {
		return shipment;
	}

	public void setShipment(String shipment) {
		this.shipment = shipment;
	}

	public TradeExpress getTradeExpress() {
		return tradeExpress;
	}

	public void setTradeExpress(TradeExpress tradeExpress) {
		this.tradeExpress = tradeExpress;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public int getS1() {
		return s1;
	}

	public void setS1(int s1) {
		this.s1 = s1;
	}

	public int getS2() {
		return s2;
	}

	public void setS2(int s2) {
		this.s2 = s2;
	}

	public int getS3() {
		return s3;
	}

	public void setS3(int s3) {
		this.s3 = s3;
	}

	public int getS4() {
		return s4;
	}

	public void setS4(int s4) {
		this.s4 = s4;
	}

	public int getS5() {
		return s5;
	}

	public void setS5(int s5) {
		this.s5 = s5;
	}

	public int getS6() {
		return s6;
	}

	public void setS6(int s6) {
		this.s6 = s6;
	}

	public int getS7() {
		return s7;
	}

	public void setS7(int s7) {
		this.s7 = s7;
	}

}
