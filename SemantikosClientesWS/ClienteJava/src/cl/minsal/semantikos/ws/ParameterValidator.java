package cl.minsal.semantikos.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase tiene como objetivo realizar validaciones sobre los parámetros, como por ejemplo, si son obligatorios,
 * etc.
 *
 * @author Andrés Farías on 10-01-17.
 */
public class ParameterValidator {

    private static final Logger logger = LoggerFactory.getLogger(ParameterValidator.class);

    /**
     * Este método tiene como responsabilidad validar que el parametro <code>parameter</code> no sea nulo.
     *
     * @param parameter El parametro a validar.
     * @throws IllegalArgumentException Śe arroja si el parámetro <code>parameter</code> es nulo o vacío.
     */
    public void required(String parameter) throws IllegalArgumentException {

        String validationMessage = "";
        if (!isProvided(parameter, validationMessage)){
            throw new IllegalArgumentException(validationMessage);
        }
    }

    /**
     * Este método es responsable de verificar que al menos uno de los parámetros <code>param1</code> y
     * <code>param2</code> han sido provistos de un valor no nulo y no vacío.
     *
     * @param param1 El primer parámetro.
     * @param param2 El segundo parámetro.
     */
    public void atLeastOneRequired(String param1, String param2) {

        /* Se verifica que al menos uno de ellos fue provisto */
        String validationMessage = "";
        if (!isProvided(param1, validationMessage) && !isProvided(param2, validationMessage)) {
            throw new IllegalArgumentException(validationMessage);
        }
    }

    /**
     * Este método es responsable de validar que un cierto parámetro ha sido provisto.
     *
     * @param parameter         El parámetro que se verifica.
     * @param validationMessage El mensaje que resulta de la validación.
     * @return <code>true</code> si el parametro ha sido provisto y <code>false</code> si no.
     */
    protected boolean isProvided(String parameter, String validationMessage) {

        /* Se verifica que el parámetro no sea nulo. */
        if (parameter == null) {
            validationMessage= "El parámetro es nulo.";
            logger.debug(validationMessage);
            return false;
        }

        if (parameter.trim().equals("")) {
            validationMessage= "El parámetro no es nulo pero está vacío.";
            logger.debug(validationMessage);
            return false;
        }

        validationMessage = "El parámetro tiene un valor no nulo y no vacío";
        logger.debug(validationMessage);
        return true;
    }
}
