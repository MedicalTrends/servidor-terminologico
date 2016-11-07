package cl.minsal.semantikos.model.snomedct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    private ConceptSCT createConceptSCTFromString(String string) {

        String[] tokens = string.split("\\t");

        if (tokens.length != 5)
            throw new RuntimeException();

        long idSnomedCT = Long.parseLong(tokens[0]);

        Timestamp effectiveTime = Timestamp.valueOf(tokens[1]);

        boolean isActive = Boolean.parseBoolean(tokens[2]);

        long moduleId = Long.parseLong(tokens[3]);

        long definitionStatusId = Long.parseLong(tokens[4]);

        return new ConceptSCT(idSnomedCT, effectiveTime, isActive, moduleId, definitionStatusId);

    }

    public List<ConceptSCT> createConceptsSCTFromPath(String path) {

        Path conceptSnapshot = Paths.get(path);
        List<ConceptSCT> conceptSCTs = new ArrayList<>();

        try {

            //BufferedReader reader = Files.newBufferedReader(FileSystems.getDefault().getPath(".", name), Charset.defaultCharset() );

            BufferedReader reader = Files.newBufferedReader(conceptSnapshot, Charset.defaultCharset());

            String line;

            while ((line = reader.readLine()) != null) {
                conceptSCTs.add(createConceptSCTFromString(line));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return conceptSCTs;
    }

    private DescriptionSCT createDescriptionSCTFromString(String string) throws Exception {

        String[] tokens = string.split("\\t");

        if (tokens.length != 9)
            throw new RuntimeException();

        //long id	effectiveTime	active	moduleId	conceptId	languageCode	typeId	term	caseSignificanceId

        long id = Long.parseLong(tokens[0]);

        Timestamp effectiveTime = Timestamp.valueOf(tokens[1]);

        boolean active = Boolean.parseBoolean(tokens[2]);

        long moduleId = Long.parseLong(tokens[3]);

        long conceptId = Long.parseLong(tokens[4]);

        String languageCode = tokens[5];

        long typeId = Long.parseLong(tokens[6]);
        DescriptionSCTType descriptionSCTType = DescriptionSCTType.valueOf(typeId);

        String term = tokens[7];

        long caseSignificanceId = Long.parseLong(tokens[8]);

        return new DescriptionSCT(id, descriptionSCTType, effectiveTime, active, moduleId, conceptId, languageCode, term, caseSignificanceId);

    }

    public List<DescriptionSCT> createDescriptionsSCTFromPath(String path) {

        Path descriptionSnapshot = Paths.get(path);
        List<DescriptionSCT> descriptionSCTs = new ArrayList<>();

        try {

            //BufferedReader reader = Files.newBufferedReader(FileSystems.getDefault().getPath(".", name), Charset.defaultCharset() );

            BufferedReader reader = Files.newBufferedReader(descriptionSnapshot, Charset.defaultCharset());

            String line;

            while ((line = reader.readLine()) != null) {
                descriptionSCTs.add(createDescriptionSCTFromString(line));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Error al parsear una descripción SCT", e);
        }

        return descriptionSCTs;
    }
}
