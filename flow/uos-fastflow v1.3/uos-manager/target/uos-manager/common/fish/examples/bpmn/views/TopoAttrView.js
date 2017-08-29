define([
    'hbs!../template/TopoAttrTpl.html',
    '../../../dist/fish-topo-bpmn.js'//'../../../fish-topo-bpmn/src/fishTopoBpmn'//'../../../../dist/fish-desktop/third-party/fish-topo/fish-topo-bpmn.js'//"../../../../src/third-party/fish-topo/fish-topo-bpmn/src/Bpmn.js"

], function (template, fishTopoBpmn) {
    var Bpmn = fishTopoBpmn.Bpmn;

    var TopoEditView = fish.View.extend({
        el:false,
        template: template,
		events: {
			"click #setTacheFlowParam" : "setTacheFlowParam",
		},
        initialize: function(options) {
            this.editor = options.fishTopoBpmn;
            this.processDefineId = options.processDefineId;
            this.systemCode = options.systemCode;
        },

        afterRender: function() {
            this.initAttr();
            this.showPanel(this.editor);
        },

        initAttr: function() {

            var that = this;
            this.rootPanel = $(".rootPanel");
            this.activitiPanel = $(".activitiPanel");
            this.gatewayPanel = $(".gatewayPanel");
            this.connectPanel = $(".connectPanel");
            this.eventPanel = $(".eventPanel");
            this.subProcessPanel = $(".subProcessPanel");

            this.hideAllPanel();
            //初始化属性
            this.rootDom = {};
            this.rootDom.bpmTxtTmplName = $("#_bpmTxtTmplName");
            this.rootDom.bpmTxtTmplVersion = $("#_bpmTxtTmplVersion");
            this.rootDom.bpmTxtTmplEffDate = $("#_bpmTxtTmplEffDate");
            this.rootDom.bpmTxtTmplEffDate.datetimepicker();
            this.rootDom.bpmTxtTmplExpDate = $("#_bpmTxtTmplExpDate");
            this.rootDom.bpmTxtTmplExpDate.datetimepicker();
            this.rootDom.bpmWidth = $("#_bpmWidth");
            this.rootDom.bpmHeight = $("#_bpmHeight");
            this.rootDom.bpmWidth.on("focusout",function(event){
                changeSize();
            });
            this.rootDom.bpmHeight.on("focusout",function(event){
                changeSize();
            });
            function changeSize(){
                var width,height;
                if(that.rootDom.bpmWidth.val().length > 0){
                    width = that.rootDom.bpmWidth.val();
                }else{
                    width = 2000;
                }
                if(that.rootDom.bpmHeight.val().length > 0){
                    height = that.rootDom.bpmHeight.val();
                }else{
                    height = 960;
                }
                var option = that.getTemplateInfo();
                that.editor.setShapeModel(that.editor, option,width,height);
            }
            changeSize();
            this.activitiDom = {};
            this.activitiDom.bpmTxtActivityName = $("#_bpmTxtActivityName");
            this.activitiDom.bpmTxtActivityActivityName = $("#_bpmTxtActivityActivityName");
            this.activitiDom.bpmTxtActivityName.on("focusout", function(event) {
                // 这个里面获取所有的任务节点信息
                var option = that.getActivityInfo();
                that.editor.setShapeModel(that.curShape, option);
            });


            this.activitiDom.bpmCboScriptType = $("#_bpmCboScriptType");
            this.activitiDom.bpmTxtScript = $("#_bpmTxtScript");
            this.activitiDom.bpmTxtActivityNotes = $("#_bpmTxtActivityNotes");

            //属性分组
            this.taskCommAttrs = {};
            this.taskCommAttrs["_attrActivityName"] = $("#_attrActivityName");
            this.taskCommAttrs["_attrActivityActivityName"] = $("#_bpmTxtActivityActivityName");

            this.scriptTaskAttrs = {};
            this.scriptTaskAttrs["_attrScriptType"] = $("#_attrScriptType");
            this.scriptTaskAttrs["_attrScript"] = $("#_attrScript");
            //初始化脚本选择
            this.scriptDs = [
                { "name": "groovy", "value": "0" },
                { "name": "jython", "value": "1" },
                { "name": "sql", "value": "2" },
                { "name": "javascript", "value": "3" },
            ];

            this.activitiDom.bpmCboScriptType.combobox({
                dataSource: self.scriptDs,
                value: "1",
                editable: false
            });


            this.gatewayDom = {};
            this.gatewayDom.bpmTxtGatewayName = $("#_bpmTxtGatewayName");
            this.gatewayDom.bpmTxtGatewayName.on("focusout", function(event) {
                // 这个里面获取所有的任务节点信息
                var option = that.getGatewayInfo();
                that.editor.setShapeModel(that.curShape, option);
            });

            this.connectDom = {};
            this.connectDom.bpmTxtConnectName= $("#_bpmTxtConnectName");
            this.connectDom.bpmTxtExpression = $("#_bpmTxtExpression");
            this.connectDom.bpmCboIsLoop = $("#_bpmCboIsLoop");
            this.connectDom.bpmCboIsDefault = $("#_bpmCboIsDefault");
            this.connectDom.bpmTxtTmplNotes = $("#_bpmTxtTmplNotes");


            this.connectDom.bpmTxtConnectName.on("focusout", function(event) {
                var option = that.getConnectorInfo();
                that.editor.setShapeModel(that.curShape, option);
            });

            //默认线 需要更改一下线段的类型
            this.connectDom.bpmCboIsDefault.on("combobox:change", function(event) {
                var option = that.getConnectorInfo();
                if (that.curShape) {
                    that.editor.setShapeModel(that.curShape, option);
                };
            });

            //初始化是否选项
            $.each([this.connectDom.bpmCboIsLoop, this.connectDom.bpmCboIsDefault], function(index, cb) {
                cb.combobox({
                    dataSource: [
                        { "name": "No", "value": "0" },
                        { "name": "Yes", "value": "1" }
                    ],
                    value: "0",
                    editable: false
                });
            });

            this.eventDom = {};
            this.eventDom.bpmTxtEventName = $("#_bpmTxtEventName");
            this.eventDom.bpmTxtEventName.on("focusout", function(event) {
                // 这个里面获取所有的任务节点信息
                var option = that.getEventInfo();
                that.editor.setShapeModel(that.curShape, option);
            });

            this.subProcessDom = {};
            this.subProcessDom.bpmTxtSubProcessName = $("#_bpmTxtSubProcessName");
            this.subProcessDom.bpmTxtSubProcessName.on("focusout", function(event) {
                // 这个里面获取所有的任务节点信息
                var option = that.getSubProcessInfo();
                that.editor.setShapeModel(that.curShape, option);
            });
        },

        hideAllPanel: function() {
            this.rootPanel.hide();
            this.activitiPanel.hide();
            this.gatewayPanel.hide();
            this.connectPanel.hide();
            this.eventPanel.hide();
            this.subProcessPanel.hide();
        },

        /**
         * 显示面板
         * @param  {[type]} shape       [description]
         * @param  {[type]} ignoreAttrs [description]
         * @return {[type]}             [description]
         */
        showPanel: function(shape, ignoreAttrs, isFirst) {
            if (!isFirst) {
                this.setPrePanelValue();
            };

            if (Bpmn.isTemplate(shape)) {
                this.hideAllPanel();
                this.rootPanel.show();

            } else if (Bpmn.isActivity(shape)) {
                this.hideAllPanel();
                this.activitiPanel.show();

            } else if (Bpmn.isGateway(shape)) {
                this.hideAllPanel();
                this.gatewayPanel.show();

            } else if (Bpmn.isFlow(shape)) {
                this.hideAllPanel();
                this.connectPanel.show();

            } else if (Bpmn.isEvent(shape)) {
                this.hideAllPanel();
                this.eventPanel.show();

            } else if (Bpmn.isSubProcess(shape)) {
                this.hideAllPanel();
                this.subProcessPanel.show();
            };
            //赋值
            this.pushData(shape);
            //属性过滤
            this._filter(shape, ignoreAttrs);

            this.curShape = shape;
        },

        defaults:function(obj, defaultVal) {
            if (obj == null) {
                return defaultVal;
            } else {
                return obj;
            }
        },
        /**
         * 把数据更新到页面上去
         * @param {type} data
         * @returns {undefined}
         */
        pushData: function(shape) {
            var self = this;
            ud = Bpmn.getProp(shape);
            if (ud) {
                if (Bpmn.isTemplate(shape)) {
                    var dom = self.rootDom;

                    //根模板
                    ud.name = this.defaults(ud.name, "Untitled");
                    dom.bpmTxtTmplName.val(ud.name);

                    dom.bpmTxtTmplVersion.val(ud.version);
                    ud.effDate = ud.effDate || "2015-09-15 13:30:21";
                    dom.bpmTxtTmplEffDate.datetimepicker("value", ud.effDate);

                    ud.expDate = ud.expDate || "2016-09-15 13:30:21";
                    dom.bpmTxtTmplExpDate.datetimepicker("value", ud.expDate);

                    ud.version = ud.version || "1.0";
                    dom.bpmTxtTmplVersion.val(ud.version);
                    ud.width = ud.width || "2000";
                    dom.bpmWidth.val(ud.width);
                    ud.height = ud.height || "960";
                    dom.bpmHeight.val(ud.height);
                }
                else if (Bpmn.isActivity(shape)) {
                    var dom = self.activitiDom;
                    ud.name = this.defaults(ud.name, Bpmn.getType(shape));
                    dom.bpmTxtActivityName.val(ud.name);
                    ud.activityName = ud.activityName || " ";
                    dom.bpmTxtActivityActivityName.val(ud.activityName);
                    dom.bpmTxtActivityActivityName.val(ud.activityName);
                    dom.bpmCboScriptType.combobox("value", ud.scriptType);
                    dom.bpmTxtScript.val(ud.scriptContent);
                    dom.bpmTxtActivityNotes.val(ud.notes);
                }
                else if (Bpmn.isGateway(shape)) {
                    ud.name = this.defaults(ud.name, Bpmn.getType(shape));
                    var dom = self.gatewayDom;
                    dom.bpmTxtGatewayName.val(ud.name);
                }
                else if (Bpmn.isFlow(shape)) {
                    var dom = self.connectDom;
                    dom.bpmTxtExpression.val(ud.conditionExpr);
                    dom.bpmCboIsLoop.combobox("value", ud.isLoop);
                    dom.bpmCboIsDefault.combobox("value", ud.isDefault);
                    dom.bpmTxtTmplNotes.val(ud.notes);
                }
                else if (Bpmn.isEvent(shape)) {
                    var dom = self.eventDom;
                    ud.name = this.defaults(ud.name, Bpmn.getType(shape));
                    dom.bpmTxtEventName.val(ud.name);
                }
                else if (Bpmn.isSubProcess(shape)) {
                    var dom = self.subProcessDom;
                    ud.name = this.defaults(ud.name, Bpmn.getType(shape));
                    dom.bpmTxtSubProcessName.val(ud.name);
                }
            }

        },

        _filter: function(shape, ignoreAttrs) {
            var ignoreAttrs = ignoreAttrs || {};
            if (Bpmn.isActivity(shape)) {
                this.filterActivityProps(shape, ignoreAttrs);
            }
        },

        setPrePanelValue: function() {
            //切换面板前，先保证上一个面板 的取值
            if (this.curShape) {
                var option = null;
                if (Bpmn.isTemplate(this.curShape)) {
                    option = this.getTemplateInfo();
                }
                else if (Bpmn.isActivity(this.curShape)) {
                    option = this.getActivityInfo();
                }
                else if (Bpmn.isFlow(this.curShape)) {
                    option = this.getConnectorInfo();
                }
                else if (Bpmn.isEvent(this.curShape)) {
                    option = this.getEventInfo();
                }
                else if (Bpmn.isGateway(this.curShape)) {
                    option = this.getGatewayInfo();
                }
                else if (Bpmn.isSubProcess(this.curShape)) {
                    option = this.getSubProcessInfo();
                }

                if(option) {
                    this.editor.setShapeModel(this.curShape, option);
                }
            }
        },

       getTemplateInfo: function() {
            var result = {};
            result.properties = {};
            result.bounds = {};
            result.properties.name =  this.rootDom.bpmTxtTmplName.val();
            result.properties.version = this.rootDom.bpmTxtTmplVersion.val();
            result.properties.effDate = this.rootDom.bpmTxtTmplEffDate.datetimepicker("value");
            result.properties.expDate = this.rootDom.bpmTxtTmplExpDate.datetimepicker("value");
            result.properties.width = this.rootDom.bpmWidth.val();
            result.properties.height = this.rootDom.bpmHeight.val();
            result.bounds.lowerRight = {
                x:this.rootDom.bpmWidth.val(),
                y:this.rootDom.bpmHeight.val(),
            };
            return result;
        },

        getActivityInfo: function() {
            var result = {};
            result.properties = {};
            result.properties.name = this.activitiDom.bpmTxtActivityName.val();
            result.properties.activityName = this.activitiDom.bpmTxtActivityActivityName.val();
            result.properties.notes = this.activitiDom.bpmTxtActivityNotes.val();
            return result;
        },

        getConnectorInfo: function(argument) {
            var result = {};
            result.properties = {};
            result.style = {};
            result.properties.name =  this.connectDom.bpmTxtConnectName.val();
            result.properties.conditionExpr = this.connectDom.bpmTxtExpression.val();
            result.properties.isLoop = this.connectDom.bpmCboIsLoop.combobox("value");
            result.properties.isDefault = this.connectDom.bpmCboIsDefault.combobox("value");
            result.properties.notes = this.connectDom.bpmTxtTmplNotes.val();
            return result;
        },
        getEventInfo:function(){
            var result = {};
            result.properties = {};
            result.properties.name =  this.eventDom.bpmTxtEventName.val();
            return result;
        },
        getGatewayInfo:function(){
            var result = {};
            result.properties = {};
            result.properties.name =  this.gatewayDom.bpmTxtGatewayName.val();
            return result;
        },
        getSubProcessInfo:function(){
            var result = {};
            result.properties = {};
            result.properties.name =  this.subProcessDom.bpmTxtSubProcessName.val();
            return result;
        },

        filterActivityProps: function(shape, ignore) {
            var t = Bpmn.getType(shape);
            var self = this;

            this.showAttrs(this.taskCommAttrs, ignore);
            this.hideAttrs(this.scriptTaskAttrs);

            if (t === Bpmn.BPMN_SCRIPT_TASK) {
                this.showAttrs(this.scriptTaskAttrs, ignore);
            }
        },

        showAttrs: function(attrs, ignore) {
            var id;
            for (id in attrs) {
                if (id in ignore) {
                    if (attrs[id]) {
                        attrs[id].hide();
                    }
                    continue;
                }
                if (attrs[id]) {
                    attrs[id].show();
                }
            }
        },
        hideAttrs: function(attrs) {
            var id;
            for (id in attrs) {
                attrs[id].hide();
            }
        },
        setTacheFlowParam: function(){
			var tacheCode = $("#_bpmTxtActivityActivityName").val();
			fish.popupView({
				url: "views/dialog/SetFlowParamDlg.js",
				viewOption:{processDefineId:this.processDefineId,systemCode:this.systemCode,paramType:'TACHE',tacheCode:tacheCode},
				callback: function(popup,view) {
					popup.result.then(function (data) {
						popup.close();
					},function (e) {
	            		console.log('关闭了',e);
	            	});
				}
			})
        }
    });
    return TopoEditView;
});








