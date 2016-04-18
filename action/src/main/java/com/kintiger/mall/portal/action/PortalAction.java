package com.kintiger.mall.portal.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.notify.INotifyService;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 
 * @author xujiakun
 * 
 */
public class PortalAction extends BaseAction {

	private static final long serialVersionUID = 5343182591626769110L;

	private static final String GOTO = "goto";

	private INotifyService notifyService;

	private String passport;

	private String s;

	private int notify;

	/**
	 * 店铺唯一 id.
	 */
	private String uuid;

	/**
	 * 门户首页.
	 * 
	 * @return
	 */
	public String index() {
		User user = getUser();
		if (user != null) {
			passport = user.getPassport();
			s = SUCCESS;

			setAttribute(this.getSession());
		} else {
			setAttribute();
		}

		return SUCCESS;
	}

	/**
	 * 移动端登录页面.
	 * 
	 * @return
	 */
	public String mindex() {
		setAttribute();
		return SUCCESS;
	}

	/**
	 * 首页.
	 * 
	 * @return
	 */
	public String home() {
		// if 存在 goto then
		HttpSession session = this.getSession();
		String url = (String) session.getAttribute(GOTO);
		if (StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(url.trim())) {
			HttpServletRequest request = getServletRequest();
			request.setAttribute(GOTO, url.trim());
			session.removeAttribute(GOTO);
		}

		// 显示当前登录用户
		User user = getUser();
		if (user != null) {
			this.setName(user.getUserName());
		}

		return SUCCESS;
	}

	@JsonResult(field = "notify")
	public String getNotifyCount() {
		User user = getUser();
		if (user != null) {
			// 显示消息中心
			notify = notifyService.getNotifyCount(user.getUserId(), INotifyService.NOTIFY_TYPE_DEFAULT);
		}

		return JSON_RESULT;
	}

	private void setAttribute(HttpSession session) {
		// if 存在 goto then
		HttpServletRequest request = getServletRequest();
		String url = request.getParameter(GOTO);
		if (StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(url.trim())) {
			session.setAttribute(GOTO, url.trim());
		}
	}

	private void setAttribute() {
		// if 存在 goto then
		HttpServletRequest request = getServletRequest();
		String url = request.getParameter(GOTO);
		if (StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(url.trim())) {
			request.setAttribute(GOTO, url.trim());
		}
	}

	public INotifyService getNotifyService() {
		return notifyService;
	}

	public void setNotifyService(INotifyService notifyService) {
		this.notifyService = notifyService;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public int getNotify() {
		return notify;
	}

	public void setNotify(int notify) {
		this.notify = notify;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
