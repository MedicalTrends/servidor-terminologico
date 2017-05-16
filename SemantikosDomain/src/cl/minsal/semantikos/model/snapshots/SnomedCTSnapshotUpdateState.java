package cl.minsal.semantikos.model.snapshots;

import cl.minsal.semantikos.model.PersistentEntity;

import java.io.Serializable;

/**
 * @author Diego Soto
 */
public class SnomedCTSnapshotUpdateState extends PersistentEntity implements Serializable {

    private boolean conceptsProcessed = false;

    private int conceptsFileLine = 0;

    private boolean descriptionsProcessed = false;

    private int descriptionsFileLine = 0;

    private boolean relationshipsProcessed = false;

    private int relationshipsFileLine = 0;

    private boolean refsetsProcessed = false;

    private int refsetsFileLine = 0;

    private boolean transitivesProcessed = false;

    private int transitivesFileLine = 0;

    public boolean isConceptsProcessed() {
        return conceptsProcessed;
    }

    public void setConceptsProcessed(boolean conceptsProcessed) {
        this.conceptsProcessed = conceptsProcessed;
    }

    public int getConceptsFileLine() {
        return conceptsFileLine;
    }

    public void setConceptsFileLine(int conceptsFileLine) {
        this.conceptsFileLine = conceptsFileLine;
    }

    public boolean isDescriptionsProcessed() {
        return descriptionsProcessed;
    }

    public void setDescriptionsProcessed(boolean descriptionsProcessed) {
        this.descriptionsProcessed = descriptionsProcessed;
    }

    public int getDescriptionsFileLine() {
        return descriptionsFileLine;
    }

    public void setDescriptionsFileLine(int descriptionsFileLine) {
        this.descriptionsFileLine = descriptionsFileLine;
    }

    public boolean isRelationshipsProcessed() {
        return relationshipsProcessed;
    }

    public void setRelationshipsProcessed(boolean relationshipsProcessed) {
        this.relationshipsProcessed = relationshipsProcessed;
    }

    public int getRelationshipsFileLine() {
        return relationshipsFileLine;
    }

    public void setRelationshipsFileLine(int relationshipsFileLine) {
        this.relationshipsFileLine = relationshipsFileLine;
    }

    public boolean isRefsetsProcessed() {
        return refsetsProcessed;
    }

    public void setRefsetsProcessed(boolean refsetsProcessed) {
        this.refsetsProcessed = refsetsProcessed;
    }

    public int getRefsetsFileLine() {
        return refsetsFileLine;
    }

    public void setRefsetsFileLine(int refsetsFileLine) {
        this.refsetsFileLine = refsetsFileLine;
    }

    public boolean isTransitivesProcessed() {
        return transitivesProcessed;
    }

    public void setTransitivesProcessed(boolean transitivesProcessed) {
        this.transitivesProcessed = transitivesProcessed;
    }

    public int getTransitivesFileLine() {
        return transitivesFileLine;
    }

    public void setTransitivesFileLine(int transitivesFileLine) {
        this.transitivesFileLine = transitivesFileLine;
    }
}
