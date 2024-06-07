package com.saucelabs.saucerest.unit;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.Helper;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.api.SauceConnectEndpoint;
import com.saucelabs.saucerest.model.sauceconnect.TunnelInformation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import okhttp3.Response;
import okhttp3.ResponseBody;

@ExtendWith(MockitoExtension.class)
class SauceConnectEndpointTest {

    @Spy
    private SauceConnectEndpoint sauceConnectEndpoint = new SauceConnectEndpoint("username", "access-key",
        DataCenter.EU_CENTRAL);

    @Test
    void getTunnelsInformationForAUserTest() throws IOException {
        Response response = mock();
        ResponseBody responseBody = mock();
        when(response.body()).thenReturn(responseBody);
        when(responseBody.string()).thenReturn(Helper.getResourceFileAsString("/tunnelsResponse.json"));
        doReturn(response).when(sauceConnectEndpoint).request(
            "https://api.eu-central-1.saucelabs.com/rest/v1/username/tunnels?full=true", HttpMethod.GET);
        List<TunnelInformation> tunnelsInfo = sauceConnectEndpoint.getTunnelsInformationForAUser();

        Assertions.assertFalse(tunnelsInfo.isEmpty());
    }
}
