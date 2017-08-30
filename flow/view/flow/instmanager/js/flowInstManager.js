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
			//$('#btnCancelProcessInstance').linkbutton('enable');
			//$('#btnTerminateProcessInstance').linkbutton('enable');
			//$('#btnException').linkbutton('disable');
			if(row.state==2){//正常执行中
				$('#btnSuspendProcessInstance').linkbutton('enable');
				$('#btnCancelProcessInstance').linkbutton('enable');
				$('#btnTerminateProcessInstance').linkbutton('enable');
			}else if(row.state==1){//挂起中
				$('#btnSuspendProcessInstance').linkbutton('disable');
				$('#btnResumeProcessInstance').linkbutton('enable');
			}else if(row.state==5){//已完成
				$('#btnCancelProcessInstance').linkbutton('disable');
				$('#btnTerminateProcessInstance').linkbutton('disable');
				$('#btnSuspendProcessInstance').linkbutton('disable');
			}else{
				$('#btnSuspendProcessInstance').linkbutton('disable');
				$('#btnResumeProcessInstance').linkbutton('disable');
			}
			//mod by che.zi 20160623 for zmp889947 begin
			/** if(row.state==7){
				$('#btnException').linkbutton('enable');
			}*/
			//mod by che.zi 20160623 for zmp889947 end
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
	    toolbar: '#workItemTb',
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
	    ]],
		onClickRow: function(index,row){
			if(row.state==1){
				$('#btnCompleteWorkItem').linkbutton('enable');
				//mod by che.zi 20160623 for zmp889947 begin
				var sFlowInst = $('#flowInstTable').datagrid('getSelected');
				if(row.direction == '1' && sFlowInst.state==2){
					$('#btnDisableWorkItem').linkbutton('enable');
				}else{
					$('#btnDisableWorkItem').linkbutton('disable');
				}
				$('#btnJumpProcessInstance').linkbutton('enable');
//				$('#btnAddSubFlow').linkbutton('enable');
				$('#btnReSend').linkbutton('enable');
			}else{
				$('#btnCompleteWorkItem').linkbutton('disable');
				$('#btnDisableWorkItem').linkbutton('disable');
				$('#btnJumpProcessInstance').linkbutton('disable');
				$('#btnReSend').linkbutton('disable');
//				$('#btnAddSubFlow').linkbutton('disable');
				////mod by che.zi 20160623 for zmp889947 end
			}
		}
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
		var ret = $.callSyn("FlowInstServ","qryProcessInstanceByCond",$.util.formatObj(params));
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
		var ret = $.callSyn("FlowInstServ","qryWorkItemByCond",{processInstanceId:id,sortColumn:"assignedDate",sortOrder:"asc"});
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
		
		var inst = $.callSyn("FlowInstServ","qryProcessInstanceForTrace",params);
		$("#inst").empty();
		$("#inst").zflow(inst,{mode:"inst",direction:"horizontal"});
		
		var def = $.callSyn("FlowInstServ","qryProcessDefineForTrace",params);
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
		var shadow = $.callSyn("FlowInstServ","qryProcInstShadowForTrace",params);
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
	//挂起
	$('#btnSuspendProcessInstance').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sItem = $('#flowInstTable').datagrid('getSelected');
		var param ={
			processInstanceId:sItem.processInstanceId,
			areaId:sItem.areaId
		};
		var ret = $.callSyn("FlowOperServ","suspendProcessInstance",param);
		if(ret=='fail'){
			$.messager.alert("提示","流程挂起失败");
		}else{
			$.messager.alert("提示","流程挂起成功");
			funQry();
		}
	});
	//解挂
	$('#btnResumeProcessInstance').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sItem = $('#flowInstTable').datagrid('getSelected');
		var param ={
			processInstanceId:sItem.processInstanceId,
			areaId:sItem.areaId
		};
		var ret = $.callSyn("FlowOperServ","resumeProcessInstance",param);
		if(ret=='fail'){
			$.messager.alert("提示","流程解挂失败");
		}else{
			$.messager.alert("提示","流程解挂成功");
			funQry();
		}
	});
	//提交工作项
	$('#btnCompleteWorkItem').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sItem = $('#workItemTable').datagrid('getSelected');
		if(sItem){
			$("#tacheFlowParamWin").data('packageDefineId',sItem.processDefineId);
			$("#tacheFlowParamWin").data('type','TACHE');
			$("#tacheFlowParamWin").data('tacheCode',sItem.tacheCode);
			$("#tacheFlowParamWin").data('workItemId',sItem.workItemId);
			$("#tacheFlowParamWin").data('areaId',sItem.areaId);
			$("#tacheFlowParamWin").dialog('open');
		}else{
			$.messager.alert("提示","请选择工作项","info");
		}
	});
	
	//生成工单消息重投
	$('#btnReSend').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sItem = $('#workItemTable').datagrid('getSelected');
		if(sItem){
			var ret = $.callSyn("FlowOperServ","reCreateWorkOrder",{workItemId:sItem.workItemId,processInstanceId:sItem.processInstanceId});
			if(ret=='success'){
				$.messager.alert("提示","生成工单消息重投成功","info");
			}else{
				$.messager.alert("提示","生成工单消息重投失败","info");
			}
		}else{
			$.messager.alert("提示","请选择工作项","info");
		}
	});
	
	//add by che.zi 2015-08-13
	//提交工作项-增加环节流程参数
	$("#tacheFlowParamWin").dialog({
		onBeforeOpen:function(){
			var flowParamDefs = $.callSyn("FlowServ","qryFlowParamDefRels",{
				packageDefineId:$("#tacheFlowParamWin").data('packageDefineId'),type:$("#tacheFlowParamWin").data('type'),tacheCode:$("#tacheFlowParamWin").data('tacheCode')});
			$('#tacheflowParamGrid').datagrid('loadData',flowParamDefs);
			$('#tacheflowParamGrid').datagrid('unselectAll');
		}
	});
	//启动流程的流程参数信息-列表 
	$('#tacheflowParamGrid').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
		pagination:false,//
	    fitColumns:true,
	    idField:'code',
	    columns:[[
				{title:'参数编码',field:'code',width:100},
				{title:'参数值',field:'value',width:100},
				{title:'类型',field:'type',width:100,formatter:function(value,row,index){
					if(value=='FLOW'){
						return '流程';
					}else{
						return '环节';
					}
				}},
				{title:'是否可变',field:'isVariable',width:100},
				{title:'系统编码',field:'systemCode',hidden:true}
	    	]],
			onClickRow: function(index,row){
				$('#tacheFlowParamModify').show();
			}
	});
	$('#tacheFlowParamWin-confirmBtn').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var rows = $('#tacheflowParamGrid').datagrid('getRows');
		var flowparam = new Object();
    	$.each(rows,function(i,n){
    		flowparam[n.code] =  n.value;
    	}); 
		var sItem = $('#workItemTable').datagrid('getSelected');
		var param ={
			workItemId:$("#tacheFlowParamWin").data('workItemId'),
			areaId:$("#tacheFlowParamWin").data('areaId'),
			flowParamMap:flowparam,
			operatePartyType:'STA',
			operatePartyId:session.staffId,
			operatePartyName:session.staffName,
			processInstanceId:sItem.processInstanceId
		};
		var ret = $.callSyn("FlowOperServ","completeWorkItem",param);
		if(ret=='fail'){
			$.messager.alert("提示","提交工作项失败");
		}else{
			$.messager.alert("提示","提交工作项成功");
			funQryWorkItem(sItem.processInstanceId);
		}
    	$("#tacheFlowParamWin").dialog("close");
	});
	$('#tacheFlowParamModify').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var row = $("#tacheflowParamGrid").datagrid("getSelected");
		if(row){
			$("#tacheflowParamInfoWin").dialog({title:'修改环节流程参数值'});
			$("#tacheflowParamInfoWin-code").textbox("setValue",row.code).textbox('disable');
			$("#tacheflowParamInfoWin-value").textbox("setValue",row.value);
			$("#tacheflowParamInfoWin-packageDefineId").val($("#tacheFlowParamWin").data('packageDefineId'));
			$("#tacheflowParamInfoWin-type").val(row.type);
			$("#tacheflowParamInfoWin").data('isVariable',row.isVariable);
			$("#tacheflowParamInfoWin-tacheCode").val($("#tacheFlowParamWin").data('tacheCode'));
			$("#tacheflowParamInfoWin").dialog("open");
		}else{
			$.messager.alert("提示","请选择你要修改的行","info");
		}
	});
	//修改流程参数值窗口的确定按钮
	$('#tacheflowParamInfoWin-confirmBtn').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		if($('#tacheflowParamInfoWin-form').form('validate')){
			var row = new Object();
			row.code = $('#tacheflowParamInfoWin-code').val();
			row.value = $('#tacheflowParamInfoWin-value').val();
			row.isVariable = $("#tacheflowParamInfoWin").data('isVariable');
			var rows = new Array();
			rows[0] = row;
			var packageDefineId = $('#tacheflowParamInfoWin-packageDefineId').val();
			var type = $('#tacheflowParamInfoWin-type').val();
			var tacheCode = $('#tacheflowParamInfoWin-tacheCode').val();
			var param = {
				packageDefineId:packageDefineId,
				type:type,
				tacheCode:tacheCode,
				rows:rows
			};
			var ret = $.callSyn("FlowServ","updateBatchFlowParamDefRel",param);
			if(ret){
				var flowParamDefs = $.callSyn("FlowServ","qryFlowParamDefRels",param);
				$('#tacheflowParamGrid').datagrid('loadData',flowParamDefs);
				$('#tacheflowParamGrid').datagrid('unselectAll');
			}
			$("#tacheflowParamInfoWin").dialog("close");
		}
	});
	
	//end
	//作废工作项————退单
	$('#exceptionGrid').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
		pagination:false,//
	    fitColumns:true,
	    columns:[[
				{title:'异常原因分类',field:'reasonTypeName',width:100,
					formatter:function(value,row,index){
						var name = value;
						var type = row.reasonType;
						if(type){
							if(type=="10R"){
								name="退单";
							}else if(type=="10W"){
								name="待装";
							}else if(type=="10Q"){
								name="改单";
							}
						};
						return name;
					}},
				{title:'异常原因',field:'reasonName',width:100},//returnReasonName
				{title:'回滚目标环节',field:'targetTacheName',width:100,//endActivityName
					formatter:function(value,row,index){
						return value||'开始节点';
					}},
				{title:'是否自动转人工',field:'autoToManual',width:100,
					formatter:function(value,row,index){
						if(value=='true'){
							return '是';
						}else{
							return '否';
						}
					}},
				{title:'发生异常环节id',field:'tacheId',hidden:true},//startActivityId
				{title:'回滚目标环节id',field:'targetTacheId',hidden:true},//endActivityId
				{title:'异常原因类型编码',field:'reasonType',hidden:true},
				{title:'异常原因id',field:'reasonId',hidden:true},//returnReasonId
				{title:'异常原因编码',field:'reasonCode',hidden:true},//returnReasonCode
				{title:'启动模式',field:'startMode',hidden:true},
				{title:'异常原因配置id',field:'id',hidden:true}//reasonConfigId
	    	]]
	});
	$('#btnDisableWorkItem').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sItem = $('#flowInstTable').datagrid('getSelected');
		if(sItem.state._state==9){
			$.messager.alert("提示","该流程处于流程回退中，不允许再次退单","info");
		}else{
			var sEx = $('#workItemTable').datagrid('getSelected');
			var proInst = $('#flowInstTable').datagrid('getSelected');
			if(sEx){
//				var params = {
//					processDefineId:sEx.processDefineId,
//					startActivityId:sEx.activityDefinitionId,
//					processInstanceId:sEx.processInstanceId
//				};
//				var ret = $.callSyn("FlowInstServ","qryProcessExceptionByCond",params);
				var rows = $('#workItemTable').datagrid('getRows');
				var reasons = $.callSyn("ReturnReasonServ","qryReturnReasonConfigs",{packageDefineCode: proInst.processDefineCode});//查询流程的所有异常原因
				var datas = [];//保存有效异常原因
				$.each(reasons.rows,function(i,n){
					if(sEx.tacheId==n.tacheId){	//匹配开始环节
						if(n.targetTacheId==0){//退单回开始节点
							datas.push(n);
						}else{
							var i=0,len =rows.length;
							for(;i<len;i++){//匹配可退单的环节
								if(rows[i].tacheId==n.targetTacheId){
									datas.push(n);
									break;
								}
							}
						}
					}
				});
				if(datas&&datas.length>0){
					$('#exceptionGrid').datagrid('loadData',datas);
					$("#exceptionWin").dialog('open');
				}else{
					$.messager.alert("提示","请配置该流程的异常信息","info");
				}
			}else{
				$.messager.alert("提示","请选择工作项","info");
			}
		}
	});
	$("#exceptionWin-confirmBtn").click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sItem = $('#workItemTable').datagrid('getSelected');
		var sEx = $('#exceptionGrid').datagrid('getSelected');
		if(sEx){
			var param ={
				workItemId:sItem.workItemId,
				reasonType:sEx.reasonType,
				//targetActivityId:sEx.endActivityId,
				reasonCode:sEx.reasonCode,//returnReasonCode
				reasonConfigId:sEx.id,//reasonConfigId
				areaId:sItem.areaId,
				returnReasonName:sEx.reasonName,//returnReasonName
				processInstanceId:sItem.processInstanceId
			};
			var ret = $.callSyn("FlowOperServ","disableWorkItem",param);
			if(ret=='fail'){
				$.messager.alert("提示","退单失败");
			}else{
				$.messager.alert("提示","退单成功");
				funQryWorkItem(sItem.processInstanceId);
				$("#exceptionWin").dialog('close');
			}
		}else{
			$.messager.alert("提示","请选择异常原因","info");
		}
	});
	
	//撤单
	$('#btnCancelProcessInstance').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sItem = $('#flowInstTable').datagrid('getSelected');
		var param ={
			processInstanceId:sItem.processInstanceId,
			areaId:sItem.areaId
		};
		var ret = $.callSyn("FlowOperServ","cancelProcessInstance",param);
		if(ret=='fail'){
			$.messager.alert("提示","流程撤单失败");
		}else{
			$.messager.alert("提示","流程撤单成功");
			funQry();
		}
	});
	
	//终止流程
	$('#btnTerminateProcessInstance').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sItem = $('#flowInstTable').datagrid('getSelected');
		var param ={
			processInstanceId:sItem.processInstanceId,
			areaId:sItem.areaId
		};
		var ret = $.callSyn("FlowOperServ","terminateProcessInstance",param);
		if(ret=='fail'){
			$.messager.alert("提示","终止流程失败");
		}else{
			$.messager.alert("提示","终止流程成功");
			funQry();
		}
	});
	//mod by che.zi 20160623 for zmp889947 begin
	//添加子流程确认窗口
	/*$('#addSubFlowWin-confirmBtn').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		if($.trim($('#subPackageDefineCode').val()).length==0){
			$.messager.alert("提示","请输入流程编码","info");
			return;
		}
		var subPackageDefine =  $.callSyn("FlowServ","findProcessDefinitionByCode",{packageDefineCode: $('#subPackageDefineCode').val()});
		if($.isEmptyObject(subPackageDefine)){
			$.messager.alert("提示","不存在此流程编码，请重新输入","info");
		}else{
			var flowParamDefs = $.callSyn("FlowServ","qryFlowParamDefRels",{packageDefineId:subPackageDefine.packageDefineId,type:'FLOW',tacheCode:'-1'});
			var flowparam = new Object();
	    	$.each(flowParamDefs.rows,function(i,n){
	    		flowparam[n.code] =  n.value;
	    	}); 
	    	var params = {
	    		processDefineId:subPackageDefine.packageDefineId,
	    		processDefineName:subPackageDefine.name,
	    		areaId:session.areaId,
	    		flowParamMap:flowparam,
	    		parentActivityInstanceId:$('#workItemTable').datagrid('getSelected').activityInstanceId
	    	};
	    	var pId = $.callSyn("FlowOperServ","startFlow",params);
	    	if(pId == 'fail'){
				$.messager.alert("提示","启动子流程失败","info");
			}else{
				$.messager.alert("提示","启动子流程成功:"+pId,"info");
				funQry();
				$('#addSubFlowWin').dialog('close');
			}
		}
	});*/

	//添加子流程按钮
	/*$('#btnAddSubFlow').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sItem = $('#workItemTable').datagrid('getSelected');
		if(sItem){
			$('#subPackageDefineCode').textbox('clear');//清空
			$('#addSubFlowWin').dialog('open');
		}else{
			$.messager.alert("提示","请选择工作项","info");
		}
	});*/
	//mod by che.zi 20160623 for zmp889947 end
	
	//流程跳转
	$('#btnJumpProcessInstance').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var sItem = $('#flowInstTable').datagrid('getSelected');
		var param ={
				processInstanceId:sItem.processInstanceId,
				areaId:sItem.areaId
			};
		var data = $.callSyn("FlowInstServ","qryUndoActivityByCond",param);
//		console.log(data);
		var tmp={total:1,rows:data};
//		console.log(tmp);
		$("#jumpProcInstWin-targetActivity").combobox('clear');
		$("#jumpProcInstWin-targetActivity").combobox('loadData',data);
//		$("#jumpProcInstWin-targetActivity").combobox('select',data[0].value);
		$("#jumpProcInstWin").data('processInstanceId',sItem.processInstanceId);
		$("#jumpProcInstWin").data('areaId',sItem.areaId);
		$("#jumpProcInstWin").dialog('open');
	});
	$('#jumpProcInstWin-confirmBtn').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var targetActivityId = $('#jumpProcInstWin-targetActivity').combobox('getValue');
		var sItem = $('#workItemTable').datagrid('getSelected');
		var param ={
				processInstanceId:$("#jumpProcInstWin").data('processInstanceId'),
				areaId:$("#jumpProcInstWin").data('areaId'),
				targetActivityId:targetActivityId,
				fromActivityInstanceId:sItem.activityInstanceId
			};
		var ret = $.callSyn("FlowOperServ","processInstanceJump",param);
		if(ret=='fail'){
			$.messager.alert("提示","流程跳转失败");
		}else{
			$.messager.alert("提示","流程跳转成功");
		}
		$("#jumpProcInstWin").dialog('close');
	});
	
	//查看父流程
	$('#btnQryParentGraph').click(function(e){
		if($(this).linkbutton('options').disabled){
			return;
		}
		var sItem = $('#flowInstTable').datagrid('getSelected');
		var ret = $.callSyn("FlowInstServ","qryActivityInstance",{activityInstanceId:sItem.parentActivityInstanceId});
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
		var ret =  $.callSyn("FlowInstServ","qryCommandMsgInfoByPid",{processInstanceId: sFlowInst.processInstanceId});
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
				$('#btnReExcute').linkbutton('enable');
				$('#msgInfo').textbox('setValue',row.commandMsg);
				$('#msgRetInfo').textbox('setValue',row.commandResultMsg);
			}
	});
	$('#btnReExcute').click(function(e){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var row = $("#procInstDetailGrid").datagrid("getSelected");
		if(row){
			var ret =  $.callSyn("FlowOperServ","reExcuteMsg",row);
			if(ret == 'fail'){
				$.messager.alert("提示","消息重投失败","info");
			}else{
				$.messager.alert("提示","消息重投成功","info");
			}
		}else{
			$.messager.alert("提示","请选择你要重投的消息","info");
		}
	});
});
