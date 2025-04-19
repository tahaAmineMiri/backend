package com.incon.backend.mapper;

import com.incon.backend.dto.request.UserRequest;
import com.incon.backend.dto.response.UserResponse;
import com.incon.backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}