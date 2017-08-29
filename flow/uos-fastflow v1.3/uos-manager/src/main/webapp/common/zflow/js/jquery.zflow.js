//=========zflow===========
(function($){
    var zflow = {};
    var color = $.color;
    var instActivities = [];
    zflow.config = {
        nodeWidth: 80,//节点的宽度-----垂直排版时生效
        nodeHeight: 50,//节点的高度-----水平排版时生效
        imageWidth: 31,
        imageHeight: 26,
        textHeight: 20,//节点文字高度
        offsetX: 50,//整个流程图的水平偏移
        offsetY: 30
    };
    
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
    
    zflow.state = {
        mode: zflow.constant.MODE.DEF,//def:定义图       inst:实例图
        direction: zflow.constant.DIRECTION.V,//vertical：垂直排版     horizontal：水平排版
        //状态编码：状态描述，状态颜色，是否闪烁
        stateConfig: {
            "10D": ["处理中", color.colorGreen, true, "tache-yd-2.gif"],
            "10F": ["处理完成", color.colorBlue, false, "tache-yd-3.png"],
            "10A": ["异常", color.colorRed, false, "fq-yd-2.png"],
            "FFF": ["未处理", color.colorLOCKED, false, "tache-yd-1.png"],
            "10I": ["挂起", color.colorYellow, false, "gq-yd.png"],
            "10E": ["终止", color.colorABOLOSH, false, "zz-yd.png"],
            "10R": ["归档", color.colorBrown, false, "fq-yd.png"],
            "EEE": ["反向单",color.colorPurple,false,"gd-yd.png"]
        },
        nameConfig:{
        	"CRMBJ":{
        		"10D": ["处理中", color.colorGreen, true, "CRMBJ-doing.gif"],
                "10F": ["处理完成", color.colorBlue, false, "CRMBJ-finish.png"],
                "10A": ["异常", color.colorRed, false, "CRMBJ-yc.png"],
                "FFF": ["未处理", color.colorLOCKED, false, "CRMBJ-undo.png"],
                "10I": ["挂起", color.colorYellow, false, "CRMBJ-gq.png"],
                "10E": ["终止", color.colorABOLOSH, false, "termit.png"],
                "10R": ["归档", color.colorBrown, false, "CRMBJ-fq.png"],
                "EEE": ["反向单",color.colorPurple,false,"CRMBJ-fx.png"],
                "DEF": ["定义图",color.colorPurple,false,"CRMBJ-def.png"]},
        	"WXSG":{
        		"10D": ["处理中", color.colorGreen, true, "WXSG-doing.gif"],
                "10F": ["处理完成", color.colorBlue, false, "WXSG-finish.png"],
                "10A": ["异常", color.colorRed, false, "WXSG-yc.png"],
                "FFF": ["未处理", color.colorLOCKED, false, "WXSG-undo.png"],
                "10I": ["挂起", color.colorYellow, false, "WXSG-gq.png"],
                "10E": ["终止", color.colorABOLOSH, false, "termit.png"],
                "10R": ["归档", color.colorBrown, false, "WXSG-fq.png"],
                "EEE": ["反向单",color.colorPurple,false,"WXSG-fx.png"],
                "DEF": ["定义图",color.colorPurple,false,"WXSG-def.png"]},
        	"WLJH":{
        		"10D": ["处理中", color.colorGreen, true, "WLJH-doing.gif"],
                "10F": ["处理完成", color.colorBlue, false, "WLJH-finish.png"],
                "10A": ["异常", color.colorRed, false, "WLJH-yc.png"],
                "FFF": ["未处理", color.colorLOCKED, false, "WLJH-undo.png"],
                "10I": ["挂起", color.colorYellow, false, "WLJH-gq.png"],
                "10E": ["终止", color.colorABOLOSH, false, "termit.png"],
                "10R": ["归档", color.colorBrown, false, "WLJH-fq.png"],
                "EEE": ["反向单",color.colorPurple,false,"WLJH-fx.png"],
                "DEF": ["定义图",color.colorPurple,false,"WLJH-def.png"]},
        	"ZYPZ":{
        		"10D": ["处理中", color.colorGreen, true, "ZYPZ-doing.gif"],
                "10F": ["处理完成", color.colorBlue, false, "ZYPZ-finish.png"],
                "10A": ["异常", color.colorRed, false, "ZYPZ-yc.png"],
                "FFF": ["未处理", color.colorLOCKED, false, "ZYPZ-undo.png"],
                "10I": ["挂起", color.colorYellow, false, "ZYPZ-gq.png"],
                "10E": ["终止", color.colorABOLOSH, false, "termit.png"],
                "10R": ["归档", color.colorBrown, false, "ZYPZ-fq.png"],
                "EEE": ["反向单",color.colorPurple,false,"ZYPZ-fx.png"],
                "DEF": ["定义图",color.colorPurple,false,"ZYPZ-def.png"]},
        	"PUBLIC":{
        		"10D": ["处理中", color.colorGreen, true, "PUBLIC-doing.gif"],
                "10F": ["处理完成", color.colorBlue, false, "PUBLIC-finish.png"],
                "10A": ["异常", color.colorRed, false, "PUBLIC-yc.png"],
                "FFF": ["未处理", color.colorLOCKED, false, "PUBLIC-undo.png"],
                "10I": ["挂起", color.colorYellow, false, "PUBLIC-gq.png"],
                "10E": ["终止", color.colorABOLOSH, false, "termit.png"],
                "10R": ["归档", color.colorBrown, false, "PUBLIC-fq.png"],
                "EEE": ["反向单",color.colorPurple,false,"PUBLIC-fx.png"],
                "DEF": ["定义图",color.colorPurple,false,"PUBLIC-def.png"]}
        }
    };
    
    //----------activity-------start--------
    zflow.Activity = function(process, activityXML, branchIndex, numOfBranch){
        //节点属性========start================
        this.activityXML = activityXML;
        this.id = $(activityXML).attr("id");
        this.name = $(activityXML).attr("name");
        this.tacheId = $(activityXML).attr("tacheId");
        this.isRunning = $(activityXML).attr("isRunning");
        this.type = $(activityXML).attr("type");
        this.state = $(activityXML).attr("state");
        this.isTimeOut = $(activityXML).attr("isTimeOut");
        this.workOrderId = $(activityXML).attr("workOrderId");
        this.direction = $(activityXML).attr("direction");
        //added by xujun 2010-09-20 ur62311 原子流程改造——组合流程的流程实例图修改
        this.atomFlowId = $(activityXML).attr("atomFlowId");
        //add by 陈智堃 2011-07-15 光进铜退改造，活动所在的原子流程/数据驱动环节定义ID
        this.atomActivityId = $(activityXML).attr("atomActivityId");
        this.workItemId = $(activityXML).attr("workItemId");
        //增加环节图标名字
        this.tacheIconName = $(activityXML).attr("tacheIconName");
        //节点属性========end================
        
        this.process = process;
        
        this.branchIndex = parseInt(branchIndex);
        this.numOfBranch = parseInt(numOfBranch);
        this.toTransitions = null;// 每次绘制重置
        this.index = -1;// 每次绘制重置
        var width, height;// 每次绘制重置
        var x, y;// 每次绘制重置
        var rightCtrl, leftCtrl;// 每次绘制重置
        this.node = zflow.node.init(this);
        
        this.paint = function(){
            if (this.node) {
                this.node.reset(process.getGraphics(), x, y);
            }
        };
        
        this.updateSize = function(){
            width = zflow.config.nodeWidth;
            height = zflow.config.nodeHeight;
        };
        
        this.updatePosition = function(px, py){
            x = px;
            y = py;
        };
        
        this.getWidth = function(){
            if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                return width;
            } else {
                rightCtrl = rightCtrl ? rightCtrl : 0;
                leftCtrl = leftCtrl ? leftCtrl : 0;
                var max = rightCtrl > leftCtrl ? rightCtrl : leftCtrl;
                if (max != 0) {
                    return max * 2;
                }
                return width;
            }
        };
        
        this.getHeight = function(){
            if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                rightCtrl = rightCtrl ? rightCtrl : 0;
                leftCtrl = leftCtrl ? leftCtrl : 0;
                var max = rightCtrl > leftCtrl ? rightCtrl : leftCtrl;
                if (max != 0) {
                    return max * 2;
                }
                return height;
            } else {
                return height;
            }
        };
        
        this.getX = function(){
            return x;
        };
        
        this.getY = function(){
            return y;
        };
        
        this.getRightCtrl = function(){
            if (rightCtrl) {
                return rightCtrl;
            } else {
                if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                    return height / 2;
                } else {
                    return width / 2;
                }
            }
        };
        
        this.getLeftCtrl = function(){
            if (leftCtrl) {
                return leftCtrl;
            } else {
                if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                    return height / 2;
                } else {
                    return width / 2;
                }
            }
        };
        
        this.setRightCtrl = function(rc){
            rightCtrl = rc;
        };
        
        this.setLeftCtrl = function(lc){
            leftCtrl = lc;
        };
        
        this.getTop = function(){
            return this.node.getTopPoint();
        };
        this.getLeft = function(){
            return this.node.getLeftPoint();
        };
        this.getRight = function(){
            return this.node.getRightPoint();
        };
        this.getBottom = function(){
            return this.node.getBottomPoint();
        };
        
        this.isTache = function(){
            return this.type == "Tache";
        };
        
        this.isRelation = function(){
			if(this.drawType){
				return this.drawType == "Relation"; 
			}
            return this.type == "Relation";
        };
        
        this.isParallel = function(){
			if(this.drawType){
				return this.drawType == "Parallel"; 
			}
            return this.type == "Parallel";
        };
		
        this.isControl = function(){
            return this.type == "Control";
        };
        
        this.log = function(){
//            console.log("activity:" + name + "~" + this.index + "~" + this.getWidth() + "~" + height + "~" + x + "~" + y);
        };
    };
    //----------activity--------end-------
    
    //----------Parallel--------start-------
    zflow.Parallel = function(){
        var process;
        
        var branches, parallelActivity;
        
        var width, height;
        var x, y;
        var rightCtrl, leftCtrl;
        
        this.init = function(proc, index){
            process = proc;
            branches = [];
            
            var parallelPair = process.getParallelPair();
            var activities = process.getActivities();
            
            parallelActivity = activities[index];
            var numOfBranch = parallelActivity.numOfBranch;
            
            var j = index + 1;
            for (var i = 0; i < numOfBranch; i++) {
                var start = j;
                for (; j < activities.length; j++) {
                    if (activities[j].isRelation()) {
                        break;
                    } else {
                        if (activities[j].branchIndex != i) {
                            break;
                        } else {
                            if (activities[j].isParallel()) {
                                j = parallelPair[activities[j].id];
                            }
                        }
                    }
                }
                
                var branch = new zflow.Branch();
                branch.init(process, start, j, parallelActivity);
                branches.push(branch);
            }
            return j-1;
        };
        
        this.updateSize = function(){
            width = 0;
            height = 0;
            parallelActivity.updateSize();
            if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                width += parallelActivity.getWidth();
                var bWidth = 0;
                for (var i = 0; i < branches.length; i++) {
                    branches[i].updateSize();
                    height += branches[i].getHeight();
                    bWidth = bWidth < branches[i].getWidth() ? branches[i].getWidth() : bWidth;
                }
                width += bWidth;
            } else {
                height += parallelActivity.getHeight();
                var bHeight = 0;
                for (var i = 0; i < branches.length; i++) {
                    branches[i].updateSize();
                    width += branches[i].getWidth();
                    bHeight = bHeight < branches[i].getHeight() ? branches[i].getHeight() : bHeight;
                }
                height += bHeight;
            }
        };
        
        this.updatePosition = function(px, py){
            x = px;
            y = py;
            if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                parallelActivity.updatePosition(x, y);
                for (var i = 0; i < branches.length; i++) {
                    var offsetY = 0;
                    for (var j = 0; j < i; j++) {
                        offsetY += branches[j].getHeight();
                    }
                    branches[i].updatePosition(x + parallelActivity.getWidth(), y + offsetY + branches[i].getHeight() / 2 - height / 2);
                }
            } else {
                parallelActivity.updatePosition(x, y);
                for (var i = 0; i < branches.length; i++) {
                    var offsetX = 0;
                    for (var j = 0; j < i; j++) {
                        offsetX += branches[j].getWidth();
                    }
                    branches[i].updatePosition(x + offsetX + branches[i].getWidth() / 2 - width / 2, y + parallelActivity.getHeight());
                }
            }
        };
        
        this.getWidth = function(){
            if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                return width;
            } else {
                rightCtrl = rightCtrl ? rightCtrl : 0;
                leftCtrl = leftCtrl ? leftCtrl : 0;
                var max = rightCtrl > leftCtrl ? rightCtrl : leftCtrl;
                if (max != 0) {
                    return max * 2;
                }
                return width;
            }
        };
        
        this.getHeight = function(){
            if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                rightCtrl = rightCtrl ? rightCtrl : 0;
                leftCtrl = leftCtrl ? leftCtrl : 0;
                var max = rightCtrl > leftCtrl ? rightCtrl : leftCtrl;
                if (max != 0) {
                    return max * 2;
                }
                return height;
            } else {
                return height;
            }
        };
        
        this.getX = function(){
            return x;
        };
        
        this.getY = function(){
            return y;
        };
        
        this.getRightCtrl = function(){
            if (rightCtrl) {
                return rightCtrl;
            } else {
                if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                    return height / 2;
                } else {
                    return width / 2;
                }
            }
        };
        
        this.getLeftCtrl = function(){
            if (leftCtrl) {
                return leftCtrl;
            } else {
                if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                    return height / 2;
                } else {
                    return width / 2;
                }
            }
        };
        
        this.setRightCtrl = function(rc){
            rightCtrl = rc;
        };
        
        this.setLeftCtrl = function(lc){
            leftCtrl = lc;
        };
        
        this.log = function(){
//            console.log("parallel:" + branches.length + "~" + this.getWidth() + "~" + height + "~" + x + "~" + y);
            parallelActivity.log();
            for (var i = 0; i < branches.length; i++) {
                branches[i].log();
            }
        };
        
        this.getId = function(){
            return parallelActivity.id;
        };
    };
    
    //----------Parallel--------end-------
    
    zflow.Branch = function(){
        var process;
        //由activity和parallel组成,串行状态，
        var nodes, parallelActivity;
        var width, height;
        var x, y;
        
        this.init = function(proc, start, end, paralActivity){
            process = proc;
            nodes = [];
            
            var activities = process.getActivities();
            
            parallelActivity = paralActivity;
            
            for (var i = start; i < end; i++) {
                if (activities[i].isParallel()) {
                    var parallel = new zflow.Parallel();
                    i = parallel.init(process, i);
                    nodes.push(parallel);
                } else {
                    nodes.push(activities[i]);
                }
            }
        };
        
        this.updateSize = function(){
            var activityMap = process.getActivityMap();
            
            width = 0;
            height = 0;
            if (nodes.length == 0) {
                width = zflow.config.nodeWidth;
                height = zflow.config.nodeHeight;
            } else {
                //更新忽略控制结构的宽和高
                for (var i = 0; i < nodes.length; i++) {
                    nodes[i].updateSize();
                }
                //引入控制结构，重置宽和高
                for (var i = 0; i < nodes.length; i++) {
                    if ((nodes[i] instanceof zflow.Activity) && nodes[i].isControl()) {
                        var toLines = nodes[i].toTransitions;
                        for (var j = 0; j < toLines.length; j++) {
                            if (toLines[j].isControl()) {
                                var startNode = activityMap[toLines[j].from];
                                var endNode = activityMap[toLines[j].to];
                                if (startNode.index > endNode.index) {
                                    var temp = startNode;
                                    startNode = endNode;
                                    endNode = temp;
                                }
                                var startFlag = false;
                                var maxRight = 0;
                                var maxLeft = 0;
                                var updateCtrlNodes = [];
                                for (var m = 0; m < nodes.length; m++) {
                                    if (nodes[m].id == startNode.id) {
                                        startFlag = true;
                                    }
                                    if (startFlag) {
                                        maxRight = maxRight < nodes[m].getRightCtrl() ? nodes[m].getRightCtrl() : maxRight;
                                        maxLeft = maxLeft < nodes[m].getLeftCtrl() ? nodes[m].getLeftCtrl() : maxLeft;
                                        updateCtrlNodes.push(nodes[m]);
                                    }
                                    if (nodes[m].id == endNode.id) {
                                        break;
                                    }
                                }
                                if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                                    if (maxRight < maxLeft) {
                                        toLines[j].updateCtrl("right", maxRight + zflow.config.nodeHeight);
                                        for (var m = 0; m < updateCtrlNodes.length; m++) {
                                            updateCtrlNodes[m].setRightCtrl(maxRight + zflow.config.nodeHeight);
                                        }
                                    } else {
                                        toLines[j].updateCtrl("left", maxLeft + zflow.config.nodeHeight);
                                        for (var m = 0; m < updateCtrlNodes.length; m++) {
                                            updateCtrlNodes[m].setLeftCtrl(maxLeft + zflow.config.nodeHeight);
                                        }
                                    }
                                } else {
                                    if (maxRight < maxLeft) {
                                        toLines[j].updateCtrl("right", maxRight + zflow.config.nodeWidth);
                                        for (var m = 0; m < updateCtrlNodes.length; m++) {
                                            updateCtrlNodes[m].setRightCtrl(maxRight + zflow.config.nodeWidth);
                                        }
                                    } else {
                                        toLines[j].updateCtrl("left", maxLeft + zflow.config.nodeWidth);
                                        for (var m = 0; m < updateCtrlNodes.length; m++) {
                                            updateCtrlNodes[m].setLeftCtrl(maxLeft + zflow.config.nodeWidth);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //更新忽略控制结构的宽和高
                if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                    for (var i = 0; i < nodes.length; i++) {
                        height = height < nodes[i].getHeight() ? nodes[i].getHeight() : height;
                        width += nodes[i].getWidth();
                    }
                } else {
                    for (var i = 0; i < nodes.length; i++) {
                    
                        width = width < nodes[i].getWidth() ? nodes[i].getWidth() : width;
                        height += nodes[i].getHeight();
                    }
                }
            }
        };
        
        this.updatePosition = function(px, py){
            var blankBranchTransitions = process.getBlankBranchTransitions();
            
            x = px;
            y = py;
            if (nodes.length == 0) {//空分支
                if (parallelActivity != null) {
                    for (var i = 0; i < blankBranchTransitions.length; i++) {
                        if (blankBranchTransitions[i].from == parallelActivity.id) {
                            blankBranchTransitions[i].setPosition(x, y);
                            blankBranchTransitions.splice(i, 1);
                            break;
                        }
                    }
                }
            } else {
                for (var i = 0; i < nodes.length; i++) {
                    if (i == 0) {
                        nodes[i].updatePosition(x, y);
                    } else {
                        if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                            nodes[i].updatePosition(nodes[i - 1].getX() + nodes[i - 1].getWidth(), y);
                        } else {
                            nodes[i].updatePosition(x, nodes[i - 1].getY() + nodes[i - 1].getHeight());
                        }
                    }
                }
            }
        };
        this.getWidth = function(){
            return width;
        };
        
        this.getHeight = function(){
            return height;
        };
        
        this.getX = function(){
            return x;
        };
        
        this.getY = function(){
            return y;
        };
        
        this.log = function(){
//            console.log("branch:" + nodes.length + "~" + this.getWidth() + "~" + height + "~" + x + "~" + y);
            for (var i = 0; i < nodes.length; i++) {
                nodes[i].log();
            }
        };
    }
    
    //----------Transition--------start-------
    zflow.Transition = function(process, transitionXML){
        //===========线条属性初始化======start==========
        this.transitionXML = transitionXML;
        this.id = $(transitionXML).attr("id");
        this.name = $(transitionXML).attr("name");
        this.from = $(transitionXML).attr("from");
        this.to = $(transitionXML).attr("to");
        this.isRunning = $(transitionXML).attr("isRunning");
        this.direction = $(transitionXML).attr("direction");
        this.lineType = $(transitionXML).attr("lineType");
        if ($(transitionXML).attr("lineType") == "控制线条") {
            this.lineType = "Control";
        }
        //===========线条属性初始化======end==========
        
        this.process = process;
        this.line = null;//绘制的线条对象
        var x, y;//绘制时重置
        var direction = "right";//绘制时重置
        var ctrlExten = 0;//绘制时重置
        //水平绘制方法
        this.paintForH = function(activityMap){
            var startNode = activityMap[this.from];
            var endNode = activityMap[this.to];
            var startPoint = startNode.getRight();
            var endPoint = endNode.getLeft();
            var config = zflow.config;
            var paralOffset = (config.nodeWidth - config.imageWidth) * 1 / 4;
            if (this.isControl()) {
                if (direction == "right") {
                    var startPoint = startNode.getBottom();
                    var endPoint = endNode.getBottom();
                    var firstPoint = {
                        x: startPoint.x,
                        y: startPoint.y + ctrlExten - config.nodeHeight / 2 - config.imageHeight / 2 - config.textHeight
                    };
                    var secondPoint = {
                        x: endPoint.x,
                        y: startPoint.y + ctrlExten - config.nodeHeight / 2 - config.imageHeight / 2 - config.textHeight
                    };
                    line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y + "L" + firstPoint.x + " " + firstPoint.y +
                    "L" +
                    secondPoint.x +
                    " " +
                    secondPoint.y +
                    "L" +
                    endPoint.x +
                    " " +
                    endPoint.y);
                } else {
                    var startPoint = startNode.getTop();
                    var endPoint = endNode.getTop();
                    var firstPoint = {
                        x: startPoint.x,
                        y: startPoint.y - ctrlExten + config.nodeHeight / 2 + config.imageHeight / 2
                    };
                    var secondPoint = {
                        x: endPoint.x,
                        y: startPoint.y - ctrlExten + config.nodeHeight / 2 + config.imageHeight / 2
                    };
                    line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y + "L" + firstPoint.x + " " + firstPoint.y +
                    "L" +
                    secondPoint.x +
                    " " +
                    secondPoint.y +
                    "L" +
                    endPoint.x +
                    " " +
                    endPoint.y);
                }
            } else {
                if (startNode.isParallel()) {
                    if (endNode.isRelation()) {
                        if (x && y) {
                            var firstPoint = {
                                x: startPoint.x + paralOffset,
                                y: startPoint.y
                            };
                            var secondPoint = {
                                x: startPoint.x + paralOffset,
                                y: y + config.imageHeight / 2
                            };
                            var thirdPoint = {
                                x: endPoint.x - paralOffset,
                                y: y + config.imageHeight / 2
                            };
                            var fourthPoint = {
                                x: endPoint.x - paralOffset,
                                y: endPoint.y
                            };
                            line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y + "L" + firstPoint.x + " " + firstPoint.y +
                            "L" +
                            secondPoint.x +
                            " " +
                            secondPoint.y +
                            "L" +
                            thirdPoint.x +
                            " " +
                            thirdPoint.y +
                            "L" +
                            fourthPoint.x +
                            " " +
                            fourthPoint.y +
                            "L" +
                            endPoint.x +
                            " " +
                            endPoint.y);
                        }else{
							line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y + "L" + endPoint.x + " " + endPoint.y + "");
						}
                    } else {
                        var firstPoint = {
                            x: startPoint.x + paralOffset,
                            y: startPoint.y
                        };
                        var secondPoint = {
                            x: startPoint.x + paralOffset,
                            y: endPoint.y
                        };
                        line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y +
                        "L" +
                        firstPoint.x +
                        " " +
                        firstPoint.y +
                        "L" +
                        secondPoint.x +
                        " " +
                        secondPoint.y +
                        "L" +
                        endPoint.x +
                        " " +
                        endPoint.y +
                        "");
                    }
                } else {
                    if (endNode.isRelation()) {
                        var firstPoint = {
                            x: endPoint.x - paralOffset,
                            y: startPoint.y
                        };
                        var secondPoint = {
                            x: endPoint.x - paralOffset,
                            y: endPoint.y
                        };
                        line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y +
                        "L" +
                        firstPoint.x +
                        " " +
                        firstPoint.y +
                        "L" +
                        secondPoint.x +
                        " " +
                        secondPoint.y +
                        "L" +
                        endPoint.x +
                        " " +
                        endPoint.y);
                    } else {
                        line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y + "L" + endPoint.x + " " + endPoint.y + "");
                    }
                }
            }
            line.attr({
                "stroke-width": 2,
                stroke: "#444",
                "arrow-end": "classic-wide-long"
            });
        };
        
        //垂直绘制方法
        this.paintForV = function(activityMap){
            var startNode = activityMap[this.from];
            var endNode = activityMap[this.to];
            var startPoint = startNode.getBottom();
            var endPoint = endNode.getTop();
            var config = zflow.config;
            var paralOffset = (config.nodeHeight - config.imageHeight - config.textHeight) * 1 / 4;
            if (this.isControl()) {
                if (direction == "right") {
                    var startPoint = startNode.getRight();
                    var endPoint = endNode.getRight();
                    var firstPoint = {
                        x: startPoint.x + ctrlExten - config.nodeWidth / 2 - config.imageWidth / 2,
                        y: startPoint.y
                    };
                    var secondPoint = {
                        x: startPoint.x + ctrlExten - config.nodeWidth / 2 - config.imageWidth / 2,
                        y: endPoint.y
                    };
                    line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y + "L" + firstPoint.x + " " + firstPoint.y +
                    "L" +
                    secondPoint.x +
                    " " +
                    secondPoint.y +
                    "L" +
                    endPoint.x +
                    " " +
                    endPoint.y);
                } else {
                    var startPoint = startNode.getLeft();
                    var endPoint = endNode.getLeft();
                    var firstPoint = {
                        x: startPoint.x - ctrlExten + config.nodeWidth / 2 + config.imageWidth / 2,
                        y: startPoint.y
                    };
                    var secondPoint = {
                        x: startPoint.x - ctrlExten + config.nodeWidth / 2 + config.imageWidth / 2,
                        y: endPoint.y
                    };
                    line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y + "L" + firstPoint.x + " " + firstPoint.y +
                    "L" +
                    secondPoint.x +
                    " " +
                    secondPoint.y +
                    "L" +
                    endPoint.x +
                    " " +
                    endPoint.y);
                }
            } else {
                if (startNode.isParallel()) {
                    if (endNode.isRelation()) {
                        if (x && y) {
                            var firstPoint = {
                                x: startPoint.x,
                                y: startPoint.y + paralOffset
                            };
                            var secondPoint = {
                                x: x,
                                y: startPoint.y + paralOffset
                            };
                            var thirdPoint = {
                                x: x,
                                y: endPoint.y - paralOffset
                            };
                            var fourthPoint = {
                                x: endPoint.x,
                                y: endPoint.y - paralOffset
                            };
                            line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y + "L" + firstPoint.x + " " + firstPoint.y +
                            "L" +
                            secondPoint.x +
                            " " +
                            secondPoint.y +
                            "L" +
                            thirdPoint.x +
                            " " +
                            thirdPoint.y +
                            "L" +
                            fourthPoint.x +
                            " " +
                            fourthPoint.y +
                            "L" +
                            endPoint.x +
                            " " +
                            endPoint.y);
                        }else{
							line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y + "L" + endPoint.x + " " + endPoint.y + "");
						}
                    } else {
                        var firstPoint = {
                            x: startPoint.x,
                            y: startPoint.y + paralOffset
                        };
                        var secondPoint = {
                            x: endPoint.x,
                            y: startPoint.y + paralOffset
                        };
                        line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y +
                        "L" +
                        firstPoint.x +
                        " " +
                        firstPoint.y +
                        "L" +
                        secondPoint.x +
                        " " +
                        secondPoint.y +
                        "L" +
                        endPoint.x +
                        " " +
                        endPoint.y +
                        "");
                    }
                } else {
                    if (endNode.isRelation()) {
                        var firstPoint = {
                            x: startPoint.x,
                            y: endPoint.y - paralOffset
                        };
                        var secondPoint = {
                            x: endPoint.x,
                            y: endPoint.y - paralOffset
                        };
                        line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y +
                        "L" +
                        firstPoint.x +
                        " " +
                        firstPoint.y +
                        "L" +
                        secondPoint.x +
                        " " +
                        secondPoint.y +
                        "L" +
                        endPoint.x +
                        " " +
                        endPoint.y);
                    } else {
                        line = process.getGraphics().path("M" + startPoint.x + " " + startPoint.y + "L" + endPoint.x + " " + endPoint.y + "");
                    }
                }
            }
            line.attr({
                "stroke-width": 2,
                stroke: "#444",
                "arrow-end": "classic-wide-long"
            });
        };
        
        this.updateCtrl = function(hd, ce){
            hDirection = hd;
            ctrlExten = ce;
        };
        
        this.setPosition = function(px, py){
            x = px;
            y = py;
        };
        
        this.getFrom = function(){
            return from;
        };
        
        this.getTo = function(){
            return to;
        };
        
        this.isControl = function(){
            return this.lineType == "Control";
        };
        
        this.log = function(){
//            console.log(hDirection + ":" + ctrlExten);
        };
    };
    //----------Transition--------end-------
    
    zflow.Process = function(){
        var graphics;
        var activities;
        var transitions;
        
        //辅助坐标逻辑计算和绘制数据结构
		var parallelPair;
        var branch;
        var activityMap;//---reset中重置
        var blankBranchTransitions;//空分支线条列表,需要计算空分支线条的绘制坐标点---reset中重置
        //重置画图需要的辅助信息
        var reset = function(){
            blankBranchTransitions = [];
			activityMap = {};
            
            var parallels = [];
            for (var i = 0; i < activities.length; i++) {
                activities[i].index = i;//重置每个节点的索引
                activityMap[activities[i].id] = activities[i];
                activities[i].toTransitions = [];
            }
            
            for (var i = 0; i < transitions.length; i++) {
                if (activityMap[transitions[i].from].isParallel() && activityMap[transitions[i].to].isRelation()) {
                    blankBranchTransitions.push(transitions[i]);//插入头部
                }
                activityMap[transitions[i].from].toTransitions.push(transitions[i]);
            }
        };
        
        this.init = function(flowXML, paper){
            var flowXML = $.parseXML(flowXML);
            var process = this;
            graphics = paper;
            
            //初始化变量
            activities = new Array();
            transitions = new Array();
			parallelPair = {};
			
            var initActivities = function(activityNodes, branchIndex){
                for (var i = 0; i < activityNodes.length; i++) {
                    if (activityNodes[i].nodeName == "Activity") {
						var numOfBranch = 0;
						var index = i;
						if (activityNodes[i + 1] && activityNodes[i + 1].nodeName == "Parallel") {
							index = i + 1;
							var branches = $(activityNodes[index]).children();
							numOfBranch = branches.length;
							
							var activity = new zflow.Activity(process, activityNodes[i], branchIndex, numOfBranch);
							activity.drawType="Parallel";
							activities.push(activity);
							
							for (var j = 0; j < branches.length; j++) {
								var actNodes = $(branches[j]).children();
								initActivities(actNodes, j);
							}
							parallelPair[activity.id] = activities.length;
						} else {
							activity = new zflow.Activity(process, activityNodes[i], branchIndex, numOfBranch);
							activities.push(activity);
						}
//						if(activityNodes[i + 1].state && activityNodes[i + 1].nodeName == "Parallel")
						if(activity.type == 'Tache' && activity.state != null) {
							instActivities.push(activity);
						}
						i = index;
					}
                }
                $.zflow.instActivities = instActivities;
            };
			
            //初始化：activities
            initActivities($(flowXML).find("Activities").children(), 0);
			
			for(var key in parallelPair){
				if(activities[parallelPair[key]]){
					activities[parallelPair[key]].drawType="Relation";
				}
			}
            
            //初始化：transitions
            $(flowXML).find("Transition").each(function(){
                var transition = new zflow.Transition(process, this);
                transitions.push(transition);
            });
        };
        
        this.paint = function(){
            reset();
            branch = new zflow.Branch();
            branch.init(this, 0, activities.length);
            branch.updateSize();
            if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                branch.updatePosition(zflow.config.offsetX, branch.getHeight() / 2 + zflow.config.offsetY);
            } else {
                branch.updatePosition(branch.getWidth() / 2 + zflow.config.offsetX, zflow.config.offsetY);
            }
			branch.log();
            
            graphics.setSize(branch.getWidth() + zflow.config.offsetX * 2, branch.getHeight() + zflow.config.offsetY * 2);
            
            for (var i = 0; i < activities.length; i++) {
                activities[i].paint();
//                activities[i].log();
            }
            
            for (var i = 0; i < transitions.length; i++) {
                if (zflow.state.direction == zflow.constant.DIRECTION.H) {
                    transitions[i].paintForH(activityMap);
                } else {
                    transitions[i].paintForV(activityMap);
                }
                
                //			transitions[i].log();
            }
        };
        this.getGraphics = function(){
            return graphics;
        };
        this.getParallelPair = function(){
            return parallelPair;
        };
        
        this.getRelationPair = function(){
            return relationPair;
        };
        
        this.getActivityMap = function(){
            return activityMap;
        };
        
        this.getBlankBranchTransitions = function(){
            return blankBranchTransitions;
        };
        
        this.getActivities = function(){
            return activities;
        };
    }
    
    zflow.init = function(ele, flowXML){
        //初始化展示实例画板
        var paper = Raphael($(ele)[0].id);
        var process = new zflow.Process();
        process.init(flowXML, paper);
        process.paint();
    };
    zflow.initState = function(ele, stateConfig){
        $.extend(zflow.state.stateConfig, stateConfig);
        var paperState = Raphael($(ele)[0].id, "100%", "100%");
        var leftOffset = 30;
        var topOffset = 10;
        var rectSize = 17;
        var fontSize = 15;
        paperState.text(leftOffset, topOffset + rectSize / 2, "图例：").attr({
            "font-size": fontSize
        });
        var running = new Array();
        var curLeftOffset = leftOffset + 30;
        for (var key in zflow.state.stateConfig) {
            var r = paperState.rect(curLeftOffset, topOffset, rectSize, rectSize).attr({
                fill: zflow.state.stateConfig[key][1],
                stroke: "#ffffff"
            });
            curLeftOffset = curLeftOffset + rectSize + zflow.state.stateConfig[key][0].length * fontSize / 2 + 10;
            paperState.text(curLeftOffset, topOffset + rectSize / 2, zflow.state.stateConfig[key][0]).attr({
                "font-size": fontSize
            });
            curLeftOffset = curLeftOffset + zflow.state.stateConfig[key][0].length * fontSize / 2 + 20;
            if (zflow.state.stateConfig[key][2]) {
                running.push(r);
            }
        }
        for (var key in running) {
            //闪烁效果
            setInterval(function(){
                running[key].animate({
                    "fill-opacity": .2
                }, 800, running[key].attr({
                    "fill-opacity": 1
                }))
            }, 800);
        }
    };
    
    zflow.initStateImage = function(ele, stateConfig){
        $.extend(zflow.state.stateConfig, stateConfig);
        var paperState = Raphael($(ele)[0].id, "100%", "100%");
        var leftOffset = 30;
        var topOffset = 10;
        var rectSize = 17;
        var fontSize = 15;
        paperState.text(leftOffset, topOffset + rectSize / 2, "图例：").attr({
            "font-size": fontSize
        });
        var running = new Array();
        var curLeftOffset = leftOffset + 30;
        for (var key in zflow.state.stateConfig) {
            var r = paperState.image(webBasePATH + "common/images/flow/" + zflow.state.stateConfig[key][3], curLeftOffset, topOffset, rectSize, rectSize);
            
            curLeftOffset = curLeftOffset + rectSize + zflow.state.stateConfig[key][0].length * fontSize / 2 + 10;
            paperState.text(curLeftOffset, topOffset + rectSize / 2, zflow.state.stateConfig[key][0]).attr({
                "font-size": fontSize
            });
            curLeftOffset = curLeftOffset + zflow.state.stateConfig[key][0].length * fontSize / 2 + 20;
            if (zflow.state.stateConfig[key][2]) {
                running.push(r);
            }
        }
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
