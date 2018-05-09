package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.model.LoadException;
import cl.minsal.semantikos.model.LoadLog;
import cl.minsal.semantikos.model.SMTKLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

            //smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles Conceptos Básicos", INFO));
            smtkLoader.printInfo(new LoadLog("Comprobando estructura DataFiles Conceptos Básicos", INFO));

            //this.path = Paths.get(smtkLoader.BASIC_CONCEPTS_PATH);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.BASIC_CONCEPTS_PATH)));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            int lines = 1;

            /**
             * Recuperar el header del archivo
             */
            String header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(BasicConceptLoader.basicConceptFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.BASIC_CONCEPTS_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            while ( reader.readLine() != null) lines++;
            reader.close();

            //this.path = Paths.get(smtkLoader.BASIC_DESCRIPTIONS_PATH);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.BASIC_DESCRIPTIONS_PATH)));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            /**
             * Recuperar el header del archivo
             */
            header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(BasicConceptLoader.basicDescriptionFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.BASIC_DESCRIPTIONS_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            //this.path = Paths.get(smtkLoader.BASIC_RELATIONSHIPS_PATH);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.BASIC_RELATIONSHIPS_PATH)));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            /**
             * Recuperar el header del archivo
             */
            header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(BasicConceptLoader.basicRelationshipFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.BASIC_RELATIONSHIPS_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            //smtkLoader.setConceptsTotal(lines-1);
            //smtkLoader.setConceptsProcessed(0);

            smtkLoader.setTotal(lines-1);
            smtkLoader.setProcessed(0);

            //smtkLoader.logTick();
            smtkLoader.printTick();

        } catch (IOException e) {
            throw e;
        } catch (LoadException e) {
            if(e.isSevere()) {
                throw e;
            }
            else {
                //smtkLoader.logError(e);
                smtkLoader.printError(e);
            }
        }
    }

    public void checkSubstanceDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            //smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles Sustancias", INFO));
            smtkLoader.printInfo(new LoadLog("Comprobando estructura DataFiles Sustancias", INFO));

            //this.path = Paths.get(smtkLoader.SUBSTANCE_PATH);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.SUBSTANCE_PATH)));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

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

            //smtkLoader.setConceptsTotal(lines-1);
            //smtkLoader.setConceptsProcessed(0);

            smtkLoader.setTotal(lines-1);
            smtkLoader.setProcessed(0);

            //smtkLoader.logTick();
            smtkLoader.printTick();

        } catch (IOException e) {
            throw e;
        } catch (LoadException e) {
            if(e.isSevere()) {
                throw e;
            }
            else {
                //smtkLoader.logError(e);
                smtkLoader.printError(e);
            }
        }
    }

    public void checkMBDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            //smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles MB", INFO));
            smtkLoader.printInfo(new LoadLog("Comprobando estructura DataFiles MB", INFO));

            //this.path = Paths.get(smtkLoader.MB_PATH);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.MB_PATH)));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

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

            //smtkLoader.setConceptsTotal(lines-1);
            //smtkLoader.setConceptsProcessed(0);

            smtkLoader.setTotal(lines-1);
            smtkLoader.setProcessed(0);

            //smtkLoader.logTick();
            smtkLoader.printTick();

        } catch (IOException e) {
            throw e;
        } catch (LoadException e) {
            if(e.isSevere()) {
                throw e;
            }
            else {
                //smtkLoader.logError(e);
                smtkLoader.printError(e);
            }
        }
    }

    public void checkMCDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            //smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles MC", INFO));
            smtkLoader.printInfo(new LoadLog("Comprobando estructura DataFiles MC", INFO));

            //this.path = Paths.get(smtkLoader.MC_PATH);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.MC_PATH)));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

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

            //this.path = Paths.get(smtkLoader.MC_VIAS_ADM_PATH);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.MC_VIAS_ADM_PATH)));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            /**
             * Recuperar el header del archivo
             */
            header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(MCConceptLoader.admViasFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.MC_VIAS_ADM_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            //smtkLoader.setConceptsTotal(lines-1);
            //smtkLoader.setConceptsProcessed(0);

            smtkLoader.setTotal(lines-1);
            smtkLoader.setProcessed(0);

            //smtkLoader.logTick();
            smtkLoader.printTick();

        } catch (IOException e) {
            throw e;
        } catch (LoadException e) {
            if(e.isSevere()) {
                throw e;
            }
            else {
                //smtkLoader.logError(e);
                smtkLoader.printError(e);
            }
        }
    }

    public void checkMCCEDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles MCCE", INFO));

            //this.path = Paths.get(smtkLoader.MCCE_PATH);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.MCCE_PATH)));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            int lines = 1;

            /**
             * Recuperar el header del archivo
             */
            String header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(MCCEConceptLoader.mcceConceptFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.MCCE_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            while (reader.readLine() != null) lines++;
            reader.close();

            //smtkLoader.setConceptsTotal(lines-1);
            //smtkLoader.setConceptsProcessed(0);
            smtkLoader.setTotal(lines-1);
            smtkLoader.setProcessed(0);

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

    public void checkGFPDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles GFP", INFO));

            //this.path = Paths.get(smtkLoader.GFP_PATH);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.GFP_PATH)));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            int lines = 1;

            /**
             * Recuperar el header del archivo
             */
            String header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(GFPConceptLoader.gfpConceptFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.GFP_PATH, null, "El encabezado del archivo no es válido", ERROR);
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

    public void checkFPDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles FP", INFO));

            //this.path = Paths.get(smtkLoader.FP_PATH);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.FP_PATH)));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            int lines = 1;

            /**
             * Recuperar el header del archivo
             */
            String header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(FPConceptLoader.fpConceptFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.FP_PATH, null, "El encabezado del archivo no es válido", ERROR);
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

    public void checkPCDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles PC", INFO));

            //this.path = Paths.get(smtkLoader.PC_PATH);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.PC_PATH)));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            int lines = 1;

            /**
             * Recuperar el header del archivo
             */
            String header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(PCConceptLoader.pcConceptFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.PC_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            while (reader.readLine() != null) lines++;
            reader.close();

            //smtkLoader.setConceptsTotal(lines-1);
            //smtkLoader.setConceptsProcessed(0);

            smtkLoader.setTotal(lines-1);
            smtkLoader.setProcessed(0);

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

    public void checkPCCEDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles PCCE", INFO));

            //this.path = Paths.get(smtkLoader.PCCE_PATH);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.PCCE_PATH)));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            int lines = 1;

            /**
             * Recuperar el header del archivo
             */
            String header = reader.readLine();

            if(!assertHeader((List<String>) (Object) Arrays.asList(PCCEConceptLoader.pcceConceptFields.keySet().toArray()),
                    Arrays.asList(header.split(separator)))) {
                throw new LoadException(smtkLoader.PCCE_PATH, null, "El encabezado del archivo no es válido", ERROR);
            }

            while (reader.readLine() != null) lines++;
            reader.close();

            //smtkLoader.setConceptsTotal(lines-1);
            //smtkLoader.setConceptsProcessed(0);

            smtkLoader.setTotal(lines-1);
            smtkLoader.setProcessed(0);

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
