$(document).ready(function() {
	$("#profession").val(profession);
	$("#education").val(education);

	$('#birthday-container .input-group.date').datepicker({
				format : "yyyy-mm-dd",
				language : "zh-CN",
				autoclose : true,
				todayHighlight : true
			});

	$('#weddingDay-container .input-group.date').datepicker({
				format : "yyyy-mm-dd",
				language : "zh-CN",
				autoclose : true,
				todayHighlight : true
			});

	$(".radioItem").change(function() {
		var $selectedvalue = $("input[name='memberInfo.maritalStatus']:checked")
				.val();
		if ($selectedvalue == 'Y') {
			$("#wd").show();
		} else {
			$("#wd").hide();
		}
	});

	$('#hideFrame').bind('load', promgtMsg);
});

function save() {
	Messenger().hideAll();

	if (!validate()) {
		retutn;
	}

	$('#btn').button('loading');

	var form = window.document.forms[0];
	form.action = appUrl + "/member/memberInfoAction!updateMemberInfo.htm";
	form.target = "hideFrame";
	form.submit();
}

function promgtMsg() {
	var hideFrame = document.getElementById("hideFrame");
	var failResult = hideFrame.contentWindow.failResult;
	var successResult = hideFrame.contentWindow.successResult;
	if (failResult != undefined && failResult != "") {
		error(failResult);
	} else if (successResult != undefined) {
		success(successResult);
	}

	$('#btn').button('reset');
}

function validate() {
	// 地址 validate
	var address = $('#address').val();

	if (address != "" && address != null && address.length > 50) {
		error("地址输入过长！")
		return false;
	}

	// 邮政编码 validate
	var postalCode = $('#postalCode').val();

	if (postalCode != "" && postalCode != null && !isPostalCode(postalCode)) {
		error("邮政编码输入有误！");
		return false;
	}

	return true;
}

// 校验邮政编码
function isPostalCode(code) {
	var patrn = /^[1-9][0-9]{5}$/;
	if (patrn.test(code)) {
		return true;
	}
	return false;
}

function success(message) {
	Messenger.options = {
		extraClasses : 'messenger-fixed messenger-on-top',
		theme : 'air'
	}
	Messenger().hideAll();
	Messenger().post({
				message : message,
				type : 'success',
				showCloseButton : true
			});
}

function error(message) {
	Messenger.options = {
		extraClasses : 'messenger-fixed messenger-on-top',
		theme : 'air'
	}
	Messenger().hideAll();
	Messenger().post({
				message : message,
				type : 'error',
				showCloseButton : true
			});
}
