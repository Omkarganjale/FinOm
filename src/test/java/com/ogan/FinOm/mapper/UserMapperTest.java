package com.ogan.FinOm.mapper;

import com.ogan.FinOm.dto.requests.UserRequest;
import com.ogan.FinOm.entity.User;
import com.ogan.FinOm.utils.AccountUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockedStatic;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void toEntity_shouldMapUserRequestToUser() {

        UserRequest userRequest = UserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .gender("Male")
                .address("123 Main St")
                .stateOfOrigin("California")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .build();

        try (MockedStatic<AccountUtils> mockedStatic = mockStatic(AccountUtils.class)) {
            mockedStatic.when(AccountUtils::generateAccountNumber).thenReturn("123456789012");

            // Act
            User user = userMapper.toEntity(userRequest);

            // Assert
            assertNotNull(user);
            assertEquals(userRequest.getFirstName(), user.getFirstName());
            assertEquals(userRequest.getLastName(), user.getLastName());
            assertEquals(userRequest.getGender(), user.getGender());
            assertEquals(userRequest.getAddress(), user.getAddress());
            assertEquals(userRequest.getStateOfOrigin(), user.getStateOfOrigin());
            assertEquals(userRequest.getEmail(), user.getEmail());
            assertEquals(userRequest.getPhoneNumber(), user.getPhoneNumber());
            assertEquals("123456789012", user.getAccountNumber());
            assertEquals(BigDecimal.ZERO, user.getAccountBalance());
            assertEquals("ACTIVE", user.getStatus());

            // Verify the static method was called
            mockedStatic.verify(AccountUtils::generateAccountNumber, times(1));
        }
    }
}
