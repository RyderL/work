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
    	if(activity.nodeType =="Start"){
        	//圆
        	this.image = paper.circle(this.x+this.nodeWidth/2,y+this.imageRadius/2+this.offsetY,this.imageRadius/2);
    		this.image.attr({stroke: "none","fill":"r(0.75, 0.25)rgb(181, 251, 162)-rgb(43, 194, 56)","fill-opacity":1});
    		//阴影
    		this.shade = paper.ellipse(this.x+this.nodeWidth/2,y+this.imageRadius/2+this.imageRadius/2-this.imageRadius/2/50+this.offsetY,
    				this.imageRadius/2, this.imageRadius/2/8);
    		this.shade.attr({stroke: "none",fill: "rhsb(" + 0.5 + ", 1, .25)-hsb(" + 0.5 + ", 1, .25)",opacity: 0});
    		//文字
    		this.text = paper.text(this.x+this.nodeWidth/2,y+this.imageRadius+this.textHeight/2+this.offsetY,activity.name);
    		this.text.attr({width:this.nodeWidth,height:this.textHeight});
        }else if(activity.nodeType =="Finish"){
        	//圆
        	this.image = paper.circle(this.x+this.nodeWidth/2,y+this.imageRadius/2+this.offsetY,this.imageRadius/2);
    		this.image.attr({stroke: "none","fill":"r(0.75, 0.25)rgb(251, 188, 191)-rgb(224, 65, 59)","fill-opacity":1});
    		//阴影
    		this.shade = paper.ellipse(this.x+this.nodeWidth/2,y+this.imageRadius/2+this.imageRadius/2-this.imageRadius/2/50+this.offsetY,
    				this.imageRadius/2, this.imageRadius/2/8);
    		this.shade.attr({stroke: "none",fill: "rhsb(" + 0.5 + ", 1, .25)-hsb(" + 0.5 + ", 1, .25)",opacity: 0});
    		//文字
    		this.text = paper.text(this.x+this.nodeWidth/2,y+this.imageRadius+this.textHeight/2+this.offsetY,activity.name);
    		this.text.attr({width:this.nodeWidth,height:this.textHeight});
        }else if(activity.nodeType == "Parallel"){
    		var px = this.x + this.offsetX + (this.imageRadius - this.parallelNodeCrossLength)/2;
        	var py = this.y + this.offsetY + (this.imageRadius - this.parallelNodeCrossLength)/2;
        	var pWidth = this.parallelNodeCrossLength;
        	var pHeight = this.parallelNodeCrossLength;
        	this.image = paper.rect(px,py,pWidth,pHeight);
            this.image.attr({stroke: color.colorMoreGray, "stroke-width": 2,fill:color.colorPurple, transform : "r45"});
            this.path = paper.path([
										["M", px+pWidth/3, py+pHeight/3], 
										["L", px+pWidth*2/3, py+pHeight*2/3], 
										["M", px+pWidth*2/3, py+pHeight/3], 
										["L", px+pWidth/3, py+pHeight*2/3]
                   	                ]).attr({stroke: color.colorMoreGray, "stroke-width": 2});
            this.text = paper.text(this.x+this.nodeWidth/2,this.y+this.imageRadius+this.textHeight/2+this.offsetY,activity.name);
    		this.text.attr({width:this.nodeWidth,height:this.textHeight});
    	}else if(activity.nodeType == "Relation"){
			var px = this.x + this.offsetX + (this.imageRadius - this.parallelNodeCrossLength)/2;
        	var py = this.y + this.offsetY + (this.imageRadius - this.parallelNodeCrossLength)/2;
        	var pWidth = this.parallelNodeCrossLength;
        	var pHeight = this.parallelNodeCrossLength;
        	this.image = paper.rect(px,py,pWidth,pHeight);
            this.image.attr({stroke: color.colorMoreGray, "stroke-width": 2,fill:color.colorPurple, transform : "r45"});
            this.path = paper.path([
                   	                    ["M", px+pWidth/5, py+pHeight/2], 
                   	                    ["L", px+pWidth*4/5, py+pHeight/2], 
                   	                    ["M", px+pWidth/2, py+pHeight/5], 
                   	                    ["L", px+pWidth/2, py+pHeight*4/5]
                   	                ]).attr({stroke: color.colorMoreGray, "stroke-width": 2});
            this.text = paper.text(this.x+this.nodeWidth/2,this.y+this.imageRadius+this.textHeight/2+this.offsetY,activity.name);
    		this.text.attr({width:this.nodeWidth,height:this.textHeight});
		}else if(activity.nodeType == "Control"){
			var px = this.x + this.offsetX + (this.imageRadius - this.parallelNodeCrossLength)/2;
        	var py = this.y + this.offsetY + (this.imageRadius - this.parallelNodeCrossLength)/2;
        	var pWidth = this.parallelNodeCrossLength;
        	var pHeight = this.parallelNodeCrossLength;
        	this.image = paper.rect(px,py,pWidth,pHeight);
            this.image.attr({stroke: color.colorPurple, "stroke-width": 2,fill:color.colorYellow, transform : "r45"});
            this.text = paper.text(this.x+this.nodeWidth/2,this.y+this.imageRadius+this.textHeight/2+this.offsetY,activity.name);
    		this.text.attr({width:this.nodeWidth,height:this.textHeight});
		}else{
        	var col = color.colorGray;
    		this.image = paper.rect(this.x+this.offsetX,this.y+this.offsetY,this.imageRadius,this.imageRadius);
            this.image.attr({stroke: "none",fill:col,r:5});
            var rect = this.image;
            this.text = paper.text(this.x+this.nodeWidth/2,this.y+this.imageRadius+this.textHeight/2+this.offsetY,activity.name);
    		this.text.attr({width:this.nodeWidth,height:this.textHeight});
		};
		var node = this;
		this.image.mousedown(function(handler){
			if(handler.which==3)//右击事件
			{
				handler.preventDefault();
				handler.stopPropagation();
				if(activity.nodeType=="Tache"||activity.nodeType=="Parallel"||activity.nodeType=="Control")
				{
					if(flow.enableEdit)
					{
						$("#tacheMenu .edit").show();
					}else{
						$("#tacheMenu .edit").hide();
					}
					$("#tacheMenu").menu('show', {
	                    left: handler.pageX,
	                    top: handler.pageY,
						hideOnUnhover:false
	                }).data("node",node);
				}
			}
		});
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
