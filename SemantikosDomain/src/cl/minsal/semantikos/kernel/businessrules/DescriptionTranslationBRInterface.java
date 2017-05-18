package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.Description;

public interface DescriptionTranslationBRInterface {

    public void apply(ConceptSMTK sourceConcept, ConceptSMTK targetConcept, Description description,
                      ConceptManager conceptManager, CategoryManager categoryManager);

}
