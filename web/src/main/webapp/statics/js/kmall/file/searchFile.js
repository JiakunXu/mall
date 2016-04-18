Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	fileNameField = new Ext.form.TextField({
		id : "fileName",
		applyTo : 'fileName'
	});

	var flagStore = new Ext.data.SimpleStore({
		fields : [ 'itemId', 'itemName' ],
		data : [ [ '', '所有状态' ], [ 'U', '有效' ], [ 'D', '删除' ] ]
	});

	var flag = new Ext.form.ComboBox({
		id : 'flag',
		store : flagStore,
		mode : 'local',
		displayField : 'itemName',
		valueField : 'itemId',
		editable : false,
		triggerAction : 'all',
		applyTo : 'flag'
	});

	flag.setValue('');

	var Item = Ext.data.Record.create([ {
		name : 'fileId',
		type : 'string'
	}, {
		name : 'fileName',
		type : 'string'
	}, {
		name : 'suffix',
		type : 'string'
	}, {
		name : 'filePath',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}, {
		name : 'createDate',
		type : 'date'
	}, {
		name : 'modifyDate',
		type : 'date'
	} ]);

	store = new Ext.data.Store({
		url : appUrl + '/file/fileAction!getFileJsonList.htm',
		reader : new Ext.data.SimpleJsonReader({
			id : 'fileId'
		}, Item),
		remoteSort : true
	});

	var sm = new Ext.grid.CheckboxSelectionModel({
		listeners : {
			beforerowselect : function(sm, rowIndex, keep, rec) {
				var fiMaterielV = store.getAt(rowIndex).get("flag");
				if (fiMaterielV == 'D') {
					return false;
				}
			}
		}
	});

	var cm = new Ext.grid.ColumnModel([
			sm,
			{
				header : "文件编号",
				dataIndex : 'fileId',
				width : 120,
				sortable : false,
				align : 'center',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return "<a href='javascript:fetchFile(\"" + v + "\")'>"
							+ Ext.util.Format.htmlEncode(v) + "</a>";
				}
			}, {
				header : "文件名称",
				dataIndex : 'fileName',
				width : 100,
				sortable : false,
				align : 'left',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "后缀",
				dataIndex : 'suffix',
				width : 40,
				sortable : false,
				align : 'center',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "文件路径",
				dataIndex : 'filePath',
				width : 100,
				sortable : false,
				align : 'left',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "状态",
				dataIndex : 'flag',
				width : 40,
				sortable : false,
				align : 'center',
				renderer : function(v, p) {
					if ('U' == v) {
						return "<p style='color:green'>有效</p>";
					} else {
						return "<p style='color:red'>删除</p>";
					}
				}
			}, {
				header : "创建时间",
				dataIndex : 'createDate',
				width : 70,
				sortable : false,
				align : 'center',
				renderer : function(v, p) {
					v = Ext.util.Format.date(v.toUTC(), 'Y-m-d H:i:s');
					p.attr = 'ext:qtip="' + v + '"';
					return v;
				}
			}, {
				header : "修改时间",
				dataIndex : 'modifyDate',
				width : 70,
				sortable : true,
				align : 'center',
				renderer : function(v, p) {
					v = Ext.util.Format.date(v.toUTC(), 'Y-m-d H:i:s');
					p.attr = 'ext:qtip="' + v + '"';
					return v;
				}
			} ]);

	grid = new Ext.grid.EditorGridPanel({
		store : store,
		cm : cm,
		sm : sm,
		renderTo : 'gridList',
		height : height,
		loadMask : true,
		enableHdMenu : false,
		viewConfig : {
			forceFit : true
		},
		stripeRows : true,
		columnLines : true,
		plugins : [ new Ext.plugins.HeaderAligning() ],
		bbar : new Ext.PagingToolbar({
			pageSize : pageSize,
			store : store,
			displayInfo : true,
			displayMsg : '共 {2} 条记录，当前显示 {0} - {1}',
			emptyMsg : "没有找到记录！"
		})
	});

	store.setDefaultSort('modifyDate', 'DESC');
	search();

	$('#hideFrame').bind('load', promgtMsg);

});

function search() {
	store.baseParams.fileName = encodeURIComponent(fileNameField.getValue());
	store.baseParams.flag = encodeURIComponent(Ext.getCmp("flag").getValue());

	store.load({
		params : {
			start : 0,
			limit : pageSize
		}
	});
}

function fetchFile(fileId) {
	document.getElementById("fileInfoList").value = "[{}]";

	var form = window.document.forms[0];
	form.action = appUrl + "/file/fileAction!fetchFile.htm?fileId=" + fileId;
	form.target = "hideFrame";
	form.submit();
}

function deleteFile() {
	Ext.Msg.confirm("提示", "确认批量删除文件？", function(button) {
		if (button == 'yes') {
			var rows = grid.getSelectionModel().getSelections();

			if (rows == "") {
				Ext.Msg.show({
					title : '提示',
					msg : '请勾选要删除文件的数据',
					buttons : Ext.Msg.OK,
					icon : Ext.Msg.ERROR
				});
				return;
			}

			var params = [];
			Ext.each(rows, function(value, index, a) {
				params.push({
					"fileId" : value.get("fileId")
				});
			});

			document.getElementById("fileInfoList").value = Ext.util.JSON
					.encode(params);

			var form = window.document.forms[0];
			form.action = appUrl + "/file/fileAction!deleteFile.htm";
			form.target = "hideFrame";
			form.submit();

			Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
		}
	});
}

function saveFile() {
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
		items : [ {
			xtype : 'fileuploadfield',
			id : 'form-file',
			emptyText : '请选择上传文件',
			fieldLabel : '文件',
			name : 'upload',
			buttonText : '浏览'
		} ],
		buttons : [ {
			text : '保存',
			handler : function() {
				Ext.Msg.confirm("提示", "确定提交？", function(button) {
					if (button == 'yes') {
						if (fp.getForm().isValid()) {
							fp.getForm().submit({
								url : 'fileAction!saveFile.htm',
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
		} ]
	});

	var win = new Ext.Window({
		title : "文件上传",
		width : 300,
		items : [ fp ],
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
					search();
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