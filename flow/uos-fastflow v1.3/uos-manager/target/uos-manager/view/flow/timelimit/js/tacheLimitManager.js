$(function($){
	var session = $.session();
	var modType = false;
	var isQryButton = false;//是否点击查询按钮
	var page=1;
	var pageSize=10;
	var top = $(document).scrollTop() + ($(window).height()-350) * 0.5;
	var left = $(document).scrollLeft() + ($(window).width()-350) * 0.5;
	var nodeType = {
			CATALOG:"1",		//目录
			ELEMENT:"2",		//元素--环节或者流程
			VERSION:"3"			//版本--流程
		};
	var flowTreeData = $.callSyn("FlowServ","queryPackageCatalogByAreaIdAndSystemCode",{areaId: session["areaId"],systemCode:session["systemCode"]});
	
	//方法：初始化区域树
	var initAreaTree = function(){
		//areaId为-1加载全部
		var areaTreeData = $.callSyn("AreaServ","getAreaJsonTree",{areaId: session["areaId"]});
		$('#areaTree').tree({
			data : areaTreeData,
			onClick:function(node)
			{
//				funBtn($('#btnAddWt'),funAddWt,true);//激活修改按钮
//				funQryWt();
			},
			onContextMenu: function (e, node) {
                e.preventDefault();
            }
		});
	};
	//方法：初始化环节树
	var initTacheTree = function()
	{
		var tacheTree = $.callSyn('TacheServ','qryTacheCatalogTree',{systemCode:session["systemCode"]});
		$('#tacheTree').tree({
			data : tacheTree,
			onClick:function(node)
			{
				if(node.type==1&&!node.loaded)
				{
					//加载环节列表
					var ret = $.callSyn('TacheServ','qryTaches',{tacheCatalogId:node.id,state:'10A',currentDate:1});
					var taches = [];
					for(var i = 0;i<ret.rows.length;i++)
					{
						var row = ret.rows[i];
						var subNode = $('#tacheTree').tree('find', row.id); 
						if(subNode){
							continue;
						}
						taches.push({id:row.id,text:row.tacheName,code:row.tacheCode});
					}
					node.loaded = true;
					$('#tacheTree').tree('append', { parent : node.target,  data : taches });  
				}else{
					isQryButton = false;
					funQry();
				}
			},
			onContextMenu: function(e, node){
				e.preventDefault();
			}
		});
	};
	
	//初始化环节时限列表
	$('#tacheLimitTable').datagrid({
		rownumbers:false,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
	    striped:true,//奇偶行使用不同背景色
	    pagination:true,//
		pageSize:pageSize,//初始化页面尺寸	
	    toolbar: '#tacheLimitTb',
	    columns:[[
			{title:'环节名称',field:'tacheName',width:120,sortable:true},
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
			{title:'tacheId',field:'tacheId',hidden:true}
	    ]],
		onClickRow: function(index,row){//激活按钮+加载工作时间
			funBtn($('#btnMod'),funMod,true);//激活修改按钮
			funBtn($('#btnDel'),funDel,true);//激活删除按钮
			
			funBtn($('#btnAddRule'),funAddRule,true);//激活添加按钮
			funBtn($('#btnDelRule'),funDelRule,true);//激活删除按钮
			funQryRule();
		}
	});
	//分页处理
	var pager=$('#tacheLimitTable').datagrid('getPager');
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
	
	//初始化环节时限Rule列表
	$('#tacheLimitRuleTable').datagrid({
		rownumbers:false,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
	    striped:true,//奇偶行使用不同背景色
	    pagination:true,//
		pageSize:pageSize,//初始化页面尺寸	
	    toolbar: '#tacheLimitRuleTb',
	    columns:[[
			{title:'流程名称',field:'flowName',width:200},
			{title:'id',field:'id',hidden:true},
			{title:'packageId',field:'packageId',hidden:true},
			{title:'tacheLimitId',field:'tacheLimitId',hidden:true}
	    ]],
		onClickRow: function(index,row){//激活按钮+加载工作时间
			funBtn($('#btnAddRule'),funAddRule,true);//激活添加按钮
			funBtn($('#btnDelRule'),funDelRule,true);//激活删除按钮
		}
	});
	//分页处理
	var pagerRule=$('#tacheLimitRuleTable').datagrid('getPager');
	pagerRule.pagination({
		showPageList: false,
		total:0,//初始化展示分页数据
		onSelectPage:function(pageNumber, pageSize){
			funQryRule(pageNumber);
			pager.pagination('refresh',{
				pageNumber:pageNumber,
				pageSize:pageSize
			});
		}
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
	// 新增环节时限
	$('#dlg').dialog({
		title: '环节时限操作',
	    width: 350,
	    closed: true,
	    cache: false,
	    modal: true,
	    top: top, 
	    left: left
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
	$('#tName').textbox({
		readonly:true,
		icons: [{
			iconCls:'icon-search',
			handler: function(e){
				$('#tacheWin').dialog('open');
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
	initTacheTree();
	$('#btnAdd').linkbutton('enable');
//	funBtn($('#btnAdd'),funAdd,true);//激活增加按钮
	//界面初始化------end--------------
	//点击环节目录方法
	var funClickCatalogNode = function(){
//		funBtn($('#btnAdd'),funAdd,true);//激活增加按钮
		funBtn($('#btnMod'),funMod,false);//失效修改按钮
		funBtn($('#btnDel'),funDel,false);//失效删除按钮
		
		isQryButton = false;
		funQry();
	};
	$('#tacheName').textbox({
		prompt: '环节名称',
		icons:[{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).textbox('clear').textbox('textbox').focus();
			}
		}]
	});
	$('#btnQry').click(function(e){
		isQryButton = true;
		funQry();
	});
	var funQry = function(pIndex){
		var selectedItem = $('#tacheTree').tree('getSelected');
		var param = {
			tacheName:isQryButton ?$('#tacheName').val():null,
			tacheId:selectedItem&&!isQryButton?selectedItem.id:null,
			areaId:$('#areaTree').tree('getSelected').id,
			pageSize:pageSize,
			page:pIndex||page
		};
		param = $.util.formatObj(param);
		var ret = $.callSyn('TacheLimitServ','qryTacheLimitByTache',param);
		if(typeof(pIndex)=='undefined'){//刷新分页信息
			pager.pagination('refresh',{pageNumber:1});
		}
		if(ret.total>0){
			$('#tacheLimitTable').datagrid('loadData',ret);
		}else{
			$('#tacheLimitTable').datagrid('loadData',{total: 0, rows:[]});//清空
		}
	};
	//选择环节弹出框
	$('#tacheWin').dialog({
		onBeforeOpen:function(){//初始化之前的选择
			initTacheWinTree();
			$('#tacheWinTree').find('.tree-node.tree-node-selected').removeClass('tree-node-selected');
			$('#tacheWin').panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
		}
	});
	var initTacheWinTree = function(){
		var tacheTree = $.callSyn('TacheServ','qryTacheCatalogTree',{systemCode:session["systemCode"]});
		$('#tacheWinTree').tree({
			data : tacheTree,
			onClick:function(node)
			{
				if(node.type==1&&!node.loaded)
				{
					//加载环节列表
					var ret = $.callSyn('TacheServ','qryTaches',{tacheCatalogId:node.id,state:'10A',currentDate:1});
					var taches = [];
					for(var i = 0;i<ret.rows.length;i++)
					{
						var row = ret.rows[i];
						var subNode = $('#tacheWinTree').tree('find', row.id); 
						if(subNode){
							continue;
						}
						taches.push({id:row.id,text:row.tacheName,code:row.tacheCode});
					}
					node.loaded = true;
					$('#tacheWinTree').tree('append', { parent : node.target,  data : taches });  
				}
			},
			onContextMenu: function(e, node){
				e.preventDefault();
			}
		});
	};
	$('#tacheWin-confirmBtn').click(function(e){
		var row = $('#tacheWinTree').tree('getSelected');
		$('#tName').textbox('setValue',row.text);
		$('#tId').val(row.id);
		$('#tacheWin').dialog('close');
	});
	// 增加环节时限
	$('#btnAdd').click(function(){
		$('#dlg').dialog({
			title: '增加环节时限',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#tacheLimitForm').form('validate')){
						var tacheId=$('#tId').val();
						var isWorkTime = $('#isWorkTime').combobox('getValue');
						if(isWorkTime=='否'){
							isWorkTime = 0;
						}
						var timeUnit = $('#timeUnit').combobox('getValue');
						var areaId = $('#areaId').val();
						var params = {
							tacheId:tacheId,
							limitValue:$('#limitValue').val(),
							alertValue:$('#alertValue').val(),
							timeUnit:timeUnit,
							isWorkTime:isWorkTime,
							areaId:areaId
						};
						var ret = $.callSyn('TacheLimitServ','addTacheLimit',params);
						if(ret.isSuccess){
							$.messager.alert("提示","新增环节时限成功");
							funQry();
							$('#tacheLimitTable').datagrid('highlightRow',0);//高亮
							$('#dlg').dialog('close');
						}else{
							$.messager.alert("提示","新增环节时限失败");
						}
					}
				}
			},{
				text:'取消',
				handler:function(){
						$('#dlg').dialog('close');
					}
				}],
			onOpen:function(){
				$('#dlg').dialog('resize',{height:350});
				$("#dlg").panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
			}
		});
		$('#dlg').dialog('open');
	});
	var funMod = function(){
		$('#dlg').dialog({
			title: '修改环节时限',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#tacheLimitForm').form('validate')){
						var tacheId=$('#tId').val();
						var isWorkTime = $('#isWorkTime').combobox('getValue');
						if(isWorkTime=='否'){
							isWorkTime = 0;
						}
						var timeUnit = $('#timeUnit').combobox('getValue');
						var areaId = $('#areaId').val();
						var params = {
							id:$('#tacheLimitTable').datagrid('getSelected').id,
							tacheId:tacheId,
							limitValue:$('#limitValue').val(),
							alertValue:$('#alertValue').val(),
							timeUnit:timeUnit,
							isWorkTime:isWorkTime,
							areaId:areaId
						};
						var ret = $.callSyn('TacheLimitServ','modTacheLimit',params);
						if(ret.isSuccess){
							$.messager.alert("提示","修改环节时限成功");
							funQry();
							$('#tacheLimitTable').datagrid('highlightRow',0);//高亮
							$('#dlg').dialog('close');
						}else{
							$.messager.alert("提示","修改环节时限失败");
						}
					}
				}
			},{
				text:'取消',
				handler:function(){
						$('#dlg').dialog('close');
					}
				}],
			onBeforeOpen:function(){
				var selectedItem = $('#tacheLimitTable').datagrid('getSelected');
				if(selectedItem){
					$('#tName').textbox('setText',selectedItem.tacheName);
					$('#tId').val(selectedItem.tacheId);
					$('#areaId').val(selectedItem.areaId);
					$('#area').combotree('setValue', selectedItem.areaName);
					$('#limitValue').textbox('setValue',selectedItem.limitValue);
					$('#alertValue').textbox('setValue',selectedItem.alertValue);
					$('#isWorkTime').combobox('select',selectedItem.isWorkTime);
					$('#timeUnit').combobox('select',selectedItem.timeUnit);
				}
			},
			onOpen:function(){
				$('#dlg').dialog('resize',{height:350});
				$("#dlg").panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
			}
		});
		$('#dlg').dialog('open');
	};
	var funDel = function(){
		$.messager.confirm('提示', '确定删除该环节时限吗？', function(r){
			if (r){
				var ret = $.callSyn('TacheLimitServ','delTacheLimit',{id:$('#tacheLimitTable').datagrid('getSelected').id});
				if(ret.isSuccess){
					$.messager.alert("提示","删除环节时限成功");
					funQry();
					$('#tacheLimitTable').datagrid('highlightRow',0);//高亮
					$('#dlg').dialog('close');
				}else{
					$.messager.alert("提示","删除环节时限失败");
				}
			}
		});
	};
	var funQryRule = function(pIndex){
		var param = {
			tacheLimitId:$('#tacheLimitTable').datagrid('getSelected').id,
			page:pIndex||page,
			pageSize:pageSize
		}
		param = $.util.formatObj(param);
		var ret = $.callSyn('TacheLimitServ','qryTacheLimitRule',param);
		if(typeof(pIndex)=='undefined'){//刷新分页信息
			pagerRule.pagination('refresh',{pageNumber:1});
		}
		if(ret.total>0){
			$('#tacheLimitRuleTable').datagrid('loadData',ret);
		}else{
			$('#tacheLimitRuleTable').datagrid('loadData',{total: 0, rows:[]});//清空
		}
	};
	//选择流程弹出框
	$('#flowWin').dialog({
		onBeforeOpen:function(){//初始化之前的选择
			initFlowWinTree(flowTreeData);
			$('#flowWinTree').find('.tree-node.tree-node-selected').removeClass('tree-node-selected');
			//$('#flowWin').panel("move",{top:$(document).scrollTop() + ($(window).height()-350) * 0.5,left:$(document).scrollLeft() + ($(window).width()-350) * 0.5});
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
		var param = {
			packageId:row.id,
			tacheLimitId:$('#tacheLimitTable').datagrid('getSelected').id
		}
		var ret = $.callSyn('TacheLimitServ','addTacheLimitRule',param);
		if(ret.isSuccess){
			$.messager.alert("提示","添加环节时限适用规则成功");
			funQryRule();
			$('#flowWin').dialog('close');
		}else{
			$.messager.alert("提示","添加环节时限适用规则失败");
			$('#flowWin').dialog('close');
		}
	});
	var funAddRule = function(){
		$('#flowWin').dialog('open');
	};
	var funDelRule = function(){
		$.messager.confirm('提示', '确定删除该环节时限适用规则吗？', function(r){
			if (r){
				var ret = $.callSyn('TacheLimitServ','delTacheLimitRule',{id:$('#tacheLimitRuleTable').datagrid('getSelected').id});
				if(ret.isSuccess){
					$.messager.alert("提示","删除环节时限适用规则成功");
					funQryRule();
					$('#dlg').dialog('close');
				}else{
					$.messager.alert("提示","删除环节时限适用规则失败");
				}
			}
		});
	}
});
