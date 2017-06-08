package cl.minsal.semantikos.loaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by root on 08-06-17.
 */
public class EntityLoader {

    Path path;

    BufferedReader reader;

    String separator;

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(EntityLoader.class);

    public EntityLoader(Path path, BufferedReader reader) {
        this.path = path;
        this.reader = reader;
    }

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

    public void initReader(String path) {

        this.path = Paths.get(path);
        try {
            reader = Files.newBufferedReader(this.path, Charset.defaultCharset());
            /**
             * skip header line (supposed to include header)
             */
            reader.readLine();

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
}
