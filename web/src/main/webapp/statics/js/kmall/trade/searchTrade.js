Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	var Item = Ext.data.Record.create([{
				name : 'tradeId',
				type : 'string'
			}, {
				name : 'tradeNo',
				type : 'string'
			}, {
				name : 'price',
				type : 'number'
			}, {
				name : 'postage',
				type : 'number'
			}, {
				name : 'receiverName',
				type : 'string'
			}, {
				name : 'receiverTel',
				type : 'string'
			}, {
				name : 'createDate',
				type : 'string'
			}, {
				name : 'modifyDate',
				type : 'string'
			}, {
				name : 'payDate',
				type : 'string'
			}, {
				name : 'sendDate',
				type : 'string'
			}, {
				name : 'signDate',
				type : 'string'
			}, {
				name : 'feedbackType',
				type : 'string'
			}, {
				name : 'feedbackDate',
				type : 'string'
			}, {
				name : 'feedbackedDate',
				type : 'string'
			}, {
				name : 'type',
				type : 'string'
			}, {
				name : 'score',
				type : 'number'
			}, {
				name : 'userId',
				type : 'string'
			}]);

	store = new Ext.data.Store({
				url : appUrl + '/trade/tradeAction!getTradeJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'tradeId'
						}, Item),
				remoteSort : true,
				listeners : {
					load : function() {
						star();
					},
					scope : this
				}
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : '',
		width : 100,
		sortable : false,
		align : 'center',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			return "<div id='" + rowIndex + "' />";
		}
	}, {
		header : "订单号",
		dataIndex : 'tradeNo',
		width : 80,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "买家",
		width : 120,
		sortable : false,
		align : 'left',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			return Ext.util.Format.htmlEncode(record.get('receiverName') + "（"
					+ record.get('receiverTel') + "）");
		}
	}, {
		header : "下单时间",
		dataIndex : 'createDate',
		width : 90,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "最近修改时间",
		dataIndex : 'modifyDate',
		width : 90,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "付款时间",
		dataIndex : 'payDate',
		width : 90,
		hidden : true,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "发货时间",
		dataIndex : 'sendDate',
		width : 90,
		hidden : true,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "签收时间",
		dataIndex : 'signDate',
		width : 90,
		hidden : true,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "关闭时间",
		dataIndex : 'modifyDate',
		width : 90,
		hidden : true,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "维权时间",
		dataIndex : 'feedbackDate',
		width : 90,
		hidden : true,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "处理时间",
		dataIndex : 'feedbackedDate',
		width : 90,
		hidden : true,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "订单状态",
		dataIndex : 'type',
		width : 50,
		sortable : false,
		align : 'center',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			if (value == 'topay') {
				return "<p>待付款</p>";
			} else if (value == 'tosend') {
				return "<p>待发货</p>";
			} else if (value == 'send') {
				return "<p>已发货</p>";
			} else if (value == 'sign') {
				return "<p>标记签收</p>";
			} else if (value == 'cancel') {
				return "<p>已关闭</p>";
			} else if (value == 'check') {
				return "<p>临时单</p>";
			} else if (value == 'feedback') {
				if (record.get('feedbackType') == 'tosend') {
					return "<p>申请退款</p>";
				} else if (record.get('feedbackType') == 'send') {
					return "<p>申请退货</p>";
				}
			} else if (value == 'feedbacked') {
				return "<p>已处理</p>";
			}
		}
	}, {
		header : "实付金额￥",
		width : 100,
		sortable : false,
		align : 'center',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			return Ext.util.Format.htmlEncode(Ext.util.Format.number(record
							.get('price'), '0,000.00')
					+ "（含运费："
					+ Ext.util.Format.number(record.get('postage'), '0,000.00')
					+ "）");
		}
	}, {
		header : "操作",
		width : 100,
		sortable : false,
		align : 'center',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			var strReturn = "";

			// 待付款
			if (type == 'topay') {
				strReturn += "<a href=javascript:cancelTrade('" + rowIndex
						+ "') > 取消订单 </a>|";
			}

			// 发货
			if (type == 'tosend') {
				strReturn += "<a href=javascript:sendTradePrepare('" + rowIndex
						+ "') > 发货 </a>|";
			}

			// 处理维权
			if (type == 'feedback') {
				strReturn += "<a href=javascript:validateReturnPoints('"
						+ rowIndex
						+ "') > 校验 </a>|<a href=javascript:feedbackedTrade('"
						+ rowIndex + "') > 处理 </a>|";
			}

			strReturn += "<a href='" + appUrl
					+ "/trade/tradeAction!searchTradeDetail.htm?tradeNo="
					+ record.get('tradeNo')
					+ "#detail' target='_blank' > 详情 </a>";

			return strReturn;
		}
	}]);

	grid = new Ext.grid.EditorGridPanel({
				store : store,
				cm : cm,
				renderTo : 'gridList',
				height : height,
				loadMask : true,
				autoScroll : true,
				enableHdMenu : false,
				viewConfig : {
					forceFit : true
				},
				stripeRows : true,
				columnLines : true,
				plugins : [new Ext.plugins.HeaderAligning()],
				bbar : new Ext.PagingToolbar({
							pageSize : pageSize,
							store : store,
							displayInfo : true,
							displayMsg : '共 {2} 条记录，当前显示 {0} - {1}',
							emptyMsg : "没有找到记录！",
							plugins : [new Ext.ux.plugin.PagingToolbarResizer({
										displayText : '',
										options : [15, 30, 50],
										prependCombo : true
									})]
						})
			});

	if (type == 'check') {
	} else if (type == 'topay') {
	} else if (type == 'tosend') {
		grid.getColumnModel().setHidden(5, true);
		grid.getColumnModel().setHidden(6, false);
	} else if (type == 'send') {
		grid.getColumnModel().setHidden(5, true);
		grid.getColumnModel().setHidden(7, false);
	} else if (type == 'sign') {
		grid.getColumnModel().setHidden(5, true);
		grid.getColumnModel().setHidden(8, false);
	} else if (type == 'cancel') {
		grid.getColumnModel().setHidden(5, true);
		grid.getColumnModel().setHidden(9, false);
	} else if (type == 'feedback') {
		grid.getColumnModel().setHidden(5, true);
		grid.getColumnModel().setHidden(10, false);
	} else if (type == 'feedbacked') {
		grid.getColumnModel().setHidden(5, true);
		grid.getColumnModel().setHidden(11, false);
	}

	store.setDefaultSort('createDate', 'DESC');

	search();

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
				star();
			});

	$(".radioItem").change(function() {
				var $selectedvalue = $("input[name='shipment']:checked").val();
				if ($selectedvalue == 'Y') {
					$("#express-section").show();
				} else {
					$("#express-section").hide();
				}
			});
});

function star() {
	var i = 0;
	store.each(function(record) {
				$('#' + (i++)).raty({
					path : imgUrl + "/jquery.raty/images/",
					cancel : true,
					cancelOff : 'cancel-custom-off.png',
					cancelOn : 'cancel-custom-on.png',
					score : record.get("score"),
					click : function(score, evt) {
						var rowIndex = this.id;
						$.ajax({
									type : "post",
									url : appUrl
											+ "/trade/tradeAction!star.htm",
									data : {
										tradeId : store.getAt(rowIndex)
												.get("tradeId"),
										score : score == null ? 0 : score,
										dateTime : new Date().getTime()
									},
									success : function(data) {
										// 修改 stroe
										var rec = store.getAt(rowIndex);
										rec.set("score", score);
										rec.commit();
										star();
									},
									error : function(data) {
										error(data);
									}
								});
					}
				});
			});
}

function sendTradePrepare(rowIndex) {
	$('#no').html(store.getAt(rowIndex).get("tradeNo"));
	$('#tradeId').val(store.getAt(rowIndex).get("tradeId"));
	$('#epsNo').val("");

	$('#sendTrade').modal('show');
}

function sendTrade() {
	$('#btn').button('loading');

	$.ajax({
				type : "post",
				url : appUrl + "/trade/tradeAction!sendTrade.htm",
				data : {
					tradeId : $('#tradeId').val(),
					shipment : $("input[name='shipment']:checked").val(),
					"tradeExpress.epsId" : $('#epsId').val(),
					"tradeExpress.epsNo" : encodeURIComponent($('#epsNo').val()),
					dateTime : new Date().getTime()
				},
				success : function(data) {
					$('#btn').button('reset');
					$('#sendTrade').modal('toggle');
					success(data);
					reload();
				},
				error : function(data) {
					error(data.responseJSON);
					$('#btn').button('reset');
				}
			});
}

function cancelTrade(rowIndex) {
	(new PNotify({
				title : '请确认',
				text : '取消订单 <span class="small">'
						+ store.getAt(rowIndex).get("tradeNo") + '</span> ？',
				icon : 'glyphicon glyphicon-question-sign',
				hide : false,
				confirm : {
					confirm : true,
					buttons : [{
								text : "确认",
								addClass : "btn-primary"
							}, {
								text : "取消"
							}]
				},
				buttons : {
					closer : false,
					sticker : false
				},
				history : {
					history : false
				}
			})).get().on('pnotify.confirm', function() {
				$.ajax({
							type : "post",
							url : appUrl + "/trade/tradeAction!cancelTrade.htm",
							data : {
								tradeId : store.getAt(rowIndex).get("tradeId"),
								dateTime : new Date().getTime()
							},
							success : function(data) {
								reload();
							},
							error : function(data) {
								error(data.responseJSON);
							}
						});
			});
}

function validateReturnPoints(rowIndex) {
	$.ajax({
				type : "post",
				url : appUrl
						+ "/member/memberPointsAction!validateReturnPoints.htm",
				data : {
					userId : store.getAt(rowIndex).get("userId"),
					tradeId : store.getAt(rowIndex).get("tradeId"),
					dateTime : new Date().getTime()
				},
				success : function(data) {
					success(data);
				},
				error : function(data) {
					error(data.responseJSON);
				}
			});
}

function feedbackedTrade(rowIndex) {
	(new PNotify({
				title : '请确认',
				text : '处理维权订单 <span class="small">'
						+ store.getAt(rowIndex).get("tradeNo") + '</span> ？',
				icon : 'glyphicon glyphicon-question-sign',
				hide : false,
				confirm : {
					confirm : true,
					buttons : [{
								text : "确认",
								addClass : "btn-primary"
							}, {
								text : "取消"
							}]
				},
				buttons : {
					closer : false,
					sticker : false
				},
				history : {
					history : false
				}
			})).get().on('pnotify.confirm', function() {
				$.ajax({
							type : "post",
							url : appUrl
									+ "/trade/tradeAction!feedbackedTrade.htm",
							data : {
								tradeId : store.getAt(rowIndex).get("tradeId"),
								dateTime : new Date().getTime()
							},
							success : function(data) {
								reload();
							},
							error : function(data) {
								error(data.responseJSON);
							}
						});
			});
}

function search() {
	store.baseParams.type = encodeURIComponent(type);
	store.baseParams.shipment = encodeURIComponent(shipment);
	store.baseParams.tradeNo = encodeURIComponent($('#tradeNo').val());
	store.baseParams.receiverName = encodeURIComponent($('#receiverName').val());
	store.baseParams.receiverTel = encodeURIComponent($('#receiverTel').val());

	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function resets() {
	$('#tradeNo').val('');
	$('#receiverName').val('');
	$('#receiverTel').val('');
}

function reload() {
	store.reload();
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

document.onkeydown = function(e) {
	var theEvent = e || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		search();
		return false;
	}
	return true;
}
