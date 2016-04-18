var deleteType = 0;

Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	dictTypeNameField = new Ext.form.TextField({
				id : "dictTypeName",
				applyTo : 'dictTypeName'
			});

	dictTypeValueField = new Ext.form.TextField({
				id : "dictTypeValue",
				applyTo : 'dictTypeValue'
			});

	remarkField = new Ext.form.TextField({
				id : "remark",
				applyTo : 'remark'
			});

	var Item = Ext.data.Record.create([{
				name : 'dictTypeId',
				type : 'string'
			}, {
				name : 'dictTypeName',
				type : 'string'
			}, {
				name : 'dictTypeValue',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}, {
				name : 'modifyDate',
				type : 'date'
			}]);

	store = new Ext.data.Store({
				url : appUrl + '/dict/dictAction!getDictTypeJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'dictTypeId'
						}, Item),
				remoteSort : true
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		id : "dictTypeId",
		header : "序号",
		dataIndex : 'dictTypeId',
		width : 40,
		sortable : false,
		align : 'center'
	}, {
		header : "类型名称",
		dataIndex : 'dictTypeName',
		width : 90,
		sortable : true,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "值",
		dataIndex : 'dictTypeValue',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "备注",
		dataIndex : 'remark',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}

	}, {
		header : "修改时间",
		dataIndex : 'modifyDate',
		width : 80,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			v = Ext.util.Format.date(v.toUTC(), 'Y-m-d H:i:s');
			p.attr = 'ext:qtip="' + v + '"';
			return v;
		}
	}, {
		header : "操作",
		sortable : false,
		align : 'center',
		width : 100,
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			var dictTypeId = record.get("dictTypeId");
			return "<a href=javascript:searchDict(" + rowIndex
					+ ");>查看系统参数 </a>|" + "<a href=javascript:modifyDictType("
					+ dictTypeId + ");> 修改 </a>|"
					+ "<a href=javascript:deleteDictType(" + dictTypeId
					+ ");> 删除</a>";
		}
	}]);

	grid = new Ext.grid.EditorGridPanel({
				store : store,
				cm : cm,
				renderTo : 'gridList',
				height : 250,
				loadMask : true,
				enableHdMenu : false,
				stripeRows : true,
				columnLines : true,
				viewConfig : {
					forceFit : true
				},
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

	var dictItem = Ext.data.Record.create([{
				name : 'dictId',
				type : 'string'
			}, {
				name : 'dictName',
				type : 'string'
			}, {
				name : 'dictValue',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}, {
				name : 'modifyDate',
				type : 'date'
			}]);

	dictStore = new Ext.data.Store({
				url : appUrl + '/dict/dictAction!getDictJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'dictId'
						}, dictItem),
				remoteSort : true
			});

	var dictcm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		id : "dictId",
		header : "序号",
		dataIndex : 'dictId',
		width : 40,
		sortable : false,
		align : 'center'
	}, {
		header : "名称",
		dataIndex : 'dictName',
		width : 90,
		sortable : true,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "值",
		dataIndex : 'dictValue',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "备注",
		dataIndex : 'remark',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "修改时间",
		dataIndex : 'modifyDate',
		width : 80,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			v = Ext.util.Format.date(v.toUTC(), 'Y-m-d H:i:s');
			p.attr = 'ext:qtip="' + v + '"';
			return v;
		}
	}, {
		header : "操作",
		sortable : false,
		align : 'center',
		width : 80,
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			var dictId = record.get("dictId");
			return "<a href=javascript:modifyDict(" + dictId + ");>修改 </a>|"
					+ "<a href=javascript:deleteDict(" + dictId + ");> 删除</a>";
		}
	}]);

	dictGrid = new Ext.grid.EditorGridPanel({
				store : dictStore,
				cm : dictcm,
				renderTo : 'dictGridList',
				height : 200,
				loadMask : true,
				enableHdMenu : false,
				viewConfig : {
					forceFit : true
				},
				stripeRows : true,
				columnLines : true,
				plugins : [new Ext.plugins.HeaderAligning()],
				bbar : new Ext.PagingToolbar({
							pageSize : pageSize,
							store : dictStore,
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

	dictStore.setDefaultSort('modifyDate', 'DESC');

	search();

	$('#hideFrame').bind('load', promgtMsg);

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
				dictGrid.getView().refresh();
			});
});

function search() {
	store.baseParams.dictTypeValue = encodeURIComponent(dictTypeValueField
			.getValue());
	store.baseParams.dictTypeName = encodeURIComponent(dictTypeNameField
			.getValue());
	store.baseParams.remark = encodeURIComponent(remarkField.getValue());

	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function resets() {
	$("#dictTypeValue").val("");
	$("#dictTypeName").val("");
	$("#remark").val("");
}

function searchDict(rowIndex) {
	var z = store.getAt(rowIndex).get("dictTypeId");

	dictStore.baseParams.dictTypeId = z;

	document.getElementById("dictTypeIdForDict").value = z;
	document.getElementById("dictTypeNameForDict").value = store
			.getAt(rowIndex).get("dictTypeName");

	dictStore.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function searchDicts() {
	dictStore.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function storeReload() {
	store.reload();
}

function dictStoreReload() {
	dictStore.reload();
}

function deleteDictType(dictTypeId) {
	Ext.Msg.confirm("提示", "确认删除系统参数类型？", function(button) {
				if (button == 'yes') {

					if ($("#dictTypeIdForDict").val() == dictTypeId) {
						deleteType = 0;
						$("#dictTypeIdForDict").val('');
					} else {
						deleteType = 1;
					}

					var form = window.document.forms[0];
					form.action = appUrl
							+ '/dict/dictAction!deleteDictType.htm?dictTypeId='
							+ dictTypeId;
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});
}

function deleteDict(dictId) {
	Ext.Msg.confirm("提示", "确认删除系统参数明细？", function(button) {
				if (button == 'yes') {
					deleteType = 2;

					var form = window.document.forms[0];
					form.action = appUrl
							+ '/dict/dictAction!deleteDict.htm?dictId='
							+ dictId;
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});
}

function modifyDictType(a) {
	var WWidth = 700;
	var WHeight = 400;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl
					+ "/dict/dictAction!updateDictTypePrepare.htm?dictTypeId="
					+ a, "modifyDictType", "left=" + WLeft + ",top=" + WTop
					+ ",width=" + WWidth + ",height=" + WHeight);
}

function modifyDict(a) {
	var WWidth = 700;
	var WHeight = 450;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/dict/dictAction!updateDictPrepare.htm?dictId=" + a,
			"modifyDict", "left=" + WLeft + ",top=" + WTop + ",width=" + WWidth
					+ ",height=" + WHeight);
}

function createDictType() {
	var WWidth = 700;
	var WHeight = 400;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/dict/dictAction!createDictTypePrepare.htm",
			"createDictType", "left=" + WLeft + ",top=" + WTop + ",width="
					+ WWidth + ",height=" + WHeight);
}

function createDict() {
	var dictTypeIdForDict = $("#dictTypeIdForDict").val();
	if (dictTypeIdForDict == "") {
		warn("请先选择系统参数类型并点击【查看系统参数】！");
		return;
	}

	var name = encodeURIComponent($("#dictTypeNameForDict").val());
	var WWidth = 700;
	var WHeight = 450;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/dict/dictAction!createDictPrepare.htm?dictTypeId="
					+ dictTypeIdForDict + "&dictTypeName=" + name,
			"createDict", "left=" + WLeft + ",top=" + WTop + ",width=" + WWidth
					+ ",height=" + WHeight);
}

function promgtMsg() {
	var hideFrame = document.getElementById("hideFrame");
	var failResult = hideFrame.contentWindow.failResult;
	var successResult = hideFrame.contentWindow.successResult;
	if (failResult != undefined && failResult != "") {
		Ext.Msg.show({
					title : '错误',
					msg : failResult,
					buttons : Ext.Msg.OK,
					fn : function(btn) {
						if (btn == 'ok') {
						}
					},
					icon : Ext.Msg.ERROR
				});
	} else if (successResult != undefined) {
		Ext.Msg.show({
					title : '信息',
					msg : successResult,
					icon : Ext.Msg.INFO,
					closable : false
				});

		setTimeout(function() {
					Ext.Msg.hide();
					if (deleteType == 0) {
						dictStore.removeAll();
						store.reload();
					} else if (deleteType == 1) {
						store.reload();
					} else {
						dictStore.reload();
					}
				}, 1000);
	}
}

function warn(msg) {
	Ext.Msg.show({
				title : '警告',
				msg : msg,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.WARNING
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