package com.incon.backend.mapper;

import com.incon.backend.dto.request.CompanyRequest;
import com.incon.backend.dto.response.CompanyResponse;
import com.incon.backend.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(target = "companyId", ignore = true)
    @Mapping(target = "companyIsVerified", ignore = true)
    @Mapping(target = "companyVerificationStatus", ignore = true)
    @Mapping(target = "companyUser", ignore = true)
    @Mapping(target = "companyCreatedAt", ignore = true)
    @Mapping(target = "companyUpdatedAt", ignore = true)
    Company toCompany(CompanyRequest companyRequest);

    @Mapping(source = "companyUser.userId", target = "userId")
    CompanyResponse toCompanyResponse(Company company);

    @Mapping(target = "companyId", ignore = true)
    @Mapping(target = "companyIsVerified", ignore = true)
    @Mapping(target = "companyVerificationStatus", ignore = true)
    @Mapping(target = "companyUser", ignore = true)
    @Mapping(target = "companyCreatedAt", ignore = true)
    @Mapping(target = "companyUpdatedAt", ignore = true)
    void updateCompanyFromRequest(CompanyRequest companyRequest, @MappingTarget Company company);
}

