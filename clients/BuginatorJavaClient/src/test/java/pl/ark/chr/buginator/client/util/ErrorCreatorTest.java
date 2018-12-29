package pl.ark.chr.buginator.client.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pl.ark.chr.buginator.client.ErrorSeverity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2017-05-01.
 */
@RunWith(JUnit4.class)
public class ErrorCreatorTest {

    private ErrorCreator sut = new ErrorCreator();

    @Test
    public void testCreateError() {
        //given
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String appName = "testApp";

        String mainException = "main exception";
        String levelOneDeep = "level one";
        String levelTwoDeep = "level two";
        String levelThreeDeep = "level three";

        String threadName = "testThread";
        Thread.currentThread().setName(threadName);

        Throwable throwable;

        try {
            throw new RuntimeException(mainException, new Exception(levelOneDeep, new RuntimeException(levelTwoDeep, new RuntimeException(levelThreeDeep))));
        } catch (Throwable t) {
            throwable = t;
        }

        //when
        JSONObject result = sut.createError(throwable, Thread.currentThread(), appName, ErrorSeverity.WARNING);

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getString("applicationName"))
                .isEqualTo(appName);
        assertThat(result.getString("errorTitle"))
                .isEqualTo("java.lang.RuntimeException");
        assertThat(result.getString("errorDescription"))
                .isEqualTo(mainException);
        assertThat(result.getString("errorSeverity"))
                .isEqualTo("WARNING");
        assertThat(result.get("stackTrace"))
                .isInstanceOf(JSONArray.class);

        try {
            dateTimeFormat.parse(result.getString("dateTimeString"));
            dateFormat.parse(result.getString("dateString"));
        } catch (ParseException e) {
            fail("Wrong date format!");
        }
    }
}