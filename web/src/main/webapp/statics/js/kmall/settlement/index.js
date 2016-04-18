$(document).ready(function() {

			var charts = echarts.init(document.getElementById('chart'), theme);

			charts.showLoading({
						text : '正在努力的读取数据中...'
					});

			charts.hideLoading();

			option = {
				title : {
					text : '订单支付方式',
					x : 'center'
				},
				tooltip : {
					trigger : 'item',
					formatter : "{a} <br/>{b} : {c} ({d}%)"
				},
				legend : {
					x : 'center',
					y : 'bottom',
					data : ['支付宝', '微信支付']
				},
				calculable : true,
				series : [{
							name : '订单数量',
							type : 'pie',
							radius : ['50%', '70%'],
							itemStyle : {
								normal : {
									label : {
										show : false
									},
									labelLine : {
										show : false
									}
								},
								emphasis : {
									label : {
										show : true,
										position : 'center',
										textStyle : {
											fontSize : '20',
											fontWeight : 'bold'
										}
									}
								}
							},
							data : [{
										value : totalAli,
										name : '支付宝'
									}, {
										value : totalWx,
										name : '微信支付'
									}]
						}]
			};

			charts.setOption(option);

		});

Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	$('.input-daterange').datepicker({
				format : "yyyy-mm-dd",
				language : "zh-CN",
				autoclose : true,
				todayHighlight : true
			});

	var Item = Ext.data.Record.create([{
				name : 'createDate',
				type : 'string'
			}, {
				name : 'totalFee',
				type : 'string'
			}, {
				name : 'outTradeNo',
				type : 'string'
			}, {
				name : 'tradeNo',
				type : 'string'
			}]);

	store = new Ext.data.Store({
		url : appUrl + '/settlement/settlementAction!getSettlementJsonList.htm',
		reader : new Ext.data.SimpleJsonReader({
					id : ''
				}, Item),
		remoteSort : true
	});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "时间",
		dataIndex : 'createDate',
		width : 100,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "收入",
		dataIndex : 'totalFee',
		width : 50,
		sortable : false,
		align : 'right',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(Ext.util.Format.number(v,
					'0,000.00'));
		}
	}, {
		header : "订单号",
		dataIndex : 'outTradeNo',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return "<a href='" + appUrl
					+ "/trade/tradeAction!searchTradeDetail.htm?tradeNo=" + v
					+ "#detail' target='_blank' > " + v + " </a>";
		}
	}, {
		header : "交易流水号",
		dataIndex : 'tradeNo',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
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

	search();

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});
});

function search() {
	store.baseParams.gmtStart = $('#gmtStart').val();
	store.baseParams.gmtEnd = $('#gmtEnd').val();
	store.baseParams.payType = payType;

	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function resets() {
	$('#gmtStart').val(gStart);
	$('#gmtEnd').val(gEnd);
}

function reload() {
	store.reload();
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
