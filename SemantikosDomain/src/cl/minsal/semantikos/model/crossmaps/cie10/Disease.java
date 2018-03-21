package cl.minsal.semantikos.model.crossmaps.cie10;

import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;

import java.io.Serializable;

/**
 * @author Andrés Farías on 11/3/16.
 */
public class Disease extends CrossmapSetMember implements Serializable {


    /** Terminología a la que pertenece */
    private CrossmapSet crossmapSet;

    private String code;

    private String gloss;
    private String code1;

    public Disease(long idCrossmapSetMember, CrossmapSet crossmapSet, String code, String gloss) {
        super(idCrossmapSetMember);
        this.crossmapSet = crossmapSet;
        this.code = code;
        this.gloss = gloss;
    }

    public Disease(long id, long idCrossmapSetMember, CrossmapSet crossmapSet, String code, String gloss) {
        super(id);
        this.crossmapSet = crossmapSet;
        this.code = code;
        this.gloss = gloss;
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
        //return this.getCrossmapSet().getAbbreviatedName()+" - Código Crossmap: ("+this.getCode()+") - "+this.getGloss();
        return this.getGloss();

    }

    // Métodos para soportar conversión automática
    @Override
    public boolean equals(Object other) {
        return (other instanceof Disease) && (String.valueOf(getId()) != null)
                ? String.valueOf(getId()).equals(String.valueOf(((Disease) other).getId()))
                : (other == this);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode() + new Long(getId()).hashCode();
    }

    @Override
    public String toString() {
        //return Long.toString(idCrossmapSetMember);
        return gloss;
    }

    @Override
    public Target copy() {
        return new Disease(this.getId(), this.getId(), this.getCrossmapSet(), this.getCode(), this.getGloss());
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

}
