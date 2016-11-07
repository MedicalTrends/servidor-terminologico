package cl.minsal.semantikos.model;

/**
 * @author Andrés Farías on 9/2/16.
 */
public class ProfileFactory {

    public static final Profile ADMINISTRATOR_PROFILE = new Profile(1, "Administrador", "Usuario administrador con acceso a toto todo todo");
    public static final Profile DESIGNER_PROFILE = new Profile(2, "Diseñador", "Usuario Diseñador");
    public static final Profile MODELER_PROFILE = new Profile(3, "Modelador", "Usuario Modelador");
    public static final Profile ADMINISTRATOR_REFSETS_PROFILE = new Profile(4, "Administrador de RefSets", "Usuario administrador de RefSets.");

}
