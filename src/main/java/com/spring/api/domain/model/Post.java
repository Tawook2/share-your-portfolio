package com.spring.api.domain.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Alias("Post")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Post {

	private int id;
	private int fileNo;
	private int writerId;
	private Date writeDate;
	private String title;
	private String content;
	private String category;
	private MultipartFile file;
	private int numOfPage;
	
	private String writer;
}
