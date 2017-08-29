$(function($) {
	/** 全局变量*/
	var page=1;
	var pageSize=20;
	var session = $.session();
	var nodeType = {
		CATALOG:"1",		//目录
		ELEMENT:"2",		//元素--环节或者流程
		VERSION:"3",		//版本--流程
		LOAD_CATALOG:"11"	//已加载过的目录
	};
	//$('body').layout('collapse','south');//初始化折叠流程图页面
	
	/** 查询条件*/
	$('#mainFlow').textbox({
		readonly:true,
		icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				$('#flowWin').data('id','mainFlow');
				$('#flowWin').dialog('open');
			}
		}]
	});
	$('#flow').textbox({
		readonly:true,
		icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				$('#flowWin').data('id','flow');
				$('#flowWin').dialog('open');
			}
		}]
	});
	$('#mainTache').textbox({
		readonly:true,
	    icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				$('#tacheWin').data('id','mainTache');
				$('#tacheWin').dialog('open');
			}
		}]
	});
	$('#tache').textbox({
		readonly:true,
		icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				$('#tacheWin').data('id','tache');
				$('#tacheWin').dialog('open');
			}
		}]
	});
	
	/** 环节同步配置列表*/
	$('#synRuleTable').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
	    fitColumns:true,
	    striped:true,//奇偶行使用不同背景色
	    pagination:true,//
		pageSize:pageSize,//初始化页面尺寸	
	    toolbar: '#synRuleTb',
	    idField:'id',
	    columns:[[
			{title:'主流程',field:'mainPackageDefineName',width:120},
			{title:'主环节',field:'mainTacheName',width:120},
			{title:'附属流程',field:'packageDefineName',width:120},
			{title:'附属环节',field:'tacheName',width:120},
			{title:'同步类型',field:'synType',width:80,
				formatter:function(value,row,index){
					if(value=='001'){
						return '同步';
					}else{
						return '依赖';
					}
				}},
			{title:'同步消息',field:'synMessage',width:200,
				formatter:function(value,row,index){
					if(value=='null'||value==null){
						value = '';
					}
					return value;
				}},
			{title:'主流程ID',field:'mainPackageDefineId',hidden:true},
			{title:'主环节ID',field:'mainTacheId',hidden:true},
			{title:'附属流程ID',field:'packageDefineId',hidden:true},
			{title:'附属环节ID',field:'tacheId',hidden:true},
			{title:'有效',field:'state',hidden:true},
			{title:'区域',field:'areaId',hidden:true},
			{title:'id',field:'id',hidden:true}
	    ]],
		rowStyler: function(index,row){
			if (row&&row.state=='10P'){//失效的记录
				return 'color:#99CC99;'; 
			}else if(row&&row.state=='10X'){//无效记录--流程模板或环节失效了
				return 'color:#CC3299;'; 
			}
		},
		onClickRow: function(index,row){//展开
			if(row.state=='10A'){
				$('#btnModSynRule').linkbutton('enable');
				$('#btnDelSynRule').linkbutton('enable');
			}else{
				$('#btnModSynRule').linkbutton('disable');
				$('#btnDelSynRule').linkbutton('disable');
			}
		}
	});
	//分页处理
	var pager=$('#synRuleTable').datagrid('getPager');
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
	/**function*/
	var funQry = function(pIndex){
		var mainPackageDefineIds=[];
		$.each($('#mainFlow').data('rows')||[],function(i,n){
			mainPackageDefineIds.push(n.id);
		});
		var mainTacheIds=[];
		$.each($('#mainTache').data('rows')||[],function(i,n){
			mainTacheIds.push(n.id);
		});
		var packageDefineIds=[];
		$.each($('#flow').data('rows')||[],function(i,n){
			packageDefineIds.push(n.id);
		});
		var tacheIds=[];
		$.each($('#tache').data('rows')||[],function(i,n){
			tacheIds.push(n.id);
		});
		var param = {
			mainPackageDefineIds:mainPackageDefineIds.join(),
			mainTacheIds:mainTacheIds.join(),
			packageDefineIds:packageDefineIds.join(),
			tacheIds:tacheIds.join(),
			isAll:$('#isAll').is(':checked')?'all':null,
			systemCode:session["systemCode"],
			page:pIndex||page,
			pageSize:pageSize
		};
		var ret = $.callSyn('SynRuleServ','qrySynRuleByCond',$.util.formatObj(param));
		pager.pagination('refresh',{pageNumber:1});//刷新分页信息
		$('#synRuleTable').datagrid('loadData',ret).datagrid('unselectAll');
		$('#btnModSynRule').linkbutton('disable');
		$('#btnDelSynRule').linkbutton('disable');
	};
	/**button*/
	$('#btnQry').click(function(){funQry();});
	$('#btnAddSynRule').click(function(e){
		$('#synRuleWin').data('mode',0).dialog('open');
	});
	$('#btnModSynRule').click(function(e){
		if($(this).linkbutton('options').disabled){
			return;
		}
		$('#synRuleWin').data('mode',1).dialog('open');
	});
	$('#btnDelSynRule').click(function(e){
		if($(this).linkbutton('options').disabled){
			return;
		}
		$.messager.confirm('提示', '确定删除该环节同步规则吗？', function(r){
			if (r){
				var sItem = $('#synRuleTable').datagrid('getSelected');
				sItem.state='10P';
				var ret = $.callSyn('SynRuleServ','modSynRule',sItem);
				if(ret.isSuccess&&ret.isSuccess>0){
					$.messager.alert("提示","删除环节同步规则成功");
					$('#synRuleTable').datagrid('deleteRow',$('#synRuleTable').datagrid('getRowIndex', $("#synRuleTable").datagrid('getSelected')));//删除
				}else{
					$.messager.alert("提示","删除环节同步规则失败");
				}
			}
		});
	});
	/**dialog*/
	//环节同步
	$('#synRuleWin').dialog({
		onBeforeOpen:function(){
			if($('#synRuleWin').data('mode')==1){//mod
				var sItem = $('#synRuleTable').datagrid('getSelected');
				$('#synRule-mainFlow').textbox('setValue',sItem.mainPackageDefineName);
				$('#synRule-mainTache').textbox('setValue',sItem.mainTacheName);
				$('#synRule-flow').textbox('setValue',sItem.packageDefineName);
				$('#synRule-tache').textbox('setValue',sItem.tacheName);
				$('#synRule-synType').combobox('select',sItem.synType);
				$('#synRule-synMessage').textbox('setValue',sItem.synMessage=='null'?'':sItem.synMessage);
				$('#synRule-mainFlow').data('rows',[{id:sItem.mainPackageDefineId,name:sItem.mainPackageDefineName}]);//version 需要找tree，所以不显示
				$('#synRule-mainTache').data('rows',[{id:sItem.mainTacheId,tacheName:sItem.mainTacheName}]);//tacheCatalogName 需要找tree，所以不显示
				$('#synRule-flow').data('rows',[{id:sItem.packageDefineId,name:sItem.packageDefineName}]);
				$('#synRule-tache').data('rows',[{id:sItem.tacheId,tacheName:sItem.tacheName}]);
			}
		},
		onClose:function(){
			//清空
			$('#synRule-mainFlow').data('rows',[]);
			$('#synRule-mainTache').data('rows',[]);
			$('#synRule-flow').data('rows',[]);
			$('#synRule-tache').data('rows',[]);
			$('#synRuleForm').form('clear');
			$('#synRule-synType').combobox('select','001');
	}
	});
	$('#synRuleWin-confirmBtn').click(function(e){
		if($('#synRuleForm').form('validate')){
			if($('#synRule-mainFlow').data('rows')[0].id==$('#synRule-flow').data('rows')[0].id){
				$.messager.alert("提示","主流程和附属流程不能是同一个流程");
				return;
			}
			var param = {
				mainPackageDefineId:$('#synRule-mainFlow').data('rows')[0].id,
				mainTacheId:$('#synRule-mainTache').data('rows')[0].id,
				packageDefineId:$('#synRule-flow').data('rows')[0].id,
				tacheId:$('#synRule-tache').data('rows')[0].id,
				synType:$('#synRule-synType').combobox('getValue'),
				synMessage:$('#synRule-synMessage').textbox('getValue'),
				systemCode:session["systemCode"]
			};
			if($('#synRuleWin').data('mode')==1){//mod
				param.id=$('#synRuleTable').datagrid('getSelected').id;
				param.state='10A';
				var ret = $.callSyn('SynRuleServ','modSynRule',$.util.formatObj(param));
				if(ret.isSuccess&&ret.isSuccess>0){
					param.mainPackageDefineName=$('#synRule-mainFlow').textbox('getValue');
					param.mainTacheName=$('#synRule-mainTache').textbox('getValue');
					param.packageDefineName=$('#synRule-flow').textbox('getValue');
					param.tacheName=$('#synRule-tache').textbox('getValue');
					$('#synRuleTable').datagrid('updateRow',{index:$('#synRuleTable').datagrid('getRowIndex', $("#synRuleTable").datagrid('getSelected')),row:param});//修改					
					$.messager.alert("提示","修改环节同步规则成功");
					$('#synRuleWin').dialog('close');
				}else{
					if(ret.isSuccess==-1){
						$.messager.alert("提示","基于模板的环节同步规则重复");
					}else{
						$.messager.alert("提示","修改环节同步规则失败");
					}
				}
			}else{//0--add
				param.areaId=session["areaId"];
				var ret = $.callSyn('SynRuleServ','addSynRule',$.util.formatObj(param));
				if(ret.id){
					if(ret.id =='-1'){
						$.messager.alert("提示","基于模板的环节同步规则重复");
					}else{
						ret.mainPackageDefineName=$('#synRule-mainFlow').textbox('getValue');
						ret.mainTacheName=$('#synRule-mainTache').textbox('getValue');
						ret.packageDefineName=$('#synRule-flow').textbox('getValue');
						ret.tacheName=$('#synRule-tache').textbox('getValue');
						ret.state='10A';
						$('#synRuleTable').datagrid('appendRow',ret);
						$.messager.alert("提示","新增环节同步规则成功");
						$('#synRuleWin').dialog('close');
					}
				}else{
					$.messager.alert("提示","新增环节同步规则失败");
				}
			}
		}
	});
	$('#synRule-mainFlow').textbox({
		readonly:true,
		icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				$('#flowWin').data('id','synRule-mainFlow');
				$('#flowWin').dialog('open');
			}
		}],
		onChange:function(newValue,oldValue){//改变的时候就去查好环节，应该比打开环节页面再去查次数要少点吧
			$('#synRule-mainTache').textbox('clear');
			$('#synRule-mainTache').data('rows',[]).data('datas',[]);
			if(newValue!=''){
				//tache tree
				if($('#synRule-mainFlow').data('rows')&&$('#synRule-mainFlow').data('rows').length>0){
					var tachesData = $.callSyn('FlowInstServ','qryProcessTacheByCond',{processDefineId:$('#synRule-mainFlow').data('rows')[0].id});
					$('#synRule-mainTache').data('datas',tachesData);
				}else{
					if($('#synRuleWin').data('mode')==1){//mod
						var tachesData = $.callSyn('FlowInstServ','qryProcessTacheByCond',{processDefineId:$("#synRuleTable").datagrid('getSelected').mainPackageDefineId});
						$('#synRule-mainTache').data('datas',tachesData);
					}
				}
			}
		}
	});
	$('#synRule-flow').textbox({
		readonly:true,
		icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				$('#flowWin').data('id','synRule-flow');
				$('#flowWin').dialog('open');
			}
		}],
		onChange:function(newValue,oldValue){
			$('#synRule-tache').textbox('clear');
			$('#synRule-tache').data('rows',[]).data('datas',[]);
			if(newValue!=''){
				//tache tree
				if($('#synRule-flow').data('rows')&&$('#synRule-flow').data('rows').length>0){
					var tachesData = $.callSyn('FlowInstServ','qryProcessTacheByCond',{processDefineId:$('#synRule-flow').data('rows')[0].id});
					$('#synRule-tache').data('datas',tachesData);
				}else{
					if($('#synRuleWin').data('mode')==1){//mod
						var tachesData = $.callSyn('FlowInstServ','qryProcessTacheByCond',{processDefineId:$("#synRuleTable").datagrid('getSelected').packageDefineId});
						$('#synRule-tache').data('datas',tachesData);
					}
				}
			}
		}
	});
	$('#synRule-mainTache').textbox({
		readonly:true,
	    icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				$('#tacheWin').data('id','synRule-mainTache');
				$('#tacheWin').dialog('open');
			}
		}]
	});
	$('#synRule-tache').textbox({
		readonly:true,
		icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				$('#tacheWin').data('id','synRule-tache');
				$('#tacheWin').dialog('open');
			}
		}]
	});
	//流程
	$('#flowWin').dialog({
		onBeforeOpen:function(){//初始化之前的选择
			var selector = $('#'+$('#flowWin').data('id')+'');
			var rows=selector.data('rows')||[];
			$('#flowGrid').datagrid('loadData',rows);
			$('.tree-node.tree-node-selected').removeClass('tree-node-selected');
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
						var row = {name:node.text,version:cNode.text,id:cNode.id};
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
						var row = {name:pNode.text,version:node.text,id:node.id};//id 用的是激活版本的id
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
		var selector = $('#'+$('#flowWin').data('id')+'');//获取选择Object
		var rows = $('#flowGrid').datagrid('getRows');
		if($('#flowWin').data('id').indexOf("synRule-")!=-1){
			if(rows.length>1){
				$.messager.alert("提示","请选择一个流程");
				return;
			}
		}
		var arr=[];
		$.each(rows,function(i,n){
			arr.push(n.name);
		});
		if(selector.textbox('getValue')==arr.join()&&arr.join()!=''){//text相同的情况--强制触发onChange
			selector.textbox('setValue','');
		}
		selector.data('rows',rows);
		selector.textbox('setValue',arr.join());
		$('#flowWin').dialog('close');
	});
	//环节
	var tacheTreeData = $.callSyn('TacheServ','qryTacheCatalogTree',{systemCode:session["systemCode"]});
	$('#tacheWin').dialog({
		onBeforeOpen:function(){//初始化之前的选择
			var selector = $('#'+$('#tacheWin').data('id')+'');
			var rows=selector.data('rows')||[];
			$('#tacheGrid').datagrid('loadData',rows);
			$('.tree-node.tree-node-selected').removeClass('tree-node-selected');
			if($('#tacheWin').data('id').indexOf("synRule-")!=-1){//diaglog的环节处理
				if($('#tacheWin').data('id').indexOf("synRule-mainTache")!=-1&&$('#synRule-mainFlow').textbox('getValue')==''){
					$.messager.alert("提示","请先选择主流程");
					return false;
				}
				if($('#tacheWin').data('id').indexOf("synRule-tache")!=-1&&$('#synRule-flow').textbox('getValue')==''){
					$.messager.alert("提示","请先选择附属流程");
					return false;
				}
				$('#tacheTree').tree({//重写tree
					data: selector.data('datas'),
					onClick:function(node){
						var noHas = true;
						$.each($('#tacheGrid').datagrid('getRows'),function(i,n){
							if(node.id==n.id){
								noHas = false;
							}
						});
						if(noHas){
							var row = {tacheName:node.text,id:node.id};//,tacheCatalogName:''
							$('#tacheGrid').datagrid('appendRow',row);
						}
					}
				});
			}else{//展示所有的环节
				$('#tacheTree').tree({
					data: tacheTreeData,
					onClick:function(node){
						if(node.type==nodeType.CATALOG){
							node.type=nodeType.LOAD_CATALOG;
							var tacheDatas = $.callSyn('TacheServ','qryTaches', {state:'10A',tacheCatalogId:node.id});
							var datas = new Array();
							if(tacheDatas.total>0){
								$.each(tacheDatas.rows, function(i, n){
									var tache = {
										id:n.id,//tacheId，直接用ID，然后根据是环节不是目录来去除重复的id
										text:n.tacheName,
										type:nodeType.ELEMENT
									}
									datas.push(tache);
								});
							}
							$('#tacheTree').tree('append',{parent:node.target,data:datas});
						}else if(node.type==nodeType.ELEMENT){
							var noHas = true;
							$.each($('#tacheGrid').datagrid('getRows'),function(i,n){
								if(node.id==n.id){
									noHas = false;
								}
							});
							if(noHas){
								var row = {tacheName:node.text,tacheCatalogName:$('#tacheTree').tree('getParent',node.target).text,id:node.id};
								$('#tacheGrid').datagrid('appendRow',row);
							}
						}
					}
				});
			}
		}
	});
	$('#tacheGrid').datagrid({
		autoRowHeight:false,
	   	fitColumns:true,
	    columns:[[
	   		{title:'',field:'del',width:30,formatter:function(value,row,index){
					return '<img src="/uos-manager/common/easyui/themes/icons/clear.png" >';
				},styler: function(value,row,index){
					return 'cursor:pointer;';
				}},
			{title:'环节',field:'tacheName',width:150},
			{title:'环节目录',field:'tacheCatalogName',width:150},
			{title:'id',field:'id',hidden:true}
	    ]],
	    onClickCell:function(index, field, value){
	    	if(field=='del'){
	    		$('#tacheGrid').datagrid('deleteRow',index);
	    	}
	    }
	});
	$('#tacheWin-confirmBtn').click(function(e){
		var selector = $('#'+$('#tacheWin').data('id')+'');//获取选择Object
		var rows = $('#tacheGrid').datagrid('getRows');
		if($('#tacheWin').data('id').indexOf("synRule-")!=-1){
			if(rows.length>1){
				$.messager.alert("提示","请选择一个环节");
				return;
			}
		}
		var arr=[];
		$.each(rows,function(i,n){
			arr.push(n.tacheName);
		});
		selector.textbox('setValue',arr.join());
		selector.data('rows',rows);
		$('#tacheWin').dialog('close');
	});
	
	$('a.textbox-icon.icon-tip.textbox-icon-disabled').removeClass("textbox-icon-disabled");//使查询tip有效
});
