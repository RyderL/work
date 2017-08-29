//=========zflow===========
(function($){
    var zflow = {};
    
    //配置信息
    zflow.config = {
        nodeWidth: 80,//节点的宽度-----垂直排版时生效
        nodeHeight: 80,//节点的高度-----水平排版时生效
        space: 30,//节点之间连线的长度
        offsetX: 0,//整个流程图的水平偏移
        offsetY: 20,//整个流程图的垂直偏移
        hLine: 10//跳转线条之间的高度间隔
    };
    
    //常量值
    zflow.constant = {
        MODE: {
            INST: 'inst',
            DEF: 'def'
        },
        DIRECTION: {
            H: 'horizontal',
            V: 'vertical'
        }
    };
    
    zflow.mode = {
        SELECT: "select",
        TACHE: "tache",
        PARALLEL: "parallel"
    }
    
    //状态信息
    zflow.state = {
        direction: "horizontal"//vertical：垂直排版     horizontal：水平排版
    };
    
    //工具
    zflow.util = {
        getGid: (function(){
            var id = 0;
            return function(){
                return ++id;
            };
        })(),
        getExtendedValue: function(doc, name){
            var node = $(doc).find("xpdl\\:ExtendedAttribute[Name='" + name + "']");
            if (node.length == 0) {
                //chrome  中对xml命名空间的解析
                node = $(doc).find("ExtendedAttribute[Name='" + name + "']");
            }
            return node.attr("Value");
        }
    };
    
    //----------activity-------start--------
    zflow.Activity = function(xml, paper, obj){
        if (xml) {
            this.id = $(xml).attr("Id");
            this.name = $(xml).attr("Name");
            
            this.branchIndex = zflow.util.getExtendedValue(xml, "branchIndex");
            this.nodeIndex = parseInt(zflow.util.getExtendedValue(xml, "nodeIndex"));
            this.nodeType = zflow.util.getExtendedValue(xml, "nodeType");
            this.numOfBranch = zflow.util.getExtendedValue(xml, "numOfBranch");
        }
        if (obj) {
            this.id = obj.id;
            this.name = obj.name;
            
            this.branchIndex = obj.branchIndex;
            this.nodeIndex = parseInt(obj.nodeIndex);
            this.nodeType = obj.nodeType;
            this.numOfBranch = obj.numOfBranch;
        }
        
        
        this.paper = paper;
        
        this.node = new zflow.node.Node(this);
        
        this.parallel = null;//结构：{start:1,end:1,vSize:1,hSize:1,branches:[{hSize:1,vSize:1,activities:[]}]};
        this.parallelActivity = null;//Relation节点对应的parallel节点
        this.parentParallel = null;//记录父并行结构
        this.circle = null;
        this.index = -1;
        this.x = 0;
        this.y = 0;
        this.getNextY = function(){
            //根据parallel和y来确定下一个y的值
            if (!this.parallel) {
                return this.y + this.node.getHeight();
            } else {
                return this.y + this.node.getHeight() + this.parallel.vSize;
            }
        };
        this.getHeight = function(){
            if (!this.parallel) {
                return this.node.getHeight();
            } else {
                return this.node.getHeight() + this.parallel.vSize;
            }
        };
        this.getWidth = function(){
            if (!this.parallel) {
                return this.node.getWidth();
            } else {
                return this.parallel.hSize;
            }
        };
        var blankIndex = -1;
        this.getBlankTransitionX = function(hSize){
            for (var i = blankIndex + 1; i < this.parallel.branches.length; i++) {
                if (this.parallel.branches[i].activities.length == 0) {
                    break;
                }
            }
			if(i==this.parallel.branches.length)
			{
				blankIndex = -1;
			}
            for (var i = blankIndex + 1; i < this.parallel.branches.length; i++) {
	            if (this.parallel.branches[i].activities.length == 0) {
					blankIndex = i;
	                break;
	            }
            }
            var countOffsetX = 0;
            for (var i = 0; i < blankIndex; i++) {
                countOffsetX += this.parallel.branches[i].hSize;
            }
            return {
                x: (this.x + zflow.config.offsetX + hSize / 2) + zflow.config.nodeWidth + countOffsetX - this.parallel.hSize / 2,
                branchIndex: blankIndex
            };
        };
        this.getCountBeforeBranchIndex = function(bIndex){
            var count = 0;
            for (var i = 0; i < bIndex; i++) {
                count += this.getCountForBranchIndex(i);
            }
            return count;
        };
        this.getCountForBranchIndex = function(bIndex){
            var count = 0;
            for (var i = 0; i < this.parallel.branches[bIndex].activities.length; i++) {
                count += this.parallel.branches[bIndex].activities[i].getCount();
            }
            return count;
        };
        this.getCount = function(){
            if (!this.parallel) {
                return 1;
            } else {
				//并行节点占一个位置
                var count = 1;
                for (var i = 0; i < this.parallel.branches.length; i++) {
                    count += this.getCountForBranchIndex(i);
                }
                return count;
            }
        };
        this.initParallel = function(activities, start){
            this.parallel = {};
            this.parallel.start = start;
            this.parallel.branches = [];
            for (var i = 0; i < this.numOfBranch; i++) {
                var actArr = [];
                this.parallel.branches.push({
                    hSize: 0,
                    vSize: 0,
                    activities: actArr
                });
            }
            for (var i = start + 1; i < activities.length; i++) {
                if (activities[i].nodeType == "Relation") {
                    activities[i].parallelActivity = this;
                    this.parallel.end = i;
                    var vSize = 0, hSize = 0;
                    for (var m = 0; m < this.parallel.branches.length; m++) {
                        var b_vSize = 0, b_hSize = 0;
                        for (var n = 0; n < this.parallel.branches[m].activities.length; n++) {
                            b_vSize += this.parallel.branches[m].activities[n].getHeight();
                            if (b_hSize < this.parallel.branches[m].activities[n].getWidth()) {
                                b_hSize = this.parallel.branches[m].activities[n].getWidth();
                            }
                        }
                        b_hSize = ((b_hSize == 0) ? zflow.config.nodeWidth : b_hSize);
                        hSize += b_hSize
                        if (vSize < b_vSize) {
                            vSize = b_vSize;
                        }
						vSize = (vSize==0?this.node.getHeight():vSize);
                        this.parallel.branches[m].hSize = b_hSize;
                        this.parallel.branches[m].vSize = b_vSize;
                    }
                    this.parallel.hSize = hSize;
                    this.parallel.vSize = vSize;
                    return (i - 1);
                } else if (activities[i].nodeType == "Parallel") {
                    activities[i].parentParallel = this;
                    var branchIndex = activities[i].branchIndex;
                    this.parallel.branches[activities[i].branchIndex].activities.push(activities[i]);
                    i = activities[i].initParallel(activities, i);
                    i = i + 1;
                    activities[i].parentParallel = this;
                    this.parallel.branches[branchIndex].activities.push(activities[i]);
                } else {
                    activities[i].parentParallel = this;
					console.log(activities[i].branchIndex+";"+this.parallel.branches.length);
                    this.parallel.branches[activities[i].branchIndex].activities.push(activities[i]);
                }
            }
        };
        
        this.updateData = function(x, y, i){
            this.x = x, this.y = y;
            if (!this.parallel) {
                return i;
            } else {
                var xOffset = 0;
                for (var m = 0; m < this.parallel.branches.length; m++) {
                    for (var n = 0; n < this.parallel.branches[m].activities.length; n++) {
                        this.parallel.branches[m].activities[n].updateData(this.x + xOffset + this.parallel.branches[m].hSize / 2 - this.parallel.hSize / 2, n == 0 ? this.y + this.node.getHeight() : this.parallel.branches[m].activities[n - 1].getNextY());
                    }
                    xOffset += this.parallel.branches[m].hSize;
                }
                return (this.parallel.end - 1);
            }
        };
		
        this.bindEventAddBranch = function(addBranch){
            if (this.circle) {
                $(this.circle.node).bind("click", {
                    activity: this
                }, addBranch);
            }
        };
        this.unbindEvent = function(){
            if (this.circle) {
                $(this.circle.node).unbind();
            }
        };
        
        this.paint = function(hSize, index){

            this.index = index;
            var x = this.x + zflow.config.offsetX + hSize / 2;
            var y = this.y + zflow.config.offsetY;
            //			console.log(this.name+";"+x+";"+y);
            if (this.node) {
                this.node.reset(this.paper, x, y);
            }
			if(this.parallel)
			{
				this.circle = this.paper.circle(x+zflow.config.nodeWidth, y+zflow.config.space/2+10, 5).attr({
		                        "fill": "#fff",cursor:"pointer"
		                    });
			}
						
        };
        
        //============四个连线点====start======
        this.getTopPoint = function(){
            return this.node.getTopPoint();
        };
        this.getLeftPoint = function(){
            return this.node.getLeftPoint();
        };
        this.getRightPoint = function(){
            return this.node.getRightPoint();
        };
        this.getBottomPoint = function(){
            return this.node.getBottomPoint();
        };
        //============四个连线点====end======
    };
    //----------activity--------end-------
    
    
    //----------Transition--------start-------
    zflow.Transition = function(xml, paper, obj){
        if (xml) {
            this.id = $(xml).attr("Id");
            this.name = $(xml).attr("Name");
            this.from = $(xml).attr("From");
            this.to = $(xml).attr("To");
            
            this.lineType = zflow.util.getExtendedValue(xml, "LineType");
            this.parentId = zflow.util.getExtendedValue(xml, "parentId");
        } else {
            this.id = obj.id;
            this.name = obj.name;
            this.from = obj.from;
            this.to = obj.to;
            this.lineType = obj.lineType;
            this.parentId = obj.parentId;
        }
        
        
        this.paper = paper;
        this.fromActivity = null;
        this.toActivity = null;
        this.branchIndex = -1;
        this.index = -1;
        this.line = null;
        this.circle = null;
        this.condition = null;
        
        this.bindEventAddNode = function(addNode){
            if (this.circle) {
                $(this.circle.node).bind("click", {
                    transition: this
                }, addNode);
            }
        };
        this.unbindEvent = function(){
            if (this.circle) {
                $(this.circle.node).unbind();
            }
        };
        
        this.paint = function(activityMap, hSize, index){
            this.index = index;
            
            var startNode = activityMap[this.from];
            var endNode = activityMap[this.to];
            this.fromActivity = startNode;
            this.toActivity = endNode;
            
            var startPoint = startNode.getBottomPoint();
            var endPoint = endNode.getTopPoint();
            
            var changePos = zflow.config.space * 2 / 5;
            
            if (startNode.nodeType == "Parallel") {
                if (endNode.nodeType == "Relation") {
                    var info = startNode.getBlankTransitionX(hSize);
					console.log(info.x);
                    var x = info.x;
                    this.branchIndex = info.branchIndex;
                    var firstPoint = {
                        x: startPoint.x,
                        y: startPoint.y + changePos
                    };
                    var secondPoint = {
                        x: x,
                        y: startPoint.y + changePos
                    };
                    var thirdPoint = {
                        x: x,
                        y: endPoint.y - changePos
                    };
                    var fourthPoint = {
                        x: endPoint.x,
                        y: endPoint.y - changePos
                    };
                    this.line = this.paper.path("M" + startPoint.x + " " + startPoint.y +
                    "L" +
                    firstPoint.x +
                    " " +
                    firstPoint.y +
                    "" +
                    "L" +
                    secondPoint.x +
                    " " +
                    secondPoint.y +
                    "" +
                    "L" +
                    thirdPoint.x +
                    " " +
                    thirdPoint.y +
                    "" +
                    "L" +
                    fourthPoint.x +
                    " " +
                    fourthPoint.y +
                    "" +
                    "L" +
                    endPoint.x +
                    " " +
                    endPoint.y +
                    "");
                    this.circle = this.paper.circle(secondPoint.x, (secondPoint.y + thirdPoint.y) / 2, 5).attr({
                        "fill": "#fff"
                    });
                } else {
                    var firstPoint = {
                        x: startPoint.x,
                        y: startPoint.y + changePos
                    };
                    var secondPoint = {
                        x: endPoint.x,
                        y: startPoint.y + changePos
                    };
                    this.line = this.paper.path("M" + startPoint.x + " " + startPoint.y +
                    "L" +
                    firstPoint.x +
                    " " +
                    firstPoint.y +
                    "" +
                    "L" +
                    secondPoint.x +
                    " " +
                    secondPoint.y +
                    "" +
                    "L" +
                    endPoint.x +
                    " " +
                    endPoint.y +
                    "");
                    this.circle = this.paper.circle(endPoint.x, (startPoint.y + endPoint.y) / 2, 5).attr({
                        "fill": "#fff"
                    });
                }
            } else {
                if (endNode.nodeType == "Relation") {
                    var firstPoint = {
                        x: startPoint.x,
                        y: endPoint.y - changePos
                    };
                    var secondPoint = {
                        x: endPoint.x,
                        y: endPoint.y - changePos
                    };
                    this.line = this.paper.path("M" + startPoint.x + " " + startPoint.y +
                    "L" +
                    firstPoint.x +
                    " " +
                    firstPoint.y +
                    "" +
                    "L" +
                    secondPoint.x +
                    " " +
                    secondPoint.y +
                    "" +
                    "L" +
                    endPoint.x +
                    " " +
                    endPoint.y +
                    "");
                    this.circle = this.paper.circle(startPoint.x, (startPoint.y + endPoint.y) / 2, 5).attr({
                        "fill": "#fff"
                    });
                } else {
                    this.line = this.paper.path("M" + startPoint.x + " " + startPoint.y + "L" + endPoint.x + " " + endPoint.y + "");
                    this.circle = this.paper.circle(startPoint.x, (startPoint.y + endPoint.y) / 2, 5).attr({
                        "fill": "#fff"
                    });
                }
            }
            if (this.line) {
                this.line.attr({
                    "stroke-width": 2,
                    stroke: "#444",
                    "arrow-end": "classic-wide-long"
                });
            }
            if (this.circle) {
                $(this.circle.node).css({
                    cursor: "pointer"
                });
                switch ($(paper).data("mode")) {
                    case zflow.mode.SELECT:
                        this.circle.hide();
                        break;
                    case zflow.mode.TACHE:
                    case zflow.mode.PARALLEL:
                        this.circle.show();
                        break;
                    default:
                        break;
                }
            }
        }
    };
    //----------Transition--------end-------
    
    //初始化方法
    zflow.init = function(ele, flowXML){
        //初始化变量
        var ele = ele;//div的dom
        var activities = new Array();
        
        var transitions = new Array();
        var flowXML = $.parseXML(flowXML);
        var paper = Raphael($(ele)[0].id);
        
        
        //解析节点
        var activityNodes = $(flowXML).find("Activities").children();
        
        for (var i = 0; i < activityNodes.length; i++) {
            activities.push(new zflow.Activity(activityNodes[i], paper));
        }
        
        //解析线条
        $(flowXML).find("Transition").each(function(){
            var transition = new zflow.Transition(this, paper);
            transitions.push(transition);
        });
        
        
        //绘制-------start-------------
        var addNode = function(evt){
            var transition = evt.data.transition;
            var fromActivity = transition.fromActivity;
            var toActivity = transition.toActivity;
            //参数
            var branchIndex = -1;
            var nodeIndex = -1;
            var insertIndex = -1;
            var parentId = -1;
            
            if (fromActivity.nodeType == "Parallel") {
                //找到插入点
                if (toActivity.nodeType == "Relation") {
                    branchIndex = transition.branchIndex;
                } else {
                    branchIndex = toActivity.branchIndex;
                }
                insertIndex = fromActivity.getCountBeforeBranchIndex(branchIndex) + fromActivity.parallel.start + 1;
                
                //更新分支的nodeIndex
                for (var i = 0; i < fromActivity.parallel.branches[branchIndex].activities.length; i++) {
                    var activity = fromActivity.parallel.branches[branchIndex].activities[i];
                    activity.nodeIndex = parseInt(activity.nodeIndex)  + 1;
                }
                
                //更新参数
                nodeIndex = 0;
                parentId = fromActivity.id;
            } else {
                var realFromActivity = fromActivity;
                if (fromActivity.nodeType == "Relation") {
                    realFromActivity = fromActivity.parallelActivity;
                    insertIndex = realFromActivity.parallel.end + 1;
                } else {
                    insertIndex = fromActivity.index + 1;
                }
                
                if (realFromActivity.parentParallel)//在并行结构里面，那么需要处理分支
                {
                    //更新父并行结构该分支里面的nodeIndex
                    parentId = fromActivity.parentParallel.id;
                    var startFlag = false;
                    for (var i = 0; i < realFromActivity.parentParallel.parallel.branches[realFromActivity.branchIndex].activities.length; i++) {
                        var activity = realFromActivity.parentParallel.parallel.branches[realFromActivity.branchIndex].activities[i];
                        if (startFlag) {
                            activity.nodeIndex = parseInt(activity.nodeIndex) + 1;
                            continue;
                        }
                        if (activity.id == fromActivity.id) {
                            startFlag = true;
                        }
                    }
                } else {//在主干流程里面，非并行结构里面
                    parentId = 0;
                    //更新主干柳妈里面的nodeIndex
                    for (var i = insertIndex; i < activities.length; i++) {
                        activities[i].nodeIndex = parseInt(activities[i].nodeIndex) + 1;
                        if (activities[i].nodeType == "Parallel") {
                            i = activities[i].parallel.end;
                        }
                    }
                }
                
                //更新参数
                nodeIndex = parseInt(realFromActivity.nodeIndex) + 1;
                parentId = fromActivity.id;
                branchIndex = realFromActivity.branchIndex;
            }
            
            if ($(paper).data("mode") == zflow.mode.TACHE) {
                //初始化节点信息
                var id = zflow.util.getGid();
                var activity = new zflow.Activity(null, paper, {
                    id: id,
                    name: "test" + zflow.util.getGid(),
                    branchIndex: branchIndex,
                    nodeIndex: nodeIndex,
                    nodeType: "Tache",
                    numOfBranch: 0
                });
                //插入节点
                activities.splice(insertIndex, 0, activity);
                
                //初始化线条
                var firstTran = new zflow.Transition(null, paper, {
                    id: zflow.util.getGid(),
                    name: "test" + zflow.util.getGid(),
                    from: fromActivity.id,
                    to: id,
                    lineType: "Normal",
                    parentId: parentId
                });
                var secondTran = new zflow.Transition(null, paper, {
                    id: zflow.util.getGid(),
                    name: "test" + zflow.util.getGid(),
                    from: id,
                    to: toActivity.id,
                    lineType: "Normal",
                    parentId: parentId
                });
                
                //插入线条													
                transitions.splice(transition.index, 1, secondTran);
                transitions.splice(transition.index, 0, firstTran);
            } else if ($(paper).data("mode") == zflow.mode.PARALLEL) {
                //初始化节点信息
                var pid = zflow.util.getGid();
                var rid = zflow.util.getGid();
                var pActivity = new zflow.Activity(null, paper, {
                    id: pid,
                    name: "并行节点",
                    branchIndex: branchIndex,
                    nodeIndex: nodeIndex,
                    nodeType: "Parallel",
                    numOfBranch: 1
                });
                var rActivity = new zflow.Activity(null, paper, {
                    id: rid,
                    name: "合并节点",
                    nodeType: "Relation",
                });
                
                //插入节点
                activities.splice(insertIndex, 0, rActivity);
                activities.splice(insertIndex, 0, pActivity);
                
                //初始化线条
                var firstTran = new zflow.Transition(null, paper, {
                    id: zflow.util.getGid(),
                    name: "test" + zflow.util.getGid(),
                    from: fromActivity.id,
                    to: pid,
                    lineType: "Normal",
                    parentId: parentId
                });
                var secondTran = new zflow.Transition(null, paper, {
                    id: zflow.util.getGid(),
                    name: "test" + zflow.util.getGid(),
                    from: pid,
                    to: rid,
                    lineType: "Normal",
                    parentId: parentId
                });
                var thirdTran = new zflow.Transition(null, paper, {
                    id: zflow.util.getGid(),
                    name: "test" + zflow.util.getGid(),
                    from: rid,
                    to: toActivity.id,
                    lineType: "Normal",
                    parentId: parentId
                });
                
                //插入线条									
                transitions.splice(transition.index, 1, thirdTran);
                transitions.splice(transition.index, 0, secondTran);
                transitions.splice(transition.index, 0, firstTran);
            }
            $("#tool-choose").trigger("click");
        };
		
		var addBranch = function(evt)
		{
			//增加分支数
			var act = evt.data.activity;
			act.numOfBranch = 1 + parseInt(act.numOfBranch);
			
			//增加线条
            var firstTran = new zflow.Transition(null, paper, {
                id: zflow.util.getGid(),
                name: "test" + zflow.util.getGid(),
                from: act.id,
                to: activities[act.parallel.end].id,
                lineType: "Normal",
                parentId: act.id
            });
			 transitions.splice(0, 0, firstTran);
			
			$("#tool-choose").trigger("click");
		};
		
        var paint = function(){
            //注销绑定事件
            for (var i = 0; i < transitions.length; i++) {
                transitions[i].unbindEvent();
            }
            for (var i = 0; i < activities.length; i++) {
                activities[i].unbindEvent();
            }
            
            for (var i = 0; i < activities.length; i++) {
                if (activities[i].nodeType == "Parallel") {
                    i = activities[i].initParallel(activities, i);
                }
            }
            
            var pre = 0;
            var hSize = 0, vSize = 0;
            for (var i = 0; i < activities.length; i++) {
                if (hSize < activities[i].getWidth()) {
                    hSize = activities[i].getWidth();
                }
                vSize += activities[i].getHeight();
                var temp = i;
                i = activities[i].updateData(0, i == 0 ? 0 : activities[pre].getNextY(), i);
                pre = temp;
            }
            paper.setSize(hSize + zflow.config.offsetX + 100, vSize + zflow.config.offsetY + 100);
            var activityMap = {};
            for (var i = 0; i < activities.length; i++) {
                activities[i].paint(hSize, i);
                activityMap[activities[i].id] = activities[i];
            }
            
            for (var i = 0; i < transitions.length; i++) {
                transitions[i].paint(activityMap, hSize, i);
            }
            //绑定事件--------start------------------
			for (var i = 0; i < activities.length; i++) {
                activities[i].bindEventAddBranch(addBranch);
            }
            for (var i = 0; i < transitions.length; i++) {
                transitions[i].bindEventAddNode(addNode);
            }
            //绑定事件--------finish------------------
        };
        //绘制-------finish-------------
        
        $(".tool-node").bind("click", function(evt){
            $(".node-checked").removeClass("node-checked");
            $(this).addClass("node-checked");
            
            var id = $(this).attr("id");
            switch (id) {
                case "tool-choose":
                    $(paper).data("mode", zflow.mode.SELECT);
                    break;
                case "tool-tache":
                    $(paper).data("mode", zflow.mode.TACHE);
                    break;
                case "tool-parallel":
                    $(paper).data("mode", zflow.mode.PARALLEL);
                    break;
                default:
                    break;
            }
            paper.clear();
            paint();
        });
        
        $("#tool-choose").trigger("click");
    };
    
    $.fn.zflow = function(flowXML, state){
        $.extend(zflow.state, state);
        this.each(function(){
            zflow.init(this, flowXML);
        });
    };
    
    $.zflow = zflow;
})(jQuery);
//=========zflow===========
