package com.ztesoft.uosflow.web.inf.client;

import com.ztesoft.uosflow.web.inf.model.RequestDto;
import com.ztesoft.uosflow.web.inf.model.ResponseDto;

public interface IClient {
	public ResponseDto sendMessage(RequestDto dto);
}
