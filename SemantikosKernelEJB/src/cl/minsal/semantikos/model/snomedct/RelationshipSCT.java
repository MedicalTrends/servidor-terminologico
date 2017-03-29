package cl.minsal.semantikos.model.snomedct;

/**
 * @author Andrés Farías on 9/26/16.
 */
public class RelationshipSCT implements ISnomedCT {

    /** Concepto origen de la relación */
    private String idSourceSCT;

    /** Concepto destino de la relación */
    private String idTargetSCT;

    /** Identificador único y numérico (de negocio) de la relación */
    private long idRelationship;

    @Override
    public void setId(long id) {

    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public boolean isPersistent() {
        return false;
    }
}
