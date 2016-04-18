Ext.onReady(function() {
			// tips
			Ext.QuickTips.init();

			$('#hideFrame').bind('load', promgtMsg);
		});

function updatePassword() {
	var pwd = $('#J_Pwd').val();
	var repwd = $('#J_RePwd').val();
	if (!pwd || !repwd) {
		warn("请输入密码！");
		return;
	}

	if (pwd != repwd) {
		warn("两次密码输入不匹配！");
		return;
	}

	if (pwd.length < 6) {
		warn("密码输入长度至少6位！");
		return;
	}

	var form = window.document.forms[0];
	form.action = appUrl + "/account/updatePassword.htm";
	form.target = "hideFrame";
	form.submit();

	Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
}

function promgtMsg() {
	var hideFrame = document.getElementById("hideFrame");
	var failResult = hideFrame.contentWindow.failResult;
	var successResult = hideFrame.contentWindow.successResult;
	if (failResult != undefined && failResult != "") {
		Ext.Msg.show({
					title : '错误',
					msg : failResult,
					buttons : Ext.Msg.OK,
					fn : function(btn) {
						if (btn == 'ok') {
						}
					},
					icon : Ext.Msg.ERROR
				});
	} else if (successResult != undefined) {
		Ext.Msg.show({
					title : '信息',
					msg : successResult,
					buttons : Ext.Msg.OK,
					fn : function(btn) {
						if (btn == 'ok') {
							var form = window.document.forms[0];
							form.action = appUrl;
							form.target = "_self";
							form.submit();
						}
					},
					icon : Ext.Msg.INFO
				});
	}
}

function warn(msg) {
	Ext.Msg.show({
				title : '警告',
				msg : msg,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.WARNING
			});
}
