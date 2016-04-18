Ext.onReady(function() {
			// tips
			Ext.QuickTips.init();

			var item = Ext.data.Record.create([{
						name : 'userId',
						type : 'string'
					}, {
						name : 'userName',
						type : 'string'
					}, {
						name : 'passport',
						type : 'string'
					}]);

			store = new Ext.data.Store({
						url : appUrl + '/user/userAction!getUsersJsonList.htm',
						reader : new Ext.data.SimpleJsonReader({
									id : 'userId'
								}, item),
						remoteSort : true
					});

			var sm = new Ext.grid.CheckboxSelectionModel({
						header : '',
						singleSelect : true
					});

			var cm = new Ext.grid.ColumnModel([sm, {
						id : 'userId',
						header : "编号",
						dataIndex : 'userId',
						width : 20,
						sortable : false,
						align : 'left',
						renderer : function(v, p) {
							p.attr = 'ext:qtip="' + v + '"';
							return Ext.util.Format.htmlEncode(v);
						}
					}, {
						id : 'passport',
						header : "人员帐号",
						dataIndex : 'passport',
						width : 40,
						sortable : false,
						align : 'left',
						renderer : function(v, p) {
							p.attr = 'ext:qtip="' + v + '"';
							return Ext.util.Format.htmlEncode(v);
						}
					}, {
						id : 'userName',
						header : "姓名 ",
						dataIndex : 'userName',
						width : 50,
						sortable : false,
						align : 'left',
						renderer : function(v, p) {
							p.attr = 'ext:qtip="' + v + '"';
							return Ext.util.Format.htmlEncode(v);
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
						plugins : [new Ext.plugins.HeaderAligning()]
					});

			store.baseParams.orgId = orgId;

			store.load();
		});

function save() {
	var rows = grid.getSelectionModel().getSelections();
	if (rows.length < 1) {
		warn("请选择数据！");
		return;
	}

	var s = new Array();
	Ext.each(rows, function(value, index, a) {
				s[0] = value.get('userId');
				s[1] = value.get('userName');
			});

	window.parent.returnValue = s;
	window.parent.close();
}

function warn(msg) {
	Ext.Msg.show({
				title : '警告',
				msg : msg,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.WARNING
			});
}