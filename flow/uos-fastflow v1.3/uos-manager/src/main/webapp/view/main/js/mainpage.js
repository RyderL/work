var obj = $.util.formatObj({"username":null});
		$('#flowInstTable').datagrid({
			autoRowHeight:false,//是否设置基于该行内容的行高度
		    pagination:false,//
		    columns:[[
				{title:'',field:'状态统计',width:80,
					formatter:function(value,row,index){
						var result= '状态统计';
						return result;
					}
				},
				{title:'初始化',field:'初始化',width:100,align:'center',
					formatter:function(value,row,index){
						var result= value;
						if(typeof(value)=="undefined" || value == null){
								result = "0";
						}
						return result;
					}
				},
				{title:'挂起',field:'挂起',width:100,align:'center',
					formatter:function(value,row,index){
						var result= value;
						if(typeof(value)=="undefined" || value == null){
								result = "0";
						}
						return result;
					}
				},
				{title:'已完成',field:'已完成',width:100,align:'center',
					formatter:function(value,row,index){
						var result= value;
						if(typeof(value)=="undefined" || value == null){
								result = "0";
						}
						return result;
					}
				},
				{title:'调度异常',field:'调度异常',width:100,align:'center',
					formatter:function(value,row,index){
						var result= value;
						if(typeof(value)=="undefined" || value == null){
								result = "0";
						}
						return result;
					}
				},
				{title:'流程回退中',field:'流程回退中',width:100,align:'center',
					formatter:function(value,row,index){
						var result= value;
						if(typeof(value)=="undefined" || value == null){
								result = "0";
						}
						return result;
					}
				}
		    ]]
		});
		var flowData = $.callSyn('DataCountQryServ','qryProcInstState4Grid',obj);
		$('#flowInstTable').datagrid('loadData',flowData);
		var flowXData = ['初始化','已完成','调度异常','流程回退中','挂起'];
		var states = ['0','5','7','9','1'];
		obj = $.util.formatObj({"states":states});
		var ret = $.callSyn('DataCountQryServ','qryProcInstState',obj);
		var flowChart = echarts.init(document.getElementById("proInstPie"));
		flowChart.setOption({
			 /*   tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },*/
				color:['#6596D5','#2C8FE9','#E4BD07','#D78505','#D9477B',
                       '#98E701','#E70137','#05AECC','#A74CC1','#99CC99'],
				legend:{
					data : ['初始化','已完成','调度异常','流程回退中','挂起']
				},
			    xAxis : [
		             {
		                 type : 'category',
		                 name : '状态',
		                 data : flowXData,
		                 axisTick: {
		                     alignWithLabel: true
		                 }
		             }
		         ],
		         yAxis : [
		             {
		                 type : 'value',
		                 name : '数量'
		             }
		         ],
			    series : [
			        {
			            name: '初始化',
			            type: 'bar',
			            data:[ret[0],0,0,0,0],
//			            data : ret,
			           /* itemStyle : {
			            	normal : {
			            		color : function(params) {
		                            // build a color map as your need.
		                            var colorList = [
		                              '#6596D5','#2C8FE9','#E4BD07','#D78505','#D9477B',
		                               '#98E701','#E70137','#05AECC','#A74CC1','#99CC99'];
		                            return colorList[params.dataIndex]
		                        }
			            	}
			            }*/
			        },
			        {
			            name: '已完成',
			            type: 'bar',
			            data:[0,ret[1],0,0,0]
			        },
			        {
			            name: '调度异常',
			            type: 'bar',
			            data:[0,0,ret[2],0,0]
			        },
			        {
			            name: '流程回退中',
			            type: 'bar',
			            data:[0,0,0,ret[3],0]
			        },
			        {
			            name: '挂起',
			            type: 'bar',
			            data:[0,0,0,0,ret[4]]
			        }
			    ]
			});
		
		//工作项
		$('#workItemTable').datagrid({
			autoRowHeight:false,//是否设置基于该行内容的行高度
		    pagination:false,//
		    columns:[[
				{title:'',field:'状态统计',width:80,align:'center',
					formatter:function(value,row,index){
						var result= '状态统计';
						return result;
					}
				},
				{title:'挂起',field:'挂起',width:100,align:'center',
					formatter:function(value,row,index){
						var result= value;
						if(typeof(value)=="undefined" || value == null){
								result = "0";
						}
						return result;
					}
				},
				{title:'作废',field:'作废',width:100,align:'center',
					formatter:function(value,row,index){
						var result= value;
						if(typeof(value)=="undefined" || value == null){
								result = "0";
						}
						return result;
					}
				},
				{title:'终止',field:'终止',width:100,align:'center',
					formatter:function(value,row,index){
						var result= value;
						if(typeof(value)=="undefined" || value == null){
								result = "0";
						}
						return result;
					}
				},
				{title:'已完成',field:'已完成',width:100,align:'center',
					formatter:function(value,row,index){
						var result= value;
						if(typeof(value)=="undefined" || value == null){
								result = "0";
						}
						return result;
					}
				},
				{title:'禁用',field:'禁用',width:100,align:'center',
					formatter:function(value,row,index){
						var result= value;
						if(typeof(value)=="undefined" || value == null){
								result = "0";
						}
						return result;
					}
				}
		    ]]
		});
		var workData = $.callSyn('DataCountQryServ','qryWorkItemState4Grid',obj);
		$('#workItemTable').datagrid('loadData',workData);
		
		var workItemXData = ['挂起','作废','终止','已完成','禁用'];
		var workItemStates = ['0','2','3','4','6'];
		obj = $.util.formatObj({"states":workItemStates});	
		var workRet = $.callSyn('DataCountQryServ','qryWorkItemState',obj);
		var workChart = echarts.init(document.getElementById("workItemPie"));
		workChart.setOption({
			  /*  tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },*/
				color:['#6596D5','#2C8FE9','#E4BD07','#D78505','#D9477B',
	                   '#98E701','#E70137','#05AECC','#A74CC1','#99CC99'],
				legend:{
					data : ['挂起','作废','终止','已完成','禁用']
				},
			    xAxis : [
			             {
			                 type : 'category',
			                 name : '状态',
			                 data : workItemXData,
			                 axisTick: {
			                     alignWithLabel: true
			                 }
			             }
			         ],
			         yAxis : [
			             {
			                 name : '数量',
			                 type : 'value'
			             }
			         ],
			    series : [
			        {
			            name: '挂起',
			            type: 'bar',
			            data: [workRet[0],0,0,0,0]
			        },
			        {
			            name: '作废',
			            type: 'bar',
			            data: [0,workRet[1],0,0,0]
			        },
			        {
			            name: '终止',
			            type: 'bar',
			            data: [0,0,workRet[2],0,0]
			        },
			        {
			            name: '已完成',
			            type: 'bar',
			            data: [0,0,0,workRet[3],0]
			        },
			        {
			            name: '禁用',
			            type: 'bar',
			            data: [0,0,0,0,workRet[4]]
			        }
			    ]
			});
			
		var defineRet = $.callSyn('DataCountQryServ','qryProcDefineUseCount',obj);
		var nameArr = defineRet.name;
		var retData = defineRet.count;
		var columns = [];
		var procDefSeries = [];
		columns[0] = {title:'',field:'数量统计',width:80,
				formatter:function(value,row,index){
					var result= '数量统计';
					return result;
				}
			};
		for(var i=0;i<nameArr.length;i++){
			var name = nameArr[i];
			if(name.length > 6){
				name = name.substring(0,5) + '</br>' + name.substring(6,name.length);
			}
			columns[i+1] = {title:name,field:nameArr[i],width:100,align:'center',
					formatter:function(value,row,index){
						var result= value;
						if(typeof(value)=="undefined" || value == null){
								result = "0";
						}
						return result;
					}
				};
			var data = [0,0,0,0,0,0];
			data[i] = retData[i];
			procDefSeries[i] = {
		        	name: nameArr[i],
		            type: 'bar',
		            data: data
		        };
		}
		$('#procDefTable').datagrid({
			autoRowHeight:false,//是否设置基于该行内容的行高度
		    pagination:false,//
		    nowrap :false,
		    fitColumns:false,
		    columns:[columns]
		});
		var procDefData = $.callSyn('DataCountQryServ','proDefinePie4Grid',obj);
		$('#procDefTable').datagrid('loadData',procDefData);
		var defineChart = echarts.init(document.getElementById("proDefinePie"));
		defineChart.setOption({
			   /* tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },*/
				color:['#6596D5','#2C8FE9','#E4BD07','#D78505','#D9477B',
	                   '#98E701','#E70137','#05AECC','#A74CC1','#99CC99'],
				legend:{
					data : nameArr
				},
			    xAxis : [
			             {
			            	 name : '流程模板',
			                 type : 'category',
			                 data : nameArr,
			                 axisLabel :{  
		                	     interval:0 ,
		                	     formatter:function(val){
		                	    	 if(val){
		                	    		 var ret;
			                	    	 if(val.length>6){
			                	 				ret = val.substr(0,5) +"\n" + val.substr(5,val.length);
			                	 		 }else{
			                	 			 ret = val;
			                	 		 }
		                	    	     return ret;
		                	    	 }
		                	    	return val;
	                	    	 }
		                	 }  
			             }
			         ],
			         yAxis : [
			             {
			            	 name : '数量',
			                 type : 'value'
			             }
			         ],
			    series : procDefSeries
			});
			
//			var data = $.callSyn('DataCountQryServ','qryExceptionFlow',obj);
//			$('#exceptionTl').datagrid({
//			rownumbers:true,//显示带有行号的列
//			singleSelect:true,//则只允许选中一行
//			autoRowHeight:false,//是否设置基于该行内容的行高度
//		    striped:true,//奇偶行使用不同背景色
//		    data:data,
//		    columns:[[
//				{title:'流程实例标识',field:'processInstanceId',width:120,sortable:true},
//				{title:'流程实例名称',field:'name',width:200},
//				{title:'流程模板定义标识',field:'processDefineId',width:120,hidden:true},
//				{title:'流程模板定义名称',field:'processDefinitionName',width:200},
//				{title:'状态',field:'state',width:80,
//					formatter:function(value,row,index){
//						var result="";
//						if(typeof(value)!="undefined"){
//							switch(value){//._state
//								case 7:
//									result="调度异常";
//									break;
//								default:
//									result = value;
//							}
//						}
//						return result;
//					}},
//				{title:'父活动标识',field:'parentActivityInstanceId',width:120},
//				{title:'创建日期',field:'createdDate',width:150,sortable:true},
//				{title:'开始日期',field:'startedDate',width:150},
//				{title:'方向',field:'direction',width:80},
//				{title:'旧流程实例标识',field:'oldProcessInstanceId',width:120},
//				{title:'标记',field:'sign',width:80,
//					formatter:function(value,row,index){
//						if(value=="1"){
//							return "撤单流程";
//						}else{//0
//							return "正常流程";
//						}
//					}},
//				{title:'区域',field:'areaId',width:100}
//		    ]],
//		    onClickRow: function(index,row){
//		    	var tab = $('#centerTab');
//				var content = '<iframe name="flowerror" id="flowerror" scrolling="no" frameborder="0"  src="../flow/flowerror/flowErrorManager.html" style="width:100%;height:99%;"></iframe>';
//				tab.tabs('add',{
//					title:"流程异常管理",
//					closable:true,
//					content:content
//				});
////				window.flowerror.document.getElementById('processInstId').value = row.processInstanceId;
////				window.flowerror.window.funQry();
//		    }
//		});