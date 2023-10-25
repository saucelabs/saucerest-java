package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucelabs.saucerest.model.AbstractModel;
import org.junit.jupiter.api.Test;

class AbstractModelTest {

    @Test
    void toJson_ShouldReturnValidJsonString() {
        // Arrange
        EmptyModel model = new EmptyModel();

        // Act
        String json = model.toJson();

        // Assert
        assertEquals("{}", json);
    }

    @Test
    void toJson_WithCustomData_ShouldReturnValidJsonString() {
        // Arrange
        DummyModel model = new DummyModel();
        model.setFoo("bar");
        model.setBaz(123);

        // Act
        String json = model.toJson();

        // Assert
        assertTrue(json.contains("\"foo\":\"bar\""));
        assertTrue(json.contains("\"baz\":123"));
    }

    @Test
    void toJson_WithNullValues_ShouldReturnValidJsonString() {
        // Arrange
        DummyModel model = new DummyModel();
        model.setFoo(null);
        model.setBaz(null);

        // Act
        String json = model.toJson();

        // Assert
        assertEquals("{}", json);
    }

    public static class EmptyModel extends AbstractModel {
        public String toJson() {
            return super.toJson();
        }
    }

    public static class DummyModel extends AbstractModel {
        private String foo;
        private Integer baz;

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        public Integer getBaz() {
            return baz;
        }

        public void setBaz(Integer baz) {
            this.baz = baz;
        }

        public String toJson() {
            return super.toJson();
        }
    }
}