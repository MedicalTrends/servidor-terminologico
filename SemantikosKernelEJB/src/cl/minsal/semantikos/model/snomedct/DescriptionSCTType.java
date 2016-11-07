package cl.minsal.semantikos.model.snomedct;

/**
 * @author Andrés Farías on 10/26/16.
 */
public enum DescriptionSCTType {

    SYNONYM(900000000000013009L), FSN(900000000000003001L);

    private long typeId;

    DescriptionSCTType(long typeId) {
        this.typeId = typeId;
    }

    public static DescriptionSCTType valueOf(long typeId) throws Exception {
        if (FSN.typeId == typeId) {
            return FSN;
        } else if (SYNONYM.typeId == typeId) {
            return SYNONYM;
        }

        throw new Exception("Error parseando el valor del Description Type");
    }
}
