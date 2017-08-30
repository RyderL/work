$(function($) {
	/** 全局变量*/
	var page=1;
	var pageSize=20;
	var session = $.session();
	
	$('#processInstId').textbox({
		prompt: '流程实例ID',
		icons:[{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).textbox('clear').textbox('textbox').focus();
			}
		}]
	});
	$('#startDate').datetimebox({
		value: '',
	    showSeconds: false
	});
	$('#endDate').datetimebox({
	    value: '9999',
	    showSeconds: false
	});
	
	$('#btnReSend').linkbutton('disable');
	
	$('#commandErrorTable').datagrid({
		rownumbers:true,//显示带有行号的列
		//singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
	    //fitColumns:true,
		sortName:'createDate',
		sortOrder: 'desc',
	    striped:true,//奇偶行使用不同背景色
	    pagination:true,//
		pageSize:pageSize,//初始化页面尺寸	
	    columns:[[
	        {field: 'ck', checkbox: true },
	    	{title:'id',field:'id',width:120,sortable:true},
			{title:'接口编码',field:'commandCode',width:100},
			{title:'流程实例标识',field:'processInstanceId',width:120,sortable:true},
			{title:'工作项',field:'workItemId',width:100},
//			{title:'状态',field:'state',width:120,
//				formatter:function(value,row,index){
//					var result=value;
//					if(value=='0'){
//						result="初始";
//					}else if(value=='1'){
//						result="等待处理";
//					}else if(value=='2'){
//						result="处理完成";
//					}
//					return result;
//				}},
			{title:'监控信息',field:'commandMsg',width:200},
			{title:'状态',field:'state',width:60},
			{title:'详情',field:'commandResultMsg',width:200},
			{title:'创建日期',field:'createDate',width:120,sortable:true},
			{title:'路由',field:'route',width:60}
	    ]],
	    
	    singleSelect: false,
	    selectOnCheck: true,
	    checkOnSelect: true,
//	    onLoadSuccess:function(data){                   
//	    if(data){
//	    	$.each(data.rows, function(index, item){
//	    		if(item.checked){
//	    			$('#commandErrorTable').datagrid('checkRow', index);
//	    		}
//	    	});
//	    	}
//	    },    
		onSortColumn:function(sort,order){
			funQry();
		},
		onCheck: function(index,row){//勾选进行判断是否激活消息重投按钮
			var checkedItems = $('#commandErrorTable').datagrid('getChecked');
			var result = "false";
			$.each(checkedItems, function(index2, item){
				if (item.exceptionType=='2201' || item.exceptionType=='2202'){//有效的记录
					result = "true";
				}else{
					result = "false";
					return false;
				}
			});
			if(result =="true"){
				$('#btnReSend').linkbutton('enable');//激活消息重投按钮
			}else{
				$('#btnReSend').linkbutton('disable');//失效消息重投按钮
			}
			
		},
		onUncheck: function(index,row){//取消勾选进行判断是否激活消息重投按钮
			var checkedItems = $('#commandErrorTable').datagrid('getChecked');
			var result = "false";
			$.each(checkedItems, function(index2, item){
				if (item.exceptionType=='2201' || item.exceptionType=='2202'){//有效的记录
					result = "true";
				}else{
					result = "false";
					return false;
				}
			});
			if(result =="true"){
				$('#btnReSend').linkbutton('enable');//激活消息重投按钮
			}else{
				$('#btnReSend').linkbutton('disable');//失效消息重投按钮
			}
			
		}
	});
	//分页处理
	var pager=$('#commandErrorTable').datagrid('getPager');
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
	//查询方法
	var funQry = function(pIndex){
		var params = {
			processInstanceId:$("#processInstId").val(),
			startDate:$("#startDate").datetimebox('getText'),
			endDate:$("#endDate").datetimebox('getText'),
			pageIndex:pIndex||page,
			pageSize:pageSize,
			sortColumn:getSortName($('#commandErrorTable').datagrid('options').sortName),
			sortOrder:$('#commandErrorTable').datagrid('options').sortOrder||"desc"
		};
		var ret = $.callSyn("CommandServ","qryUosCommandErrorsByCond",$.util.formatObj(params));
		pager.pagination('refresh',{pageNumber:1});//刷新分页信息
		if(ret.total>0){
			$('#commandErrorTable').datagrid('loadData',ret);
		}else{
			$('#commandErrorTable').datagrid('loadData',{total: 0, rows:[]});//清空
		}
	};
	$('#btnQry').click(function(){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		funQry();
	});
	//异常处理
	$('#btnException').click(function(e){
	/*	if($(this).linkbutton('options').disabled){
			return;
		}*/
		var checkedItems = $('#commandErrorTable').datagrid('getChecked');
		var errorIds = [];
		$.each(checkedItems, function(index, item){
			errorIds.push(item.id);
		});
		var params ={
				errorIds:errorIds
			};
		var ret = $.callSyn("ExceptionServ","dealExceptions",params);
		if(ret.isSuccess == "false"){
			$.messager.alert("提示","流程异常处理失败");
		}else{
			$.messager.alert("提示","流程异常处理成功");
		}
		funQry();
	});
	var getSortName = function(sortName){
		var newSortName;
		if(sortName=='id'){
			newSortName="id";
		}else if(sortName=='processInstanceId'){
			newSortName="process_instance_id";
		}else{
			newSortName="create_date";
		}
		return newSortName;
	};
	
	//消息重投
	$('#btnReSend').click(function(e){
	/*	if($(this).linkbutton('options').disabled){
			return;
		}*/
		var checkedItems = $('#commandErrorTable').datagrid('getChecked');
		var errorIds = [];
		$.each(checkedItems, function(index, item){
			errorIds.push(item.id);
		});
		var params ={
				errorIds:errorIds
			};
		var ret = $.callSyn("ExceptionServ","dealExceptions",params);
		if(ret == "false"){
			$.messager.alert("提示","流程异常处理失败");
		}else{
			$.messager.alert("提示","流程异常处理成功");
		}
		funQry();
	});
	var getSortName = function(sortName){
		var newSortName;
		if(sortName=='id'){
			newSortName="id";
		}else if(sortName=='processInstanceId'){
			newSortName="process_instance_id";
		}else{
			newSortName="create_date";
		}
		return newSortName;
	};
});
