function updateAccount() {
	$('#btn0').button('loading');

	$.ajax({
				type : "post",
				url : appUrl + "/account/accountAction!updateAccount.htm",
				data : {
					"user.userName" : $("#globalUserName").val(),
					"user.email" : $("#globalEmail").val(),
					dateTime : new Date().getTime()
				},
				success : function(data) {
					success(data);
					$('#btn0').button('reset');

					$('#uname').html($("#userName").val());
				},
				error : function(data) {
					$('#btn0').button('reset');

					error(data.responseJSON);
				}
			});
}

function resetPassword() {
	var opwd = $('#O_Pwd').val();
	var pwd = $('#J_Pwd').val();
	var repwd = $('#J_RePwd').val();

	if (!opwd) {
		error("请输入原密码！");
		return;
	}

	if (!pwd || !repwd) {
		error("请输入密码！");
		return;
	}

	if (pwd != repwd) {
		error("两次密码输入不匹配！");
		return;
	}

	if (pwd.length < 6) {
		error("密码输入长度至少6位！");
		return;
	}

	$('#btn1').button('loading');

	$.ajax({
				type : "post",
				url : appUrl + "/account/accountAction!resetPassword.htm",
				data : {
					"oldPassword" : opwd,
					"password" : pwd,
					dateTime : new Date().getTime()
				},
				success : function(data) {
					success(data);
					resets();
					$('#btn1').button('reset');
				},
				error : function(data) {
					resets();
					$('#btn1').button('reset');

					error(data.responseJSON);
				}
			});
}

function resets() {
	$('#O_Pwd').val('');
	$('#J_Pwd').val('');
	$('#J_RePwd').val('');
}

function error(text) {
	new PNotify({
				title : '出错啦',
				text : text,
				type : 'error'
			});
}

function success(text) {
	new PNotify({
				text : text,
				type : 'success'
			});
}