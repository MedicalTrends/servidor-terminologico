package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.tags.TagSMTK;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Los Tag Semántikos son una agrupación de contenido que permite agrupar los conceptos de manera independiente de la
 * Categorías a la que pertenecen.
 *
 * Existe o se ha definido un Tag Semántikos por defecto para cada categoría.
 * Los Tags Semántikos se almacenan en la BDD y por ende uno es "real" si existe en la BDD.
 *
 * @author Andrés Farías on 9/4/16.
 */
@Local
public interface TagSMTKManager {

    /**
     * Este método es responsable de recuperar los Tag Semántikos persistidos en la BDD.
     *
     * @return La lista de Tag Semántikos.
     */
    public List<TagSMTK> getAllTagSMTKs();

    /**
     * Este método es responsable de recuperar un Tag SMTK por su id.
     *
     * @param idTag Identificador único del Tag Semántikos.
     *
     * @return El Tag Semántikos a retornar.
     */
    public TagSMTK findTagSTMKByID(@NotNull long idTag);
}
