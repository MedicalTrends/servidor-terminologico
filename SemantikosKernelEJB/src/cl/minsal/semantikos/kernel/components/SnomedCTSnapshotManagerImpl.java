package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.SnomedCTSnapshotDAO;
import cl.minsal.semantikos.model.businessrules.ConceptSearchBR;
import cl.minsal.semantikos.model.snapshots.*;
import cl.minsal.semantikos.model.snomedct.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.*;

/**
 * @author Diego Soto
 */
@Stateless
public class SnomedCTSnapshotManagerImpl implements SnomedCTSnapshotManager {

    @EJB
    private SnomedCTSnapshotDAO snomedCTSnapshotDAO;

    @EJB
    private ConceptSearchBR conceptSearchBR;

    private SnapshotPreprocessingRequest snapshotPreprocessingRequest = new SnapshotPreprocessingRequest();

    private SnapshotProcessingRequest snapshotProcessingRequest = new SnapshotProcessingRequest();

    private static int BUFFER_SIZE = 100000;

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void updateSnapshot(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate) {

        // Si no existe esta actualización, se crea una nueva
        if(snomedCTSnapshotDAO.getSnomedCTSnapshotUpdateById(snomedCTSnapshotUpdate.getRelease())==null) {
            snomedCTSnapshotDAO.persistSnomedCTSnapshotUpdate(snomedCTSnapshotUpdate);
        }

        // Se inicializan los datos de control
        SnomedCTSnapshotFactory.getInstance().initSnomedCTSnapshotUpdate(snomedCTSnapshotUpdate);

        snomedCTSnapshotUpdate.setStarted(true);

        if(!snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isConceptsProcessed()) {
            // Primero se procesan los conceptos
            SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getConceptSnapshotPath(), snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getConceptsFileLine());

            //Se hace un update de los cambios al buffer de snapshot
            while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createConceptsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
                processRequest(snomedCTSnapshotUpdate);
            }

            SnomedCTSnapshotFactory.getInstance().haltReader();
        }

        if(!snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isDescriptionsProcessed()) {
            // Luego se procesan las descripciones
            SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getDescriptionSnapshotPath(), snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getDescriptionsFileLine());

            //Se hace un update de los cambios al buffer de snapshot
            while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createDescriptionsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
                processRequest(snomedCTSnapshotUpdate);
            }

            SnomedCTSnapshotFactory.getInstance().haltReader();
        }

        if(!snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isRelationshipsProcessed()) {
            // Luego se procesan las relaciones
            SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getRelationshipSnapshotPath(), snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getRelationshipsFileLine());

            //Se hace un update de los cambios al buffer de snapshot
            while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createRelationshipsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
                processRequest(snomedCTSnapshotUpdate);
                snomedCTSnapshotUpdate.setRelationshipsProcessed(snomedCTSnapshotUpdate.getRelationshipsProcessed()+snapshotPreprocessingRequest.getRegisters().size());
            }

            SnomedCTSnapshotFactory.getInstance().haltReader();
        }

        if(!snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isRefsetsProcessed()) {
            // Luego se procesan los refsets
            SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getRefsetSnapshotPath(), snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getRefsetsFileLine());

            //Se hace un update de los cambios al buffer de snapshot
            while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createRefsetsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
                processRequest(snomedCTSnapshotUpdate);
            }

            SnomedCTSnapshotFactory.getInstance().haltReader();
        }

        if(!snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().isRefsetsProcessed()) {
            // Luego se procesan los hijos transitivos
            SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getTransitiveSnapshotPath(), snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateState().getTransitivesFileLine());

            //Se hace un update de los cambios al buffer de snapshot
            while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createTransitivesSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
                processRequest(snomedCTSnapshotUpdate);
                snomedCTSnapshotUpdate.setRefsetsProcessed(snomedCTSnapshotUpdate.getTransitiveProcessed()+snapshotPreprocessingRequest.getRegisters().size());
            }

            SnomedCTSnapshotFactory.getInstance().haltReader();

            postProcessRequest(snomedCTSnapshotUpdate);
        }

    }

    private void processRequest(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate) {
        //Se preprocesan los cambios: Esto es, extraer los elementos a insertar, a actualizar y errores
        snapshotProcessingRequest = snomedCTSnapshotDAO.preprocessRequest(snapshotPreprocessingRequest);
        //Se pushean los cambios a la BD
        snomedCTSnapshotUpdate.setSnomedCTSnapshotUpdateDetails(snomedCTSnapshotDAO.processRequest(snapshotProcessingRequest));
        snomedCTSnapshotUpdate.updateStats();
        //Se agregan los cambios a la actualizacion del snapshot y se actualizan las estadísticas
        snomedCTSnapshotDAO.updateSnomedCTSnapshotUpdate(snomedCTSnapshotUpdate);
        //Se limpia el request
        snapshotPreprocessingRequest.clear();
        snapshotProcessingRequest.clear();
    }

    private void postProcessRequest(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate) {
        //Se pushean los cambios a la BD
        snomedCTSnapshotUpdate.setSnomedCTSnapshotUpdateDetails(snomedCTSnapshotDAO.postProcessRequest(snomedCTSnapshotUpdate));
        //Se agregan los cambios a la actualizacion del snapshot y se actualizan las estadísticas
        snomedCTSnapshotUpdate.updateStats();
        snomedCTSnapshotDAO.updateSnomedCTSnapshotUpdate(snomedCTSnapshotUpdate);
        //Se limpia el request
        snapshotPreprocessingRequest.clear();
        snapshotProcessingRequest.clear();
    }


}
