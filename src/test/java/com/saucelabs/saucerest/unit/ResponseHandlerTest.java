package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.api.AbstractEndpoint;
import com.saucelabs.saucerest.api.Job;
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
        Response response = getMockResponse(getMockRequest("https://saucelabs.com/rest/v1/fakeuser/tunnels/1234", HttpMethod.DELETE), 404);

        assertThrows(SauceException.NotFound.class, () -> responseHandler(new SauceConnect(DataCenter.EU_CENTRAL), response));
    }

    @Test
    public void notAuthorizedTest() {
        Response response = getMockResponse(getMockRequest("https://fakewebsite.com", HttpMethod.GET), 401);

        assertThrows(SauceException.NotAuthorized.class, () -> responseHandler(getMockAbstractEndpoint(null, null), response));
        assertThrows(SauceException.NotAuthorized.class, () -> responseHandler(getMockAbstractEndpoint("fakeuser", null), response));
        assertThrows(SauceException.NotAuthorized.class, () -> responseHandler(getMockAbstractEndpoint(null, "fakeaccesskeyy"), response));
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

    private Job getMockJob(String username, String accessKey) {
        return Mockito.mock(
            Job.class,
            Mockito.withSettings()
                .useConstructor(username, accessKey, "apiserver", "1234")
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