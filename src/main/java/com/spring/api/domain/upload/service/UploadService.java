package com.spring.api.domain.upload.service;


import java.util.List;
import com.spring.api.domain.model.Post;
import com.spring.api.domain.upload.dto.AttachFileDTO;

public interface UploadService {

	public void createPdf(Post post, AttachFileDTO attachDTO) throws Exception;
		
	public List<Post> bringPostList();
	
	public int searchFileNo(Post post);
	
	public String searchFilePath(int fileNo);
	
	public boolean isAllowedMIMEType(String mimeType);
	
	public Post getPost(int id);
}
