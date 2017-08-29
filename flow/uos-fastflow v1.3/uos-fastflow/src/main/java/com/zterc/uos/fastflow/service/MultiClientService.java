package com.zterc.uos.fastflow.service;

import java.util.List;

import com.zterc.uos.fastflow.dao.specification.MultiClientDAO;
import com.zterc.uos.fastflow.dto.specification.MultiClientDto;

public class MultiClientService {
	private MultiClientDAO multiClientDAO;
	
	public void setMultiClientDAO(MultiClientDAO multiClientDAO) {
		this.multiClientDAO = multiClientDAO;
	}

	public List<MultiClientDto> qryAllMultiClientDto(){
		return multiClientDAO.qryAllMultiClientDto();
	}

}
