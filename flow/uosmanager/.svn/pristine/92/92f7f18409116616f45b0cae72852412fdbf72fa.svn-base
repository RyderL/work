(function($){
	var util = {};
	util.generateFormObj = function(dom)
	{
		var obj = {};
		$(dom).each(function(i){
			obj[this.id] = $(this).val();
		 });
		return obj;
	};
	util.formatObj = function(obj){//去掉obj中的值为undefined,null,''的属性
		var newObj={};
		for(var i in obj){
			if(obj[i]&&obj[i]!=null&&obj[i]!=''){
				newObj[i]=obj[i];
			}
		}
		return newObj;
	}
    $.util = util;
    //增加最小非空字符验证，避免一个空格通过required:true
    $.extend($.fn.validatebox.defaults.rules, {
		minLength: {
	        validator: function(value, param){
	            return $.trim(value).length >= param[0];
	        },
        	message: '请输入至少{0}个非空字符'
    	}
	});
})(jQuery);
