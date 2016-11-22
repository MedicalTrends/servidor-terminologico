package cl.minsal.semantikos.model;

import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static cl.minsal.semantikos.model.CategoryFactory.getNullCategory;
import static org.junit.Assert.*;

public class ConceptSMTKTest {

    @Test
    public void testHasFavouriteDescription() throws Exception {

        ConceptSMTK conceptSMTK = new ConceptSMTK(getNullCategory());

        assertTrue(conceptSMTK.getDescriptions().isEmpty());
        assertFalse(conceptSMTK.hasFavouriteDescription());
    }

    @Test
    public void testHasFavouriteDescription02() throws Exception {

        ConceptSMTK conceptSMTK = new ConceptSMTK(getNullCategory());
        Description description = new Description(conceptSMTK, "Infarto Agudo al Miocardio", DescriptionTypeFactory.getInstance().getFavoriteDescriptionType());
        conceptSMTK.addDescription(description);

        assertEquals(1, conceptSMTK.getDescriptions().size());
        assertTrue(conceptSMTK.hasFavouriteDescription());
    }

    /**
     * Este test es importante: permite comparar relaciones.
     * @throws Exception
     */
    @Test
    public void testContains() throws Exception {
        ConceptSMTK conceptSMTK = new ConceptSMTK(getNullCategory());
        ConceptSCT theTarget = new ConceptSCT(10, new Timestamp(System.currentTimeMillis()), true, 1, 1);
        RelationshipDefinition relationshipDefinition = new RelationshipDefinition("RelTest", "Nada", MultiplicityFactory.ONE_TO_MANY, new Category());

        Relationship relationship1 = new Relationship(conceptSMTK, theTarget, relationshipDefinition,new ArrayList<RelationshipAttribute>(), null);
        conceptSMTK.addRelationship(relationship1);

        assertTrue(conceptSMTK.contains(relationship1));
    }

    /**
     * Este test es importante: permite comparar relaciones.
     * @throws Exception
     */
    @Test
    public void testContains02() throws Exception {

        ConceptSMTK conceptSMTK = new ConceptSMTK(getNullCategory());
        ConceptSCT theTarget1 = new ConceptSCT(10, new Timestamp(System.currentTimeMillis()), true, 1, 1);
        ConceptSCT theTarget2 = new ConceptSCT(11, new Timestamp(System.currentTimeMillis()), true, 1, 1);
        RelationshipDefinition relationshipDefinition = new RelationshipDefinition("RelTest", "Nada", MultiplicityFactory.ONE_TO_MANY, new Category());

        Relationship relationship1 = new Relationship(conceptSMTK, theTarget1, relationshipDefinition,new ArrayList<RelationshipAttribute>(), null);
        conceptSMTK.addRelationship(relationship1);

        Relationship relationship2 = new Relationship(conceptSMTK, theTarget2, relationshipDefinition,new ArrayList<RelationshipAttribute>(), null);
        assertFalse(conceptSMTK.contains(relationship2));
    }

    /**
     * Este test es importante: permite comparar relaciones.
     * @throws Exception
     */
    @Test
    public void testContains03() throws Exception {

        ConceptSMTK conceptSMTK = new ConceptSMTK(getNullCategory());
        ConceptSCT theTarget1 = new ConceptSCT(10, new Timestamp(System.currentTimeMillis()), true, 1, 1);
        RelationshipDefinition relationshipDefinition1 = new RelationshipDefinition("RelTest", "Nada", MultiplicityFactory.ONE_TO_MANY, new Category());
        RelationshipDefinition relationshipDefinition2 = new RelationshipDefinition("RelTest", "Nada", MultiplicityFactory.ONE_TO_MANY, new Category());

        Relationship relationship1 = new Relationship(conceptSMTK, theTarget1, relationshipDefinition1,new ArrayList<RelationshipAttribute>(), null);
        conceptSMTK.addRelationship(relationship1);

        Relationship relationship2 = new Relationship(conceptSMTK, theTarget1, relationshipDefinition2,new ArrayList<RelationshipAttribute>(), null);
        assertTrue(conceptSMTK.contains(relationship2));
    }
}