Ext.onReady(function() {
			// tips
			Ext.QuickTips.init();
		});

function sendCheckCode() {
	if ($("#passport").val() == "") {
		warn("请输入登录账号！");
		return;
	}

	$('#btn').button('loading');

	var form = window.document.forms[0];
	form.action = appUrl + "/account/sendCheckCode.htm";
	form.submit();
}

function warn(msg) {
	Ext.Msg.show({
				title : '警告',
				msg : msg,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.WARNING
			});
}

document.onkeydown = function(e) {
	var theEvent = e || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		sendCheckCode();
		return false;
	}
	return true;
}