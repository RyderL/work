(function($){
	var zflow = $.zflow;
	
	zflow.node = {};
	
    zflow.node.init = function(activity)
    {
    	return new zflow.node.Node(activity);
    };
	
	zflow.node.config = {
		imageRadius:30,//节点大小
		textHeight:20//节点文字高度
	};
	
	zflow.node.getNodeOffsetY = function(){
		return ((zflow.config.nodeHeight-(zflow.node.config.imageRadius+zflow.node.config.textHeight))/2+zflow.node.config.imageRadius/2);
	};
    
    zflow.node.Node = function(activity)
    {
		this.activity = activity;
		var name = this.activity.name;
		if(name.length>6){
			name = name.substr(0,6)+"...";
		}
    	//reset的时候进行重新设置
    	this.x = 0;
    	this.y = 0;
    	this.imageRadius = zflow.node.config.imageRadius;
		this.parallelNodeCrossLength = zflow.node.config.imageRadius*7/10;
    	this.textHeight = zflow.node.config.textHeight;
        if(zflow.state.direction ==zflow.constant.DIRECTION.H)
    	{
        	this.nodeWidth = this.imageRadius+zflow.config.space;
        	this.nodeHeight = zflow.config.nodeHeight;
    	}else if(zflow.state.direction ==zflow.constant.DIRECTION.V)
		{
    		this.nodeWidth = zflow.config.nodeWidth;
        	this.nodeHeight = this.imageRadius+zflow.config.space+this.textHeight;
		}
        this.textWidth = this.nodeWidth;
    	this.offsetX = (this.nodeWidth - this.imageRadius)/2;
    	this.offsetY = (this.nodeHeight-(this.imageRadius+this.textHeight))/2;
    	
    	
    	this.image = null;
    	this.shade = null;
    	this.text = null;

    	this.reset = function(paper,x,y)
    	{
    		this.x = x;
    		this.y = y;
    		var color = $.color;
        	if(this.activity.type =="Start"){
				this.image = paper.image(webBasePATH+"common/images/flow/start-yd.png",this.x+this.nodeWidth/2-this.imageRadius/2,this.y+this.offsetY,this.imageRadius,this.imageRadius);
				//文字
        		this.text = paper.text(this.x+this.nodeWidth/2,y+this.imageRadius+this.textHeight/2+this.offsetY,name);
        		this.text.attr({width:this.nodeWidth,height:this.textHeight});
            }else if(this.activity.type =="Finish"){
        		this.image = paper.image(webBasePATH+"common/images/flow/finish-yd.png",this.x+this.nodeWidth/2-this.imageRadius/2,this.y+this.offsetY,this.imageRadius,this.imageRadius);
				//文字
				this.text = paper.text(this.x+this.nodeWidth/2,y+this.imageRadius+this.textHeight/2+this.offsetY,name);
        		this.text.attr({width:this.nodeWidth,height:this.textHeight});
            }else if(this.activity.type == "Parallel"){
                this.image = paper.image(webBasePATH+"common/images/flow/parallel_h.png",this.x+this.nodeWidth/2-this.imageRadius/2,this.y+this.offsetY,this.imageRadius,this.imageRadius);
				this.text = paper.text(this.x+this.nodeWidth/2,this.y+this.imageRadius+this.textHeight/2+this.offsetY,name);
        		this.text.attr({width:this.nodeWidth,height:this.textHeight});
        	}else if(this.activity.type == "Relation"){
                this.image = paper.image(webBasePATH+"common/images/flow/parallel_and.png",this.x+this.nodeWidth/2-this.imageRadius/2,this.y+this.offsetY,this.imageRadius,this.imageRadius);
			    this.text = paper.text(this.x+this.nodeWidth/2,this.y+this.imageRadius+this.textHeight/2+this.offsetY,name);
        		this.text.attr({width:this.nodeWidth,height:this.textHeight});
    		}else if(this.activity.type == "Control"){
				this.image = paper.image(webBasePATH+"common/images/flow/公共图标-未处理.png",this.x+this.nodeWidth/2-this.imageRadius/2,this.y+this.offsetY,this.imageRadius,this.imageRadius);
			    this.text = paper.text(this.x+this.nodeWidth/2,this.y+this.imageRadius+this.textHeight/2+this.offsetY,name);
        		this.text.attr({width:this.nodeWidth,height:this.textHeight});
			}else{
				var image = "PUBLIC-undo.png";
				if(zflow.state.nameConfig[this.activity.tacheIconName]){
					image = zflow.state.nameConfig[this.activity.tacheIconName]["FFF"][3];
					if (zflow.state.mode == zflow.constant.MODE.INST) {
						image = "PUBLIC-finish.png";
	        			if(zflow.state.nameConfig[this.activity.tacheIconName][this.activity.state])
	    				{
	        				image = zflow.state.nameConfig[this.activity.tacheIconName][this.activity.state][3];
	    				}
					}
				}
				
          		this.image = paper.image(webBasePATH+"common/images/flow/"+image,this.x+this.nodeWidth/2-this.imageRadius/2,this.y+this.offsetY,this.imageRadius,this.imageRadius);
                this.text = paper.text(this.x+this.nodeWidth/2,this.y+this.imageRadius+this.textHeight/2+this.offsetY,name);
        		this.text.attr({width:this.nodeWidth,height:this.textHeight});
    		};
    	};
    	//============四个连线点====start======
    	this.getTopPoint = function(){
    		return {x:this.x+this.nodeWidth/2,y:this.y+this.offsetY};
    	};
    	this.getLeftPoint = function(){
    		return {x:this.x+this.offsetX,y:this.y+this.imageRadius/2+this.offsetY};
    	};
    	this.getRightPoint = function(){
    		return {x:this.x+this.offsetX+this.imageRadius,y:this.y+this.imageRadius/2+this.offsetY};
    	};
    	this.getBottomPoint = function(){
    		    		return {x:this.x+this.nodeWidth/2,y:this.y+this.offsetY+this.imageRadius+this.textHeight};
    	};
        //============四个连线点====end======
    	
    	this.getWidth = function()
    	{
    		return this.nodeWidth;
    	};
    	this.getHeight = function()
    	{
    		return this.nodeHeight;
    	};
    	
    };
})(jQuery);
(function($){
	var zflow = $.zflow;
	
	zflow.node = {};
	
	var getQueryString = function(name)
	{
	     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	     var r = window.location.search.substr(1).match(reg);
	     if(r!=null)return  unescape(r[2]); return null;
	}
	
	var processInstId = getQueryString("processInstId");
    
    zflow.node.init = function(activity)
    {
    	return new zflow.node.Node(activity);
    };

	zflow.node.getNodeOffsetY = function(){
		return ((zflow.config.nodeHeight-(zflow.config.imageWidth+zflow.config.textHeight))/2+zflow.config.imageWidth/2);
	};
    
    zflow.node.Node = function(activity)
    {
		this.activity = activity;
    	//reset的时候进行重新设置
    	this.x = 0;
    	this.y = 0;
		this.parallelNodeCrossLength = zflow.config.imageWidth*7/10;
    	this.image = null;
    	this.shade = null;
    	this.text = null;

    	this.reset = function(paper,x,y)
    	{
    		this.x = x;
    		this.y = y;
    		var color = $.color;
    		var displayName = this.activity.name.length > 5 ? this.activity.name.substr(0, 4) + "..." : this.activity.name;
    		
			var image = "PUBLIC-def.png";
        	if(this.activity.type =="Start"){
        		if(zflow.state.mode == zflow.constant.MODE.INST){
    				image = "inst-start.png";
        		}else{
    				image = "def-start.png";
        		}
            }else if(this.activity.type =="Finish"){
        		if(zflow.state.mode == zflow.constant.MODE.INST){
        			image = "inst-finish.png";
        		}else{
        			image = "def-finish.png";
        		}
            }else if(this.activity.type == "Parallel"){
				image = "parallel-def.png";
				if (zflow.state.mode == zflow.constant.MODE.INST) {
					image = "parallel_h.png";
				}
        	}else if(this.activity.type == "Relation"){
				image = "and-def.png";
				if (zflow.state.mode == zflow.constant.MODE.INST) {
					image = "parallel_and.png";
				}
    		}else if(this.activity.type == "Control"){
				image = "PUBLIC-undo.png";
			}else {
				if(zflow.state.nameConfig[this.activity.tacheIconName]){
					image = zflow.state.nameConfig[this.activity.tacheIconName]["DEF"][3];
					if (zflow.state.mode == zflow.constant.MODE.INST) {
						image = "PUBLIC-finish.png";
						if(0 == this.activity.direction){
							this.activity.state = "EEE";
						}
	        			if(zflow.state.nameConfig[this.activity.tacheIconName][this.activity.state])
	    				{
	        				image = zflow.state.nameConfig[this.activity.tacheIconName][this.activity.state][3];
	    				}
					}
				}
			}
			this.image = paper.image(webBasePATH+"common/images/flow/"+image,this.x-zflow.config.imageWidth/2,this.y,zflow.config.imageWidth,zflow.config.imageHeight);
			//文字
            this.text = paper.text(this.x,this.y+zflow.config.imageHeight+zflow.config.textHeight/2,displayName);
            
            if(this.text&&displayName!=activity.name){
    		    $(this.text.node).tooltip({
    				position: 'right',
    				content: '<span style="color:#fff" width="100">'+activity.name+'</span>',
    				onShow: function(){
    					$(this).tooltip('tip').css({
    						backgroundColor: '#666',
    						borderColor: '#666'
    					});
    				}
    			});
    		}
            
            if (zflow.state.mode == zflow.constant.MODE.INST) {
        		var node = this;
        		 $(this.image.node).mousedown(function(handler){
        			if(handler.which==1)//左点击事件
        			{
        				handler.preventDefault();
        				handler.stopPropagation();
        				if(activity.isTache && activity.tacheId)
        				{
//        					var data = $.callSyn("FlowInstServ","qryUndoActivityByCond",param);
        				}
        			}
        		});
            }
            if (zflow.state.mode == zflow.constant.MODE.DEF) {
            	var node = this;
            	$(this.image.node).mousedown(function(handler){
            		var canJumpFlag = getQueryString("canJumpFlag"); //添加是否允许跳转标识
            		if(handler.which==1 && (canJumpFlag == null || canJumpFlag != 0))//左点击事件
            		{
            			handler.preventDefault();
            			handler.stopPropagation();
            			if(activity.isTache && activity.tacheId)
            			{
            				var param ={
            						processInstanceId:processInstId
            				};
            				if($.zflow.instActivities.length == 0) {
            					$.messager.alert("提示","流程不存在执行环节，不能执行跳转！");
            					return;
            				}
            				var areaId = $.callSyn("FlowInstServ","qryAreaIdByProcessInstId",param);
            				$.messager.confirm('问询', '确认要跳转到环节『' + activity.name + '』吗?', function(flag){
            					if (flag){
	            					var flowPassList = eval('(' + getQueryString("flowPassList") + ')');
	            					var param ={
	            							processInstanceId:processInstId,
	            							areaId : areaId,
	            							targetActivityId:activity.id,
	            							fromActivityInstanceId :$.zflow.instActivities[$.zflow.instActivities.length-1].id,
	            							flowPassList : flowPassList
	            					};
	            					var ret = $.callSyn("FlowOperServ","processInstanceJumpForServer",param);
	            					if(ret=='fail'){
	            						$.messager.alert("提示","流程跳转失败");
	            					}else{
	            						$.messager.alert("提示","流程跳转成功，请刷新页面查看跳转后流程图！");
	            					}             
            					}
            				});
            			}
            		}
            	});
            }
    	};
    	
    	//============四个连线点====start======
    	this.getTopPoint = function(){
    		return {x:this.x,y:this.y};
    	};
    	this.getLeftPoint = function(){
    		return {x:this.x-zflow.config.imageWidth/2,y:this.y+zflow.config.imageHeight/2};
    	};
    	this.getRightPoint = function(){
    		return {x:this.x+zflow.config.imageWidth/2,y:this.y+zflow.config.imageHeight/2};
    	};
    	this.getBottomPoint = function(){
    		return {x:this.x,y:this.y+zflow.config.imageHeight+zflow.config.textHeight};
    	};
        //============四个连线点====end======
    };
})(jQuery);

var getQueryString = function(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

var confirmProcessJump = function(){
//    	if($(this).linkbutton('options').disabled)
//    	{
//    		return;
//    	}
	var targetActivityId = $('#jumpProcInstWin-targetActivity').combobox('getValue');
//    	var sItem = $('#workItemTable').datagrid('getSelected');
	var flowPassList = eval('(' + getQueryString("flowPassList") + ')');
//	flowPassList = flowPassList.parseJSON();
//	console.log($.zflow.instActivities[instActivities.length-1] + ":" + $("#jumpProcInstWin").data('activityId'));
	var param ={
			processInstanceId:$("#jumpProcInstWin").data('processInstanceId'),
//			areaId:'108',
			targetActivityId:targetActivityId,
			fromActivityInstanceId :$("#jumpProcInstWin").data('activityId'),
			flowPassList : flowPassList
	};
	var ret = $.callSyn("FlowOperServ","processInstanceJumpForServer",param);
	if(ret=='fail'){
		$.messager.alert("提示","流程跳转失败");
	}else{
		$.messager.alert("提示","流程跳转成功");
	}             
	$("#jumpProcInstWin").dialog('close');
}
