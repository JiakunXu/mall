Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	var s = new Ext.plugins.OrgVersionSelector({
				url : appUrl
						+ '/org/orgVersionAction!getOrgVersionJsonList.htm',
				triggerAction : 'power',
				searchable : true,
				multiable : false,
				allowBlank : false,
				displayField : 'verName',
				hiddenField : {
					verId : 'verId',
					run : 'run'
				},
				onselect : afterSelect
			});

	s.editor.applyToMarkup('verName');

	var item = Ext.data.Record.create([{
				name : 'stId',
				type : 'string'
			}, {
				name : 'strategy',
				type : 'string'
			}, {
				name : 'fromOrgName',
				type : 'string'
			}, {
				name : 'toOrgName',
				type : 'string'
			}, {
				name : 'rank',
				type : 'int'
			}]);

	store = new Ext.data.Store({
				url : appUrl
						+ '/org/orgVersionAction!getOrgVersionSTJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'stId'
						}, item),
				remoteSort : true
			});

	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true,
				header : '',
				listeners : {
					rowselect : function(sm, rowIndex, keep, rec) {
						searchData(store.getAt(rowIndex).get("stId"));
					}
				}
			});

	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "步骤",
		dataIndex : 'rank',
		width : 40,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			return "第 " + v + " 步";
		}
	}, {
		header : "操作类型",
		dataIndex : 'strategy',
		width : 40,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			if (v == 'M') {
				return "合并";

			} else if (v == 'S') {
				return "拆分";
			} else {
				return "兼并";
			}
		}
	}, {
		header : "从",
		dataIndex : 'fromOrgName',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "到",
		dataIndex : 'toOrgName',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}

	}, {
		header : "操作",
		sortable : false,
		align : 'center',
		width : 80,
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			return "<a href=javascript:cancel(" + rowIndex + ");> 取消调整步骤</a>";
		}
	}]);

	grid = new Ext.grid.EditorGridPanel({
				store : store,
				cm : cm,
				sm : sm,
				renderTo : 'gridList1',
				height : 230,
				loadMask : true,
				enableHdMenu : false,
				stripeRows : true,
				columnLines : true,
				viewConfig : {
					forceFit : true
				},
				plugins : [new Ext.plugins.HeaderAligning()]
			});

	// 调整涉及主数据

	var item1 = Ext.data.Record.create([{
				name : 'userId',
				type : 'string'
			}, {
				name : 'userName',
				type : 'string'
			}]);

	store1 = new Ext.data.Store({
				url : appUrl + '/org/orgVersionAction!getUserJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : ''
						}, item1),
				remoteSort : true
			});

	var cm1 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						renderer : function(v, p, record, rowIndex) {
							return rowIndex + 1;
						}
					}), {
				header : "用户编号",
				dataIndex : 'userId',
				width : 100,
				sortable : false,
				align : 'left',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "用户名称",
				dataIndex : 'userName',
				width : 100,
				sortable : false,
				align : 'left',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}]);

	grid1 = new Ext.grid.EditorGridPanel({
				store : store1,
				cm : cm1,
				height : 230,
				loadMask : true,
				enableHdMenu : false,
				stripeRows : true,
				columnLines : true,
				viewConfig : {
					forceFit : true
				},
				plugins : [new Ext.plugins.HeaderAligning()]
			});

	var item2 = Ext.data.Record.create([{
				name : 'staId',
				type : 'string'
			}, {
				name : 'staName',
				type : 'string'
			}]);

	store2 = new Ext.data.Store({
				url : appUrl + '/org/orgVersionAction!getStationJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : ''
						}, item2),
				remoteSort : true
			});

	var cm2 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						renderer : function(v, p, record, rowIndex) {
							return rowIndex + 1;
						}
					}), {
				header : "岗位编号",
				dataIndex : 'staId',
				width : 100,
				sortable : false,
				align : 'left',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "岗位名称",
				dataIndex : 'staName',
				width : 100,
				sortable : false,
				align : 'left',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}]);

	grid2 = new Ext.grid.EditorGridPanel({
				store : store2,
				cm : cm2,
				height : 230,
				loadMask : true,
				enableHdMenu : false,
				stripeRows : true,
				columnLines : true,
				viewConfig : {
					forceFit : true
				},
				plugins : [new Ext.plugins.HeaderAligning()]
			});

	var item3 = Ext.data.Record.create([{
				name : 'posId',
				type : 'string'
			}, {
				name : 'posName',
				type : 'string'
			}]);

	store3 = new Ext.data.Store({
				url : appUrl + '/org/orgVersionAction!getPositionJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : ''
						}, item3),
				remoteSort : true
			});

	var cm3 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						renderer : function(v, p, record, rowIndex) {
							return rowIndex + 1;
						}
					}), {
				header : "职位编号",
				dataIndex : 'posId',
				width : 100,
				sortable : false,
				align : 'left',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "职位名称",
				dataIndex : 'posName',
				width : 100,
				sortable : false,
				align : 'left',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}]);

	grid3 = new Ext.grid.EditorGridPanel({
				store : store3,
				cm : cm3,
				height : 230,
				loadMask : true,
				enableHdMenu : false,
				stripeRows : true,
				columnLines : true,
				viewConfig : {
					forceFit : true
				},
				plugins : [new Ext.plugins.HeaderAligning()]
			});

	tabPanel = new Ext.TabPanel({
				renderTo : 'gridList2',
				region : 'center',
				border : false,
				height : 230,
				enableTabScroll : true,
				activeTab : 0,
				items : [{
							title : '组织相关人员',
							items : grid1
						}, {
							title : '组织相关岗位',
							items : grid2
						}, {
							title : '组织相关编制',
							items : grid3
						}]
			});

	$('#hideFrame').bind('load', promgtMsg);
});

function select(verId, verName, run) {
	$('#verId').val(verId);
	$('#verName').val(verName);
	$('#run').val(run);

	afterSelect();
}

function afterSelect() {
	if ($('#run').val() == 'Y') {
		$('#msg').html("已执行组织架构调整");
		document.getElementById("addd").style.display = "none";
	} else {
		$('#msg').html("未已执行组织架构调整");
		document.getElementById("addd").style.display = "";
	}

	search();
}

function search() {
	store.baseParams.verId = $("#verId").val();

	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function searchData(stId) {
	store1.baseParams.stId = stId;
	store1.load();

	store2.baseParams.stId = stId;
	store2.load();

	store3.baseParams.stId = stId;
	store3.load();
}

function removeData() {
	store1.removeAll();
	store2.removeAll();
	store3.removeAll();
}

function createOrgVersion() {
	var form = window.document.forms[0];
	var WWidth = 450;
	var WHeight = 300;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/org/orgVersionAction!createOrgVersionPrepare.htm",
			"createOrgVersion", "left=" + WLeft + ",top=" + WTop + ",width="
					+ WWidth + ",height=" + WHeight);
}

var strategy;
var fromOrgId;

function windowClose() {
	strategy = '';
	fromOrgId = '';
}

function first() {
	var win1 = new Ext.Window({
				title : '调整步骤：第一步，选择调整策略',
				width : 300,
				height : 200,
				closeAction : 'hide',
				plain : true,
				layout : 'form',
				items : [{
					layout : 'column',
					style : 'padding:10px 20px 0px;',
					baseCls : 'x-plain',
					items : [{
						layout : 'form',
						baseCls : 'x-plain',
						labelWidth : 30,
						items : [{
							xtype : "combo",
							id : "combo1",
							fieldLabel : "策略",
							emptyText : '请选择组织调整策略.',
							mode : 'local',
							displayField : 'itemName',
							valueField : 'itemId',
							allowBlank : false,
							editable : false,
							triggerAction : 'all',
							store : new Ext.data.SimpleStore({
										fields : ['itemId', 'itemName'],
										data : [['M', '合并'], ['T', '兼并'],
												['S', '拆分']]
									}),
							value : strategy,
							listeners : {
								'select' : function(combo, record, index) {
									strategy = record.get("itemId");
								}
							}
						}]
					}]
				}],
				buttonAlign : 'center',
				buttons : [{
							text : '下一步',
							handler : function() {
								var p = Ext.getCmp("combo1").validate();
								if (!p) {
									return;
								}
								win1.close();
								second();
							}
						}, {
							text : '取消',
							handler : function() {
								win1.close();
								windowClose();
							}
						}]
			});

	win1.show();
}

function second() {
	var win2 = new Ext.Window({
				title : '调整步骤：第二步，选择被调整组织',
				width : 300,
				height : 200,
				closeAction : 'hide',
				plain : true,
				layout : 'form',
				items : [{
					layout : 'column',
					style : 'padding:10px 20px 0px;',
					baseCls : 'x-plain',
					items : [{
						layout : 'form',
						baseCls : 'x-plain',
						labelWidth : 30,
						items : [{
							xtype : "combo",
							fieldLabel : "策略",
							emptyText : '请选择组织调整策略.',
							mode : 'local',
							displayField : 'itemName',
							valueField : 'itemId',
							allowBlank : false,
							editable : false,
							triggerAction : 'all',
							store : new Ext.data.SimpleStore({
										fields : ['itemId', 'itemName'],
										data : [['Y', '合并'], ['N', '兼并'],
												['N', '拆分']]
									}),
							value : strategy,
							disabled : true
						}, {
							xtype : 'textfield',
							id : "textfield2",
							fieldLabel : '从',
							emptyText : '从左边组织树中选择.',
							allowBlank : false,
							value : fromOrgId
						}]
					}]
				}],
				buttonAlign : 'center',
				buttons : [{
							text : '上一步',
							handler : function() {
								win2.close();
								first();
							}
						}, {
							text : '下一步',
							handler : function() {
								var p = Ext.getCmp("textfield2").validate();
								if (!p) {
									return;
								}
								fromOrgId = Ext.getCmp("textfield2").getValue();
								win2.close();
								third();
							}
						}, {
							text : '取消',
							handler : function() {
								win2.close();
								windowClose();
							}
						}]
			});

	win2.show();
}

function third() {
	win3 = new Ext.Window({
		title : '调整步骤：第三步，选择调整后组织',
		width : 300,
		height : 200,
		closeAction : 'hide',
		plain : true,
		layout : 'form',
		items : [{
			layout : 'column',
			style : 'padding:10px 20px 0px;',
			baseCls : 'x-plain',
			items : [{
				layout : 'form',
				baseCls : 'x-plain',
				labelWidth : 30,
				items : [{
							xtype : "combo",
							fieldLabel : "策略",
							emptyText : '请选择组织调整策略.',
							mode : 'local',
							displayField : 'itemName',
							valueField : 'itemId',
							allowBlank : false,
							editable : false,
							triggerAction : 'all',
							store : new Ext.data.SimpleStore({
										fields : ['itemId', 'itemName'],
										data : [['Y', '合并'], ['N', '兼并'],
												['N', '拆分']]
									}),
							value : strategy,
							disabled : true
						}, {
							xtype : 'textfield',
							fieldLabel : '从',
							value : fromOrgId,
							disabled : true
						}, {
							xtype : 'textfield',
							id : "textfield3",
							fieldLabel : '到',
							emptyText : '从左边组织树中选择.'
						}]
			}]
		}],
		buttonAlign : 'center',
		buttons : [{
					text : '上一步',
					handler : function() {
						win3.close();
						second();
					}
				}, {
					text : '完成',
					handler : function() {
						var p = Ext.getCmp("textfield3").validate();
						if (!p) {
							return;
						}
						var va = Ext.getCmp("textfield3").getValue();

						$('#strategy').val(strategy);
						$('#fromOrgId').val(fromOrgId);

						if (strategy == 'M' || strategy == 'S') {
							$('#toOrgName').val(va);
						} else {
							$('#toOrgId').val(va);
						}

						var form = window.document.forms[0];
						form.action = appUrl
								+ "/org/orgVersionAction!createOrgVersionST.htm";
						form.target = "hideFrame";
						form.submit();

						Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
					}
				}, {
					text : '取消',
					handler : function() {
						win3.close();
						windowClose();
					}
				}]
	});

	win3.show();
}

function cancel(rowIndex) {
	var stId = store.getAt(rowIndex).get("stId");
	var tips = "确认取消调整步骤？该步骤之后的步骤都将被自动取消";
	Ext.Msg.confirm("提示", tips, function(button) {
				if (button == 'yes') {
					var form = window.document.forms[0];
					form.action = appUrl
							+ '/org/orgVersionAction!cancelOrgVersionST.htm?stId='
							+ stId;
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
					buttons : Ext.Msg.OK,
					fn : function(btn) {
						if (btn == 'ok') {
							search();
							win3.close();
							windowClose();
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
