$(function($){//不要name，表结构typename要去掉
	var data_do = $("#dispatchRulesWin").data("do");
	if(data_do=='edit'){//不支持.linkbutton('enable')
		$('#dispatchRulesTab').find(".edit").linkbutton({disabled:false});
	}else{//show
		$('#dispatchRulesTab').find(".edit").linkbutton({disabled:true});
	}
	/**派发规则列表*/
	$('#dispatchRulesGrid').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
		pagination:false,//
	    fitColumns:true,
	    toolbar: '#dispatchRulesTab',
	    columns:[[
				{title:'区域',field:'areaName',width:100},
				{title:'派发规则类型',field:'type',width:120,
					formatter:function(value,row,index){
						if(value=='ARE'){
							return '按区域派单';
						}else{//DEF
							return '自定义派单规则';
						}
					}},
				{title:'回退方式',field:'rollbackType',width:100,
					formatter:function(value,row,index){
						if(value=='SAM'){
							return '同派单配置';
						}else{//ROL
							return '自动回滚方式';
						}
					}},
				{title:'参与人类型',field:'partyType',width:100,
					formatter:function(value,row,index){
						var value_=value;
						if(value=="SYS"){
							value_="系统";
						}else if(value=="JOB"){
							value_="职位";
						}else if(value=="ORG"){
							value_="组织";
						}else if(value=="STA"){
							value_="人员";
						}
						return value_;
					}},
				{title:'参与人',field:'partyName',width:120},
				/*{title:'自动工单',field:'autoWorkOrder',width:80,
					formatter:function(value,row,index){
						if(value=='1'){
							return '是';
						}else{
							return '否';
						}
					}},*/
				{title:'适用于所有流程',field:'applyAll',width:120,
					formatter:function(value,row,index){
						if(value=='1'){
							return '是';
						}else{
							return '否';
						}
					}},	
				{title:'id',field:'id',hidden:true},
				{title:'packageDefineId',field:'packageDefineId',hidden:true},
				{title:'tacheCode',field:'tacheCode',hidden:true},
				{title:'areaId',field:'areaId',hidden:true},
//				{title:'type',field:'type',hidden:true},
//				{title:'rollbackType',field:'rollbackType',hidden:true},
//				{title:'partyType',field:'partyType',hidden:true},
				{title:'partyId',field:'partyId',hidden:true},
//				{title:'orgId',field:'orgId',hidden:true},
				{title:'manualPartyType',field:'manualPartyType',hidden:true},
				{title:'manualPartyId',field:'manualPartyId',hidden:true},
				{title:'manualPartyName',field:'manualPartyName',hidden:true},
				{title:'callType',field:'callType',hidden:true},
				{title:'bizId',field:'bizId',hidden:true},
				{title:'bizName',field:'bizName',hidden:true},
				{title:'isAutomaticReturn',field:'isAutomaticReturn',hidden:true},
				{title:'isAutoManual',field:'isAutoManual',hidden:true},
				{title:'isReverseAutomaticReturn',field:'isReverseAutomaticReturn',hidden:true},
				{title:'isReverseAutomaticManual',field:'isReverseAutomaticManual',hidden:true}
	    	]]
	});
	/**派发规则操作*/
	$("#dispatchRulesGridAdd").click(function(evt)
	{
		if($(this).linkbutton('options').disabled){
			return;
		}
		$('#dispatchRuleInfoWin').data('do','add');
		$("#dispatchRuleInfoWin").dialog("open");
	});
	$("#dispatchRulesGridModify").click(function(evt)
	{
		if($(this).linkbutton('options').disabled){
			return;
		}
		var row = $('#dispatchRulesGrid').datagrid('getSelected');
		if(row){
			$('#dispatchRuleInfoWin').data('do','mod');
			$("#dispatchRuleInfoWin").dialog("open");
		}else{
			$.messager.alert("提示","请选择要修改的行","info");
		}
	});
	$("#dispatchRulesGridDelete").click(function(evt)
	{
		if($(this).linkbutton('options').disabled){
			return;
		}
		var row = $("#dispatchRulesGrid").datagrid('getSelected');
		if(row){
			$.messager.confirm('提示', '确定删除该派发规则吗？', function(r){
				if (r){
					var ret = $.callSyn('FlowServ','delDispatchRule',row);
					if(ret.isSuccess){
						$('#dispatchRulesGrid').datagrid('deleteRow',$('#dispatchRulesGrid').datagrid('getRowIndex',row));//删除
						$.messager.alert("提示","删除派发规则成功");
					}else{
						$.messager.alert("提示","删除派发规则失败");
					}
				}
			});
		}else{
			$.messager.alert("提示","请选择要删除的行","info");
		}
	});
	/**派发规则信息配置*/
	$('#dispatchRuleInfoWin').dialog({
		width:600,
		height:440,
		closed:true,
		cache:false,
		modal:true,
		iconCls:'icon-save',
		buttons:[{
			text:'确定',
			handler:function(){
				//valid
				if(!$('#dispatchRuleInfoWin-area').textbox('isValid')){
					$.messager.alert("提示","区域不能为空!","info");
					return;
				}
				if(!$('#dispatchRuleInfoWin-party').textbox('isValid')){
					$.messager.alert("提示",$('#dispatchRuleInfoWin-partyName').text()+"不能为空!","info");
					return;
				}
				var partyType = $('#dispatchRuleInfoWin-partyType').combobox('getValue');
				if(partyType=='SYS'){
					if(!$('#dispatchRuleInfoWin-manual-party').textbox('isValid')){
						$.messager.alert("提示",$('#dispatchRuleInfoWin-manual-partyName').text()+"不能为空!","info");
						return;
					}
//					if(!$('#dispatchRuleInfoWin-sys-biz').textbox('isValid')){
//						$.messager.alert("提示","组件不能为空!","info");
//						return;
//					}
				}
				var params = {
					packageDefineId:$("#dispatchRulesWin").data("packageDefineId"),
					tacheId:$("#dispatchRulesWin").data("tacheId"),
					tacheCode:$("#dispatchRulesWin").data("tacheCode"),
					areaId:$('#dispatchRuleInfoWin-area').textbox('getValue'),
					areaName:$('#dispatchRuleInfoWin-area').textbox('getText'),
					type:$('#dispatchRuleInfoWin-type').combobox('getValue'),
//					typeName:$('#dispatchRuleInfoWin-type').combobox('getText'),
					rollbackType:$('#dispatchRuleInfoWin-rollbackType').combobox('getValue'),
//					rollbackTypeName:$('#dispatchRuleInfoWin-rollbackType').combobox('getText'),
					applyAll:$('#dispatchRuleInfoWin-applyAll').combobox('getValue'),
					partyType:partyType,
//					partyTypeName:$('#dispatchRuleInfoWin-partyType').combobox('getText'),
					partyId:$('#dispatchRuleInfoWin-party').textbox('getValue'),
					partyName:$('#dispatchRuleInfoWin-party').textbox('getText')
//					autoWorkOrder:'0'
				};
				if(partyType=='SYS'){
					var _params = {
						manualPartyType:$('#dispatchRuleInfoWin-manual-partyType').combobox('getValue'),
						manualPartyId:$('#dispatchRuleInfoWin-manual-party').textbox('getValue'),
						manualPartyName:$('#dispatchRuleInfoWin-manual-party').textbox('getText'),
						callType:$('#dispatchRuleInfoWin-callType').combobox('getValue'),
						bizId:$('#dispatchRuleInfoWin-sys-biz').textbox('getValue'),
						bizName:$('#dispatchRuleInfoWin-sys-biz').textbox('getText'),
						isAutomaticReturn:$('#dispatchRuleInfoWin-isAutomaticReturn').combobox('getValue'),
						isAutoManual:$('#dispatchRuleInfoWin-isAutoManual').combobox('getValue'),
						isReverseAutomaticReturn:$('#dispatchRuleInfoWin-isReverseAutomaticReturn').combobox('getValue'),
						isReverseAutomaticManual:$('#dispatchRuleInfoWin-isReverseAutomaticManual').combobox('getValue')
//						autoWorkOrder:'1'
					};				
					$.extend(params,_params);
				}
				var doAct = $('#dispatchRuleInfoWin').data('do');
				if('add'==doAct){
					var ret = $.callSyn('FlowServ','addDispatchRule',params);
					if(ret.id){
						if(ret.id =='-1'){
							$.messager.alert("提示","已存在相同配置的派发规则!","info");
						}else{
							$('#dispatchRulesGrid').datagrid('appendRow',params);
							$.messager.alert("提示","新增派发规则成功","info");
							$('#dispatchRuleInfoWin').dialog('close');
						}
					}else{
						$.messager.alert("提示","新增派发规则失败","info");
					}
				}else if('mod'==doAct){//mod
					/*
					全局的，修改成非all的，则新增一条单独的，全局的不动
					单独的，修改成all的，则直接新增一条全局。
					*/
					var row = $('#dispatchRulesGrid').datagrid('getSelected');
					var ret;
					if(row.applyAll==params.applyAll){
						params.id=row.id;
						ret = $.callSyn('FlowServ','modDispatchRule',params);
						if(ret.isSuccess){
							$('#dispatchRulesGrid').datagrid('updateRow',{index:$('#dispatchRulesGrid').datagrid('getRowIndex',row),row:params});
							$.messager.alert("提示","修改派发规则成功","info");
							$('#dispatchRuleInfoWin').dialog('close');
						}else{
							$.messager.alert("提示","修改派发规则失败","info");
						}
					}else if(row.applyAll=='0'){//单独的，修改成all的，则直接删除单独的，新增一条all。--删除此流程模板的派发规则，
						$.messager.confirm('提示', '确定新增一条适用于所有流程的派发规则吗？', function(r){
							if (r){
//								ret = $.callSyn('FlowServ','delDispatchRule',row);
//								if(ret.isSuccess){
//									$('#dispatchRulesGrid').datagrid('deleteRow',$('#dispatchRulesGrid').datagrid('getRowIndex',row));
									ret = $.callSyn('FlowServ','addDispatchRule',params);
									if(ret.id){
										if(ret.id =='-1'){
											$.messager.alert("提示","已存在相同配置的适用于所有流程的派发规则!","info");
										}else{
											$('#dispatchRulesGrid').datagrid('appendRow',params);
											$.messager.alert("提示","新增适用于所有流程的派发规则成功","info");
											$('#dispatchRuleInfoWin').dialog('close');
										}
									}else{
										$.messager.alert("提示","新增适用于所有流程的派发规则失败","info");
									}
//								}else{
//									$.messager.alert("提示","删除此流程模板的派发规则失败","info");
//								}
							}
						});
					}else{//all的，修改成单独的，则新增一条单独的，全局的不动
						$.messager.confirm('提示', '确定新增一条适用于此流程模板的派发规则吗？', function(r){
							if (r){
								ret = $.callSyn('FlowServ','addDispatchRule',params);
								if(ret.id){
									if(ret.id =='-1'){
										$.messager.alert("提示","已存在相同配置的适用于此流程的派发规则!","info");
									}else{
										$('#dispatchRulesGrid').datagrid('appendRow',params);
										$.messager.alert("提示","修改成适用于此流程的派发规则成功","info");
										$('#dispatchRuleInfoWin').dialog('close');
									}
								}else{
									$.messager.alert("提示","修改成适用于此流程的派发规则失败","info");
								}
							}
						});
					}
				}
			}
		},{
			text:'取消',
			handler:function(){
				$('#dispatchRuleInfoWin').dialog('close');
			}
		}],
		onBeforeOpen:function(){
			var doAct = $('#dispatchRuleInfoWin').data('do');
			if('add'==doAct){
				$('#dispatchRuleInfoWin-area').textbox('clear');
				$('#dispatchRuleInfoWin-type').combobox('select','ARE');
				$('#dispatchRuleInfoWin-rollbackType').combobox('select','SAM');
				$('#dispatchRuleInfoWin-applyAll').combobox('select','0');
				$('#dispatchRuleInfoWin-partyType').combobox('select','SYS');
				$('#dispatchRuleInfoWin-party').textbox('clear');
				$('#dispatchRuleInfoWin-manual-partyType').combobox('select','JOB');
				$('#dispatchRuleInfoWin-manual-party').textbox('clear');
				$('#dispatchRuleInfoWin-callType').combobox('select','AYN');
				$('#dispatchRuleInfoWin-sys-biz').textbox('clear');
				$('#dispatchRuleInfoWin-isAutomaticReturn').combobox('select','1');
				$('#dispatchRuleInfoWin-isAutoManual').combobox('select','1');
				$('#dispatchRuleInfoWin-isReverseAutomaticReturn').combobox('select','1');
				$('#dispatchRuleInfoWin-isReverseAutomaticManual').combobox('select','1');
				$("#dispatchRuleInfoWin").dialog('setTitle','新增派发规则');
			}else if('mod'==doAct){
				var row = $('#dispatchRulesGrid').datagrid('getSelected');
				if(row){
					$('#dispatchRuleInfoWin-area').textbox('setValue',row.areaId);
					$('#dispatchRuleInfoWin-area').textbox('setText',row.areaName);
					$('#dispatchRuleInfoWin-type').combobox('select',row.type);
					$('#dispatchRuleInfoWin-rollbackType').combobox('select',row.rollbackType);
					$('#dispatchRuleInfoWin-applyAll').combobox('select',row.applyAll);
					$('#dispatchRuleInfoWin-partyType').combobox('select',row.partyType);
					$('#dispatchRuleInfoWin-party').textbox('setValue',row.partyId);
					$('#dispatchRuleInfoWin-party').textbox('setText',row.partyName);
					if(row.partyType=='SYS'){
						$('#dispatchRuleInfoWin-manual-partyType').combobox('select',row.manualPartyType);
						$('#dispatchRuleInfoWin-manual-party').textbox('setValue',row.manualPartyId);
						$('#dispatchRuleInfoWin-manual-party').textbox('setText',row.manualPartyName);
						$('#dispatchRuleInfoWin-callType').combobox('select',row.callType);
						$('#dispatchRuleInfoWin-sys-biz').textbox('setValue',row.bizId);
						$('#dispatchRuleInfoWin-sys-biz').textbox('setText',row.bizName);
						$('#dispatchRuleInfoWin-isAutomaticReturn').combobox('select',row.isAutomaticReturn);
						$('#dispatchRuleInfoWin-isAutoManual').combobox('select',row.isAutoManual);
						$('#dispatchRuleInfoWin-isReverseAutomaticReturn').combobox('select',row.isReverseAutomaticReturn);
						$('#dispatchRuleInfoWin-isReverseAutomaticManual').combobox('select',row.isReverseAutomaticManual);
					}
					$("#dispatchRuleInfoWin").dialog('setTitle','修改派发规则');
				}else{
					$.messager.alert("提示","请选择要修改的行","info");
				}
			}
		}
	});
	/*区域*/
	$('#dispatchRuleInfoWin-area').textbox({
		width:120,
		readonly:true,
		required:true,
		icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				$('#dispatchRule-areaWin').dialog('open');
			}
		}],
		onChange:function(newValue,oldValue){
			
		}
	});
	/*派发规则类型*/
	$('#dispatchRuleInfoWin-type').combobox({
		editable:false,
		panelHeight:50,
		width:120,
		valueField:'value',
		textField:'label',
		data: [{
			label: '按区域派单',
			value: 'ARE'
		},{
			label: '自定义派单规则',
			value: 'DEF'
		}]
	});
	/*回退方式*/
	$('#dispatchRuleInfoWin-rollbackType').combobox({
		editable:false,
		panelHeight:50,
		width:120,
		valueField:'value',
		textField:'label',
		data: [{
			label: '自动回滚方式',
			value: 'ROL'
		},{
			label: '同派单配置',
			value: 'SAM'
		}]
	});
	/*是否使用于所有流程*/
	$('#dispatchRuleInfoWin-applyAll').combobox({
		editable:false,
		panelHeight:50,
		width:120,
		valueField:'value',
		textField:'label',
		data: [{
			label: '否',
			value: '0'
		},{
			label: '是',
			value: '1'
		}]
	});
	/*参与人类型*/
	$('#dispatchRuleInfoWin-partyType').combobox({
		editable:false,
		panelHeight:100,
		width:120,
		valueField:'value',
		textField:'label',
		data: [{
			label: '人员',
			value: 'STA'
		},{
			label: '组织',
			value: 'ORG'
		},{
			label: '职位',
			value: 'JOB'
		},{
			label: '系统',
			value: 'SYS'
		}],
		onChange:function(newValue,oldValue){
			$('#dispatchRuleInfoWin-party').textbox('clear');
			$('#dispatchRuleInfoWin-manual-party').textbox('clear');
			$('#dispatchRuleInfoWin-sys-biz').textbox('clear');
			if(newValue!='SYS'){//非选择系统
				$('#dispatchRuleInfoWin').dialog('resize',{width:600,height:240});
				$('#dispatchRuleInfoWin-partyType-sys').hide();
			}
			switch(newValue){
				case "SYS":
					if(oldValue!=''){//非初始选择
						$('#dispatchRuleInfoWin').dialog('resize',{width:600,height:440});
						$('#dispatchRuleInfoWin-partyType-sys').show();
					}
					$('#dispatchRuleInfoWin-manual-partyType').combobox('select','STA');
					$('#dispatchRuleInfoWin-manual-party').textbox('clear');
					$('#dispatchRuleInfoWin-callType').combobox('select','AYN');
					$('#dispatchRuleInfoWin-sys-biz').textbox('clear');
					$('#dispatchRuleInfoWin-isAutomaticReturn').combobox('select','1');
					$('#dispatchRuleInfoWin-isAutoManual').combobox('select','1');
					$('#dispatchRuleInfoWin-isReverseAutomaticReturn').combobox('select','1');
					$('#dispatchRuleInfoWin-isReverseAutomaticManual').combobox('select','1'); 
					$('#dispatchRuleInfoWin-partyName').text('系统名称');
					break;
				case "JOB":
					$('#dispatchRuleInfoWin-partyName').text('职位名称');
					break;
				case "ORG":
					$('#dispatchRuleInfoWin-partyName').text('组织名称');
					break;
				case "STA":
					$('#dispatchRuleInfoWin-partyName').text('人员名称');
					break;	
				default:
					$.messager.alert("提示","未识别的选择"+newValue,"info");
			}
		}
	});
	/*参与人名称*/
	$('#dispatchRuleInfoWin-party').textbox({
		width:120,
		readonly:true,
		required:true,
		icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				var partyType = $('#dispatchRuleInfoWin-partyType').combobox('getValue');
				$('#dispatchRuleInfoWin-party').data('choice','1');
				switch(partyType){
					case "SYS":
						$('#dispatchRule-sysWin').dialog('open');
						break;
					case "JOB":
						$('#dispatchRule-jobWin').dialog('open');
						break;
					case "ORG":
						$('#dispatchRule-orgWin').dialog('open');
						break;
					case "STA":
						$('#dispatchRule-staWin').dialog('open');
						break;	
					default:
						$.messager.alert("提示","未识别的选择"+partyType,"info");
				}
			}
		}]
	});
	/*人工执行类型*/
	$('#dispatchRuleInfoWin-manual-partyType').combobox({
		editable:false,
		panelHeight:75,
		width:120,
		valueField:'value',
		textField:'label',
		data: [{
			label: '人员',
			value: 'STA'
		},{
			label: '组织',
			value: 'ORG'
		},{
			label: '职位',
			value: 'JOB'
		}],
		onChange:function(newValue,oldValue){
			$('#dispatchRuleInfoWin-manual-party').textbox('clear');
			switch(newValue){
				case "JOB":
					$('#dispatchRuleInfoWin-manual-partyName').text('职位名称');
					break;
				case "ORG":
					$('#dispatchRuleInfoWin-manual-partyName').text('组织名称');
					break;
				case "STA":
					$('#dispatchRuleInfoWin-manual-partyName').text('人员名称');
					break;	
				default:
					$.messager.alert("提示","未识别的选择"+newValue,"info");
			}
		}
	});
	/*执行人名称*/
	$('#dispatchRuleInfoWin-manual-party').textbox({
		width:120,
		readonly:true,
		required:true,
		icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				var partyType = $('#dispatchRuleInfoWin-manual-partyType').combobox('getValue')
				$('#dispatchRuleInfoWin-party').data('choice','2');
				switch(partyType){
					case "JOB":
						$('#dispatchRule-jobWin').dialog('open');
						break;
					case "ORG":
						$('#dispatchRule-orgWin').dialog('open');
						break;
					case "STA":
						$('#dispatchRule-staWin').dialog('open');
						break;	
					default:
						$.messager.alert("提示","未识别的选择"+partyType,"info");
				}
			}
		}]
	});
	/*调用方式*/
	$('#dispatchRuleInfoWin-callType').combobox({
		editable:false,
		panelHeight:75,
		width:120,
		valueField:'value',
		textField:'label',
		data: [{
			label: '同步调用',
			value: 'AYN'
		},{
			label: '消息队列',
			value: 'JMS'
		},{
			label: '定时任务',
			value: 'TIM'
		}]
	});
	/*调用组件*/
	$('#dispatchRuleInfoWin-sys-biz').textbox({
		width:120,
		readonly:true,
//		required:true,
		icons: [{
			iconCls:'icon-tip',
			handler: function(e){
				$('#dispatchRule-bizWin').dialog('open');
			}
		}]
	});
	/*自动组件是否自动回单*/
	$('#dispatchRuleInfoWin-isAutomaticReturn').combobox({
		editable:false,
		panelHeight:50,
		width:120,
		valueField:'value',
		textField:'label',
		data: [{
			label: '否',
			value: '0'
		},{
			label: '是',
			value: '1'
		}]
	});
	/*失败是否自动转人工*/
	$('#dispatchRuleInfoWin-isAutoManual').combobox({
		editable:false,
		panelHeight:50,
		width:120,
		valueField:'value',
		textField:'label',
		data: [{
			label: '否',
			value: '0'
		},{
			label: '是',
			value: '1'
		}]
	});
	/*回滚是否自动回单*/
	$('#dispatchRuleInfoWin-isReverseAutomaticReturn').combobox({
		editable:false,
		panelHeight:50,
		width:120,
		valueField:'value',
		textField:'label',
		data: [{
			label: '否',
			value: '0'
		},{
			label: '是',
			value: '1'
		}]
	});
	/*反向失败是否自动转人工*/
	$('#dispatchRuleInfoWin-isReverseAutomaticManual').combobox({
		editable:false,
		panelHeight:50,
		width:120,
		valueField:'value',
		textField:'label',
		data: [{
			label: '否',
			value: '0'
		},{
			label: '是',
			value: '1'
		}]
	});
	$('a.textbox-icon.icon-tip.textbox-icon-disabled').removeClass("textbox-icon-disabled");//使查询tip有效
	/**区域dialog*/
	$('#dispatchRule-areaWin').dialog({
		width:300,
		height:350,
		closed:true,
		maximizable:true,
		cache:false,
		modal:true,
		iconCls:'icon-search',
		title:'选择区域',
		buttons:[{
			text:'确定',
			handler:function(){
				var selArea = $('#dispatchRule-areaTree').tree('getSelected');
				if(selArea){
					$('#dispatchRuleInfoWin-area').textbox('setValue',selArea.id);
					$('#dispatchRuleInfoWin-area').textbox('setText',selArea.text);
				}else{
					$('#dispatchRuleInfoWin-area').textbox('clear');
				}
				$('#dispatchRule-areaWin').dialog('close');
			}
		},{
			text:'取消',
			handler:function(){
				$('#dispatchRule-areaWin').dialog('close');
			}
		}],
		onBeforeOpen:function(){
			var ret = $.callSyn("InteractiveServ","getAreaTree",{});
			$('#dispatchRule-areaTree').tree({
				data:ret
			});
			var root = $('#dispatchRule-areaTree').tree('getRoot');
			if(root){
				$('#dispatchRule-areaTree').tree('expand',root.target);
			}
		}
	});
	/**系统dialog*/
	$('#dispatchRule-sysWin').dialog({
		width:300,
		height:300,
		closed:true,
		maximizable:true,
		cache:false,
		modal:true,
		iconCls:'icon-search',
		title:'选择系统',
		buttons:[{
			text:'确定',
			handler:function(){
				var selSys = $('#dispatchRule-sysWin-datagrid').datagrid('getSelected');
				var selParty = $('#dispatchRuleInfoWin-party');
//				if($('#dispatchRuleInfoWin-party').data('choice')=='2'){
//					selParty = $('#dispatchRuleInfoWin-manual-party');
//				}
				if(selSys){
					selParty.textbox('setValue',selSys.id);
					selParty.textbox('setText',selSys.text);
				}else{
					selParty.textbox('clear');
				}
				$('#dispatchRule-sysWin').dialog('close');
			}
		},{
			text:'取消',
			handler:function(){
				$('#dispatchRule-sysWin').dialog('close');
			}
		}],
		onBeforeOpen:function(){
			var ret = $.callSyn("InteractiveServ","getSystemTree",{});
			$('#dispatchRule-sysWin-datagrid').datagrid('loadData',ret);
		}
	});
	$('#dispatchRule-sysWin-datagrid').datagrid({
		fit:true,
		autoRowHeight:false,
	   	fitColumns:false,
	   	singleSelect:true,
		columns:[[
	        {title:'系统',field:'text',width:280},
			{title:'系统id',field:'id',hidden:true}
	    ]]
	});
	/**组织dialog*/
	$('#dispatchRule-orgWin').dialog({
		width:300,
		height:200,
		closed:true,
		maximizable:true,
		cache:false,
		modal:true,
		iconCls:'icon-search',
		title:'选择组织',
		buttons:[{
			text:'确定',
			handler:function(){
				var selOrg = $('#dispatchRule-orgTree').tree('getSelected');
				var selParty = $('#dispatchRuleInfoWin-party');
				if($('#dispatchRuleInfoWin-party').data('choice')=='2'){
					selParty = $('#dispatchRuleInfoWin-manual-party');
				}
				if(selOrg){
					selParty.textbox('setValue',selOrg.id);
					selParty.textbox('setText',selOrg.text);
				}else{
					selParty.textbox('clear');
				}
				$('#dispatchRule-orgWin').dialog('close');
			}
		},{
			text:'取消',
			handler:function(){
				$('#dispatchRule-orgWin').dialog('close');
			}
		}],
		onBeforeOpen:function(){
			var ret = $.callSyn("InteractiveServ","getOrgTree",{});
			$('#dispatchRule-orgTree').tree({
				data:ret
			});
		}
	});
	/**职位dialog*/
	$('#dispatchRule-jobWin').dialog({
		width:600,
		height:300,
		closed:true,
		maximizable:true,
		cache:false,
		modal:true,
		iconCls:'icon-search',
		title:'选择职位',
		buttons:[{
			text:'确定',
			handler:function(){
				var selJob = $('#dispatchRule-jobWin-datagrid').datagrid('getSelected');
				var selParty = $('#dispatchRuleInfoWin-party');
				if($('#dispatchRuleInfoWin-party').data('choice')=='2'){
					selParty = $('#dispatchRuleInfoWin-manual-party');
				}
				if(selJob){
					selParty.textbox('setValue',selJob.id);
					selParty.textbox('setText',selJob.text);
				}else{
					selParty.textbox('clear');
				}
				$('#dispatchRule-jobWin').dialog('close');
			}
		},{
			text:'取消',
			handler:function(){
				$('#dispatchRule-jobWin').dialog('close');
			}
		}],
		onBeforeOpen:function(){
			var ret = $.callSyn("InteractiveServ","getOrgTree",{});
			$('#dispatchRule-jobWin-orgTree').tree({
				data:ret,
				onClick: function(node){
					var jobRet = $.callSyn("InteractiveServ","getJobTree",{orgId:node.id});
					$('#dispatchRule-jobWin-datagrid').datagrid('loadData',jobRet);
				}
			});
			var root = $('#dispatchRule-jobWin-orgTree').tree('getRoot');
			if(root){
				$('#dispatchRule-jobWin-orgTree').tree('select',root.target);
				var jobRet = $.callSyn("InteractiveServ","getJobTree",{orgId:root.id});
				$('#dispatchRule-jobWin-datagrid').datagrid('loadData',jobRet);
			}
		}
	});
	$('#dispatchRule-jobWin-layout').layout({
		fit:true
	});
	$('#dispatchRule-jobWin-layout').layout('add',{
	    region: 'west',
	    width: 150,
	    title: '组织',
	    split: true,
	    content:'<ul id="dispatchRule-jobWin-orgTree" class="easyui-tree"></ul>'
	});
	$('#dispatchRule-jobWin-layout').layout('add',{
	    region: 'center',
	    title: '职位',
	    content:'<table id="dispatchRule-jobWin-datagrid"></table>'
	});
	$('#dispatchRule-jobWin-datagrid').datagrid({
		fit:true,
		autoRowHeight:false,
	   	fitColumns:true,
	   	singleSelect:true,
		columns:[[
	        {title:'职位',field:'text',width:150},
			{title:'岗位',field:'postName',width:150},
			{title:'备注',field:'comment',width:150},
			{title:'职位id',field:'id',hidden:true},
			{title:'组织id',field:'orgId',hidden:true}
	    ]]
	});
	/**人员dialog*/
	$('#dispatchRule-staWin').dialog({
		width:600,
		height:400,
		closed:true,
		maximizable:true,
		cache:false,
		modal:true,
		iconCls:'icon-search',
		title:'选择人员',
		buttons:[{
			text:'确定',
			handler:function(){
				var selSta = $('#dispatchRule-staWin-datagrid').datagrid('getSelected');
				var selParty = $('#dispatchRuleInfoWin-party');
				if($('#dispatchRuleInfoWin-party').data('choice')=='2'){
					selParty = $('#dispatchRuleInfoWin-manual-party');
				}
				if(selSta){
					selParty.textbox('setValue',selSta.id);
					selParty.textbox('setText',selSta.text);
				}else{
					selParty.textbox('clear');
				}
				$('#dispatchRule-staWin').dialog('close');
			}
		},{
			text:'取消',
			handler:function(){
				$('#dispatchRule-staWin').dialog('close');
			}
		}],
		onBeforeOpen:function(){
			var ret = $.callSyn("InteractiveServ","getOrgTree",{});
			$('#dispatchRule-staWin-orgTree').tree({
				data:ret,
				onClick: function(node){
					var staRet = $.callSyn("InteractiveServ","getStaffTree",{orgId:node.id,pageIndex:'1',pageSize:'100'});//暂不考虑分页
					$('#dispatchRule-staWin-datagrid').datagrid('loadData',staRet);
				}
			});
			var root = $('#dispatchRule-staWin-orgTree').tree('getRoot');
			if(root){
				$('#dispatchRule-staWin-orgTree').tree('select',root.target);
				var staRet = $.callSyn("InteractiveServ","getStaffTree",{orgId:root.id,pageIndex:'1',pageSize:'100'});//暂不考虑分页
				$('#dispatchRule-staWin-datagrid').datagrid('loadData',staRet);
			}
		}
	});
	$('#dispatchRule-staWin-layout').layout({
		fit:true
	});
	$('#dispatchRule-staWin-layout').layout('add',{
	    region: 'west',
	    width: 150,
	    title: '组织',
	    split: true,
	    content:'<ul id="dispatchRule-staWin-orgTree" class="easyui-tree"></ul>'
	});
	$('#dispatchRule-staWin-layout').layout('add',{
	    region: 'center',
	    title: '人员',
	    content:'<table id="dispatchRule-staWin-datagrid"></table>'
	});
	$('#dispatchRule-staWin-datagrid').datagrid({
		fit:true,
		autoRowHeight:false,
	   	fitColumns:false,
	   	singleSelect:true,
		columns:[[
	        {title:'人员',field:'text',width:150},
			{title:'用户名',field:'userName',width:120},
			{title:'职位',field:'jobName',width:150},
			{title:'电话',field:'officeTel',width:100},
			{title:'移动电话',field:'mobileTel',width:100},
			{title:'职位是否为缺省',field:'isBasic',width:50},
			{title:'人员id',field:'id',hidden:true},
			{title:'组织id',field:'orgId',hidden:true}
	    ]]
	});
	/**组件dialog*/
	$('#dispatchRule-bizWin').dialog({
		width:300,
		height:400,
		closed:true,
		maximizable:true,
		cache:false,
		modal:true,
		iconCls:'icon-search',
		title:'选择组件',
		buttons:[{
			text:'确定',
			handler:function(){
				var selBiz = $('#dispatchRule-bizWin-datagrid').datagrid('getSelected');
				if(selBiz){
					$('#dispatchRuleInfoWin-sys-biz').textbox('setValue',selBiz.id);
					$('#dispatchRuleInfoWin-sys-biz').textbox('setText',selBiz.text);
				}else{
					$('#dispatchRuleInfoWin-sys-biz').textbox('clear');
				}
				$('#dispatchRule-bizWin').dialog('close');
			}
		},{
			text:'取消',
			handler:function(){
				$('#dispatchRule-bizWin').dialog('close');
			}
		}],
		onBeforeOpen:function(){
			var ret = $.callSyn("InteractiveServ","getBizObjTree",{});
			$('#dispatchRule-bizWin-datagrid').datagrid('loadData',ret);
		}
	});
	$('#dispatchRule-bizWin-datagrid').datagrid({
		fit:true,
		autoRowHeight:false,
	   	fitColumns:false,
	   	singleSelect:true,
		columns:[[
	        {title:'组件',field:'text',width:280},
			{title:'组件id',field:'id',hidden:true}
	    ]]
	});
	
	//loading
	var params ={
		packageDefineId:$("#dispatchRulesWin").data("packageDefineId"),
		tacheCode:$("#dispatchRulesWin").data("tacheCode")
	};
	var ret = $.callSyn('FlowServ','qryDispatchRuleByCond',params);
	$('#dispatchRulesGrid').datagrid('loadData',ret);
});


