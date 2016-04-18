Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	new Ext.form.TextField({
				id : "passport",
				applyTo : 'passport'
			});

	new Ext.form.TextField({
				id : "userName",
				applyTo : 'userName'
			});

	new Ext.form.TextField({
				id : "orgName",
				readOnly : true,
				applyTo : 'orgName'
			});

	new Ext.form.TextField({
				id : "phone",
				applyTo : 'phone'
			});

	new Ext.form.TextField({
				id : "mobile",
				applyTo : 'mobile'
			});

	var item1 = Ext.data.Record.create([{
				name : 'dictId',
				type : 'string'
			}, {
				name : 'dictName',
				type : 'string'
			}, {
				name : 'dictValue',
				type : 'string'
			}]);

	store1 = new Ext.data.Store({
		url : appUrl
				+ '/dict/dictAction!getDictList4ComboBox.htm?dictTypeValue=userType',
		reader : new Ext.data.SimpleJsonReader({
					id : 'dictId'
				}, item1),
		remoteSort : true
	});

	typeField = new Ext.form.ComboBox({
				id : 'type',
				store : store1,
				mode : 'local',
				displayField : 'dictName',
				valueField : 'dictValue',
				editable : false,
				triggerAction : 'all',
				applyTo : 'type'
			});

	store1.load({
				callback : function(records, options, success) {
					store1.add(new item1({
								dictName : '所有用户类型',
								dictValue : ""
							}));
					typeField.setValue("1");
				}
			});

	var Item = Ext.data.Record.create([{
				name : 'userId',
				type : 'string'
			}, {
				name : 'passport',
				type : 'string'
			}, {
				name : 'userName',
				type : 'string'
			}, {
				name : 'phone',
				type : 'string'
			}, {
				name : 'mobile',
				type : 'string'
			}, {
				name : 'email',
				type : 'string'
			}, {
				name : 'address',
				type : 'string'
			}, {
				name : 'state',
				type : 'string'
			}, {
				name : 'orgName',
				type : 'string'
			}, {
				name : 'passwordExpireDate',
				type : 'date'
			}]);

	store = new Ext.data.Store({
				url : appUrl + '/user/userAction!getUserJsonList.htm',
				reader : new Ext.data.SimpleJsonReader({
							id : 'userId'
						}, Item),
				remoteSort : true
			});

	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "用户帐号",
		dataIndex : 'passport',
		width : 70,
		sortable : true,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "用户名称",
		dataIndex : 'userName',
		width : 70,
		sortable : true,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "所属组织",
		dataIndex : 'orgName',
		width : 70,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "办公电话",
		dataIndex : 'phone',
		width : 80,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "手机",
		dataIndex : 'mobile',
		sortable : false,
		align : 'left',
		width : 80,
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "邮箱地址",
		dataIndex : 'email',
		width : 130,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "通信地址",
		dataIndex : 'address',
		sortable : false,
		align : 'left',
		width : 100,
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

	store.setDefaultSort('userName', 'ASC');

	search();

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});
});

function search() {
	store.baseParams.passport = encodeURIComponent($("#passport").val());
	store.baseParams.userName = encodeURIComponent($("#userName").val());
	store.baseParams.orgId = $("#orgId").val();
	store.baseParams.phone = encodeURIComponent($("#phone").val());
	store.baseParams.mobile = encodeURIComponent($("#mobile").val());
	store.baseParams.type = typeField.getValue();
	store.baseParams.include = (document.getElementById("include").checked)
			? 'y'
			: '';

	store.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
}

function resets() {
	$("#passport").val("");
	$("#userName").val("");
	$("#orgId").val("");
	$("#orgName").val("");
	$("#phone").val("");
	$("#mobile").val("");
	typeField.setValue("");
}

function reload() {
	store.reload();
}

function getOrgTree() {
	var form = window.document.forms[0];

	var URL = appUrl + "/org/orgTreeAction!getOrgAllTree.htm";
	var windowhight = 900;
	var windowwidth = 500;
	var top = (window.screen.availHeight - windowhight) / 2;
	var left = (window.screen.availWidth - windowwidth) / 2;
	var x = window.showModalDialog(URL, form,
			"dialogWidth=300px;dialogHeight=400px;");

	if (x == undefined) {
	} else {
		document.all.orgId.value = x[0];
		document.all.orgName.value = x[1];
	}
}

var tr = false;

function trigger() {
	if (tr) {
		$("#more1").show();
		$("#more2").show();
		$("#trigger").text("收起更多");
		tr = false;
	} else {
		$("#more1").hide();
		$("#more2").hide();
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
