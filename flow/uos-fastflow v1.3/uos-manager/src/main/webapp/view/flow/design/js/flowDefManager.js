$(function($){
	//对象：初始化绘制区域管理对象
	var workArea = new WorkArea();
	var session = $.session();
	
	//方法：初始化环节树
	var initTacheTree = function()
	{
		var tacheTree = $.callSyn('TacheServ','qryTacheCatalogTree',{systemCode:session["systemCode"]});
		$('#tacheTree').tree({
			data : tacheTree,
			onClick:function(node)
			{
				if(node.type==1)
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
					if(!node.type){
						workArea.paintBeforeAdd("Tache",node);
					}
				}
			},
			onContextMenu: function(e, node){
				e.preventDefault();
			}
		});
	};
	
	//方法：初始化区域树
	var initAreaTree = function()
	{
		//areaId为-1加载全部
		var areaTreeData = $.callSyn("AreaServ","getAreaJsonTree",{areaId: session["areaId"]});
		$('#areaTree').tree({
			data : areaTreeData,
			onSelect:function(node)
			{
				var flowTreeData = $.callSyn("FlowServ","queryPackageCatalogByAreaIdAndSystemCode",{areaId: node.id,systemCode:session["systemCode"]});
				initFlowTree(flowTreeData);
			},
			onContextMenu: function (e, node) {
                e.preventDefault();
            }
		});
	};
	
	//方法：初始化流动定义树：
	//flowTreeData:流程定义数的json格式数据
	var initFlowTree = function(flowTreeData)
	{
		var subLoad = function(node){
				if(node.type==1&&!node.loaded)
				{
					//加载环节列表
					var ret = $.callSyn('FlowServ','qryPackageDefineByCatalogId',{catalogId:node.id});
					node.loaded = true;
					$('#flowTree').tree('append', { parent : node.target,  data : ret });  
				}
		}
		$('#flowTree').tree({
			data : flowTreeData,
			onExpand:function(node){
				subLoad(node);
			},
			onClick:function(node)
			{
				subLoad(node);
			},
		 	onContextMenu: function (e, node) {
                e.preventDefault();
				switch(node.type)
				{
					//目录
					case 1:
						var pp = $('#flowTree').tree("getParent",node.target);
						if(pp)
						{
							$(".topflag").show();
						}else{
							$(".topflag").hide();
						}
						$("#catalogMenu").menu('show', {
		                    left: e.pageX,
		                    top: e.pageY
		                }).data("node", node);
						break;
					//模板
					case 2:
						$("#packageMenu").menu('show', {
		                    left: e.pageX,
		                    top: e.pageY
		                }).data("node", node);
						break;
					//版本
					case 3:
						if(node.text.indexOf("失效")!=-1){//只显示打开目录
							$("#flowMenu > .menu-item[name!='openVersion']").hide();
						}else{
							$("#flowMenu > .menu-item").show();
						}
						$("#flowMenu").menu('show', {
		                    left: e.pageX,
		                    top: e.pageY
		                }).data("node", node);
						break;
				}
            }
		});
	};

	var msg = false;
	//方法：初始化流程建模展示tab
	var initFlowTab = function()
	{
		workArea.clear();
		$('#flowTab').tabs("add",{
			title: '没有打开的流程图',
			selected: true
		}).tabs({
			onClose:function(title,index)
			{
				if(title=="没有打开的流程图")
				{
					return;
				}
				var tab = $('#flowTab').tabs('getTab',0);
				if(!tab)
				{
					initFlowTab();
				}
				
			},
			onBeforeClose:function(title,index){
					if(title=="没有打开的流程图"){
						return true;
					}
					if(workArea.isDirty()){
						msg = true;
						$.messager.confirm("确认","当前流程被修改了，请确认是否保存版本？",function(r){
							if(r){
								var node = $('#flowTree').tree('getSelected');
								if (node.text.indexOf("激活") != -1) {// 模板处于激活状态
									// modify by bobping 
									$.messager.defaults = { ok: "是", cancel: "否" };
									$.messager.confirm("确认", "为避免对已在途的流程造成影响，对流程的修改是否另存为新版本？", function(ret) {
										if (ret) {//是
											var xpdl = workArea.generateXPDL();
											saveAsVersion(node, xpdl);
											$.messager.alert("提示", "流程已另存为新版本",
													"info");
											$('#flowTab').tabs("close",index);
											return;
										}else{
											saveFlow();
											return;
										}
									});
		
								}else{
									saveFlow();
								}
							}else{
								workArea.setDirty(false);
								$('#flowTab').tabs("close",index);
							}
					
						});
					return false;
				}
			},
			onSelect:function(title,index)
			{
				workArea.paint();
			}
		});
	};
	
	//界面初始化------start--------------
	initTacheTree();
	initAreaTree();
	$('#areaTree').tree("select",$("#areaTree").tree("getRoot").target);
	initFlowTree();
	initFlowTab();
//	$('#flowPanel').layout('collapse','north');//初始化折叠流程图页面
	//界面初始化------end--------------
	
	//流程目录的菜单操作--------------start------------------
	//流程目录操作：新增子目录和修改目录----start-------
	$("#catalogWin-confirmBtn").click(function(evt)
	{
		var catalog = $("#catalogWin-catalogText").val();
		if($.trim(catalog).length==0){
			$.messager.alert("提示","流程目录不能为空!","info");
			return;
		}
		var node = $("#catalogWin").data("node");//菜单操作对应的流程定义树的节点
		var type = $("#catalogWin").data("type");//1 ：表示增加子目录  2： 表示修改目录
		var params = {};
		params.catalogName = catalog;
		params.areaId = node.areaId;
		params.systemCode = node.systemCode;
		if(type==1)//增加子目录
		{
			params.parentId = node.id;
			params.pathCode = node.pathCode;
			var ret = $.callSyn("FlowServ","addPackageCatalog",params);
			$('#flowTree').tree('append', { parent : node.target,  data : ret });  
		}else if(type==2)//修改目录名称
		{
			params.id = node.id;
			var ret = $.callSyn("FlowServ","updatePackageCatalog",params);
			$('#flowTree').tree('update', { target : node.target,  text : params.catalogName });  
		}
		$("#catalogWin").dialog("close");
	});
	//流程目录操作：新增目录和新增子目录----end--------
	
	//流程目录操作：新增流程模板和修改流程模板----start-------
	$("#packageWin-confirmBtn").click(function(evt)
	{
		var node = $("#packageWin").data("node");
		var type = $("#packageWin").data("type");
		var packageName = $("#packageWin-packageName").val();
		if($('#packageForm').form('validate')){
			var packageType = $("#packageWin-packageType").combobox("getValue");
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
			
			if(type==1)//新增流程模板
			{
				var params = {};
				params.areaId = node.areaId;
				params.packageName = packageName;
				params.packageType = packageType;
				params.parentId = node.id;
				params.effDate = effDate;
				params.expDate = expDate;
				var ret = $.callSyn("FlowServ","addPackage",params);
				$('#flowTree').tree('append', { parent : node.target,  data : ret });  
				$("#packageWin").dialog("close");
			}else if(type==2){//修改流程模板
				var ret = $.callSyn("FlowServ","updatePackage",{packageName:packageName,id:node.id,effDate:effDate,expDate:expDate});
				$('#flowTree').tree('update', { target : node.target,  text : packageName });  
				$("#packageWin").dialog("close");
			}
		}
		
	});
	//流程目录操作：新增流程模板----end--------
	
	//流程定义操作：新增流程定义----start-------
	$("#pDefWin-confirmBtn").click(function(evt)
	{
		var node = $("#pDefWin").data("node");
		var type = $("#pDefWin").data("type");
		

		var defCode = $("#pDefWin-defCode").val();
		if($.trim(defCode).length==0){
			$.messager.alert("提示","流程定义编码不能为空!","info");
			return;
		}
		var checkExitObj =  $.callSyn("FlowServ","findProcessDefinitionByCode",{packageDefineCode:defCode});
		if(checkExitObj&&checkExitObj.packageDefineCode){
			$.messager.alert("提示","该流程定义编码已经存在，请更换编码!","info");
			return;
		}
		
		var params = {};
		params.parentId = node.id;
		params.name = node.text;
		params.editUser = session["staffName"]; 
		params.packageDefineCode = defCode;
		var ret = $.callSyn("FlowServ","addProcessDefine",params);
		$('#flowTree').tree('append', { parent : node.target,  data : ret });  
		$("#pDefWin").dialog("close");
	});
	//流程定义操作：新增流程定义----end--------
	
	//流程目录菜单初始化-------start-----------
    $('#catalogMenu').menu({
        onClick : function (item) {
			var node = $(this).data("node");
			switch(item.name)
			{
				//新增流程子目录
				case "newSubCatalog":
					$("#catalogWin-catalogText").textbox("setValue","");
					$("#catalogWin").data("node",node);
					$("#catalogWin").data("type",1);//添加子目录
					$("#catalogWin").dialog({title:item.text});
					$("#catalogWin").dialog("open");
					break;
				//修改流程目录
				case "modifyCatalog":
					$("#catalogWin-catalogText").textbox("setValue",node.text);
					$("#catalogWin").data("node",node);
					$("#catalogWin").data("type",2);//修改目录名称
					$("#catalogWin").dialog({title:item.text});
					$("#catalogWin").dialog("open");
					break;
				//删除流程目录
				case "deleteCatalog":
					var ret = $.callSyn("FlowServ","qryPackageDefineByCatalogId",{catalogId:node.id});
					if(ret.length!=0)
					{
						$.messager.alert("提示","该目录下存在流程定义，不能删除！","info");
					}else{
						$.messager.confirm("确认","是否确认删除该流程目录:"+node.text+"?",function(r){
							if(r)
							{
								$.callSyn("FlowServ","deletePackageCatalog",{catalogId:node.id});
								$("#flowTree").tree("remove",node.target);
								$.messager.alert("提示","删除该流程目录:"+node.text+".成功！","info");
							}
						});
					}
					break;
				//新增流程模板
				case "newPackage":
					$("#packageWin").data("node",node);
					$("#packageWin").data("type",1);
					$("#packageWin").dialog({title:"新增流程模板"});
					$("#packageWin-packageName").textbox("setValue","");
					$("#packageWin-packageType").combobox("setValue","COMP");
					$("#packageWin-packageType").combobox("enable");
					$('#effDate').datetimebox('setValue',"");
					$('#expDate').datetimebox('setValue',"");
					$("#packageWin").dialog("open");
					break;
				default:
					break;
			}
        }
    });
	//流程目录菜单初始化-------end-----------
	//流程目录的菜单操作--------------end------------------
	
	//流程模板的菜单操作--------------start------------------
    $('#packageMenu').menu({
        onClick : function (item) {
			var node = $(this).data("node");
			switch(item.name)
			{
				//修改流程模板
				case "modifyPackage":
					$("#packageWin").data("node",node);
					$("#packageWin").data("type",2);
					$("#packageWin").dialog({title:item.text});
					$("#packageWin-packageName").textbox("setValue",node.text);
					$("#packageWin-packageType").combobox("setValue",node.packageType);
					$("#packageWin-packageType").combobox("disable");
					$('#effDate').datetimebox('setValue',node.effDate);
					$('#expDate').datetimebox('setValue',node.expDate);
					$("#packageWin").dialog("open");
					break;
				//删除流程模板
				case "deletePackage":
					var ret = $.callSyn("FlowServ","qryPackageDefineByPackageId",{packageId:node.id});
					if(ret.length!=0)
					{
						$.messager.alert("提示","该模板下存在流程定义，不能删除！","info");
					}else{
						$.messager.confirm("确认","是否确认删除该流程模板:"+node.text+"?",function(r){
							if(r)
							{
								$.callSyn("FlowServ","deletePackage",{packageId:node.id});
								$("#flowTree").tree("remove",node.target);
								$.messager.alert("提示","删除该流程模板:"+node.text+".成功！","info");
							}
						});
					}
					break;
				//另存流程模板
				case "saveAsPackage":
					break;
				//新增流程版本
				case "newVersion":
					$("#pDefWin-defCode").textbox("clear");
					$("#pDefWin").data("node",node);
					$("#pDefWin").data("type",1);//新增流程定义
					$("#pDefWin").dialog({title:"新增流程定义"});
					$("#pDefWin").dialog("open");
					break;
				default:
					alert(item.text);
					break;
			}
        }
    });
	//流程模板的菜单操作--------------end------------------
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
	//流程定义的菜单操作--------------start-------------------
	//流程定义操作：打开和绘制流程----start----------
	var addTab = function(node, xpdl, enableEdit){
		var id = node.id;
		var tab = $('#flowTab');
		var title = "";
		for(var n = node;n;)
		{
			title = n.text+"/"+title;
			n = $('#flowTree').tree("getParent",n.target);
		}
		//modify by bobping 增加流程模板编的显示
		if (tab.tabs('exists', title)){//如果tab已经存在,则选中并刷新该tab    	
			workArea.paintTitle(id,title+"[流程模板编码:"+node.code+"]/",enableEdit);
	        tab.tabs('select', title);
		} else {
			if (tab.tabs('exists', "没有打开的流程图")){//如果tab已经存在,则选中并刷新该tab    	
		        tab.tabs('close', 0);
			}
			workArea.addFlow(id,xpdl);
			workArea.paintTitle(id,title+"[流程模板编码:"+node.code+"]/",enableEdit);
			tab.tabs('add',{
		    	title:title,
				id:id,
				code:node.code,
		    	closable:true
	    	});
		}
	};
	var openFlow = function(node,enbaleEdit)
	{
		//todo---查询流程定义
		var ret = $.callSyn("FlowServ","getXPDL",{processDefineId:node.id});
		if(enbaleEdit||ret.xpdl)
		{
			addTab(node,ret.xpdl,enbaleEdit);
		}else{
			$.messager.alert('提示','该流程定义还未绘制！','info');
		}
	};
	//流程定义操作：打开和绘制流程----end-------------
	//流程定义操作：保存流程----start--------
	var saveFlow = function(node)
	{
		if(msg){
			var xpdl = workArea.generateXPDL();
			$.callSyn("FlowServ", "saveXPDL", {
				processDefineId: workArea.getCurId(),
				xpdl: xpdl
			});
			workArea.setDirty(false);
			$.messager.alert("提示","保存流程定义成功!","info");
			$('#flowTab').tabs("close",index);
			return ;
		}
		var id = workArea.getCurId();
		if(!id)
		{
			$.messager.alert("提示","没有打开的流程定义!","warn");
			return;
		}
		$.messager.confirm("确认","是否确认保存为当前流程定义版本？",function(r){
			if (r) {
				var xpdl = workArea.generateXPDL();
				$.callSyn("FlowServ", "saveXPDL", {
					processDefineId: workArea.getCurId(),
					xpdl: xpdl
				});
				workArea.setDirty(false);
				$.messager.alert("提示","保存流程定义成功!","info");
			}
		});
	};
	//流程定义操作：保存流程----end--------
	//流程定义操作：强制流程失效----start--------
	var disableFlow = function(node)
	{
		var ret = $.callSyn("FlowServ", "updateFlowState", {
					processDefineId: node.id,
					enable: false
				});
		$("#flowTree").tree("update",{target: node.target,text:ret.text});
		$.messager.alert("提示","流程模板强制失效成功!","info");
	};
	//流程定义操作：强制流程失效----end--------
	//流程定义操作：强制流程生效----start--------
	var enableFlow = function(node)
	{
		var ret = $.callSyn("FlowServ", "updateFlowState", {
					processDefineId: node.id,
					enable: true
				});
		$("#flowTree").tree("update",{target: node.target,text:ret.text});
		$.messager.alert("提示","流程模板强制生效成功!","info");
	};
	//流程定义操作：强制流程生效----end--------
	//流程定义菜单初始化------start-------------
    $('#flowMenu').menu({
        onClick : function (item) {
			var node = $(this).data("node");
			switch(item.name)
			{
				case "startFlow":
					if(node.text.indexOf("激活")==-1)
					{
						$.messager.alert("提示","该流程定义处于非激活状态,不能启动！","info");
					}else{
						// modify che.zi 2015-08-11
						$('#startFlowParamModify').hide();
						$("#startResult").hide();
						$("#startFlowParamWin-buttons").show();
						var title = "";
						var parentNode = $('#flowTree').tree("getParent",node.target);
						for(var n = parentNode;n;)
						{
							title = n.text+"/"+title;
							n = $('#flowTree').tree("getParent",n.target);
						}
						$("#startFlowParamWin").data('packageDefineId',node.id);
						$("#startFlowParamWin").data('type','FLOW');
						$("#startFlowParamWin").data('tacheCode','-1');
						$("#startFlowParamWin").data('processDefineName',parentNode.text);
						$("#startFlowParamWin").data('title',title);
						$("#startFlowParamWin").dialog('open');
					}
					break;
				//打开
				case "openVersion":
					openFlow(node,false);
					break;
				//绘制
				case "editVersion":
					var isHasPrivlege = $.callSyn("PrivlegeServ","isExistButtonPriv",{staffId:session["staffId"],privlegeCode:'editVersion'});
					if("false"==isHasPrivlege){
						$.messager.alert("提示","该用户没有绘制流程版本的权限，不能绘制","info");
						return;
					}
					openFlow(node,true);
					break;
				//绘制（fish）
				case "editVersionFish":
					var isHasPrivlege = $.callSyn("PrivlegeServ","isExistButtonPriv",{staffId:session["staffId"],privlegeCode:'editVersion'});
					if("false"==isHasPrivlege){
						$.messager.alert("提示","该用户没有绘制流程版本的权限，不能绘制","info");
						return;
					}
//					window.location.href=webBasePATH+'common/fish/examples/bpmn/default.html?packageDefineId='+node.id+'&systemCode='+session["systemCode"];
					var url=webBasePATH+'common/fish/examples/bpmn/default.html?packageDefineId='+node.id+'&systemCode='+session["systemCode"];
					window.open(url);
					break;
				//强制版本失效
				case "disableVersion":
					if(node.text.indexOf("失效")!=-1){
						$.messager.alert("提示","该流程版本已经失效","info");
					}else{
						disableFlow(node);
					}
					break;
				//强制版本生效
				case "enableVersion":
					if(node.text.indexOf("激活")!=-1){
						$.messager.alert("提示","该流程版本已经激活","info");
					}else if(node.text.indexOf("失效")!=-1){
						$.messager.alert("提示","该流程版本已经失效，不能激活","info");
					}else{
						//add by che.zi 20160628 for zmp:889946 begin 
						var isHasPrivlege = $.callSyn("PrivlegeServ","isExistButtonPriv",{staffId:session["staffId"],privlegeCode:'enableVersion'});
						if("false"==isHasPrivlege){
							$.messager.alert("提示","该用户没有激活流程版本的权限，不能激活","info");
							return;
						}
						//add by che.zi 20160628 for zmp:889946 end 
						var pNode = $('#flowTree').tree('getParent',node.target);
						var cNodes = $('#flowTree').tree('getChildren',pNode.target);
						var isHas = false;
						$.each(cNodes,function(i,n){
							if(n.text.indexOf("激活")!=-1){
								isHas = true;
							}
						});
						if(isHas){
							$.messager.alert("提示","该流程已存在激活版本","info");
						}else{
							enableFlow(node);
						}
					}
					break;
				case "deleteVersion":
					var isHasPrivlege = $.callSyn("PrivlegeServ","isExistButtonPriv",{staffId:session["staffId"],privlegeCode:'deleteVersion'});
					if("false"==isHasPrivlege){
						$.messager.alert("提示","该用户没有删除流程版本的权限，不能删除","info");
						return;
					}
					if(node.text.indexOf("激活")!=-1)
					{
						$.messager.alert("提示","该流程定义处于激活状态,不能删除！","info");
					}else{
						$.messager.confirm("确认","是否确认删除该流程定义？",function(r){
							if(r){
								$.callSyn("FlowServ","deleteProcessDefine",{packageDefineId:node.id});
								$("#flowTree").tree("remove",node.target);
								$.messager.alert("提示","流程定义删除成功！","info");
							}
						});
					}
					break;
				case "saveAsVersion":
					var xpdlRet = $.callSyn("FlowServ","getXPDL",{processDefineId:node.id});
					var xpdl = xpdlRet.xpdl;
					/*var pNode = $('#flowTree').tree('getParent',node.target);
					var params = {};
					params.parentId = pNode.id;
					params.name = pNode.text;
					params.editUser = session["staffName"]; 
					var oldDefine = $.callSyn("FlowServ","findProcessDefinitionById",{processDefineId:node.id});
					var maxVersion = 0;
					var version = parseInt(oldDefine.version.split(".")[1]);
					if (maxVersion < version) {
						maxVersion = version;
					}
					maxVersion = Number(maxVersion) + Number(1);
					var oldCode =  oldDefine.packageDefineCode;
					if(oldCode.indexOf("_")>0){
						oldCode = oldCode.substring(0,oldCode.indexOf("_"));
					}
					params.packageDefineCode = oldCode + "_" + maxVersion;
					var ret = $.callSyn("FlowServ","addProcessDefine",params); 
					$.callSyn("FlowServ","saveXPDL",{processDefineId:ret.id,xpdl:xpdl});
					$('#flowTree').tree('append', { parent : pNode.target,  data : ret });*/ 
					saveAsVersion(node,xpdl);
					$.messager.alert("提示","流程版本另存为成功！","info");
					break;
				default:
					alert(item.text);
					break;
			}
        }
    });
	//流程定义菜单初始化------end-------------
	//流程定义的菜单操作--------------end-------------------
	
	//流程定义图中：环节右键菜单初始化------start---------
	$("#tacheMenu").menu({
		onClick:function(item)
		{
			var node = $(this).data("node");
			switch(item.name)
			{
				//删除
				case "deleteTache":
					workArea.deleteTache(node);
					break;
				case "editFlowParams":
					workArea.editFlowParams(node);
					break;	
				case "editDispatchRules":
					workArea.editDispatchRules(node);
					break;
				case "editFlowException":
					workArea.editFlowException(node);
					break;
				default:
					alert(item.name);
					break;
			}
		}
	});
	//流程定义图中：环节右键菜单初始化------end---------
	
	//流程定义图中：查看和设置线条组件------start---------
	var openLineWin = function(transition,isEdit)
	{	
		var condition = transition.condition;
		if(isEdit)
		{
			$("#lineWin").dialog({title:"设置线条属性"});
			$("#tr1").show();
			$("#tr2").show();
			$("#tr3").show();
			$("#operTr").show();
			$('#condition').attr('readonly',false); 
			$('#condName').textbox('enable'); 
			$("#lineWin").data("packageDefineId",workArea.getCurId());
			$("#lineWin").data("tacheCode",-1);
			$("#lineWin").data("type","FLOW");
			$("#condName").textbox('setValue',transition.name);
			$("#condition").val(transition.condition);
		}else{
			$("#lineWin").dialog({title:"查看线条属性"});
			$("#tr1").hide();
			$("#tr2").hide();
			$("#tr3").hide();
			$("#operTr").hide();
			$("#condName").textbox('setValue',transition.name);
			$('#condition').attr('readonly',true); 
			$('#condName').textbox('disable'); 
			$("#condition").val(transition.condition);
			
		}
		$("#lineWin").data("transition",transition);
		$("#lineWin").data("isDel",false);
		$("#lineWin").dialog("open");
	};
	$("#lineWin-confirmBtn").click(function(evt)
	{
		var transition = $("#lineWin").data("transition");
		var condition = $("#condition").val();
		var condName = $("#condName").val();
		var isDel = $("#lineWin").data("isDel");
		if(isDel){
			transition.condition = condition;
			transition.name = condName;
			transition.refreshText();
			workArea.setDirty(true);
			$.messager.alert("提示","线条属性删除成功！","info");
		}else{
			if(condition)
			{
				transition.condition = condition;
				transition.name = condName;
				transition.refreshText();
				workArea.setDirty(true);
				$.messager.alert("提示","线条属性设置成功！","info");
			}
		}
		$("#lineWin").dialog("close");
	});
	$("#lineWin").dialog({
		onBeforeOpen:function(){
			var flowParamDefs = $.callSyn("FlowServ","qryFlowParamDefRels",{
				packageDefineId:$("#lineWin").data('packageDefineId'),type:$("#lineWin").data('type'),tacheCode:$("#lineWin").data('tacheCode')});
			$('#paramGrid').datagrid('loadData',flowParamDefs);
			$('#paramGrid').datagrid('unselectAll');
		}
	});
	$('#paramGrid').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
		pagination:false,//
	    fitColumns:true,
	    idField:'code',
	    columns:[[
				{title:'参数编码',field:'code',width:100},
				{title:'参数值',field:'value',width:100},
				{title:'类型',field:'type',width:100,formatter:function(value,row,index){
					if(value=='FLOW'){
						return '流程';
					}else{
						return '环节';
					}
				}},
				{title:'系统编码',field:'systemCode',hidden:true}
	    	]],
		    onDblClickRow:function(index,data){
		    	$("#leftArea").textbox("setValue",data.code);
		    }
	});
	$('#addBtn').click(function(evt){
		var rightAreaValue = $("#rightArea").textbox("getValue");
		if((rightAreaValue == null) || (rightAreaValue == "")){
			$.messager.alert("提示","右表达式不能为空","info");
			return;
		}
		var operator = $("#operType").combobox("getValue");
		var tempStr = $("#condition").val();
		tempStr = tempStr + "{$" + $("#leftArea").textbox("getValue") + "$" + operator + $("#rightArea").textbox("getValue") + "}";
		$("#condition").val(tempStr);
		$("#rightArea").textbox("setValue","");
		$("#leftArea").textbox("setValue","");
	});
	$('#delBtn').click(function(evt){
		$.messager.confirm("确认","是否确认删除此线条组件？",function(ret){
			if(ret){
				$("#condition").val("");
				$("#rightArea").textbox("setValue","");
				$("#leftArea").textbox("setValue","");
				$("#lineWin").data("isDel",true);
			}
		});
	});
	$('#andBtn').click(function(evt){
		var val = $("#condition").val();
		$("#condition").val(val + "与");
	});
	$('#orBtn').click(function(evt){
		var val = $("#condition").val();
		$("#condition").val(val + "或");
	});
	$('#leftParenthesesBtn').click(function(evt){
		var val = $("#condition").val();
		$("#condition").val(val + "(");
	});
	$('#rightParenthesesBtn').click(function(evt){
		var val = $("#condition").val();
		$("#condition").val(val + ")");
	});
	
	//流程定义图中：查看和设置线条组件------end---------
	//流程定义图中：线条右键菜单初始化------start---------
	$("#lineMenu").menu({
		onClick:function(item)
		{
			var transition = $(this).data("transition");
			switch(item.name)
			{
				case "viewCondition":
					openLineWin(transition,false);
					break;
				case "setCondition":
					openLineWin(transition,true);
					break;
				case "deleteBranch":
					workArea.deleteBranch(transition);
					break;
				default:
					break;
			}
		}
	});
	//流程定义图中：线条右键菜单初始化------end---------
	
	//工具栏的操作-------------------start--------------
	//鼠标状态
	$("#select").click(function(evt){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		workArea.paintAfterAdd();
	});
	//查看xpdl
	$("#viewXPDL").click(function(evt){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		$("#xpdlWin-xpdl").textbox("setValue",workArea.generateXPDL());
		$("#xpdlWin").dialog("open");
	});
	//校验
	$("#validate").click(function(evt){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		$.messager.alert("提示","校验功能待后续补充完善！","info");
	});
	//保存
	$("#saveVersion").click(function(evt){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var node = $('#flowTree').tree('getSelected');
		if (node.text.indexOf("激活") != -1) {// 模板处于激活状态
			// modify by bobping 
			$.messager.defaults = { ok: "是", cancel: "否" };
			$.messager.confirm("确认", "为避免对已在途的流程造成影响，对流程的修改是否另存为新版本？", function(ret) {
						if (ret) {//是
							var xpdl = workArea.generateXPDL();
							saveAsVersion(node, xpdl);
							$.messager.alert("提示", "流程已另存为新版本",
									"info");
							return;
						}else{
							saveFlow();
							return;
						}
					});

		}else{
			saveFlow();
		}
	});
	var saveAsVersion = function(node,xpdl){
		var pNode = $('#flowTree').tree('getParent',node.target);
		var params = {};
		params.parentId = pNode.id;
		params.name = pNode.text;
		params.editUser = session["staffName"]; 
		//模板编码另存为新版本时不需要改变模板编码 注释替换为start--end部分  modify by bobping
//		var oldDefine = $.callSyn("FlowServ","findProcessDefinitionById",{processDefineId:node.id});
//		var maxVersion = 0;
//		var version = parseInt(oldDefine.version.split(".")[1]);
//		if (maxVersion < version) {
//			maxVersion = version;
//		}
//		maxVersion = Number(maxVersion) + Number(1);
//		var oldCode =  oldDefine.packageDefineCode;
//		if(oldCode.indexOf("_")>0){
//			oldCode = oldCode.substring(0,oldCode.indexOf("_"));
//		}
//		params.packageDefineCode = oldCode + "_" + maxVersion;
		//......start.....//
		var packageDefineCode = workArea.getCurCode();
		if(!packageDefineCode){
			var oldDefine = $.callSyn("FlowServ","findProcessDefinitionById",{processDefineId:node.id});
			packageDefineCode = oldDefine.packageDefineCode;
		}
		params.packageDefineCode = packageDefineCode;
		//......end.......//
		var ret = $.callSyn("FlowServ","addProcessDefine",params); 
		$.callSyn("FlowServ", "saveXPDL", {
			processDefineId: ret.id,
			xpdl: xpdl
		});
		if(workArea.getCurCode()){
			workArea.setDirty(false);
		}
		
		var obj = {};
		obj.oldProcessDefId=node.id;
		obj.newProcessDefId=ret.id;
		var retObj = $.callSyn("FlowServ","saveProcessDefsAsNew",obj);
		$('#flowTree').tree('append', { parent : pNode.target,  data : ret }); 
	}
	//并行结构
	$("#addParallel").click(function(evt){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		workArea.paintBeforeAdd("Parallel");
	});
	//控制结构
	$("#addControl").click(function(evt){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		workArea.paintBeforeAdd("Control");
	});
	//异常原因处理
	$("#exceptionInfoWin-startActivity").combobox({
		onChange:function(newValue,oldValue){
			$('#exceptionInfoWin-returnReasonGrid').datagrid('loadData',{total: 0, rows:[]});//清空
					var type = $("#exceptionInfoWin").data("type");
					var select = $("#exceptionInfoWin-startActivity").combobox('getValue');
					if(type!=1||!select)
					{
						return;
					}
					//该数组需要根据起始环节到xpdl里面去找到可回退的目标环节
					var data = workArea.getExceptionReturnTache(select);
					$("#exceptionInfoWin-endActivity").combobox('clear');
					$("#exceptionInfoWin-endActivity").combobox('loadData',data);
					$("#exceptionInfoWin-endActivity").combobox('select',data[0].value);
		}
	});
	$("#exceptionInfoWin-reasonType").combobox({
		onChange:function(newValue,oldValue){
			$('#exceptionInfoWin-returnReasonGrid').datagrid('loadData',{total: 0, rows:[]});//清空
		}
	});
	
	$("#exceptionInfoWin-qryBtn").click(function(evt){
		var type = $("#exceptionInfoWin").data("type");
		var select = $("#exceptionInfoWin-startActivity").combobox('getValue');
		if(type!=1||!select)
		{
			return;
		}
		var tacheId = workArea.getTacheId(select);
		var reasonType = $("#exceptionInfoWin-reasonType").combobox("getValue");
		var reasonList = $.callSyn("ReturnReasonServ",'qryReturnReasonsByTacheId',{tacheId:tacheId});
		var arr = [];
		$.each(reasonList.rows,function(i,n){//过滤符合类型的数据(过滤掉系统异常)
			if(n.reasonType==reasonType && n.reasonClass == "1"){
				arr.push(n);
			}
		});
		$("#exceptionInfoWin-returnReasonGrid").datagrid("loadData",arr);
	});
	
	$("#exceptionInfoWin-confirmBtn").click(function(evt){
		var sItem = $("#exceptionInfoWin-returnReasonGrid").datagrid('getSelected');
		if(sItem){
			var type = $("#exceptionInfoWin").data("type");
			var reasonType = $("#exceptionInfoWin-reasonType").combobox("getValue");
			var exceptionConfig = {};
			exceptionConfig.startActivityId = $("#exceptionInfoWin-startActivity").combobox("getValue");
			exceptionConfig.startActivityName =  $("#exceptionInfoWin-startActivity").combobox("getText");
			exceptionConfig.endActivityId =  $("#exceptionInfoWin-endActivity").combobox("getValue");
			exceptionConfig.endActivityName =  $("#exceptionInfoWin-endActivity").combobox("getText");
			exceptionConfig.reasonCatalogId =  reasonType;
			exceptionConfig.reasonCatalogName =  $("#exceptionInfoWin-reasonType").combobox("getText");
			exceptionConfig.returnReasonId =  sItem.id;
			exceptionConfig.returnReasonName =  sItem.returnReasonName;
			//exceptionConfig.returnReasonCode =  sItem.reasonCode;
			exceptionConfig.autoToManual =  $("#exceptionInfoWin-autoToManual").combobox("getValue");
			var startMode = "Returnback";
			switch(reasonType){
				case '10W':
					startMode = "Wait";
					break;
				case '10Q':
					startMode = "ChangeReturnback";
					break;
				default://10R
			}
			exceptionConfig.startMode = startMode;
			exceptionConfig.tacheId = workArea.getTacheId(exceptionConfig.startActivityId);
			exceptionConfig.targetTacheId = workArea.getTacheId(exceptionConfig.endActivityId)||0;
			exceptionConfig.reasonId = exceptionConfig.returnReasonId;
			switch(type){
				//新增
				case 1:
					var rows = $('#exceptionGrid').datagrid('getRows');
					var isHas = false;
					if(rows){
						$.each(rows,function(i,n){//相同开始节点，相同异常原因(含相同异常原因类型)只允许配置一条，确定一个目标节点
//							if(exceptionConfig.startActivityId==n.startActivityId&&exceptionConfig.endActivityId==n.endActivityId
//								&&exceptionConfig.reasonCatalogId==n.reasonCatalogId&&exceptionConfig.returnReasonId==n.returnReasonId){
//								isHas = true;
//							}
							if(exceptionConfig.tacheId==n.tacheId&&exceptionConfig.reasonId==n.reasonId){
								isHas = true;
							}
						});
					}
					if(isHas){
						$.messager.alert("提示","已存在相同配置的流程异常原因","info");
					}else{
						$("#exceptionGrid").datagrid("appendRow",exceptionConfig);
						$("#exceptionWin").data('change','Y');
					}
					break;
				//修改
				case 2:
					var row = $("#exceptionInfoWin").data("row");
					var rowIndex = $("#exceptionGrid").datagrid("getRowIndex",row);
					$("#exceptionGrid").datagrid("updateRow",{index:rowIndex,row:exceptionConfig});
					break;
				default:
					break;
			}
			if(!isHas){
				$("#exceptionInfoWin").dialog("close");
			}
		}else{
			$.messager.alert("提示","请选择异常原因","info");
		}
	});
	//设置异常原因
	$("#exceptionGridAdd").click(function(evt)
	{
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		$("#exceptionInfoWin").data("type",1);
		//回退的起始环节列表，从xpdl里面获取
		var data = workArea.getExceptionStartTache();
		$("#exceptionInfoWin-startActivity").combobox("clear");
		$('#exceptionInfoWin-startActivity').combobox('enable');
		
		$("#exceptionInfoWin-endActivity").combobox("clear");
		$('#exceptionInfoWin-endActivity').combobox('enable');
		
		$("#exceptionInfoWin-returnReasonGrid").datagrid("loadData",[]);
		
		$("#exceptionInfoWin-returnReasonName").textbox('clear').textbox('enable');
		
		$('#exceptionInfoWin-reasonType').combobox("clear").combobox('enable').combobox('select','10R');//clear后才能设值
		
		$("#exceptionInfoWin-qryBtn").linkbutton('enable');
		
		$("#exceptionInfoWin-autoToManual").combobox("select",'false');
		
		$("#exceptionInfoWin-startActivity").combobox('loadData',data);
		$("#exceptionInfoWin-startActivity").combobox("select",data[0].value);
		
		$("#exceptionInfoWin").dialog({title:'新增异常配置'});
		$("#exceptionInfoWin").dialog("open");
	});
	$("#exceptionGridModify").click(function(evt)
	{
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var row = $("#exceptionGrid").datagrid("getSelected");
		if(row)
		{
			$("#exceptionInfoWin").data("type",2);
			$("#exceptionInfoWin").data("row",row);
			
			$("#exceptionInfoWin-startActivity").combobox("clear");
			$("#exceptionInfoWin-startActivity").combobox("loadData",[{label:row.startActivityName,value:row.startActivityId}]);
			$("#exceptionInfoWin-startActivity").combobox("setValue",row.startActivityId);
			$('#exceptionInfoWin-startActivity').combobox('disable');
			
			var select = $("#exceptionInfoWin-startActivity").combobox('getValue');
			var data = workArea.getExceptionReturnTache(select);
			$("#exceptionInfoWin-endActivity").combobox('clear');
			$("#exceptionInfoWin-endActivity").combobox('loadData',data);
			$("#exceptionInfoWin-endActivity").combobox('select',row.endActivityId);
			
			$("#exceptionInfoWin-reasonType").combobox("setValue",row.reasonCatalogId);
			$('#exceptionInfoWin-reasonType').combobox('disable');
			
			$("#exceptionInfoWin-autoToManual").combobox("select",row.autoToManual);
			
			$("#exceptionInfoWin-qryBtn").linkbutton('disable');
			
			var reasonObj = [{
				id:row.returnReasonId,
				reasonCode:row.reasonCode,
				returnReasonName:row.returnReasonName
			}];
			$("#exceptionInfoWin-returnReasonGrid").datagrid('loadData',reasonObj).datagrid('selectRow',0);
			
			$("#exceptionInfoWin").dialog({title:'修改异常配置'});
			$("#exceptionInfoWin").dialog("open");
		}else{
			$.messager.alert("提示","请选择你要修改的行？","info");
		}
	});
	$("#exceptionGridDelete").click(function(evt)
	{
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		var row = $("#exceptionGrid").datagrid("getSelected");
		if(row)
		{
			$.messager.confirm("确认","是否确认删除该异常原因配置？",function(ret){
				if(ret)
				{
					var rowIndex = $("#exceptionGrid").datagrid("getRowIndex",row);
					$("#exceptionGrid").datagrid("deleteRow",rowIndex);
					$("#exceptionWin").data('change','Y');
				}
			});
		}else{
			$.messager.alert("提示","请选择你要删除的行？","info");
		}
	});
	$("#exceptionGridClear").click(function(evt)
	{
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		$.messager.confirm("确认","是否确认清空所有的异常原因配置？",function(ret){
			if(ret)
			{
				$("#exceptionGrid").datagrid("loadData",[]);
				$("#exceptionWin").data('change','Y');
			}
		});
	});
	$("#exceptionWin-confirmBtn").click(function(evt)
	{
		var rows = $("#exceptionGrid").datagrid("getRows");
		var change= $("#exceptionWin").data('change');
		if(change=='Y'){
			workArea.setException(rows);
		}
		$("#exceptionWin").dialog('close');
	});
	$('#exceptionGrid').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
		pagination:false,//
	    fitColumns:true,
	    toolbar: '#reasonTab',
	    columns:[[
				{title:'发生异常环节',field:'startActivityName',width:100},
				{title:'异常原因分类',field:'reasonCatalogName',width:100},
				{title:'异常原因',field:'returnReasonName',width:100},
				{title:'回滚目标环节',field:'endActivityName',width:100},
				{title:'是否自动转人工',field:'autoToManual',width:100,
					formatter:function(value,row,index){
						if(value=='true'){
							return '是';
						}else{
							return '否';
						}
					}},
				{title:'发生异常环节id',field:'startActivityId',hidden:true},
				{title:'回滚目标环节id',field:'endActivityId',hidden:true},
				{title:'异常原因类型编码',field:'reasonCatalogId',hidden:true},
				{title:'异常原因id',field:'returnReasonId',hidden:true},
				//{title:'异常原因编码',field:'returnReasonCode',hidden:true},
				{title:'启动模式',field:'startMode',hidden:true}
	    	]]
	});
	$("#setException").click(function(evt){
		if($(this).linkbutton('options').disabled)
		{
			return;
		}
		$('#exceptionGrid').datagrid('loadData',workArea.getException());
		$("#exceptionWin").dialog('open');
		$("#exceptionWin").data('change','N');//判断是否有变化——有则保存，无则不处理setException
	});
	
	/**设置流程参数*/
	$("#flowParamWin").dialog({
		onBeforeOpen:function(){
			//查询公共的流程参数 
			var flowParamDefs = $.callSyn("FlowServ","qryFlowParamDefs",{systemCode:session["systemCode"]});
			$('#commonFlowParamGrid').datagrid('loadData',flowParamDefs);
			$('#commonFlowParamGrid').datagrid('unselectAll');
			//查询应用于某个流程的流程参数 type:FLOW/TACHE
			var flowParamDefs = $.callSyn("FlowServ","qryFlowParamDefRels",{
				packageDefineId:workArea.getCurId(),type:$("#flowParamWin").data('type'),tacheCode:$("#flowParamWin").data('tacheCode')});
			$('#flowParamGrid').datagrid('loadData',flowParamDefs);
			$('#flowParamGrid').datagrid('unselectAll');
		}
	});
	$("#setFlowParam").click(function(evt){
		if($(this).linkbutton('options').disabled){
			return;
		}
		$("#flowParamWin").data('type','FLOW');
		$("#flowParamWin").data('tacheCode','-1');
		$("#flowParamWin").dialog('open');
	});
	//流程参数列表——公共
	$('#commonFlowParamGrid').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
		pagination:false,//
	    fitColumns:true,
	    idField:'code',
	    toolbar: '#commonFlowParamTab',
	    columns:[[
				{title:'参数编码',field:'code',width:100},
				{title:'参数名称',field:'name',width:100},
				{title:'参数值',field:'value',width:100},
				{title:'是否可变',field:'isVariable',width:100,
					formatter:function(value,row,index){
						if(value=='1'){
							return '是';
						}else{
							return '否';
						}
					}},
				{title:'备注',field:'comments',width:100},
				{title:'系统编码',field:'systemCode',hidden:true}
	    	]],
	    onDblClickRow:function(index,data){
	    	if($("#commonFlowParamGridAdd").linkbutton('options').disabled){
	    		return;
	    	}
	    	var rows = $('#flowParamGrid').datagrid('getRows');
	    	var insertFlag = true;
	    	$.each(rows,function(i,n){
	    		if(data.code==n.code){
	    			insertFlag = false;
	    			$('#flowParamGrid').datagrid('highlightRow',i);
	    		}
	    	});
	    	if(insertFlag){
	    		var param = {
		    		packageDefineId:workArea.getCurId(),
					code:data.code,
					value:data.value,
					type:$("#flowParamWin").data('type'),
					isVariable:data.isVariable
				};
		    	$('#flowParamGrid').datagrid('appendRow',param);
		    	if(rows.length>1){
		    		$('#flowParamGrid').datagrid('highlightRow',rows.length-1);
		    	}
	    	}
	    }
	});
	$('#flowParamInfoWin-isVariable').combobox({
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
	//流程参数列表——绑定到流程
	$('#flowParamGrid').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
		pagination:false,//
	    fitColumns:true,
	    idField:'code',
	    columns:[[
				{title:'参数编码',field:'code',width:100},
				{title:'参数值',field:'value',width:100},
				{title:'类型',field:'type',width:100,formatter:function(value,row,index){
					if(value=='FLOW'){
						return '流程';
					}else{
						return '环节';
					}
				}},
				{title:'是否可变',field:'isVariable',width:100,
					formatter:function(value,row,index){
						if(value=='1'){
							return '是';
						}else{
							return '否';
						}
					}},
				{title:'操作',field:'del',width:30,
				formatter:function(value,row,index){
					if($("#commonFlowParamGridAdd").linkbutton('options').disabled){
						return '';
					}
					return '<img src="/uos-manager/common/easyui/themes/icons/cancel.png" onclick="delFlowParam('+index+')">';
				},
				styler: function(value,row,index){
					return 'cursor:pointer;';
				}},
				{title:'系统编码',field:'systemCode',hidden:true}
	    	]]
	});
	//公共流程参数——增加
	$("#commonFlowParamGridAdd").click(function(evt)
	{
		if($(this).linkbutton('options').disabled){
			return;
		}
		$("#flowParamInfoWin").dialog({title:'新增流程参数'});
		$("#flowParamInfoWin").data("type","add");
		$('#flowParamInfoWin-form').form('clear');
		$("#flowParamInfoWin-code").textbox('enable');
		$("#flowParamInfoWin").dialog("open");
	});
	//公共流程参数——修改
	$("#commonFlowParamGridModify").click(function(evt)
	{
		if($(this).linkbutton('options').disabled){
			return;
		}
		var row = $("#commonFlowParamGrid").datagrid("getSelected");
		if(row){
			$("#flowParamInfoWin").dialog({title:'修改流程参数'});
			$("#flowParamInfoWin").data("type","mod");
			$("#flowParamInfoWin-code").textbox("setValue",row.code).textbox('disable');
			$("#flowParamInfoWin-name").textbox("setValue",row.name);
			$("#flowParamInfoWin-value").textbox("setValue",row.value);
			$("#flowParamInfoWin-comments").textbox("setValue",row.comments);
			$("#flowParamInfoWin-isVariable").combobox('select',row.isVariable);
			$("#flowParamInfoWin").dialog("open");
		}else{
			$.messager.alert("提示","请选择你要修改的行","info");
		}
	});
	//公共流程参数——删除
	$("#commonFlowParamGridDelete").click(function(evt)
	{
		if($(this).linkbutton('options').disabled){
			return;
		}
		var row = $("#commonFlowParamGrid").datagrid("getSelected");
		var ret = $.callSyn("FlowServ","isExistRela",{code:row.code});
		if(ret&&ret.isExist){
			$.messager.alert("提示","流程参数已被使用，不能删除");
		}else{
			if(row){
				$.messager.confirm("确认","是否确认删除该流程参数？",function(ret){
					if(ret){
						var rowIndex = $("#commonFlowParamGrid").datagrid("getRowIndex",row);
						$("#commonFlowParamGrid").datagrid("deleteRow",rowIndex);
						var ret = $.callSyn("FlowServ","delFlowParamDef",{code:row.code});
					}
				});
			}else{
				$.messager.alert("提示","请选择你要删除的行","info");
			}
		}
	});
	//配置流程参数窗口的确定按钮
	$('#flowParamInfoWin-confirmBtn').click(function(e){
		if($('#flowParamInfoWin-form').form('validate')){
			var type = $("#flowParamInfoWin").data("type");
			var param = {
				code:$('#flowParamInfoWin-code').val(),
				name:$('#flowParamInfoWin-name').val(),
				value:$('#flowParamInfoWin-value').val(),
				systemCode:session["systemCode"],
				comments:$('#flowParamInfoWin-comments').val(),
				isVariable:$('#flowParamInfoWin-isVariable').combobox('getValue')
			};
			if(type=="add"){
				var isHas = false;
				$.each($("#commonFlowParamGrid").datagrid("getRows"),function(i,n){
					if(n.code==param.code){
						isHas = true;
					}
				});
				if(isHas){
					$.messager.alert("提示","参数编码重复");
					return;
				}else{
					var ret = $.callSyn("FlowServ","addFlowParamDef",param);
					if(ret){
						$('#commonFlowParamGrid').datagrid('appendRow',ret);
					}
				}
			}else{//mod
				var ret = $.callSyn("FlowServ","modFlowParamDef",param);
				if(ret){
					$('#commonFlowParamGrid').datagrid('updateRow',{index:$('#commonFlowParamGrid').datagrid('getRowIndex',$('#commonFlowParamGrid').datagrid('getSelected')),row:ret});
				}
			}
			$("#flowParamInfoWin").dialog("close");
		}
	});
	$('#flowParamWin-confirmBtn').click(function(e){
		if(!$("#commonFlowParamGridAdd").linkbutton('options').disabled){
    		var rows = $('#flowParamGrid').datagrid('getRows');
			var params = {
				packageDefineId:workArea.getCurId(),
				type:$("#flowParamWin").data('type'),
				tacheCode:$("#flowParamWin").data('tacheCode'),
				rows:rows
			};
			var ret = $.callSyn("FlowServ","delAddBatchFlowParamDefRel",params);
    	}
    	$("#flowParamWin").dialog("close");
	});
	//工具栏的操作-------------------end--------------
	

	//add by che.zi 2015-08-11
	$("#startFlowParamWin").dialog({
		onBeforeOpen:function(){
			$('#flowCatalog').html($("#startFlowParamWin").data('title'));
			var flowParamDefs = $.callSyn("FlowServ","qryFlowParamDefRels",{
				packageDefineId:$("#startFlowParamWin").data('packageDefineId'),type:$("#startFlowParamWin").data('type'),tacheCode:$("#startFlowParamWin").data('tacheCode')});
			$('#startflowParamGrid').datagrid('loadData',flowParamDefs);
			$('#startflowParamGrid').datagrid('unselectAll');
		}
	});
	//启动流程的流程参数信息-列表 
	$('#startflowParamGrid').datagrid({
		rownumbers:true,//显示带有行号的列
		singleSelect:true,//则只允许选中一行
		autoRowHeight:false,//是否设置基于该行内容的行高度
		pagination:false,//
	    fitColumns:true,
	    idField:'code',
	    columns:[[
				{title:'参数编码',field:'code',width:100},
				{title:'参数值',field:'value',width:100},
				{title:'类型',field:'type',width:100,formatter:function(value,row,index){
					if(value=='FLOW'){
						return '流程';
					}else{
						return '环节';
					}
				}},
				{title:'是否可变',field:'isVariable',width:100},
				{title:'系统编码',field:'systemCode',hidden:true}
	    	]],
			onClickRow: function(index,row){
				$('#startFlowParamModify').show();
			}
	});
	$('#startFlowParamWin-confirmBtn').click(function(e){
		var rows = $('#startflowParamGrid').datagrid('getRows');
		var flowparams = new Array();
    	$.each(rows,function(i,n){
    		var flowparam = new Object();
    		flowparam.FLOW_PARAM_CODE = n.code;
    		flowparam.FLOW_PARAM_VALUE = n.value;
    		flowparams[i] = flowparam;
    	}); 
		var pId = $.callSyn("FlowOperServ","startFlow",{processDefineId:$("#startFlowParamWin").data('packageDefineId'),processDefineName:$("#startFlowParamWin").data('processDefineName'),areaId:session.areaId,flowParamList:flowparams});
		if(pId == 'fail'){
			$.messager.alert("提示","启动流程失败");
			$("#startFlowParamWin").dialog("close");
		}else{
//			$.messager.alert("提示","流程定义启动成功！流程实例id:"+pId,"info");
			$('#flowInstId').html(pId);
			$("#startResult").show();
			$("#startFlowParamWin-buttons").hide();
		}
//    	$("#startFlowParamWin").dialog("close");
	});
	$('#startFlowParamModify').click(function(e){
		var row = $("#startflowParamGrid").datagrid("getSelected");
		if(row){
			$("#startflowParamInfoWin").dialog({title:'修改流程参数值'});
			$("#startflowParamInfoWin-code").textbox("setValue",row.code).textbox('disable');
			$("#startflowParamInfoWin-value").textbox("setValue",row.value);
			$("#startflowParamInfoWin-packageDefineId").val($("#startFlowParamWin").data('packageDefineId'));
			$("#startflowParamInfoWin-type").val($("#startFlowParamWin").data('type'));
			$("#startflowParamInfoWin").data('isVariable',row.isVariable);
			$("#startflowParamInfoWin-tacheCode").val($("#startFlowParamWin").data('tacheCode'));
			$("#startflowParamInfoWin").dialog("open");
		}else{
			$.messager.alert("提示","请选择你要修改的行","info");
		}
	});
	//修改流程参数值窗口的确定按钮
	$('#startflowParamInfoWin-confirmBtn').click(function(e){
		if($('#startflowParamInfoWin-form').form('validate')){
			var row = new Object();
			row.code = $('#startflowParamInfoWin-code').val();
			row.value = $('#startflowParamInfoWin-value').val();
			row.isVariable = $("#startflowParamInfoWin").data('isVariable');
			var rows = new Array();
			rows[0] = row;
			var packageDefineId = $('#startflowParamInfoWin-packageDefineId').val();
			var type = $('#startflowParamInfoWin-type').val();
			var tacheCode = $('#startflowParamInfoWin-tacheCode').val();
			var param = {
				packageDefineId:packageDefineId,
				type:type,
				tacheCode:tacheCode,
				rows:rows
			};
			var ret = $.callSyn("FlowServ","updateBatchFlowParamDefRel",param);
			if(ret){
//				$('#startflowParamGrid').datagrid('updateRow',{index:$('#startflowParamGrid').datagrid('getRowIndex',$('#startflowParamGrid').datagrid('getSelected')),row:ret});
				var flowParamDefs = $.callSyn("FlowServ","qryFlowParamDefRels",param);
				$('#startflowParamGrid').datagrid('loadData',flowParamDefs);
				$('#startflowParamGrid').datagrid('unselectAll');
			}
			$("#startflowParamInfoWin").dialog("close");
		}
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
	$('#btnQry').click(function(e){
		var tacheName = $('#tName').val();
		if(!tacheName || tacheName == ''){
			$.messager.alert("提示","请输入环节名称");
			return;
		}
		var ret = $.callSyn('TacheServ','qryTaches',{tacheName:tacheName,state:"10A"});
		for(var i = 0;i<ret.rows.length;i++)
		{
			var row = ret.rows[i];
			var taches = [];
			taches.push({id:row.id,text:row.tacheName,code:row.tacheCode});
			var subNode = $('#tacheTree').tree('find', row.id); 
			if(subNode){
				$('#tacheTree').tree('remove', subNode.target);
//				continue;
			}
			var node = $('#tacheTree').tree('find', row.tacheCatalogId); 
			var parent = node;
			do {
				$('#tacheTree').tree('expand', parent.target);
				var parent = $('#tacheTree').tree('getParent', parent.target);
			} while (parent);  
			$('#tacheTree').tree('expand', node.target);
			$('#tacheTree').tree('append', { parent : node.target,  data : taches }); 
		}
	});

	$('#pName').textbox({
		prompt: '流程名称',
		icons:[{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).textbox('clear').textbox('textbox').focus();
			}
		}]
	});
	$('#btnFlowQry').click(function(e){
		var flowName = $('#pName').val();
		if(!flowName || flowName == ''){
			$.messager.alert("提示","请输入流程名称");
			return;
		}
		var ret = $.callSyn('FlowServ','qryProcessDefineByName',{name:flowName,state:"10A"});
		var root = $('#flowTree').tree('find', 1);
		$('#flowTree').tree('expand', root.target);
		for(var i = 0;i<ret.length;i++)
		{
			var row = ret[i];
//			var flows = [];
//			flows.push({id:row.packageId,text:row.name});
			var subNode = $('#flowTree').tree('find', row.packageId); 
			if(subNode){
				$('#flowTree').tree('remove', subNode.target);
//				continue;
			}
			var node = $('#flowTree').tree('find', row.parentId); 
			var parent = node;
			do {
				$('#flowTree').tree('expand', parent.target);
				var parent = $('#flowTree').tree('getParent', parent.target);
			} while (parent);  
			$('#flowTree').tree('expand', node.target);
			$('#flowTree').tree('append', { parent : node.target,  data : row }); 
		}
	});
});
/**公用方法*/
//删除流程参数——无法用datagrid.onClickCell方法直接处理，因为在此方法deleteRow之后，easyui自己再次调用selectRow,导致后面继续的时候，无法deleteRow
function delFlowParam(index){
	$('#flowParamGrid').datagrid('deleteRow',index);
	var rows = $('#flowParamGrid').datagrid("getRows");
	$('#flowParamGrid').datagrid("loadData", rows);
}

