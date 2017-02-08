package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.auth.UserManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.CrossmapsManager;
import cl.minsal.semantikos.kernel.components.DescriptionManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.ws.request.DescriptionIDorConceptIDRequest;
import cl.minsal.semantikos.ws.response.CrossmapSetMembersResponse;
import cl.minsal.semantikos.ws.response.CrossmapSetsResponse;
import cl.minsal.semantikos.ws.response.IndirectCrossMapSearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@Stateless
public class CrossmapController {

    private static final Logger logger = LoggerFactory.getLogger(CrossmapController.class);

    @EJB
    private CrossmapsManager crossmapManager;

    @EJB
    private DescriptionManager descriptionManager;

    @EJB
    private UserManager userManager;

    @EJB
    private ConceptManager conceptManager;

    /**
     * Este método es responsable de recuperar los crossmaps indirectos asociados al concepto cuya descripción posee el
     * identificador de negocio dado como parámetro.
     *
     * @param descriptionIDorConceptIDRequest El identificador de negocio <em>DESCRIPTION_ID</em> de la descripción.
     * @return La respuesta XML con la lista de los crossmaps indirectos asociados al concepto de la descripción
     * indicada.
     */
    public IndirectCrossMapSearchResponse getIndirectCrossmapsByDescriptionID(DescriptionIDorConceptIDRequest
                                                                                      descriptionIDorConceptIDRequest) {

        /* Se recupera la descripción a partir de su identificador de negocio, y luego el concepto en la que se
        encuentra */
        Description theDescription = descriptionManager.getDescriptionByDescriptionID(descriptionIDorConceptIDRequest
                .getDescriptionId());
        ConceptSMTK conceptSMTK = theDescription.getConceptSMTK();

        /* Luego se recuperan los crossmaps indirectos del concepto */
        List<IndirectCrossmap> indirectCrossmaps = crossmapManager.getIndirectCrossmaps(conceptSMTK);

        return new IndirectCrossMapSearchResponse(indirectCrossmaps);
    }

    /**
     * Este método es responsable de recuperar los crossmapSetMembers de los crossmpas directos asociados al concepto
     * cuya descripción posee el identificador de negocio dado como parámetro.
     *
     * @param desOrConReq El identificador de negocio <em>DESCRIPTION_ID</em> de la descripción.
     * @return La respuesta XML con la lista de los crossmapSetMembers directos asociados al concepto de la descripción
     * indicada.
     */
    public CrossmapSetMembersResponse getDirectCrossmapsSetMembersByDescriptionID(DescriptionIDorConceptIDRequest
                                                                                          desOrConReq) {

        ConceptSMTK conceptSMTK;

        /* Primero se recupera el concepto, ya sea por su CONCEPT_ID o por su DESCRIPTION_ID */
        String conceptId = desOrConReq.getConceptId();
        if (conceptId != null) {
            conceptSMTK = conceptManager.getConceptByCONCEPT_ID(conceptId);
        }

        /* Sino, se recupera el concepto a partir del DESCRIPTION_ID */
        else {
            Description theDescription = descriptionManager.getDescriptionByDescriptionID(desOrConReq.getDescriptionId());
            conceptSMTK = theDescription.getConceptSMTK();

        }

        /* Luego se recuperan los crossmapSetMembers directos del concepto */
        List<CrossmapSetMember> directCrossmapsSetMembersOf = crossmapManager.getDirectCrossmapsSetMembersOf
                (conceptSMTK);

        return new CrossmapSetMembersResponse(directCrossmapsSetMembersOf);
    }

    /**
     * Este método es repsonsable de recuperar los crossmapSetMembers de un crossmapSet dado por su nombre abreviado.
     *
     * @param crossmapSetAbbreviatedName El nombre abreviado del crossmapSet que se quiere recuperar.
     * @return El response de un conjunto de crossmapsetMembers del crossmapSet <code>crossmapSetAbbreviatedName</code>.
     */
    public CrossmapSetMembersResponse getCrossmapSetMembersByCrossmapSetAbbreviatedName(String crossmapSetAbbreviatedName) {

        List<CrossmapSetMember> crossmapSetByAbbreviatedName = crossmapManager.getCrossmapSetByAbbreviatedName
                (crossmapSetAbbreviatedName);
        logger.debug("CrossmapController.getCrossmapSetMembersByCrossmapSetAbbreviatedName:: " +
                "crossmapSetByAbbreviatedName=" + crossmapSetByAbbreviatedName);
        return new CrossmapSetMembersResponse(crossmapSetByAbbreviatedName);
    }

    /**
     * Este método es responsable de recuperar todos los crossmapSets del sistema.
     *
     * @param idInstitution La institución desde la cual se realiza la petición.
     * @return Una lista (response) de crossmapSets.
     */
    public CrossmapSetsResponse getCrossmapSets(String idInstitution) {

        /* Se realiza la validación de seguridad */
        verifyInstitution(idInstitution);

        /* Se retornan los crossmapSets */
        return new CrossmapSetsResponse(crossmapManager.getCrossmapSets());
    }

    /**
     * TODO: Este método es responsable de validar que el usuario actualmente conectado que realiza la ejecución tiene
     * asociado la institución indicada.
     *
     * @param idInstitution El identificador de negocio de la institución.
     */
    private void verifyInstitution(String idInstitution) {

    }
}
