/**
 * Created by wxh on 2016-2-19.
 */
define([
	'hbs!../template/LeftTreeTemplate.html',
	'../../../dist/fish-topo-bpmn.js',//'../../../fish-topo-bpmn/src/fishTopoBpmn', //'../../../../dist/fish-desktop/third-party/fish-topo/fish-topo-bpmn.js', //"../../../../src/third-party/fish-topo/fish-topo-bpmn/src/FishTopoBpmn",
	'css!../styles/LeftTreeViewCss.css'
], function (leftTreeTemplate, fishTopoBpmn) {
	var LefTreeView = fish.View.extend({
		template: leftTreeTemplate,
		events: {
			"click #divSplitLine": "_clickSplit",
		},

		initialize: function(options) {

		},


		afterRender: function() {
			var me = this;
			this.$tree = this.$("#leftTree");
			this.$divRight = this.$("#divRight");
			this.$divRightPanel = this.$("#divRight-panel");
			this.$divLeft = this.$("#divLeft");

			this.initTree();

			var canvasDom = document.getElementById("flowEditorIns");
			this.fishTopoBpmn = fishTopoBpmn.init(canvasDom);
			this.fishTopoBpmn.forbidEdit = true;
			this.adjustLayout();
			$(window).resize(_.debounce(function() {
				me.adjustLayout();
			}, 80));

			var node = this.$tree.tree("getNodeByParam", "name", "MODIFY_ORDER");
			this.$tree.tree("selectNode", node);
			this.loadJson("MODIFY_ORDER");
			$("#divRight-panel").css("overflow","auto");
		},

		loadJson: function(name) {
			var me = this;
			$.ajax({
                url: 'json/' + name + '.json',
                type: 'GET',
                dataType: 'json'
            }).done(function(data) {
				console.log(data);
				me.fishTopoBpmn.fromJson(data);
            }).error(function(err) {
                console.log("error");
            });
		},

		initTree: function() {
			var me = this;
			var options2 = {
				data: {
					simpleData: {
						enable: true
					},
					key: {
						iconFontEnable: true
					}
				},
				callback: {
					onClick: function(e, treeNode) {
						me.loadJson(treeNode.name);
					}
				},
				fNodes : [
					{ id:1, pId:0, name:"EBS BPMN", open:true, iconSkinOpen:"glyphicon  glyphicon-triangle-right", iconSkinClose:"glyphicon glyphicon-triangle-bottom"},
					{ id:11, pId:1, name:"MODIFY_ORDER", iconSkin:"glyphicon glyphicon-asterisk"},
					{ id:12, pId:1, name:"ORDER_CAPTURING", iconSkin:"glyphicon glyphicon-glass"},
				]
			};
			this.$tree.tree(options2);
		},

		adjustLayout: function() {
			this.$el.outerHeight(document.documentElement.clientHeight -3);
			var treeHeight = this.$el.height() - this.$divLeft.outerHeight() + this.$tree.height();
			this.$tree.height(treeHeight);

			var rightHeight = this.$el.height() - this.$divRight.outerHeight() + this.$divRightPanel.height();
			this.$divRightPanel.height(rightHeight);
		},


		_clickSplit: function() {
			if(this.$("#divSplitLine i")[0].className.indexOf("left") > 0) {
				this.$divLeft.css("display","none");
				this.$("#divSplitLine i").removeClass().addClass("fa fa-angle-right");
				this.$("#divSplitLine").css("padding-left", "5px");
				this.$divRight.removeClass().addClass("col-md-12");
			}
			else {
				this.$divLeft.css("display", "block");
				this.$("#divSplitLine i").removeClass().addClass("fa fa-angle-left");
				this.$("#divSplitLine").css("padding-left", "0px");
				this.$divRight.removeClass().addClass("col-md-10");
			}
		}
	});
	return LefTreeView;
});
