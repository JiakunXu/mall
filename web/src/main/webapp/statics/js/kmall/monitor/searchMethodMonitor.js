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

	className = new Ext.form.TextField({
				id : "className",
				applyTo : 'className'
			});

	methodName = new Ext.form.TextField({
				id : "methodName",
				applyTo : 'methodName'
			});

	var item = Ext.data.Record.create([{
				name : 'methodMonitorId',
				type : 'string'
			}, {
				name : 'className',
				type : 'string'
			}, {
				name : 'createDate',
				type : 'date'
			}, {
				name : 'methodName',
				type : 'string'
			}, {
				name : 'cost',
				type : 'int'
			}]);

	store = new Ext.data.Store({
				url : appUrl
						+ '/monitor/methodMonitorAction!getMethodMonitorJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'methodMonitorId'
						}, item),
				remoteSort : true
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "编号",
		dataIndex : 'methodMonitorId',
		width : 20,
		sortable : false,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "类名",
		dataIndex : 'className',
		width : 100,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "方法名",
		dataIndex : 'methodName',
		width : 40,
		sortable : false,
		align : 'left',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			return "<a href=javascript:getConcurrency('" + rowIndex + "');>"
					+ Ext.util.Format.htmlEncode(value) + "</a>";
		}
	}, {
		header : "执行时间（毫秒）",
		dataIndex : 'cost',
		width : 30,
		sortable : false,
		align : 'right',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "创建时间",
		dataIndex : 'createDate',
		width : 70,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			v = Ext.util.Format.date(v.toUTC(), 'Y-m-d H:i:s');
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

	store.setDefaultSort('createDate', 'DESC');

	search();

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});
});

function search() {
	store.baseParams.gmtStart = $('#gmtStart').val();
	store.baseParams.gmtEnd = $('#gmtEnd').val();
	store.baseParams.className = encodeURIComponent(className.getValue());
	store.baseParams.methodName = encodeURIComponent(methodName.getValue());
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
	$("#className").val("");
	$("#methodName").val("");
}

function getConcurrency(rowIndex) {
	var className = store.getAt(rowIndex).get("className");
	var methodName = store.getAt(rowIndex).get("methodName");

	$.ajax({
				type : "post",
				url : appUrl + "/monitor/methodMonitorAction!get.htm",
				data : {
					key : methodName + "@" + className,
					dateTime : new Date().getTime()
				},
				success : function(data) {
					info("并发请求：" + data);
				},
				error : function() {

				}
			});
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

function info(msg) {
	Ext.Msg.show({
				title : '信息',
				msg : msg,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.INFO
			});
}