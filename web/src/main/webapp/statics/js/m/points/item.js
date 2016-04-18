$(document).ready(function() {

			var t = $("#text");
			$("#plus").click(function() {
						if (parseInt(t.val()) <= stock - 1) {
							t.val(parseInt(t.val()) + 1)
						}

					})

			$("#minus").click(function() {
						if (parseInt(t.val()) >= 2) {
							t.val(parseInt(t.val()) - 1)
						}
					})

		});

function exchange(flag) {
	if ($("#text").val() == "") {
		error("请选择需要兑换商品的数量！");
		return;
	}

	if (flag == '1') {
		$('#btn1').button('loading');
	} else {
		$('#btn2').button('loading');
	}

	$.ajax({
				type : "post",
				url : appUrl + "/points/exchange.htm",
				data : {
					"pointsId" : $("#pointsId").val(),
					"itemId" : $("#itemId").val(),
					"skuId" : $("#skuId").val(),
					"quantity" : $("#text").val(),
					dateTime : new Date().getTime()
				},
				success : function(data) {
					if (flag == '0') {
						success(data);
						$('#btn2').button('reset');
					} else if (flag == '1') {
						top.location.href = appUrl + "/cart/index.htm";
					}
				},
				error : function(data) {
					if ("login" == data.responseJSON) {
						top.location.href = appUrl + "/mindex.htm?uuid=" + uuid
								+ "&goto="
								+ encodeURIComponent(top.location.href);
						return;
					}

					error(data);
					if (flag == '1') {
						$('#btn1').button('reset');
					} else {
						$('#btn2').button('reset');
					}
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