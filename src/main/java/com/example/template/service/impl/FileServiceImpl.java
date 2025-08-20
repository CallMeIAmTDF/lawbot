package com.example.template.service.impl;

import com.example.template.advice.exception.NotFoundException;
import com.example.template.dto.call.request.FileUploadRequest;
import com.example.template.dto.call.response.FileUploadResponse;
import com.example.template.dto.mapper.FileMapper;
import com.example.template.dto.request.FileCreationRequest;
import com.example.template.dto.request.FileSearchRequest;
import com.example.template.dto.request.FileUpdateRequest;
import com.example.template.dto.response.FileResponse;
import com.example.template.entity.File;
import com.example.template.repository.FileRepository;
import com.example.template.service.FileService;
import com.example.template.service.call.RAGService;
import com.example.template.specification.FileSpecification;
import com.example.template.util.FileTypeDetector;
import com.example.template.util.RandomStringUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileServiceImpl implements FileService {

    RAGService ragService;
    FileRepository fileRepository;
    FileMapper fileMapper;

    @Override
    @CacheEvict(value = "shortCache", allEntries = true)
    public void saveFile(FileCreationRequest request) throws IOException {

        if(!FileTypeDetector.detectFileType(request.getFile()).equals("PDF")){
            throw new BadRequestException("File Not Supported");
        }

        File file = File.builder()
                .name(request.getName())
                .id(RandomStringUtil.generateRandomString(19))
                .fileType(File.FileType.PDF)
                .category(request.getCategory())
                .description(request.getDescription())
                .deleted(false)
                .build();

        FileUploadResponse response = ragService.upload(FileUploadRequest.builder().file(request.getFile()).build());

        file.setSize(response.getSize());
        file.setUrl(response.getHash() + ".pdf");

        fileRepository.save(file);
    }

    @Override
    @Cacheable(value = "shortCache", key = "#request.hashCode()")
    public Page<FileResponse> getFiles(FileSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());
        Specification<File> spec = FileSpecification.search(request);
        Page<File> filePage = fileRepository.findAll(spec, pageable);
        return filePage.map(fileMapper::toFileResponse);
    }

    @Override
    @CacheEvict(value = "shortCache", allEntries = true)
    public void deleteFile(String id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("File not found"));
        file.setDeleted(!file.getDeleted());
        fileRepository.save(file);
    }

    @Override
    @CacheEvict(value = "shortCache", allEntries = true)
    public FileResponse updateFile(String id, FileUpdateRequest request) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("File not found"));

        file.setName(request.getName());
        file.setDescription(request.getDescription());
        file.setCategory(request.getCategory());

        File updated = fileRepository.save(file);
        return fileMapper.toFileResponse(updated);
    }

    @Override
    @Cacheable(value = "eternalCache", key = "#url")
    public byte[] getFile(String url) {
        fileRepository.findFirstByUrl(url).orElseThrow(() -> new NotFoundException("File not found"));
        return ragService.getFile(url);
    }
}
