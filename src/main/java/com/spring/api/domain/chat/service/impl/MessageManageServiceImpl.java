package com.spring.api.domain.chat.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.api.domain.chat.mapper.MessageManageMapper;
import com.spring.api.domain.chat.model.MessageModel;
import com.spring.api.domain.chat.model.UserListDTO;
import com.spring.api.domain.chat.service.MessageManageService;

@Service
public class MessageManageServiceImpl implements MessageManageService{

	@Autowired
	MessageManageMapper mapper;
	
	@Override
	public void saveMessage(MessageModel msgModel) {
		
		mapper.saveMessage(msgModel);
	}

	@Override
	public List<MessageModel> recallChat(MessageModel msgModel) {
			
		return mapper.recallChat(msgModel);
	}

	@Override
	public Date checkTime(int messageNo) {
		
		return mapper.checkTime(messageNo);
	}
	
	@Override
	public List<UserListDTO> bringUserList(int userId) {
		
		return mapper.bringUserList(userId);
	}

}
