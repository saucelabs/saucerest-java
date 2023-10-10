package com.saucelabs.saucerest.unit;

import static com.saucelabs.saucerest.api.ResponseHandler.responseHandler;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.api.AbstractEndpoint;
import com.saucelabs.saucerest.api.JobsEndpoint;
import com.saucelabs.saucerest.api.SauceConnectEndpoint;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ResponseHandlerTest {

    @Test
    public void tunnelNotFoundTest() {
        Response response = getMockResponse(getMockRequest("https://saucelabs.com/rest/v1/fakeuser/tunnels/1234", HttpMethod.DELETE), 404);

        assertThrows(SauceException.NotFound.class, () -> responseHandler(new SauceConnectEndpoint(DataCenter.EU_CENTRAL), response));
    }

    @Test
    public void notAuthorizedTest() {
        Response response = getMockResponse(getMockRequest("https://fakewebsite.com", HttpMethod.GET), 401);

        assertThrows(SauceException.NotAuthorized.class, () -> responseHandler(getMockAbstractEndpoint("user", "key"), response));
    }

    @Test
    public void jobNotFinishedTest() {
        Response response = getMockResponse(getMockRequest("https://saucelabs.com/rest/v1/fakeuser/jobs/1234", HttpMethod.GET), 400, "Job hasn't finished running");

        assertThrows(SauceException.NotYetDone.class, () -> responseHandler(getMockJob("fakeuser", "fakeaccesskey"), response));
    }

    @Test
    public void defaultExceptionTest() {
        Response response = getMockResponse(getMockRequest("https://saucelabs.com", HttpMethod.GET), 500);

        assertThrows(RuntimeException.class, () -> responseHandler(getMockAbstractEndpoint("fakeuser", "fakeaccesskey"), response));
    }

    private AbstractEndpoint getMockAbstractEndpoint(String username, String accessKey) {
        return Mockito.mock(
            AbstractEndpoint.class,
            Mockito.withSettings()
                .useConstructor(username, accessKey, "null")
                .defaultAnswer(Mockito.CALLS_REAL_METHODS)
        );
    }

    private JobsEndpoint getMockJob(String username, String accessKey) {
        return Mockito.mock(
                JobsEndpoint.class,
                Mockito.withSettings()
                        .useConstructor(username, accessKey, "apiserver")
                        .defaultAnswer(Mockito.CALLS_REAL_METHODS)
        );
    }

    private Request getMockRequest(String url, HttpMethod httpMethod) {
        return new Request.Builder()
            .url(url)
            .method(httpMethod.label, null)
            .build();
    }

    private Response getMockResponse(Request request, int code, String message) {
        return new Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message(message)
            .build();
    }

    private Response getMockResponse(Request request, int code) {
        return getMockResponse(request, code, "");
    }
}