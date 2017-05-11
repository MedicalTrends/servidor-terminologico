package cl.minsal.semantikos.kernel.factories;

import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.queries.GeneralQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías
 */
public class QueryFactory {

    private static final QueryFactory instance = new QueryFactory();

    /** La lista de tagSMTK */
    private List<GeneralQuery> queries;

    /** Mapa de queries por id de categoría. */
    private Map<Long, GeneralQuery> queryMap;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private QueryFactory() {
        this.queries = new ArrayList<>();
        this.queryMap = new HashMap<>();
    }

    public static QueryFactory getInstance() {
        return instance;
    }

    /**
     * Este método es responsable de retornar la query dado el id de categoría.
     *
     * @return Retorna una instancia de query.
     */
    public GeneralQuery findQueryByCategory(Category category) {

        if (queryMap.containsKey(category.getId())) {
            return this.queryMap.get(category.getId());
        }

        return null;
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de queries. Al hacerlo, es necesario actualizar
     * los mapas.
     */
    public void setQueries( List<GeneralQuery> queries) {

        /* Se actualiza la lista */
        this.queries = queries;

        /* Se actualiza el mapa por ids */
        this.queryMap.clear();

        for (GeneralQuery query : queries) {
            this.queryMap.put(query.getCategories().get(0).getId(), query);
        }
    }

}
