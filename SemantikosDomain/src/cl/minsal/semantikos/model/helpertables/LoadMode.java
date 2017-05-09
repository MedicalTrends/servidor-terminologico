package cl.minsal.semantikos.model.helpertables;

/**
 * @author Andrés Farías on 11/14/16.
 */
public enum LoadMode {

    INITIAL_LOAD("Carga Inicial!"),
    DELTA("Carga de un delta");


    private String name;

    LoadMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
