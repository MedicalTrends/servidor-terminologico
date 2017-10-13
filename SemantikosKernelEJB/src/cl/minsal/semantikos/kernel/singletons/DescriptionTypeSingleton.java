package cl.minsal.semantikos.kernel.singletons;

import cl.minsal.semantikos.model.descriptions.DescriptionType;

import javax.ejb.Lock;
import javax.ejb.Singleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static javax.ejb.LockType.READ;
import static javax.ejb.LockType.WRITE;

/**
 * @author Andrés Farías
 */
@Singleton
public class DescriptionTypeSingleton implements Serializable {


    public static final DescriptionType TYPELESS_DESCRIPTION_TYPE = new DescriptionType(-1, "Sin Tipo", "El tipo de descripcion sin tipo :).");

    public static final String FAVOURITE_DESCRIPTION_TYPE_NAME = "preferida";

    /** La lista de descripciones */
    private List<DescriptionType> descriptionTypes = new ArrayList<>();

    /** Mapa de Descripciones por su nombre. */
    private ConcurrentHashMap<String, DescriptionType> descriptionTypesByName = new ConcurrentHashMap<>();

    /** Mapa de Descripciones por su ID */
    private ConcurrentHashMap<Long, DescriptionType> descriptionTypesByID = new ConcurrentHashMap<>();

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    @Lock(READ)
    public DescriptionType getFSNDescriptionType() {

        if (descriptionTypesByName.containsKey("FSN")) {
            return this.descriptionTypesByName.get("FSN");
        }

        return new DescriptionType(-1, "FSN", "Full Specified Name");
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado Sinónimo.
     *
     * @return Retorna una instancia de Sinónimo.
     */
    @Lock(READ)
    public DescriptionType getSynonymDescriptionType() {

        if (descriptionTypesByName.containsKey("sinónimo")) {
            return this.descriptionTypesByName.get("sinónimo");
        }

        return new DescriptionType(-1, "sinónimo", "Sinónimo");
    }

    /**
     * Este método retorna la descripción preferida, si existe. De no existir crea una y la retorna
     *
     * @return El tipo de Descripción llamado Preferida.
     */
    @Lock(READ)
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
    @Lock(WRITE)
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

    @Lock(READ)
    public List<DescriptionType> getDescriptionTypes() {
        return descriptionTypes;
    }

    @Lock(READ)
    public DescriptionType getDescriptionTypeByID(long idDescriptionType) {

        if (this.descriptionTypesByID.containsKey(idDescriptionType)){
            return this.descriptionTypesByID.get(idDescriptionType);
        }

        throw new IllegalArgumentException("DescriptionType con ID=" + idDescriptionType + " no existe.");
    }

    @Lock(READ)
    public List<DescriptionType> getDescriptionTypesButFSNandFavorite() {

        List<DescriptionType> otherDescriptionTypes = new ArrayList<DescriptionType>();
        DescriptionType fsnType = getFSNDescriptionType();
        DescriptionType favoriteType = getFavoriteDescriptionType();

        for (DescriptionType descriptionType : getDescriptionTypes()) {
            if (!descriptionType.equals(fsnType) && !descriptionType.equals(favoriteType)) {
                otherDescriptionTypes.add(descriptionType);
            }
        }

        return otherDescriptionTypes;
    }

    @Lock(READ)
    public List<DescriptionType> getDescriptionTypesButFSN() {

        List<DescriptionType> otherDescriptionTypes = new ArrayList<DescriptionType>();
        DescriptionType fsnType = getFSNDescriptionType();

        for (DescriptionType descriptionType : getDescriptionTypes()) {
            if (!descriptionType.equals(fsnType)) {
                otherDescriptionTypes.add(descriptionType);
            }
        }

        return otherDescriptionTypes;
    }
}
