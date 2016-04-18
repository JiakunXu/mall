package com.kintiger.mall.notify.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.notify.INotifyService;
import com.kintiger.mall.api.notify.bo.Notify;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 消息中心.
 * 
 * @author xujiakun
 * 
 */
public class NotifyAction extends BaseAction {

	private static final long serialVersionUID = 3000465534747955841L;

	private INotifyService notifyService;

	private List<Notify> notifyList;

	private int total;

	private String review;

	/**
	 * 初始化查询时间.
	 */
	private void initGMT() {
		gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -90), DateUtil.DEFAULT_DATE_FORMAT);
		gmtEnd = DateUtil.getNowDateStr();
	}

	/**
	 * index.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "消息中心")
	public String index() {
		initGMT();
		return SUCCESS;
	}

	@JsonResult(field = "notifyList", include = { "id", "system", "message", "sendDate", "review", "modifyDate" }, total = "total")
	public String getNotifyJsonList() {
		User user = this.getUser();

		if (user == null) {
			return JSON_RESULT;
		}

		Notify notify = new Notify();
		notify = getSearchInfo(notify);

		notify.setUserId(user.getUserId());

		if (StringUtils.isNotEmpty(review) && StringUtils.isNotEmpty(review.trim())) {
			notify.setReview(review.trim());
		}

		// 系统提醒
		notify.setType(INotifyService.NOTIFY_TYPE_DEFAULT);

		total = notifyService.getNotifyCount(notify);

		if (total > 0) {
			notifyList = notifyService.getNotifyList(notify);
		}

		return JSON_RESULT;
	}

	public String review() {
		User user = this.getUser();
		if (user == null) {
			this.setFailMessage(INotifyService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		if (notifyList == null || notifyList.size() == 0) {
			this.setFailMessage("未读通知的编号不能为空！");
			return RESULT_MESSAGE;
		}

		String[] n = new String[notifyList.size()];
		int i = 0;

		for (Notify notfile : notifyList) {
			n[i++] = notfile.getId();
		}

		// 无有效的id
		if (i == 0) {
			this.setFailMessage("请选择未读通知！");
			return RESULT_MESSAGE;
		}

		BooleanResult r = notifyService.review(n, user.getUserId());
		if (r.getResult()) {
			this.setSuccessMessage("通知状态修改成功!");
		} else {
			this.setFailMessage(r.getCode());
		}

		return RESULT_MESSAGE;
	}

	public INotifyService getNotifyService() {
		return notifyService;
	}

	public void setNotifyService(INotifyService notifyService) {
		this.notifyService = notifyService;
	}

	public List<Notify> getNotifyList() {
		return notifyList;
	}

	public void setNotifyList(List<Notify> notifyList) {
		this.notifyList = notifyList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

}
