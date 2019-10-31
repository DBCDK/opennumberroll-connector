/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.opennumberroll;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpennumberRollResponse {

    /*
        OpennumberRoll server responds, for outputType=json, with this object:

        {"numberRollResponse":{"rollNumber":{"$":"166718546"}},"@namespaces":null}
    */

    public static class NumberRollResponse {

        public static class RollNumber {

            // Strange fieldname, but that's what we get :)
            @JsonProperty("$")
            public String id;

            public String get$() {
                return id;
            }

            public void set$(String $) {
                this.id = $;
            }
        }

        public RollNumber rollNumber;

        public RollNumber getRollNumber() {
            return rollNumber;
        }

        public void setRollNumber(RollNumber rollNumber) {
            this.rollNumber = rollNumber;
        }
    }

    public NumberRollResponse numberRollResponse;

    public NumberRollResponse getNumberRollResponse() {
        return numberRollResponse;
    }

    public void setNumberRollResponse(NumberRollResponse numberRollResponse) {
        this.numberRollResponse = numberRollResponse;
    }
}
