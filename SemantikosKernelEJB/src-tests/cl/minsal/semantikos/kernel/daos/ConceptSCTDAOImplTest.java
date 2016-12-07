package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;
import static org.junit.Assert.*;

public class ConceptSCTDAOImplTest {

    @Test
    public void testCreateConceptCSTFromJSON() throws Exception {

        String jsonResult = "{\"id\":1,\"effectiveTime\":null,\"active\":true,\"moduleId\":1}";

        ObjectMapper mapper = new ObjectMapper();
        ConceptSCT conceptSCT = mapper.readValue(underScoreToCamelCaseJSON(jsonResult), ConceptSCT.class);
        conceptSCT.toString();
    }
}