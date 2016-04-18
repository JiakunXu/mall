var data = {};
var key = new Array();
var i = 0;
Ext.onReady(function() {
	tree = new Ext.tree.TreePanel({
		useArrows : true,
		autoScroll : true,
		animate : true,
		enableDD : true,
		containerScroll : true,
		border : false,
		dataUrl : appUrl + '/org/orgTreeAction!getOrgTreeList.htm',
		root : {
			nodeType : 'async',
			text : companyName,
			draggable : false,
			id : '$!node' == '-1' ? '-1' : '1'
		},
		listeners : {
			"contextmenu" : function(node, e) {
				var menu = new Ext.menu.Menu({
							items : [{
										text : "创建下级组织",
										icon : imgUrl
												+ '/image/actions/icon_add.gif',
										handler : function() {
											if (!node.leaf) {
												node.expand();
												// 创建下级组织
												create(node.id, node.text);
											}
										},
										listeners : {
											render : function(com) {
												if (node.leaf) {
													com.setVisible(false);
												}
											}
										}
									}]
						});

				menu.showAt(e.getPoint());
			}
		}
	});

	tree.render('tree-div');
	tree.getRootNode().expand();

	tree.on('click', function(node) {
				var form = window.document.forms[0];
				document.getElementById("orgId").value = node.id;
				document.getElementById("orgName").value = node.text;

				var actionName = document.getElementById("actionName").value
				if (actionName != "") {
					form.action = actionName;
					form.target = target;
					form.submit();
				}
			});

	tree.on('movenode', function(tree, node, oldParent, newParent, index) {
				if (data[newParent.id] == undefined || data[newParent.id] == '') {
					key[i++] = newParent.id;
				}
				data[newParent.id] = {
					'id' : newParent.id
				};
			});

	$('#hideFrame').bind('load', promgtMsg);
});

function save() {
	if (i == 0) {
		warn('请先单击选中组织树节点，拖动完成组织层级调整！');
		return;
	}

	Ext.Msg.confirm("提示", "确认组织层级调整？", function(button) {
				if (button == 'yes') {

					var params = [];
					for (var j = 0; j < i; j++) {
						Ext.each(tree.getNodeById(data[key[j]].id).childNodes,
								function(node) {
									params.push({
												"orgId" : node.id,
												"orgParentId" : node.parentNode.id
											});
								});
					}

					document.getElementById("orgList").value = Ext.util.JSON
							.encode(params);
					var form = window.document.forms[0];
					form.action = appUrl + "/org/orgAction!adjustOrgLevel.htm";
					form.target = "hideFrame";
					form.submit();

					Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
				}
			});
}

function appendChild(pid, id, text) {
	var n = new Ext.tree.TreeNode({
				id : id,
				text : text,
				leaf : false,
				icon : imgUrl + '/image/actions/icon_folder.gif'
			});

	var node = tree.getNodeById(pid)
	node.appendChild(n);
	node.expand();
}

function remove(id) {
	var node = tree.getNodeById(id)
	node.remove();
}

function setText(id, text) {
	var node = tree.getNodeById(id)
	node.setText(text);
}

function create(orgParentId, orgParentName) {
	orgParentName = encodeURIComponent(orgParentName);

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

function searchOrgVersion() {
	var form = window.document.forms[0];
	var WWidth = 1000;
	var WHeight = 600;
	var WLeft = Math.ceil((window.screen.width - WWidth) / 2);
	var WTop = Math.ceil((window.screen.height - WHeight) / 2);
	window.open(appUrl + "/org/orgVersionAction!searchOrgVersion.htm",
			"searchOrgVersion", "left=" + WLeft + ",top=" + WTop + ",width="
					+ WWidth + ",height=" + WHeight);
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
					i = 0;
					data = {};
					key = new Array();
					var params = [];
					document.getElementById("orgList").value = Ext.util.JSON
							.encode(params);
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