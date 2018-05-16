package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.kernel.daos.DescriptionDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.List;

import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN_MAPEO_DE;

/**
 * @author Diego Soto
 */
@Singleton
public class RelelationshipCreationBRImpl implements RelationshipCreationBR {

    @EJB
    private HelperTablesManager helperTablesManager;

    @Override
    public void verifyPreConditions(Relationship relationship) {
        brCrossmap001(relationship);
    }

    @Override
    public void brCrossmap001(Relationship relationship) {
        // Si esta definición de relación es de tipo CROSSMAP, Se agrega el atributo tipo de relacion = "ES_UN_MAPEO_DE" (por defecto)
        if (relationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType()) {

            //Ya se agregó el atributo en la capa controlador
            if(relationship.getRelationshipTypeAttribute() != null) {
                return;
            }

            for (RelationshipAttributeDefinition attDef : relationship.getRelationshipDefinition().getRelationshipAttributeDefinitions()) {
                if (attDef.isRelationshipTypeAttribute()) {

                    HelperTable helperTable = (HelperTable) attDef.getTargetDefinition();

                    List<HelperTableRow> relationshipTypes = helperTablesManager.searchRows(helperTable, ES_UN_MAPEO_DE);

                    RelationshipAttribute ra = new RelationshipAttribute(attDef, relationship, relationshipTypes.get(0));
                    relationship.getRelationshipAttributes().add(ra);
                }
            }
        }
    }
}
