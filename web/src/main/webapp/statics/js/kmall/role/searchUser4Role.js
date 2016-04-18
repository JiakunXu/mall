Ext.onReady(function() {

	Ext.QuickTips.init();

	new Ext.form.TextField({
				id : "passport",
				allowBlank : true,
				applyTo : 'passport'
			});

	new Ext.form.TextField({
				id : "userName",
				allowBlank : true,
				applyTo : 'userName'
			});

	new Ext.form.TextField({
				id : "roleName",
				readOnly : true,
				grow : true,
				applyTo : 'roleName'
			});

	Item = Ext.data.Record.create([{
				name : 'id',
				type : 'string'
			}, {
				name : 'userId',
				type : 'string'
			}, {
				name : 'userName',
				type : 'string'
			}, {
				name : 'passport',
				type : 'string'
			}, {
				name : 'expireDate',
				type : 'string'
			}, {
				name : 'state',
				type : 'string'
			}]);

	userStore = new Ext.data.Store({
				url : appUrl + '/role/roleAction!getUser4RoleJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'id'
						}, Item),
				remoteSort : true
			})

	cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		id : "userId",
		header : "用户编号",
		dataIndex : 'userId',
		width : 40,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "用户帐号",
		dataIndex : 'passport',
		width : 80,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "用户名称",
		dataIndex : 'userName',
		width : 80,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "过期日期",
		dataIndex : 'expireDate',
		width : 60,
		sortable : false,
		align : 'center',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			var s = record.get("state");
			if (s == 'U') {
				return "<p style='color:green'>"
						+ Ext.util.Format.htmlEncode(value) + "</p>";
			} else {
				return "<p style='color:red'>"
						+ Ext.util.Format.htmlEncode(value) + "</p>";
			}
		}
	}, {
		header : "操作",
		width : 100,
		sortable : false,
		align : 'center',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			var id = (record.get("id"));
			return "<a href=javascript:updateExpireDate('" + rowIndex
					+ "')>修改过期日期 </a>|" + "<a href=javascript:deleteUser('"
					+ id + "')> 删除</a>";
		}
	}])

	grid = new Ext.grid.EditorGridPanel({
				store : userStore,
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
							store : userStore,
							displayInfo : true,
							displayMsg : '共 {2} 条记录，当前显示 {0} - {1}',
							emptyMsg : "没有找到记录！",
							plugins : [new Ext.ux.plugin.PagingToolbarResizer({
										displayText : '',
										options : [15, 30, 50],
										prependCombo : true
									})]
						})
			})

	search();

	$('#hideFrame').bind('load', promgtMsg);
})

function search() {
	userStore.baseParams.roleId = encodeURIComponent($("#roleId").val());
	userStore.baseParams.passport = encodeURIComponent($("#passport").val());
	userStore.baseParams.userName = encodeURIComponent($("#userName").val());
	userStore.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function resets() {
	$("#passport").val("");
	$("#userName").val("");
}

function deleteUser(id) {
	Ext.Msg.confirm("提示", "确认删除角色人员？", function(button) {
				if (button == 'yes') {
					var form = window.document.forms[0];
					form.action = appUrl
							+ '/role/roleAction!deleteUser4Role.htm?id=' + id;
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});
}

function updateExpireDate(rowIndex) {
	var id = userStore.getAt(rowIndex).get("id");
	var expireDate = userStore.getAt(rowIndex).get("expireDate");

	win = new Ext.Window({
				title : "修改",
				closeAction : 'close',
				closable : true,
				modal : 'true',
				width : 290,
				buttonAlign : "center",
				items : [new Ext.form.FormPanel({
							frame : true,
							labelAlign : 'right',
							labelWidth : 90,
							items : [{
										xtype : "datefield",
										width : 120,
										fieldLabel : "过期日期",
										allowBlank : false,
										format : 'Y-m-d',
										value : expireDate,
										id : 'expireDate'
									}]
						})],
				buttons : [{
					text : '确定',
					handler : function() {
						var c = Ext.getCmp("expireDate").validate();
						if (!c) {
							return;
						}

						var v = Ext.getCmp('expireDate').getValue()
								.format('Y-m-d');

						var form = window.document.forms[0];
						form.action = appUrl
								+ "/role/roleAction!updateExpireDate.htm?id="
								+ id + "&expireDate=" + v;
						form.target = "hideFrame";
						form.submit();

						Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
					}
				}, {
					text : '取消',
					handler : function() {
						win.close();
					}
				}]
			}).show();
}

function searchUser(roleId) {
	var WWidth = 600;
	var WHeight = 500;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	var url = appUrl + "/role/roleAction!searchUser.htm?roleId="
			+ encodeURIComponent(roleId);

	window.open(url, "searchUser", "left=" + WLeft + ",top=" + WTop + ",width="
					+ WWidth + ",height=" + WHeight);
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
							try {
								win.close();
							} catch (e) {

							}
							userStore.reload();
							window.opener.search();
						}
					},
					icon : Ext.Msg.INFO
				});
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