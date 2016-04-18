package com.kintiger.mall.account.service.impl;

import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.account.IAccountService;
import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.dict.IDictService;
import com.kintiger.mall.api.sms.ISMSService;
import com.kintiger.mall.api.user.IUserService;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.mail.MailService;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.framework.util.UUIDUtil;

/**
 * 账号管理.
 * 
 * @author xujiakun
 * 
 */
public class AccountServiceImpl implements IAccountService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(AccountServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private IUserService userService;

	private IDictService dictService;

	private ISMSService smsService;

	private String appUrl;

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	@Override
	public BooleanResult generateCheckCode4Mail(String passport) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(passport)) {
			result.setCode("登录帐号不能为空！");
			return result;
		}

		User user = userService.getUser4Validate(passport);

		if (user == null) {
			result.setCode("系统内无该用户,请确认账号是否正确！");
			return result;
		}

		if (StringUtils.isBlank(user.getEmail())) {
			result.setCode("邮件发送失败，没有找到该账号的邮箱地址，请确认账号是否正确！如账号是准确的，请联系管理员，确认邮箱信息是否维护！");
			return result;
		}

		String token = UUIDUtil.generate();

		try {
			memcachedCacheService.add(IMemcachedCacheService.CACHE_KEY_CHECK_CODE + token, user,
				IMemcachedCacheService.CACHE_KEY_CHECK_CODE_DEFAULT_EXP);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(user), e);

			result.setCode("系统正忙，请稍后再试！");
			return result;
		}

		StringBuilder content = new StringBuilder();
		content.append("尊敬的用户<br>&nbsp;&nbsp;您的姓名为：").append(user.getUserName())
			.append("&nbsp;&nbsp;<br>&nbsp;&nbsp;您的登陆账号为：").append(passport)
			.append("&nbsp;&nbsp;<br>&nbsp;&nbsp;请点击修改:<a href='").append(appUrl)
			.append("/account/setPassword.htm?checkCode=").append(token)
			.append("'>点击此链接修改</a><br>请在5分钟内完成验证，5分钟后链接失效，您需要重新进行验证。如果您错误的收到了本电子邮件，请您忽略上述内容<br>");

		Map<String, String> m = initMail();

		MailService mail =
			new MailService(m.get("smtpServer"), m.get("from"), "EXP", m.get("username"), m.get("password"),
				user.getEmail(), "密码修改确认函", content.toString());
		Map<String, String> map = mail.send();

		if ("success".equals(map.get("state"))) {
			String[] ss = user.getEmail().split("@");
			StringBuilder email = new StringBuilder();
			email.append(ss[0].substring(0, 1));
			for (int i = 0; i < ss[0].length() - 2; i++) {
				email.append("*");
			}
			email.append(ss[0].substring(ss[0].length() - 1, ss[0].length()));
			email.append("@").append(ss[1]);

			result.setCode("尊敬的用户 " + user.getUserName() + "，您好：<br />邮件已发送至您邮箱 " + email.toString() + "，注意查收！");
			result.setResult(true);
			return result;
		}

		result.setCode("邮件发送失败,请联系系统管理员！");
		return result;
	}

	@Override
	public BooleanResult updateAccount(String userId, User user) {
		return userService.updateUser(userId, user);
	}

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	@Override
	public BooleanResult generateCheckCode4Mobile(String mobile) {
		return generateCheckCode4Mobile(mobile, false);
	}

	@Override
	public BooleanResult generateCheckCode4Mobile(String mobile, boolean type) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(mobile)) {
			result.setCode("手机号不能为空！");
			return result;
		}

		User user = userService.getUser4Validate(mobile);

		if (type) {
			if (user == null) {
				result.setCode("手机号在系统中不存在！");
				return result;
			}
		} else {
			user = new User();
		}

		Random random = new Random();
		int min = 100000;
		int max = 999999;

		String token = String.valueOf(random.nextInt(max) % (max - min + 1) + min);

		try {
			memcachedCacheService.add(IMemcachedCacheService.CACHE_KEY_CHECK_CODE + token, user,
				IMemcachedCacheService.CACHE_KEY_CHECK_CODE_DEFAULT_EXP);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(user), e);

			result.setCode("验证码生成失败！");
			return result;
		}

		// 发送短信
		return smsService.send("注册/找回登录密码", mobile, token, "system");
	}

	// >>>>>>>>>>以下是公共接口<<<<<<<<<<

	@Override
	public BooleanResult setPassword(String password, String checkCode) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(checkCode)) {
			result.setCode("验证码不能为空！");
			return result;
		}

		User user = validateCheckCode(checkCode);
		if (user == null) {
			result.setCode("验证码错误或已失效，请重新点击获取验证码！");
			return result;
		}

		if (StringUtils.isBlank(password)) {
			result.setCode("密码不能为空！");
			return result;
		}

		// 验证码失效
		invalidCheckCode(checkCode);

		return userService.setPassword(user.getUserId(), password, user.getUserId());
	}

	@Override
	public BooleanResult resetPassword(String userId, String password, String oldPassword) {
		return userService.resetPassword(userId, password, oldPassword);
	}

	private User validateCheckCode(String checkCode) {
		try {
			return (User) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_CHECK_CODE + checkCode);
		} catch (Exception e) {
			logger.error("checkCode:" + checkCode, e);
		}

		return null;
	}

	private void invalidCheckCode(String checkCode) {
		try {
			memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_CHECK_CODE + checkCode);
		} catch (Exception e) {
			logger.error("checkCode:" + checkCode, e);
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

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public IDictService getDictService() {
		return dictService;
	}

	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}

	public ISMSService getSmsService() {
		return smsService;
	}

	public void setSmsService(ISMSService smsService) {
		this.smsService = smsService;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

}
