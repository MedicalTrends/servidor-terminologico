package cl.minsal.semantikos.model.basictypes;

import java.io.Serializable;

/**
 * Esta clase representa un intervalo. El intervalo se define como un límite inferior y uno superior.
 */
public class CloseInterval<T extends Comparable> extends Interval<T> implements Serializable {

    public CloseInterval() {
        super();
    }

    public CloseInterval(T bottomBoundary, T upperBoundary) {
        super(bottomBoundary, upperBoundary);
    }

    @Override
    public boolean contains(T anElement) {
        return ((anElement.compareTo(this.upperBoundary) <= 0) &&
                (anElement.compareTo(this.lowerBoundary) >= 0));
    }
}
