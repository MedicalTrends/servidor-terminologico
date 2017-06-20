package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.model.LoadException;
import cl.minsal.semantikos.model.LoadLog;
import cl.minsal.semantikos.model.SMTKLoader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;

/**
 * Created by root on 12-06-17.
 */
public class Initializer extends EntityLoader {

    public void checkBasicConceptsDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles Conceptos Básicos", INFO));

            this.path = Paths.get(smtkLoader.BASIC_CONCEPTS_PATH);
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            int lines = 1;

            /**
             * Recuperar el header del archivo
             */
            String header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(BasicConceptLoader.basicConceptFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.BASIC_CONCEPTS_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            while (reader.readLine() != null) lines++;
            reader.close();

            this.path = Paths.get(smtkLoader.BASIC_DESCRIPTIONS_PATH);
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            /**
             * Recuperar el header del archivo
             */
            header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(BasicConceptLoader.basicDescriptionFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.BASIC_DESCRIPTIONS_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            this.path = Paths.get(smtkLoader.BASIC_RELATIONSHIPS_PATH);
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            /**
             * Recuperar el header del archivo
             */
            header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(BasicConceptLoader.basicRelationshipFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.BASIC_RELATIONSHIPS_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            smtkLoader.setConceptsTotal(lines-1);
            smtkLoader.setConceptsProcessed(0);

            smtkLoader.logTick();

        } catch (IOException e) {
            throw e;
        } catch (LoadException e) {
            if(e.isSevere()) {
                throw e;
            }
            else {
                smtkLoader.logError(e);
            }
        }
    }

    public void checkSubstanceDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles Sustancias", INFO));

            this.path = Paths.get(smtkLoader.SUBSTANCE_PATH);
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            int lines = 1;

            /**
             * Recuperar el header del archivo
             */
            String header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(SubstanceConceptLoader.substanceConceptFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.SUBSTANCE_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            while (reader.readLine() != null) lines++;
            reader.close();

            smtkLoader.setConceptsTotal(lines-1);
            smtkLoader.setConceptsProcessed(0);

            smtkLoader.logTick();

        } catch (IOException e) {
            throw e;
        } catch (LoadException e) {
            if(e.isSevere()) {
                throw e;
            }
            else {
                smtkLoader.logError(e);
            }
        }
    }

    public void checkMBDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles MB", INFO));

            this.path = Paths.get(smtkLoader.MB_PATH);
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            int lines = 1;

            /**
             * Recuperar el header del archivo
             */
            String header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(MBConceptLoader.mbConceptFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.MB_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            while (reader.readLine() != null) lines++;
            reader.close();

            smtkLoader.setConceptsTotal(lines-1);
            smtkLoader.setConceptsProcessed(0);

            smtkLoader.logTick();

        } catch (IOException e) {
            throw e;
        } catch (LoadException e) {
            if(e.isSevere()) {
                throw e;
            }
            else {
                smtkLoader.logError(e);
            }
        }
    }

    public void checkMCDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles MC", INFO));

            this.path = Paths.get(smtkLoader.MC_PATH);
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            int lines = 1;

            /**
             * Recuperar el header del archivo
             */
            String header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(MCConceptLoader.mcConceptFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.MC_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            while (reader.readLine() != null) lines++;
            reader.close();

            this.path = Paths.get(smtkLoader.MC_VIAS_ADM_PATH);
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            /**
             * Recuperar el header del archivo
             */
            header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(MCConceptLoader.admViasFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.MC_VIAS_ADM_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            smtkLoader.setConceptsTotal(lines-1);
            smtkLoader.setConceptsProcessed(0);

            smtkLoader.logTick();

        } catch (IOException e) {
            throw e;
        } catch (LoadException e) {
            if(e.isSevere()) {
                throw e;
            }
            else {
                smtkLoader.logError(e);
            }
        }
    }
}
