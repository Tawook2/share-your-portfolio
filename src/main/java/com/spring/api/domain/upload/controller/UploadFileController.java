package com.spring.api.domain.upload.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.api.domain.model.Post;
import com.spring.api.domain.upload.dto.AttachFileDTO;
import com.spring.api.domain.upload.service.UploadService;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@CrossOrigin
public class UploadFileController {
	
	@Autowired
	UploadService service;
	
	@Value("${image.path.root}")
	private String root;
		
	@RequestMapping(value="/post/uploadFile", method = RequestMethod.POST)
	public ResponseEntity<?> uploadFiles(@ModelAttribute Post post){
						
		MultipartFile file = post.getFile();
		
		InputStream  inputStream;
		
		int user = post.getWriterId();
		
		String category = post.getCategory();
		String title = post.getTitle();
			
		AttachFileDTO attachDTO = new AttachFileDTO();
		
		if(user == 0 || (category == null || category.trim().isEmpty() || (title == null || title.trim().isEmpty()))) {
			return new ResponseEntity<>("fail", HttpStatus.NOT_FOUND);
		}
						
		else if(file.isEmpty() || file == null) {
			return new ResponseEntity<>("fail", HttpStatus.NOT_FOUND);		
		}
		
		else {		
			try {
				inputStream = file.getInputStream();
				
				Tika tika = new Tika();
				
				String mimeType = tika.detect(inputStream);
				
				if(!service.isAllowedMIMEType(mimeType)) {
					return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		try {
			service.createPdf(post, attachDTO);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			log.info("예외 발생" + e);
			
			return new ResponseEntity<>("fail", HttpStatus.CONFLICT);
		}		
						
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/post/postList")
	public List<Post> bringPostList(){
		
		List<Post> result = service.bringPostList();
		
		return result;	
	}
	
	@GetMapping("/images/{writerId}/{id}/{imageNo}")
	public ResponseEntity<byte[]> searchFileNo(@PathVariable int writerId, @PathVariable int id, @PathVariable int imageNo) throws Exception{
		
		InputStream in = null;
		
		ResponseEntity<byte[]> entity = null;
		
		HttpHeaders headers = new HttpHeaders();
		
		Post temPost = new Post();
		
		temPost.setWriterId(writerId);
		
		temPost.setId(id);
		
		int fileNo = service.searchFileNo(temPost);
		
		String filePath = service.searchFilePath(fileNo) + File.separator + imageNo + ".png";
				
		in = new FileInputStream(filePath);
		
		try {
			
			headers.setContentType(MediaType.IMAGE_PNG);
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
				
		} catch (IOException e) {
			
			e.printStackTrace();
			
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
			
		}finally {
			
			in.close();
		}
		return entity;
		
	}
	
	@GetMapping("/posts/{id}")
	public ResponseEntity<Post> getPost(@PathVariable int id) {
		Post post = service.getPost(id);
		return post != null ? new ResponseEntity<Post>(post, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
}
