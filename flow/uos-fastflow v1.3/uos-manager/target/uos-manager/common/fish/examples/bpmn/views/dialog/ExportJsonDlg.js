define([
	'hbs!template/dialog/ExportJsonDlg.html'
], function (template) {
	var tag = 'ExportJsonDlg';
	var ExportJsonDlg = fish.View.extend({
		el:false,
		template: template,
		events: {
			"click #save-button" : "ok",
		},
		initialize: function(options) {
			this.bpmnJson = fish.clone(options.bpmnJson);
		},

        beforeRender: function () {
            console.log(tag, '--------beforeRender--------');
        },

		afterRender: function() {
            console.log(tag, '--------afterRender--------');
            $("#taJson").val(this.bpmnJson);
		},

        remove: function () {
            console.log(tag, '--------remove--------');
        },	

		ok: function() {
			this.popup.close($("#taJson").val());
		}         			
	});
	return ExportJsonDlg;
});

