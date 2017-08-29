$(document).ready(function() {
	
	$("#btnCode").bind("click", function(evt){
        $("#imgcode").attr("src", "code.do?time=" + new Date().getTime());
    });
    
    var submitFun = function(evt){
    	$('#loginForm').form('submit',{
			onSubmit:function(){
				if($(this).form('enableValidation').form('validate')){
					$.ajax({
		                url: "login.do",
		                type: 'post',
		                data: {
		                    username: $("#username").val(),
		                    password: $("#password").val(),
		                    certcode: $("#certcode").val()
		                },
		                error: function(xml){
		                    throw new Error(0, "Network issue or remote server issue");
		                },
		                success: function(retVal){
		                    retVal = retVal.replace(/^\s*|\s*$/g, '');
		                    if (retVal == "certCodeIncorrect") {
		                        alert('验证码不正确！！');
		                        $("#btnCode").trigger("click");
		                        return;
		                    } else if (retVal == "usernameorpasswordIncorrect") {
		                        alert('用户名或者密码不正确！！');
		                        $("#btnCode").trigger("click");
		                        return;
		                    }else if(retVal == "ipIncorrect"){
		                    	alert('IP地址不正确！！');
		                        $("#btnCode").trigger("click");
		                        return;
		                    }else if (retVal == "Y") {
		                        self.location = 'view/main/main.html';
		                    }
		                }
		            });
				}
				return;
			}
		});
    };
	
	$("#btnLogin").bind("click", submitFun);
	
	$("#btnReset").bind("click", function(evt){
		$('#username').attr("value","");
		$('#password').attr("value","");
	});
	
	$('#loginForm').keydown(function(evt){
		if(evt.keyCode==13){//Enter事件
			submitFun(evt);
		}
	});
});
