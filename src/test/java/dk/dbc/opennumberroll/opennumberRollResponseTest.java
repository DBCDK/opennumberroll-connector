/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.opennumberroll;

import dk.dbc.jsonb.JSONBContext;
import dk.dbc.jsonb.JSONBException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class opennumberRollResponseTest {
    private final JSONBContext jsonbContext = new JSONBContext();
    final String expectedJson =
            "{\"numberRollResponse\":{\"rollNumber\":{\"$\":\"12345678\"}}}";

    @Test
    void jsonMarshalling() throws JSONBException {
        final OpennumberRollResponse entity = new OpennumberRollResponse();
        OpennumberRollResponse.NumberRollResponse numberRollResponse = new OpennumberRollResponse.NumberRollResponse();
        OpennumberRollResponse.NumberRollResponse.RollNumber rollNumber = new OpennumberRollResponse.NumberRollResponse.RollNumber();
        rollNumber.set$("12345678");
        numberRollResponse.setRollNumber(rollNumber);
        entity.setNumberRollResponse(numberRollResponse);

        assertThat(jsonbContext.marshall(entity), is(expectedJson));
    }
    
    @Test
    void jsonUnmarshalling() throws JSONBException {
        final OpennumberRollResponse unmarshalled = jsonbContext.unmarshall(expectedJson, OpennumberRollResponse.class);
        assertThat(jsonbContext.marshall(unmarshalled), is(expectedJson));
    }
}