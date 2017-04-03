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
 * @author Andrés Farías on 9/26/16.
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

    private static  int CONCEPT_SNAPSHOT_COMPONENT = 1;

    private static  int DESCRIPTION_SNAPSHOT_COMPONENT = 2;

    private static  int RELATIONSHIP_SNAPSHOT_COMPONENT = 3;

    private List<SnomedCTComponent> inserts = new ArrayList<>();

    private List<SnomedCTComponent> updates = new ArrayList<>();

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public SnapshotProcessingResult processSnapshot(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate) {

        // Primero se procesan los conceptos
        SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getConceptSnapshotPath());

        //Se hace un update de los cambios al buffer de snapshot
        while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createConceptsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
            //Se preprocesa los cambios: Esto es, extraer los elementos a insertar y a actualizar
            preprocessRequest();
            //Se pushean los cambios a la BD
            processRequest();
            //Se limpia el request
            clearRequest();
        }

        SnomedCTSnapshotFactory.getInstance().haltReader();

        // Luego se procesan las descripciones
        SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getDescriptionSnapshotPath());

        //Se hace un update de los cambios al buffer de snapshot
        while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createDescriptionsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
            //Se commitean los cambios: Esto es, extraer los elementos a insertar y a actualizar
            preprocessRequest();
            //Se pushean los cambios a la BD
            processRequest();
            //Se limpia el request
            clearRequest();
        }

        SnomedCTSnapshotFactory.getInstance().haltReader();

        // Luego se procesan las relaciones
        SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.getRelationshipSnapshotPath());

        //Se hace un update de los cambios al buffer de snapshot
        while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createRelationshipsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
            //Se commitean los cambios: Esto es, extraer los elementos a insertar y a actualizar
            preprocessRequest();
            //Se pushean los cambios a la BD
            processRequest();
            //Se limpia el request
            clearRequest();
        }

        SnomedCTSnapshotFactory.getInstance().haltReader();

        // Luego se procesan los refsets
        //SnomedCTSnapshotFactory.getInstance().initReader(snomedCTSnapshotUpdate.());

        //Se hace un update de los cambios al buffer de snapshot
        while(!(snapshotPreprocessingRequest = SnomedCTSnapshotFactory.getInstance().createRelationshipsSnapshotPreprocessingRequest(BUFFER_SIZE)).isEmpty()) {
            //Se commitean los cambios: Esto es, extraer los elementos a insertar y a actualizar
            preprocessRequest();
            //Se pushean los cambios a la BD
            processRequest();
            //Se limpia el request
            clearRequest();
        }

        SnomedCTSnapshotFactory.getInstance().haltReader();

        return new SnapshotProcessingResult();
    }


    private void preprocessRequest() {
        snapshotProcessingRequest = snomedCTSnapshotDAO.preprocessRequest(snapshotPreprocessingRequest);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void processRequest() {
        snomedCTSnapshotDAO.processRequest(snapshotProcessingRequest);
    }

    private void clearRequest() {
        snapshotPreprocessingRequest.clear();
        snapshotProcessingRequest.clear();
    }


}
