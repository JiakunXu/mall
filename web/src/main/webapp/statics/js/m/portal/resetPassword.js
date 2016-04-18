function save() {
	var opwd = $('#O_Pwd').val();

	if (opwd == "") {
		error("原密码不能为空！");
		return;
	}

	var pwd = $('#J_Pwd').val();

	if (pwd == "") {
		error("新密码不能为空！");
		return;
	}

	if (pwd.length < 6) {
		error("密码输入长度至少6位！");
		return;
	}

	$('#btn').button('loading');

	$.ajax({
				type : "post",
				url : appUrl + "/account/mobileAccountAction!renewPassword.htm",
				data : {
					"oldPassword" : $("#O_Pwd").val(),
					"password" : $("#J_Pwd").val(),
					dateTime : new Date().getTime()
				},
				success : function(data) {
					success(data);
					$('#btn').button('reset');
				},
				error : function(data) {
					error(data.responseJSON);
					$('#btn').button('reset');
				}
			});
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