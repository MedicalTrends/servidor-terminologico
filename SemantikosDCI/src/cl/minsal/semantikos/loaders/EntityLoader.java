package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.model.LoadException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import static cl.minsal.semantikos.model.LoadLog.ERROR;

/**
 * Created by root on 08-06-17.
 */
public class EntityLoader {

    private static final Logger logger = Logger.getLogger(EntityLoader.class.getName() );

    Path path;

    BufferedReader reader;

    FileWriter fw;

    BufferedWriter writer;

    public static String separator = ";";

    private static String newline = System.getProperty("line.separator");

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void initReader(String path) throws LoadException {

        this.path = Paths.get(path);
        try {
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));

            /**
             * Descartar header
             */
            String line = reader.readLine();

            if(line == null) {
                throw new LoadException(path, null, "Archivo sin cabecera!!", ERROR);
            }

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

    public boolean assertHeader(List<String> fields, List<String> header) {
        for (String field : fields) {
            if(!header.contains(field)) {
                return false;
            }

        }
        return true;
    }

    public void initWriter(String path) throws LoadException {

        try {
            fw = new FileWriter(path);

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            writer.write("ID_CONCEPT;TIPO;MENSAJE");
            writer.write(newline);
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void haltWriter() {
        try {
            writer.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(LoadException ex) {
        try {
            if(ex.getMessage() != null) {
                writer.write(ex.getIdConcept() + separator + ex.getType() + separator + ex.getDescription());
            }
            else {
                writer.write(ex.getIdConcept() + separator + ex.getType() + separator + ex.getDescription());
            }

            writer.write(newline);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
