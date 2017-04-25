package cl.minsal.semantikos.model.basictypes;

import java.io.Serializable;

/**
 * Created by andres on 7/21/16.
 */
public class EmptyInterval<T extends Comparable> extends Interval<T> implements Serializable {

    @Override
    public boolean contains(T anElement) {
        return false;
    }
}
