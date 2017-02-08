package cl.minsal.semantikos.ws.shared;

import com.sun.istack.internal.NotNull;
import com.sun.org.apache.regexp.internal.RE;

import java.util.List;

/**
 * @author Andrès Farías on 11-01-17.
 */
public class HTMLer {

    public static String toHTML(RespuestaConceptosPorCategoria.Conceptos serviceResponse) {
        StringBuilder result = new StringBuilder("<ul>");
        for (Concepto concepto : serviceResponse.getConcepto()) {
            result.append("<li>" + Stringer.toString(concepto) + "</li>");
        }
        return result.append("</ul>").toString();
    }

    public static String toHTML(RespuestaConceptosPorCategoria serviceResponse) {
        if (serviceResponse == null) {
            return "Sin conceptos que mostrar!";
        } else {
            List<Concepto> conceptos = serviceResponse.getConceptos().getConcepto();
            StringBuilder result = new StringBuilder(conceptos.size() + "conceptos retornados; <br/>");
            result.append("<ol>");
            for (Concepto concepto : conceptos) {
                result.append("\t<li>").append(concepto.getId()).append(": ").append(toHTML(concepto.getDescripciones
                        ()));

                Concepto.CrossmapsIndirectos crossmapsIndirectos = concepto.getCrossmapsIndirectos();
                if (crossmapsIndirectos == null) {
                    continue;
                }

                List<IndirectCrossmap> crossmapIndirecto = crossmapsIndirectos.getCrossmapIndirecto();
                if (crossmapIndirecto != null && crossmapIndirecto.size() > 0) {
                    result.append("CrossInd: {");
                    for (IndirectCrossmap indirectCrossmap : crossmapIndirecto) {
                        result.append(indirectCrossmap.getCorrelation());
                    }
                    result.append("}");
                }

                result.append("\t</li>");
            }
            result.append("</ol>");

            return result.toString();
        }
    }

    private static String toHTML(Concepto.Descripciones descripciones) {

        StringBuilder result = new StringBuilder();
        for (Descripcion descripcion : descripciones.getDescripcion()) {
            result.append(descripcion.getId()).append(":").append(descripcion.getTermino()).append(".");
        }

        return result.toString();
    }

    public static String toHTML(@NotNull RespuestaCategorias response) {

        /* Caso sin categorías */
        RespuestaCategorias.Categorias categorias = response.getCategorias();
        if (categorias == null) {
            return "<b>No</b> hay categorías en el sistema!";
        }

        StringBuilder result = new StringBuilder("<ol>\n");
        for (Categoria categoria : categorias.getCategoria()) {
            result.append("<li>").append(categoria.getNombre()).append("(").append(categoria.getNombreAbreviado())
                    .append(")</li>");
        }

        return result.append("\n</ol>").toString();
    }

    public static String toHTML(RespuestaBuscarTerminoGenerica.DescripcionesPerfectMatch serviceResponse) {
        if (serviceResponse == null) {
            return "";
        }

        List<DescripcionPerfectMatch> descripcionPerfectMatch = serviceResponse.getDescripcionPerfectMatch();
        if (descripcionPerfectMatch == null || descripcionPerfectMatch.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder("<em>Perfect Match</em>: <br/><ol>");
        for (DescripcionPerfectMatch description : descripcionPerfectMatch) {
            result.append("<li>").append(description.getTermino()).append(", ID=" + description.getIdDescripcion())
                    .append("</li>");
        }
        return result.append("</ol>").toString();
    }
}
