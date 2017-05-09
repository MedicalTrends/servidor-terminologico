package cl.minsal.semantikos.model.relationships;

import cl.minsal.semantikos.model.snomedct.SnomedCT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import java.io.IOException;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;

@Singleton
public class TargetDefinitionFactory {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(TargetDefinitionFactory.class);

    private static final int BASIC_TYPE_ID = 1;
    private static final int SMTK_TYPE_ID = 2;
    private static final int SCT_TYPE_ID = 3;
    private static final int HELPER_TABLE_TYPE_ID = 4;
    private static final int CROSSMAP_TYPE_ID = 5;

    @EJB
    private BasicTypeDefinitionDAO basicTypeDefinitionDAO;

    @EJB
    private CategoryDAO categoryDAO;

    @EJB
    private ConceptSCTDAO conceptSCTDAO;

    @EJB
    private HelperTableDAO helperTableDAO;

    @EJB
    private CrossmapsDAO crossmapsDAO;

    public TargetDefinition createFromJSON(String jsonResult) {

        logger.debug("creando target definition de json: {}", jsonResult);

        /* Se parsea el JSON y se lleva a un DTO */
        ObjectMapper mapper = new ObjectMapper();
        TargetDefinitionDTO targetDefinitionDTO;
        try {
            targetDefinitionDTO = mapper.readValue(underScoreToCamelCaseJSON(jsonResult), TargetDefinitionDTO.class);
        } catch (IOException e) {
            String errorMsg = "No se pudo parsear el TargetDefinition desde un JSON.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        /* El DTO se utiliza para crear la instancia final */
        switch ((int) targetDefinitionDTO.idTargetType) {

            case BASIC_TYPE_ID:
                return basicTypeDefinitionDAO.getBasicTypeDefinitionById(targetDefinitionDTO.idBasicType);

            case SMTK_TYPE_ID:
                return categoryDAO.getCategoryById(targetDefinitionDTO.idCategory);

            case SCT_TYPE_ID:
                return new SnomedCT("1.0");

            case HELPER_TABLE_TYPE_ID:
                return helperTableDAO.getHelperTableByID(targetDefinitionDTO.idHelperTableName);

            case CROSSMAP_TYPE_ID:
                return crossmapsDAO.getCrossmapSetByID(targetDefinitionDTO.idExternTableName);

            default:
                throw new EJBException("TIPO DE DEFINICION INCORRECTO. ID Target Type=" + targetDefinitionDTO.idTargetType);
        }
    }
}

/**
 * DTO utilizado para convertir los resultados de la base de datos.
 */
class TargetDefinitionDTO {

    protected long id;
    protected String name;
    protected long idCategory;
    protected long idHelperTableName;
    protected long idExternTableName;
    protected long idTargetType;
    protected long idBasicType;
    protected boolean sctType;

    public TargetDefinitionDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(long idCategory) {
        this.idCategory = idCategory;
    }

    public long getIdHelperTableName() {
        return idHelperTableName;
    }

    public void setIdHelperTableName(long idHelperTableName) {
        this.idHelperTableName = idHelperTableName;
    }

    public long getIdExternTableName() {
        return idExternTableName;
    }

    public void setIdExternTableName(long idExternTableName) {
        this.idExternTableName = idExternTableName;
    }

    public long getIdTargetType() {
        return idTargetType;
    }

    public void setIdTargetType(long idTargetType) {
        this.idTargetType = idTargetType;
    }

    public long getIdBasicType() {
        return idBasicType;
    }

    public void setIdBasicType(long idBasicType) {
        this.idBasicType = idBasicType;
    }

    public boolean isSctType() {
        return sctType;
    }

    public void setSctType(boolean isSctType) {
        this.sctType = isSctType;
    }

    @Override
    public String toString() {
        return "TargetDefinitionDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
