$(document).ready(function() {

	specCat = (specCat != '') ? jQuery.parseJSON(specCat) : '';
	specItem = (specItem != '') ? jQuery.parseJSON(specItem) : '';

	$.each(specItem, function() {
				$("#specItemValue" + this.specCId).append(this.specItemValue
						+ " ");

				$("#specItem" + this.specCId)
						.append("<span class='unchecked' name='span"
								+ this.specCId + "' checked='false' id='"
								+ this.specItemId
								+ "' onclick='change(this);'>"
								+ this.specItemValue + "</span> ");
			});

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

function addCart() {
	if ($("#checkSkuId").val() == "true" && $("#skuId").val() == "") {
		error("请选择需要购买商品的规格！");
		return;
	}

	if ($("#text").val() == "") {
		error("请选择需要购买商品的数量！");
		return;
	}

	$('#btn').button('loading');

	$.ajax({
				type : "post",
				url : appUrl + "/cart/addCart.htm",
				data : {
					"itemId" : $("#itemId").val(),
					"skuId" : $("#skuId").val(),
					"quantity" : $("#text").val(),
					dateTime : new Date().getTime()
				},
				success : function(data) {
					if (flag == '0') {
						success(data);
						$('#btn').button('reset');
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

					error(data.responseJSON);
					$('#btn').button('reset');
				}
			});
}

function change(span) {
	$('span[name="' + $(span).attr('name') + '"]').each(function() {
				if (this.checked && this != span) {
					this.className = "unchecked";
					this.checked = false;
				}
			});
	span.className = "checked";
	span.checked = true;

	select();
}

function select() {
	var properties = "";
	$.each(specCat, function() {
				var specCId = this.specCId;
				$('span[name="span' + specCId + '"]').each(function() {
							if (this.checked) {
								if (properties != "") {
									properties += ";";
								}
								properties += specCId + ":" + this.id;
							}
						});
			});

	try {
		var o = document.getElementById("price" + properties);
		if (o == null) {
			$("#price").html("暂无此规格商品");
			$("#stock").html("");
		} else {
			$("#price").html(o.value);
			$("#stock").html("库存："
					+ document.getElementById("stock" + properties).value);

			$("#skuId").val(document.getElementById("sku" + properties).value);
			$("#origin")
					.html(document.getElementById("origin" + properties).value);
		}
	} catch (e) {
	}
}

// 立即购买 or 加入购物车
var flag = '0';

function next(type) {
	flag = type;

	$("#step2").fadeIn();
}

function cancel() {
	$("#step2").fadeOut();
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