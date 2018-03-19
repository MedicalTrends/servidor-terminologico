package cl.minsal.semantikos.model.externtables;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.Multiplicity;
import cl.minsal.semantikos.model.relationships.MultiplicityFactory;

import java.io.Serializable;

/**
 * Created by des01c7 on 16-03-18.
 */
public class ExternTableReference extends PersistentEntity implements Serializable {

    ExternTable origin;
    ExternTable destiny;
    String name;
    Cardinality cardinality;

    public ExternTableReference(ExternTable origin, ExternTable destiny, String name, Cardinality cardinality) {
        this.origin = origin;
        this.destiny = destiny;
        this.name = name;
        this.cardinality = cardinality;
    }

    public ExternTableReference(long id, ExternTable origin, ExternTable destiny, String name, Cardinality cardinality) {
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

    public Cardinality getCardinality() {
        return cardinality;
    }

    public void setCardinality(Cardinality cardinality) {
        this.cardinality = cardinality;
    }
}
