package cl.minsal.semantikos.model;

/**
 * @author Andrés Farías on 8/29/16.
 */
public interface IPersistentEntity {

    void setId(long id);

    long getId();

    /**
     * Este método determina si la entidad está persistida o no ( si tiene un ID positivo y distinto al por defecto
     * (<code>NON_PERSISTED_ID</code>).
     *
     * @return <code>true</code> si está persistido y <code>false</code> sino.
     */
    boolean isPersistent();

    String toString();
}
