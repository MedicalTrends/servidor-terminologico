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

        // Si ya existe esta actualización, se reemplaza
        if(snomedCTSnapshotDAO.getSnomedCTSnapshotUpdateById(snomedCTSnapshotUpdate.getRelease())!=null) {
            snomedCTSnapshotDAO.replaceSnomedCTSnapshotUpdate(snomedCTSnapshotUpdate);
        }

        // Se inicializan los datos de control
        SnomedCTSnapshotFactory.getInstance().initSnomedCTSnapshotUpdate(snomedCTSnapshotUpdate);

        // Primero se procesan los conceptos
        SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getConceptSnapshotPath());

        //Se hace un update de los cambios al buffer de snapshot
        while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createConceptsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
            //Se preprocesan los cambios: Esto es, extraer los elementos a insertar, a actualizar y errores
            preprocessRequest();
            //Se pushean los cambios a la BD
            snomedCTSnapshotUpdate.setSnomedCTSnapshotUpdateDetails(processRequest());
            //Se agregan los cambios a la actualizacion del snapshot y se actualizan las estadísticas
            snomedCTSnapshotUpdate.updateStats();
            snomedCTSnapshotDAO.updateSnomedCTSnapshotUpdate(snomedCTSnapshotUpdate);
            snomedCTSnapshotUpdate.setConceptsProcessed(snomedCTSnapshotUpdate.getConceptsProcessed()+BUFFER_SIZE);
            //Se limpia el request
            clearRequest();
        }

        SnomedCTSnapshotFactory.getInstance().haltReader();

        // Luego se procesan las descripciones
        SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getDescriptionSnapshotPath());

        //Se hace un update de los cambios al buffer de snapshot
        while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createDescriptionsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
            //Se preprocesan los cambios: Esto es, extraer los elementos a insertar, a actualizar y errores
            preprocessRequest();
            //Se pushean los cambios a la BD
            snomedCTSnapshotUpdate.setSnomedCTSnapshotUpdateDetails(processRequest());
            //Se agregan los cambios a la actualizacion del snapshot y se actualizan las estadísticas
            snomedCTSnapshotUpdate.updateStats();
            snomedCTSnapshotDAO.updateSnomedCTSnapshotUpdate(snomedCTSnapshotUpdate);
            snomedCTSnapshotUpdate.setDescriptionsProcessed(snomedCTSnapshotUpdate.getDescriptionsProcessed()+snapshotPreprocessingRequest.getRegisters().size());
            //Se limpia el request
            clearRequest();
        }

        SnomedCTSnapshotFactory.getInstance().haltReader();

        // Luego se procesan las relaciones
        SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getRelationshipSnapshotPath());

        //Se hace un update de los cambios al buffer de snapshot
        while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createRelationshipsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
            //Se preprocesan los cambios: Esto es, extraer los elementos a insertar, a actualizar y errores
            preprocessRequest();
            //Se pushean los cambios a la BD
            snomedCTSnapshotUpdate.setSnomedCTSnapshotUpdateDetails(processRequest());
            //Se agregan los cambios a la actualizacion del snapshot y se actualizan las estadísticas
            snomedCTSnapshotUpdate.updateStats();
            snomedCTSnapshotDAO.updateSnomedCTSnapshotUpdate(snomedCTSnapshotUpdate);
            snomedCTSnapshotUpdate.setDescriptionsProcessed(snomedCTSnapshotUpdate.getDescriptionsProcessed()+snapshotPreprocessingRequest.getRegisters().size());
            //Se limpia el request
            clearRequest();
        }

        SnomedCTSnapshotFactory.getInstance().haltReader();

        // Luego se procesan los refsets
        SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getRefsetSnapshotPath());

        //Se hace un update de los cambios al buffer de snapshot
        while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createRelationshipsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
            //Se commitean los cambios: Esto es, extraer los elementos a insertar y a actualizar
            preprocessRequest();
            //Se pushean los cambios a la BD
            //snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateDetails().addAll(processRequest());
            snomedCTSnapshotUpdate.setSnomedCTSnapshotUpdateDetails(processRequest());
            snomedCTSnapshotUpdate.updateStats();
            snomedCTSnapshotDAO.updateSnomedCTSnapshotUpdate(snomedCTSnapshotUpdate);
            snomedCTSnapshotUpdate.setDescriptionsProcessed(snomedCTSnapshotUpdate.getDescriptionsProcessed()+snapshotPreprocessingRequest.getRegisters().size());
            //Se limpia el request
            clearRequest();
        }

        SnomedCTSnapshotFactory.getInstance().haltReader();

        // Luego se procesan los hijos transitivos
        SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getTransitiveSnapshotPath());

        //Se hace un update de los cambios al buffer de snapshot
        while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createRelationshipsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
            //Se commitean los cambios: Esto es, extraer los elementos a insertar y a actualizar
            preprocessRequest();
            //Se pushean los cambios a la BD
            //snomedCTSnapshotUpdate.getSnomedCTSnapshotUpdateDetails().addAll(processRequest());
            snomedCTSnapshotUpdate.setSnomedCTSnapshotUpdateDetails(processRequest());
            snomedCTSnapshotUpdate.updateStats();
            snomedCTSnapshotDAO.updateSnomedCTSnapshotUpdate(snomedCTSnapshotUpdate);
            snomedCTSnapshotUpdate.setDescriptionsProcessed(snomedCTSnapshotUpdate.getDescriptionsProcessed()+snapshotPreprocessingRequest.getRegisters().size());
            //Se limpia el request
            clearRequest();
        }

        SnomedCTSnapshotFactory.getInstance().haltReader();
    }


    private void preprocessRequest() {
        snapshotProcessingRequest = snomedCTSnapshotDAO.preprocessRequest(snapshotPreprocessingRequest);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private List<SnomedCTSnapshotUpdateDetail> processRequest() {
        return snomedCTSnapshotDAO.processRequest(snapshotProcessingRequest);
    }

    private void clearRequest() {
        snapshotPreprocessingRequest.clear();
        snapshotProcessingRequest.clear();
    }

}
