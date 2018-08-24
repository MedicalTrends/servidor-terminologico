package cl.minsal.semantikos.model.dtos;

import java.io.Serializable;

/**
 * @author Andrés Farías on 11/3/16.
 */
public class DiseaseDTO extends CrossmapSetMemberDTO implements Serializable {

    private String code;

    private String gloss;
    private String code1;

    public DiseaseDTO() {
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

    // Métodos para soportar conversión automática
    @Override
    public boolean equals(Object other) {
        return (other instanceof DiseaseDTO) && (String.valueOf(getId()) != null)
                ? String.valueOf(getId()).equals(String.valueOf(((DiseaseDTO) other).getId()))
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

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

}
