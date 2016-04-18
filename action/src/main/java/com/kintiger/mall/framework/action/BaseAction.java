package com.kintiger.mall.framework.action;

import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.annotation.Decode;
import com.kintiger.mall.framework.bo.SearchInfo;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionSupport;

/**
 * BaseAction.
 * 
 * @author xujiakun
 * 
 */
public class BaseAction extends ActionSupport {

	public static final String LOGIN = "login";
	public static final String LOGIN_TIMEOUT = "440";
	public static final String LOGOUT = "logout";

	/**
	 * 移动设备登录页面.
	 */
	public static final String M_PORTAL_INDEX = "m_portal_index";

	public static final String CREATE = "create";
	public static final String UPDATE = "update";
	public static final String CREATE_PREPARE = "createPrepare";
	public static final String UPDATE_PREPARE = "updatePrepare";
	public static final String DETAIL = "detail";
	public static final String DELETE = "delete";

	public static final String LIST = "list";
	public static final String JSON_RESULT = "jsonresult";
	public static final String RESULT_MESSAGE = "resultMessage";

	private static final long serialVersionUID = 7674615559114195895L;

	private static final int LIMIT = 15;

	protected Properties env = new Properties();

	protected Map<String, Boolean> permission;

	protected String token;

	private String failMessage;

	private String successMessage;

	private int start;

	private String dir;

	private String sort;

	private int limit = LIMIT;

	@Decode
	private String name;

	private String code;

	protected String gmtStart;

	protected String gmtEnd;

	@Decode
	private String search;

	protected String actionName;

	/**
	 * 用户名 特供 top.vm.
	 */
	protected User globalUser;

	/**
	 * 取得HttpServletRequest对象.
	 * 
	 * @return HttpServletRequest对象.
	 */
	protected HttpServletRequest getServletRequest() {
		ActionContext ctx = ActionContext.getContext();
		return (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
	}

	protected HttpSession getSession() {
		return getServletRequest().getSession();
	}

	/**
	 * 取得HttpServletResponse对象.
	 * 
	 * @return HttpServletResponse对象.
	 */
	protected HttpServletResponse getServletResponse() {
		ActionContext ctx = ActionContext.getContext();
		return (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);
	}

	/**
	 * getUser from session.
	 * 
	 * @return
	 */
	public User getUser() {
		return (User) getSession().getAttribute("ACEGI_SECURITY_LAST_LOGINUSER");
	}

	/**
	 * getShop from session.
	 * 
	 * @return
	 */
	public Shop getShop() {
		return (Shop) getSession().getAttribute("ACEGI_SECURITY_LAST_SHOP");
	}

	public <T extends SearchInfo> T getSearchInfo(T info) {
		info.setStart(start);
		info.setDir(dir);
		info.setSort(sort);
		info.setLimit(limit);
		info.setName(name);
		info.setCode(code);
		info.setGmtStart(gmtStart);
		info.setGmtEnd(gmtEnd);
		info.setSearch(search);
		return info;
	}

	public SearchInfo getSearchInfo() {
		return this.getSearchInfo(new SearchInfo());
	}

	public Properties getEnv() {
		return env;
	}

	public void setEnv(Properties env) {
		this.env = env;
	}

	public String getEnv(String key) {
		return env.getProperty(key);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Boolean> getPermission() {
		return (Map<String, Boolean>) getSession().getAttribute("PERMISSION");
	}

	public void setPermission(Map<String, Boolean> permission) {
		this.permission = permission;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFailMessage() {
		return failMessage;
	}

	public void setFailMessage(String failMessage) {
		this.failMessage = failMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGmtStart() {
		return gmtStart;
	}

	public void setGmtStart(String gmtStart) {
		this.gmtStart = gmtStart;
	}

	public String getGmtEnd() {
		return gmtEnd;
	}

	public void setGmtEnd(String gmtEnd) {
		this.gmtEnd = gmtEnd;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public User getGlobalUser() {
		return (User) getSession().getAttribute("ACEGI_SECURITY_LAST_LOGINUSER");
	}

}
