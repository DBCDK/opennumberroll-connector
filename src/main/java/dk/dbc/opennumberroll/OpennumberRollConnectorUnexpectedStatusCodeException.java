package dk.dbc.opennumberroll;

public class OpennumberRollConnectorUnexpectedStatusCodeException extends OpennumberRollConnectorException {
    private final int statusCode;

    /**
     * Constructs a new exception with the specified detail message
     * <p>
     * The cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message    detail message saved for later retrieval by the
     *                   {@link #getMessage()} method. May be null.
     * @param statusCode the http status code returned by the REST service
     */
    public OpennumberRollConnectorUnexpectedStatusCodeException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

}
