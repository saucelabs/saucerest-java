package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.AbstractEndpoint;
import com.saucelabs.saucerest.api.SauceConnect;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.saucelabs.saucerest.api.ResponseHandler.responseHandler;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResponseHandlerTest {

    @Test
    public void tunnelNotFoundTest() {
        Request mockRequest = new Request.Builder()
            .url(new SauceREST(DataCenter.EU_CENTRAL).getSauceConnect().getBaseEndpoint() + "fakeuser/tunnels/1234")
            .delete()
            .build();

        Response response = new Response.Builder()
            .request(mockRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(404)
            .message("")
            .build();

        assertThrows(SauceException.NotFound.class, () -> responseHandler(new SauceConnect(DataCenter.EU_CENTRAL), response));
    }

    @Test
    public void notAuthorizedTest() {
        Request mockRequest = new Request.Builder()
            .url("https://fakewebsite.com")
            .get()
            .build();

        Response response = new Response.Builder()
            .request(mockRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(401)
            .message("")
            .build();

        AbstractEndpoint abstractEndpoint = Mockito.mock(
            AbstractEndpoint.class,
            Mockito.withSettings()
                .useConstructor(null, null, null)
                .defaultAnswer(Mockito.CALLS_REAL_METHODS)
        );

        //assertThrows(SauceException.NotAuthorized.class, () -> responseHandler(new SauceConnect(DataCenter.EU_CENTRAL), response));
        //responseHandler(new SauceConnect(null, "123", DataCenter.EU_CENTRAL), response);
        //responseHandler(new SauceConnect("null", null, DataCenter.EU_CENTRAL), response);
        responseHandler(abstractEndpoint, response);
    }

}