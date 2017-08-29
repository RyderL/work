var Node = function(workArea,flow,activity)
{
	var workArea = workArea;
	var flow = flow;
	this.activity = activity;
	
	var config = {	
					imageRadius:30,//节点大小
					textHeight:20//节点文字高度
				 };	
	
	//reset的时候进行重新设置
	this.x = 0;
	this.y = 0;
	this.imageRadius = config.imageRadius;
	this.parallelNodeCrossLength = config.imageRadius*7/10;
	this.textHeight = config.textHeight;
	this.nodeWidth = workArea.config.nodeWidth;
	this.nodeHeight = this.imageRadius+workArea.config.space+this.textHeight;
    this.textWidth = this.nodeWidth;
	this.offsetX = (this.nodeWidth - this.imageRadius)/2;
	this.offsetY = (this.nodeHeight-(this.imageRadius+this.textHeight))/2;
	
	
	this.image = null;
	this.shade = null;
	this.text = null;

	this.reset = function(paper,x,y,ctrlFlag)
	{
		this.x = x;
		this.y = y;
		var color = $.color;
		var activity = this.activity;
//		var activityName = activity.name;
//		var displayName = "";
//		var textHeight = config.textHeight;
//		if(activityName.length > 5){
//			var i = activityName.length;
//			while(i>5){
//				displayName = displayName + activityName.substr(0,4) +"\n";
//				activityName = activityName.substr(4,activityName.length);
//				i = activityName.length;
//				textHeight = textHeight + config.textHeight;
//			}
//			displayName = displayName + activityName;
//		}else{
//			displayName = activityName;
//		}
//		this.textHeight = textHeight;
		var displayName = activity.name.length > 6 ? activity.name.substr(0, 5) + "..." : activity.name;
        
    	if(activity.nodeType =="Start"){
			this.image = paper.image(webBasePATH+"common/images/flow/start.png",this.x+this.nodeWidth/2-this.imageRadius/2,this.y+this.offsetY,this.imageRadius,this.imageRadius);
			//文字
    		this.text = paper.text(this.x+this.nodeWidth/2,y+this.imageRadius+this.textHeight/2+this.offsetY,displayName);
    		this.text.attr({width:this.nodeWidth,height:this.textHeight});
        }else if(activity.nodeType =="Finish"){
			this.image = paper.image(webBasePATH+"common/images/flow/finish.png",this.x+this.nodeWidth/2-this.imageRadius/2,this.y+this.offsetY,this.imageRadius,this.imageRadius);
			//文字
    		this.text = paper.text(this.x+this.nodeWidth/2,y+this.imageRadius+this.textHeight/2+this.offsetY,displayName);
    		this.text.attr({width:this.nodeWidth,height:this.textHeight});
        }else if(activity.nodeType == "Parallel"){
            this.image = paper.image(webBasePATH+"common/images/flow/parallel.png",this.x+this.nodeWidth/2-this.imageRadius/2,this.y+this.offsetY,this.imageRadius,this.imageRadius);
			this.text = paper.text(this.x+this.nodeWidth/2,this.y+this.imageRadius+this.textHeight/2+this.offsetY,displayName);
    		this.text.attr({width:this.nodeWidth,height:this.textHeight});
    	}else if(activity.nodeType == "Relation"){
            this.image = paper.image(webBasePATH+"common/images/flow/parallel_and.png",this.x+this.nodeWidth/2-this.imageRadius/2,this.y+this.offsetY,this.imageRadius,this.imageRadius);
			this.text = paper.text(this.x+this.nodeWidth/2,this.y+this.imageRadius+this.textHeight/2+this.offsetY,displayName);
    		this.text.attr({width:this.nodeWidth,height:this.textHeight});
		}else if(activity.nodeType == "Control"){
            this.image = paper.image(webBasePATH+"common/images/flow/control.png",this.x+this.nodeWidth/2-this.imageRadius/2,this.y+this.offsetY,this.imageRadius,this.imageRadius);
			this.text = paper.text(this.x+this.nodeWidth/2,this.y+this.imageRadius+this.textHeight/2+this.offsetY,displayName);
    		this.text.attr({width:this.nodeWidth,height:this.textHeight});
		}else{
			this.image = paper.image(webBasePATH+"common/images/flow/tache.png",this.x+this.nodeWidth/2-this.imageRadius/2,this.y+this.offsetY,this.imageRadius,this.imageRadius);
            var rect = this.image;
            this.text = paper.text(this.x+this.nodeWidth/2,this.y+this.imageRadius+this.textHeight/2+this.offsetY,displayName);
    		this.text.attr({width:this.nodeWidth,height:this.textHeight});
		};
		var node = this;
		this.image.mousedown(function(handler){
			if(handler.which==3)//右击事件
			{
				handler.preventDefault();
				handler.stopPropagation();
				if(flow.enableEdit)
				{
					$("#tacheMenu .edit").show();
				}else{
					$("#tacheMenu .edit").hide();
				}
				if(activity.nodeType=="Tache"||activity.nodeType=="Parallel"||activity.nodeType=="Control")
				{
					$("#tacheMenu").menu('show', {
	                    left: handler.pageX,
	                    top: handler.pageY,
						hideOnUnhover:false
	                }).data("node",node);
				}
			}
		});
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
		//新插入控制节点需要显示可以连接的节点
		if(ctrlFlag)
		{
			var box = paper.rect(this.x+this.offsetX-2,this.y+this.offsetY-2,this.imageRadius+4,this.imageRadius+4).attr({"stroke":"red"});
			this.image.click(function(handler){
				var ctrlActivity = flow.ctrlActivity;
				workArea.addTransition(ctrlActivity,activity);
			});
		}else{
			this.image.unclick();
		}
		if(activity.nodeType == "Control")
		{
			this.image.click(function(handler){
				workArea.paintAfterAdd(true,activity);
			});
		}
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
	
	this.getNodeOffsetY = function(){
		return ((workArea.config.nodeHeight-(config.imageRadius+config.textHeight))/2+config.imageRadius/2);
	};
	this.getNodeConfig = function(key)
	{
		return config[key];
	};
}
