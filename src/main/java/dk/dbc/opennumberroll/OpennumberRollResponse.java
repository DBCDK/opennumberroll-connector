/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.opennumberroll;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpennumberRollResponse {

    /*
        OpennumberRoll server responds, for outputType=json, with this object:

        {"numberRollResponse":{"rollNumber":{"$":"166718546"}},"@namespaces":null}
    */

    public static class NumberRollResponse {

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
                        "$='" + $ + '\'' +
                        '}';
            }
        }

        private RollNumber rollNumber;

        public RollNumber getRollNumber() {
            return rollNumber;
        }

        public void setRollNumber(RollNumber rollNumber) {
            this.rollNumber = rollNumber;
        }

        @Override
        public String toString() {
            return "NumberRollResponse{" +
                    "rollNumber=" + rollNumber +
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
