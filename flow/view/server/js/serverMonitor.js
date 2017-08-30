$(function($) {
	var session = $.session();

	$('#serverThreadTable').datagrid({
		singleSelect : false,// 则只允许选中一行
		autoRowHeight : false,
		fitColumns : true,
		pagination : false,
		
		private String host;
		private String port;
		private String type;
		private String appName;
		
		columns : [ [ {
			field : 'ck',
			checkbox : true,
		}, {
			title : '名称',
			field : 'appName',
			width : 100
		}, {
			title : 'IP地址',
			field : 'host',
			width : 80
		}, {
			title : 'JMX端口',
			field : 'port',
			width : 80
		}, {
			title : '类型',
			field : 'type',
			width : 80
		}] ]
	});

	var threadInfo = $.callSyn('ServerServ', 'getServerThreadInfo', {
		systemCode : session["systemCode"]
	});
	$('#serverThreadTable').datagrid('loadData', threadInfo).datagrid(
			"checkAll");

	var validate = function() {
		var row = $('#serverThreadTable').datagrid('getChecked');
		if (!row || row.length == 0) {
			alert("你没有选择需要操作的线程server！");
			return false;
		}
		return true;
	}

	$("#btnClearStatics").click(function(evt) {
		if (validate()) {
			var rows = $('#serverThreadTable').datagrid('getChecked');
			$.callSyn('ServerServ', 'clearStatics', {
				systemCode : session["systemCode"],
				rows : rows
			});
			$.messager.alert("提示", "清理静态缓存成功！！", "info");
		}
	});

	$("#btnViewCounter").click(
			function(evt) {
				if (validate()) {
					var rows = $('#serverThreadTable').datagrid('getChecked');
					var counterInfo = $.callSyn('ServerServ', 'getCounterInfo',
							{
								systemCode : session["systemCode"],
								rows : rows
							});
					// 基于准备好的dom，初始化echarts实例
					var myChart = echarts.init(document
							.getElementById('counterChart'));

					var codeArr = [];
					var valueArr = [];
					for ( var key in counterInfo) {
						if (key == "createAndStartProcessInstance") {
							codeArr.push("createProcessInstance");
						}else{
							codeArr.push(key);
						}
						valueArr.push(counterInfo[key]);
					}
					// 指定图表的配置项和数据
					var option = {
						title : {
							text : '线程执行命令统计'
						},
						tooltip : {
							trigger : 'axis'
						},
						legend : {
							data : [ '执行量' ]
						},
						xAxis : [ {
							type : 'category',
							data : codeArr
						} ],
						yAxis : [ {
							type : 'value'
						} ],
						series : [ {
							name : '执行量',
							type : 'bar',
							data : valueArr
						} ]
					};
					// 使用刚指定的配置项和数据显示图表。
					myChart.setOption(option);
				}
			});

	$("#btnClearCounter").click(function(evt) {
		if (validate()) {
			var rows = $('#serverThreadTable').datagrid('getChecked');
			$.callSyn('ServerServ', 'clearCounterInfo', {
				systemCode : session["systemCode"],
				rows : rows
			});
			$.messager.alert("提示", "清理统计信息成功！！", "info");
		}
	});
});
