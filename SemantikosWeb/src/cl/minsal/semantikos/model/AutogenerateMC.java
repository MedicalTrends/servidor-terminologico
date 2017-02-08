package cl.minsal.semantikos.model;

import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gustavo Punucura on 18-10-16.
 */
public class AutogenerateMC {
    private List<String> sustancias;
    private List<String> ffa;
    private Map<Integer,String> ffaMap;
    private String volumen;
    private String unidadVolumen;

    public AutogenerateMC() {
        this.sustancias = new ArrayList<>();
        this.ffa = new ArrayList<>();
        this.ffaMap= new HashMap<>();
        this.volumen = "";
        this.unidadVolumen = "";
    }

    public List<String> getSustancias() {
        return sustancias;
    }

    public void setSustancias(List<String> sustancias) {
        this.sustancias = sustancias;
    }

    public List<String> getFfa() {
        return ffa;
    }

    public void setFfa(List<String> ffa) {
        this.ffa = ffa;
    }

    public String getVolumen() {
        return volumen;
    }

    public void setVolumen(Relationship relationship) {
        String vol = relationship.getTarget().toString();
        volumen = vol;
    }
    public void setVolumenEmpty() {
        volumen = "";
    }

    public String getUnidadVolumen() {
        return unidadVolumen;
    }

    public void setUnidadVolumen(RelationshipAttribute relationshipAttribute) {
        this.unidadVolumen = (((HelperTableRow) relationshipAttribute.getTarget()).getDescription());
    }
    public void setUnidadVolumenEmpty() {
        this.unidadVolumen = "";
    }

    /**
     * Este método es responsable de...
     *
     * @param relationship La sustancia, representada por una relación.
     */
    public void addSustancia(Relationship relationship) {
        sustancias.add(generateNameSustancia(relationship));
    }

    public String generateNameSustancia(Relationship relationship){
        String sustancia = ((ConceptSMTK) relationship.getTarget()).getDescriptionFavorite().getTerm();
        String[] attributes = new String[4];
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);

        for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {
            if (relationshipAttribute.getRelationAttributeDefinition().getId() == 8)
                attributes[0] = " " + format.format(Double.valueOf(relationshipAttribute.getTarget().toString())) +" ";
            if (relationshipAttribute.getRelationAttributeDefinition().getId() == 9)
                attributes[1] = (((HelperTableRow) relationshipAttribute.getTarget()).getDescription());
            if (relationshipAttribute.getRelationAttributeDefinition().getId() == 10) {
                if (Float.parseFloat(relationshipAttribute.getTarget().toString()) != 1) {
                    attributes[2] = "/" + format.format(Double.valueOf(relationshipAttribute.getTarget().toString()))+" ";
                } else {
                    attributes[2] = "/";
                }
            }
            if (relationshipAttribute.getRelationAttributeDefinition().getId() == 11)
                attributes[3] = (((HelperTableRow) relationshipAttribute.getTarget()).getDescription());
        }
        for (int i = 0; i < 4; i++) {
            if (attributes[i] != null) {
                sustancia = sustancia + attributes[i];
            }

        }
        return sustancia;
    }

    public void addFFA(Relationship relationship) {
        ffa.add(((HelperTableRow) relationship.getTarget()).getDescription() + "");
        ffaMap.put(relationship.getOrder(),((HelperTableRow) relationship.getTarget()).getDescription() + "");
    }
    public void voidRemoveFFA(Relationship relationship) {
        ffa.remove(((HelperTableRow) relationship.getTarget()).getDescription() + "");
    }

    public void addVol(Relationship relationship) {
        String vol = relationship.getTarget().toString();
        for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {
            if (relationshipAttribute.getRelationAttributeDefinition().getId() == 12)
                vol = " " + (((HelperTableRow) relationshipAttribute.getTarget()).getDescription());
        }
        volumen = vol;
    }

    @Override
    public String toString() {
        String term = "";
        for (int i = 0; i < sustancias.size(); i++) {
            if (i == 0) {
                term = sustancias.get(i);
            } else {
                term = term + " + " + sustancias.get(i);
            }
        }
        term = " " + term;
        for (int i = 0; i < ffa.size(); i++) {

            term = term + " " + ffaMap.get(i+1);

        }
        term = term + " " + volumen + " " + unidadVolumen;

        return term;
    }
}
