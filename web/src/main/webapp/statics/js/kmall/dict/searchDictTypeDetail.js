Ext.onReady(function() {
			// tips
			Ext.QuickTips.init();

			new Ext.form.TextField({
						id : "dictTypeName",
						allowBlank : false,
						applyTo : 'dictTypeName'
					});

			new Ext.form.TextField({
						id : "dictTypeValue",
						allowBlank : false,
						applyTo : 'dictTypeValue'
					});

			$('#hideFrame').bind('load', promgtMsg);
		});

function save() {
	var p = Ext.getCmp("dictTypeName").validate();
	var n = Ext.getCmp("dictTypeValue").validate();

	if (!(p && n)) {
		return;
	}

	var form = window.document.forms[0];
	form.action = appUrl + "/dict/dictAction!updateDictType.htm";
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
							window.opener.storeReload();
							window.close();
						}
					},
					icon : Ext.Msg.INFO
				});
	}
}
