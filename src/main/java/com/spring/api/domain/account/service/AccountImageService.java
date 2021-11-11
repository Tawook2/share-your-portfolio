package com.spring.api.domain.account.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import com.spring.api.domain.account.dto.AccountImageUploadRequest;

public interface AccountImageService {

	public String findAccountImagePath(int accountId);

	public HttpHeaders generateHttpHeader(String path);
	
	public byte[] parseByteArray(String path) throws FileNotFoundException, IOException;
	
	public void uploadAccountImage(MultipartFile file, int accountId, AccountImageUploadRequest request) throws IOException;
	
	public void deleteAccountImage(int accountId);
	
}
