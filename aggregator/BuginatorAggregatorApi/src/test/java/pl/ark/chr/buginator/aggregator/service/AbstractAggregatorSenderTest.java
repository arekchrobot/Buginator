package pl.ark.chr.buginator.aggregator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.ark.chr.buginator.aggregator.domain.Aggregator;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.domain.core.ErrorStatus;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class AbstractAggregatorSenderTest {

    private AbstractAggregatorSender sut;
    private Aggregator aggregator;
    private Error error;

    @BeforeEach
    public void setUp() throws Exception {
        sut = Mockito.spy(AbstractAggregatorSender.class);
        Application application = Mockito.mock(Application.class);
        aggregator = new Aggregator("testCLass", application);
        error = Error
                .builder("title", ErrorSeverity.ERROR, ErrorStatus.CREATED, "2018-09-10 22:13:44", application)
                .build();
    }

    @Test
    @DisplayName("should successfully execute sendData()")
    public void testSendDataSuccess() {
        //given
        aggregator.setCount(1);
        aggregator.setErrorSeverity(ErrorSeverity.ERROR);

        //when
        sut.notifyExternalAggregator(aggregator, error);

        //then
        verify(sut).sendData(eq(aggregator), eq(error));
    }

    @Test
    @DisplayName("should not execute sendData() since aggregator count is greater than error count")
    public void testSendDataCountLessThan() {
        //given
        aggregator.setCount(5);
        aggregator.setErrorSeverity(ErrorSeverity.ERROR);

        //when
        sut.notifyExternalAggregator(aggregator, error);

        //then
        verify(sut, never()).sendData(eq(aggregator), eq(error));
    }

    @Test
    @DisplayName("should not execute sendData() since error severities do not match")
    public void testSendDataSeverityNotMatch() {
        //given
        aggregator.setCount(5);
        aggregator.setErrorSeverity(ErrorSeverity.CRITICAL);

        //when
        sut.notifyExternalAggregator(aggregator, error);

        //then
        verify(sut, never()).sendData(eq(aggregator), eq(error));
    }
}