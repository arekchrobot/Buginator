package pl.ark.chr.buginator.client.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2017-04-30.
 */
@RunWith(JUnit4.class)
public class StackTraceCreatorTest {

    private StackTraceCreator sut = new StackTraceCreator();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testCreateStackTrace() throws Exception {
        //given
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
        JSONArray stackTrace = sut.createStackTrace(throwable, Thread.currentThread());

        //then
        assertThat(stackTrace)
                .isNotNull();
        assertThat(stackTrace.get(0))
                .isInstanceOf(JSONObject.class);
        assertThat(((JSONObject) stackTrace.get(0)).get("description"))
                .isInstanceOf(String.class)
                .isEqualTo("Exception in thread \"" + threadName + "\"" + ": java.lang.RuntimeException: " + mainException);

        int causedByCounter = 0;
        for (int i = 0; i < stackTrace.length(); i++) {
            assertThat(((JSONObject)stackTrace.get(i)).get("order"))
                    .isEqualTo(i+1);
            if(((JSONObject)stackTrace.get(i)).getString("description").contains("Caused by")) {
                causedByCounter++;
            }
        }
        assertThat(causedByCounter).isEqualTo(3);
    }
}