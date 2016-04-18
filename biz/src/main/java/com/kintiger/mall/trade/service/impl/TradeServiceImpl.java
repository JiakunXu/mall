package com.kintiger.mall.trade.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.cart.ICartService;
import com.kintiger.mall.api.cart.bo.Cart;
import com.kintiger.mall.api.item.IItemFileService;
import com.kintiger.mall.api.item.IItemService;
import com.kintiger.mall.api.item.IItemSkuService;
import com.kintiger.mall.api.item.bo.Item;
import com.kintiger.mall.api.item.bo.ItemFile;
import com.kintiger.mall.api.item.bo.ItemSku;
import com.kintiger.mall.api.member.IMemberPointsService;
import com.kintiger.mall.api.member.IMemberService;
import com.kintiger.mall.api.member.bo.Member;
import com.kintiger.mall.api.member.bo.MemberPoints;
import com.kintiger.mall.api.pay.IAlipayService;
import com.kintiger.mall.api.pay.IWxapService;
import com.kintiger.mall.api.pay.bo.WxNotify;
import com.kintiger.mall.api.trade.IOrderService;
import com.kintiger.mall.api.trade.ITradeExpressService;
import com.kintiger.mall.api.trade.ITradeService;
import com.kintiger.mall.api.trade.bo.Order;
import com.kintiger.mall.api.trade.bo.Trade;
import com.kintiger.mall.api.trade.bo.TradeExpress;
import com.kintiger.mall.api.user.IUserAddressService;
import com.kintiger.mall.api.user.bo.UserAddress;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.framework.util.NoUtil;
import com.kintiger.mall.trade.dao.ITradeDao;
import com.wxap.ResponseHandler;

/**
 * 交易实现.
 * 
 * @author xujiakun
 * 
 */
public class TradeServiceImpl implements ITradeService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(TradeServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IMemcachedCacheService memcachedCacheService;

	private ICartService cartService;

	private IOrderService orderService;

	private IItemFileService itemFileService;

	private IUserAddressService userAddressService;

	private IItemSkuService itemSkuService;

	private IItemService itemService;

	private IMemberService memberService;

	private IMemberPointsService memberPointsService;

	private IAlipayService alipayService;

	private IWxapService wxapService;

	private ITradeExpressService tradeExpressService;

	private ITradeDao tradeDao;

	@Override
	public List<Trade> getTradeStats(String shopId, String gmtStart, String gmtEnd) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(gmtStart) || StringUtils.isBlank(gmtEnd)) {
			return null;
		}

		Trade trade = new Trade();
		trade.setShopId(shopId.trim());
		trade.setGmtStart(gmtStart.trim());
		trade.setGmtEnd(gmtEnd.trim());

		List<Trade> list0 = new ArrayList<Trade>();

		Date start = DateUtil.getDateTime(gmtStart);
		Date end = DateUtil.getDateTime(gmtEnd);

		// 判断 开始日期 是否小于 结束日期
		if (DateUtil.getQuot(start, end) < 0) {
			return null;
		}

		do {
			Trade t = new Trade();
			t.setDates(DateUtil.datetime(start, DateUtil.DEFAULT_DATE_FORMAT));
			list0.add(t);

			// 加 1 天
			start = DateUtil.addDays(start, 1);
		} while (DateUtil.getQuot(start, end) >= 0);

		Map<String, Integer> map1 = new HashMap<String, Integer>();
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		Map<String, Integer> map3 = new HashMap<String, Integer>();
		Map<String, Integer> map4 = new HashMap<String, Integer>();
		Map<String, Integer> map5 = new HashMap<String, Integer>();

		List<Trade> list1 = getTradeStats(ITradeService.CREATE, trade);
		if (list1 != null && list1.size() > 0) {
			for (Trade tt : list1) {
				map1.put(tt.getDates(), tt.getCount());
			}
		}

		List<Trade> list2 = getTradeStats(ITradeService.PAY, trade);
		if (list2 != null && list2.size() > 0) {
			for (Trade tt : list2) {
				map2.put(tt.getDates(), tt.getCount());
			}
		}

		List<Trade> list3 = getTradeStats(ITradeService.SEND, trade);
		if (list3 != null && list3.size() > 0) {
			for (Trade tt : list3) {
				map3.put(tt.getDates(), tt.getCount());
			}
		}

		List<Trade> list4 = getTradeStats(ITradeService.SIGN, trade);
		if (list4 != null && list4.size() > 0) {
			for (Trade tt : list4) {
				map4.put(tt.getDates(), tt.getCount());
			}
		}

		List<Trade> list5 = getTradeStats(ITradeService.CANCEL, trade);
		if (list5 != null && list5.size() > 0) {
			for (Trade tt : list5) {
				map5.put(tt.getDates(), tt.getCount());
			}
		}

		for (Trade tt : list0) {
			String dates = tt.getDates();

			Integer count1 = map1.get(dates);
			tt.setCountOfCreate(count1 == null ? 0 : count1);

			Integer count2 = map2.get(dates);
			tt.setCountOfPay(count2 == null ? 0 : count2);

			Integer count3 = map3.get(dates);
			tt.setCountOfSend(count3 == null ? 0 : count3);

			Integer count4 = map4.get(dates);
			tt.setCountOfSign(count4 == null ? 0 : count4);

			Integer count5 = map5.get(dates);
			tt.setCountOfCancel(count5 == null ? 0 : count5);
		}

		return list0;
	}

	private List<Trade> getTradeStats(String type, Trade trade) {
		try {
			trade.setType(type);
			return tradeDao.getTradeStats(trade);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(trade), e);
		}

		return null;
	}

	@Override
	public int getTradeRevenue(String shopId, String gmtStart, String gmtEnd) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(gmtStart) || StringUtils.isBlank(gmtEnd)) {
			return 0;
		}

		Trade trade = new Trade();
		trade.setShopId(shopId.trim());
		trade.setGmtStart(gmtStart.trim());
		trade.setGmtEnd(gmtEnd.trim());

		try {
			return tradeDao.getTradeRevenue(trade);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(trade), e);
		}

		return 0;
	}

	@Override
	public int getTradeCount(String shopId, Trade trade) {
		if (StringUtils.isBlank(shopId) || trade == null) {
			return 0;
		}

		trade.setShopId(shopId.trim());

		return getTradeCount(trade);
	}

	@Override
	public List<Trade> getTradeList(String shopId, Trade trade) {
		if (StringUtils.isBlank(shopId) || trade == null) {
			return null;
		}

		trade.setShopId(shopId.trim());

		return getTradeList(trade);
	}

	@Override
	public BooleanResult star(String shopId, String tradeId, String score, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		Trade trade = new Trade();

		if (StringUtils.isBlank(shopId)) {
			result.setCode("请先创建/选择店铺！");
			return result;
		}

		trade.setShopId(shopId.trim());

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("请先选择交易订单！");
			return result;
		}

		trade.setTradeId(tradeId.trim());

		if (score == null) {
			result.setCode("请先选择加星级别！");
			return result;
		}

		try {
			trade.setScore(Integer.parseInt(score));
		} catch (Exception e) {
			logger.error("score:" + score, e);

			result.setCode("请重新选择加星级别！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("修改人信息不能为空！");
			return result;
		}

		trade.setModifyUser(modifyUser.trim());

		return updateTrade(trade);
	}

	@Override
	public Trade getTrade(String shopId, String tradeNo) {
		// not null
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(tradeNo)) {
			return null;
		}

		Trade trade = new Trade();
		trade.setShopId(shopId.trim());
		trade.setTradeNo(tradeNo.trim());

		trade = getTrade(trade);

		// 交易不存在
		if (trade == null) {
			return null;
		}

		// 判断是否需要物流
		if ("Y".equals(trade.getShipment())) {
			trade.setTradeExpressList(tradeExpressService.getTradeExpressList(trade.getTradeId()));
		}

		return getTradeDetail(trade);
	}

	@Override
	public BooleanResult sendTrade(String shopId, final String tradeId, final String shipment,
		final TradeExpress tradeExpress, final String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		final Trade trade = new Trade();

		if (StringUtils.isBlank(shopId)) {
			result.setCode("请先创建/选择店铺！");
			return result;
		}

		trade.setShopId(shopId.trim());

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("请先选择交易订单！");
			return result;
		}

		trade.setTradeId(tradeId.trim());

		if (StringUtils.isBlank(shipment)) {
			result.setCode("请先选择发货方式！");
			return result;
		}

		if (!"Y".equals(shipment.trim()) && !"N".equals(shipment.trim())) {
			result.setCode("请先选择发货方式！");
			return result;
		}

		trade.setShipment(shipment.trim());

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("修改人信息不能为空！");
			return result;
		}

		trade.setModifyUser(modifyUser.trim());

		trade.setType(ITradeService.SEND);

		// if 存在物流
		if ("Y".equals(shipment.trim())) {
			if (tradeExpress == null) {
				result.setCode("物流信息不能为空！");
				return result;
			}
		}

		// 判断订单是否已发货
		Trade t = getTrade(trade);
		if (t == null) {
			result.setCode("当前订单不存在！");
			return result;
		}

		if (!ITradeService.TO_SEND.equals(t.getType())) {
			result.setCode("当前订单已发货！");
			return result;
		}

		// if payType == wxap then
		WxNotify n = null;
		boolean s = false;
		if (ITradeService.PAY_TYPE_WXAP.equals(t.getPayType())) {
			// 获取微信支付信息
			n = wxapService.getNotify(t.getTradeNo());
			if (n == null) {
				result.setCode("当前订单支付信息不存在，请稍后再试！");
				return result;
			}

			s = true;
		}

		// 发货通知
		final WxNotify notify = n;
		// 是否微信支付
		final boolean status = s;

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = updateTrade(trade);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 存在物流信息
				if ("Y".equals(shipment.trim())) {
					result =
						tradeExpressService.createTradeExpress(tradeId, tradeExpress.getEpsId(),
							tradeExpress.getEpsNo(), modifyUser);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				// if 微信支付 then 调用微信发货接口
				if (status) {
					result =
						wxapService.deliverNotify(notify.getAppId(), notify.getOpenId(), notify.getTransactionId(),
							notify.getOutTradeNo(), "1", "ok");
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				return result;
			}
		});

		return res;
	}

	public BooleanResult cancelTrade(String shopId, String tradeId, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		final Trade trade = new Trade();

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		trade.setShopId(shopId.trim());

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("订单信息不能为空！");
			return result;
		}

		trade.setTradeId(tradeId.trim());

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("修改人信息不能为空！");
			return result;
		}

		trade.setModifyUser(modifyUser.trim());

		// 锁定当前订单
		try {
			memcachedCacheService.add(IMemcachedCacheService.CACHE_KEY_TRADE_ID + tradeId.trim(), tradeId,
				IMemcachedCacheService.CACHE_KEY_TRADE_ID_DEFAULT_EXP);
		} catch (ServiceException e) {
			result.setCode("当前订单已被锁定，请稍后再试！");
			return result;
		}

		// 0. 查询 未付款交易订单
		Trade t = getTrade(trade);
		if (t == null) {
			result.setCode("当前订单不存在！");
			return result;
		}

		if (!ITradeService.CHECK.equals(t.getType()) && !ITradeService.TO_PAY.equals(t.getType())) {
			result.setCode("当前订单已付款或取消！");
			return result;
		}

		return cancelTrade(trade, t.getType(), t.getUserId());
	}

	@Override
	public BooleanResult feedbackedTrade(String shopId, String tradeId, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		final Trade trade = new Trade();

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		trade.setShopId(shopId.trim());

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("订单信息不能为空！");
			return result;
		}

		trade.setTradeId(tradeId.trim());

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("修改人信息不能为空！");
			return result;
		}

		trade.setModifyUser(modifyUser.trim());

		trade.setType(ITradeService.FEEDBACKED);

		// 锁定当前订单
		try {
			memcachedCacheService.add(IMemcachedCacheService.CACHE_KEY_TRADE_ID + tradeId.trim(), tradeId,
				IMemcachedCacheService.CACHE_KEY_TRADE_ID_DEFAULT_EXP);
		} catch (ServiceException e) {
			result.setCode("当前订单已被锁定，请稍后再试！");
			return result;
		}

		// 0. 查询 维权订单
		Trade t = getTrade(trade);
		if (t == null) {
			result.setCode("当前订单不存在！");
			return result;
		}

		if (!ITradeService.FEEDBACK.equals(t.getType())) {
			result.setCode("当前订单已处理或无需处理维权！");
			return result;
		}

		// 买家id
		final String userId = t.getUserId();

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// 1. 更新订单状态 -> 维权已处理
				BooleanResult result = updateTrade(trade);

				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 2. 查询交易相关积分
				List<MemberPoints> memberPointsList =
					memberPointsService.getMemberPointsList(userId, trade.getShopId(), trade.getTradeId());

				// 不存在积分 -> 直接返回 true
				if (memberPointsList == null || memberPointsList.size() == 0) {
					return result;
				}

				Collections.sort(memberPointsList, new MemberPoints());

				// 3. 积分返还
				for (MemberPoints memberPoints : memberPointsList) {
					result =
						memberPointsService.returnMemberPoints(userId, trade.getShopId(), memberPoints.getPoints()
							.negate(), memberPoints.getPointsSource() + "(交易退款返还积分)", trade.getTradeId());
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				return result;
			}
		});

		return res;
	}

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	@Override
	public int getTradeCount(String userId, String shopId, String[] type) {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(shopId) || type == null || type.length == 0) {
			return 0;
		}

		Trade trade = new Trade();
		trade.setUserId(userId.trim());
		trade.setShopId(shopId.trim());
		trade.setCodes(type);

		return getTradeCount(trade);
	}

	@Override
	public List<Trade> getTradeList(String userId, String shopId, String[] type) {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(shopId) || type == null || type.length == 0) {
			return null;
		}

		int count = getTradeCount(userId, shopId, type);
		// 不存在
		if (count == 0) {
			return null;
		}

		Trade trade = new Trade();
		trade.setUserId(userId.trim());
		trade.setShopId(shopId.trim());
		trade.setCodes(type);

		// 暂不分页
		trade.setStart(0);
		trade.setLimit(count);
		trade.setSort("createDate");
		trade.setDir("desc");

		return getTradeList(trade);
	}

	@Override
	public BooleanResult createTrade(final String userId, final String shopId, final String[] cartId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		if (cartId == null || cartId.length == 0) {
			result.setCode("购物车不能为空！");
			return result;
		}

		// 获取选中商品总计价格，兑换积分，运费
		final Cart cart = cartService.getCartStats(userId.trim(), shopId.trim(), cartId);

		// 合计价格 合计积分 最小运费
		if (cart.getPrice() == null || cart.getPoints() == null || cart.getPostage() == null) {
			result.setCode("购物车商品总价为空！");
			return result;
		}

		// 获取当前用户会员等级 -> 获取折扣
		BigDecimal discount = BigDecimal.ZERO;
		Member member = memberService.getMember(shopId.trim(), userId.trim());
		if (member != null) {
			discount = cart.getPrice().multiply(BigDecimal.ONE.negate().add(member.getDiscount()));
		}
		// 获取折扣
		final BigDecimal change = discount;

		// 获取默认收货地址
		final UserAddress userAddress = userAddressService.getDefaultUserAddress(userId.trim());

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = new BooleanResult();
				result.setResult(false);

				// 1. 创建交易
				// create trade
				String tradeId = null;

				Trade trade = new Trade();
				trade.setUserId(userId.trim());
				trade.setShopId(shopId);
				// 交易价格
				trade.setTradePrice(cart.getPrice());
				// 积分兑换
				trade.setTradePoints(cart.getPoints());
				trade.setCouponPrice(BigDecimal.ZERO);
				trade.setPostage(cart.getPostage());
				trade.setChange(change);
				// 买家结算
				trade.setType(ITradeService.CHECK);

				// 收货地址
				if (userAddress != null) {
					trade.setReceiverName(userAddress.getContactName());
					trade.setReceiverProvince(userAddress.getProvince());
					trade.setReceiverCity(userAddress.getCity());
					trade.setReceiverArea(userAddress.getArea());
					trade.setReceiverBackCode(userAddress.getBackCode());
					trade.setReceiverAddress(userAddress.getAddress());
					trade.setReceiverZip(userAddress.getPostalCode());
					trade.setReceiverTel(userAddress.getTel());
				}

				// 14位日期 ＋ 3位随机数
				trade.setTradeNo("E" + DateUtil.getNowDateminStr() + NoUtil.generate().substring(7));

				// 交易订单 关联 购物车
				StringBuilder sb = new StringBuilder();
				for (String id : cartId) {
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(id);
				}
				trade.setCartId(sb.toString());

				try {
					tradeId = tradeDao.createTrade(trade);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(trade), e);
					ts.setRollbackOnly();

					result.setCode("创建交易失败！");
					return result;
				}

				// 2. 创建订单
				result = orderService.createOrder(tradeId, shopId.trim(), cartId, userId.trim());
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				result.setCode(trade.getTradeNo());
				return result;
			}
		});

		return res;
	}

	@Override
	public Trade getTrade(String userId, String shopId, String tradeNo) {
		// not null
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(shopId) || StringUtils.isBlank(tradeNo)) {
			return null;
		}

		Trade trade = new Trade();
		trade.setUserId(userId.trim());
		trade.setShopId(shopId.trim());
		trade.setTradeNo(tradeNo.trim());

		trade = getTrade(trade);

		// 交易不存在
		if (trade == null) {
			return null;
		}

		return getTradeDetail(trade);
	}

	@Override
	public BooleanResult updateReceiver(String userId, String shopId, String tradeId, Trade trade) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("订单信息不能为空！");
			return result;
		}

		if (trade == null) {
			result.setCode("联系人信息不能为空！");
			return result;
		}

		trade.setUserId(userId.trim());
		trade.setModifyUser(userId.trim());
		trade.setShopId(shopId.trim());
		trade.setTradeId(tradeId.trim());

		return updateTrade(trade);
	}

	@Override
	public BooleanResult cancelMyTrade(String userId, String shopId, String tradeId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		final Trade trade = new Trade();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		trade.setUserId(userId.trim());
		trade.setModifyUser(userId);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		trade.setShopId(shopId.trim());

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("订单信息不能为空！");
			return result;
		}

		trade.setTradeId(tradeId.trim());

		// 锁定当前订单
		try {
			memcachedCacheService.add(IMemcachedCacheService.CACHE_KEY_TRADE_ID + tradeId.trim(), tradeId,
				IMemcachedCacheService.CACHE_KEY_TRADE_ID_DEFAULT_EXP);
		} catch (ServiceException e) {
			result.setCode("当前订单已被锁定，请稍后再试！");
			return result;
		}

		// 0. 查询 未付款交易订单
		Trade t = getTrade(trade);
		if (t == null) {
			result.setCode("当前订单不存在！");
			return result;
		}

		if (!ITradeService.CHECK.equals(t.getType()) && !ITradeService.TO_PAY.equals(t.getType())) {
			result.setCode("当前订单已付款或取消！");
			return result;
		}

		return cancelTrade(trade, t.getType(), t.getUserId());
	}

	@Override
	public BooleanResult payTrade(final String userId, final String shopId, String tradeId, String payType,
		HttpServletRequest request, HttpServletResponse response) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		final Trade trade = new Trade();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		trade.setUserId(userId.trim());
		trade.setModifyUser(userId);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		trade.setShopId(shopId.trim());

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("订单信息不能为空！");
			return result;
		}

		trade.setTradeId(tradeId.trim());

		// 验证支付方式
		if (StringUtils.isNotBlank(payType) && !ITradeService.PAY_TYPE_ALIPAY.equals(payType)
			&& !ITradeService.PAY_TYPE_WXAP.equals(payType)) {
			result.setCode("请重新选择支付方式！");
			return result;
		}

		// set 支付方式
		trade.setPayType(payType.trim());

		// 锁定当前订单
		try {
			memcachedCacheService.add(IMemcachedCacheService.CACHE_KEY_TRADE_ID + tradeId.trim(), tradeId,
				IMemcachedCacheService.CACHE_KEY_TRADE_ID_DEFAULT_EXP);
		} catch (ServiceException e) {
			result.setCode("当前订单已被锁定，请稍后再试！");
			return result;
		}

		// 0. 查询 未付款交易订单
		Trade t = getTrade(trade);
		if (t == null) {
			result.setCode("当前订单不存在！");
			return result;
		}

		// 已生成未付款订单 直接调用 alipay 接口
		if (ITradeService.TO_PAY.equals(t.getType())) {
			// 1. 判断 未付款订单的支付方式 与 当前操作支付方式 是否一致
			if (!t.getPayType().equals(payType)) {
				result.setCode("请重新选择支付方式！");
				return result;
			}

			if (ITradeService.PAY_TYPE_ALIPAY.equals(payType)) {
				try {
					result.setCode(buildRequest(t.getTradeNo(), t.getPrice().toString()));
					result.setResult(true);
				} catch (Exception e) {
					logger.error(e);

					result.setCode(IAlipayService.ERROR_MESSAGE);
				}
			} else if (ITradeService.PAY_TYPE_WXAP.equals(payType)) {
				try {
					result.setCode(getBrandWCPayRequest(t.getTradeNo(), t.getPrice().multiply(new BigDecimal("100"))
						.setScale(0, BigDecimal.ROUND_HALF_UP).toString(), request, response));
					result.setResult(true);
				} catch (Exception e) {
					logger.error(e);

					result.setCode(IWxapService.ERROR_MESSAGE);
				}
			}

			return result;
		}

		// if not equals check then 已付款
		if (!ITradeService.CHECK.equals(t.getType())) {
			result.setCode("已生成付款订单！");
			return result;
		}

		// set 所需付款金额
		final BigDecimal price = t.getPrice();

		// set 积分兑换
		trade.setTradePoints(t.getTradePoints());

		// 相关联购物车id
		final String[] cartId = t.getCartId().split(",");

		// 1. 判断库存
		List<Order> orderList = orderService.getOrderList(tradeId.trim(), shopId.trim());
		if (orderList == null || orderList.size() == 0) {
			result.setCode("当前订单明细不存在！");
			return result;
		}

		// 存放各个商品库存数量 存在 购物相同商品 的情况
		Map<String, Integer> map = new HashMap<String, Integer>();

		for (Order order : orderList) {
			String key = order.getItemId() + "&" + order.getSkuId();
			if (!map.containsKey(key)) {
				map.put(key, order.getStock());
			}

			int quantity = order.getQuantity();
			int stock = map.get(key);
			if (quantity > stock) {
				String propertiesName = order.getPropertiesName();
				result.setCode(order.getItemName()
					+ (StringUtils.isBlank(propertiesName) ? "" : "(" + propertiesName + ")") + " 库存不足！");

				return result;
			}

			map.put(key, stock - quantity);
		}

		// 根据 map 组装 skuList
		// item sku 表库存
		final List<ItemSku> skuList = new ArrayList<ItemSku>();
		// item 表库存 即不存在 sku
		final List<Item> itemList = new ArrayList<Item>();
		// 用于统计 还有 sku 的商品的合计库存数
		final String[] itemId = new String[orderList.size()];
		int i = 0;

		for (Map.Entry<String, Integer> m : map.entrySet()) {
			String[] key = m.getKey().split("&");

			// if skuId is null or 0 then 商品没有规格
			if (StringUtils.isBlank(key[1]) || "0".equals(key[1])) {
				Item item = new Item();
				item.setItemId(key[0]);
				item.setStock(m.getValue());

				itemList.add(item);

				continue;
			}

			ItemSku sku = new ItemSku();
			sku.setItemId(key[0]);
			sku.setSkuId(key[1]);
			sku.setStock(m.getValue());

			skuList.add(sku);

			itemId[i++] = key[0];
		}

		// 1.1 判断是否使用积分
		// tradePoints 大于 0 then 存在消费积分
		if (t.getTradePoints().compareTo(BigDecimal.ZERO) == 1) {
			// 获取当前用户积分
			Member member = memberService.getMember(shopId.trim(), userId);
			if (member == null) {
				result.setCode("会员信息不存在！");
				return result;
			}

			// 消费积分 大于 剩余积分
			if (t.getTradePoints().compareTo(member.getSurplusPoints()) == 1) {
				result.setCode("会员积分不够，不能兑换商品！");
				return result;
			}
		}

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = new BooleanResult();

				// 2. 占用库存

				// 2.1 存在 item_sku
				if (skuList != null && skuList.size() > 0) {
					// 2.1.1 更新 item_sku stock
					result = itemSkuService.updateItemSkuStock(skuList, userId);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}

					// 2.1.2 更新 还有 sku 的 item 合计库存数
					result = itemService.updateItemStock(shopId.trim(), itemId, userId);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				// 2.2 不存在 item_sku
				if (itemList != null && itemList.size() > 0) {
					// 2.2.1 更新 item stock
					result = itemService.updateItemStock(shopId.trim(), itemList, userId);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				// 3. if 存在积分兑换 then
				if (trade.getTradePoints().compareTo(BigDecimal.ZERO) == 1) {
					// 3.1 增加用户消费积分记录
					result =
						memberPointsService.recordMemberPoints(userId, shopId.trim(), trade.getTradePoints().negate(),
							IMemberPointsService.POINTS_SOURCE_EXCHANGE, trade.getTradeId());
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				// 4. 修改交易状态 -> topay
				// update trade type
				trade.setType(ITradeService.TO_PAY);
				result = updateTrade(trade);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 5. 修改购物车状态
				result = cartService.completeCart(userId, shopId, cartId);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 6. if 消费金额等于 0 then 交易状态 -> TO_SEND
				if (BigDecimal.ZERO.compareTo(price) == 0) {
					trade.setType(ITradeService.TO_SEND);
					result = updateTrade(trade);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				result.setResult(true);
				return result;
			}
		});

		// 消费金额大于 0 then 返回 第三方支付地址
		if (res.getResult() && (BigDecimal.ZERO.compareTo(price) == -1)) {
			if (ITradeService.PAY_TYPE_ALIPAY.equals(payType)) {
				try {
					res.setCode(buildRequest(t.getTradeNo(), t.getPrice().toString()));
				} catch (Exception e) {
					logger.error(e);

					res.setResult(false);
					res.setCode(IAlipayService.ERROR_MESSAGE);
				}
			} else if (ITradeService.PAY_TYPE_WXAP.equals(payType)) {
				try {
					res.setCode(getBrandWCPayRequest(t.getTradeNo(), t.getPrice().multiply(new BigDecimal("100"))
						.setScale(0, BigDecimal.ROUND_HALF_UP).toString(), request, response));
				} catch (Exception e) {
					logger.error(e);

					res.setResult(false);
					res.setCode(IAlipayService.ERROR_MESSAGE);
				}
			}
		}

		return res;
	}

	/**
	 * 生成支付.
	 * 
	 * @param tradeNo
	 * @param totalFee
	 * @return
	 * @throws Exception
	 */
	private String buildRequest(String tradeNo, String totalFee) throws Exception {
		return alipayService.buildRequest(tradeNo, "在线商城 - 订单编号" + tradeNo, totalFee);
	}

	/**
	 * 生成支付.
	 * 
	 * @param tradeNo
	 * @param totalFee
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private String getBrandWCPayRequest(String tradeNo, String totalFee, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		return wxapService.getBrandWCPayRequest(tradeNo, "在线商城 - 订单编号" + tradeNo, totalFee, request, response);
	}

	@Override
	public BooleanResult signTrade(String userId, String shopId, String tradeId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		Trade trade = new Trade();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		trade.setUserId(userId.trim());
		trade.setModifyUser(userId);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("请先创建/选择店铺！");
			return result;
		}

		trade.setShopId(shopId.trim());

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("请先选择交易订单！");
			return result;
		}

		trade.setTradeId(tradeId.trim());

		trade.setType(ITradeService.SIGN);

		// send 可以签收
		Trade t = getTrade(trade);

		if (t == null) {
			result.setCode("交易订单信息不存在！");
			return result;
		}

		if (!ITradeService.SEND.equals(t.getType())) {
			result.setCode("交易订单尚未发货或已签收！");
			return result;
		}

		return updateTrade(trade);
	}

	@Override
	public BooleanResult feedbackTrade(String userId, String shopId, String tradeId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		Trade trade = new Trade();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		trade.setUserId(userId.trim());
		trade.setModifyUser(userId);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("请先创建/选择店铺！");
			return result;
		}

		trade.setShopId(shopId.trim());

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("请先选择交易订单！");
			return result;
		}

		trade.setTradeId(tradeId.trim());

		trade.setType(ITradeService.FEEDBACK);

		// tosend or send 可以维权
		Trade t = getTrade(trade);

		if (t == null) {
			result.setCode("交易订单信息不存在！");
			return result;
		}

		if (!ITradeService.TO_SEND.equals(t.getType()) && !ITradeService.SEND.equals(t.getType())) {
			result.setCode("交易订单尚未付款或已签收！");
			return result;
		}

		// 记录维权时的订单类型(已付款 or 已发货)
		trade.setFeedbackType(t.getType());

		return updateTrade(trade);
	}

	@Override
	public BooleanResult getTradeExpress(String userId, String shopId, String tradeId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		Trade trade = new Trade();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		trade.setUserId(userId.trim());
		trade.setModifyUser(userId);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("请先创建/选择店铺！");
			return result;
		}

		trade.setShopId(shopId.trim());

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("请先选择交易订单！");
			return result;
		}

		trade.setTradeId(tradeId.trim());

		Trade tr = getTrade(trade);
		if (tr == null) {
			result.setCode("当前交易订单不存在或您没有查看的权限！");
			return result;
		}

		List<TradeExpress> tradeExpressList = tradeExpressService.getTradeExpressList(trade.getTradeId());
		if (tradeExpressList == null || tradeExpressList.size() == 0) {
			result.setCode("当前交易订单不存在物流信息！");
			return result;
		}

		TradeExpress tradeExpress = tradeExpressList.get(0);
		result.setCode(tradeExpress.getMobileApi() + tradeExpress.getEpsNo());
		result.setResult(true);

		return result;
	}

	// >>>>>>>>>>以下是第三方交易平台<<<<<<<<<<

	private Trade getTrade(String tradeNo) {
		// not null
		if (StringUtils.isBlank(tradeNo)) {
			return null;
		}

		Trade trade = new Trade();
		trade.setTradeNo(tradeNo.trim());

		return getTrade(trade);
	}

	private BooleanResult updateTrade(String shopId, String tradeId, String type) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		Trade trade = new Trade();

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		trade.setShopId(shopId.trim());

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("交易信息不能为空！");
			return result;
		}

		trade.setTradeId(tradeId.trim());

		if (StringUtils.isBlank(type)) {
			result.setCode("交易类型不能为空！");
			return result;
		}

		trade.setType(type.trim());
		trade.setModifyUser("system");

		return updateTrade(trade);
	}

	@Override
	public BooleanResult callbackTrade(final HttpServletRequest request) {
		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = null;

				try {
					result = alipayService.recordCallback(request);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(request), e);
					ts.setRollbackOnly();

					result.setCode("交易返回信息解析失败！");
					return result;
				}

				// callback 失败
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 订单编号
				result = callbackTrade(result.getCode());
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				return result;
			}
		});

		return res;
	}

	@Override
	public boolean notifyTrade(final String tradeNo, final Map<String, String> params) {
		if (StringUtils.isBlank(tradeNo)) {
			return false;
		}

		boolean res = (Boolean) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// 1. 调用 callbackTrade 修改订单状态 未付款 -> 付款
				BooleanResult result = callbackTrade(tradeNo);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return false;
				}

				// 2. 记录 notify
				if (!alipayService.recordNotify(params)) {
					ts.setRollbackOnly();

					return false;
				}

				return true;
			}
		});

		return res;
	}

	@Override
	public boolean notifyTrade(final String tradeNo, final ResponseHandler resHandler) {
		if (StringUtils.isBlank(tradeNo)) {
			return false;
		}

		boolean res = (Boolean) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// 1. 调用 callbackTrade 修改订单状态 未付款 -> 付款
				BooleanResult result = callbackTrade(tradeNo);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return false;
				}

				// 2. 记录 notify
				if (!wxapService.recordNotify(resHandler)) {
					ts.setRollbackOnly();

					return false;
				}

				return true;
			}
		});

		return res;
	}

	private BooleanResult callbackTrade(String tradeNo) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(tradeNo)) {
			result.setCode("交易信息不能为空！");
			return result;
		}

		// 锁定当前订单
		try {
			memcachedCacheService.add(IMemcachedCacheService.CACHE_KEY_TRADE_ID + tradeNo.trim(), tradeNo,
				IMemcachedCacheService.CACHE_KEY_TRADE_ID_DEFAULT_EXP);
		} catch (ServiceException e) {
			result.setCode("当前订单已被锁定，请稍后再试！");
			return result;
		}

		final Trade trade = getTrade(tradeNo.trim());

		if (trade == null) {
			result.setCode("当前订单不存在！");
			return result;
		}

		if (!ITradeService.TO_PAY.equals(trade.getType())) {
			result.setCode("当前订单已完成支付！");
			result.setResult(true);
			return result;
		}

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = new BooleanResult();
				result.setResult(false);

				// 1. 修改交易状态 topay -> tosend
				result = updateTrade(trade.getShopId(), trade.getTradeId(), ITradeService.TO_SEND);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 2. 记录会员积分
				result =
					memberPointsService.recordMemberPoints(trade.getUserId(), trade.getCompanyId(), trade.getPrice(),
						IMemberPointsService.POINTS_SOURCE_TRADE, trade.getTradeId());
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				return result;
			}
		});

		return res;
	}

	// >>>>>>>>>>公共方法<<<<<<<<<<

	private int getTradeCount(Trade trade) {
		try {
			return tradeDao.getTradeCount(trade);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(trade), e);
		}

		return 0;
	}

	private List<Trade> getTradeList(Trade trade) {
		try {
			return tradeDao.getTradeList(trade);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(trade), e);
		}

		return null;
	}

	private BooleanResult updateTrade(Trade trade) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		try {
			int c = tradeDao.updateTrade(trade);
			if (c == 1) {
				result.setCode("操作成功！");
				result.setResult(true);
			} else {
				result.setCode("交易信息更新失败，请稍后再试！");
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(trade), e);

			result.setCode("交易信息更新失败！");
		}

		return result;
	}

	private Trade getTrade(Trade trade) {
		try {
			return tradeDao.getTrade(trade);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(trade), e);
		}

		return null;
	}

	/**
	 * 取消订单 返还积分.
	 * 
	 * @param trade
	 * @param userId
	 *            买家.
	 * @return
	 */
	private BooleanResult cancelTrade(final Trade trade, String type, final String userId) {
		// 是否需要释放库存
		boolean f = false;
		// item sku 表库存
		List<ItemSku> skus = null;
		// item 表库存 即不存在 sku
		List<Item> items = null;
		// 用于统计 还有 sku 的商品的合计库存数
		String[] itemIds = null;

		// 根据 type 判断 是否需要 释放已占库存
		if (ITradeService.TO_PAY.equals(type)) {
			// 需要释放库存
			f = true;

			// 1. 判断库存
			List<Order> orderList = orderService.getOrderList(trade.getTradeId(), trade.getShopId());
			if (orderList == null || orderList.size() == 0) {
				BooleanResult result = new BooleanResult();
				result.setResult(false);
				result.setCode("当前订单明细不存在！");

				return result;
			}

			// 存放各个商品库存数量 存在 购物相同商品 的情况
			Map<String, Integer> map = new HashMap<String, Integer>();

			for (Order order : orderList) {
				String key = order.getItemId() + "&" + order.getSkuId();
				if (!map.containsKey(key)) {
					map.put(key, order.getStock());
				}

				int quantity = order.getQuantity();
				int stock = map.get(key);

				map.put(key, stock + quantity);
			}

			// 根据 map 组装 skuList
			skus = new ArrayList<ItemSku>();
			items = new ArrayList<Item>();
			itemIds = new String[orderList.size()];
			int i = 0;

			for (Map.Entry<String, Integer> m : map.entrySet()) {
				String[] key = m.getKey().split("&");

				// if skuId is null or 0 then 商品没有规格
				if (StringUtils.isBlank(key[1]) || "0".equals(key[1])) {
					Item item = new Item();
					item.setItemId(key[0]);
					item.setStock(m.getValue());

					items.add(item);

					continue;
				}

				ItemSku sku = new ItemSku();
				sku.setItemId(key[0]);
				sku.setSkuId(key[1]);
				sku.setStock(m.getValue());

				skus.add(sku);

				itemIds[i++] = key[0];
			}
		}

		// type 取消
		trade.setType(ITradeService.CANCEL);
		// 是否需要释放库存
		final boolean flag = f;
		// item sku 表库存
		final List<ItemSku> skuList = skus;
		// item 表库存 即不存在 sku
		final List<Item> itemList = items;
		// 用于统计 还有 sku 的商品的合计库存数
		final String[] itemId = itemIds;

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// 1. 订单状态取消
				BooleanResult result = updateTrade(trade);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 4. 释放库存
				if (flag) {
					// 4.1 存在 item_sku
					if (skuList != null && skuList.size() > 0) {
						// 4.1.1 更新 item_sku stock
						result = itemSkuService.updateItemSkuStock(skuList, userId);
						if (!result.getResult()) {
							ts.setRollbackOnly();

							return result;
						}

						// 4.1.2 更新 还有 sku 的 item 合计库存数
						result = itemService.updateItemStock(trade.getShopId(), itemId, userId);
						if (!result.getResult()) {
							ts.setRollbackOnly();

							return result;
						}
					}

					// 4.2 不存在 item_sku
					if (itemList != null && itemList.size() > 0) {
						// 2.2.1 更新 item stock
						result = itemService.updateItemStock(trade.getShopId(), itemList, userId);
						if (!result.getResult()) {
							ts.setRollbackOnly();

							return result;
						}
					}
				}

				// 2. 查询交易相关积分
				List<MemberPoints> memberPointsList =
					memberPointsService.getMemberPointsList(userId, trade.getShopId(), trade.getTradeId());

				// 不存在积分 -> 直接返回 true
				if (memberPointsList == null || memberPointsList.size() == 0) {
					return result;
				}

				Collections.sort(memberPointsList, new MemberPoints());

				// 3. 积分返还
				for (MemberPoints memberPoints : memberPointsList) {
					result =
						memberPointsService.returnMemberPoints(userId, trade.getShopId(), memberPoints.getPoints()
							.negate(), memberPoints.getPointsSource() + "(交易取消返还积分)", trade.getTradeId());
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				return result;
			}
		});

		return res;
	}

	/**
	 * 获取交易详细信息 包括订单明细 图片.
	 * 
	 * @param trade
	 * @return
	 */
	private Trade getTradeDetail(Trade trade) {
		List<Order> orderList = orderService.getOrderList(trade.getTradeId(), trade.getShopId());

		if (orderList == null || orderList.size() == 0) {
			return trade;
		}

		trade.setOrderList(orderList);

		// 1. 查找商品对应图片
		String[] itemId = new String[orderList.size()];
		int i = 0;
		for (Order order : orderList) {
			itemId[i++] = order.getItemId();
		}

		// 2. 获取商品文件信息
		Map<String, List<ItemFile>> map = itemFileService.getItemFileList(trade.getShopId(), itemId);

		// 不存在商品文件 直接返回
		if (map == null || map.isEmpty()) {
			return trade;
		}

		for (Order order : orderList) {
			order.setItemFileList(map.get(order.getItemId()));
		}

		return trade;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public ICartService getCartService() {
		return cartService;
	}

	public void setCartService(ICartService cartService) {
		this.cartService = cartService;
	}

	public IOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

	public IItemFileService getItemFileService() {
		return itemFileService;
	}

	public void setItemFileService(IItemFileService itemFileService) {
		this.itemFileService = itemFileService;
	}

	public IUserAddressService getUserAddressService() {
		return userAddressService;
	}

	public void setUserAddressService(IUserAddressService userAddressService) {
		this.userAddressService = userAddressService;
	}

	public IItemSkuService getItemSkuService() {
		return itemSkuService;
	}

	public void setItemSkuService(IItemSkuService itemSkuService) {
		this.itemSkuService = itemSkuService;
	}

	public IItemService getItemService() {
		return itemService;
	}

	public void setItemService(IItemService itemService) {
		this.itemService = itemService;
	}

	public IMemberService getMemberService() {
		return memberService;
	}

	public void setMemberService(IMemberService memberService) {
		this.memberService = memberService;
	}

	public IMemberPointsService getMemberPointsService() {
		return memberPointsService;
	}

	public void setMemberPointsService(IMemberPointsService memberPointsService) {
		this.memberPointsService = memberPointsService;
	}

	public IAlipayService getAlipayService() {
		return alipayService;
	}

	public void setAlipayService(IAlipayService alipayService) {
		this.alipayService = alipayService;
	}

	public IWxapService getWxapService() {
		return wxapService;
	}

	public void setWxapService(IWxapService wxapService) {
		this.wxapService = wxapService;
	}

	public ITradeExpressService getTradeExpressService() {
		return tradeExpressService;
	}

	public void setTradeExpressService(ITradeExpressService tradeExpressService) {
		this.tradeExpressService = tradeExpressService;
	}

	public ITradeDao getTradeDao() {
		return tradeDao;
	}

	public void setTradeDao(ITradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

}
