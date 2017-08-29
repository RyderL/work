define([
    'hbs!../template/ShapeSelectTpl.html'
], function (template) {
    var fishTopo = null;
    var ShapeSelectView = fish.View.extend({
        el:false,
        template: template,
        serialize: function() {
            return {
               iconPrefixUrl : this.getWebRootPath(),
               shapes:
                [
                    {type: "StartNoneEvent", iconsrc: "bpmn2.0/icons/startevent/none.png", title: "开始节点", desc: "A start event without a specific trigger"},
                    {type: "EndNoneEvent", iconsrc: "bpmn2.0/icons/endevent/none.png", title: "结束节点", desc: "An intermediate catching event with a signal trigger"},
                    {type: "UserTask", iconsrc: "bpmn2.0/icons/activity/list/user.png", title: "任务节点", desc: "A manual task assigned to a specific person"},
//                    {type: "ManualTask", iconsrc: "bpmn2.0/icons/activity/list/manual.png", title: "Manual task", desc: "An automatic task with no logic"},
//                    {type: "ScriptTask", iconsrc: "bpmn2.0/icons/activity/list/script.png", title: "Script task", desc: "An automatic task with script logic"},
//                    {type: "MailTask", iconsrc: "bpmn2.0/icons/activity/list/mail.send.png", title: "Mail task", desc: "An mail task"},
//                    {type: "CatchTimerEvent", iconsrc: "bpmn2.0/icons/catching/timer.png", title: "Intermediate timer catching event", desc: "An intermediate catching event with a timer trigger"},
//                    {type: "CatchSignalEvent", iconsrc: "bpmn2.0/icons/catching/signal.png", title: "Intermediate signal catching event", desc: "An intermediate catching event with a signal trigger"},
//                    {type: "ThrowSignalEvent", iconsrc: "bpmn2.0/icons/throwing/signal.png", title: "Intermediate signal catching event", desc: "An intermediate catching event with a signal trigger"},
//                    {type: "InclusiveGateway", iconsrc: "bpmn2.0/icons/gateway/inclusive.png", title: "Inclusive gateway", desc: "An inclusive gateway"},
                    {type: "ExclusiveGateway", iconsrc: "bpmn2.0/icons/gateway/exclusive.databased.png", title: "排他网关", desc: "A choice gateway"},
                    {type: "ParallelGateway", iconsrc: "bpmn2.0/icons/gateway/parallel.png", title: "并行网关", desc: "A parallel gateway"},
//                    {type: "SubProcess", iconsrc: "bpmn2.0/icons/activity/subprocess.png", title: "SubProcess Task", desc: "An SubProcessTask"},
//                    {type: "CustomNode", iconsrc: "bpmn2.0/icons/startevent/none.png", title: "CustomNode", desc: "An CustomNode"},
                ]
            }
        },

        initialize: function(options) {
            fishTopo = options.fishTopoBpmn;
        },

        afterRender: function() {
            var me = this;
            this.$("li").each(function() {
                //添加拖放事件
                $(this).draggable({
                    helper: "clone"
                    , zIndex: 10000
                    , scroll: false
                    , appendTo: "#div_bpmn_main"
                    , start: function (event, ui) {}
                    , drag: function (event, ui) {}
                    , stop: function (event, ui) {
                        var itemType = $(this).data('itemtype');
                        //添加节点
                        var point = {x: event.pageX, y: event.pageY};
                        var offset = $(fishTopo.getDom()).offset();

                        x = point.x - offset.left ;
                        y = point.y - offset.top ;
                        x=parseInt(x/10)*10;
                        y=parseInt(y/10)*10;

                        fishTopo.addNode(itemType, x, y, {
                            name: me.getFishTopName(itemType),   //节点的名称
                            operationIcons:[{name:'DEL'},{ name: 'STRAIGHT' },{name:'JAGGED'},{ name: 'CURVE' }],
                            // 增加节点操作图标 上面分别是"删除、直线、折线、曲线"，
                            // 也可以增加自定图标 如{name: "custom1", iconPath: "img/host.png", callback: function(e) { alert(e.data.name + " clicked") }} //e.node是当前的节点
                            userData:{businessData:'我是业务数据,通过Bpmn.getUserData可获取'}
                            // 增加节点的自定义业务，可以通过Bpmn.getUserData(node)获取
                        });
                    }
                });
            })
        },
        getFishTopName: function(itemType){
        	if("StartNoneEvent"==itemType){
        		return "开始节点";
        	}else if("EndNoneEvent"==itemType){
        		return "结束节点";
        	}else if("UserTask"==itemType){
        		return " ";
        	}else if("ExclusiveGateway"==itemType){
        		return "排他网关";
        	}else if("ParallelGateway"==itemType){
        		return "并行网关";
        	}else{
        		return itemType;
        	}
        },
        getWebRootPath: function() {
            var webroot = document.location.href;
            webroot = webroot.substring(0, webroot.lastIndexOf('/'));
            return webroot ;
        },

    });
    return ShapeSelectView;
});



