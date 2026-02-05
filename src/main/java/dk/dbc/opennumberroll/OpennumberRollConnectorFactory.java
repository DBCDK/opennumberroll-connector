package dk.dbc.opennumberroll;

import dk.dbc.commons.useragent.UserAgent;
import dk.dbc.httpclient.HttpClient;
import dk.dbc.opennumberroll.OpennumberRollConnector.TimingLogLevel;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;

/**
 * OpennumberRollConnector factory
 * <p>
 * Synopsis:
 * </p>
 * <pre>
 *    // New instance
 *    OpennumberRollConnector onrc = OpennumberRollConnectorFactory.create("http://guesstimate/~mib/OpenNumberRoll/trunk/server.php");
 *
 *    // Singleton instance in CDI enabled environment
 *    {@literal @}Inject
 *    OpennumberRollConnectorFactory factory;
 *    ...
 *    OpennumberRollConnector onrc = factory.getInstance();
 *
 *    // or simply
 *    {@literal @}Inject
 *    OpennumberRollConnector onrc;
 * </pre>
 * <p>
 * CDI case depends on the opennumberroll service baseurl being defined as
 * the value of either a system property or environment variable
 * named OPENNUMBERROLL_SERVICE_URL. OPENNUMBERROLL_SERVICE_TIMING_LOG_LEVEL
 * should be one of TRACE, DEBUG, INFO(default), WARN or ERROR, for setting
 * log level
 * </p>
 */
@ApplicationScoped
public class OpennumberRollConnectorFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpennumberRollConnectorFactory.class);
    private static final UserAgent userAgent = UserAgent.forInternalRequests();

    public static OpennumberRollConnector create(String opennumberRollServiceBaseUrl) {
        final Client client = HttpClient.newClient(new ClientConfig()
                .register(new JacksonFeature()));
        LOGGER.info("Creating OpennumberRollConnector for: {}", opennumberRollServiceBaseUrl);
        return new OpennumberRollConnector(client, opennumberRollServiceBaseUrl, userAgent);
    }

    public static OpennumberRollConnector create(String opennumberRollServiceBaseUrl, OpennumberRollConnector.TimingLogLevel level) {
        final Client client = HttpClient.newClient(new ClientConfig()
                .register(new JacksonFeature()));
        LOGGER.info("Creating OpennumberRollConnector for: {}", opennumberRollServiceBaseUrl);
        return new OpennumberRollConnector(client, opennumberRollServiceBaseUrl, level, userAgent);
    }

    @Inject
    @ConfigProperty(name = "OPENNUMBERROLL_SERVICE_URL")
    private String opennumberRollServiceUrl;

    @Inject
    @ConfigProperty(name = "OPENNUMBERROLL_LOG_LEVEL", defaultValue = "INFO")
    private TimingLogLevel level;

    OpennumberRollConnector opennumberRollConnector;

    @PostConstruct
    public void initializeConnector() {
        opennumberRollConnector = OpennumberRollConnectorFactory.create(opennumberRollServiceUrl, level);
    }

    @Produces
    public OpennumberRollConnector getInstance() {
        return opennumberRollConnector;
    }

    @PreDestroy
    public void tearDownConnector() {
        opennumberRollConnector.close();
    }
}
