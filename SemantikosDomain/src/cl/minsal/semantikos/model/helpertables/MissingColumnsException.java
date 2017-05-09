package cl.minsal.semantikos.model.helpertables;

import java.io.IOException;
import java.util.List;


/**
 * @author Andrés Farías on 11/15/16.
 */
public class MissingColumnsException extends IOException {

    private List<String> missingColumns;

    public MissingColumnsException(List<String> missingColumns) {
        this.missingColumns = missingColumns;
    }

    @Override
    public String toString() {
        return "MissingColumnsException{" +
                "missingColumns=" + missingColumns +
                '}';
    }
}
