package com.ogan.FinOm.mapper;

import com.ogan.FinOm.dto.requests.UserRequest;
import com.ogan.FinOm.entity.User;
import com.ogan.FinOm.enums.Role;
import org.mapstruct.*;

import java.math.BigDecimal;

import static com.ogan.FinOm.utils.AccountUtils.generateAccountNumber;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest userRequest);

    @AfterMapping
    default void addUserDetails(@MappingTarget User user, UserRequest userRequest){
        user.setAccountNumber(generateAccountNumber());
        user.setAccountBalance(BigDecimal.ZERO);
        user.setStatus("ACTIVE");
        user.setRole(Role.ROLE_USER);
    }

}
