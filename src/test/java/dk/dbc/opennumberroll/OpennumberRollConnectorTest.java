package dk.dbc.opennumberroll;

import dk.dbc.commons.useragent.UserAgent;
import dk.dbc.httpclient.HttpClient;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.client.Client;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OpennumberRollConnectorTest {

    private static WireMockServer wireMockServer;
    private static String wireMockHost;

    final static Client CLIENT = HttpClient.newClient(new ClientConfig()
            .register(new JacksonFeature()));
    static OpennumberRollConnector connector;

    @BeforeAll
    static void startWireMockServer() {
        wireMockServer = new WireMockServer(options().dynamicPort()
                .dynamicHttpsPort());
        wireMockServer.start();
        wireMockHost = "http://localhost:" + wireMockServer.port() + "/1.1";
        configureFor("localhost", wireMockServer.port());
    }

    @BeforeAll
    static void setConnector() {
        connector = OpennumberRollConnectorFactory.create(wireMockHost, new UserAgent("OpenNumberRollConnectorTest"));
    }

    @AfterAll
    static void stopWireMockServer() {
        wireMockServer.stop();
    }

    @Test
    void testGetRollNumber() throws OpennumberRollConnectorException {
        OpennumberRollConnector.Params params = new OpennumberRollConnector.Params();
        params.withRollName("faust");

        String actual = connector.getId(params);

        assertThat(actual.length(), is(9));
        assertThat(actual, is("140217492"));
    }

}
