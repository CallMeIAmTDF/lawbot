package com.example.template.dto.mapper;

import com.example.template.dto.response.UserResponse;
import com.example.template.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper {

    public UserResponse toUserResponse(User user) {

        return UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .avatar(user.getAvatar())
                        .email(user.getEmail())
                        .noPassword(!StringUtils.hasText(user.getPassword()))
                        .isLocked(user.getIsLocked())
                        .build();
    }
}