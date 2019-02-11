package pl.ark.chr.buginator.auth.password.reset;

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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PasswordResetResource.class, secure = false)
public class PasswordResetResourceIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private PasswordResetService passwordResetService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("should correctly send password reset token for given email")
    public void shouldCorrectlySendPasswordResetToken() throws Exception {
        //given
        String email = "testEmail@gmail.com";

        //when
        mockMvc.perform(post("/api/auth/password/reset?email=" + email)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        //then
        verify(passwordResetService, times(1)).sendPasswordResetEmail(eq(email));
    }

    @Test
    @DisplayName("should return HttpStatus.BAD_REQUEST when no email passed")
    public void shouldReturn400StatusWhenNoEmailPassed() throws Exception {
        //given
        String email = "testEmail@gmail.com";

        //when
        mockMvc.perform(post("/api/auth/password/reset")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());

        //then
        verify(passwordResetService, never()).sendPasswordResetEmail(eq(email));
    }

    @Test
    @DisplayName("should change password correctly")
    public void shouldChangePasswordCorrectly() throws Exception {
        //given
        String passwordResetDTO = new String(Files.readAllBytes(Paths.get(getClass()
                .getResource("/json/passwordReset.json").toURI())));

        //when
        mockMvc.perform(post("/api/auth/password/change")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(passwordResetDTO))
                .andExpect(status().isOk());

        //then
        verify(passwordResetService, times(1)).changePassword(any(PasswordResetDTO.class));
    }

    @Test
    @DisplayName("should return validation error when request body misse required fields")
    public void shouldReturnValidationError() throws Exception {
        //given
        String passwordResetDTO = new String(Files.readAllBytes(Paths.get(getClass()
                .getResource("/json/empty.json").toURI())));

        //when
        String errorResult = mockMvc.perform(post("/api/auth/password/change")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(passwordResetDTO))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        verify(passwordResetService, never()).changePassword(any(PasswordResetDTO.class));
        assertThat(errorResult)
                .contains("user.password.reset.token")
                .contains("user.password.blank");
    }
}
