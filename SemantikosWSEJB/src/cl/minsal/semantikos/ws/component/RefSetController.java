package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.InstitutionManager;
import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.modelws.fault.IllegalInputFault;
import cl.minsal.semantikos.modelws.request.ConceptToRefSetRequest;
import cl.minsal.semantikos.modelws.response.ConceptToRefSetResponse;
import cl.minsal.semantikos.modelws.response.RefSetResponse;
import cl.minsal.semantikos.modelws.response.RefSetSearchResponse;
import cl.minsal.semantikos.modelws.response.RefSetsResponse;
import cl.minsal.semantikos.modelws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.mapping.RefSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.util.*;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by Development on 2016-11-18.
 */
@Stateless
public class RefSetController {

    private static final Logger logger = LoggerFactory.getLogger(RefSetController.class);

    @EJB
    private RefSetManager refSetManager;

    @EJB
    private ConceptManager conceptManager;

    @EJB
    private InstitutionManager institutionManager;

    @EJB
    private ConceptController conceptController;

    public List<RefSetSearchResponse> findRefSetsByDescriptions(List<String> descriptionIds, Boolean includeInstitutions, String idStablishment) throws NotFoundFault {

        /* Se recupera el concepto asociado a la descripción */
        List<ConceptSMTK> conceptsByDescriptionID = new ArrayList<>();

        for (String descriptionId : descriptionIds) {
            try {
                conceptsByDescriptionID.add(this.conceptManager.getConceptByDescriptionID(descriptionId));
            }
            catch (EJBException e) {
                throw new NotFoundFault("Descripcion no encontrada: " + descriptionId);
            }
        }

        Institution connectionInstitution = institutionManager.getInstitutionById(Long.parseLong(idStablishment));

        List<RefSetSearchResponse> res = new ArrayList<>();

        for (ConceptSMTK conceptByDescriptionID : conceptsByDescriptionID) {
            RefSetSearchResponse refSetSearchResponse = new RefSetSearchResponse();
            List<RefSet> refSets = refSetManager.findByConcept(conceptByDescriptionID);

            Iterator<RefSet> it = refSets.iterator();

            while (it.hasNext()) {
                RefSet refSet = it.next();
                if(!refSet.isValid()) {
                    it.remove();
                }
            }

            List<String> validInstitutions = new ArrayList<>();

            if(!connectionInstitution.getName().equals("MINSAL") && includeInstitutions ) {
                validInstitutions = Arrays.asList(new String[]{"MINSAL", connectionInstitution.getName()});
            }

            if(!connectionInstitution.getName().equals("MINSAL") && !includeInstitutions ) {
                validInstitutions = Arrays.asList(new String[]{connectionInstitution.getName()});
            }

            if(connectionInstitution.getName().equals("MINSAL") && !includeInstitutions ) {
                validInstitutions = Arrays.asList(new String[]{"MINSAL"});
            }

            it = refSets.iterator();

            if(!validInstitutions.isEmpty()) {
                while (it.hasNext()) {
                    RefSet refSet = it.next();
                    if(!validInstitutions.contains(refSet.getInstitution().getName())) {
                        it.remove();
                    }
                }
            }

            refSetSearchResponse.setConceptId(conceptByDescriptionID.getConceptID());
            refSetSearchResponse.setDescriptionId(conceptByDescriptionID.getDescriptionFavorite().getDescriptionId());
            refSetSearchResponse.setDescription(conceptByDescriptionID.getDescriptionFavorite().getTerm());
            refSetSearchResponse.setCategory(conceptByDescriptionID.getCategory().getName());
            refSetSearchResponse.setRefSetsResponse(new RefSetsResponse(refSets));

            res.add(refSetSearchResponse);
        }

        return res;
    }

    public List<RefSet> findRefsets(List<String> refSetsNames) throws NotFoundFault {

        logger.debug("RefSetController.findRefsets(" + refSetsNames + ")");

        if (isEmpty(refSetsNames)) {
            logger.debug("RefSetController.findRefsets(" + refSetsNames + ") --> " + 0 + " refsets " +
                    "retornados.");
            return Collections.emptyList();
        }

        /* Validacion de parámetros */
        //List<RefSet> refSets = this.refSetManager.getAllRefSets();
        List<RefSet> someRefsets = new ArrayList<>();
        Map<String, RefSet> refSetsMap = new HashMap<>();

        List<RefSet> refSets = refSetManager.findRefSetsByName(refSetsNames);

        /*
        Iterator<String> it = refSetsNames.iterator();

        while (it.hasNext()) {
            String refSetName = it.next();
            if(refSetName.isEmpty()) {
                it.remove();
            }
        }
        */

        for (RefSet refSet : refSets) {
            refSetsMap.put(refSet.getName(), refSet);
        }

        /* Se recuperan los refsets solicitados */
        try {
            //refSets = this.refSetManager.findRefSetsByName(refSetsNames);
            for (String refSetName : refSetsNames) {
                if(refSetsMap.containsKey(refSetName)) {
                    someRefsets.add(refSetsMap.get(refSetName));
                }
                else {
                    throw new NotFoundFault("No existe un RefSet de nombre: "+refSetName);
                }
            }

            logger.debug("RefSetController.findRefsets(" + refSetsNames + ") --> " + refSets.size() + " refsets " +
                    "retornados.");
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }

        return someRefsets;
    }

    private boolean isEmpty(List<String> list) {
        return list == null || list.isEmpty() || (list.size() == 1 && list.contains(EMPTY_STRING));
    }

    /**
     * TODO: Claramente este método no fue implementado correctamente.
     *
     * @param includeInstitutions El parametro no considerado
     * @return La lista de RefSets encapsulada.
     * @throws NotFoundFault Arrojada si no ... ???
     */
    public RefSetsResponse refSetList(Boolean includeInstitutions, String idStablishment) throws NotFoundFault {

        Institution connectionInstitution = institutionManager.getInstitutionById(Long.parseLong(idStablishment));

        List<RefSet> refSets = this.refSetManager.getAllRefSets();

        Iterator<RefSet> it = refSets.iterator();

        while (it.hasNext()) {
            RefSet refSet = it.next();
            if(!refSet.isValid()) {
                it.remove();
            }
        }

        List<String> validInstitutions = new ArrayList<>();

        if(!connectionInstitution.getName().equals("MINSAL") && includeInstitutions ) {
            validInstitutions = Arrays.asList(new String[] {"MINSAL", connectionInstitution.getName()});
        }

        if(!connectionInstitution.getName().equals("MINSAL") && !includeInstitutions ) {
            validInstitutions = Arrays.asList(new String[] {connectionInstitution.getName()});
        }

        if(connectionInstitution.getName().equals("MINSAL") && !includeInstitutions ) {
            validInstitutions = Arrays.asList(new String[] {"MINSAL"});
        }

        it = refSets.iterator();

        if(!validInstitutions.isEmpty()) {
            while (it.hasNext()) {
                RefSet refSet = it.next();
                if(!validInstitutions.contains(refSet.getInstitution().getName())) {
                    it.remove();
                }
            }
        }

        return new RefSetsResponse(refSets);
    }

    /**
     *
     * @param request El parametro no considerado
     * @return La lista de RefSets encapsulada.
     * @throws NotFoundFault Arrojada si no ... ???
     */
    public ConceptToRefSetResponse addConceptToRefSet(ConceptToRefSetRequest request, User user, Institution institution)
            throws NotFoundFault, IllegalInputFault {

        if ((request.getConceptID() == null || "".equals(request.getConceptID()))
                && (request.getDescriptionID() == null || "".equals(request.getDescriptionID()))) {
            throw new IllegalInputFault("Debe indicar por lo menos un idConcepto o idDescripcion");
        }

        ConceptSMTK conceptSMTK = getConcept(request.getConceptID(), request.getDescriptionID());

        List<RefSet> refSets = refSetManager.findRefsetsByName(request.getRefSetName());

        if(refSets.isEmpty()) {
            throw new NotFoundFault("No existe un RefSet de nombre: '"+ request.getRefSetName()+"'");
        }

        if(!conceptSMTK.isValid()) {
            throw new NotFoundFault("El concepto: '"+ conceptSMTK +"' no es valido");
        }

        RefSet refSet = refSets.get(0);

        if(!refSet.isValid()) {
            throw new NotFoundFault("El refset: '"+ refSet +"' no es valido");
        }

        /*
        if(!user.getInstitutions().contains(institution)) {
            throw new NotFoundFault("El idEstablecimiento no correponde a ninguno de los establecimientos del usuario: '"+user+"'");
        }
        */

        if(!institution.equals(refSet.getInstitution())) {
            throw new NotFoundFault("El establecimiento dueño del RefSet: '"+refSet.getInstitution()+"' no corresponde al establecimiento de conexión: '"+institution+"'");
        }

        if(refSet.getConcepts().contains(conceptSMTK)) {
            throw new NotFoundFault("El RefSet: '"+refSet+"' ya contiene el concepto: '"+conceptSMTK+"'");
        }

        refSetManager.bindConceptToRefSet(conceptSMTK, refSet, user);

        return new ConceptToRefSetResponse(conceptSMTK);
    }

    /**
     *
     * @param request El parametro no considerado
     * @return La lista de RefSets encapsulada.
     * @throws NotFoundFault Arrojada si no ... ???
     */
    public ConceptToRefSetResponse removeConceptFromRefSet(ConceptToRefSetRequest request, User user, Institution institution)
            throws NotFoundFault, IllegalInputFault {

        if ((request.getConceptID() == null || "".equals(request.getConceptID()))
                && (request.getDescriptionID() == null || "".equals(request.getDescriptionID()))) {
            throw new IllegalInputFault("Debe indicar por lo menos un idConcepto o idDescripcion");
        }

        ConceptSMTK conceptSMTK = getConcept(request.getConceptID(), request.getDescriptionID());

        List<RefSet> refSets = refSetManager.findRefsetsByName(request.getRefSetName());

        if(refSets.isEmpty()) {
            throw new NotFoundFault("No existe un RefSet de nombre: '"+ request.getRefSetName()+"'");
        }

        if(!conceptSMTK.isValid()) {
            throw new NotFoundFault("El concepto: '"+ conceptSMTK +"' no es valido");
        }

        RefSet refSet = refSets.get(0);

        if(!refSet.isValid()) {
            throw new NotFoundFault("El refset: '"+ refSet +"' no es valido");
        }

        /*
        if(!user.getInstitutions().contains(institution)) {
            throw new NotFoundFault("El idEstablecimiento no correponde a ninguno de los establecimientos del usuario: '"+user+"'");
        }
        */

        if(!institution.equals(refSet.getInstitution())) {
            throw new NotFoundFault("El establecimiento dueño del RefSet: '"+refSet.getInstitution()+"' no corresponde al establecimiento de conexión: '"+institution+"'");
        }

        if(!refSet.getConcepts().contains(conceptSMTK)) {
            throw new NotFoundFault("El RefSet: '"+refSet+"' no contiene el concepto: '"+conceptSMTK+"'");
        }

        refSetManager.unbindConceptToRefSet(conceptSMTK, refSets.get(0), user);

        return new ConceptToRefSetResponse(conceptSMTK);
    }

    public RefSetResponse getResponse(RefSet refSet) throws NotFoundFault {
        if (refSet == null) {
            throw new NotFoundFault("RefSet no encontrado");
        }
        return RefSetMapper.map(refSet);
    }

    private ConceptSMTK getConcept(String conceptId, String descriptionId) throws NotFoundFault {
        ConceptSMTK conceptSMTK = null;

        try {
            if (descriptionId != null && !descriptionId.isEmpty()) {
                conceptSMTK = this.conceptManager.getConceptByDescriptionID(descriptionId);
            } else {
                conceptSMTK = this.conceptManager.getConceptByCONCEPT_ID(conceptId);
            }
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }

        if (conceptSMTK == null) {
            throw new NotFoundFault("Concepto no encontrado: " + (conceptId != null ? conceptId : "") +
                    (descriptionId != null ? descriptionId : ""));
        }

        return conceptSMTK;
    }
}
