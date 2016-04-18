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

	var Item = Ext.data.Record.create([{
				name : 'userId',
				type : 'string'
			}, {
				name : 'userName',
				type : 'string'
			}, {
				name : 'dn',
				type : 'string'
			}, {
				name : 'passport',
				type : 'string'
			}, {
				name : 'password',
				type : 'string'
			}]);

	store = new Ext.data.Store({
				url : appUrl
						+ '/user/userCordysAction!getUserCordysJsonList.htm',
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
		width : 50,
		sortable : true,
		align : 'left',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			return "<a href=javascript:searchUserInfo('" + record.get('userId')
					+ "');>" + Ext.util.Format.htmlEncode(value) + "</a>";
		}
	}, {
		header : "用户名称",
		dataIndex : 'userName',
		width : 70,
		sortable : true,
		align : 'center',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "Cordys用户信息",
		dataIndex : 'dn',
		width : 200,
		sortable : false,
		align : 'left',
		renderer : function(v, p) {
			p.attr = 'ext:qtip="' + v + '"';
			return Ext.util.Format.htmlEncode(v);
		}
	}, {
		header : "Cordys密码",
		dataIndex : 'password',
		width : 50,
		sortable : false,
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

	store.setDefaultSort('userName', 'ASC');

	search();

	$('#hideFrame').bind('load', promgtMsg);

	$(window).bind("resize", function(e) {
				grid.getView().refresh();
			});
});

function search() {
	store.baseParams.passport = encodeURIComponent($("#passport").val());
	store.baseParams.userName = encodeURIComponent($("#userName").val());

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
}

function reload() {
	store.reload();
}

function syncUser2Cordys() {
	Ext.Msg.confirm("提示", "确认同步Cordys用户？", function(button) {
		if (button == 'yes') {
			var form = window.document.forms[0];
			form.action = appUrl + "/user/userCordysAction!syncUser2Cordys.htm";
			form.target = "hideFrame";
			form.submit();

			Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
		}
	});
}

function searchUserInfo(a) {
	var WWidth = 450;
	var WHeight = 520;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	var url = appUrl + "/user/userAction!searchUserInfo.htm?userId=" + a;
	window.open(url, "searchUserInfo", "left=" + WLeft + ",top=" + WTop
					+ ",width=" + WWidth + ",height=" + WHeight);
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
				}, 1000);
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

function warn(msg) {
	Ext.Msg.show({
				title : '警告',
				msg : msg,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.WARNING
			});
}