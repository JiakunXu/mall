<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.wxap.util.TenpayUtil"%>
<%@ page import="com.wxap.util.MD5Util"%>
<%@ page import="com.wxap.RequestHandler"%>
<%@ page import="com.wxap.ResponseHandler"%>
<%@ page import="com.wxap.client.TenpayHttpClient"%>
<%@ include file="config.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="org.springframework.context.ApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="com.jk.mall.api.trade.ITradeService"%>
<%@ page import="com.wxap.config.WxapConfig"%>
<%
	//---------------------------------------------------------
	//΢��֧��֧��֪ͨ����̨֪ͨ��ʾ���̻����մ��ĵ����п�������
	//---------------------------------------------------------

	//����֧��Ӧ�����

	ResponseHandler resHandler = new ResponseHandler(request, response);
	resHandler.setKey(WxapConfig.PARTNER_KEY);
	// demo error
	// resHandler.setKey(WxapConfig.APP_KEY);
	//�����������
	RequestHandler queryReq = new RequestHandler(null, null);
	queryReq.init();
	if (resHandler.isValidSign() == true) {
		if (resHandler.isWXsign() == true) {
		//�̻�������
		String out_trade_no = resHandler.getParameter("out_trade_no");
		//�Ƹ�ͨ������
		String transaction_id = resHandler
				.getParameter("transaction_id");
		//���,�Է�Ϊ��λ
		String total_fee = resHandler.getParameter("total_fee");
		//�����ʹ���ۿ�ȯ��discount��ֵ��total_fee+discount=ԭ�����total_fee
		String discount = resHandler.getParameter("discount");
		//֧�����
		String trade_state = resHandler.getParameter("trade_state");
		// ҵ����ݴ�����
    	boolean result = false;
		
		//�ж�ǩ����
		if ("0".equals(trade_state)) {
			//------------------------------
			//��ʱ���˴���ҵ��ʼ
			//------------------------------

			//������ݿ��߼�
			//ע�⽻�׵���Ҫ�ظ�����
			//ע���жϷ��ؽ��
			
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
    		ITradeService tradeService = (ITradeService)ctx.getBean("tradeService");
    		result = tradeService.notifyTrade(out_trade_no, resHandler);
    		if (!result) {
    			resHandler.sendToCFT("fail");
    		}
			
			//------------------------------
			//��ʱ���˴���ҵ�����
			//------------------------------

			// System.out.println("success ��̨֪ͨ�ɹ�");
			//��Ƹ�ͨϵͳ���ͳɹ���Ϣ���Ƹ�ͨϵͳ�յ��˽����ٽ��к���֪ͨ
		} else {
			// System.out.println("fail ֧��ʧ��");
		}
		
		if (result) {
			resHandler.sendToCFT("success");	
		}
	} else {//sha1ǩ��ʧ��
		// System.out.println("fail -SHA1 failed");
		resHandler.sendToCFT("fail");
	}
	}else{//MD5ǩ��ʧ��
		// System.out.println("fail -Md5 failed");
}	
%>

