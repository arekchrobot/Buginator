package pl.ark.chr.buginator.aggregator.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.ark.chr.buginator.aggregator.domain.Aggregator;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.domain.core.ErrorStatus;

import static org.assertj.core.api.Assertions.assertThat;


public class AggregatorSenderValidatorTest {

    private Aggregator aggregator;
    private Error error;

    @BeforeEach
    public void setUp() throws Exception {
        Application application = Mockito.mock(Application.class);
        aggregator = new Aggregator("testCLass", application);
        error = Error
                .builder("title", ErrorSeverity.ERROR, ErrorStatus.CREATED, "2018-09-10 22:13:44", application)
                .build();
    }

    @Test
    @DisplayName("should successfully not match severities")
    public void testErrorSeverityDoesNotMatchTrue() {
        //given
        aggregator.setErrorSeverity(ErrorSeverity.CRITICAL);

        //when
        boolean result = AggregatorSenderValidator.checkErrorSeverityDoesNotMatch(aggregator, error);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should return false since severity is matched to be equal")
    public void testErrorSeverityDoesNotMatch__False() {
        //given
        aggregator.setErrorSeverity(ErrorSeverity.ERROR);

        //when
        boolean result = AggregatorSenderValidator.checkErrorSeverityDoesNotMatch(aggregator, error);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should return false since error count is >= aggregator count")
    public void testErrorCountLessThanFalse() {
        //given
        aggregator.setCount(1);

        //when
        boolean result = AggregatorSenderValidator.checkErrorCountLessThanAggregator(aggregator, error);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should return true since error count <= aggregator count")
    public void testErrorCountLessThan__True() {
        //given
        aggregator.setCount(5);

        //when
        boolean result = AggregatorSenderValidator.checkErrorCountLessThanAggregator(aggregator, error);

        //then
        assertThat(result).isTrue();
    }
}