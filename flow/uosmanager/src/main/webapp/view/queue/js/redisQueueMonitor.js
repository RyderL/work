var publicQueueChart;
var busiQueueChart1;
var busiQueueChart2;
var infQueueChart;
var publicQueueNames="";
var busiQueueNames="";
var infQueueNames="";
var time=10000;
var timer = -1;
var timeout = -1;
var countdownTimer = -1;
var queueDisplayNames="";
var host = ['127.0.0.1:6379'/*,//业务一
            '134.96.177.24:16380',//公共
            '134.96.177.23:16387',//业务二
            '134.96.177.23:16388'*/];//接口

function initMyChart(ec){
	// 基于准备好的dom，初始化echarts图表
	publicQueueChart = ec.init(document.getElementById('publicQueueChart'),'macarons');
	/*busiQueueChart1 = ec.init(document.getElementById('busiQueueChart1'),'macarons');
	busiQueueChart2 = ec.init(document.getElementById('busiQueueChart2'),'macarons');
	infQueueChart = ec.init(document.getElementById('infQueueChart'),'macarons');*/
}

function getQueueHosts() {
	var publicData={
			host:'127.0.0.1',
			port:'6901'
	};
	$.callAsyn("QueueMonitorServ","qryQueueHost",publicData,function(ret){
		var data=JSON.parse(ret);
		var count=data['count'];
		if (count && count>0) {
			host = data;
		}
	});
}

function getQueueNames() {
	var publicData={
			host:'127.0.0.1',
			port:'6901'
	};
	$.callAsyn("QueueMonitorServ","getQueueName",publicData,function(ret){
		var data=JSON.parse(ret);
		publicQueueNames=data['publicQueueNames'];
		queueDisplayNames=data['queueDisplayNames'];
		/*busiQueueNames=data['busiQueueNames'];
		infQueueNames=data['infQueueNames'];*/
		refreshData();
	});
}


function refreshData(){
	var publicData={//公共队列
			queueNames:	publicQueueNames,
			address:	host[0],
			host:'127.0.0.1',
			port:'6901'
	};

	$.callAsyn("QueueMonitorServ","qryQueueLength",publicData,function(ret){
		var  data=JSON.parse(ret);
		if (data == null || data == undefined) {
			$("#publicQueueChart").html('<p class="msg" style="border:1px solid black;text-align:center;font-size:15pt;font-weight:bold">Redis队列异常</p>');
			return;
		}	
		var queueNames = [];
		for (var name in data){
			queueNames.push(name);
		}
		var option=initOption(data,queueNames,"公共");
		publicQueueChart.setOption(option);
	});
}


function countdown(){
	var second = document.getElementById("second").innerHTML;
	second--;
	if (second > 0) {
		document.getElementById("second").innerHTML=second;
	} 
	else{
		document.getElementById("second").innerHTML=time/1000;
	}
}


function autoRefreshData(){
	if (timer == -1){
		timer=setInterval(refreshData,time);
		countdownTimer = setInterval(countdown,1000);
		document.getElementById("countdown").innerHTML='<a>离下次刷新还有 </a><b id="second">'+time/1000+'</b><a> 秒</a>';
	}
	else {
		clearInterval(timer);
		clearInterval(countdownTimer);
		timer=setInterval(refreshData,time);
		countdownTimer = setInterval(countdown,1000);
		document.getElementById("countdown").innerHTML='<a>离下次刷新还有 </a><b id="second">'+time/1000+'</b><a> 秒</a>';
	}
	
	if (timeout == -1){
		timeout = setTimeout("cancelAutoRefreshData()", 900000 );
	}
	else{
		clearTimeout(timeout);
		timeout = setTimeout("cancelAutoRefreshData()", 900000 );
	}
	alert("成功设置每十秒自动刷新（持续15分钟）");
}


function cancelAutoRefreshData(){
	clearInterval(timer);
	clearInterval(countdownTimer);
	clearTimeout(timeout);
	timer = -1;
	countdownTimer = -1;
	timeout = -1;
	document.getElementById("countdown").innerHTML='';
	alert("已取消自动刷新");
}


function initOption(data,queueNames,title){ 
	var fields=queueNames;
	var queueData=[];
	for (var i = 0; i < fields.length; i++) {
		queueData.push(data[fields[i]]);
	} 
	var option = {
		    title : {
		        text: 'Redis队列监控',
		        subtext: '流程平台'
		    },
		    tooltip : {
		        trigger: 'axis'
		    },
		    legend: {
		        data:['队列长度']
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {show: true, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    xAxis : [
		        {
		            type : 'category',
		            axisLabel : {
		              	interval:0,
		              	textStyle:{baseline:'middle',align:'center'}
		            },
		            data :fields
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
		        {
		            name:'队列长度量',
		            type:'bar',
		            itemStyle: {
		                normal: {
		                    label: {
		                        show: true,
		                        position: 'top',
		                        formatter: '{c}'
		                    }
		                }
		            },
		            data:queueData 
		        } 
		    ]
		};
	return option;
} 
 
