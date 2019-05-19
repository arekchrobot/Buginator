package pl.ark.chr.buginator.app.application;

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
import pl.ark.chr.buginator.app.error.ErrorDTO;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ApplicationResource.class, secure = false)
class ApplicationResourceIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ApplicationService applicationService;

    private MockMvc mockMvc;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("should correctly create application based on given json request")
    void shouldCorrectlyCreateApplication() throws Exception {
        //given
        String applicationRequestDTO = new String(Files.readAllBytes(Paths.get(getClass()
                .getResource("/json/createApplication.json").toURI())));

        //when
        mockMvc.perform(post("/api/application")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(applicationRequestDTO))
                .andExpect(status().isCreated());

        //then
        verify(applicationService, times(1)).create(any(ApplicationRequestDTO.class));
    }

    @Test
    @DisplayName("should return validation error when request body misses required fields")
    void shouldReturnValidationError() throws Exception {
        //given
        String registerDTO = new String(Files.readAllBytes(Paths.get(getClass()
                .getResource("/json/empty.json").toURI())));

        //when
        String errorResult = mockMvc.perform(post("/api/application")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(registerDTO))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        verify(applicationService, never()).create(any(ApplicationRequestDTO.class));
        assertThat(errorResult)
                .contains("application.name.blank");
    }

    @Test
    @DisplayName("should get all applications for logged user")
    void shouldGetUserApplications() throws Exception {
        //given
        var applicationDTO = ApplicationDTO.builder(UserApplicationDTO.builder().build())
                .id(1L)
                .name("TestApp")
                .modify(true)
                .allErrorCount(15)
                .lastWeekErrorCount(10)
                .build();
        var applicationDTO2 = ApplicationDTO.builder(UserApplicationDTO.builder().build())
                .id(2L)
                .name("TestApp2")
                .modify(false)
                .allErrorCount(5)
                .lastWeekErrorCount(1)
                .build();
        Set<ApplicationDTO> userApps = new LinkedHashSet<>();
        userApps.add(applicationDTO);
        userApps.add(applicationDTO2);
        doReturn(userApps).when(applicationService).getUserApps();

        //when
        String result = mockMvc.perform(get("/api/application/by-user")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResults =
                "[" +
                    "{" +
                        "\"id\":1," +
                        "\"name\":\"TestApp\"," +
                        "\"modify\":true," +
                        "\"allErrorCount\":15," +
                        "\"lastWeekErrorCount\":10" +
                    "}," +
                    "{" +
                        "\"id\":2," +
                        "\"name\":\"TestApp2\"," +
                        "\"modify\":false," +
                        "\"allErrorCount\":5," +
                        "\"lastWeekErrorCount\":1" +
                    "}" +
                "]";

        assertThat(result).isEqualTo(expectedResults);
    }

    @Test
    @DisplayName("should get application details by id")
    void shouldGetApplicationDetailsById() throws Exception {
        //given
        LocalDate now = LocalDate.now();
        var errorDTO = ErrorDTO.builder()
                .id(1L)
                .count(2)
                .description("error test")
                .title("TestError")
                .lastOccurrence(now)
                .severity("CRITICAL")
                .status("ONGOING")
                .build();

        Long appId = 1L;
        var applicationDetailsDTO = ApplicationDetailsDTO.builder(UserApplicationDTO.builder().build())
                .id(appId)
                .name("TestApp")
                .modify(true)
                .errors(List.of(errorDTO))
                .build();

        doReturn(applicationDetailsDTO).when(applicationService).get(eq(appId));

        String result = mockMvc.perform(get("/api/application/" + appId)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResult =
                "{" +
                    "\"id\":1," +
                    "\"name\":\"TestApp\"," +
                    "\"modify\":true," +
                    "\"errors\":[" +
                        "{" +
                            "\"id\":1," +
                            "\"title\":\"TestError\"," +
                            "\"description\":\"error test\"," +
                            "\"status\":\"ONGOING\"," +
                            "\"severity\":\"CRITICAL\"," +
                            "\"lastOccurrence\":\"" + now.format(DATE_FORMATTER) + "\"," +
                            "\"count\":2" +
                        "}" +
                    "]" +
                "}";

        assertThat(result).isEqualTo(expectedResult);
    }
}