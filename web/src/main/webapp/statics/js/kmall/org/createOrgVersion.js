Ext.onReady(function() {

			Ext.QuickTips.init();

			new Ext.form.TextField({
						id : "verName",
						allowBlank : false,
						applyTo : 'verName'
					});

			new Ext.form.TextArea({
						id : "remark",
						width : 300,
						grow : true,
						applyTo : 'remark'
					});

			$('#hideFrame').bind('load', promgtMsg);
		});

function save() {
	var n = Ext.getCmp("verName").validate();

	if (!n) {
		return;
	}

	var form = window.document.forms[0];
	form.action = appUrl + "/org/orgVersionAction!createOrgVersion.htm";
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
					msg : "创建成功，第二步：维护组织调整步骤！",
					buttons : Ext.Msg.OK,
					fn : function(btn) {
						if (btn == 'ok') {
							window.parent.opener.select(successResult,
									$('#verName').val(), 'N');
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