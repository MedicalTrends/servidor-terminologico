package cl.minsal.semantikos.model.snomedct;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.TargetDefinition;

import java.io.Serializable;

/**
 * Esta clase, representa la terminología internacional estándar SNOMED CT.
 *
 * @author Andrés Farías
 */
public class SnomedCT extends PersistentEntity implements TargetDefinition, Serializable {

    /** Descripción del concepto */
    private String version;

    public SnomedCT(String version) {
        super();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean isBasicType() {
        return false;
    }

    @Override
    public boolean isSMTKType() {
        return false;
    }

    @Override
    public boolean isHelperTable() {
        return false;
    }

    @Override
    public boolean isSnomedCTType() {
        return true;
    }

    @Override
    public boolean isCrossMapType() {
        return false;
    }

    @Override
    public String getRepresentation() {
        return "ConceptID SNM ¦ Preferido SNM";
    }
}
