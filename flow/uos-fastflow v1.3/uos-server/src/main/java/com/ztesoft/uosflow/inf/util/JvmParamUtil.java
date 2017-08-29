package com.ztesoft.uosflow.inf.util;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zterc.uos.base.helper.StringHelper;

public class JvmParamUtil {
	private static Map<String,Object> paramMap = new HashMap<String,Object>();
	private static JvmParamUtil instance;
	
	public static JvmParamUtil getInstance(){
	   if (instance == null) {
            synchronized (JvmParamUtil.class) {
                if (instance == null) {
                	instance = new JvmParamUtil();
                }
            }
        }
        return instance;
	}
	
	public JvmParamUtil(){
		List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
		if(inputArguments != null && inputArguments.size()>0){
			for(String inputArgument:inputArguments){
				String[] array = inputArgument.split("=");
				if(array != null && array.length>=2){
					String name = array[0];
					if(name.contains("-Djvm")){
						name = name.substring(6, array[0].length());
					}
					paramMap.put(name, array[1]);
				}
			}
		}
	}
	
	public Object getJvmParam(String paramName){
		return paramMap.get(paramName);
	}

	public String getServerAddr() {
		return StringHelper.valueOf(getJvmParam("server-addr"));
	}

}
