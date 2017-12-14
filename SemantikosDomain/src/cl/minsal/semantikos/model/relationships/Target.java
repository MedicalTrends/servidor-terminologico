package cl.minsal.semantikos.model.relationships;

/**
 * @author Andrés Farías
 */
public interface Target {

    /**
     * Este método es responsable de retornar el ID del target.
     *
     * @return El identificador único en la base de datos del target.
     */
    public long getId();

    /**
     * Este método es responsable de retornar el tipo de Destino que representa este objeto.
     *
     * @return El <code>TargetType</code> del objeto que recibe el mensaje.
     */
    public TargetType getTargetType();

    public String getRepresentation();
    
    public Target copy();
    
    
}
