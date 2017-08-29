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
	$('#startDate').datetimebox({
		value: '',
	    showSeconds: false
	});
	$('#endDate').datetimebox({
	    value: '9999',
	    showSeconds: false
	});
	
	$('#flowErrorTable').datagrid({
		rownumbers:true,//显示带有行号的列
		//singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
	    //fitColumns:true,
		sortName:'createDate',
		sortOrder: 'desc',
	    striped:true,//奇偶行使用不同背景色
	    pagination:true,//
		pageSize:pageSize,//初始化页面尺寸	
		toolbar: '#flowErrorTb',
	    columns:[[
	        {field: 'ck', checkbox: true },
	    	{title:'异常标识',field:'id',width:120,sortable:true},
			{title:'接口编码',field:'commandCode',width:120,sortable:true},
			{title:'流程实例标识',field:'processInstanceId',width:120,sortable:true},
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
			{title:'异常描述',field:'errorInfo',width:200},
			{title:'详情',field:'msg',width:200},
			{title:'创建日期',field:'createDate',width:150,sortable:true},
			{title:'处理日期',field:'dealDate',width:150},
			{title:'处理次数',field:'dealTimes',width:80}
	    ]],
	    
	    singleSelect: false,
	    selectOnCheck: true,
	    checkOnSelect: true,
//	    onLoadSuccess:function(data){                   
//	    if(data){
//	    	$.each(data.rows, function(index, item){
//	    		if(item.checked){
//	    			$('#flowErrorTable').datagrid('checkRow', index);
//	    		}
//	    	});
//	    	}
//	    },    
		onSortColumn:function(sort,order){
			funQry();
		},
		onCheck: function(index,row){//勾选进行判断是否激活消息重投按钮
			var checkedItems = $('#flowErrorTable').datagrid('getChecked');
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
			var checkedItems = $('#flowErrorTable').datagrid('getChecked');
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
		onClickRow: function(index,row){//加载详情
			$('#msgInfo').textbox('setValue',row.msg);
			$('#errorInfo').textbox('setValue',row.errorInfo);
		}
	});
	//分页处理
	var pager=$('#flowErrorTable').datagrid('getPager');
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
			sortColumn:getSortName($('#flowErrorTable').datagrid('options').sortName),
			sortOrder:$('#flowErrorTable').datagrid('options').sortOrder||"desc"
		};
		var ret = $.callSyn("ExceptionServ","qryUosFlowErrorsByCond",$.util.formatObj(params));
		pager.pagination('refresh',{pageNumber:1});//刷新分页信息
		if(ret.total>0){
			$('#flowErrorTable').datagrid('loadData',ret);
		}else{
			$('#flowErrorTable').datagrid('loadData',{total: 0, rows:[]});//清空
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
		var checkedItems = $('#flowErrorTable').datagrid('getChecked');
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
		var checkedItems = $('#flowErrorTable').datagrid('getChecked');
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
