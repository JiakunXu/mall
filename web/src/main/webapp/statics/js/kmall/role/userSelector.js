Ext.onReady(function() {
			// tips
			Ext.QuickTips.init();

			new Ext.form.DateField({
						id : "expireDate",
						applyTo : 'date',
						format : 'Y-m-d',
						minValue : gmtStart,
						allowBlank : false,
						editable : false
					});

			var Item = Ext.data.Record.create([{
						name : 'userId',
						type : 'long'
					}, {
						name : 'userName',
						type : 'string'
					}]);

			store = new Ext.data.Store({
						url : '',
						reader : new Ext.data.SimpleJsonReader({
									id : 'userId'
								}, Item),
						remoteSort : true
					});
			var has = Ext.getDom('jsonList').value;
			if (has != '') {
				store.loadData(Ext.util.JSON.decode(has), false);
			}

			authorizestore = new Ext.data.Store({
						url : '',
						reader : new Ext.data.SimpleJsonReader({
									id : 'userId'
								}, Item),
						remoteSort : true
					});

			Ext.form.Field.prototype.msgTarget = 'side';

			isForm = new Ext.form.FormPanel({
						border : false,
						height : 400,
						labelAlign : 'left',
						bodyBorder : false,
						labelWidth : 50,
						buttonAlign : 'center',
						bodyStyle : 'padding-top:10px; padding-right:50px',
						renderTo : 'itemselector',
						items : [{
									id : 'myit',
									xtype : 'itemselector',
									name : 'itemselector',
									multiselects : [{
												width : 100,
												height : 350,
												store : store,
												displayField : 'userName',
												valueField : 'userId',
												legend : '未授权'

											}, {
												width : 100,
												height : 350,
												store : authorizestore,
												displayField : 'userName',
												valueField : 'userId',
												legend : '待授权'

											}]
								}],
						buttons : [{
							text : '保存',
							handler : function() {
								if (isForm.getForm().isValid()) {
									var c = Ext.getCmp("expireDate").validate();
									if (!c) {
										return;
									}

									$('#expireDate').val(Ext
											.getCmp('expireDate').getValue()
											.format('Y-m-d'));

									Ext.Msg.confirm("添加", "确定授权给以上人员此角色？",
											function(bn) {
												if (bn == 'yes') {
													save();
												}
											});
								}
							}
						}]

					});

			$('#hideFrame').bind('load', promgtMsg);

		});

function save() {
	// isForm.getEl().mask();
	var ids = '';
	isForm.getForm().findField('itemselector').toMultiselect.store.each(
			function(record) {
				if (ids != '') {
					ids += ',';
				}
				ids += record.get('userId');
			});

	$('#userIds').val(ids);

	var form = window.document.forms[0];
	form.action = appUrl + '/role/roleAction!createUser4Role.htm';
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
							window.parent.opener.opener.search();
							window.parent.window.close();
						}
					},
					icon : Ext.Msg.INFO
				});
	}
}
