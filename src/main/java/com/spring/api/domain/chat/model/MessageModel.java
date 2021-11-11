package com.spring.api.domain.chat.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Alias("MessageModel")
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageModel {

	@Setter
	private int messageNo;
	@Setter
	private int senderId;
	@Setter
	private int receiverId;
	private String content; 
	@Setter
	private Date createDate;
		
}
