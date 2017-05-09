package cl.minsal.semantikos.model.basictypes;

/**
 * Created by andres on 7/21/16.
 */
public class EmptyInterval<T extends Comparable> extends Interval<T> {

    @Override
    public boolean contains(T anElement) {
        return false;
    }
}
