var __last_login_passport__ = "__last_login_passport__";

Ext.onReady(function() {
			// tips
			Ext.QuickTips.init();

			new Ext.form.NumberField({
						id : "mobile",
						allowBlank : false,
						regex : /(^0?[1][3|4|5|7|8][0-9]{9}$)/,
						regexText : '请输入正确的手机号码',
						applyTo : 'mobile'
					});

			new Ext.form.TextField({
						id : "J_Pwd",
						allowBlank : false,
						applyTo : 'J_Pwd'
					});

			new Ext.form.TextField({
						id : "J_RePwd",
						allowBlank : false,
						applyTo : 'J_RePwd'
					});

			new Ext.form.TextField({
						id : "userName",
						allowBlank : true,
						applyTo : 'userName'
					});

			$('#hideFrame').bind('load', promgtMsg);
		});

function save() {
	var p = Ext.getCmp("mobile").validate();
	var n = Ext.getCmp("J_Pwd").validate();
	var c = Ext.getCmp("J_RePwd").validate();

	if (!(p && n && c)) {
		if (!p) {
			warn("手机号码不能为空或不正确！");
		}

		if (!n) {
			warn("密码不能为空！");
		}

		if (!c) {
			warn("确认密码不能为空！");
		}

		return;
	}

	var pwd = $('#J_Pwd').val();
	var repwd = $('#J_RePwd').val();

	if (pwd != repwd) {
		warn("两次密码输入不匹配！");
		return;
	}

	if (pwd.length < 6) {
		warn("密码输入长度至少6位！");
		return;
	}

	var form = window.document.forms[0];
	form.action = appUrl + "/register/registerAction!register.htm";
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
							// 写cookie
							var passport = $("#mobile").val();

							$.cookie(__last_login_passport__, passport, {
										expires : 30,
										path : '/' + appName + '/',
										domain : domain
									});

							top.location.href = appUrl;
						}
					},
					icon : Ext.Msg.INFO
				});
	}
}

function warn(text) {
	new PNotify({
				title : '警告',
				text : text
			});
}