package com.kintiger.mall.register.service.impl;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.member.IMemberPointsService;
import com.kintiger.mall.api.member.IMemberService;
import com.kintiger.mall.api.register.IRegisterService;
import com.kintiger.mall.api.register.bo.RegisterResult;
import com.kintiger.mall.api.user.IUserService;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.EncryptUtil;

/**
 * 用户注册实现.
 * 
 * @author xujiakun
 * 
 */
public class RegisterServiceImpl implements IRegisterService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(RegisterServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IMemcachedCacheService memcachedCacheService;

	private IUserService userService;

	private IMemberService memberService;

	private IMemberPointsService memberPointsService;

	private RegisterResult validate(String mobile, String password) {
		// 初始化返回值 状态 = 失败
		RegisterResult result = new RegisterResult();
		result.setResultCode(IRegisterService.RESULT_FAILED);

		if (StringUtils.isBlank(mobile)) {
			result.setMessage("手机号码不能为空！");
			return result;
		}

		if (StringUtils.isBlank(password)) {
			result.setMessage("密码不能为空！");
			return result;
		}

		result.setResultCode(IRegisterService.RESULT_SUCCESS);
		return result;
	}

	@Override
	public RegisterResult registerUser(String mobile, String password, String userName) {
		// 验证
		RegisterResult res = validate(mobile, password);
		if (IRegisterService.RESULT_FAILED.equals(res.getResultCode())) {
			return res;
		}

		// 构造对象
		User user = new User();
		user.setPassport(mobile.trim());

		// encrypt
		try {
			user.setPassword(EncryptUtil.encryptSHA(password));
		} catch (IOException e) {
			logger.error("password:" + password, e);

			res.setResultCode(IRegisterService.RESULT_FAILED);
			res.setMessage("系统异常，请稍后再试！");
			return res;
		}

		user.setUserName(StringUtils.isEmpty(userName) ? mobile : userName);
		user.setMobile(mobile.trim());

		BooleanResult result = userService.createUser(user);
		// 创建失败
		if (!result.getResult()) {
			res.setResultCode(IRegisterService.RESULT_FAILED);
			res.setMessage(result.getCode());
			return res;
		}

		user.setPassword(null);
		user.setUserId(result.getCode());
		res.setUser(user);

		res.setMessage("用户注册成功！");
		return res;
	}

	@Override
	public RegisterResult registerUser(final String mobile, final String password, final String userName,
		final String shopId, String checkCode, String recommend) {
		// 初始化返回值 状态 = 失败
		RegisterResult result = new RegisterResult();
		result.setResultCode(IRegisterService.RESULT_FAILED);

		if (StringUtils.isBlank(shopId)) {
			result.setMessage("店铺信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(checkCode)) {
			result.setMessage("验证码不能为空！");
			return result;
		}

		User user = validateCheckCode(checkCode);
		if (user == null) {
			result.setMessage("验证码错误或已失效，请重新点击获取验证码！");
			return result;
		}

		// 验证码失效
		invalidCheckCode(checkCode);

		RegisterResult res = (RegisterResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				RegisterResult res1 = registerUser(mobile, password, userName);
				if (IRegisterService.RESULT_FAILED.equals(res1.getResultCode())) {
					ts.setRollbackOnly();

					return res1;
				}

				BooleanResult res2 = memberService.createMember(shopId.trim(), res1.getUser().getUserId());
				if (!res2.getResult()) {
					ts.setRollbackOnly();

					res1.setResultCode(IRegisterService.RESULT_FAILED);
					res1.setMessage(res2.getCode());
					return res1;
				}

				return res1;
			}
		});

		// if recommend is not null then
		if (IRegisterService.RESULT_SUCCESS.equals(res.getResultCode()) && StringUtils.isNotBlank(recommend)) {
			memberPointsService.recordMemberPoints(recommend.trim(), shopId.trim(), BigDecimal.TEN,
				IMemberPointsService.POINTS_SOURCE_RECOMMEND);
		}

		return res;
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

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
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

}
