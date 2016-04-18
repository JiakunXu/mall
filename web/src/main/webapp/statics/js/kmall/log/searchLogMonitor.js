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

	methodNameField = new Ext.form.TextField({
				id : "methodName",
				applyTo : 'methodName'
			});

	var Item = Ext.data.Record.create([{
				name : 'logMonitorId',
				type : 'long'
			}, {
				name : 'className',
				type : 'string'
			}, {
				name : 'methodName',
				type : 'string'
			}, {
				name : 'lineNumber',
				type : 'string'
			}, {
				name : 'message',
				type : 'string'
			}, {
				name : 'e',
				type : 'string'
			}, {
				name : 'createDate',
				type : 'string'
			}]);

	store = new Ext.data.Store({
				url : appUrl
						+ '/monitor/logMonitorAction!getLogMonitorJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'logMonitorId'
						}, Item),
				remoteSort : true
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "ID",
		dataIndex : 'logMonitorId',
		width : 50,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "Create Date",
		dataIndex : 'createDate',
		width : 80,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "class Name",
		dataIndex : 'className',
		width : 100,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'style="white-space:normal;"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "Method Name",
		dataIndex : 'methodName',
		width : 100,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'style="white-space:normal;"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "Line Number",
		dataIndex : 'lineNumber',
		width : 50,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "Message",
		dataIndex : 'message',
		width : 300,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'style="white-space:normal;"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "e",
		dataIndex : 'e',
		width : 1000,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'style="white-space:normal;"';
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

	store.setDefaultSort('logMonitorId', 'DESC');

	search();

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});
});

function search() {
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
	$("#methodName").val("");
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