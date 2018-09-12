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
            int start = 0, end = 0;

            //Se calculan los offsets
            if(loader instanceof SubstanceLoader) {
                start = 0;
                end = start + SubstanceLoader.LENGHT;
            }
            if(loader instanceof MBLoader) {
                start = SubstanceLoader.LENGHT;
                end = start + MBLoader.LENGHT;
            }
            if(loader instanceof MCLoader) {
                start = SubstanceLoader.LENGHT + MBLoader.LENGHT;
                end = start + loader.fields.size();
            }
            if(loader instanceof MCCELoader) {
                start = SubstanceLoader.LENGHT + MBLoader.LENGHT + MCLoader.LENGHT;
                end = start + loader.fields.size();
            }
            if(loader instanceof GFPLoader) {
                start = SubstanceLoader.LENGHT + MBLoader.LENGHT + MCLoader.LENGHT + MCCELoader.LENGHT;
                end = start + loader.fields.size();
            }
            if(loader instanceof FPLoader) {
                start = SubstanceLoader.LENGHT + MBLoader.LENGHT + MCLoader.LENGHT + MCCELoader.LENGHT + GFPLoader.LENGHT;
                end = start + loader.fields.size();
            }
            if(loader instanceof PCLoader) {
                start = SubstanceLoader.LENGHT + MBLoader.LENGHT + MCLoader.LENGHT + MCCELoader.LENGHT + GFPLoader.LENGHT + FPLoader.LENGHT;
                end = start + loader.fields.size();
            }
            if(loader instanceof PCCELoader) {
                start = SubstanceLoader.LENGHT + MBLoader.LENGHT + MCLoader.LENGHT + MCCELoader.LENGHT + GFPLoader.LENGHT + FPLoader.LENGHT + PCLoader.LENGHT;
                end = start + loader.fields.size();
            }

            String[] headerFields = header.split(";");

            //Se remueven añadidos a los nombres de los campos para evitar inconsistencias
            for(int i = start; i < end; ++i) {
                cleanHeader.add(headerFields[i].split(" ")[0]);
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
