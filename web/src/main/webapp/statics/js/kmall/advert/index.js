$(document).ready(function() {
	// main.js
	for (var i = 1; i <= size; i++) {
		new ZeroClipboard($('#copy-button' + i)).on("ready", function(
						readyEvent) {
					this.on("aftercopy", function(event) {
								new PNotify({
											text : '复制成功！',
											type : 'success'
										});
							});
				});
	}

	charts = echarts.init(document.getElementById('chart'), theme);

	getAdvertStats('', '广告');
});

function getAdvertStats(advertId, advertName) {
	charts.showLoading({
				text : '正在努力的读取数据中...'
			});

	$.ajax({
				type : "post",
				url : appUrl + "/advert/advertAction!getAdvertStats.htm",
				data : {
					advertId : advertId,
					dateTime : new Date().getTime()
				},
				success : function(data) {
					var dates = [];
					var pv = [];
					var uv = [];
					var points = [];

					for (var i = 0; i < data.length; i++) {
						dates.push(data[i].dates);
						pv.push(data[i].pv);
						uv.push(data[i].uv);
						points.push(data[i].points);
					}

					charts.hideLoading();

					option = {
						title : {
							text : '30天 ' + advertName + ' 投放统计'
						},
						tooltip : {
							trigger : 'axis'
						},
						legend : {
							data : ['PV', 'UV', '赠送积分']
						},
						toolbox : {
							show : true,
							orient : 'vertical',
							y : 'center',
							feature : {
								mark : {
									show : true
								},
								dataZoom : {
									show : true
								},
								dataView : {
									show : true
								},
								magicType : {
									show : true,
									type : ['line', 'bar', 'stack', 'tiled']
								},
								restore : {
									show : true
								},
								saveAsImage : {
									show : true
								}
							}
						},
						calculable : true,
						dataZoom : {
							show : true,
							realtime : true,
							start : 50,
							end : 100
						},
						xAxis : [{
									type : 'category',
									boundaryGap : false,
									data : dates
								}],
						yAxis : [{
									type : 'value',
									name : '点击次数',
									axisLabel : {
										formatter : '{value}'
									}
								}, {
									type : 'value',
									name : '赠送积分',
									axisLabel : {
										formatter : '{value}'
									}
								}],
						series : [{
									name : 'PV',
									type : 'bar',
									data : pv,
									markPoint : {
										data : [{
													type : 'max',
													name : '最大值'
												}]
									}
								}, {
									name : 'UV',
									type : 'bar',
									data : uv,
									markPoint : {
										data : [{
													type : 'max',
													name : '最大值'
												}]
									}
								}, {
									name : '赠送积分',
									type : 'line',
									yAxisIndex : 1,
									data : points,
									markPoint : {
										data : [{
													type : 'max',
													name : '最大值'
												}]
									}
								}]
					};

					charts.setOption(option);
				},
				error : function() {

				}
			});
}

function exportAdvertPointsData() {
	var form = window.document.forms[0];
	form.action = appUrl
			+ "/advert/advertPointsAction!exportAdvertPointsData.htm";
	form.target = "_blank";
	form.submit();
}
