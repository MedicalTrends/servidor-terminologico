package cl.minsal.semantikos.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías
 */
public class DescriptionTypeFactory {

    private static final DescriptionTypeFactory instance = new DescriptionTypeFactory();

    public static final DescriptionType TYPELESS_DESCRIPTION_TYPE = new DescriptionType(-1, "Sin Tipo", "El tipo de descripcion sin tipo :).");

    public static final String FAVOURITE_DESCRIPTION_TYPE_NAME = "preferida";

    /** La lista de descripciones */
    private List<DescriptionType> descriptionTypes;

    /** Mapa de Descripciones por su nombre. */
    private Map<String, DescriptionType> descriptionTypesByName;

    /** Mapa de Descripciones por su ID */
    private Map<Long, DescriptionType> descriptionTypesByID;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private DescriptionTypeFactory() {
        this.descriptionTypes = new ArrayList<>();
        this.descriptionTypesByID = new HashMap<>();
        this.descriptionTypesByName = new HashMap<>();
    }

    public static DescriptionTypeFactory getInstance() {
        return instance;
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    public DescriptionType getFSNDescriptionType() {

        if (descriptionTypesByName.containsKey("FSN")) {
            return this.descriptionTypesByName.get("FSN");
        }

        return new DescriptionType(-1, "FSN", "Full Specified Name");
    }

    /**
     * Este método retorna la descripción preferida, si existe. De no existir crea una y la retorna
     *
     * @return El tipo de Descripción llamado Preferida.
     */
    public DescriptionType getFavoriteDescriptionType() {
        if (descriptionTypesByName.containsKey(FAVOURITE_DESCRIPTION_TYPE_NAME)) {
            return this.descriptionTypesByName.get(FAVOURITE_DESCRIPTION_TYPE_NAME);
        } else {
            return new DescriptionType(-1, FAVOURITE_DESCRIPTION_TYPE_NAME, "Descripción Preferida (por defecto)");
        }
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de descripciones. Al hacerlo, es necesario actualizar
     * los
     * mapas de tipo de descripciones.
     */
    public void setDescriptionTypes( List<DescriptionType> descriptionTypes) {

        /* Se actualiza la lista */
        this.descriptionTypes = descriptionTypes;

        /* Se actualiza el mapa por nombres */
        this.descriptionTypesByName.clear();
        for (DescriptionType descriptionType : descriptionTypes) {
            this.descriptionTypesByName.put(descriptionType.getName(), descriptionType);
        }

        /* Se actualiza el mapa por ID's */
        this.descriptionTypesByID.clear();
        for (DescriptionType descriptionType : descriptionTypes) {
            this.descriptionTypesByID.put(descriptionType.getId(), descriptionType);
        }
    }

    public List<DescriptionType> getDescriptionTypes() {
        return descriptionTypes;
    }

    public DescriptionType getDescriptionTypeByID(long idDescriptionType) {

        if (this.descriptionTypesByID.containsKey(idDescriptionType)){
            return this.descriptionTypesByID.get(idDescriptionType);
        }

        throw new IllegalArgumentException("DescriptionType con ID=" + idDescriptionType + " no existe.");
    }

    public List<DescriptionType> getDescriptionTypesButFSNandFavorite() {

        List<DescriptionType> otherDescriptionTypes = new ArrayList<DescriptionType>();
        DescriptionType fsnType = DescriptionTypeFactory.getInstance().getFSNDescriptionType();
        DescriptionType favoriteType = DescriptionTypeFactory.getInstance().getFavoriteDescriptionType();

        for (DescriptionType descriptionType : getDescriptionTypes()) {
            if (!descriptionType.equals(fsnType) && !descriptionType.equals(favoriteType)) {
                otherDescriptionTypes.add(descriptionType);
            }
        }

        return otherDescriptionTypes;
    }

    public List<DescriptionType> getDescriptionTypesButFSN() {

        List<DescriptionType> otherDescriptionTypes = new ArrayList<DescriptionType>();
        DescriptionType fsnType = DescriptionTypeFactory.getInstance().getFSNDescriptionType();

        for (DescriptionType descriptionType : getDescriptionTypes()) {
            if (!descriptionType.equals(fsnType)) {
                otherDescriptionTypes.add(descriptionType);
            }
        }

        return otherDescriptionTypes;
    }
}
