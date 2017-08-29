(function($){
	var callSyn = function(beanName,methodName,param)
	{
		var result = $.ajax({
            url:webBasePATH+"call/call.do",
            type:'post',
            async: false,
            data:{bean:beanName,method:methodName,param:JSON.stringify(param)}
        }).responseText;
		return JSON.parse(result);
	};
	var callAsyn = function(beanName,methodName,param,callBack)
	{
		$.ajax({
            url:webBasePATH+"call/call.do",
            type:'post',
            async: true,
            data:{bean:beanName,method:methodName,param:JSON.stringify(param)},
            success: callBack
        });
	};
	
    $.callAsyn = callAsyn;
    $.callSyn = callSyn;
})(jQuery);
