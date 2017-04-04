package cl.minsal.semantikos.model.snapshots;

import cl.minsal.semantikos.model.snomedct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 28-09-16.
 */
public class SnomedCTSnapshotFactory {

    private static final SnomedCTSnapshotFactory instance = new SnomedCTSnapshotFactory();

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(SnomedCTSnapshotFactory.class);

    public static SnomedCTSnapshotFactory getInstance() {
        return instance;
    }

    public Path path;

    public BufferedReader reader; // = Files.newBufferedReader(conceptSnapshot, Charset.defaultCharset());

    private ConceptSCT createConceptSCTFromString(String string) {

        String[] tokens = string.split("\\t");

        if (tokens.length != 5)
            throw new RuntimeException();

        long idSnomedCT = Long.parseLong(tokens[0]);

        String time = tokens[1].substring(0, 4) + "-" + tokens[1].substring(4, 6) + "-" + tokens[1].substring(6, 8) + " 00:00:00";

        Timestamp effectiveTime = Timestamp.valueOf(time);

        boolean isActive = (tokens[2].equals("1")) ? true : false;

        long moduleId = Long.parseLong(tokens[3]);

        long definitionStatusId = Long.parseLong(tokens[4]);

        return new ConceptSCT(idSnomedCT, effectiveTime, isActive, moduleId, definitionStatusId);

    }

    public void initSnomedCTSnapshotUpdate(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate) {

        try {
            this.path = Paths.get(snomedCTSnapshotUpdate.getConceptSnapshotPath());
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            int lines = 0;
            while (reader.readLine() != null) lines++;
            reader.close();

            snomedCTSnapshotUpdate.setConceptsTotal(lines-1);

            this.path = Paths.get(snomedCTSnapshotUpdate.getDescriptionSnapshotPath());
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            lines = 0;
            while (reader.readLine() != null) lines++;
            reader.close();

            snomedCTSnapshotUpdate.setDescriptionsTotal(lines-1);

            this.path = Paths.get(snomedCTSnapshotUpdate.getRelationshipSnapshotPath());
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            lines = 0;
            while (reader.readLine() != null) lines++;
            reader.close();

            snomedCTSnapshotUpdate.setRelationshipsTotal(lines-1);

            this.path = Paths.get(snomedCTSnapshotUpdate.getRefsetSnapshotPath());
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            lines = 0;
            while (reader.readLine() != null) lines++;
            reader.close();

            snomedCTSnapshotUpdate.setRefsetsTotal(lines-1);

            this.path = Paths.get(snomedCTSnapshotUpdate.getTransitiveSnapshotPath());
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            lines = 0;
            while (reader.readLine() != null) lines++;
            reader.close();

            snomedCTSnapshotUpdate.setTransitiveTotal(lines-1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initReader(String path) {

        this.path = Paths.get(path);
        try {
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());
            /**
             * skip header line (supposed to include header)
             */
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void haltReader() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SnapshotPreprocessingRequest createConceptsSnapshotPreprocessingRequest(int streamSize) {

        SnapshotPreprocessingRequest snapshotPreprocessingRequest = new SnapshotPreprocessingRequest();

        try {

            String line;
            int cont = 0;

            while ((line = reader.readLine()) != null) {

                if(cont >= streamSize) {
                    break;
                }

                ConceptSCT conceptSCT = createConceptSCTFromString(line);

                snapshotPreprocessingRequest.getRegisters().put(conceptSCT.getIdSnomedCT(), conceptSCT);

                ++cont;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return snapshotPreprocessingRequest;
    }

    private DescriptionSCT createDescriptionSCTFromString(String string) throws Exception {

        String[] tokens = string.split("\\t");

        if (tokens.length != 9)
            throw new RuntimeException();

        //long id	effectiveTime	active	moduleId	conceptId	languageCode	typeId	term	caseSignificanceId

        long id = Long.parseLong(tokens[0]);

        String time = tokens[1].substring(0, 4) + "-" + tokens[1].substring(4, 6) + "-" + tokens[1].substring(6, 8) + " 00:00:00";

        Timestamp effectiveTime = Timestamp.valueOf(time);

        boolean isActive = (tokens[2].equals("1")) ? true : false;

        long moduleId = Long.parseLong(tokens[3]);

        long conceptId = Long.parseLong(tokens[4]);

        String languageCode = tokens[5];

        long typeId = Long.parseLong(tokens[6]);

        DescriptionSCTType descriptionSCTType = DescriptionSCTType.valueOf(typeId);

        String term = tokens[7];

        long caseSignificanceId = Long.parseLong(tokens[8]);

        return new DescriptionSCT(id, descriptionSCTType, effectiveTime, isActive, moduleId, conceptId, languageCode, term, caseSignificanceId);

    }

    public SnapshotPreprocessingRequest createDescriptionsSnapshotPreprocessingRequest(int streamSize) {

        SnapshotPreprocessingRequest snapshotPreprocessingRequest = new SnapshotPreprocessingRequest();

        try {

            String line;
            int cont = 0;

            while ((line = reader.readLine()) != null) {

                if(cont >= streamSize) {
                    break;
                }

                try {
                    DescriptionSCT descriptionSCT = createDescriptionSCTFromString(line);
                    snapshotPreprocessingRequest.getRegisters().put(descriptionSCT.getId(), descriptionSCT);
                    snapshotPreprocessingRequest.getReferencesFrom().put(descriptionSCT.getId(), descriptionSCT.getConceptId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ++cont;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return snapshotPreprocessingRequest;
    }


    private RelationshipSCT createRelationshipSCTFromString(String string) throws Exception {

        String[] tokens = string.split("\\t");

        if (tokens.length != 10)
            throw new RuntimeException();

        //long id	effectiveTime	active	moduleId	conceptId	languageCode	typeId	term	caseSignificanceId

        long id = Long.parseLong(tokens[0]);

        String time = tokens[1].substring(0, 4) + "-" + tokens[1].substring(4, 6) + "-" + tokens[1].substring(6, 8) + " 00:00:00";

        Timestamp effectiveTime = Timestamp.valueOf(time);

        boolean isActive = (tokens[2].equals("1")) ? true : false;

        long moduleId = Long.parseLong(tokens[3]);

        long sourceId = Long.parseLong(tokens[4]);

        long destinationId = Long.parseLong(tokens[5]);

        long relationshipGroup = Long.parseLong(tokens[6]);

        long typeId = Long.parseLong(tokens[7]);

        long characteristicTypeId = Long.parseLong(tokens[8]);

        long modifierId = Long.parseLong(tokens[9]);

        return new RelationshipSCT(id, effectiveTime, isActive, moduleId, sourceId, destinationId, relationshipGroup, typeId, characteristicTypeId, modifierId);

    }

    public SnapshotPreprocessingRequest createRelationshipsSnapshotPreprocessingRequest(int streamSize) {

        SnapshotPreprocessingRequest snapshotPreprocessingRequest = new SnapshotPreprocessingRequest();

        try {

            String line;
            int cont = 0;

            while ((line = reader.readLine()) != null) {

                if(cont >= streamSize) {
                    break;
                }

                try {
                    RelationshipSCT relationshipSCT = createRelationshipSCTFromString(line);
                    snapshotPreprocessingRequest.getRegisters().put(relationshipSCT.getId(), relationshipSCT);
                    snapshotPreprocessingRequest.getReferencesFrom().put(relationshipSCT.getId(), relationshipSCT.getSourceId());
                    snapshotPreprocessingRequest.getReferencesTo().put(relationshipSCT.getId(), relationshipSCT.getDestinationId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ++cont;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return snapshotPreprocessingRequest;
    }

    private LanguageRefsetSCT createLanguageRefsetSCTFromString(String string) throws Exception {

        String[] tokens = string.split("\\t");

        if (tokens.length != 7)
            throw new RuntimeException();

        //long id	effectiveTime	active	moduleId	conceptId	languageCode	typeId	term	caseSignificanceId

        long id = Long.parseLong(tokens[0]);

        String time = tokens[1].substring(0, 4) + "-" + tokens[1].substring(4, 6) + "-" + tokens[1].substring(6, 8) + " 00:00:00";

        Timestamp effectiveTime = Timestamp.valueOf(time);

        boolean isActive = (tokens[2].equals("1")) ? true : false;

        long moduleId = Long.parseLong(tokens[3]);

        long refsetId = Long.parseLong(tokens[4]);

        long referencedComponentId = Long.parseLong(tokens[5]);

        long acceptabilityId = Long.parseLong(tokens[6]);

        return new LanguageRefsetSCT(id, effectiveTime, isActive, moduleId, refsetId, referencedComponentId, acceptabilityId);

    }

    public SnapshotPreprocessingRequest createRefsetsSnapshotPreprocessingRequest(int streamSize) {

        SnapshotPreprocessingRequest snapshotPreprocessingRequest = new SnapshotPreprocessingRequest();

        try {

            String line;
            int cont = 0;

            while ((line = reader.readLine()) != null) {

                if(cont >= streamSize) {
                    break;
                }

                try {
                    LanguageRefsetSCT languageRefsetSCT = createLanguageRefsetSCTFromString(line);
                    snapshotPreprocessingRequest.getRegisters().put(languageRefsetSCT.getId(), languageRefsetSCT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ++cont;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return snapshotPreprocessingRequest;
    }
}
