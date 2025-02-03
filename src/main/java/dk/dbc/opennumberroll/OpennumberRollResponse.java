package dk.dbc.opennumberroll;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpennumberRollResponse {

    /*
        OpennumberRoll server responds, for outputType=jsonb, with this object:

        {"numberRollResponse":{"rollNumber":"166718546"},"@namespaces":null}
        or in case of errors
        {"numberRollResponse":{"error":{"rollNumber":"some error"}}
    */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NumberRollResponse {

        public static class Error {

            private String number;

            public String getNumber() { return number; }

            public void setNumber(String number) { this.number = number; }

            @Override
            public String toString() {
                return "Error{" +
                        "rollNumber='" + number + '\'' +
                        '}';
            }
        }

        private String rollNumber;
        private Error error;

        public String getRollNumber() {
            return rollNumber;
        }

        public void setRollNumber(String rollNumber) {
            this.rollNumber = rollNumber;
        }

        public Error getError() { return error; }

        public void setError(Error error) { this.error = error; }

        public boolean hasError() { return error != null && !error.getNumber().isEmpty(); }

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
        return numberRollResponse.getRollNumber();
    }

    void setId(String id) {
        setNumberRollResponse(new NumberRollResponse());
        numberRollResponse.setRollNumber(id);
    }

    @Override
    public String toString() {
        return "OpennumberRollResponse{" +
                "numberRollResponse=" + numberRollResponse +
                '}';
    }
}