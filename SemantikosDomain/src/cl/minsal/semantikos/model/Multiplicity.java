package cl.minsal.semantikos.model;

/**
 * Esta clase representa una multiplicidad a lo UML: [1-2] [0-100] [0-*]
 */
public class Multiplicity {

    /** Limite inferior */
    private int lowerBoundary;

    /** Limite superior */
    private int upperBoundary;

    /**
     * Constructor por defecto.
     *
     * @param lowerBoundary Límite inferior
     * @param upperBoundary Límite superior
     */
    public Multiplicity(int lowerBoundary, int upperBoundary) {
        this.lowerBoundary = lowerBoundary;
        this.upperBoundary = upperBoundary;
    }

    public int getUpperBoundary() {
        return upperBoundary;
    }

    public void setUpperBoundary(int upperBoundary) {
        this.upperBoundary = upperBoundary;
    }

    public int getLowerBoundary() {
        return lowerBoundary;
    }

    public void setLowerBoundary(int lowerBoundary) {
        this.lowerBoundary = lowerBoundary;
    }

    public boolean isCollection(){
        return (this.upperBoundary!=1);
    }

    public boolean isSimple(){ return (this.upperBoundary==1); }

    public boolean isOptional(){ return this.lowerBoundary==0; }

    public boolean isUpperBoundaryReached(int cardinality){
        return ((cardinality>=this.getUpperBoundary()) && this.getUpperBoundary()!=0);
    }

    public boolean isRequired(int cardinality){
        return ((cardinality<this.getLowerBoundary()));
    }

    public int getLowerBoundaryView(){
        return Math.max(1,this.getLowerBoundary());
    }
}
