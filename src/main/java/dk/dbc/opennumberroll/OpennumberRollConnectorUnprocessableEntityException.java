package dk.dbc.opennumberroll;

public class OpennumberRollConnectorUnprocessableEntityException extends OpennumberRollConnectorUnexpectedStatusCodeException {

    public OpennumberRollConnectorUnprocessableEntityException(String message) {
        super(message, 422);

    }

}
