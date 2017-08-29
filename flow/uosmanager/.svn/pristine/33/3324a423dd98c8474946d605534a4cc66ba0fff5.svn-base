$(function($) {
	/** 全局变量*/
	var page=1;
	var pageSize=10;
	var nodeType = {
		CATALOG:"1",		//目录
		REASON:"2",			//异常原因
		LOAD_CATALOG:"11",	//已加载旗下环节后的目录
		ELEMENT:"2",		//元素--环节或者流程
		VERSION:"3"			//版本--流程
	};
	var session = $.session();
	var isQryButton = false;//是否点击查询按钮
	
	/** 布局处理 */
	
	/** 目录区域 */
	var catalogDatas = $.callSyn('TacheServ','qryTacheCatalogTree',{systemCode:session["systemCode"]});
	$('#cTree').tree({
		data:catalogDatas,
		onClick: function(node){
			funClickCatalogNode();
		},
		onLoadSuccess:function(node,data){
			if($('#cTree').tree('getRoot')){
				$('#cTree').tree('expandAll',$('#cTree').tree('getRoot').target);
			}
		},
		onContextMenu: function(e,node){
			e.preventDefault();//该方法将通知 Web 浏览器不要执行与事件关联的默认动作
			$(this).tree('select',node.target);
			$('#mm').menu('show',{
				left: e.pageX,
				top: e.pageY
			});
			funClickCatalogNode();
		}
	});
	//目录区域的空白部分右键点击
	$('body').layout('panel','west').mousedown(function(e){
		if(e&&e.which==3&&(!$('#cTree').tree('getSelected'))){//1 左键，3右键
			$('#ms').menu('show',{
				left: e.pageX,
				top: e.pageY
			});
			e.stopPropagation();//  阻止事件冒泡
		}
	});

	/** 环节区域 */
	$('#tCode').textbox({
		prompt: '环节编码',
		icons:[{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).textbox('clear').textbox('textbox').focus();
			}
		}]
	});
	$('#tName').textbox({
		prompt: '环节名称',
		icons:[{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).textbox('clear').textbox('textbox').focus();
			}
		}]
	});
	$('#tacheTable').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
		pagination:true,//
		pageSize:pageSize,//初始化页面尺寸	
	    idField:'tacheCode',
	    fitColumns:true,
	    toolbar: '#tacheTb',
	    columns:[[
			{title:'编码',field:'tacheCode',width:180},
			{title:'名称',field:'tacheName',width:200},
			{title:'类型',field:'tacheType',width:180,
				formatter:function(value,row,index){
					if(value=='DUMMY'){
						return '数据驱动环节';
					}else if(value=='FLOW'){
						return '子流程环节';
					}else{
						return '普通环节';
					}
				}},
			{title:'是否自动回单',field:'isAuto',width:180,
				formatter:function(value,row,index){
					if(value=='1'){
						return '是';
					}else{
						return '否';
					}
				}},	
			{title:'子流程编码',field:'packageDefineCodes',width:200},
			{title:'生效时间',field:'effDate',width:200},
			{title:'失效时间',field:'expDate',width:200},
			{title:'目录',field:'tacheCatalog',width:200,
				formatter:function(value,row,index){
					var node = $('#cTree').tree('find', row.tacheCatalogId);//环节目录ID
					if(node){
						value = node.text; 
					}
					return value;
				}}
	    ]],
	    rowStyler: function(index,row){
			if (row.state=='10P'){//失效的记录
				return 'color:#99CC99;'; 
			}
		},
		onClickRow: function(index,row){//加载异常原因
			var ret = $.callSyn('ReturnReasonServ','qryTacheReturnReasons',{tacheId:row.id});
			$('#relationTable').datagrid('loadData',ret);
			$('#relationTable').datagrid('unselectAll');//取消之前的选择
			if (row.state=='10A'){//有效的记录
				funBtn($('#btnMod'),funMod,true);//激活环节修改按钮
				funBtn($('#btnAddRelation'),funAddRelation,true);
//				if(ret&&ret.total>0){
//					funBtn($('#btnDel'),funDel,false);//失效环节删除按钮
//				}else{
					funBtn($('#btnDel'),funDel,true);//激活环节删除按钮
//				}
			}else{
				funBtn($('#btnMod'),funMod,false);//失效环节修改按钮
				funBtn($('#btnDel'),funDel,false);//失效环节删除按钮
				funBtn($('#btnAddRelation'),funAddRelation,false);
			}
			funBtn($('#btnDelRelation'),funDelRelation,false);
			funBtn($('#btnModRelation '),funModRelation,false);//add for 882104
			funBtn($('#btnAdd'),funAdd,true);//激活环节增加按钮
			
			if(isQryButton){
				var node = $('#cTree').tree('find', row.tacheCatalogId);
				$('#cTree').tree('select',node.target);
			}
		}
	});
	//环节的分页处理
	var pager=$('#tacheTable').datagrid('getPager');
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
	//环节增改窗口
	$('#dlg').dialog({
		title: '环节操作',
	    width: 350,
//	    height: 250,
	    closed: true,
	    cache: false,
	    modal: true
	});
	//环节目录增改窗口
	$('#dlg2').dialog({
		title: '环节目录操作',
	    width: 350,
	    height: 140,
	    closed: true,
	    cache: false,
	    modal: true
	});
	
	/** 关联异常原因区域 */
	$('#main').layout('panel','south').panel('resize',{height:$('#main').height()*0.4});
	$('#main').layout('resize');//根据浏览器窗口动态初始化调整关联区域大小
	$('#relationTable').datagrid({
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,
		//idField:'RETURN_REASON_NAME',
	   	fitColumns:true,
	    toolbar: '#relationTb',
	   	pagination:true,
		pageList:[5],//必须先设置这个，才能改pageSize，否则是NaN
		pageSize:5,//初始化页面尺寸
	    columns:[[
			{title:'名称',field:'returnReasonName',width:100},
			{title:'区域',field:'areaName',width:80},
			{title:'异常原因类别',field:'reasonType',width:100,
				formatter:function(value,row,index){
					var value_=value;
					if(value=="10R"){
						value_="退单";
					}else if(value=="10W"){
						value_="待装";
					}else if(value=="10C"){
						value_="撤单";
					}else if(value=="10Q"){
						value_="改单";
					}else if(value=="10P"){
						value_="缓装";
					}
					return value_;
				}},
			{title:'状态',field:'state',width:80,
				formatter:function(value,row,index){
					if(value=="10A"){
						return "启用";
					}else{
						return "停用";
					}
				}},// add for 882104
			{title:'是否需要审核',field:'auditFlag',width:80,
				formatter:function(value,row,index){
					if(value=="1"){
						return "是";
					}else{
						return "否";
					}
				}}
	    ]],
		onClickRow: function(index,row){
			funBtn($('#btnDelRelation'),funDelRelation,true);//激活异常原因关联环节删除按钮
			funBtn($('#btnModRelation'),funModRelation,true);//激活异常原因关联环节修改按钮 add for 882104
		},
		loadFilter: function(data){//页面端分页
			var dg = $(this);
			var opts = dg.datagrid('options');
			var pager = dg.datagrid('getPager');
			pager.pagination({
				showPageList: false,
				onSelectPage:function(pageNum, pageSize){
					opts.pageNumber = pageNum;
					opts.pageSize = pageSize;
					pager.pagination('refresh',{
						pageNumber:pageNum,
						pageSize:pageSize
					});
					dg.datagrid('loadData',data);
				}
			});
			if (!data.originalRows){
				data.originalRows = (data.rows);
			}
			var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
			var end = start + parseInt(opts.pageSize);
			data.rows = (data.originalRows.slice(start, end));
			return data;
		}	
	});
	$('#relationTable').datagrid('getPager').pagination({
		showPageList: false
	});//初始设置不显示pageList
	
	/** 方法 */
	//点击环节目录方法
	var funClickCatalogNode = function(){
		funBtn($('#btnAdd'),funAdd,true);//激活环节增加按钮
		funBtn($('#btnMod'),funMod,false);//失效环节修改按钮
		funBtn($('#btnDel'),funDel,false);//失效环节删除按钮
		funBtn($('#btnAddRelation'),funAddRelation,false);
		funBtn($('#btnDelRelation'),funDelRelation,false);
		funBtn($('#btnModRelation '),funModRelation,false);//add for 882104
		
		isQryButton = false;
		funQry();
	};
	//点击查询按钮
	var funClickQryBtn = function(){
		$('.tree-node.tree-node-selected').removeClass('tree-node-selected');//去掉cTree的选择
		funBtn($('#btnAdd'),funAdd,false);//失效环节增加按钮
		funBtn($('#btnMod'),funMod,false);//失效环节修改按钮
		funBtn($('#btnDel'),funDel,false);//失效环节删除按钮
		funBtn($('#btnAddRelation'),funAddRelation,false);
		funBtn($('#btnDelRelation'),funDelRelation,false);
		funBtn($('#btnModRelation '),funModRelation,false);//add for 882104
		isQryButton = true;
		funQry();
	}
	//得到查询条件
	var funGetConditionObj = function(pIndex){
		//获取选中的状态
		var value = $('#tState').combobox('getValue');
		var state;
		if(value=='enabled'){
			state='10A';
		}else if(value=='disable'){
			state='10P';
		}
		var params = {
			tacheCatalogId:isQryButton?null:$('#cTree').tree('getSelected').id,
			state:state,
			tacheCode:$('#tCode').val(),
			tacheName:$('#tName').val(),
			page:pIndex||page,
			pageSize:pageSize
		};
		return $.util.formatObj(params);
	};
	//查询方法
	var funQry = function(pIndex){
		var params = funGetConditionObj(pIndex);
		var ret = $.callSyn('TacheServ','qryTaches',params);
		if(typeof(pIndex)=='undefined'){//刷新分页信息
			pager.pagination('refresh',{pageNumber:1});
		}
		$('#tacheTable').datagrid('loadData',ret);
		
		$('#tacheTable').datagrid('unselectAll');//取消之前的选择
		$('#relationTable').datagrid('loadData',{total: 0, rows:[]});//清空
	};
	//增加环节
	var funAdd = function(){
		$('#dlg').dialog({
			title: '增加环节',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#tacheForm').form('validate')){
						var tacheCatalogId = $('#cTree').tree('getSelected').id;
						var tacheType = $('#tacheType').combobox('getValue');
						if(tacheType=='普通环节'){//默认不操作的话，getValue获取到的是value，选择的话，获取到的是label
							tacheType = '';
						}else if(tacheType=='FLOW'){
							if($.trim($('#subFlowNames').textbox('getText')).length==0){
								$.messager.alert("提示","请选择子流程");
								return;
							}
						}
						var packageDefineCodes=[];
						$.each($('#subFlowNames').data('rows'),function(i,n){
							packageDefineCodes.push(n.code);
						});
						var isAuto = $('#isAuto').combobox('getValue');
						if(isAuto=='否'){
							isAuto = 0;
						}
						var effDate = $('#effDate').datetimebox('getValue');
						var expDate = $('#expDate').datetimebox('getValue');
						var currentDate = new Date();
						
						if(effDate!=''){
							if(StringToDate(effDate).getTime() > currentDate.getTime()){
								$.messager.alert("提示","生效效时间不能晚于当前时间");
								return;
							}
						}
						if(expDate!=''){
							if(StringToDate(expDate).getTime() < currentDate.getTime()){
								$.messager.alert("提示","失效时间必须晚于当前时间");
								return;
							}
						}
						
						if((effDate!='') && (expDate !='')){
							if(StringToDate(effDate).getTime() >= StringToDate(expDate).getTime()){
								$.messager.alert("提示","生效时间必须早于失效时间");
								return;
							}
						}

						var params = {
							tacheCatalogId:tacheCatalogId,
							tacheCode:$('#tacheCode').val(),//.textbox('getValue')
							tacheName:$('#tacheName').val(),
							tacheType:tacheType,
							isAuto:isAuto,
							effDate:effDate,
							expDate:expDate,
							packageDefineCodes:packageDefineCodes.join()
						};
						var ret = $.callSyn('TacheServ','addTache',params);
						if(ret.id){
							if(ret.id =='-1'){
								$.messager.alert("提示","环节编码重复");
							}else{
								$.messager.alert("提示","新增环节成功");
								var node = $('#cTree').tree('find', tacheCatalogId);
								$('#cTree').tree('select', node.target);
								funClickCatalogNode();
								$('#tacheTable').datagrid('highlightRow',0);//高亮
								$('#dlg').dialog('close');
							}
						}else{
							$.messager.alert("提示","新增环节失败");
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
				if($('#tacheTable').datagrid('getSelected')){
					var sTache = $('#tacheTable').datagrid('getSelected');
					$('#tacheCode').textbox('setValue',sTache.tacheCode).textbox('enable');
					$('#tacheName').textbox('setValue',sTache.tacheName);
					$('#tacheCatalogName').combotree('setValue', $('#cTree').tree('getSelected').text);
					$('#tCatalogForm').show();
					$('#tacheType').combobox('setValue',sTache.tacheType||'').combobox('enable');
					$('#isAuto').combobox('select',sTache.isAuto);
					$('#effDate').datetimebox('setValue',sTache.effDate);
					$('#expDate').datetimebox('setValue',sTache.expDate);
					if(sTache.tacheType=='FLOW'){
						var ret =  $.callSyn('FlowServ','findProcessDefinitionsByCodes',{packageDefineCodes:sTache.packageDefineCodes});
						var rows = [];
						var names =[];
						$.each(ret,function(i,n){
							var obj = {
								id:n.packageDefineId,
								name:n.name,
								code:n.packageDefineCode,
								version:n.version+'('+n.stateName+')'
							};
							rows.push(obj);
							names.push(n.name)
						});
						$('#subFlowNames').data('rows',rows);
						$('#subFlowNames').textbox('setText',names.join());
						$('#subFlow').show();
					}else{
						$('#subFlow').hide();
						$('#subFlowNames').textbox('clear');
						$('#subFlowNames').data('rows',[]);
					}
				}else{
					$('#tacheCode').textbox('clear').textbox('enable');
					$('#tacheName').textbox('clear');
					$('#tacheCatalogName').combotree('setValue', $('#cTree').tree('getSelected').text);//为了通过validate验证
					$('#tCatalogForm').hide();
					$('#tacheType').combobox('enable');
					//tacheType选中第一个值
					var tacheTypeData = $('#tacheType').combobox('getData');
					if (tacheTypeData.length > 0) {
						$('#tacheType').combobox('select', tacheTypeData[0].value);
					}
					$('#isAuto').combobox('select','否');
					$('#subFlow').hide();
					$('#subFlowNames').textbox('clear');
					$('#subFlowNames').data('rows',[]);
					$('#effDate').datetimebox('clear');
					$('#expDate').datetimebox('clear');
				}
			},
			onOpen:function(){
				$('#dlg').dialog('resize',{height:350});
			}
		});
		$('#dlg').dialog('open');
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
	}
	//修改环节--目录和名称
	var funMod = function(){
		$('#dlg').dialog({
			title: '修改环节',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#tacheForm').form('validate')){
						var sTreeNode = $('#tacheCatalogName').combotree('tree').tree('getSelected');
						var tacheCatalogId = $('#cTree').tree('getSelected').id;
						if(sTreeNode){
							tacheCatalogId = sTreeNode.id;
						}
						if($('#tacheType').combobox('getValue')=='FLOW'){
							if($.trim($('#subFlowNames').textbox('getText')).length==0){
								$.messager.alert("提示","请选择子流程");
								return;
							}
						}
						var packageDefineCodes=[];
						$.each($('#subFlowNames').data('rows'),function(i,n){
							packageDefineCodes.push(n.code);
						});
						var isAuto = $('#isAuto').combobox('getValue');
						if(isAuto=='否'){
							isAuto = 0;
						}
						
						var effDate = $('#effDate').datetimebox('getValue');
						var expDate = $('#expDate').datetimebox('getValue');
						var currentDate = new Date();
						
						if(effDate!=''){
							if(StringToDate(effDate).getTime() > currentDate.getTime()){
								$.messager.alert("提示","生效效时间不能晚于当前时间");
								return;
							}
						}
						
						if(expDate!=''){
							if(StringToDate(expDate).getTime() < currentDate.getTime()){
								$.messager.alert("提示","失效时间必须晚于当前时间");
								return;
							}
						}
						
						if((effDate!='') && (expDate !='')){
							if(StringToDate(effDate).getTime() >= StringToDate(expDate).getTime()){
								$.messager.alert("提示","生效时间必须早于失效时间");
								return;
							}
						}
						
						var params = {
							id:$('#tacheTable').datagrid('getSelected').id,
							tacheCatalogId:tacheCatalogId,
							tacheName:$('#tacheName').val(),
							isAuto:isAuto,
							effDate:effDate,
							expDate:expDate,
							packageDefineCodes:packageDefineCodes.join()
						};
						var ret = $.callSyn('TacheServ','modTache',params);
						if(ret.isSuccess){
							$.messager.alert("提示","修改环节成功");
							var node = $('#cTree').tree('find', tacheCatalogId);
							$('#cTree').tree('select', node.target);
							funClickCatalogNode();
							$('#tacheTable').datagrid('highlightRow',0);//高亮
							$('#dlg').dialog('close');
						}else{
							$.messager.alert("提示","修改环节失败");
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
				var sTache = $('#tacheTable').datagrid('getSelected');
				$('#tacheCode').textbox('setValue',sTache.tacheCode).textbox('disable');//.textbox('readonly',true);
				$('#tacheName').textbox('setValue',sTache.tacheName);
				$('#tacheCatalogName').combotree('setValue', $('#cTree').tree('getSelected').text);
				$('#tCatalogForm').show();
				$('#tacheType').combobox('setValue',sTache.tacheType||'').combobox('disable');
				$('#isAuto').combobox('select',sTache.isAuto);
				$('#effDate').datetimebox('setValue',sTache.effDate);
				$('#expDate').datetimebox('setValue',sTache.expDate);
				if(sTache.tacheType=='FLOW'){
					var ret =  $.callSyn('FlowServ','findProcessDefinitionsByCodes',{packageDefineCodes:sTache.packageDefineCodes});
					var rows = [];
					var names =[];
					$.each(ret,function(i,n){
						var obj = {
							id:n.packageDefineId,
							name:n.name,
							code:n.packageDefineCode,
							version:n.version+'('+n.stateName+')'
						};
						rows.push(obj);
						names.push(n.name)
					});
					$('#subFlowNames').data('rows',rows);
					$('#subFlowNames').textbox('setText',names.join());
					$('#subFlow').show();
				}else{
					$('#subFlow').hide();
					$('#subFlowNames').textbox('clear');
					$('#subFlowNames').data('rows',[]);
				}
			},
			onOpen:function(){//放在onBeforeOpen里resize，会导致两次执行onBeforeOpen方法，导致窗口大小不正确。所以初始化窗口大小放在onOpen里。
				if($('#tacheTable').datagrid('getSelected').tacheType=='FLOW'){
					$('#dlg').dialog('resize',{height:350});
				}else{
					$('#dlg').dialog('resize',{height:350});
				}
			}
		});
		$('#dlg').dialog('open');
	};
	//删除环节
	var funDel = function(){
		$.messager.confirm('提示', '确定删除该环节吗？', function(r){
			if (r){
				var result = $.callSyn('ReturnReasonServ','hasActiveReturnReasonsByTacheId',{tacheId:$('#tacheTable').datagrid('getSelected').id});
				if(result.isHas){
					$.messager.alert("提示","该环节存在启用状态的异常原因，请先禁用异常原因！");
					return;
				}
				var ret = $.callSyn('TacheServ','delTache',{id:$('#tacheTable').datagrid('getSelected').id});
				if(ret.isSuccess){
					$.messager.alert("提示","删除环节成功");
					//$('#tacheTable').datagrid('deleteRow',$('#tacheTable').datagrid('getRowIndex', $("#tacheTable").datagrid('getSelected')));//删除
					var tacheCatalogId = $('#cTree').tree('getSelected').id;
					var node = $('#cTree').tree('find', tacheCatalogId);
					$('#cTree').tree('select', node.target);
					funClickCatalogNode();
					$('#tacheTable').datagrid('highlightRow',0);//高亮
					$('#dlg').dialog('close');
				}else{
					$.messager.alert("提示","删除环节失败");
				}
			}
		});
	};
	//easyui-linkbutton 点击问题(disable无法去除点击事件)
	var funBtn = function(btn, fun, enable){
		if(enable){
			btn.unbind('click').bind('click',fun);//每次绑定前先取消上次的绑定
			btn.linkbutton('enable');//激活
		}else{
			btn.unbind('click',fun);
			btn.linkbutton('disable');//失效
		}
	}
	//增加环节目录
	var funAddCatalog = function(){
		$('#dlg2').dialog({
			title: '增加环节目录',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#catalogName').textbox('isValid')){
						var sNode =$('#cTree').tree('getSelected');
						var pCatalogId;
						var systemCode = session["systemCode"];
						if(sNode&&($('#cTree').tree('getParent',sNode.target))){
							pCatalogId = $('#cTree').tree('getParent',sNode.target).id;
							systemCode = sNode.systemCode;
						}else{
							pCatalogId = -1;
						}
						var params = {
							tacheCatalogName:$('#catalogName').val(),
							parentTacheCatalogId:pCatalogId,
							systemCode:systemCode
						};
						var ret = $.callSyn('TacheServ','addTacheCatalog',params);
						if(ret.catalogId){
							$.messager.alert("提示","新增环节目录成功");
							if(sNode){
								$('#cTree').tree('insert',{after:sNode.target,data:{id:ret.catalogId,text:params.tacheCatalogName,type:nodeType.CATALOG,systemCode:params.systemCode}});
							}else{
								$('#cTree').tree('append',{data:[{id:ret.catalogId,text:params.tacheCatalogName,type:nodeType.CATALOG,systemCode:params.systemCode}]});
							}
							$('#cTree').tree('select', $('#cTree').tree('find', ret.catalogId).target);//选中新增node
							funClickCatalogNode();
							$('#dlg2').dialog('close');
						}else{
							$.messager.alert("提示","新增环节目录失败");
						}
					}else{
						$.messager.alert("提示","请输入环节目录");
					}
				}
			},{
				text:'取消',
				handler:function(){
					$('#dlg2').dialog('close');
				}
				}],
			onBeforeOpen:function(){
				$('#catalogName').textbox('clear');
			}
		});
		$('#dlg2').dialog('open');
	};
	//修改环节目录
	var funModCatalog = function(){
		$('#dlg2').dialog({
			title: '修改环节目录',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#catalogName').textbox('isValid')){
						var sNode =$('#cTree').tree('getSelected');
						var params = {
							tacheCatalogName:$('#catalogName').val(),
							id:sNode.id
						};
						var ret = $.callSyn('TacheServ','modTacheCatalog',params);
						if(ret.isSuccess){
							$.messager.alert("提示","修改环节目录成功");
							$('#cTree').tree('update',{target:sNode.target,text:params.tacheCatalogName});
							$('#dlg2').dialog('close');
						}else{
							$.messager.alert("提示","修改环节目录失败");
						}
					}else{
						$.messager.alert("提示","请输入环节目录");
					}
				}
			},{
				text:'取消',
				handler:function(){
					$('#dlg2').dialog('close');
				}
				}],
			onBeforeOpen:function(){
				$('#catalogName').textbox('setValue',$('#cTree').tree('getSelected').text);
			}
		});
		$('#dlg2').dialog('open');
	};
	//删除环节目录
	var funDelCatalog = function(){
		var cNodes = $('#cTree').tree('getChildren',$('#cTree').tree('getSelected').target);
		if(cNodes.length>0){
			$.messager.alert("提示","该目录下存在子目录，无法删除");
		}else{
			$.messager.confirm('提示', '确定删除该环节目录吗？', function(r){
				if (r){
					var ret = $.callSyn('TacheServ','delTacheCatalog',{id:$('#cTree').tree('getSelected').id});
					if(ret.isSuccess){
						$.messager.alert("提示","删除环节目录成功");
						$('#cTree').tree('remove',$('#cTree').tree('getSelected').target);//删除
					}else{
						$.messager.alert("提示","删除环节目录失败");
					}
					
				}
			});
		}
	};
	//增加环节子目录
	var funAddSubCatalog = function(){
		$('#dlg2').dialog({
			title: '增加环节子目录',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#catalogName').textbox('isValid')){
						var sNode =$('#cTree').tree('getSelected');
						var params = {
							tacheCatalogName:$('#catalogName').val(),
							parentTacheCatalogId:sNode.id,
							systemCode:sNode.systemCode
						};
						var ret = $.callSyn('TacheServ','addTacheCatalog',params);
						if(ret.catalogId){
							$.messager.alert("提示","新增环节目录成功");
							$('#cTree').tree('append',{parent:sNode.target,data:[{id:ret.catalogId,text:params.tacheCatalogName,type:nodeType.CATALOG,systemCode:params.systemCode}]});
							$('#cTree').tree('select', $('#cTree').tree('find', ret.catalogId).target);//选中新增node
							funClickCatalogNode();
							$('#dlg2').dialog('close');
						}else{
							$.messager.alert("提示","新增环节目录失败");
						}
					}else{
						$.messager.alert("提示","请输入环节目录");
					}
				}
			},{
				text:'取消',
				handler:function(){
					$('#dlg2').dialog('close');
				}
			}],
			onBeforeOpen:function(){
				$('#catalogName').textbox('clear');
			}
		});
		$('#dlg2').dialog('open');
	};
	
	/** 绑定/加载 */
	$('#btnQry').bind('click',funClickQryBtn);
	$('#btnAddCatalogs').bind('click',funAddCatalog);
	$('#btnAddCatalog').bind('click',funAddCatalog);
	$('#btnModCatalog').bind('click',funModCatalog);
	$('#btnDelCatalog').bind('click',funDelCatalog);
	$('#btnAddSubCatalog').bind('click',funAddSubCatalog);
	
	/** 对话框 */
	$('#tacheType').combobox({
		required:true,
		editable:false,
		panelHeight:'auto',
		valueField: 'label',
		textField: 'value',
		data: [{
			label: '',//NORMAL
			value: '普通环节'
		},{
			label: 'DUMMY',
			value: '数据驱动环节'
		},{
			label:'FLOW',
			value:'子流程环节'
		}],
		onSelect:function(record){
			if(record){
				if(record.label=='FLOW'){
					$('#dlg').dialog('resize',{height:350});
					$('#subFlow').show();
				}else{
					$('#subFlow').hide();
					$('#subFlowNames').textbox('clear');
					$('#subFlowNames').data('rows',[]);
					$('#dlg').dialog('resize',{height:350});
				}
			}
		}
	});
	$('#subFlowNames').textbox({
		readonly:true,
		icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				$('#flowWin').dialog('open');
			}
		}]
	});
	$('#subFlow').find('a.textbox-icon.icon-tip.textbox-icon-disabled').removeClass("textbox-icon-disabled");//使添加Tip有效
	$('#isAuto').combobox({
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
	//对话框中选择目录
	$('#tacheCatalogName').combotree({
		panelHeight:88,
		onShowPanel: function(){
			var datas = $.callSyn('TacheServ','qryTacheCatalogTree',{systemCode:session["systemCode"]});
			$('#tacheCatalogName').combotree('loadData',datas);
			var dTree = $('#tacheCatalogName').combotree('tree');
			dTree.tree('expandAll',dTree.tree('getRoot').target);
		}
	});
	/**环节和异常原因的关联关系*/
	//窗口定义
	$('#dlg3').dialog({
		title: '关联操作',
	    width: 400,
	    height: 420,
	    closed: true,
	    cache: false,
	    modal: true
	});
	//窗口view
	var reasonCatalogDatas = $.callSyn('ReturnReasonServ','qryReturnReasonCatalogTree',{systemCode:session["systemCode"]});
	$('#reasonCatalogTree').tree({
		data:reasonCatalogDatas,
		checkbox:true,
		onClick: function(node){
			if(node.type==nodeType.CATALOG){
				loadReasonsOfCatalog(node);
			}
		},
		onExpand:function(node){
			if(node.type==nodeType.CATALOG){
				loadReasonsOfCatalog(node);
			}
		},
		onBeforeCheck:function(node, checked){
			if(checked&&node.type==nodeType.CATALOG){
				loadReasonsOfCatalog(node);
			}
		}
	});
	$('#reasonNameR').searchbox({
	    searcher:function(value){
	    	if(value&&value!=''){
				var reasonDatas = $.callSyn('ReturnReasonServ','qryReturnReasons',{state:'10A',returnReasonName:value});
				if(reasonDatas.total>0){
					var sItem = $('#reasonCatalogTree').tree('getSelected');
					var findReasonCatalogId = reasonDatas.rows[0].reasonCatalogId;
					var findId = reasonDatas.rows[0].id;
					if(sItem){
						var i=0,len = reasonDatas.rows.length;
						for(;i<len;i++){
							var tr = reasonDatas.rows[i].reasonCatalogId+"-"+reasonDatas.rows[i].id;
							if(tr==sItem.id){
								var j = (i+1==len)? 0:(i+1);
								findReasonCatalogId = reasonDatas.rows[j].reasonCatalogId;
								findId = reasonDatas.rows[j].id;
								break;
							}
						}
					}
					var catalogNode = $('#reasonCatalogTree').tree('find', findReasonCatalogId);
					if(catalogNode.type==nodeType.CATALOG){
						loadReasonsOfCatalog(catalogNode);
					}
					var reasonTreeId = findReasonCatalogId+"-"+findId;
					var sNode = $('#reasonCatalogTree').tree('find', reasonTreeId);
					$('#reasonCatalogTree').tree('select', sNode.target).tree('expandTo', sNode.target);
				}
	    	}else{
	    		$.messager.alert("提示","模糊查询不能为空");
	    	}
	    },
	    prompt:'异常原因名称'
	});
	//区域
	var areaTreeData = $.callSyn("AreaServ","getAreaJsonTree",{areaId: session["areaId"]});
	$('#areaR').combotree({
		panelHeight:115,
		onShowPanel: function(){
			$('#areaR').combotree('loadData',areaTreeData);
			var dTree = $('#areaR').combotree('tree');
			dTree.tree('expand',dTree.tree('getRoot').target);
			dTree.tree({
				onClick:function(node){
					$('#areaIdR').val(node.id);
					$('#areaR').combotree('setValue',node.text);
					$('#areaR').combotree('hidePanel');
				}
			});
		}
	});
	$('#auditR').combobox({
		required:true,
		editable:false,
		panelHeight:'auto',
		valueField: 'label',
		textField: 'value',
		data: [{
			label: '0',
			value: '否'
		},{
			label: '1',
			value: '是'
		}]
	});
	//add by che.zi 20160628 fro zmp:882104 begin
	$('#stateR').combobox({
		required:true,
		editable:false,
		panelHeight:'auto',
		valueField: 'label',
		textField: 'value',
		data: [{
			label: '10A',
			value: '启用'
		},{
			label: '10X',
			value: '停用'
		}]
	});
	//方法——修改异常原因适配环节
	var funModRelation = function(){
		$('#dlg3').dialog({
			title: '启用环节的异常原因',
		    height: 200,
			buttons:[{
				text:'确定',
				handler:function(){
					var sTache = $('#tacheTable').datagrid('getSelected');
					var params = {
						tacheId:sTache.id,
						returnReasonId:$('#relationTable').datagrid('getSelected').returnReasonId,
						state:$('#stateR').combobox('getValue'),
						areaId:$('#areaIdR').val()
					};
					var ret = $.callSyn('ReturnReasonServ','modTacheReturnReason',params);
					if(ret.isSuccess){
						$.messager.alert("提示","启用环节的异常原因成功");
						var ret = $.callSyn('ReturnReasonServ','qryTacheReturnReasons',{tacheId:sTache.id});
						$('#relationTable').datagrid('loadData',ret);
						funBtn($('#btnDelRelation'),funDelRelation,false);//失效异常原因关联环节删除按钮
						funBtn($('#btnModRelation '),funModRelation,false);//add for 882104
						$('#dlg3').dialog('close');
					}else{
						$.messager.alert("提示","启用环节的异常原因失败");
					}
				}
			},{
				text:'取消',
				handler:function(){
						$('#dlg3').dialog('close');
					}
				}],
			onBeforeOpen:function(){
				var relation = $('#relationTable').datagrid('getSelected');
				$('#reasonCatalogForm').hide();
				$('#reasonNameRForm').hide();
				$('#areaRForm').hide();
				$('#auditRTr').hide();
				$('#areaIdR').val(relation.areaId);
				$('#auditR').combobox('setValue',relation.auditR||'').combobox('disable');
				$('#stateR').combobox('setValue',relation.state||'');
				$('#reasonCatalogTree').tree('getChecked','');
				$('#reasonNameR').searchbox('setValue','');
				$('#areaIdR').val(relation.areaId);
			}
		});
		$('#dlg3').dialog('open');
	};
	//882104 end
	//方法——增加异常原因适配环节
	var funAddRelation = function(){
		$('#dlg3').dialog({
			title: '增加环节的异常原因',
		    height: 430,
			buttons:[{
				text:'确定',
				handler:function(){
					var sTacheNodes = $('#reasonCatalogTree').tree('getChecked');
					if((sTacheNodes.length>0)&&($('#relationForm').form('validate'))){
						var sTache = $('#tacheTable').datagrid('getSelected');
						var reasonIds = new Array();
						$.each(sTacheNodes, function(i, n){
							if(n.type==nodeType.REASON){
								reasonIds.push(n.id.split("-")[1]);//取出异常原因
							}
						});
						var params = {
							tacheId:sTache.id,
							returnReasonIds:reasonIds,
							audiFlag:$('#auditR').combobox('getValue'),
							state:$('#stateR').combobox('getValue'),
							areaId:$('#areaIdR').val()
						};
						//判断是否已配置  -- 后端查询的数据是所有的数据，只在前端页面分页展示的。。
//						var rows = $('#relationTable').datagrid('getRows');
						var rows = $('#relationTable').datagrid('getData').originalRows;
						var isHas = false;
						if(rows){
							$.each(rows,function(i,n){
								if(n.areaId==params.areaId&&n.tacheId==params.tacheId){
									$.each(params.returnReasonIds,function(j,m){
										if(m==n.returnReasonId){
											isHas = true;
										}
									});
								}
							});
						}
						if(isHas){
							$.messager.alert("提示","已存在相同配置的环节异常原因");
						}else{
							var ret = $.callSyn('ReturnReasonServ','addTacheReturnReason',params);
							if(ret.isSuccess){
								$.messager.alert("提示","新增环节的异常原因成功");
								var ret = $.callSyn('ReturnReasonServ','qryTacheReturnReasons',{tacheId:sTache.id});
								$('#relationTable').datagrid('loadData',ret);
								funBtn($('#btnDelRelation'),funDelRelation,false);//失效异常原因关联环节删除按钮
								funBtn($('#btnModRelation '),funModRelation,false);//add for 882104
								$('#dlg3').dialog('close');
							}else{
								$.messager.alert("提示","新增环节的异常原因失败");
							}
						}
					}
				}
			},{
				text:'取消',
				handler:function(){
						$('#dlg3').dialog('close');
					}
				}],
			onBeforeOpen:function(){
				$('#reasonCatalogForm').show();
				$('#reasonNameRForm').show();
				$('#areaRForm').show();
				$('#auditRTr').show();
				$.each($('#reasonCatalogTree').tree('getRoots'), function(i, n){//循环遍历
					$('#reasonCatalogTree').tree('collapseAll',n.target);//初始化关闭
					if(i==0){
						$('#reasonCatalogTree').tree('select',n.target);//切换选择成第一个根节点
						$(n.target).removeClass("tree-node-selected");//并去掉选择
					}
				});
				$.each($('#reasonCatalogTree').tree('getChecked'), function(i, n){//取消勾选的项
					$('#reasonCatalogTree').tree('uncheck',n.target);
				});
				
				$('#reasonNameR').searchbox('setValue','');
				$('#areaIdR').val('');
				$('#areaR').combotree('clear');
				var auditData = $('#auditR').combobox('getData');
				if (auditData.length > 0) {
					$('#auditR').combobox('select', auditData[0].label);
				}
				var stateData = $('#stateR').combobox('getData');
				if (stateData.length > 0) {
					$('#stateR').combobox('select', stateData[0].label);
				}
			}
		});
		$('#dlg3').dialog('open');
	};
	//删除环节的异常原因
	var funDelRelation = function(){
		$.messager.confirm('提示', '确定禁用该环节的异常原因吗？', function(r){
			if (r){
				var params = {
					returnReasonId:$('#relationTable').datagrid('getSelected').returnReasonId,
					tacheId:$('#relationTable').datagrid('getSelected').tacheId,
					areaId:$('#relationTable').datagrid('getSelected').areaId
				};
				var ret = $.callSyn('ReturnReasonServ','delTacheReturnReason',params);
				if(ret.isSuccess){
					$.messager.alert("提示","禁用环节的异常原因成功");
					//$('#relationTable').datagrid('deleteRow',$('#relationTable').datagrid('getRowIndex', $("#relationTable").datagrid('getSelected')));//删除
					//因为页面端分页，所以需要重新加载。
					var arr = $.callSyn('ReturnReasonServ','qryTacheReturnReasons',{tacheId:$('#tacheTable').datagrid('getSelected').id});
					$('#relationTable').datagrid('loadData',arr);
					$('#relationTable').datagrid('unselectAll');//取消之前的选择
					funBtn($('#btnDelRelation'),funDelRelation,false);//失效异常原因关联环节删除按钮
					funBtn($('#btnModRelation '),funModRelation,false);//add for 882104
				}else{
					$.messager.alert("提示","禁用环节的异常原因失败");
				}
				
			}
		});
	};
	//方法——加载异常原因目录tree旗下的异常原因节点
	var loadReasonsOfCatalog = function(node){
		var reasonDatas = $.callSyn('ReturnReasonServ','qryReturnReasons', {state:'10A',reasonCatalogId:node.id});
		var datas = new Array();
		if(reasonDatas.total>0){
			$.each(reasonDatas.rows, function(i, n){
				var reason = {
					id:node.id+"-"+n.id,//catalogId-reasonId
					text:n.returnReasonName,
					type:nodeType.REASON
				}
				datas.push(reason);
			});
		}
		$('#reasonCatalogTree').tree('append',{parent:node.target,data:datas});
		node.type=nodeType.LOAD_CATALOG;//标识已经加载过了
	};
	
	//子流程
	$('#flowWin').dialog({
		onBeforeOpen:function(){//初始化之前的选择
			var rows=$('#subFlowNames').data('rows')||[];
			$('#flowGrid').datagrid('loadData',rows);
			$('#flowTree').find('.tree-node.tree-node-selected').removeClass('tree-node-selected');
		}
	});
	var flowTreeData = $.callSyn("FlowServ","queryPackageCatalogByAreaIdAndSystemCode",{areaId: session["areaId"],systemCode:session["systemCode"]});
	$('#flowTree').tree({
		data:flowTreeData,
		onClick:function(node){
			if(node.type==nodeType.CATALOG){
				node.type=nodeType.LOAD_CATALOG;
				var flowDatas = $.callSyn('FlowServ','qryPackageDefineByCatalogId',{catalogId:node.id});
				$('#flowTree').tree('append',{parent:node.target,data:flowDatas});
			}else if(node.type==nodeType.ELEMENT){//点击流程增加
				var noHas = true;
				var cNodes = $('#flowTree').tree('getChildren',node.target);
				var i=0,len=cNodes.length,cNode;
				for(;i<len;i++){
					if(cNodes[i].text.indexOf("激活")!=-1){
						cNode=cNodes[i];
						break;
					}
				}
				if(cNode){
					$.each($('#flowGrid').datagrid('getRows'),function(i,n){
					if(cNode.id==n.id){
						noHas = false;
					}
					});
					if(noHas){
						var row = {name:node.text,code:cNode.code,version:cNode.text,id:cNode.id};
						$('#flowGrid').datagrid('appendRow',row);
					}
				}
			}else if(node.type==nodeType.VERSION){//点击版本添加
				if(node.text.indexOf("激活")!=-1){
					var noHas = true;
					var pNode = $('#flowTree').tree('getParent',node.target);
					$.each($('#flowGrid').datagrid('getRows'),function(i,n){
						if(node.id==n.id){
							noHas = false;
						}
					});
					if(noHas){
						var row = {name:pNode.text,code:node.code,version:node.text,id:node.id};//id 用的是激活版本的id
						$('#flowGrid').datagrid('appendRow',row);
					}
				}
			}
		}
	});
	$('#flowGrid').datagrid({
		autoRowHeight:false,
	   	fitColumns:true,
	    columns:[[
	    	{title:'',field:'del',width:30,formatter:function(value,row,index){
					return '<img src="/uos-manager/common/easyui/themes/icons/clear.png" >';
				},styler: function(value,row,index){
					return 'cursor:pointer;';
				}},
			{title:'流程名称',field:'name',width:150},
			{title:'流程编码',field:'code',width:150},
			{title:'流程版本',field:'version',width:150},
			{title:'流程id',field:'id',hidden:true}
	    ]],
	    onClickCell:function(index, field, value){
	    	if(field=='del'){
	    		$('#flowGrid').datagrid('deleteRow',index);
	    	}
	    }
	});
	$('#flowWin-confirmBtn').click(function(e){
		var rows = $('#flowGrid').datagrid('getRows');
		var arr=[];
		$.each(rows,function(i,n){
			arr.push(n.name);
		});
		$('#subFlowNames').data('rows',rows);
		$('#subFlowNames').textbox('setValue',arr.join());
		$('#flowWin').dialog('close');
	});
});
