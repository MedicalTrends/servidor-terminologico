package cl.minsal.semantikos.model.snomedct;

/**
 * @author Andrés Farías on 10/26/16.
 */
public enum DescriptionSCTType {

    SYNONYM(900000000000013009L,"Sinónimo"), FSN(900000000000003001L,"FSN"), PREFERRED(900000000000548007L,"Preferida"), ACCEPTABLE(900000000000549004L,"Aceptable");

    private long typeId;

    private String name;

    DescriptionSCTType(long typeId, String name) {
        this.typeId = typeId;
        this.name = name;
    }

    public static DescriptionSCTType valueOf(long typeId) throws Exception {
        if (FSN.typeId == typeId) {
            return FSN;
        } else if (SYNONYM.typeId == typeId) {
            return SYNONYM;
        } else if (PREFERRED.typeId == typeId) {
            return PREFERRED;
        } else if (ACCEPTABLE.typeId == typeId) {
            return ACCEPTABLE;
        }

        throw new Exception("Error parseando el valor del Description Type");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
