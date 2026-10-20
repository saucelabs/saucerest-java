package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucelabs.saucerest.api.HttpClientConfig;
import com.saucelabs.saucerest.api.StorageEndpoint;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class UserAgentTest {
  @TempDir File tempDir;

  private StorageEndpoint endpointCapturing(AtomicReference<Request> captured) {
    StorageEndpoint endpoint = new StorageEndpoint("user", "key", "http://localhost/");
    endpoint.createHttpClient(
        HttpClientConfig.defaultConfig()
            .interceptor(
                chain -> {
                  captured.set(chain.request());
                  throw new IOException("short-circuit");
                }));
    return endpoint;
  }

  private void assertUserAgent(Request request) {
    assertNotNull(request);
    String userAgent = request.header("User-Agent");
    assertNotNull(userAgent);
    assertTrue(userAgent.startsWith("SauceREST/"), userAgent);
  }

  @Test
  void regularRequestSendsUserAgent() {
    AtomicReference<Request> captured = new AtomicReference<>();
    assertThrows(IOException.class, () -> endpointCapturing(captured).getFiles());
    assertUserAgent(captured.get());
  }

  @Test
  void multipartUploadSendsUserAgent() throws IOException {
    File file = new File(tempDir, "app.apk");
    Files.write(file.toPath(), new byte[] {1, 2, 3});
    AtomicReference<Request> captured = new AtomicReference<>();
    assertThrows(IOException.class, () -> endpointCapturing(captured).uploadFile(file));
    assertUserAgent(captured.get());
  }
}
