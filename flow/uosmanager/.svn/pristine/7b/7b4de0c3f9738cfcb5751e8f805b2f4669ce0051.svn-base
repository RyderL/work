var Activity = function(flow, workArea, xml, paper, obj){
    //布局变量对象引用
    var workArea = workArea;
    var flow = flow;
    var activity = this;
    var paper = paper;
    var node = new Node(workArea, flow, this);
    
    //画图相关变量-----重绘需要清空
    this.toTransitions = [];//控制结构对应的控制线条
    this.fromTransitions = [];//控制结构对应的控制线条
    this.maxCtrlLeft = 0;//控制结构或者控制结构连接点的最大左延展
    this.maxCtrlRight = 0;//控制结构或者控制结构连接点的最大右延展
    this.index = -1;//在节点中的位置
    //xpdl里面相关信息变量
    this.description = "";
    this.limit = "";
    this.performer = "";
    this.startMode = "Automatic";
    this.finishMode = "Automatic";
    this.priority = "";
    this.transitionRestrictions = [];
    this.application = {};
    
    if (xml) {
        this.id = $(xml).attr("Id");
        this.name = $(xml).attr("Name");
        
        this.branchIndex = workArea.util.getExtendedValue(xml, "branchIndex");
        this.nodeIndex = parseInt(workArea.util.getExtendedValue(xml, "nodeIndex"));
        this.nodeType = workArea.util.getExtendedValue(xml, "nodeType");
        this.numOfBranch = workArea.util.getExtendedValue(xml, "numOfBranch");
        this.exTacheId = workArea.util.getExtendedValue(xml, "ExTacheId");
        this.exTacheCode = workArea.util.getExtendedValue(xml, "ExTacheCode");
        this.exTacheName = workArea.util.getExtendedValue(xml, "ExTacheName");
        this.exOperType = workArea.util.getExtendedValue(xml, "ExOperType");
        this.exWithdraw = workArea.util.getExtendedValue(xml, "ExWithdraw");
        this.exChange = workArea.util.getExtendedValue(xml, "ExChange");
        this.application.id = $(xml).find("Implementation Tool").attr("Id");
        if (!this.application.id) {
            this.application.id = workArea.util.getGid();
        }
        this.exceptionConfigs = [];
        if (this.nodeType == 'Exception') {
            var exExceptionConfigNodes = workArea.util.getExtendedNodes(xml, 'ExExceptionConfig');
            for (var i = 0; i < exExceptionConfigNodes.length; i++) {
                var exceptionConfig = {};
                exceptionConfig.startActivityId = $(exExceptionConfigNodes[i]).attr("ExStartActivityId");
                exceptionConfig.startActivityName = $(exExceptionConfigNodes[i]).attr("ExStartActivityName");
                exceptionConfig.endActivityId = $(exExceptionConfigNodes[i]).attr("ExEndActivityId");
                exceptionConfig.endActivityName = $(exExceptionConfigNodes[i]).attr("ExEndActivityName");
                exceptionConfig.reasonCatalogId = $(exExceptionConfigNodes[i]).attr("ExReasonCatalogId");//异常原因类型编码
                exceptionConfig.reasonCatalogName = $(exExceptionConfigNodes[i]).attr("ExReasonCatalogName");//异常原因类型
                exceptionConfig.returnReasonId = $(exExceptionConfigNodes[i]).attr("ExReturnReasonId");
                exceptionConfig.returnReasonName = $(exExceptionConfigNodes[i]).attr("ExReturnReasonName");
                exceptionConfig.autoToManual = $(exExceptionConfigNodes[i]).attr("ExAutoToManual");
                exceptionConfig.startMode = $(exExceptionConfigNodes[i]).attr("ExStartMode");
                this.exceptionConfigs.push(exceptionConfig);
            }
        }
    } else if (obj) {
        this.id = obj.id;
        this.name = obj.name;
        
        this.branchIndex = obj.branchIndex;
        this.nodeIndex = parseInt(obj.nodeIndex);
        this.nodeType = obj.nodeType;
        this.numOfBranch = obj.numOfBranch;
        this.exTacheId = obj.tacheId;
        this.exTacheCode = obj.tacheCode;
        this.exTacheName = obj.tacheName;
        this.exOperType = "1";
        this.exWithdraw = "true";
        this.exChange = "true";
        this.exceptionConfigs = [];
        this.application.id = workArea.util.getGid();
    }
    
    //辅助参数
    this.parallel = null;//结构：{start:1,end:1,vSize:1,hSize:1,branches:[{hSize:1,vSize:1,activities:[]}]};
    this.parallelActivity = null;//Relation节点对应的parallel节点
    this.parentParallel = null;//记录父并行结构
    this.circle = null;
    this.x = 0;
    this.y = 0;
    
    this.getNextY = function(){
        //根据parallel和y来确定下一个y的值
        if (!this.parallel) {
            return this.y + node.getHeight();
        } else {
            return this.y + node.getHeight() + this.parallel.vSize;
        }
    };
    this.getHeight = function(){
        if (!this.parallel) {
            return node.getHeight();
        } else {
            return node.getHeight() + this.parallel.vSize;
        }
    };
    this.getWidth = function(){
        if (!this.parallel) {
            return node.getWidth();
        } else {
            return this.parallel.hSize;
        }
    };
    this.getHSize = function(){
        var ctrl = this.maxCtrlLeft <= this.maxCtrlRight ? this.maxCtrlRight : this.maxCtrlLeft;
        if (ctrl != 0) {
            return ctrl * 2;
        }
        return this.getWidth();
    };
    this.getLeftSize = function(){
        if (this.maxCtrlLeft) {
            return this.maxCtrlLeft;
        }
        if (!this.parallel) {
            return node.getWidth() / 2;
        } else {
            return this.parallel.hSize / 2;
        }
    };
    this.getRightSize = function(){
        if (this.maxCtrlRight) {
            return this.maxCtrlRight;
        }
        if (!this.parallel) {
            return node.getWidth() / 2;
        } else {
            return this.parallel.hSize / 2;
        }
    };
    //空线条：并行节点到合并节点的线条 绘制辅助方法（是否可以优化？）----start------------
    var blankIndex = -1;
    this.getBlankTransitionX = function(hSize){
        for (var i = blankIndex + 1; i < this.parallel.branches.length; i++) {
            if (this.parallel.branches[i].activities.length == 0) {
                break;
            }
        }
        if (i >= this.parallel.branches.length) {
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
            x: (this.x + workArea.config.offsetX + hSize / 2) + workArea.config.nodeWidth + countOffsetX - this.parallel.hSize / 2,
            branchIndex: blankIndex
        };
    };
    //空线条：并行节点到合并节点的线条 绘制辅助方法----end------------
    //节点插入位置辅助方法：辅助方法（是否可以优化？）----start------------
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
    //节点插入位置辅助方法：辅助方法（是否可以优化？）----end------------
    
    this.getFrontTache = function(activityMap, activities){
        var acts = [];
        switch (this.nodeType) {
            case 'Tache':
                acts.push({
                    label: this.name,
                    value: this.id
                });
                for (var i = 0; i < this.fromTransitions.length; i++) {
                    if (this.fromTransitions[i].lineType == 'Control') {
                        continue;
                    }
                    var as = activityMap[this.fromTransitions[i].from].getFrontTache(activityMap, activities);
                    for (var j = 0; j < as.length; j++) {
                        acts.push(as[j]);
                    }
                }
                break;
            case 'Relation':
                var palAct = this.parallelActivity;
                var pal = palAct.parallel;
                var start = pal.start;
                var end = pal.end;
                for (var i = start; i <= end; i++) {
                    if (activities[i].nodeType == 'Tache') {
                        acts.push(activities[i]);
                    }
                }
                for (var i = 0; i < palAct.fromTransitions.length; i++) {
                    if (this.fromTransitions[i].lineType == 'Control') {
                        continue;
                    }
                    var as = activityMap[palAct.fromTransitions[i].from].getFrontTache(activityMap, activities);
                    for (var j = 0; j < as.length; j++) {
                        acts.push(as[j]);
                    }
                }
                break;
            case 'Start':
                return [{
                    label: this.name,
                    value: this.id
                }];
            case 'Control':
            case 'Parallel':
                for (var i = 0; i < this.fromTransitions.length; i++) {
                    if (this.fromTransitions[i].lineType == 'Control') {
                        continue;
                    }
                    var as = activityMap[this.fromTransitions[i].from].getFrontTache(activityMap, activities);
                    for (var j = 0; j < as.length; j++) {
                        acts.push(as[j]);
                    }
                }
            default:
                break;
        }
        
        return acts;
    };
    
    //初始化并行结构
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
                    b_hSize = ((b_hSize == 0) ? workArea.config.nodeWidth : b_hSize);
                    hSize += b_hSize
                    if (vSize < b_vSize) {
                        vSize = b_vSize;
                    }
                    vSize = (vSize == 0 ? node.getHeight() : vSize);
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
                this.parallel.branches[activities[i].branchIndex].activities.push(activities[i]);
            }
        }
    };
    
    //初始化控制结构
    this.initControl = function(){
        for (var i = 0; i < this.parallel.branches.length; i++) {
            var branch = this.parallel.branches[i];
            for (var j = 0; j < branch.activities.length; j++) {
                var activity = branch.activities[j];
                if (activity.nodeType == "Parallel") {
                    activity.initControl();
                }
            }
        }
        for (var i = 0; i < this.parallel.branches.length; i++) {
            var branch = this.parallel.branches[i];
            for (var j = 0; j < branch.activities.length; j++) {
                flow.updateCtrl(branch.activities[j]);
            }
        }
    }
    
    //更新节点的坐标值
    this.updateData = function(x, y, i){
        this.x = x, this.y = y;
        if (!this.parallel) {
            return i;
        } else {
            var xOffset = 0;
            for (var m = 0; m < this.parallel.branches.length; m++) {
                for (var n = 0; n < this.parallel.branches[m].activities.length; n++) {
                    this.parallel.branches[m].activities[n].updateData(this.x + xOffset + this.parallel.branches[m].hSize / 2 - this.parallel.hSize / 2, n == 0 ? this.y + node.getHeight() : this.parallel.branches[m].activities[n - 1].getNextY());
                }
                xOffset += this.parallel.branches[m].hSize;
            }
            return (this.parallel.end - 1);
        }
    };
    
    //更新并行结构的hSize
    this.updateParallelHSize = function(){
        for (var m = 0; m < this.parallel.branches.length; m++) {
            for (var n = 0; n < this.parallel.branches[m].activities.length; n++) {
                var activity = this.parallel.branches[m].activities[n];
                if (activity.nodeType == 'Parallel') {
                    activity.updateParallelHSize();
                }
            }
        }
        for (var i = 0; i < this.parallel.branches.length; i++) {
            var branch = this.parallel.branches[i];
            for (var j = 0; j < branch.activities.length; j++) {
                flow.updateCtrl(branch.activities[j]);
            }
        }
        for (var m = 0; m < this.parallel.branches.length; m++) {
            var max = 0;
            for (var n = 0; n < this.parallel.branches[m].activities.length; n++) {
                var activity = this.parallel.branches[m].activities[n];
                var right = activity.maxCtrlRight;
                var left = activity.maxCtrlLeft;
                if (max < right) {
                    max = right;
                }
                if (max < left) {
                    max = left;
                }
				if(activity.nodeType=="Parallel"){
					if(2*max<activity.parallel.hSize){
						max = activity.parallel.hSize/2;
					}
				}
            }
            if (max * 2 > this.parallel.branches[m].hSize) {
                this.parallel.hSize = this.parallel.hSize - this.parallel.branches[m].hSize + max * 2;
                this.parallel.branches[m].hSize = max * 2;
            }
        }
    };
    
    //查找控制节点可达的Activity
    this.findCtrlToActivies = function(ctrlAct){
        if (!this.parallel) {
            return null;
        }
        for (var m = 0; m < this.parallel.branches.length; m++) {
            for (var n = 0; n < this.parallel.branches[m].activities.length; n++) {
                if (this.parallel.branches[m].activities[n].id == ctrlAct.id) {
                    return this.parallel.branches[m].activities;
                }
                var ret = this.parallel.branches[m].activities[n].findCtrlToActivies(ctrlAct);
                if (ret) {
                    return ret;
                }
            }
        }
    };
    
    //绘制方法
    this.paint = function(hSize, index, ctrlFlag){
        if (this.nodeType == 'Exception') {
            return;
        }
        this.index = index;
        var x = this.x + workArea.config.offsetX + hSize / 2;
        var y = this.y + workArea.config.offsetY;
        if (node) {
            node.reset(paper, x, y, ctrlFlag);
        }
        if (this.parallel && flow.enableEdit) {
            this.circle = paper.circle(x + workArea.config.nodeWidth, y + workArea.config.space / 2 + 10, 5).attr({
                "fill": "#fff",
                cursor: "pointer"
            });
            this.circle.click(function(evt){
                flow.addBranch(activity);
            });
        }
    };
    
    //工具方法：获取node配置信息，掺入参数为key
    this.getNodeConfig = function(key){
        return node.getNodeConfig(key);
    };
    
    //============四个连线点====start======
    this.getTopPoint = function(){
        return node.getTopPoint();
    };
    this.getLeftPoint = function(){
        return node.getLeftPoint();
    };
    this.getRightPoint = function(){
        return node.getRightPoint();
    };
    this.getBottomPoint = function(){
        return node.getBottomPoint();
    };
    //============四个连线点====end======
    
    this.generateStartOrFinish = function(){
        var xpdl = "<Activity Id='" + this.id + "' Name='" + this.name + "'>";
        //加上描述
        if (this.description) {
            xpdl += "<Description>" + this.description + "</Description>";
        } else {
            xpdl += "<Description />"
        }
        //限制
        if (this.limit) {
            xpdl += "<Limit>" + this.limit + "</Limit>";
        } else {
            xpdl += "<Limit />";
        }
        //执行者
        if (this.performer) {
            xpdl += "<Performer>" + this.performer + "</Performer>";
        } else {
            xpdl += "<Performer />";
        }
        //开始模式
        xpdl += "<StartMode>" + this.startMode + "</StartMode>";
        //结束模式
        xpdl += "<FinishMode>" + this.finishMode + "</FinishMode>";
        //优先级
        if (this.priority) {
            xpdl += "<Priority>" + this.priority + "</Priority>";
        } else {
            xpdl += "<Priority />"
        }
        xpdl += "<Implementation><No/></Implementation>";
        //扩展属性
        var extendedAttributes = [];
        extendedAttributes.push({
            name: "branchIndex",
            value: this.branchIndex
        });
        extendedAttributes.push({
            name: "nodeIndex",
            value: this.nodeIndex
        });
        extendedAttributes.push({
            name: "nodeType",
            value: this.nodeType
        });
        xpdl += "<xpdl:ExtendedAttributes>";
        for (var i = 0; i < extendedAttributes.length; i++) {
            xpdl += "<xpdl:ExtendedAttribute Name='" + extendedAttributes[i].name +
            "' Value='" +
            extendedAttributes[i].value +
            "' />";
        }
        xpdl += "</xpdl:ExtendedAttributes>";
        xpdl += "</Activity>";
        return xpdl;
    };
    
    this.generateParallel = function(){
        //并行节点首先要生成一个路由节点
        var xpdl = "<Activity Id='" + this.id + "' Name='" + this.name + "'>";
        xpdl += "<Route />";
        //流入流出方式
        if (this.transitionRestrictions.length > 0) {
            xpdl += "<TransitionRestrictions>";
            for (var i = 0; i < this.transitionRestrictions.length; i++) {
                xpdl += "<TransitionRestriction>" +
                "<Join Type='" +
                this.transitionRestrictions[i].joinType +
                "'/><Split Type='" +
                this.transitionRestrictions[i].splitType +
                "'/></TransitionRestriction>";
            }
            xpdl += "</TransitionRestrictions>";
        } else {
            xpdl += "<TransitionRestrictions>";
            xpdl += "<TransitionRestriction>" +
            "<Join Type='AND'/><Split Type='AND'/></TransitionRestriction>";
            xpdl += "</TransitionRestrictions>";
        }
        
        //加上描述
        if (this.description) {
            xpdl += "<Description>" + this.description + "</Description>";
        } else {
            xpdl += "<Description />"
        }
        //扩展属性
        xpdl += "<xpdl:ExtendedAttributes>";
        //加上本节点所处的分支序号和节点序号
        var extendedAttributes = [];
        extendedAttributes.push({
            name: "branchIndex",
            value: this.branchIndex
        });
        extendedAttributes.push({
            name: "nodeIndex",
            value: this.nodeIndex
        });
        extendedAttributes.push({
            name: "nodeType",
            value: this.nodeType
        });
        extendedAttributes.push({
            name: "numOfBranch",
            value: this.numOfBranch
        });
        for (var i = 0; i < extendedAttributes.length; i++) {
            xpdl += "<xpdl:ExtendedAttribute Name='" + extendedAttributes[i].name +
            "' Value='" +
            extendedAttributes[i].value +
            "' />";
        }
        xpdl += "</xpdl:ExtendedAttributes>";
        xpdl += "</Activity>";
        
        return xpdl;
    };
    
    this.generateRelation = function(){
        //最后要生成并行节点最后的条件合并节点，这个也作为路由节点处理
        var xpdl = "<Activity Id='" + this.id + "' Name='" + this.name + "'>";
        xpdl += "<Route />";
        //流入流出方式
        if (this.transitionRestrictions.length > 0) {
            xpdl += "<TransitionRestrictions>";
            for (var i = 0; i < this.transitionRestrictions.length; i++) {
                xpdl += "<TransitionRestriction>" +
                "<Join Type='" +
                this.transitionRestrictions[i].joinType +
                "'/><Split Type='" +
                this.transitionRestrictions[i].splitType +
                "'/></TransitionRestriction>";
            }
            xpdl += "</TransitionRestrictions>";
        } else {
            xpdl += "<TransitionRestrictions>";
            xpdl += "<TransitionRestriction>" +
            "<Join Type='AND'/><Split Type='AND'/></TransitionRestriction>";
            xpdl += "</TransitionRestrictions>";
        }
        //加上描述
        if (this.description) {
            xpdl += "<Description>" + this.description + "</Description>";
        } else {
            xpdl += "<Description />"
        }
        //扩展属性
        xpdl += "<xpdl:ExtendedAttributes>";
        //其他扩展属性
        var extendedAttributes = [];
        extendedAttributes.push({
            name: "nodeType",
            value: this.nodeType
        });
        for (var i = 0; i < extendedAttributes.length; i++) {
            xpdl += "<xpdl:ExtendedAttribute Name='" + extendedAttributes[i].name +
            "' Value='" +
            extendedAttributes[i].value +
            "' />";
        }
        xpdl += "</xpdl:ExtendedAttributes>";
        xpdl += "</Activity>";
        return xpdl;
    };
    
    this.generateTache = function(){
        var xpdl = "<Activity Id='" + this.id + "' Name='" + workArea.util.changeSign(this.name) + "'>";
        //加上描述
        if (this.description) {
            xpdl += "<Description>" + this.description + "</Description>";
        } else {
            xpdl += "<Description />"
        }
        //限制
        if (this.limit) {
            xpdl += "<Limit>" + this.limit + "</Limit>";
        } else {
            xpdl += "<Limit />";
        }
        //执行者
        if (this.performer) {
            xpdl += "<Performer>" + this.performer + "</Performer>";
        } else {
            xpdl += "<Performer />";
        }
        //开始模式
        xpdl += "<StartMode>" + this.startMode + "</StartMode>";
        //结束模式
        xpdl += "<FinishMode>" + this.finishMode + "</FinishMode>";
        //流入流出方式
        if (this.transitionRestrictions.length > 0) {
            xpdl += "<TransitionRestrictions>";
            for (var i = 0; i < this.transitionRestrictions.length; i++) {
                xpdl += "<TransitionRestriction>" +
                "<Join Type='" +
                this.transitionRestrictions[i].joinType +
                "'/><Split Type='" +
                this.transitionRestrictions[i].splitType +
                "'/></TransitionRestriction>";
            }
            xpdl += "</TransitionRestrictions>";
        } else {
            xpdl += "<TransitionRestrictions>";
            xpdl += "<TransitionRestriction>" +
            "<Join Type='AND'/><Split Type='AND'/></TransitionRestriction>";
            xpdl += "</TransitionRestrictions>";
        }
        //优先级
        if (this.priority) {
            xpdl += "<Priority>" + this.priority + "</Priority>";
        } else {
            xpdl += "<Priority />"
        }
        if (this.application.id) {
            xpdl += "<Implementation>";
            xpdl += "<Tool Id='" + this.application.id + "' Type='Application'>";
            xpdl += "</Tool></Implementation>";
        }
        
        //扩展属性
        var extendedAttributes = [];
        extendedAttributes.push({
            name: "branchIndex",
            value: this.branchIndex
        });
        extendedAttributes.push({
            name: "nodeIndex",
            value: this.nodeIndex
        });
        extendedAttributes.push({
            name: "nodeType",
            value: this.nodeType
        });
        extendedAttributes.push({
            name: "ExTacheId",
            value: this.exTacheId
        });
        extendedAttributes.push({
            name: "ExTacheCode",
            value: this.exTacheCode
        });
        extendedAttributes.push({
            name: "ExTacheName",
            value: workArea.util.changeSign(this.exTacheName)
        });
        extendedAttributes.push({
            name: "ExOperType",
            value: this.exOperType
        });
        extendedAttributes.push({
            name: "ExWithdraw",
            value: this.exWithdraw
        });
        extendedAttributes.push({
            name: "ExChange",
            value: this.exChange
        });
        xpdl += "<xpdl:ExtendedAttributes>";
        for (var i = 0; i < extendedAttributes.length; i++) {
            xpdl += "<xpdl:ExtendedAttribute Name='" + extendedAttributes[i].name +
            "' Value='" +
            extendedAttributes[i].value +
            "' />";
        }
        xpdl += "</xpdl:ExtendedAttributes>";
        xpdl += "</Activity>";
        return xpdl;
    };
    
    this.generateException = function(){
        var xpdl = "<Activity Id='" + this.id + "' Name='" + this.name + "'>";
        //加上描述
        if (this.description) {
            xpdl += "<Description>" + this.description + "</Description>";
        } else {
            xpdl += "<Description />"
        }
        //限制
        if (this.limit) {
            xpdl += "<Limit>" + this.limit + "</Limit>";
        } else {
            xpdl += "<Limit />";
        }
        //执行者
        if (this.performer) {
            xpdl += "<Performer>" + this.performer + "</Performer>";
        } else {
            xpdl += "<Performer />";
        }
        //开始模式
        xpdl += "<StartMode>" + this.startMode + "</StartMode>";
        //结束模式
        xpdl += "<FinishMode>" + this.finishMode + "</FinishMode>";
        //优先级
        if (this.priority) {
            xpdl += "<Priority>" + this.priority + "</Priority>";
        } else {
            xpdl += "<Priority />"
        }
        xpdl += "<Implementation><No/></Implementation>";
        //扩展属性
        var extendedAttributes = [];
        extendedAttributes.push({
            name: "branchIndex",
            value: this.branchIndex
        });
        extendedAttributes.push({
            name: "nodeIndex",
            value: this.nodeIndex
        });
        extendedAttributes.push({
            name: "nodeType",
            value: this.nodeType
        });
        extendedAttributes.push({
            name: "ExExceptionConfigs",
            value: this.exceptionConfigs
        });
        xpdl += "<xpdl:ExtendedAttributes>";
        for (var i = 0; i < extendedAttributes.length; i++) {
            //异常配置
            if (extendedAttributes[i].name == "ExExceptionConfigs") {
                xpdl += "<xpdl:ExtendedAttribute Name='" + extendedAttributes[i].name + "'>";
                xpdl += "<xpdl:ExExceptionConfigs>";
                
                var configs = extendedAttributes[i].value;
                for (var j = 0; j < configs.length; j++) {
                    xpdl += "<xpdl:ExExceptionConfig ExStartActivityId='" + configs[j].startActivityId +
                    "' ExStartActivityName='" +
                    configs[j].startActivityName +
                    "' ExEndActivityId='" +
                    configs[j].endActivityId +
                    "' ExEndActivityName='" +
                    configs[j].endActivityName +
                    "' ExReasonCatalogId='" +
                    configs[j].reasonCatalogId +
                    "' ExReasonCatalogName='" +
                    configs[j].reasonCatalogName +
                    "' ExReturnReasonId='" +
                    configs[j].returnReasonId +
                    "' ExReturnReasonName='" +
                    configs[j].returnReasonName +
                    "' ExAutoToManual='" +
                    configs[j].autoToManual +
                    "' ExStartMode='" +
                    configs[j].startMode +
                    "' />";
                }
                xpdl += "</xpdl:ExExceptionConfigs>";
                xpdl += "</xpdl:ExtendedAttribute>";
                
            } else {
                xpdl += "<xpdl:ExtendedAttribute Name='" + extendedAttributes[i].name +
                "' Value='" +
                extendedAttributes[i].value +
                "' />";
            }
        }
        xpdl += "</xpdl:ExtendedAttributes>";
        xpdl += "</Activity>";
        return xpdl;
    };
    
    this.generateControl = function(){
        var xpdl = "<Activity Id='" + this.id + "' Name='" + this.name + "'>";
        xpdl += "<Route />";
        //流入流出方式
        this.transitionRestrictions = [];
        if (this.transitionRestrictions.length > 0) {
            xpdl += "<TransitionRestrictions>";
            for (var i = 0; i < this.transitionRestrictions.length; i++) {
                xpdl += "<TransitionRestriction>" +
                "<Join Type='" +
                this.transitionRestrictions[i].joinType +
                "'/><Split Type='" +
                this.transitionRestrictions[i].splitType +
                "'/></TransitionRestriction>";
            }
            xpdl += "</TransitionRestrictions>";
        } else {
            xpdl += "<TransitionRestrictions>";
            xpdl += "<TransitionRestriction>" +
            "<Join Type='AND'/><Split Type='AND'/></TransitionRestriction>";
            xpdl += "</TransitionRestrictions>";
        }
        //加上描述
        if (this.description) {
            xpdl += "<Description>" + this.description + "</Description>";
        } else {
            xpdl += "<Description />";
        }
        //扩展属性
        var extendedAttributes = [];
        extendedAttributes.push({
            name: "branchIndex",
            value: this.branchIndex
        });
        extendedAttributes.push({
            name: "nodeIndex",
            value: this.nodeIndex
        });
        extendedAttributes.push({
            name: "nodeType",
            value: this.nodeType
        });
        xpdl += "<xpdl:ExtendedAttributes>";
        for (var i = 0; i < extendedAttributes.length; i++) {
            xpdl += "<xpdl:ExtendedAttribute Name='" + extendedAttributes[i].name +
            "' Value='" +
            extendedAttributes[i].value +
            "' />";
        }
        xpdl += "</xpdl:ExtendedAttributes>";
        
        xpdl += "</Activity>";
        return xpdl;
    };
    
    this.generateActivityXPDL = function(){
        switch (this.nodeType) {
            case "Start":
            case "Finish":
                return this.generateStartOrFinish();
            case "Parallel":
                return this.generateParallel();
            case "Relation":
                return this.generateRelation();
            case "Tache":
                return this.generateTache();
            case "Exception":
                return this.generateException();
            case "Control":
                return this.generateControl();
            default:
                break;
        }
    }
}
