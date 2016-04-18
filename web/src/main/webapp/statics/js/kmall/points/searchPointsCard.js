$(document).ready(function() {

			var charts = echarts.init(document.getElementById('chart'), theme);

			charts.showLoading({
						text : '正在努力的读取数据中...'
					});

			charts.hideLoading();

			option = {
				title : {
					text : '积分卡',
					x : 'center'
				},
				tooltip : {
					trigger : 'item',
					formatter : "{a} <br/>{b} : {c} ({d}%)"
				},
				calculable : true,
				series : [{
							name : '积分卡数量',
							type : 'pie',
							radius : '50%',
							center : ['50%', '60%'],
							data : [{
										value : countOfNotUsed,
										name : '未兑换积分卡'
									}, {
										value : countOfUsed,
										name : '已兑换积分卡'
									}]
						}]
			};

			charts.setOption(option);

		});

Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	$("#isUsed").val(isUsed == '' ? 'Y' : isUsed);

	var Item = Ext.data.Record.create([{
				name : 'id',
				type : 'string'
			}, {
				name : 'cardNo',
				type : 'string'
			}, {
				name : 'password',
				type : 'string'
			}, {
				name : 'points',
				type : 'number'
			}, {
				name : 'expireDate',
				type : 'string'
			}, {
				name : 'userName',
				type : 'string'
			}, {
				name : 'isUsed',
				type : 'string'
			}, {
				name : 'expire',
				type : 'string'
			}]);

	store = new Ext.data.Store({
				url : appUrl
						+ '/points/pointsCardAction!getPointsCardJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'id'
						}, Item),
				remoteSort : true
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "积分卡",
		dataIndex : 'cardNo',
		width : 50,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "密码",
		dataIndex : 'password',
		width : 50,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "积分值",
		dataIndex : 'points',
		width : 50,
		sortable : false,
		align : 'right',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "兑换活动到期时间",
		dataIndex : 'expireDate',
		width : 50,
		sortable : false,
		align : 'center',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			var isUsed = record.get("isUsed");
			var expire = record.get("expire");
			var expireDate = record.get("expireDate");

			if (isUsed == 'N' && expire == 'Y') {
				return "<p style='color:red'>"
						+ Ext.util.Format.htmlEncode(expireDate) + "</p>";
			} else {
				return Ext.util.Format.htmlEncode(expireDate);
			}
		}
	}, {
		header : "是否充值",
		dataIndex : 'isUsed',
		width : 50,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			if (v == 'Y') {
				return "已充值";
			} else {
				return "未充值";
			}
		}
	}, {
		header : "充值人",
		dataIndex : 'userName',
		width : 50,
		sortable : false,
		align : 'center',
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

	store.setDefaultSort('modifyDate', 'DESC');
	search();

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});

	new Ext.form.NumberField({
				id : "points",
				allowBlank : false,
				applyTo : 'points'
			});

	new Ext.form.NumberField({
				id : "quantity",
				minValue : 1,
				allowBlank : false,
				applyTo : 'quantity'
			});

	new Ext.form.DateField({
				id : "expireDate",
				width : 143,
				format : 'Y-m-d',
				minValue : gmtStart,
				allowBlank : false,
				editable : false,
				applyTo : 'expireDate'
			});

	$('#hideFrame').bind('load', promgtMsg);
});

function search() {
	store.baseParams.cardNo = encodeURIComponent($('#cardNo').val());
	store.baseParams.userName = encodeURIComponent($('#userName').val());
	store.baseParams.isUsed = encodeURIComponent($('#isUsed').val());

	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function resets() {
	$('#cardNo').val('');
	$('#userName').val('');
}

function reload() {
	store.reload();
}

function createPointsCard() {
	var p = Ext.getCmp("points").validate();
	var q = Ext.getCmp("quantity").validate();
	var e = Ext.getCmp("expireDate").validate();

	if (!(p && q && e)) {
		return;
	}

	$('#btn').button('loading');

	var form = window.document.forms[1];
	form.action = appUrl + "/points/pointsCardAction!createPointsCard.htm";
	form.target = "hideFrame";
	form.submit();
}

function exportPointsCardData() {
	var form = window.document.forms[0];
	form.action = appUrl + "/points/pointsCardAction!exportPointsCardData.htm";
	form.target = "_blank";
	form.submit();
}

function promgtMsg() {
	var hideFrame = document.getElementById("hideFrame");
	var failResult = hideFrame.contentWindow.failResult;
	var successResult = hideFrame.contentWindow.successResult;
	if (failResult != undefined && failResult != "") {
		error(failResult);
	} else if (successResult != undefined) {
		$('#createPointsCard').modal('toggle');
		success(successResult);
		reload();
	}

	$('#btn').button('reset');
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
