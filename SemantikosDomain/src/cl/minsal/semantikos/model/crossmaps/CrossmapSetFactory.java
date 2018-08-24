package cl.minsal.semantikos.model.crossmaps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andrés Farías
 */
public class CrossmapSetFactory implements Serializable {

    private static final CrossmapSetFactory instance = new CrossmapSetFactory();

    /** La lista de crossmapsets */
    private List<CrossmapSet> crossmapSets;

    /** Mapa de categorías por su id. */
    private ConcurrentHashMap<Long, CrossmapSet> crossmapSetById;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private CrossmapSetFactory() {
        this.crossmapSets = new ArrayList<>();
        this.crossmapSetById = new ConcurrentHashMap<>();
    }

    public static CrossmapSetFactory getInstance() {
        return instance;
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    public CrossmapSet findCrossmapSetsById(long id) {

        if (crossmapSetById.containsKey(id)) {
            return this.crossmapSetById.get(id);
        }
        return null;
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de tagsSMTJ. Al hacerlo, es necesario actualizar
     * los mapas.
     */
    public void setCrossmapSets( List<CrossmapSet> crossmapSets) {

        /* Se actualiza la lista */
        this.crossmapSets = crossmapSets;

        /* Se actualiza el mapa por nombres */
        this.crossmapSetById.clear();

        for (CrossmapSet crossmapSet : crossmapSets) {
            this.crossmapSetById.put(crossmapSet.getId(), crossmapSet);
        }
    }

    public List<CrossmapSet> getCrossmapSets() {
        return crossmapSets;
    }

}
