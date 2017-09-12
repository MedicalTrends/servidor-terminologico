package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;

import javax.ejb.Singleton;
import javax.validation.constraints.NotNull;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andrés Farías on 11/24/16.
 */
@Singleton
public class DescriptionSearchBR {

    static final String[] STOP_WORDS = new String[]{"a","aquí","cuantos","esta","misma","nosotras","querer","tales",
            "usted","acá","cada","cuán","estar","mismas","nosotros","qué","tan","ustedes","ahí","cierta","cuánto",
            "estas","mismo","nuestra","quien","tanta","varias","ajena","ciertas","cuántos","este","mismos","nuestras",
            "quienes","tantas","varios","ajenas","cierto","de","estos","mucha","nuestro","quienesquiera","tanto",
            "vosotras","ajeno","ciertos","dejar","hacer","muchas","nuestros","quienquiera","tantos","vosotros","ajenos",
            "como","del","hasta","muchísima","nunca","quién","te","vuestra","al","cómo","demasiada","jamás","muchísimas",
            "os","ser","tener","vuestras","algo","con","demasiadas","junto","muchísimo","otra","si","ti","vuestro",
            "alguna","conmigo","demasiado","juntos","muchísimos","otras","siempre","toda","vuestros","algunas","consigo",
            "demasiados","la","mucho","otro","sí","todas","y","alguno","contigo","demás","las","muchos","otros","sín",
            "todo","yo","algunos","cualquier","el","lo","muy","para","Sr","todos ","algún","cualquiera","ella","los",
            "nada","parecer","Sra","tomar ","allá","cualquieras","él","me","ningunas","poco","suya","tú ","aquella",
            "cuantas","esa","menos","ninguno","pocos","suyas","un ","aquellas","cuánta","esas","mía","ningunos","por",
            "suyo","una ","aquello","cuántas","ese","mientras","no","porque","suyos","unas","aquellos","cuanto","esos",
            "mío","nos","que","tal","unos"};

    /**
     * Método de normalización del patrón de búsqueda, lleva las palabras a minúsculas,
     * le quita los signos de puntuación y ortográficos
     *
     * @param pattern patrón de texto a normalizar
     * @return patrón normalizado
     */
    public String standardizationPattern(String pattern) {

        String result = "";

        List stopWords = Arrays.asList(STOP_WORDS);

        for (String s : pattern.toLowerCase().split("\\b")) {
            if (!stopWords.contains(s)) {
                result=result+s;
            }
        }

        result = Normalizer.normalize(result, Normalizer.Form.NFD);
        result = result.toLowerCase();
        result = result.replaceAll("[^\\p{ASCII}]", "");
        result = result.replaceAll("\\p{Punct}+", "");

        return result;
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
