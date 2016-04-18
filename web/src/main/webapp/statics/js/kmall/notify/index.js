Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	new Ext.form.DateField({
				id : "gmtStart",
				applyTo : 'gmtStart',
				format : 'Y-m-d',
				editable : false
			});

	new Ext.form.DateField({
				id : "gmtEnd",
				width : "120px",
				applyTo : 'gmtEnd',
				format : 'Y-m-d',
				editable : false
			});

	var store2 = new Ext.data.SimpleStore({
				fields : ['itemId', 'itemName'],
				data : [['N', '未读'], ['Y', '已读'], ['', '所有状态类型']]
			});

	review = new Ext.form.ComboBox({
				id : 'review',
				store : store2,
				mode : 'local',
				displayField : 'itemName',
				valueField : 'itemId',
				allowBlank : false,
				editable : false,
				triggerAction : 'all',
				applyTo : 'review'
			});

	review.setValue('N');

	var Item = Ext.data.Record.create([{
				name : 'id',
				type : 'string'
			}, {
				name : 'system',
				type : 'string'
			}, {
				name : 'message',
				type : 'string'
			}, {
				name : 'sendDate',
				type : 'string'
			}, {
				name : 'review',
				type : 'string'
			}, {
				name : 'modifyDate',
				type : 'date'
			}]);

	store = new Ext.data.Store({
				url : appUrl + '/notify/notifyAction!getNotifyJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'id'
						}, Item),
				remoteSort : true
			});

	var sm = new Ext.grid.CheckboxSelectionModel({
				renderer : function(v, c, r) {
					if (r.get("review") == 'N') {
						return '<div class="x-grid3-row-checker"></div>';
					}
				},
				listeners : {
					beforerowselect : function(sm, rowIndex, keep, rec) {
						var occupy = store.getAt(rowIndex).get("review");
						if (occupy == 'Y') {
							return false;
						}
					}
				}
			});

	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "编号",
		dataIndex : 'id',
		width : 50,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "通知发送方",
		dataIndex : 'system',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "通知",
		dataIndex : 'message',
		width : 300,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "发送时间",
		dataIndex : 'sendDate',
		width : 120,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "修改时间",
		dataIndex : 'modifyDate',
		width : 120,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			v = Ext.util.Format.date(v.toUTC(), 'Y-m-d H:i:s');
			p.attr = 'ext:qtip="' + v + '"';
			return v;
		}
	}, {
		header : "已读状态",
		dataIndex : 'review',
		sortable : false,
		align : 'center',
		width : 80,
		renderer : function(v, p) {
			if (v == 'Y') {
				return "<p style='color:green'>已读</p>";
			} else {
				return "<p style='color:red'>未读</p>";
			}
		}
	}]);

	grid = new Ext.grid.EditorGridPanel({
				store : store,
				cm : cm,
				sm : sm,
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

	store.setDefaultSort('sendDate', 'DESC');

	search();

	$('#hideFrame').bind('load', promgtMsg);

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});
});

function search() {
	store.baseParams.review = review.getValue();
	store.baseParams.gmtStart = $('#gmtStart').val();
	store.baseParams.gmtEnd = $('#gmtEnd').val();

	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function resets() {
	$('#gmtStart').val(gStart);
	$('#gmtEnd').val(gEnd);
	review.setValue('');
}

function reload() {
	store.reload();
}

function reviewNotify() {
	Ext.Msg.confirm("提示", "确认已读通知？", function(button) {
				if (button == 'yes') {
					var rows = grid.getSelectionModel().getSelections();
					if (rows == "") {
						warn('请勾选需要已读的通知！');
						return;
					}

					var params = [];
					Ext.each(rows, function(value, index, a) {
								params.push({
											"id" : value.get("id")
										});
							});

					document.getElementById("notifyList").value = Ext.util.JSON
							.encode(params);

					var form = window.document.forms[0];
					form.action = appUrl + "/notify/notifyAction!review.htm";
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
					// 更新首页显示
					window.parent.parent.getNotifyCount();
				}, 1000);
	}
}

var tr = true;

function trigger() {
	if (tr) {
		$("#more").show();
		$("#trigger").text("收起更多");
		tr = false;
	} else {
		$("#more").hide();
		$("#trigger").text("更多条件");
		tr = true;
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
