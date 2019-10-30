/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.opennumberroll;

import dk.dbc.jsonb.JSONBContext;
import dk.dbc.jsonb.JSONBException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class NumberrollResponseTest {
    private final JSONBContext jsonbContext = new JSONBContext();
    final String expectedJson =
            "{\"rollNumber\":\"12345678\"}";

    @Test
    void jsonMarshalling() throws JSONBException {
        final NumberrollResponse entity = new NumberrollResponse();
        entity.setRollNumber("12345678");
        assertThat(jsonbContext.marshall(entity), is(expectedJson));
    }
    
    @Test
    void jsonUnmarshalling() throws JSONBException {
        final NumberrollResponse unmarshalled = jsonbContext.unmarshall(expectedJson, NumberrollResponse.class);
        assertThat(jsonbContext.marshall(unmarshalled), is(expectedJson));
    }
}