package cl.minsal.semantikos.model.basictypes;

/**
 * Created by andres on 7/21/16.
 */
public class OpenInterval<T extends Comparable> extends Interval<T> {

    public OpenInterval(T bottomBoundary, T upperBoundary) {
        super(bottomBoundary, upperBoundary);
    }

    @Override
    public boolean contains(T anElement) {
        return true;
    }
}
