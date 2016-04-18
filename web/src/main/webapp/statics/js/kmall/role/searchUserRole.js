Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	new Ext.form.TextField({
				id : "passport",
				readOnly : true,
				grow : true,
				applyTo : 'passport'
			});

	new Ext.form.TextField({
				id : "copyUserName",
				readOnly : true,
				applyTo : 'copyUserName'
			});

	var role = new Ext.plugins.RoleSelector({
				url : appUrl + '/role/roleAction!getRoleJsonList.htm',
				triggerAction : 'power',
				searchable : true,
				multiable : true,
				displayField : 'roleId'
			});

	role.editor.applyToMarkup('roleIds');

	new Ext.form.DateField({
				id : "date1",
				applyTo : 'date1',
				format : 'Y-m-d',
				minValue : gmtStart,
				allowBlank : false,
				editable : false
			});

	new Ext.form.DateField({
				id : "date2",
				applyTo : 'date2',
				format : 'Y-m-d',
				minValue : gmtStart,
				allowBlank : false,
				editable : false
			});

	new Ext.form.DateField({
				id : "date3",
				applyTo : 'date3',
				format : 'Y-m-d',
				minValue : gmtStart,
				allowBlank : false,
				editable : false
			});

	var item = Ext.data.Record.create([{
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
				name : 'dictName',
				type : 'string'
			}, {
				name : 'expireDate',
				type : 'string'
			}, {
				name : 'state',
				type : 'string'
			}]);

	store = new Ext.data.Store({
				url : appUrl + '/role/roleAction!getUserRoleJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'id'
						}, item),
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
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "角色名称",
		dataIndex : 'roleName',
		width : 150,
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
		header : "过期日期",
		dataIndex : 'expireDate',
		width : 80,
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
		width : 110,
		sortable : false,
		align : 'center',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			var id = (record.get("id"));
			return "<a href=javascript:updateExpireDate('" + rowIndex
					+ "')>修改过期日期 </a>";
		}
	}]);

	grid = new Ext.grid.EditorGridPanel({
				store : store,
				cm : cm,
				sm : sm,
				renderTo : 'gridList',
				height : 350,
				loadMask : true,
				enableHdMenu : false,
				viewConfig : {
					forceFit : true
				},
				stripeRows : true,
				columnLines : true,
				plugins : [new Ext.plugins.HeaderAligning()]
			});

	search();

	$('#hideFrame').bind('load', promgtMsg);
});

function search() {
	store.baseParams.userId = userId;
	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function selectRole() {
	if ($('#roleIds').val() == '') {
		warn('请选择角色！');
		return;
	}

	var c = Ext.getCmp("date1").validate();
	if (!c) {
		return;
	}

	$('#expireDate').val(Ext.getCmp('date1').getValue().format('Y-m-d'));

	var params = [];
	document.getElementById("roleList").value = Ext.util.JSON.encode(params);

	var form = window.document.forms[0];
	form.action = "roleAction!createRole4User.htm";
	form.target = "hideFrame";
	form.submit();

	Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
}

function copyRole() {
	if ($('#copyUserId').val() == '') {
		warn('请选择用户！');
		return;
	}

	var c = Ext.getCmp("date2").validate();
	if (!c) {
		return;
	}

	$('#expireDate').val(Ext.getCmp('date2').getValue().format('Y-m-d'));

	var params = [];
	document.getElementById("roleList").value = Ext.util.JSON.encode(params);

	var form = window.document.forms[0];
	form.action = "roleAction!copyRole4User.htm";
	form.target = "hideFrame";
	form.submit();

	Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
}

function deleteUser4Role() {
	Ext.Msg.confirm("提示", "确认批量删除角色人员？", function(button) {
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
							+ "/role/roleAction!deleteUser4Role.htm";
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});
}

function updateExpireDate(rowIndex) {
	var id = store.getAt(rowIndex).get("id");
	var expireDate = store.getAt(rowIndex).get("expireDate");

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
										id : 'expire'
									}]
						})],
				buttons : [{
					text : '确定',
					handler : function() {
						var c = Ext.getCmp("expire").validate();
						if (!c) {
							return;
						}

						$('#expireDate').val(Ext.getCmp('expire').getValue()
								.format('Y-m-d'));

						var params = [];
						document.getElementById("roleList").value = Ext.util.JSON
								.encode(params);

						var form = window.document.forms[0];
						form.action = appUrl
								+ "/role/roleAction!updateExpireDate.htm?id="
								+ id;
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

function selectUser() {
	var form = window.document.forms[0];

	var URL = appUrl + "/user/userAction!selectUser.htm";
	var windowhight = 900;
	var windowwidth = 500;
	var top = (window.screen.availHeight - windowhight) / 2;
	var left = (window.screen.availWidth - windowwidth) / 2;
	var x = window.showModalDialog(URL, form,
			"dialogWidth=500px;dialogHeight=450px;");

	if (x == undefined) {
	} else {
		document.all.copyUserId.value = x[0];
		document.all.copyUserName.value = x[1];
	}
}

function forever(id) {
	$('#date' + id).val('9999-12-31');
}

function exportDataTemplate() {
	var params = [];
	document.getElementById("roleList").value = Ext.util.JSON.encode(params);

	var form = window.document.forms[0];
	form.action = "roleAction!exportDataTemplate.htm";
	form.target = "hideFrame";
	form.submit();
}

function importData() {
	Ext.getBody().mask();

	fp = new Ext.FormPanel({
		fileUpload : true,
		width : 280,
		frame : true,
		autoHeight : true,
		bodyStyle : 'padding: 10px 10px 0 10px;',
		labelWidth : 35,
		defaults : {
			anchor : '95%',
			allowBlank : false,
			msgTarget : 'side'
		},
		items : [{
					xtype : 'fileuploadfield',
					id : 'form-file',
					emptyText : '请选择excel文件',
					fieldLabel : '文件',
					name : 'upload',
					buttonText : '浏览'
				}],
		buttons : [{
			text : '保存',
			handler : function() {
				Ext.Msg.confirm("提示", "确定提交？", function(button) {
							if (button == 'yes') {
								if (fp.getForm().isValid()) {
									fp.getForm().submit({
										url : 'roleAction!importData.htm',
										params : {
											expireDate : Ext.getCmp('date3')
													.getValue().format('Y-m-d'),
											userId : $('#userId').val()
										},
										waitMsg : '正在提交文件...',
										success : function(form, action) {
											try {
												Ext.Msg.show({
															title : '信息',
															msg : action.result.msg,
															buttons : Ext.Msg.OK,
															fn : function(btn) {
																if (btn == 'ok') {
																	search();
																}
															},
															icon : Ext.Msg.INFO
														});
												win.close();
												Ext.getBody().unmask();
											} catch (err) {
											}
										},
										failure : function(form, action) {
											Ext.Msg.show({
														title : '信息',
														msg : action.result.msg,
														buttons : Ext.Msg.OK,
														icon : Ext.Msg.ERROR
													});
										}
									});
								}
							}
						});
			}
		}, {
			text : '重置',
			handler : function() {
				fp.getForm().reset();
			}
		}, {
			text : '取消',
			handler : function() {
				win.close();
				Ext.getBody().unmask();
			}
		}]
	});

	var win = new Ext.Window({
				title : "文件上传",
				width : 300,
				items : [fp],
				listeners : {
					"close" : function() {
						Ext.getBody().unmask();
					}
				}

			});

	win.show();
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
							store.reload();
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

function warn(msg) {
	Ext.Msg.show({
				title : '警告',
				msg : msg,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.WARNING
			});
}