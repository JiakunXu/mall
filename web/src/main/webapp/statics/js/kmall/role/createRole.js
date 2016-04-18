Ext.onReady(function() {
	// tips
	Ext.QuickTips.init();

	new Ext.form.TextField({
				id : "roleId",
				allowBlank : false,
				maxLength : 30,
				applyTo : 'roleId'
			});

	new Ext.form.TextField({
				id : "roleName",
				allowBlank : false,
				maxLength : 80,
				applyTo : 'roleName'
			});

	var store1 = new Ext.data.SimpleStore({
				fields : ['itemId', 'itemName'],
				data : [['Y', '是'], ['N', '否']]
			});

	defaults = new Ext.form.ComboBox({
				id : 'defaults',
				store : store1,
				mode : 'local',
				displayField : 'itemName',
				valueField : 'itemId',
				hiddenName : 'role.defaults',
				allowBlank : false,
				editable : false,
				triggerAction : 'all',
				applyTo : 'defaults'
			});

	defaults.setValue('N');

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
				+ '/dict/dictAction!getDictList4ComboBox.htm?dictTypeValue=roleType',
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
				hiddenName : 'role.type',
				editable : false,
				triggerAction : 'all',
				applyTo : 'type'
			});

	store1.load({
				callback : function(records, options, success) {
					store1.each(function(record) {
								if ('S' == record.get('dictValue')) {
									store1.remove(record);
								}
							});
				}
			});

	$('#hideFrame').bind('load', promgtMsg);
});

var type = 0;

function save(n) {
	type = n;

	var p = Ext.getCmp("roleId").validate();
	var n = Ext.getCmp("roleName").validate();
	var c = Ext.getCmp("type").validate();

	if (!(p && n && c)) {
		return;
	}

	var pp = Ext.getCmp("roleId").getValue().substring(0, 1);
	if (pp == 'Z' || pp == 'z' || pp == 'Y' || pp == 'y') {
		warn("Z和Y开头的角色编号为ERP中保留编号，请重新命名！");
		return;
	}

	var form = window.document.forms[0];
	form.action = "roleAction!createRole.htm";
	form.target = "hideFrame";
	form.submit();

	Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
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
							window.parent.opener.search();

							if (type == 0) {
								window.close();
							} else {
								$("#roleId").val('');
								$("#roleName").val('');
								$("#remark").val('');
								$("#type").val('');
							}
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