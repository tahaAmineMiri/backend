package com.incon.backend.mapper;

import com.incon.backend.dto.request.UserRequest;
import com.incon.backend.dto.response.UserResponse;
import com.incon.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "userEmail", target = "userEmail")
    @Mapping(source = "userFullName", target = "userFullName")
    @Mapping(source = "userPosition", target = "userPosition")
    @Mapping(source = "userBusinessPhone", target = "userBusinessPhone")
    @Mapping(source = "userIsVerified", target = "userIsVerified")
    UserResponse toUserResponse(User user);
}