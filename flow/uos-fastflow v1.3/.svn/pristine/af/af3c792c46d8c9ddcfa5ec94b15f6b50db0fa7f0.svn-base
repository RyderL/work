var queueChart = [];
var time=10000;
var timer = -1;
var timeout = -1;
var countdownTimer = -1;
var hostName = null;

function initMyChart(ec){
	// 基于准备好的dom，初始化echarts图表
	var data = $.callSyn("QueueMonitorServ","qryFqAddrs",{});
	$('#hostIp').combobox({
		required:false,
		editable:false,
		panelHeight:'auto',
		valueField: 'label',
		textField: 'value',
		data: data
	});
	$('#hostIp').combobox('select', data[0].value);
	queueChart[1] = ec.init(document.getElementById('queueChart1'),'macarons');
}

function refreshData(){
		var addr = $('#hostIp').combobox('getValue');
		var addrArr = addr.split(':');
		var arg = {
				hostName: addrArr[0],
				port: addrArr[1],
				index: "1"
		};
		 
		 $.callAsyn("QueueMonitorServ","qryFQueueLength",arg,function(ret){
			
			var  data = JSON.parse(ret);
			var queueNames = [];
			var index = data.index;
			delete data.index;
			
			for (var name in data){
				queueNames.push(name);
			}
		
			var total = queueNames.length;
			
			//没有数据
			if (total < 1) {
				var msg = addrArr[0] + ' JMX无法连接，请确保'+addrArr[0]+'的jetty或进程使用的是fqueue模式';
				$("#queueChart"+Math.ceil(index/3)).html('<p class="msg" style="border:1px solid black;text-align:center;font-size:12pt;font-weight:bold">'+msg+'</p>');
				return;
			}
			//有数据
			else {
				var option=initOption(data,queueNames,addrArr[0]+'-'+addrArr[1]);
				queueChart[Math.ceil(index/3)].setOption(option);
			}
			
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
	var queueData=[];
	for (var i = 0; i < queueNames.length; i++) {
		queueData.push(data[queueNames[i]]);
	} 
	var option = {
		    title : {
		        text: title,
		        subtext: '  FQueue队列监控'
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
		              	rotate:-45,
		              	textStyle:{baseline:'middle'}
		            },
		            data : queueNames
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
