Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	if (staId != '') {
		new Ext.form.TextField({
					id : "staName",
					readOnly : true,
					grow : true,
					applyTo : 'staName'
				});
	}

	if (posId != '') {
		new Ext.form.TextField({
					id : "posName",
					readOnly : true,
					grow : true,
					applyTo : 'posName'
				});
	}

	var role = new Ext.plugins.RoleSelector({
				url : appUrl + '/role/roleAction!getRoleJsonList.htm',
				triggerAction : 'power',
				searchable : true,
				multiable : true,
				displayField : 'roleId'
			});

	role.editor.applyToMarkup('roleIds');

	if (staId != '') {
		var st = new Ext.plugins.StationSelector({
					url : appUrl
							+ '/station/stationAction!getStationJsonList.htm',
					triggerAction : 'power',
					searchable : true,
					multiable : false,
					allowBlank : false,
					displayField : 'staName',
					hiddenField : {
						staId : 'staIds'
					}
				});

		st.editor.applyToMarkup('staNames');
	}

	idField = new Ext.form.TextField({
				id : "roleId",
				applyTo : 'roleId'
			});

	nameField = new Ext.form.TextField({
				id : "roleName",
				applyTo : 'roleName'
			});

	var Item = Ext.data.Record.create([{
				name : 'id',
				type : 'string'
			}, {
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
				name : 'dictName',
				type : 'string'
			}, {
				name : 'menuCount',
				type : 'int'
			}]);

	store = new Ext.data.Store({
				url : appUrl + '/role/roleAction!getSelectedRoleJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'id'
						}, Item),
				remoteSort : true
			});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "角色编号",
		dataIndex : 'roleId',
		width : 100,
		sortable : true,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "角色名称",
		dataIndex : 'roleName',
		width : 200,
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
		header : "操作",
		width : 100,
		sortable : false,
		align : 'center',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			return "<a href=javascript:searchMenu4Role('" + rowIndex
					+ "') >维护菜单</a>";
		}
	}]);

	grid = new Ext.grid.EditorGridPanel({
				store : store,
				cm : cm,
				sm : sm,
				renderTo : 'gridList',
				height : 360,
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

});

function search() {
	store.baseParams.roleId = encodeURIComponent(idField.getValue());
	store.baseParams.roleName = encodeURIComponent(nameField.getValue());
	store.baseParams.staId = staId;
	store.baseParams.posId = posId;

	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function resets() {
	$('#roleId').val("");
	$('#roleName').val("");
}

function selectRole() {
	if ($('#roleIds').val() == '') {
		warn('请选择角色！');
		return;
	}

	var params = [];
	document.getElementById("roleList").value = Ext.util.JSON.encode(params);

	var form = window.document.forms[0];
	form.action = "roleAction!selectRole.htm";
	form.target = "hideFrame";
	form.submit();

	Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
}

function selectStationRole() {
	if ($('#staIds').val() == '') {
		warn('请选择岗位！');
		return;
	}

	var params = [];
	document.getElementById("roleList").value = Ext.util.JSON.encode(params);

	var form = window.document.forms[0];
	form.action = "roleAction!selectStationRole.htm";
	form.target = "hideFrame";
	form.submit();

	Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
}

function deleteRole() {
	Ext.Msg.confirm("提示", "确认批量删除角色？", function(button) {
				if (button == 'yes') {
					var rows = grid.getSelectionModel().getSelections();

					if (rows == "") {
						Ext.Msg.show({
									title : '提示',
									msg : '请勾选要删除角色的数据',
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
						return;
					}

					var params = [];
					Ext.each(rows, function(value, index, a) {
								params.push({
											"id" : value.get("id")
										});
							});

					document.getElementById("roleList").value = Ext.util.JSON
							.encode(params);

					var form = window.document.forms[0];
					form.action = appUrl
							+ "/role/roleAction!deleteSelectedRole.htm";
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});

}

function searchMenu4Role(rowIndex) {
	var id = encodeURIComponent(store.getAt(rowIndex).get("roleId"));
	var name = encodeURIComponent(store.getAt(rowIndex).get("roleName"));
	var type = store.getAt(rowIndex).get("type");
	var WWidth = 800;
	var WHeight = 530;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/menu/menuAction!searchMenu4Role.htm?roleId=" + id
					+ "&roleName=" + name + "&type=" + type, "searchMenu4Role",
			"left=" + WLeft + ",top=" + WTop + ",width=" + WWidth + ",height="
					+ WHeight);
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
					buttons : Ext.Msg.OK,
					fn : function(btn) {
						if (btn == 'ok') {
							$('#roleIds').val('');
							$('#staIds').val('');
							$('#staNames').val('');
							store.reload();
							window.parent.opener.reload();
						}
					},
					icon : Ext.Msg.INFO
				});
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