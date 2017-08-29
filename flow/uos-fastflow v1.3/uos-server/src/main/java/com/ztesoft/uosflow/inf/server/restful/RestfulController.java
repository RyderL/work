package com.ztesoft.uosflow.inf.server.restful;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztesoft.uosflow.inf.server.common.ServerProxy;


@Controller
@RequestMapping("/restful")
@Configuration
public class RestfulController{
	
	@RequestMapping(value = "/call",produces = "text/html;charset=UTF-8")
    public @ResponseBody String methodInvoke(String param) {
		return ServerProxy.getInstance().dealForJson(param);
    }
}
