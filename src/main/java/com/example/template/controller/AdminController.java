package com.example.template.controller;


import com.example.template.dto.ApiResponse;
import com.example.template.dto.response.DashboardResponse;
import com.example.template.dto.response.FileStatsResponse;
import com.example.template.dto.response.UserResponse;
import com.example.template.service.AdminService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {
        DashboardResponse dashboard = adminService.dashboard();
        ApiResponse<DashboardResponse> response = ApiResponse.<DashboardResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get dashboard data successfully")
                .data(dashboard)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/files")
    public ResponseEntity<ApiResponse<List<FileStatsResponse>>> getAllFiles() {
        List<FileStatsResponse> files = adminService.getAllFiles();
        ApiResponse<List<FileStatsResponse>> response = ApiResponse.<List<FileStatsResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all files successfully")
                .data(files)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> files = adminService.getAllUsers();
        ApiResponse<List<UserResponse>> response = ApiResponse.<List<UserResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all users successfully")
                .data(files)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/users/{userId}/lock")
    public ResponseEntity<ApiResponse<Void>> lockUser(@PathVariable String userId) {
        adminService.lock(userId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("User locked successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
