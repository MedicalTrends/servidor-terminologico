package cl.minsal.semantikos.model.basictypes;

import java.io.Serializable;

/**
 * Created by andres on 7/21/16.
 */
public class OpenInterval<T extends Comparable> extends Interval<T> implements Serializable {

    public OpenInterval(T bottomBoundary, T upperBoundary) {
        super(bottomBoundary, upperBoundary);
    }

    @Override
    public boolean contains(T anElement) {
        return true;
    }
}
