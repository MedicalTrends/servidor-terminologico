package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.snapshots.AuditActionType;
import cl.minsal.semantikos.model.snapshots.SnapshotPreprocessingRequest;
import cl.minsal.semantikos.model.snapshots.SnapshotProcessingRequest;
import cl.minsal.semantikos.model.snapshots.SnomedCTSnapshotUpdateDetail;
import cl.minsal.semantikos.model.snomedct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.*;

/**
 * Funciones de base de dato para acceder a los datos de Snomed.
 *
 * @author Andrés Farías on 10/25/16.
 */
@Stateless
public class SnomedCTSnapshotDAOImpl implements SnomedCTSnapshotDAO {

    private static final Logger logger = LoggerFactory.getLogger(SnomedCTSnapshotDAOImpl.class);

    @EJB
    private SnomedCTDAO snomedCTDAO;

    public void persist(List<SnomedCTComponent> snomedCTComponents) {

        List<SnomedCTSnapshotUpdateDetail> snomedCTSnapshotUpdateDetails = new ArrayList<>();

        for (SnomedCTComponent snomedCTComponent : snomedCTComponents) {

            snomedCTSnapshotUpdateDetails.add(new SnomedCTSnapshotUpdateDetail(snomedCTComponent, AuditActionType.SNOMED_CT_CREATION));

        }

        ConnectionBD connect = new ConnectionBD();

        String QUERY = "";

        if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof ConceptSCT )
            QUERY = "{call semantikos.create_concept_sct(?,?,?,?,?)}";
        if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof DescriptionSCT)
            QUERY = "{call semantikos.create_description_sct(?,?,?,?,?,?,?,?,?)}";
        if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof RelationshipSCT)
            QUERY = "{call semantikos.create_relationship_sct(?,?,?,?,?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(QUERY)) {

            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof ConceptSCT) {
                for (ConceptSCT conceptSCT : (List<ConceptSCT>) (Object) snomedCTComponents) {
                    call.setLong(1, conceptSCT.getIdSnomedCT());
                    call.setTimestamp(2, conceptSCT.getEffectiveTime());
                    call.setBoolean(3, conceptSCT.isActive());
                    call.setLong(4, conceptSCT.getModuleId());
                    call.setLong(5, conceptSCT.getDefinitionStatusId());
                    call.addBatch();
                }
            }
            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof DescriptionSCT) {
                for (DescriptionSCT descriptionSCT : (List<DescriptionSCT>) (Object) snomedCTComponents) {
                    call.setLong(1, descriptionSCT.getId());
                    call.setTimestamp(2, descriptionSCT.getEffectiveTime());
                    call.setBoolean(3, descriptionSCT.isActive());
                    call.setLong(4, descriptionSCT.getModuleId());
                    call.setLong(5, descriptionSCT.getConceptId());
                    call.setString(6, descriptionSCT.getLanguageCode());
                    call.setLong(7, descriptionSCT.getDescriptionType().getTypeId());
                    call.setString(8, descriptionSCT.getTerm());
                    call.setLong(9, descriptionSCT.getCaseSignificanceId());
                    call.addBatch();
                }
            }
            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof RelationshipSCT) {
                for (RelationshipSCT relationshipSCT : (List<RelationshipSCT>) (Object) snomedCTComponents) {
                    call.setLong(1, relationshipSCT.getId());
                    call.setTimestamp(2, relationshipSCT.getEffectiveTime());
                    call.setBoolean(3, relationshipSCT.isActive());
                    call.setLong(4, relationshipSCT.getModuleId());
                    call.setLong(5, relationshipSCT.getSourceId());
                    call.setLong(6, relationshipSCT.getDestinationId());
                    call.setLong(7, relationshipSCT.getRelationshipGroup());
                    call.setLong(8, relationshipSCT.getTypeId());
                    call.setLong(9, relationshipSCT.getCharacteristicTypeId());
                    call.setLong(10, relationshipSCT.getModifierId());
                    call.addBatch();
                }
            }

            call.executeBatch();
        }
        catch (SQLException e) {
            String errorMsg = "SnomedCTSnapshotDAOImpl.persist(): Error al persistir Objeto ISnomed: "+e;
            // Aquí se debe registrar el error en el log de salida
            //SnomedCTSnapshotFactory.logError(errorMsg);
            logger.error(errorMsg);
            if(e.getNextException()!=null) {
                logger.error("Detalle: "+e.getNextException().getMessage());
            }
            //throw new EJBException(errorMsg, e);

        }

    }

    public void update(List<SnomedCTComponent> snomedCTComponents) {
        /**
         * Para el caso de las actualizaciones conviene realizar por cada registro para evaluar los casos posibles
         */
        ConnectionBD connect = new ConnectionBD();

        String QUERY = "";

        List<SnomedCTSnapshotUpdateDetail> snomedCTSnapshotUpdateDetails = new ArrayList<>();

        SnomedCTComponent persistedSnomedCTComponent = null;

        for (SnomedCTComponent snomedCTComponent : snomedCTComponents) {

            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof ConceptSCT )
                persistedSnomedCTComponent = snomedCTDAO.getConceptByID(snomedCTComponent.getId());
            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof DescriptionSCT)
                persistedSnomedCTComponent = snomedCTDAO.getDescriptionSCTBy(snomedCTComponent.getId());
            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof RelationshipSCT)
                //persistedSnomedCTComponent = snomedCTDAO.getRelationshipSCTBy(snomedCTComponent.getId());

            if(persistedSnomedCTComponent.equals(snomedCTComponent)) {
                snomedCTComponents.remove(snomedCTComponent);
            }

            snomedCTSnapshotUpdateDetails.add(new SnomedCTSnapshotUpdateDetail(snomedCTComponent, persistedSnomedCTComponent.evaluateChange(snomedCTComponent)));

        }


    }

    private List<Long> getErrors(Map<Long, Long> references) {

        ConnectionBD connect = new ConnectionBD();

        String QUERY = "{call semantikos.get_unexisting_concept_sct_ids(?)}";

        List<Long> errors = new ArrayList<>();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(QUERY)) {

            //if (!map.isEmpty() && map.get(map.keySet().toArray()[0]) instanceof ConceptSCT)
            call.setArray(1, connection.createArrayOf("bigint", references.values().toArray(new Long[references.size()])));

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                errors.add(rs.getLong(1));
            }
        }
        catch (SQLException e) {
            String errorMsg = "SnomedCTSnapshotDAOImpl.getErrors(): Error al obtener referencias inexistentes: "+e;
            logger.error(errorMsg);
            //throw new EJBException(errorMsg, e);
        }

        if(!errors.isEmpty()) {
            Iterator it = references.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if(!errors.contains(pair.getValue())) {
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
            return new ArrayList<Long>((List<Long>) (Object) Arrays.asList(references.keySet().toArray()));
        }

        return errors;
    }


    private List<SnomedCTComponent> getRegistersToUpdate(Map<Long, SnomedCTComponent> map) {

        ConnectionBD connect = new ConnectionBD();

        String QUERY = "";

        if (!map.isEmpty() && map.get(map.keySet().toArray()[0]) instanceof ConceptSCT )
            QUERY = "{call semantikos.get_existing_concept_sct_ids(?)}";
        if (!map.isEmpty() && map.get(map.keySet().toArray()[0]) instanceof DescriptionSCT)
            QUERY = "{call semantikos.get_existing_description_sct_ids(?)}";
        if (!map.isEmpty() && map.get(map.keySet().toArray()[0]) instanceof RelationshipSCT)
            QUERY = "{call semantikos.get_existing_relationship_sct_ids(?)}";

        List<SnomedCTComponent> registersToUpdate = new ArrayList<>();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(QUERY)) {

            //if (!map.isEmpty() && map.get(map.keySet().toArray()[0]) instanceof ConceptSCT)
            call.setArray(1, connection.createArrayOf("bigint", map.keySet().toArray(new Long[map.size()])));

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                registersToUpdate.add(map.get(rs.getLong(1)));
            }
        }
        catch (SQLException e) {
            String errorMsg = "SnomedCTSnapshotDAOImpl.getRegistersToUpdate(): Error al obtener registros a actualizar: "+e;
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return registersToUpdate;
    }



    @Override
    public SnapshotProcessingRequest preprocessRequest(SnapshotPreprocessingRequest snapshotPreprocessingRequest) {

        List<SnomedCTComponent> errors = new ArrayList<>();
        List<SnomedCTComponent> inserts = new ArrayList<>();
        List<SnomedCTComponent> updates = new ArrayList<>();

        List<Long> errorIds = getErrors(snapshotPreprocessingRequest.getReferencesFrom());
        errorIds.addAll(getErrors(snapshotPreprocessingRequest.getReferencesTo()));

        for (Long error : errorIds) {
            errors.add(snapshotPreprocessingRequest.getRegisters().get(error));
            snapshotPreprocessingRequest.getRegisters().remove(error);
        }

        updates = getRegistersToUpdate(snapshotPreprocessingRequest.getRegisters());

        inserts = new ArrayList<SnomedCTComponent>((List<SnomedCTComponent>) (Object) Arrays.asList(snapshotPreprocessingRequest.getRegisters().values().toArray()));

        SnapshotProcessingRequest snapshotProcessingRequest = new SnapshotProcessingRequest();

        snapshotProcessingRequest.setErrors(errors);
        snapshotProcessingRequest.setInserts(inserts);
        snapshotProcessingRequest.setUpdates(updates);

        return snapshotProcessingRequest;
    }

    @Override
    public void processRequest(SnapshotProcessingRequest snapshotProcessingRequest) {

        /**
         * Log errors
         */
        //log(snapshotProcessingRequest.getErrors());
        persist(snapshotProcessingRequest.getInserts());
        update(snapshotProcessingRequest.getUpdates());
    }
}
