package com.example.template.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public class FileTypeDetector {
    public static String detectFileType(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[8];
            int bytesRead = is.read(header);
            if (bytesRead < 4) {
                return "UNKNOWN";
            }
            if (header[0] == 0x25 && header[1] == 0x50 && header[2] == 0x44 && header[3] == 0x46) {
                return "PDF";
            }
//            if ((header[0] & 0xFF) == 0xD0 && (header[1] & 0xFF) == 0xCF &&
//                (header[2] & 0xFF) == 0x11 && (header[3] & 0xFF) == 0xE0) {
//                return "DOC";
//            }
//            if ((header[0] & 0xFF) == 0x50 && (header[1] & 0xFF) == 0x4B &&
//                (header[2] & 0xFF) == 0x03 && (header[3] & 0xFF) == 0x04) {
//                return "DOCX";
//            }
            return "UNKNOWN";
        } catch (Exception e) {
            return "ERROR";
        }
    }
}