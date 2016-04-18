package com.kintiger.mall.api.cache;

import java.net.InetSocketAddress;
import java.util.List;

import com.kintiger.mall.api.cache.bo.CacheStats;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.exception.SystemException;

/**
 * MemcachedCache.
 * 
 * @author xujiakun
 * 
 */
public interface IMemcachedCacheService extends ICacheService<String, Object> {

	/**
	 * default_exp_time.
	 */
	int DEFAULT_EXP = 24 * 60 * 60;

	/**
	 * session.
	 */
	int CACHE_KEY_SESSION_DEFAULT_EXP = 24 * 60 * 60;

	/**
	 * session.
	 */
	int CACHE_KEY_SESSION_EXP = 3 * 60;

	// >>>>>>>>>>以下是权限相关<<<<<<<<<<

	/**
	 * sso token.
	 */
	int CACHE_KEY_SSO_TOKEN_DEFAULT_EXP = 60;

	/**
	 * check code.
	 */
	String CACHE_KEY_CHECK_CODE = "key_check_code_";

	int CACHE_KEY_CHECK_CODE_DEFAULT_EXP = 10 * 60;

	/**
	 * user id.
	 */
	String CACHE_KEY_USER_ID = "key_user_id_";

	int CACHE_KEY_USER_ID_DEFAULT_EXP = 1 * 60 * 60;

	/**
	 * passport.
	 */
	String CACHE_KEY_PASSPORT = "key_passport_";

	int CACHE_KEY_PASSPORT_DEFAULT_EXP = 1 * 60 * 60;

	// >>>>>>>>>>以下是系统参数相关<<<<<<<<<<

	/**
	 * dict list.
	 */
	int CACHE_KEY_DICT_DEFAULT_EXP = 1 * 60;

	// >>>>>>>>>>以下是监控相关<<<<<<<<<<

	/**
	 * pageview.
	 */
	String CACHE_KEY_PAGEVIEW = "key_pageview";

	int CACHE_KEY_PAGEVIEW_DEFAULT_EXP = 0;

	/**
	 * log monitor.
	 */
	String CACHE_KEY_LOG_MONITOR = "key_log_monitor";

	int CACHE_KEY_LOG_MONITOR_DEFAULT_EXP = 0;

	/**
	 * action monitor.
	 */
	String CACHE_KEY_ACTION_MONITOR = "key_action_monitor";

	int CACHE_KEY_ACTION_MONITOR_DEFAULT_EXP = 0;

	// >>>>>>>>>>以下是接口相关<<<<<<<<<<

	/**
	 * open api.
	 */
	String CACHE_KEY_OPEN_API = "key_open_api";

	int CACHE_KEY_OPEN_API_DEFAULT_EXP = 0;

	// >>>>>>>>>>以下是文件相关<<<<<<<<<<

	/**
	 * file.
	 */
	String CACHE_KEY_FILE_ID = "key_file_id_";

	int CACHE_KEY_FILE_ID_DEFAULT_EXP = 1 * 60 * 60;

	// >>>>>>>>>>以下是店铺相关<<<<<<<<<<

	/**
	 * shop id.
	 */
	String CACHE_KEY_SHOP_ID = "key_shop_id_";

	int CACHE_KEY_SHOP_ID_DEFAULT_EXP = 0;

	/**
	 * shop uuid.
	 */
	String CACHE_KEY_SHOP_UUID = "key_shop_uuid_";

	int CACHE_KEY_SHOP_UUID_DEFAULT_EXP = 0;

	// >>>>>>>>>>以下是商品相关<<<<<<<<<<

	/**
	 * item file.
	 */
	String CACHE_KEY_ITEM_FILE_ITEM_ID = "key_item_file_item_id_";

	int CACHE_KEY_ITEM_FILE_ITEM_ID_DEFAULT_EXP = 1 * 60 * 60;

	/**
	 * tag.
	 */
	String CACHE_KEY_TAG_SHOP_ID = "key_tag_shop_Id_";

	// >>>>>>>>>>以下是交易订单相关<<<<<<<<<<

	/**
	 * trade.
	 */
	String CACHE_KEY_TRADE_ID = "key_trade_id_";

	int CACHE_KEY_TRADE_ID_DEFAULT_EXP = 15;

	// >>>>>>>>>>以下是积分卡相关<<<<<<<<<<

	/**
	 * card no.
	 */
	String CACHE_KEY_CARD_NO = "key_card_no_";

	int CACHE_KEY_CARD_NO_DEFAULT_EXP = 10;

	// >>>>>>>>>>以下是会员相关<<<<<<<<<<

	/**
	 * member.
	 */
	String CACHE_KEY_MEMBER_SHOP_USER_ID = "key_member_shop_user_id_";

	/**
	 * member info.
	 */
	String CACHE_KEY_MEMBER_INFO_MEM_ID = "key_member_info_mem_id_";

	/**
	 * member level.
	 */
	String CACHE_KEY_MEMBER_LEVEL_SHOP_ID = "key_member_level_shop_id_";

	/**
	 * mem id.
	 */
	String CACHE_KEY_MEM_ID = "key_mem_id_";

	int CACHE_KEY_MEM_ID_DEFAULT_EXP = 10;

	// >>>>>>>>>>以下是物流相关<<<<<<<<<<

	/**
	 * express.
	 */
	String CACHE_KEY_EXPRESS = "key_express";

	// >>>>>>>>>>以下是IP相关<<<<<<<<<<

	/**
	 * ip.
	 */
	String CACHE_KEY_IP = "key_ip_";

	int CACHE_KEY_IP_DEFAULT_EXP = 10;

	/**
	 * ip count.
	 */
	String CACHE_KEY_IP_COUNT = "key_ip_count_";

	int CACHE_KEY_IP_COUNT_DEFAULT_EXP = 0;

	// >>>>>>>>>>以下是广告相关<<<<<<<<<<

	/**
	 * advert stats.
	 */
	String CACHE_KEY_ADVERT_STATS = "key_advert_stats";

	int CACHE_KEY_ADVERT_STATS_DEFAULT_EXP = 0;

	// >>>>>>>>>>以下是微信相关<<<<<<<<<<

	/**
	 * wx app id.
	 */
	String CACHE_KEY_WX_APP_ID = "key_wx_app_id_";

	int CACHE_KEY_WX_APP_ID_DEFAULT_EXP = 7200 - 60;

	// >>>>>>>>>>以下是其他相关<<<<<<<<<<

	/**
	 * favorite list.
	 */
	String CACHE_KEY_FAVORITE = "favorite.getFavoriteList";

	int CACHE_KEY_FAVORITE_DEFAULT_EXP = 1 * 60 * 60;

	/**
	 * notify user type.
	 */
	String CACHE_KEY_NOTIFY_USER_TYPE_ID = "key_notify_user_type_id_";

	int CACHE_KEY_NOTIFY_USER_TYPE_ID_DEFAULT_EXP = 1 * 60 * 60;

	/**
	 * incr.
	 * 
	 * @param key
	 * @param inc
	 * @return
	 * @throws ServiceException
	 */
	long incr(String key, int inc) throws ServiceException;

	/**
	 * incr.
	 * 
	 * @param key
	 * @param inc
	 * @return
	 * @throws ServiceException
	 */
	long incr(String key, long inc) throws ServiceException;

	/**
	 * decr.
	 * 
	 * @param key
	 * @param decr
	 * @return
	 * @throws ServiceException
	 */
	long decr(String key, int decr) throws ServiceException;

	/**
	 * decr.
	 * 
	 * @param key
	 * @param decr
	 * @return
	 * @throws ServiceException
	 */
	long decr(String key, long decr) throws ServiceException;

	/**
	 * flushAll.
	 * 
	 * @param address
	 * @throws SystemException
	 */
	void flushAll(InetSocketAddress address) throws SystemException;

	/**
	 * cache stats.
	 * 
	 * @return
	 * @throws ServiceException
	 */
	List<CacheStats> getStats() throws ServiceException;

}
