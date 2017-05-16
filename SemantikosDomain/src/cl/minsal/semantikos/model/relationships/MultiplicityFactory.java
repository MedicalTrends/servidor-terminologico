package cl.minsal.semantikos.model.relationships;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author Andrés Farías
 */
public class MultiplicityFactory implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(MultiplicityFactory.class);

    public static final Multiplicity ONE_TO_MANY = new MultiplicityFactory().createMultiplicityByUML("1-N");

    public static final Multiplicity ONE_TO_ONE = new MultiplicityFactory().createMultiplicityByUML("1-1");

    /**
     * Este método es responsable de crear una multiplicidad, a partir de una expresión UML.
     * <p>Ejemplo: <code>1-N</code>, o <code>1-1</code>, o <code>0-N</code></p>
     *
     * @param umlMultiplicity La expresión de multiplicidad UML.
     *
     * @return Un objeto <code>Multiplicity</code>.
     */
    public Multiplicity createMultiplicityByUML(String umlMultiplicity) {
        int separator = umlMultiplicity.indexOf('-');

        /* Debe haber un separador '-' */
        if (separator < 0) {
            String errorMsg = "La expresión " + umlMultiplicity + " no satisface el formato UML para multiplicidad.";
            logger.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        String lowerBoundary = umlMultiplicity.substring(0, separator);
        String upperBoundary = umlMultiplicity.substring(separator+1);

        int theLowerBoundary = interpretLimit(lowerBoundary);
        int theUpperBoundary = interpretLimit(upperBoundary);

        return new Multiplicity(theLowerBoundary, theUpperBoundary);
    }

    protected int interpretLimit( String lowerBoundary) {

        if (lowerBoundary.equalsIgnoreCase("N") || lowerBoundary.equalsIgnoreCase("*")) {
            return 0;
        }

        int theBoundary;
        try {
            theBoundary = Integer.parseInt(lowerBoundary);
        } catch (NumberFormatException nfe) {
            String errorMsg = "La expresion no contiene numeros: " + lowerBoundary;
            logger.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        return theBoundary;
    }
}
