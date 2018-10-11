package pl.ark.chr.buginator.aggregator.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.ark.chr.buginator.domain.aggregator.Aggregator;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.domain.core.ErrorStatus;

import static org.assertj.core.api.Assertions.assertThat;


public class AggregatorSenderValidatorTest {

    private Aggregator aggregator;
    private Error error;

    @Before
    public void setUp() throws Exception {
        Application application = Mockito.mock(Application.class);
        aggregator = new Aggregator("testCLass", application);
        error = Error
                .builder("title", ErrorSeverity.ERROR, ErrorStatus.CREATED, "2018-09-10 22:13:44", application)
                .build();
    }

    @Test
    public void testErrorSeverityDoesNotMatch__True() {
        //given
        aggregator.setErrorSeverity(ErrorSeverity.CRITICAL);

        //when
        boolean result = AggregatorSenderValidator.checkErrorSeverityDoesNotMatch(aggregator, error);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void testErrorSeverityDoesNotMatch__False() {
        //given
        aggregator.setErrorSeverity(ErrorSeverity.ERROR);

        //when
        boolean result = AggregatorSenderValidator.checkErrorSeverityDoesNotMatch(aggregator, error);

        //then
        assertThat(result).isFalse();
    }

    @Test
    public void testErrorCountLessThan__False() {
        //given
        aggregator.setCount(1);

        //when
        boolean result = AggregatorSenderValidator.checkErrorCountLessThanAggregator(aggregator, error);

        //then
        assertThat(result).isFalse();
    }

    @Test
    public void testErrorCountLessThan__True() {
        //given
        aggregator.setCount(5);

        //when
        boolean result = AggregatorSenderValidator.checkErrorCountLessThanAggregator(aggregator, error);

        //then
        assertThat(result).isTrue();
    }
}