function f1() {
	window.parent.addTab("系统操作日志", appUrl
					+ "/log/logAction!searchActionLog.htm");
}

function f2() {
	window.parent.addTab("系统错误日志", appUrl
					+ "/log/logMonitorAction!searchLogMonitor.htm");
}

function f3() {
	window.parent.addTab("系统接口监控", appUrl
					+ "/monitor/methodMonitorAction!searchMethodMonitor.htm");
}

function f4() {
	window.parent.addTab("系统缓存监控", appUrl
					+ "/monitor/cacheMonitorAction!searchCacheStats.htm");
}
