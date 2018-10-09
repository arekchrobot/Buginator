package pl.ark.chr.buginator.aggregator.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.ark.chr.buginator.domain.Aggregator;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.Error;
import pl.ark.chr.buginator.domain.enums.ErrorSeverity;
import pl.ark.chr.buginator.domain.enums.ErrorStatus;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class AbstractAggregatorSenderTest {

    private AbstractAggregatorSender sut;
    private Aggregator aggregator;
    private Error error;

    @Before
    public void setUp() throws Exception {
        sut = Mockito.spy(AbstractAggregatorSender.class);
        Application application = Mockito.mock(Application.class);
        aggregator = new Aggregator("testCLass", application);
        error = Error
                .builder("title", ErrorSeverity.ERROR, ErrorStatus.CREATED, "2018-09-10 22:13:44", application)
                .build();
    }

    @Test
    public void testSendData__Success() {
        //given
        aggregator.setCount(1);
        aggregator.setErrorSeverity(ErrorSeverity.ERROR);

        //when
        sut.notifyExternalAggregator(aggregator, error);

        //then
        verify(sut).sendData(eq(aggregator), eq(error));
    }

    @Test
    public void testSendData__CountLessThan() {
        //given
        aggregator.setCount(5);
        aggregator.setErrorSeverity(ErrorSeverity.ERROR);

        //when
        sut.notifyExternalAggregator(aggregator, error);

        //then
        verify(sut, never()).sendData(eq(aggregator), eq(error));
    }

    @Test
    public void testSendData__SeverityNotMatch() {
        //given
        aggregator.setCount(5);
        aggregator.setErrorSeverity(ErrorSeverity.CRITICAL);

        //when
        sut.notifyExternalAggregator(aggregator, error);

        //then
        verify(sut, never()).sendData(eq(aggregator), eq(error));
    }
}