package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.browser.QueryFactory;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Diego Soto on 22/02/17
 */
@Local
public interface InitFactoriesDAO {

    /**
     * Este método es responsable de retornar un Factory.
     *
     * @return El factory correspondiente
     */
    CategoryFactory refreshCategories();

    /**
     * Este método es responsable de retornar un Factory.
     *
     * @return El factory correspondiente
     */
    QueryFactory refreshQueries();

    /**
     * Este método es responsable de retornar un Factory.
     *
     * @return El factory adecuado... //TODO: WHAT?!
     */
    DescriptionTypeFactory refreshDescriptionTypes();

    /**
     * Este método es responsable de retornar un Factory.
     *
     * @return El factory correspondiente
     */
    TagSMTKFactory refreshTagsSMTK();

    /**
     * Este método es responsable de retornar un Factory.
     *
     * @return El factory correspondiente
     */
    HelperTableColumnFactory refreshColumns();
}
