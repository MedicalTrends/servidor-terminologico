package cl.minsal.semantikos.model.crossmaps;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;

import java.sql.Timestamp;

/**
 * @author Andrés Farías on 11/3/16.
 */
public class CrossmapSetMember extends PersistentEntity implements Target {

    /** ID de negocio */
    private long idCrossmapSetMember;

    /** Terminología a la que pertenece */
    private CrossmapSet crossmapSet;

    private String code;

    private String gloss;

    public CrossmapSetMember(long idCrossmapSetMember, CrossmapSet crossmapSet, String code, String gloss) {
        this.idCrossmapSetMember = idCrossmapSetMember;
        this.crossmapSet = crossmapSet;
        this.code = code;
        this.gloss = gloss;
    }

    public CrossmapSetMember(long id, long idCrossmapSetMember, CrossmapSet crossmapSet, String code, String gloss) {
        super(id);
        this.idCrossmapSetMember = idCrossmapSetMember;
        this.crossmapSet = crossmapSet;
        this.code = code;
        this.gloss = gloss;
    }

    public long getIdCrossmapSetMember() {
        return idCrossmapSetMember;
    }

    public void setIdCrossmapSetMember(long idCrossmapSetMember) {
        this.idCrossmapSetMember = idCrossmapSetMember;
    }

    public CrossmapSet getCrossmapSet() {
        return crossmapSet;
    }

    public void setCrossmapSet(CrossmapSet crossmapSet) {
        this.crossmapSet = crossmapSet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGloss() {
        return gloss;
    }

    public void setGloss(String gloss) {
        this.gloss = gloss;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.CrossMap;
    }

    @Override
    public String getRepresentation() {

        return this.getCrossmapSet().getAbbreviatedName()+" - Código Crossmap: ("+this.getCode()+") - "+this.getGloss();

    }

    // Métodos para soportar conversión automática
    @Override
    public boolean equals(Object other) {
        return (other instanceof CrossmapSetMember) && (String.valueOf(idCrossmapSetMember) != null)
                ? String.valueOf(idCrossmapSetMember).equals(String.valueOf(((CrossmapSetMember) other).idCrossmapSetMember))
                : (other == this);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode() + new Long(idCrossmapSetMember).hashCode();
    }

    @Override
    public String toString() {
        return Long.toString(idCrossmapSetMember);
    }


    @Override
    public Target copy() {
        return new CrossmapSetMember(this.getIdCrossmapSetMember(), this.getIdCrossmapSetMember(), this.getCrossmapSet(), this.getCode(), this.getGloss());
    }
}
