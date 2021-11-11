package com.spring.api.domain.upload.mapper;

import java.util.List;

import com.spring.api.domain.model.Post;
import com.spring.api.domain.upload.dto.AttachFileDTO;

public interface ManagePostMapper {

	public void uploadFiles(AttachFileDTO attachDTO) throws Exception;
	
	public void savePosts(Post post) throws Exception;
	
	public List<Post> bringPostList();
	
	public int searchFileNo(Post post);
	
	public String searchFilePath(int fileNo);
	
	public Post findById(int id);
}
