/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.opennumberroll;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.dbc.jsonb.JSONBContext;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NumberrollResponse {
    private static final JSONBContext JSONB_CONTEXT = new JSONBContext();

    private String rollNumber;

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String id) {
        this.rollNumber = id;
    }

    @Override
    public String toString() {
        return "NumberrollResponse{" +
                "rollNumber='" + rollNumber + '\'' +
                '}';
    }
}
