$(document).ready(function() {

			var charts = echarts.init(document.getElementById('chart'), theme);

			charts.showLoading({
						text : '正在努力的读取数据中...'
					});

			$.ajax({
						type : "post",
						url : appUrl + "/trade/tradeAction!getTradeStats.htm",
						data : {
							dateTime : new Date().getTime()
						},
						success : function(data) {
							var dates = [];
							var countOfCreate = [];
							var countOfPay = [];
							var countOfSend = [];
							var countOfSign = [];
							var countOfCancel = [];

							for (var i = 0; i < data.length; i++) {
								dates.push(data[i].dates);
								countOfCreate.push(data[i].countOfCreate);
								countOfPay.push(data[i].countOfPay);
								countOfSend.push(data[i].countOfSend);
								countOfSign.push(data[i].countOfSign);
								countOfCancel.push(data[i].countOfCancel);
							}

							// ajax callback
							charts.hideLoading();

							option = {
								title : {
									text : '30天订单信息统计'
								},
								tooltip : {
									trigger : 'axis'
								},
								legend : {
									data : ['下订单', '已付款', '已发货', '标记签收', '已关闭']
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
											name : '下订单',
											type : 'line',
											data : countOfCreate,
											markPoint : {
												data : [{
															type : 'max',
															name : '最大值'
														}]
											}
										}, {
											name : '已付款',
											type : 'line',
											data : countOfPay,
											markPoint : {
												data : [{
															type : 'max',
															name : '最大值'
														}]
											}
										}, {
											name : '已发货',
											type : 'line',
											data : countOfSend,
											markPoint : {
												data : [{
															type : 'max',
															name : '最大值'
														}]
											}
										}, {
											name : '标记签收',
											type : 'line',
											data : countOfSign,
											markPoint : {
												data : [{
															type : 'max',
															name : '最大值'
														}]
											}
										}, {
											name : '已关闭',
											type : 'line',
											data : countOfCancel,
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