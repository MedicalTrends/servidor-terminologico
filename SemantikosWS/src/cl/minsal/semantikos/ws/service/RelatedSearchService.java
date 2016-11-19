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
    @WebResult(name = "sugerenciasDeDescripciones")
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
    @WebResult(name = "conceptosRelacionados")
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
    @WebResult(name = "conceptosRelacionadosLite")
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
    @WebResult(name = "obtenerMedicamentoClinico")
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
    @WebResult(name = "obtenerMedicamentoClinicoLite")
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
    @WebResult(name = "obtenerMedicamentoClinicoPorMedicamentoBasico")
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
    @WebResult(name = "obtenerMedicamentoClinicoPorMedicamentoBasicoLite")
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
    @WebResult(name = "obtenerMedicamentoClinicoPorProductoComercial")
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
    @WebResult(name = "obtenerMedicamentoClinicoPorProductoComercialLite")
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
    @WebResult(name = "obtenerMedicamentoBasico")
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
    @WebResult(name = "obtenerMedicamentoBasicoLite")
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
    @WebResult(name = "obtenerMedicamentoBasicoPorMedicamentoClinico")
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
    @WebResult(name = "obtenerMedicamentoBasicoPorMedicamentoClinicoLite")
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
    @WebResult(name = "obtenerProductoComercial")
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
    @WebResult(name = "obtenerProductoComercialLite")
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
    @WebResult(name = "obtenerProductoComercialPorMedicamentoClinico")
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
    @WebResult(name = "obtenerProductoComercialPorMedicamentoClinicoLite")
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
    @WebResult(name = "obtenerProductoComercialPorFamiliaProducto")
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
    @WebResult(name = "obtenerProductoComercialPorFamiliaProductoLite")
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
    @WebResult(name = "obtenerMedicamentoClinicoConEnvase")
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
    @WebResult(name = "obtenerMedicamentoClinicoConEnvaseLite")
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
    @WebResult(name = "obtenerMedicamentoClinicoConEnvasePorMedicamentoClinico")
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
    @WebResult(name = "obtenerMedicamentoClinicoConEnvasePorMedicamentoClinicoLite")
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
    @WebResult(name = "obtenerProductoComercialConEnvase")
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
    @WebResult(name = "obtenerProductoComercialConEnvaseLite")
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
    @WebResult(name = "obtenerProductoComercialConEnvasePorMedicamentoClinicoConEnvase")
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
    @WebResult(name = "obtenerProductoComercialConEnvasePorMedicamentoClinicoConEnvaseLite")
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
    @WebResult(name = "obtenerFamiliaProducto")
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
    @WebResult(name = "obtenerFamiliaProductoPorGrupoFamiliaProducto")
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
    @WebResult(name = "obtenerProductoClinicoConEnvase")
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
    @WebResult(name = "obtenerProductoClinicoConEnvaseLite")
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
    @WebResult(name = "obtenerProductoClinicoConEnvasePorProductoComercial")
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
    @WebResult(name = "obtenerProductoClinicoConEnvasePorProductoComercialLite")
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
    @WebResult(name = "obtenerSustancia")
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
    @WebResult(name = "obtenerSustanciaPorMedicamentoBasico")
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
    @WebResult(name = "obtenerRegistroISPPorProductoComercial")
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
    @WebResult(name = "obtenerBioequivalentes")
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
    @WebResult(name = "obtenerBioequivalentesPorProductoComercial")
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
