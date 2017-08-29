(function($){
	var session = function()
	{
		return JSON.parse($.ajax({
            url:webBasePATH+"session/getSession.do",
            type:'post',
            async: false
        }).responseText);
	}
    $.session = session;
})(jQuery);
