/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.plugins.HeaderAligning.js" ></script>
 */

/**
 * @namespace Ext.plugins.HeaderAligning
 */
Ext.ns('Ext.plugins.HeaderAligning');

/**
 * 列头对齐格式设置
 * 
 * @alias Ext.plugins.HeaderAligning
 * @author Tingjia Chen tingjia.chentj@alibaba-inc.com
 */
Ext.plugins.HeaderAligning = function(align) {
	this.align = align || 'center';
	// call parent
	Ext.plugins.HeaderAligning.superclass.constructor.call(this);
};

Ext.extend(Ext.plugins.HeaderAligning, Ext.util.Observable, {
			/**
			 * @method
			 */
			init : function(grid) {
				var view = grid.getView();
				var headerAlign = this.align;
				Ext.apply(view, {
							getColumnStyle : function(col, isHeader) {
								var style = !isHeader
										? (this.cm.config[col].css || '')
										: '';
								style += 'width:' + this.getColumnWidth(col)
										+ ';';
								if (this.cm.isHidden(col)) {
									style += 'display:none;';
								}
								/* added by chentingjia */
								if (isHeader) {
									style += 'text-align:' + headerAlign + ';';
									style += 'color: #5D6B82;';
									/* end */
								} else {
									var align = this.cm.config[col].align;
									if (align) {
										style += 'text-align:' + align + ';';
									}
								}
								return style;
							} // eo getColumnStyle
						});

			}
		});

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.data.SimpleJsonReader.js" ></script>
 */

/**
 * @class Ext.data.SimpleJsonReader
 * @extends Ext.data.DataReader Data reader class to create an Array of
 *          Ext.data.Record objects from a JSON response based on mappings in a
 *          provided Ext.data.Record constructor.<br>
 *          <p>
 *          Example code:
 * 
 * <pre><code>
 * var Employee = Ext.data.Record.create([{
 * 			name : 'name',
 * 			mapping : 'name'
 * 		}, // &quot;mapping&quot; property not needed if it's the same as &quot;name&quot;
 * 		{
 * 			name : 'occupation'
 * 		} // This field will use &quot;occupation&quot; as the mapping.
 * ]);
 * var myReader = new Ext.data.SimpleJsonReader({
 * 			id : 'name'
 * 		}, Employee);
 * 
 * var store = new Ext.data.Store({
 * 			url : 'topics-browse-remote.php',
 * 			reader : myReader,
 * 			remoteSort : true
 * 		});
 * </code></pre>
 * 
 * <p>
 * This would consume a JSON file like this:
 * 
 * <pre><code>
 *  [
 *  { 'id': 1, 'name': 'Bill', occupation: 'Gardener' },
 *  { 'id': 2, 'name': 'Ben', occupation: 'Horticulturalist' }
 *  ]
 *  or like this:
 *  {values:
 *  [
 *  { 'id': 1, 'name': 'Bill', occupation: 'Gardener' },
 *  { 'id': 2, 'name': 'Ben', occupation: 'Horticulturalist' }
 *  ]
 *  ,total: 10
 *  }
 * </code></pre>
 */
Ext.data.SimpleJsonReader = function(meta, recordType) {
	meta = meta || {};
	meta.totalProperty = meta.totalProperty || 'total';
	meta.root = meta.root || 'values';
	Ext.data.SimpleJsonReader.superclass.constructor.call(this, meta,
			recordType || meta.fields);
};
Ext.extend(Ext.data.SimpleJsonReader, Ext.data.JsonReader, {

	/**
	 * Create a data block containing Ext.data.Records from a JSON object.
	 * 
	 * @param {Object}
	 *            o An object which contains an Array of row objects.
	 * @return {Object} data A data block which is used by an Ext.data.Store
	 *         object as a cache of Ext.data.Records.
	 */
	readRecords : function(o) {
		var results = o, s = this.meta;
		if ((typeof o.length != 'undefined')) {
			results = {
				values : o,
				total : o.length
			};
		} else if ((typeof o.values == 'undefined')) {
			results = {
				values : [],
				total : 0
			};
		}
		if (s.blanks) {
			for (var i = 0, len = s.blanks - results.values.length; i < len; i++) {
				results.values[results.values.length] = {};
			}
		}

		return Ext.data.SimpleJsonReader.superclass.readRecords.call(this,
				results);
	}
});

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.ux.plugins.CheckBoxMemory.js"></script>
 */

Ext.ns('Ext.ux.plugins.CheckBoxMemory');
/**
 * 为CheckboxSelectionModel增加保持状态的功能
 * 
 * @alias Ext.plugins.GridHotKey
 * @author Zhijun Yuan zhijun.yuanzj@alibaba-inc.com
 */
Ext.ux.plugins.CheckBoxMemory = Ext.extend(Object, {
			constructor : function(config) {
				if (!config)
					config = {};

				this.prefix = 'id_';
				this.items = {};
				this.idProperty = config.idProperty || 'id';
			},

			init : function(grid) {
				this.grid = grid;
				this.view = grid.getView();
				this.store = null;
				this.sm = grid.getSelectionModel();
				this.sm.on('rowselect', this.onSelect, this);
				this.sm.on('rowdeselect', this.onDeselect, this);
				this.view.on('refresh', this.reConfigure, this);
				this.grid.totalSelection = new Ext.util.MixedCollection(false,
						function(o) {
							return o.id;
						});
			},

			reConfigure : function() {
				this.store = this.grid.getStore();
				this.store.on('clear', this.onClear, this);
				this.store.on('datachanged', this.restoreState, this);
			},

			onSelect : function(sm, idx, rec) {
				this.items[this.getId(rec)] = true;
				if (this.grid.totalSelection) {
					this.grid.totalSelection.add(rec);
				}
			},

			onDeselect : function(sm, idx, rec) {
				delete this.items[this.getId(rec)];
				if (this.grid.totalSelection) {
					this.grid.totalSelection.remove(rec);
				}
			},

			restoreState : function() {
				if (this.store != null) {
					var i = 0;
					var sel = [];
					var isAllSelected = true;
					var els = Ext.select("*[class=x-grid3-hd-checker]");
					var hd;
					if (els) {
						hd = Ext.fly(els.first().dom.parentNode);
					}
					this.store.each(function(rec) {
								var id = this.getId(rec);
								if (this.items[id] === true) {
									sel.push(i);
								} else {
									isAllSelected = false;
								}
								++i;
							}, this);
					if (hd) {
						if (isAllSelected) {
							hd.addClass('x-grid3-hd-checker-on');
						} else {
							hd.removeClass('x-grid3-hd-checker-on');
						}
					}
					if (sel.length > 0) {
						this.sm.selectRows(sel);
					}
				}
			},

			onClear : function() {
				var sel = [];
				this.items = {};
			},

			getId : function(rec) {
				return rec.get(this.idProperty);
			}
		});

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.ux.grid.PowerSearchGridPanel.js"></script>
 */

Ext.ns('Ext.ux.grid.PowerSearchGridPanel');
/**
 * @class Ext.ux.grid.PowerSearchGridPanel
 * @extends Ext.grid.GridPanel
 * 
 * @constructor
 * @param {Object}
 *            config The config object
 */
Ext.ux.grid.PowerSearchGridPanel = Ext.extend(Ext.grid.GridPanel, {
	/**
	 * @cfg {Boolean} multiable 是否支持多选
	 */
	multiable : true,

	/**
	 * @cfg {Boolean} searchable 是否支持查找
	 */
	searchable : true,

	/**
	 * @cfg {Boolean} creatable 是否支持快速新建
	 */
	creatable : true,

	/**
	 * @cfg {Boolean} paginal 是否支持分页显示
	 */
	paginal : true,

	// private
	initComponent : function() {
		if (this.deletable || this.updatable) {
			var actionConfig = [];
			if (this.updatable) {
				actionConfig.push({
							iconCls : 'icon-edit-record'
						});
			}
			if (this.deletable) {
				actionConfig.push({
							iconCls : 'icon-del-table'
						});
			}
			var da = new Ext.ux.grid.RowActions({
						actions : actionConfig
					});
			da.on('action', function(grid, r, action, row, col, e) {
						if (action == 'icon-del-table') {
							e.stopEvent();
							grid.fireEvent("rowdeleted", grid, r, row, col);
						}
						if (action == 'icon-edit-record') {
							e.stopEvent();
							grid.fireEvent("rowupdated", grid, r, row, col);
						}
					});
			this.plugins = this.plugins ? [da].concat(this.plugins) : [da];
			this.cm.setConfig([da].concat(this.cm.config), true);
		}
		if (this.multiable) {
			this.sm = new Ext.grid.CheckboxSelectionModel();
			Ext.apply(this.sm, {
						onMouseDown : function(e, t) {
							e.stopEvent();
							var row = e.getTarget('.x-grid3-row');
							if (row) {
								var index = row.rowIndex;
								if (this.isSelected(index)) {
									this.deselectRow(index);
								} else {
									this.selectRow(index, true);
								}
							}
						}
					});
			this.cm.setConfig([this.sm].concat(this.cm.config), true);
			if (this.store && this.store.reader) {
				this.plugins.push(new Ext.ux.plugins.CheckBoxMemory({
							idProperty : this.store.reader.meta.id
						}));
			}
		} else {
			this.sm = this.sm || new Ext.grid.RowSelectionModel();
		}

		this.tbar = [];
		if (this.searchable) {
			this.tbar.push('查找: ');
			this.searchField = new Ext.ux.grid.SearchField({
						pageSize : this.pageSize,
						paginal : this.paginal,
						store : this.store,
						paramName : this.searchParamName || 'search',
						emptyText : this.searchEmptyText || ''
					});
			this.tbar.push(this.searchField);
		}
		this.tbar.push('->');
		if (this.multiable) {
			this.tbar.push({
						text : '选定',
						iconCls : 'btn_icon_select',
						handler : function() {
							if (this.totalSelection.items.length > 0) {
								this.fireEvent("rowselected",
										[].concat(this.totalSelection.items));
							}
						},
						scope : this
					});
		}
		if (this.creatable && this.multiable) {
			this.tbar.push('-');
		}
		if (this.creatable) {
			this.tbar.push({
				text : '新建',
				iconCls : 'btn_icon_add',
				handler : function() {
					this.fireEvent("showadd", this);
				},
				scope : this
					// grid
				});
		}
		this.tbar.push({
			iconCls : 'btn_icon_close',
			handler : function() {
				this.fireEvent("close", this);
			},
			scope : this
				// grid
			});
		if (this.paginal) {
			this.bbar = this.bbar || new Ext.PagingToolbar({
						pageSize : this.pageSize,
						store : this.store,
						displayInfo : true,
						displayMsg : '共 {2} 条记录',
						emptyMsg : "没有找到记录！"
					});
		}

		Ext.ux.grid.PowerSearchGridPanel.superclass.initComponent.call(this);

		this.addEvents(
				/**
				 * @event rowselected Fires after row(s) be selected. <br />
				 * @param {Array/Object}
				 *            array of Record / Record
				 */
				'rowselected',
				/**
				 * @event showadd
				 * @param {GridPanel}
				 *            grid
				 */
				'showadd',
				/**
				 * @event rowdeleted Fires after row be deleted. <br />
				 * @param {Object}
				 *            Record
				 * @param {Number}
				 *            row
				 * @param {Number}
				 *            col
				 */
				'rowdeleted',
				/**
				 * @event close
				 * @param {GridPanel}
				 *            grid
				 */
				'close');
	},

	// private
	initEvents : function() {
		Ext.ux.grid.PowerSearchGridPanel.superclass.initEvents.call(this);
		// 单选时单击选中／多选时双击选中
		if (this.multiable) {
			this.on('rowdblclick', function(g, i, e) {
						if (this.totalSelection.items.length > 0) {
							this.fireEvent("rowselected", []
											.concat(this.totalSelection.items));
						}
					}, this);
		} else {
			this.on('rowclick', function(g, i, e) {
						if (this.getSelectionModel().hasSelection()) {
							this.fireEvent("rowselected", this
											.getSelectionModel().getSelected(),
									i);
						}
					}, this);
		}

		this.keyNav = new Ext.KeyNav(this.getGridEl(), {
					"enter" : function(e) {
						if (this.getSelectionModel().hasSelection()) {
							this.fireEvent("rowselected", this
											.getSelectionModel().getSelected(),
									this.getSelectionModel().last);
						}
					},
					scope : this,
					forceKeyDown : true
				});
	}
});
Ext.reg('powersearch', Ext.ux.grid.PowerSearchGridPanel);

Ext.ux.grid.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
			initComponent : function() {
				if (!this.store.baseParams) {
					this.store.baseParams = {};
				}
				Ext.ux.grid.SearchField.superclass.initComponent.call(this);
				this.on('specialkey', function(f, e) {
							if (e.getKey() == e.ENTER) {
								this.onTrigger2Click();
							}
						}, this);
			},

			validationEvent : false,
			validateOnBlur : false,
			trigger1Class : 'x-form-clear-trigger',
			trigger2Class : 'x-form-search-trigger',
			hideTrigger1 : true,
			width : 180,
			hasSearch : false,
			paramName : 'search',

			// 清除查找条件并恢复列表到初始结果
			onTrigger1Click : function() {
				if (this.hasSearch) {
					delete this.store.baseParams[this.paramName];
					this.store.fireEvent("clearCheckBoxMemeory");
					this.store.load({
								params : this.paginal ? {
									start : 0,
									limit : this.pageSize
								} : {}
							});
					this.el.dom.value = '';
					this.triggers[0].hide();
					this.hasSearch = false;
					this.focus();
				}
			},

			// 查找按钮响应
			onTrigger2Click : function() {
				var v = this.getRawValue();
				if (v.length < 1) {
					this.onTrigger1Click();
					return;
				}
				this.store.baseParams[this.paramName] = encodeURIComponent(v);
				this.store.fireEvent("clearCheckBoxMemeory");
				this.store.load({
							params : this.paginal ? {
								start : 0,
								limit : this.pageSize
							} : {}
						});
				this.hasSearch = true;
				this.triggers[0].show();
				this.focus();
			}
		});

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.ux.PowerSelectBase.js"></script>
 */

Ext.ux.PowerSelectBase = function(config){
	Ext.useShims = true;
	this.canCollapse = true;
	this.config = Ext.apply({}, config, {
		     width: 700,
		    height: 350,
		  pageSize: 10,
		   paginal: false,
		 creatable: true,
		 deletable: false,
		 updatable: false,
		 deletable: false,
		searchable: true,
		 multiable: false,
		stripeRows: true,
	   columnLines: true,
		  moreText: null,
	 triggerAction: null,		// 'power'
	     deleteURL: null,
	     submitURL: null,
	        params: null, // for datasource
	       dparams: null, // for delete
	       cparams: null, // for create
	        reload: true, // reload on expand
	     autoField: null,
	valueDataIndex: []
	});
	return {		
		powerExpand : (function(config){
			return function(preventLoad){
				if(config.beforeExpand){
					if(typeof config.beforeExpand == 'function'){
						if(!(config.beforeExpand)()){
							return
						}
					}
				}
		        if(this.isPowerExpanded() ){
		            return;
		        }
		        if(!preventLoad && config.triggerAction == 'power'){
					this.loadPowerStore();
		        }
	        	this.power.alignTo(config.trigger?config.trigger:this.wrap, "tl-bl?");
	        	if(this.grid&&!this.grid.isVisible()){
	        		this.grid.show(false);
	        	}
	        	this.power.show(false);
		        this.power.getShim().setLeft(this.power.getShim().getLeft()-(Ext.isIE6 ? 10:0));
		        Ext.getDoc().on('mousewheel', this.powerCollapseIf, this);
		        Ext.getDoc().on('mousedown', this.powerCollapseIf, this);
		        if(!config.trigger){
		        	this.fireEvent('expand', this, true);
		        }
			};
		})(this.config), // eo powerExpand
		
		loadPowerStore: (function(config){
			return function() {
		        var store = this.grid.getStore();
				store.fireEvent("clearCheckBoxMemeory");
		        if( !( store.data && store.data.getCount() > 0) || config.reload ) {
		        	var params = config.params ? ( typeof config.params == 'object' ?
							   	config.params : typeof config.params == 'function' ?
							   	(config.params)(/* no params */) : {} ) : {};
					for( key in params ) {
			        	store.baseParams[key] = params[key];
					}
		        	if( this.grid.paginal ) {
		        		store.load( {params:{ start: 0, limit: this.grid.pageSize }} );
		        	} else {
		        		store.load();
		        	}
		        }
			};
		})(this.config), // eo loadPowerStore
		
		// modify by xujiakun 2011-5-11
		setConfigParams : (function(config) {
			return function(params) {
				config.params = params;
				config.reload = true;
			};
		})(this.config),
		
		// private method
		powerCollapse : function(){
	        if( !this.isPowerExpanded() ){
	            return;
	        }
	        if( this.cw ) this.cw.hide();
	        if( this.uw ) this.uw.hide();
	        this.power.hide();
	        Ext.getDoc().un('mousewheel', this.powerCollapseIf, this);
	        Ext.getDoc().un('mousedown', this.powerCollapseIf, this);
	        if(!config.trigger){
	        	this.fireEvent('collapse', this, true);
	        }
		}, // eo powerCollapse
		
		// private method
		isPowerExpanded : function(){
			return this.power && this.power.isVisible();
		}, // eo isPowerExpanded
	
	    // private
	    powerCollapseIf : function(e){
	    	if(config.trigger){
		        if(!e.within(this.power)&&(this.canCollapse==true||this.canCollapse==undefined)){
		            this.powerCollapse();
		        }
	    	}else{
	    		if(!e.within(this.wrap) && !e.within(this.power)&&(this.canCollapse==true||this.canCollapse==undefined)){
		            this.powerCollapse();
		        }
	    	}
	    },
	    
		// private method
		initGrid: (function(config){
			return function() {
	        	if(!this.power){
		            this.power = new Ext.Layer({ shadow: false, constrain: true, shim: true });
	        		if(config.triggerAction != 'tree'){
		            this.grid  = new Ext.ux.grid.PowerSearchGridPanel({
							region: 'center',
						viewConfig: config.viewConfig,
						   paginal: config.paginal,
						 creatable: config.creatable,
						 updatable: config.updatable,
						 deletable: config.deletable,
						searchable: config.searchable,
						 multiable: config.multiable,
				             width: config.multiable?config.width+20:config.width,
				            height: config.height,
						stripeRows: config.stripeRows,
					   columnLines: config.columnLines,
						     store: config.store,
							    cm: config.cm,
					      pageSize: config.pageSize,
				  searchParamName : config.searchParamName,
				  searchEmptyText : config.searchEmptyText,
					       plugins: [new Ext.plugins.HeaderAligning()],
					  enableHdMenu: false,
					  	  loadMask: config.loadMask
					});
					
					if(config.hasNag){
						config.grid = this.grid;
						this.powerInner = new Ext.ux.PowerSearchPanel(config);
						this.powerInner.render(this.power);
		        		this.power.appendChild(this.powerInner.getEl());
					}else{
						this.grid.render(this.power);
		        		this.power.appendChild(this.grid.getEl());
					}
					
		        	this.power.swallowEvent('mousedown');
		            this.power.setWidth(config.width);
		            this.power.setHeight(config.height);
		            this.power.hide();
		            this.grid.hide();
		        	if( config.creatable ) {
						this.grid.on('showadd', function(gd) {
							this.initCreateWindow();
							// this.cw.setPosition((gd.width * (1-0.618)),
							// (gd.height * (1-0.618)));
	    					this.cw.show();
						}, this);
		        	}
		        	if( config.deletable ) {
						this.grid.on('rowdeleted', function(grid, r, row, col) {
							var promptMsg;
							if(config.deletePromptMsg){
								if(typeof config.deletePromptMsg == 'function'){
									promptMsg = (config.deletePromptMsg)(grid, r, row, col);
								}else{
									promptMsg = config.deletePromptMsg;
								}
							}else{
								promptMsg = "确定删掉此记录吗？";
							}
							if(window.confirm(promptMsg)){
								Ext.Ajax.request({
								   url: config.deleteURL,
								   scope: this,
								   success: function() {
										this.reloadGrid();
								   },
								   failure: Ext.emptyFn,
								   params: config.dparams ? ( typeof config.dparams == 'object' ?
								   	config.dparams : typeof config.dparams == 'function' ?
								   	(config.dparams)(grid,r,row,col) : {} ) : {}
								});
							}
// Ext.MessageBox.confirm('确认删除', 'Are you sure you want to do that?',
// function(btn){
// if(btn=='ok'){
// Ext.Ajax.request({
// url: config.deleteURL,
// scope: this,
// success: function() {
// this.reloadGrid();
// },
// failure: Ext.emptyFn,
// params: config.dparams ? ( typeof config.dparams == 'object' ?
// config.dparams : typeof config.dparams == 'function' ?
// (config.dparams)(grid,r,row,col) : {} ) : {}
// });
// }
// },this);
						}, this);
		        	}
		        	if( config.updatable ) {
						this.grid.on('rowupdated', function(grid, r, row, col) {
							this.initUpdateWindow(r);
	    					this.uw.show();
						}, this);
		        	}
					
					this.grid.on('rowselected', function(r,i) {
						/*
						 * if( this.store ) { var add = []; for( var i = 0, tmp =
						 * [].concat(r), n = tmp.length; i < n; i++ ) { if (
						 * this.store.getById(tmp[i].id) ) { continue; }
						 * add.push(tmp[i]); } this.store.add(add); }
						 */
						this.onSelect(r,i);
			            this.powerCollapse();
			            if(!config.trigger){
			            	this.focus();
			            }
					}, this);
					
					this.grid.on('close', function() {
			            this.powerCollapse();
					}, this);
					
					// "修改"页面时初始化下拉框的值
					this.grid.store.on('load', function(){
						for( var key in config.hiddenValue ) {
							var i = this.grid.store.findBy(function(r){
								return r.get(key) == config.hiddenValue[key];
							});
							if( i != -1 ) { // found
								var r = this.grid.store.getAt(i);
								this.value = r.get(this.displayField);
								for( var j = 0, l = config.valueDataIndex.length; j < l; j++ ) {
									var k = config.valueDataIndex[j];
									if( config.hiddenField ) {
										var f = Ext.get(config.hiddenField[k]);
										if( f ) f.set({value:r.get(k)});
									} else {
										this.record.set(k, v); // TODO fix
																// 可能this.record会不存在
									}
								}
								this.initValue();
								break;
							}
						}
					}, this, {single: true});
					
					// modify by xujiakun 2011-5-16
					// this.loadPowerStore();
	        		}
	        		
	        		if(config.triggerAction == 'tree'){
	        			this.tree = new Ext.ux.tree.PowerTreePanel({
								region: 'center',
							 creatable: config.creatable,
							 updatable: config.updatable,
							 deletable: config.deletable,
							searchable: config.searchable,
							 multiable: config.multiable,
					             width: config.multiable?config.width+20:config.width,
					            height: config.height,
					            titleText: config.titleText,
					            autoScroll : config.autoScroll,
								autoHeight : config.autoHeight||false,
								autoWidth : config.autoWidth||false,
						        loader: new Ext.tree.TreeLoader({
						            dataUrl:config.url
						        }),
						        root : new Ext.tree.AsyncTreeNode({
							        text: config.rootText||'',
							        draggable:false,
							        id:config.rootId
							    }),
        					rootVisible:config.rootVisible
						});
						this.tree.render(this.power);
		        		this.power.appendChild(this.tree.getEl());
	        			this.power.swallowEvent('mousedown');
			            this.power.setWidth(config.width);
			            this.power.setHeight(config.height);
			            this.power.hide();
			            this.tree.on('close', function() {
				            this.powerCollapse();
						}, this);
						
						this.tree.on('click', function(n){
					    	var sn = this.selModel.selNode || {}; 		
							if( config.hiddenField ) {
								var f1 = Ext.get(config.hiddenField);
								if( f1 ) f1.set({value:n.id});
							} 
							if(config.displayField){
								var f2 = Ext.get(config.displayField);
								if( f2 ) f2.set({value:n.text});									
							}
			            	this.fireEvent("nodeselected", n);
						});
						
						this.tree.on('nodeselected', function(n){
				            this.powerCollapse();
						},this);
	        		}
				}
			}; 
		})(this.config),
		
		reloadGrid: (function(config){
			return function() {
				if(this.grid.searchField){
					this.grid.searchField.onTrigger1Click();
				}
				this.grid.getStore().reload( {
					params: config.paginal ? { start: 0, limit: config.pageSize } : {},
					callback: function() {
						// this.getSelectionModel().selectFirstRow();
					},
					scope: this.grid	
				});
			}
		})(this.config),
		
		// private method
	    initCreateWindow: (function(config){
			return function() {
				if(this.cw){
					return
				}
				var tpl = '<span style="color:red;padding-left:15px;">{0}</span>';
				var form = Ext.apply({}, config.createForm, {
					xtype : 'form',
					border: false,
					defaultType : 'textfield',
					defaults:{
						width : 200,
						labelSeparator : '',
						labelStyle:'text-align:right',
						validationEvent: 'change',
						validateOnBlur: false,
						stripCharsRe: /^\s+|\s+$/g
					}
				});
				this.cw  = new Ext.Window({
					title : '新建',
					width : config.formWidth||400,
					shadow: false,
					hideParent: true,
					resizable: false,
					closeAction: 'hide',
					defaults: { bodyStyle: 'padding:15px' },
					items : [form, {xtype:'tbtext', text: String.format(tpl, '&#160;')}],
					buttons: [{
						text: '保存',
						scope: this,	// combo
						handler: function(btn, e){
							var fm = this.cw.items.itemAt(0).getForm();
							var tip = this.cw.items.itemAt(1).getEl();
							tip.innerHTML = String.format(tpl, '&#160;');
							if( fm.isValid() ){
								btn.setDisabled(true);
								var params = config.cparams;
								params = Ext.apply(params ? ( typeof params == 'object' ?
					   				params : typeof params == 'function' ?
					   				(params)() : {} ) : {}, fm.getValues());
					   			for( key in params ) {
					   				params[key] = encodeURIComponent(params[key]);
					   			}
								Ext.Ajax.request({
								   	url: config.submitURL,
									method: 'post',
								   	failure: function(){btn.setDisabled(false);},
								   	params: params,
									scope: this,
								   	success: function(r, p) {
								   		var result = Ext.decode(r.responseText);
										if( result) {
											// 保存成功
											if(result.type == '1' ){
												this.reloadGrid();
												this.cw.hide();
												tip.innerHTML = String.format(tpl, '&#160;');
											}else{
												var msg = result && result.message || '保存失败!';
												tip.innerHTML = String.format(tpl, msg);											
											}
										} else {
											var msg = result && result.message || '保存失败!';
											tip.innerHTML = String.format(tpl, msg);
										}
										btn.setDisabled(false);
								   	}
								});
							}
						}}, {
						text: '关闭',
						scope: this,	// combo
						handler: function(){
							this.cw.hide();
						}
					}],
					buttonAlign: 'center'
				});
				this.cw.on('hide', function(){
					this.itemAt(0).getForm().reset();
					var tip = this.itemAt(1).getEl();
					tip.innerHTML = String.format(tpl, '&#160;');
				}, this.cw.items);
				this.cw.on('show', function(){
					this.canCollapse = false;
				}, this);
				this.cw.on('hide', function(){
					this.canCollapse = true;					
				}, this);
				this.cw.render(this.power);
				this.cw.body.setStyle('background-color','white');
			};
		})(this.config) ,
		
		// private method
	    initUpdateWindow: (function(config){
			return function(record) {
				var tpl = '<span style="color:red;padding-left:15px;">{0}</span>';
				var form = Ext.apply({}, (config.updateForm)(record), {
					xtype : 'form',
					border: false,
					defaultType : 'textfield',
					defaults:{
						width : 200,
						allowBlank : false,
						labelSeparator : '',
						labelStyle:'text-align:right',
						validationEvent: 'change',
						validateOnBlur: false,
						stripCharsRe: /^\s+|\s+$/g
					}
				});
				this.uw  = new Ext.Window({
					title : '新建',
					width : config.formWidth||this.grid.width,
					shadow: false,
					hideParent: true,
					resizable: false,
					closeAction: 'hide',
					defaults: { bodyStyle: 'padding:15px' },
					items : [form, {xtype:'tbtext', text: String.format(tpl, '&#160;')}],
					buttons: [{
						text: '保存',
						scope: this,	// combo
						handler: function(btn, e){
							var fm = this.uw.items.itemAt(0).getForm();
							var tip = this.uw.items.itemAt(1).getEl();
							tip.innerHTML = String.format(tpl, '&#160;');
							if( fm.isValid() ){
								btn.setDisabled(true);
								var params = config.uparams;
								params = Ext.apply(params ? ( typeof params == 'object' ?
					   				params : typeof params == 'function' ?
					   				(params)(record) : {} ) : {}, fm.getValues());
					   			for( key in params ) {
					   				params[key] = encodeURIComponent(params[key]);
					   			}
								Ext.Ajax.request({
								   	url: config.updateURL,
									method: 'post',
								   	failure: function(){btn.setDisabled(false);},
								   	params: params,
									scope: this,
								   	success: function(r, p) {
								   		var result = Ext.decode(r.responseText);
										if( result) {
											// 保存成功
											if(result.type == '1' ){
												this.reloadGrid();
												this.uw.hide();
												tip.innerHTML = String.format(tpl, '&#160;');
											}else{
												var msg = result && result.message || '保存失败!';
												tip.innerHTML = String.format(tpl, msg);											
											}
										} else {
											var msg = result && result.message || '保存失败!';
											tip.innerHTML = String.format(tpl, msg);
										}
										btn.setDisabled(false);
								   	}
								});
							}
						}}, {
						text: '关闭',
						scope: this,	// combo
						handler: function(){
							this.uw.hide();
						}
					}],
					buttonAlign: 'center'
				});
				this.uw.on('hide', function(){
					this.itemAt(0).getForm().reset();
					var tip = this.itemAt(1).getEl();
					tip.innerHTML = String.format(tpl, '&#160;');
				}, this.uw.items);
				this.uw.on('show', function(){
					this.canCollapse = false;
				}, this);
				this.uw.on('hide', function(){
					this.canCollapse = true;					
				}, this);
				this.uw.render(this.power);
				this.uw.body.setStyle('background-color','white');
			};
		})(this.config) ,
		
		onSelect : (function(config){
			return function(record, index) {
				if(typeof config.onSelect == 'function'){
// setTimeout(function(){
				        Ext.MessageBox.show({
				           title: 'Please wait',
				           msg: 'Loading items...',
				           progressText: 'Initializing...',
				           width:300,
				           progress:true,
				           closable:false
				       });
						(config.onSelect)(record, index);
						Ext.MessageBox.hide();
// },0);
				}
			}; 
		})(this.config)
	}
};

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.plugins.ComplexGridCombox.js"></script>
 */

/**
 * @namespace Ext.plugins.ComplexGridCombox
 */
Ext.ns('Ext.plugins.ComplexGridCombox');

/**
 * 在列表中加入科目选择列
 * 
 * @alias Ext.plugins.ComplexGridCombox
 * @author Tingjia Chen tingjia.chentj@alibaba-inc.com
 */
Ext.plugins.ComplexGridCombox = function(config){
	this.config = Ext.apply({}, config, {
		     width: 420,
		    height: 300,
		  pageSize: 10,
		   paginal: false,
		 creatable: true,
		 deletable: false,
		searchable: true,
		 multiable: false,
		stripeRows: true,
	   columnLines: true,
		  moreText: null,
	 triggerAction: null,		// 'power'
	     deleteURL: null,
	     submitURL: null,
	        params: null, // for datasource
	       dparams: null, // for delete
	       cparams: null, // for create
	        reload: true, // reload on expand
	     autoField: null,
	valueDataIndex: [],
		  loadMask: true
	});
};

Ext.extend(Ext.plugins.ComplexGridCombox, Ext.util.Observable, {
	
	/**
	 * @param combo /
	 *            grid
	 * @method
	 */
    init : function(combo){
    	if( combo.getColumnModel ) { // combo is a grid
	    	var cm = combo.getColumnModel();
	    	cm.getColumnsBy( function(c, i) {
	    		if( c.editor ) {
	    			c.editor.ignoreNoChange = true;
	    			c.editor.dataIndex = c.dataIndex;
	    			c.editor.valueDataIndex = [].concat(c.valueDataIndex || []);
	    			c.editor.startEdit = c.editor.startEdit.createInterceptor(function(el, value) {
	    				this.field.cellEl = Ext.get(el);
	    				this.field.editing = false;
	    				this.field.col = this.col;
	    				this.field.row = this.row;
						this.field.owerGrid = combo;
	    				this.field.record = this.record;
	    				this.field.dataIndex = this.dataIndex;
	    				this.field.valueDataIndex = this.valueDataIndex;
						this.field.validateOnBlur = false;
	    				return true;
	    			}, c.editor).createSequence(function(el, value) {
	    				this.field.editing = true;
	    			}, c.editor);
	    			
				    c.editor.completeEdit = function(remainVisible){
				        if(!this.editing){
				            return;
				        }
				        var v = this.getValue();
				        if(String(v) === String(this.startValue) && this.ignoreNoChange){
				            this.editing = false;
				            this.hide();
				            return;
				        }
				        if(this.revertInvalid !== false && !this.field.isValid()){
				            v = this.startValue;
				            this.cancelEdit(true);
				        }
				        if(this.fireEvent("beforecomplete", this, v, this.startValue) !== false){
				            this.editing = false;
				            if(this.updateEl && this.boundEl){
				                this.boundEl.update(v);
				            }
				            if(remainVisible !== true){
					            if(this.field.powerExpand){
									this.field.fireEvent("change",this.field);
					            }
				                this.hide();
				            }
				            this.fireEvent("complete", this, v, this.startValue);
				        }
				    };

	    		}
	    	});
	    	return;
    	}
    	
    	// combo is a combobox
    	combo.addEvents(
            /**
			 * @event beforeexpand
			 * @param {Ext.form.ComboBox}
			 *            combo This combo box
			 */
            'beforeexpand',
            /**
			 * @event afterexpand
			 * @param {Ext.form.ComboBox}
			 *            combo This combo box
			 */
            'afterexpand',
            'restrictHeight'
        );
        
		Ext.apply(combo, {
			initList: combo.initList.createSequence((function(config){
				return config.moreText ? function() {
					if(!this.more){
	                	this.more = this.list.createChild();
	                	this.morebar = new Ext.Toolbar({
	                		renderTo: this.more
	                	});
	                	this.morebar.add({
	                		text: config.moreText,
	                		handler: function() {
        						this.list.setHeight(10);
	                			this.powerExpand();
	                		},
	                		scope: this
	                	});
	                	this.assetHeight += this.more.getHeight();
					}
				} : Ext.emptyFn;
			})(this.config), combo).createInterceptor((function(config){
				// "修改"页面时初始化下拉框的值
				return function(){
					if(config.hiddenValue){
						this.store.on('load', function(){
							for( var key in config.hiddenValue ) {
								var i = this.store.findBy(function(r){
									return r.get(key) == config.hiddenValue[key];
								});
								if( i != -1 ) { // found
									var r = this.store.getAt(i);
									this.value = r.get(this.displayField);
									for( var j = 0, l = config.valueDataIndex.length; j < l; j++ ) {
										var k = config.valueDataIndex[j];
										if( config.hiddenField ) {
											var f = Ext.get(config.hiddenField[k]);
											if( f ) f.set({value:r.get(k)});
										} else {
											this.record.set(k, v); // TODO fix
																	// 可能this.record会不存在
										}
									}
									this.initValue();
									break;
								}
							}
						}, this, {single: true});
					}
					return true;
				}
			})(this.config), combo).createInterceptor((function(config){
				return function() {
					if(!this.list&&this.store){
						if(config.autocomplete){
							this.store.load();
							// 实现autocomplete的多字段支持
							this.store.createFilterFn = (function(scope, method, field) {
								return function() {
									var fn = [];
									for( var i = 0, n = field.length; i < n; i++ ) {
										fn[i] = (method.apply(scope, [field[i]].concat(Array.prototype.slice.call(arguments, 1))));
										if( fn[i] === false ) return false;
									}
									return function(r) {
										var result = false;
										for( var i = 0, n = fn.length; i < n; i++ ) {
											result = result || (fn[i] ? (fn[i])(r) : false);
										}
										return result;
									};
								};
							})(this.store, this.store.createFilterFn, [].concat(config.autoField));
						}else{
							return false;
						}
					}
					return true;
				};
			})(this.config), combo), // eo initList
			
			onRender: combo.onRender.createSequence(function() {
				if (!this.lazyInit) {
					this.initGrid();
				} else {
					this.on('focus', this.initGrid, this, {	single : true });
				}
			}, combo), // eo onRender
			
			restrictHeight: combo.restrictHeight.createSequence(function() {
				this.fireEvent('restrictHeight', this);
			}, combo), // eo restrictHeight
			
			getValue: combo.getValue.createInterceptor(function() {
				if(this.forceSelection){
		            this.doForce();
		        }
		        return true;
		    }, combo), // eo getValue
			
			expand: combo.expand.createSequence(function() {
			    this.list.getShim().setLeft(this.list.getShim().getLeft()-(Ext.isIE6 ? 10:0));
		    }, combo), // eo expand
			
			onLoad: combo.onLoad.createSequence(function() {
			    try {this.list.getShim().setLeft(this.list.getShim().getLeft()-(Ext.isIE6 ? 10:0));}
			    catch(ex){}
		    }, combo), // eo onLoad
			
			doQuery: combo.doQuery.createSequence((function(config) {
				return function(q) {
		        	if( this.el.dom.value == '' || this.el.dom.value === null ) {
						// 当无值时清除其它相关字段值
						if( (q === undefined || q === null || q == '') ) {
							this.clearValue();
							for( var j = 0, l = config.valueDataIndex.length; j < l; j++ ) {
								var k = config.valueDataIndex[j];
								if( config.hiddenField ) {
									var f = Ext.get(config.hiddenField[k]);
									if( f ) f.set({value: ''});
								} else {
									if (this.record) { // add
										if(this.record.get(k) != undefined) {
											// this.record.set(k, ''); //
											// set会重新render，导致cell失去焦点？
											this.record.data[k] = '';
										}
									}
								}
							}
						}
		        	}
				};
			})(this.config), combo), // eo doQuery
		    
			initEvents: combo.initEvents.createSequence(function() {
				if( this.keyNav ) {
					this.keyNav['esc'] = this.keyNav['esc'].createSequence(
						function() {
							this.powerCollapse();
						}
					, this);
					this.keyNav['doRelay'] = this.keyNav['doRelay'].createInterceptor(
						function(foo, bar, hname) {
			                if(hname == 'esc' || this.scope.isPowerExpanded()){
			                   return Ext.KeyNav.prototype.doRelay.apply(this, arguments);
			                }
			                return true;
						}
					, this.keyNav);
				}
			}, combo), // eo initEvents
			
			onTriggerClick: combo.onTriggerClick.createInterceptor((function(config){
				if(config.triggerAction == 'power'){
					return function() {	
						// show grid
				        if (!this.disabled) {
							if (this.isPowerExpanded()) {
								this.powerCollapse();
								this.el.focus();
							} else {
								this.onFocus({});
								this.powerExpand();
								if(this.grid.getSelectionModel().hasSelection()&&!config.multiable){
									this.grid.view.focusRow(this.grid.getSelectionModel().last);
								}
								this.grid.focus();
							}
						}
						return false;
					};
				}else if(config.triggerAction == 'tree'){
					return function() {	
						// show tree
				        if (!this.disabled) {
							if (this.isPowerExpanded()) {
								this.powerCollapse();
								this.el.focus();
							} else {
								this.onFocus({});
								this.powerExpand();
							}
						}
						return false;
					}; 
				}
			})(this.config), combo).createInterceptor(function() {
				if(this.isExpanded()){
					return true;
				} else {
					return this.fireEvent('beforeexpand', this);
				}
			}).createSequence(function() {
				if(this.isExpanded()){
					this.fireEvent('afterexpand', this, this.list);
				}else if (this.isPowerExpanded()) {
					this.fireEvent('afterexpand', this, this.power, this.grid);
				}
			}), // eo onTriggerClick
			
			onSelect: combo.onSelect.createInterceptor((function(config){
				return function(record, index) {
					if(this.fireEvent('beforeselect', this, record, index) !== false){
						// TODO 暂未考虑多选情况
						// 使用值列来保存选定的结果
						for( var j = 0, l = config.valueDataIndex.length; j < l; j++ ) {
							var v = (config.formatValue || function(r, field){
					        	if( r.length ) {
					        		var values = [];
					        		for( var i = 0, len = r.length; i < len; i++ ) {
					        			values.push(r[i].get(field));
					        		}
					        		return values.join(', ');
					        	} else {
					        		return r.get(field);
					        	}
					        })(record, config.valueDataIndex[j]);
							if( config.hiddenField ) {
								var f = Ext.get(config.hiddenField[config.valueDataIndex[j]]);
								if( f ) f.set({value:v});
							} else {
								// modify by xujiakun 2011-5-10
								// this.record.data[config.valueDataIndex[j]] =
								// v;
								
								// modify by xujiakun 2013-12-11
								// if (this.displayField !=
								// config.valueDataIndex[j]) {
									if (store && store.getAt(rowIndex)) {
										// modify by xujiakun 2013-12-11
										// store.getAt(rowIndex).data[config.valueDataIndex[j]]
										// = v;
										var rec = store.getAt(rowIndex);
										rec.set(config.valueDataIndex[j], v);
										rec.commit();
									}
								 // }
							}
							if(config.multiable&&this.displayField==config.valueDataIndex[j]){
								var f = Ext.get(this.el.dom.id);
								// modify by xujiakun 2013-10-25
								// if( f ) f.set({value:v});
								if ( f ) {
									if ( Ext.isIE ) {
										f.set({value:v});
									} else {
										document.getElementById(f.id).value = {value:v}.value;
									}
								}
							}
						}
						this.on('change', function() {
							if( this.record ) {
								var old = this.record.get(config.valueDataIndex[0]);
								this.record.data[config.valueDataIndex[0]] = '';
								this.record.set(config.valueDataIndex[0], old);
							}
						}, this, {single: true});
						if(config.multiable){
							return false;
						}else{
							return true;
						}
					}else{
						return false;
					}
				}; // eo return
			})(this.config), combo)// eo onSelect
			
		},new Ext.ux.PowerSelectBase(this.config)); // eo apply
    }
});

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.ux.MonthPickerPlugin.js"></script>
 */

/*
 * ! Copyright(c) 2009 Costin Bereveanu, KEYPOINT SOLUTIONS costin@keypoint.ro
 * http://www.keypoint.ro/ext/extensions/license.txt
 */
Ext.ux.MonthPickerPlugin = function() {
	var picker;
	var oldDateDefaults;

	this.init = function(pk) {
		picker = pk;
		picker.onTriggerClick = picker.onTriggerClick.createSequence(onClick);
		picker.getValue = picker.getValue.createInterceptor(setDefaultMonthDay)
				.createSequence(restoreDefaultMonthDay);
		picker.beforeBlur = picker.beforeBlur
				.createInterceptor(setDefaultMonthDay)
				.createSequence(restoreDefaultMonthDay);
	};

	function setDefaultMonthDay() {
		oldDateDefaults = Date.defaults.d;
		Date.defaults.d = 1;
		return true;
	}

	function restoreDefaultMonthDay(ret) {
		Date.defaults.d = oldDateDefaults;
		return ret;
	}

	function onClick(e, el, opt) {
		var p = picker.menu.picker;
		p.activeDate = p.activeDate.getFirstDateOfMonth();
		if (p.value) {
			p.value = p.value.getFirstDateOfMonth();
		}

		p.showMonthPicker();

		if (!p.disabled) {
			p.monthPicker.stopFx();
			p.monthPicker.show();

			p.mun(p.monthPicker, 'click', p.onMonthClick, p);
			p.mun(p.monthPicker, 'dblclick', p.onMonthDblClick, p);
			p.onMonthClick = p.onMonthClick.createSequence(pickerClick);
			p.onMonthDblClick = p.onMonthDblClick
					.createSequence(pickerDblclick);
			p.mon(p.monthPicker, 'click', p.onMonthClick, p);
			p.mon(p.monthPicker, 'dblclick', p.onMonthDblClick, p);
		}
	}

	function pickerClick(e, t) {
		var el = new Ext.Element(t);
		if (el.is('button.x-date-mp-cancel')) {
			picker.menu.hide();
		} else if (el.is('button.x-date-mp-ok')) {
			var p = picker.menu.picker;
			p.setValue(p.activeDate);
			p.fireEvent('select', p, p.value);
		}
	}

	function pickerDblclick(e, t) {
		var el = new Ext.Element(t);
		if (el.parent()
				&& (el.parent().is('td.x-date-mp-month') || el.parent()
						.is('td.x-date-mp-year'))) {

			var p = picker.menu.picker;
			p.setValue(p.activeDate);
			p.fireEvent('select', p, p.value);
		}
	}
};

Ext.preg('monthPickerPlugin', Ext.ux.MonthPickerPlugin);

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.ux.form.DateTimeField.js"></script>
 */

Ext.ns('Ext.ux.form');

Ext.ux.form.TimePickerField = function(config) {
	Ext.ux.form.TimePickerField.superclass.constructor.call(this, config);
}

Ext.extend(Ext.ux.form.TimePickerField, Ext.form.Field, {
			defaultAutoCreate : {
				tag : 'div'
			},
			cls : 'x-form-timepickerfield',
			hoursSpinner : null,
			minutesSpinner : null,
			secondsSpinner : null,
			spinnerCfg : {
				width : 40
			},
			spinnerFixBoundries : function(value) {
				if (value < this.field.minValue) {
					value = this.field.maxValue;
				}
				if (value > this.field.maxValue) {
					value = this.field.minValue;
				}
				return this.fixPrecision(value);
			},
			onRender : function(ct, position) {
				Ext.ux.form.TimePickerField.superclass.onRender.call(this, ct,
						position);
				this.rendered = false;
				this.date = new Date();
				var values = {};
				if (this.value) {
					values = this._valueSplit(this.value);
					this.date.setHours(values.h);
					this.date.setMinutes(values.m);
					this.date.setSeconds(values.s);
					delete this.value;
				} else {
					values = {
						h : this.date.getHours(),
						m : this.date.getMinutes(),
						s : this.date.getSeconds()
					};
				}
				var spinnerWrap = Ext.DomHelper.append(this.el, {
							tag : 'div'
						});
				var cfg = Ext.apply({}, this.spinnerCfg, {
							renderTo : spinnerWrap,
							readOnly : this.readOnly,
							disabled : this.disabled,
							listeners : {
								spin : {
									fn : this.onSpinnerChange,
									scope : this
								},
								valid : {
									fn : this.onSpinnerChange,
									scope : this
								},
								afterrender : {
									fn : function(spinner) {
										spinner.wrap.applyStyles('float: left');
									},
									single : true
								}
							}
						});
				this.hoursSpinner = new Ext.ux.form.SpinnerField(Ext.apply({},
						cfg, {
							minValue : 0,
							maxValue : 23,
							cls : 'first',
							value : values.h
						}));
				this.minutesSpinner = new Ext.ux.form.SpinnerField(Ext.apply(
						{}, cfg, {
							minValue : 0,
							maxValue : 59,
							value : values.m
						}));
				this.secondsSpinner = new Ext.ux.form.SpinnerField(Ext.apply(
						{}, cfg, {
							minValue : 0,
							maxValue : 59,
							value : values.s
						}));
				Ext.DomHelper.append(spinnerWrap, {
							tag : 'div',
							cls : 'x-form-clear-left'
						});
				this.rendered = true;
			},
			_valueSplit : function(v) {
				var split = v.split(':');
				return {
					h : split.length > 0 ? split[0] : 0,
					m : split.length > 1 ? split[1] : 0,
					s : split.length > 2 ? split[2] : 0
				};
			},
			onSpinnerChange : function() {
				if (!this.rendered) {
					return;
				}
				this.fireEvent('change', this, this.getRawValue());
			},
			disable : function() {
				Ext.ux.form.TimePickerField.superclass.disable.call(this);
				this.hoursSpinner.disable();
				this.minutesSpinner.disable();
				this.secondsSpinner.disable();
			},
			enable : function() {
				Ext.ux.form.TimePickerField.superclass.enable.call(this);
				this.hoursSpinner.enable();
				this.minutesSpinner.enable();
				this.secondsSpinner.enable();
			},
			setReadOnly : function(r) {
				Ext.ux.form.TimePickerField.superclass.setReadOnly
						.call(this, r);
				this.hoursSpinner.setReadOnly(r);
				this.minutesSpinner.setReadOnly(r);
				this.secondsSpinner.setReadOnly(r);
			},
			clearInvalid : function() {
				Ext.ux.form.TimePickerField.superclass.clearInvalid.call(this);
				this.hoursSpinner.clearInvalid();
				this.minutesSpinner.clearInvalid();
				this.secondsSpinner.clearInvalid();
			},
			getRawValue : function() {
				if (!this.hoursSpinner) {
					this.date = new Date();
					return {
						h : this.date.getHours(),
						m : this.date.getMinutes(),
						s : this.date.getSeconds()
					};
				} else {
					return {
						h : this.hoursSpinner.getValue(),
						m : this.minutesSpinner.getValue(),
						s : this.secondsSpinner.getValue()
					};
				}
			},
			setRawValue : function(v) {
				this.hoursSpinner.setValue(v.h);
				this.minutesSpinner.setValue(v.m);
				this.secondsSpinner.setValue(v.s);
			},
			isValid : function(preventMark) {
				return this.hoursSpinner.isValid(preventMark)
						&& this.minutesSpinner.isValid(preventMark)
						&& this.secondsSpinner.isValid(preventMark);
			},
			validate : function() {
				return this.hoursSpinner.validate()
						&& this.minutesSpinner.validate()
						&& this.secondsSpinner.validate();
			},
			getValue : function() {
				var v = this.getRawValue();
				return String.leftPad(v.h, 2, '0') + ':'
						+ String.leftPad(v.m, 2, '0') + ':'
						+ String.leftPad(v.s, 2, '0');
			},
			setValue : function(value) {
				if (!this.rendered) {
					this.value = value;
					return;
				}
				value = this._valueSplit(value);
				this.setRawValue(value);
				this.validate();
			}
		});

Ext.form.TimePickerField = Ext.ux.form.TimePickerField;

Ext.reg('timepickerfield', Ext.form.TimePickerField);

Ext.ns('Ext.ux.form');

Ext.DateTimePicker = Ext.extend(Ext.DatePicker, {
			timeFormat : 'g:i:s A',
			timeLabel : '时间',
			timeWidth : 100,
			initComponent : function() {
				Ext.DateTimePicker.superclass.initComponent.call(this);
				this.id = Ext.id();
			},
			onRender : function(container, position) {
				Ext.DateTimePicker.superclass.onRender.apply(this, arguments);
				var table = Ext.get(Ext.DomQuery.selectNode('table tbody',
						container.dom));
				var tfEl = Ext.DomHelper.insertBefore(table.last(), {
							tag : 'tr',
							children : [{
										tag : 'td',
										cls : 'x-date-bottom',
										html : this.timeLabel,
										style : 'font-size:12; width:30;'
									}, {
										tag : 'td',
										cls : 'x-date-bottom ux-timefield',
										colspan : '2'
									}]
						}, true);
				this.tf.render(table.child('td.ux-timefield'));
				var p = this.getEl().parent('div.x-layer');
				if (p) {
					p.setStyle("height", p.getHeight() + 31);
				}
			},
			setValue : function(value) {
				var old = this.value;
				if (!this.tf) {
					this.tf = new Ext.ux.form.TimePickerField();
					this.tf.ownerCt = this;
				}
				this.value = this.getDateTime(value);
			},
			getDateTime : function(value) {
				if (this.tf) {
					var dt = new Date();
					var timeval = this.tf.getValue();
					value = Date.parseDate(value.format(this.dateFormat) + ' '
									+ this.tf.getValue(), this.format);
				}
				return value;
			},
			selectToday : function() {
				if (this.todayBtn && !this.todayBtn.disabled) {
					this.value = this.getDateTime(new Date());
					this.fireEvent("select", this, this.value);
				}
			}
		});

Ext.reg('datetimepickerfield', Ext.DateTimePicker);

if (parseInt(Ext.version.substr(0, 1), 10) > 2) {
	Ext.menu.DateTimeItem = Ext.DateTimePicker;
	Ext.override(Ext.menu.DateMenu, {
				initComponent : function() {
					this.on('beforeshow', this.onBeforeShow, this);
					if (this.strict = (Ext.isIE7 && Ext.isStrict)) {
						this.on('show', this.onShow, this, {
									single : true,
									delay : 20
								});
					}
					Ext.apply(this, {
								plain : true,
								showSeparator : false,
								items : this.picker = new Ext.DatePicker(Ext
										.apply({
													internalRender : this.strict
															|| !Ext.isIE,
													ctCls : 'x-menu-date-item'
												}, this.initialConfig))
							});
					Ext.menu.DateMenu.superclass.initComponent.call(this);
					this.relayEvents(this.picker, ["select"]);
					this.on('select', this.menuHide, this);
					if (this.handler) {
						this.on('select', this.handler, this.scope || this);
					}
				}
			});
} else {
	Ext.menu.DateTimeItem = function(config) {
		Ext.menu.DateTimeItem.superclass.constructor.call(this,
				new Ext.DateTimePicker(config), config);
		this.picker = this.component;
		this.addEvents('select');

		this.picker.on("render", function(picker) {
					picker.getEl().swallowEvent("click");
					picker.container.addClass("x-menu-date-item");
				});

		this.picker.on("select", this.onSelect, this);
	};

	Ext.extend(Ext.menu.DateTimeItem, Ext.menu.DateMenu, {
				onSelect : function(picker, date) {
					this.fireEvent("select", this, date, picker);
					Ext.menu.DateTimeItem.superclass.handleClick.call(this);
				}
			});
}

Ext.menu.DateTimeMenu = function(config) {
	Ext.menu.DateTimeMenu.superclass.constructor.call(this, config);
	this.plain = true;
	var di = new Ext.menu.DateTimeItem(config);
	this.add(di);
	this.picker = di;
	this.relayEvents(di, ["select"]);

	this.on('beforeshow', function() {
				if (this.picker) {
					this.picker.hideMonthPicker(true);
				}
			}, this);
};

Ext.extend(Ext.menu.DateTimeMenu, Ext.menu.Menu, {
	cls : 'x-date-menu',
	beforeDestroy : function() {
		this.picker.destroy();
	},
	hide : function(deep) {
		if (this.picker.tf.innerList) {
			if ((Ext.EventObject.within(this.picker.tf.innerList))
					|| (Ext.get(Ext.EventObject.getTarget()) == this.picker.tf.innerList))
				return false;
		}
		if (this.el && this.isVisible()) {
			this.fireEvent("beforehide", this);
			if (this.activeItem) {
				this.activeItem.deactivate();
				this.activeItem = null;
			}
			this.el.hide();
			this.hidden = true;
			this.fireEvent("hide", this);
		}
		if (deep === true && this.parentMenu) {
			this.parentMenu.hide(true);
		}
	}
});

Ext.ux.form.DateTimeField = Ext.extend(Ext.form.DateField, {
			dateFormat : 'Y-m-d',
			timeFormat : 'H:i:s',
			defaultAutoCreate : {
				tag : "input",
				type : "text",
				size : "20",
				autocomplete : "off"
			},
			initComponent : function() {
				Ext.ux.form.DateTimeField.superclass.initComponent.call(this);
				this.format = this.dateFormat + ' ' + this.timeFormat;
				this.afterMethod('afterRender', function() {
							this.getEl().applyStyles('top:0');
						});
			},
			getValue : function() {
				return this.parseDate(Ext.form.DateField.superclass.getValue
						.call(this))
						|| '';
			},
			onTriggerClick : function() {
				if (this.disabled) {
					return;
				}
				if (this.menu == null) {
					this.menu = new Ext.menu.DateTimeMenu();
				}
				Ext.apply(this.menu.picker, {
							minDate : this.minValue,
							maxDate : this.maxValue,
							disabledDatesRE : this.ddMatch,
							disabledDatesText : this.disabledDatesText,
							disabledDays : this.disabledDays,
							disabledDaysText : this.disabledDaysText,
							format : this.format,
							timeFormat : this.timeFormat,
							dateFormat : this.dateFormat,
							showToday : this.showToday,
							minText : String.format(this.minText, this
											.formatDate(this.minValue)),
							maxText : String.format(this.maxText, this
											.formatDate(this.maxValue))
						});
				if (this.menuEvents) {
					this.menuEvents('on');
				} else {
					this.menu.on(Ext.apply({}, this.menuListeners, {
								scope : this
							}));
				}
				this.menu.picker.setValue(this.getValue() || new Date());
				this.menu.show(this.el, "tl-bl?");
			}
		});

Ext.reg('datetimefield', Ext.ux.form.DateTimeField);

/**
 * <script type="text/javascript" src="$!{env.imgUrl}/js/ext/Ext.ux.Spinner.js"></script>
 */

/*
 * ! Ext JS Library 3.4.0 Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com http://www.sencha.com/license
 */
/**
 * @class Ext.ux.Spinner
 * @extends Ext.util.Observable Creates a Spinner control utilized by
 *          Ext.ux.form.SpinnerField
 */
Ext.ux.Spinner = Ext.extend(Ext.util.Observable, {
    incrementValue: 1,
    alternateIncrementValue: 5,
    triggerClass: 'x-form-spinner-trigger',
    splitterClass: 'x-form-spinner-splitter',
    alternateKey: Ext.EventObject.shiftKey,
    defaultValue: 0,
    accelerate: false,

    constructor: function(config){
        Ext.ux.Spinner.superclass.constructor.call(this, config);
        Ext.apply(this, config);
        this.mimicing = false;
    },

    init: function(field){
        this.field = field;

        field.afterMethod('onRender', this.doRender, this);
        field.afterMethod('onEnable', this.doEnable, this);
        field.afterMethod('onDisable', this.doDisable, this);
        field.afterMethod('afterRender', this.doAfterRender, this);
        field.afterMethod('onResize', this.doResize, this);
        field.afterMethod('onFocus', this.doFocus, this);
        field.beforeMethod('onDestroy', this.doDestroy, this);
    },

    doRender: function(ct, position){
        var el = this.el = this.field.getEl();
        var f = this.field;

        if (!f.wrap) {
            f.wrap = this.wrap = el.wrap({
                cls: "x-form-field-wrap"
            });
        }
        else {
            this.wrap = f.wrap.addClass('x-form-field-wrap');
        }

        this.trigger = this.wrap.createChild({
            tag: "img",
            src: Ext.BLANK_IMAGE_URL,
            cls: "x-form-trigger " + this.triggerClass
        });

        if (!f.width) {
            this.wrap.setWidth(el.getWidth() + this.trigger.getWidth());
        }

        this.splitter = this.wrap.createChild({
            tag: 'div',
            cls: this.splitterClass,
            style: 'width:13px; height:2px;'
        });
        this.splitter.setRight((Ext.isIE) ? 1 : 2).setTop(10).show();

        this.proxy = this.trigger.createProxy('', this.splitter, true);
        this.proxy.addClass("x-form-spinner-proxy");
        this.proxy.setStyle('left', '0px');
        this.proxy.setSize(14, 1);
        this.proxy.hide();
        this.dd = new Ext.dd.DDProxy(this.splitter.dom.id, "SpinnerDrag", {
            dragElId: this.proxy.id
        });

        this.initTrigger();
        this.initSpinner();
    },

    doAfterRender: function(){
        var y;
        if (Ext.isIE && this.el.getY() != (y = this.trigger.getY())) {
            this.el.position();
            this.el.setY(y);
        }
    },

    doEnable: function(){
        if (this.wrap) {
            this.disabled = false;
            this.wrap.removeClass(this.field.disabledClass);
        }
    },

    doDisable: function(){
        if (this.wrap) {
	        this.disabled = true;
            this.wrap.addClass(this.field.disabledClass);
            this.el.removeClass(this.field.disabledClass);
        }
    },

    doResize: function(w, h){
        if (typeof w == 'number') {
            this.el.setWidth(w - this.trigger.getWidth());
        }
        this.wrap.setWidth(this.el.getWidth() + this.trigger.getWidth());
    },

    doFocus: function(){
        if (!this.mimicing) {
            this.wrap.addClass('x-trigger-wrap-focus');
            this.mimicing = true;
            Ext.get(Ext.isIE ? document.body : document).on("mousedown", this.mimicBlur, this, {
                delay: 10
            });
            this.el.on('keydown', this.checkTab, this);
        }
    },

    // private
    checkTab: function(e){
        if (e.getKey() == e.TAB) {
            this.triggerBlur();
        }
    },

    // private
    mimicBlur: function(e){
        if (!this.wrap.contains(e.target) && this.field.validateBlur(e)) {
            this.triggerBlur();
        }
    },

    // private
    triggerBlur: function(){
        this.mimicing = false;
        Ext.get(Ext.isIE ? document.body : document).un("mousedown", this.mimicBlur, this);
        this.el.un("keydown", this.checkTab, this);
        this.field.beforeBlur();
        this.wrap.removeClass('x-trigger-wrap-focus');
        this.field.onBlur.call(this.field);
    },

    initTrigger: function(){
        this.trigger.addClassOnOver('x-form-trigger-over');
        this.trigger.addClassOnClick('x-form-trigger-click');
    },

    initSpinner: function(){
        this.field.addEvents({
            'spin': true,
            'spinup': true,
            'spindown': true
        });

        this.keyNav = new Ext.KeyNav(this.el, {
            "up": function(e){
                e.preventDefault();
                this.onSpinUp();
            },

            "down": function(e){
                e.preventDefault();
                this.onSpinDown();
            },

            "pageUp": function(e){
                e.preventDefault();
                this.onSpinUpAlternate();
            },

            "pageDown": function(e){
                e.preventDefault();
                this.onSpinDownAlternate();
            },

            scope: this
        });

        this.repeater = new Ext.util.ClickRepeater(this.trigger, {
            accelerate: this.accelerate
        });
        this.field.mon(this.repeater, "click", this.onTriggerClick, this, {
            preventDefault: true
        });

        this.field.mon(this.trigger, {
            mouseover: this.onMouseOver,
            mouseout: this.onMouseOut,
            mousemove: this.onMouseMove,
            mousedown: this.onMouseDown,
            mouseup: this.onMouseUp,
            scope: this,
            preventDefault: true
        });

        this.field.mon(this.wrap, "mousewheel", this.handleMouseWheel, this);

        this.dd.setXConstraint(0, 0, 10)
        this.dd.setYConstraint(1500, 1500, 10);
        this.dd.endDrag = this.endDrag.createDelegate(this);
        this.dd.startDrag = this.startDrag.createDelegate(this);
        this.dd.onDrag = this.onDrag.createDelegate(this);
    },

    onMouseOver: function(){
        if (this.disabled) {
            return;
        }
        var middle = this.getMiddle();
        this.tmpHoverClass = (Ext.EventObject.getPageY() < middle) ? 'x-form-spinner-overup' : 'x-form-spinner-overdown';
        this.trigger.addClass(this.tmpHoverClass);
    },

    // private
    onMouseOut: function(){
        this.trigger.removeClass(this.tmpHoverClass);
    },

    // private
    onMouseMove: function(){
        if (this.disabled) {
            return;
        }
        var middle = this.getMiddle();
        if (((Ext.EventObject.getPageY() > middle) && this.tmpHoverClass == "x-form-spinner-overup") ||
        ((Ext.EventObject.getPageY() < middle) && this.tmpHoverClass == "x-form-spinner-overdown")) {
        }
    },

    // private
    onMouseDown: function(){
        if (this.disabled) {
            return;
        }
        var middle = this.getMiddle();
        this.tmpClickClass = (Ext.EventObject.getPageY() < middle) ? 'x-form-spinner-clickup' : 'x-form-spinner-clickdown';
        this.trigger.addClass(this.tmpClickClass);
    },

    // private
    onMouseUp: function(){
        this.trigger.removeClass(this.tmpClickClass);
    },

    // private
    onTriggerClick: function(){
        if (this.disabled || this.el.dom.readOnly) {
            return;
        }
        var middle = this.getMiddle();
        var ud = (Ext.EventObject.getPageY() < middle) ? 'Up' : 'Down';
        this['onSpin' + ud]();
    },

    // private
    getMiddle: function(){
        var t = this.trigger.getTop();
        var h = this.trigger.getHeight();
        var middle = t + (h / 2);
        return middle;
    },

    // private
    // checks if control is allowed to spin
    isSpinnable: function(){
        if (this.disabled || this.el.dom.readOnly) {
            Ext.EventObject.preventDefault(); // prevent scrolling when
												// disabled/readonly
            return false;
        }
        return true;
    },

    handleMouseWheel: function(e){
        // disable scrolling when not focused
        if (this.wrap.hasClass('x-trigger-wrap-focus') == false) {
            return;
        }

        var delta = e.getWheelDelta();
        if (delta > 0) {
            this.onSpinUp();
            e.stopEvent();
        }
        else
            if (delta < 0) {
                this.onSpinDown();
                e.stopEvent();
            }
    },

    // private
    startDrag: function(){
        this.proxy.show();
        this._previousY = Ext.fly(this.dd.getDragEl()).getTop();
    },

    // private
    endDrag: function(){
        this.proxy.hide();
    },

    // private
    onDrag: function(){
        if (this.disabled) {
            return;
        }
        var y = Ext.fly(this.dd.getDragEl()).getTop();
        var ud = '';

        if (this._previousY > y) {
            ud = 'Up';
        } // up
        if (this._previousY < y) {
            ud = 'Down';
        } // down
        if (ud != '') {
            this['onSpin' + ud]();
        }

        this._previousY = y;
    },

    // private
    onSpinUp: function(){
        if (this.isSpinnable() == false) {
            return;
        }
        if (Ext.EventObject.shiftKey == true) {
            this.onSpinUpAlternate();
            return;
        }
        else {
            this.spin(false, false);
        }
        this.field.fireEvent("spin", this);
        this.field.fireEvent("spinup", this);
    },

    // private
    onSpinDown: function(){
        if (this.isSpinnable() == false) {
            return;
        }
        if (Ext.EventObject.shiftKey == true) {
            this.onSpinDownAlternate();
            return;
        }
        else {
            this.spin(true, false);
        }
        this.field.fireEvent("spin", this);
        this.field.fireEvent("spindown", this);
    },

    // private
    onSpinUpAlternate: function(){
        if (this.isSpinnable() == false) {
            return;
        }
        this.spin(false, true);
        this.field.fireEvent("spin", this);
        this.field.fireEvent("spinup", this);
    },

    // private
    onSpinDownAlternate: function(){
        if (this.isSpinnable() == false) {
            return;
        }
        this.spin(true, true);
        this.field.fireEvent("spin", this);
        this.field.fireEvent("spindown", this);
    },

    spin: function(down, alternate){
        var v = parseFloat(this.field.getValue());
        var incr = (alternate == true) ? this.alternateIncrementValue : this.incrementValue;
        (down == true) ? v -= incr : v += incr;

        v = (isNaN(v)) ? this.defaultValue : v;
        v = this.fixBoundries(v);
        this.field.setRawValue(v);
    },

    fixBoundries: function(value){
        var v = value;

        if (this.field.minValue != undefined && v < this.field.minValue) {
            v = this.field.minValue;
        }
        if (this.field.maxValue != undefined && v > this.field.maxValue) {
            v = this.field.maxValue;
        }

        return this.fixPrecision(v);
    },

    // private
    fixPrecision: function(value){
        var nan = isNaN(value);
        if (!this.field.allowDecimals || this.field.decimalPrecision == -1 || nan || !value) {
            return nan ? '' : value;
        }
        return parseFloat(parseFloat(value).toFixed(this.field.decimalPrecision));
    },

    doDestroy: function(){
        if (this.trigger) {
            this.trigger.remove();
        }
        if (this.wrap) {
            this.wrap.remove();
            delete this.field.wrap;
        }

        if (this.splitter) {
            this.splitter.remove();
        }

        if (this.dd) {
            this.dd.unreg();
            this.dd = null;
        }

        if (this.proxy) {
            this.proxy.remove();
        }

        if (this.repeater) {
            this.repeater.purgeListeners();
        }
        if (this.mimicing){
            Ext.get(Ext.isIE ? document.body : document).un("mousedown", this.mimicBlur, this);
        }
    }
});

// backwards compat
Ext.form.Spinner = Ext.ux.Spinner;

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.ux.form.SpinnerField.js"></script>
 */

/*
 * ! Ext JS Library 3.4.0 Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com http://www.sencha.com/license
 */
Ext.ns('Ext.ux.form');

/**
 * @class Ext.ux.form.SpinnerField
 * @extends Ext.form.NumberField Creates a field utilizing Ext.ux.Spinner
 * @xtype spinnerfield
 */
Ext.ux.form.SpinnerField = Ext.extend(Ext.form.NumberField, {
    actionMode: 'wrap',
    deferHeight: true,
    autoSize: Ext.emptyFn,
    onBlur: Ext.emptyFn,
    adjustSize: Ext.BoxComponent.prototype.adjustSize,

	constructor: function(config) {
		var spinnerConfig = Ext.copyTo({}, config, 'incrementValue,alternateIncrementValue,accelerate,defaultValue,triggerClass,splitterClass');

		var spl = this.spinner = new Ext.ux.Spinner(spinnerConfig);

		var plugins = config.plugins
			? (Ext.isArray(config.plugins)
				? config.plugins.push(spl)
				: [config.plugins, spl])
			: spl;

		Ext.ux.form.SpinnerField.superclass.constructor.call(this, Ext.apply(config, {plugins: plugins}));
	},

    // private
    getResizeEl: function(){
        return this.wrap;
    },

    // private
    getPositionEl: function(){
        return this.wrap;
    },

    // private
    alignErrorIcon: function(){
        if (this.wrap) {
            this.errorIcon.alignTo(this.wrap, 'tl-tr', [2, 0]);
        }
    },

    validateBlur: function(){
        return true;
    }
});

Ext.reg('spinnerfield', Ext.ux.form.SpinnerField);

// backwards compat
Ext.form.SpinnerField = Ext.ux.form.SpinnerField;

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.ux.form.MultiSelect.js"></script>
 */

/*
 * ! Ext JS Library 3.4.0 Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com http://www.sencha.com/license
 */
Ext.ns('Ext.ux.form');

/**
 * @class Ext.ux.form.MultiSelect
 * @extends Ext.form.Field A control that allows selection and form submission
 *          of multiple list items.
 * 
 * @history 2008-06-19 bpm Original code contributed by Toby Stuart (with
 *          contributions from Robert Williams) 2008-06-19 bpm Docs and demo
 *          code clean up
 * 
 * @constructor Create a new MultiSelect
 * @param {Object}
 *            config Configuration options
 * @xtype multiselect
 */
Ext.ux.form.MultiSelect = Ext.extend(Ext.form.Field,  {
    /**
	 * @cfg {String} legend Wraps the object with a fieldset and specified
	 *      legend.
	 */
    /**
	 * @cfg {Ext.ListView} view The {@link Ext.ListView} used to render the
	 *      multiselect list.
	 */
    /**
	 * @cfg {String/Array} dragGroup The ddgroup name(s) for the MultiSelect
	 *      DragZone (defaults to undefined).
	 */
    /**
	 * @cfg {String/Array} dropGroup The ddgroup name(s) for the MultiSelect
	 *      DropZone (defaults to undefined).
	 */
    /**
	 * @cfg {Boolean} ddReorder Whether the items in the MultiSelect list are
	 *      drag/drop reorderable (defaults to false).
	 */
    ddReorder:false,
    /**
	 * @cfg {Object/Array} tbar The top toolbar of the control. This can be a
	 *      {@link Ext.Toolbar} object, a toolbar config, or an array of
	 *      buttons/button configs to be added to the toolbar.
	 */
    /**
	 * @cfg {String} appendOnly True if the list should only allow append drops
	 *      when drag/drop is enabled (use for lists which are sorted, defaults
	 *      to false).
	 */
    appendOnly:false,
    /**
	 * @cfg {Number} width Width in pixels of the control (defaults to 100).
	 */
    width:100,
    /**
	 * @cfg {Number} height Height in pixels of the control (defaults to 100).
	 */
    height:100,
    /**
	 * @cfg {String/Number} displayField Name/Index of the desired display field
	 *      in the dataset (defaults to 0).
	 */
    displayField:0,
    /**
	 * @cfg {String/Number} valueField Name/Index of the desired value field in
	 *      the dataset (defaults to 1).
	 */
    valueField:1,
    /**
	 * @cfg {Boolean} allowBlank False to require at least one item in the list
	 *      to be selected, true to allow no selection (defaults to true).
	 */
    allowBlank:true,
    /**
	 * @cfg {Number} minSelections Minimum number of selections allowed
	 *      (defaults to 0).
	 */
    minSelections:0,
    /**
	 * @cfg {Number} maxSelections Maximum number of selections allowed
	 *      (defaults to Number.MAX_VALUE).
	 */
    maxSelections:Number.MAX_VALUE,
    /**
	 * @cfg {String} blankText Default text displayed when the control contains
	 *      no items (defaults to the same value as
	 *      {@link Ext.form.TextField#blankText}.
	 */
    blankText:Ext.form.TextField.prototype.blankText,
    /**
	 * @cfg {String} minSelectionsText Validation message displayed when
	 *      {@link #minSelections} is not met (defaults to 'Minimum {0} item(s)
	 *      required'). The {0} token will be replaced by the value of
	 *      {@link #minSelections}.
	 */
    minSelectionsText:'Minimum {0} item(s) required',
    /**
	 * @cfg {String} maxSelectionsText Validation message displayed when
	 *      {@link #maxSelections} is not met (defaults to 'Maximum {0} item(s)
	 *      allowed'). The {0} token will be replaced by the value of
	 *      {@link #maxSelections}.
	 */
    maxSelectionsText:'Maximum {0} item(s) allowed',
    /**
	 * @cfg {String} delimiter The string used to delimit between items when set
	 *      or returned as a string of values (defaults to ',').
	 */
    delimiter:',',
    /**
	 * @cfg {Ext.data.Store/Array} store The data source to which this
	 *      MultiSelect is bound (defaults to <tt>undefined</tt>). Acceptable
	 *      values for this property are: <div class="mdetail-params">
	 *      <ul>
	 *      <li><b>any {@link Ext.data.Store Store} subclass</b></li>
	 *      <li><b>an Array</b> : Arrays will be converted to a
	 *      {@link Ext.data.ArrayStore} internally. <div class="mdetail-params">
	 *      <ul>
	 *      <li><b>1-dimensional array</b> : (e.g., <tt>['Foo','Bar']</tt>)<div
	 *      class="sub-desc"> A 1-dimensional array will automatically be
	 *      expanded (each array item will be the combo
	 *      {@link #valueField value} and {@link #displayField text})</div></li>
	 *      <li><b>2-dimensional array</b> : (e.g.,
	 *      <tt>[['f','Foo'],['b','Bar']]</tt>)<div class="sub-desc"> For a
	 *      multi-dimensional array, the value in index 0 of each item will be
	 *      assumed to be the combo {@link #valueField value}, while the value
	 *      at index 1 is assumed to be the combo {@link #displayField text}.
	 *      </div></li>
	 *      </ul>
	 *      </div></li>
	 *      </ul>
	 *      </div>
	 */
    
    cls: 'ux-form-multiselect',

    // private
    defaultAutoCreate : {tag: "div"},

    // private
    initComponent: function(){
        Ext.ux.form.MultiSelect.superclass.initComponent.call(this);

        if(Ext.isArray(this.store)){
            if (Ext.isArray(this.store[0])){
                this.store = new Ext.data.ArrayStore({
                    fields: ['value','text'],
                    data: this.store
                });
                this.valueField = 'value';
            }else{
                this.store = new Ext.data.ArrayStore({
                    fields: ['text'],
                    data: this.store,
                    expandData: true
                });
                this.valueField = 'text';
            }
            this.displayField = 'text';
        } else {
            this.store = Ext.StoreMgr.lookup(this.store);
        }

        this.addEvents({
            'dblclick' : true,
            'click' : true,
            'change' : true,
            'drop' : true
        });
    },

    // private
    onRender: function(ct, position){
        Ext.ux.form.MultiSelect.superclass.onRender.call(this, ct, position);

        var fs = this.fs = new Ext.form.FieldSet({
            renderTo: this.el,
            title: this.legend,
            height: this.height,
            width: this.width,
            style: "padding:0;",
            tbar: this.tbar
        });
        fs.body.addClass('ux-mselect');

        this.view = new Ext.ListView({
            selectedClass: 'ux-mselect-selected',
            multiSelect: true,
            store: this.store,
            columns: [{ header: 'Value', width: 1, dataIndex: this.displayField }],
            hideHeaders: true
        });

        fs.add(this.view);

        this.view.on('click', this.onViewClick, this);
        this.view.on('beforeclick', this.onViewBeforeClick, this);
        this.view.on('dblclick', this.onViewDblClick, this);

        this.hiddenName = this.name || Ext.id();
        var hiddenTag = { tag: "input", type: "hidden", value: "", name: this.hiddenName };
        this.hiddenField = this.el.createChild(hiddenTag);
        this.hiddenField.dom.disabled = this.hiddenName != this.name;
        fs.doLayout();
    },

    // private
    afterRender: function(){
        Ext.ux.form.MultiSelect.superclass.afterRender.call(this);

        if (this.ddReorder && !this.dragGroup && !this.dropGroup){
            this.dragGroup = this.dropGroup = 'MultiselectDD-' + Ext.id();
        }

        if (this.draggable || this.dragGroup){
            this.dragZone = new Ext.ux.form.MultiSelect.DragZone(this, {
                ddGroup: this.dragGroup
            });
        }
        if (this.droppable || this.dropGroup){
            this.dropZone = new Ext.ux.form.MultiSelect.DropZone(this, {
                ddGroup: this.dropGroup
            });
        }
    },

    // private
    onViewClick: function(vw, index, node, e) {
        this.fireEvent('change', this, this.getValue(), this.hiddenField.dom.value);
        this.hiddenField.dom.value = this.getValue();
        this.fireEvent('click', this, e);
        this.validate();
    },

    // private
    onViewBeforeClick: function(vw, index, node, e) {
        if (this.disabled || this.readOnly) {
            return false;
        }
    },

    // private
    onViewDblClick : function(vw, index, node, e) {
        return this.fireEvent('dblclick', vw, index, node, e);
    },

    /**
	 * Returns an array of data values for the selected items in the list. The
	 * values will be separated by {@link #delimiter}.
	 * 
	 * @return {Array} value An array of string data values
	 */
    getValue: function(valueField){
        var returnArray = [];
        var selectionsArray = this.view.getSelectedIndexes();
        if (selectionsArray.length == 0) {return '';}
        for (var i=0; i<selectionsArray.length; i++) {
            returnArray.push(this.store.getAt(selectionsArray[i]).get((valueField != null) ? valueField : this.valueField));
        }
        return returnArray.join(this.delimiter);
    },

    /**
	 * Sets a delimited string (using {@link #delimiter}) or array of data
	 * values into the list.
	 * 
	 * @param {String/Array}
	 *            values The values to set
	 */
    setValue: function(values) {
        var index;
        var selections = [];
        this.view.clearSelections();
        this.hiddenField.dom.value = '';

        if (!values || (values == '')) { return; }

        if (!Ext.isArray(values)) { values = values.split(this.delimiter); }
        for (var i=0; i<values.length; i++) {
            index = this.view.store.indexOf(this.view.store.query(this.valueField,
                new RegExp('^' + values[i] + '$', "i")).itemAt(0));
            selections.push(index);
        }
        this.view.select(selections);
        this.hiddenField.dom.value = this.getValue();
        this.validate();
    },

    // inherit docs
    reset : function() {
        this.setValue('');
    },

    // inherit docs
    getRawValue: function(valueField) {
        var tmp = this.getValue(valueField);
        if (tmp.length) {
            tmp = tmp.split(this.delimiter);
        }
        else {
            tmp = [];
        }
        return tmp;
    },

    // inherit docs
    setRawValue: function(values){
        setValue(values);
    },

    // inherit docs
    validateValue : function(value){
        if (value.length < 1) { // if it has no value
             if (this.allowBlank) {
                 this.clearInvalid();
                 return true;
             } else {
                 this.markInvalid(this.blankText);
                 return false;
             }
        }
        if (value.length < this.minSelections) {
            this.markInvalid(String.format(this.minSelectionsText, this.minSelections));
            return false;
        }
        if (value.length > this.maxSelections) {
            this.markInvalid(String.format(this.maxSelectionsText, this.maxSelections));
            return false;
        }
        return true;
    },

    // inherit docs
    disable: function(){
        this.disabled = true;
        this.hiddenField.dom.disabled = true;
        this.fs.disable();
    },

    // inherit docs
    enable: function(){
        this.disabled = false;
        this.hiddenField.dom.disabled = false;
        this.fs.enable();
    },

    // inherit docs
    destroy: function(){
        Ext.destroy(this.fs, this.dragZone, this.dropZone);
        Ext.ux.form.MultiSelect.superclass.destroy.call(this);
    }
});


Ext.reg('multiselect', Ext.ux.form.MultiSelect);

// backwards compat
Ext.ux.Multiselect = Ext.ux.form.MultiSelect;


Ext.ux.form.MultiSelect.DragZone = function(ms, config){
    this.ms = ms;
    this.view = ms.view;
    var ddGroup = config.ddGroup || 'MultiselectDD';
    var dd;
    if (Ext.isArray(ddGroup)){
        dd = ddGroup.shift();
    } else {
        dd = ddGroup;
        ddGroup = null;
    }
    Ext.ux.form.MultiSelect.DragZone.superclass.constructor.call(this, this.ms.fs.body, { containerScroll: true, ddGroup: dd });
    this.setDraggable(ddGroup);
};

Ext.extend(Ext.ux.form.MultiSelect.DragZone, Ext.dd.DragZone, {
    onInitDrag : function(x, y){
        var el = Ext.get(this.dragData.ddel.cloneNode(true));
        this.proxy.update(el.dom);
        el.setWidth(el.child('em').getWidth());
        this.onStartDrag(x, y);
        return true;
    },

    // private
    collectSelection: function(data) {
        data.repairXY = Ext.fly(this.view.getSelectedNodes()[0]).getXY();
        var i = 0;
        this.view.store.each(function(rec){
            if (this.view.isSelected(i)) {
                var n = this.view.getNode(i);
                var dragNode = n.cloneNode(true);
                dragNode.id = Ext.id();
                data.ddel.appendChild(dragNode);
                data.records.push(this.view.store.getAt(i));
                data.viewNodes.push(n);
            }
            i++;
        }, this);
    },

    // override
    onEndDrag: function(data, e) {
        var d = Ext.get(this.dragData.ddel);
        if (d && d.hasClass("multi-proxy")) {
            d.remove();
        }
    },

    // override
    getDragData: function(e){
        var target = this.view.findItemFromChild(e.getTarget());
        if(target) {
            if (!this.view.isSelected(target) && !e.ctrlKey && !e.shiftKey) {
                this.view.select(target);
                this.ms.setValue(this.ms.getValue());
            }
            if (this.view.getSelectionCount() == 0 || e.ctrlKey || e.shiftKey) return false;
            var dragData = {
                sourceView: this.view,
                viewNodes: [],
                records: []
            };
            if (this.view.getSelectionCount() == 1) {
                var i = this.view.getSelectedIndexes()[0];
                var n = this.view.getNode(i);
                dragData.viewNodes.push(dragData.ddel = n);
                dragData.records.push(this.view.store.getAt(i));
                dragData.repairXY = Ext.fly(n).getXY();
            } else {
                dragData.ddel = document.createElement('div');
                dragData.ddel.className = 'multi-proxy';
                this.collectSelection(dragData);
            }
            return dragData;
        }
        return false;
    },

    // override the default repairXY.
    getRepairXY : function(e){
        return this.dragData.repairXY;
    },

    // private
    setDraggable: function(ddGroup){
        if (!ddGroup) return;
        if (Ext.isArray(ddGroup)) {
            Ext.each(ddGroup, this.setDraggable, this);
            return;
        }
        this.addToGroup(ddGroup);
    }
});

Ext.ux.form.MultiSelect.DropZone = function(ms, config){
    this.ms = ms;
    this.view = ms.view;
    var ddGroup = config.ddGroup || 'MultiselectDD';
    var dd;
    if (Ext.isArray(ddGroup)){
        dd = ddGroup.shift();
    } else {
        dd = ddGroup;
        ddGroup = null;
    }
    Ext.ux.form.MultiSelect.DropZone.superclass.constructor.call(this, this.ms.fs.body, { containerScroll: true, ddGroup: dd });
    this.setDroppable(ddGroup);
};

Ext.extend(Ext.ux.form.MultiSelect.DropZone, Ext.dd.DropZone, {
    /**
	 * Part of the Ext.dd.DropZone interface. If no target node is found, the
	 * whole Element becomes the target, and this causes the drop gesture to
	 * append.
	 */
    getTargetFromEvent : function(e) {
        var target = e.getTarget();
        return target;
    },

    // private
    getDropPoint : function(e, n, dd){
        if (n == this.ms.fs.body.dom) { return "below"; }
        var t = Ext.lib.Dom.getY(n), b = t + n.offsetHeight;
        var c = t + (b - t) / 2;
        var y = Ext.lib.Event.getPageY(e);
        if(y <= c) {
            return "above";
        }else{
            return "below";
        }
    },

    // private
    isValidDropPoint: function(pt, n, data) {
        if (!data.viewNodes || (data.viewNodes.length != 1)) {
            return true;
        }
        var d = data.viewNodes[0];
        if (d == n) {
            return false;
        }
        if ((pt == "below") && (n.nextSibling == d)) {
            return false;
        }
        if ((pt == "above") && (n.previousSibling == d)) {
            return false;
        }
        return true;
    },

    // override
    onNodeEnter : function(n, dd, e, data){
        return false;
    },

    // override
    onNodeOver : function(n, dd, e, data){
        var dragElClass = this.dropNotAllowed;
        var pt = this.getDropPoint(e, n, dd);
        if (this.isValidDropPoint(pt, n, data)) {
            if (this.ms.appendOnly) {
                return "x-tree-drop-ok-below";
            }

            // set the insert point style on the target node
            if (pt) {
                var targetElClass;
                if (pt == "above"){
                    dragElClass = n.previousSibling ? "x-tree-drop-ok-between" : "x-tree-drop-ok-above";
                    targetElClass = "x-view-drag-insert-above";
                } else {
                    dragElClass = n.nextSibling ? "x-tree-drop-ok-between" : "x-tree-drop-ok-below";
                    targetElClass = "x-view-drag-insert-below";
                }
                if (this.lastInsertClass != targetElClass){
                    Ext.fly(n).replaceClass(this.lastInsertClass, targetElClass);
                    this.lastInsertClass = targetElClass;
                }
            }
        }
        return dragElClass;
    },

    // private
    onNodeOut : function(n, dd, e, data){
        this.removeDropIndicators(n);
    },

    // private
    onNodeDrop : function(n, dd, e, data){
        if (this.ms.fireEvent("drop", this, n, dd, e, data) === false) {
            return false;
        }
        var pt = this.getDropPoint(e, n, dd);
        if (n != this.ms.fs.body.dom)
            n = this.view.findItemFromChild(n);

        if(this.ms.appendOnly) {
            insertAt = this.view.store.getCount();
        } else {
            insertAt = n == this.ms.fs.body.dom ? this.view.store.getCount() - 1 : this.view.indexOf(n);
            if (pt == "below") {
                insertAt++;
            }
        }

        var dir = false;

        // Validate if dragging within the same MultiSelect
        if (data.sourceView == this.view) {
            // If the first element to be inserted below is the target node,
			// remove it
            if (pt == "below") {
                if (data.viewNodes[0] == n) {
                    data.viewNodes.shift();
                }
            } else {  // If the last element to be inserted above is the
						// target node, remove it
                if (data.viewNodes[data.viewNodes.length - 1] == n) {
                    data.viewNodes.pop();
                }
            }

            // Nothing to drop...
            if (!data.viewNodes.length) {
                return false;
            }

            // If we are moving DOWN, then because a store.remove() takes place
			// first,
            // the insertAt must be decremented.
            if (insertAt > this.view.store.indexOf(data.records[0])) {
                dir = 'down';
                insertAt--;
            }
        }

        for (var i = 0; i < data.records.length; i++) {
            var r = data.records[i];
            if (data.sourceView) {
                data.sourceView.store.remove(r);
            }
            this.view.store.insert(dir == 'down' ? insertAt : insertAt++, r);
            var si = this.view.store.sortInfo;
            if(si){
                this.view.store.sort(si.field, si.direction);
            }
        }
        return true;
    },

    // private
    removeDropIndicators : function(n){
        if(n){
            Ext.fly(n).removeClass([
                "x-view-drag-insert-above",
                "x-view-drag-insert-left",
                "x-view-drag-insert-right",
                "x-view-drag-insert-below"]);
            this.lastInsertClass = "_noclass";
        }
    },

    // private
    setDroppable: function(ddGroup){
        if (!ddGroup) return;
        if (Ext.isArray(ddGroup)) {
            Ext.each(ddGroup, this.setDroppable, this);
            return;
        }
        this.addToGroup(ddGroup);
    }
});

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.ux.form.ItemSelector.js"></script>
 */
		
/*
 * ! Ext JS Library 3.4.0 Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com http://www.sencha.com/license
 */
/*
 * Note that this control will most likely remain as an example, and not as a
 * core Ext form control. However, the API will be changing in a future release
 * and so should not yet be treated as a final, stable API at this time.
 */

/**
 * @class Ext.ux.form.ItemSelector
 * @extends Ext.form.Field A control that allows selection of between two
 *          Ext.ux.form.MultiSelect controls.
 * 
 * @history 2008-06-19 bpm Original code contributed by Toby Stuart (with
 *          contributions from Robert Williams)
 * 
 * @constructor Create a new ItemSelector
 * @param {Object}
 *            config Configuration options
 * @xtype itemselector
 */
Ext.ux.form.ItemSelector = Ext.extend(Ext.form.Field,  {
    hideNavIcons:false,
    imagePath:"../statics/image/itemSelector",
    iconUp:"up2.gif",
    iconDown:"down2.gif",
    iconLeft:"left2.gif",
    iconRight:"right2.gif",
    iconTop:"top2.gif",
    iconBottom:"bottom2.gif",
    drawUpIcon:true,
    drawDownIcon:true,
    drawLeftIcon:true,
    drawRightIcon:true,
    drawTopIcon:true,
    drawBotIcon:true,
    delimiter:',',
    bodyStyle:null,
    border:false,
    defaultAutoCreate:{tag: "div"},
    /**
	 * @cfg {Array} multiselects An array of {@link Ext.ux.form.MultiSelect}
	 *      config objects, with at least all required parameters (e.g., store)
	 */
    multiselects:null,

    initComponent: function(){
        Ext.ux.form.ItemSelector.superclass.initComponent.call(this);
        this.addEvents({
            'rowdblclick' : true,
            'change' : true
        });
    },

    onRender: function(ct, position){
        Ext.ux.form.ItemSelector.superclass.onRender.call(this, ct, position);

        // Internal default configuration for both multiselects
        var msConfig = [{
            legend: 'Available',
            draggable: true,
            droppable: true,
            width: 100,
            height: 100
        },{
            legend: 'Selected',
            droppable: true,
            draggable: true,
            width: 100,
            height: 100
        }];

        this.fromMultiselect = new Ext.ux.form.MultiSelect(Ext.applyIf(this.multiselects[0], msConfig[0]));
        this.fromMultiselect.on('dblclick', this.onRowDblClick, this);

        this.toMultiselect = new Ext.ux.form.MultiSelect(Ext.applyIf(this.multiselects[1], msConfig[1]));
        this.toMultiselect.on('dblclick', this.onRowDblClick, this);

        var p = new Ext.Panel({
            bodyStyle:this.bodyStyle,
            border:this.border,
            layout:"table",
            layoutConfig:{columns:3}
        });

        p.add(this.fromMultiselect);
        var icons = new Ext.Panel({header:false});
        p.add(icons);
        p.add(this.toMultiselect);
        p.render(this.el);
        icons.el.down('.'+icons.bwrapCls).remove();

        // ICON HELL!!!
        if (this.imagePath!="" && this.imagePath.charAt(this.imagePath.length-1)!="/")
            this.imagePath+="/";
        this.iconUp = this.imagePath + (this.iconUp || 'up2.gif');
        this.iconDown = this.imagePath + (this.iconDown || 'down2.gif');
        this.iconLeft = this.imagePath + (this.iconLeft || 'left2.gif');
        this.iconRight = this.imagePath + (this.iconRight || 'right2.gif');
        this.iconTop = this.imagePath + (this.iconTop || 'top2.gif');
        this.iconBottom = this.imagePath + (this.iconBottom || 'bottom2.gif');
        var el=icons.getEl();
        this.toTopIcon = el.createChild({tag:'img', src:this.iconTop, style:{cursor:'pointer', margin:'2px'}});
        el.createChild({tag: 'br'});
        this.upIcon = el.createChild({tag:'img', src:this.iconUp, style:{cursor:'pointer', margin:'2px'}});
        el.createChild({tag: 'br'});
        this.addIcon = el.createChild({tag:'img', src:this.iconRight, style:{cursor:'pointer', margin:'2px'}});
        el.createChild({tag: 'br'});
        this.removeIcon = el.createChild({tag:'img', src:this.iconLeft, style:{cursor:'pointer', margin:'2px'}});
        el.createChild({tag: 'br'});
        this.downIcon = el.createChild({tag:'img', src:this.iconDown, style:{cursor:'pointer', margin:'2px'}});
        el.createChild({tag: 'br'});
        this.toBottomIcon = el.createChild({tag:'img', src:this.iconBottom, style:{cursor:'pointer', margin:'2px'}});
        this.toTopIcon.on('click', this.toTop, this);
        this.upIcon.on('click', this.up, this);
        this.downIcon.on('click', this.down, this);
        this.toBottomIcon.on('click', this.toBottom, this);
        this.addIcon.on('click', this.fromTo, this);
        this.removeIcon.on('click', this.toFrom, this);
        if (!this.drawUpIcon || this.hideNavIcons) { this.upIcon.dom.style.display='none'; }
        if (!this.drawDownIcon || this.hideNavIcons) { this.downIcon.dom.style.display='none'; }
        if (!this.drawLeftIcon || this.hideNavIcons) { this.addIcon.dom.style.display='none'; }
        if (!this.drawRightIcon || this.hideNavIcons) { this.removeIcon.dom.style.display='none'; }
        if (!this.drawTopIcon || this.hideNavIcons) { this.toTopIcon.dom.style.display='none'; }
        if (!this.drawBotIcon || this.hideNavIcons) { this.toBottomIcon.dom.style.display='none'; }

        var tb = p.body.first();
        this.el.setWidth(p.body.first().getWidth());
        p.body.removeClass();

        this.hiddenName = this.name;
        var hiddenTag = {tag: "input", type: "hidden", value: "", name: this.name};
        this.hiddenField = this.el.createChild(hiddenTag);
    },
    
    doLayout: function(){
        if(this.rendered){
            this.fromMultiselect.fs.doLayout();
            this.toMultiselect.fs.doLayout();
        }
    },

    afterRender: function(){
        Ext.ux.form.ItemSelector.superclass.afterRender.call(this);

        this.toStore = this.toMultiselect.store;
        this.toStore.on('add', this.valueChanged, this);
        this.toStore.on('remove', this.valueChanged, this);
        this.toStore.on('load', this.valueChanged, this);
        this.valueChanged(this.toStore);
    },

    toTop : function() {
        var selectionsArray = this.toMultiselect.view.getSelectedIndexes();
        var records = [];
        if (selectionsArray.length > 0) {
            selectionsArray.sort();
            for (var i=0; i<selectionsArray.length; i++) {
                record = this.toMultiselect.view.store.getAt(selectionsArray[i]);
                records.push(record);
            }
            selectionsArray = [];
            for (var i=records.length-1; i>-1; i--) {
                record = records[i];
                this.toMultiselect.view.store.remove(record);
                this.toMultiselect.view.store.insert(0, record);
                selectionsArray.push(((records.length - 1) - i));
            }
        }
        this.toMultiselect.view.refresh();
        this.toMultiselect.view.select(selectionsArray);
    },

    toBottom : function() {
        var selectionsArray = this.toMultiselect.view.getSelectedIndexes();
        var records = [];
        if (selectionsArray.length > 0) {
            selectionsArray.sort();
            for (var i=0; i<selectionsArray.length; i++) {
                record = this.toMultiselect.view.store.getAt(selectionsArray[i]);
                records.push(record);
            }
            selectionsArray = [];
            for (var i=0; i<records.length; i++) {
                record = records[i];
                this.toMultiselect.view.store.remove(record);
                this.toMultiselect.view.store.add(record);
                selectionsArray.push((this.toMultiselect.view.store.getCount()) - (records.length - i));
            }
        }
        this.toMultiselect.view.refresh();
        this.toMultiselect.view.select(selectionsArray);
    },

    up : function() {
        var record = null;
        var selectionsArray = this.toMultiselect.view.getSelectedIndexes();
        selectionsArray.sort();
        var newSelectionsArray = [];
        if (selectionsArray.length > 0) {
            for (var i=0; i<selectionsArray.length; i++) {
                record = this.toMultiselect.view.store.getAt(selectionsArray[i]);
                if ((selectionsArray[i] - 1) >= 0) {
                    this.toMultiselect.view.store.remove(record);
                    this.toMultiselect.view.store.insert(selectionsArray[i] - 1, record);
                    newSelectionsArray.push(selectionsArray[i] - 1);
                }
            }
            this.toMultiselect.view.refresh();
            this.toMultiselect.view.select(newSelectionsArray);
        }
    },

    down : function() {
        var record = null;
        var selectionsArray = this.toMultiselect.view.getSelectedIndexes();
        selectionsArray.sort();
        selectionsArray.reverse();
        var newSelectionsArray = [];
        if (selectionsArray.length > 0) {
            for (var i=0; i<selectionsArray.length; i++) {
                record = this.toMultiselect.view.store.getAt(selectionsArray[i]);
                if ((selectionsArray[i] + 1) < this.toMultiselect.view.store.getCount()) {
                    this.toMultiselect.view.store.remove(record);
                    this.toMultiselect.view.store.insert(selectionsArray[i] + 1, record);
                    newSelectionsArray.push(selectionsArray[i] + 1);
                }
            }
            this.toMultiselect.view.refresh();
            this.toMultiselect.view.select(newSelectionsArray);
        }
    },

    fromTo : function() {
        var selectionsArray = this.fromMultiselect.view.getSelectedIndexes();
        var records = [];
        if (selectionsArray.length > 0) {
            for (var i=0; i<selectionsArray.length; i++) {
                record = this.fromMultiselect.view.store.getAt(selectionsArray[i]);
                records.push(record);
            }
            if(!this.allowDup)selectionsArray = [];
            for (var i=0; i<records.length; i++) {
                record = records[i];
                if(this.allowDup){
                    var x=new Ext.data.Record();
                    record.id=x.id;
                    delete x;
                    this.toMultiselect.view.store.add(record);
                }else{
                    this.fromMultiselect.view.store.remove(record);
                    this.toMultiselect.view.store.add(record);
                    selectionsArray.push((this.toMultiselect.view.store.getCount() - 1));
                }
            }
        }
        this.toMultiselect.view.refresh();
        this.fromMultiselect.view.refresh();
        var si = this.toMultiselect.store.sortInfo;
        if(si){
            this.toMultiselect.store.sort(si.field, si.direction);
        }
        this.toMultiselect.view.select(selectionsArray);
    },

    toFrom : function() {
        var selectionsArray = this.toMultiselect.view.getSelectedIndexes();
        var records = [];
        if (selectionsArray.length > 0) {
            for (var i=0; i<selectionsArray.length; i++) {
                record = this.toMultiselect.view.store.getAt(selectionsArray[i]);
                records.push(record);
            }
            selectionsArray = [];
            for (var i=0; i<records.length; i++) {
                record = records[i];
                this.toMultiselect.view.store.remove(record);
                if(!this.allowDup){
                    this.fromMultiselect.view.store.add(record);
                    selectionsArray.push((this.fromMultiselect.view.store.getCount() - 1));
                }
            }
        }
        this.fromMultiselect.view.refresh();
        this.toMultiselect.view.refresh();
        var si = this.fromMultiselect.store.sortInfo;
        if (si){
            this.fromMultiselect.store.sort(si.field, si.direction);
        }
        this.fromMultiselect.view.select(selectionsArray);
    },

    valueChanged: function(store) {
        var record = null;
        var values = [];
        for (var i=0; i<store.getCount(); i++) {
            record = store.getAt(i);
            values.push(record.get(this.toMultiselect.valueField));
        }
        this.hiddenField.dom.value = values.join(this.delimiter);
        this.fireEvent('change', this, this.getValue(), this.hiddenField.dom.value);
    },

    getValue : function() {
        return this.hiddenField.dom.value;
    },

    onRowDblClick : function(vw, index, node, e) {
        if (vw == this.toMultiselect.view){
            this.toFrom();
        } else if (vw == this.fromMultiselect.view) {
            this.fromTo();
        }
        return this.fireEvent('rowdblclick', vw, index, node, e);
    },

    reset: function(){
        range = this.toMultiselect.store.getRange();
        this.toMultiselect.store.removeAll();
        this.fromMultiselect.store.add(range);
        var si = this.fromMultiselect.store.sortInfo;
        if (si){
            this.fromMultiselect.store.sort(si.field, si.direction);
        }
        this.valueChanged(this.toMultiselect.store);
    }
});

Ext.reg('itemselector', Ext.ux.form.ItemSelector);

// backwards compat
Ext.ux.ItemSelector = Ext.ux.form.ItemSelector;

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.ux.grid.RowExpander.js"></script>
 */

/*
 * ! Ext JS Library 3.4.0 Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com http://www.sencha.com/license
 */
Ext.ns('Ext.ux.grid');

/**
 * @class Ext.ux.grid.RowExpander
 * @extends Ext.util.Observable Plugin (ptype = 'rowexpander') that adds the
 *          ability to have a Column in a grid which enables a second row body
 *          which expands/contracts. The expand/contract behavior is
 *          configurable to react on clicking of the column, double click of the
 *          row, and/or hitting enter while a row is selected.
 * 
 * @ptype rowexpander
 */
Ext.ux.grid.RowExpander = Ext.extend(Ext.util.Observable, {
    /**
	 * @cfg {Boolean} expandOnEnter <tt>true</tt> to toggle selected row(s)
	 *      between expanded/collapsed when the enter key is pressed (defaults
	 *      to <tt>true</tt>).
	 */
    expandOnEnter : true,
    /**
	 * @cfg {Boolean} expandOnDblClick <tt>true</tt> to toggle a row between
	 *      expanded/collapsed when double clicked (defaults to <tt>true</tt>).
	 */
    expandOnDblClick : true,

    header : '',
    width : 20,
    sortable : false,
    fixed : true,
    hideable: false,
    menuDisabled : true,
    dataIndex : '',
    id : 'expander',
    lazyRender : true,
    enableCaching : true,

    constructor: function(config){
        Ext.apply(this, config);

        this.addEvents({
            /**
			 * @event beforeexpand Fires before the row expands. Have the
			 *        listener return false to prevent the row from expanding.
			 * @param {Object}
			 *            this RowExpander object.
			 * @param {Object}
			 *            Ext.data.Record Record for the selected row.
			 * @param {Object}
			 *            body body element for the secondary row.
			 * @param {Number}
			 *            rowIndex The current row index.
			 */
            beforeexpand: true,
            /**
			 * @event expand Fires after the row expands.
			 * @param {Object}
			 *            this RowExpander object.
			 * @param {Object}
			 *            Ext.data.Record Record for the selected row.
			 * @param {Object}
			 *            body body element for the secondary row.
			 * @param {Number}
			 *            rowIndex The current row index.
			 */
            expand: true,
            /**
			 * @event beforecollapse Fires before the row collapses. Have the
			 *        listener return false to prevent the row from collapsing.
			 * @param {Object}
			 *            this RowExpander object.
			 * @param {Object}
			 *            Ext.data.Record Record for the selected row.
			 * @param {Object}
			 *            body body element for the secondary row.
			 * @param {Number}
			 *            rowIndex The current row index.
			 */
            beforecollapse: true,
            /**
			 * @event collapse Fires after the row collapses.
			 * @param {Object}
			 *            this RowExpander object.
			 * @param {Object}
			 *            Ext.data.Record Record for the selected row.
			 * @param {Object}
			 *            body body element for the secondary row.
			 * @param {Number}
			 *            rowIndex The current row index.
			 */
            collapse: true
        });

        Ext.ux.grid.RowExpander.superclass.constructor.call(this);

        if(this.tpl){
            if(typeof this.tpl == 'string'){
                this.tpl = new Ext.Template(this.tpl);
            }
            this.tpl.compile();
        }

        this.state = {};
        this.bodyContent = {};
    },

    getRowClass : function(record, rowIndex, p, ds){
        p.cols = p.cols-1;
        var content = this.bodyContent[record.id];
        if(!content && !this.lazyRender){
            content = this.getBodyContent(record, rowIndex);
        }
        if(content){
            p.body = content;
        }
        return this.state[record.id] ? 'x-grid3-row-expanded' : 'x-grid3-row-collapsed';
    },

    init : function(grid){
        this.grid = grid;

        var view = grid.getView();
        view.getRowClass = this.getRowClass.createDelegate(this);

        view.enableRowBody = true;


        grid.on('render', this.onRender, this);
        grid.on('destroy', this.onDestroy, this);
    },

    // @private
    onRender: function() {
        var grid = this.grid;
        var mainBody = grid.getView().mainBody;
        mainBody.on('mousedown', this.onMouseDown, this, {delegate: '.x-grid3-row-expander'});
        if (this.expandOnEnter) {
            this.keyNav = new Ext.KeyNav(this.grid.getGridEl(), {
                'enter' : this.onEnter,
                scope: this
            });
        }
        if (this.expandOnDblClick) {
            grid.on('rowdblclick', this.onRowDblClick, this);
        }
    },
    
    // @private
    onDestroy: function() {
        if(this.keyNav){
            this.keyNav.disable();
            delete this.keyNav;
        }
        /*
		 * A majority of the time, the plugin will be destroyed along with the
		 * grid, which means the mainBody won't be available. On the off chance
		 * that the plugin isn't destroyed with the grid, take care of removing
		 * the listener.
		 */
        var mainBody = this.grid.getView().mainBody;
        if(mainBody){
            mainBody.un('mousedown', this.onMouseDown, this);
        }
    },
    // @private
    onRowDblClick: function(grid, rowIdx, e) {
        this.toggleRow(rowIdx);
    },

    onEnter: function(e) {
        var g = this.grid;
        var sm = g.getSelectionModel();
        var sels = sm.getSelections();
        for (var i = 0, len = sels.length; i < len; i++) {
            var rowIdx = g.getStore().indexOf(sels[i]);
            this.toggleRow(rowIdx);
        }
    },

    getBodyContent : function(record, index){
        if(!this.enableCaching){
            return this.tpl.apply(record.data);
        }
        var content = this.bodyContent[record.id];
        if(!content){
            content = this.tpl.apply(record.data);
            this.bodyContent[record.id] = content;
        }
        return content;
    },

    onMouseDown : function(e, t){
        e.stopEvent();
        var row = e.getTarget('.x-grid3-row');
        this.toggleRow(row);
    },

    renderer : function(v, p, record){
        p.cellAttr = 'rowspan="2"';
        return '<div class="x-grid3-row-expander">&#160;</div>';
    },

    beforeExpand : function(record, body, rowIndex){
        if(this.fireEvent('beforeexpand', this, record, body, rowIndex) !== false){
            if(this.tpl && this.lazyRender){
                body.innerHTML = this.getBodyContent(record, rowIndex);
            }
            return true;
        }else{
            return false;
        }
    },

    toggleRow : function(row){
        if(typeof row == 'number'){
            row = this.grid.view.getRow(row);
        }
        this[Ext.fly(row).hasClass('x-grid3-row-collapsed') ? 'expandRow' : 'collapseRow'](row);
    },

    expandRow : function(row){
        if(typeof row == 'number'){
            row = this.grid.view.getRow(row);
        }
        var record = this.grid.store.getAt(row.rowIndex);
        var body = Ext.DomQuery.selectNode('tr:nth(2) div.x-grid3-row-body', row);
        if(this.beforeExpand(record, body, row.rowIndex)){
            this.state[record.id] = true;
            Ext.fly(row).replaceClass('x-grid3-row-collapsed', 'x-grid3-row-expanded');
            this.fireEvent('expand', this, record, body, row.rowIndex);
        }
    },

    collapseRow : function(row){
        if(typeof row == 'number'){
            row = this.grid.view.getRow(row);
        }
        var record = this.grid.store.getAt(row.rowIndex);
        var body = Ext.fly(row).child('tr:nth(1) div.x-grid3-row-body', true);
        if(this.fireEvent('beforecollapse', this, record, body, row.rowIndex) !== false){
            this.state[record.id] = false;
            Ext.fly(row).replaceClass('x-grid3-row-expanded', 'x-grid3-row-collapsed');
            this.fireEvent('collapse', this, record, body, row.rowIndex);
        }
    }
});

Ext.preg('rowexpander', Ext.ux.grid.RowExpander);

// backwards compat
Ext.grid.RowExpander = Ext.ux.grid.RowExpander;

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.ux.form.FileUploadField.js"></script>
 */

/*
 * ! Ext JS Library 3.4.0 Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com http://www.sencha.com/license
 */
Ext.ns('Ext.ux.form');

/**
 * @class Ext.ux.form.FileUploadField
 * @extends Ext.form.TextField Creates a file upload field.
 * @xtype fileuploadfield
 */
Ext.ux.form.FileUploadField = Ext.extend(Ext.form.TextField, {
			/**
			 * @cfg {String} buttonText The button text to display on the upload
			 *      button (defaults to 'Browse...'). Note that if you supply a
			 *      value for {@link #buttonCfg}, the buttonCfg.text value will
			 *      be used instead if available.
			 */
			buttonText : 'Browse...',
			/**
			 * @cfg {Boolean} buttonOnly True to display the file upload field
			 *      as a button with no visible text field (defaults to false).
			 *      If true, all inherited TextField members will still be
			 *      available.
			 */
			buttonOnly : false,
			/**
			 * @cfg {Number} buttonOffset The number of pixels of space reserved
			 *      between the button and the text field (defaults to 3). Note
			 *      that this only applies if {@link #buttonOnly} = false.
			 */
			buttonOffset : 3,
			/**
			 * @cfg {Object} buttonCfg A standard {@link Ext.Button} config
			 *      object.
			 */

			// private
			readOnly : true,

			/**
			 * @hide
			 * @method autoSize
			 */
			autoSize : Ext.emptyFn,

			// private
			initComponent : function() {
				Ext.ux.form.FileUploadField.superclass.initComponent.call(this);

				this.addEvents(
						/**
						 * @event fileselected Fires when the underlying file
						 *        input field's value has changed from the user
						 *        selecting a new file from the system file
						 *        selection dialog.
						 * @param {Ext.ux.form.FileUploadField}
						 *            this
						 * @param {String}
						 *            value The file value returned by the
						 *            underlying file input field
						 */
						'fileselected');
			},

			// private
			onRender : function(ct, position) {
				Ext.ux.form.FileUploadField.superclass.onRender.call(this, ct,
						position);

				this.wrap = this.el.wrap({
							cls : 'x-form-field-wrap x-form-file-wrap'
						});
				this.el.addClass('x-form-file-text');
				this.el.dom.removeAttribute('name');
				this.createFileInput();

				var btnCfg = Ext.applyIf(this.buttonCfg || {}, {
							text : this.buttonText
						});
				this.button = new Ext.Button(Ext.apply(btnCfg, {
							renderTo : this.wrap,
							cls : 'x-form-file-btn'
									+ (btnCfg.iconCls ? ' x-btn-icon' : '')
						}));

				if (this.buttonOnly) {
					this.el.hide();
					this.wrap.setWidth(this.button.getEl().getWidth());
				}

				this.bindListeners();
				this.resizeEl = this.positionEl = this.wrap;
			},

			bindListeners : function() {
				this.fileInput.on({
							scope : this,
							mouseenter : function() {
								this.button.addClass(['x-btn-over',
										'x-btn-focus'])
							},
							mouseleave : function() {
								this.button.removeClass(['x-btn-over',
										'x-btn-focus', 'x-btn-click'])
							},
							mousedown : function() {
								this.button.addClass('x-btn-click')
							},
							mouseup : function() {
								this.button.removeClass(['x-btn-over',
										'x-btn-focus', 'x-btn-click'])
							},
							change : function() {
								var v = this.fileInput.dom.value;
								this.setValue(v);
								this.fireEvent('fileselected', this, v);
							}
						});
			},

			createFileInput : function() {
				this.fileInput = this.wrap.createChild({
							id : this.getFileInputId(),
							name : this.name || this.getId(),
							cls : 'x-form-file',
							tag : 'input',
							type : 'file',
							size : 1
						});
			},

			reset : function() {
				if (this.rendered) {
					this.fileInput.remove();
					this.createFileInput();
					this.bindListeners();
				}
				Ext.ux.form.FileUploadField.superclass.reset.call(this);
			},

			// private
			getFileInputId : function() {
				return this.id + '-file';
			},

			// private
			onResize : function(w, h) {
				Ext.ux.form.FileUploadField.superclass.onResize
						.call(this, w, h);

				this.wrap.setWidth(w);

				if (!this.buttonOnly) {
					var w = this.wrap.getWidth()
							- this.button.getEl().getWidth()
							- this.buttonOffset;
					this.el.setWidth(w);
				}
			},

			// private
			onDestroy : function() {
				Ext.ux.form.FileUploadField.superclass.onDestroy.call(this);
				Ext.destroy(this.fileInput, this.button, this.wrap);
			},

			onDisable : function() {
				Ext.ux.form.FileUploadField.superclass.onDisable.call(this);
				this.doDisable(true);
			},

			onEnable : function() {
				Ext.ux.form.FileUploadField.superclass.onEnable.call(this);
				this.doDisable(false);

			},

			// private
			doDisable : function(disabled) {
				this.fileInput.dom.disabled = disabled;
				this.button.setDisabled(disabled);
			},

			// private
			preFocus : Ext.emptyFn,

			// private
			alignErrorIcon : function() {
				this.errorIcon.alignTo(this.wrap, 'tl-tr', [2, 0]);
			}

		});

Ext.reg('fileuploadfield', Ext.ux.form.FileUploadField);

// backwards compat
Ext.form.FileUploadField = Ext.ux.form.FileUploadField;

/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.ux.plugin.PagingToolbarResizer.js"></script>
 */

/**
 * PagingToolbarResizer plugin for Ext PagingToolbar
 * 
 * Contains a combobox where user can choose the pagesize dynamically
 * 
 * @author Loiane Groner <http://loianegroner.com> <http://loiane.com>
 * @date June 01, 2010
 * @version 1
 * 
 * @license Ext.ux.plugin.PagingToolbarResizer is licensed under the terms of
 *          the Open Source LGPL 3.0 license. Commercial use is permitted to the
 *          extent that the code/component(s) do NOT become part of another Open
 *          Source or Commercially licensed development library or toolkit
 *          without explicit permission.
 * 
 * License details: http://www.gnu.org/licenses/lgpl.html
 */
Ext.namespace('Ext.ux.plugin');

/**
 * @class Ext.ux.plugin.PagingToolbarResizer
 * @extends Ext.Component
 * 
 * Creates new PagingToolbarResizer plugin
 * @constructor
 * @param {Object}
 *            config The config object
 * 
 * How to use
 * 
 * 
 * Just instaciate a new PagingToolbarResizer inside PagingToolbar plugins
 * option:
 * 
 * bbar: new Ext.PagingToolbar({ pageSize: 25, store: store, displayInfo: true,
 * displayMsg: 'Displaying topics {0} - {1} of {2}', emptyMsg: "No topics to
 * display", plugins : [new Ext.ux.plugin.PagingToolbarResizer( {options : [ 50,
 * 100, 500 ] })] })
 */
Ext.ux.plugin.PagingToolbarResizer = Ext.extend(Object, {

  /**
	 * @cfg {Ext.data.Store} options The {@link Ext.data.Store} combobox should
	 *      use as its data source (required). You can also use an array of
	 *      integers. Defaults to [5, 10, 15, 20, 25, 30, 50, 75, 100, 200, 300,
	 *      500, 1000]
	 */	
  options: [5, 10, 15, 20, 25, 30, 50, 75, 100, 200, 300, 500, 1000],
  
  /**
	 * @cfg {String} mode Acceptable values are:
	 * 
	 * 
	 * 'remote' : Default
	 * 
	 * Automatically loads the {@link #store} the first time the trigger is
	 * clicked. If you do not want the store to be automatically loaded the
	 * first time the trigger is clicked, set to 'local' and manually load the
	 * store. To force a requery of the store every time the trigger is clicked
	 * see {@link #lastQuery}.
	 * 
	 * 'local' :
	 * 
	 * ComboBox loads local data
	 * 
	 * 
	 */
  mode: 'remote',
  
  /**
	 * @cfg {String} displayText The message to display before the combobox
	 *      (defaults to 'Records per Page')
	 */
  displayText: 'Records per Page',
  
  /**
	 * @cfg {Boolean} prependCombo true to insert the combobox before the paging
	 *      buttons. Defaults to false.
	 */
  prependCombo: false,

  constructor: function(config){
	
    Ext.apply(this, config);
    Ext.ux.plugin.PagingToolbarResizer.superclass.constructor.call(this, config);
  },

  init : function(pagingToolbar) {
	
	var comboStore = this.options;
	  
    var combo = new Ext.form.ComboBox({
      typeAhead: false,
      triggerAction: 'all',
      forceSelection: true,
      selectOnFocus:true,
      lazyRender:true,
      editable: false,
      mode: this.mode,
      value: pagingToolbar.pageSize,
      width: 39,
      store: comboStore,
      listeners: {
        select: function(combo, value, i){
          pagingToolbar.pageSize = comboStore[i];
          pagingToolbar.doLoad(Math.floor(pagingToolbar.cursor/pagingToolbar.pageSize)*pagingToolbar.pageSize);
        }
      }
    });

    var index = 0;
    
    if (this.prependCombo){
    	index = pagingToolbar.items.indexOf(pagingToolbar.first);
    	index--;
    } else{
    	index = pagingToolbar.items.indexOf(pagingToolbar.refresh);
        pagingToolbar.insert(++index,'-');
    }
    
    pagingToolbar.insert(++index, this.displayText);
    pagingToolbar.insert(++index, combo);
    
    if (this.prependCombo){
    	 pagingToolbar.insert(++index,'-');
    }
    
    // destroy combobox before destroying the paging toolbar
    pagingToolbar.on({
      beforedestroy: function(){
    	combo.destroy();
      }
    });

  }
});

/**
 * <script type="text/javascript" src="$!{env.imgUrl}/js/ext/ext-copy.js"></script>
 */

if (!Ext.grid.GridView.prototype.templates) {
	Ext.grid.GridView.prototype.templates = {};
}

Ext.grid.GridView.prototype.templates.cell = new Ext.Template(
		'<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>',
		'<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>',
		'</td>');
		
/**
 * <script type="text/javascript"
 * src="$!{env.imgUrl}/js/ext/Ext.date.Conversion.js"></script>
 */

Ext.override(Date, {
			toUTC : function() {
				// Convert the date to the UTC date
				return this.add(Date.MINUTE, -840);
			}
		});
		