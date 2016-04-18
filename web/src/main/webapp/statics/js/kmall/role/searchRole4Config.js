Ext.onReady(function() {
	Ext.QuickTips.init();

	new Ext.form.TextField({
				id : "roleId",
				allowBlank : true,
				applyTo : "roleId"
			});

	new Ext.form.TextField({
				id : "roleName",
				allowBlank : true,
				applyTo : "roleName"
			});

	Item = Ext.data.Record.create([{
				name : 'roleId',
				type : 'string'
			}, {
				name : 'roleName',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}]);

	var url;

	if (menuId != '') {
		url = appUrl + '/role/roleAction!getRole4ConfigJsonList.htm'
				+ '?menuId=' + menuId
	}

	store = new Ext.data.Store({
				url : url,
				reader : new Ext.data.SimpleJsonReader({
							id : 'roleId'
						}, Item),
				remoteSort : true
			})

	cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		id : "roleId",
		header : "角色ID",
		width : 80,
		dataIndex : 'roleId',
		sortable : true,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "角色名称",
		dataIndex : 'roleName',
		width : 200,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "角色描述",
		dataIndex : 'remark',
		width : 80,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}])

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
			})

	store.setDefaultSort('roleId', 'DESC');

	search();
})

function search() {
	store.baseParams.roleId = $('#roleId').val();
	store.baseParams.roleName = encodeURIComponent($('#roleName').val());
	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function resets() {
	$("#roleId").val("");
	$("#roleName").val("");
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