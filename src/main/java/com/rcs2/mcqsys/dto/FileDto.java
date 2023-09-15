package com.rcs2.mcqsys.dto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FileDto implements MultipartFile {
	 private transient MultipartFile file;

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public String getOriginalFilename() {
		return file.getOriginalFilename();
	}

	@Override
	public String getContentType() {
		return file.getContentType();
	}

	@Override
	public boolean isEmpty() {
		return file.isEmpty();
	}

	@Override
	public long getSize() {
		return file.getSize();
	}

	@Override
	public byte[] getBytes() throws IOException {
		return file.getBytes();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return file.getInputStream();
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		// TODO Auto-generated method stub
		
	}
}
