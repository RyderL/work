var WorkArea = function(){
    var flows = {};//存储流程列表
    var paper = Raphael("flow", 100, 100);//画板对象
    var flowbar = $("#flowbar");//工具栏对象
    var session = $.session();
    
    //打开或者绘制流程
    this.addFlow = function(id, xpdl){
        var flow = new Flow(paper, xpdl, this);
        flows[id] = flow;
    };
    //关闭流程
    this.removeFlow = function(id){
        flows[id] = null;
    };
    //获取当前打开的流程定义id
    this.getCurId = function(){
        return $('#flowTab').tabs('getSelected').panel("options").id;
    };
    //获取当前打开的流程定义code --add by bobping on 2017-3-9
	    this.getCurCode = function(){
        return $('#flowTab').tabs('getSelected').panel("options").code;
    };
    this.setDirty = function(isDirty){
        var id = this.getCurId();
        flows[id].isDirty = isDirty;
    }
    this.isDirty = function(){
        var id = this.getCurId();
        return flows[id].isDirty;
    }
    //清除
    this.clear = function(){
        paper.clear();
        $("#flow_title").html("");
        flowbar.find("a").linkbutton('disable');
    };
    //绘制
    this.paint = function(){
        var id = this.getCurId();
        if (!id) {
            return;
        }
        flows[id].paint();
        this.paintTitle(id, flows[id].title, flows[id].enableEdit);
    };
    //绘制标题	
    this.paintTitle = function(id, title, enableEdit){
        var id = id;
        if (title) {
            flows[id].title = title;
            flows[id].enableEdit = enableEdit;
        }
        var model = "";
        flowbar.find("a").linkbutton('enable');
        if (flows[id].enableEdit) {
            model = "编辑";
            $("#exceptionWin").dialog({
                title: "设置异常原因配置"
            });
            $("#reasonTab").find(".edit").linkbutton('enable');
            flowbar.find(".edit").linkbutton('enable');
            
            $("#flowParamWin").dialog({
                title: "设置流程参数配置"
            });
            $("#commonFlowParamTab").find(".edit").linkbutton('enable');
            $("#flowParamGrid-title").text("双击选定应用于当前流程的参数");
            
            $("#dispatchRulesWin").data("do","edit");
            $("#dispatchRulesWin").dialog("setTitle","配置派发规则");
        } else {
            model = "查看";
            flows[id].showAddFlag = false;
            $("#exceptionWin").dialog({
                title: "查看异常原因配置"
            });
            $("#reasonTab").find(".edit").linkbutton('disable');
            flowbar.find(".edit").linkbutton('disable');
            
            $("#flowParamWin").dialog({
                title: "查看流程参数配置"
            });
            $("#commonFlowParamTab").find(".edit").linkbutton('disable');
            $("#flowParamGrid-title").text("查看应用于当前流程的参数");
            
            $("#dispatchRulesWin").data("do","show");
            $("#dispatchRulesWin").dialog("setTitle","查看派发规则");
        }
        var workAreaMsg = flows[id].title + "动作:" + model;
        $("#flow_title").html(workAreaMsg);
    };
    
    //刷新流程建模状态：在环节插入后
    //如果插入为普通环节或者并行结构：那么showControl,ctrlActivity不需要传入
    //如果插入为控制节点，那么showControl,ctrlActivity必须传入
    this.paintAfterAdd = function(showControl, ctrlActivity){
        var id = this.getCurId();
        if (!id) {
            return;
        }
        flows[id].showAddFlag = false;
        flows[id].addType = null;
        flows[id].node = null;
        flows[id].showControl = showControl;
        flows[id].ctrlActivity = ctrlActivity;
        
        this.paint();
    };
    
    //刷新流程建模状态：显示可以插入的点
    //addType:Tache,Parallel,Control
    //addType为Tache时，必须传入node，否则不需要
    this.paintBeforeAdd = function(addType, node){
        var id = this.getCurId();
        if (!id) {
            return;
        }
        flows[id].showAddFlag = true;
        flows[id].addType = addType;
        flows[id].node = node;
        this.paint();
    };
    
    //画图操作：删除环节
    this.deleteTache = function(node){
        flows[this.getCurId()].deleteTache(node);
        this.setDirty(true);
    };
    //画图操作：增加控制线条
    this.addTransition = function(from, to){
        flows[this.getCurId()].addTransition(from, to);
    };
    //画图操作：删除分支
    this.deleteBranch = function(transition){
        flows[this.getCurId()].deleteBranch(transition);
        this.setDirty(true);
    };
    this.setException = function(rows){
        //		flows[this.getCurId()].setException(rows);//保存到xpdl
        var ret = $.callSyn("ReturnReasonServ", "saveReturnReasonConfigs", {
            packageDefineId: this.getCurId(),
            areaId: session["areaId"],
           	packageDefineCode: this.getCurCode(),
            rows: rows
        });
        this.setDirty(true);
    };
    this.getException = function(){
        //		return flows[this.getCurId()].getException();//从xpdl里读取
    	//modify by bobping
        var reasons = $.callSyn("ReturnReasonServ", "qryReturnReasonConfigs", {
            packageDefineCode: this.getCurCode()
        });
        var that = this;
        $.each(reasons.rows, function(i, n){
            n.startActivityName = n.tacheName;
            n.endActivityName = n.targetTacheName || '开始节点';
            n.reasonCatalogName = that.getCalalogName(n.reasonType);
            n.returnReasonName = n.reasonName;
        });
        return reasons.rows;
    };
    this.getExceptionStartTache = function(){
        return flows[this.getCurId()].getExceptionStartTache();
    };
    this.getExceptionReturnTache = function(id){
        var exArrs = flows[this.getCurId()].getExceptionReturnTache(id);
        var exTaches = [];//去掉包含的Activity对象，以免造成回滚目标环节的下拉框空白
        $.each(exArrs, function(i, n){
            if (n.label) {
                exTaches.push(n);
            }
        });
        return exTaches;
    };
    this.getTacheId = function(id){
        return flows[this.getCurId()].getTacheId(id);
    };
    this.getCalalogName = function(id){
        var catalogName = '退单';
        if (id == '10W') {
            catalogName = '待装';
        } else if (id == '10Q') {
            catalogName = '改单';
        }
        return catalogName;
    }
    
    //配置信息
    this.config = {
        nodeWidth: 80,//节点的宽度-----垂直排版时生效
        nodeHeight: 80,//节点的高度-----水平排版时生效
        space: 30,//节点之间连线的长度
        offsetX: 0,//整个流程图的水平偏移
        offsetY: 20,//整个流程图的垂直偏移
        hLine: 10//跳转线条之间的高度间隔
    };
    
    //工具
    this.util = {
        getGid: function(){
            var now = new Date();
            var uuid = "A" + parseInt(Math.random() * 1e5) + "-" + now.getSeconds() + "-" + parseInt(Math.random() * 1e5) + "-" + now.getMilliseconds() + "-" + parseInt(Math.random() * 1e5);
            return uuid;
        },
        getExtendedValue: function(doc, name){
            var node = $(doc).find("xpdl\\:ExtendedAttribute[Name='" + name + "']");
            if (node.length == 0) {
                //chrome  中对xml命名空间的解析
                node = $(doc).find("ExtendedAttribute[Name='" + name + "']");
            }
            return node.attr("Value");
        },
        getExtendedText: function(doc, name){
            var node = $(doc).find("xpdl\\:" + name);
            if (node.length == 0) {
                node = $(doc).find(name);
            }
            return node.text();
        },
        getExtendedNodes: function(doc, name){
            var nodes = $(doc).find("xpdl\\:" + name);
            if (nodes.length == 0) {
                nodes = $(doc).find(name);
            }
            return nodes;
        },
        changeSign: function(name){
            return name.replace(new RegExp(/(<)/g), '&lt;').replace(new RegExp(/(>)/g), '&gt;');
        }
    };
    
    //生成xpdl
    this.generateXPDL = function(){
        var versionId = this.getCurId();
        ;
        var version = $.callSyn("FlowServ", "findProcessDefinitionById", {
            processDefineId: versionId
        });
        var pack = $.callSyn("FlowServ", "findPackageById", {
            packageId: version.packageId
        });
        flows[versionId].name = pack.name;
        flows[versionId].processState = version.state;
        flows[versionId].editDate = version.editDate;
        flows[versionId].validFromDate = version.validFromDate;
        flows[versionId].validToDate = version.validToDate;
        flows[versionId].description = version.description || "";
        flows[versionId].editUser = version.editUser;
        flows[versionId].version = version.version;
        flows[versionId].state = version.state;
        flows[versionId].type = pack.packageType;
        var workflowXPDL = flows[versionId].generateXPDL();
        var xpdl = "<?xml version='1.0' encoding='UTF-8'?>";
        xpdl += "<Package xmlns='http://www.wfmc.org/2002/XPDL1.0' " +
        "xmlns:xpdl='http://www.wfmc.org/2002/XPDL1.0' " +
        "xmlns:xsd='http://www.w3.org/2000/10/XMLSchema' " +
        "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
        "xsi:noNamespaceSchemaLocation='TC-1025_schema_10_xpdl.xsd' " +
        "Id='" +
        flows[versionId].id +
        "' " +
        "Name='" +
        flows[versionId].name +
        "'>";
        xpdl += flows[versionId].getPackageHeaderXml();
        xpdl += "<WorkflowProcesses>";
        xpdl += "<WorkflowProcess Id='" + flows[versionId].id +
        "' Name='" +
        flows[versionId].name +
        "' AccessLevel='PUBLIC'>";
        xpdl += workflowXPDL;
        xpdl += "</WorkflowProcess>";
        xpdl += "</WorkflowProcesses>";
        xpdl += "</Package>";
        return xpdl;
    }
    
    this.editFlowParams = function(node){
        $("#flowParamWin").data('type', 'TACHE');
        $("#flowParamWin").data('tacheCode', node.activity.exTacheCode);
        $("#flowParamWin").dialog('open');
    }
    
    this.editDispatchRules = function(node){
        $("#dispatchRulesWin").data('type', 'TACHE');
        $("#dispatchRulesWin").data('tacheId', node.activity.exTacheId);
        $("#dispatchRulesWin").data('tacheCode', node.activity.exTacheCode);
        $("#dispatchRulesWin").data('packageDefineId',  this.getCurId());
        $("#dispatchRulesWin").dialog('open');
    }
    
    this.editFlowException = function(node){
    	$('#exceptionGrid').datagrid('loadData',this.getException());
		$("#exceptionWin").dialog('open');
		$("#exceptionWin").data('change','N');//判断是否有变化——有则保存，无则不处理setException
    }
}
