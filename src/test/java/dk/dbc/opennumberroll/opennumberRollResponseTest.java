package dk.dbc.opennumberroll;

import dk.dbc.jsonb.JSONBContext;
import dk.dbc.jsonb.JSONBException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class opennumberRollResponseTest {
    private final JSONBContext jsonbContext = new JSONBContext();
    final String expectedJson =
            "{\"numberRollResponse\":{\"rollNumber\":{\"$\":\"12345678\"},\"error\":null}}";

    @Test
    void jsonMarshalling() throws JSONBException {
        final OpennumberRollResponse entity = new OpennumberRollResponse();
        entity.setId("12345678");

        assertThat(jsonbContext.marshall(entity), is(expectedJson));
    }
    
    @Test
    void jsonUnmarshalling() throws JSONBException {
        final OpennumberRollResponse unmarshalled = jsonbContext.unmarshall(expectedJson, OpennumberRollResponse.class);
        assertThat(jsonbContext.marshall(unmarshalled), is(expectedJson));
    }
}
