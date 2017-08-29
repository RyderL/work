(function($){
	var zflow = $.zflow;
	
	zflow.node = {};
    
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
    		this.activity.name = this.activity.name.length > 5 ? this.activity.name.substr(0, 4) + "..." : this.activity.name;
        	if(this.activity.type =="Start"){
            	//圆
            	this.image = paper.circle(this.x,this.y+zflow.config.imageWidth/2,zflow.config.imageWidth/2);
        		this.image.attr({stroke: "none","fill":"r(0.75, 0.25)rgb(181, 251, 162)-rgb(43, 194, 56)","fill-opacity":1});
        		//阴影
        		this.shade = paper.ellipse(this.x,y+zflow.config.imageWidth/2+zflow.config.imageWidth/2-zflow.config.imageWidth/2/50,
        				zflow.config.imageWidth/2, zflow.config.imageWidth/2/8);
        		this.shade.attr({stroke: "none",fill: "rhsb(" + 0.5 + ", 1, .25)-hsb(" + 0.5 + ", 1, .25)",opacity: 0});
        		//文字
        		this.text = paper.text(this.x,y+zflow.config.imageHeight+zflow.config.textHeight/2,this.activity.name);
            }else if(this.activity.type =="Finish"){
				//圆
            	this.image = paper.circle(this.x,this.y+zflow.config.imageWidth/2,zflow.config.imageWidth/2);
        		this.image.attr({stroke: "none","fill":"r(0.75, 0.25)rgb(251, 188, 191)-rgb(224, 65, 59)","fill-opacity":1});
        		//阴影
        		this.shade = paper.ellipse(this.x,y+zflow.config.imageWidth/2+zflow.config.imageWidth/2-zflow.config.imageWidth/2/50,
        				zflow.config.imageWidth/2, zflow.config.imageWidth/2/8);
        		this.shade.attr({stroke: "none",fill: "rhsb(" + 0.5 + ", 1, .25)-hsb(" + 0.5 + ", 1, .25)",opacity: 0});
        		//文字
        		this.text = paper.text(this.x,y+zflow.config.imageHeight+zflow.config.textHeight/2,this.activity.name);
            }else if(this.activity.type == "Parallel"){
        		var px = this.x - this.parallelNodeCrossLength/2;
            	var py = this.y  + (zflow.config.imageWidth - this.parallelNodeCrossLength)/2;
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
                this.text = paper.text(this.x,this.y+zflow.config.imageHeight+zflow.config.textHeight/2,this.activity.name);
        	}else if(this.activity.type == "Relation"){
				var px = this.x - this.parallelNodeCrossLength/2;
            	var py = this.y + (zflow.config.imageWidth - this.parallelNodeCrossLength)/2;
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
                this.text = paper.text(this.x,this.y+zflow.config.imageHeight+zflow.config.textHeight/2,this.activity.name);
    		}else{
            	var col = color.colorGray;
            	var running = false;
        		//判断是否为定义图
        		if (zflow.state.mode == zflow.constant.MODE.INST) {
        			if(zflow.state.stateConfig[this.activity.state])
    				{
        				col = zflow.state.stateConfig[this.activity.state][1];
        				running = zflow.state.stateConfig[this.activity.state][2];
    				}
				}
        		this.image = paper.rect(this.x - zflow.config.imageWidth/2,this.y,zflow.config.imageWidth,zflow.config.imageWidth);
                this.image.attr({stroke: "none",fill:col,r:5});
                var rect = this.image;
                if(running == true){
            		// 闪烁效果
            		setInterval(function(){rect.animate({"fill-opacity": .2}, 800, rect.attr({"fill-opacity": 1}))},800);
                }
                this.text = paper.text(this.x,this.y+zflow.config.imageHeight+zflow.config.textHeight/2,this.activity.name);
    		};
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
