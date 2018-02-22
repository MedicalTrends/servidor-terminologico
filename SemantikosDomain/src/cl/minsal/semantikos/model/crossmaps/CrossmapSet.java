package cl.minsal.semantikos.model.crossmaps;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.TargetDefinition;

import java.io.Serializable;

/**
 * Esta clase implementa el concepto de terminología externa
 * @author Andrés Farías on 11/3/16.
 */
public class CrossmapSet extends PersistentEntity implements TargetDefinition, Serializable {

    /** Identificador de negocio */
    //private long idCrossmapSet;

    private String abbreviatedName;

    private String name;

    public static final String CIE10 = "CIE-10";

    public static final String GMDN = "GMDN";

    /** Año de la versión */
    private int version;

    public CrossmapSet(String abbreviatedName, String name, int version, boolean state) {
        this.abbreviatedName = abbreviatedName;
        this.name = name;
        this.version = version;
        this.state = state;
    }

    public CrossmapSet(long id, String abbreviatedName, String name, int version, boolean state) {
        super(id);
        this.abbreviatedName = abbreviatedName;
        this.name = name;
        this.version = version;
        this.state = state;
    }

    public boolean isState() {
        return state;

    }

    public void setState(boolean state) {
        this.state = state;
    }

    private boolean state;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviatedName() {
        return abbreviatedName;
    }

    public void setAbbreviatedName(String abbreviatedName) {
        this.abbreviatedName = abbreviatedName;
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
        return false;
    }

    @Override
    public boolean isCrossMapType() {
        return true;
    }

    @Override
    public String getRepresentation() {
        return toString();
    }

    @Override
    public String toString() {
        return abbreviatedName;
    }
}
