package com.spring.api.domain.account.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.api.domain.account.dto.AccountImageUploadRequest;
import com.spring.api.domain.account.service.AccountImageService;
import com.spring.api.global.security.model.LoginDetails;

import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/account/image")
@RestController
public class AccountImageController {
	
	@Autowired
	private AccountImageService accountImageService;
	
	@GetMapping("/{accountId}")
	public ResponseEntity<byte[]> getAccountImage(@PathVariable int accountId) throws FileNotFoundException, IOException {
		String path = accountImageService.findAccountImagePath(accountId);
		if (path == null) {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
		
		HttpHeaders headers = accountImageService.generateHttpHeader(path);
		byte[] imageBytes = accountImageService.parseByteArray(path);		
		return new ResponseEntity<byte[]>(imageBytes, headers, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Void> uploadAccountImage(MultipartFile file,
												   AccountImageUploadRequest request, 
												   @AuthenticationPrincipal LoginDetails loginDetails) 
														   throws IOException {
		if (file == null) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		
		accountImageService.uploadAccountImage(file, loginDetails.getId(), request);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteAccountImage(@AuthenticationPrincipal LoginDetails loginDetails) {
		accountImageService.deleteAccountImage(loginDetails.getId());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ExceptionHandler({IllegalArgumentException.class})
	protected ResponseEntity<String> handleBadRequest(Exception e) {
		log.warn(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<String> handleException(Exception e) {
		log.warn(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
