package cl.minsal.semantikos.model.exceptions;


import cl.minsal.semantikos.model.ConceptSMTK;

/**
 * @author Andrés Farias
 */
public class BusinessRuleException extends RuntimeException {

    /** El código de la regla de negocio violada */
    private String code;

    /** El concepto en el que se ha violado la regla de negocio */
    private ConceptSMTK conceptSMTK;

    /**
     * Un constructor básico para la regla de negocio.
     *
     * @param code         El código de la regla de negocio.
     * @param errorMessage El mensaje de error de la regla de negocio.
     */
    public BusinessRuleException(String code, String errorMessage) {
        super(errorMessage);
        this.code = code;
    }

    /**
     * @param code         El código de la regla de negocio.
     * @param errorMessage El mensaje de error de la regla de negocio.
     * @param conceptSMTK  El concepto que viola la regla de negocio.
     */
    public BusinessRuleException(String code, String errorMessage, ConceptSMTK conceptSMTK) {
        this(code, errorMessage);
        this.conceptSMTK = conceptSMTK;
    }

    public ConceptSMTK getConceptSMTK() {
        return conceptSMTK;
    }
}
