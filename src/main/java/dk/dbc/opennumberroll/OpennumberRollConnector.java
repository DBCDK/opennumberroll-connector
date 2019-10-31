/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.opennumberroll;

import dk.dbc.httpclient.FailSafeHttpClient;
import dk.dbc.httpclient.HttpGet;
import dk.dbc.httpclient.PathBuilder;
import dk.dbc.invariant.InvariantUtil;
import dk.dbc.util.Stopwatch;
import net.jodah.failsafe.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class OpennumberRollConnector {

    public enum TimingLogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(OpennumberRollConnector.class);
    private static final String PATH_GET_ROLLNUMBER = "1.0/server.php";

    private static final int STATUS_CODE_GONE = 410;
    private static final int STATUS_CODE_UNPROCESSABLE_ENTITY = 422;

    private static final RetryPolicy RETRY_POLICY = new RetryPolicy()
            .retryOn(Collections.singletonList(ProcessingException.class))
            .retryIf((Response response) -> response.getStatus() == 404
                    || response.getStatus() == 500
                    || response.getStatus() == 502)
            .withDelay(1, TimeUnit.SECONDS)
            .withMaxRetries(3);

    private final FailSafeHttpClient failSafeHttpClient;
    private final String baseUrl;
    private final OpennumberRollConnector.LogLevelMethod logger;

    /**
     * Returns new instance with default retry policy
     *
     * @param httpClient web resources client
     * @param baseUrl    base URL for opennumberRoll service endpoint
     */
    public OpennumberRollConnector(Client httpClient, String baseUrl) {
        this(FailSafeHttpClient.create(httpClient, RETRY_POLICY), baseUrl, OpennumberRollConnector.TimingLogLevel.INFO);
    }

    /**
     * Returns new instance with default retry policy
     *
     * @param httpClient web resources client
     * @param baseUrl    base URL for opennumberRoll service endpoint
     * @param level      timings log level
     */
    public OpennumberRollConnector(Client httpClient, String baseUrl, OpennumberRollConnector.TimingLogLevel level) {
        this(FailSafeHttpClient.create(httpClient, RETRY_POLICY), baseUrl, level);
    }

    /**
     * Returns new instance with custom retry policy
     *
     * @param failSafeHttpClient web resources client with custom retry policy
     * @param baseUrl            base URL for opennumberRoll service endpoint
     */
    public OpennumberRollConnector(FailSafeHttpClient failSafeHttpClient, String baseUrl) {
        this(failSafeHttpClient, baseUrl, OpennumberRollConnector.TimingLogLevel.INFO);
    }

    /**
     * Returns new instance with custom retry policy
     *
     * @param failSafeHttpClient web resources client with custom retry policy
     * @param baseUrl            base URL for opennumberRoll service endpoint.
     * @param level              timings log level
     */
    public OpennumberRollConnector(FailSafeHttpClient failSafeHttpClient, String baseUrl, OpennumberRollConnector.TimingLogLevel level) {
        this.failSafeHttpClient = InvariantUtil.checkNotNullOrThrow(
                failSafeHttpClient, "failSafeHttpClient");
        this.baseUrl = InvariantUtil.checkNotNullNotEmptyOrThrow(
                baseUrl, "baseUrl");
        switch (level) {
            case TRACE:
                logger = LOGGER::trace;
                break;
            case DEBUG:
                logger = LOGGER::debug;
                break;
            case INFO:
                logger = LOGGER::info;
                break;
            case WARN:
                logger = LOGGER::warn;
                break;
            case ERROR:
                logger = LOGGER::error;
                break;
            default:
                logger = LOGGER::info;
                break;
        }
    }

    /**
     * Get a new id (faust) number
     * @param params Request parameters
     * @return An OpennumberRollResponse object with the new id number
     * @throws OpennumberRollConnectorException
     */
    public String getId(Params params) throws OpennumberRollConnectorException {
        final Stopwatch stopwatch = new Stopwatch();
        try {
            final OpennumberRollResponse response = sendRequest(PATH_GET_ROLLNUMBER, params, OpennumberRollResponse.class);
            return response.getId();
        } finally {
            logger.log("getRollNumber() took {} milliseconds",
                    stopwatch.getElapsedTime(TimeUnit.MILLISECONDS));
        }
    }

    private <T> T sendRequest(String basePath, Params params, Class<T> type)
            throws OpennumberRollConnectorException {
        final PathBuilder path = new PathBuilder(basePath);
        final HttpGet httpGet = new HttpGet(failSafeHttpClient)
                .withBaseUrl(baseUrl)
                .withPathElements(path.build());
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                httpGet.withQueryParameter(param.getKey(), param.getValue());
            }
        }
        final Response response = httpGet.execute();
        assertResponseStatus(response, Response.Status.OK);
        return readResponseEntity(response, type);
    }

    private <T> T readResponseEntity(Response response, Class<T> type)
            throws OpennumberRollConnectorException {
        final T entity = response.readEntity(type);
        if (entity == null) {
            throw new OpennumberRollConnectorException(
                    String.format("OpennumberRoll service returned with null-valued %s entity",
                            type.getName()));
        }
        return entity;
    }

    private String readErrorResponseMessage(Response response) throws OpennumberRollConnectorException {
        if (response.hasEntity()) {
            return readResponseEntity(response, String.class);
        }
        return "";
    }

    private void assertResponseStatus(Response response, Response.Status... expectedStatus)
            throws OpennumberRollConnectorException {
        final Response.Status actualStatus =
                Response.Status.fromStatusCode(response.getStatus());
        if (!Arrays.asList(expectedStatus).contains(actualStatus)) {
            if (actualStatus.getStatusCode() == STATUS_CODE_GONE) {
                throw new OpennumberRollConnectorException(readErrorResponseMessage(response));
            } else if (actualStatus.getStatusCode() == STATUS_CODE_UNPROCESSABLE_ENTITY) {
                throw new OpennumberRollConnectorUnprocessableEntityException(readErrorResponseMessage(response));
            } else {
                throw new OpennumberRollConnectorUnexpectedStatusCodeException(
                        String.format("OpennumberRoll service returned with unexpected status code: %s",
                                actualStatus),
                        actualStatus.getStatusCode());
            }
        }
    }

    public void close() {
        failSafeHttpClient.getClient().close();
    }

    @FunctionalInterface
    interface LogLevelMethod {
        void log(String format, Object... objs);
    }

    public static class Params extends HashMap<String, Object> {
        public enum Key {
            ACTION("action"),
            OUTPUTTYPE( "outputType"),
            ROLLNAME("numberRollName");

            private final String keyName;

            Key(String keyName) {
                this.keyName = keyName;
            }

            public String getKeyName() {
                return keyName;
            }
        }

        public Params() {

            // Seed the map with fixed values
            putOrRemoveOnNull(Key.ACTION, "numberRoll");
            putOrRemoveOnNull(Key.OUTPUTTYPE, "json");
        }

        public Params withRollName(String rollName) {
            putOrRemoveOnNull(Key.ROLLNAME, rollName);
            return this;
        }

        public Optional<String> getRollName() {
            return Optional.ofNullable((String) this.get(Key.ROLLNAME));
        }

        public Optional<String> getAction() {
            return Optional.ofNullable((String) this.get(Key.ACTION));
        }

        public Optional<String> getOutputType() {
            return Optional.ofNullable((String) this.get(Key.OUTPUTTYPE));
        }

        private void putOrRemoveOnNull(Key param, Object value) {
            if (value == null) {
                this.remove(param.keyName);
            } else {
                this.put(param.keyName, value);
            }
        }
    }

}
