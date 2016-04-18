$(document).ready(function() {

			var charts = echarts.init(document.getElementById('chart'), theme);

			charts.showLoading({
						text : '正在努力的读取数据中...'
					});

			$.ajax({
						type : "post",
						url : appUrl
								+ "/member/memberAction!getMemberStats.htm",
						data : {
							dateTime : new Date().getTime()
						},
						success : function(data) {
							var dates = [];
							var count = [];

							for (var i = 0; i < data.length; i++) {
								dates.push(data[i].dates);
								count.push(data[i].count);
							}

							// ajax callback
							charts.hideLoading();

							option = {
								title : {
									text : '30天会员信息统计'
								},
								tooltip : {
									trigger : 'axis'
								},
								legend : {
									data : ['新增会员人数']
								},
								toolbox : {
									show : true,
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
											type : ['line', 'bar', 'stack',
													'tiled']
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
											type : 'value'
										}],
								series : [{
											name : '新增会员人数',
											type : 'line',
											data : count,
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

		});