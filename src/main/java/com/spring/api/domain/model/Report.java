package com.spring.api.domain.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Alias("Report")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Report {

	private int reportNo;
	private int reportId;
	private int reportedId;
	private String reportType;
	private String reportDetail;
	private Date reportDate;
	
	
}
