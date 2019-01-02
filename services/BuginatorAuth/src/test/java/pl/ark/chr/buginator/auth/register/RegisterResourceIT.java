package pl.ark.chr.buginator.auth.register;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RegisterResource.class, secure = false)
public class RegisterResourceIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private RegisterService registerService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("should correctly register company and user based on given json request")
    public void shouldCorrectlyRegister() throws Exception {
        //given
        String registerDTO = new String(Files.readAllBytes(Paths.get(getClass()
                .getResource("/json/register.json").toURI())));

        //when
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(registerDTO))
                .andExpect(status().isCreated());

        //then
        verify(registerService, times(1)).registerCompanyAndUser(any(RegisterDTO.class));
    }

    @Test
    @DisplayName("should return validation error when request body misse required fields")
    public void shouldReturnValidationError() throws Exception {
        //given
        String registerDTO = new String(Files.readAllBytes(Paths.get(getClass()
                .getResource("/json/empty.json").toURI())));

        //when
        String errorResult = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(registerDTO))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        verify(registerService, never()).registerCompanyAndUser(any(RegisterDTO.class));
        assertThat(errorResult)
                .contains("user.password.blank")
                .contains("company.name.blank")
                .contains("user.email.blank");
    }
}