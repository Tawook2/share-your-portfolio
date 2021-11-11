package com.spring.api.domain.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.api.domain.account.service.AccountFindService;
import com.spring.api.domain.chat.model.MessageModel;
import com.spring.api.domain.chat.model.UserListDTO;
import com.spring.api.domain.chat.service.MessageManageService;
import com.spring.api.domain.chat.service.ReportService;
import com.spring.api.domain.model.Report;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@CrossOrigin
public class APIController {
	
	@Autowired
	MessageManageService service;
		
	@Autowired
	ReportService serviceRP;
	
	@GetMapping("/recallChat/{sender}/{receiver}")
	public List<MessageModel> bringUpMessageRecord(@PathVariable int sender, @PathVariable int receiver) {
			
		MessageModel msgModel = new MessageModel();
		
		msgModel.setSenderId(sender);
		
		msgModel.setReceiverId(receiver);
			
		return service.recallChat(msgModel);
		
	}
	
	@GetMapping("/chat/{userId}")
	public List<UserListDTO> bringUpChatUser(@PathVariable int userId) {
		
		List<UserListDTO> result = service.bringUserList(userId); 
	
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/report", method = RequestMethod.POST)
	public ResponseEntity<String> saveReportInfo(@RequestBody Report rp) {
			
		log.info(rp.toString());
	
		serviceRP.saveReport(rp);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
		
	}
}
