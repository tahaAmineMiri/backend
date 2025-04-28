package com.incon.backend.mapper;

import com.incon.backend.dto.request.AdminRequest;
import com.incon.backend.dto.response.AdminResponse;
import com.incon.backend.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(target = "adminId", ignore = true)
    @Mapping(target = "adminCreatedAt", ignore = true)
    @Mapping(target = "adminUpdatedAt", ignore = true)
    @Mapping(target = "adminRole", constant = "ADMIN")
    Admin toAdmin(AdminRequest adminRequest);

    AdminResponse toAdminResponse(Admin admin);
}