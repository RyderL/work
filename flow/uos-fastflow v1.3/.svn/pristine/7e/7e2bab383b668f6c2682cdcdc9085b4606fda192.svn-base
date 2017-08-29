/**
 * Created by wxh on 2016-2-19.
 */
define([
	'hbs!../template/TopoEditTpl.html',
    '../../../dist/fish-topo-bpmn.js',//'../../../fish-topo-bpmn/src/fishTopoBpmn',//'../../../../dist/fish-desktop/third-party/fish-topo/fish-topo-bpmn.js'  //"../../../../src/third-party/fish-topo/fish-topo-bpmn/src/FishTopoBpmn"
    './CustomNode.js',
    '../../../../ext/jquery.call.js',
], function (topoEditTpl, fishTopoBpmn, CustomNode) {
	var topoAttrView = null;
	var BPMNNode = fishTopoBpmn.BPMNNode;
	var LefTreeView = fish.View.extend({
		template: topoEditTpl,
		events: {
			"click #btnExport" : "onExport",
			"click #btnImport" : "onImport",
			"click #btnTrash" : "onTrash",
			"click #btnGridLine" : "onGridLine",
			"click #btnEdit" : "onEdit",
			"click #btnForward" : "onForward",
			"click #btnBack" : "onBack",
			"click #btnSaveXpdl" : "onSaveXpdl",
			"click #btnSetFlowParam" : "onSetFlowParam",
		},

		afterRender: function() {
			this.$tabs = this.$(".ui-tabs")
			this.$splitter = this.$("#div_bpmn_splitter")

			this.initUI();
			this.initEvent();

			this.registerNewNode();
		},

		initUI: function() {
			this.$tabs.tabs();
			this.$splitter.splitter({
	            panes: [
	                {collapsible: true, size: "180px", resizable: false},
	                {collapsible: false},
	                {collapsible: true, size: "200px", resizable: false}
	            ]
      		 });

			var canvasDom = document.getElementById("flowEditorIns");
			this.fishTopoBpmn = fishTopoBpmn.init(canvasDom);
			var processDefineId = this.getQueryString('packageDefineId');
			var systemCode = this.getQueryString('systemCode');
			// 左侧选择节点视图
			this.requireView({
                            url: "views/ShapeSelectView.js",
                            selector: "#shapesNodeAccordion",
                            viewOption: {fishTopoBpmn:this.fishTopoBpmn}
                        });

			// 右侧选择属性视图
			this.requireView({
                            url: "views/TopoAttrView.js",
                            selector: ".flowPropertyPanel",
                            viewOption: {fishTopoBpmn:this.fishTopoBpmn,processDefineId:processDefineId,systemCode:systemCode},
                            callback: function(viewInstance) {
                            	topoAttrView = viewInstance;
                            }
                        });
		},

		initEvent: function() {
			var me = this;
			//双击弹出节点对应的json
			this.fishTopoBpmn.on("dblclick",function(e) {
				var target = e.target;

				if(fishTopoBpmn.Bpmn.isFlow(target)) {
					console.log("dblclick line");
				}
				else{
					var json = target.model.option;
					fish.popupView({
						url: "views/dialog/ExportJsonDlg.js",
						viewOption:{bpmnJson:JSON.stringify(json,null,4)},
						callback: function(popup,view) {
							console.log("OK");
						},
						close: function(msg) {
							console.log("return value: " + msg);
						}
					});
				}
			});


			this.fishTopoBpmn.on("click",_.bind(function(e) {
				var target = e.target;
				if(fishTopoBpmn.Bpmn.isFlow(target)) {
					var lineNode = e.target;
					this.fishTopoBpmn.bindLineDelete(lineNode);
					if(lineNode.startNode.model.get("properties.name") === "111" && lineNode.endNode.model.get("properties.name") === "222"){
						this.fishTopoBpmn.addIcon("icon1",{
							icon:"bpmn2.0/icons/activity/list/mail.send.png",
							lineNode:lineNode,
							callback:function(lineNode){
								console.log(me.fishTopoBpmn.childOfName("111"))
								alert(JSON.stringify(lineNode))
							},
						});
						this.fishTopoBpmn.addIcon("icon2",{
							icon:"bpmn2.0/icons/activity/list/script.png",
							lineNode:lineNode,
							callback:_.bind(function(lineNode){
								var nodeArray = this.fishTopoBpmn.checkLineNode(lineNode.startNode);
								console.log("连向该节点的节点数组："+nodeArray[0]+"||该节点连向的节点数组："+nodeArray[1]);
								var arr = me.fishTopoBpmn.findElements(function(e){ return e.position[0] > 100; });
								console.log("x坐标大于100的节点：")
								console.log(arr)
							},this),
						});
					}
					if(lineNode.startNode.model.get("properties.name") === "111" && lineNode.endNode.model.get("properties.name") === "333"){
						lineNode.isDelete == false;
						this.fishTopoBpmn.addIcon("icon3",{
							icon:"bpmn2.0/icons/activity/list/script.png",
							lineNode:lineNode,
							callback:function(){
//								alert("测试");
							},
						});
					}
				}
			}, this));
			//点击显示对应的节点属性面板
			this.fishTopoBpmn.on("click",function(e) {
				e.cancelBubble = true;
				topoAttrView.showPanel(e.target);
			});
			//节点删除完毕事件
			this.fishTopoBpmn.on("delete", function(e) {
//				alert(111)
			});
			//节点创建完毕事件
			this.fishTopoBpmn.on("create", function(e) {
				if(fishTopoBpmn.Bpmn.isFlow(e.target)) {
					var line = e.target;
					console.log("连线创建完毕，从" + line.startNode.model.get("properties.name") + " 至 " + line.endNode.model.get("properties.name"));
				} else {
					var node = e.target;
					console.log("节点：" + node.model.get("properties.name") + " 创建完毕");
					console.log(node)
				}
			});
		},
		registerNewNode: function() {
            BPMNNode.registerClass(CustomNode, "CustomNode");
		},

		onExport: function() {
			if(this.fishTopoBpmn) {
				var json = this.fishTopoBpmn.toJson();

				fish.popupView({
					url: "views/dialog/ExportJsonDlg.js",
					viewOption:{bpmnJson:JSON.stringify(json,null,4)},
					callback: function(popup,view) {
						console.log("OK");
					},
					close: function(msg) {
						console.log("return value: " + msg);
					}
				})
			}
		},

		onImport: function() {
			console.log("onImport");
			if(this.fishTopoBpmn) {
				fish.popupView({
					url: "views/dialog/ExportJsonDlg.js",
					viewOption:{bpmnJson:""},
					callback: function(popup,view) {
						console.log("OK");
					},
					close: _.bind(function(msg) {
						console.log("return value: " + msg);
						var json = JSON.parse(msg);
						this.fishTopoBpmn.fromJson(json);
						 topoAttrView.showPanel(this.fishTopoBpmn,null,true);
					},this)
				})
			}
		},

		onTrash: function() {
			if(this.fishTopoBpmn) {
				this.fishTopoBpmn.clear();
			}
		},
		onGridLine: function() {
			if(this.fishTopoBpmn) {
				this.fishTopoBpmn.forbidGridLine($("#btnGridLine").is(":checked"));
			}
		},
		onEdit: function() {
			if(this.fishTopoBpmn) {
				this.fishTopoBpmn.forbidEdit = $("#btnEdit").is(":checked");
			}
		},
		onForward: function() { //this.fishTopoBpmn.step计步，this.fishTopoBpmn.stepJson每步存储的json
			if(this.fishTopoBpmn) {
				if (this.fishTopoBpmn.step < this.fishTopoBpmn.stepJson.length) {
                    this.fishTopoBpmn.clear(true);
                    this.fishTopoBpmn.step += 1;
                    var json = JSON.parse(this.fishTopoBpmn.stepJson[this.fishTopoBpmn.step - 1]);
                    this.fishTopoBpmn.fromJson(json);
                }
			}
		},
		onBack: function() {
			if(this.fishTopoBpmn) {
				if (this.fishTopoBpmn.step > 0) {
                    this.fishTopoBpmn.clear(true);
                    this.fishTopoBpmn.step -= 1;
                    if (this.fishTopoBpmn.step - 1 >= 0) {
                        var json = JSON.parse(this.fishTopoBpmn.stepJson[this.fishTopoBpmn.step - 1]);
                        this.fishTopoBpmn.fromJson(json);
                    }
                }
			}
		},
		onSaveXpdl: function() {
			if(this.fishTopoBpmn) {
				var json = this.fishTopoBpmn.toJson();
				var packageDefineId = this.getQueryString('packageDefineId');
				var ret = $.callSyn("FlowFishServ","saveXpdl",{"json":JSON.stringify(json,null,4),"processDefineId":packageDefineId});
//				window.location.href=webBasePATH+'view/flow/design/flowDefManager.html';
//				window.parent.location.href=webBasePATH+'view/flow/design/flowDefManager.html';
				window.close();
			}
		},
		getQueryString: function(name){
		    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		    var r = window.location.search.substr(1).match(reg);
		    if(r!=null)return  unescape(r[2]); return null;
		},
		onSetFlowParam: function(){
			var processDefineId = this.getQueryString('packageDefineId');
			var systemCode = this.getQueryString('systemCode');
			fish.popupView({
				url: "views/dialog/SetFlowParamDlg.js",
				viewOption:{processDefineId:processDefineId,systemCode:systemCode,paramType:'FLOW',tacheCode:'-1'},
				callback: function(popup,view) {
					popup.result.then(function (data) {
						popup.close();
					},function (e) {
	            		console.log('关闭了',e);
	            	});
				}
			})
		},
	});
	return LefTreeView;
});
