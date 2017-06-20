package com.markkryzh.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
public class RestUploadController {
	// Save the uploaded file to this folder
	public final static String UPLOADED_FOLDER = "F://";

	// Multiple file upload
	@PostMapping("/api/upload/multi")
	public ResponseEntity<?> uploadFileMulti(
			@RequestParam(value = "pollQuestionsImages", required = false) MultipartFile[] pollQuestionsImages,
			@RequestParam(value = "pollAnswersImages", required = false) MultipartFile[] pollAnswersImages,
			@RequestParam(value = "pollId", required = true) Integer pollId) {

		String uploadedFileName = Arrays.stream(pollQuestionsImages).map(x -> x.getOriginalFilename())
				.filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

		try {
			if (pollQuestionsImages != null)
				saveUploadedFiles(pollQuestionsImages);
			if (pollAnswersImages != null)
				saveUploadedFiles(pollAnswersImages);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity("Successfully uploaded - " + uploadedFileName, HttpStatus.OK);

	}

	// save file
	private void saveUploadedFiles(MultipartFile[] files) throws IOException {
		for (int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			if (file.isEmpty()) {
				continue;
			}
			byte[] bytes = file.getBytes();
			Path fullPath = Paths.get(UPLOADED_FOLDER+ file.getOriginalFilename());
			Files.write(fullPath, bytes);
		}
	}
}
