package cl.minsal.semantikos.model.snomedct;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetDefinition;
import cl.minsal.semantikos.model.relationships.TargetType;

import java.util.ArrayList;
import java.util.List;

import static cl.minsal.semantikos.model.relationships.TargetType.SnomedCT;

/**
 * Esta clase, representa la terminología internacional estándar SNOMED CT.
 *
 * @author Andrés Farías
 */
public class SnomedCT extends PersistentEntity implements TargetDefinition{

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
}
