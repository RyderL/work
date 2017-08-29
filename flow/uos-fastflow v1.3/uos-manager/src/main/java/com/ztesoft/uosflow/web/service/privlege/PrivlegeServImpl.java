package com.ztesoft.uosflow.web.service.privlege;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.fastflow.service.PrivlegeService;

@Service("PrivlegeServ")
public class PrivlegeServImpl implements PrivlegeServ {
	@Autowired
	private PrivlegeService privlegeService;

	@Override
	public String isExistButtonPriv(Map<String, Object> param) {
		String result = "false";
		boolean isExist = privlegeService.isExistButtonPriv(param);
		if(isExist){
			result = "true";
		}
		return GsonHelper.toJson(result);
	}

}
