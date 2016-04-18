function save() {
	Messenger().hideAll();
	$('#btn').button('loading');

	$.ajax({
				type : "post",
				url : appUrl + "/points/pointsAction!recharge.htm",
				data : {
					"cardNo" : $("#cardNo").val(),
					"password" : $("#password").val(),
					dateTime : new Date().getTime()
				},
				success : function(data) {
					success(data);
					$('#cardNo').val('');
					$('#password').val('');
					$('#btn').button('reset');
				},
				error : function(data) {
					if ("login" == data.responseJSON) {
						top.location.href = appUrl + "/mindex.htm?uuid=" + uuid
								+ "&goto="
								+ encodeURIComponent(top.location.href);
						return;
					}

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