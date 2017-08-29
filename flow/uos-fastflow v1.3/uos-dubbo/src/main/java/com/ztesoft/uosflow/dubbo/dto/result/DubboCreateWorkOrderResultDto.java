package com.ztesoft.uosflow.dubbo.dto.result;

import com.ztesoft.uosflow.dubbo.dto.DubboCommandResultDto;

/**
 * 生成工单结果对象
 * @author Administrator
 *
 */
public class DubboCreateWorkOrderResultDto extends DubboCommandResultDto {

	private static final long serialVersionUID = 1321722353839706943L;
	private String isAtuoComplete;//是否自动提交工作项
	public String getIsAtuoComplete() {
		return isAtuoComplete;
	}
	public void setIsAtuoComplete(String isAtuoComplete) {
		this.isAtuoComplete = isAtuoComplete;
	}
	
}
