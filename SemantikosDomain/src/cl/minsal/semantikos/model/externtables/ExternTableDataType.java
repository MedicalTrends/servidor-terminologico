package cl.minsal.semantikos.model.externtables;

/**
 * @author Andrés Farías
 */
public enum ExternTableDataType {

    STRING_TYPE(1, "string"),
    BOOLEAN_TYPE(2, "boolean"),
    INTEGER_TYPE(3, "int"),
    FLOAT_TYPE(4, "float"),
    DATE_TYPE(5, "date");

    private long id;

    private String typeName;

    ExternTableDataType(long id, String typeName) {
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

    public static ExternTableDataType valueOf(long id){
        for (ExternTableDataType basicTypeType : values()) {
            if (basicTypeType.getId()==id){
                return basicTypeType;
            }
        }

        throw new IllegalArgumentException("No existe el tipo de ID=" + id);
    }

}
