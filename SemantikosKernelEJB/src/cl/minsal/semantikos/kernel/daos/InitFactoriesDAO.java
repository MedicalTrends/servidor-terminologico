package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.DescriptionTypeFactory;
import cl.minsal.semantikos.model.HelperTableColumnFactory;
import cl.minsal.semantikos.model.TagSMTK;
import cl.minsal.semantikos.model.TagSMTKFactory;

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
