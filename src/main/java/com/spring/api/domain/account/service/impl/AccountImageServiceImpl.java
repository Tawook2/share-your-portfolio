package com.spring.api.domain.account.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.api.domain.account.dto.AccountImageUploadRequest;
import com.spring.api.domain.account.mapper.AccountImageMapper;
import com.spring.api.domain.account.service.AccountImageService;
import com.spring.api.domain.model.AccountImage;
import com.spring.api.global.util.file.AccountImageMediaType;
import com.spring.api.global.util.file.FileUtils;

@Service
public class AccountImageServiceImpl implements AccountImageService {

	@Autowired
	private AccountImageMapper mapper;

	@Autowired
	private FileUtils fileUtils;
	
	@Value("${file.path.root}")
	private String root;
	
	@Value("${file.path.account}")
	private String accountPath;
	
	@Override
	public String findAccountImagePath(int accountId) {
		return mapper.findAccountImagePath(accountId);
	}

	@Override
	public HttpHeaders generateHttpHeader(String path) {
		String ext = fileUtils.getFileExtension(path);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(AccountImageMediaType.valueOf(ext).getMediaType());
		return headers;
	}

	@Override
	public byte[] parseByteArray(String path) throws FileNotFoundException, IOException {
		return IOUtils.toByteArray(new FileInputStream(getRootPath() + path));
	}
	
	@Override
	public void uploadAccountImage(MultipartFile file, int accountId, AccountImageUploadRequest request) throws IOException {
		String path = findAccountImagePath(accountId);
		String newPath = fileUtils.saveCroppedImageFile(file, getRootPath(), request);
		AccountImage accountImage = AccountImage.builder()
												.accountId(accountId)
												.path(newPath)
												.build();
		mapper.save(accountImage);
		if (path != null) {
			fileUtils.deleteFile(getRootPath() + path);
		}
	}

	private String getRootPath() {
		return root + File.separator + accountPath;
	}

	@Override
	public void deleteAccountImage(int accountId) {
		String path = findAccountImagePath(accountId);
		if (path != null) {
			mapper.delete(accountId);
			fileUtils.deleteFile(getRootPath() + path);
		}
	}
	
}
