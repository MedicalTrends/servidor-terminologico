package cl.minsal.semantikos.model.externtables;

import cl.minsal.semantikos.model.PersistentEntity;

import java.io.Serializable;

/**
 * Created by des01c7 on 16-03-18.
 */
public class ExternTableRelationship extends PersistentEntity implements Serializable {

    ExternTableRow origin;
    ExternTableRow destiny;
    ExternTableReference definition;

    public ExternTableRelationship(long id, ExternTableRow origin, ExternTableRow destiny, ExternTableReference definition) {
        super(id);
        this.origin = origin;
        this.destiny = destiny;
        this.definition = definition;
    }

    public ExternTableRow getOrigin() {
        return origin;
    }

    public void setOrigin(ExternTableRow origin) {
        this.origin = origin;
    }

    public ExternTableRow getDestiny() {
        return destiny;
    }

    public void setDestiny(ExternTableRow destiny) {
        this.destiny = destiny;
    }

    public ExternTableReference getDefinition() {
        return definition;
    }

    public void setDefinition(ExternTableReference definition) {
        this.definition = definition;
    }
}
