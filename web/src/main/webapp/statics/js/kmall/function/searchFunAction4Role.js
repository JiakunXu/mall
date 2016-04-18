var item;
Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	item = Ext.data.Record.create([{
				name : 'id',
				type : 'string'
			}, {
				name : 'actionId',
				type : 'string'
			}, {
				name : 'actionName',
				type : 'string'
			}]);

	store = new Ext.data.Store({
		url : appUrl + '/function/functionAction!getFunAction4RoleJsonList.htm',
		reader : new Ext.data.SimpleJsonReader({
					id : 'id'
				}, item),
		remoteSort : true
	});

	var selector = new Ext.plugins.FunActionSelector({
				url : appUrl
						+ '/function/functionAction!getFunActionJsonList.htm',
				params : {
					roleId : encodeURIComponent($("#roleId").val())
				},
				triggerAction : 'power',
				searchable : true,
				multiable : false,
				allowBlank : false
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		xtype : 'actioncolumn',
		header : "操作",
		align : 'center',
		width : 40,
		items : [{
					icon : imgUrl + '/image/actions/icon_del.gif',
					handler : function(grid, record) {
						store.removeAt(record);
					}
				}]
	}, {
		header : "功能操作描述",
		dataIndex : 'actionName',
		width : 200,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		},
		editor : selector.editor
	}]);

	grid = new Ext.grid.EditorGridPanel({
				store : store,
				cm : cm,
				renderTo : 'gridList',
				height : 350,
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

	grid.on('beforeedit', function(obj) {
				rowIndex = obj.row;
				return true;
			});

	search();

	$('#hideFrame').bind('load', promgtMsg);
});

function search() {
	store.baseParams.roleId = encodeURIComponent($("#roleId").val());
	store.load({
				params : {
					start : 0,
					limit : pageSize
				},
				callback : function(records, options, success) {
					if (store.totalLength == 0) {
						add();
					}
				}
			});
}

function add() {
	store.add(new item({
				id : '',
				actionId : '',
				actionName : ''
			}));
}

function save() {
	var flag = true;
	var params = [];
	store.each(function(record) {
				if (record.get("actionId") == ''
						|| record.get("actionName") == '') {
					warn("功能操作描述 为必填项！");
					flag = false;
					return;
				}

				params.push({
							"id" : record.get("id"),
							"actionId" : record.get("actionId")
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
							+ "/function/functionAction!updateFunAction4Role.htm";
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
					store.reload();
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
