package cl.minsal.semantikos.helpertables;

/**
 * Created by des01c7 on 23-11-18.
 */
public enum BooleanOption {

    SI(true),
    NO(false);

    /** El identificador único del tipo de Entidad */
    private boolean value;

    BooleanOption(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
