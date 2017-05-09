package cl.minsal.semantikos.model.snomedct;

import cl.minsal.semantikos.model.MultiplicityFactory;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

/**
 * @author Andrés Farías on 9/26/16.
 */
public class SnomedCTRelationships {

    public RelationshipDefinition ES_UN_MAPEO_DE = new RelationshipDefinition("Es un mapeo de", "Relación de mapeo a Snomed-CT.", MultiplicityFactory.ONE_TO_MANY, new SnomedCT("1.0"));
}
