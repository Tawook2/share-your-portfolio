package com.spring.api.global.util.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileUtils {

	public String saveCroppedImageFile(MultipartFile file, String rootPath, CropRequest cropRequest) throws IOException {
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		String savedPath = makeCurrentDayPath(rootPath);
		
		File target = new File(savedPath, fileName);
		BufferedImage src = ImageIO.read(file.getInputStream());
		
		BufferedImage croppedImage = Scalr.crop(src, cropRequest.getLeft(), cropRequest.getTop(), cropRequest.getWidth(), cropRequest.getHeight());
		BufferedImage savedImage = Scalr.resize(croppedImage, cropRequest.getSize());
		
		ImageIO.write(savedImage, getFileExtension(fileName), target);
		log.info("File Upload Success");
		return savedPath.substring(rootPath.length()) + File.separator + fileName;
	}
	
	public void deleteFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			log.info("File Not Exist");
			return;
		}
		
		while (file.exists()) {
			file.delete();
		}
		log.info("File Delete Success");
	}
	
	private String makeCurrentDayPath(String rootPath) {
		Calendar today = Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		int month = today.get(Calendar.MONTH)+1;
		int day = today.get(Calendar.DATE);
		
		return makeDir(rootPath, 
					   String.valueOf(year), 
					   String.format("%02d", month), 
					   String.format("%02d", day));
	}
	
	private String makeDir(String rootPath, String... paths) {
		StringBuilder sb = new StringBuilder(rootPath);
		for (String path : paths) {
			sb.append(File.separator + path);
			File file = new File(sb.toString());
			if (!file.exists()) {
				file.mkdir();
			}
		}
		return sb.toString();
	}
	
	public String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".")+1).toUpperCase();
	}
	
}
