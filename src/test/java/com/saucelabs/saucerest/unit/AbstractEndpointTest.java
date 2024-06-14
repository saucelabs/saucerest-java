package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.Helper;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.api.AbstractEndpoint;
import com.saucelabs.saucerest.model.builds.Build;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import okhttp3.HttpUrl;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
        ResponseBody body = mock();
        when(body.string()).thenReturn("[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}]");
        Response response = mock();
        when(response.body()).thenReturn(body);

        // Deserialize the JSON response string into a list of Person objects
        List<Person> persons = new PersonEndpoint("").publicDeserializeJSONArray(response, Person.class);

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
        ResponseBody body = mock();
        when(body.string()).thenReturn("{\"name\":\"John\",\"age\":30}");
        Response response = mock();
        when(response.body()).thenReturn(body);

        // Deserialize the JSON response string into a list of Person objects
        Person person = new PersonEndpoint("").publicDeserializeJSONObject(response, Person.class);

        // Verify that the deserialized list contains the expected objects
        assertAll("persons",
                () -> assertEquals(person.getName(), "John"),
                () -> assertEquals(person.getAge(), 30)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "/buildsResponses.json, 2",
        "/buildsResponse.json, 1"
    })
    void testDeserializeListFromJSONObject(String resource, int expectedNumberOfItems) throws IOException {
        ResponseBody body = mock();
        when(body.string()).thenReturn(Helper.getResourceFileAsString(resource));
        Response response = mock();
        when(response.body()).thenReturn(body);

        List<Build> builds = new PersonEndpoint("").publicDeserializeListFromJSONObject(response, Build.class);

        assertEquals(expectedNumberOfItems, builds.size());
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
            super(apiServer, false);
        }

        public PersonEndpoint(DataCenter dataCenter) {
            super(dataCenter, false);
        }

        public PersonEndpoint(String username, String accessKey, String apiServer) {
            super(username, accessKey, apiServer);
        }

        public <T> List<T> publicDeserializeJSONArray(Response json, Class<T> elementClass) throws IOException {
            return deserializeJSONArray(json, elementClass);
        }

        public <T> T publicDeserializeJSONObject(Response json, Class<T> clazz) throws IOException {
            return deserializeJSONObject(json, clazz);
        }

        public <T> List<T> publicDeserializeListFromJSONObject(Response json, Class<T> elementClass) throws IOException {
            return deserializeListFromJSONObject(json, elementClass);
        }

        public String getCredentials() {
            return credentials;
        }

    }
}
