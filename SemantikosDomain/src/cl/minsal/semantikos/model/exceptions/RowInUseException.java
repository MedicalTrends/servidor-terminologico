package cl.minsal.semantikos.model.exceptions;

import cl.minsal.semantikos.model.ConceptSMTK;

import java.util.List;

/**
 * Created by root on 12-05-17.
 */
public class RowInUseException extends Exception{

    private List<ConceptSMTK> concepts;

    public RowInUseException(List<ConceptSMTK> concepts) {
        this.concepts = concepts;
    }

    public List<ConceptSMTK> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<ConceptSMTK> concepts) {
        this.concepts = concepts;
    }
}
