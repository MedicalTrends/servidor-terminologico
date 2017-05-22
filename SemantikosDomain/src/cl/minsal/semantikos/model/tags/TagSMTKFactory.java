package cl.minsal.semantikos.model.tags;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías
 */
public class TagSMTKFactory implements Serializable {

    private static final TagSMTKFactory instance = new TagSMTKFactory();

    /** La lista de tagSMTK */
    private List<TagSMTK> tagsSMTK;

    /** Mapa de tagSMTK por su nombre. */
    private Map<String, TagSMTK> tagsSMTKByName;

    public List<TagSMTK> getTagsSMTK() {
        return tagsSMTK;
    }

    public Map<String, TagSMTK> getTagsSMTKByName() {
        return tagsSMTKByName;
    }

    public void setTagsSMTKByName(Map<String, TagSMTK> tagsSMTKByName) {
        this.tagsSMTKByName = tagsSMTKByName;
    }

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private TagSMTKFactory() {
        this.tagsSMTK = new ArrayList<>();
        this.tagsSMTKByName = new HashMap<>();
    }

    public TagSMTKFactory(List<TagSMTK> tagsSMTK, Map<String, TagSMTK> tagsSMTKByName) {
        this.tagsSMTK = tagsSMTK;
        this.tagsSMTKByName = tagsSMTKByName;
    }

    public static TagSMTKFactory getInstance() {
        return instance;
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    public TagSMTK findTagSMTKByName(String name) {

        if (tagsSMTKByName.containsKey(name)) {
            return this.tagsSMTKByName.get(name);
        }

        return null;
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de tagsSMTJ. Al hacerlo, es necesario actualizar
     * los mapas.
     */
    public void setTagsSMTK( List<TagSMTK> tagsSMTK) {

        /* Se actualiza la lista */
        this.tagsSMTK = tagsSMTK;

        /* Se actualiza el mapa por nombres */
        this.tagsSMTKByName.clear();
        for (TagSMTK tagSMTK : tagsSMTK) {
            this.tagsSMTKByName.put(tagSMTK.getName(), tagSMTK);
        }
    }

}
