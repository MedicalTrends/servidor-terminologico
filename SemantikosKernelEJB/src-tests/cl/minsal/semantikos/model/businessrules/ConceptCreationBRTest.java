package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Profile;
import cl.minsal.semantikos.model.User;
import org.junit.Test;

import static cl.minsal.semantikos.model.CategoryFactory.getNullCategory;
import static cl.minsal.semantikos.model.ProfileFactory.MODELER_PROFILE;

public class ConceptCreationBRTest {

    private ConceptCreationBR conceptCreationBRC = new ConceptCreationBR();

    private User IUserModeler = createUserProfile(MODELER_PROFILE);

    /**
     * Usuarios con rol de Diseñador o Modelador pueden crear conceptos de esta categoria.
     *
     * @throws Exception
     */
    @Test
    public void testBR001_02() throws Exception {
        conceptCreationBRC.br001creationRights(new ConceptSMTK(getNullCategory()), IUserModeler);
    }

    /**
     * Este método es responsable de crear un usuario con perfil Diseñador.
     *
     * @return Un usuario fresco de perfil diseñador.
     */
    private User createUserProfile(Profile profile) {
        User user = new User();
        user.addProfile(profile);
        return user;
    }
}