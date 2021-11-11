package com.spring.api.domain.upload.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import com.spring.api.domain.model.Post;
import com.spring.api.domain.upload.dto.AttachFileDTO;
import com.spring.api.domain.upload.mapper.ManagePostMapper;
import com.spring.api.domain.upload.service.UploadService;

import lombok.extern.log4j.Log4j;


@Log4j
@Service
public class UploadServiceImpl implements UploadService{
	
	@Autowired
	ManagePostMapper mapper;
	
	@Value("${image.path.root}")
	private String root;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public void createPdf(Post post, AttachFileDTO attachDTO) throws Exception {
		
		MultipartFile file = post.getFile();
		
		UUID fileUuid = UUID.randomUUID();
		
		String gf = getFolder(post, attachDTO, file, fileUuid);
		
		String FolderPath = root + File.separator + gf;
		
		pdfToImg(attachDTO, post, FolderPath, fileUuid);
			
		try {
			mapper.uploadFiles(attachDTO);
			
			int fileNo = attachDTO.getFileNo();
			
			post.setFileNo(fileNo);
			
			mapper.savePosts(post);
					
		} catch (Exception e) {
			e.printStackTrace();
			
			deleteFile(FolderPath);
			
			throw new Exception();
		}
			
	}
	
	public AttachFileDTO pdfToImg(AttachFileDTO attachDTO, Post post, String FolderPath, UUID fileUuid) {
		
		MultipartFile file = post.getFile();
		
		List<AttachFileDTO> dto = new ArrayList<>();
			
		attachDTO.setUuid(fileUuid.toString());
		
		try {
			
			PDDocument pdfDoc = PDDocument.load(file.getInputStream());
			PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);
			
			String testCategory = post.getCategory();
			
			attachDTO.setFileCategory(testCategory);
			
			attachDTO.setUploaderId(post.getWriterId());
			
			attachDTO.setOriginalFileName(file.getOriginalFilename());
			
			attachDTO.setSavedFileName(fileUuid.toString() + file.getOriginalFilename());
			
			attachDTO.setUploadPath(FolderPath);

			attachDTO.setFileSize(file.getSize());
						
			Files.createDirectories(Paths.get(FolderPath));
			
			int numOfPage = pdfDoc.getNumberOfPages();
					
			post.setNumOfPage(numOfPage);
			
			for(int i=0; i < pdfDoc.getPages().getCount(); i++) {
															
				String imgFileName = FolderPath + File.separator + i + ".png";
				
				BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);
				
				ImageIOUtil.writeImage(bim, imgFileName, 300);
				
				dto.add(attachDTO);
									
			}
			
			log.info("convert pdf to img successfully");
			
			pdfDoc.close();
			
		} catch (InvalidPasswordException e) {

			e.printStackTrace();
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return attachDTO;
	}

	public String getFolder(Post post, AttachFileDTO attachDTO, MultipartFile file, UUID fileUuid) {
		
		int uploader = post.getWriterId();
		
		String testCategory = post.getCategory();
		
		String fileName = fileUuid + "-" + FilenameUtils.getBaseName(file.getOriginalFilename());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		
		Date today = new Date();
					
		String str = sdf.format(today) + "/" + testCategory + "/" + uploader + "/" + fileName;
				
		return str.replace("/", File.separator);
	}
	
	public void deleteFile(String FolderPath) {
		
		File deleteFolder = new File(FolderPath);
		
		if(deleteFolder.exists()) {
			File[] deleteFolderList = deleteFolder.listFiles();
			
			for(int i = 0; i < deleteFolderList.length; i++) {
				if(deleteFolderList[i].isFile()) {
					deleteFolderList[i].delete();
				}else {
					deleteFile(deleteFolderList[i].getPath());
				}
				deleteFolderList[i].delete();
			}
			deleteFolder.delete();
		}
			
		log.info("delete an inappropriate file");
	}
	
	@Override
	public boolean isAllowedMIMEType(String mimeType) {
		
		if(mimeType == null || mimeType.equals("")) return false;
		
		String allowedMIMETypesEquals = "application/pdf";
		
		log.info("MimeType : " + mimeType);
		
		if(mimeType.equals(allowedMIMETypesEquals)) return true;
		
		return false;
	}

	@Override
	public List<Post> bringPostList() {
		
		return mapper.bringPostList();
	}

	@Override
	public int searchFileNo(Post post) {
		
		return mapper.searchFileNo(post);
	}

	@Override
	public String searchFilePath(int fileNo) {
		
		return mapper.searchFilePath(fileNo);
	}

	@Override
	public Post getPost(int id) {
		return mapper.findById(id);
	}

}
