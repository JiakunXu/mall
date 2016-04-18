Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	idField = new Ext.form.TextField({
				id : "roleId",
				applyTo : 'roleId'
			});

	nameField = new Ext.form.TextField({
				id : "roleName",
				applyTo : 'roleName'
			});

	var item1 = Ext.data.Record.create([{
				name : 'dictId',
				type : 'string'
			}, {
				name : 'dictName',
				type : 'string'
			}, {
				name : 'dictValue',
				type : 'string'
			}]);

	store1 = new Ext.data.Store({
		url : appUrl
				+ '/dict/dictAction!getDictList4ComboBox.htm?dictTypeValue=roleType',
		reader : new Ext.data.SimpleJsonReader({
					id : 'dictId'
				}, item1),
		remoteSort : true
	});

	typeField = new Ext.form.ComboBox({
				id : 'type',
				store : store1,
				mode : 'local',
				displayField : 'dictName',
				valueField : 'dictValue',
				editable : false,
				triggerAction : 'all',
				applyTo : 'type'
			});

	store1.load({
				callback : function(records, options, success) {
					store1.add(new item1({
								dictName : '所有角色类型',
								dictValue : ""
							}));
					typeField.setValue("");
				}
			});

	var store2 = new Ext.data.SimpleStore({
				fields : ['itemId', 'itemName'],
				data : [['Y', '已分配'], ['N', '未分配'], ['', '所有状态类型']]
			});

	staState = new Ext.form.ComboBox({
				id : 'staState',
				store : store2,
				mode : 'local',
				displayField : 'itemName',
				valueField : 'itemId',
				allowBlank : false,
				editable : false,
				triggerAction : 'all',
				applyTo : 'staState'
			});

	staState.setValue('');

	var store3 = new Ext.data.SimpleStore({
				fields : ['itemId', 'itemName'],
				data : [['Y', '已分配'], ['N', '未分配'], ['', '所有状态类型']]
			});

	userState = new Ext.form.ComboBox({
				id : 'userState',
				store : store3,
				mode : 'local',
				displayField : 'itemName',
				valueField : 'itemId',
				allowBlank : false,
				editable : false,
				triggerAction : 'all',
				applyTo : 'userState'
			});

	userState.setValue('');

	var store4 = new Ext.data.SimpleStore({
				fields : ['itemId', 'itemName'],
				data : [['Y', '是'], ['N', '否'], ['', '所有状态类型']]
			});

	defaults = new Ext.form.ComboBox({
				id : 'defaults',
				store : store4,
				mode : 'local',
				displayField : 'itemName',
				valueField : 'itemId',
				allowBlank : false,
				editable : false,
				triggerAction : 'all',
				applyTo : 'defaults'
			});

	defaults.setValue('');

	var item = Ext.data.Record.create([{
				name : 'roleId',
				type : 'string'
			}, {
				name : 'roleName',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}, {
				name : 'type',
				type : 'string'
			}, {
				name : 'menuCount',
				type : 'int'
			}, {
				name : 'stationCount',
				type : 'int'
			}, {
				name : 'userCount',
				type : 'int'
			}, {
				name : 'positionCount',
				type : 'int'
			}, {
				name : 'dictName',
				type : 'string'
			}]);

	store = new Ext.data.Store({
				url : appUrl + '/role/roleAction!getRoleJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'roleId'
						}, item),
				remoteSort : true
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "角色编号",
		dataIndex : 'roleId',
		width : 50,
		sortable : true,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "角色名称",
		dataIndex : 'roleName',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "角色描述",
		dataIndex : 'remark',
		width : 50,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "角色类型",
		dataIndex : 'dictName',
		width : 50,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "是否维护菜单",
		dataIndex : 'menuCount',
		width : 50,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			if (v == 0) {
				return "<p style='color:red'>未维护</p>";
			} else {
				return "<p style='color:green'>已维护</p>";
			}
		}
	}, {
		header : "是否分配岗位",
		dataIndex : 'stationCount',
		width : 50,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			if (v == 0) {
				return "<p style='color:red'>未分配</p>";
			} else {
				return "<p style='color:green'>已分配</p>";
			}
		}
	}, {
		header : "是否分配人员",
		dataIndex : 'userCount',
		width : 50,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			if (v == 0) {
				return "<p style='color:red'>未分配</p>";
			} else {
				return "<p style='color:green'>已分配</p>";
			}
		}
	}, {
		header : "操作",
		width : 180,
		sortable : false,
		align : 'center',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			var strReturn = "<a href=javascript:searchMenu4Role('" + rowIndex
					+ "') >维护菜单 </a>|"
					+ "<a href=javascript:searchFunAction4Role('" + rowIndex
					+ "') > 维护权限点 </a>|"
					+ "<a href=javascript:searchUser4Role('" + rowIndex
					+ "') > 分配人员 </a>|" + "<a href=javascript:updateRole('"
					+ rowIndex + "') > 修改 </a>|"
					+ "<a href=javascript:deleteRole('" + rowIndex
					+ "') > 刪除</a>";

			return strReturn;
		}
	}]);

	grid = new Ext.grid.EditorGridPanel({
				store : store,
				cm : cm,
				renderTo : 'gridList',
				height : height,
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

	store.setDefaultSort('roleId', 'DESC');
	search();

	$('#hideFrame').bind('load', promgtMsg);

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});
});

function search() {
	store.baseParams.roleId = encodeURIComponent(idField.getValue());
	store.baseParams.roleName = encodeURIComponent(nameField.getValue());
	store.baseParams.type = typeField.getValue();
	store.baseParams.staState = staState.getValue();
	store.baseParams.userState = userState.getValue();
	store.baseParams.defaults = defaults.getValue();

	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function resets() {
	$("#roleId").val("");
	$("#roleName").val("");
	typeField.setValue("");
	staState.setValue("");
	userState.setValue("");
	defaults.setValue("");
}

function reload() {
	store.reload();
}

function createRole() {
	var WWidth = 700;
	var WHeight = 500;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/role/roleAction!createRolePrepare.htm",
			"createRole", "left=" + WLeft + ",top=" + WTop + ",width=" + WWidth
					+ ",height=" + WHeight);
}

function updateRole(rowIndex) {
	var id = encodeURIComponent(store.getAt(rowIndex).get("roleId"));
	var WWidth = 700;
	var WHeight = 500;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/role/roleAction!updateRolePrepare.htm?roleId=" + id,
			"updateRole", "left=" + WLeft + ",top=" + WTop + ",width=" + WWidth
					+ ",height=" + WHeight);
}

function searchMenu4Role(rowIndex) {
	var id = encodeURIComponent(store.getAt(rowIndex).get("roleId"));
	var name = encodeURIComponent(store.getAt(rowIndex).get("roleName"));
	var type = store.getAt(rowIndex).get("type");
	var WWidth = 800;
	var WHeight = 610;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/menu/menuAction!searchMenu4Role.htm?roleId=" + id
					+ "&roleName=" + name + "&type=" + type, "searchMenu4Role",
			"left=" + WLeft + ",top=" + WTop + ",width=" + WWidth + ",height="
					+ WHeight);
}

function searchFunAction4Role(rowIndex) {
	var id = encodeURIComponent(store.getAt(rowIndex).get("roleId"));
	var name = encodeURIComponent(store.getAt(rowIndex).get("roleName"));
	var WWidth = 500;
	var WHeight = 470;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	var url = appUrl
			+ "/function/functionAction!searchFunAction4Role.htm?roleId=" + id
			+ "&roleName=" + name;
	window.open(url, "searchFunAction4Role", "left=" + WLeft + ",top=" + WTop
					+ ",width=" + WWidth + ",height=" + WHeight);
}

function deleteRole(rowIndex) {
	var id = encodeURIComponent(store.getAt(rowIndex).get("roleId"));
	var userCount = store.getAt(rowIndex).get("userCount");
	var stationCount = store.getAt(rowIndex).get("stationCount");
	var positionCount = store.getAt(rowIndex).get("positionCount");

	var tips = "";
	if (userCount != '0') {
		tips += "该角色已分配人员，";
	}

	if (stationCount != '0') {
		tips += "该角色已分配岗位，";
	}

	if (positionCount != '0') {
		tips += "该角色已分配编制，";
	}

	if (tips == "") {
		tips = "确认删除角色？";
	} else {
		tips += "如果删除角色，则以上关联都将自动删除，确认是否删除角色？";
	}

	Ext.Msg.confirm("提示", tips, function(button) {
				if (button == 'yes') {
					var form = window.document.forms[0];
					form.action = appUrl
							+ '/role/roleAction!deleteRole.htm?roleId=' + id;
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});
}

function syncSAPRole() {
	Ext.Msg.confirm("提示", "确认同步ERP角色？", function(button) {
				if (button == 'yes') {
					var form = window.document.forms[0];
					form.action = appUrl
							+ "/sap/sapAccountAction!syncSAPRole.htm";
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});
}

function searchUser4Role(rowIndex) {
	var roleId = store.getAt(rowIndex).get("roleId");
	var value = store.getAt(rowIndex).get("roleName");
	var WWidth = 650;
	var WHeight = 550;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	var url = appUrl + "/role/roleAction!searchUser4RolePrepare.htm?roleId="
			+ encodeURIComponent(roleId) + "&roleName="
			+ encodeURIComponent(value);
	window.open(url, "searchUser4Role", "left=" + WLeft + ",top=" + WTop
					+ ",width=" + WWidth + ",height=" + WHeight);
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

var tr = false;

function trigger() {
	if (tr) {
		$("#more1").show();
		$("#more2").show();
		$("#trigger").text("收起更多");
		tr = false;
	} else {
		$("#more1").hide();
		$("#more2").hide();
		$("#trigger").text("更多条件");
		tr = true;
	}
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