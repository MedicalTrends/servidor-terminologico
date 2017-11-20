package cl.minsal.semantikos.model.dtos;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;

import java.io.Serializable;

/**
 * @author Andrés Farías on 11/3/16.
 */
public class CrossmapSetMemberDTO implements TargetDTO, Serializable {

    /** ID de negocio */
    private long id;

    /** Terminología a la que pertenece */
    private long crossmapSetId;

    private String code;

    private String gloss;
    private String code1;

    public CrossmapSetMemberDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCrossmapSetId() {
        return crossmapSetId;
    }

    public void setCrossmapSetId(long crossmapSetId) {
        this.crossmapSetId = crossmapSetId;
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

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }
}
