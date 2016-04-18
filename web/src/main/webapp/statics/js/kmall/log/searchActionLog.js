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
				applyTo : 'gmtEnd',
				format : 'Y-m-d',
				editable : false
			});

	passportField = new Ext.form.TextField({
				id : "passport",
				applyTo : 'passport'
			});

	var Item = Ext.data.Record.create([{
				name : 'id',
				type : 'long'
			}, {
				name : 'actionName',
				type : 'string'
			}, {
				name : 'passport',
				type : 'string'
			}, {
				name : 'userName',
				type : 'string'
			}, {
				name : 'ip',
				type : 'string'
			}, {
				name : 'createDate',
				type : 'string'
			}]);

	store = new Ext.data.Store({
				url : appUrl
						+ ((type == 'i')
								? '/log/logAction!getMyActionLogJsonList.htm'
								: '/log/logAction!getActionLogJsonList.htm'),
				reader : new Ext.data.SimpleJsonReader({
							id : 'id'
						}, Item),
				remoteSort : true
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "ID",
		dataIndex : 'id',
		width : 50,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "操作人",
		dataIndex : 'userName',
		width : 100,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "操作人帐号",
		dataIndex : 'passport',
		width : 100,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "操作方法",
		dataIndex : 'actionName',
		width : 100,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "操作时间",
		dataIndex : 'createDate',
		width : 80,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "IP",
		dataIndex : 'ip',
		width : 80,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
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

	store.setDefaultSort('id', 'DESC');

	search();

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});
});

function search() {
	store.baseParams.passport = encodeURIComponent(passportField.getValue());
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
	$("#passport").val("");
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

document.onkeydown = function(e) {
	var theEvent = e || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		search();
		return false;
	}
	return true;
}