package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.NoValidDescription;
import cl.minsal.semantikos.model.descriptions.ObservationNoValid;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;


/**
 * @author Andrés Farías.
 */
@Local
public interface WSDAO {

    /**
     * Este método es responsable de buscar y retornar todas las descripciones que hagan perfect match con el término
     * dado como parámetro en cada una de las categorías y refsets indicadas.
     *
     * @return
     */
    List<Description> searchDescriptionsPerfectMatch(String term, Long[] categories, Long[] refsets, int page, int pageSize) throws IOException;

}
