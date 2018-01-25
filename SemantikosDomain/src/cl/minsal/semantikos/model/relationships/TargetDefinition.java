package cl.minsal.semantikos.model.relationships;

import cl.minsal.semantikos.model.IPersistentEntity;

/**
 * @author Andres Farias
 */
public interface TargetDefinition extends IPersistentEntity {

    public static final String COMERCIALIZADO = "Comercializado";

    public static final String ATC = "ATC";

    public static final String ISP = "ISP";

    public static final String BIOEQUIVALENTE = "Bioequivalente";

    public static final String U_ASIST = "Unidad de UAsist";

    public static final String CONDICION_DE_VENTA = "Condición de Venta";

    public static final String PEDIBLE = "Pedible";

    public static final String SNOMED_CT = "SNOMED CT";

    public static final String SUSTANCIA = "Sustancia";

    public static final String MC_SPECIAL = "Medicamento Clínico Especial";

    public static final String GTINGS1 = "Número GTIN";

    /**
     * Este metodo es responsable de determinar si el target type es de tipo básico es o no.
     *
     * @return <code>true</code> si es de tipo básico y <code>false</code> sino.
     */
    public boolean isBasicType();

    /**
     * Este método es responsable de determinar si el target type es de tipo SMTK o no.
     *
     * @return <code>true</code> si es de tipo SMTK y <code>false</code> si no.
     */

    public boolean isSMTKType();

    /**
     * Este método es responsable de determinar si el target type es de tipo Tabla Auxiliar o no.
     *
     * @return <code>true</code> si es de tipo Tabla Auxiliar y <code>false</code> si no.
     */
    public boolean isHelperTable();

    /**
     * Este método es responsable de determinar si el target type es de tipo Concept SCT o no.
     *
     * @return <code>true</code> si es de tipo Snomed CT y <code>false</code> si no.
     */
    public boolean isSnomedCTType();

    /**
     * Este método es responsable de determinar si el target type es de tipo CrossMap o no.
     *
     * @return <code>true</code> si es de tipo CrossMap y <code>false</code> si no.
     */
    public boolean isCrossMapType();


    public String getRepresentation();

}