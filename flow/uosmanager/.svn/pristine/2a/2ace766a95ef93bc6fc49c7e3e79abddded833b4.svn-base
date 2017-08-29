var Transition = function(flow, workArea, xml, paper, obj){
    var flow = flow;
    var workArea = workArea;
    var transition = this;
    
    //画图相关变量-----重绘需要清空
    this.index = -1;//在列表中的位置
    this.ctrlExten = 0;//控制线条的广度
    this.hDirection = "left";//控制线条在左边还是在右边
    this.description = "";
    this.condition = "";//{$paramName$=value}
    this.extendedAttributes = [];
    
    if (xml) {
        this.id = $(xml).attr("Id");
        this.name = $(xml).attr("Name");
        this.from = $(xml).attr("From");
        this.to = $(xml).attr("To");
        this.lineType = workArea.util.getExtendedValue(xml, "LineType");
        this.parentId = workArea.util.getExtendedValue(xml, "parentId");
        //		var conditionStr = workArea.util.getExtendedText(xml,"Xpression").split(",");
        //		this.condition.id = conditionStr[0];
        //		this.condition.name = conditionStr[1];
        var conditionStr = workArea.util.getExtendedText(xml, "Xpression");
        this.condition = conditionStr;
        if ("[object Object]" == conditionStr) {
            this.condition = "";
        }
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
    this.line = null;
    this.circle = null;
    
    this.refreshText = function(){
        if (this.text) {
            this.text.attr("text", this.name);
        }
    }
    
    this.paint = function(activityMap, hSize, index){
        this.index = index;
        
        var startNode = activityMap[this.from];
        var endNode = activityMap[this.to];
        this.fromActivity = startNode;
        this.toActivity = endNode;
        
        var startPoint = startNode.getBottomPoint();
        var endPoint = endNode.getTopPoint();
        var displayName = this.name.length > 5 ? this.name.substr(0, 4) + "..." : this.name;
        
        var changePos = workArea.config.space * 2 / 5;
        if (this.lineType == "Normal") {
            if (startNode.nodeType == "Parallel") {
                var frontChangePos = workArea.config.space * 1 / 10;
                if (endNode.nodeType == "Relation") {
                    var info = startNode.getBlankTransitionX(hSize);
                    var x = info.x;
                    this.branchIndex = info.branchIndex;
                    var firstPoint = {
                        x: startPoint.x,
                        y: startPoint.y + frontChangePos
                    };
                    var secondPoint = {
                        x: x,
                        y: startPoint.y + frontChangePos
                    };
                    var thirdPoint = {
                        x: x,
                        y: endPoint.y - changePos
                    };
                    var fourthPoint = {
                        x: endPoint.x,
                        y: endPoint.y - changePos
                    };
                    this.line = this.paper.path("M" + startPoint.x + " " + startPoint.y + "L" + firstPoint.x + " " + firstPoint.y +
	                    "L" + secondPoint.x + " " + secondPoint.y +
	                    "L" + thirdPoint.x + " " + thirdPoint.y +
	                    "L" + fourthPoint.x + " " + fourthPoint.y +
	                    "L" + endPoint.x + " " + endPoint.y );
                    this.text = paper.text(secondPoint.x + 3, secondPoint.y + 10, displayName).attr({
                        "text-anchor": "start",
                        "width": 75,
                        "overflow": "hidden",
                        "display": "block"
                    });
                    this.circle = this.paper.circle(secondPoint.x, (secondPoint.y + thirdPoint.y) / 2, 5).attr({
                        "fill": "#fff"
                    });
                } else {
                    changePos = workArea.config.space * 1 / 10;
                    var firstPoint = {
                        x: startPoint.x,
                        y: startPoint.y + changePos
                    };
                    var secondPoint = {
                        x: endPoint.x,
                        y: startPoint.y + changePos
                    };
                    this.line = this.paper.path("M" + startPoint.x + " " + startPoint.y +
	                    "L" +  firstPoint.x + " " +  firstPoint.y +
	                    "L" +  secondPoint.x +  " " +  secondPoint.y +
	                    "L" +  endPoint.x + " " + endPoint.y);
                    this.text = paper.text(secondPoint.x + 3, secondPoint.y + 10, displayName).attr({
                        "text-anchor": "start",
                        "width": 75,
                        "overflow": "hidden",
                        "display": "block"
                    });
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
	                    "L" + firstPoint.x + " " + firstPoint.y +
	                    "L" + secondPoint.x + " " + secondPoint.y +
	                    "L" + endPoint.x + " " +  endPoint.y);
					this.text = paper.text(startPoint.x + 3, startPoint.y + 10, displayName).attr({
                        "text-anchor": "start",
                        "width": 75,
                        "overflow": "hidden",
                        "display": "block"
                    });
                    this.circle = this.paper.circle(startPoint.x, (startPoint.y + endPoint.y) / 2, 5).attr({
                        "fill": "#fff"
                    });
                } else {
					this.text = paper.text(startPoint.x + 3, startPoint.y + 10, displayName).attr({
                        "text-anchor": "start",
                        "width": 75,
                        "overflow": "hidden",
                        "display": "block"
                    });
                    this.line = this.paper.path("M" + startPoint.x + " " + startPoint.y + "L" + endPoint.x + " " + endPoint.y + "");
                    this.circle = this.paper.circle(startPoint.x, (startPoint.y + endPoint.y) / 2, 5).attr({
                        "fill": "#fff"
                    });
                }
            }
            if (this.circle) {
                this.circle.hide();
                $(this.circle.node).css({
                    cursor: "pointer"
                });
                if (flow.showAddFlag) {
                    this.circle.show();
                    this.circle.click(function(evt){
                        flow.addNode(transition);
                    });
                } else {
                    this.circle.hide();
                }
            }
        } else if (this.lineType == "Control") {
            var offset = 0;
            if (this.hDirection == 'left') {
                var startPoint = startNode.getLeftPoint();
                var endPoint = endNode.getLeftPoint();
                offset = 0 - this.ctrlExten;
            } else if (this.hDirection == 'right') {
                var startPoint = startNode.getRightPoint();
                var endPoint = endNode.getRightPoint();
                offset = 0 + this.ctrlExten;
            }
            var firstPoint = {
                x: startPoint.x + offset,
                y: startPoint.y
            };
            var secondPoint = {
                x: startPoint.x + offset,
                y: endPoint.y
            };
            if (startPoint.y > endPoint.y) {
                this.text = paper.text(firstPoint.x + 3, firstPoint.y - 10, displayName).attr({
                    "text-anchor": "start",
                    "width": 75,
                    "overflow": "hidden",
                    "display": "block"
                });
            } else {
                this.text = paper.text(firstPoint.x + 3, firstPoint.y + 10, displayName).attr({
                    "text-anchor": "start",
                    "width": 75,
                    "overflow": "hidden",
                    "display": "block"
                });
            }
            
            this.line = this.paper.path("M" + startPoint.x + " " + startPoint.y + "L" + firstPoint.x + " " + firstPoint.y +
		            "L" + secondPoint.x + " " +  secondPoint.y +
		            "L" + endPoint.x + " " + endPoint.y  );
        }
        if (this.line) {
            this.line.attr({
                "stroke-width": 2,
                stroke: "#444",
                "arrow-end": "classic-wide-long"
            });
            var transition = this;
            this.line.mousedown(function(handler){
                if (handler.which == 3)//右击事件
                {
                    handler.preventDefault();
                    handler.stopPropagation();
                    if (flow.enableEdit) {
						if (transition.lineType == 'Control'||(endNode.nodeType == "Relation"&&startNode.nodeType == "Parallel")) {
	                         $("#lineMenu .edit").show();
	                    }else{
							$("#deleteBranch").hide();
						}
                    } else {
                        $("#lineMenu .edit").hide();
                    }
                    $("#lineMenu").menu('show', {
                        left: handler.pageX,
                        top: handler.pageY,
                        hideOnUnhover: false
                    }).data("transition", transition);
                }
            });
        }
    };
    
    this.generateTransitionXPDL = function(){
        var xpdl = "";
        //如果开始节点是并行节点，那么线条开始节点的id应当是这个并行节点的合并条件
        xpdl += "<Transition Id='" + this.id + "' Name='" + this.name +
        "' From='" +
        this.fromActivity.id +
        "' To='" +
        this.toActivity.id +
        "'>";
        
        //加上描述
        if (this.description) {
            xpdl += "<Description>" + this.description + "</Description>";
        } else {
            xpdl += "<Description />"
        }
        if (this.condition) {
            xpdl += "<xpdl:Condition><xpdl:Xpression>" +
            "<![CDATA[" +
            this.condition +
            "]]>" +
            "</xpdl:Xpression></xpdl:Condition>";
        } else {
            xpdl += "<xpdl:Condition><xpdl:Xpression/></xpdl:Condition>";
        }
        //扩展属性
        this.extendedAttributes = [];
        this.extendedAttributes.push({
            name: "LineType",
            value: this.lineType
        });
        if (this.lineType != "Control") {
            this.extendedAttributes.push({
                name: "parentId",
                value: this.parentId
            });
        }
        xpdl += "<xpdl:ExtendedAttributes>";
        for (var i = 0; i < this.extendedAttributes.length; i++) {
            xpdl += "<xpdl:ExtendedAttribute Name='" + this.extendedAttributes[i].name +
            "' Value='" +
            this.extendedAttributes[i].value +
            "' />";
        }
        xpdl += "</xpdl:ExtendedAttributes>";
        
        xpdl += "</Transition>";
        return xpdl;
    }
};
