$(function() {
			$("img.lazy").lazyload({
						threshold : 200,
						effect : "fadeIn"
					});
		});

function cancel(tradeId, tradeNo) {
	Messenger.options = {
		extraClasses : 'messenger-fixed messenger-on-top',
		theme : 'air'
	}
	Messenger().hideAll();
	Messenger().post({
		message : "确认取消订单" + tradeNo + "？",
		type : 'info',
		actions : {
			retry : {
				label : '是',
				action : function() {
					Messenger().hideAll();
					$('#cancel').button('loading');

					$.ajax({
								type : "post",
								url : appUrl
										+ "/trade/tradeAction!cancelMyTrade.htm",
								data : {
									"tradeId" : tradeId,
									dateTime : new Date().getTime()
								},
								success : function(data) {
									top.location.href = appUrl
											+ "/trade/searchTrade.htm?type=pay";
								},
								error : function(data) {
									$('#cancel').button('reset');

									error(data.responseJSON);
								}
							});
				}
			},
			cancel : {
				label : '否',
				phrase : 'Retrying TIME',
				auto : true,
				delay : 10,
				action : function() {
					return this.cancel();
				}
			}
		}
	});
}

function signTrade(tradeId, tradeNo) {
	Messenger.options = {
		extraClasses : 'messenger-fixed messenger-on-top',
		theme : 'air'
	}
	Messenger().hideAll();
	Messenger().post({
		message : "确认签收订单" + tradeNo + "？",
		type : 'info',
		actions : {
			retry : {
				label : '是',
				action : function() {
					Messenger().hideAll();
					$('#sign').button('loading');

					$.ajax({
								type : "post",
								url : appUrl
										+ "/trade/tradeAction!signTrade.htm",
								data : {
									"tradeId" : tradeId,
									dateTime : new Date().getTime()
								},
								success : function(data) {
									top.location.href = appUrl
											+ "/trade/searchTrade.htm?type=paid";
								},
								error : function(data) {
									$('#sign').button('reset');

									error(data.responseJSON);
								}
							});
				}
			},
			cancel : {
				label : '否',
				phrase : 'Retrying TIME',
				auto : true,
				delay : 10,
				action : function() {
					return this.cancel();
				}
			}
		}
	});
}

function feedbackTrade(tradeId, tradeNo) {
	Messenger.options = {
		extraClasses : 'messenger-fixed messenger-on-top',
		theme : 'air'
	}
	Messenger().hideAll();
	Messenger().post({
		message : "确认维权订单" + tradeNo + "？",
		type : 'info',
		actions : {
			retry : {
				label : '是',
				action : function() {
					Messenger().hideAll();
					$('#feedback').button('loading');

					$.ajax({
								type : "post",
								url : appUrl
										+ "/trade/tradeAction!feedbackTrade.htm",
								data : {
									"tradeId" : tradeId,
									dateTime : new Date().getTime()
								},
								success : function(data) {
									top.location.href = appUrl
											+ "/trade/searchTrade.htm?type=paid";
								},
								error : function(data) {
									$('#sign').button('reset');

									error(data.responseJSON);
								}
							});
				}
			},
			cancel : {
				label : '否',
				phrase : 'Retrying TIME',
				auto : true,
				delay : 10,
				action : function() {
					return this.cancel();
				}
			}
		}
	});
}

function searchTradeExpress(tradeId) {
	$.ajax({
				type : "post",
				url : appUrl + "/trade/tradeAction!searchTradeExpress.htm",
				data : {
					"tradeId" : tradeId,
					dateTime : new Date().getTime()
				},
				success : function(data) {
					top.location.href = data;
				},
				error : function(data) {
					error(data.responseJSON);
				}
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
