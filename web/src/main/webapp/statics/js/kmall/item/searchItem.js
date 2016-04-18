Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	var Item = Ext.data.Record.create([{
				name : 'itemId',
				type : 'string'
			}, {
				name : 'itemNo',
				type : 'string'
			}, {
				name : 'itemName',
				type : 'string'
			}, {
				name : 'stock',
				type : 'int'
			}, {
				name : 'quota',
				type : 'int'
			}, {
				name : 'createDate',
				type : 'date'
			}]);

	store = new Ext.data.Store({
				url : appUrl + '/item/itemAction!getItemJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'itemId'
						}, Item),
				remoteSort : true
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "商品名称",
		dataIndex : 'itemName',
		width : 150,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "货号",
		dataIndex : 'itemNo',
		width : 60,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "库存",
		dataIndex : 'stock',
		width : 60,
		sortable : false,
		align : 'right',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "限购",
		dataIndex : 'quota',
		width : 60,
		sortable : false,
		align : 'right',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "创建时间",
		dataIndex : 'createDate',
		width : 120,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			v = Ext.util.Format.date(v.toUTC(), 'Y-m-d H:i:s');
			p.attr = 'ext:qtip="' + v + '"';
			return v;
		}
	}, {
		header : "操作",
		width : 150,
		sortable : false,
		align : 'center',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			return "<a href='"
					+ appUrl
					+ "/item/itemAction!searchItemDetail.htm?itemId="
					+ record.get('itemId')
					+ "#detail' target='_blank' > 编辑 </a>|<a href=javascript:deleteItem('"
					+ rowIndex
					+ "') > 删除 </a>|<a href='"
					+ appUrl
					+ "/item/item.htm?alias="
					+ alias
					+ "&itemId="
					+ record.get('itemId')
					+ "' target='_blank' > 链接 </a>|<a href=javascript:addPointsMall('"
					+ rowIndex + "') > 添加积分商城 </a>";
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

	store.setDefaultSort('createDate', 'DESC');
	search();

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});

	new Ext.form.NumberField({
				id : "points",
				minValue : 1,
				allowBlank : false,
				applyTo : 'points'
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
	store.baseParams.type = $('#type').val();
	store.baseParams.isDisplay = $('#isDisplay').val();
	store.baseParams.itemName = $('#itemName').val();
	store.baseParams.itemNo = $('#itemNo').val();

	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function deleteItem(rowIndex) {
	(new PNotify({
				title : '请确认',
				text : '删除商品 <span class="small">'
						+ store.getAt(rowIndex).get("itemName") + '</span> ？',
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
		form.action = appUrl + "/item/itemAction!deleteItem.htm?itemId="
				+ store.getAt(rowIndex).get('itemId');
		form.target = "hideFrame";
		form.submit();
	});
}

/**
 * 添加积分商城
 */
function addPointsMall(rowIndex) {
	$('#itemId').val(store.getAt(rowIndex).get("itemId"));
	$('#name').val(store.getAt(rowIndex).get("itemName"));

	$('#addPointsMall').modal('show');
}

flag = true;

function add() {
	var p = Ext.getCmp("points").validate();
	var e = Ext.getCmp("expireDate").validate();

	if (!(p && e)) {
		return;
	}

	$('#btn').button('loading');

	var form = window.document.forms[1];
	form.action = appUrl + "/points/pointsAction!createPoints.htm";
	form.target = "hideFrame";
	form.submit();

	flag = false;
}

function resets() {
	$('#gmtStart').val(gStart);
	$('#gmtEnd').val(gEnd);
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
		if (!flag) {
			$('#addPointsMall').modal('hide');
		}
		success(successResult);
		if (flag) {
			reload();
		}
	}

	if (!flag) {
		$('#btn').button('reset');
	}

	flag = true;
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
