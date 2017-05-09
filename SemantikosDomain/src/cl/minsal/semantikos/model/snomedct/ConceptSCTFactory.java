package cl.minsal.semantikos.model.snomedct;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * @author Andrés Farías
 */
public class ConceptSCTFactory {

    private static final ConceptSCTFactory instance = new ConceptSCTFactory();

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(ConceptSCTFactory.class);

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private ConceptSCTFactory() {

    }

    public static ConceptSCTFactory getInstance() {
        return instance;
    }

    public ConceptSCT createFromJSON(@NotNull String jsonExpression){

        ConceptSCT conceptSCT;
        ObjectMapper mapper = new ObjectMapper();
        try {
            conceptSCT = mapper.readValue(underScoreToCamelCaseJSON(jsonExpression), ConceptSCT.class);
        } catch (IOException e) {
            String errorMsg = "Error al parsear un JSON hacia un ConceptSCT";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return conceptSCT;
    }

    public List<ConceptSCT> createListFromJSON(@NotNull String jsonExpression){

        ConceptSCT[] conceptsSCT = new ConceptSCT[0];
        ObjectMapper mapper = new ObjectMapper();
        try {
            conceptsSCT = mapper.readValue(underScoreToCamelCaseJSON(jsonExpression), ConceptSCT[].class);
        } catch (IOException e) {
            String errorMsg = "Error al parsear un JSON hacia un ConceptSCT";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return Arrays.asList(conceptsSCT);
    }

}

