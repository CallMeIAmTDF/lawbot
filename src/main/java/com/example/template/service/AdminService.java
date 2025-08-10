package com.example.template.service;

import com.example.template.dto.request.FileSearchRequest;
import com.example.template.dto.response.DashboardResponse;
import com.example.template.dto.response.FileStatsResponse;
import com.example.template.dto.response.UserResponse;

import java.util.List;

public interface AdminService {
    DashboardResponse dashboard();
    List<FileStatsResponse> getAllFiles();
    List<UserResponse> getAllUsers();
    void lock(String userId);
}
