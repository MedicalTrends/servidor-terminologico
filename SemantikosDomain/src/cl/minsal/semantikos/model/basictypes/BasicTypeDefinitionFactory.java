package cl.minsal.semantikos.model.basictypes;

import cl.minsal.semantikos.model.relationships.BasicTypeType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.ejb.Singleton;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;
import static java.util.Arrays.asList;

/**
 * @author Andrés Farías & Gustavo Punucura.
 */
@Singleton
public class BasicTypeDefinitionFactory {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(BasicTypeDefinitionFactory.class);

    /**
     * Crea un BasicTypeDefinition en su forma básica: sin dominio ni intervalos
     *
     * @param jsonResult El JSON a partir del cual se crea el dominio.
     *
     * @return Un BasicTypeDefinition básico, sin dominio ni intervalos.
     */
    public BasicTypeDefinition createSimpleBasicTypeDefinitionFromJSON(String jsonResult) {

        /* Se parsea el JSON y se lleva a un DTO */
        ObjectMapper mapper = new ObjectMapper();
        BasicTypeDefinitionDTO basicTypeDefinitionDTO;
        try {
            basicTypeDefinitionDTO = mapper.readValue(underScoreToCamelCaseJSON(jsonResult), BasicTypeDefinitionDTO.class);
        } catch (IOException e) {
            String errorMsg = "No se pudo parsear el TargetDefinition desde un JSON.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        /* El DTO se utiliza para crear la instancia final */
        long idBasicType = basicTypeDefinitionDTO.id;
        String nameBasicType = basicTypeDefinitionDTO.name;
        String descriptionBasicType = basicTypeDefinitionDTO.description;
        BasicTypeType basicTypeType = BasicTypeType.valueOf(basicTypeDefinitionDTO.idType);

        switch (basicTypeType) {

            case STRING_TYPE:
                BasicTypeDefinition<String> stringBasicTypeDefinition;
                stringBasicTypeDefinition = new BasicTypeDefinition<>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);

                String[] domain = basicTypeDefinitionDTO.getDomain();
                if (domain != null) {
                    stringBasicTypeDefinition.setDomain(asList(domain));
                } else {
                    stringBasicTypeDefinition.setDomain(new ArrayList<String>());
                }
                return stringBasicTypeDefinition;

            case BOOLEAN_TYPE:
                return new BasicTypeDefinition<Boolean>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);
            case INTEGER_TYPE:
                return new BasicTypeDefinition<Integer>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);
            case FLOAT_TYPE:
                return new BasicTypeDefinition<Float>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);
            case DATE_TYPE:
                return new BasicTypeDefinition<Timestamp>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);
            default:
                throw new IllegalArgumentException("TODO");
        }
    }

}

class BasicTypeDefinitionDTO {
    protected long id;
    protected String name;
    protected String description;
    protected long idType;
    protected String typeName;
    protected String[] domain;
    protected String[] interval;

    public BasicTypeDefinitionDTO() {
    }

    public String[] getInterval() {
        return interval;
    }

    public void setInterval(String[] interval) {
        this.interval = interval;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String[] getDomain() {
        return domain;
    }

    public void setDomain(String[] domain) {
        this.domain = domain;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getIdType() {
        return idType;
    }

    public void setIdType(long idType) {
        this.idType = idType;
    }
}
