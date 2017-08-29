$(function($) {
	/** 全局变量*/
	var page=1;
	var pageSize=20;
	var session = $.session();
	$('body').layout('collapse','south');//初始化折叠流程图页面
	
	$('#processInstId').textbox({
		prompt: '流程实例ID',
		icons:[{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).textbox('clear').textbox('textbox').focus();
			}
		}]
	});
	var date = new Date();
	var preDate = new Date(date.getTime()- 24*60*60*1000);
	var startDateStr = preDate.getFullYear()+"-"+(preDate.getMonth()+1)+"-"+preDate.getDate()+" 00:00:00";
	$('#startDate').datetimebox({
		value: startDateStr,
	    showSeconds: false
	});
	var dateStr = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" 23:59:59";
	$('#endDate').datetimebox({
	    value: dateStr,
	    showSeconds: false
	});
	var pStateDatas =  [{
		label: '',
		value: '全部'
	},{
		label: '0',
		value: '未启动'
	},{
		label: '1',
		value: '挂起'
	},{
		label: '2',
		value: '正常执行中'
	},{
		label: '3',
		value: '作废'
	},{
		label: '4',
		value: '终止'
	},{
		label: '5',
		value: '已完成'
	},{
		label: '7',
		value: '调度异常'
	},{
		label: '8',
		value: '归零，流程回退到开始节点'
	},{
		label: '9',
		value: '流程回退中'
	}];
	$('#pState').combobox({
		editable:false,
		panelHeight:'auto',
		valueField: 'label',
		textField: 'value',
		data:pStateDatas
	});
	$("#pState").combobox('select','');
	
	$('#flowInstTable').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
	    //fitColumns:true,
		sortName:'createdDate',
		sortOrder: 'desc',
	    striped:true,//奇偶行使用不同背景色
	    pagination:true,//
		pageSize:pageSize,//初始化页面尺寸	
	    toolbar: '#flowInstTb',
	    columns:[[
			{title:'流程实例标识',field:'processInstanceId',width:120,sortable:true},
			{title:'流程实例名称',field:'name',width:120},
			{title:'流程定义标识',field:'processDefineId',width:120,hidden:true},
			{title:'流程定义编码',field:'processDefineCode',width:120},
			{title:'流程定义名称',field:'processDefinitionName',width:120},
			{title:'状态',field:'state',width:100,
				formatter:function(value,row,index){
					var result="";
					if(typeof(value)!="undefined"){
						switch(value){//._state
							case 0:
								result="未启动";
								break;
							case 1:
								result="挂起";
								break;
							case 2:
								result="正常执行中";
								break;
							case 3:
								result="作废";
								break;
							case 4:
								result="终止";
								break;
							case 5:
								result="已完成";
								break;
							case 7:
								result="调度异常";
								break;
							case 8:
								result="归零，流程回退到开始节点";
								break;
							case 9:
								result="流程回退中";
								break;
							default:
								result = value;
						}
					}
					return result;
				}},
			{title:'创建日期',field:'createdDate',width:150,sortable:true},
			{title:'开始日期',field:'startedDate',width:150},
			{title:'完成日期',field:'completedDate',width:150},
			{title:'标记',field:'sign',width:80,
				formatter:function(value,row,index){
					if(value=="1"){
						return "撤单流程";
					}else{//0
						return "正常流程";
					}
				}},
			{title:'区域',field:'areaName',width:100},
			{title:'区域',field:'areaId',hidden:true},
			{title:'有效日期',field:'dueDate',width:150},
			{title:'优先级',field:'priority',width:80},
			{title:'方向',field:'direction',width:80},
			{title:'流程启动者标识',field:'participantId',width:100},
			{title:'流程启动者职位',field:'participantPositionId',width:100},
			{title:'旧流程实例标识',field:'oldProcessInstanceId',width:100},
			{title:'同步等待活动实例数',field:'synCount',width:80},
			{title:'是否处于同步等待中',field:'isSyn',width:80,
				formatter:function(value,row,index){
					if(value=="1"){
						return "是";
					}else{//0
						return "否";
					}
				}},
			{title:'父活动标识',field:'parentActivityInstanceId',width:120}
	    ]],
	    rowStyler: function(index,row){
			if (row.state=='10P'){//失效的记录
				return 'color:#99CC99;'; 
			}
		},
		onClickRow: function(index,row){//激活按钮+加载工作项
			$('#btnQryGraph').linkbutton('enable');
			$('#btnQryShadowGraph').linkbutton('enable');
			$('#btnQryProcInstDetail').linkbutton('enable');
			if(row.parentActivityInstanceId){//存在父流程
				$('#btnQryParentGraph').linkbutton('enable');
			}else{
				$('#btnQryParentGraph').linkbutton('disable');
			}
			funQryWorkItem(row.processInstanceId);
		},
		onDblClickRow: function(index,row){//展开工作项
			if($('body').layout('panel','south').css('display')!="block"){//没有展开，即none
				$('body').layout('expand','south');
			}
		},
		onSortColumn:function(sort,order){
			funQry();
		}
	});
	//分页处理
	var pager=$('#flowInstTable').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		total:0,//初始化展示分页数据
		onSelectPage:function(pageNumber, pageSize){
			funQry(pageNumber);
			pager.pagination('refresh',{
				pageNumber:pageNumber,
				pageSize:pageSize
			});
		}
	});
	/**工作项区域*/
	$('#workItemTable').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
	    striped:true,//奇偶行使用不同背景色
	    remoteSort:false,//是否从服务器排序数据，否的时候才能对已有数据生效
	    columns:[[
			{title:'工作项标识',field:'workItemId',width:120,sortable:true},
			{title:'环节名称',field:'name',width:120},
			{title:'状态',field:'state',width:80,
				formatter:function(value,row,index){
					var result="";
					if(typeof(value)!="undefined"){
						switch(value){//._state
							case 0:
								result="作废";
								break;
							case 1:
								result="正常执行中";
								break;
							case 2:
								result="作废";
								break;
							case 3:
								result="终止（工单作废）";
								break;
							case 4:
								result="已完成";
								break;
							case 6:
								result="作废";
								break;
							default:
								result = value;
						}
					}
					return result;
				}},
			{title:'开始时间',field:'startedDate',width:150},
			{title:'完成时间',field:'completedDate',width:150},
			{title:'区域',field:'areaName',width:80},
			{title:'是否自动回单',field:'isAuto',width:100,
				formatter:function(value,row,index){
					if(value=='1'){
						return '是';
					}else{
						return '否';
					}
			}},
			{title:'方向',field:'direction',hidden:true},
			{title:'环节标识',field:'tacheId',width:100},
			{title:'环节编码',field:'tacheCode',width:100},
			{title:'活动定义标识',field:'activityDefinitionId',width:120},
			{title:'活动实例标识',field:'activityInstanceId',width:120},
			{title:'流程定义标识',field:'processDefineId',width:120},
			{title:'流程实例标识',field:'processInstanceId',width:120},
			{title:'流程实例名称',field:'processInstanceName',width:120},
			{title:'参与者标识',field:'participantId',width:100},
			{title:'参与者职位',field:'participantPositionId',width:100},
			{title:'分配到组织标识',field:'organizationId',width:120},
			{title:'分配时间',field:'assignedDate',width:150,sortable:true},
			{title:'持续时间',field:'dueDate',width:150},
			{title:'优先级',field:'priority',width:80},
			{title:'注释',field:'memo',width:200},
			{title:'批次',field:'batchid',width:80},
			{title:'创建来源',field:'createSource',width:80},
			{title:'参与人',field:'partyName',width:100},
			{title:'人工执行人',field:'manualPartyName',width:100},
			{title:'操作人',field:'operatePartyName',width:100},
			{title:'旧工作项标识',field:'oldWorkItemId',width:120},
			{title:'区域id',field:'areaId',hidden:true}
	    ]]
	});
	//查询方法
	var funQry = function(pIndex){
		startDate = $("#startDate").datetimebox('getText');
		endDate = $("#endDate").datetimebox('getText');
		if(Date.parse(endDate) < Date.parse(startDate)){
			$.messager.alert("提示","结束时间不能早于开始时间");
			return;
		}
		var now = new Date();
		if(now < Date.parse(startDate)){
			$.messager.alert("提示","开始时间不能晚于当前时间");
			return;
		}
		var params = {
			processInstanceId:$("#processInstId").val(),
			startDate:$("#startDate").datetimebox('getText'),
			endDate:$("#endDate").datetimebox('getText'),
			state:$('#pState').combobox('getValue'),
			pageIndex:pIndex||page,
			pageSize:pageSize,
			sortColumn:$('#flowInstTable').datagrid('options').sortName||"createdDate",
			sortOrder:$('#flowInstTable').datagrid('options').sortOrder||"desc"
		};
		var ret = $.callSyn("FlowInstHisServ","queryProcessInstancesHisByCond",$.util.formatObj(params));
		pager.pagination('refresh',{pageNumber:1});//刷新分页信息
		if(ret.total>0){
			$('#flowInstTable').datagrid('loadData',ret);
		}else{
			$('#flowInstTable').datagrid('loadData',{total: 0, rows:[]});//清空
		}
		$('#workItemTable').datagrid('loadData',{total: 0, rows:[]});//清空
		
		$('#flowInstTb > .easyui-linkbutton').linkbutton('disable');//失效流程实例按钮
		$('#workItemTb > .easyui-linkbutton').linkbutton('disable');//失效工作项按钮
	};
	//查询工作项
	var funQryWorkItem = function(id){
		var ret = $.callSyn("FlowInstHisServ","qryWorkItemByCond",{processInstanceId:id,sortColumn:"assignedDate",sortOrder:"asc"});
		if(ret.total>0){
			$('#workItemTable').datagrid('loadData',ret);
		}else{
			$('#workItemTable').datagrid('loadData',{total: 0, rows:[]});//清空
		}
	};
	//查询流程图
	var funShowGraph = function(processInstanceId,isHistory){
		$('#flowGraphWin').dialog('open');
			
		if(!$("#state").html()){
			$.zflow.initStateImage($("#state"));//初始化 流程图状态
		}
		
		var params ={processInstanceId:processInstanceId,isHistory:isHistory||false};
		
		var inst = $.callSyn("FlowInstHisServ","qryProcessInstanceForTrace",params);
		$("#inst").empty();
		$("#inst").zflow(inst,{mode:"inst",direction:"horizontal"});
		
		var def = $.callSyn("FlowInstHisServ","qryProcessDefineForTrace",params);
		$("#def").empty();
        $("#def").zflow(def,{mode:"def",direction:"horizontal"});
        
	};
	
	$('#btnQry').click(function(){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		funQry();
	});
	$('#btnQryGraph').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sFlowInst = $('#flowInstTable').datagrid('getSelected');
		funShowGraph(sFlowInst.processInstanceId);
	});

	//查询影子流程图
	var funShowShadowGraph = function(processInstanceId,isHistory){
		$('#shadowFlowGraphWin').dialog('open');
			
		var params ={processInstanceId:processInstanceId,isHistory:isHistory||false};
		var shadow = $.callSyn("FlowInstHisServ","qryProcInstShadowForTrace",params);
		$("#shadow").empty();
        $("#shadow").zflow(shadow,{mode:"inst",direction:"horizontal"});
	};
	
	$('#btnQryShadowGraph').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sFlowInst = $('#flowInstTable').datagrid('getSelected');
		funShowShadowGraph(sFlowInst.processInstanceId);
	});
	
	//查看父流程
	$('#btnQryParentGraph').click(function(e){
		if($(this).linkbutton('options').disabled){
			return;
		}
		var sItem = $('#flowInstTable').datagrid('getSelected');
		var ret = $.callSyn("FlowInstHisServ","qryActivityInstance",{activityInstanceId:sItem.parentActivityInstanceId});
		funShowGraph(ret.processInstanceId);
	});
	$("#procInstDetailWin").dialog({
		onBeforeOpen:function(){
			$('#detailInfoDiv').hide();
			$('#msgInfo').textbox('setValue','');
			$('#msgRetInfo').textbox('setValue','');
		}
	});
	$('#btnQryProcInstDetail').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sFlowInst = $('#flowInstTable').datagrid('getSelected');
		var ret =  $.callSyn("FlowInstHisServ","qryCommandMsgInfoByPid",{processInstanceId: sFlowInst.processInstanceId});
		$('#procInstDetailGrid').datagrid('loadData',ret.rows);
		$("#procInstDetailWin").dialog('open');
	});
	
	$('#procInstDetailGrid').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
		pagination:false,//
	    fitColumns:true,
	    idField:'code',
	    columns:[[
				{title:'接口编码',field:'commandCode',width:100},
				{title:'流程实例标识',field:'processInstanceId',width:100},
				{title:'工作项',field:'workItemId',width:100},
				{title:'创建时间',field:'createDate',width:120},
				{title:'接口接收消息',field:'commandMsg',width:120},
				{title:'接口反馈消息',field:'commandResultMsg',width:120}
	    	]],
			onClickRow: function(index,row){
				$('#detailInfoDiv').show();		
				$('#msgInfo').textbox('setValue',row.commandMsg);
				$('#msgRetInfo').textbox('setValue',row.commandResultMsg);
			}
	});
	
});
