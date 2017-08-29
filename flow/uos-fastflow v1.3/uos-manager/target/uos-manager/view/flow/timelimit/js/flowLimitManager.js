$(function($){
	var session = $.session();
	var modType = false;
	var pageSize = 10;
	var page = 1;
	var nodeType = {
		CATALOG:"1",		//目录
		ELEMENT:"2",		//元素--环节或者流程
		VERSION:"3"			//版本--流程
	};
	var isQryButton = false;

	var flowTreeData = $.callSyn("FlowServ","queryPackageCatalogByAreaIdAndSystemCode",{areaId: session["areaId"],systemCode:session["systemCode"]});
	//方法：初始化区域树
	var initAreaTree = function(){
		//areaId为-1加载全部
		var areaTreeData = $.callSyn("AreaServ","getAreaJsonTree",{areaId: session["areaId"]});
		$('#areaTree').tree({
			data : areaTreeData,
			onClick:function(node){
//				var flowTreeData = $.callSyn("FlowServ","queryPackageCatalogByAreaIdAndSystemCode",{areaId: node.id,systemCode:session["systemCode"]});
//				initFlowTree(flowTreeData);
//				funBtn($('#btnAddFl'),funAddFl,true);
//				funQryFl();
			},
			onSelect:function(node){
				var data = $.callSyn("FlowServ","queryPackageCatalogByAreaIdAndSystemCode",{areaId: node.id,systemCode:session["systemCode"]});
				initFlowTree(data);
			},
			onContextMenu: function (e, node) {
                e.preventDefault();
            }
		});
	};
	//方法：初始化流动定义树：
	//flowTreeData:流程定义数的json格式数据
	var initFlowTree = function(flowTreeData){
		var subLoad = function(node){
				if(node.type==1&&!node.loaded)
				{
					//加载环节列表
					var ret = $.callSyn('FlowServ','qryProcessDefineByCatalogId',{catalogId:node.id});
					node.loaded = true;
					$('#flowTree').tree('append', { parent : node.target,  data : ret });  
				}
		};
		$('#flowTree').tree({
			data : flowTreeData,
			onExpand:function(node){
				subLoad(node);
			},
			onClick:function(node){
				subLoad(node);
				if(node.type==nodeType.ELEMENT){
					isQryButton = false;
					funQryFl();
				}
			}
		});
	};
	$('#processName').textbox({
		prompt: '流程名称',
		icons:[{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).textbox('clear').textbox('textbox').focus();
			}
		}]
	});
	//easyui-linkbutton 点击问题(disable无法去除点击事件)
	var funBtn = function(btn, fun, enable){
		if(enable){
			btn.unbind('click').bind('click',fun);//每次绑定前先取消上次的绑定
			btn.linkbutton('enable');//激活
		}else{
			btn.unbind('click',fun);
			btn.linkbutton('disable');//失效
		}
	};

	var StringToDate = function(s){
		if(s&&typeof(s)=="string"){
			var s = s.substring(0,19);
			var aD=s.split(/[\/\-: ]/);
			if(aD.length<3) return null;
			if(aD.length<4) aD[3]=aD[4]=aD[5]="00";
			var d=new Date(aD[0],parseInt(aD[1]-1,10),aD[2],aD[3],aD[4],aD[5]);
			if(isNaN(d)) return null;
			return d;
			}
		else return null;
	};
	//初始化流程时限列表
	$('#flowLimitTable').datagrid({
		rownumbers:false,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
	    striped:true,//奇偶行使用不同背景色
	    pagination:true,//
		pageSize:pageSize,//初始化页面尺寸	
	    toolbar: '#flowLimitTb',
	    columns:[[
			{title:'流程名称',field:'processName',width:120,sortable:true},
			{title:'区域',field:'areaName',width:150},
			{title:'完成时限值',field:'limitValue',width:150},
			{title:'告警时限值',field:'alertValue',width:150},
			{title:'时间单位',field:'timeUnit',width:150},
			{title:'只计算工作日',field:'isWorkTime',width:150,
				formatter:function(value,row,index){
					if(value=='1'){
						return '是';
					}else{
						return '否';
					}
				}},
			{title:'id',field:'id',hidden:true},
			{title:'areaId',field:'areaId',hidden:true},
			{title:'packageId',field:'packageId',hidden:true}
	    ]],
		onClickRow: function(index,row){//激活按钮+加载工作时间
			funBtn($('#btnModFl'),funModFl,true);//激活修改按钮
			funBtn($('#btnDelFl'),funDelFl,true);//激活删除按钮
		}
	});
	//分页处理
	var pager=$('#flowLimitTable').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		total:0,//初始化展示分页数据
		onSelectPage:function(pageNumber, pageSize){
			funQryFl(pageNumber);
			pager.pagination('refresh',{
				pageNumber:pageNumber,
				pageSize:pageSize
			});
		}
	});
	// 新增流程时限
	$('#fldlg').dialog({
		title: '流程时限操作',
	    width: 350,
	    closed: true,
	    cache: false,
	    modal: true,
	    top: 75, 
	    left: 400
	});
	/** 对话框 */
	$('#timeUnit').combobox({
		required:true,
		editable:false,
		panelHeight:'auto',
		valueField: 'label',
		textField: 'value',
		data: [{
			label: 'MIN',//NORMAL
			value: '分钟'
		},{
			label: 'HOR',
			value: '时'
		},{
			label:'DAY',
			value:'天'
		}]
	});
	$('#packageName').textbox({
		readonly:true,
		icons: [{
			iconCls:'icon-search',
			handler: function(e){
				$('#flowWin').dialog('open');
			}
		}]
	});
	//区域
	var areaTreeData = $.callSyn("AreaServ","getAreaJsonTree",{areaId: session["areaId"]});
	$('#area').combotree({
		panelHeight:115,
		onShowPanel: function(){
			$('#area').combotree('loadData',areaTreeData);
			var dTree = $('#area').combotree('tree');
			dTree.tree('expand',dTree.tree('getRoot').target);
			dTree.tree({
				onClick:function(node){
					$('#areaId').val(node.id);
					$('#area').combotree('setValue',node.text);
					$('#area').combotree('hidePanel');
				}
			});
		}
	});
	$('a.textbox-icon.icon-search.textbox-icon-disabled').removeClass("textbox-icon-disabled");//使添加Tip有效
	$('#isWorkTime').combobox({
		required:true,
		editable:false,
		panelHeight:'auto',
		valueField: 'label',
		textField: 'value',
		data: [{
			label: '1',
			value: '是'
		},{
			label: '0',
			value: '否'
		}]
	});
	
	//界面初始化------start--------------
	initAreaTree();
	$('#areaTree').tree("select",$("#areaTree").tree("getRoot").target);
	$('#btnAddFl').linkbutton('enable');
//	initFlowTree();
	//界面初始化------end--------------
	$('#btnQry').click(function(e){
		isQryButton = true;
		funQryFl();
	});
	//查询流程时限列表
	var funQryFl = function(pageIndex){
		var seletedFlow = $('#flowTree').tree('getSelected');
		var param = {
			packageDefineName:isQryButton?$('#processName').val():null,
			packageId:seletedFlow?seletedFlow.id:null,
			areaId:$('#areaTree').tree('getSelected').id,
			pageSize:pageSize,
			page:pageIndex||page
		}
		param = $.util.formatObj(param);
		var ret = $.callSyn('FlowLimitServ','qryFlowLimitByPackageDefine',param);
		if(typeof(pIndex)=='undefined'){//刷新分页信息
			pager.pagination('refresh',{pageNumber:1});
		}
		if(ret.total>0){
			$('#flowLimitTable').datagrid('loadData',ret);
		}else{
			$('#flowLimitTable').datagrid('loadData',{total: 0, rows:[]});//清空
		}
	}
	
	//选择流程弹出框
	$('#flowWin').dialog({
		onBeforeOpen:function(){//初始化之前的选择
			initFlowWinTree(flowTreeData);
			$('#flowWinTree').find('.tree-node.tree-node-selected').removeClass('tree-node-selected');
			$('#flowWin').panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
		}
	});
	var initFlowWinTree = function(flowTreeData){
		var subLoad = function(node){
				if(node.type==1&&!node.loaded){
					//加载环节列表
					var ret = $.callSyn('FlowServ','qryProcessDefineByCatalogId',{catalogId:node.id});
					node.loaded = true;
					$('#flowWinTree').tree('append', { parent : node.target,  data : ret });  
				}
		};
		$('#flowWinTree').tree({
			data : flowTreeData,
			onExpand:function(node){
				subLoad(node);
			},
			onClick:function(node){
				if(node.type==nodeType.CATALOG){
					subLoad(node);
				}
			}
		});
	};
	$('#flowWin-confirmBtn').click(function(e){
		var row = $('#flowWinTree').tree('getSelected');
		$('#packageName').textbox('setValue',row.text);
		$('#packageId').val(row.id);
		$('#flowWin').dialog('close');
	});
	// 增加流程时限
	$('#btnAddFl').click(function(){
		$('#fldlg').dialog({
			title: '增加流程时限',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#flowLimitForm').form('validate')){
						var packageId=$('#packageId').val();
						var isWorkTime = $('#isWorkTime').combobox('getValue');
						if(isWorkTime=='否'){
							isWorkTime = 0;
						}
						var timeUnit = $('#timeUnit').combobox('getValue');
						var areaId = $('#areaId').val();
						var params = {
							packageId:packageId,
							limitValue:$('#limitValue').val(),
							alertValue:$('#alertValue').val(),
							timeUnit:timeUnit,
							isWorkTime:isWorkTime,
							areaId:areaId
						};
						var ret = $.callSyn('FlowLimitServ','addFlowLimit',params);
						if(ret.isSuccess){
							$.messager.alert("提示","新增流程时限成功");
							funQryFl();
							$('#flowLimitTable').datagrid('highlightRow',0);//高亮
							$('#fldlg').dialog('close');
						}else{
							$.messager.alert("提示","新增流程时限失败");
						}
					}
				}
			},{
				text:'取消',
				handler:function(){
						$('#fldlg').dialog('close');
					}
				}],
			onOpen:function(){
				$('#fldlg').dialog('resize',{height:350});
				$("#fldlg").panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
			}
		});
		$('#fldlg').dialog('open');
	});
	var funModFl = function(){
		$('#fldlg').dialog({
			title: '修改流程时限',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#flowLimitForm').form('validate')){
						var packageId=$('#packageId').val();
						var isWorkTime = $('#isWorkTime').combobox('getValue');
						if(isWorkTime=='否'){
							isWorkTime = 0;
						}
						var timeUnit = $('#timeUnit').combobox('getValue');
						var areaId = $('#areaId').val();
						var params = {
							id:$('#flowLimitTable').datagrid('getSelected').id,
							packageId:packageId,
							limitValue:$('#limitValue').val(),
							alertValue:$('#alertValue').val(),
							timeUnit:timeUnit,
							isWorkTime:isWorkTime,
							areaId:areaId
						};
						var ret = $.callSyn('FlowLimitServ','modFlowLimit',params);
						if(ret.isSuccess){
							$.messager.alert("提示","修改流程时限成功");
							funQryFl();
							$('#flowLimitTable').datagrid('highlightRow',0);//高亮
							$('#fldlg').dialog('close');
						}else{
							$.messager.alert("提示","修改流程时限失败");
						}
					}
				}
			},{
				text:'取消',
				handler:function(){
						$('#fldlg').dialog('close');
					}
				}],
			onBeforeOpen:function(){
				var selectedItem = $('#flowLimitTable').datagrid('getSelected');
				if(selectedItem){
					$('#packageName').textbox('setText',selectedItem.processName);
					$('#packageId').val(selectedItem.packageId);
					$('#areaId').val(selectedItem.areaId);
					$('#area').combotree('setValue', selectedItem.areaName);
					$('#limitValue').textbox('setValue',selectedItem.limitValue);
					$('#alertValue').textbox('setValue',selectedItem.alertValue);
					$('#isWorkTime').combobox('select',selectedItem.isWorkTime);
					$('#timeUnit').combobox('select',selectedItem.timeUnit);
				}
			},
			onOpen:function(){
				$('#fldlg').dialog('resize',{height:350});
				$("#fldlg").panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
			}
		});
		$('#fldlg').dialog('open');
	};
	var funDelFl = function(){
		$.messager.confirm('提示', '确定删除该流程时限吗？', function(r){
			if (r){
				var ret = $.callSyn('FlowLimitServ','delFlowLimit',{id:$('#flowLimitTable').datagrid('getSelected').id});
				if(ret.isSuccess){
					$.messager.alert("提示","删除流程时限成功");
					funQryFl();
					$('#flowLimitTable').datagrid('highlightRow',0);//高亮
					$('#fldlg').dialog('close');
				}else{
					$.messager.alert("提示","删除流程时限失败");
				}
			}
		});
	};
});
