package com.kintiger.mall.notify.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.dict.IDictService;
import com.kintiger.mall.api.notify.INotifyService;
import com.kintiger.mall.api.notify.bo.Notify;
import com.kintiger.mall.api.user.IUserService;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.mail.MailService;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.notify.dao.INotifyDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class NotifyServiceImpl implements INotifyService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(NotifyServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IMemcachedCacheService memcachedCacheService;

	private IDictService dictService;

	private IUserService userService;

	private INotifyDao notifyDao;

	public int getNotifyCount(String userId, String type) {
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userId.trim()) || StringUtils.isEmpty(type)
			|| StringUtils.isEmpty(type.trim())) {
			return 0;
		}

		Integer count = null;

		try {
			count =
				(Integer) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_NOTIFY_USER_TYPE_ID
					+ userId.trim() + "&" + type.trim());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_NOTIFY_USER_TYPE_ID + userId.trim() + "&" + type.trim(), e);
		}

		if (count != null) {
			return count;
		}

		Notify notify = new Notify();
		notify.setUserId(userId.trim());
		notify.setType(type.trim());
		notify.setReview("N");

		count = getNotifyCount(notify);

		// not null then set to cache
		if (count != null) {
			try {
				// 1h 超时
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_NOTIFY_USER_TYPE_ID + userId.trim() + "&"
					+ type.trim(), count, IMemcachedCacheService.CACHE_KEY_NOTIFY_USER_TYPE_ID_DEFAULT_EXP);
			} catch (ServiceException e) {
				logger.error(IMemcachedCacheService.CACHE_KEY_NOTIFY_USER_TYPE_ID + userId.trim() + "&" + type.trim(),
					e);
			}

			return count;
		}

		return 0;
	}

	public int getNotifyCount(Notify notify) {
		try {
			return notifyDao.getNotifyCount(notify);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(notify), e);
		}

		return 0;
	}

	public List<Notify> getNotifyList(Notify notify) {
		try {
			return notifyDao.getNotifyList(notify);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(notify), e);
		}

		return null;
	}

	public BooleanResult sendNotify(String system, String type, String userId, String message, String modifyUser) {
		return sendNotify(system, type, userId, message, null, modifyUser);
	}

	public BooleanResult sendNotify(String system, final String type, String userId, String message, String sendDate,
		String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);
		result.setCode(INotifyService.ERROR_MESSAGE);

		if (StringUtils.isEmpty(system) || StringUtils.isEmpty(system.trim())) {
			result.setCode("通知发送方不能为空！");
			return result;
		}

		if (StringUtils.isEmpty(type) || StringUtils.isEmpty(type.trim())) {
			result.setCode("通知类型不能为空！");
			return result;
		}

		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userId.trim())) {
			result.setCode("通知送达方不能为空！");
			return result;
		}

		if (StringUtils.isEmpty(message) || StringUtils.isEmpty(message.trim())) {
			result.setCode("通知内容不能为空！");
			return result;
		}

		if (StringUtils.isEmpty(modifyUser) || StringUtils.isEmpty(modifyUser.trim())) {
			result.setCode("通知维护方不能为空！");
			return result;
		}

		final Notify notify = new Notify();
		notify.setType(type.trim());
		notify.setSystem(system.trim());
		notify.setUserId(userId.trim());
		notify.setMessage(message.trim());
		if (StringUtils.isNotEmpty(sendDate) && StringUtils.isNotEmpty(sendDate.trim())) {
			notify.setSendDate(sendDate.trim());
		}
		notify.setModifyUser(modifyUser.trim());

		return (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = new BooleanResult();
				result.setResult(false);
				result.setCode(INotifyService.ERROR_MESSAGE);

				// create notify
				try {
					notifyDao.createNotify(notify);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(notify), e);
					ts.setRollbackOnly();

					result.setCode("消息中心写入失败！");
					return result;
				}

				// if sendDate is null then send notify now
				if (StringUtils.isEmpty(notify.getSendDate())) {
					if (INotifyService.NOTIFY_TYPE_MAIL.equals(type.trim())) {
						Map<String, String> m = initMail();

						User user = userService.getUser(notify.getUserId());

						MailService mail =
							new MailService(m.get("smtpServer"), m.get("from"), "EXP", m.get("username"), m
								.get("password"), user.getEmail(), "消息中心", notify.getMessage());
						Map<String, String> map = mail.send();

						if (!"success".equals(map.get("state"))) {
							ts.setRollbackOnly();

							result.setCode(map.get("message"));
							return result;
						}
					}
					// else if (INotifyService.NOTIFY_TYPE_SMS.equals(type.trim())) {
					//
					// }
				}

				result.setResult(true);
				// remove cache
				remove(notify.getUserId(), INotifyService.NOTIFY_TYPE_DEFAULT);

				return result;
			}
		});
	}

	public BooleanResult review(String[] id, String userId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);
		result.setCode(INotifyService.ERROR_MESSAGE);

		if (id == null || StringUtils.isEmpty(userId) || StringUtils.isEmpty(userId.trim())) {
			return result;
		}

		Notify notify = new Notify();
		notify.setUserId(userId.trim());
		notify.setModifyUser(userId.trim());
		notify.setReview("Y");
		notify.setCodes(id);

		int c = updateNotify(notify);
		if (c == -1) {
			return result;
		}

		result.setCode(String.valueOf(c));
		result.setResult(true);

		remove(userId.trim(), INotifyService.NOTIFY_TYPE_DEFAULT);

		return result;
	}

	private int updateNotify(Notify notify) {
		try {
			return notifyDao.updateNotify(notify);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(notify), e);
		}

		return -1;
	}

	/**
	 * remove cache.
	 * 
	 * @param userId
	 * @param passport
	 */
	private void remove(String userId, String type) {
		try {
			memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_NOTIFY_USER_TYPE_ID + userId + "&" + type);
		} catch (ServiceException e) {
			logger.error(e);
		}
	}

	/**
	 * 获取mail参数.
	 * 
	 * @return
	 */
	private Map<String, String> initMail() {
		return dictService.getDictMap("mail");
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

	public IDictService getDictService() {
		return dictService;
	}

	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public INotifyDao getNotifyDao() {
		return notifyDao;
	}

	public void setNotifyDao(INotifyDao notifyDao) {
		this.notifyDao = notifyDao;
	}

}
