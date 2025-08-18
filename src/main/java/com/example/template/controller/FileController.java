package com.example.template.controller;


import com.example.template.dto.ApiResponse;
import com.example.template.dto.request.FileCreationRequest;
import com.example.template.dto.request.FileSearchRequest;
import com.example.template.dto.request.FileUpdateRequest;
import com.example.template.dto.response.AuthenticationResponse;
import com.example.template.dto.response.FileResponse;
import com.example.template.service.FileService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService fileService;
    @PostMapping("/upload")
    ResponseEntity<ApiResponse<Void>> uploadFile(@ModelAttribute FileCreationRequest request) throws IOException {
        fileService.saveFile(request);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Upload file success")
                .data(null)
                .build();
        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<FileResponse>>> searchFiles(@RequestBody FileSearchRequest request) {
        Page<FileResponse> result = fileService.getFiles(request);
        ApiResponse<Page<FileResponse>> apiResponse = ApiResponse.<Page<FileResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Search files successfully")
                .data(result)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@PathVariable String id) {
        fileService.deleteFile(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("File deleted successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FileResponse>> updateFile(
            @PathVariable String id,
            @RequestBody FileUpdateRequest request) {
        FileResponse updated = fileService.updateFile(id, request);
        ApiResponse<FileResponse> response = ApiResponse.<FileResponse>builder()
                .code(HttpStatus.OK.value())
                .message("File updated successfully")
                .data(updated)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view/{url}")
    public ResponseEntity<byte[]> getFile(@PathVariable String url) {
        byte[] fileData = fileService.getFile(url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("file.pdf").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileData);
    }
}
