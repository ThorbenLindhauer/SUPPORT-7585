package org.camunda.bpm.support;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.util.Scanner;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class MockHttpServer {

  public static void main(String[] args) {
    
    WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
        .jettyStopTimeout(1000L)
        .port(8089));
    wireMockServer.start();
    
    configureFor(8089);
    stubFor(get(urlEqualTo("/endpoint"))
        .willReturn(aResponse()
            .withStatus(204)
            .withFixedDelay(8000)));

    System.out.println("Press Enter to stop server.");
    
    try (Scanner scanner = new Scanner(System.in)) {
      scanner.nextLine();
    }
    
    System.out.println("Stopping mock HTTP server");

    wireMockServer.stop();

  }
}
