package com.kintiger.mall.account.action;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.account.IAccountService;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 账户管理.
 * 
 * @author xujiakun
 * 
 */
public class AccountAction extends BaseAction {

	private static final long serialVersionUID = -618238287549084080L;

	private Logger4jExtend logger = Logger4jCollection.getLogger(AccountAction.class);

	private IAccountService accountService;

	private String passport;

	private String password;

	/**
	 * 原密码.
	 */
	private String oldPassword;

	/**
	 * 验证码.
	 */
	private String checkCode;

	/**
	 * 修改用户基本信息.
	 */
	private User user;

	/**
	 * ajax 返回.
	 */
	private String message;

	// >>>>>>>>>>以下是忘记密码<<<<<<<<<<

	/**
	 * 忘记密码.
	 * 
	 * @return
	 */
	public String forgetPassword() {
		if (StringUtils.isNotEmpty(passport) && StringUtils.isNotEmpty(passport.trim())) {
			try {
				passport = new String(passport.trim().getBytes("ISO8859-1"), "UTF-8");
			} catch (Exception e) {
				logger.error(passport, e);
			}
		}

		return SUCCESS;
	}

	/**
	 * 找回登录密码 发送验证码.
	 * 
	 * @return
	 */
	public String sendCheckCode() {
		BooleanResult result = accountService.generateCheckCode4Mail(passport);

		this.setSuccessMessage(result.getCode());

		if (result.getResult()) {
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	/**
	 * 忘记密码 修改密码.
	 * 
	 * @return
	 */
	public String setPassword() {
		return SUCCESS;
	}

	@ActionMonitor(actionName = "密码重置")
	public String updatePassword() {
		BooleanResult result = accountService.setPassword(password, checkCode);

		if (result.getResult()) {
			this.setFailMessage(result.getCode());
		} else {
			this.setSuccessMessage("成功修改密码！");
		}

		return RESULT_MESSAGE;
	}

	// >>>>>>>>>>以下是修改密码<<<<<<<<<<

	@ActionMonitor(actionName = "密码修改")
	@JsonResult(field = "message")
	public String resetPassword() {
		this.getServletResponse().setStatus(500);

		// 验证用户是否登陆
		User u = super.getUser();
		if (u == null) {
			message = "login";
			return JSON_RESULT;
		}

		BooleanResult result = accountService.resetPassword(u.getUserId(), password, oldPassword);

		if (result.getResult()) {
			this.getServletResponse().setStatus(200);
		}

		message = result.getCode();

		return JSON_RESULT;
	}

	// >>>>>>>>>>以下是修改用户基本信息<<<<<<<<<<

	@JsonResult(field = "message")
	public String updateAccount() {
		this.getServletResponse().setStatus(500);

		// 验证用户是否登陆
		User u = super.getUser();
		if (u == null) {
			message = "login";
			return JSON_RESULT;
		}

		BooleanResult result = accountService.updateAccount(u.getUserId(), user);

		if (result.getResult()) {
			// session setAttribute
			u.setUserName(user.getUserName());
			u.setEmail(user.getEmail());
			getSession().setAttribute("ACEGI_SECURITY_LAST_LOGINUSER", u);

			this.getServletResponse().setStatus(200);
		}

		message = result.getCode();

		return JSON_RESULT;
	}

	public IAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
