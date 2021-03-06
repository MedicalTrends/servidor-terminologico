package cl.minsal.semantikos.designer;

import cl.minsal.semantikos.category.CategoryBean;
import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.kernel.componentsweb.ViewAugmenter;
import cl.minsal.semantikos.messages.MessageBean;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;
import cl.minsal.semantikos.modelweb.RelationshipDefinitionWeb;
import cl.minsal.semantikos.modelweb.RelationshipWeb;
import cl.minsal.semantikos.util.StringUtils;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.sql.BatchUpdateException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN_MAPEO_DE;

/**
 * Created by des01c7 on 14-10-16.
 */
@ManagedBean(name = "autogenerateBean")
@ViewScoped
public class AutogenerateBean {

    //@EJB
    RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);

    //@EJB
    HelperTablesManager helperTablesManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    @ManagedProperty( value="#{categoryBean}")
    CategoryBean categoryBean;

    @ManagedProperty(value = "#{messageBean}")
    MessageBean messageBean;

    public MessageBean getMessageBean() {
        return messageBean;
    }

    public void setMessageBean(MessageBean messageBean) {
        this.messageBean = messageBean;
    }

    public CategoryBean getCategoryBean() {
        return categoryBean;
    }

    public void setCategoryBean(CategoryBean categoryBean) {
        this.categoryBean = categoryBean;
    }

    private boolean inheritedCrossmaps = false;

    private boolean special = false;

    public boolean isInheritedCrossmaps() {
        return inheritedCrossmaps;
    }

    public void setInheritedCrossmaps(boolean inheritedCrossmaps) {
        this.inheritedCrossmaps = inheritedCrossmaps;
    }

    public void load(ConceptSMTKWeb concept, RelationshipDefinition relationshipDefinition) {

        if(concept.isModeled()) {
            return;
        }

        if(!categoryBean.getRelationshipDefinitionById(concept.getCategory(), relationshipDefinition).isAutogenerate()) {
            return;
        }

        if(special) {
            return;
        }

        String autogeneratedTerm = autogenerate(concept, categoryBean.getRelationshipDefinitionsByCategory(concept.getCategory()));
        concept.getValidDescriptionFavorite().setTerm(autogeneratedTerm);
        concept.getValidDescriptionFSN().setTerm(autogeneratedTerm);

    }

    public String cleanTerm(String term) {

        term = term.replaceAll("\\((.*?)\\)", "");

        return term;
    }

    public String autogenerate(ConceptSMTKWeb concept, List<RelationshipDefinitionWeb> relationshipDefinitionWebs) {

        String separator = "";
        String autogeneratedTerm = "";

        if(concept.getCategory().isOnlyCommercial()) {
            autogeneratedTerm = cleanTerm(concept.getDescriptionFavorite().getTerm());
        }
        //Si la categoría es Fármacos - Medicamento Clínico, alterar orden para que la cantidad vaya despues de las sustancias
        if(concept.getCategory().isMC()) {
            reorderMCDefinitions(relationshipDefinitionWebs);
        }
        // Se asume que las definiciones están ordenadas de acuerdo a negocio
        for (RelationshipDefinitionWeb relationshipDefinition : relationshipDefinitionWebs) {
            if(relationshipDefinition.isAutogenerate()) {
                List<RelationshipWeb> autogenerableRelationships = concept.getValidRelationshipsWebByRelationDefinition(relationshipDefinition);
                if (!autogenerableRelationships.isEmpty()) {
                    if (relationshipDefinition.getTargetDefinition().isHelperTable() || relationshipDefinition.getTargetDefinition().isBasicType()) {
                        separator = " ";
                    } else {
                        if(relationshipDefinition.isMCCE()) {
                            separator = " ";
                        }
                        else {
                            separator = " + ";
                        }
                    }
                }
                Collections.sort(autogenerableRelationships);

                for (RelationshipWeb relationship : autogenerableRelationships) {
                    if(relationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType()) {
                        if(relationship.isInherited() /*|| relationship.isPersistent()*/) {
                            continue;
                        }
                        if(autogeneratedTerm.contains(relationship.getTarget().toString())) {
                            continue;
                        }
                    }
                    autogeneratedTerm = autogeneratedTerm + separator + autogenerate(relationship);
                }
            }
        }

        if(!autogeneratedTerm.isEmpty()) {
            autogeneratedTerm = StringUtils.normalizeSpaces(autogeneratedTerm).trim();
            autogeneratedTerm = autogeneratedTerm.replaceAll(" /","/").replaceAll("/ ","/");

            if(autogeneratedTerm.charAt(0) == '+') {
                autogeneratedTerm = autogeneratedTerm.substring(1).trim();
            }
        }
        //Si la categoría es Fármacos - Medicamento Clínico, alterar orden para que la cantidad vaya despues de las sustancias
        if(concept.getCategory().isMC()) {
            reorderMCDefinitions(relationshipDefinitionWebs);
        }
        return autogeneratedTerm;
    }

    public String autogenerate(Relationship relationship) {

        String autogeneratedTerm = "";

        if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {

            ConceptSMTK concept = (ConceptSMTK) relationship.getTarget();

            if(relationship.getRelationshipDefinition().isMCCE()) {
                concept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(concept));
                List<RelationshipDefinitionWeb> relationshipDefinitionWebs = new ArrayList<>();

                for (RelationshipDefinitionWeb relationshipDefinitionWeb : categoryBean.getRelationshipDefinitionsByCategory(concept.getCategory())) {
                    if(!relationshipDefinitionWeb.getTargetDefinition().isSMTKType()) {
                        relationshipDefinitionWebs.add(relationshipDefinitionWeb);
                    }
                }

                autogeneratedTerm = autogeneratedTerm + autogenerate(new ConceptSMTKWeb(concept), relationshipDefinitionWebs);
            }
            else {
                autogeneratedTerm = concept.getDescriptionFavorite().getTerm();
            }

            if(!relationship.getRelationshipAttributes().isEmpty()) {
                autogeneratedTerm = autogeneratedTerm + " ";
            }

            autogeneratedTerm = autogeneratedTerm + autogenerateAttributes(relationship);
        }

        if(relationship.getRelationshipDefinition().getTargetDefinition().isHelperTable()) {
            HelperTableRow helperTableRow = (HelperTableRow) relationship.getTarget();

            if(relationship.getRelationshipDefinition().isAttributeLaboratory()) {
                autogeneratedTerm = " (" + helperTableRow.getCellByColumnName("DSC_ABREVIADA").getStringValue() + ") ";
            }
            else {
                autogeneratedTerm = helperTableRow.getDescription();
            }
        }

        if(relationship.getRelationshipDefinition().getTargetDefinition().isBasicType()) {
            BasicTypeValue basicTypeValue = (BasicTypeValue) relationship.getTarget();

            if(basicTypeValue.isFloat()) {
                DecimalFormat df = new DecimalFormat("###,###.##");
                autogeneratedTerm = df.format(basicTypeValue.getValue());
            }
            else {
                autogeneratedTerm = relationship.getTarget().toString();
            }

            if(!relationship.getRelationshipAttributes().isEmpty()) {
                autogeneratedTerm = autogeneratedTerm + " ";
            }

            autogeneratedTerm = autogeneratedTerm + autogenerateAttributes(relationship);
        }

        if(relationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType()) {
            autogeneratedTerm = relationship.getTarget().toString();
        }

        return autogeneratedTerm;

    }

    public String autogenerateAttributes(Relationship relationship) {

        String autogeneratedTerm = "";

        for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {

            if(relationshipAttribute.getRelationAttributeDefinition().isOrderAttribute()) {
                continue;
            }

            if(relationshipAttribute.getRelationAttributeDefinition().isUnidadPotenciaAttribute() ||
                    relationshipAttribute.getRelationAttributeDefinition().isUnidadPPAttribute() ||
                    relationshipAttribute.getRelationAttributeDefinition().isUnidadAttribute() ||
                    relationshipAttribute.getRelationAttributeDefinition().isUnidadPackMultiAttribute() ||
                    relationshipAttribute.getRelationAttributeDefinition().isUnidadVolumenTotalAttribute() ||
                    relationshipAttribute.getRelationAttributeDefinition().isUnidadVolumenAttribute()) {

                HelperTableRow helperTableRow = (HelperTableRow) relationshipAttribute.getTarget();
                autogeneratedTerm = autogeneratedTerm + helperTableRow.getCellByColumnName("descripcion abreviada").getStringValue() + " ";

                if(relationshipAttribute.getRelationAttributeDefinition().isUnidadPotenciaAttribute()
                        && !relationship.getAttributesByAttributeDefinition(relationship.getRelationshipDefinition().getPPAttributeDefinition()).isEmpty()) {
                    autogeneratedTerm = autogeneratedTerm + "/";
                }
            }
            else {
                if(relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition().isBasicType()) {

                    if(relationshipAttribute.getRelationAttributeDefinition().isCantidadPPAttribute() &&
                            Float.parseFloat(relationshipAttribute.getTarget().toString()) == 1) {
                        continue;
                    }

                    BasicTypeValue basicTypeValue = (BasicTypeValue) relationshipAttribute.getTarget();
                    BasicTypeDefinition basicTypeDefinition = (BasicTypeDefinition) relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition();

                    if (basicTypeDefinition.getType().equals(BasicTypeType.FLOAT_TYPE)) {
                        DecimalFormat df = new DecimalFormat("###,###.##");
                        autogeneratedTerm = autogeneratedTerm + df.format(Float.parseFloat(basicTypeValue.getValue().toString())) + " ";
                    }
                }
                else {
                    autogeneratedTerm = autogeneratedTerm + relationshipAttribute.getTarget().toString() + " ";
                }
            }
        }

        return autogeneratedTerm;
    }

    public void reorderMCDefinitions(List<RelationshipDefinitionWeb> relationshipDefinitionWebs) {

        int i = 0;
        int j = 0;
        int cont = 0;

        for (RelationshipDefinitionWeb relationshipDefinitionWeb : relationshipDefinitionWebs) {
            if(relationshipDefinitionWeb.isCantidadVolumenTotal()) {
                i = cont;
            }
            if(relationshipDefinitionWeb.isSubstance()) {
                j = cont;
            }
            cont++;
        }

        if(i != j) {
            Collections.swap(relationshipDefinitionWebs, i, j);
        }
    }

    public boolean canInheritDirectCrossmaps(ConceptSMTK concept) {

        for (Relationship relationship : concept.getRelationships()) {
            if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {
                ConceptSMTK targetConcept = (ConceptSMTK) relationship.getTarget();
                if(!targetConcept.isRelationshipsLoaded()) {
                    targetConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(targetConcept));
                }
                for (Relationship targetRelationship : targetConcept.getRelationships()) {
                    if(targetRelationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType()) {
                        if(targetRelationship.getRelationshipDefinition().isGMDN()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public List<Relationship> getHeritableDirectCrossmaps(ConceptSMTK concept) {

        List<Relationship> directCrossmaps = new ArrayList<>();

        for (Relationship relationship : concept.getRelationships()) {
            if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {
                ConceptSMTK targetConcept = (ConceptSMTK) relationship.getTarget();
                if(!targetConcept.isRelationshipsLoaded()) {
                    targetConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(targetConcept));
                }
                for (Relationship targetRelationship : targetConcept.getRelationships()) {
                    if(targetRelationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType()) {
                        if(targetRelationship.getRelationshipDefinition().isGMDN()) {
                            directCrossmaps.add(targetRelationship);
                        }
                    }
                }
            }
        }

        return directCrossmaps;
    }

    public void inheritDirectCrossmaps(ConceptSMTKWeb concept) throws BusinessRuleException {

        List<RelationshipWeb> directCrossmaps = new ArrayList<>();

        try {

            for (Relationship relationship : concept.getRelationships()) {
                if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {
                    ConceptSMTK targetConcept = (ConceptSMTK) relationship.getTarget();
                    if(!targetConcept.isRelationshipsLoaded()) {
                        targetConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(targetConcept));
                    }
                    for (Relationship targetRelationship : targetConcept.getRelationships()) {
                        if(targetRelationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType()) {
                            if(targetRelationship.getRelationshipDefinition().isGMDN()) {
                                RelationshipWeb relationshipWeb = new RelationshipWeb(targetRelationship, targetRelationship.getRelationshipAttributes());
                                relationshipWeb.setSourceConcept(concept);
                                relationshipWeb.setInherited(true);
                                directCrossmaps.add(relationshipWeb);
                            }
                        }
                    }
                }
            }

            for (RelationshipWeb directCrossmap : directCrossmaps) {
                concept.addRelationshipWeb(directCrossmap);
            }

            inheritedCrossmaps = true;

        }
        catch(BusinessRuleException e) {
            messageBean.messageError(e.getMessage());
        }
    }

    public void addRelationshipType(ConceptSMTKWeb concept) {

        for (RelationshipWeb relationshipWeb : concept.getRelationshipsWeb()) {
            // Si esta definición de relación es de tipo CROSSMAP, Se agrega el atributo tipo de relacion = "ES_UN_MAPEO_DE" (por defecto)
            if (relationshipWeb.getRelationshipDefinition().getTargetDefinition().isCrossMapType()) {

                //Ya se agregó el atributo en la capa controlador
                if(relationshipWeb.getRelationshipTypeAttribute() != null) {
                    return;
                }

                for (RelationshipAttributeDefinition attDef : relationshipWeb.getRelationshipDefinition().getRelationshipAttributeDefinitions()) {
                    if (attDef.isRelationshipTypeAttribute()) {

                        HelperTable helperTable = (HelperTable) attDef.getTargetDefinition();

                        List<HelperTableRow> relationshipTypes = helperTablesManager.searchRows(helperTable, ES_UN_MAPEO_DE);

                        RelationshipAttribute ra = new RelationshipAttribute(attDef, relationshipWeb, relationshipTypes.get(0));
                        relationshipWeb.getRelationshipAttributes().add(ra);
                    }
                }
            }
        }
    }

    public void evaluateMCSpecial(ConceptSMTKWeb concept) {

        if(concept.getCategory().hasAttributeSpecial()) {

            for (Relationship relationship : concept.getRelationships()) {

                if (relationship.getRelationshipDefinition().isMCSpecial()) {

                    BasicTypeValue isMCSpecial = (BasicTypeValue<Boolean>) relationship.getTarget();
                    int lowerBoundary;

                    special = (Boolean) isMCSpecial.getValue();

                    if(special) {
                        lowerBoundary = 0;
                    }
                    else {
                        lowerBoundary = 1;
                    }

                    for (RelationshipDefinitionWeb relationshipDefinition : categoryBean.getRelationshipDefinitionsByCategory(concept.getCategory())) {
                        if(relationshipDefinition.isSubstance() || relationshipDefinition.isMB() || relationshipDefinition.isFFA() ||
                                relationshipDefinition.isDB() || relationshipDefinition.isFFADisp()) {

                            relationshipDefinition.getMultiplicity().setLowerBoundary(lowerBoundary);
                            concept.getCategory().findRelationshipDefinitionsById(relationshipDefinition.getId()).get(0).getMultiplicity().setLowerBoundary(lowerBoundary);
                        }
                    }
                }
            }
        }

    }

    public boolean checkDynamicMultiplicity(Relationship relationship) {

        int potencia = 0;
        int pp = 0;

        // Chequear consistencia bidireccional entre cantidad potencia y unidad potencia
        for (RelationshipAttribute firstAttribute : relationship.getRelationshipAttributes()) {
            // Si existe cantidad potencia --> debe existir unidad potencia
            if(firstAttribute.getRelationAttributeDefinition().isCantidadPotenciaAttribute()) {
                potencia++;
            }
            if(firstAttribute.getRelationAttributeDefinition().isUnidadPotenciaAttribute()) {
                potencia--;
            }
        }
        // Si no está balanceado, levantar excepción
        if(potencia != 0) {
            return false;
        }

        // Chequear consistencia bidireccional entre cantidad pp y unidad pp
        for (RelationshipAttribute firstAttribute : relationship.getRelationshipAttributes()) {
            // Si existe cantidad potencia --> debe existir unidad potencia
            if(firstAttribute.getRelationAttributeDefinition().isCantidadPPAttribute()) {
                pp++;
            }
            if(firstAttribute.getRelationAttributeDefinition().isUnidadPPAttribute()) {
                pp--;
            }
        }
        // Si no está balanceado, levantar excepción
        if(pp != 0) {
            return false;
        }

        return true;
    }

}
