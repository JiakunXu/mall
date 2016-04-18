$(function() {
			$("img.lazy").lazyload({
						threshold : 200,
						effect : "fadeIn"
					});
		});

$(document).ready(function() {
			var e = {
				provinceId : "province",
				cityId : "city",
				countyId : "area",
				dfCode : backCode
			};
			$.fn.cityTools(e);
		});

function next() {
	$('#next').button('loading');

	$.ajax({
				type : "post",
				url : appUrl + "/user/userAddressAction!createUserAddress.htm",
				data : {
					"userAddress.contactName" : $("#contactName").val(),
					"userAddress.province" : $("#province").val(),
					"userAddress.city" : $("#city").val(),
					"userAddress.area" : $("#area").val(),
					"userAddress.backCode" : $("#backCode").val(),
					"userAddress.address" : $("#address").val(),
					"userAddress.postalCode" : $("#postalCode").val(),
					"userAddress.tel" : $("#tel").val(),
					"tradeId" : tradeId,
					dateTime : new Date().getTime()
				},
				success : function(data) {
					address();
					cancel();

					$('#next').button('reset');
				},
				error : function(data) {
					$('#next').button('reset');

					error(data.responseJSON);
				}
			});
}

function pay(type) {
	$('#' + type).button('loading');

	$.ajax({
		type : "post",
		url : appUrl + "/trade/tradeAction!payTrade.htm",
		data : {
			"tradeId" : tradeId,
			"payType" : type == "pay" ? "" : type,
			dateTime : new Date().getTime()
		},
		success : function(data) {
			if (type == 'alipay') {
				$("#3rd_pay").html(data);
			} else if (type == 'wxap') {
				getBrandWCPayRequest(data, type);
			} else {
				top.location.href = appUrl + "/trade/searchTrade.htm?type=paid";
			}
		},
		error : function(data) {
			$('#' + type).button('reset');
			error(data.responseJSON);
		}
	});
}

function getBrandWCPayRequest(data, type) {
	var obj = JSON.parse(data);

	try {
		$('#' + type).button('reset');
		WeixinJSBridge.invoke('getBrandWCPayRequest', {
					"appId" : obj.appId,
					"timeStamp" : obj.timeStamp,
					"nonceStr" : obj.nonceStr,
					"package" : obj.packageValue,
					"signType" : "SHA1",
					"paySign" : obj.paySign
				}, function(res) {
					WeixinJSBridge.log(res.err_msg);

					if (res.err_msg == 'get_brand_wcpay_request:ok') {
						top.location.href = appUrl
								+ "/trade/searchTrade.htm?type=paid";
					} else if (res.err_msg == 'get_brand_wcpay_request:fail') {
						alert(res.err_code + res.err_desc + res.err_msg);
					}
				});
	} catch (e) {
		alert(e);
	}
}

function address() {
	$("#addr").html("<strong>" + $("#contactName").val() + " "
			+ $("#tel").val() + "</strong><br />" + $("#province").val() + " "
			+ $("#city").val() + " " + $("#area").val() + "<br />"
			+ $("#address").val() + " " + $("#postalCode").val() + "<br />");
}

function edit() {
	$("#step1").show();
	$("#step2").hide();
}

function cancel() {
	$("#step1").hide();
	$("#step2").show();
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
