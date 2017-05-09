package cl.minsal.semantikos.model.relationships;

import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;

import java.util.List;

/**
 * @author Andrés Farías
 */
public enum TargetType {

    BasicType(1, "SCT", "Snomed CT"),
    SMTK(2, "SCT", "Snomed CT"),
    SnomedCT(3, "SCT", "Snomed CT"),
    HelperTable(4, "Helper Table", ""),
    CrossMap(5, "CrossMap", "Relaciones a terminologías externas");


    TargetType(long idTargetType, String nombre, String description) {
        this.idTargetType = idTargetType;
        this.nombre = nombre;
        this.description = description;
    }

    private long idTargetType;

    private String nombre;

    private String description;

    private List<TargetDefinition> targetDefinitions;

    private List<BasicTypeDefinition> basicTypeDefinitions;

    public long getIdTargetType() {
        return idTargetType;
    }

    public void setIdTargetType(long idTargetType) {
        this.idTargetType = idTargetType;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TargetDefinition> getTargetDefinitions() {
        return targetDefinitions;
    }

    public void setTargetDefinitions(List<TargetDefinition> targetDefinitions) {
        this.targetDefinitions = targetDefinitions;
    }

    public List<BasicTypeDefinition> getBasicTypeDefinitions() {
        return basicTypeDefinitions;
    }

    public void setBasicTypeDefinitions(List<BasicTypeDefinition> basicTypeDefinitions) {
        this.basicTypeDefinitions = basicTypeDefinitions;
    }
}
