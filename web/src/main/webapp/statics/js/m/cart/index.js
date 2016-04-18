$(function() {
			$("img.lazy").lazyload({
						threshold : 200,
						effect : "fadeIn"
					});

			$('input').iCheck({
						checkboxClass : 'iradio_square-blue'
					});
			$('input').on('ifToggled', function(event) {
						check();
					});
		});

$(document).ready(function() {
			$('#hideFrame').bind('load', promgtMsg);
		});

var checkType = true;
var sync = true;

function checkAll() {
	sync = false;

	var total = 0;
	var points = 0;
	var count = 0;

	$("input[name='cartId']").each(function() {
		if (checkType) {
			$(this).iCheck('check');

			total = dcmAdd(total, dcmMul($("#price" + this.value).val(),
							$("#quantity" + this.value).val()));
			points = dcmAdd(points, dcmMul($("#points" + this.value).val(),
							$("#quantity" + this.value).val()));
			count++;
		} else {
			$(this).iCheck('uncheck');
		}
	});

	if (checkType && count != 0) {
		$("#total").html(total);
		$("#points").html(points);
		$("#count").html("(" + count + ")");
		$("#count1").html("(" + count + ")");
	} else {
		$("#total").html(0);
		$("#points").html(0);
		$("#count").html("");
		$("#count1").html("");
	}

	checkType = !checkType;

	sync = true;
}

function check() {
	if (!sync) {
		return;
	}

	var total = 0;
	var points = 0;
	var count = 0;

	$("input[name='cartId']").each(function() {
		if (this.checked) {
			total = dcmAdd(total, dcmMul($("#price" + this.value).val(),
							$("#quantity" + this.value).val()));
			points = dcmAdd(points, dcmMul($("#points" + this.value).val(),
							$("#quantity" + this.value).val()));
			count++;
		}
	});

	if (count == 0) {
		$("#total").html(0);
		$("#points").html(0);
		$("#count").html("");
		$("#count1").html("");
	} else {
		$("#total").html(total);
		$("#points").html(points);
		$("#count").html("(" + count + ")");
		$("#count1").html("(" + count + ")");
	}
}

function confirm() {
	$('#btn1').button('loading');

	var form = window.document.forms[0];

	if (form == undefined) {
		$('#btn1').button('reset');
		return;
	}

	form.action = appUrl + "/trade/tradeAction!createTrade.htm";
	form.submit();
}

function edit(editype) {
	if (editype == 'n') {
		$("#edit2cancel").hide();
		$("#cancel").hide();
		$("#edit2topay").show();
		$("#topay").show();

		$('input').iCheck({
					checkboxClass : 'iradio_square-blue'
				});
		$('input').on('ifToggled', function(event) {
					check();
				});
	} else if (editype == 'y') {
		$("#edit2topay").hide();
		$("#topay").hide();
		$("#edit2cancel").show();
		$("#cancel").show();

		$('input').iCheck({
					checkboxClass : 'iradio_square-red'
				});
		$('input').on('ifToggled', function(event) {
					check();
				});
	}
}

function cancel() {
	$('#btn2').button('loading');

	var form = window.document.forms[0];

	if (form == undefined) {
		$('#btn2').button('reset');
		return;
	}

	form.action = appUrl + "/cart/cartAction!removeCart.htm";
	form.target = "hideFrame";
	form.submit();
}

function promgtMsg() {
	var hideFrame = document.getElementById("hideFrame");
	var failResult = hideFrame.contentWindow.failResult;
	var successResult = hideFrame.contentWindow.successResult;
	if (failResult != undefined && failResult != "") {
		error(failResult);
		$('#btn2').button('reset');
	} else if (successResult != undefined) {
		top.location.href = appUrl + "/cart/index.htm";
	}
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