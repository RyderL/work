$(function($){
	var session = $.session();
	/** 初始化头部信息 **/
	$("#username").html(session.staffName);
	$("#cance").click(function(){
		$.ajax({
			url:webBasePATH+"logout.do",
            type:'post',
			async: true
		});
		self.location = webBasePATH+'login.html';
	});
	$("#exit").click(function(){
		$.ajax({
			url:webBasePATH+"logout.do",
            type:'post',
			async: true
		});
		window.open('','_parent','');  
  		window.close();  
	});
	
	/** 菜单tab切换逻辑 **/
	var addTab = function(title, href){
		var tab = $('#centerTab');
		if (tab.tabs('exists', title)){//如果tab已经存在,则选中并刷新该tab    	
			tab.tabs('select', title);
			refreshTab({tabTitle:title,url:href});
		} else {
			if (href){
				var content = '<iframe scrolling="no" frameborder="0"  src="'+href+'" style="width:100%;height:99%;"></iframe>';
			} else {
				var content = '未实现';
			}
			tab.tabs('add',{
				title:title,
				closable:true,
				content:content
			});
		}
	};
	/**    
	 * 刷新tab
	 * @cfg 
	 *example: {tabTitle:'tabTitle',url:'refreshUrl'}
	 *如果tabTitle为空，则默认刷新当前选中的tab
	 *如果url为空，则默认以原来的url进行reload
	 */
	var refreshTab = function(cfg){
		var refreshTab = cfg.tabTitle?$('#centerTab').tabs('getTab',cfg.tabTitle):$('#centerTab').tabs('getSelected');
		if(refreshTab && refreshTab.find('iframe').length > 0){
			var refreshIframe = refreshTab.find('iframe')[0];
			var refreshUrl = cfg.url?cfg.url:refreshIframe.src;
			//refreshIframe.src = refreshUrl;
			refreshIframe.contentWindow.location.href=refreshUrl;
		}
	}
	var treeOper = {};
	treeOper.onClick = function(node)
	{
		addTab(node.text,node.data);
	};
	treeOper.onContextMenu = function(e, node)
	{
   		e.preventDefault();
    };
	/**菜单处理--流程管理*/
	var flowTree = [ 
	                {
						id : "flowconfig",
						text : "流程配置",
						iconCls:"menu-tree-icon",
						state: 'closed',
						children:[
							{
								id : "tache",
								text : "环节管理",
								iconCls:"menu-tree-icon",
								data:"../flow/tache/tacheManager.html"
							}, {
								id : "exception",
								text : "异常原因管理",
								iconCls:"menu-tree-icon",
								data:"../flow/exception/exceptionManager.html"
							} , {
								id : "flowDef",
								text : "流程建模",
								iconCls:"menu-tree-icon",
								data:"../flow/design/flowDefManager.html"
							} 
						]
	                }, 
					{
							id : "flowmonitor",
							text : "流程监控",
							iconCls:"menu-tree-icon",
							data:"",
							state: 'closed',
							children:[
								{
									id : "flowInst",
									text : "流程实例管理",
									iconCls:"menu-tree-icon",
									data:"../flow/instmanager/flowInstManager.html"
								} , {
									id : "flowError",
									text : "流程异常管理",
									iconCls:"menu-tree-icon",
									data:"../flow/flowerror/flowErrorManager.html"
								},{
									id : "commandError",
									text : "流程消息监控",
									iconCls:"menu-tree-icon",
									data:"../flow/command/commandErrorManager.html"
								}/*
	//							, {
	//							id : "synRule",
	//							text : "同步规则管理",
	//							iconCls:"menu-tree-icon",
	//							data:"../flow/synrule/synRuleManager.html"
							} */, {
								id : "fqueueMonitor",
								text : "消息监控",
								iconCls:"menu-tree-icon",
								data:"../queue/fQueueMonitor.html"
							} /*, {
	//							id : "redisQueueMonitor",
	//							text : "redis队列监控",
	//							iconCls:"menu-tree-icon",
	//							data:"../queue/redisQueueMonitor.html"
	//						}  , {
	//							id : "serverMonitor",
	//							text : "进程监控",
	//							iconCls:"menu-tree-icon",
	//							data:"../server/serverMonitor.html"
	//						}  */      
						]
	                },  
					{
						id : "flowmonitorhis",
						text : "历史流程监控",
						iconCls:"menu-tree-icon",
						data:"",
						state: 'closed',
						children:[
							{
								id : "flowInstHis",
								text : "流程实例管理(历史)",
								iconCls:"menu-tree-icon",
								data:"../flow/instmanager/flowInstManagerHis.html"
							},{
								id : "flowErrorHis",
								text : "流程异常管理(历史)",
								iconCls:"menu-tree-icon",
								data:"../flow/flowerror/flowErrorManagerHis.html"
							},{
								id : "commandErrorHis",
								text : "流程消息监控(历史)",
								iconCls:"menu-tree-icon",
								data:"../flow/command/commandErrorManagerHis.html"
							}
						]
					}, 
					{
						id : "timeLimit",
						text : "时限管理",
						iconCls:"menu-tree-icon",
						data:"",
						state: 'closed',
						children:[
								{
									id : "tacheLimitManager",
									text : "环节时限管理",
									iconCls:"menu-tree-icon",
									data:"../flow/timelimit/tacheLimitManager.html"
								}
								, {
									id : "flowLimitManager",
									text : "流程时限管理",
									iconCls:"menu-tree-icon",
									data:"../flow/timelimit/flowLimitManager.html"
								}, {
									id : "workTimeManager",
									text : "工作时间管理",
									iconCls:"menu-tree-icon",
									data:"../flow/timelimit/workTimeManager.html"
								}, {
									id : "holidayManager",
									text : "节假日管理",
									iconCls:"menu-tree-icon",
									data:"../flow/timelimit/holidayManager.html"
								}
						     ]
					}
					];
	$("#flowTree").tree({
		data:flowTree,
		onContextMenu: treeOper.onContextMenu,
		onClick:treeOper.onClick
	});

	/**菜单处理--表单管理*/
	var formTree = [{
						id:"",
						text : "元素管理",
						iconCls:"menu-tree-icon",
						data:"../form/dynamicform/pageElementManager.html"
					},{
						id:"",
						text : "模板管理",
						iconCls:"menu-tree-icon",
						data:"../form/dynamicform/templateManager/templateManager.html"
					}];
	$("#formTree").tree({
		data:formTree,
		onContextMenu: treeOper.onContextMenu,
		onClick:treeOper.onClick
	});

	/**菜单处理--人员管理*/
	/*var userTree = [{
                	   id : "userManager",
                	   text : "人员管理",
                	   iconCls : "menu-tree-icon"
                   }];
	$('#userTree').tree({
		data : userTree,
		onContextMenu: treeOper.onContextMenu,
		onClick:treeOper.onClick
	});*/
	
});
