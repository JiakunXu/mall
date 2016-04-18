var type;
Ext.onReady(function() {

	Ext.QuickTips.init();

	new Ext.form.TextField({
				id : "orgId",
				allowBlank : false,
				readOnly : true,
				applyTo : 'orgId'
			});

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
					typeField.setValue(orgType);
				}
			});

	$('#hideFrame').bind('load', promgtMsg);
});

function save() {
	var p = Ext.getCmp("orgId").validate();
	var n = Ext.getCmp("orgName").validate();
	var o = Ext.getCmp("orgParentName").validate();

	if (!(p && n && o)) {
		return;
	}

	type = 'update';

	var form = window.document.forms[0];
	form.action = appUrl + "/org/orgAction!updateOrg.htm";
	form.target = "hideFrame";
	form.submit();

	Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
}

function create() {
	var orgParentId = $("#orgId").val();
	var orgParentName = encodeURIComponent($("#orgName").val());

	if (orgParentId == null || orgParentId == "") {
		warn("请先在组织树中选择父级组织");
		return;
	}

	var form = window.document.forms[0];
	var WWidth = 600;
	var WHeight = 400;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/org/orgAction!createOrgPrepare.htm?orgParentId="
					+ orgParentId + "&orgParentName=" + orgParentName,
			"create", "left=" + WLeft + ",top=" + WTop + ",width=" + WWidth
					+ ",height=" + WHeight);
}

function deletes() {
	Ext.Msg.confirm("提示", "确认删除组织？", function(button) {
				if (button == 'yes') {
					type = 'delete';

					var form = window.document.forms[0];
					form.action = appUrl + '/org/orgAction!deleteOrg.htm';
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});
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

					if (type == 'update') {
						window.parent.frames[0].setText($("#orgId").val(),
								$("#orgName").val());
					} else if (type == 'delete') {
						window.parent.frames[0].remove($("#orgId").val());

						// 清空页面参数
						$("#td").html("");
						$("#orgId").val("");
						$("#orgName").val("");
						$("#shortName").val("");
						$("#orgParentName").val("");
						$("#orgParentId").val("");
						$("#remark").val("");
					}
					type = "";
				}, 1000);
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