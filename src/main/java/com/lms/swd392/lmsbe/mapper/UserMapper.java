package com.lms.swd392.lmsbe.mapper;

import com.lms.swd392.lmsbe.entity.User;
import com.lms.swd392.lmsbe.model.request.RegisterRequest;
import com.lms.swd392.lmsbe.model.response.RegisterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    User toUser(RegisterRequest request);

    RegisterResponse toRegisterResponse(User user);
}
