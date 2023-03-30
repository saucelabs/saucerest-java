package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.api.AbstractEndpoint;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractEndpointTest {

    @Test
    void testDeserializeJSONArray() throws IOException {
        // Create a JSON response string
        String jsonResponse = "[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}]";

        // Deserialize the JSON response string into a list of Person objects
        List<Person> persons = new PersonEndpoint("").publicDeserializeJSONArray(jsonResponse, Person.class);

        // Verify that the deserialized list contains the expected objects
        assertAll("persons",
            () -> assertEquals(persons.size(), 2),
            () -> assertEquals(persons.get(0).getName(), "John"),
            () -> assertEquals(persons.get(0).getAge(), 30),
            () -> assertEquals(persons.get(1).getName(), "Jane"),
            () -> assertEquals(persons.get(1).getAge(), 25)
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

        public <T> List<T> publicDeserializeJSONArray(String json, Class<T> clazz) throws IOException {
            return deserializeJSONArray(json, clazz);
        }

        public <T> T publicDeserializeJSONObject(String json, Class<T> clazz) throws IOException {
            return deserializeJSONObject(json, clazz);
        }
    }
}