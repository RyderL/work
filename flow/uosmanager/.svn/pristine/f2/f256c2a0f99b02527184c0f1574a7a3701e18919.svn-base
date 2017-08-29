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
		autoRowHeight:false,//是否设置基于该行内容的行高度
		sortName:'createDate',
		sortOrder: 'desc',
	    striped:true,//奇偶行使用不同背景色
	    pagination:true,//
		pageSize:pageSize,//初始化页面尺寸	
	    columns:[[
	    	{title:'异常标识',field:'id',width:120,sortable:true},
			{title:'接口编码',field:'commandCode',width:120,sortable:true},
			{title:'流程实例标识',field:'processInstanceId',width:120,sortable:true},
			{title:'异常描述',field:'errorInfo',width:200},
			{title:'详情',field:'msg',width:200},
			{title:'创建日期',field:'createDate',width:150,sortable:true},
			{title:'处理日期',field:'dealDate',width:150},
			{title:'处理次数',field:'dealTimes',width:80}
	    ]],
	    
	    singleSelect: false,
	    selectOnCheck: false,
	    checkOnSelect: false,
		onSortColumn:function(sort,order){
			funQry();
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
		var ret = $.callSyn("ExceptionHisServ","qryUosFlowErrorsByCond",$.util.formatObj(params));
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
