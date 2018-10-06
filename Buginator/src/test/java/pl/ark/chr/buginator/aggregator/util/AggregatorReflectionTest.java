package pl.ark.chr.buginator.aggregator.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.ark.chr.buginator.TestApplicationConfiguration;
import pl.ark.chr.buginator.aggregator.service.AggregatorService;
import pl.ark.chr.buginator.aggregator.service.impl.EmailAggregatorServiceImpl;
import pl.ark.chr.buginator.domain.Aggregator;
import pl.ark.chr.buginator.domain.EmailAggregator;
import pl.ark.chr.buginator.repository.AggregatorRepository;
import pl.ark.chr.buginator.repository.EmailAggregatorRepository;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2017-03-12.
 */
@ActiveProfiles("UNIT_TEST")
@SpringBootTest(classes = TestApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class AggregatorReflectionTest {

    private AggregatorReflection sut = new AggregatorReflection();

    @Autowired
    private ApplicationContext springContext;

    @Test
    public void getAggregatorRepository_EmailAggregatorRepository__Exists() throws Exception {
        //given
        Aggregator aggregator = new EmailAggregator();
        aggregator.setAggregatorClass("EmailAggregator");

        //when
        CrudRepository emailAggregatorRepository = sut.getAggregatorRepository(aggregator, springContext);

        //then
        assertThat(emailAggregatorRepository).isNotNull();
        assertThat(emailAggregatorRepository).isInstanceOf(EmailAggregatorRepository.class);
    }

    @Test
    public void getAggregatorRepository_EmailAggregatorRepository__NotExists() throws Exception {
        //given
        Aggregator aggregator = new EmailAggregator();
        aggregator.setAggregatorClass("EmailAggregatorNull");

        //when
        CrudRepository emailAggregatorRepository = sut.getAggregatorRepository(aggregator, springContext);

        //then
        assertThat(emailAggregatorRepository).isNotNull();
        assertThat(emailAggregatorRepository).isInstanceOf(AggregatorRepository.class);
    }

    @Test
    public void createEmptyAggregator_EmailAggregator__Exists() throws Exception {
        //given
        Aggregator aggregator = new EmailAggregator();
        aggregator.setAggregatorClass("EmailAggregator");

        //when
        Aggregator result = sut.createEmptyAggregator(aggregator);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(EmailAggregator.class);
    }

    @Test
    public void createEmptyAggregator_EmailAggregator__NotExists() throws Exception {
        //given
        Aggregator aggregator = new EmailAggregator();
        aggregator.setAggregatorClass("EmailAggregatorNull");

        //when
        Aggregator result = sut.createEmptyAggregator(aggregator);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Aggregator.class);
        //TODO: fix
//        assertThat(result).isEqualToComparingFieldByField(aggregator);
    }

    @Test
    public void getAggregatorService_EmailAggregatorService__Exists() throws Exception {
        //given
        Aggregator aggregator = new EmailAggregator();
        aggregator.setAggregatorClass("EmailAggregator");

        //when
        AggregatorService service = sut.getAggregatorService(aggregator, springContext);

        //then
        assertThat(service).isNotNull();
        assertThat(service).isInstanceOf(EmailAggregatorServiceImpl.class);
    }

    @Test
    public void getAggregatorService_EmailAggregatorService__NotExists() throws Exception {
        //given
        Aggregator aggregator = new EmailAggregator();
        aggregator.setAggregatorClass("EmailAggregatorNull");

        //when
        AggregatorService service = sut.getAggregatorService(aggregator, springContext);

        //then
        assertThat(service).isNotNull();
    }
}