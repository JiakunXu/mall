Ext.onReady(function() {
			// tips
			Ext.QuickTips.init();

			new Ext.form.TextField({
						id : "orgName",
						allowBlank : false,
						readOnly : true,
						applyTo : 'orgName'
					});

			$('#hideFrame').bind('load', promgtMsg);
		});

function updateUser() {
	var u = Ext.getCmp("orgName").validate();

	if (!(u)) {
		return;
	}

	var form = window.document.forms[0];
	form.action = "userAction!updateUserOrg.htm";
	form.target = "hideFrame";
	form.submit();

	Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
}

function getOrgTree() {
	var form = window.document.forms[0];

	var URL = appUrl + "/org/orgTreeAction!getOrgAllTree.htm";
	var windowhight = 900;
	var windowwidth = 500;
	var top = (window.screen.availHeight - windowhight) / 2;
	var left = (window.screen.availWidth - windowwidth) / 2;
	var x = window.showModalDialog(URL, form,
			"dialogWidth=300px;dialogHeight=400px;");

	if (x == undefined) {
	} else {
		document.all.orgId.value = x[0];
		document.all.orgName.value = x[1];
	}

	Ext.getCmp("orgName").validate();
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
							window.parent.opener.reload();
							window.close();
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