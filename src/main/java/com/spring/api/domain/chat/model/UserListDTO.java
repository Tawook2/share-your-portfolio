package com.spring.api.domain.chat.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Alias("UserList")
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserListDTO {
	
	private int id;
	private String name;
	private String content;
	private Date createDate;

}
