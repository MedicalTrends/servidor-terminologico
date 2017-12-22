package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.model.LoadException;
import cl.minsal.semantikos.model.LoadLog;
import cl.minsal.semantikos.model.SMTKLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;

/**
 * Created by root on 12-06-17.
 */
public class Initializer extends EntityLoader {

    public void checkSubstanceDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            smtkLoader.logInfo(new LoadLog("Comprobando estructura DataFiles Sustancias", INFO));

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

            smtkLoader.setTotal(lines-1);

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
