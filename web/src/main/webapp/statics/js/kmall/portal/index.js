var __last_login_passport__ = "__last_login_passport__";

function setPassport() {
	var lastuser = $.cookie(__last_login_passport__);

	if (lastuser != "" && lastuser != null) {
		$("#passport").val(lastuser);
		$("#password").focus();
	} else {
		$("#passport").focus();
		$("#passport").select();
	}
}

function setPassportCookies() {
	$.cookie(__last_login_passport__, null);

	var passport = $("#passport").val();

	$.cookie(__last_login_passport__, passport, {
				expires : 30,
				path : '/' + appName + '/',
				domain : domain
			});

	// _gaq.push(['_trackEvent', 'Home', 'Login', passport]);
}

function submit() {
	$('#btn').button('loading');

	setPassportCookies();
	window.document.forms[0].submit();
}

document.onkeydown = function(e) {
	var theEvent = e || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		submit();
		return false;
	}
	return true;
}

function reg() {
	window.document.forms[0].action = "";
	window.document.forms[0].submit();

	// _gaq.push([ '_trackEvent', 'Home', 'Register' ]);
}

function forgetPasswd() {
	var passport = $("#passport").val();

	window.open(appUrl
					+ "/account/forgetPassword.htm"
					+ ((passport == '')
							? ''
							: ('?passport=' + encodeURIComponent(passport))),
			"forgetPassword");
}
