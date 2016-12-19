package cl.minsal.semantikos.model.crossmaps;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;

/**
 * @author Andrés Farías on 11/3/16.
 */
public class CrossmapSetMember extends PersistentEntity implements Target {

    /** ID de negocio */
    private long idCrossmapSetMember;

    /** Terminología a la que pertenece */
    private CrossmapSet crossmapSet;

    private String code1;

    /** El segundo código */
    private String code2;

    private String gloss;

    public CrossmapSetMember(long idCrossmapSetMember, CrossmapSet crossmapSet, String code1, String gloss) {
        this.idCrossmapSetMember = idCrossmapSetMember;
        this.crossmapSet = crossmapSet;
        this.code1 = code1;
        this.gloss = gloss;
    }

    public CrossmapSetMember(long id, long idCrossmapSetMember, CrossmapSet crossmapSet, String code1, String code2, String gloss) {
        super(id);
        this.idCrossmapSetMember = idCrossmapSetMember;
        this.crossmapSet = crossmapSet;
        this.code1 = code1;
        this.code2 = code2;
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

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
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
        return toString();
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
        return new CrossmapSetMember(this.getIdCrossmapSetMember(), this.getIdCrossmapSetMember(), this.getCrossmapSet(), this.getCode1(), this.code2, this.getGloss());
    }
}
