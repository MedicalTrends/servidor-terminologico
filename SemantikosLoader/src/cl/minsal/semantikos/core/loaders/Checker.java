package cl.minsal.semantikos.core.loaders;

import cl.minsal.semantikos.loaders.EntityLoader;
import cl.minsal.semantikos.model.LoadException;
import cl.minsal.semantikos.model.LoadLog;
import cl.minsal.semantikos.model.SMTKLoader;
import cl.minsal.semantikos.model.users.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;

/**
 * Created by root on 12-06-17.
 */
public class Checker extends BaseLoader {

    public Checker(User user) {
        super(user);
    }

    public void checkDataFile(SMTKLoader smtkLoader, BaseLoader loader) throws LoadException, IOException {

        try {

            smtkLoader.printInfo(new LoadLog("Comprobando estructura DataFiles '" + loader.category.getName() + "'", INFO));

            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(loader.dataFile)));

            int lines = 1;

            // 1a Línea contiene la categoría, saltar a la siguiente línea
            reader.readLine();

            lines = 2;

            /**
             * Recuperar el header del archivo
             */
            String header = reader.readLine();

            List<String> cleanHeader = new ArrayList<>();
            List<String> fields = (List<String>) (Object) Arrays.asList(loader.fields.keySet().toArray());

            //Se remueven añadidos a los nombres de los campos para evitar inconsistencias
            for (String field : header.split(";")) {
                cleanHeader.add(field.split(" ")[0]);
            }

            try {
                assertHeader(fields, cleanHeader);
            }
            catch (LoadException ex) {
                throw ex;
            }

            while (reader.readLine() != null) lines++;
            reader.close();

            smtkLoader.setTotal(lines-1);
            smtkLoader.setProcessed(0);

            smtkLoader.printTick();

        } catch (IOException e) {
            throw e;
        } catch (LoadException e) {
            throw e;
        }
    }

    @Override
    public void loadConceptFromFileLine(String line) throws LoadException {

    }
}
