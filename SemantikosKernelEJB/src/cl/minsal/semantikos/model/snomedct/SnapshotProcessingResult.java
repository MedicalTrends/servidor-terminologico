package cl.minsal.semantikos.model.snomedct;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * @author Andrés Farías on 9/26/16.
 */
public class SnapshotProcessingResult {

    /**
     * Este método es responsable de retornar los conceptos que se encuentran no vigentes respecto al estado anterior
     * de Snomed-CT.
     *
     * <p>Esto responde a la regla de negocio BR-SCT-001.</p>
     *
     * @return Una lista de los conceptos que se encuentran no vigentes.
     */
    public List<SnomedCT> getNewInvalidSCTConcepts() {
        return emptyList();
    }
}
