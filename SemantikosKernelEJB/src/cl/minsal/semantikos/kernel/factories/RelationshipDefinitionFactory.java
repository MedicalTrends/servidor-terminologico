package cl.minsal.semantikos.kernel.factories;

import cl.minsal.semantikos.kernel.daos.TargetDefinitionDAO;
import cl.minsal.semantikos.model.relationships.Multiplicity;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.TargetDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * @author Andrés Farías / Gustavo Punucura
 */
@Singleton
public class RelationshipDefinitionFactory {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(RelationshipDefinitionFactory.class);

    @EJB
    TargetDefinitionDAO targetDefinitionDAO;

    public RelationshipDefinition createFromJSON(String jsonResult) {

        String newJson = "[" + jsonResult + "]";
        return createRelDefinitionsFromJSON(newJson).get(0);
    }

    /**
     * Este método es responsable de crear una lista de relaciones a partir de un arreglo json de relaciones.
     *
     * @param jsonExpression La expresión que contiene un arreglo JSON de RD_DTO.
     *
     * @return Una lista de <code>RelationshipDefinition</code>.
     */
    public List<RelationshipDefinition> createRelDefinitionsFromJSON(String jsonExpression) {
        ObjectMapper mapper = new ObjectMapper();
        RelationshipDefinitionDTO[] relationshipDefinitionDTOs;
        try {

            relationshipDefinitionDTOs = mapper.readValue(underScoreToCamelCaseJSON(jsonExpression), RelationshipDefinitionDTO[].class);
        } catch (IOException e) {
            String errorMsg = "No se pudo parsear el RelationshipDefinition desde un JSON.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        List<RelationshipDefinition> relationshipDefinitions = new ArrayList<>();
        for (RelationshipDefinitionDTO relationshipDefinitionDTO : relationshipDefinitionDTOs) {

            TargetDefinition targetDefinition = targetDefinitionDAO.getTargetDefinitionById(relationshipDefinitionDTO.idTargetDefinition);
            Multiplicity multiplicity = new Multiplicity(relationshipDefinitionDTO.lowerBoundary, relationshipDefinitionDTO.upperBoundary);
            long id = relationshipDefinitionDTO.id;
            String name = relationshipDefinitionDTO.name;
            String description = relationshipDefinitionDTO.description;

            relationshipDefinitions.add(new RelationshipDefinition(id, name, description, targetDefinition, multiplicity));
        }

        return relationshipDefinitions;
    }

}

class RelationshipDefinitionDTO {

    protected long id;
    protected long idTargetDefinition;
    protected String name;
    protected String description;
    protected int lowerBoundary;
    protected int upperBoundary;

    public RelationshipDefinitionDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdTargetDefinition() {
        return idTargetDefinition;
    }

    public void setIdTargetDefinition(long idTargetDefinition) {
        this.idTargetDefinition = idTargetDefinition;
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

    public int getLowerBoundary() {
        return lowerBoundary;
    }

    public void setLowerBoundary(int lowerBoundary) {
        this.lowerBoundary = lowerBoundary;
    }

    public int getUpperBoundary() {
        return upperBoundary;
    }

    public void setUpperBoundary(int upperBoundary) {
        this.upperBoundary = upperBoundary;
    }
}

