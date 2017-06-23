package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.descriptions.Description;
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
public class DescriptionSearchBR {

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
     * @param descriptions Los registros que se desea ordenar.
     */
    public void applyPostActions(@NotNull List<Description> descriptions) {

        /* Se ordenan los resultados */
        postActionsortCollections(descriptions);
    }

    private void postActionsortCollections(List<Description> descriptions) {

        /* Las listas vacías no requieren ser ordenadas */
        if (descriptions == null || descriptions.isEmpty()){
            return;
        }

        /* Si la lista de registros es de la tabla HT_ATC_NAME, el ordenamiento es especial */
        Collections.sort(descriptions, new DescriptionComparator());
    }

    class DescriptionComparator implements Comparator<Description> {

        @Override
        public int compare(Description description1, Description description2) {

            return description1.getTerm().length() - description2.getTerm().length();
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

}
