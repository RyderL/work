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
	
	$('#commandErrorTable').datagrid({
		rownumbers:true,//显示带有行号的列
		autoRowHeight:false,//是否设置基于该行内容的行高度
		sortName:'createDate',
		sortOrder: 'desc',
	    striped:true,//奇偶行使用不同背景色
	    pagination:true,//
		pageSize:pageSize,//初始化页面尺寸	
	    columns:[[
	    	{title:'id',field:'id',width:120,sortable:true},
			{title:'接口编码',field:'commandCode',width:100},
			{title:'流程实例标识',field:'processInstanceId',width:120,sortable:true},
			{title:'工作项',field:'workItemId',width:100},
			{title:'监控信息',field:'commandMsg',width:200},
			{title:'状态',field:'state',width:60},
			{title:'详情',field:'commandResultMsg',width:200},
			{title:'创建日期',field:'createDate',width:120,sortable:true},
			{title:'路由',field:'route',width:60}
	    ]],
	    singleSelect: false,
		onSortColumn:function(sort,order){
			funQry();
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
		var ret = $.callSyn("CommandHisServ","qryUosCommandErrorsByCond",$.util.formatObj(params));
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
