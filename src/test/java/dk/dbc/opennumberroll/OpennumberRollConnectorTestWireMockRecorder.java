package dk.dbc.opennumberroll;

public class OpennumberRollConnectorTestWireMockRecorder {
        /*
        Steps to reproduce wiremock recording:

        * Start standalone runner
            java -jar wiremock-standalone-{WIRE_MOCK_VERSION}.jar --proxy-all="{OPENNUMBERROLL_HOST}" --record-mappings --verbose

        * Run the main method of this class

        * Replace content of src/test/resources/{__files|mappings} with that produced by the standalone runner
     */

    public static void main(String[] args) throws Exception {
        OpennumberRollConnectorTest.connector = new OpennumberRollConnector(
                OpennumberRollConnectorTest.CLIENT, "http://localhost:8080");
        final OpennumberRollConnectorTest opennumberRollConnectorTest = new OpennumberRollConnectorTest();
        recordGetIdRequests(opennumberRollConnectorTest);
    }

    private static void recordGetIdRequests(OpennumberRollConnectorTest opennumberRollConnectorTest)
            throws OpennumberRollConnectorException {
        opennumberRollConnectorTest.testGetRollNumber();
    }

}
