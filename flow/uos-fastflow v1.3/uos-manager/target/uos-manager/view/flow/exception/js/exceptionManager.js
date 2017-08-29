$(function($) {
	/** 全局变量*/
	var catalogId;
	var nodeType = {
		CATALOG:"1",		//目录
		TACHE:"2",			//环节
		LOAD_CATALOG:"11"	//已加载旗下环节后的目录
	};
	var session = $.session();
	var page=1;
	var pageSize=10;
	var isClick = false;
	/** 布局处理 */
	
	/** 目录区域 */
	var catalogDatas = $.callSyn('ReturnReasonServ','qryReturnReasonCatalogTree',{systemCode:session["systemCode"]});
	$('#cTree').tree({
		data:catalogDatas,
		onClick: function(node){
			funClickCatalogNode(node.id);
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
			funClickCatalogNode(node.id);
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
	/** 异常原因区域 */
	$('#tCode').textbox({
		prompt: '异常原因编码',
		icons:[{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).textbox('clear').textbox('textbox').focus();
			}
		}]
	});
	$('#tName').textbox({
		prompt: '异常原因名称',
		icons:[{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).textbox('clear').textbox('textbox').focus();
			}
		}]
	});
	$('#reasonTable').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
	   // idField:'reasonCode',
	    fitColumns:true,
	    toolbar: '#reasonTb',
	    pagination:true,//
		pageSize:pageSize,//初始化页面尺寸	
	    columns:[[
			{title:'编码',field:'reasonCode',width:180},
			{title:'名称',field:'returnReasonName',width:200},
			{title:'类型',field:'reasonType',width:200,
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
				}}
	    ]],
	    rowStyler: function(index,row){
			if (row.state=='10P'){//失效的记录
				return 'color:#99CC99;'; 
			}
		},
		onClickRow: function(index,row){//加载适用环节
			var ret = $.callSyn('ReturnReasonServ','qryTacheReturnReasons',{returnReasonId:row.id});
			$('#relationTable').datagrid('loadData',ret);
			$('#relationTable').datagrid('unselectAll');//取消之前的选择
			if (row.state=='10A'){//有效的记录
				funBtn($('#btnMod'),funMod,true);//激活异常原因修改按钮
				funBtn($('#btnAddRelation'),funAddRelation,true);//激活异常原因关联环节新增按钮
				if(ret&&ret.total>0){
					funBtn($('#btnDel'),funDel,true);//失效异常原因删除按钮
				}else{
					funBtn($('#btnDel'),funDel,true);//激活异常原因删除按钮
				}
			}else{
				funBtn($('#btnMod'),funMod,false);//失效异常原因修改按钮
				funBtn($('#btnDel'),funDel,false);//失效异常原因删除按钮
				funBtn($('#btnAddRelation'),funAddRelation,false);//失效异常原因关联环节新增按钮
			}
			funBtn($('#btnDelRelation'),funDelRelation,false);//失效异常原因关联环节删除按钮
			
			if(catalogId!=row.reasonCatalogId){
				var node = $('#cTree').tree('find', row.reasonCatalogId);
				funClickCatalogNode(node.id,true);
				$('#cTree').tree('select', node.target);
				$('#cTree').tree('expandTo', node.target);
			}
		}
	});
	//分页处理
	var pager=$('#reasonTable').datagrid('getPager');
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
	/**窗口*/
	//异常原因增改窗口
	$('#dlg').dialog({
		title: '异常原因操作',
	    width: 350,
	    height: 220,
	    closed: true,
	    cache: false,
	    modal: true
	});
	//异常原因目录增改窗口
	$('#dlg2').dialog({
		title: '异常原因目录操作',
	    width: 280,
	    height: 140,
	    closed: true,
	    cache: false,
	    modal: true
	});
	/** 关联环节区域 */
	$('#main').layout('panel','south').panel('resize',{height:$('#main').height()*0.4});
	$('#main').layout('resize');//根据浏览器窗口动态初始化调整关联区域大小
	$('#relationTable').datagrid({
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,
		//idField:'TACHE_CODE',
	   	fitColumns:true,
	    toolbar:'#relationTb',
	   	pagination:true,
		pageList:[5],//必须先设置这个，才能改pageSize，否则是NaN
		pageSize:5,//初始化页面尺寸
	    columns:[[
			{title:'环节目录',field:'tacheCatalogName',width:100},
			{title:'区域',field:'areaName',width:80},
			{title:'环节名称',field:'tacheName',width:100},
			{title:'环节编码',field:'tacheCode',width:80},
			{title:'状态',field:'state',width:80,
				formatter:function(value,row,index){
					if(value=="10A"){
						return "启用";
					}else{
						return "停用";
					}
				}},
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
	//点击异常原因目录方法 id:目录ID noQry:存在此参数则标明不用重新加载查询此目录下的对应所有异常原因
	var funClickCatalogNode = function(id,noQry){
		catalogId = id;
		if(typeof(noQry)=="undefined"){
			isClick = true;
			funQry();
		}
		funBtn($('#btnAdd'),funAdd,true);//激活异常原因增加按钮
		funBtn($('#btnMod'),funMod,false);//失效异常原因修改按钮
		funBtn($('#btnDel'),funDel,false);//失效异常原因删除按钮
		funBtn($('#btnAddRelation'),funAddRelation,false);//激活异常原因关联环节新增按钮
		funBtn($('#btnDelRelation'),funDelRelation,false);//激活异常原因关联环节删除按钮
	};
	//点击查询按钮
	var funClickQryBtn = function(){
		isClick = false;
		funQry();
	}
	//得到查询条件
	var funGetConditionObj = function(pageIndex){
		//获取选中的状态
		var value = $('#tState').combobox('getValue');
		var state;
		if(value=='enabled'){
			state='10A';
		}else if(value=='disable'){
			state='10P';
		}
		var reasonClass = $('#tType').combobox('getValue');//异常大类
		var reasonCatalogId;
		if(isClick){
			reasonCatalogId = catalogId;
		}
		var params = {
			reasonCatalogId:reasonCatalogId,
			state:state,
			reasonClass:reasonClass,
			reasonCode:$('#tCode').val(),
			returnReasonName:$('#tName').val(),
			page:pageIndex||page,
			pageSize:pageSize
		};
		return $.util.formatObj(params);
	};
	//查询方法
	var funQry = function(pageIndex){
		var params = funGetConditionObj(pageIndex);
		var ret = $.callSyn('ReturnReasonServ','qryReturnReasons',params);
		if(typeof(pageIndex)=='undefined'){//刷新分页信息
			pager.pagination('refresh',{pageNumber:1});
		}
		$('#reasonTable').datagrid('loadData',ret);
		$('#reasonTable').datagrid('unselectAll');//取消之前的选择
		$('#relationTable').datagrid('loadData',{total: 0, rows:[]});//清空
		
		if((!isClick)&&$('#reasonTable').datagrid('getRows').length>0){
			$('#reasonTable').datagrid('selectRow',0);//选择第一条
			var row = $('#reasonTable').datagrid('getSelected');
			var node = $('#cTree').tree('find', row.reasonCatalogId);
			funClickCatalogNode(node.id,true);
			$('#cTree').tree('select', node.target);
			$('#cTree').tree('expandTo', node.target);
		}
	};
	//增加异常原因
	var funAdd = function(){
		$('#dlg').dialog({
			title: '增加异常原因',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#reasonForm').form('validate')){
						var reasonCatalogId = catalogId;//只在选择好的目录中增加
						var params = {
							reasonCatalogId:reasonCatalogId,
							reasonCode:$('#reasonCode').val(),//.textbox('getValue')
							returnReasonName:$('#returnReasonName').val(),
							reasonType:$('#reasonType').combobox('getValue')
						};
						var ret = $.callSyn('ReturnReasonServ','addReturnReason',params);
						if(ret.returnReasonId){
							if(ret.returnReasonId =='-1'){
								$.messager.alert("提示","异常原因编码重复");
							}else{
								$.messager.alert("提示","新增异常原因成功");
								var node = $('#cTree').tree('find', reasonCatalogId);
								$('#cTree').tree('select', node.target);
								funClickCatalogNode(node.id);
								$('#reasonTable').datagrid('highlightRow',0);//高亮
								$('#dlg').dialog('close');
							}
						}else{
							$.messager.alert("提示","新增异常原因失败");
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
				$('#reasonCode').textbox('clear').textbox('enable');
				$('#returnReasonName').textbox('clear');
				$('#reasonCatalogName').combotree('setValue', $('#cTree').tree('getSelected').text);//为了通过validate验证
				$('#tCatalogForm').hide();
				$('#reasonType').combobox('enable');
				//reasonType选中第一个值
				var reasonTypeData = $('#reasonType').combobox('getData');
				if (reasonTypeData.length > 0) {
					$('#reasonType').combobox('select', reasonTypeData[0].label);
				}
			}
		});
		$('#dlg').dialog('open');
	};
	//修改异常原因--目录和名称
	var funMod = function(){
		$('#dlg').dialog({
			title: '修改异常原因',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#reasonForm').form('validate')){
						var sTreeNode = $('#reasonCatalogName').combotree('tree').tree('getSelected');
						var reasonCatalogId = catalogId;
						if(sTreeNode){
							reasonCatalogId = sTreeNode.id;
						}
						var params = {
							id:$('#reasonTable').datagrid('getSelected').id,
							reasonCatalogId:reasonCatalogId,
							returnReasonName:$('#returnReasonName').val()
						};
						var ret = $.callSyn('ReturnReasonServ','modReturnReason',params);
						if(ret.isSuccess){
							$.messager.alert("提示","修改异常原因成功");
							var node = $('#cTree').tree('find', reasonCatalogId);
							$('#cTree').tree('select', node.target);
							funClickCatalogNode(node.id);
							$('#reasonTable').datagrid('highlightRow',0);//高亮
							$('#dlg').dialog('close');
						}else{
							$.messager.alert("提示","修改异常原因失败");
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
				var sReason = $('#reasonTable').datagrid('getSelected');
				$('#reasonCode').textbox('setValue',sReason.reasonCode).textbox('disable');//.textbox('readonly',true);
				$('#returnReasonName').textbox('setValue',sReason.returnReasonName);
				$('#reasonCatalogName').combotree('setValue', $('#cTree').tree('getSelected').text);
				$('#tCatalogForm').show();
				$('#reasonType').combobox('setValue',sReason.reasonType).combobox('disable');
			}
		});
		$('#dlg').dialog('open');
	};
	//删除异常原因
	var funDel = function(){
		$.messager.confirm('提示', '确定删除该异常原因吗？', function(r){
			if (r){
				var retR = $.callSyn('ReturnReasonServ','qryTacheReturnReasons',{returnReasonId:$('#reasonTable').datagrid('getSelected').id,state:'10A'});
				if(retR && retR.total>0){
					$.messager.alert("提示","该异常原因已经配置在环节上，请停用之后再删除异常原因");
					return;
				}else{
					var ret = $.callSyn('ReturnReasonServ','delReturnReason',{id:$('#reasonTable').datagrid('getSelected').id});
					if(ret.isSuccess){
						$.messager.alert("提示","删除异常原因成功");
						$('#reasonTable').datagrid('deleteRow',$('#reasonTable').datagrid('getRowIndex', $("#reasonTable").datagrid('getSelected')));//删除
					}else{
						$.messager.alert("提示","删除异常原因失败");
					}
				}
			}
		});
	};
	//增加异常原因目录
	var funAddCatalog = function(){
		$('#dlg2').dialog({
			title: '增加异常原因目录',
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
							reasonCatalogName:$('#catalogName').val(),
							parentReasonCatalogId:pCatalogId,
							systemCode:systemCode
						};
						var ret = $.callSyn('ReturnReasonServ','addReturnReasonCatalog',params);
						if(ret.catalogId){
							$.messager.alert("提示","新增异常原因目录成功");
							if(sNode){
								$('#cTree').tree('insert',{after:sNode.target,data:{id:ret.catalogId,text:params.reasonCatalogName,type:nodeType.CATALOG,systemCode:params.systemCode}});
							}else{
								$('#cTree').tree('append',{data:[{id:ret.catalogId,text:params.reasonCatalogName,type:nodeType.CATALOG,systemCode:params.systemCode}]});
							}
							$('#cTree').tree('select', $('#cTree').tree('find', ret.catalogId).target);//选中新增node
							funClickCatalogNode(ret.catalogId);
							$('#dlg2').dialog('close');
						}else{
							$.messager.alert("提示","新增异常原因目录失败");
						}
					}else{
						$.messager.alert("提示","请输入异常原因目录");
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
	//修改异常原因目录
	var funModCatalog = function(){
		$('#dlg2').dialog({
			title: '修改异常原因目录',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#catalogName').textbox('isValid')){
						var sNode =$('#cTree').tree('getSelected');
						var params = {
							reasonCatalogName:$('#catalogName').val(),
							id:sNode.id
						};
						var ret = $.callSyn('ReturnReasonServ','modReturnReasonCatalog',params);
						if(ret.isSuccess){
							$.messager.alert("提示","修改异常原因目录成功");
							$('#cTree').tree('update',{target:sNode.target,text:params.reasonCatalogName});
							$('#dlg2').dialog('close');
						}else{
							$.messager.alert("提示","修改异常原因目录失败");
						}
					}else{
						$.messager.alert("提示","请输入异常原因目录");
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
	//删除异常原因目录
	var funDelCatalog = function(){
		var cNodes = $('#cTree').tree('getChildren',$('#cTree').tree('getSelected').target);
		if(cNodes.length>0){
			$.messager.alert("提示","该目录下存在子目录，无法删除");
		}else{
			$.messager.confirm('提示', '确定删除该异常原因目录吗？', function(r){
				if (r){
					var ret = $.callSyn('ReturnReasonServ','delReturnReasonCatalog',{id:$('#cTree').tree('getSelected').id});
					if(ret.isSuccess){
						$.messager.alert("提示","删除异常原因目录成功");
						$('#cTree').tree('remove',$('#cTree').tree('getSelected').target);//删除
					}else{
						$.messager.alert("提示","删除异常原因目录失败");
					}
					
				}
			});
		}
	};
	//增加异常原因子目录
	var funAddSubCatalog = function(){
		$('#dlg2').dialog({
			title: '增加异常原因子目录',
			buttons:[{
				text:'确定',
				handler:function(){
					if($('#catalogName').textbox('isValid')){
						var sNode =$('#cTree').tree('getSelected');
						var params = {
							reasonCatalogName:$('#catalogName').val(),
							parentReasonCatalogId:sNode.id,
							systemCode:sNode.systemCode
						};
						var ret = $.callSyn('ReturnReasonServ','addReturnReasonCatalog',params);
						if(ret.catalogId){
							$.messager.alert("提示","新增异常原因目录成功");
							$('#cTree').tree('append',{parent:sNode.target,data:[{id:ret.catalogId,text:params.reasonCatalogName,type:nodeType.CATALOG,systemCode:params.systemCode}]});
							$('#cTree').tree('select', $('#cTree').tree('find', ret.catalogId).target);//选中新增node
							funClickCatalogNode(ret.catalogId);
							$('#dlg2').dialog('close');
						}else{
							$.messager.alert("提示","新增异常原因目录失败");
						}
					}else{
						$.messager.alert("提示","请输入异常原因目录");
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
	$('#btnQry').bind('click',funClickQryBtn);//bind后面的方法,不能带括号,带括号就自动执行了funClickQryBtn
	$('#btnAddCatalogs').bind('click',funAddCatalog);
	$('#btnAddCatalog').bind('click',funAddCatalog);
	$('#btnModCatalog').bind('click',funModCatalog);
	$('#btnDelCatalog').bind('click',funDelCatalog);
	$('#btnAddSubCatalog').bind('click',funAddSubCatalog);
	
	/** 对话框 */
	$('#reasonType').combobox({
		required:true,
		editable:false,
		panelHeight:'auto',
		valueField: 'label',
		textField: 'value',
		data: [{
			label: '10R',
			value: '退单'
		},{
			label: '10W',
			value: '待装'
		},{
			label: '10C',
			value: '撤单'
		},{
			label: '10Q',
			value: '改单'
		},{
			label: '10P',
			value: '缓装'
		}]
	});
	//对话框中选择目录
	$('#reasonCatalogName').combotree({
		panelHeight:88,
		onShowPanel: function(){
			var datas = $.callSyn('ReturnReasonServ','qryReturnReasonCatalogTree',{systemCode:session["systemCode"]});//从后台加载数据，不与cTree关联，容易bug
			$('#reasonCatalogName').combotree('loadData',datas);
			var dTree = $('#reasonCatalogName').combotree('tree');
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
	var tacheCatalogDatas = $.callSyn('TacheServ','qryTacheCatalogTree',{systemCode:session["systemCode"]});
	$('#tacheCatalogTree').tree({
		data:tacheCatalogDatas,
		checkbox:true,
		onClick: function(node){
			if(node.type==nodeType.CATALOG){
				loadTachesOfCatalog(node);
			}
		},
		onExpand:function(node){
			if(node.type==nodeType.CATALOG){
				loadTachesOfCatalog(node);
			}
		},
		onBeforeCheck:function(node, checked){
			if(checked&&node.type==nodeType.CATALOG){
				loadTachesOfCatalog(node);
			}
		}
	});
	$('#tacheNameR').searchbox({
	    searcher:function(value){
	    	if(value&&value!=''){
				var tacheDatas = $.callSyn('TacheServ','qryTaches',{state:'10A',tacheName:value});
				if(tacheDatas.total>0){
					var sItem = $('#tacheCatalogTree').tree('getSelected');
					var findTacheCatalogId = tacheDatas.rows[0].tacheCatalogId;
					var findId = tacheDatas.rows[0].id;
					if(sItem){
						var i=0,len = tacheDatas.rows.length;
						for(;i<len;i++){
							var tr = tacheDatas.rows[i].tacheCatalogId+"-"+tacheDatas.rows[i].id;
							if(tr==sItem.id){
								var j = (i+1==len)? 0:(i+1);
								findTacheCatalogId = tacheDatas.rows[j].tacheCatalogId;
								findId = tacheDatas.rows[j].id;
								break;
							}
						}
					}
					var catalogNode = $('#tacheCatalogTree').tree('find', findTacheCatalogId);
					if(catalogNode.type==nodeType.CATALOG){
						loadTachesOfCatalog(catalogNode);
					}
					var tacheTreeId = findTacheCatalogId+"-"+findId;
					var sNode = $('#tacheCatalogTree').tree('find', tacheTreeId);
					$('#tacheCatalogTree').tree('select', sNode.target).tree('expandTo', sNode.target);
				}
	    	}else{
	    		$.messager.alert("提示","模糊查询不能为空");
	    	}
	    },
	    prompt:'环节名称'
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
	//882104 end
	//方法——增加异常原因适配环节
	var funAddRelation = function(){
		$('#dlg3').dialog({
			title: '增加异常原因适配环节',
			buttons:[{
				text:'确定',
				handler:function(){
					var sTacheNodes = $('#tacheCatalogTree').tree('getChecked');
					if((sTacheNodes.length>0)&&($('#relationForm').form('validate'))){
						var sReason = $('#reasonTable').datagrid('getSelected');
						var tacheIds = new Array();
						$.each(sTacheNodes, function(i, n){
							if(n.type==nodeType.TACHE){
								tacheIds.push(n.id.split("-")[1]);//取出环节
							}
						});
						var params = {
							returnReasonId:sReason.id,
							tacheIds:tacheIds,
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
								if(n.areaId==params.areaId&&n.returnReasonId==params.returnReasonId){
									$.each(params.tacheIds,function(j,m){
										if(m==n.tacheId){
											isHas = true;
										}
									});
								}
							});
						}
						if(isHas){
							$.messager.alert("提示","已存在相同配置的异常原因适配环节");
						}else{
							var ret = $.callSyn('ReturnReasonServ','addTacheReturnReason',params);
							if(ret.isSuccess){
								$.messager.alert("提示","新增异常原因适配环节成功");
								var ret = $.callSyn('ReturnReasonServ','qryTacheReturnReasons',{returnReasonId:sReason.id});
								$('#relationTable').datagrid('loadData',ret);
								funBtn($('#btnDelRelation'),funDelRelation,false);//失效异常原因关联环节删除按钮
								$('#dlg3').dialog('close');
							}else{
								$.messager.alert("提示","新增异常原因适配环节失败");
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
				$.each($('#tacheCatalogTree').tree('getRoots'), function(i, n){//循环遍历
					$('#tacheCatalogTree').tree('collapseAll',n.target);//初始化关闭
					if(i==0){
						$('#tacheCatalogTree').tree('select',n.target);//切换选择成第一个根节点
						$(n.target).removeClass("tree-node-selected");//并去掉选择
					}
				});
				$.each($('#tacheCatalogTree').tree('getChecked'), function(i, n){//取消勾选的项
					$('#tacheCatalogTree').tree('uncheck',n.target);
				});
				
				$('#tacheNameR').searchbox('setValue','');
				$('#areaIdR').val('');
				$('#areaR').combotree('clear');
				var auditData = $('#auditR').combobox('getData');
				if (auditData.length > 0) {
					$('#auditR').combobox('select', auditData[0].label);
				}
			}
		});
		$('#dlg3').dialog('open');
	};
	//删除异常原因适配环节
	var funDelRelation = function(){
		$.messager.confirm('提示', '确定删除该异常原因的适配环节吗？', function(r){
			if (r){
				var params = {
					returnReasonId:$('#relationTable').datagrid('getSelected').returnReasonId,
					tacheId:$('#relationTable').datagrid('getSelected').tacheId,
					areaId:$('#relationTable').datagrid('getSelected').areaId
				};
				var ret = $.callSyn('ReturnReasonServ','delTacheReturnReason',params);
				if(ret.isSuccess){
					$.messager.alert("提示","删除异常原因的适配环节成功");
					//$('#relationTable').datagrid('deleteRow',$('#relationTable').datagrid('getRowIndex', $("#relationTable").datagrid('getSelected')));//删除
					//因为是页面端分页，所以需要重新加载。
					var arr = $.callSyn('ReturnReasonServ','qryTacheReturnReasons',{returnReasonId:$('#reasonTable').datagrid('getSelected').id});
					$('#relationTable').datagrid('loadData',arr);
					$('#relationTable').datagrid('unselectAll');//取消之前的选择
					funBtn($('#btnDelRelation'),funDelRelation,false);//失效异常原因关联环节删除按钮
				}else{
					$.messager.alert("提示","删除异常原因的适配环节失败");
				}
				
			}
		});
	};
	//方法——加载环节目录tree旗下的环节节点
	var loadTachesOfCatalog = function(node){
		var tacheDatas = $.callSyn('TacheServ','qryTaches', {state:'10A',tacheCatalogId:node.id});
		var datas = new Array();
		if(tacheDatas.total>0){
			$.each(tacheDatas.rows, function(i, n){
				var tache = {
					id:node.id+"-"+n.id,//catalogId-tacheId
					text:n.tacheName,
					type:nodeType.TACHE
				}
				datas.push(tache);
			});
		}
		$('#tacheCatalogTree').tree('append',{parent:node.target,data:datas});
		node.type=nodeType.LOAD_CATALOG;//标识已经加载过了
	};
});

