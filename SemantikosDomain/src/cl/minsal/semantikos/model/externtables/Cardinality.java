package cl.minsal.semantikos.model.externtables;

/**
 * @author Andrés Farías
 */
public enum Cardinality {

    ONE_TO_ONE(1, "1-1"),
    ONE_TO_MANY(2, "1-N"),
    MANY_TO_MANY(3, "N-N");

    private long id;

    private String typeName;

    Cardinality(long id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static Cardinality getValue(String expr){

        switch (expr) {
            case "1-1":
                return ONE_TO_ONE;
            case "1-N":
                return ONE_TO_MANY;
            case "N-N":
                return MANY_TO_MANY;

        }

        throw new IllegalArgumentException("No existe una cardinalidad para la expresión: " + expr);
    }

}
