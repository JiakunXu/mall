Ext.onReady(function() {
			// tips
			Ext.QuickTips.init();

			new Ext.form.TextField({
						id : "dictName",
						allowBlank : false,
						applyTo : 'dictName'
					});

			new Ext.form.TextField({
						id : "dictValue",
						allowBlank : false,
						applyTo : 'dictValue'
					});

			$('#hideFrame').bind('load', promgtMsg);

		});

var type = 0;

function save(n) {
	type = n;

	if ($("#dictTypeId").val() == "") {
		warn("请先选择系统参数类型并点击【查看系统参数】！");
		return;
	}

	var p = Ext.getCmp("dictName").validate();
	var n = Ext.getCmp("dictValue").validate();

	if (!(p && n)) {
		return;
	}

	var form = window.document.forms[0];
	form.action = appUrl + "/dict/dictAction!createDict.htm";
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
							window.opener.searchDicts();

							if (type == 0) {
								window.close();
							} else {
								$("#dictName").val('');
								$("#dictValue").val('');
								$("#remark").val('');
							}
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