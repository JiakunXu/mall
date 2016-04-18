var item;
var index = 0;
Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	item = Ext.data.Record.create([{
				name : 'actionId',
				type : 'string'
			}, {
				name : 'actionName',
				type : 'string'
			}, {
				name : 'alias',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}, {
				name : 'url',
				type : 'string'
			}, {
				name : 'defaults',
				type : 'string'
			}, {
				name : 'sq',
				type : 'string'
			}]);

	store = new Ext.data.Store({
				url : appUrl
						+ '/function/functionAction!getFunActionJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'actionId'
						}, item),
				remoteSort : true
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		xtype : 'actioncolumn',
		header : "操作",
		align : 'center',
		width : 30,
		items : [{
					icon : imgUrl + '/image/actions/icon_del.gif',
					handler : function(grid, record) {
						store.removeAt(record);
					}
				}]
	}, {
		header : "名称",
		dataIndex : 'actionName',
		width : 80,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		},
		editor : new Ext.form.TextField({
					allowBlank : false
				})
	}, {
		header : "别名",
		dataIndex : 'alias',
		sortable : false,
		align : 'left',
		width : 70,
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		},
		editor : new Ext.form.TextField({
					allowBlank : false
				})
	}, {
		header : "描述",
		dataIndex : 'remark',
		sortable : false,
		align : 'left',
		width : 50,
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		},
		editor : new Ext.form.TextField()
	}, {
		header : "url",
		dataIndex : 'url',
		sortable : false,
		align : 'left',
		width : 160,
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		},
		editor : new Ext.form.TextField({
					allowBlank : false
				})
	}, {
		header : "是否默认",
		dataIndex : 'defaults',
		sortable : false,
		align : 'center',
		width : 45,
		renderer : function(v, p, record, rowIndex) {
			if (v == 'Y') {
				v = '<input type="checkbox" name="defaults' + record.get('sq')
						+ '" checked />';
			} else {
				v = '<input type="checkbox" name="defaults' + record.get('sq')
						+ '" />';
			}

			return v;
		}
	}]);

	grid = new Ext.grid.EditorGridPanel({
				store : store,
				cm : cm,
				renderTo : 'gridList',
				height : 360,
				loadMask : true,
				autoScroll : true,
				enableHdMenu : false,
				viewConfig : {
					forceFit : true
				},
				stripeRows : true,
				columnLines : true,
				clicksToEdit : 1,
				plugins : [new Ext.plugins.HeaderAligning()]
			});

	search();

	$('#hideFrame').bind('load', promgtMsg);
});

function search() {
	store.baseParams.funId = encodeURIComponent($("#funId").val());
	store.load({
				params : {
					start : 0,
					limit : pageSize
				},
				callback : function(records, options, success) {
					store.each(function(record) {
								record.set('sq', index++);
							});

					if (store.totalLength == 0) {
						add();
					}
				}
			});
}

function add() {
	store.add(new item({
				actionId : '',
				actionName : '',
				alias : '',
				remark : '',
				url : '',
				defaults : '',
				sq : index++
			}));
}

function save() {
	var flag = true;
	var params = [];
	store.each(function(record) {
		if (record.get("actionName") == '' || record.get("alias") == ''
				|| record.get("url") == '') {
			warn("名称 别名 url 为必填项！");
			flag = false;
			return;
		}

		var f = document.getElementsByName('defaults' + record.get('sq'))[0].checked;

		params.push({
					"actionId" : record.get("actionId"),
					"actionName" : record.get("actionName"),
					"alias" : record.get("alias"),
					"remark" : record.get("remark"),
					"url" : record.get("url"),
					"defaults" : (f) ? 'Y' : 'N'
				});
	}, this);

	if (!flag) {
		return;
	}

	Ext.Msg.confirm("提示", "确认保存配置？", function(button) {
				if (button == 'yes') {
					document.getElementById("functionList").value = Ext.util.JSON
							.encode(params);

					var form = window.document.forms[0];
					form.action = appUrl
							+ "/function/functionAction!updateFunAction.htm";
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});
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
					index = 0;
					search();
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
