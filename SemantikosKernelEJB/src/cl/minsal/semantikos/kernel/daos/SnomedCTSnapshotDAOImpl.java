package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.snapshots.*;
import cl.minsal.semantikos.model.snomedct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import java.sql.*;
import java.util.*;

/**
 * Funciones de base de dato para acceder a los datos de Snomed.
 *
 * @author Diego Soto
 */
@Stateless
public class SnomedCTSnapshotDAOImpl implements SnomedCTSnapshotDAO {

    private static final Logger logger = LoggerFactory.getLogger(SnomedCTSnapshotDAOImpl.class);

    @EJB
    private SnomedCTDAO snomedCTDAO;

    public List<SnomedCTSnapshotUpdateDetail> persist(List<SnomedCTComponent> snomedCTComponents) {

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
        if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof LanguageRefsetSCT)
            QUERY = "{call semantikos.create_language_ref_set_sct(?,?,?,?,?,?,?)}";
        if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof LanguageRefsetSCT)
            QUERY = "{call semantikos.create_language_ref_set_sct(?,?,?,?,?,?,?)}";

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
            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof LanguageRefsetSCT) {
                for (LanguageRefsetSCT languageRefsetSCT: (List<LanguageRefsetSCT>) (Object) snomedCTComponents) {
                    call.setLong(1, languageRefsetSCT.getId());
                    call.setTimestamp(2, languageRefsetSCT.getEffectiveTime());
                    call.setBoolean(3, languageRefsetSCT.isActive());
                    call.setLong(4, languageRefsetSCT.getModuleId());
                    call.setLong(5, languageRefsetSCT.getRefsetId());
                    call.setLong(6, languageRefsetSCT.getReferencedComponentId());
                    call.setLong(7, languageRefsetSCT.getAcceptabilityId());
                    call.addBatch();
                }
            }
            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof TransitiveSCT) {
                for (TransitiveSCT transitiveSCT: (List<TransitiveSCT>) (Object) snomedCTComponents) {
                    call.setLong(1, transitiveSCT.getIdPartent());
                    call.setLong(2, transitiveSCT.getIdChild());
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

        return snomedCTSnapshotUpdateDetails;
    }

    public List<SnomedCTSnapshotUpdateDetail> update(List<SnomedCTComponent> snomedCTComponents) {
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

        return snomedCTSnapshotUpdateDetails;
    }

    public List<SnomedCTSnapshotUpdateDetail> delete(List<SnomedCTComponent> snomedCTComponents) {

        List<SnomedCTSnapshotUpdateDetail> snomedCTSnapshotUpdateDetails = new ArrayList<>();

        for (SnomedCTComponent snomedCTComponent : snomedCTComponents) {

            snomedCTSnapshotUpdateDetails.add(new SnomedCTSnapshotUpdateDetail(snomedCTComponent, AuditActionType.SNOMED_CT_REMOVAL));

        }

        ConnectionBD connect = new ConnectionBD();

        String QUERY = "";

        if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof ConceptSCT )
            QUERY = "{call semantikos.delete_concept_sct(?)}";
        if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof DescriptionSCT)
            QUERY = "{call semantikos.delete_description_sct(?)}";
        if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof RelationshipSCT)
            QUERY = "{call semantikos.delete_relationship_sct(?)}";
        if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof LanguageRefsetSCT)
            QUERY = "{call semantikos.delete_language_ref_set_sct(?)}";
        if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof LanguageRefsetSCT)
            QUERY = "{call semantikos.delete_transitive_sct(?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(QUERY)) {

            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof ConceptSCT) {
                for (ConceptSCT conceptSCT : (List<ConceptSCT>) (Object) snomedCTComponents) {
                    call.setLong(1, conceptSCT.getIdSnomedCT());
                    call.addBatch();
                }
            }
            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof DescriptionSCT) {
                for (DescriptionSCT descriptionSCT : (List<DescriptionSCT>) (Object) snomedCTComponents) {
                    call.setLong(1, descriptionSCT.getId());
                    call.addBatch();
                }
            }
            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof RelationshipSCT) {
                for (RelationshipSCT relationshipSCT : (List<RelationshipSCT>) (Object) snomedCTComponents) {
                    call.setLong(1, relationshipSCT.getId());
                    call.addBatch();
                }
            }
            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof LanguageRefsetSCT) {
                for (LanguageRefsetSCT languageRefsetSCT: (List<LanguageRefsetSCT>) (Object) snomedCTComponents) {
                    call.setLong(1, languageRefsetSCT.getId());
                    call.addBatch();
                }
            }
            if (!snomedCTComponents.isEmpty() && snomedCTComponents.get(0) instanceof TransitiveSCT) {
                for (TransitiveSCT transitiveSCT: (List<TransitiveSCT>) (Object) snomedCTComponents) {
                    call.setLong(1, transitiveSCT.getIdPartent());
                    call.setLong(2, transitiveSCT.getIdChild());
                    call.addBatch();
                }
            }

            call.executeBatch();
        }
        catch (SQLException e) {
            String errorMsg = "SnomedCTSnapshotDAOImpl.persist(): Error al eliminar Objeto ISnomed: "+e;
            // Aquí se debe registrar el error en el log de salida
            //SnomedCTSnapshotFactory.logError(errorMsg);
            logger.error(errorMsg);
            if(e.getNextException()!=null) {
                logger.error("Detalle: "+e.getNextException().getMessage());
            }
            //throw new EJBException(errorMsg, e);

        }

        return snomedCTSnapshotUpdateDetails;
    }

    public List<SnomedCTSnapshotUpdateDetail> log(List<SnomedCTComponent> snomedCTComponents) {

        List<SnomedCTSnapshotUpdateDetail> snomedCTSnapshotUpdateDetails = new ArrayList<>();

        for (SnomedCTComponent snomedCTComponent : snomedCTComponents) {

            snomedCTSnapshotUpdateDetails.add(new SnomedCTSnapshotUpdateDetail(snomedCTComponent, AuditActionType.SNOMED_CT_ERROR));

        }

        return snomedCTSnapshotUpdateDetails;
    }

    private List<Long> getErrors(Map<Long, SnomedCTComponent> registers, Map<Long, Long> references) {

        ConnectionBD connect = new ConnectionBD();

        String QUERY = "{call semantikos.get_unexisting_concept_sct_ids(?)}";

        if (!registers.isEmpty() && registers.get(0) instanceof TransitiveSCT) {
            QUERY = "{call semantikos.get_unexisting_relationship_sct_ids(?)}";
        }

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
            return new ArrayList<>((List<Long>) (Object) Arrays.asList(references.keySet().toArray()));
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

    private List<SnomedCTComponent> getRegistersToDelete() {

        ConnectionBD connect = new ConnectionBD();

        String QUERY = "{call semantikos.get_leftovers_transitive_sct(?)}";

        List<SnomedCTComponent> registersToDelete = new ArrayList<>();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(QUERY)) {

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                TransitiveSCT transitiveSCT = new TransitiveSCT(rs.getLong(1), rs.getLong(2));
                registersToDelete.add(transitiveSCT);
            }
        }
        catch (SQLException e) {
            String errorMsg = "SnomedCTSnapshotDAOImpl.getRegistersToUpdate(): Error al obtener registros a actualizar: "+e;
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return registersToDelete;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public SnapshotProcessingRequest preprocessRequest(SnapshotPreprocessingRequest snapshotPreprocessingRequest) {

        List<SnomedCTComponent> errors = new ArrayList<>();
        List<SnomedCTComponent> inserts;
        List<SnomedCTComponent> updates;

        List<Long> errorIds = getErrors(snapshotPreprocessingRequest.getRegisters(), snapshotPreprocessingRequest.getReferencesFrom());
        errorIds.addAll(getErrors(snapshotPreprocessingRequest.getRegisters(), snapshotPreprocessingRequest.getReferencesTo()));

        for (Long error : errorIds) {
            errors.add(snapshotPreprocessingRequest.getRegisters().get(error));
            snapshotPreprocessingRequest.getRegisters().remove(error);
        }

        updates = getRegistersToUpdate(snapshotPreprocessingRequest.getRegisters());

        inserts = new ArrayList<>((List<SnomedCTComponent>) (Object) Arrays.asList(snapshotPreprocessingRequest.getRegisters().values().toArray()));

        inserts.removeAll(updates);

        SnapshotProcessingRequest snapshotProcessingRequest = new SnapshotProcessingRequest();

        snapshotProcessingRequest.setErrors(errors);
        snapshotProcessingRequest.setInserts(inserts);
        snapshotProcessingRequest.setUpdates(updates);

        return snapshotProcessingRequest;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<SnomedCTSnapshotUpdateDetail> processRequest(SnapshotProcessingRequest snapshotProcessingRequest) {

        List<SnomedCTSnapshotUpdateDetail> snomedCTSnapshotUpdateDetails = new ArrayList<>();

        List<SnomedCTSnapshotUpdateDetail> createdDetail = persist(snapshotProcessingRequest.getInserts());
        List<SnomedCTSnapshotUpdateDetail> updatedDetail = update(snapshotProcessingRequest.getUpdates());
        List<SnomedCTSnapshotUpdateDetail> errorDetail = log(snapshotProcessingRequest.getErrors());

        snomedCTSnapshotUpdateDetails.addAll(createdDetail);
        snomedCTSnapshotUpdateDetails.addAll(updatedDetail);
        snomedCTSnapshotUpdateDetails.addAll(errorDetail);

        return snomedCTSnapshotUpdateDetails;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<SnomedCTSnapshotUpdateDetail> postProcessRequest(SnomedCTSnapshotUpdate snomedSCTSnapshotUpdate) {

        List<SnomedCTComponent> deletes = getRegistersToDelete();

        snomedSCTSnapshotUpdate.setRemoved(deletes.size());

        return delete(deletes);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void persistSnomedCTSnapshotUpdate(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate) {
        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.create_snapshot_sct_update(?,?,?)}")) {

            call.setString(1, snomedCTSnapshotUpdate.getRelease());
            call.setTimestamp(2, snomedCTSnapshotUpdate.getDate());
            call.setString(3, snomedCTSnapshotUpdate.getUser().getEmail());

            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                /* Se recupera el ID del concepto persistido */
                snomedCTSnapshotUpdate.setId(rs.getLong(1));
            } else {
                String errorMsg = "La actualización de snapshot SnomedCT no fue creada por una razon desconocida. Alertar al area de desarrollo sobre esto";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al persistir La actualización de snapshot SnomedCT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        createSnomedCTSnapshotUpdateState(snomedCTSnapshotUpdate);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateSnomedCTSnapshotUpdate(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate) {
        ConnectionBD connect = new ConnectionBD();
        boolean updated = false;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.update_snapshot_sct_update(?,?,?,?,?,?,?,?)}")) {

            call.setLong(1, snomedCTSnapshotUpdate.getTotal());
            call.setLong(2, snomedCTSnapshotUpdate.getCreated());
            call.setLong(3, snomedCTSnapshotUpdate.getRemoved());
            call.setLong(4, snomedCTSnapshotUpdate.getUnmodified());
            call.setLong(5, snomedCTSnapshotUpdate.getInvalidated());
            call.setLong(6, snomedCTSnapshotUpdate.getRestored());
            call.setLong(7, snomedCTSnapshotUpdate.getFailed());
            call.setLong(8, snomedCTSnapshotUpdate.getId());

            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                /* Se recupera el status de la transacción */
                updated = rs.getBoolean(1);
            } else {
                String errorMsg = "La actualización de snapshot SnomedCT no fue actualizada por una razon desconocida. Alertar al area de desarrollo sobre esto";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar La actualización de snapshot SnomedCT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        if (updated) {
            logger.info("Información de actualización de snapshot SnomedCT  actualizada exitosamente.");
        } else {
            String errorMsg = "Información de actualización de snapshot SnomedCT  no fue actualizada.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg);
        }

        updateSnomedCTSnapshotUpdateState(snomedCTSnapshotUpdate);
        addSnomedCTSnapshotUpdateDetails(snomedCTSnapshotUpdate);

    }

    private void createSnomedCTSnapshotUpdateState(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate) {

        ConnectionBD connect = new ConnectionBD();

        String QUERY = "{call semantikos.create_snapshot_sct_update_state(?,?,?,?,?,?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(QUERY)) {

            call.setBoolean(1, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isConceptsProcessed());
            call.setInt(2, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getConceptsFileLine());
            call.setBoolean(3, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isDescriptionsProcessed());
            call.setInt(4, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getDescriptionsFileLine());
            call.setBoolean(5, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isRelationshipsProcessed());
            call.setInt(6, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getRelationshipsFileLine());
            call.setBoolean(7, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isRefsetsProcessed());
            call.setInt(8, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getRefsetsFileLine());
            call.setBoolean(9, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isTransitivesProcessed());
            call.setInt(10, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getTransitivesFileLine());
            call.setLong(11, snomedCTSnapshotUpdate.getId());

            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                /* Se recupera el status de la transacción */
                snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().setId(rs.getLong(1));
            } else {
                String errorMsg = "El estado de la actualización del snapshot SnomedCT no fue creado por una razon desconocida. Alertar al area de desarrollo sobre esto";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al crear el estado de La actualización de snapshot SnomedCT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

    }

    private void updateSnomedCTSnapshotUpdateState(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate) {

        ConnectionBD connect = new ConnectionBD();

        String QUERY = "{call semantikos.update_snapshot_sct_update_state(?,?,?,?,?,?,?,?,?,?,?)}";

        boolean updated = false;

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(QUERY)) {

            call.setBoolean(1, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isConceptsProcessed());
            call.setInt(2, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getConceptsFileLine());
            call.setBoolean(3, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isDescriptionsProcessed());
            call.setInt(4, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getDescriptionsFileLine());
            call.setBoolean(5, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isRelationshipsProcessed());
            call.setInt(6, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getRelationshipsFileLine());
            call.setBoolean(7, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isRefsetsProcessed());
            call.setInt(8, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getRefsetsFileLine());
            call.setBoolean(9, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isTransitivesProcessed());
            call.setInt(10, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getTransitivesFileLine());
            call.setLong(11, snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getId());

            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                /* Se recupera el status de la transacción */
                updated = rs.getBoolean(1);
            } else {
                String errorMsg = "El estado de la actualización del snapshot SnomedCT no fue actualizada por una razon desconocida. Alertar al area de desarrollo sobre esto";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar el estado de La actualización de snapshot SnomedCT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        if (updated) {
            logger.info("Información del estado de la actualización de snapshot SnomedCT actualizada exitosamente.");
        } else {
            String errorMsg = "Información del estado de la actualización de snapshot SnomedCT no fue actualizada.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg);
        }

    }

    private void addSnomedCTSnapshotUpdateDetails(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate) {

        ConnectionBD connect = new ConnectionBD();

        String QUERY = "{call semantikos.create_snapshot_sct_update_detail(?,?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(QUERY)) {

            for (SnomedCTSnapshotUpdateDetail snomedCTSnapshotUpdateDetail : snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateDetails()) {

                int[] paramPositions = {1,2,3,4,5};

                if(snomedCTSnapshotUpdateDetail.getSnomedCTComponent() instanceof ConceptSCT)
                    paramPositions =  new int[]{1,2,3,4,5};

                if(snomedCTSnapshotUpdateDetail.getSnomedCTComponent() instanceof DescriptionSCT)
                    paramPositions = new int[]{2,1,3,4,5};

                if(snomedCTSnapshotUpdateDetail.getSnomedCTComponent() instanceof RelationshipSCT)
                    paramPositions = new int[]{3,1,2,4,5};

                if(snomedCTSnapshotUpdateDetail.getSnomedCTComponent() instanceof LanguageRefsetSCT)
                    paramPositions = new int[]{4,1,2,3,5};

                if(snomedCTSnapshotUpdateDetail.getSnomedCTComponent() instanceof TransitiveSCT)
                    paramPositions = new int[]{5,1,2,3,4};

                call.setLong(paramPositions[0], snomedCTSnapshotUpdateDetail.getSnomedCTComponent().getId());
                call.setNull(paramPositions[1], Types.BIGINT);
                call.setNull(paramPositions[2], Types.BIGINT);
                call.setNull(paramPositions[3], Types.BIGINT);
                call.setNull(paramPositions[4], Types.BIGINT);
                call.setLong(6, snomedCTSnapshotUpdateDetail.getAuditActionType().getId());
                call.setLong(7, snomedCTSnapshotUpdate.getId());

                call.addBatch();

            }

            call.executeBatch();
        }
        catch (SQLException e) {
            String errorMsg = "Error al persistir El detalle de actualización de snapshot SnomedCT "+e;
            // Aquí se debe registrar el error en el log de salida
            //SnomedCTSnapshotFactory.logError(errorMsg);
            logger.error(errorMsg);
            if(e.getNextException()!=null) {
                logger.error("Detalle: "+e.getNextException().getMessage());
            }
            //throw new EJBException(errorMsg, e);

        }
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public SnomedCTSnapshotUpdate getSnomedCTSnapshotUpdateById(String id) {
        return null;
    }

    @Override
    public List<SnomedCTSnapshotUpdate> getAllSnomedCTSnapshotUpdates() {
        return null;
    }
}
