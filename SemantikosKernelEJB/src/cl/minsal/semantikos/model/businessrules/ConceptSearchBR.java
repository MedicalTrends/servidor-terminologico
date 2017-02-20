package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.TargetType;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.validation.constraints.NotNull;
import java.text.Normalizer;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author Andrés Farías on 11/24/16.
 */
@Singleton
public class ConceptSearchBR {

    /**
     * Método de normalización del patrón de búsqueda, lleva las palabras a minúsculas,
     * le quita los signos de puntuación y ortográficos
     *
     * @param pattern patrón de texto a normalizar
     * @return patrón normalizado
     */
    //TODO: Falta quitar los StopWords (no se encuentran definidos)
    public String standardizationPattern(String pattern) {

        if (pattern != null) {
            pattern = Normalizer.normalize(pattern, Normalizer.Form.NFD);
            pattern = pattern.toLowerCase();
            pattern = pattern.replaceAll("[^\\p{ASCII}]", "");
            pattern = pattern.replaceAll("\\p{Punct}+", "");
        }else{
            pattern="";
        }
        return pattern;
    }

    /**
     * <p>Este método es responsable de implementar la regla de negocio:</p>
     * <b>BR-HT-PA01</b>: Los elementos de las tabla auxiliar deben ser ordenados alfabéticamente, excepto por la tabla
     * HT_ATC_NAME que se ordena por el largo de los resultados.
     *
     * @param conceptSCTs Los registros que se desea ordenar.
     */
    public void applyPostActions(@NotNull List<ConceptSCT> conceptSCTs) {

        /* Se ordenan los resultados */
        postActionsortCollections(conceptSCTs);
    }

    private void postActionsortCollections(List<ConceptSCT> conceptSCTs) {

        /* Las listas vacías no requieren ser ordenadas */
        if (conceptSCTs == null || conceptSCTs.isEmpty()){
            return;
        }

        /* Si la lista de registros es de la tabla HT_ATC_NAME, el ordenamiento es especial */
        Collections.sort(conceptSCTs, new SCTComparator());
    }

    class SCTComparator implements Comparator<ConceptSCT> {

        @Override
        public int compare(ConceptSCT conceptSCT1, ConceptSCT conceptSCT2) {

            return conceptSCT1.getDescriptionFSN().getTerm().length() - conceptSCT2.getDescriptionFSN().getTerm().length();
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

}
