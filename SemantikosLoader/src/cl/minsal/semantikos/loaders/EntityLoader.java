package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.model.LoadException;
import cl.minsal.semantikos.model.SMTKLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.logging.Logger;

import static cl.minsal.semantikos.model.LoadLog.ERROR;

/**
 * Created by root on 08-06-17.
 */
public class EntityLoader {

    private static final Logger logger = java.util.logging.Logger.getLogger(EntityLoader.class.getName() );

    Path path;

    BufferedReader reader;

    String separator = ";";

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
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());
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

}
