# OpennumberRoll Rest Connector
Jar library containing helper functions for calling the OpennumberRoll service

### Usage
In pom.xml add this dependency:

    <groupId>dk.dbc</groupId>
    <artifactId>opennumberroll-connector</artifactId>
    <version>2.0-SNAPSHOT</version>

In your EJB add the following inject:

    @Inject
    private OpennumberRollConnector opennumberRollConnector;

You must have the following environment variables in your deployment:

    OPENNUMBERROLL_SERVICE_URL

### Examples
        OpennumberRollConnector.Params params = new OpennumberRollConnector.Params();
        params.withRollName("faust_test");

        Applicant[] applicants = opennumberRollConnector.getApplicants(params);
