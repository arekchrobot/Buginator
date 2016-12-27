package pl.ark.chr.buginator.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import pl.ark.chr.buginator.repository.ApplicationRepository;
import pl.ark.chr.buginator.repository.ErrorRepository;
import pl.ark.chr.buginator.service.ChartService;

import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2016-12-26.
 */
@RunWith(MockitoJUnitRunner.class)
public class ChartServiceImplTest {

    private static final String TEST_MESSAGE_SOURCE_RETURN = "TEST INFO";

    @InjectMocks
    private ChartService sut = new ChartServiceImpl();

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ErrorRepository errorRepository;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() throws Exception {
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class))).thenReturn(TEST_MESSAGE_SOURCE_RETURN);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGenerateLastWeekErrorsForApplication__ProperValues() {
        fail("IMPLEMENT THIS YOU IDIOT");
    }
}