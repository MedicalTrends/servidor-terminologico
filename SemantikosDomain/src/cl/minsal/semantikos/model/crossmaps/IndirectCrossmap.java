package cl.minsal.semantikos.model.crossmaps;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Andrés Farías on 11/3/16.
 */
public class IndirectCrossmap extends Crossmap implements Serializable {

    private long idSnomedCT;

    private long idCrossmapSet;

    private int mapGroup;

    private int mapPriority;

    private String mapRule;

    private String mapAdvice;

    private String mapTarget;

    private long correlation;

    private long idCrossmapCategory;

    private boolean state;

    public IndirectCrossmap(ConceptSMTK sourceConcept, CrossmapSetRecord target, RelationshipDefinition relationshipDefinition, Timestamp validityUntil) {
        super(sourceConcept, target, relationshipDefinition, validityUntil);
    }

    public IndirectCrossmap(@NotNull long id, @NotNull ConceptSMTK sourceConcept, @NotNull CrossmapSetMember target, @NotNull RelationshipDefinition relationshipDefinition, Timestamp validityUntil) {
        super(id, sourceConcept, target, relationshipDefinition, validityUntil);
    }

    @Override
    public boolean is(CrossMapType indirect) {
        return indirect.equals(CrossMapType.INDIRECT);
    }

    public long getIdCrossmapSet() {
        return idCrossmapSet;
    }

    public void setIdCrossmapSet(long idCrossmapSet) {
        this.idCrossmapSet = idCrossmapSet;
    }


    public long getIdSnomedCT() {
        return idSnomedCT;
    }

    public void setIdSnomedCT(long idSnomedCT) {
        this.idSnomedCT = idSnomedCT;
    }

    public int getMapGroup() {
        return mapGroup;
    }

    public void setMapGroup(int mapGroup) {
        this.mapGroup = mapGroup;
    }

    public int getMapPriority() {
        return mapPriority;
    }

    public void setMapPriority(int mapPriority) {
        this.mapPriority = mapPriority;
    }

    public String getMapRule() {
        return mapRule;
    }

    public void setMapRule(String mapRule) {
        this.mapRule = mapRule;
    }

    public String getMapAdvice() {
        return mapAdvice;
    }

    public void setMapAdvice(String mapAdvice) {
        if(mapAdvice!=null)this.mapAdvice = mapAdvice;
    }

    public String getMapTarget() {
        return mapTarget;
    }

    public void setMapTarget(String mapTarget) {
        this.mapTarget = mapTarget;
    }

    public long getCorrelation() {
        return correlation;
    }

    public void setCorrelation(long correlation) {
        this.correlation = correlation;
    }

    public long getIdCrossmapCategory() {
        return idCrossmapCategory;
    }

    public void setIdCrossmapCategory(long idCrossmapCategory) {
        this.idCrossmapCategory = idCrossmapCategory;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }


    @Override
    public String toString() {
        return "IndirectCrossmap{" +
                "mapGroup=" + mapGroup +
                ", mapPriority=" + mapPriority +
                ", mapRule='" + mapRule + '\'' +
                ", mapAdvice='" + mapAdvice + '\'' +
                ", mapTarget='" + mapTarget + '\'' +
                ", correlation=" + correlation +
                ", idCrossmapCategory=" + idCrossmapCategory +
                ", state=" + state +
                '}';
    }
}
