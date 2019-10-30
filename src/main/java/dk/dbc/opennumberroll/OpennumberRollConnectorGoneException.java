package dk.dbc.opennumberroll;

public class OpennumberRollConnectorGoneException extends OpennumberRollConnectorUnexpectedStatusCodeException {

    public OpennumberRollConnectorGoneException(String message) {
        super(message, 410);
    }

}
