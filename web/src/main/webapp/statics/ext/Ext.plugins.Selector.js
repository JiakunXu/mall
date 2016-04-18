Ext.ns('Ext.plugins.Selector');

/**
 * RoleSelector
 * 
 * @param {}
 * 
 */
Ext.plugins.RoleSelector = function(config) {
	var role = Ext.data.Record.create([{
				name : 'roleId',
				type : 'string'
			}, {
				name : 'roleName',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按角色编号或角色名称或角色描述',
				valueDataIndex : ['roleId', 'roleName', 'remark'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 320,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'roleId'
									}, role),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "角色编号",
							dataIndex : 'roleId',
							width : 120,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "角色名称",
							dataIndex : 'roleName',
							width : 180,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "角色描述",
							dataIndex : 'remark',
							width : 100,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}])
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'roleId',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{roleId:htmlEncode}</span><span style="width:60%!important;">{roleName:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'roleId'
							}, role)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.RoleSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * StationSelector
 * 
 * @param {}
 * 
 */
Ext.plugins.StationSelector = function(config) {
	var station = Ext.data.Record.create([{
				name : 'staId',
				type : 'string'
			}, {
				name : 'staName',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按岗位编号或岗位名称或岗位描述',
				valueDataIndex : ['staId', 'staName', 'remark'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 320,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'staId'
									}, station),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "岗位编号",
							dataIndex : 'staId',
							width : 120,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "岗位名称",
							dataIndex : 'staName',
							width : 180,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "岗位描述",
							dataIndex : 'remark',
							width : 100,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}]),
				viewConfig : {
					forceFit : true
				}
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'staId',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{staId:htmlEncode}</span><span style="width:60%!important;">{staName:htmlEncode}</span><span style="width:60%!important;">{remark:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'staId'
							}, station)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.StationSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * menu
 * 
 * @param {}
 *            config
 */
Ext.plugins.MenuSelector = function(config) {
	var menu = Ext.data.Record.create([{
				name : 'pid',
				type : 'long'
			}, {
				name : 'pname',
				type : 'string'
			}, {
				name : 'id',
				type : 'long'
			}, {
				name : 'name',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按菜单编号或名称查询',
				valueDataIndex : ['pid', 'pname', 'id', 'name'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 320,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'id'
									}, menu),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "父级菜单编号",
							dataIndex : 'pid',
							width : 70,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "父级菜单名称",
							dataIndex : 'pname',
							width : 100,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "菜单编号",
							dataIndex : 'id',
							width : 70,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "菜单名称",
							dataIndex : 'name',
							width : 130,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}]),
				viewConfig : {
					forceFit : true
				}
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'id',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{id:htmlEncode}</span><span style="width:60%!important;">{name:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'id'
							}, menu)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.MenuSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * sapAuth
 * 
 * @param {}
 *            config
 */
Ext.plugins.SAPAuthSelector = function(config) {
	var sapAuth = Ext.data.Record.create([{
				name : 'profl',
				type : 'string'
			}, {
				name : 'textb',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按权限参数文件或名称查询',
				valueDataIndex : ['profl', 'textb'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 266,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'profl'
									}, sapAuth),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "权限参数文件",
							dataIndex : 'profl',
							width : 60,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "权限参数文件名",
							dataIndex : 'textb',
							width : 80,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}]),
				viewConfig : {
					forceFit : true
				}
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'profl',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{profl:htmlEncode}</span><span style="width:60%!important;">{textb:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'profl'
							}, sapAuth)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.SAPAuthSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * profile
 * 
 * @param {}
 *            config
 */
Ext.plugins.SAProfileSelector = function(config) {
	var profile = Ext.data.Record.create([{
				name : 'bapiProf',
				type : 'string'
			}, {
				name : 'text',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按参数文件或名称查询',
				valueDataIndex : ['bapiProf', 'text'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 266,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'bapiProf'
									}, profile),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "参数文件",
							dataIndex : 'bapiProf',
							width : 60,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "参数文件名",
							dataIndex : 'text',
							width : 80,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}]),
				viewConfig : {
					forceFit : true
				}
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'bapiProf',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{bapiProf:htmlEncode}</span><span style="width:60%!important;">{text:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'bapiProf'
							}, profile)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.SAProfileSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * funAction
 * 
 * @param {}
 *            config
 */
Ext.plugins.FunActionSelector = function(config) {
	var funAction = Ext.data.Record.create([{
				name : 'actionId',
				type : 'string'
			}, {
				name : 'funName',
				type : 'string'
			}, {
				name : 'actionName',
				type : 'string'
			}, {
				name : 'alias',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按菜单或名称查询',
				valueDataIndex : ['actionId', 'funName', 'actionName', 'alias'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 266,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'actionId'
									}, funAction),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "菜单名称",
							dataIndex : 'funName',
							width : 60,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "功能操作描述",
							dataIndex : 'actionName',
							width : 80,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "别名",
							dataIndex : 'alias',
							width : 80,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}]),
				viewConfig : {
					forceFit : true
				}
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'actionId',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{actionId:htmlEncode}</span><span style="width:60%!important;">{actionName:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'actionId'
							}, funAction)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.FunActionSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * position
 * 
 * @param {}
 *            config
 */
Ext.plugins.PositionSelector = function(config) {
	var position = Ext.data.Record.create([{
				name : 'posId',
				type : 'string'
			}, {
				name : 'posName',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}, {
				name : 'orgName',
				type : 'string'
			}, {
				name : 'quota',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按编制编号或名称查询',
				valueDataIndex : ['posId', 'posName', 'remark', 'orgName',
						'quota'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 320,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'posId'
									}, position),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "编号",
							dataIndex : 'posId',
							width : 20,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "编制名称",
							dataIndex : 'posName',
							width : 60,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "编制描述",
							dataIndex : 'remark',
							width : 30,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "组织名称",
							dataIndex : 'orgName',
							width : 40,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "数量",
							dataIndex : 'quota',
							width : 20,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}]),
				viewConfig : {
					forceFit : true
				}
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'posId',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{posId:htmlEncode}</span><span style="width:60%!important;">{posName:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'posId'
							}, position)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.PositionSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * orgVersion
 * 
 * @param {}
 *            config
 */
Ext.plugins.OrgVersionSelector = function(config) {
	var orgVersion = Ext.data.Record.create([{
				name : 'verId',
				type : 'string'
			}, {
				name : 'verName',
				type : 'string'
			}, {
				name : 'defaults',
				type : 'string'
			}, {
				name : 'state',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}, {
				name : 'run',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按版本编号或名称查询',
				valueDataIndex : ['verId', 'verName', 'defaults', 'state',
						'remark', 'run'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 320,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'verId'
									}, orgVersion),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "版本编号",
							dataIndex : 'verId',
							width : 20,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "版本名称",
							dataIndex : 'verName',
							width : 50,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "版本描述",
							dataIndex : 'remark',
							width : 30,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "状态",
							dataIndex : 'state',
							width : 15,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								if (v == 'U') {
									v = '正常';
								} else if (v == 'F') {
									v = '禁用';
								}
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "是否默认",
							dataIndex : 'defaults',
							width : 20,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								if (v == 'Y') {
									v = '是';
								} else if (v == 'N') {
									v = '否';
								}
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}]),
				viewConfig : {
					forceFit : true
				}
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'verId',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{verId:htmlEncode}</span><span style="width:60%!important;">{verName:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'verId'
							}, orgVersion)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.OrgVersionSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * process
 * 
 * @param {}
 *            config
 */
Ext.plugins.ProcessSelector = function(config) {
	var process = Ext.data.Record.create([{
				name : 'procId',
				type : 'string'
			}, {
				name : 'procName',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}, {
				name : 'alias',
				type : 'string'
			}, {
				name : 'type',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按流程编号或名称查询',
				valueDataIndex : ['procId', 'procName', 'remark', 'alias',
						'type'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 320,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'procId'
									}, process),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "流程编号",
							dataIndex : 'procId',
							width : 20,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "流程名称",
							dataIndex : 'procName',
							width : 50,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "流程描述",
							dataIndex : 'remark',
							width : 30,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "别名",
							dataIndex : 'alias',
							width : 20,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "类型",
							dataIndex : 'type',
							width : 30,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								if (v == 'F') {
									return '固定流程';
								} else if (v == 'A') {
									return '任意流程';
								}
							}
						}]),
				viewConfig : {
					forceFit : true
				}
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'procId',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{procId:htmlEncode}</span><span style="width:60%!important;">{procName:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'procId'
							}, process)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.ProcessSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * model
 * 
 * @param {}
 *            config
 */
Ext.plugins.ModelSelector = function(config) {
	var model = Ext.data.Record.create([{
				name : 'modId',
				type : 'string'
			}, {
				name : 'modName',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}, {
				name : 'defaultVerId',
				type : 'int'
			}, {
				name : 'type',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
		searchEmptyText : '按事务模板编号或名称查询',
		valueDataIndex : ['modId', 'modName', 'remark', 'defaultVerId', 'type'],
		hiddenValue : config.hiddenValue,
		hiddenField : config.hiddenField,
		searchable : config.searchable || false,
		creatable : config.creatable || false,
		multiable : config.multiable || false,
		reload : config.reload || false,
		paginal : true,
		width : 405,
		height : 320,
		submitURL : config.submitURL,
		params : config.params || {},
		triggerAction : config.triggerAction || null,
		store : new Ext.data.Store({
					url : config.url,
					reader : new Ext.data.SimpleJsonReader({
								id : 'modId'
							}, model),
					remoteSort : true
				}),
		cm : new Ext.grid.ColumnModel([{
					header : "事务模板编号",
					dataIndex : 'modId',
					width : 20,
					sortable : false,
					align : 'center',
					renderer : function(v, p) {
						p.attr = 'ext:qtip="' + v + '"';
						return Ext.util.Format.htmlEncode(v);
					}
				}, {
					header : "事务模板名称",
					dataIndex : 'modName',
					width : 50,
					sortable : false,
					align : 'left',
					renderer : function(v, p) {
						p.attr = 'ext:qtip="' + v + '"';
						return Ext.util.Format.htmlEncode(v);
					}
				}, {
					header : "备注",
					dataIndex : 'remark',
					width : 30,
					sortable : false,
					align : 'left',
					renderer : function(v, p) {
						p.attr = 'ext:qtip="' + v + '"';
						return Ext.util.Format.htmlEncode(v);
					}
				}]),
		viewConfig : {
			forceFit : true
		}
	})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'modId',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{modId:htmlEncode}</span><span style="width:60%!important;">{modName:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'modId'
							}, model)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.ModelSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * module
 * 
 * @param {}
 *            config
 */
Ext.plugins.ModuleSelector = function(config) {
	var module = Ext.data.Record.create([{
				name : 'modId',
				type : 'string'
			}, {
				name : 'modName',
				type : 'string'
			}, {
				name : 'domain',
				type : 'string'
			}, {
				name : 'type',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按模块名称或模块域名查询',
				valueDataIndex : ['modId', 'modName', 'domain', 'type'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 320,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'modId'
									}, module),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "模块编号",
							dataIndex : 'modId',
							width : 20,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "模块名称",
							dataIndex : 'modName',
							width : 30,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "域名",
							dataIndex : 'domain',
							width : 50,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "类型",
							dataIndex : 'type',
							width : 30,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								if ('E' == v) {
									return "基础平台";
								} else if ('S' == v) {
									return "子系统";
								} else if ('C' == v) {
									return "CORDYS";
								}
							}
						}]),
				viewConfig : {
					forceFit : true
				}
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'modId',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{modId:htmlEncode}</span><span style="width:60%!important;">{modName:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'modId'
							}, module)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.ModuleSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * customSql
 * 
 * @param {}
 *            config
 */
Ext.plugins.CustomSqlSelector = function(config) {
	var customSql = Ext.data.Record.create([{
				name : 'id',
				type : 'string'
			}, {
				name : 'value',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按编号或值查询',
				valueDataIndex : ['id', 'value', 'remark'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 320,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'id'
									}, customSql),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "编号",
							dataIndex : 'id',
							width : 20,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "值",
							dataIndex : 'value',
							width : 50,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "备注",
							dataIndex : 'remark',
							width : 30,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}]),
				viewConfig : {
					forceFit : true
				}
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'id',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{id:htmlEncode}</span><span style="width:60%!important;">{value:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'id'
							}, customSql)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.CustomSqlSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * sqlMonitor
 * 
 * @param {}
 *            config
 */
Ext.plugins.SqlMonitorSelector = function(config) {
	var sqlMonitor = Ext.data.Record.create([{
				name : 'sqlMonitorId',
				type : 'long'
			}, {
				name : 'sqlMonitorTitle',
				type : 'string'
			}, {
				name : 'status',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按监控计划编号或监控标题查询',
				valueDataIndex : ['sqlMonitorId', 'sqlMonitorTitle', 'status'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 320,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'sqlMonitorId'
									}, sqlMonitor),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "监控计划编号",
							dataIndex : 'sqlMonitorId',
							width : 50,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "监控标题",
							dataIndex : 'sqlMonitorTitle',
							width : 100,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "监控计划状态",
							dataIndex : 'status',
							width : 50,
							sortable : false,
							align : 'center',
							renderer : function(v) {
								if (v == 'Y') {
									return "<p style='color:green'>正常运行</p>";
								}
								if (v == 'N') {
									return "<p style='color:red'>停止运行</p>";
								}
							}
						}]),
				viewConfig : {
					forceFit : true
				}
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'sqlMonitorId',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{sqlMonitorId:htmlEncode}</span><span style="width:60%!important;">{sqlMonitorTitle:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'sqlMonitorId'
							}, sqlMonitor)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.SqlMonitorSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});

/**
 * dbTable
 * 
 * @param {}
 *            config
 */
Ext.plugins.DBTableSelector = function(config) {
	var dbTable = Ext.data.Record.create([{
				name : 'itemId',
				type : 'long'
			}, {
				name : 'itemValue',
				type : 'string'
			}, {
				name : 'remark',
				type : 'string'
			}, {
				name : 'itemState',
				type : 'string'
			}]);

	var p = [new Ext.plugins.ComplexGridCombox({
				searchEmptyText : '按编号或表名查询',
				valueDataIndex : ['itemId', 'itemValue', 'remark', 'itemState'],
				hiddenValue : config.hiddenValue,
				hiddenField : config.hiddenField,
				searchable : config.searchable || false,
				creatable : config.creatable || false,
				multiable : config.multiable || false,
				reload : config.reload || false,
				paginal : true,
				width : 405,
				height : 320,
				submitURL : config.submitURL,
				params : config.params || {},
				triggerAction : config.triggerAction || null,
				store : new Ext.data.Store({
							url : config.url,
							reader : new Ext.data.SimpleJsonReader({
										id : 'itemId'
									}, dbTable),
							remoteSort : true
						}),
				cm : new Ext.grid.ColumnModel([{
							header : "编号",
							dataIndex : 'itemId',
							width : 50,
							sortable : false,
							align : 'center',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "表名",
							dataIndex : 'itemValue',
							width : 150,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "sequence",
							dataIndex : 'remark',
							width : 100,
							sortable : false,
							align : 'left',
							renderer : function(v, p) {
								p.attr = 'ext:qtip="' + v + '"';
								return Ext.util.Format.htmlEncode(v);
							}
						}, {
							header : "状态",
							dataIndex : 'itemState',
							width : 50,
							sortable : false,
							align : 'center',
							renderer : function(v) {
								if (v == 'U') {
									return "<p style='color:green'>启用</p>";
								} else {
									return "<p style='color:red'>禁用</p>";
								}
							}
						}]),
				viewConfig : {
					forceFit : true
				}
			})];

	if (config.plugins) {
		if (Ext.isArray(config.plugins)) {
			p = p.concat(config.plugins);
		} else {
			p.push(config.plugins);
		}
	}

	this.editor = new Ext.form.ComboBox({
		listWidth : config.listWidth || undefined,
		displayField : config.displayField || 'itemId',
		triggerAction : 'all',
		mode : 'local',
		lazyInit : config.lazyInit || true,
		shadow : false,
		editable : config.autocomplete || false,
		tpl : '<tpl for="."><div class="x-combo-list-item"><span style="width:35%!important;">{itemId:htmlEncode}</span><span style="width:60%!important;">{itemValue:htmlEncode}</span></div></tpl>',
		store : new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy({}),
					reader : new Ext.data.SimpleJsonReader({
								id : 'itemId'
							}, dbTable)
				}),
		plugins : p,
		style : config.style
	});

	if (config.onselect) {
		this.editor.on('select', config.onselect);
	}

	if (config.beforeexpand) {
		this.editor.on('beforeexpand', config.beforeexpand);
	}

};

Ext.extend(Ext.plugins.DBTableSelector, Ext.util.Observable, {
			init : function(grid) {
				// do nothing
			}
		});
