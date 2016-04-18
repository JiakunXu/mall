package com.kintiger.mall.api.trade;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kintiger.mall.api.trade.bo.Trade;
import com.kintiger.mall.api.trade.bo.TradeExpress;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.wxap.ResponseHandler;

/**
 * 交易接口.
 * 
 * @author xujiakun
 * 
 */
public interface ITradeService {

	String CREATE = "create";

	String CHECK = "check";

	String TO_PAY = "topay";

	String PAY = "pay";

	String TO_SEND = "tosend";

	String SEND = "send";

	String SIGN = "sign";

	String FEEDBACK = "feedback";

	String FEEDBACKED = "feedbacked";

	String CANCEL = "cancel";

	String PAY_TYPE_ALIPAY = "alipay";

	String PAY_TYPE_WXAP = "wxap";

	/**
	 * 获取店铺交易概况统计.
	 * 
	 * @param shopId
	 *            店铺.
	 * @param gmtStart
	 *            开始时间.
	 * @param gmtEnd
	 *            结束时间.
	 * @return
	 */
	List<Trade> getTradeStats(String shopId, String gmtStart, String gmtEnd);

	/**
	 * 获取店铺交易收入概况.
	 * 
	 * @param shopId
	 *            店铺.
	 * @param gmtStart
	 *            开始时间.
	 * @param gmtEnd
	 *            结束时间.
	 * @return
	 */
	int getTradeRevenue(String shopId, String gmtStart, String gmtEnd);

	/**
	 * 卖家查询某店铺交易.
	 * 
	 * @param shopId
	 *            必填.
	 * @param trade
	 * @return
	 */
	int getTradeCount(String shopId, Trade trade);

	/**
	 * 卖家查询某店铺交易.
	 * 
	 * @param shopId
	 *            必填.
	 * @param trade
	 * @return
	 */
	List<Trade> getTradeList(String shopId, Trade trade);

	/**
	 * 卖家 订单加星.
	 * 
	 * @param shopId
	 *            店铺.
	 * @param tradeId
	 *            交易订单.
	 * @param score
	 *            星.
	 * @param modifyUser
	 *            修改人.
	 * @return
	 */
	BooleanResult star(String shopId, String tradeId, String score, String modifyUser);

	/**
	 * 卖家查看订单.
	 * 
	 * @param shopId
	 * @param tradeNo
	 * @return
	 */
	Trade getTrade(String shopId, String tradeNo);

	/**
	 * 卖家发货.
	 * 
	 * @param shopId
	 * @param tradeId
	 * @param shipment
	 * @param tradeExpress
	 * @param modifyUser
	 * @return
	 */
	BooleanResult sendTrade(String shopId, String tradeId, String shipment, TradeExpress tradeExpress, String modifyUser);

	/**
	 * 卖家取消订单.
	 * 
	 * @param shopId
	 * @param tradeId
	 * @param modifyUser
	 * @return
	 */
	BooleanResult cancelTrade(String shopId, String tradeId, String modifyUser);

	/**
	 * 卖家处理维权订单.
	 * 
	 * @param shopId
	 * @param tradeId
	 * @param modifyUser
	 * @return
	 */
	BooleanResult feedbackedTrade(String shopId, String tradeId, String modifyUser);

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 买家查询某店铺交易.
	 * 
	 * @param userId
	 *            必填.
	 * @param shopId
	 *            必填.
	 * @param type
	 * @return
	 */
	int getTradeCount(String userId, String shopId, String[] type);

	/**
	 * 买家查询某店铺交易.
	 * 
	 * @param userId
	 *            必填.
	 * @param shopId
	 *            必填.
	 * @param type
	 * @return
	 */
	List<Trade> getTradeList(String userId, String shopId, String[] type);

	/**
	 * 买家下单交易.
	 * 
	 * @param userId
	 *            必填.
	 * @param shopId
	 *            必填.
	 * @param cartId
	 *            购物车id.
	 * @return
	 */
	BooleanResult createTrade(String userId, String shopId, String[] cartId);

	/**
	 * 买家查看订单.
	 * 
	 * @param userId
	 * @param shopId
	 * @param tradeNo
	 * @return
	 */
	Trade getTrade(String userId, String shopId, String tradeNo);

	/**
	 * 修改订单联系方式.
	 * 
	 * @param userId
	 * @param shopId
	 * @param tradeId
	 * @param trade
	 * @return
	 */
	BooleanResult updateReceiver(String userId, String shopId, String tradeId, Trade trade);

	/**
	 * 取消订单.
	 * 
	 * @param userId
	 * @param shopId
	 * @param tradeId
	 * @return
	 */
	BooleanResult cancelMyTrade(String userId, String shopId, String tradeId);

	/**
	 * 买家订单付款.
	 * 
	 * @param userId
	 * @param shopId
	 * @param tradeId
	 * @param payType
	 *            支付方式.
	 * @param request
	 * @param response
	 * @return
	 */
	BooleanResult payTrade(String userId, String shopId, String tradeId, String payType, HttpServletRequest request,
		HttpServletResponse response);

	/**
	 * 买家签收.
	 * 
	 * @param userId
	 * @param shopId
	 * @param tradeId
	 * @return
	 */
	BooleanResult signTrade(String userId, String shopId, String tradeId);

	/**
	 * 买家维权.
	 * 
	 * @param userId
	 * @param shopId
	 * @param tradeId
	 * @return
	 */
	BooleanResult feedbackTrade(String userId, String shopId, String tradeId);

	/**
	 * 买家查看物流信息.
	 * 
	 * @param userId
	 * @param shopId
	 * @param tradeId
	 * @return
	 */
	BooleanResult getTradeExpress(String userId, String shopId, String tradeId);

	// >>>>>>>>>>以下是第三方交易平台<<<<<<<<<<

	/**
	 * 支付回调.
	 * 
	 * @param request
	 * @return
	 */
	BooleanResult callbackTrade(HttpServletRequest request);

	/**
	 * 支付通知.
	 * 
	 * @param tradeNo
	 * @param params
	 * @return
	 */
	boolean notifyTrade(String tradeNo, Map<String, String> params);

	/**
	 * 支付通知.
	 * 
	 * @param tradeNo
	 * @param resHandler
	 * @return
	 */
	boolean notifyTrade(String tradeNo, ResponseHandler resHandler);

}
