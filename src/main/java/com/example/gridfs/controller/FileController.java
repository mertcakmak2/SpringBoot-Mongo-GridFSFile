package com.example.gridfs.controller;

import com.example.gridfs.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(path = "")
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return fileService.uploadFile(multipartFile);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ByteArrayResource> displayFile(@PathVariable String id) throws IOException {
        var fileResponse = fileService.displayFile(id);
        var bytes = fileResponse.getBytes();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileResponse.getContentType()))
                .contentLength(bytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileResponse.getFileName() + "\"")
                .body(new ByteArrayResource(bytes));
    }

    @GetMapping(path = "/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String id) throws IOException {
        var fileResponse = fileService.displayFile(id);
        var bytes = fileResponse.getBytes();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileResponse.getContentType()))
                .contentLength(bytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResponse.getFileName() + "\"")
                .body(new ByteArrayResource(bytes));
    }
}
