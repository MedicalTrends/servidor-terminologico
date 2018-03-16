package cl.minsal.semantikos.model.externtables;

import cl.minsal.semantikos.model.PersistentEntity;

import java.io.Serializable;

/**
 * Created by des01c7 on 16-03-18.
 */
public class ExternTableReference extends PersistentEntity implements Serializable {

    ExternTable origin;
    ExternTable destiny;
    String name;
    String cardinality;

    public ExternTableReference(ExternTable origin, ExternTable destiny, String name, String cardinality) {
        this.origin = origin;
        this.destiny = destiny;
        this.name = name;
        this.cardinality = cardinality;
    }

    public ExternTableReference(long id, ExternTable origin, ExternTable destiny, String name, String cardinality) {
        super(id);
        this.origin = origin;
        this.destiny = destiny;
        this.name = name;
        this.cardinality = cardinality;
    }

    public ExternTable getOrigin() {
        return origin;
    }

    public void setOrigin(ExternTable origin) {
        this.origin = origin;
    }

    public ExternTable getDestiny() {
        return destiny;
    }

    public void setDestiny(ExternTable destiny) {
        this.destiny = destiny;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardinality() {
        return cardinality;
    }

    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }
}
