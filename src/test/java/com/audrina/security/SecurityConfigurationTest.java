package com.audrina.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SecurityApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityConfigurationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void deleteProductWithoutAuthenticationShouldReturn401() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatedUserAccessAdminEndpointShouldReturn403() throws Exception {
        mockMvc.perform(delete("/products/1")
                        .with(user("testUser").roles("USER"))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanAccessAdminEndpointAndShouldReturn200() throws Exception {
        mockMvc.perform(delete("/products/1")
                        .with(user("testUser").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void customerRegistrationSuccessShouldReturn201() throws Exception {
        mockMvc.perform(post("/customer")
                .contentType("application/json")
                .content(
                        """
                                {
                                  "firstName": "Test",
                                  "lastName": "User",
                                  "email": "testuser@example.com",
                                  "password": "Password123!"
                                }
                                """
                )
        ).andExpect(status().isCreated());

    }

    @Test
    void customerRegistrationFailedShouldReturn400() throws Exception {
        mockMvc.perform(post("/customer")
                .contentType("application/json")
                .content(
                        """
                                {
                                  "firstName": "Test",
                                  "lastName": "User",
                                  "password": "Password123!"
                                }
                                """
                )
        ).andExpect(status().isBadRequest());

    }

}
