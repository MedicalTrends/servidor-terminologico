package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.ws.modelws.response.SnomedCTRelationshipResponse;

/**
 * Created by Development on 2016-12-28.
 *
 */
public class SnomedCTRelationshipMapper {

   public static SnomedCTRelationshipResponse map(SnomedCTRelationship relationship) throws Exception {
       if ( relationship != null && relationship.getTarget() != null ) {
           SnomedCTRelationshipResponse res = new SnomedCTRelationshipResponse();

           ConceptSCT conceptSCT = relationship.getTarget();
           res.setDescription(conceptSCT.getDescriptionFSN().getTerm());
           res.setId(String.valueOf(conceptSCT.getIdSnomedCT()));
           res.setType(relationship.getSnomedCTRelationshipType());
           res.setGroup(relationship.getGroup());

           return res;
       }
       return null;
   }

}
