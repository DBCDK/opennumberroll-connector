package dk.dbc.opennumberroll;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpennumberRollResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpennumberRollResponse.class);
    /*
        OpennumberRoll server responds, for outputType=json, with this object:

        {"numberRollResponse":{"rollNumber":{"$":"166718546"}},"@namespaces":null}
        or in case of errors
        {"numberRollResponse":{"error":{"$":"some error"}}
    */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NumberRollResponse {

        public static class Error {

            // Strange fieldname, but that's what we get :)
            private String $;

            public String get$() { return $; }

            public void set$(String $) { this.$ = $; }

            @Override
            public String toString() {
                return "Error{" +
                        "$='" + $ + '\'' +
                        '}';
            }
        }

        public static class RollNumber {

            // Strange fieldname, but that's what we get :)
            private String $;

            public String get$() {
                return $;
            }

            public void set$(String $) {
                this.$ = $;
            }

            @Override
            public String toString() {
                return "RollNumber{" +
                        "$='" + $ + "\'," +
                        '}';
            }
        }

        private RollNumber rollNumber;
        private Error error;

        public RollNumber getRollNumber() {
            return rollNumber;
        }

        public void setRollNumber(RollNumber rollNumber) {
            this.rollNumber = rollNumber;
        }

        public Error getError() { return error; }

        public void setError(Error error) { this.error = error; }

        public boolean hasError() { return error != null && !error.get$().isEmpty(); }

        @Override
        public String toString() {
            return "NumberRollResponse{" +
                    "rollNumber=" + rollNumber + "," +
                    "error=" + error +
                    '}';
        }
    }

    private NumberRollResponse numberRollResponse;

    public NumberRollResponse getNumberRollResponse() {
        return numberRollResponse;
    }

    public void setNumberRollResponse(NumberRollResponse numberRollResponse) {
        this.numberRollResponse = numberRollResponse;
    }

    @JsonIgnore
    public String getId() {
        LOGGER.info("GetId called: {}", numberRollResponse.toString());
        return numberRollResponse.rollNumber.$;
    }

    void setId(String id) {
        setNumberRollResponse(new NumberRollResponse());
        numberRollResponse.setRollNumber(new NumberRollResponse.RollNumber());
        numberRollResponse.rollNumber.set$(id);
    }

    @Override
    public String toString() {
        return "OpennumberRollResponse{" +
                "numberRollResponse=" + numberRollResponse +
                '}';
    }
}
