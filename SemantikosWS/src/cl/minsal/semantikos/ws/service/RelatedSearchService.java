package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.ws.fault.IllegalInputFault;
import cl.minsal.semantikos.ws.response.RelatedConceptsResponse;
import cl.minsal.semantikos.ws.response.TermSearchResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Development on 2016-11-18.
 *
 */
@WebService(serviceName = "ServicioDeBusquedaDeRelacionados")
public class RelatedSearchService {

    // REQ-WS-006
    @WebMethod(operationName = "sugerenciasDeDescripciones")
    public TermSearchResponse sugerenciasDeDescripciones(
            @XmlElement(required = true)
            @WebParam(name = "termino")
                    String term,
            @XmlElement(required = false)
            @WebParam(name = "nombreCategoria")
                    List<String> categoryNames,
            @XmlElement(required = false)
            @WebParam(name = "nombreRefSet")
                    List<String> refSetNames
    ) throws IllegalInputFault {
        if ( (categoryNames == null && refSetNames == null)
                || (categoryNames.isEmpty() && refSetNames.isEmpty())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet");
        }
        // TODO
        return null;
    }

    // REQ-WS-010...021
    @WebMethod(operationName = "conceptosRelacionados")
    public RelatedConceptsResponse conceptosRelacionados(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId,
            @XmlElement(required = true)
            @WebParam(name = "categoriaRelacion")
                    String relatedCategoryName
    ) throws IllegalInputFault {
        if ( (conceptId == null || "".equals(conceptId) )
                && (descriptionId == null || "".equals(descriptionId)) ) {
            throw new IllegalInputFault("Debe ingresar un idConcepto o idDescripcion");
        }
        // TODO
        return null;
    }

    // REQ-WS-010...021 Lite
    @WebMethod(operationName = "conceptosRelacionadosLite")
    public RelatedConceptsResponse conceptosRelacionadosLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId,
            @XmlElement(required = true)
            @WebParam(name = "categoriaRelacion")
                    String relatedCategoryName
    ) throws IllegalInputFault {
        if ( (conceptId == null || "".equals(conceptId) )
                && (descriptionId == null || "".equals(descriptionId)) ) {
            throw new IllegalInputFault("Debe ingresar un idConcepto o idDescripcion");
        }
        // TODO
        return null;
    }

    // REQ-WS-010
    // REQ-WS-011
    @WebMethod(operationName = "obtenerMedicamentoClinico")
    public RelatedConceptsResponse obtenerMedicamentoClinico(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionados(conceptId, descriptionId, "");
    }

    // REQ-WS-010 Lite
    // REQ-WS-011 Lite
    @WebMethod(operationName = "obtenerMedicamentoClinicoLite")
    public RelatedConceptsResponse obtenerMedicamentoClinicoLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionadosLite(conceptId, descriptionId, "");
    }

    // REQ-WS-010
    @WebMethod(operationName = "obtenerMedicamentoClinicoPorMedicamentoBasico")
    public RelatedConceptsResponse obtenerMedicamentoClinicoPorMedicamentoBasico(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerMedicamentoClinico(conceptId, descriptionId);
    }

    // REQ-WS-010.1-Lite
    @WebMethod(operationName = "obtenerMedicamentoClinicoPorMedicamentoBasicoLite")
    public RelatedConceptsResponse obtenerMedicamentoClinicoPorMedicamentoBasicoLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerMedicamentoClinicoLite(conceptId, descriptionId);
    }

    // REQ-WS-011
    @WebMethod(operationName = "obtenerMedicamentoClinicoPorProductoComercial")
    public RelatedConceptsResponse obtenerMedicamentoClinicoPorProductoComercial(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerMedicamentoClinico(conceptId, descriptionId);
    }

    // REQ-WS-011.1-Lite
    @WebMethod(operationName = "obtenerMedicamentoClinicoPorProductoComercialLite")
    public RelatedConceptsResponse obtenerMedicamentoClinicoPorProductoComercialLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerMedicamentoClinicoLite(conceptId, descriptionId);
    }

    // REQ-WS-012
    @WebMethod(operationName = "obtenerMedicamentoBasico")
    public RelatedConceptsResponse obtenerMedicamentoBasico(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionados(conceptId, descriptionId, "");
    }

    // REQ-WS-012.1-Lite
    @WebMethod(operationName = "obtenerMedicamentoBasicoLite")
    public RelatedConceptsResponse obtenerMedicamentoBasicoLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionadosLite(conceptId, descriptionId, "");
    }

    // REQ-WS-012
    @WebMethod(operationName = "obtenerMedicamentoBasicoPorMedicamentoClinico")
    public RelatedConceptsResponse obtenerMedicamentoBasicoPorMedicamentoClinico(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerMedicamentoBasico(conceptId, descriptionId);
    }

    // REQ-WS-012.1-Lite
    @WebMethod(operationName = "obtenerMedicamentoBasicoPorMedicamentoClinicoLite")
    public RelatedConceptsResponse obtenerMedicamentoBasicoPorMedicamentoClinicoLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerMedicamentoBasicoLite(conceptId, descriptionId);
    }

    // REQ-WS-013
    // REQ-WS-017
    @WebMethod(operationName = "obtenerProductoComercial")
    public RelatedConceptsResponse obtenerProductoComercial(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionados(conceptId, descriptionId, "");
    }

    // REQ-WS-013 Lite
    // REQ-WS-017 Lite
    @WebMethod(operationName = "obtenerProductoComercialLite")
    public RelatedConceptsResponse obtenerProductoComercialLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionadosLite(conceptId, descriptionId, "");
    }

    // REQ-WS-013
    @WebMethod(operationName = "obtenerProductoComercialPorMedicamentoClinico")
    public RelatedConceptsResponse obtenerProductoComercialPorMedicamentoClinico(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerProductoComercial(conceptId, descriptionId);
    }

    // REQ-WS-013.1-Lite
    @WebMethod(operationName = "obtenerProductoComercialPorMedicamentoClinicoLite")
    public RelatedConceptsResponse obtenerProductoComercialPorMedicamentoClinicoLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerProductoComercialLite(conceptId, descriptionId);
    }

    // REQ-WS-017
    @WebMethod(operationName = "obtenerProductoComercialPorFamiliaProducto")
    public RelatedConceptsResponse obtenerProductoComercialPorFamiliaProducto(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerProductoComercial(conceptId, descriptionId);
    }

    // REQ-WS-017.1-Lite
    @WebMethod(operationName = "obtenerProductoComercialPorFamiliaProductoLite")
    public RelatedConceptsResponse obtenerProductoComercialPorFamiliaProductoLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerProductoComercialLite(conceptId, descriptionId);
    }

    // REQ-WS-014
    @WebMethod(operationName = "obtenerMedicamentoClinicoConEnvase")
    public RelatedConceptsResponse obtenerMedicamentoClinicoConEnvase(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionados(conceptId, descriptionId, "");
    }

    // REQ-WS-014 Lite
    @WebMethod(operationName = "obtenerMedicamentoClinicoConEnvaseLite")
    public RelatedConceptsResponse obtenerMedicamentoClinicoConEnvaseLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionadosLite(conceptId, descriptionId, "");
    }

    // REQ-WS-014
    @WebMethod(operationName = "obtenerMedicamentoClinicoConEnvasePorMedicamentoClinico")
    public RelatedConceptsResponse obtenerMedicamentoClinicoConEnvasePorMedicamentoClinico(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerMedicamentoClinicoConEnvase(conceptId, descriptionId);
    }

    // REQ-WS-014.1 Lite
    @WebMethod(operationName = "obtenerMedicamentoClinicoConEnvasePorMedicamentoClinicoLite")
    public RelatedConceptsResponse obtenerMedicamentoClinicoConEnvasePorMedicamentoClinicoLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerMedicamentoClinicoConEnvaseLite(conceptId, descriptionId);
    }

    // REQ-WS-015
    @WebMethod(operationName = "obtenerProductoComercialConEnvase")
    public RelatedConceptsResponse obtenerProductoComercialConEnvase(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionados(conceptId, descriptionId, "");
    }

    // REQ-WS-015 Lite
    @WebMethod(operationName = "obtenerProductoComercialConEnvaseLite")
    public RelatedConceptsResponse obtenerProductoComercialConEnvaseLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionadosLite(conceptId, descriptionId, "");
    }

    // REQ-WS-015
    @WebMethod(operationName = "obtenerProductoComercialConEnvasePorMedicamentoClinicoConEnvase")
    public RelatedConceptsResponse obtenerProductoComercialConEnvasePorMedicamentoClinicoConEnvase(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerProductoComercialConEnvase(conceptId, descriptionId);
    }

    // REQ-WS-015.1 Lite
    @WebMethod(operationName = "obtenerProductoComercialConEnvasePorMedicamentoClinicoConEnvaseLite")
    public RelatedConceptsResponse obtenerProductoComercialConEnvasePorMedicamentoClinicoConEnvaseLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerProductoComercialConEnvaseLite(conceptId, descriptionId);
    }

    // REQ-WS-016
    @WebMethod(operationName = "obtenerFamiliaProducto")
    public RelatedConceptsResponse obtenerFamiliaProducto(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionados(conceptId, descriptionId, "");
    }

    // REQ-WS-016
    @WebMethod(operationName = "obtenerFamiliaProductoPorGrupoFamiliaProducto")
    public RelatedConceptsResponse obtenerFamiliaProductoPorGrupoFamiliaProducto(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerFamiliaProducto(conceptId, descriptionId);
    }

    // REQ-WS-018
    @WebMethod(operationName = "obtenerProductoClinicoConEnvase")
    public RelatedConceptsResponse obtenerProductoClinicoConEnvase(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionados(conceptId, descriptionId, "");
    }

    // REQ-WS-018 Lite
    @WebMethod(operationName = "obtenerProductoClinicoConEnvaseLite")
    public RelatedConceptsResponse obtenerProductoClinicoConEnvaseLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionadosLite(conceptId, descriptionId, "");
    }

    // REQ-WS-018
    @WebMethod(operationName = "obtenerProductoClinicoConEnvasePorProductoComercial")
    public RelatedConceptsResponse obtenerProductoClinicoConEnvasePorProductoComercial(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerProductoClinicoConEnvase(conceptId, descriptionId);
    }

    // REQ-WS-018.1 Lite
    @WebMethod(operationName = "obtenerProductoClinicoConEnvasePorProductoComercialLite")
    public RelatedConceptsResponse obtenerProductoClinicoConEnvasePorProductoComercialLite(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerProductoClinicoConEnvaseLite(conceptId, descriptionId);
    }

    // REQ-WS-019
    @WebMethod(operationName = "obtenerSustancia")
    public RelatedConceptsResponse obtenerSustancia(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.conceptosRelacionados(conceptId, descriptionId, "");
    }

    // REQ-WS-019
    @WebMethod(operationName = "obtenerSustanciaPorMedicamentoBasico")
    public RelatedConceptsResponse obtenerSustanciaPorMedicamentoBasico(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        // TODO
        return this.obtenerSustancia(conceptId, descriptionId);
    }

    // REQ-WS-020
    @WebMethod(operationName = "obtenerRegistroISP")
    @WebResult(name = "obtenerRegistroISP")
    public RelatedConceptsResponse obtenerRegistroISP(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        return this.conceptosRelacionados(conceptId, descriptionId, "");
    }

    // REQ-WS-020
    @WebMethod(operationName = "obtenerRegistroISPPorProductoComercial")
    public RelatedConceptsResponse obtenerRegistroISPPorProductoComercial(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        return this.obtenerRegistroISP(conceptId, descriptionId);
    }

    // REQ-WS-021
    @WebMethod(operationName = "obtenerBioequivalentes")
    public RelatedConceptsResponse obtenerBioequivalentes(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        return this.conceptosRelacionados(conceptId, descriptionId, "");
    }

    // REQ-WS-021
    @WebMethod(operationName = "obtenerBioequivalentesPorProductoComercial")
    public RelatedConceptsResponse obtenerBioequivalentesPorProductoComercial(
            @XmlElement(required = false)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = false)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws IllegalInputFault {
        return this.obtenerBioequivalentes(conceptId, descriptionId);
    }

}
