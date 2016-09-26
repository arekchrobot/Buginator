package pl.ark.chr.buginator.domain.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.domain.UserApplicationId;

import java.io.IOException;

/**
 * Created by Arek on 2016-09-26.
 */
public class UserApplicationIdSerializer extends JsonSerializer<UserApplicationId>{

    private class UserApplicationIdWrapper {
        private KeyWrapper user;
        private KeyWrapper application;

        public UserApplicationIdWrapper(User user, Application application) {
            this.user = new KeyWrapper(user);
            this.application = new KeyWrapper(application);
        }
    }

    private class KeyWrapper {
        private Long id;
        private String name;

        public KeyWrapper(User user) {
            this.id = user.getId();
            this.name = user.getName();
        }

        public KeyWrapper(Application application) {
            this.id = application.getId();
            this.name = application.getName();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Override
    public void serialize(UserApplicationId userApplicationId, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeObject(new UserApplicationIdWrapper(userApplicationId.getUser(), userApplicationId.getApplication()));
    }
}
