package com.kintiger.mall.sms.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.api.sms.ISMSService;
import com.kintiger.mall.api.sms.bo.SMS;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.sms.dao.ISMSDao;

/**
 * SMS.
 * 
 * @author xujiakuin
 * 
 */
public class SMSServiceImpl implements ISMSService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(SMSServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private ISMSDao smsDao;

	@Override
	public BooleanResult send(String sender, String receiver, String content, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		final SMS sms = new SMS();

		if (StringUtils.isBlank(sender)) {
			result.setCode("短信发送方信息不能为空！");
			return result;
		}

		sms.setSender(sender.trim());

		if (StringUtils.isBlank(receiver)) {
			result.setCode("短信接受方信息不能为空！");
			return result;
		}

		sms.setReceiver(receiver.trim());

		if (StringUtils.isBlank(content)) {
			result.setCode("短信内容信息不能为空！");
			return result;
		}

		sms.setContent(content.trim());

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		sms.setModifyUser(modifyUser);

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = new BooleanResult();
				result.setResult(false);

				// 1. 短信记录
				try {
					smsDao.createSMS(sms);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(sms), e);
					ts.setRollbackOnly();

					result.setCode("短信记录写入失败！");
					return result;
				}

				// 2. 短信发送
				if (!send(sms.getReceiver(), sms.getContent())) {
					ts.setRollbackOnly();

					result.setCode("短信发送失败！");
					return result;
				}

				result.setCode("短信发送成功！");
				result.setResult(true);
				return result;
			}
		});

		return res;
	}

	private boolean send(String receiver, String content) {
		JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
		Client client = factory.createClient("");

		Object[] res = new Object[2];
		res[0] = receiver;
		res[1] = content;

		try {
			Object[] results = client.invoke("senddx", res);
			if ("ok".equals(results[0])) {
				return true;
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return false;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public ISMSDao getSmsDao() {
		return smsDao;
	}

	public void setSmsDao(ISMSDao smsDao) {
		this.smsDao = smsDao;
	}

}
