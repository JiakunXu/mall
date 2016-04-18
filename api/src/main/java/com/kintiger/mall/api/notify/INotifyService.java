package com.kintiger.mall.api.notify;

import java.util.List;

import com.kintiger.mall.api.notify.bo.Notify;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * Notification Center.
 * 
 * @author xujiakun
 * 
 */
public interface INotifyService {

	String ERROR_MESSAGE = "操作失败！";

	String ERROR_INPUT_MESSAGE = "操作失败，输入有误！";

	String NOTIFY_TYPE_DEFAULT = "D";

	String NOTIFY_TYPE_MAIL = "M";

	String NOTIFY_TYPE_SMS = "S";

	/**
	 * 查询具体用户 未读通知消息数量.
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	int getNotifyCount(String userId, String type);

	/**
	 * 
	 * @param notify
	 * @return
	 */
	int getNotifyCount(Notify notify);

	/**
	 * 
	 * @param notify
	 * @return
	 */
	List<Notify> getNotifyList(Notify notify);

	/**
	 * 消息发送（默认立即发送）.
	 * 
	 * @param system
	 *            来自 system 的消息.
	 * @param type
	 *            消息类型.
	 * @param userId
	 *            消息接受方.
	 * @param message
	 *            消息内容.
	 * @param modifyUser
	 *            消息创建人.
	 * @return
	 */
	BooleanResult sendNotify(String system, String type, String userId, String message, String modifyUser);

	/**
	 * 消息发送.
	 * 
	 * @param system
	 *            来自 system 的消息.
	 * @param type
	 *            消息类型.
	 * @param userId
	 *            消息接受方.
	 * @param message
	 *            消息内容.
	 * @param sendDate
	 *            指定消息发送时间 yyyy-MM-dd HH:mm:ss if 为空 then 立即发送.
	 * @param modifyUser
	 *            消息创建人.
	 * @return
	 */
	BooleanResult sendNotify(String system, String type, String userId, String message, String sendDate,
		String modifyUser);

	/**
	 * 通知消息已读.
	 * 
	 * @param id
	 *            通知消息 id.
	 * @param userId
	 *            已读方.
	 * @return
	 */
	BooleanResult review(String[] id, String userId);

}
