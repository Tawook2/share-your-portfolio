package com.spring.api.domain.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.api.domain.chat.mapper.ReportSaveMapper;
import com.spring.api.domain.chat.service.ReportService;
import com.spring.api.domain.model.Report;

@Service
public class ReportServiceImpl implements ReportService{

	@Autowired
	ReportSaveMapper mapper;

	@Override
	public void saveReport(Report rp) {
		mapper.saveReport(rp);
		
	}
	
	

}
