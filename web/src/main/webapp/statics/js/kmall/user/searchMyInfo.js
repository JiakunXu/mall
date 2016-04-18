Ext.onReady(function() {
			// tips
			Ext.QuickTips.init();

			new Ext.form.TextField({
						id : "userName",
						allowBlank : false,
						applyTo : 'userName'
					});

			new Ext.form.TextField({
						id : "phone",
						applyTo : 'phone'
					});

			new Ext.form.TextField({
						id : "mobile",
						allowBlank : false,
						applyTo : 'mobile',
						regex : /(^0?[1][3|4|5|7|8][0-9]{9}$)/,
						regexText : '请输入正确的手机号码'
					});

			new Ext.form.TextField({
						id : "email",
						allowBlank : false,
						vtype : "email",
						applyTo : 'email'
					});

			new Ext.form.TextField({
						id : "workFax",
						applyTo : 'workFax'
					});

			new Ext.form.TextField({
						id : "address",
						applyTo : 'address'
					});

			var store2 = new Ext.data.SimpleStore({
						fields : ['itemId', 'itemName'],
						data : [['ZH', '中文'], ['EN', '英文']]
					});

			langu = new Ext.form.ComboBox({
						id : 'langu',
						store : store2,
						mode : 'local',
						displayField : 'itemName',
						valueField : 'itemId',
						hiddenName : 'user.langu',
						editable : false,
						triggerAction : 'all',
						applyTo : 'langu'
					});

			langu.setValue(lan);

			$('#hideFrame').bind('load', promgtMsg);
		});

function updateUser() {
	var e = Ext.getCmp("email").validate();
	var m = Ext.getCmp("mobile").validate();

	if (!(e && m)) {
		return;
	}

	var form = window.document.forms[0];
	form.target = "hideFrame";
	form.submit();

	Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
}

function resetPassword() {
	var WWidth = 700;
	var WHeight = 500;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/loginAction!resetPassword.htm", "winSub", "left="
					+ WLeft + ",top=" + WTop + ",width=" + WWidth + ",height="
					+ WHeight);
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
					icon : Ext.Msg.INFO,
					closable : false
				});

		setTimeout(function() {
					Ext.Msg.hide();
				}, 1000);
	}
}
