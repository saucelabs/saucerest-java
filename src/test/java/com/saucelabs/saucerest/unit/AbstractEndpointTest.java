package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.Helper;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.api.AbstractEndpoint;
import com.saucelabs.saucerest.model.builds.Build;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import okhttp3.HttpUrl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AbstractEndpointTest {
    /**
     * Test of {@link com.saucelabs.saucerest.api.AbstractEndpoint#buildUrl(String, Map)} method, of class AbstractEndpoint using reflection.
     */
    @Test
    public void testBuildUrl() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        AbstractEndpoint instance = mock(AbstractEndpoint.class);
        Method method = AbstractEndpoint.class.getDeclaredMethod("buildUrl", String.class, Map.class);
        method.setAccessible(true);

        String url = "https://example.com";
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("param1", "value1");
        params.put("param2", new String[]{"value2", "value3"});
        params.put("param3", 20);
        params.put("param4", true);

        String expected = HttpUrl.parse("https://example.com")
                .newBuilder()
                .addQueryParameter("param1", "value1")
                .addQueryParameter("param2", "value2")
                .addQueryParameter("param2", "value3")
                .addQueryParameter("param3", "20")
                .addQueryParameter("param4", "true")
                .build()
                .toString();

        String actual = (String) method.invoke(instance, url, params);

        assertEquals(expected, actual);
    }

    @Test
    void testDeserializeJSONArray() throws IOException {
        // Create a JSON response string
        String jsonResponse = "[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}]";

        // Deserialize the JSON response string into a list of Person objects
        List<Person> persons = new PersonEndpoint("").publicDeserializeJSONArray(jsonResponse, Person.class);

        // Verify that the deserialized list contains the expected objects
        assertAll("persons",
                () -> assertEquals(2, persons.size()),
                () -> assertEquals("John", persons.get(0).getName()),
                () -> assertEquals(30, persons.get(0).getAge()),
                () -> assertEquals("Jane", persons.get(1).getName()),
                () -> assertEquals(25, persons.get(1).getAge())
        );
    }

    @Test
    void testDeserializeJSONObject() throws IOException {
        // Create a JSON response string
        String jsonResponse = "{\"name\":\"John\",\"age\":30}";

        // Deserialize the JSON response string into a list of Person objects
        Person person = new PersonEndpoint("").publicDeserializeJSONObject(jsonResponse, Person.class);

        // Verify that the deserialized list contains the expected objects
        assertAll("persons",
                () -> assertEquals(person.getName(), "John"),
                () -> assertEquals(person.getAge(), 30)
        );
    }

    @Test
    void testDeserializeJSONObjectWithListOfClassTypes_WithTwoItems() throws IOException {
        Helper helper = new Helper();
        String jsonResponse = helper.getResourceFileAsString("/buildsResponses.json");

        List<Build> builds = new PersonEndpoint("").publicDeserializeJSONObject(jsonResponse, Collections.singletonList(Build.class));

        assertEquals(2, builds.size());
    }

    @Test
    void testDeserializeJSONObjectWithListOfClassTypes_WithOneItem() throws IOException {
        Helper helper = new Helper();
        String jsonResponse = helper.getResourceFileAsString("/buildsResponse.json");

        List<Build> builds = new PersonEndpoint("").publicDeserializeJSONObject(jsonResponse, Collections.singletonList(Build.class));

        assertEquals(1, builds.size());
    }

    @Test
    public void testConstructorWithNullCredentials() {
        assertThrows(SauceException.MissingCredentials.class, () -> new PersonEndpoint(null, null, null));
    }

    @Test
    public void testConstructorWithNullCredentialsAndNeedsAuthenticationFalse() {
        PersonEndpoint endpoint = new PersonEndpoint(DataCenter.US_WEST);
        assertNull(endpoint.getCredentials());
    }


    public static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    public static class PersonEndpoint extends AbstractEndpoint {

        public PersonEndpoint(String apiServer) {
            super(apiServer);
        }

        public PersonEndpoint(DataCenter dataCenter) {
            super(dataCenter, false);
        }

        public PersonEndpoint(String username, String accessKey, String apiServer) {
            super(username, accessKey, apiServer);
        }

        public <T> List<T> publicDeserializeJSONArray(String json, Class<T> clazz) throws IOException {
            return deserializeJSONArray(json, clazz);
        }

        public <T> T publicDeserializeJSONObject(String json, Class<T> clazz) throws IOException {
            return deserializeJSONObject(json, clazz);
        }

        public <T> List<T> publicDeserializeJSONObject(String json, List<Class<? extends T>> clazz) throws IOException {
            return deserializeJSONObject(json, clazz);
        }

        public String getCredentials() {
            return credentials;
        }

    }
}
