package com.ogan.FinOm.mapper;

import com.ogan.FinOm.dto.requests.UserRequest;
import com.ogan.FinOm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testAfterMapping() {
        UserRequest request = new UserRequest();
        request.setFirstName("John");
        request.setEmail("john.doe@example.com");
        request.setLastName("Doe");

        User user = userMapper.toEntity(request);
        assertEquals(request.getEmail(),user.getEmail());
        assertNotNull(user.getAccountNumber());
        assertEquals(BigDecimal.ZERO, user.getAccountBalance());
        assertEquals("ACTIVE", user.getStatus());


    }
}

