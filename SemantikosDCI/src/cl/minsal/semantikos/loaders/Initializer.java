package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.model.LoadException;
import cl.minsal.semantikos.model.LoadLog;
import cl.minsal.semantikos.model.SMTKLoader;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
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
public class Initializer extends EntityLoader {

    private static final String UTF_8 = "UTF-8";

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
                throw new LoadException(smtkLoader.SUBSTANCE_PATH, "", "El encabezado del archivo no es válido", ERROR);
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

    public void checkISPDataFiles(SMTKLoader smtkLoader) throws LoadException, IOException {

        try {

            smtkLoader.logInfo(new LoadLog("Comprobando DataFiles ISP", INFO));

            //this.path = Paths.get(smtkLoader.SUBSTANCE_PATH);
            //reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(smtkLoader.ISP_PATH)));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(smtkLoader.ISP_PATH), "UTF-8"));
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());

            int lines = 1;
            /**
             * Recuperar el header del archivo
             */
            String header = reader.readLine();

            if(!Arrays.asList(new String[]{"txt","csv"}).contains(getFileExtension(smtkLoader.ISP_PATH))) {
                throw new LoadException(smtkLoader.ISP_PATH, "", "Extension de archivo no valida. Extensiones soportadas: '.csv' y '.txt'", ERROR);
            }

            String encoding = detectEncoding(smtkLoader.ISP_PATH);

            if(encoding.equals(EMPTY_STRING)) {
                throw new LoadException(smtkLoader.ISP_PATH, "", "Ninguna codificacion detectada. La codificacion del archivo debe ser 'UTF-8'", ERROR);
            }

            if(!encoding.equals(UTF_8)) {
                throw new LoadException(smtkLoader.ISP_PATH, "", "Codificacion detectada: " + encoding +". La codificacion del archivo debe ser 'UTF-8'", ERROR);
            }

            List<String> cleanHeader = new ArrayList<>();

            for (String s : header.split(";")) {
                cleanHeader.add(s.replaceAll("\\p{C}", ""));
            }

            List<String> fields = (List<String>) (Object) Arrays.asList(PCConceptLoader.pcConceptFields.keySet().toArray());

            // ASSERT HEADER
            for (String field : cleanHeader) {
                if(!fields.contains(field)) {
                    String msg = "El encabezado no contiene el campo '" + field + "'. Lista ordenada de campos: " + fields.toString();
                    LoadException ex = new LoadException(smtkLoader.ISP_PATH, "", msg, ERROR);
                    throw ex;
                }
            }

            while (reader.readLine() != null) lines++;
            reader.close();

            smtkLoader.setTotal(lines-1);

            smtkLoader.logTick();

        } catch (IOException e) {
            throw e;
        } catch (LoadException e) {
            smtkLoader.logError(e);
            throw e;
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

        if (encoding != null) {
            System.out.println("Detected encoding = " + encoding);
        } else {
            System.out.println("No encoding detected.");
            encoding = EMPTY_STRING;
        }

        // (5)
        detector.reset();

        return encoding;

    }

}
