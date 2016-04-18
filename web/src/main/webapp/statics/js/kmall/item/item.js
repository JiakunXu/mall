function next() {
	// $("#step1").hide();
	$("#step1").css("background", "rgba(0, 0, 0, 0.6)");
	$("#step2").show();
}

function cancel() {
	// $("#step1").show();
	$("#step1").css("background", "");
	$("#step2").hide();
}

// 规格选择
var obj = {
	colorSpan : "",
	sizeSpan : ""
};
//计件功能
$(function() {
	var t = $("#text_box");
	$("#add").click(function() {
		//件数不能超过此商品的限购数，和库存
		t.val(parseInt(t.val()) + 1)
		setTotal();
	})
	$("#min").click(function() {
		//件数必须为正值
		if((parseInt(t.val()) - 1) >= 0){
			t.val(parseInt(t.val()) - 1)
			setTotal();
		}	
	})
	function setTotal() {
		$("#total").html((parseInt(t.val()) * 3.95).toFixed(2));
	}
	setTotal();
})

function change(span) {
	$('span[name="' + $(span).attr('name') + '"]').each(function() {
		if (this.checked && this != span) {
			this.className = "unchecked";
			this.checked = false;
		}
	});
	obj[$(span).attr('name')] = span.innerHTML;
	span.className = "checked";
	span.checked = true;
	select();
}

function select() {
	var html = '';
	for ( var i in obj) {
		if (obj[i] != '') {
			html += '<font color=orange>"' + obj[i] + '"</font> 、';
		}
	}
	html = '<b>已选择:</b> ' + html.slice(0, html.length - 1);

	$('#resultSpan').html(html);
}
