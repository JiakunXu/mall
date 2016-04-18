Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	var Item = Ext.data.Record.create([{
				name : 'pointsId',
				type : 'string'
			}, {
				name : 'itemName',
				type : 'string'
			}, {
				name : 'propertiesName',
				type : 'string'
			}, {
				name : 'points',
				type : 'number'
			}, {
				name : 'expireDate',
				type : 'string'
			}]);

	store = new Ext.data.Store({
				url : appUrl + '/points/pointsAction!getPointsJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'pointsId'
						}, Item),
				remoteSort : true
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "编号",
		dataIndex : 'pointsId',
		width : 50,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "商品名称",
		dataIndex : 'itemName',
		width : 50,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "规格名称",
		dataIndex : 'propertiesName',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "所需积分",
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
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "操作",
		width : 50,
		sortable : false,
		align : 'center',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			return "<a href=javascript:deletePoints('" + rowIndex
					+ "') > 积分商品下架 </a>";
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

	store.setDefaultSort('pointsId', 'DESC');
	search();

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});

	$('#hideFrame').bind('load', promgtMsg);
});

function search() {
	store.baseParams.itemName = encodeURIComponent($('#itemName').val());

	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function deletePoints(rowIndex) {
	(new PNotify({
				title : '请确认',
				text : '<span class="small">'
						+ store.getAt(rowIndex).get("itemName")
						+ '</span> 积分商品下架 ？',
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

		var form = window.document.forms[0];
		form.action = appUrl
				+ "/points/pointsAction!deletePoints.htm?pointsId="
				+ store.getAt(rowIndex).get('pointsId');
		form.target = "hideFrame";
		form.submit();
	});
}

function resets() {
	$('#itemName').val('');
}

function reload() {
	store.reload();
}

function promgtMsg() {
	var hideFrame = document.getElementById("hideFrame");
	var failResult = hideFrame.contentWindow.failResult;
	var successResult = hideFrame.contentWindow.successResult;
	if (failResult != undefined && failResult != "") {
		error(failResult);
	} else if (successResult != undefined) {
		success(successResult);
		reload();
	}
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
