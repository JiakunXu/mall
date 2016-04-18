Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	var Item = Ext.data.Record.create([{
				name : 'memId',
				type : 'string'
			}, {
				name : 'userId',
				type : 'string'
			}, {
				name : 'userName',
				type : 'string'
			}, {
				name : 'levelName',
				type : 'string'
			}, {
				name : 'points',
				type : 'int'
			}, {
				name : 'surplusPoints',
				type : 'int'
			}, {
				name : 'mobile',
				type : 'string'
			}, {
				name : 'sex',
				type : 'string'
			}, {
				name : 'birthday',
				type : 'date'
			}, {
				name : 'address',
				type : 'string'
			}, {
				name : 'postalCode',
				type : 'string'
			}, {
				name : 'profession',
				type : 'string'
			}, {
				name : 'education',
				type : 'string'
			}, {
				name : 'maritalStatus',
				type : 'string'
			}, {
				name : 'weddingDay',
				type : 'date'
			}]);

	store = new Ext.data.Store({
				url : appUrl + '/member/memberAction!getMemberJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'memId'
						}, Item),
				remoteSort : true
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "会员编号",
		dataIndex : 'memId',
		width : 80,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "用户名称",
		dataIndex : 'userName',
		width : 80,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "等级名称",
		dataIndex : 'levelName',
		width : 80,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "积分",
		dataIndex : 'points',
		width : 80,
		sortable : false,
		align : 'right',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "消费剩余积分",
		dataIndex : 'surplusPoints',
		width : 80,
		sortable : false,
		align : 'right',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "电话",
		dataIndex : 'mobile',
		width : 120,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "性别",
		dataIndex : 'sex',
		width : 60,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			if (v == 'F') {
				v = '女';
			} else {
				v = '男';
			}
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "生日",
		dataIndex : 'birthday',
		width : 80,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			v = Ext.util.Format.date(v, 'Y-m-d');
			p.attr = 'ext:qtip="' + v + '"';
			return v;
		}
	}, {
		header : "地址",
		dataIndex : 'address',
		width : 200,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "邮政编码",
		dataIndex : 'postalCode',
		width : 80,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "职业",
		dataIndex : 'profession',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "学历",
		dataIndex : 'education',
		width : 80,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "婚姻状况",
		dataIndex : 'maritalStatus',
		width : 60,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			if (v == 'N') {
				v = '未婚';
			} else {
				v = '已婚';
			}
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "结婚纪念日",
		dataIndex : 'weddingDay',
		width : 80,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			v = Ext.util.Format.date(v, 'Y-m-d');
			p.attr = 'ext:qtip="' + v + '"';
			return v;
		}
	}]);

	grid = new Ext.grid.EditorGridPanel({
				store : store,
				cm : cm,
				renderTo : 'gridList',
				height : height,
				loadMask : true,
				autoScroll : true,
				enableHdMenu : false,
				// viewConfig : {
				// forceFit : true
				// },
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

	store.setDefaultSort('memId', 'DESC');
	search();

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});
});

function search() {
	store.baseParams.userName = encodeURIComponent($('#userName').val());
	store.baseParams.levelId = $('#levelId').val();

	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function resets() {
	$('#userName').val('');
	$('#levelName').val('');
}

function reload() {
	store.reload();
}

function exportMemberData() {
	var form = window.document.forms[0];
	form.action = appUrl + "/member/memberAction!exportMemberData.htm";
	form.target = "_blank";
	form.submit();
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
