Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	new Ext.form.TextField({
				id : "userName",
				allowBlank : false,
				applyTo : 'userName'
			});

	new Ext.form.TextField({
				id : "passport",
				allowBlank : false,
				applyTo : 'passport'
			});

	new Ext.form.TextField({
				id : "password",
				allowBlank : false,
				applyTo : 'password'
			});

	new Ext.form.TextField({
				id : "orgName",
				allowBlank : false,
				readOnly : true,
				applyTo : 'orgName'
			});

	new Ext.form.TextField({
				id : "phone",
				applyTo : 'phone'
			});

	new Ext.form.TextField({
				id : "mobile",
				allowBlank : false,
				applyTo : 'mobile',
				regex : /(^0?[1][3|4|5|7|8][0-9]{9}$)/,
				regexText : '请输入正确的手机号码'
			});

	new Ext.form.TextField({
				id : "email",
				allowBlank : false,
				vtype : "email",
				applyTo : 'email'
			});

	new Ext.form.TextField({
				id : "workFax",
				applyTo : 'workFax'
			});

	new Ext.form.TextField({
				id : "address",
				applyTo : 'address'
			});

	var p = new Ext.plugins.PositionSelector({
				url : appUrl
						+ '/position/positionAction!getPositionJsonList.htm',
				triggerAction : 'power',
				searchable : true,
				multiable : false,
				allowBlank : false,
				displayField : 'posName',
				beforeexpand : function() {
					var o = $("#orgId").val();
					if (o == '') {
						warn('请先选择组织！');
						return false;
					}

					p.editor.setConfigParams({
								orgId : o
							});
				},
				hiddenField : {
					posId : 'posId'
				}
			});

	p.editor.applyToMarkup('posName');

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

	var typeField = new Ext.form.ComboBox({
				id : 'type',
				store : store1,
				mode : 'local',
				displayField : 'dictName',
				valueField : 'dictValue',
				hiddenName : 'user.type',
				editable : false,
				triggerAction : 'all',
				applyTo : 'type'
			});

	store1.load({
				callback : function(records, options, success) {
					typeField.setValue(1);
				}
			});

	var store2 = new Ext.data.SimpleStore({
				fields : ['itemId', 'itemName'],
				data : [['ZH', '中文'], ['EN', '英文']]
			});

	langu = new Ext.form.ComboBox({
				id : 'langu',
				store : store2,
				mode : 'local',
				displayField : 'itemName',
				valueField : 'itemId',
				hiddenName : 'user.langu',
				editable : false,
				triggerAction : 'all',
				applyTo : 'langu'
			});

	langu.setValue('ZH');

	$('#hideFrame').bind('load', promgtMsg);
});

function save() {
	var u = Ext.getCmp("userName").validate();
	var pp = Ext.getCmp("passport").validate();
	var pw = Ext.getCmp("password").validate();
	var o = Ext.getCmp("orgName").validate();
	var e = Ext.getCmp("email").validate();
	var t = Ext.getCmp("type").validate();
	var m = Ext.getCmp("mobile").validate();

	if (!(u && pp && pw && o && e && t && m)) {
		return;
	}

	var form = window.document.forms[0];
	form.action = "userAction!createUser.htm";
	form.target = "hideFrame";
	form.submit();

	Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
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

		// 清空编制
		$("#posId").val('');
		$("#posName").val('');
	}

	Ext.getCmp("orgName").validate();
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
					buttons : Ext.Msg.OK,
					fn : function(btn) {
						if (btn == 'ok') {
							window.parent.opener.reload();
							window.close();
						}
					},
					icon : Ext.Msg.INFO
				});
	}
}

function warn(msg) {
	Ext.Msg.show({
				title : '警告',
				msg : msg,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.WARNING
			});
}