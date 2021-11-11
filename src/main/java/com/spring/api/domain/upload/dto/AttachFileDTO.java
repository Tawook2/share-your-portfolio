package com.spring.api.domain.upload.dto;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Alias("FileDTO")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class AttachFileDTO {

	private int fileNo;
	private String fileCategory;
	private int uploaderId;
	private String uuid;
	private String originalFileName;
	private String savedFileName;
	private String uploadPath;
	private long fileSize;
	private Date uplaodDate;
}
