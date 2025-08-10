package com.example.template.service.impl;

import com.example.template.dto.mapper.FileMapper;
import com.example.template.dto.mapper.UserMapper;
import com.example.template.dto.response.DashboardResponse;
import com.example.template.dto.response.FileStatsResponse;
import com.example.template.dto.response.UserResponse;
import com.example.template.entity.ChatMessage;
import com.example.template.entity.User;
import com.example.template.repository.ChatMessageRepository;
import com.example.template.repository.FileRepository;
import com.example.template.repository.UserRepository;
import com.example.template.service.AdminService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)


public class AdminServiceImpl implements AdminService {
    FileRepository fileRepository;
    UserRepository userRepository;
    ChatMessageRepository chatMessageRepository;
    FileMapper fileMapper;
    UserMapper userMapper;

    private static final List<String> CATEGORIES = List.of(
            "Luật Dân sự",
            "Luật Hình sự",
            "Luật Lao động",
            "Luật Đất đai",
            "Luật Thương mại",
            "Luật Hành chính",
            "Luật Khoa học, công nghệ"
    );

    @Override
    public DashboardResponse dashboard() {
        List<Object[]> rows = fileRepository.countFilesByCategory();

        Map<String, Integer> dbMap = rows.stream()
                .collect(Collectors.toMap(
                        r -> (String) r[0],
                        r -> {
                            Number n = (Number) r[1];
                            return n.intValue();
                        }
                ));

        return DashboardResponse.builder()
                .messageStats(getLast7DaysStats())
                .fileStats(CATEGORIES.stream()
                        .map(cat -> DashboardResponse.FileStats.builder()
                                .category(cat)
                                .count(dbMap.getOrDefault(cat, 0))
                                .build())
                        .collect(Collectors.toList()))
                .totalFiles(fileRepository.countByDeletedFalse())
                .totalMessages(chatMessageRepository.countByRole(ChatMessage.MessageRole.BOT))
                .totalUsers(userRepository.countByRole("USER"))
                .build();
    }

    @Override
    public List<FileStatsResponse> getAllFiles() {
        return fileRepository.findAllOrderByDeleted().stream().map(fileMapper::toFileStatsResponse).collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAllOrderByLocked().stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    @Override
    public void lock(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setIsLocked(!user.getIsLocked());
        userRepository.save(user);
    }

    public List<DashboardResponse.MessageStats> getLast7DaysStats() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);

        List<Object[]> rows = chatMessageRepository.countMessagesByDate(start.atStartOfDay());

        Map<LocalDate, Integer> dbMap = rows.stream()
                .collect(Collectors.toMap(
                        r -> ((java.sql.Date) r[0]).toLocalDate(),
                        r -> ((Number) r[1]).intValue()
                ));

        return IntStream.rangeClosed(0, 6)
                .mapToObj(i -> {
                    LocalDate date = start.plusDays(i);
                    return DashboardResponse.MessageStats.builder()
                            .date(date.atStartOfDay())
                            .count(dbMap.getOrDefault(date, 0))
                            .build();
                })
                .collect(Collectors.toList());
    }

}
