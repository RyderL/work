var Flow = function(paper, flowXML, workArea){
    //布局变量对象引用
    var flowXML = $.parseXML(flowXML);
    var workArea = workArea;
    var paper = paper;
    var flow = this;
    
    this.isDirty = false;
    
    //节点列表
    var activities = new Array();
    var activityMap = {};
    //线条列表
    var transitions = new Array();
    var transitionMap = {};
    
    //界面展示相关的变量-----重绘需要清空
    this.title = null;//流程定义图的标题
    this.enableEdit = false;//是否可编辑状态
    this.showAddFlag = false;//流程图是否展示  插入点
    this.addType = null;//插入的类型：Parallel Tache Control
    this.node = null;//插入的 节点信息：不是Tache的情况下为null
    this.showControl = false;//插入控制节点后和点击控制节点后，需要展示控制节点的可连接位置
    this.ctrlActivity = null;//插入控制节点后和点击控制节点后，控制节点的Activity对象
    //----这些根据表里面记录来生成，在generateXPDL里面---start----
    this.name = "";
    this.processState = "";
    this.editDate = "";
    this.validFromDate = "";
    this.validToDate = "";
    this.description = "";
    this.editUser = "";
    this.version = "";
    this.extendedAttributes = [];
    this.state = "";
    this.type = "";
    //----这些根据表里面记录来生成，在generateXPDL里面---end----
    
    //如果flowXML为空，表示要新建流程定义。
    if (!flowXML) {
        this.id = workArea.util.getGid();
        //开始节点
        var startNode = new Activity(flow, workArea, null, paper, {
            id: workArea.util.getGid(),
            name: "开始节点",
            branchIndex: 0,
            nodeIndex: 0,
            nodeType: "Start"
        });
        activities.push(startNode);
        //结束节点
        var finishNode = new Activity(flow, workArea, null, paper, {
            id: workArea.util.getGid(),
            name: "结束节点",
            branchIndex: 0,
            nodeIndex: 1,
            nodeType: "Finish"
        });
        activities.push(finishNode);
        //异常节点
        var exceptionNode = new Activity(flow, workArea, null, paper, {
            id: workArea.util.getGid(),
            name: "异常节点",
            branchIndex: 0,
            nodeIndex: 2,
            nodeType: "Exception"
        });
        activities.push(exceptionNode);
        
        //增加线条
        var tran = new Transition(flow, workArea, null, paper, {
            id: workArea.util.getGid(),
            name: "",
            from: startNode.id,
            to: finishNode.id,
            lineType: "Normal",
            parentId: 0
        });
        transitions.push(tran);
    }    //如果flowXML不为空，解析流程定义。
    else {
        //xpdl报文相关的
        this.id = $(flowXML).find("Package").attr("Id");
        
        //解析节点
        var activityNodes = $(flowXML).find("Activities").children();
        for (var i = 0; i < activityNodes.length; i++) {
            activities.push(new Activity(flow, workArea, activityNodes[i], paper));
        }
        
        //解析线条
        $(flowXML).find("Transition").each(function(){
            var transition = new Transition(flow, workArea, this, paper);
            transitions.push(transition);
        });
    };
    
    this.getExceptionStartTache = function(){
        var ret = [];
        for (var i = 0; i < activities.length; i++) {
            if (activities[i].nodeType == 'Tache') {
                var obj = {};
                obj.label = activities[i].name;
                obj.value = activities[i].id;
                ret.push(obj);
            }
        }
        return ret;
    };
    this.getExceptionReturnTache = function(id){
        var startActivity = activityMap[id];
        if (!startActivity) {
            return [];
        }
        var arr = [];
        for (var i = 0; i < startActivity.index; i++) {
            if (activities[i].id == id) {
                break;
            } else {
                if (activities[i].nodeType == "Tache" || activities[i].nodeType == "Start") {
                    if (this.canReach(activities[i], startActivity)) {
                        arr.push({
                            label: activities[i].name,
                            value: activities[i].id
                        });
                    }
                }
            }
        }
        //var arr = startActivity.getFrontTache(activityMap,activities);
        //arr.splice(0,1);
        return arr;
    };
    this.canReach = function(fromActivity, toActivity){
        if (fromActivity.id == toActivity.id) {
            return true;
        }
        var toTransitions = fromActivity.toTransitions;
        if (toTransitions==null || toTransitions.length == 0) {
            return false;
        }
        for (var i = 0; i < toTransitions.length; i++) {
        	if(toTransitions[i].lineType=="Control"
        		&&parseInt(activityMap[toTransitions[i].to].nodeIndex)<parseInt(activityMap[toTransitions[i].from].nodeIndex)){
        		continue;
        	}
            var bol = this.canReach(activityMap[toTransitions[i].to], toActivity);
            if (bol) {
                return true;
            }
        }
        return false;
    };
    this.getTacheId = function(id){
        return activityMap[id].exTacheId;
    };
    
    //绘制方法
    this.paint = function(){
        paper.clear();
        
        for (var i = 0; i < activities.length; i++) {
            //重置画图相关变量
            activities[i].maxCtrlLeft = 0;
            activities[i].maxCtrlRight = 0;
            activities[i].toTransitions = [];
            
            //设置index
            activities[i].index = i;
            activityMap[activities[i].id] = activities[i];
        }
        
        //清理
        for (var i = 0; i < transitions.length; i++) {
            //重置画图相关变量
            transitions[i].hDirection == "left";
            transitions[i].exten = 0;
            
            //设置index
            transitions[i].index = i;
            transitionMap[transitions[i].id] = transitions[i];
            activityMap[transitions[i].from].toTransitions.push(transitions[i]);
            activityMap[transitions[i].to].fromTransitions.push(transitions[i]);
        }
        
        //第一步：初始化Parallel结构，在Parallel节点中，以parallel对象存储下来
        for (var i = 0; i < activities.length; i++) {
            if (activities[i].nodeType == "Parallel") {
                i = activities[i].initParallel(activities, i);
            }
        }
		
        //第二步：在初始化Parallel结构之后，对每个分支的Control结构进行初始化，主要是刷新控制线条的绘制偏移位置以及每个节点的左右延展度
//        for (var i = 0; i < activities.length; i++) {
//            if (activities[i].nodeType == "Parallel") {
//                activities[i].initControl();
//            }
//        }
//        
        //第二步：综合第一步和第二步的数据，来更新每个分支的宽度以及每个Parallel结构的宽度
        for (var i = 0; i < activities.length; i++) {
            if (activities[i].nodeType == "Parallel") {
                activities[i].updateParallelHSize();
            }
        }
        
        //第三步：初始化主干上面的控制结构，并更新主干上面节点的左右延展度
        for (var i = 0; i < activities.length; i++) {
            if (activities[i].nodeType == "Parallel") {
                i = activities[i].parallel.end;
                continue;
            }
			flow.updateCtrl(activities[i]);
        }
        
        //第四步：根据前面的数据，更新每个Acitivity的坐标位置
        var pre = 0;
        for (var i = 0; i < activities.length; i++) {
            var temp = i;
            i = activities[i].updateData(0, i == 0 ? 0 : activities[pre].getNextY(), i);
            pre = temp;
        }
        
        //第五步：获取整个流程定义的宽度和高度
        var hSize = 0, vSize = 0;
        for (var i = 0; i < activities.length; i++) {
            if (hSize < activities[i].getHSize()) {
                hSize = activities[i].getHSize();
            }
            vSize += activities[i].getHeight();
        }
        
        //第七步：根据流程定义的宽度和高度，重置画板的Size        
        paper.setSize(hSize + workArea.config.offsetX + 100, vSize + workArea.config.offsetY + 100);
        
        //第八步：如果需要展示控制结构的可连接点，那么需要获取控制节点可以连线的环节列表
        var ctrlToAct = [];
        var ctrlAct = flow.ctrlActivity;
        if (flow.showControl) {
            //除去开始节点、结束节点和异常节点
            for (var i = 1; i < activities.length - 1; i++) {
                var ret = activities[i].findCtrlToActivies(ctrlAct);
                if (ret) {
                    ctrlToAct = ret;
                    break;
                } else {
                    ctrlToAct.push(activities[i]);
                    if (activities[i].parallel) {
                        i = activities[i].parallel.end;
                    }
                }
            }
        }
        
        //第九步：绘制节点
        for (var i = 0; i < activities.length; i++) {
            var ctrlFlag = false;
            if (activities[i].nodeType != 'Control') {
                for (var j = 0; j < ctrlToAct.length; j++) {
                    if (ctrlAct.id != ctrlToAct[j].id && ctrlToAct[j].id == activities[i].id) {
                        ctrlFlag = true;
                        break;
                    }
                }
            }
            activities[i].paint(hSize, i, ctrlFlag);
        }
        
        //第十步：绘制线条
        for (var i = 0; i < transitions.length; i++) {
            transitions[i].paint(activityMap, hSize, i);
        }
    };
    
    //流程建模操作：添加节点
    this.addNode = function(transition){
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
                activity.nodeIndex = parseInt(activity.nodeIndex) + 1;
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
            nodeIndex = parseInt(realFromActivity.nodeIndex)  + 1;
            parentId = fromActivity.id;
            branchIndex = realFromActivity.branchIndex;
        }
        
        if ("Tache" == flow.addType) {
            //初始化节点信息
            var id = workArea.util.getGid();
            var node = flow.node;
            var activity = new Activity(flow, workArea, null, paper, {
                id: id,
                name: node.text,
                branchIndex: branchIndex,
                nodeIndex: nodeIndex,
                nodeType: "Tache",
                numOfBranch: 0,
                tacheId: node.id,
                tacheCode: node.code,
                tacheName: node.text
            });
            //插入节点
            activities.splice(insertIndex, 0, activity);
            
            //初始化线条
            var firstTran = new Transition(flow, workArea, null, paper, {
                id: workArea.util.getGid(),
                name: "",
                from: fromActivity.id,
                to: id,
                lineType: "Normal",
                parentId: parentId
            });
            var secondTran = new Transition(flow, workArea, null, paper, {
                id: workArea.util.getGid(),
                name: "",
                from: id,
                to: toActivity.id,
                lineType: "Normal",
                parentId: parentId
            });
            
            //插入线条													
            transitions.splice(transition.index, 1, secondTran);
            transitions.splice(transition.index, 0, firstTran);
            workArea.paintAfterAdd();
        } else if ("Parallel" == flow.addType) {
            //初始化节点信息
            var pid = workArea.util.getGid();
            var rid = workArea.util.getGid();
            var pActivity = new Activity(flow, workArea, null, paper, {
                id: pid,
                name: "并行节点",
                branchIndex: branchIndex,
                nodeIndex: nodeIndex,
                nodeType: "Parallel",
                numOfBranch: 1
            });
            var rActivity = new Activity(flow, workArea, null, paper, {
                id: rid,
                name: "合并节点",
                nodeType: "Relation"
            });
            
            //插入节点
            activities.splice(insertIndex, 0, rActivity);
            activities.splice(insertIndex, 0, pActivity);
            
            //初始化线条
            var firstTran = new Transition(flow, workArea, null, paper, {
                id: workArea.util.getGid(),
                name: "",
                from: fromActivity.id,
                to: pid,
                lineType: "Normal",
                parentId: parentId
            });
            var secondTran = new Transition(flow, workArea, null, paper, {
                id: workArea.util.getGid(),
                name: "",
                from: pid,
                to: rid,
                lineType: "Normal",
                parentId: parentId
            });
            var thirdTran = new Transition(flow, workArea, null, paper, {
                id: workArea.util.getGid(),
                name: "",
                from: rid,
                to: toActivity.id,
                lineType: "Normal",
                parentId: parentId
            });
            
            //插入线条									
            transitions.splice(transition.index, 1, thirdTran);
            transitions.splice(transition.index, 0, secondTran);
            transitions.splice(transition.index, 0, firstTran);
            workArea.paintAfterAdd();
        } else if ("Control" == flow.addType) {
            //初始化节点信息
            var id = workArea.util.getGid();
            var activity = new Activity(flow, workArea, null, paper, {
                id: id,
                name: "控制节点",
                branchIndex: branchIndex,
                nodeIndex: nodeIndex,
                nodeType: "Control",
                numOfBranch: 0
            });
            //插入节点
            activities.splice(insertIndex, 0, activity);
            
            //初始化线条
            var firstTran = new Transition(flow, workArea, null, paper, {
                id: workArea.util.getGid(),
                name: "",
                from: fromActivity.id,
                to: id,
                lineType: "Normal",
                parentId: parentId
            });
            var secondTran = new Transition(flow, workArea, null, paper, {
                id: workArea.util.getGid(),
                name: "",
                from: id,
                to: toActivity.id,
                lineType: "Normal",
                parentId: parentId
            });
            
            //插入线条													
            transitions.splice(transition.index, 1, secondTran);
            transitions.splice(transition.index, 0, firstTran);
            workArea.paintAfterAdd(true, activity);
        }
        this.isDirty = true;
    };
    
    //流程建模操作：添加分支
    this.addBranch = function(activity){
        //增加分支数
        var act = activity;
        act.numOfBranch = 1 + parseInt(act.numOfBranch);
        
        //增加线条
        var firstTran = new Transition(flow, workArea, null, paper, {
            id: workArea.util.getGid(),
            name: "",
            from: act.id,
            to: activities[act.parallel.end].id,
            lineType: "Normal",
            parentId: act.id
        });
        transitions.splice(0, 0, firstTran);
        
        workArea.paintAfterAdd();
    };
    
    //流程建模操作：添加控制线条
    this.addTransition = function(from, to){
        for (var i = 0; i < transitions.length; i++) {
            if (transitions[i].lineType == "Control" && transitions[i].from == from.id && transitions[i].to == to.id) {
                $.messager.alert("提示", "控制节点到目标节点的线条已经存在！", "info");
                return;
            }
        }
        //初始化线条
        var firstTran = new Transition(flow, workArea, null, paper, {
            id: workArea.util.getGid(),
            name: "",
            from: from.id,
            to: to.id,
            lineType: "Control",
            parentId: null
        });
        transitions.push(firstTran);
        workArea.paintAfterAdd();
    };
    
    //流程建模操作：删除分支
    this.deleteBranch = function(transition){
        if (transition.lineType == 'Control') {
            transitions.splice(transition.index, 1);
        } else {
            var parallelActivity = transition.fromActivity;
			for(var i = transition.branchIndex;i<parallelActivity.parallel.branches.length;i++){
				 for (var j = 0; j < parallelActivity.parallel.branches[i].activities.length; j++) {
				 	parallelActivity.parallel.branches[i].activities[j].branchIndex 
						= parseInt(parallelActivity.parallel.branches[i].activities[j].branchIndex) - 1;
                }
			}
            parallelActivity.parallel.branches.splice(transition.branchIndex, 1);
            parallelActivity.numOfBranch--;
            transitions.splice(transition.index, 1);
        }
        workArea.paintAfterAdd();
    };
    
    //流程建模操作：删除环节
    this.deleteTache = function(node){
        //普通环节删除
        if (node.activity.nodeType == 'Tache') {
            for (var i = 0; i < activities.length; i++) {
                if (activities[i].id == node.activity.id) {
                    activities.splice(i, 1);
                    break;
                }
            }
            var to = null;
            for (var i = 0; i < transitions.length; i++) {
                if (transitions[i].from == node.activity.id) {
                    to = transitions[i].to;
                    transitions.splice(i, 1);
                    i--;
                }
            }
            for (var i = 0; i < transitions.length; i++) {
                if (transitions[i].to == node.activity.id) {
                    transitions[i].to = to;
                }
            }
        }        //并行结构删除
        else if (node.activity.nodeType == 'Parallel') {
            //获取要删除的节点的起点位置和结束位置
            var start = node.activity.parallel.start;
            var end = node.activity.parallel.end;
            
            var ids = [];
            var relationId = activities[end].id;
            for (var i = start; i <= end; i++) {
                ids.push(activities[i].id);
            }
            activities.splice(start, end - start + 1);
            
            var from = null;
            var to = null;
            var parentId = null;
            for (var i = 0; i < transitions.length; i++) {
                if (transitions[i].to == node.activity.id) {
                    from = transitions[i].from;
                    parentId = transitions[i].parentId;
                } else if (transitions[i].from == relationId) {
                    to = transitions[i].to;
                }
                for (var j = 0; j < ids.length; j++) {
                    if (transitions[i].from == ids[j] || transitions[i].to == ids[j]) {
                        transitions.splice(i, 1);
                        i--;
                        break;
                    }
                }
            }
            //初始化线条
            var firstTran = new Transition(flow, workArea, null, paper, {
                id: workArea.util.getGid(),
                name: "",
                from: from,
                to: to,
                lineType: "Normal",
                parentId: parentId
            });
            transitions.push(firstTran);
        } else if (node.activity.nodeType == 'Control') {
            for (var i = 0; i < activities.length; i++) {
                if (activities[i].id == node.activity.id) {
                    activities.splice(i, 1);
                    break;
                }
            }
            var to = null;
            for (var i = 0; i < transitions.length; i++) {
                if (transitions[i].from == node.activity.id) {
                    if (transitions[i].lineType != 'Control') {
                        to = transitions[i].to;
                    }
                    transitions.splice(i, 1);
                    i--;
                }
            }
            for (var i = 0; i < transitions.length; i++) {
                if (transitions[i].to == node.activity.id) {
                    transitions[i].to = to;
                }
            }
        }
        workArea.paintAfterAdd();
    };
    
    this.setException = function(rows){
        activities[activities.length - 1].exceptionConfigs = rows;
    };
    this.getException = function(){
        return activities[activities.length - 1].exceptionConfigs;
    };
    //更新控制结构影响的控制线条属性和控制线条覆盖的节点属性
    this.updateCtrl = function(activity){
        if (activity.nodeType == "Control") {
            var ctrlLines = activity.toTransitions;
            for (var m = 0; m < ctrlLines.length; m++) {
                if (ctrlLines[m].lineType == 'Control') {
                    var startNode = activityMap[ctrlLines[m].from];
                    var endNode = activityMap[ctrlLines[m].to];
                    var right = flow.getRSize(startNode.index, endNode.index);
                    var left = flow.getLSize(startNode.index, endNode.index);
                    if (right < left) {
                        transitions[ctrlLines[m].index].hDirection = "right";
                        transitions[ctrlLines[m].index].ctrlExten = right + workArea.config.nodeWidth / 2 - activity.getNodeConfig("imageRadius") / 2;
                        flow.updateRightCtrl(startNode.index, endNode.index, right + workArea.config.nodeWidth);
                    } else {
                        transitions[ctrlLines[m].index].hDirection = "left";
                        transitions[ctrlLines[m].index].ctrlExten = left + workArea.config.nodeWidth / 2 - activity.getNodeConfig("imageRadius") / 2;
                        flow.updateLeftCtrl(startNode.index, endNode.index, left + workArea.config.nodeWidth);
                    }
                }
            }
        }
    };
    
    //流程建模辅助方法：获取两个节点之间的左边延展宽度
    this.getLSize = function(fromIndex, toIndex){
        var hsize = 0;
        var start = fromIndex < toIndex ? fromIndex : toIndex;
        var end = fromIndex > toIndex ? fromIndex : toIndex;
        for (var i = start; i <= end; i++) {
            if (hsize < activities[i].getLeftSize()) {
                hsize = activities[i].getLeftSize();
            }
            if (activities[i].nodeType == 'Parallel') {
                i = activities[i].parallel.end;
            }
        }
        return hsize;
    };
    
    //流程建模辅助方法：获取两个节点之间的右边延展宽度
    this.getRSize = function(fromIndex, toIndex){
        var hsize = 0;
        var start = fromIndex < toIndex ? fromIndex : toIndex;
        var end = fromIndex > toIndex ? fromIndex : toIndex;
        for (var i = start; i <= end; i++) {
            if (hsize < activities[i].getRightSize()) {
                hsize = activities[i].getRightSize();
            }
            if (activities[i].nodeType == 'Parallel') {
                i = activities[i].parallel.end;
            }
        }
        return hsize;
    };
    
    //流程建模辅助方法：更新两个节点之间的所有节点的左边延展宽度
    this.updateLeftCtrl = function(fromIndex, toIndex, left){
        var start = fromIndex < toIndex ? fromIndex : toIndex;
        var end = fromIndex >= toIndex ? fromIndex : toIndex;
        for (var i = start; i <= end; i++) {
            activities[i].maxCtrlLeft = left;
            if (activities[i].nodeType == 'Parallel') {
                i = activities[i].parallel.end;
            }
        }
    };
    
    //流程建模辅助方法：更新两个节点之间的所有节点的右边延展宽度
    this.updateRightCtrl = function(fromIndex, toIndex, right){
        var start = fromIndex < toIndex ? fromIndex : toIndex;
        var end = fromIndex >= toIndex ? fromIndex : toIndex;
        for (var i = start; i <= end; i++) {
            activities[i].maxCtrlRight = right;
            if (activities[i].nodeType == 'Parallel') {
                i = activities[i].parallel.end;
            }
        }
    };
    
    //生成模板头xpdl
    this.getPackageHeaderXml = function(){
        var redefinableHeaderXml = "<RedefinableHeader PublicationStatus='UNDER_TEST'>" +
        "<Author>" +
        this.editUser +
        "</Author>" +
        "<Version>" +
        this.version +
        "</Version>" +
        "<Countrykey>GB</Countrykey>" +
        "</RedefinableHeader>";
        
        var packageHeaderXml = "<PackageHeader DurationUnit='m'>" +
        "<XPDLVersion>1.0</XPDLVersion>" +
        "<Vendor>ZTERC UOSFlow V5.0</Vendor>" +
        "<Created>" +
        this.editDate +
        "</Created>" +
        "<Description>" +
        this.description +
        "</Description>" +
        "<Priority>" +
        "1" +
        "</Priority>" +
        "<ValidFrom>" +
        this.validFromDate +
        "</ValidFrom>" +
        "<ValidTo>" +
        this.validToDate +
        "</ValidTo>" +
        "</PackageHeader>" +
        redefinableHeaderXml;
        return packageHeaderXml;
    };
    
    //生成xpdl
    this.generateXPDL = function(){
        var headerXml = "<ProcessHeader DurationUnit='m'>" +
        "<Created>" +
        this.editDate +
        "</Created>" +
        "<Description>" +
        this.description +
        "</Description>" +
        "<Priority>" +
        "1" +
        "</Priority>" +
        "<ValidFrom>" +
        this.validFromDate +
        "</ValidFrom>" +
        "<ValidTo>" +
        this.validToDate +
        "</ValidTo>" +
        "</ProcessHeader>";
        
        var redefinableHeaderXml = "<RedefinableHeader PublicationStatus='UNDER_TEST'>" +
        "<Author>" +
        this.editUser +
        "</Author>" +
        "<Version>" +
        this.version +
        "</Version>" +
        "<Countrykey>GB</Countrykey>" +
        "</RedefinableHeader>";
        
        this.packageHeaderXml = "<PackageHeader DurationUnit='m'>" +
        "<XPDLVersion>1.0</XPDLVersion>" +
        "<Vendor>ZTERC UOSFlow V5.0</Vendor>" +
        "<Created>" +
        this.editDate +
        "</Created>" +
        "<Description>" +
        this.description +
        "</Description>" +
        "<Priority>" +
        "1" +
        "</Priority>" +
        "<ValidFrom>" +
        this.validFromDate +
        "</ValidFrom>" +
        "<ValidTo>" +
        this.validToDate +
        "</ValidTo>" +
        "</PackageHeader>" +
        redefinableHeaderXml;
        
        //流程变量的XPDL
        var variableXml = "<DataFields>";
        //	    for(var i=0; i<workArea.dataFields.length; i++){
        //				variableXml += "<DataField Id='" + workArea.dataFields[i].variableName
        //					 + "' Name='" + workArea.dataFields[i].variableName + "'>";
        //				
        //				//数据类型
        //				variableXml += "<xpdl:DataType>";
        //				variableXml += "<xpdl:BasicType Type='" + workArea.dataFields[i].dataType + "'/>";
        //				variableXml += "</xpdl:DataType>";
        //				
        //				//初始值
        //	
        //				variableXml += "<InitialValue>" + workArea.dataFields[i].initialValue + "</InitialValue>";
        //				
        //				//描述
        //				variableXml +=
        //					 "<xpdl:Description>" + workArea.dataFields[i].description + "</xpdl:Description>";
        //				
        //				variableXml += "</DataField>";
        //	    }
        variableXml += "</DataFields>";
        
        //流程参数的XPDL
        var parameterXml = "<xpdl:FormalParameters>";
        //	    for(var i=0; i<workArea.formalParameters.length; i++){
        //				parameterXml += "<xpdl:FormalParameter Id='" + workArea.formalParameters[i].paramName
        //					 + "' Name='" + workArea.formalParameters[i].paramName
        //					 + "' Mode='" + workArea.formalParameters[i].paramType + "'>";
        //				
        //				//数据类型
        //				parameterXml += "<xpdl:DataType>";
        //				parameterXml += "<xpdl:BasicType Type='" + workArea.formalParameters[i].dataType + "'/>";
        //				parameterXml += "</xpdl:DataType>";
        //				
        //				//描述
        //				parameterXml +=
        //					 "<xpdl:Description>" + workArea.formalParameters[i].description + "</xpdl:Description>";
        //				
        //				parameterXml += "</xpdl:FormalParameter>";
        //	    }
        parameterXml += "</xpdl:FormalParameters>";
        
        //参与者的XPDL
        var participantsXml = "<Participants>";
        //		for(var i=0; i<this.nodes.length; i++){
        //			if((this.nodes[i].getNodeType() == "Tache") || (this.nodes[i].getNodeType() == "Parallel")){
        //				participantsXml += this.nodes[i].generateParticipantXPDL();
        //			}
        //		}		
        participantsXml += "</Participants>";
        
        //应用程序的XPDL
        var applicationsXml = "<Applications>";
        //		for(var i=0; i<this.nodes.length; i++){
        //			if((this.nodes[i].getNodeType() == "Tache") || (this.nodes[i].getNodeType() == "Parallel")){
        //				applicationsXml += this.nodes[i].generateApplicationXPDL();
        //			}
        //		}
        applicationsXml += "</Applications>"
        
        //活动的XPDL
        var activitiesXml = "<Activities>";
        for (var i = 0; i < activities.length; i++) {
            activitiesXml += activities[i].generateActivityXPDL();
        }
        activitiesXml += "</Activities>";
        
        //转移的XPDL
        var transitionsXml = "<Transitions>";
        for (var i = 0; i < transitions.length; i++) {
            transitionsXml += transitions[i].generateTransitionXPDL();
        }
        transitionsXml += "</Transitions>";
        
        var xpdl = "";
        xpdl += headerXml + redefinableHeaderXml + parameterXml + participantsXml + variableXml + applicationsXml + activitiesXml + transitionsXml;
        
        //扩展属性
        this.extendedAttributes = [];
        
        this.extendedAttributes.push({
            name: "ExStartOfWF",
            value: activities[0].id
        });
        this.extendedAttributes.push({
            name: "ExExceptionOfWF",
            value: activities[activities.length - 1].id
        });
        this.extendedAttributes.push({
            name: "ExEndOfWFs",
            value: [{
                name: "ExEndOfWF",
                value: activities[activities.length - 2].id
            }]
        });
        this.extendedAttributes.push({
            name: "ExStateOfWF",
            value: this.state
        });
        this.extendedAttributes.push({
            name: "ExTypeOfWF",
            value: this.type
        });
        xpdl += "<xpdl:ExtendedAttributes>";
        for (var i = 0; i < this.extendedAttributes.length; i++) {
            if (this.extendedAttributes[i].name == "ExEndOfWFs") {
                xpdl += "<xpdl:ExtendedAttribute Name='" + this.extendedAttributes[i].name + "'>";
                xpdl += "<xpdl:ExEndOfWFs>";
                //结束节点
                var ends = this.extendedAttributes[i].value;
                for (var j = 0; j < ends.length; j++) {
                    xpdl += "<xpdl:ExEndOfWF Name='" + ends[j].name +
                    "' Value='" +
                    ends[j].value +
                    "' />";
                }
                xpdl += "</xpdl:ExEndOfWFs>";
                xpdl += "</xpdl:ExtendedAttribute>";
            } else {
                xpdl += "<xpdl:ExtendedAttribute Name='" + this.extendedAttributes[i].name +
                "' Value='" +
                this.extendedAttributes[i].value +
                "' />";
            }
        }
        xpdl += "</xpdl:ExtendedAttributes>";
        
        return xpdl;
    }
}
