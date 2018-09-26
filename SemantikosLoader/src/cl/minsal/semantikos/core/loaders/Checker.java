package cl.minsal.semantikos.core.loaders;

import cl.minsal.semantikos.loaders.EntityLoader;
import cl.minsal.semantikos.model.LoadException;
import cl.minsal.semantikos.model.LoadLog;
import cl.minsal.semantikos.model.SMTKLoader;
import cl.minsal.semantikos.model.users.User;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;
import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by root on 12-06-17.
 */
public class Checker extends BaseLoader {

    private static final String UTF_8 = "UTF-8";

    public Checker(User user) {
        super(user);
    }

    public void checkDataFile(SMTKLoader smtkLoader, BaseLoader loader) throws LoadException, IOException {

        try {

            smtkLoader.printInfo(new LoadLog("Comprobando DataFiles '" + loader.category.getName() + "'", INFO));

            if(!Arrays.asList(new String[]{"txt","csv"}).contains(getFileExtension(loader.dataFile))) {
                throw new LoadException(loader.dataFile, "", "Extension de archivo no valida. Extensiones soportadas: '.csv' y '.txt'", ERROR);
            }

            String encoding = detectEncoding(loader.dataFile);

            if(encoding.equals(EMPTY_STRING)) {
                throw new LoadException(loader.dataFile, "", "Ninguna codificacion detectada. La codificacion del archivo debe ser 'UTF-8'", ERROR);
            }

            if(!encoding.equals(UTF_8)) {
                throw new LoadException(loader.dataFile, "", "Codificacion detectada: " + encoding +". La codificacion del archivo debe ser 'UTF-8'", ERROR);
            }

            //reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(loader.dataFile)));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(loader.dataFile), "UTF-8"));

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

            //Se calculan los offsets de acuerdo a la categoría
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

    //header es el archivo
    public void assertHeader(List<String> fields, List<String> header) throws LoadException {
        for (String field : fields) {
            if(!header.contains(field)) {
                String msg = "El encabezado del archivo no contiene el campo '" + field + "'. Lista ordenada de campos: " + fields.toString();
                LoadException ex = new LoadException(dataFile, "", msg, ERROR);
                throw ex;
            }
        }
    }

    String getFileExtension(String fileName) {

        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i+1);
        }

        return extension;
    }

    String detectEncoding(String filename) throws IOException {

        byte[] buf = new byte[4096];

        java.io.InputStream fis = java.nio.file.Files.newInputStream(java.nio.file.Paths.get(filename));

        // (1)
        UniversalDetector detector = new UniversalDetector(null);

        // (2)
        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }

        // (3)
        detector.dataEnd();

        // (4)
        String encoding = detector.getDetectedCharset();

        if (encoding == null) {
            encoding = EMPTY_STRING;
        }

        // (5)
        detector.reset();

        return encoding;

    }
}
