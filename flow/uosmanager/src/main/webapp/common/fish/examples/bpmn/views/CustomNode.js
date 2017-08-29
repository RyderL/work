define(function (require) {
    var fishTopoBpmn = require('../../../dist/fish-topo-bpmn.js');
    var BPMNNode = fishTopoBpmn.BPMNNode;
    var util = fishTopoBpmn.util;
    var BoundingRect = fishTopoBpmn.BoundingRect;
    var graphic = fishTopoBpmn.graphic;

    function CustomNode(model, api) {
        BPMNNode.call(this, model, api);
        this.bpmnInfo = {type: -1, name:"CustomNode" };    //节点信息
        this.model = model;
        this.slot = [];
        this.slotEvent = [];
        this.isCanSlot = true;
        this.rectSize = {width:100, height:60};
        this.iconPath = "M14,0H2C0.895,0,0,0.895,0,2v12c0,1.105,0.895,2,2,2h12c1.105,0,2-0.895,2-2V2C16,0.895,15.105,0,14,0z M14,13c0,0.552-0.448,1-1,1H3c-0.552,0-1-0.448-1-1V3c0-0.552,0.448-1,1-1h10c0.552,0,1,0.448,1,1V13z M5.5,7C6.328,7,7,6.328,7,5.5S6.328,4,5.5,4S4,4.672,4,5.5S4.672,7,5.5,7z M7,11L6,9l-2,3h8l-2-6L7,11z";
        this.render();
    };

    CustomNode.prototype.render = function() {
        var rect = new graphic.Rect({
            shape: {
                x: 0.5,
                y: 0.5,
                width: this.rectSize.width-1,
                height: this.rectSize.height-1,
                r: 7
            },
            style: {

                fill: '#f9f9f9',
                stroke: '#bbbbbb'
            },
            z:2
        });
        rect.name = "Rect";
        this.add(rect);

        var rect = {x:5, y: 5, width: 15, height:15};
        var pathIcon = graphic.makePath(this.iconPath, {style: {fill: '#4990E2'},z:2}, rect)
        this.add(pathIcon);
       // this.add(graphic.makePath(this.iconPath2, {style: {fill: '#4990E2'}}, rect,"center"));
        this.position =  [this.model.get("bounds.upperLeft.x"), this.model.get("bounds.upperLeft.y")];
        var title = this.drawText(this.model.get("properties.name"));
        title.text.name = "Title";
        this.add(title.text);
    };

    CustomNode.prototype.getRect = function(json) {
        // body...
        var boundingRect = this.getBoundingRect();
        //创建最小包围盒虚线
        var points = [];
        points[0] =[-boundingRect.width/2,-boundingRect.height/2];
        points[1] =[boundingRect.width/2,-boundingRect.height/2];
        points[2] =[boundingRect.width/2,boundingRect.height/2];
        points[3] =[-boundingRect.width/2,boundingRect.height/2];
        points[4] =[-boundingRect.width/2,-boundingRect.height/2];

        var boundRect = new BoundingRect(this.position[0] ,
                                         this.position[1],
                                         boundingRect.width, boundingRect.height);
        return {
            x: this.position[0]+boundingRect.width/2,
            y: this.position[1]+boundingRect.height/2,
            width: boundingRect.width,
            height: boundingRect.height,
            points: points,
            boundingRect: boundRect,
        };
    };
    CustomNode.prototype.setPosition = function(pX,pY) {
        var boundingRect = this.getBoundingRect();
        this.attr('position',[pX-boundingRect.width/2,pY-boundingRect.height/2]);
    };

    CustomNode.prototype.getBoundingRect = function() {
        var rect = this.childOfName("Rect");
        return rect.getBoundingRect();
    };

    util.inherits(CustomNode,BPMNNode);
    return CustomNode;
});
