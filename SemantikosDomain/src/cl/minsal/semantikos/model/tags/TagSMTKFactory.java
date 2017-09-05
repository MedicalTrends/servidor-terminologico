package cl.minsal.semantikos.model.tags;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andrés Farías
 */
public class TagSMTKFactory implements Serializable {

    private static final TagSMTKFactory instance = new TagSMTKFactory();

    /** La lista de tagSMTK */
    private List<TagSMTK> tagsSMTK;

    public ConcurrentHashMap<String, TagSMTK> getTagsSMTKByName() {
        return tagsSMTKByName;
    }

    public List<TagSMTK> getTagsSMTK() {

        return tagsSMTK;
    }

    public void setTagsSMTKByName(ConcurrentHashMap<String, TagSMTK> tagsSMTKByName) {
        this.tagsSMTKByName = tagsSMTKByName;
    }

    public void setTagsSMTKById(ConcurrentHashMap<Long, TagSMTK> tagsSMTKById) {
        this.tagsSMTKById = tagsSMTKById;
    }

    /** Mapa de tagSMTK por su nombre. */
    private ConcurrentHashMap<String, TagSMTK> tagsSMTKByName;

    /** Mapa de tagSMTK por su nombre. */
    private ConcurrentHashMap<Long, TagSMTK> tagsSMTKById;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private TagSMTKFactory() {
        this.tagsSMTK = new ArrayList<>();
        this.tagsSMTKByName = new ConcurrentHashMap<>();
        this.tagsSMTKById = new ConcurrentHashMap<>();
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

        if (tagsSMTKByName.containsKey(name.toLowerCase())) {
            return this.tagsSMTKByName.get(name.toLowerCase());
        }

        return null;
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    public TagSMTK findTagSMTKById(long id) {

        if (tagsSMTKById.containsKey(id)) {
            return this.tagsSMTKById.get(id);
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
            this.tagsSMTKByName.put(tagSMTK.getName().toLowerCase(), tagSMTK);
            this.tagsSMTKById.put(tagSMTK.getId(), tagSMTK);
        }
    }

    public TagSMTK assertTagSMTK(String term) {

        Matcher m = Pattern.compile("\\((.*?)\\)").matcher(term);

        TagSMTK tagSMTK = null;

        while(m.find()) {
            if(findTagSMTKByName(m.group(1))!=null) {
                tagSMTK = findTagSMTKByName(m.group(1));
            }
        }

        return tagSMTK;
    }

}
