package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;

import javax.ejb.Singleton;
import javax.validation.constraints.NotNull;
import java.text.Normalizer;
import java.util.Collections;
import java.util.Comparator;
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
     * @param conceptSMTKs Los registros que se desea ordenar.
     */
    public void applyPostActions(@NotNull List<ConceptSMTK> conceptSMTKs) {

        /* Se ordenan los resultados */
        postActionsortCollections(conceptSMTKs);
    }

    private void postActionsortCollections(List<ConceptSMTK> conceptSMTKs) {

        /* Las listas vacías no requieren ser ordenadas */
        if (conceptSMTKs == null || conceptSMTKs.isEmpty()){
            return;
        }

        /* Si la lista de registros es de la tabla HT_ATC_NAME, el ordenamiento es especial */
        Collections.sort(conceptSMTKs, new STKComparator());
    }

    class STKComparator implements Comparator<ConceptSMTK> {

        @Override
        public int compare(ConceptSMTK conceptSMTK1, ConceptSMTK conceptSMTK2) {

            return conceptSMTK1.getDescriptionFavorite().getTerm().length() - conceptSMTK2.getDescriptionFavorite().getTerm().length();
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

}
