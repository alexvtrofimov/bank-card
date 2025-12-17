package com.example.bankcards.controller;

import com.example.bankcards.dto.SignUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(new ExceptionApiHandler())
                .build();
    }

    @Test
    void testSignUpExistUser() throws Exception {
        SignUserDto userRequest = new SignUserDto("admin", "123");
        String stringSignDto = objectMapper.writeValueAsString(userRequest);

        mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringSignDto))
                .andExpect(content().string("User already exist"));
    }


    @Test
    void testSignInBadCredentials() throws Exception {
        SignUserDto userRequest = new SignUserDto("test", "123");
        String stringSignDto = objectMapper.writeValueAsString(userRequest);
        mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringSignDto))
                .andExpect(status().isUnauthorized());
    }
}