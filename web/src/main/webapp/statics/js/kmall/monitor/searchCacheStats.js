Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	var Item = Ext.data.Record.create([{
				name : 'address',
				type : 'string'
			}, {
				name : 'uptime',
				type : 'string'
			}, {
				name : 'version',
				type : 'string'
			}, {
				name : 'bytes',
				type : 'string'
			}, {
				name : 'limitMaxbytes',
				type : 'string'
			}, {
				name : 'currItems',
				type : 'string'
			}, {
				name : 'getHits',
				type : 'string'
			}, {
				name : 'getMisses',
				type : 'string'
			}, {
				name : 'totalItems',
				type : 'string'
			}, {
				name : 'cmdGet',
				type : 'string'
			}, {
				name : 'currConnections',
				type : 'string'
			}, {
				name : 'totalConnections',
				type : 'string'
			}, {
				name : 'bytesWritten',
				type : 'string'
			}, {
				name : 'bytesRead',
				type : 'string'
			}]);

	store = new Ext.data.Store({
				url : appUrl
						+ '/monitor/cacheMonitorAction!getCacheStatsJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'address'
						}, Item),
				remoteSort : true
			});

	var cm = new Ext.grid.ColumnModel([{
				header : "Memcached Hosts",
				dataIndex : 'address',
				width : 220,
				align : 'center',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "Uptime",
				dataIndex : 'uptime',
				width : 120,
				align : 'center',
				renderer : function(v, p) {
					d = parseInt(v / 86400);
					h = parseInt((v % 86400) / 3600);
					s = parseInt((v % 3600) / 60);
					v = v % 60;

					var str = d + "d " + h + "h " + s + "m " + v + "s";
					p.attr = 'ext:qtip="' + str + '"';
					return Ext.util.Format.htmlEncode(str);
				}
			}, {
				header : "Memcached Server Version",
				dataIndex : 'version',
				width : 80,
				align : 'center',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "Used Cache Size",
				dataIndex : 'bytes',
				width : 120,
				align : 'center',
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					var bytes = parseFloat(record.get('bytes'));
					var limitMaxbytes = parseFloat(record.get('limitMaxbytes'));

					var str = parseInt(bytes / 1048576 * 1000) / 1000 + ' M'
							+ " (" + parseInt(bytes / limitMaxbytes * 10000)
							/ 100 + "%)";

					cellmeta.attr = 'ext:qtip="' + str + '"';
					return str;
				}
			}, {
				header : "Total Cache Size",
				dataIndex : 'limitMaxbytes',
				width : 80,
				align : 'center',
				renderer : function(v, p) {
					v = parseFloat(v);
					var str = parseInt(v / 1048576 * 1000) / 1000 + ' M';
					p.attr = 'ext:qtip="' + str + '"';
					return Ext.util.Format.htmlEncode(str);
				}
			}, {
				header : "Current Items",
				dataIndex : 'currItems',
				width : 80,
				align : 'center',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "Total Items",
				dataIndex : 'totalItems',
				width : 80,
				align : 'center',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "Get Hits",
				dataIndex : 'getHits',
				width : 120,
				align : 'center',
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					var getHits = parseFloat(record.get('getHits'));
					var cmdGet = parseFloat(record.get('cmdGet'));

					var str = getHits + " ("
							+ parseInt(getHits / cmdGet * 10000) / 100 + "%)";

					cellmeta.attr = 'ext:qtip="' + str + '"';
					return str;
				}
			}, {
				header : "Get Misses",
				dataIndex : 'getMisses',
				width : 80,
				align : 'center',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "Total Gets",
				dataIndex : 'cmdGet',
				width : 80,
				align : 'center',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "Curr Connections",
				dataIndex : 'currConnections',
				width : 80,
				align : 'center',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "Total Connections",
				dataIndex : 'totalConnections',
				width : 80,
				align : 'center',
				renderer : function(v, p) {
					p.attr = 'ext:qtip="' + v + '"';
					return Ext.util.Format.htmlEncode(v);
				}
			}, {
				header : "Bytes Read",
				dataIndex : 'bytesRead',
				width : 80,
				align : 'center',
				renderer : function(v, p) {
					v = parseFloat(v);
					var str = parseInt(v / 1048576 * 1000) / 1000 + ' M';
					p.attr = 'ext:qtip="' + str + '"';
					return Ext.util.Format.htmlEncode(str);
				}
			}, {
				header : "Bytes Written",
				dataIndex : 'bytesWritten',
				width : 80,
				align : 'center',
				renderer : function(v, p) {
					v = parseFloat(v);
					var str = parseInt(v / 1048576 * 1000) / 1000 + ' M';
					p.attr = 'ext:qtip="' + str + '"';
					return Ext.util.Format.htmlEncode(str);
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
							emptyMsg : "没有找到记录！"
						})
			});

	search();

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});
});

function search() {
	store.load();
}

function config() {
	var WWidth = 500;
	var WHeight = 250;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/monitor/cacheMonitorAction!configCachePrepare.htm",
			"config", "left=" + WLeft + ",top=" + WTop + ",width=" + WWidth
					+ ",height=" + WHeight);
}