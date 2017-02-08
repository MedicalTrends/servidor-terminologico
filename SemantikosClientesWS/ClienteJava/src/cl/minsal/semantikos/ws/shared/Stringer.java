package cl.minsal.semantikos.ws.shared;

import java.util.List;

/**
 * @author Andres Farias on 09-01-17.
 */
public class Stringer {

    public static String toString(RespuestaBuscarTerminoGenerica respuestaBuscarTerminoGenerica) {

        RespuestaBuscarTerminoGenerica.DescripcionesPerfectMatch descripcionesPerfectMatch =
                respuestaBuscarTerminoGenerica.getDescripcionesPerfectMatch();

        return "Perfect match (" + descripcionesPerfectMatch.getDescripcionPerfectMatch().size() + "): " +
                descripcionesPerfectMatch.getDescripcionPerfectMatch() + ", No validas: " + Stringer.toString
                (respuestaBuscarTerminoGenerica.getDescripcionesNoValidas());
    }

    private static String toString(RespuestaBuscarTerminoGenerica.DescripcionesNoValidas descripcionesNoValidas) {
        StringBuilder result = new StringBuilder("[");
        for (DescripcionNoValida descripcionNoValida : descripcionesNoValidas.getDescripcionNoValida()) {
            result.append(descripcionNoValida);
        }

        return result.append("]").toString();
    }

    public static String toString(RespuestaBuscarTerminoGenerica.DescripcionesPerfectMatch descripcionesPerfectMatch) {
        StringBuilder result = new StringBuilder("[");
        for (DescripcionPerfectMatch descripcionPerfectMatch : descripcionesPerfectMatch.getDescripcionPerfectMatch()) {
            result.append(descripcionPerfectMatch);
        }

        return result.append("]").toString();
    }

    public static String toString(RespuestaConceptosPorCategoria.Conceptos conceptosResponse) {
        StringBuilder result = new StringBuilder("[");
        for (Concepto aConcept : conceptosResponse.getConcepto()) {
            result.append(aConcept);
        }

        return result.append("]").toString();
    }

    public static String toString(PeticionPorCategoria peticion) {
        StringBuilder result = new StringBuilder("[");
        result.append("Categoría='").append(peticion.getNombreCategoria());
        result.append(";ID Establecimiento=").append(peticion.getIdEstablecimiento());

        return result.append("]").toString();
    }

    public static String toString(PeticionBuscarTermino peticion) {
        StringBuilder result = new StringBuilder("[");
        result.append("Term='").append(peticion.getTermino());
        result.append(", Categoría='").append(peticion.getNombreCategoria());
        result.append(", RefSets='").append(peticion.getNombreRefSet());
        result.append(", ID Establecimiento=").append(peticion.getIdEstablecimiento());

        return result.append("]").toString();
    }

    public static String toString(PeticionConceptosPedibles peticion) {
        StringBuilder result = new StringBuilder("[");
        result.append("Pedible='").append(peticion.getPedible());
        result.append(", Categoría='").append(peticion.getNombreCategoria());
        result.append(", RefSets='").append(peticion.getNombreRefSet());
        result.append(", ID Establecimiento=").append(peticion.getIdEstablecimiento());

        return result.append("]").toString();
    }

    public static String toString(RespuestaConceptosPorCategoria response) {
        StringBuilder result = new StringBuilder("[");

        RespuestaConceptosPorCategoria.Conceptos conceptos = response.getConceptos();
        for (Concepto concepto : conceptos.getConcepto()) {
            result.append(toString(concepto));
        }

        return result.append("]").toString();
    }

    protected static String toString(Concepto concepto) {
        return "Concepto[" + concepto.getId() + "]";
    }

    protected static String toString(ConceptoLight concepto) {
        return "Concepto[" + concepto.getConceptID() + "]";
    }

    public static String toString(RespuestaBuscarTermino.Conceptos response) {

        if (response == null || response.getConcepto() == null) {
            return "[null]";
        }

        List<ConceptoLight> conceptos = response.getConcepto();
        StringBuilder result = new StringBuilder("[");
        for (ConceptoLight concepto : conceptos) {
            result.append(toString(concepto));
        }

        return result.append("]").toString();
    }

    public static String toString(RespuestaBuscarTermino response) {

        RespuestaBuscarTermino.Conceptos conceptos = response.getConceptos();
        if (conceptos == null || conceptos.getConcepto() == null || conceptos.getConcepto().isEmpty()) {
            return "[null]";
        }

        StringBuilder result = new StringBuilder("[");
        for (ConceptoLight concepto : conceptos.getConcepto()) {
            result.append(toString(concepto));
        }

        return result.append("]").toString();
    }

    public static String toString(RespuestaRefSets response) {
        return null;
    }

    public static String toString(PeticionRefSetsPorIdDescripcion peticion) {
        return "[DESCRIPTION_ID=" + peticion.getIdDescripcion() + ", ID Establecimiento" + peticion.getIdStablishment
                () + "Incluye=" + peticion.getIncluyeEstablecimientos() + "]";
    }

    public static String toString(RespuestaRefSets.Refsets serviceResponse) {

        if (serviceResponse == null) {
            return "";
        }

        List<RefSet> refsets = serviceResponse.getRefset();
        if (refsets == null || refsets.isEmpty()) {
            return "[null]";
        }

        StringBuilder result = new StringBuilder("[");
        for (RefSet refSet : refsets) {
            result.append(toString(refSet));
        }

        return result.append("]").toString();
    }

    private static String toString(RefSet refSet) {
        return "[" + refSet.getNombre() + "]";
    }

    public static String toString(PeticionConceptosPorRefSet peticion) {
        return "[RefSet=" + peticion.getNombreRefSet() + ", ID Establecimiento=" + peticion.getIdEstablecimiento() +
                "]";
    }

    public static String toString(RespuestaConceptosPorRefSet response) {
        RespuestaConceptosPorRefSet.Conceptos conceptos = response.getConceptos();
        if (conceptos == null || conceptos.getConcepto() == null) {
            return "[null]";
        }

        StringBuilder result = new StringBuilder("[");
        for (ConceptoLight conceptoLight : conceptos.getConcepto()) {
            result.append(toString(conceptoLight));
        }

        return result.append("]").toString();
    }

    public static String toString(String idStablishment) {
        return "[" + idStablishment + "]";
    }

    public static String toString(CrossmapSetsResponse response) {
        return "[" + toString(response.getCrossmapSets()) + "]";
    }

    public static String toString(CrossmapSetsResponse.CrossmapSets crossmapSets) {
        StringBuilder result = new StringBuilder();
        for (CrossmapSet crossmapSet : crossmapSets.getCrossmapSet()) {
            result.append(crossmapSet.getName()).append("; ");
        }
        return result.toString();
    }

    public static String toString(CrossmapSetMembersResponse response) {

        CrossmapSetMembersResponse.CrossmapSetMembers crossmapSetMembers = response.getCrossmapSetMembers();
        if (crossmapSetMembers == null) {
            return "[Null]";
        }

        StringBuilder result = new StringBuilder("[");
        for (CrossmapSetMember crossmapSetMember : crossmapSetMembers.getCrossmapSetMember()) {
            result.append(crossmapSetMember.getCode() + "/" + crossmapSetMember.getGloss() + ", ");
        }
        return result.append("]").toString();
    }

    public static String toString(IndirectCrossmapsSearch response) {
        return "[" + response.getIndirectCrossmaps() + "]";
    }

    public static String toString(DescriptionIDorConceptIDRequest request) {
        return "[DESCRIPTION_ID=" + request.getDescriptionId() + ", CONCEPT_ID=" + request.getConceptId() + ", " +
                "Stablishment ID=" + request.getStablishmentId() + "]";
    }

    public static String toString(RespuestaCategorias response) {
        StringBuilder result = new StringBuilder();

        if (response == null || response.getCategorias() == null){
            return "RespuestaCategorias es nulo";
        }

        List<Categoria> categorias = response.getCategorias().getCategoria();
        int i = 0;
        for (Categoria categoria : categorias) {
            result.append(++i).append(")").append(categoria.getNombre()).append(" / ").append(categoria
                    .getNombreAbreviado());
        }

        return result.toString();
    }
}
