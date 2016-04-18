Ext.onReady(function() {

	Ext.QuickTips.init();

	new Ext.form.TextField({
				id : "orgName",
				allowBlank : false,
				applyTo : 'orgName'
			});

	new Ext.form.TextField({
				id : "shortName",
				applyTo : 'shortName'
			});

	new Ext.form.TextField({
				id : "orgParentName",
				allowBlank : false,
				readOnly : true,
				grow : true,
				applyTo : 'orgParentName'
			});

	new Ext.form.TextArea({
				id : "remark",
				width : 300,
				grow : true,
				applyTo : 'remark'
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
				+ '/dict/dictAction!getDictList4ComboBox.htm?dictTypeValue=orgType',
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
				hiddenName : 'org.type',
				editable : false,
				triggerAction : 'all',
				applyTo : 'type'
			});

	store1.load({
				callback : function(records, options, success) {
					store1.add(new item1({
								dictId : '',
								dictName : '无',
								dictValue : ''
							}));
					typeField.setValue('');
				}
			});

	$('#hideFrame').bind('load', promgtMsg);
});

function save() {
	var n = Ext.getCmp("orgName").validate();
	var o = Ext.getCmp("orgParentName").validate();

	if (!(n && o)) {
		return;
	}

	var form = window.document.forms[0];
	form.action = appUrl + "/org/orgAction!createOrg.htm";
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
					msg : "组织创建成功！",
					buttons : Ext.Msg.OK,
					fn : function(btn) {
						if (btn == 'ok') {
							window.parent.opener.parent.frames[0].appendChild(
									$("#orgParentId").val(), successResult,
									$("#orgName").val());
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