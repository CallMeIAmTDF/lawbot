package com.example.template.service.call.impl;

import com.example.template.dto.call.request.ChatRequest;
import com.example.template.dto.call.request.FileUploadRequest;
import com.example.template.dto.call.response.ChatResponse;
import com.example.template.dto.call.response.FileUploadResponse;
import com.example.template.service.call.RAGService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RAGServiceImpl implements RAGService {
    private final WebClient ragWebClient;
    private RestTemplate restTemplate;

    @NonFinal
    @Value("${rag-service.host}")
    protected String RAG_SERVICE_BASE_HOST;

    @NonFinal
    @Value("${rag-service.protocol}")
    protected String RAG_SERVICE_BASE_PROTOCOL;

    @NonFinal
    @Value("${rag-service.port}")
    protected String RAG_SERVICE_BASE_PORT;

    @NonFinal
    @Value("${rag-service.path.chat}")
    protected String RAG_SERVICE_CHAT_PATH;

    @NonFinal
    @Value("${rag-service.path.upload}")
    protected String RAG_SERVICE_UPLOAD_PATH;

    @NonFinal
    @Value("${rag-service.path.file}")
    protected String RAG_SERVICE_FILE_PATH;


    //    @Override
//    public FileUploadResponse upload(FileUploadRequest request) throws IOException {
//        MultiValueMap<String, Object> formData = getStringObjectMultiValueMap(request);
//        WebClient.ResponseSpec responseSpec = ragWebClient.post()
//                .uri(RAG_SERVICE_UPLOAD_PATH)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(formData))
//                .retrieve();
//
//        return handleResponse(responseSpec, FileUploadResponse.class).block();
//    }
    @Override
    public FileUploadResponse upload(FileUploadRequest request) throws IOException {
        MultipartFile file = request.getFile();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // Tạo FileSystemResource từ MultipartFile
        Resource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        body.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<FileUploadResponse> response = restTemplate.exchange(
                RAG_SERVICE_BASE_PROTOCOL + "://" + RAG_SERVICE_BASE_HOST + ":" + RAG_SERVICE_BASE_PORT + RAG_SERVICE_UPLOAD_PATH,
                HttpMethod.POST,
                requestEntity,
                FileUploadResponse.class
        );

        return response.getBody();
    }

    private byte[] getBytes(MultipartFile file) throws BadRequestException {
        try {
            return file.getBytes();
        } catch (Exception e) {
            throw new BadRequestException("Không đọc được dữ liệu file: " + e.getMessage());
        }
    }
    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        WebClient.ResponseSpec responseSpec = ragWebClient.post()
                .uri(RAG_SERVICE_CHAT_PATH)
                .bodyValue(request)
                .retrieve();

        return handleResponse(responseSpec, ChatResponse.class).block();
    }

    @Override
    public byte[] getFile(String url) {
        WebClient.ResponseSpec responseSpec = ragWebClient.get()
                .uri(RAG_SERVICE_FILE_PATH + url)
                .accept(MediaType.APPLICATION_PDF)
                .retrieve();

        return handleResponse(responseSpec, byte[].class).block();
    }

    private <T> Mono<T> handleResponse(WebClient.ResponseSpec responseSpec, Class<T> clazz) {
//        return responseSpec
//                .toEntity(String.class) // lấy cả body và status
//                .doOnNext(entity -> {
//                    System.out.println("Status: " + entity.getStatusCode());
//                    System.out.println("Headers: " + entity.getHeaders());
//                    System.out.println("Body: " + entity.getBody());
//                })
//                .flatMap(entity -> {
//                    try {
//                        ObjectMapper mapper = new ObjectMapper();
//                        return Mono.just(mapper.readValue(entity.getBody(), clazz));
//                    } catch (Exception e) {
//                        return Mono.error(e);
//                    }
//                });
        return responseSpec
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BadRequestException("Client error: " + body))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BadRequestException("Server error: " + body))
                )
                .bodyToMono(clazz);
    }

    private static MultiValueMap<String, Object> getStringObjectMultiValueMap(FileUploadRequest request) throws IOException {
        MultipartFile file = request.getFile();
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File không hợp lệ");
        }
        ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename(); // Đảm bảo filename được gửi
            }
        };

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("file", fileAsResource);

        return formData;
    }
}
