define([
	'hbs!template/dialog/FlowParamInfoDlg.html',
    '../../../../../ext/jquery.call.js',
], function (template) {
	var tag = 'FlowParamInfoDlg';
	var FlowParamInfoDlg = fish.View.extend({
		el:false,
		template: template,
		events: {
			"click #save-button" : "ok",
			"click #close-button" : "close",
		},
		initialize: function(options) {
			this.systemCode = fish.clone(options.systemCode);
			if(options.paramData){
				this.paramData = fish.clone(options.paramData);
			}
		},

        beforeRender: function () {
            
        },

		afterRender: function() {
			$('#flowParamInfoWin-isVariable').combobox({
				required:true,
				dataTextField: 'name',
				dataValueField: 'value',
				dataSource: [{
					name: '是',
					value: '1'
				},{
					name: '否',
					value: '0'
				}]
			});
			$("#flowParamInfoWin-isVariable").combobox('value', '0');
			if(this.paramData){
				this.loadParamInfo(this.paramData);
			}
		},
		loadParamInfo:function(data){
			$("#flowParamInfoWin-code").val(data.code);
			$("#flowParamInfoWin-code").attr('disabled','disabled');
			$("#flowParamInfoWin-name").val(data.name);
			$("#flowParamInfoWin-value").val(data.value);
			$("#flowParamInfoWin-comments").val(data.comments);
			$("#flowParamInfoWin-isVariable").combobox('value', data.isVariable+'');
		},
		ok: function() {
			var param = {
				code:$('#flowParamInfoWin-code').val(),
				name:$('#flowParamInfoWin-name').val(),
				value:$('#flowParamInfoWin-value').val(),
				systemCode:this.systemCode,
				comments:$('#flowParamInfoWin-comments').val(),
				isVariable:$('#flowParamInfoWin-isVariable').combobox('value')
			};
			this.popup.close(param);
		} ,

		close: function() {
			this.popup.close();
		}         			
	});
	return FlowParamInfoDlg;
});

