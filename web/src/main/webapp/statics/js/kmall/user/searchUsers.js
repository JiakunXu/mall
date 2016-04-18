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

	var sm = new Ext.grid.CheckboxSelectionModel();

	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
				renderer : function(v, p, record, rowIndex) {
					return record.store.lastOptions.params.start + rowIndex + 1;
				}
			}), {
		header : "用户帐号",
		dataIndex : 'passport',
		width : 70,
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
	}, {
		header : "状态",
		dataIndex : 'state',
		sortable : false,
		align : 'center',
		width : 50,
		renderer : function(v, p) {
			if (v == 'U') {
				return "<p style='color:green'>正常</p>";
			} else if (v == 'F') {
				return "<p style='color:red'>禁用</p>";
			}
		}
	}, {
		header : "密码过期时间",
		dataIndex : 'passwordExpireDate',
		sortable : false,
		align : 'center',
		width : 80,
		renderer : function(v, p) {
			v = Ext.util.Format.date(v, 'Y-m-d');
			p.attr = 'ext:qtip="' + v + '"';
			return v;
		}
	}, {
		header : "操作",
		width : 650,
		sortable : false,
		align : 'left',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			var s = record.get("state");

			var strReturn = "";
			if ('true' == userRole) {
				strReturn += "<a href=javascript:searchUserRole('" + rowIndex
						+ "') > 维护临时角色 </a>|";
			}

			if ('true' == userSAPar) {
				strReturn += "<a href=javascript:searchUserSAPar('" + rowIndex
						+ "') > 维护ERP用户参数 </a>|"
			}

			if ('true' == userSAProfile) {
				strReturn += "<a href=javascript:searchUserSAProfile('"
						+ rowIndex + "') > 维护ERP用户参数文件 </a>|"
			}

			if ('true' == userSAPAuth) {
				strReturn += "<a href=javascript:searchUserSAPAuth('"
						+ rowIndex + "') > 维护HR参数文件 </a>|"
			}

			strReturn += "<a href=javascript:searchUserStation('" + rowIndex
					+ "') > 查看岗位 </a>|";

			strReturn += "<a href=javascript:searchUserPosition('" + rowIndex
					+ "') > 查看编制 </a>|";

			strReturn += "<a href=javascript:updateUserOrg('" + rowIndex
					+ "') > 维护组织 </a>|";

			strReturn += "<a href=javascript:resetPassword('" + rowIndex
					+ "') > 重置密码 </a>|";

			if (s == 'U') {
				strReturn += "<a href=javascript:disableAccount('" + rowIndex
						+ "') > 禁用</a>";
			} else if (s == 'F') {
				strReturn += "<a href=javascript:enableAccount('" + rowIndex
						+ "') > 启用</a>";
			}

			return strReturn;
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

function disableAccount(rowIndex) {
	var userId = store.getAt(rowIndex).get("userId");
	Ext.Msg.confirm("提示", "确认禁用帐号？", function(button) {
				if (button == 'yes') {
					document.getElementById("userId").value = userId;

					var params = [];
					document.getElementById("userList").value = Ext.util.JSON
							.encode(params);

					var form = window.document.forms[0];
					form.action = "userAction!disableAccount.htm";
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});
}

function enableAccount(rowIndex) {
	var userId = store.getAt(rowIndex).get("userId");
	Ext.Msg.confirm("提示", "确认启用帐号？", function(button) {
				if (button == 'yes') {
					document.getElementById("userId").value = userId;

					var params = [];
					document.getElementById("userList").value = Ext.util.JSON
							.encode(params);

					var form = window.document.forms[0];
					form.action = "userAction!enableAccount.htm";
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});
}

function searchUserInfo(a) {
	var WWidth = 550;
	var WHeight = 600;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	var url = appUrl + "/user/userAction!searchUser.htm?userId=" + a;
	window.open(url, "searchUserInfo", "left=" + WLeft + ",top=" + WTop
					+ ",width=" + WWidth + ",height=" + WHeight);
}

function searchUserRole(rowIndex) {
	var id = store.getAt(rowIndex).get("userId");
	var passport = encodeURIComponent(store.getAt(rowIndex).get("passport"));
	var userName = encodeURIComponent(store.getAt(rowIndex).get("userName"));

	var WWidth = 800;
	var WHeight = 680;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	var url = appUrl + "/role/roleAction!searchUserRole.htm?userId=" + id
			+ "&passport=" + passport + "&userName=" + userName;
	window.open(url, "searchUserRole", "left=" + WLeft + ",top=" + WTop
					+ ",width=" + WWidth + ",height=" + WHeight);
}

function searchUserSAPar(rowIndex) {
	var id = store.getAt(rowIndex).get("userId");
	var name = encodeURIComponent(store.getAt(rowIndex).get("userName"));
	var WWidth = 500;
	var WHeight = 470;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	var url = appUrl + "/user/userSAParAction!searchUserSAPar.htm?userId=" + id
			+ "&userName=" + name;
	window.open(url, "searchUserSAPar", "left=" + WLeft + ",top=" + WTop
					+ ",width=" + WWidth + ",height=" + WHeight);
}

function searchUserSAProfile(rowIndex) {
	var id = store.getAt(rowIndex).get("userId");
	var name = encodeURIComponent(store.getAt(rowIndex).get("userName"));
	var WWidth = 500;
	var WHeight = 470;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	var url = appUrl
			+ "/user/userSAProfileAction!searchUserSAProfile.htm?userId=" + id
			+ "&userName=" + name;
	window.open(url, "searchUserSAProfile", "left=" + WLeft + ",top=" + WTop
					+ ",width=" + WWidth + ",height=" + WHeight);
}

function searchUserSAPAuth(rowIndex) {
	var id = store.getAt(rowIndex).get("userId");
	var name = encodeURIComponent(store.getAt(rowIndex).get("userName"));
	var WWidth = 650;
	var WHeight = 470;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	var url = appUrl + "/user/userSAPAuthAction!searchUserSAPAuth.htm?userId="
			+ id + "&userName=" + name;
	window.open(url, "searchUserSAPAuth", "left=" + WLeft + ",top=" + WTop
					+ ",width=" + WWidth + ",height=" + WHeight);
}

function searchUserStation(rowIndex) {
	var id = store.getAt(rowIndex).get("userId");
	var name = encodeURIComponent(store.getAt(rowIndex).get("userName"));
	var WWidth = 500;
	var WHeight = 470;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	var url = appUrl + "/station/stationAction!searchUserStation.htm?userIds="
			+ id + "&userName=" + name;
	window.open(url, "searchUserStation", "left=" + WLeft + ",top=" + WTop
					+ ",width=" + WWidth + ",height=" + WHeight);
}

function searchUserPosition(rowIndex) {
	var id = store.getAt(rowIndex).get("userId");
	var name = encodeURIComponent(store.getAt(rowIndex).get("userName"));
	var WWidth = 500;
	var WHeight = 470;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	var url = appUrl
			+ "/position/positionAction!searchUserPosition.htm?userIds=" + id
			+ "&userName=" + name;
	window.open(url, "searchUserPosition", "left=" + WLeft + ",top=" + WTop
					+ ",width=" + WWidth + ",height=" + WHeight);
}

function updatePasswordExpireDate() {
	Ext.Msg.confirm("提示", "确认批量设置密码过期？", function(button) {
				if (button == 'yes') {
					var rows = grid.getSelectionModel().getSelections();

					if (rows == "") {
						Ext.Msg.show({
									title : '提示',
									msg : '请勾选要设置用户的数据',
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
						return;
					}

					var params = [];
					Ext.each(rows, function(value, index, a) {
								params.push({
											"userId" : value.get("userId")
										});
							});

					document.getElementById("userList").value = Ext.util.JSON
							.encode(params);

					var form = window.document.forms[0];
					form.action = appUrl
							+ "/user/userAction!updatePasswordExpireDate.htm";
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});
}

function resetPassword(rowIndex) {
	Ext.MessageBox.prompt("重置帐号密码", "请输入密码(密码输入长度至少6位): ",
			function(btn, value) {
				if (btn == 'ok') {
					if (value == '') {
						warn("请输入密码");
					} else {
						if (value.length < 6) {
							warn("密码输入长度至少6位！");
							return;
						}

						var params = [];
						document.getElementById("userList").value = Ext.util.JSON
								.encode(params);

						document.getElementById("userId").value = store
								.getAt(rowIndex).get("userId");
						document.getElementById("password").value = value;

						var form = window.document.forms[0];
						form.action = appUrl
								+ "/user/userAction!resetPassword.htm";
						form.target = "hideFrame";
						form.submit();

						Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
					}
				}
			});
}

function createUser() {
	var WWidth = 900;
	var WHeight = 600;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/user/userAction!createUserPrepare.htm",
			"createUser", "left=" + WLeft + ",top=" + WTop + ",width=" + WWidth
					+ ",height=" + WHeight);
}

function updateUserOrg(rowIndex) {
	var params = [];
	params.push({
				"userId" : store.getAt(rowIndex).get("userId")
			});
	var userName = encodeURIComponent(store.getAt(rowIndex).get("userName"));
	var orgName = encodeURIComponent(store.getAt(rowIndex).get("orgName"));
	var WWidth = 500;
	var WHeight = 400;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/user/userAction!updateUserOrgPrepare.htm?userId="
					+ Ext.util.JSON.encode(params) + "&userName=" + userName
					+ "&orgName=" + orgName, "updateUserOrg", "left=" + WLeft
					+ ",top=" + WTop + ",width=" + WWidth + ",height="
					+ WHeight);
}

function updateUsersOrg() {
	var rows = grid.getSelectionModel().getSelections();

	if (rows == "") {
		Ext.Msg.show({
					title : '提示',
					msg : '请勾选要设置用户的数据',
					buttons : Ext.Msg.OK,
					icon : Ext.Msg.ERROR
				});
		return;
	}

	var params = [];
	var userName = '';
	Ext.each(rows, function(value, index, a) {
				params.push({
							"userId" : value.get("userId")
						});
				if (userName != '') {
					userName += ",";
				}
				userName += value.get("userName");
			});

	var WWidth = 500;
	var WHeight = 400;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/user/userAction!updateUserOrgPrepare.htm?userId="
					+ Ext.util.JSON.encode(params) + "&userName=" + userName,
			"updateUsersOrg", "left=" + WLeft + ",top=" + WTop + ",width="
					+ WWidth + ",height=" + WHeight);

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

function warn(msg) {
	Ext.Msg.show({
				title : '警告',
				msg : msg,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.WARNING
			});
}