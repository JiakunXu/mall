package com.kintiger.mall.api.notify.bo;

import java.util.Date;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 通知消息.
 * 
 * @author xujiakun
 * 
 */
public class Notify extends SearchInfo {

	private static final long serialVersionUID = -4812891429059901966L;

	private String id;

	/**
	 * 通知类型（系统提默认 D; 邮件 E; 短信 S）.
	 */
	private String type;

	/**
	 * 通知发送方 所属系统.
	 */
	private String system;

	/**
	 * 通知接受方.
	 */
	private String userId;

	/**
	 * 通知消息.
	 */
	private String message;

	/**
	 * 通知发送时间 yyyy-MM-dd HH:mm:ss.
	 */
	private String sendDate;

	private String modifyUser;

	/**
	 * 已读 Y or N.
	 */
	private String review;

	private Date modifyDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public Date getModifyDate() {
		return modifyDate != null ? (Date) modifyDate.clone() : null;
	}

	public void setModifyDate(Date modifyDate) {
		if (modifyDate != null) {
			this.modifyDate = (Date) modifyDate.clone();
		}
	}

}
