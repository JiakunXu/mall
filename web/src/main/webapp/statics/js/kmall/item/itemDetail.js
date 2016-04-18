$(document).ready(function() {
	var maxImageWidth = 10000, maxImageHeight = 10000;

	Dropzone.options.uploadDropzone = {
		url : appUrl + "/file/fileAction!saveFile.htm",
		paramName : "upload",
		maxFilesize : 1,
		addRemoveLinks : true,
		acceptedFiles : "image/*",
		dictDefaultMessage : '将文件拖拽至此区域进行上传（或点击此区域）',
		dictCancelUpload : "取消上传",
		dictRemoveFile : "取消选中图片",
		accept : function(file, done) {
			file.acceptDimensions = done;
			file.rejectDimensions = function() {
				done("Invalid dimension.");
			};
		},
		init : function() {
			this.on("success", function(file, responseText) {
						var obj = JSON.parse(responseText);
						file.id = obj.id;
						file.downloadPath = obj.downloadPath;
					});

			this.on("thumbnail", function(file) {
				if (file.width > maxImageWidth || file.height > maxImageHeight) {
					file.rejectDimensions()
				} else {
					file.acceptDimensions();
				}
			});
		}
	};

	// 商品所属类目
	$("#pItemCid").select2({
				placeholder : "商品所属类目",
				data : [{
							id : 1,
							text : '百货食品'
						}]
			}).select2("val", [1]);

	$("#itemCid").select2({
				placeholder : "商品所属类目",
				data : [{
							id : 2,
							text : '酒水'
						}]
			}).select2("val", [2]);

	var specCatData = [];
	var specItemData = [[], [], []];
	var flag = [true, true, true];

	$.ajax({
				type : "post",
				url : appUrl + "/spec/specAction!getSpecCatJsonList.htm",
				data : {
					dateTime : new Date().getTime()
				},
				success : function(data) {
					specCatData = data;
				},
				error : function() {

				}
			});

	for (var i = 1; i <= 3; i++) {
		$("#specCat" + i).select2({
			placeholder : "规格项目" + i,
			allowClear : true,
			query : function(query) {
				var data = {
					results : []
				};

				$.each(specCatData, function() {
							if (query.term.length == 0
									|| this.text.toUpperCase()
											.indexOf(query.term.toUpperCase()) >= 0) {
								var aaa = true;
								for (var t = 1; t <= 3; t++) {
									if ($("#specCat" + t).select2("val") == this.id) {
										aaa = false;
										break;
									}
								}

								if (aaa) {
									data.results.push({
												id : this.id,
												text : this.text
											});
								}
							}
						});

				query.callback(data);
			}
		});

		$("#specCat" + i).on("select2-selecting", function(e) {
					$("#specItem" + this.id.substr(7)).select2("enable", true);
				}).on("select2-removed", function(e) {
					$("#specItem" + this.id.substr(7)).select2("val", "");
					$("#specItem" + this.id.substr(7)).select2("enable", false);
				}).on("change", function(e) {
					specItemData[this.id.substr(7) - 1] = [];
					flag[this.id.substr(7) - 1] = true;
					// 重绘表格
					sku();
					// 设置价格库存 readonly
					setReadonly();
				});

		$("#specItem" + i).select2({
			itemId : i,
			placeholder : "规格",
			allowClear : true,
			multiple : true,
			query : function(query) {
				var data = {
					results : []
				};

				var f = false;

				// true need to ajax
				if (flag[this.itemId - 1]) {
					var iid = this.itemId;
					$.ajax({
						type : "post",
						url : appUrl
								+ "/spec/specAction!getSpecItemJsonList.htm",
						data : {
							specCId : $("#specCat" + iid).select2("val"),
							dateTime : new Date().getTime()
						},
						success : function(datas) {
							// 缓存
							specItemData[iid - 1] = datas;
							flag[iid - 1] = false;

							$.each(datas, function() {
										if (query.term.length == 0
												|| this.text.toUpperCase()
														.indexOf(query.term
																.toUpperCase()) >= 0) {
											data.results.push({
														id : this.id,
														text : this.text
													});
											f = true;
										}
									});

							if (!f && query.term.length != 0) {
								data.results.push({
											id : 0,
											text : query.term
										});
							}

							query.callback(data);
						},
						error : function() {

						}
					});
				} else {
					$.each(specItemData[this.itemId - 1], function() {
								if (query.term.length == 0
										|| this.text.toUpperCase()
												.indexOf(query.term
														.toUpperCase()) >= 0) {
									data.results.push({
												id : this.id,
												text : this.text
											});
									f = true;
								}
							});

					if (!f && query.term.length != 0) {
						data.results.push({
									id : -new Date().getTime(),
									text : query.term
								});
					}

					query.callback(data);
				}
			}
		});

		$("#specItem" + i).select2("enable", false).on("select2-selecting",
				function(e) {
					if (e.choice.id == 0) {
						// 1 获取 before select val
						var iid = this.id.substr(8);
						var tmp = [];
						tmp = $("#specItem" + iid).select2("data");

						$.ajax({
									type : "post",
									url : appUrl
											+ "/spec/specAction!createSpecItem.htm",
									data : {
										specCId : $("#specCat" + iid)
												.select2("val"),
										specItemValue : e.choice.text,
										dateTime : new Date().getTime()
									},
									success : function(data) {
										tmp.push({
													id : data,
													text : e.choice.text
												});
										$("#specItem" + iid).select2("data",
												tmp);
									},
									error : function(data) {
										$("#specItem" + iid).select2("data",
												tmp);

										new PNotify({
													title : '出错啦',
													text : data.responseJSON,
													type : 'error'
												});
									}
								});
					}
				}).on("change", function(e) {
					// 重绘表格
					sku();
					// 设置价格库存 readonly
					setReadonly();
				});
	}

	// 商品查询 动态赋值
	specCat = (specCat != '') ? jQuery.parseJSON(specCat) : '';
	specItem = (specItem != '') ? jQuery.parseJSON(specItem) : '';

	var i = 0;
	$.each(specCat, function() {
				$("#specCat" + (++i)).select2("data", {
							id : this.specCId,
							text : this.specCName
						});
			});

	i = 0;
	$.each(specCat, function() {
				var specCId = this.specCId;

				$.each(specItem, function() {
							if (specCId == this.specCId) {
								$("#specItem" + (i + 1))
										.select2("enable", true);

								var data = $("#specItem" + (i + 1))
										.select2("data");
								data.push({
											id : this.specItemId,
											text : this.specItemValue
										});
								$("#specItem" + (i + 1)).select2("data", data);
							}
						});

				i++;
			});

	for (var j = 0; j < i; j++) {
		$("#sku").rowspan(j);
	}

	$('#hideFrame').bind('load', promgtMsg);
});

function sku() {
	var rowCount = 0, cellCount = 0;
	var cell = [];
	var row = [];
	var item = [[], [], []];
	for (var i = 1; i <= 3; i++) {
		if ($("#specCat" + i).select2("data") != null
				&& $("#specItem" + i).select2("data").length > 0) {
			cell.push($("#specCat" + i).select2("data"));
			item[i - 1] = $("#specItem" + i).select2("data");

			cellCount++;
		}
	}

	if (cellCount == 0) {
		$("#skuT").empty();
		return;
	}

	cell.push({
				id : 'th-price',
				text : '价格（元）'
			}, {
				id : 'th-stock',
				text : '库存'
			}, {
				id : 'th-code',
				text : '商品编码'
			}, {
				id : 'th-origin',
				text : '市场价格'
			});

	var sp = "~!@#$%^&*()_+";

	for (var i = 0; i <= 2; i++) {
		if (row.length == 0) {
			row = item[i];
			continue;
		}

		var tmp = [];

		for (var j = 0; j < row.length; j++) {
			for (var m = 0; m < item[i].length; m++) {
				tmp.push({
							id : row[j].id + sp + item[i][m].id,
							text : row[j].text + sp + item[i][m].text
						});
			}
		}

		if (item[i].length > 0) {
			row = tmp;
		}
	}

	rowCount = row.length;

	$("#skuT").empty();

	var table = $("<table class=\"table table-bordered\" id=\"sku\">");
	table.appendTo($("#skuT"));

	var thead = $("<thead></thead>");
	thead.appendTo(table);

	var theadtr = $("<tr></tr>");
	theadtr.appendTo(thead);

	for (var i = 0; i < cell.length; i++) {
		var th;
		if (cell[i].id == 'th-price') {
			th = $("<th class='th-price'>" + cell[i].text + "</th>");
		} else if (cell[i].id == 'th-code') {
			th = $("<th class='th-code'>" + cell[i].text + "</th>");
		} else if (cell[i].id == 'th-stock') {
			th = $("<th class='th-stock'>" + cell[i].text + "</th>");
		} else if (cell[i].id == 'th-origin') {
			th = $("<th class='th-origin'>" + cell[i].text + "</th>");
		} else {
			th = $("<th>" + cell[i].text + "</th>");
		}

		th.appendTo(theadtr);
	}

	var tbody = $("<tbody></tbody>");
	tbody.appendTo(table);

	for (var i = 0; i < rowCount; i++) {
		var tbodytr = $("<tr></tr>");
		tbodytr.appendTo(tbody);

		// p1:v1;p2:v2
		var properties = "";
		// Pid1:vid1:pid_name1:vid_name1
		var propertiesName = "";

		for (var j = 0; j < cell.length; j++) {
			var text = row[i].text.split(sp)[j];
			var id = row[i].id.split(sp)[j];
			if (cell[j].id == 'th-code') {
				var td = $("<td><input type='text' class='form-control input-small' name='codes' ></input></td>");
			} else if (cell[j].id == 'th-price') {
				var td = $("<td><input type='text' class='form-control input-mini text-right' id='price"
						+ i
						+ "' name='price' onblur='onPriceBlur(this);' ></input></td>");
			} else if (cell[j].id == 'th-stock') {
				var td = $("<td><input type='text' class='form-control input-mini text-right' name='stock' onblur='onStockBlur(this);' ></input></td>");
			} else if (cell[j].id == 'th-origin') {
				var td = $("<td><input type='text' class='form-control input-mini text-right' name='origin' onblur='onOriginBlur(this);' ></input></td>");
			} else {
				var td = $("<td>" + text + "</td>");

				// 更新
				if (properties != "") {
					properties += ";";
				}

				properties += cell[j].id + ":" + id;

				if (propertiesName != "") {
					propertiesName += ";";
				}

				propertiesName += cell[j].text + ":" + text;
			}

			td.appendTo(tbodytr);
		}

		$("<input type='hidden' name='properties' value='" + properties
				+ "' ></input>").appendTo(tbodytr);
		$("<input type='hidden' name='propertiesName' value='" + propertiesName
				+ "' ></input>").appendTo(tbodytr);
	}

	for (var i = 0; i < cellCount; i++) {
		$("#sku").rowspan(i);
	}
}

function onPriceBlur(e) {
	var v = e.value;

	// 1. 验证输入
	if (!v) {
		warn("价格不能为空！");
		return;
	}

	var t = Number(v);
	if (isNaN(t)) {
		warn("价格字段，请输入数字！");
		return;
	}

	if (.01 > t) {
		warn("价格最低为 0.01！");
		return;
	}

	$(e).val(t.toFixed(2, 10));

	// 2. 统计金额范围
	var min = -1;
	$("input[name='price']").each(function() {
				var t = this.value;
				if (t != "") {
					if (min == -1) {
						min = t;
					}
					if (t < min) {
						min = t;
					}
				}
			})
	$('#price').val(Number(min).toFixed(2, 10));
}

function onOriginBlur(e) {
	var v = e.value;

	// 1. 验证输入
	if (v) {
		var t = Number(v);
		if (isNaN(t)) {
			warn("原始价格字段，请输入数字！");
			return;
		}

		$(e).val(t.toFixed(2, 10));
	}
}

function onStockBlur(e) {
	var v = e.value;

	// 1. 验证输入
	if (v) {
		var t = Number(v);
		if (isNaN(t)) {
			warn("库存字段，请输入数字！");
			return;
		}
	}

	// 2. 统计库存
	var total = 0;
	$("input[name='stock']").each(function() {
				total += Number($(this).val());
			})
	$('#stock').val(total);
}

function onPostageBlur(e) {
	var v = e.value;

	// 1. 验证输入
	if (!v) {
		warn("运费设置-统一邮费不能为空！");
		return;
	}

	var t = Number(v);
	if (isNaN(t)) {
		warn("运费设置-统一邮费，请输入数字！");
		return;
	}

	$('#postage').val(Number(v).toFixed(2, 10));
}

function setReadonly() {
	if ($('#sku').length >= 1) {
		$('#price').prop('readonly', true);
		$('#stock').prop('readonly', true);
	} else {
		$('#price').prop('readonly', false);
		$('#stock').prop('readonly', false);
	}

	$('#price').val('');
	$('#stock').val('');
}

function save(isDisplay) {
	$('#isDisplay').val(isDisplay);

	var form = window.document.forms[0];
	form.action = appUrl + "/item/itemAction!createItem.htm";
	form.target = "hideFrame";
	form.submit();

	Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
}

function update(isDisplay) {
	$('#isDisplay').val(isDisplay);

	var form = window.document.forms[0];
	form.action = appUrl + "/item/itemAction!updateItem.htm";
	form.target = "hideFrame";
	form.submit();

	Ext.Msg.wait('正在处理数据, 请稍候...', '提示');
}

/**
 * 每次打开图片上传页面，清空已上传图片
 */
function removeSelectedPic() {
	uploadDropzone.dropzone.removeAllFiles();
}

function select(files) {
	for (var i = 0; i < files.length; i++) {
		var file = files[i];

		if (file.status == 'error') {
			continue;
		}

		$('#uploadPic')
				.before('<div class="col-md-3 picture" id="fileId'
						+ file.id
						+ '"><input type="hidden" name="fileId" value="'
						+ file.id
						+ '" ></input><a href="javascript:removePic(\'fileId'
						+ file.id
						+ '\');"><span class="glyphicon glyphicon-remove-sign pull-right" style="margin-bottom: -5px;"></span></a><a href="'
						+ file.downloadPath
						+ '" target="_blank">'
						+ '<img class="img-thumbnail" style="height: 60px;" src="'
						+ file.downloadPath + '"></img></a></div>');
	}

	$('#uploadModal').modal('toggle');
	removeSelectedPic();
}

/**
 * 选中图片
 */
function selectPic() {
	var files = uploadDropzone.dropzone.getAcceptedFiles();
	select(files);
}

/**
 * 删除图片
 */
function removePic(id) {
	$("#" + id).remove();
}

function previous() {
	var start = $('#p').val();
	if (start == '1') {
		return;
	}

	getFileJsonList(Number(start) - 1);
}

function next() {
	var start = $('#p').val();
	var total = $('#total').val();

	if (start == total) {
		return;
	}

	getFileJsonList(Number(start) + 1);
}

/**
 * 获取已上传照片
 */
function getFileJsonList(start) {
	$.ajax({
		type : "post",
		url : appUrl + "/item/itemAction!getFileJsonList.htm",
		data : {
			p : start,
			dateTime : new Date().getTime()
		},
		success : function(data) {
			$('#pic4select').html('');

			if (data.length == 0) {
				setPageNo(0, 0);
				return;
			}

			for (var i = 0; i < data.length; i++) {
				var file = data[i];

				if (i == 0) {
					setPageNo(start, file.code);
				}

				$('#pic4select')
						.append('<div class="col-md-3 picture">'
								+ '<label class="checkbox-inline">'
								+ '<input style="margin-top: 35px;" type="checkbox" name="picId" value="'
								+ file.fileId
								+ '"></input>'
								+ '<a href="'
								+ file.filePath
								+ '" target="_blank">'
								+ ' <img class="img-thumbnail" style="height: 80px;" src="'
								+ file.filePath + '"></img>' + '</a>'
								+ '</label>' + '</div>');
			}
		},
		error : function(data) {
		}
	});
}

function setPageNo(p, total) {
	$('#pp').text(p);
	$('#tt').text(total);
	$('#p').val(p);
	$('#total').val(total);

	if (p == 0 || p == 1) {
		$('#previous').attr("class", "disabled");
	} else {
		$('#previous').removeClass("disabled");
	}

	if (p == total) {
		$('#next').attr("class", "disabled");
	} else {
		$('#next').removeClass("disabled");
	}
}

/**
 * 选择已上传图片.
 */
function selectedPic() {
	var files = new Array();

	var sp = "~!@#$%^&*()_+";
	var i = 0;
	$("input[type='checkbox'][name='picId']:checked").each(function() {
				var file = {};
				var t = $(this).val().split(sp);
				file.id = t[0];
				file.downloadPath = t[1];

				files[i++] = file;
			});

	select(files);
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
						}
					},
					icon : Ext.Msg.INFO
				});
	}
}

function warn(text) {
	new PNotify({
				title : '警告',
				text : text
			});
}

jQuery.fn.rowspan = function(colIdx) {
	return this.each(function() {
				var that;
				$('tr', this).each(function(row) {
					$('td:eq(' + colIdx + ')', this).filter(':visible').each(
							function(col) {
								if (that != null
										&& $(this).html() == $(that).html()) {
									rowspan = $(that).attr("rowSpan");
									if (rowspan == undefined) {
										$(that).attr("rowSpan", 1);
										rowspan = $(that).attr("rowSpan");
									}
									rowspan = Number(rowspan) + 1;
									$(that).attr("rowSpan", rowspan);
									$(this).hide();
								} else {
									that = this;
								}
							});
				});
			});
}